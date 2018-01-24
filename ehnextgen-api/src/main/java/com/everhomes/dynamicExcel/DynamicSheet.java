//@formatter:off
package com.everhomes.dynamicExcel;

/**
 * Created by Wentian Wang on 2018/1/11.
 */

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


public class DynamicSheet {
    /**
     * className ： sheet对应的java class的名
     */
    @NotNull
    private String className;
    /**
     * displayName : sheet在excel上的展示名称
     */
    @NotNull
    private String displayName;

    private List<DynamicField> dynamicFields;

    public List<DynamicField> getDynamicFields() {
        return dynamicFields;
    }

    public void setDynamicFields(List<DynamicField> dynamicFields) {
        this.dynamicFields = dynamicFields;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
