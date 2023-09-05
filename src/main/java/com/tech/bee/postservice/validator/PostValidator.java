package com.tech.bee.postservice.validator;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.enums.Enums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostValidator {

    public List<ErrorDTO> validate(PostDTO postDTO){
        List<ErrorDTO> notificationErrors = new ArrayList<>();
        validateTitle(postDTO , notificationErrors);
        validateSubTitle(postDTO , notificationErrors);
        validateContent(postDTO , notificationErrors);
        validateAuthor(postDTO , notificationErrors);
        validateTags(postDTO , notificationErrors);
        return notificationErrors;
    }

    private void validateTitle(PostDTO postDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getTitle()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateSubTitle(PostDTO postDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getSubtitle()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_SUB_TITLE)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateContent(PostDTO postDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getContent()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_CONTENT)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateAuthor(PostDTO postDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(postDTO.getAuthorId()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_AUTHOR)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateTags(PostDTO postDTO , List<ErrorDTO> notificationErrors){
        if(!CollectionUtils.isEmpty(postDTO.getTags())){

        }
    }
}
