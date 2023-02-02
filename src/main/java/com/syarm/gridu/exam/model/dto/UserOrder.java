package com.syarm.gridu.exam.model.dto;

import com.syarm.gridu.exam.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserOrder {
    private long userId;
    private String userName;
    private long orderId;
    private long orderDate;
    private List<OrderItem> orderItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserOrder userOrder = (UserOrder) o;
        return userId == userOrder.userId
                && orderId == userOrder.orderId
                && orderDate == userOrder.orderDate
                && userName.equals(userOrder.userName)
                && orderItems.size() == userOrder.orderItems.size()
                && new HashSet<>(orderItems).containsAll(userOrder.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, orderId, orderDate, orderItems);
    }
}
