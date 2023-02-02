package com.syarm.gridu.exam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {
    private long itemId;
    private long productId;
    private String productName;
    private int amount;
    private double price;
}
