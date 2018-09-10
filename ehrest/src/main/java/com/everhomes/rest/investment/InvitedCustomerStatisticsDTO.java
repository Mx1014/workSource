package com.everhomes.rest.investment;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>key: 客户状态</li>
 *     <li>value: 统计组数</li>
 *     <li>itemId: 用于搜索</li>
 * </ul>
 */
public class InvitedCustomerStatisticsDTO {
    private String key;
    private String value;
    private Long itemId;

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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
