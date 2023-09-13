package com.tech.bee.postservice.service;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.util.AppUtil;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.entity.TagEntity;
import com.tech.bee.postservice.exception.BaseCustomException;
import com.tech.bee.postservice.mapper.TagMapper;
import com.tech.bee.postservice.repository.TagRepository;
import com.tech.bee.postservice.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final TagValidator tagValidator;

    public TagDTO findTagByName(final String tagName){
        TagEntity tagEntity = tagRepository.findByNameIgnoreCase(tagName)
                .orElseThrow( () -> BaseCustomException.builder().
                 errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_TAG))).httpStatus(HttpStatus.NOT_FOUND)
                         .build());
        return tagMapper.toDto(tagEntity);
    }

    public String create(TagDTO tagDTO){
        List<ErrorDTO> validationErrors = tagValidator.validate(tagDTO);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.BAD_REQUEST).build();
        TagEntity tagEntity = tagMapper.toEntity(tagDTO.getName());
        tagRepository.save(tagEntity);
        return tagEntity.getIdentifier();
    }
}
