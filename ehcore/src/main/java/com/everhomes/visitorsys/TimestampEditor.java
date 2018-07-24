// @formatter:off
package com.everhomes.visitorsys;

import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.sql.Timestamp;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/12 18:30
 */
@Component
public class TimestampEditor extends PropertiesEditor {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null) {
            setValue(new Timestamp(Long.parseLong(text)));
        }
    }
}
