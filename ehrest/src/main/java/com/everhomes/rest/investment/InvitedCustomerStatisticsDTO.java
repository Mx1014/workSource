package com.everhomes.rest.investment;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>key: 客户状态</li>
 *     <li>value: 统计组数</li>
 * </ul>
 */
public class InvitedCustomerStatisticsDTO {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
