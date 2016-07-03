package com.lge.hems.device.utilities.customize;

import com.google.gson.*;

import java.lang.reflect.Type;


/**
 * Created by netsga on 2016. 5. 25..
 */
final public class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String DATA = "DATA";

    @Override
    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();
        Class klass = getObjectClass(className);
        return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
    }

    @Override
    public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
//        JsonObject jsonObject = new JsonObject();
//        System.out.println(type);
//        jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
//        jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
        return jsonSerializationContext.serialize(jsonElement, jsonElement.getClass());
    }
    /****** Helper method to get the className of the object to be deserialized *****/
    public Class getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
    }
}