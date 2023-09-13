package com.tech.bee.postservice.validator;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.exception.BaseCustomException;
import com.tech.bee.postservice.repository.TagRepository;
import com.tech.bee.postservice.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TagValidator {

    final TagRepository tagRepository;

    public List<ErrorDTO> validate(TagDTO tagDTO){
        List<ErrorDTO> notificationErrors = new ArrayList<>();
        validateTagName(tagDTO , notificationErrors);
        return notificationErrors;
    }

    private void validateTagName(TagDTO tagDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(tagDTO.getName()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_TAG_NAME)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        if(StringUtils.isNotEmpty(tagDTO.getName())){
            tagRepository.findByNameIgnoreCase(tagDTO.getName()).ifPresent(tag -> {
                throw BaseCustomException.builder().errors(Collections.singletonList(AppUtil.buildResourceConflictError(
                        ApiConstants.KeyConstants.KEY_TAG_NAME))).httpStatus(HttpStatus.CONFLICT)
                        .build();
            });
            if(StringUtils.length(tagDTO.getName()) > 30)
                notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "30"))
                    .key(ApiConstants.KeyConstants.KEY_TAG_NAME)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        }
    }
}
