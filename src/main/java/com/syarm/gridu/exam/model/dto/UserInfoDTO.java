package com.syarm.gridu.exam.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDTO {
    private String userName;
    private String phoneNumber;
    private String orderNumber;
    private String productCode;
    private String productName;
    private String productId;
}
