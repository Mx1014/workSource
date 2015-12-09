package com.everhomes.common;

import java.io.Serializable;


import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为LIST_GROUPS(36)，用户关联的圈，调用接口/group/listGroupsByNamespaceId
 * <li>privateFlag：公私有标志，兴趣圈为公有、私有邻居圈为私有，参考{@link com.everhomes.group.GroupPrivacy}</li>
 * </ul>
 */
public class GroupByNamespaceIdActionData implements Serializable{
    private static final long serialVersionUID = 2232055231950950495L;
   
    private Byte privateFlag;
    
    private String keywords;

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
    }
    
    

    public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
