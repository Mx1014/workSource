package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

import java.io.Serializable;


/**
 * 该类是给到服务广场的配置
 * <ul>
 * <li>type：服务联盟类型id (eh_service_alliance_categories)</li>
 * <li>parentId: 服务联盟中筛选条件列表中的类型对应的父亲类型 ——当前未用到，可能是旧版本用到了，先不处理</li>
 * <li>enableComment: 是否允许评论 0-不允许 1-允许</li>
 * <li>enableProvider: 是否开启服务商的功能 0-不开启 1-开启</li>
 * <li>enableCustomerService: 是否开启客服会话查看和导出功能 0-不开启 1-开启</li>
 * </ul>
 */
public class ServiceAllianceActionData implements Serializable{
    private static final long serialVersionUID = -742724365939053762L;
    
    private Long type;
    
    private Long parentId;

    private String displayType;
    
    private Byte enableComment;
	
	private Byte enableProvider;
	
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
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

	public Byte getEnableComment() {
		return enableComment;
	}

	public void setEnableComment(Byte enableComment) {
		this.enableComment = enableComment;
	}

	public Byte getEnableProvider() {
		return enableProvider;
	}

	public void setEnableProvider(Byte enableProvider) {
		this.enableProvider = enableProvider;
	}
}
