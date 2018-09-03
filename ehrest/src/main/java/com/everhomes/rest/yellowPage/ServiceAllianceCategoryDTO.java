package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 类型ID</li>
 * <li>ownerType: 拥有者类型：现在是community</li>
 * <li>ownerId: 拥有者ID</li>
 * <li>parentId: 父类型ID</li>
 * <li>name: 类型名称</li>
 * <li>path: 类型路径</li>
 * <li>status: 状态，{@link com.everhomes.rest.category.CategoryAdminStatus}</li>
 * <li>creatorUid: 创建者uid</li>
 * <li>createTime: 创建时间</li>
 * <li>deleteUid: 删除者uid</li>
 * <li>deleteTime: 删除时间</li>
 * <li>namespaceId: 域空间id</li>
 * <li>logoUrl: 类型logo的url地址</li>
 * <li> selectedLogoUrl: 类型logo被选中时的url地址</li>
 * <li>displayMode: 显示类型</li>
 * <li>displayModeName: 显示类型名称</li>
 * <li> skipType: 只有一个企业时是否跳过列表页，0 不跳； 1 跳过</li>
 * </ul>
 */
public class ServiceAllianceCategoryDTO {

	private Long id;
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	
	private Long parentId;
	
	private Long type;
	
	private String name;
	
	private String path;
	
	private Byte status;
	
	private Long creatorUid;
	
	private Timestamp createTime;
	
	private Long deleteUid;
	
    private Timestamp deleteTime;
	
	private Integer namespaceId;
	
	private String logoUrl;
	
	private String selectedLogoUrl;

	private Byte displayMode;
	private String displayModeName;
	private Byte skipType;
	private Integer entryId;

	public Byte getSkipType() {
		return skipType;
	}

	public void setSkipType(Byte skipType) {
		this.skipType = skipType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

    public Byte getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Byte displayMode) {
        this.displayMode = displayMode;
    }

    public String getDisplayModeName() {
        return displayModeName;
    }

    public void setDisplayModeName(String displayModeName) {
        this.displayModeName = displayModeName;
    }

    public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getDeleteUid() {
		return deleteUid;
	}

	public void setDeleteUid(Long deleteUid) {
		this.deleteUid = deleteUid;
	}

	public Timestamp getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Timestamp deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getSelectedLogoUrl() {
		return selectedLogoUrl;
	}

	public void setSelectedLogoUrl(String selectedLogoUrl) {
		this.selectedLogoUrl = selectedLogoUrl;
	}

	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

}
