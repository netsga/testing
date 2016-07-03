package com.lge.hems.device.utilities.customize;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lge.hems.device.model.controller.response.Response;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Created by netsga on 2016. 5. 25..
 */
@Component
public class JsonConverter {
    @LoggerImpl
    private Logger logger;

    private Gson gson;
    private JsonParser parser = new JsonParser();

    @PostConstruct
    private void init() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Response.class, new InterfaceAdapter<>()).setExclusionStrategies(new GsonExclusionStrategy(GsonExclude.class));
        gson = builder.create();
    }

    public JsonElement toJson(String str) {
        return parser.parse(str);
    }

    public String toJson(Object src) {
        return gson.toJson(src);
    }

    public String toJson(Object src, Type typeOfSrc) {
        return gson.toJson(src, typeOfSrc);
    }

    public void toJson(Object src, Appendable writer) throws JsonIOException {
        gson.toJson(src, writer);
    }

    public void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
        gson.toJson(src, typeOfSrc, writer);
    }

    public void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
        gson.toJson(src, typeOfSrc, writer);
    }

    public String toJson(JsonElement jsonElement) {
        return gson.toJson(jsonElement);
    }

    public void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
        gson.toJson(jsonElement, writer);
    }

    public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
        gson.toJson(jsonElement, writer);
    }

    public <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }

    public <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        return gson.fromJson(json, classOfT);
    }

    public <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }

    public <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, typeOfT);
    }

    public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }

    public JsonElement toJsonTree(Object src, Type typeOfSrc) {
        return gson.toJsonTree(src, typeOfSrc);
    }

    public JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }

    public Gson getGson() {
        return gson;
    }
}
