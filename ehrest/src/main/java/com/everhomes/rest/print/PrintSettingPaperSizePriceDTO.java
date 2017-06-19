// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>athreePrice : A3价格, 参考 {@link com.everhomes.rest.print.PrintSettingColorTypeDTO}</li>
 * <li>afourPrice : A4价格，参考 {@link com.everhomes.rest.print.PrintSettingColorTypeDTO}</li>
 * <li>afivePrice : A5价格，参考 {@link com.everhomes.rest.print.PrintSettingColorTypeDTO}</li>
 * <li>asixPrice : A6价格，参考 {@link com.everhomes.rest.print.PrintSettingColorTypeDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class PrintSettingPaperSizePriceDTO {
	private PrintSettingColorTypeDTO athreePrice;
	private PrintSettingColorTypeDTO afourPrice;
	private PrintSettingColorTypeDTO afivePrice;
	private PrintSettingColorTypeDTO asixPrice;
	public PrintSettingColorTypeDTO getAthreePrice() {
		return athreePrice;
	}
	public void setAthreePrice(PrintSettingColorTypeDTO athreePrice) {
		this.athreePrice = athreePrice;
	}
	public PrintSettingColorTypeDTO getAfourPrice() {
		return afourPrice;
	}
	public void setAfourPrice(PrintSettingColorTypeDTO afourPrice) {
		this.afourPrice = afourPrice;
	}
	public PrintSettingColorTypeDTO getAfivePrice() {
		return afivePrice;
	}
	public void setAfivePrice(PrintSettingColorTypeDTO afivePrice) {
		this.afivePrice = afivePrice;
	}
	public PrintSettingColorTypeDTO getAsixPrice() {
		return asixPrice;
	}
	public void setAsixPrice(PrintSettingColorTypeDTO asixPrice) {
		this.asixPrice = asixPrice;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
