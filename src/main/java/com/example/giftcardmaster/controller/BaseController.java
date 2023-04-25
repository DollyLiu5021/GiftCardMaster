package com.example.giftcardmaster.controller;

import com.example.giftcardmaster.exception.ReturnedException;
import com.example.giftcardmaster.exception.ServiceError;
import com.example.giftcardmaster.util.DataUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.BindException;

@Controller
public class BaseController {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    // exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public ReturnedException handleReturnedException(ReturnedException exception) {
        logger.warn("ReturnedException 400 log: {}\n{}", DataUtil.obj2JsonStr(exception),
                ExceptionUtils.getFullStackTrace(exception));
        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public ReturnedException handleValidationException(MethodArgumentNotValidException exception) {
        ReturnedException e = new ReturnedException(exception.getBindingResult());
        logger.warn("MethodArgumentNotValidException 400 log: {}\n{}", DataUtil.obj2JsonStr(e),
                ExceptionUtils.getFullStackTrace(exception));
        return e;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public ReturnedException handleValidationException(UnsatisfiedServletRequestParameterException exception) {
        ReturnedException e = new ReturnedException(ServiceError.COMMON_INPUT_INVALID, exception.getMessage());
        logger.warn("UnsatisfiedServletRequestParameterException 400 log: {}\n{}", DataUtil.obj2JsonStr(e),
                ExceptionUtils.getFullStackTrace(exception));
        return e;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public ReturnedException handleValidationException(MissingServletRequestParameterException exception) {
        ReturnedException e = new ReturnedException(ServiceError.COMMON_INPUT_INVALID, exception.getMessage());
        logger.warn("MissingServletRequestParameterException 400 log: {}\n{}", DataUtil.obj2JsonStr(e),
                ExceptionUtils.getFullStackTrace(exception));
        return e;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public ReturnedException handleBindException(BindException exception) {
        ReturnedException e = new ReturnedException(exception.getBindingResult());
        logger.warn("BindException 400 log: {}\n{}", DataUtil.obj2JsonStr(e),
                ExceptionUtils.getFullStackTrace(exception));
        return e;
    }

    @ExceptionHandler
    public void handleException(Exception exception) throws Exception {
        logger.error("Exception 500 log:\n{}",
                ExceptionUtils.getFullStackTrace(exception));
        throw exception;
    }

}
