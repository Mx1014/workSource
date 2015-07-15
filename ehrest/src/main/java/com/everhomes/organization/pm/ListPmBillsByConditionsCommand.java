package com.everhomes.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * 
  <ul>
  	<li>pageAnchor : 下一条记录id</li>	
	<li>pageSize : 页大小</li>	
	<li>buildingName : 楼栋号</li>	
	<li>apartmentName : 门牌号</li>
	<li>dateStr : 账单日期</li>
	<li>telephone : 业主电话</li>
  </ul>
 *
 */

public class ListPmBillsByConditionsCommand {
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	private Long buildingName;
	
	private Long apartmentName;
	
	private String dateStr;
	
	private String telephone;
	
	public Long getPageAnchor() {
		return pageAnchor;
	}
	
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public Long getBuildingName() {
		return buildingName;
	}
	
	public void setBuildingName(Long buildingName) {
		this.buildingName = buildingName;
	}
	
	public Long getApartmentName() {
		return apartmentName;
	}
	
	public void setApartmentName(Long apartmentName) {
		this.apartmentName = apartmentName;
	}
	
	public String getDateStr() {
		return dateStr;
	}
	
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
