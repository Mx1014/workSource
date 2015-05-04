package com.everhomes.user;

import javax.validation.constraints.NotNull;

/**
 * 获取邀请码
 * 
 * @author elians
 *
 */
public class CreateInvitationCommand {

    /** 邀请类型 **/
    @NotNull
    private String inviteType;
    /** 实体类型 **/
    @NotNull
    private String targetEntityType;
    /** 实体id **/
    @NotNull
    private Long targetEntityId;

    public String getInviteType() {
        return inviteType;
    }

    public void setInviteType(String inviteType) {
        this.inviteType = inviteType;
    }

    public String getTargetEntityType() {
        return targetEntityType;
    }

    public void setTargetEntityType(String targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    public Long getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(Long targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

}
