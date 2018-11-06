package com.everhomes.techpark.punch.recordmapper;

import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemDTO;
import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemDTO;
import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.sun.xml.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface PunchStatisticsParser {
    String PUNCH_STATUS_STATISTICS_ITEM_NAME_SCOPE = "PunchStatusStatisticsItemName";
    String PUNCH_EXCEPTION_REQUEST_STATISTICS_ITEM_NAME_SCOPE = "PunchExceptionRequestStatisticsItemName";

    default List<PunchStatusStatisticsItemDTO> parseToPunchStatusStatisticsItems(LocaleStringService localeStringService, String locale) {
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
                String itemName = localeStringService.getLocalizedString(PUNCH_STATUS_STATISTICS_ITEM_NAME_SCOPE, String.valueOf(annotation.type().getCode()), locale, annotation.type().toString());
                PunchStatusStatisticsItemDTO item = new PunchStatusStatisticsItemDTO();
                item.setItemName(itemName);
                item.setItemType(annotation.type().getCode());
                item.setNum(value != null ? (int) value : 0);
                item.setUnit(annotation.type().getUnit());
                items.put(annotation.defaultOrder(), item);
            }
        } catch (IllegalAccessException e) {

        }
        return new ArrayList<>(items.values());
    }

    default List<PunchExceptionRequestStatisticsItemDTO> parseToPunchExceptionRequestStatisticsItems(LocaleStringService localeStringService, String locale) {
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
                String itemName = localeStringService.getLocalizedString(PUNCH_EXCEPTION_REQUEST_STATISTICS_ITEM_NAME_SCOPE, String.valueOf(annotation.type().getCode()), locale, annotation.type().toString());
                PunchExceptionRequestStatisticsItemDTO item = new PunchExceptionRequestStatisticsItemDTO();
                item.setItemName(itemName);
                item.setItemType(annotation.type().getCode());
                item.setNum(value != null ? (int) value : 0);
                item.setUnit("次");
                items.put(annotation.defaultOrder(), item);
            }
        } catch (IllegalAccessException e) {

        }
        return new ArrayList<>(items.values());
    }
}
