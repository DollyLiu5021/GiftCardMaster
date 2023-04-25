package com.example.giftcardmaster.data.dbentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiftCardProductEntity {

    private long id;

    private long giftCardId;

    private String giftCardName;

    private String productName;

    private String imgUrl;

    private String countryCode;

    private int active;

    private int levelOneDiscount;

    private int levelTwoDiscount;

    private int levelThreeDiscount;

    private Timestamp updateAt;

    private String updateBy;

    private Timestamp createAt;

    private String createBy;

    private long rowVersion;
}
