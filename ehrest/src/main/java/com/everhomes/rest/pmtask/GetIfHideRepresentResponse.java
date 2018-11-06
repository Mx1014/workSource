package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>ifHide: 是否隐藏代发 0 不隐藏 1 隐藏</li>
 * <li>ifAppHide: app代发是否隐藏代发 0 不隐藏 1 隐藏</li>
 * <li>ifBgHide: 后台是否隐藏代发 0 不隐藏 1 隐藏</li>
 * </ul>
 */
public class GetIfHideRepresentResponse {

    private Integer ifHide;

    private Integer ifAppHide;

    private Integer ifBgHide;

    public Integer getIfHide() {
        return ifHide;
    }

    public void setIfHide(Integer ifHide) {
        this.ifHide = ifHide;
    }

    public Integer getIfAppHide() {
        return ifAppHide;
    }

    public void setIfAppHide(Integer ifAppHide) {
        this.ifAppHide = ifAppHide;
    }

    public Integer getIfBgHide() {
        return ifBgHide;
    }

    public void setIfBgHide(Integer ifBgHide) {
        this.ifBgHide = ifBgHide;
    }
}
