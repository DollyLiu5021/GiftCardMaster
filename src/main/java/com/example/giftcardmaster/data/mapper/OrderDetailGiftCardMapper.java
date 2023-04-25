package com.example.giftcardmaster.data.mapper;

import com.example.giftcardmaster.data.dbentity.OrderDetailGiftCardEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderDetailGiftCardMapper {

    @Select("select * from orderdetailgiftcard")
    List<OrderDetailGiftCardEntity> getAllOrderDetailGiftCards();

    @Select("select * from orderdetailgiftcard where id = #{id}")
    OrderDetailGiftCardEntity getOrderDetailGiftCardById(Long id);

    @Select("select * from orderdetailgiftcard where orderid = #{orderId}")
    OrderDetailGiftCardEntity getOrderDetailGiftCardByOrderId(Long orderId);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into `orderdetailgiftcard`(orderid, updateby, createby) " +
            "values(#{orderId}, #{updateBy}, #{createBy})")
    void createOrderDetailGiftCard(OrderDetailGiftCardEntity orderDetailGiftCardEntity);

    @Update("update orderdetailgiftcard set giftcardnumber = #{giftCardNumber}, pin = #{pin} " +
            "where orderid = #{orderId}")
    void updateOrderDetailGiftCardWithGiftCardInfo(@Param("giftCardNumber") String giftCardNumber,
                                                   @Param("pin") String pin,
                                                   @Param("orderId") Long orderId);
}
