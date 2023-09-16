package com.tech.bee.postservice.resources;

import com.tech.bee.postservice.annotation.TransactionId;
import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiConstants.PathConstants.PATH_TAG_RESOURCE)
public class TagResource {

    private final TagService tagService;

    @TransactionId
    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(@RequestBody TagDTO tagDTO){
        String tagId = tagService.create(tagDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(tagId).build() , HttpStatus.OK);
    }
    @TransactionId
    @GetMapping(value="/search")
    public ResponseEntity<ApiResponseDTO> findTagByName(@RequestParam("tagName") final String tagName){
        TagDTO tagDTO = tagService.findTagByName(tagName);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(tagDTO).build() , HttpStatus.OK);
    }



}
