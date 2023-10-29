package com.tech.bee.postservice.dto;

import com.tech.bee.postservice.enums.Enums;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReactionDTO {
    private String userId;
    private Enums.ReactionEnum reaction;
}
