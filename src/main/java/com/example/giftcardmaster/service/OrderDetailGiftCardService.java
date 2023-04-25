package com.example.giftcardmaster.service;

import com.example.giftcardmaster.data.dbentity.OrderDetailGiftCardEntity;
import com.example.giftcardmaster.data.mapper.OrderDetailGiftCardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailGiftCardService {

    @Autowired
    OrderDetailGiftCardMapper orderDetailGiftCardMapper;

    public void createOrderDetail(OrderDetailGiftCardEntity orderDetailGiftCardEntity) {
        orderDetailGiftCardMapper.createOrderDetailGiftCard(orderDetailGiftCardEntity);
    }
}
