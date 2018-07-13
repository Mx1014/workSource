package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemDTO;
import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface PunchStatisticsParser {
    default List<PunchStatusStatisticsItemDTO> parseToPunchStatusStatisticsItems() {
        Field[] fields = this.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return null;
        }
        Map<Integer, PunchStatusStatisticsItemDTO> items = new TreeMap<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                PunchStatusStatisticsItem annotation = field.getAnnotation(PunchStatusStatisticsItem.class);
                if (annotation == null) {
                    continue;
                }
                if (!field.getType().isAssignableFrom(Integer.class)) {
                    throw new IllegalAccessException("Annotation 'PunchStatusStatisticsItem' only supports 'Integer' type");
                }
                Object value = field.get(this);
                PunchStatusStatisticsItemDTO item = new PunchStatusStatisticsItemDTO();
                item.setItemType(annotation.type().getCode());
                item.setNum(value != null ? (int) value : 0);
                items.put(annotation.defaultOrder(), item);
            }
        } catch (IllegalAccessException e) {

        }
        return new ArrayList<>(items.values());
    }

    default List<PunchExceptionRequestStatisticsItemDTO> parseToPunchExceptionRequestStatisticsItems() {
        Field[] fields = this.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return null;
        }
        Map<Integer, PunchExceptionRequestStatisticsItemDTO> items = new TreeMap<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                PunchExceptionRequestStatisticsItem annotation = field.getAnnotation(PunchExceptionRequestStatisticsItem.class);
                if (annotation == null) {
                    continue;
                }
                if (!field.getType().isAssignableFrom(Integer.class)) {
                    throw new IllegalAccessException("Annotation 'PunchExceptionRequestStatisticsItem' only supports 'Integer' type");
                }
                Object value = field.get(this);
                PunchExceptionRequestStatisticsItemDTO item = new PunchExceptionRequestStatisticsItemDTO();
                item.setItemType(annotation.type().getCode());
                item.setNum(value != null ? (int) value : 0);
                items.put(annotation.defaultOrder(), item);
            }
        } catch (IllegalAccessException e) {

        }
        return new ArrayList<>(items.values());
    }
}
