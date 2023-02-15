package com.andela.XMLFileUploadToMySQLDb.repository;

import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XMLRepository extends JpaRepository<XMLData, Long> {
    Page<XMLData> findAll(Specification<XMLData> specification, Pageable pageable);

}
