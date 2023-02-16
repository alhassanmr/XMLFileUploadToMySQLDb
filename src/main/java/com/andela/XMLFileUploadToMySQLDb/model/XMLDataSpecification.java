package com.andela.XMLFileUploadToMySQLDb.model;

import com.andela.XMLFileUploadToMySQLDb.dto.XMLDataFilterDTO;
import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class XMLDataSpecification implements Specification<XMLData> {
    private final XMLDataFilterDTO xmlDataFilterDTO;
    public XMLDataSpecification(XMLDataFilterDTO xmlDataFilterDTO) {
        this.xmlDataFilterDTO = xmlDataFilterDTO;
    }
    @Override
    public Predicate toPredicate(Root<XMLData> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(xmlDataFilterDTO.getNewspaperName())) {
            predicates.add(criteriaBuilder.equal(root.get("newspaperName"), xmlDataFilterDTO.getNewspaperName()));
        }

        if (StringUtils.isNotBlank(xmlDataFilterDTO.getScreenWidth())) {
            predicates.add(criteriaBuilder.equal(root.get("screenWidth"), xmlDataFilterDTO.getScreenWidth()));
        }

        if (StringUtils.isNotBlank(xmlDataFilterDTO.getScreenHeight())) {
            predicates.add(criteriaBuilder.equal(root.get("screenHeight"), xmlDataFilterDTO.getScreenHeight()));
        }

        if (StringUtils.isNotBlank(xmlDataFilterDTO.getScreenDpi())) {
            predicates.add(criteriaBuilder.equal(root.get("screenDpi"), xmlDataFilterDTO.getScreenDpi()));
        }

        if (StringUtils.isNotBlank(xmlDataFilterDTO.getFilename())) {
            predicates.add(criteriaBuilder.equal(root.get("filename"), xmlDataFilterDTO.getFilename()));
        }

        if (xmlDataFilterDTO.getSortOrders() != null && StringUtils.isNotBlank(xmlDataFilterDTO.getSortOrders().getSortBy())) {
            Path<Object> orderBy;
            if (xmlDataFilterDTO.getSortOrders().getSortBy().equalsIgnoreCase("newspaperName")) {
                orderBy = root.get("newspaperName");
            } else if (xmlDataFilterDTO.getSortOrders().getSortBy().equalsIgnoreCase("screenWidth")) {
                orderBy = root.get("screenWidth");
            } else if (xmlDataFilterDTO.getSortOrders().getSortBy().equalsIgnoreCase("screenHeight")) {
                orderBy = root.get("screenHeight");
            } else if (xmlDataFilterDTO.getSortOrders().getSortBy().equalsIgnoreCase("screenDpi")) {
                orderBy = root.get("screenDpi");
            } else if (xmlDataFilterDTO.getSortOrders().getSortBy().equalsIgnoreCase("filename")) {
                orderBy = root.get("filename");
            } else {
                throw new IllegalArgumentException("Invalid sort field: " + xmlDataFilterDTO.getSortOrders().getSortBy());
            }
            if (xmlDataFilterDTO.getSortOrders().isDescending()) {
                query.orderBy(criteriaBuilder.desc(orderBy));
            } else {
                query.orderBy(criteriaBuilder.asc(orderBy));
            }
        }

        return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
