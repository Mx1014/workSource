package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>item: 变更项</li>
 * <li>value: 变更值</li>
 * </ul>
 */
public class FixedAssetOperationItemDTO {
    private String item;
    private String value;

    public FixedAssetOperationItemDTO() {

    }

    public FixedAssetOperationItemDTO(String item, String value) {
        this.item = item;
        this.value = (value != null && value.length() > 0) ? value : "-";
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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
