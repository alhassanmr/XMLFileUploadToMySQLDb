package com.andela.XMLFileUploadToMySQLDb.dto;

import lombok.Data;
import org.w3c.dom.Document;

@Data
public class XMLValidDto {
    private Boolean isValid;
    private Document document;
}
