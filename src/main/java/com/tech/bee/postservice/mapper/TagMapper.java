package com.tech.bee.postservice.mapper;

import com.tech.bee.postservice.util.AppUtil;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.entity.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(source = "tagName" , target = "name")
    @Mapping(expression = "java(toId())" , target="tagId")
    public TagEntity toEntity(String  tagName);

    @Mapping(source = "tagId" , target = "tagId")
    @Mapping(source = "name" , target = "name")
    @Mapping(source = "identifier" , target = "identifier")
    @Mapping(source = "createdWhen" , target = "createdWhen")
    @Mapping(source = "lastModified" , target = "lastModified")
    public TagDTO toDto(TagEntity tagEntity);

    default String toId(){
        return AppUtil.generateIdentifier("TA");
    }
}
