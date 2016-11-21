package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowUserSelectionDTO {
    private Byte     status;
    private String     sourceType;
    private String     selectType;
    private Long     sourceId;
    private Long     flowMainId;
    private Long     belongTo;
    private String     belongType;
    private String     belongEntity;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Integer     flowVersion;
    private Long     id;


    public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}


	public String getSourceType() {
		return sourceType;
	}


	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}


	public String getSelectType() {
		return selectType;
	}


	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}


	public Long getSourceId() {
		return sourceId;
	}


	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}


	public Long getFlowMainId() {
		return flowMainId;
	}


	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}


	public String getBelongEntity() {
		return belongEntity;
	}


	public void setBelongEntity(String belongEntity) {
		this.belongEntity = belongEntity;
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


	public Integer getFlowVersion() {
		return flowVersion;
	}


	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getBelongTo() {
		return belongTo;
	}


	public void setBelongTo(Long belongTo) {
		this.belongTo = belongTo;
	}


	public String getBelongType() {
		return belongType;
	}


	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

