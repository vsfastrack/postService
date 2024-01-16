package com.tech.bee.postservice.mapper;

import com.tech.bee.postservice.dto.PostSummaryDTO;
import com.tech.bee.postservice.dto.PreferenceDTO;
import com.tech.bee.postservice.model.PreferenceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PreferenceMapper {

    @Mapping(source = "preferenceName" , target = "preference")
    @Mapping(source = "postSummaries" , target = "postSummaries")
    PreferenceDTO toDTO(final String preferenceName ,
                        final List<PostSummaryDTO> postSummaries);
}
