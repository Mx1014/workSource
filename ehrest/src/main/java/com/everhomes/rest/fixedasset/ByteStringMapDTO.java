package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>code: 编号</li>
 * <li>name: 名称</li>
 * </ul>
 */
public class ByteStringMapDTO {
    private Byte code;
    private String name;

    public ByteStringMapDTO() {

    }

    public ByteStringMapDTO(Byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
