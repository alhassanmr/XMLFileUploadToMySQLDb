package com.andela.XMLFileUploadToMySQLDb.service;

import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.repository.XMLRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.metadata.IIOMetadataNode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class XMLServiceTest {
    @Mock
    private XMLRepository xmlRepository;

    @InjectMocks
    private XMLService xmlService;
    int page = 0;
    int size = 10;
    String sortBy = "createdAt";
    Sort.Direction direction = Sort.Direction.ASC;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testIsXMLValid() throws IOException {
        // Given
        MultipartFile validXmlFile = mock(MultipartFile.class);
        InputStream validXmlFileInputStream = new ByteArrayInputStream("<root><element>data</element></root>".getBytes());
        when(validXmlFile.getInputStream()).thenReturn(validXmlFileInputStream);

        MultipartFile invalidXmlFile = mock(MultipartFile.class);
        InputStream invalidXmlFileInputStream = new ByteArrayInputStream("<root><element>data".getBytes());
        when(invalidXmlFile.getInputStream()).thenReturn(invalidXmlFileInputStream);

        // When
        Object resultForValidXml = xmlService.isXMLValid(validXmlFile);
        Object resultForInvalidXml = xmlService.isXMLValid(invalidXmlFile);

        // Then
        assertTrue(resultForValidXml instanceof Document);
        assertFalse(resultForInvalidXml instanceof Document);
        assertEquals(false, resultForInvalidXml);
    }

    @Test
    void testParseXMLData() {
        XMLService xmlService = mock(XMLService.class);
        Document document = mock(Document.class);
        NodeList nList = mock(NodeList.class);
        Node nNode = mock(Node.class);
        Element eElement = mock(Element.class);
        when(document.getElementsByTagName("deviceInfo")).thenReturn(nList);
        when(nList.getLength()).thenReturn(1);
        when(nList.item(0)).thenReturn(nNode);
        when(nNode.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(nNode.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(eElement.getElementsByTagName("newspaperName")).thenReturn(nList);
        when(nList.item(0)).thenReturn(nNode);
        when(nNode.getTextContent()).thenReturn("The Times");
        when(eElement.getElementsByTagName("screenInfo")).thenReturn(nList);
        when(nList.item(0)).thenReturn(nNode);
        NamedNodeMap attributes = mock(NamedNodeMap.class);
        when(nNode.getAttributes()).thenReturn(attributes);
        Node widthAttribute = mock(Node.class);
        when(attributes.getNamedItem("width")).thenReturn(widthAttribute);
        when(widthAttribute.getNodeValue()).thenReturn("1920");
        Node heightAttribute = mock(Node.class);
        when(attributes.getNamedItem("height")).thenReturn(heightAttribute);
        when(heightAttribute.getNodeValue()).thenReturn("1080");
        Node dpiAttribute = mock(Node.class);
        when(attributes.getNamedItem("dpi")).thenReturn(dpiAttribute);
        when(dpiAttribute.getNodeValue()).thenReturn("72");

        // When
        xmlService.parseXMLData(document, "test.xml");

        // Then
        XMLData xmlData = new XMLData();
        xmlData.setNewspaperName("The Times");
        xmlData.setScreenWidth("1920");
        xmlData.setScreenHeight("1080");
        xmlData.setScreenDpi("72");
        xmlData.setFilename("test.xml");
        xmlData.setUploadTime(new Timestamp(System.currentTimeMillis()));
        verify(xmlRepository, times(1)).save(xmlData);
    }

    @Test
    public void testGetXMLDataWithFilter_withoutFilter() {
        // Given
        String filter = "";

        // Mock repository behavior
        Page<XMLData> expectedData = createMockData();
        when(xmlRepository.findAll(any(Pageable.class))).thenReturn(expectedData);

        // When
        Page<XMLData> result = xmlService.getXMLDataWithFilter(filter, page, size, sortBy, direction);

        // Then
        verify(xmlRepository, times(1)).findAll(any(Pageable.class));
        assertEquals(expectedData, result);
    }

    @Test
    public void testGetXMLDataWithFilter_withFilter() {
        // Given
        String filter = "data1";

        // Mock repository behavior
        Page<XMLData> expectedData = createMockData();
        when(xmlRepository.findByFilter(eq(filter), any(Pageable.class))).thenReturn(expectedData);

        // When
        Page<XMLData> result = xmlService.getXMLDataWithFilter(filter, page, size, sortBy, direction);

        // Then
        verify(xmlRepository, times(1)).findByFilter(eq(filter), any(Pageable.class));
        assertEquals(expectedData, result);
    }


    private Page<XMLData> createMockData() {
        List<XMLData> content = Arrays.asList(
                new XMLData(),
                new XMLData(),
                new XMLData()
        );
        return new PageImpl<>(content);
    }
}