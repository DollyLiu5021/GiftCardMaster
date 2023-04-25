package com.example.giftcardmaster.service;

import com.example.giftcardmaster.data.dbentity.OrderEntity;
import com.example.giftcardmaster.data.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    public List<OrderEntity> getByUserId(Long userId) {
        return orderMapper.getGiftCardOrdersByUserId(userId);
    }

    public void createOrderEntity(OrderEntity orderEntity) {
        orderMapper.createOrder(orderEntity);
    }

    public void updateOrderEntityStatus(Long id, Integer statusId) {
        orderMapper.updateOrderStatus(id, statusId);
    }
}
