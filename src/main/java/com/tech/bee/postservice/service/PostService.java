package com.tech.bee.postservice.service;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.common.PageResponseDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.PostSearchDTO;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.entity.LinkEntity;
import com.tech.bee.postservice.entity.TagEntity;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.mapper.PostMapper;
import com.tech.bee.postservice.mapper.LinkMapper;
import com.tech.bee.postservice.mapper.TagMapper;
import com.tech.bee.postservice.repository.CustomRepository;
import com.tech.bee.postservice.repository.PostRepository;
import com.tech.bee.postservice.repository.TagRepository;
import com.tech.bee.postservice.util.AppUtil;
import com.tech.bee.postservice.validator.PostValidator;
import com.tech.bee.postservice.exception.BaseCustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostValidator postValidator;
    private final PostMapper postMapper;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final LinkMapper linkMapper;
    private final EntityManager entityManager;
    private final CustomRepository customRepository;
    private final PostRepository postRepository;

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
        entityManager.persist(postEntity);
        return postEntity.getIdentifier();
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
        PostEntity post = postRepository.findByIdentifier(postIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_POST))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<TagDTO> tags = post.getTags().stream().map(tagMapper::toDto).collect(Collectors.toList());
        List<String> links = post.getLinks().stream().map(LinkEntity::getContent).collect(Collectors.toList());
        return postMapper.toDto(post , tags , links);
    }

    @Transactional
    public void update(PostDTO postDTO , final String postIdentifier){
        PostEntity existingPost =  postRepository.findByIdentifier(postIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_POST))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<ErrorDTO> validationErrors = postValidator.validatePatchRequest(postDTO);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.BAD_REQUEST).build();
        updatePost(existingPost,postDTO);
    }

    public void delete(final String postIdentifier){
        PostEntity existingPost =  postRepository.findByIdentifier(postIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_POST))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        existingPost.setDeleted(true);
    }

    private void updatePost(PostEntity existingPost , PostDTO patchDTO){
        if(StringUtils.isNotEmpty(patchDTO.getTitle()))
            existingPost.setTitle(patchDTO.getTitle());
        if(StringUtils.isNotEmpty(patchDTO.getSubtitle()))
            existingPost.setSubtitle(patchDTO.getSubtitle());
        if(StringUtils.isNotEmpty(patchDTO.getCategory()))
            existingPost.setCategory(patchDTO.getCategory());
        if(StringUtils.isNotEmpty(patchDTO.getSeries()))
            existingPost.setSeries(patchDTO.getSeries());
        if(StringUtils.isNotEmpty(patchDTO.getContent()))
            existingPost.setContent(patchDTO.getContent());
        if(CollectionUtils.isNotEmpty(patchDTO.getTags())){
            List<TagEntity> tags = patchDTO.getTags().stream().map(TagDTO::getName).map(tagMapper::toEntity).collect(Collectors.toList());
            tags.forEach(tagEntity -> {
                existingPost.getTags().add(tagEntity);
            });
        }
        if(CollectionUtils.isNotEmpty(patchDTO.getLinks())){
            List<LinkEntity> links = patchDTO.getLinks().stream().map(linkMapper::toEntity).collect(Collectors.toList());
            links.forEach(link -> {
                existingPost.getLinks().add(link);
            });
        }
    }

    Predicate<TagDTO> isEligibleForTagCreation =  (tagDTO -> {
        return StringUtils.isNotEmpty(tagDTO.getName()) && StringUtils.isEmpty(tagDTO.getTagId());
    });

    private Set<TagEntity> fetchTags(PostDTO postDTO){
        if(CollectionUtils.isNotEmpty(postDTO.getTags())){
            List<String> tagIdList = postDTO.getTags().stream().map(TagDTO::getTagId)
                    .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            List<String> newTagNames = postDTO.getTags().stream().filter(isEligibleForTagCreation)
                                    .map(TagDTO::getName).collect(Collectors.toList());
            Set<TagEntity> tags = createNewTags(newTagNames);
            if(CollectionUtils.isNotEmpty(tagIdList))
                tags.addAll(tagRepository.findByTagIdIn(tagIdList));
            return tags;
        }
        return null;
    }
    private  Set<TagEntity> createNewTags(List<String> tagNames){
        return tagNames.stream().map(tagMapper::toEntity).collect(Collectors.toSet());
    }
    private Set<LinkEntity> createLinks(PostDTO postDTO){
        return postDTO.getLinks().stream().map(linkMapper::toEntity).collect(Collectors.toSet());
    }

}
