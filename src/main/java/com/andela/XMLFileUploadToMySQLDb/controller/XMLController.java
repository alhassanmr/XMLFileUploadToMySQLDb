package com.andela.XMLFileUploadToMySQLDb.controller;

import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.service.XMLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

@Slf4j
@RestController
@RequestMapping("/api/xml")
public class XMLController {

    private final XMLService xmlService;

    public XMLController(XMLService xmlService) {
        this.xmlService = xmlService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadXMLFile(@RequestParam("file") MultipartFile file) {
        log.info("POST /api/xml/upload");
        // check for empty file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file received");
        }
        // Validate the XML file against the XSD schema
        Object isValid = xmlService.isXMLValid(file);
        if (isValid.equals(false)) {
            return ResponseEntity.badRequest().body("Invalid XML file");
        }
        // Parse the data from the XML file
        xmlService.parseXMLData((Document) isValid, file.getOriginalFilename());
        return ResponseEntity.ok().body("File uploaded successfully");
    }

    @GetMapping("")
    public Page<XMLData> getXMLDataWithFilter(@RequestParam(required = false) String filter,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "1") int size,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        log.info("GET /api/xml filter= {}, page= {}, size= {}, sortBy= {}, direction= {}", filter, page, size, sortBy, direction);
        return xmlService.getXMLDataWithFilter(filter, page, size, sortBy, direction);
    }

}
