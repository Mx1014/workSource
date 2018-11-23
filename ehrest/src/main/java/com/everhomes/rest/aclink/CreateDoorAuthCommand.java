package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 授权给的用户，必填项</li>
 * <li>doorId: 门禁 ID，必填项</li>
 * <li>approveUserId: 谁授权的用户ID</li>
 * <li>AuthType: 0 永久授权， 1 临时授权</li>
 * <li>validFromMs: 授权开始有效时间</li>
 * <li>validEndMs: 授权失效时间</li>
 * <li>operatorOrgId: 操作人所在的公司</li>
 * <li>organization: 用户来自于，访客授权使用</li>
 * <li>description: 授权描述，访客授权使用</li>
 * <li>notice: 访客来访提示 1 需提示 null 不提示（只在app端）</li>
 * </ul>
 * @author janson
 *
 */
public class CreateDoorAuthCommand {
    @NotNull
    private Long     userId;
    
    @NotNull
    private Long     doorId;
    
    private Long approveUserId;
    
    @NotNull
    private Byte     authType;
    
    private Byte rightOpen;
    private Byte rightVisitor;
    private Byte rightRemote;
    
    private Long     validFromMs;
    private Long     validEndMs;
    private Long operatorOrgId;
    private String organization;
    private String description;
    private Integer namespaceId;
    private String phone;
    private String authMethod;

    private String keyU;
    private Byte notice;

    public Byte getNotice() {
        return notice;
    }

    public void setNotice(Byte notice) {
        this.notice = notice;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getDoorId() {
        return doorId;
    }
    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }
    public Byte getAuthType() {
        return authType;
    }
    public void setAuthType(Byte authType) {
        this.authType = authType;
    }
    public Long getValidFromMs() {
        return validFromMs;
    }
    public void setValidFromMs(Long validFromMs) {
        this.validFromMs = validFromMs;
    }
    public Long getValidEndMs() {
        return validEndMs;
    }
    public void setValidEndMs(Long validEndMs) {
        this.validEndMs = validEndMs;
    }
    public String getOrganization() {
        return organization;
    }
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getApproveUserId() {
        return approveUserId;
    }
    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }
    
    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Byte getRightOpen() {
        return rightOpen;
    }
    public void setRightOpen(Byte rightOpen) {
        this.rightOpen = rightOpen;
    }
    public Byte getRightVisitor() {
        return rightVisitor;
    }
    public void setRightVisitor(Byte rightVisitor) {
        this.rightVisitor = rightVisitor;
    }
    public Byte getRightRemote() {
        return rightRemote;
    }
    public void setRightRemote(Byte rightRemote) {
        this.rightRemote = rightRemote;
    }

    public String getAuthMethod() {
        return authMethod;
    }
    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }
    public Long getOperatorOrgId() {
        return operatorOrgId;
    }
    public void setOperatorOrgId(Long operatorOrgId) {
        this.operatorOrgId = operatorOrgId;
    }

    public String getKeyU() {
        return keyU;
    }

    public void setKeyU(String keyU) {
        this.keyU = keyU;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
