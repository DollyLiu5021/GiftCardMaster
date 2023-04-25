package com.example.giftcardmaster.data.mapper;

import com.example.giftcardmaster.data.dbentity.GiftCardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GiftCardMapper {

    @Select("select * from giftcard as g " +
            "join giftcardcategory as gc where g.categoryid = gc.id and g.active = 1")
    List<GiftCardEntity> getAllGiftCards();

    @Select("select * from giftcard where giftcardname like '%' #{giftCardName} '%'")
    List<GiftCardEntity> fuzzySearchGiftCardsByName(String giftCardName);

    @Select("select * from giftcard as g " +
            "join giftcardcategory as gc where g.categoryid = gc.id and g.active = 1 " +
            "and gc.categoryname like '%' #{categoryName} '%'")
    List<GiftCardEntity> fuzzySearchGiftCardsByCategoryName(String categoryName);
}
