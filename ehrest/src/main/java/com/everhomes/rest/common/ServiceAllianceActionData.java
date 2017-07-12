package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

import java.io.Serializable;


/**
 * <ul>actionType为SERVICEALLIANCE(33)，服务联盟
 * <li>type：类型，参考{@link com.everhomes.rest.yellowPage.YellowPageType}</li>
 * <li>parentId: 服务联盟中筛选条件列表中的类型对应的父亲类型</li>
 * </ul>
 */
public class ServiceAllianceActionData implements Serializable{
    private static final long serialVersionUID = -742724365939053762L;
    
    private Byte type;
    
    private Long parentId;

    private String displayType;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
