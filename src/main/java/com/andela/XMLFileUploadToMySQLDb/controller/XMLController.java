package com.andela.XMLFileUploadToMySQLDb.controller;

import com.andela.XMLFileUploadToMySQLDb.dto.XMLDataFilterDTO;
import com.andela.XMLFileUploadToMySQLDb.model.XMLValid;
import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.repository.XMLRepository;
import com.andela.XMLFileUploadToMySQLDb.service.XMLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        XMLValid isValid = xmlService.isXMLValid(file);
        if (isValid.getIsValid().equals(false)) {
            return ResponseEntity.badRequest().body("Invalid XML file");
        }
        // Parse the data from the XML file
        xmlService.parseXMLData(isValid.getDocument(), file.getOriginalFilename());
        return ResponseEntity.ok().body("File uploaded successfully");
    }

    @GetMapping
    public ResponseEntity<List<XMLData>> getXMLData( @RequestParam(required = false) XMLDataFilterDTO xmlDataFilterDTO,
                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "5") int pageSize) {
        log.info("GET /api/xml xmlDataFilterDTO= {}, pageNumber= {}, pageSize= {}", xmlDataFilterDTO, pageNumber, pageSize);
        return new ResponseEntity<>(xmlService.findXMLDataByFilter(xmlDataFilterDTO, pageNumber, pageSize).getContent(), HttpStatus.OK);
    }
}
