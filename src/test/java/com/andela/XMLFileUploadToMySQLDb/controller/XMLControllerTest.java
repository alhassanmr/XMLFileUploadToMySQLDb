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
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.util.Collections;

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

    //testing a valid xml file
    @Test
    public void testUploadXMLFile() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        Document document = mock(Document.class);
        when(xmlService.isXMLValid(file)).thenReturn(document);
        ResponseEntity<String> response = xmlController.uploadXMLFile(file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File uploaded successfully", response.getBody());
        verify(xmlService).isXMLValid(file);
        verify(xmlService).parseXMLData(document, file.getOriginalFilename());
    }

    //testing empty file
    @Test
    public void testUploadXMLFileWithEmptyFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);
        ResponseEntity<String> response = xmlController.uploadXMLFile(file);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No file received", response.getBody());
        verify(xmlService, never()).isXMLValid(file);
        verify(xmlService, never()).parseXMLData(any(Document.class), anyString());
    }

    @Test
    public void testUploadXMLFileWithInvalidXML() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(xmlService.isXMLValid(file)).thenReturn(false);
        ResponseEntity<String> response = xmlController.uploadXMLFile(file);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid XML file", response.getBody());
        verify(xmlService).isXMLValid(file);
        verify(xmlService, never()).parseXMLData(any(Document.class), anyString());
    }

    @Test
    public void testGetXMLDataWithFilter() {
        String filter = "filter";
        int page = 0;
        int size = 1;
        String sortBy = "id";
        Sort.Direction direction = Sort.Direction.ASC;

        Page<XMLData> expectedResult = new PageImpl<>(Collections.emptyList());
        when(xmlService.getXMLDataWithFilter(filter, page, size, sortBy, direction)).thenReturn(expectedResult);

        Page<XMLData> result = xmlController.getXMLDataWithFilter(filter, page, size, sortBy, direction);

        assertEquals(expectedResult, result);
    }
}