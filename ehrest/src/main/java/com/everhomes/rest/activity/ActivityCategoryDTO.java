package com.everhomes.rest.activity;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>ownerType: 所属者类型</li>
 * <li>ownerId: 所属者id</li>
 * <li>entryId: 实际入口id</li>
 * <li>parentId: 父id</li>
 * <li>name: 名称</li>
 * <li>path: 路径</li>
 * <li>defaultOrder: 排序</li>
 * <li>status: 状态</li>
 * <li>creatorUid: 创建者</li>
 * <li>createTime: 创建时间</li>
 * <li>deleteUid: 删除者</li>
 * <li>deleteTime: 删除时间</li>
 * <li>namespaceId: 域空间</li>
 * <li>enabled: 是否启用，1是0否，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>iconUri: icon地址uri</li>
 * <li>selectedIconUri: 选中时的icon地址uri</li>
 * <li>iconUrl: icon地址url</li>
 * <li>selectedIconUrl: 选中时的icon地址url</li>
 * <li>allFlag: 是否表示全部，1是0否，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ActivityCategoryDTO {

	private Long id;
	private String ownerType;
	private Long ownerId;
	private Long entryId;
	private Long parentId;
	private String name;
	private String path;
	private Integer defaultOrder;
	private Byte status;
	private Long creatorUid;
	private Timestamp createTime;
	private Long deleteUid;
	private Timestamp deleteTime;
	private Integer namespaceId;
	private Byte enabled;
	private String iconUri;
	private String selectedIconUri;
	private String iconUrl;
	private String selectedIconUrl;
	private Byte allFlag;

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getSelectedIconUrl() {
		return selectedIconUrl;
	}

	public void setSelectedIconUrl(String selectedIconUrl) {
		this.selectedIconUrl = selectedIconUrl;
	}

	public Byte getEnabled() {
		return enabled;
	}

	public void setEnabled(Byte enabled) {
		this.enabled = enabled;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public String getSelectedIconUri() {
		return selectedIconUri;
	}

	public void setSelectedIconUri(String selectedIconUri) {
		this.selectedIconUri = selectedIconUri;
	}

	public Byte getAllFlag() {
		return allFlag;
	}

	public void setAllFlag(Byte allFlag) {
		this.allFlag = allFlag;
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

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
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

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
