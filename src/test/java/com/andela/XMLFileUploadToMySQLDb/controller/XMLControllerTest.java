package com.andela.XMLFileUploadToMySQLDb.controller;

import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.service.XMLService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class XMLControllerTest {

    @InjectMocks
    private XMLController xmlController;

    @Mock
    private XMLService xmlService;

    @Test
    void testUploadXMLFileWithValidFile() throws Exception {
        // create a valid XML file
        String xml = "<root><data>test</data></root>";
        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", xml.getBytes());

        // call the uploadXMLFile method
        ResponseEntity<String> response = xmlController.uploadXMLFile(file);

        // assert that the response is successful and contains the expected message
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded successfully", response.getBody());
    }

    @Test
    void testUploadXMLFileWithInvalidFile() throws Exception {
        // create an invalid XML file
        String xml = "<root><data>test</data>";
        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", xml.getBytes());

        // call the uploadXMLFile method
        ResponseEntity<String> response = xmlController.uploadXMLFile(file);

        // assert that the response is a bad request and contains the expected error message
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid XML file", response.getBody());
    }

    @Test
    void testUploadXMLFileWithNoFile() throws Exception {
        // create an empty file
        MockMultipartFile file = new MockMultipartFile("file", new byte[0]);

        // call the uploadXMLFile method
        ResponseEntity<String> response = xmlController.uploadXMLFile(file);

        // assert that the response is a bad request and contains the expected error message
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("No file received", response.getBody());
    }

    @Test
    void testGetXMLDataWithFilter() {
        // create a filter with some values
        String newspaperName = "Test Newspaper";
        String screenWidth = "1920";
        String filename = "test.xml";
        String sortField = "date";
        String isDescending = "true";

        // call the getXMLData method with the filter
        ResponseEntity<List<XMLData>> response = xmlController.getXMLData(newspaperName, screenWidth, null, null, filename, sortField, isDescending, 0, 5);

        // assert that the response is successful and contains some data
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5, response.getBody().size());
    }

    @Test
    void testGetXMLDataWithEmptyFilter() {
        // call the getXMLData method with an empty filter
        ResponseEntity<List<XMLData>> response = xmlController.getXMLData(null, null, null, null, null, null, null, 0, 5);

        // assert that the response is successful and contains an empty list
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void testGetXMLDataWithPagination() {
        // call the getXMLData method with pagination parameters
        ResponseEntity<List<XMLData>> response = xmlController.getXMLData(null, null, null, null, null, null, null, 1, 5);

        // assert that the response is successful and contains the correct number of items
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5, response.getBody().size());
    }

}