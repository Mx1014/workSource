package com.everhomes.util;

import org.jooq.Record;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RecordHelper {

    private final static Map<Class, Tuple<PropertyDescriptor, Method>[]> PDCACHEMAPS = new HashMap<>();
    private final static ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    public static <T> T convert(Record record, Class<T> clazz) {
        if (record == null) {
            return null;
        }

        try {
            T t = clazz.newInstance();
            Tuple<PropertyDescriptor, Method>[] clazzInfos = getClazzInfoFromCaches(clazz);
            if (clazzInfos != null && clazzInfos.length > 0) {
                for (Tuple<PropertyDescriptor, Method> info : clazzInfos) {
                    PropertyDescriptor pd = info.first();
                    Object value = getValue(record, pd.getName(), pd.getPropertyType());
                    if (value != null) {
                        info.second().invoke(t, value);
                    }
                }
            }
            return t;
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Tuple<PropertyDescriptor, Method>[] getClazzInfoFromCaches(Class clazz) {
        Tuple<PropertyDescriptor, Method>[] pdCaches;
        try {
            LOCK.readLock().lock();
            pdCaches = PDCACHEMAPS.get(clazz);
            if (pdCaches != null) {
                return pdCaches;
            }
        } finally {
            LOCK.readLock().unlock();
        }

        try {
            LOCK.writeLock().lock();
            pdCaches = PDCACHEMAPS.get(clazz);
            if (pdCaches != null) {
                return pdCaches;
            }
            pdCaches = getClazzInfos(clazz);
            PDCACHEMAPS.put(clazz, pdCaches);
        } finally {
            LOCK.writeLock().unlock();
        }
        return pdCaches;
    }

    @SuppressWarnings("unchecked")
    private static Tuple<PropertyDescriptor, Method>[] getClazzInfos(Class clazz) {
        Tuple[] pdCacheArr = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            if (pds != null && pds.length > 0) {
                pdCacheArr = new Tuple[pds.length];
                for (int i = 0; i < pds.length; i++) {
                    pdCacheArr[i] = new Tuple<>(pds[i], pds[i].getWriteMethod());
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return pdCacheArr;
    }

    private static <T> T getValue(Record record, String fieldName, Class<T> clazz) {
        String column = camelToUnderline(fieldName);
        try {
            return record.getValue(column, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    private static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
