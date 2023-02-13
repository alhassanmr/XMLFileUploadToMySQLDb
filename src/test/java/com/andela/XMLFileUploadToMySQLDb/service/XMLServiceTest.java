package com.andela.XMLFileUploadToMySQLDb.service;

import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.repository.XMLRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

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
    @InjectMocks
    private XMLService xmlService;
    @Mock
    private XMLRepository xmlRepository;
    @Mock
    private Document document;
    @Mock
    private NodeList nodeList;
    @Mock
    private Node node;
    @Mock
    private Element element;

    int page = 0;
    int size = 10;
    String sortBy = "createdAt";
    Sort.Direction direction = Sort.Direction.ASC;

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
        Document document = mock(Document.class);
        NodeList nList = mock(NodeList.class);
        Node nNode = mock(Node.class);
        Element eElement = mock(Element.class);

        when(document.getElementsByTagName("deviceInfo")).thenReturn(nList);
        when(nList.getLength()).thenReturn(1);
        when(nList.item(0)).thenReturn(nNode);
        when(nNode.getNodeType()).thenReturn(Node.ELEMENT_NODE);

        when(eElement.getElementsByTagName("newspaperName").item(0).getTextContent()).thenReturn("The Times");
        when(eElement.getElementsByTagName("screenInfo").item(0).getAttributes().getNamedItem("width").getNodeValue()).thenReturn("800");
        when(eElement.getElementsByTagName("screenInfo").item(0).getAttributes().getNamedItem("height").getNodeValue()).thenReturn("600");
        when(eElement.getElementsByTagName("screenInfo").item(0).getAttributes().getNamedItem("dpi").getNodeValue()).thenReturn("96");

        XMLRepository xmlRepository = mock(XMLRepository.class);

        xmlService.parseXMLData(document, "test.xml");

        XMLData xmlData = new XMLData();
        xmlData.setNewspaperName("The Times");
        xmlData.setScreenWidth("800");
        xmlData.setScreenHeight("600");
        xmlData.setScreenDpi("96");
        xmlData.setFilename("test.xml");

        verify(xmlRepository).save(xmlData);
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
        PageImpl<XMLData> page = new PageImpl<>(content);
        return page;
    }
}