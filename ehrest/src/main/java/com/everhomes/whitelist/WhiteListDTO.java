// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 白名单ID</li>
 *     <li>phoneNumber: 白名单手机号码</li>
 *     <li>createTime: 创建时间</li>
 * </ul>
 */
public class WhiteListDTO {

    private Long id;

    private String phoneNumber;

    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
