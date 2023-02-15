package com.andela.XMLFileUploadToMySQLDb.dto;

import lombok.Data;

@Data
public class XMLDataFilterDTO {
    private String newspaperName;
    private String screenWidth;
    private String screenHeight;
    private String screenDpi;
    private String filename;
    private Integer pageNumber;
    private Integer pageSize;
    private String sortField;
    private String sortOrder;
}
