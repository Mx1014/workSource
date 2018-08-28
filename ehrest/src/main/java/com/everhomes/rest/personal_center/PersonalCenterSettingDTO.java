package com.everhomes.rest.personal_center;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class PersonalCenterSettingDTO {
    private Byte     status;
    private Long     createUid;
    private Timestamp     updateTime;
    private String     type;
    private Byte     groupType;
    private String     name;
    private Byte     region;
    private String     functionName;
    private Byte     editable;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Long     updateUid;
    private Long     version;
    private String     iconUri;
    private Byte     showable;
    private Integer     sortNum;
    private Long     id;


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

	public String getType() {
		return type;
	}

	public void setType(String type) {
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


	public Long getVersion() {
		return version;
	}


	public void setVersion(Long version) {
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

