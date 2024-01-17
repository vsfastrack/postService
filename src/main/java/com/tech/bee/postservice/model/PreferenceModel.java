package com.tech.bee.postservice.model;

import com.tech.bee.postservice.entity.PostEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreferenceModel {
    private String name;
    private String title;
    private String postId;
    private String subtitle;
    private String subTitle;
    private Long likes;
    private Long readMinutes;
    private Long viewedBy;
}
