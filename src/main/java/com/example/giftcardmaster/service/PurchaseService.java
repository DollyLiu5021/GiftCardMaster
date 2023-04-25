package com.example.giftcardmaster.service;

import com.example.giftcardmaster.data.httpentity.CitconPaymentEntity;
import com.example.giftcardmaster.util.DataUtil;
import com.example.giftcardmaster.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class PurchaseService {

    @Autowired
    OrderService orderService;

    public String citconPurchaseTransactionProcess(CitconPaymentEntity citconPaymentEntity) {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Authorization", "Bearer 9A7CB966A47C4D55AD1F8284ACC15903");

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("payment_method", citconPaymentEntity.getPaymentMethod());
        paramsMap.put("amount", String.valueOf(citconPaymentEntity.getAmount()));
        paramsMap.put("currency", citconPaymentEntity.getCurrency());
        paramsMap.put("reference", citconPaymentEntity.getReference());
        paramsMap.put("ipn_url", citconPaymentEntity.getIpnUrl());
        paramsMap.put("callback_url_success", citconPaymentEntity.getCallbackUrlSuccess());
        paramsMap.put("callback_url_fail", citconPaymentEntity.getCallbackUrlFail());
        paramsMap.put("mobile_result_url", citconPaymentEntity.getMobileResultUrl());
        paramsMap.put("allow_duplicates", citconPaymentEntity.getAllowDuplicates());

        String response = HttpUtil.sendPostWithHeader("https://uat.citconpay.com/chop/chop", paramsMap, headersMap);

        if (response.equals("error")) {
            return "error";
        }
        Map<String, Object> responseMap = DataUtil.jsonStr2Map(response);
        if (responseMap.get("result").equals("fail") || responseMap.get("result").equals("error")) {
            return "error";
        }

        try {
            return URLDecoder.decode((String) responseMap.get("url"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "error";
    }

    @Transactional
    public void citconGiftCardIpnHandler() {

    }
}
