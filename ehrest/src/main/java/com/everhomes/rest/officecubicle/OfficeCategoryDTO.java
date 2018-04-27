package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul> 
 * <li>id: siteid</li>
 * <li>spaceId: 空间id</li>
 * <li>rentType: 租赁类别:1-开放式（默认space_type 1）,2-办公室 {@link com.everhomes.rest.officecubicle.OfficeRentType}</li>
 * <li>spaceType: 空间类别:1-工位,2-面积 </li>
 * <li>name: 空间名称</li>
 * <li>positionNums: 工位数量</li>
 * <li>unitPrice: 单位价格 </li>
 * <li>size: 场所大小 - 对于工位是个数，对于面积是平米</li>
 * </ul>
 */
public class OfficeCategoryDTO {
	private Long id;
	private Long spaceId;
    private Byte rentType;
    @Deprecated
    private Byte spaceType;
    private String name;

    private Integer positionNums;
    private BigDecimal unitPrice;
	private Integer size;
	
	public Integer getPositionNums() {
		return positionNums;
	}

	public void setPositionNums(Integer positionNums) {
		this.positionNums = positionNums;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public Byte getRentType() {
		return rentType;
	}

	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}
    @Deprecated
	public Byte getSpaceType() {
		return spaceType;
	}
    @Deprecated
	public void setSpaceType(Byte spaceType) {
		this.spaceType = spaceType;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
  
	public int compareTo(OfficeCategoryDTO other ){
		return this.getSize() - other.getSize();
	}
}
