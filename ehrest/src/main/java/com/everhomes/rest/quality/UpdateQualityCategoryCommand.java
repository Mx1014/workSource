package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: category表的主键id</li>
 *  <li>parentId: 父类型在category表的主键id</li>
 *  <li>name: 类型名称</li>
 *  <li>ownerId: 类型所属的主体id</li>
 *  <li>ownerType: 类型所属的主体，如enterprise</li>
 *  <li>score: 分数</li>
 *  <li>description: 规范内容</li>
 * </ul>
 */
public class UpdateQualityCategoryCommand {
	
    private Long id;
    
    private Long parentId;
    
    @NotNull
    private String name;
    
    @NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Double score;
	
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
    
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
