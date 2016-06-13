package com.everhomes.rest.user;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 获取邀请码
 * 
 * @author elians
 *         <ul>
 *         <li>
 *         inviteType:邀请类型.支持类型1.SMS,2.wechat,3.friend_circle,4.weibo,5.phone</li>
 *         <li>targetEntityType:实体类型</li>
 *         <li>targetEntityId:实体ID</li>
 *         <li>identifiers:用户标识，手机或者邮箱，用;分割</li>
 *         <li>communityId:小区ID</li>
 *         <li>buildingNum:楼栋号</li>
 *         <li>aptNumber:公寓号</li>
 *         </ul>
 *
 */
public class CreateInvitationCommand {

    
    @NotNull
    private Byte inviteType;
    @NotNull
    private String targetEntityType;
    private Long targetEntityId;

    private String identifiers;
    
    private Long communityId;
    
    
    private String buildingNum;

    private String aptNumber;

    public Byte getInviteType() {
        return inviteType;
    }

    public void setInviteType(Byte inviteType) {
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

    public String getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }
    
    

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(String buildingNum) {
        this.buildingNum = buildingNum;
    }

    public String getAptNumber() {
        return aptNumber;
    }

    public void setAptNumber(String aptNumber) {
        this.aptNumber = aptNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
