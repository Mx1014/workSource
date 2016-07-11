// @formatter:off
package com.everhomes.group;

import com.everhomes.rest.group.GroupMemberMuteNotificationFlag;
import com.everhomes.rest.group.GroupMemberPhonePrivacy;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;
import com.everhomes.util.StringHelper;

public class GroupMember extends EhGroupMembers {
    private static final long serialVersionUID = -4772430969286554458L;

    private Byte phonePrivateFlag;
    
    private Byte muteNotificationFlag;

    public GroupMember() {
        // Set the default value for custom fileds
        setPhonePrivateFlag(GroupMemberPhonePrivacy.PRIVATE.getCode());
        setMuteNotificationFlag(GroupMemberMuteNotificationFlag.NONE.getCode());
    }
    
    public Byte getPhonePrivateFlag() {
        Long value = GroupMemberCustomField.PHONE_PRIVATE_FLAG.getIntegralValue(this);
        
        Byte phonePrivateFlag = null;
        if(value != null) {
            phonePrivateFlag = Byte.valueOf(value.byteValue()); 
        }
        
        return phonePrivateFlag;
    }
    
    public void setPhonePrivateFlag(Byte phonePrivateFlag) {
        Long value = null;
        this.phonePrivateFlag = phonePrivateFlag;
        if(phonePrivateFlag != null) {
            value = phonePrivateFlag.longValue();
        }
        GroupMemberCustomField.PHONE_PRIVATE_FLAG.setIntegralValue(this, value);
    }
    
    public Byte getMuteNotificationFlag() {
        Long value = GroupMemberCustomField.MUTE_NOTIFICATION_FLAG.getIntegralValue(this);
        
        Byte muteNotificationFlag = null;
        if(value != null) {
            muteNotificationFlag = Byte.valueOf(value.byteValue()); 
        }
        
        return muteNotificationFlag;
    }
    
    public void setMuteNotificationFlag(Byte muteNotificationFlag) {
        Long value = null;
        this.muteNotificationFlag = muteNotificationFlag;
        if(muteNotificationFlag != null) {
            value = muteNotificationFlag.longValue();
        }
        GroupMemberCustomField.MUTE_NOTIFICATION_FLAG.setIntegralValue(this, value);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
