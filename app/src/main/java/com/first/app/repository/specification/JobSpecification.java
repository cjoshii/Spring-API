package com.first.app.repository.specification;
import org.springframework.data.jpa.domain.Specification;

import com.first.app.entity.Job;

public class JobSpecification {
    

    public static Specification<Job> hasTitle(String title) {
        return (root, _, criteriaBuilder) ->
                title == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Job> hasDescription(String description) {
        return (root, _, criteriaBuilder) ->
                description == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Job> hasLocation(String location) {
        return (root, _, criteriaBuilder) ->
        location == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }
}