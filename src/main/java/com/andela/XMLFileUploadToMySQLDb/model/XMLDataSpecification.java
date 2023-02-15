package com.andela.XMLFileUploadToMySQLDb.model;

import com.andela.XMLFileUploadToMySQLDb.dto.XMLDataFilterDTO;
import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

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
        if (xmlDataFilterDTO.getNewspaperName() != null) {
            predicates.add(criteriaBuilder.equal(root.get("newspaperName"), xmlDataFilterDTO.getNewspaperName()));
        }
        if (xmlDataFilterDTO.getScreenWidth() != null) {
            predicates.add(criteriaBuilder.equal(root.get("screenWidth"), xmlDataFilterDTO.getScreenWidth()));
        }
        if (xmlDataFilterDTO.getScreenHeight() != null) {
            predicates.add(criteriaBuilder.equal(root.get("screenHeight"), xmlDataFilterDTO.getScreenHeight()));
        }
        if (xmlDataFilterDTO.getScreenDpi() != null) {
            predicates.add(criteriaBuilder.equal(root.get("screenDpi"), xmlDataFilterDTO.getScreenDpi()));
        }
        if (xmlDataFilterDTO.getFilename() != null) {
            predicates.add(criteriaBuilder.equal(root.get("filename"), xmlDataFilterDTO.getFilename()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
