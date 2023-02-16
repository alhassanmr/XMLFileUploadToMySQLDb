package com.andela.XMLFileUploadToMySQLDb.model;

import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class DefaultSpecification implements Specification<XMLData> {
    @Override
    public Predicate toPredicate(Root<XMLData> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
}
