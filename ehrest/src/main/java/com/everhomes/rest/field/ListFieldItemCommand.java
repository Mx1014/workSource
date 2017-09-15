package com.everhomes.rest.field;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>fieldId: 在系统里的字段id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ListFieldItemCommand {

    private Integer namespaceId;

    private Long fieldId;

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
