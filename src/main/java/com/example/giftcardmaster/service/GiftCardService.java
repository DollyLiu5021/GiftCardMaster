package com.example.giftcardmaster.service;

import com.example.giftcardmaster.data.dbentity.GiftCardEntity;
import com.example.giftcardmaster.data.mapper.GiftCardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GiftCardService {

    @Autowired
    GiftCardMapper giftCardMapper;

    public List<GiftCardEntity> getAllGiftCards(){
        return giftCardMapper.getAllGiftCards();
    }

    public Map<String, List<GiftCardEntity>> getGiftCardsGroupByCategory() {
        Map<String, List<GiftCardEntity>> categoryMap = new HashMap<>();
        for (GiftCardEntity gc : getAllGiftCards()) {
            if (categoryMap.containsKey(gc.getCategoryName())) {
                categoryMap.get(gc.getCategoryName()).add(gc);
            }
            else {
                List<GiftCardEntity> list = new ArrayList<>();
                list.add(gc);
                categoryMap.put(gc.getCategoryName(), list);
            }
        }
        return categoryMap;
    }

    public List<GiftCardEntity> fuzzySearchByName(String giftCardName) {
        return giftCardMapper.fuzzySearchGiftCardsByName(giftCardName.toLowerCase());
    }

    public List<GiftCardEntity> fuzzySearchByCategoryName(String categoryName) {
        return giftCardMapper.fuzzySearchGiftCardsByCategoryName(categoryName.toLowerCase());
    }
}
