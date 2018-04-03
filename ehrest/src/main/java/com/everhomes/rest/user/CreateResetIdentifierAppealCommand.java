// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>oldIdentifier: 旧手机号</li>
 *     <li>oldRegionCode: oldRegionCode</li>
 *     <li>newIdentifier: 新手机号</li>
 *     <li>newRegionCode: newRegionCode</li>
 *     <li>name: 名称</li>
 *     <li>email: 邮箱</li>
 *     <li>remarks: 备注</li>
 * </ul>
 */
public class CreateResetIdentifierAppealCommand {

    @NotNull
    private String oldIdentifier;
    @NotNull
    private Integer oldRegionCode;
    @NotNull
    private String newIdentifier;
    @NotNull
    private Integer newRegionCode;
    @NotNull
    private String name;
    @NotNull
    private String email;
    private String remarks;

    public String getOldIdentifier() {
        return oldIdentifier;
    }

    public void setOldIdentifier(String oldIdentifier) {
        this.oldIdentifier = oldIdentifier;
    }

    public Integer getOldRegionCode() {
        return oldRegionCode;
    }

    public void setOldRegionCode(Integer oldRegionCode) {
        this.oldRegionCode = oldRegionCode;
    }

    public String getNewIdentifier() {
        return newIdentifier;
    }

    public void setNewIdentifier(String newIdentifier) {
        this.newIdentifier = newIdentifier;
    }

    public Integer getNewRegionCode() {
        return newRegionCode;
    }

    public void setNewRegionCode(Integer newRegionCode) {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
