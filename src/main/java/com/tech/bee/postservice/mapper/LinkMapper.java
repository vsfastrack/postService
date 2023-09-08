package com.tech.bee.postservice.mapper;

import com.tech.bee.postservice.entity.LinkEntity;
import com.tech.bee.postservice.util.AppUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LinkMapper {

    @Mapping(source = "link" , target = "content")
    @Mapping(expression = "java(toId())" , target="linkId")
    public LinkEntity toEntity(String  link);

    default String toId(){
        return AppUtil.generateIdentifier("LI");
    }
}
