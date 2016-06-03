package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>id: category表的主键id</li>
 *  <li>parentId: 父类型在category表的主键id</li>
 *  <li>name: 类型名称</li>
 *  <li>ownerId: 类型所属的主体id</li>
 *  <li>ownerType: 类型所属的主体，如enterprise</li>
 *  <li>path: 类型路径</li>
 *  <li>status: 类型状态 参考 com.everhomes.rest.quality.QualityInspectionCategoryStatus</li>
 *  <li>defaultOrder: </li>
 *  <li>score: 分数</li>
 *  <li>description: 规范内容</li>
 *  <li>childrens：子集参考{@link com.everhomes.rest.quality.QualityCategoriesDTO}</li>
 * </ul>
 */
public class QualityCategoriesDTO {

	private Long id;
	
	private String ownerType;
	
	private Long ownerId;
	
	private Long parentId;
	
	private String name;
	
	private String path;
	
	private Integer defaultOrder;
	
	private Byte status;
	
	private double score;
	
	private String description;
	
	@ItemType(QualityCategoriesDTO.class)
	private List<QualityCategoriesDTO> childrens;

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
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<QualityCategoriesDTO> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<QualityCategoriesDTO> childrens) {
		this.childrens = childrens;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
