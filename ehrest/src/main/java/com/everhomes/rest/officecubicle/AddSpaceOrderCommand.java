package com.everhomes.rest.officecubicle;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间订单
 *<li> siteId: office site id	</li> 
 * <li>orderType: 预定类别：1-参观 2-预定 </li>  
 * <li>rentType: 租赁类别:1-开放式（默认space_type 1）,2-办公室 {@link com.everhomes.rest.officecubicle.OfficeRentType}</li>  
 * <li>size: 预定空间大小</li>  
 * <li>positionNums: 工位数量</li>  
 * <li>reserverName: 预订人姓名 </li> 
 * <li>reserveContactToken:  预订人联系方式</li> 
 * <li>reserveEnterprise: 预订人公司</li> 
 * <li>categoryName: 预定空间名称</li>
 * <li>categoryId: 空间id</li>
 * <li>employeeNumber: 雇员数量</li>
 * <li>financingFlag: 是否融资 0否，1是</li>
 * </ul>
 */
public class AddSpaceOrderCommand {

	private Long spaceId;
    private Byte orderType;
    private Byte rentType;
    @Deprecated
    private Byte spaceType;
    
    private Integer positionNums;
    private Integer size;
    private String reserverName;
    private String reserveEnterprise;
	private String reserveContactToken;
	private String categoryName;
	private Long categoryId;

	private Integer employeeNumber;
	private Byte financingFlag;


	public Integer getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public Byte getFinancingFlag() {
		return financingFlag;
	}

	public void setFinancingFlag(Byte financingFlag) {
		this.financingFlag = financingFlag;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	  
	public Integer getPositionNums() {
		return positionNums;
	}


	public void setPositionNums(Integer positionNums) {
		this.positionNums = positionNums;
	}


	public Byte getRentType() {
		return rentType;
	}


	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}


	public Byte getSpaceType() {
		return spaceType;
	}


	public void setSpaceType(Byte spaceType) {
		this.spaceType = spaceType;
	}

 
	public Byte getOrderType() {
		return orderType;
	}


	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
	}


	public Integer getSize() {
		return size;
	}


	public void setSize(Integer size) {
		this.size = size;
	}


	public Long getSpaceId() {
		return spaceId;
	}


	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}


	public String getReserverName() {
		return reserverName;
	}


	public void setReserverName(String reserverName) {
		this.reserverName = reserverName;
	}


	public String getReserveEnterprise() {
		return reserveEnterprise;
	}


	public void setReserveEnterprise(String reserveEnterprise) {
		this.reserveEnterprise = reserveEnterprise;
	}


	public String getReserveContactToken() {
		return reserveContactToken;
	}


	public void setReserveContactToken(String reserveContactToken) {
		this.reserveContactToken = reserveContactToken;
	}
}
