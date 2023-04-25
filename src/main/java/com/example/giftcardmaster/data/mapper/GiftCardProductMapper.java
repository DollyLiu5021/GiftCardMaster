package com.example.giftcardmaster.data.mapper;

import com.example.giftcardmaster.data.dbentity.GiftCardProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GiftCardProductMapper {

    @Select("select * from giftcardproduct as gp " +
            "join giftcard as g on gp.giftcardid = g.id " +
            "where gp.active = 1 and gp.countrycode = #{countryCode}")
    List<GiftCardProductEntity> getAllGiftCardProducts(String countryCode);

    @Select("select * from giftcardproduct as gp " +
            "join giftcard as g on gp.giftcardid = g.id " +
            "where gp.active = 1 and gp.countrycode = #{countryCode} " +
            "order by gp.levelthreediscount desc")
    List<GiftCardProductEntity> getAllGiftCardProductsByDiscount(String countryCode);

    @Select("select * from giftcardproduct as gp " +
            "join giftcard as g on gp.giftcardid = g.id " +
            "where gp.active = 1 and gp.countrycode = #{countryCode} " +
            "order by gp.updateat desc")
    List<GiftCardProductEntity> getAllGiftCardProductsByUpdateTime(String countryCode);

    @Select("select * from giftcardproduct as gp " +
            "join giftcard as g on gp.giftcardid = g.id " +
            "where gp.id = #{id} and gp.active = 1")
    GiftCardProductEntity getGiftCardProductById(Long id);

    @Select("select * from giftcardproduct as gp " +
            "join giftcard as g on gp.giftcardid = g.id " +
            "where gp.giftcardid = #{giftCardId} and gp.active = 1 and gp.countrycode = #{countryCode}")
    List<GiftCardProductEntity> getGiftCardProductsByGiftCardId(@Param("giftCardId") Long giftCardId,
                                                                @Param("countryCode") String countryCode);

    @Select("<script> " +
            "select * from giftcardproduct as gp " +
            "join giftcard as g on gp.giftcardid = g.id " +
            "where gp.giftcardid in " +
            "<foreach item='item' index='index' collection='giftCardIdList' open='(' separator=',' close=')'> #{item} </foreach> " +
            "and gp.active = 1 and gp.countrycode = #{countryCode} " +
            "</script>")
    List<GiftCardProductEntity> getGiftCardProductsByGiftCardIds(@Param("giftCardIdList") List giftCardIdList,
                                                                 @Param("countryCode") String countryCode);

    @Select("select * from giftcardproduct as gp " +
            "join giftcard as g on gp.giftcardid = g.id" +
            "where g.giftcardname = #{giftCardName} and gp.active = 1 and gp.countrycode = #{countryCode}")
    GiftCardProductEntity getGiftCardProductByGiftCardName(@Param("giftCardName") String giftCardName,
                                                           @Param("countryCode") String countryCode);

    @Select("select * from giftcardproduct as gp " +
            "join giftcard as g on gp.giftcardid = g.id" +
            "where g.categoryid = #{categoryId} and gp.active = 1 and gp.countrycode = #{countryCode}")
    List<GiftCardProductEntity> getGiftCardProductByCategoryId(@Param("categoryId") Long categoryId,
                                                               @Param("countryCode") String countryCode);
}
