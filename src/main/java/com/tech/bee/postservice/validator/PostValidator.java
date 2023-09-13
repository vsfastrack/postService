package com.tech.bee.postservice.validator;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.entity.TagEntity;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.repository.TagRepository;
import com.tech.bee.postservice.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostValidator {

    private final TagRepository tagRepository;

    public List<ErrorDTO> validate(PostDTO postDTO){
        List<ErrorDTO> notificationErrors = new ArrayList<>();
        validateTitle(postDTO, notificationErrors);
        validateSubTitle(postDTO, notificationErrors);
        validateContent(postDTO, notificationErrors);
        validateAuthor(postDTO, notificationErrors);
        validateTags(postDTO, notificationErrors);
        validateSeries(postDTO , notificationErrors);
        return notificationErrors;
    }

    public List<ErrorDTO> validatePatchRequest(PostDTO postDTO){
        List<ErrorDTO> notificationErrors = new ArrayList<>();
        validateTitle(postDTO, notificationErrors);
        validateSubTitle(postDTO, notificationErrors);
        validateContent(postDTO, notificationErrors);
        validateTags(postDTO, notificationErrors);
        validateSeries(postDTO , notificationErrors);
        return notificationErrors;
    }


    private void validateTitle(PostDTO postDTO, List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getTitle()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        if(StringUtils.isNotEmpty(postDTO.getTitle()) && StringUtils.length(postDTO.getTitle()) > 30)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "30"))
                    .key(ApiConstants.KeyConstants.KEY_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateSubTitle(PostDTO postDTO, List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getSubtitle()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_SUB_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        if(StringUtils.isNotEmpty(postDTO.getSubtitle()) && StringUtils.length(postDTO.getSubtitle()) > 40)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "40"))
                    .key(ApiConstants.KeyConstants.KEY_SUB_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateContent(PostDTO postDTO, List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getContent()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_CONTENT)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        if(StringUtils.isNotEmpty(postDTO.getContent()) && StringUtils.length(postDTO.getContent()) > 1000)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "1000"))
                    .key(ApiConstants.KeyConstants.KEY_CONTENT)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateAuthor(PostDTO postDTO, List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getAuthorId()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_AUTHOR)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateSeries(PostDTO postDTO, List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getSeries()))
            notificationErrors.add(AppUtil.buildValidationErrorError(ApiConstants.KeyConstants.KEY_SERIES));
        if(StringUtils.isNotEmpty(postDTO.getSeries()) && StringUtils.length(postDTO.getSeries()) > 1000)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "30"))
                    .key(ApiConstants.KeyConstants.KEY_SERIES)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateTags(PostDTO postDTO, List<ErrorDTO> notificationErrors){
        if(CollectionUtils.isNotEmpty(postDTO.getTags())){
            List<String> existingTagIdentifiers = postDTO.getTags().stream().map(TagDTO::getIdentifier)
                    .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            List<TagEntity> tagEntities = tagRepository.findAllByIdentifierIn(existingTagIdentifiers);
            if(tagEntities.size() != existingTagIdentifiers.size())
                notificationErrors.add(ErrorDTO.builder()
                        .code(ApiConstants.ErrorCodeConstants.CODE_RESOURCE_NOT_FOUND)
                        .message(ApiConstants.ErrorMsgConstants.MESSAGE_RESOURCE_NOT_FOUND)
                        .key(ApiConstants.KeyConstants.KEY_TAG)
                        .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
            List<String> tagNames = postDTO.getTags().stream().map(TagDTO::getName).collect(Collectors.toList());
            List<TagEntity> existingTags = tagRepository.findAllByNameIn(tagNames);
            if(CollectionUtils.isNotEmpty(existingTags)){
                notificationErrors.add(ErrorDTO.builder()
                        .code(ApiConstants.ErrorCodeConstants.CODE_RESOURCE_CONFLICT)
                        .message(ApiConstants.ErrorMsgConstants.MESSAGE_RESOURCE_CONFLICT)
                        .key(existingTags.stream().map(TagEntity::getName).collect(Collectors.joining(",")))
                        .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
            }
        }
    }
}
