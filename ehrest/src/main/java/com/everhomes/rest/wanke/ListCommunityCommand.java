package com.everhomes.rest.wanke;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>token: 来自对接方</li>
 * <li>type: 对接方类型</li>
 * <li>namespaceId: 域空间Id</li>
 * <li>pageSize: 每页条数</li>
 * <li>pageAnchor: 分页瞄</li>
 * </ul>
 */
public class ListCommunityCommand {
	private String token;
	private String type;
	private Integer namespaceId;
	private Integer pageSize;
	private Long pageAnchor;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
