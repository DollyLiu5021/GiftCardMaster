package com.example.giftcardmaster.util;

import com.example.giftcardmaster.exception.ReturnedException;
import com.example.giftcardmaster.exception.ServiceError;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataUtil {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    // --- Json Transfer ---

    public static String obj2JsonStr(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReturnedException(ServiceError.COMMON_JSON_ILLEGAL);
        }
    }

    public static <T> T jsonStr2Obj(String jsonStr, Class<T> objClass) {
        if (jsonStr == null || objClass == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonStr, objClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReturnedException(ServiceError.COMMON_JSON_ILLEGAL);
        }
    }

    public static <T> T jsonStr2Obj(String jsonStr, TypeReference<T> typeReference) {
        if (jsonStr == null || typeReference == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReturnedException(ServiceError.COMMON_JSON_ILLEGAL);
        }
    }

    public static Map jsonStr2Map(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReturnedException(ServiceError.COMMON_JSON_ILLEGAL);
        }
    }

    public static <T> ArrayList<T> jsonStr2List(String jsonStr, Class<T> elementClass) {
        if (jsonStr == null || elementClass == null) {
            return null;
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass);
            return objectMapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReturnedException(ServiceError.COMMON_JSON_ILLEGAL);
        }
    }

    // --- XML Transfer ---

    public static String obj2Xml(Object object, Class clazz) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Marshaller marshaller = jc.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter writer = new StringWriter();
            marshaller.marshal(object, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new ReturnedException(ServiceError.COMMON_XML_ILLEGAL);
        }
    }

    public static <T> T xml2Obj(String xmlStr, Class<T> clazz) {
        T t;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xmlStr));
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ReturnedException(ServiceError.COMMON_XML_ILLEGAL);
        }
    }

    // --- Map Transfer ---

    public static Map obj2Map(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }

}
