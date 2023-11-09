package com.tech.bee.postservice.service;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.common.PageResponseDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.*;
import com.tech.bee.postservice.entity.LinkEntity;
import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.entity.ReactionEntity;
import com.tech.bee.postservice.entity.TagEntity;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.exception.BaseCustomException;
import com.tech.bee.postservice.mapper.LinkMapper;
import com.tech.bee.postservice.mapper.PostMapper;
import com.tech.bee.postservice.mapper.ReactionMapper;
import com.tech.bee.postservice.mapper.TagMapper;
import com.tech.bee.postservice.repository.CustomRepository;
import com.tech.bee.postservice.repository.PostRepository;
import com.tech.bee.postservice.repository.ReactionRepository;
import com.tech.bee.postservice.repository.TagRepository;
import com.tech.bee.postservice.util.AppUtil;
import com.tech.bee.postservice.validator.PostValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostValidator postValidator;
    private final PostMapper postMapper;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final LinkMapper linkMapper;
    private final CustomRepository customRepository;
    private final PostRepository postRepository;
    private final SecurityService securityService;
    private final ReactionMapper reactionMapper;
    private final ReactionRepository reactionRepository;

    @Transactional
    public String createPost(PostDTO postDTO){
        List<ErrorDTO> validationErrors = postValidator.validate(postDTO);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.BAD_REQUEST).build();
        PostEntity postEntity = postMapper.toEntity(postDTO);
        Set<TagEntity> tags = fetchTags(postDTO);
        if(CollectionUtils.isNotEmpty(tags)){
            tags.forEach(tagEntity -> {
                postEntity.getTags().add(tagEntity);
            });
        }
        Set<LinkEntity> links = createLinks(postDTO);
        if(CollectionUtils.isNotEmpty(links)){
            links.forEach(linkEntity -> {
                postEntity.getLinks().add(linkEntity);
            });
        }
        postEntity.setIdentifier(UUID.randomUUID().toString());
        postEntity.setCreatedBy(securityService.getCurrentLoggedInUser());
        postRepository.save(postEntity);
        return postEntity.getPostId();
    }

    public PageResponseDTO findPosts(PostSearchDTO postSearchDTO , final int pageIndex ,
                                     final int pageSize , final Enums.SortDirection sortDir ,
                                     final String sortKey){
        if(Enums.SortDirection.DESC == sortDir){
            Sort sort = Sort.by(sortKey).descending();
            Pageable pageable = PageRequest.of(pageIndex, pageSize ,sort);
            Page<PostEntity> postPage = customRepository.findPosts(postSearchDTO , pageable);
            List<PostDTO> posts = postPage.get().map(postMapper::toDto).collect(Collectors.toList());
            return PageResponseDTO.builder().totalResults(postPage.getTotalElements()).results(posts).build();
        }else{
            Sort sort = Sort.by(sortKey).ascending();
            Pageable pageable = PageRequest.of(pageIndex, pageSize ,sort);
            Page<PostEntity> postPage = customRepository.findPosts(postSearchDTO , pageable);
            List<PostDTO> posts = postPage.get().map(postMapper::toDto).collect(Collectors.toList());
            return PageResponseDTO.builder().totalResults(postPage.getTotalElements()).results(posts).build();
        }
    }

    public PostDTO getPostDetails(final String postIdentifier){
        PostEntity post = postRepository.findByPostId(postIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_POST))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<TagDTO> tags = post.getTags().stream().map(tagMapper::toDto).collect(Collectors.toList());
        List<String> links = post.getLinks().stream().map(LinkEntity::getContent).collect(Collectors.toList());
        PostDTO postDTO = postMapper.toDto(post , tags , links);
        ReactionEntity reactionByExistingUser = reactionRepository.findByPostIdAndUserId(postIdentifier , securityService.getCurrentLoggedInUser());
        postDTO.setLiked(Objects.nonNull(reactionByExistingUser) &&
                reactionByExistingUser.getReaction() ==  Enums.ReactionEnum.LIKE);
        postDTO.setAuthor(post.getCreatedBy().equals(securityService.getCurrentLoggedInUser()));
        return postDTO;
    }

    @Transactional
    public void update(PostDTO postDTO , final String postIdentifier){
        PostEntity existingPost =  postRepository.findByPostId(postIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_POST))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<ErrorDTO> validationErrors = postValidator.validatePatchRequest(postDTO);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.BAD_REQUEST).build();
        List<ErrorDTO> ownershipErrors = securityService.validateOwnership(existingPost.getCreatedBy());
        if(CollectionUtils.isNotEmpty(ownershipErrors))
            throw BaseCustomException.builder().errors(ownershipErrors).httpStatus(HttpStatus.FORBIDDEN).build();
        updatePost(existingPost,postDTO);
    }

    public void delete(final String postIdentifier){
        PostEntity existingPost =  postRepository.findByPostId(postIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_POST))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<ErrorDTO> ownershipErrors = securityService.validateOwnership(existingPost.getCreatedBy());
        if(CollectionUtils.isNotEmpty(ownershipErrors))
            throw BaseCustomException.builder().errors(ownershipErrors).httpStatus(HttpStatus.FORBIDDEN).build();
        existingPost.getTags().clear();
        postRepository.save(existingPost);
        postRepository.delete(existingPost);
    }

    private void updatePost(PostEntity existingPost , PostDTO patchDTO){
        AppUtil.mergeObjectsWithProperties(patchDTO , existingPost ,
                Arrays.asList("title" , "subtitle" ,"category","series",
                        "content" , "description","series","content",
                        "coverImage"));
        if(Objects.nonNull(patchDTO.getTags())){
            Set<TagEntity> tags = fetchTags(patchDTO);
            if(CollectionUtils.isNotEmpty(tags)){
                existingPost.setTags(tags);
            }
        }
    }

    private Set<TagEntity> fetchTags(PostDTO postDTO){
        if(CollectionUtils.isNotEmpty(postDTO.getTags())){
            Set<TagEntity> resultTags = new HashSet<>();
            List<String> tagIdList = postDTO.getTags().stream().map(TagDTO::getTagId)
                    .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            List<String> tagNameList = postDTO.getTags().stream().map(TagDTO::getName).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(tagIdList)){
                Set<TagEntity> existingTags = tagRepository.findAllByTagIdIn(tagIdList);
                resultTags.addAll(existingTags);
            }
            if(CollectionUtils.isNotEmpty(tagNameList)){
                List<TagEntity> existingTags = tagRepository.findAllByNameIn(tagNameList);
                resultTags.addAll(existingTags);
                List<String> existingTagNames = existingTags.stream().map(TagEntity::getName).collect(Collectors.toList());
                List<String> newTags = tagNameList.stream().filter(newTag -> !existingTagNames.contains(newTag)).collect(Collectors.toList());
                resultTags.addAll(createNewTags(newTags));
            }
            return resultTags;
        }
        return null;
    }
    private  Set<TagEntity> createNewTags(List<String> tagNames){
        return tagNames.stream().map(tagMapper::toEntity).collect(Collectors.toSet());
    }
    private Set<LinkEntity> createLinks(PostDTO postDTO){
        return (CollectionUtils.isNotEmpty(postDTO.getLinks())) ?
                postDTO.getLinks().stream().map(linkMapper::toEntity).collect(Collectors.toSet()) :
                null;
    }

    public List<PostSummaryDTO> findSummariesForTopRatedPosts(Integer count){
        List<PostEntity> postEntities = postRepository.findTopRatedPosts(count);
        return postEntities.stream().map(this::convertToPostSummary).collect(Collectors.toList());
    }

    private PostSummaryDTO convertToPostSummary(PostEntity postEntity){
        List<String> tagNames = postEntity.getTags().stream().map(TagEntity::getName).collect(Collectors.toList());
        return postMapper.toPostSummary(postEntity , tagNames);
    }

    public PageResponseDTO findPostSummariesList(final int pageIndex ,
                                     final int pageSize , final Enums.SortDirection sortDir ,
                                     final String sortKey){
        if(Enums.SortDirection.DESC == sortDir){
            Sort sort = Sort.by(sortKey).descending();
            Pageable pageable = PageRequest.of(pageIndex, pageSize ,sort);
            Page<PostEntity> postPage = customRepository.findPosts(pageable);
            List<PostSummaryDTO> postSummaryDTOList = postPage.get().map(postEntity -> {
                return postMapper.toPostSummary(postEntity ,
                        postEntity.getTags().stream().map(TagEntity::getName).collect(Collectors.toList()));
            }).collect(Collectors.toList());
            return PageResponseDTO.builder().totalResults(postPage.getTotalElements()).
                    results(postSummaryDTOList).build();
        }else{
            Sort sort = Sort.by(sortKey).ascending();
            Pageable pageable = PageRequest.of(pageIndex, pageSize ,sort);
            Page<PostEntity> postPage = customRepository.findPosts(pageable);
            List<PostSummaryDTO> postSummaryDTOList = postPage.get().map(postEntity -> postMapper.toPostSummary(postEntity ,
                    postEntity.getTags().stream().map(TagEntity::getName).collect(Collectors.toList()))).collect(Collectors.toList());
            return PageResponseDTO.builder().totalResults(postPage.getTotalElements()).
                    results(postSummaryDTOList).build();
        }
    }

    public String react(String postIdentifier , Enums.ReactionEnum reaction){
        PostEntity existingPost =  postRepository.findByPostId(postIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_POST))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        ReactionEntity existingReactionByUser = reactionRepository.findByPostIdAndUserId(postIdentifier ,
                                                securityService.getCurrentLoggedInUser());
        if(Enums.ReactionEnum.DISLIKE == reaction){
            if(Objects.isNull(existingReactionByUser))
                throw BaseCustomException.builder().errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_REACTION)))
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            existingReactionByUser.setReaction(Enums.ReactionEnum.DISLIKE);
            dislikePost(existingPost , existingReactionByUser);
            return existingReactionByUser.getReactionId();
        }else if(Enums.ReactionEnum.LIKE == reaction){
            if(Objects.nonNull(existingReactionByUser)){
                if(Enums.ReactionEnum.DISLIKE == existingReactionByUser.getReaction())
                    existingReactionByUser.setReaction(Enums.ReactionEnum.LIKE);
                likePost(existingPost , existingReactionByUser);
                return existingReactionByUser.getReactionId();
            }else{
                ReactionEntity newReaction = reactionMapper.toEntity(reaction , postIdentifier ,
                        securityService.getCurrentLoggedInUser());
                likePost(existingPost , newReaction);
                return newReaction.getReactionId();
            }
        }
        throw BaseCustomException.builder().errors(Collections.singletonList(AppUtil.buildValidationErrorError(ApiConstants.KeyConstants.KEY_REACTION)))
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }
    @Transactional
    private void dislikePost(PostEntity postEntity , ReactionEntity reactionEntity){
        Long likesCount = postEntity.getLikes();
        postEntity.setLikes(likesCount - 1);
        reactionRepository.save(reactionEntity);
        postRepository.save(postEntity);
    }
    @Transactional
    private void likePost(PostEntity postEntity , ReactionEntity reactionEntity){
        Long likesCount = postEntity.getLikes() != null ? postEntity.getLikes() : 0;
        postEntity.setLikes(likesCount + 1);
        reactionRepository.save(reactionEntity);
        postRepository.save(postEntity);
    }

}
