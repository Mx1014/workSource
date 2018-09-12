// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>invoiceFlag: 是否支持发票 {@link ParkingConfigFlag} 0：不支持，1：支持</li>
 * <li>noticeFlag: 是否开启用户须知 {@link ParkingConfigFlag} 0：关闭，1：开启</li>
 * </ul>
 */
public class UserNoticeInvoiceDTO {
    private Byte invoiceFlag;
	private Byte noticeFlag;
    

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public Byte getNoticeFlag() {
		return noticeFlag;
	}

	public void setNoticeFlag(Byte noticeFlag) {
		this.noticeFlag = noticeFlag;
	}

	public UserNoticeInvoiceDTO() {
    }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
