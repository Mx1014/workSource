// @formatter:off
package com.everhomes.util;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.common.NoParamActionData;
import com.everhomes.rest.launchpad.ActionType;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;

/**
 * ActionType-ActionData mapping router.
 * Created by vvlavida on 22/3/2017.
 */
public class Action2Router {

    private static final Logger LOGGER = LoggerFactory.getLogger(Action2Router.class);

    public static String action(byte actionType, String actionData) {
        return action(actionType, actionData, null);
    }

    public static String action(byte actionType, String actionData, String displayName) {
        String notFound = "zl://404";
        String route = mapping(actionType, actionData, displayName);
        if (null == route) return notFound;
        return route;
    }

    public static String action(ActionType actionType, Object actionData, String displayName) {
        return mapping(actionType, actionData, displayName);
    }

    private static String mapping(ActionType actionType, Object actionData, String displayName) {
        assert actionType != null;

        Route route = new Route(actionType.getRouter()).withParam("displayName", displayName);

        if (actionData == null || actionData instanceof NoParamActionData) {
            return route.build();
        }

        Class<?> clazz = actionData.getClass();
        if (clazz != actionType.getClz()) {
            return route.build();
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            if (pds != null && pds.length > 0) {
                for (PropertyDescriptor pd : pds) {
                    Method rm = pd.getReadMethod();
                    Object result = rm.invoke(actionData);
                    route.withParam(pd.getName(), result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("actionData to router error, class={}", clazz);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "actionData to router error");
        }
        return route.build();
    }

    private static <T> T fromJson(String json, Class<T> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new GsonJacksonDateAdapter());
        builder.registerTypeAdapter(Timestamp.class, new GsonJacksonTimestampAdapter());
        return builder.create().fromJson(json, clazz);
    }

    private static String mapping(byte actionType, String actionData, String displayName) {
        ActionType at = ActionType.fromCode(actionType);
        assert at != null;
        Object ad = fromJson(actionData, at.getClz());
        return mapping(at, ad, displayName);
    }

    private static class Route {
        private String path;
        private StringBuilder params;

        Route(String path) {
            this.path = path;
            this.params = new StringBuilder();
        }

        Route withParam(String key, Object param) {
            if (null == param) return this;
            try {
                this.params = params.append("&")
                        .append(key).append("=")
                        .append(URLEncoder.encode(String.valueOf(param), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return this;
        }

        public String build() {
            String route;
            if (null == params || params.length() == 0) {
                route = path;
            } else {
                route = path + params.toString().replaceFirst("&", "?");
            }
            return route;
        }
    }

    public static void main(String[] args) {
        // System.out.println(Action2Router.action(ActionType.ACTIVITY_DETAIL, new ActivityDetailActionData(1L, 2L), null));
    }
}
