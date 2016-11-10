package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: specification表的主键id</li>
 *  <li>parentId: 父类型在specification表的主键id</li>
 *  <li>name: 类型名称</li>
 *  <li>ownerId: 类型所属的主体id</li>
 *  <li>ownerType: 类型所属的主体，如enterprise</li>
 *  <li>path: 类型路径</li>
 *  <li>score: 分数</li>
 *  <li>weight: 权重</li>
 *  <li>description: 规范内容</li>
 *  <li>scopeType: specification可见范围类型 0: all, 1: community</li>
 *  <li>scopeId: 看见范围具体Id，全部为0</li>
 * </ul>
 */
public class UpdateQualitySpecificationCommand {

	@NotNull
	private Long id;
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Byte scopeCode;
  
	private Long scopeId;
  
	private Long parentId;
  
	private String name;
  
	private String path;
  
	private Double score;
  
	private String description;
  
	private Double weight;

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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
