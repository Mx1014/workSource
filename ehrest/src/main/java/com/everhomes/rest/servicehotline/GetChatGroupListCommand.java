package com.everhomes.rest.servicehotline;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * 参数
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>ownerId: 所属ID</li>
 * <li>pageSize: 每页的数量</li>
 * <li>pageAnchor: 锚点</li>
 * <li>servicerId: 客服id</li>
 * <li>keyword: 搜索词，如姓名，电话</li>
 * <li>namespaceId: 域空间</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id</li>
 * <li>appId: 应用id</li> 
 * <li>extraParam: 额外参数　json字符串</li> 
 * </ul>
 */
public class GetChatGroupListCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	private Integer pageSize;
	private Long pageAnchor;
	private Long servicerId;
	private String keyword;
	private Integer namespaceId;
	@NotNull
	private Long currentPMId;
	@NotNull
	private Long currentProjectId;
	@NotNull
	private Long appId;	
	
	private String extraParam;
	
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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
	public Long getServicerId() {
		return servicerId;
	}
	public void setServicerId(Long servicerId) {
		this.servicerId = servicerId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getCurrentPMId() {
		return currentPMId;
	}
	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}
	public Long getCurrentProjectId() {
		return currentProjectId;
	}
	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public String getExtraParam() {
		return extraParam;
	}
	public void setExtraParam(String extraParam) {
		this.extraParam = extraParam;
	}
	
}
