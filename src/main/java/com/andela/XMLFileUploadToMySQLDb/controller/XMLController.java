package com.andela.XMLFileUploadToMySQLDb.controller;

import com.andela.XMLFileUploadToMySQLDb.dto.XMLValidDto;
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
        XMLValidDto isValid = xmlService.isXMLValid(file);
        if (isValid.getIsValid().equals(false)) {
            return ResponseEntity.badRequest().body("Invalid XML file");
        }
        // Parse the data from the XML file
        xmlService.parseXMLData(isValid.getDocument(), file.getOriginalFilename());
        return ResponseEntity.ok().body("File uploaded successfully");
    }

    

}
