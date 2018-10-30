package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>province: 省份名</li>
 * <li>value：已激活门禁数</li>
 * </ul>
 */
public class ActiveDoorByPlaceDTO {
    private String name;
    private Integer value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
