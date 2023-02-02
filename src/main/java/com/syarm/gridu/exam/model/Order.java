package com.syarm.gridu.exam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Order {
    private long id;
    private long date;
    private List<OrderItem> items;
}
