package com.everhomes.rest.portal;

/**
 * Created by Administrator on 2018/3/12.
 */
public class LeaseProjectInstanceConfig {
    private Byte categoryId;
    private Byte hideAddressFlag;

    public Byte getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Byte categoryId) {
        this.categoryId = categoryId;
    }

    public Byte getHideAddressFlag() {
        return hideAddressFlag;
    }

    public void setHideAddressFlag(Byte hideAddressFlag) {
        this.hideAddressFlag = hideAddressFlag;
    }
}
