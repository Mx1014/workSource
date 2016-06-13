package com.everhomes.rest.common;

import java.io.Serializable;




import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为LIST_GROUPS(36)，用户关联的圈，调用接口/group/listGroupsByNamespaceId
 * <li>privateFlag：公私有标志，兴趣圈为公有、私有邻居圈为私有，参考{@link com.everhomes.rest.group.GroupPrivacy}</li>
 * <li>keywords：圈tag</li>
 * <li>categoryId：圈类型ID</li>
 * </ul>
 */
public class GroupByNamespaceIdActionData implements Serializable{
    private static final long serialVersionUID = 2232055231950950495L;
   
    private Byte privateFlag;
    
    private String keywords;
    
    private Long categoryId;

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

	public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
