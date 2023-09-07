package com.tech.bee.postservice.api;

import com.tech.bee.postservice.annotation.TransactionId;
import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tag")
public class TagApi {

    private final TagService tagService;

    @TransactionId
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO> findTagByName(@RequestParam("tagName") final String tagName){
        TagDTO tagDTO = tagService.findTagByName(tagName);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(tagDTO).build() , HttpStatus.CREATED);
    }
}
