package com.andela.XMLFileUploadToMySQLDb.service;

import com.andela.XMLFileUploadToMySQLDb.model.XMLValid;
import com.andela.XMLFileUploadToMySQLDb.repository.XMLRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
class XMLServiceTest {
    @Mock
    private XMLRepository xmlRepository;
    @Mock
    XMLValid xmlValid;

    @InjectMocks
    private XMLService xmlService;
    int page = 0;
    int size = 5;
    @Mock
    private MockMultipartFile mockMultipartFile;

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
        XMLValid xmlValid = xmlService.isXMLValid(mockMultipartFile);

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
        XMLValid xmlValid = xmlService.isXMLValid(file);

        // Assert
        assertFalse(xmlValid.getIsValid());
    }

    @Test
    public void testIsXMLValidWithIOException() throws IOException {
        when(mockMultipartFile.getInputStream()).thenThrow(new IOException("Error reading file"));

        XMLValid result = new XMLValid();
        try {
            result = xmlService.isXMLValid(mockMultipartFile);
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
        XMLValid result = xmlService.isXMLValid(mockMultipartFile);
        assertFalse(result.getIsValid());
    }
    @Test
    public void testIsXMLValidWithInvalidXML() {
        // Create a mock MultipartFile object with invalid XML
        String xml = "<root><element></root>";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "filename.xml", "text/xml", xml.getBytes());

        // Call the isXMLValid() method and assert that it returns an invalid XMLValidDto object
        XMLValid result = xmlService.isXMLValid(mockMultipartFile);
        assertFalse(result.getIsValid());
    }
}