package com.tech.bee.postservice.mapper;

import com.tech.bee.postservice.entity.ReferenceEntity;
import com.tech.bee.postservice.util.AppUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReferenceMapper {

    @Mapping(source = "reference" , target = "content")
    @Mapping(expression = "java(toId())" , target="referenceId")
    public ReferenceEntity toEntity(String  reference);

    default String toId(){
        return AppUtil.generateIdentifier("RE");
    }
}
