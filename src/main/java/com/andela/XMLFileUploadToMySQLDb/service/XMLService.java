package com.andela.XMLFileUploadToMySQLDb.service;

import com.andela.XMLFileUploadToMySQLDb.dto.XMLDataFilterDTO;
import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.model.XMLValid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

public interface XMLService {

    XMLValid isXMLValid(MultipartFile xmlFile);

    void parseXMLData(Document document, String filename);

    Page<XMLData> findXMLDataByFilter(XMLDataFilterDTO xmlDataFilterDTO, int pageNumber, int pageSize);

    Page<XMLData> findAll(XMLDataFilterDTO xmlDataFilterDTO, Pageable pageable);
}
