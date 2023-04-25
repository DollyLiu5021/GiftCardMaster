package com.example.giftcardmaster.data.httpentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CitconPaymentEntity {

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("reference")
    private String reference;

    @JsonProperty("ipn_url")
    private String ipnUrl;

    @JsonProperty("callback_url_success")
    private String callbackUrlSuccess;

    @JsonProperty("callback_url_fail")
    private String callbackUrlFail;

    @JsonProperty("mobile_result_url")
    private String mobileResultUrl;

    @JsonProperty("allow_duplicates")
    private String allowDuplicates;
}
