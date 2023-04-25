package com.example.giftcardmaster.data.mapper;

import com.example.giftcardmaster.data.dbentity.OrderEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {

    @Select("select * from `order`")
    List<OrderEntity> getAllOrders();

    @Select("select * from `order` as o " +
            "join giftcardproduct as gp on o.productid = gp.id " +
            "join giftcard as g on gp.giftcardid = g.id " +
            "join orderstatus as os on os.id = o.orderstatusid " +
            "where o.userid = #{userId} and o.producttypeid = 1 " +
            "order by purchasetime DESC")
    List<OrderEntity> getGiftCardOrdersByUserId(Long userId);

    @Select("select * from `order` " +
            "where producttypeid = #{productTypeId} and idtoken = #{idToken}")
    OrderEntity getOrderByTypeAndIdToken(@Param("productTypeId") Integer productTypeId,
                                         @Param("idToken") String idToken);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into `order`(idtoken, producttypeid, userid, productid, originprice, discount, purchaseprice, purchasetime, orderstatusid, paymentmethod, updateby, createby) " +
            "values(#{idToken}, #{productTypeId}, #{userId}, #{productId}, #{originPrice}, #{discount}, #{purchasePrice}, #{purchaseTime}, #{orderStatusId}, #{paymentMethod}, #{updateBy}, #{createBy})")
    void createOrder(OrderEntity orderEntity);

    @Update("update `order` set orderstatusid = #{statusId} where id = #{id}")
    void updateOrderStatus(@Param("id") Long id, @Param("statusId") Integer statusId);
}
