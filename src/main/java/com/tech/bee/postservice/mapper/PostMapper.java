package com.tech.bee.postservice.mapper;

import com.tech.bee.postservice.dto.LinkDTO;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.util.AppUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

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
    PostEntity toEntity(PostDTO postDTO);

    @Mapping(source = "title" , target = "title")
    @Mapping(source = "subtitle" , target = "subtitle")
    @Mapping(source = "content" , target = "content")
    @Mapping(source = "authorId" , target = "authorId")
    @Mapping(source = "series" , target = "series")
    @Mapping(ignore = true, target = "links")
    @Mapping(ignore = true, target = "tags")
    @Mapping(source = "postId" , target="postId")
    @Mapping(source = "identifier" , target="identifier")
    PostDTO toDto(PostEntity postEntity);

    @Mapping(source = "postEntity.title" , target = "title")
    @Mapping(source = "postEntity.subtitle" , target = "subtitle")
    @Mapping(source = "postEntity.content" , target = "content")
    @Mapping(source = "postEntity.authorId" , target = "authorId")
    @Mapping(source = "postEntity.series" , target = "series")
    @Mapping(source = "postEntity.postId" , target="postId")
    @Mapping(source = "postEntity.identifier" , target="identifier")
    @Mapping(source = "tags" , target="tags")
    @Mapping(source = "links" , target="links")
    public PostDTO toDto(PostEntity postEntity , List<TagDTO> tags , List<String> links);

    default String toId(PostDTO postDTO){
        return AppUtil.generateIdentifier("PO");
    }
}
