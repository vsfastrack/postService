package com.tech.bee.postservice.mapper;


import com.tech.bee.postservice.entity.ReactionEntity;
import com.tech.bee.postservice.enums.Enums;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReactionMapper {

    @Mapping(source = "userId" , target = "userId")
    @Mapping(source = "reaction" , target = "reaction")
    @Mapping(source = "postId" , target = "postId")
    ReactionEntity toEntity(Enums.ReactionEnum reaction, String postId , String userId);
}
