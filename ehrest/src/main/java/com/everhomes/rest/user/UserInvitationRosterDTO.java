package com.everhomes.rest.user;
/**
 * 邀请用户记录
 * @author elians
 *<ul>
 *<li>id:id标识</li>
 *<li>inviteId:邀请人ID</li>
 *<li>name:名字</li>
 *<li>contact:联系方式</li>
 *</ul>
 */
public class UserInvitationRosterDTO {
    private java.lang.Long id;
    private java.lang.Long inviteId;
    private java.lang.String name;
    private java.lang.String contact;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getInviteId() {
        return inviteId;
    }

    public void setInviteId(java.lang.Long inviteId) {
        this.inviteId = inviteId;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getContact() {
        return contact;
    }

    public void setContact(java.lang.String contact) {
        this.contact = contact;
    }

}
