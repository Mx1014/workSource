// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: 更新白名单的ID[必填]</li>
 *     <li>phoneNumber: 白名单手机号码[选填]</li>
 * </ul>
 */
public class UpdateWhiteListCommand {

    @NotNull
    private Long id;

    private String phoneNumber;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
