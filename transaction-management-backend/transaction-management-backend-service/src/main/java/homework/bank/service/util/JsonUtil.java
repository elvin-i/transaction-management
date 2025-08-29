package homework.bank.service.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static<T> T parseFromObject(String str,Class<T> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        Gson gson = builder.create();
        return gson.fromJson(str,clazz);
    }

    public static<T> List<T> parseArray(String object, Class<T> clazz) {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        try {
             return mapper.readValue(object, javaType);
        } catch (IOException e) {
            log.info("parseArray 解析失败!");
            e.printStackTrace();
            return null;
        }
    }

    public static String toJSONString(Object obj) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        return gson.toJson(obj);
    }

}
