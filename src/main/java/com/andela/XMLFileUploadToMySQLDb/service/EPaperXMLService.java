package com.andela.XMLFileUploadToMySQLDb.service;

import com.andela.XMLFileUploadToMySQLDb.dto.XMLDataFilterDTO;
import com.andela.XMLFileUploadToMySQLDb.model.DefaultSpecification;
import com.andela.XMLFileUploadToMySQLDb.model.SortOrders;
import com.andela.XMLFileUploadToMySQLDb.model.XMLValid;
import com.andela.XMLFileUploadToMySQLDb.entity.XMLData;
import com.andela.XMLFileUploadToMySQLDb.model.XMLDataSpecification;
import com.andela.XMLFileUploadToMySQLDb.repository.XMLRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Qualifier("epaper")
public class EPaperXMLService implements XMLService{

    private final XMLRepository xmlRepository;

    public EPaperXMLService(XMLRepository xmlRepository) {
        this.xmlRepository = xmlRepository;
    }
    @Override
    public XMLValid isXMLValid(MultipartFile xmlFile) {
        XMLValid xmlValid = new XMLValid();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile.getInputStream());
            xmlValid.setDocument(document);
            xmlValid.setIsValid(true);
        } catch (SAXException e) {
            log.error("The XML file is not valid: {}", e.getMessage());
            xmlValid.setIsValid(false);
            return xmlValid;
        } catch (ParserConfigurationException e) {
            log.error("Error configuring the parser: {}", e.getMessage());
            xmlValid.setIsValid(false);
            return xmlValid;
        } catch (IOException e) {
            log.error("Error reading the file: {}", e.getMessage());
            xmlValid.setIsValid(false);
            return xmlValid;
        }
        return xmlValid;
    }

    @Override
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

    @Override
    public Page<XMLData> findXMLDataByFilter(XMLDataFilterDTO xmlDataFilterDTO, int pageNumber, int pageSize) {
        // fetching all records based on filter
        List<Sort.Order> orders = new ArrayList<>();
        SortOrders sortOrders;
            sortOrders = xmlDataFilterDTO.getSortOrders();
        if (sortOrders != null && sortOrders.getSortBy() != null) {
            orders.add(new Sort.Order(sortOrders.isDescending() ? Sort.Direction.DESC : Sort.Direction.ASC, sortOrders.getSortBy()));
        }

        if (orders.isEmpty())
            orders.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orders));

        return findAll(xmlDataFilterDTO, pageable);
    }
    @Override
    public Page<XMLData> findAll(XMLDataFilterDTO xmlDataFilterDTO, Pageable pageable) {
        Specification<XMLData> specification = new XMLDataSpecification(xmlDataFilterDTO);

        if (xmlDataFilterDTO == null) {
            specification = new DefaultSpecification();
        }

        return xmlRepository.findAll(specification, pageable);
    }

}
