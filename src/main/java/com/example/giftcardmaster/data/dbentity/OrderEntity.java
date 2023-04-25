package com.example.giftcardmaster.data.dbentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    private long id;

    private String idToken;

    private int productTypeId;

    private long userId;

    private long productId;

    private BigDecimal originPrice;

    private int discount;

    private BigDecimal purchasePrice;

    private int orderStatusId;

    private String paymentMethod;

    private Timestamp purchaseTime;

    private String productName;

    private String imgUrl;

    private String status;

    private Timestamp updateAt;

    private String updateBy;

    private Timestamp createAt;

    private String createBy;

    private long rowVersion;
}
