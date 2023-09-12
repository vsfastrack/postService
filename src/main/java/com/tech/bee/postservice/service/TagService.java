package com.tech.bee.postservice.service;

import com.tech.bee.postservice.util.AppUtil;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.entity.TagEntity;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.exception.BaseCustomException;
import com.tech.bee.postservice.mapper.TagMapper;
import com.tech.bee.postservice.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagDTO findTagByName(final String tagName){
        TagEntity tagEntity = tagRepository.findByNameIgnoreCase(tagName)
                .orElseThrow( () -> BaseCustomException.builder().
                 errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_TAG)))
                         .build());
        return tagMapper.toDto(tagEntity);
    }
}
