package com.everhomes.rest.personal_center;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class PersonalCenterSettingDTO {
    private Byte     status;
    private Long     createUid;
    private Timestamp     updateTime;
    private String     routerUrl;
    private Byte     group;
    private String     name;
    private Byte     region;
    private String     funcitonName;
    private Byte     editalbe;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Long     updateUid;
    private Long     version;
    private String     iconUri;
    private Byte     showalbe;
    private Integer     order;
    private Long     id;


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


	public String getRouterUrl() {
		return routerUrl;
	}


	public void setRouterUrl(String routerUrl) {
		this.routerUrl = routerUrl;
	}


	public Byte getGroup() {
		return group;
	}


	public void setGroup(Byte group) {
		this.group = group;
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


	public String getFuncitonName() {
		return funcitonName;
	}


	public void setFuncitonName(String funcitonName) {
		this.funcitonName = funcitonName;
	}


	public Byte getEditalbe() {
		return editalbe;
	}


	public void setEditalbe(Byte editalbe) {
		this.editalbe = editalbe;
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


	public Byte getShowalbe() {
		return showalbe;
	}


	public void setShowalbe(Byte showalbe) {
		this.showalbe = showalbe;
	}


	public Integer getOrder() {
		return order;
	}


	public void setOrder(Integer order) {
		this.order = order;
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

