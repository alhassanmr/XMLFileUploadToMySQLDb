package com.andela.XMLFileUploadToMySQLDb.service;

import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.repository.XMLRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Timestamp;

@Slf4j
@Service
public class XMLService {

    private final XMLRepository xmlRepository;

    public XMLService(XMLRepository xmlRepository) {
        this.xmlRepository = xmlRepository;
    }

    public Object isXMLValid(MultipartFile xmlFile) {
        Document document;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(xmlFile.getInputStream());
        } catch (SAXException e) {
            log.error("The XML file is not valid: " + e.getMessage());
            return false;
        } catch (ParserConfigurationException e) {
            log.error("Error configuring the parser: " + e.getMessage());
            return false;
        } catch (IOException e) {
            log.error("Error reading the file: " + e.getMessage());
            return false;
        }
        return document;
    }
    public void parseXMLData(Document document, String filename) {
        NodeList nList = document.getElementsByTagName("deviceInfo");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String newspaperName = eElement.getElementsByTagName("newspaperName").item(0).getTextContent();
                String width = eElement.getElementsByTagName("screenInfo").item(0).getAttributes().getNamedItem("width").getNodeValue();
                String height = eElement.getElementsByTagName("screenInfo").item(0).getAttributes().getNamedItem("height").getNodeValue();
                String dpi = eElement.getElementsByTagName("screenInfo").item(0).getAttributes().getNamedItem("dpi").getNodeValue();

                log.info("newspaperName: {},width: {},height: {}, dpi: {}",newspaperName, width, height, dpi);

                // Store the data in the database
                XMLData xmlData = new XMLData();
                xmlData.setNewspaperName(newspaperName);
                xmlData.setScreenWidth(width);
                xmlData.setScreenHeight(height);
                xmlData.setScreenDpi(dpi);
                xmlData.setFilename(filename);
                xmlData.setUploadTime(new Timestamp(System.currentTimeMillis()));
                xmlRepository.save(xmlData);
            }
        }
    }
    public Page<XMLData> getXMLDataWithFilter(String filter, int page, int size, String sortBy, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        if (filter == null || filter.isEmpty()) {
            return xmlRepository.findAll(pageable);
        }
        return xmlRepository.findByFilter(filter, pageable);
    }
}
