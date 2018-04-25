// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>details: 工资条详情</li>
 * <li>titiles: 额外的表头列表</li>
 * </ul>
 */
public class ListSendPayslipDetailsResponse {

	@ItemType(PayslipDetailDTO.class)
	private List<PayslipDetailDTO> details;
	
	private List<String> titiles;
	public ListSendPayslipDetailsResponse() {

	}

	public ListSendPayslipDetailsResponse(List<PayslipDetailDTO> details) {
		super();
		this.details = details;
	}

	public List<PayslipDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<PayslipDetailDTO> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<String> getTitiles() {
		return titiles;
	}

	public void setTitiles(List<String> titiles) {
		this.titiles = titiles;
	}

}
