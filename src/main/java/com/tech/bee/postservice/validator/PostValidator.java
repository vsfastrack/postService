package com.tech.bee.postservice.validator;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.entity.TagEntity;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.repository.TagRepository;
import com.tech.bee.postservice.service.SecurityService;
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
    private final SecurityService securityService;

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
//        validateTitleLength(postDTO, notificationErrors);
//        validateSubTitleLength(postDTO, notificationErrors);
//        validateContentLength(postDTO, notificationErrors);
//        validateTags(postDTO, notificationErrors);
//        validateSeriesLength(postDTO , notificationErrors);
        return notificationErrors;
    }




    private void validateTitle(PostDTO postDTO, List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getTitle()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        //validateTitleLength(postDTO ,notificationErrors);
    }

    private void validateTitleLength(PostDTO postDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isNotEmpty(postDTO.getTitle()) && StringUtils.length(postDTO.getTitle()) > 30)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "30"))
                    .key(ApiConstants.KeyConstants.KEY_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateSubTitleLength(PostDTO postDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isNotEmpty(postDTO.getSubtitle()) && StringUtils.length(postDTO.getSubtitle()) > 40)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "40"))
                    .key(ApiConstants.KeyConstants.KEY_SUB_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateSubTitle(PostDTO postDTO, List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getSubtitle()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_SUB_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        //validateSubTitleLength(postDTO , notificationErrors);
    }

    private void validateContentLength(PostDTO postDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isNotEmpty(postDTO.getContent()) && StringUtils.length(postDTO.getContent()) > 1000)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "1000"))
                    .key(ApiConstants.KeyConstants.KEY_CONTENT)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateContent(PostDTO postDTO, List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getContent()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_CONTENT)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        //validateContentLength(postDTO , notificationErrors);
    }

    private void validateSeriesLength(PostDTO postDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isNotEmpty(postDTO.getSeries()) && StringUtils.length(postDTO.getSeries()) > 1000)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "30"))
                    .key(ApiConstants.KeyConstants.KEY_SERIES)
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
        //validateSeriesLength(postDTO , notificationErrors);
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
            List<String> existingTagNames = existingTags.stream().map(TagEntity::getName).collect(Collectors.toList());
            List<String> newTagNames = tagNames.stream().filter(tagName -> !existingTagNames.contains(tagName)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(newTagNames)){
                newTagNames.forEach(newTagName -> {
                    if(StringUtils.length(newTagName) > 30)
                        notificationErrors.add(ErrorDTO.builder()
                                .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                                .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "30"))
                                .key(ApiConstants.KeyConstants.KEY_TAG_NAME +":"+newTagName)
                                .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
                });
            }
        }
    }
}
