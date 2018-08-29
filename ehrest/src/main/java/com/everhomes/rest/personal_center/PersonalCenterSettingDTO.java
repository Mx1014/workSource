package com.everhomes.rest.personal_center;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 *     <li>id:id</li>
 *     <li>name: 个人中心配置项展示名称</li>
 *     <li>functionName: 个人中心配置项所属功能名称，固定不变</li>
 *     <li>status: 状态，请参考{@link com.everhomes.rest.personal_center.PersonalCenterSettingStatus}</li>
 *     <li>region: 展示区域，请参考{@link com.everhomes.rest.personal_center.PersonalCenterSettingRegionType}</li>
 *     <li>groupType: 分组：1 2 3 ....</li>
 *     <li>sortNum: 展示顺序</li>
 *     <li>type: 个人中心配置项所属类型，请参考{@link com.everhomes.rest.personal_center.PersonalCenterSettingType}</li>
 *     <li>iconUri:图标URI</li>
 *     <li>iconUrl: 图标URL</li>
 *     <li>showable: 是否展示，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>editable: 是否可编辑，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>version: 版本号</li>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>createUid: 创建人ID</li>
 *     <li>createTime: 创建时间</li>
 *     <li>updateUid: 修改人ID</li>
 *     <li>updateTime: 修改时间</li>
 * </ul>
 */
public class PersonalCenterSettingDTO {
    private Byte     status;
    private Long     createUid;
    private Timestamp     updateTime;
    private Integer     type;
    private Byte     groupType;
    private String     name;
    private Byte     region;
    private String     functionName;
    private Byte     editable;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Long     updateUid;
    private Integer     version;
    private String     iconUri;
    private String iconUrl;
    private Byte     showable;
    private Integer     sortNum;
    private Long     id;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Byte getEditable() {
		return editable;
	}

	public void setEditable(Byte editable) {
		this.editable = editable;
	}

	public Byte getShowable() {
		return showable;
	}

	public void setShowable(Byte showable) {
		this.showable = showable;
	}

	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}


	public Long getCreateUid() {
		return createUid;
	}


	public void setCreateUid(Long createUid) {
		this.createUid = createUid;
	}


	public Timestamp getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Byte getRegion() {
		return region;
	}


	public void setRegion(Byte region) {
		this.region = region;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	public Long getUpdateUid() {
		return updateUid;
	}


	public void setUpdateUid(Long updateUid) {
		this.updateUid = updateUid;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getIconUri() {
		return iconUri;
	}


	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public Byte getGroupType() {
		return groupType;
	}

	public void setGroupType(Byte groupType) {
		this.groupType = groupType;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

