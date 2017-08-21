package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>fieldGroups: (List)所有字段组对象</li>
 * <li>fields: (List)所有字段对象</li>
 * </ul>
 */
public class GetArchivesFieldResponse {

    @ItemType(String.class)
    private List<String> fieldGroups;

    @ItemType(String.class)
    private List<String> fields;

    public GetArchivesFieldResponse() {
    }

    public List<String> getFieldGroups() {
        return fieldGroups;
    }

    public void setFieldGroups(List<String> fieldGroups) {
        this.fieldGroups = fieldGroups;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
