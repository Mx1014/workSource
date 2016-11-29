package com.everhomes.rest.quality;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: specification表的主键id</li>
 *  <li>parentId: 父类型在specification表的主键id</li>
 *  <li>name: 类型名称</li>
 *  <li>ownerId: 类型所属的主体id</li>
 *  <li>ownerType: 类型所属的主体，如enterprise</li>
 *  <li>path: 类型路径</li>
 *  <li>status: 类型状态 参考 {@link com.everhomes.rest.quality.QualityInspectionCategoryStatus}</li>
 *  <li>score: 分数</li>
 *  <li>weight: 权重</li>
 *  <li>description: 规范内容</li>
 *  <li>inspectionType: 规范类型 0: 类型, 1: 规范, 2: 规范事项</li>
 *  <li>namespaceId: 域空间id</li>
 *  <li>scopeType: specification可见范围类型 0: all, 1: community</li>
 *  <li>scopeId: 看见范围具体Id，全部为0</li>
 *  <li>applyPolicy: 应用策略 0: add, 1: modify, 2: delete</li>
 *  <li>referId: 关联规范id</li>
 *  <li>creatorUid: 创建者uid</li>
 *  <li>createTime: 创建时间</li>
 *  <li>childrens：子集参考{@link com.everhomes.rest.quality.QualityInspectionSpecificationDTO}</li>
 * </ul>
 */
public class QualityInspectionSpecificationDTO {
  
	private Long id;
  
	private String ownerType;

	private Long ownerId;
  
	private Byte scopeCode;
  
	private Long scopeId;
  
	private Long parentId;
  
	private String name;
  
	private String path;
  
	private Double score;
  
	private String description;
  
	private Double weight;
  
	private Byte inspectionType;
  
	private Integer namespaceId;
  
	private Byte applyPolicy;
  
	private Long referId;
  
	private Long creatorUid;
  
	private Timestamp createTime;
  
	private Byte status;
	
	@ItemType(QualityInspectionSpecificationDTO.class)
	private List<QualityInspectionSpecificationDTO> childrens;
	
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

	public Byte getScopeCode() {
		return scopeCode;
	}

	public void setScopeCode(Byte scopeCode) {
		this.scopeCode = scopeCode;
	}

	public Long getScopeId() {
		return scopeId;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
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

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Byte getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(Byte inspectionType) {
		this.inspectionType = inspectionType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getApplyPolicy() {
		return applyPolicy;
	}

	public void setApplyPolicy(Byte applyPolicy) {
		this.applyPolicy = applyPolicy;
	}

	public Long getReferId() {
		return referId;
	}

	public void setReferId(Long referId) {
		this.referId = referId;
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public List<QualityInspectionSpecificationDTO> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<QualityInspectionSpecificationDTO> childrens) {
		this.childrens = childrens;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
