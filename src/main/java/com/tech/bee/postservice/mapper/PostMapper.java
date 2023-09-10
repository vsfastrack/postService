package com.tech.bee.postservice.mapper;

import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.util.AppUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(source = "title" , target = "title")
    @Mapping(source = "subtitle" , target = "subtitle")
    @Mapping(source = "content" , target = "content")
    @Mapping(source = "authorId" , target = "authorId")
    @Mapping(source = "series" , target = "series")
    @Mapping(ignore = true, target = "links")
    @Mapping(ignore = true, target = "tags")
    @Mapping(expression = "java(toId(postDTO))" , target="postId")
    public PostEntity toEntity(PostDTO postDTO);

    @Mapping(source = "title" , target = "title")
    @Mapping(source = "subtitle" , target = "subtitle")
    @Mapping(source = "content" , target = "content")
    @Mapping(source = "authorId" , target = "authorId")
    @Mapping(source = "series" , target = "series")
    @Mapping(ignore = true, target = "links")
    @Mapping(ignore = true, target = "tags")
    @Mapping(source = "postId" , target="postId")
    @Mapping(source = "identifier" , target="identifier")
    public PostDTO toDto(PostEntity postEntity);

    default String toId(PostDTO postDTO){
        return AppUtil.generateIdentifier("PO");
    }
}
