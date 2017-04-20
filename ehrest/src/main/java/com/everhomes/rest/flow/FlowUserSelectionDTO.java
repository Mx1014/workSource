package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul> 用户选择项
 * <li>selectType: 用户选择的类型 {@link com.everhomes.rest.flow.FlowUserSelectionType}</li>
 * </ul>
 * @author janson
 *
 */
public class FlowUserSelectionDTO {
	private Long id;
	private Integer namespaceId;
	private Long flowMainId;
	private Integer flowVersion;
	private Long organizationId;
	private String selectType;
	private Long sourceIdA;
	private String sourceTypeA;
	private Long sourceIdB;
	private String sourceTypeB;
	private Long belongTo;
	private String belongEntity;
	private String belongType;
	private Byte status;
	private Timestamp createTime;
	private String selectionName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getFlowMainId() {
		return flowMainId;
	}

	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}

	public Integer getFlowVersion() {
		return flowVersion;
	}

	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public Long getSourceIdA() {
		return sourceIdA;
	}

	public void setSourceIdA(Long sourceIdA) {
		this.sourceIdA = sourceIdA;
	}

	public String getSourceTypeA() {
		return sourceTypeA;
	}

	public void setSourceTypeA(String sourceTypeA) {
		this.sourceTypeA = sourceTypeA;
	}

	public Long getSourceIdB() {
		return sourceIdB;
	}

	public void setSourceIdB(Long sourceIdB) {
		this.sourceIdB = sourceIdB;
	}

	public String getSourceTypeB() {
		return sourceTypeB;
	}

	public void setSourceTypeB(String sourceTypeB) {
		this.sourceTypeB = sourceTypeB;
	}

	public Long getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(Long belongTo) {
		this.belongTo = belongTo;
	}

	public String getBelongEntity() {
		return belongEntity;
	}

	public void setBelongEntity(String belongEntity) {
		this.belongEntity = belongEntity;
	}

	public String getBelongType() {
		return belongType;
	}

	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getSelectionName() {
		return selectionName;
	}

	public void setSelectionName(String selectionName) {
		this.selectionName = selectionName;
	}

	public String getFlowUserSelectionType() {
		return selectType;
	}

	public void setFlowUserSelectionType(String flowUserSelectionType) {
		this.selectType = flowUserSelectionType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

