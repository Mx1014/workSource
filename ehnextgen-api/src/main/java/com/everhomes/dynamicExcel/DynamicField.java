//@formatter:off
package com.everhomes.dynamicExcel;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/11.
 */

public class DynamicField {
    @NotNull
    /**
     * displayName: 字段在excel上展示的名字
     */
    private String displayName;
    private String defaultValue = "";
    private boolean isMandatory = false;
    private List<String> allowedValued;
    //字段的日期格式
    private String dateFormat = "yyyy-MM-dd";

    public String getDateFormat() {
        return dateFormat;
    }

    public List<String> getAllowedValued() {
        return allowedValued;
    }

    public void setAllowedValued(List<String> allowedValued) {
        this.allowedValued = allowedValued;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
