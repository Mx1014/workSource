// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorId: 门禁ID</li>
 * <li>keyword：搜索关键字，可以是手机号或对象名称</li>
 * <li>creator:创建者名称</li>
 * <li>createTimeStart:创建时间最小值</li>
 * <li>createTimeEnd:创建时间最大值</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:分页大小</li>
 * </ul>
 */
public class ListFormalAuthCommand {
	private Long doorId;
	private String keyword;
	private String creator;
	private Long createTimeStart;
	private Long createTimeEnd;
	private Long pageAnchor;
	private Integer pageSize;
	private Byte ownerType;
	private Long ownerId;
    //权限
    private Long appId;
    private Long currentPMId;
    private Long currentProjectId;
	
	public Long getDoorId() {
		return doorId;
	}
	public void setDoorId(Long doorId) {
		this.doorId = doorId;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
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
	public Byte getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Long getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(Long createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public Long getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(Long createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
    public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
