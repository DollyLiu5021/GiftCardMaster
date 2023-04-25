package com.example.giftcardmaster.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class ReturnedException extends RuntimeException {

    private int code;

    private String message;

    public ReturnedException(BindingResult result) {
        code = ServiceError.COMMON_INPUT_INVALID.code;
        message = ServiceError.COMMON_INPUT_INVALID.message;

        if (result.hasErrors()) {
            ObjectError objectError = result.getAllErrors().get(0);
            message = objectError.getDefaultMessage();
            if (objectError instanceof FieldError) {
                if (StringUtils.isEmpty(message)) {
                    message = ((FieldError) objectError).getField() + " : " + message;
                }
            }
        }
    }

    public ReturnedException(String prefixStr, ServiceError error) {
        code = error.code;
        message = prefixStr + error.message;
    }

    public ReturnedException(ServiceError error, String suffixStr) {
        code = error.code;
        message = error.message + suffixStr;
    }


    public ReturnedException(ServiceError error) {
        code = error.code;
        message = error.message;
    }

    public ReturnedException(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
