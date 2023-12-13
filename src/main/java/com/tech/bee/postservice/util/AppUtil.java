package com.tech.bee.postservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.exception.BaseCustomException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@UtilityClass
public class AppUtil {

    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String generateIdentifier(String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        String currentTimeStamp = LocalDateTime.now().toString().replace("-", "");
        String timeStamp = currentTimeStamp.replaceAll(":", "");
        String identifier = timeStamp.replace(".", "");
        String finalIdentifier = identifier.replace("T", "");
        return stringBuilder.append(prefix).append(finalIdentifier).toString();
    }
    private ErrorDTO buildError(Enums.ErrorCategory category, String key, String code, String message) {
        return ErrorDTO.builder().category(category).key(key)
                .code(code).message(message).build();
    }
    public ErrorDTO buildResourceNotFoundError(String key) {
        return buildError(
                Enums.ErrorCategory.BUSINESS_VALIDATION_ERROR,
                key,
                ApiConstants.ErrorCodeConstants.CODE_RESOURCE_NOT_FOUND,
                ApiConstants.ErrorMsgConstants.MESSAGE_RESOURCE_NOT_FOUND
        );
    }
    public ErrorDTO buildValidationErrorError(String key) {
        return buildError(
                Enums.ErrorCategory.VALIDATION_ERROR,
                key,
                ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY,
                ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY
        );
    }
    public ErrorDTO buildResourceConflictError(String key) {
        return buildError(
                Enums.ErrorCategory.BUSINESS_VALIDATION_ERROR,
                key,
                ApiConstants.ErrorCodeConstants.CODE_RESOURCE_CONFLICT,
                ApiConstants.ErrorMsgConstants.MESSAGE_RESOURCE_CONFLICT
        );
    }
    public void mergeObjectsWithProperties(Object source, Object destination, List<String> propertyNames) {
        BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
        propertyNames.forEach(propertyName -> {
            try {
                Object sourceValue = beanUtils.getPropertyUtils().getProperty(source, propertyName);
                Object destinationValue = beanUtils.getPropertyUtils().getProperty(destination, propertyName);
                if (sourceValue!=null && !Objects.equals(sourceValue, destinationValue)) {
                    beanUtils.copyProperty(destination, propertyName, sourceValue);
                }
            } catch (Exception exception) {
                log.error("Error occurred while merging beans {}", ExceptionUtils.getMessage(exception));
            }
        });
    }

    public static String getAsJsonString(Object object){
        try{
            return objectMapper.writeValueAsString(object);
        }catch(Exception exception){
            log.error("Exception occurred while parsing input {}",ExceptionUtils.getMessage(exception));
        }
        return null;
    }
    public static String publishedOn(PostEntity postEntity){
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d' 'MMM' 'yyyy");
        return postEntity.getCreatedWhen().format(outputFormatter);
    }
}
