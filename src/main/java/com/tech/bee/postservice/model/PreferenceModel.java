package com.tech.bee.postservice.model;

import com.tech.bee.postservice.entity.PostEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreferenceModel {
    private PostEntity post;
    private String name;
}
