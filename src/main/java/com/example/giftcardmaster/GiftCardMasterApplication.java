package com.example.giftcardmaster;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@SpringBootApplication
public class GiftCardMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiftCardMasterApplication.class, args);
    }

}
