package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为USER_GROUPS(35)，用户关联的圈，调用接口/group/listUserRelatedGroups 
 * <li>privateFlag：公私有标志，兴趣圈为公有、私有邻居圈为私有，参考{@link com.everhomes.rest.group.GroupPrivacy}</li>
 * </ul>
 */
public class UserGroupActionData implements Serializable{
    private static final long serialVersionUID = 2232055231950950495L;
   
    private Byte privateFlag;

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
