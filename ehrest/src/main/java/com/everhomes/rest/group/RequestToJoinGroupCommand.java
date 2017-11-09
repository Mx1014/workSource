// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>groupId: group id</li>
 *     <li>inviterId: 邀请人id</li>
 *     <li>requestText: 发起请求时可填写的说明文本</li>
 *     <li>avatar: 行业协会头像URI</li>
 *     <li>name: name</li>
 *     <li>phone: phone</li>
 *     <li>email: email</li>
 *     <li>organizationName: organizationName</li>
 *     <li>registeredCapital: registeredCapital</li>
 *     <li>industryType: industryType</li>
 * </ul>
 */
public class RequestToJoinGroupCommand {
    @NotNull
    private Long groupId;

    private Long inviterId;

    private String requestText;

    private String avatar;
    private String name;
    private String phone;
    private String email;
    private String organizationName;
    private String registeredCapital;
    private String industryType;

    public RequestToJoinGroupCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getInviterId() {
        return inviterId;
    }

    public void setInviterId(Long inviterId) {
        this.inviterId = inviterId;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
