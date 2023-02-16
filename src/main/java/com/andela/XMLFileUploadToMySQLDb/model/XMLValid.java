package com.andela.XMLFileUploadToMySQLDb.model;

import lombok.Data;
import org.w3c.dom.Document;

@Data
public class XMLValid {
    private Boolean isValid;
    private Document document;
}
