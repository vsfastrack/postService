package com.tech.bee.postservice.service;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.entity.LinkEntity;
import com.tech.bee.postservice.entity.TagEntity;
import com.tech.bee.postservice.mapper.PostMapper;
import com.tech.bee.postservice.mapper.LinkMapper;
import com.tech.bee.postservice.mapper.TagMapper;
import com.tech.bee.postservice.repository.TagRepository;
import com.tech.bee.postservice.validator.PostValidator;
import com.tech.bee.postservice.exception.BaseCustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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
        return postEntity.getPostId();
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
