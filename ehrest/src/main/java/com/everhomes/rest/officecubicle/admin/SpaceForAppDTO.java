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
 * <li>coverUrl:封面url</li>
 * <li>rentType:1长租，0短租</li>
 * </ul>
 */
public class SpaceForAppDTO {
    private Long spaceId;
    private String spaceName;
    private BigDecimal minUnitPrice;
    private Integer allPositonNums;
    private String address;
    private String coverUrl;
    private Byte rentType;
	private Integer dailyPrice;
	private Integer halfdailyPrice;
	private Byte needPay;

	
    public Byte getNeedPay() {
		return needPay;
	}
	public void setNeedPay(Byte needPay) {
		this.needPay = needPay;
	}

	public Integer getDailyPrice() {
		return dailyPrice;
	}




	public void setDailyPrice(Integer dailyPrice) {
		this.dailyPrice = dailyPrice;
	}




	public Integer getHalfdailyPrice() {
		return halfdailyPrice;
	}




	public void setHalfdailyPrice(Integer halfdailyPrice) {
		this.halfdailyPrice = halfdailyPrice;
	}




	public Byte getRentType() {
		return rentType;
	}




	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}




	public String getCoverUrl() {
		return coverUrl;
	}




	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}




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
