// @formatter:off
package com.everhomes.util;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.common.NoParamActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.launchpad.ActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

/**
 * ActionType-ActionData mapping router.
 * Created by vvlavida on 22/3/2017.
 */
public class RouterBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouterBuilder.class);

    public static String build(Router router, Object actionData) {
        return build(router, actionData, null);
    }

    public static String build(Router router, Object actionData, String displayName) {
        String notFound = "zl://404";
        String route = mapping(router, actionData, displayName);
        if (null == route) return notFound;
        return route;
    }

    public static String build(ActionType actionType, Object actionData) {
        return build(actionType, actionData, null);
    }

    public static String build(ActionType actionType, Object actionData, String displayName) {
        Router router = Router.fromActionType(actionType);
        return build(router, actionData, displayName);
    }

    private static String mapping(Router router, Object actionData, String displayName) {
        assert router != null;

        Route route = new Route(router.getRouter()).withParam("displayName", displayName);

        if (actionData == null || actionData instanceof NoParamActionData) {
            return route.build();
        }

        Class<?> clazz = actionData.getClass();
        if (clazz != router.getClz()) {
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

    /*private static <T> T fromJson(String json, Class<T> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new GsonJacksonDateAdapter());
        builder.registerTypeAdapter(Timestamp.class, new GsonJacksonTimestampAdapter());
        return builder.create().fromJson(json, clazz);
    }*/

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
}
