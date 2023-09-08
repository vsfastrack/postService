package com.tech.bee.postservice.resources;

import com.tech.bee.postservice.annotation.TransactionId;
import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiConstants.PathConstants.PATH_POST_RESOURCE)
public class PostResource {

    private final PostService postService;

    @TransactionId
    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(@RequestBody PostDTO postDTO){
        String postId = postService.createPost(postDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(postId).build() , HttpStatus.CREATED);
    }
}
