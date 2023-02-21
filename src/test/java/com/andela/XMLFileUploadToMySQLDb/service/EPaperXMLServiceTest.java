package com.andela.XMLFileUploadToMySQLDb.service;

import com.andela.XMLFileUploadToMySQLDb.dto.XMLDataFilterDTO;
import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.model.DefaultSpecification;
import com.andela.XMLFileUploadToMySQLDb.model.XMLDataSpecification;
import com.andela.XMLFileUploadToMySQLDb.model.XMLValid;
import com.andela.XMLFileUploadToMySQLDb.repository.XMLRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EPaperXMLServiceTest {
    @Mock
    private XMLRepository xmlRepository;
    @Mock
    XMLValid xmlValid;

    @InjectMocks
    private EPaperXMLService EPaperXmlService;
    int page = 0;
    int size = 5;
    @Mock
    private MockMultipartFile mockMultipartFile;
    public EPaperXMLServiceTest() {
        MockitoAnnotations.openMocks(this);
        EPaperXmlService = new EPaperXMLService(xmlRepository);
    }

    @Test
    public void testValidXML() throws Exception {
        // Arrange
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<epaperRequest>\n" +
                "<deviceInfo name=\"Browser\" id=\"test@comp\">\n" +
                "<screenInfo width=\"1280\" height=\"752\" dpi=\"160\" />\n" +
                "<osInfo name=\"Browser\" version=\"1.0\" />\n" +
                "<appInfo>\n" +
                "<newspaperName>abb</newspaperName>\n" +
                "<version>1.0</version>\n" +
                "</appInfo>\n" +
                "</deviceInfo>\n" +
                "<getPages editionDefId=\"11\" publicationDate=\"2017-06-06\" />\n" +
                "</epaperRequest>\n";
        when(mockMultipartFile.getInputStream()).thenReturn(
                new ByteArrayInputStream(xml.getBytes()));
//        XMLValidation xmlService = new XMLValidation();

        // Act
        XMLValid xmlValid = EPaperXmlService.isXMLValid(mockMultipartFile);

        // Assert
        assertTrue(xmlValid.getIsValid());
    }

    @Test
    public void testInvalidXML() throws Exception {
        // Arrange
        String xml = "this is not valid xml";

        // Create a mock MultipartFile from the invalid XML string
        MockMultipartFile file = new MockMultipartFile("file", "test.jpeg", "image/jpeg", xml.getBytes());

        // Act
        XMLValid xmlValid = EPaperXmlService.isXMLValid(file);

        // Assert
        assertFalse(xmlValid.getIsValid());
    }

    @Test
    public void testIsXMLValidWithIOException() throws IOException {
        when(mockMultipartFile.getInputStream()).thenThrow(new IOException("Error reading file"));

        XMLValid result = new XMLValid();
        try {
            result = EPaperXmlService.isXMLValid(mockMultipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(result.getIsValid());
    }
    @Test
    public void testIsXMLValidWithParserConfigurationException() {
        // Create a mock MultipartFile object that triggers the ParserConfigurationException
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "filename.xml", "text/xml", new byte[0]);

        // Call the isXMLValid() method and assert that it returns an invalid XMLValidDto object
        XMLValid result = EPaperXmlService.isXMLValid(mockMultipartFile);
        assertFalse(result.getIsValid());
    }
    @Test
    public void testIsXMLValidWithInvalidXML() {
        // Create a mock MultipartFile object with invalid XML
        String xml = "<root><element></root>";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "filename.xml", "text/xml", xml.getBytes());

        // Call the isXMLValid() method and assert that it returns an invalid XMLValidDto object
        XMLValid result = EPaperXmlService.isXMLValid(mockMultipartFile);
        assertFalse(result.getIsValid());
    }

    @Test
    void testFindAllWithFilter() {
        // create a filter with some values
        XMLDataFilterDTO filter = new XMLDataFilterDTO();
        filter.setNewspaperName("Test Newspaper");
        filter.setFilename("test.xml");

        // create a pageable object
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        // create a list of XMLData objects to return from the repository
        XMLData xmlData = new XMLData();
        xmlData.setId(1L);
        xmlData.setNewspaperName("Test Newspaper");
        xmlData.setFilename("test.xml");
        PageImpl<XMLData> page = new PageImpl<>(Collections.singletonList(xmlData));

        // mock the repository method to return the list of XMLData objects
        when(xmlRepository.findAll(any(XMLDataSpecification.class), any(Pageable.class))).thenReturn(page);

        // call the service method with the filter and pageable objects
        Page<XMLData> result = EPaperXmlService.findAll(filter, pageable);

        // assert that the returned page object contains the expected data
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId().longValue());
        assertEquals("Test Newspaper", result.getContent().get(0).getNewspaperName());
        assertEquals("test.xml", result.getContent().get(0).getFilename());
    }

    @Test
    void testFindAllWithoutFilter() {
        // create a pageable object
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        // create a list of XMLData objects to return from the repository
        XMLData xmlData = new XMLData();
        xmlData.setId(1L);
        xmlData.setNewspaperName("Test Newspaper");
        xmlData.setFilename("test.xml");
        PageImpl<XMLData> page = new PageImpl<>(Collections.singletonList(xmlData));

        // mock the repository method to return the list of XMLData objects
        when(xmlRepository.findAll(any(DefaultSpecification.class), any(Pageable.class))).thenReturn(page);

        // call the service method without a filter and with the pageable object
        Page<XMLData> result = EPaperXmlService.findAll(null, pageable);

        // assert that the returned page object contains the expected data
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId().longValue());
        assertEquals("Test Newspaper", result.getContent().get(0).getNewspaperName());
        assertEquals("test.xml", result.getContent().get(0).getFilename());
    }

}