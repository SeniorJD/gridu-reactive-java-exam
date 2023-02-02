package com.syarm.gridu.exam.repository;

import com.syarm.gridu.exam.model.Order;
import com.syarm.gridu.exam.model.OrderItem;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class OrderRepository {
    public static final int ORDERS_COUNT = 10;
    private final Map<Long, Order> orderMap = new HashMap<>();

    public OrderRepository() {
    }

    public Order findById(long orderId) {
        return orderMap.get(orderId);
    }

    @PostConstruct
    public void fillMap() {
        for (long i = 0; i < ORDERS_COUNT; i++) {
            orderMap.put(i, generateOrder(i));
        }
    }

    private Order generateOrder(long id) {
        var items = new ArrayList<OrderItem>();
        var itemId = id * 10;
        for (int i = 0; i <= id; i++) {
            items.add(generateOrderItem(itemId++));
        }

        return new Order(
                id,
                System.currentTimeMillis() - id * TimeUnit.DAYS.toMillis(id % 1000),
                items
        );
    }

    private OrderItem generateOrderItem(long orderItemId) {
        var item = new OrderItem();

        item.setItemId(orderItemId);
        item.setAmount((int) (orderItemId * 3));
        item.setProductId((int) (100 - orderItemId));
        item.setPrice(100000. / (orderItemId + 10));

        return item;
    }
}
