package com.tech.bee.postservice.resources;

import com.tech.bee.postservice.annotation.TransactionId;
import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.common.PageResponseDTO;
import com.tech.bee.postservice.dto.PostSearchDTO;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiConstants.PathConstants.PATH_POST_RESOURCE)
public class PostResource {

    private final PostService postService;

    @TransactionId
    @PostMapping
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> create(@RequestBody PostDTO postDTO){
        String postId = postService.createPost(postDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(postId).build() , HttpStatus.CREATED);
    }

    @TransactionId
    @GetMapping("/search")
    @Secured(ApiConstants.RoleConstants.ROLE_USER)
    public ResponseEntity<ApiResponseDTO> find(@RequestBody PostSearchDTO postSearchDTO ,
                                               @RequestParam(name = "pageIndex" , defaultValue = "0") final int pageIndex ,
                                               @RequestParam(name = "pageSize", defaultValue = "10") final int pageSize ,
                                               @RequestParam(name ="sortDir" , defaultValue = "DESC") final Enums.SortDirection sortDir ,
                                               @RequestParam(name="sortKey" , defaultValue = "createdWhen") final String sortKey
                                               ){
        return new ResponseEntity<>(ApiResponseDTO.builder().content(
                postService.findPosts(postSearchDTO , pageIndex , pageSize ,sortDir ,sortKey)).build()
                , HttpStatus.OK);
    }

    @TransactionId
    @GetMapping("/{postIdentifier}")
    @Secured(ApiConstants.RoleConstants.ROLE_USER)
    public ResponseEntity<ApiResponseDTO> findPostDetails(@PathVariable("postIdentifier") final String postIdentifier){
        PostDTO postDTO = postService.getPostDetails(postIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(postDTO).build(), HttpStatus.OK);
    }

    @TransactionId
    @PatchMapping("/{postIdentifier}")
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> update(@PathVariable("postIdentifier") final String postIdentifier ,
                                                     @RequestBody PostDTO postDTO){
        postService.update(postDTO,postIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }

    @TransactionId
    @DeleteMapping("/{postIdentifier}")
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable("postIdentifier") final String postIdentifier){
        postService.delete(postIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }
}
