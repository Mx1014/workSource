// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>oldIdentifier: 旧手机号</li>
 *     <li>oldRegionCode: oldRegionCode</li>
 *     <li>newIdentifier: 新手机号</li>
 *     <li>newRegionCode: newRegionCode</li>
 *     <li>name: 名称</li>
 *     <li>email: 邮箱</li>
 *     <li>remarks: 备注</li>
 * </ul>
 */
public class UserAppealLogDTO {

    private Long id;
    private String oldIdentifier;
    private String oldRegionCode;
    private String newIdentifier;
    private String newRegionCode;
    private String name;
    private String email;
    private String remarks;

    public String getOldIdentifier() {
        return oldIdentifier;
    }

    public void setOldIdentifier(String oldIdentifier) {
        this.oldIdentifier = oldIdentifier;
    }

    public String getOldRegionCode() {
        return oldRegionCode;
    }

    public void setOldRegionCode(String oldRegionCode) {
        this.oldRegionCode = oldRegionCode;
    }

    public String getNewIdentifier() {
        return newIdentifier;
    }

    public void setNewIdentifier(String newIdentifier) {
        this.newIdentifier = newIdentifier;
    }

    public String getNewRegionCode() {
        return newRegionCode;
    }

    public void setNewRegionCode(String newRegionCode) {
        this.newRegionCode = newRegionCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
