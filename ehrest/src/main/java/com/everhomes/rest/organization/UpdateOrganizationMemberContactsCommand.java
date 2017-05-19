package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * Created by M on 2017/5/18.
 * <ul>
 * <li>email: 邮箱</li>
 * <li>weChat: 微信号码</li>
 * <li>qq: QQ号码</li>
 * <li>emergencyNmae: 紧急联系人姓名</li>
 * <li>emergencyContact: 紧急联系人号码</li>
 * <li>address: 住址</li>
 * </ul>
 */
public class UpdateOrganizationMemberContactsCommand {

    private Long memberId;

    private String email;

    private String weChat;

    private String qq;

    private String emergencyName;

    private String emergencyContact;

    private String address;

    public UpdateOrganizationMemberContactsCommand() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
