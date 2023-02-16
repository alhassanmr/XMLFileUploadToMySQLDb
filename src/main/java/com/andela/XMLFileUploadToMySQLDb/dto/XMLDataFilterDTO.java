package com.andela.XMLFileUploadToMySQLDb.dto;

import com.andela.XMLFileUploadToMySQLDb.model.SortOrders;
import lombok.Data;

@Data
public class XMLDataFilterDTO {
    private String newspaperName;
    private String screenWidth;
    private String screenHeight;
    private String screenDpi;
    private String filename;
    private String sortField;
    private SortOrders sortOrders;
}
