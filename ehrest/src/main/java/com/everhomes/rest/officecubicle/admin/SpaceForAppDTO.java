// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>spaceId：空间id</li>
 * <li>spaceName：空间名称</li>
 * <li>minUnitPrice：最小价格</li>
 * <li>allPositonNums：所有工位数量</li>
 * <li>address:地址</li>
 * </ul>
 */
public class SpaceForAppDTO {
    private Long spaceId;
    private String spaceName;
    private BigDecimal minUnitPrice;
    private Integer allPositonNums;
    private String address;


    public Long getSpaceId() {
		return spaceId;
	}




	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}




	public String getSpaceName() {
		return spaceName;
	}




	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}




	public BigDecimal getMinUnitPrice() {
		return minUnitPrice;
	}




	public void setMinUnitPrice(BigDecimal minUnitPrice) {
		this.minUnitPrice = minUnitPrice;
	}




	public Integer getAllPositonNums() {
		return allPositonNums;
	}




	public void setAllPositonNums(Integer allPositonNums) {
		this.allPositonNums = allPositonNums;
	}




	public String getAddress() {
		return address;
	}




	public void setAddress(String address) {
		this.address = address;
	}




	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
