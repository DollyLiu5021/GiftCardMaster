package com.example.giftcardmaster.service;

import com.example.giftcardmaster.data.dbentity.GiftCardProductEntity;
import com.example.giftcardmaster.data.dbentity.UserEntity;
import com.example.giftcardmaster.data.mapper.GiftCardProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftCardProductService {

    @Autowired
    GiftCardProductMapper giftCardProductMapper;

    public List<GiftCardProductEntity> getAllProducts(String countryCode) {
        return giftCardProductMapper.getAllGiftCardProducts(countryCode);
    }

    public List<GiftCardProductEntity> getAllProductsByDiscount(String countryCode) {
        return giftCardProductMapper.getAllGiftCardProductsByDiscount(countryCode);
    }

    public List<GiftCardProductEntity> getAllProductsByUpdateTime(String countryCode) {
        return giftCardProductMapper.getAllGiftCardProductsByUpdateTime(countryCode);
    }

    public GiftCardProductEntity getProductById(Long productId) {
        return giftCardProductMapper.getGiftCardProductById(productId);
    }

    public List<GiftCardProductEntity> getByGiftCardId(Long giftCardId, String countryCode) {
        return giftCardProductMapper.getGiftCardProductsByGiftCardId(giftCardId, countryCode);
    }

    public List<GiftCardProductEntity> getByGiftCardIds(List<Long> giftCardIdList, String countryCode) {
        return giftCardProductMapper.getGiftCardProductsByGiftCardIds(giftCardIdList, countryCode);
    }

    public int getUserDiscount(UserEntity user, GiftCardProductEntity productEntity) {
        int userDiscount;
        switch (user.getUserRoleId()) {
            case 1:
                userDiscount = productEntity.getLevelOneDiscount();
                break;
            case 2:
                userDiscount = productEntity.getLevelTwoDiscount();
                break;
            case 3:
                userDiscount = productEntity.getLevelThreeDiscount();
                break;
            default:
                userDiscount = 0;
        }
        return userDiscount;
    }
}
