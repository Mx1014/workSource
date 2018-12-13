//@formatter:off
package com.everhomes.util;

import com.everhomes.rest.asset.BaseVo;
import com.everhomes.rest.asset.utils.EDSGsonFilter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class GsonUtil {
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping()
            .create();
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    @Deprecated
    public static <T> BaseVo<T> fromJsonT(String json, Class<T> clazz) {
        return gson.fromJson(json, new TypeToken<BaseVo<T>>(){}.getType());
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static String toJson(Object obj,EDSGsonFilter filter){
        Gson skipGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping()
                .setExclusionStrategies(filter).create();
        return skipGson.toJson(obj);
    }
}
