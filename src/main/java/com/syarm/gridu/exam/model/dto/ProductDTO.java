package com.syarm.gridu.exam.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {
    private String productId;
    private String productCode;
    private String productName;
    private double score;
}
