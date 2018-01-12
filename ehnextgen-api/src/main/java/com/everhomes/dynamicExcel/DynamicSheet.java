//@formatter:off
package com.everhomes.dynamicExcel;

/**
 * Created by Wentian Wang on 2018/1/11.
 */

import javax.validation.constraints.NotNull;
import java.util.Map;


public class DynamicSheet {
    /**
     * className ： sheet对应的java class的名
     */
    @NotNull
    private String className;

    private Object storage;
    /**
     * displayName : sheet在excel上的展示名称
     */
    @NotNull
    private String displayName;

    private Map<String, DynamicField> dynamicFields;

    public Map<String, DynamicField> getDynamicFields() {
        return dynamicFields;
    }

    public void setDynamicFields(Map<String, DynamicField> dynamicFields) {
        this.dynamicFields = dynamicFields;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Object getStorage() {
        return storage;
    }

    public void setStorage(Object storage) {
        this.storage = storage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
