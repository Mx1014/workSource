package com.everhomes.rest.statistics.transaction;


/**
 *<ul>
 *<li>wareId:商品ID</li>
 *<li>wareName:商品名称</li>
 *<li>number:商品数量</li>
 *</ul>
 */
public class StatWareDTO {
	
	private String wareId;
	
	private String wareName;
	
	private String number;

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	
	
}
