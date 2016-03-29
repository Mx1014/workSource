package com.everhomes.rest.videoconf;


import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>confCapacity: 账号类型 0-25方 1-50方 2-100方</li>
 *  <li>price: 正常价格</li>
 *  <li>mutipleNum: 多账号临界个数 </li>
 *  <li>mutiplePrice: 多账号购买价格 </li>
 * </ul>
 *
 */
public class ConfCategoryDTO {
	
	private Byte confCapacity;
	
	private BigDecimal price;
	
	private int mutipleNum;
	
	private BigDecimal mutiplePrice;
	
	public Byte getConfCapacity() {
		return confCapacity;
	}

	public void setConfCapacity(Byte confCapacity) {
		this.confCapacity = confCapacity;
	}
	

	public int getMutipleNum() {
		return mutipleNum;
	}

	public void setMutipleNum(int mutipleNum) {
		this.mutipleNum = mutipleNum;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMutiplePrice() {
		return mutiplePrice;
	}

	public void setMutiplePrice(BigDecimal mutiplePrice) {
		this.mutiplePrice = mutiplePrice;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }	
}
