package com.su.scott.slibrary.util;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
    private static Gson gson = new Gson();

    /**
     * 获取简单json格式的某个值
     *
     * @param json
     * @param key
     * @return
     */
    public static String getSimpleString(String json, String key) {
        String res = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            res = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * json、bean转换
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * json、beanList转换
     *
     * @param jsonArr
     * @param classOfT
     * @return
     */
    public static <T> ArrayList<T> jsonToList(String jsonArr, Class<T> classOfT) {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjs = new Gson().fromJson(jsonArr, type);
            ArrayList<T> listOfT = new ArrayList<T>();
            for (JsonObject jsonObj : jsonObjs) {
                listOfT.add(new Gson().fromJson(jsonObj, classOfT));
            }
            return listOfT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
