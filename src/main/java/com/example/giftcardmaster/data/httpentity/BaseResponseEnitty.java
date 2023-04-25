package com.example.giftcardmaster.data.httpentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseEnitty {

    private int code;

    private String msg;

    private Object data;
}
