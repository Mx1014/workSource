package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 *  <li>specificationParentId: 规范事项所属规范id</li>
 *  <li>specificationId: 规范事项id</li>
 *  <li>specificationPath: 规范事项path</li>
 *  <li>itemDescription: 规范事项描述</li>
 *  <li>itemScore: 事项分数</li>
 *  <li>quantity: 数量</li>
 * </ul>
 */
public class ReportSpecificationItemResultsDTO {
	
	private Long specificationParentId;
	
	private String specificationPath;

	private Long specificationId;
	  
	private String itemDescription;
	  
	private Double itemScore;
	  
	private Integer quantity;
	
	public Long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(Long specificationId) {
		this.specificationId = specificationId;
	}

	public String getSpecificationPath() {
		return specificationPath;
	}

	public void setSpecificationPath(String specificationPath) {
		this.specificationPath = specificationPath;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Double getItemScore() {
		return itemScore;
	}

	public void setItemScore(Double itemScore) {
		this.itemScore = itemScore;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Long getSpecificationParentId() {
		return specificationParentId;
	}

	public void setSpecificationParentId(Long specificationParentId) {
		this.specificationParentId = specificationParentId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
