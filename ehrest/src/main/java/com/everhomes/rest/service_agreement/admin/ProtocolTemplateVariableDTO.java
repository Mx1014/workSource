// @formatter:off
package com.everhomes.rest.service_agreement.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>name: 变量名称</li>
 *     <li>type: 变量类型，1：固定变量，2：自定义变量</li>
 * </ul>
 */
public class ProtocolTemplateVariableDTO {

    private String name;

    private Byte type;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
