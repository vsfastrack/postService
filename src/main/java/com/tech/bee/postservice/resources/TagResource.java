package com.tech.bee.postservice.resources;

import com.tech.bee.postservice.annotation.TransactionId;
import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.TagDTO;
import com.tech.bee.postservice.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiConstants.PathConstants.PATH_TAG_RESOURCE)
public class TagResource {

    private final TagService tagService;

    @TransactionId
    @PostMapping
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> create(@RequestBody TagDTO tagDTO){
        String tagId = tagService.create(tagDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(tagId).build() , HttpStatus.OK);
    }
    @TransactionId
    @GetMapping(value="/search")
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> findTagByName(@RequestParam("tagName") final String tagName){
        TagDTO tagDTO = tagService.findTagByName(tagName);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(tagDTO).build() , HttpStatus.OK);
    }

    @TransactionId
    @GetMapping
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> find(){
        return new ResponseEntity<>(ApiResponseDTO.builder().content(tagService.findTags()).build() , HttpStatus.OK);
    }

    @TransactionId
    @DeleteMapping("/{tagIdentifier}")
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable("tagIdentifier") final String tagIdentifier){
        tagService.delete(tagIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }
    @TransactionId
    @PatchMapping("/{tagIdentifier}")
    @Secured(ApiConstants.RoleConstants.ROLE_AUTHOR)
    public ResponseEntity<ApiResponseDTO> update(@PathVariable("tagIdentifier") final String tagIdentifier,
                                                            @RequestBody final TagDTO tagDTO){
        tagService.update(tagIdentifier , tagDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }
}
