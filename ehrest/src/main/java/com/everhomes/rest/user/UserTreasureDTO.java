package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>status: 是否显示标识 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>count: 数量</li>
 *     <li>urlStatus: url是否可以点击跳转 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>url: 点击跳转链接</li>
 * </ul>
 */
public class UserTreasureDTO {

    private Long count;
    private Byte status;
    private String url;
    private Byte urlStatus;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Byte getUrlStatus() {
        return urlStatus;
    }

    public void setUrlStatus(Byte urlStatus) {
        this.urlStatus = urlStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
