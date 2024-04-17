package org.example.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsManufacturers {
    private String itemId;
    private String itemDescription;
    private Double itemPrice;
    private String ManufacturerId;
    private String ManufacturerName;
    private String ManufacturerAddress;
}
