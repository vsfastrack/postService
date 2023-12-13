package com.tech.bee.postservice.resources;

import com.tech.bee.postservice.annotation.RequestMetrics;
import com.tech.bee.postservice.annotation.TransactionId;
import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.dto.PostSearchDTO;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.PostSummaryDTO;
import com.tech.bee.postservice.dto.ReactionDTO;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.metrics.RequestMetricCounter;
import com.tech.bee.postservice.service.PostService;
import com.tech.bee.postservice.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiConstants.PathConstants.PATH_POST_RESOURCE)
@Slf4j
public class PostResource {

    private final PostService postService;

    @TransactionId
    @RequestMetrics
    @PostMapping
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> create(@RequestBody PostDTO postDTO){
        String postId = postService.createPost(postDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(postId).build() , HttpStatus.CREATED);
    }

    @TransactionId
    @RequestMetrics
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
    @RequestMetrics
    @GetMapping("/{postIdentifier}/details")
    public ResponseEntity<ApiResponseDTO> findPostDetails(@PathVariable("postIdentifier") final String postIdentifier){
        log.info("{} hit with postIdentifier {}" , "/{postIdentifier}/details" , postIdentifier);
        PostDTO postDTO = postService.getPostDetails(postIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(postDTO).build(), HttpStatus.OK);
    }

    @TransactionId
    @RequestMetrics
    @PatchMapping("/{postIdentifier}")
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> update(@PathVariable("postIdentifier") final String postIdentifier ,
                                                     @RequestBody PostDTO postDTO){
        postService.update(postDTO,postIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }

    @TransactionId
    @RequestMetrics
    @DeleteMapping("/{postIdentifier}")
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable("postIdentifier") final String postIdentifier){
        postService.delete(postIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }

    @TransactionId
    @RequestMetrics
    @GetMapping("/summaries")
    public ResponseEntity<ApiResponseDTO> getPostSummaries(@RequestParam(name = "count" , required = false ,
                                                                        defaultValue = "3") Integer count){
        List<PostSummaryDTO> postSummaries = postService.findSummariesForTopRatedPosts(count);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(postSummaries).build(), HttpStatus.OK);
    }

    @TransactionId
    @RequestMetrics
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> findPostSummariesList(@RequestParam(name = "pageIndex" , defaultValue = "0") final int pageIndex ,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") final int pageSize ,
                                                       @RequestParam(name ="sortDir" , defaultValue = "DESC") final Enums.SortDirection sortDir ,
                                                       @RequestParam(name="sortKey" , defaultValue = "createdWhen") final String sortKey
    ){
        return new ResponseEntity<>(ApiResponseDTO.builder().content(
                postService.findPostSummariesList(pageIndex , pageSize ,sortDir ,sortKey)).build()
                , HttpStatus.OK);
    }

    @TransactionId
    @RequestMetrics
    @PatchMapping("/{postIdentifier}/react")
    public ResponseEntity<ApiResponseDTO> like(@PathVariable("postIdentifier") final String postIdentifier ,
                                                 @RequestParam(name = "reaction") final Enums.ReactionEnum reaction){
        String reactionIdentifier = postService.react(postIdentifier , reaction);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(reactionIdentifier).build(), HttpStatus.OK);
    }
}
