package com.tech.bee.postservice.constants;

public class ApiConstants {
    public static final class PathConstants{
        public static final String PATH_POST_RESOURCE="/api/v1/post";
        public static final String PATH_TAG_RESOURCE="/api/v1/tag";
    }
    public static final class ErrorCodeConstants{
        public static final String CODE_FIELD_CANNOT_BE_EMPTY="400.001";
        public static final String CODE_RESOURCE_NOT_FOUND="404.001";
    }
    public static final class ErrorMsgConstants{
        public static final String MESSAGE_FIELD_CANNOT_BE_EMPTY="Field cannot be empty";
        public static final String MESSAGE_RESOURCE_NOT_FOUND="Resource not found";
    }
    public static final class KeyConstants{
        public static final String KEY_TITLE="title";
        public static final String KEY_SUB_TITLE="subtitle";
        public static final String KEY_CONTENT="content";
        public static final String KEY_AUTHOR="authorId";
    }
}
