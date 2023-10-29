package com.tech.bee.postservice.repository;

import com.tech.bee.postservice.dto.PostSearchDTO;
import com.tech.bee.postservice.entity.PostEntity;
import com.tech.bee.postservice.entity.TagEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomRepository {

    private final PostRepository postRepository;
    private static Specification<PostEntity> findPostsWithSearchCriteria(PostSearchDTO postSearchDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotEmpty(postSearchDTO.getTitle()))
                predicates.add(criteriaBuilder.equal(root.get("title"), StringUtils.trimToEmpty(postSearchDTO.getTitle())));
            if(StringUtils.isNotEmpty(postSearchDTO.getSubtitle()))
                predicates.add(criteriaBuilder.equal(root.get("subtitle"), StringUtils.trimToEmpty(postSearchDTO.getSubtitle())));
            if(StringUtils.isNotEmpty(postSearchDTO.getCategory()))
                predicates.add(criteriaBuilder.equal(root.get("category"), StringUtils.trimToEmpty(postSearchDTO.getCategory())));
            if(StringUtils.isNotEmpty(postSearchDTO.getSeries()))
                predicates.add(criteriaBuilder.equal(root.get("series"), StringUtils.trimToEmpty(postSearchDTO.getSeries())));
            if(StringUtils.isNotEmpty(postSearchDTO.getAuthorId()))
                predicates.add(criteriaBuilder.equal(root.get("authorId"), StringUtils.trimToEmpty(postSearchDTO.getAuthorId())));
            if(CollectionUtils.isNotEmpty(postSearchDTO.getTags())){
                Join<PostEntity, TagEntity> tagJoins = root.join("tags", JoinType.INNER);
                Predicate[] tagPredicates = new Predicate[postSearchDTO.getTags().size()];
                for (int i = 0; i < postSearchDTO.getTags().size(); i++) {
                    tagPredicates[i] = criteriaBuilder.like(tagJoins.get("name"), "%" + postSearchDTO.getTags().get(i) + "%");
                }
                Predicate tagPredicate = criteriaBuilder.or(tagPredicates);
                predicates.add(tagPredicate);
                Predicate[] predicateList = predicates.toArray(new Predicate[0]);
                return criteriaBuilder.and(predicateList);
            }
            Predicate[] predicateList = predicates.toArray(new Predicate[0]);
            return criteriaBuilder.and(predicateList);
        };
    }

    public Page<PostEntity> findPosts(PostSearchDTO postSearchDTO , Pageable pageable){
        Specification<PostEntity> specification = findPostsWithSearchCriteria(postSearchDTO);
        return postRepository.findAll(specification , pageable);
    }

    public Page<PostEntity> findPosts(Pageable pageable){
        return postRepository.findAll(pageable);
    }
}
