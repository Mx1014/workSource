package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 获取邀请码
 * 
 * @author elians
 * <ul>
 * <li>inviteType:邀请类型.支持类型1.SMS,2.wechat,3.friend_circle,4.weibo,5.phone</li>
 * <li>targetEntityType:实体类型</li>
 * <li>targetEntityId:实体ID</li>
 * </ul>
 *
 */
public class CreateInvitationCommand {

    @NotNull
    private String inviteType;
    @NotNull
    private String targetEntityType;
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
    
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
