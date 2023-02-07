package com.syarm.gridu.exam.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {
    private String phoneNumber;
    private String orderNumber;
    private String productCode;
}
