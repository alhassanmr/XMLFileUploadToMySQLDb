package com.andela.XMLFileUploadToMySQLDb.controller;

import com.andela.XMLFileUploadToMySQLDb.dto.XMLDataFilterDTO;
import com.andela.XMLFileUploadToMySQLDb.dto.XMLValidDto;
import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.model.XMLDataSpecification;
import com.andela.XMLFileUploadToMySQLDb.repository.XMLRepository;
import com.andela.XMLFileUploadToMySQLDb.service.XMLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/xml")
public class XMLController {

    private final XMLService xmlService;

    final
    XMLRepository xmlRepository;

    public XMLController(XMLService xmlService, XMLRepository xmlRepository) {
        this.xmlService = xmlService;
        this.xmlRepository = xmlRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadXMLFile(@RequestParam("file") MultipartFile file) {
        log.info("POST /api/xml/upload");
        // check for empty file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file received");
        }
        // Validate the XML file against the XSD schema
        XMLValidDto isValid = xmlService.isXMLValid(file);
        if (isValid.getIsValid().equals(false)) {
            return ResponseEntity.badRequest().body("Invalid XML file");
        }
        // Parse the data from the XML file
        xmlService.parseXMLData(isValid.getDocument(), file.getOriginalFilename());
        return ResponseEntity.ok().body("File uploaded successfully");
    }

    @GetMapping
    public ResponseEntity<List<XMLData>> getXMLData( @RequestParam(required = false) String newspaperName,
                                                     @RequestParam(required = false) Integer screenWidth,
                                                     @RequestParam(required = false) Integer screenHeight,
                                                     @RequestParam(required = false) Integer screenDpi,
                                                     @RequestParam(required = false) String filename,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "uploadTime,desc") String[] sort,
                                                     @ModelAttribute XMLDataFilterDTO xmlDataFilterDTO) {
        return new ResponseEntity<>(xmlService.findXMLDataByFilter(xmlDataFilterDTO).getContent(), HttpStatus.OK);
    }
}
