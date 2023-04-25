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
public class GiftCardEntity {

    private long id;

    private String giftCardName;

    private String merchant;

    private long categoryId;

    private String categoryName;

    private String imgUrl;

    private int active;

    private Timestamp updateAt;

    private String updateBy;

    private Timestamp createAt;

    private String createBy;

    private long rowVersion;
}
