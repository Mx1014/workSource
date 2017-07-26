// @formatter:off
package com.everhomes.rest.print;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>blackWhitePrice : 黑白价格</li>
 * <li>colorPrice : 彩色价格</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class PrintSettingColorTypeDTO {
	private BigDecimal blackWhitePrice;
	private BigDecimal colorPrice;
	public BigDecimal getBlackWhitePrice() {
		return blackWhitePrice;
	}
	public void setBlackWhitePrice(BigDecimal blackWhitePrice) {
		this.blackWhitePrice = blackWhitePrice;
	}
	public BigDecimal getColorPrice() {
		return colorPrice;
	}
	public void setColorPrice(BigDecimal colorPrice) {
		this.colorPrice = colorPrice;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
