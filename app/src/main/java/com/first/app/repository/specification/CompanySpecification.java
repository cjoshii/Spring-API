package com.first.app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.first.app.entity.Company;

public class CompanySpecification {
    
    public static Specification<Company> hasName(String name) {
        return (root, _, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Company> hasLocation(String location) {
        return (root, _, criteriaBuilder) ->
                location == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<Company> hasDescription(String description) {
        return (root, _, criteriaBuilder) ->
                description == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }
}
