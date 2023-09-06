package com.tech.bee.postservice.service;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.entity.ReferenceEntity;
import com.tech.bee.postservice.entity.TagEntity;
import com.tech.bee.postservice.exception.BaseCustomException;
import com.tech.bee.postservice.mapper.PostMapper;
import com.tech.bee.postservice.mapper.ReferenceMapper;
import com.tech.bee.postservice.mapper.TagMapper;
import com.tech.bee.postservice.repository.PostRepository;
import com.tech.bee.postservice.repository.TagRepository;
import com.tech.bee.postservice.validator.PostValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    private final PostRepository postRepository;
    private final TagMapper tagMapper;
    private final ReferenceMapper referenceMapper;

    public String createPost(PostDTO postDTO){
        List<ErrorDTO> validationErrors = postValidator.validate(postDTO);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.BAD_REQUEST).build();
        PostEntity postEntity = postMapper.toEntity(postDTO);
        addTags(postEntity , postDTO);
        addReferences(postEntity , postDTO);
        postRepository.save(postEntity);
        return postEntity.getPostId();
    }

    private void addTags(PostEntity postEntity , PostDTO postDTO){
        Set<TagEntity> tags = fetchTags(postDTO);
        if(CollectionUtils.isNotEmpty(tags))
            postEntity.getTags().addAll(tags);
    }

    private void addReferences(PostEntity postEntity , PostDTO postDTO){
        List<ReferenceEntity> references = createReferences(postDTO);
        if(CollectionUtils.isNotEmpty(references))
            postEntity.getReferences().addAll(references);
    }

    Predicate<TagDTO> isEligibleForTagCreation =  (tagDTO -> {
        return StringUtils.isNotEmpty(tagDTO.getName()) && StringUtils.isEmpty(tagDTO.getTagId());
    });

    private Set<TagEntity> fetchTags(PostDTO postDTO){
        if(CollectionUtils.isNotEmpty(postDTO.getTags())){
            List<String> tagIdList = postDTO.getTags().stream().map(TagDTO::getTagId).collect(Collectors.toList());
            List<String> newTagNames = postDTO.getTags().stream().filter(isEligibleForTagCreation)
                                    .map(TagDTO::getName).collect(Collectors.toList());
            List<TagEntity> newTags = createNewTags(newTagNames);
            Set<TagEntity> existingTags = tagRepository.findByTagIdIn(tagIdList);
            existingTags.addAll(newTags);
            return existingTags;
        }
        return null;
    }

    private  List<TagEntity> createNewTags(List<String> tagNames){
        return tagNames.stream().map(tagMapper::toEntity).collect(Collectors.toList());
    }

    private List<ReferenceEntity> createReferences(PostDTO postDTO){
        return postDTO.getReferences().stream().map(referenceMapper::toEntity).collect(Collectors.toList());
    }

}
