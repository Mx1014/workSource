package com.everhomes.rest.acl.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>originalContactToken: 原管理员手机号</li>
 * <li>originalContactName: 原管理员名称</li>
 * <li>newContactToken: 新管理员手机号</li>
 * <li>newContactName: 新管理员名称</li>
 * </ul>
 */
public class TransferOrganizationSuperAdminCommand {

    private Long organizationId;

    private String originalContactToken;

    private String originalContactName;

    private String newContactToken;

    private String newContactName;

    public TransferOrganizationSuperAdminCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOriginalContactToken() {
        return originalContactToken;
    }

    public void setOriginalContactToken(String originalContactToken) {
        this.originalContactToken = originalContactToken;
    }

    public String getOriginalContactName() {
        return originalContactName;
    }

    public void setOriginalContactName(String originalContactName) {
        this.originalContactName = originalContactName;
    }

    public String getNewContactToken() {
        return newContactToken;
    }

    public void setNewContactToken(String newContactToken) {
        this.newContactToken = newContactToken;
    }

    public String getNewContactName() {
        return newContactName;
    }

    public void setNewContactName(String newContactName) {
        this.newContactName = newContactName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
