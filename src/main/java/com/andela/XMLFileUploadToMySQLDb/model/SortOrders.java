package com.andela.XMLFileUploadToMySQLDb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortOrders {
    boolean isDescending;
    String sortBy;
}
