package com.andela.XMLFileUploadToMySQLDb.repository;

import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface XMLRepository extends JpaRepository<XMLData, Long> {
    @Query("SELECT t FROM XMLData t WHERE t.newspaperName LIKE %:filter% OR t.screenWidth LIKE %:filter% " +
            "OR t.screenHeight like %:filter% OR t.screenDpi like %:filter%")
    Page<XMLData> findByFilter(@Param("filter") String filter, Pageable pageable);

}
