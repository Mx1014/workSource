// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值: 
 * <li>paymentMonth: 月份如201702</li>
 * <li>paySocialSecurityNumber: 社保在缴人数</li>
 * <li>payAccumulationFundNumber: 公积金在缴人数</li>
 * <li>increaseNumber: 增员人数</li>
 * <li>decreaseNumber: 减员人数</li>
 * <li>inWorkNumber: 入职人数</li>
 * <li>outWorkNumber: 离职人数</li>
 * </ul>
 */
public class ListSocialSecurityEmployeeStatusResponse {

 
	private String paymentMonth;

	private Integer paySocialSecurityNumber;
	private Integer payAccumulationFundNumber; 
	private Integer increaseNumber; 
	private Integer decreaseNumber; 
	private Integer inWorkNumber; 
	private Integer outWorkNumber;
	
	public ListSocialSecurityEmployeeStatusResponse() {

	} 
	
	
	public String getPaymentMonth() {
		return paymentMonth;
	}


	public void setPaymentMonth(String paymentMonth) {
		this.paymentMonth = paymentMonth;
	}


	public Integer getPaySocialSecurityNumber() {
		return paySocialSecurityNumber;
	}


	public void setPaySocialSecurityNumber(Integer paySocialSecurityNumber) {
		this.paySocialSecurityNumber = paySocialSecurityNumber;
	}


	public Integer getPayAccumulationFundNumber() {
		return payAccumulationFundNumber;
	}


	public void setPayAccumulationFundNumber(Integer payAccumulationFundNumber) {
		this.payAccumulationFundNumber = payAccumulationFundNumber;
	}


	public Integer getIncreaseNumber() {
		return increaseNumber;
	}


	public void setIncreaseNumber(Integer increaseNumber) {
		this.increaseNumber = increaseNumber;
	}


	public Integer getDecreaseNumber() {
		return decreaseNumber;
	}


	public void setDecreaseNumber(Integer decreaseNumber) {
		this.decreaseNumber = decreaseNumber;
	}


	public Integer getInWorkNumber() {
		return inWorkNumber;
	}


	public void setInWorkNumber(Integer inWorkNumber) {
		this.inWorkNumber = inWorkNumber;
	}


	public Integer getOutWorkNumber() {
		return outWorkNumber;
	}


	public void setOutWorkNumber(Integer outWorkNumber) {
		this.outWorkNumber = outWorkNumber;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
