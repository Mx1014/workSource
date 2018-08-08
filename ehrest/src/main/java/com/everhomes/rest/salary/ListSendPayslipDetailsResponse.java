// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>details: 工资条详情 {@link com.everhomes.rest.salary.PayslipDetailDTO}</li>
 * <li>titiles: 额外的表头列表</li>
 * <li>nextPageAnchor: 下页锚点</li>
 * </ul>
 */
public class ListSendPayslipDetailsResponse {

	@ItemType(PayslipDetailDTO.class)
	private List<PayslipDetailDTO> details;
	
	private List<String> titiles;

	private Long nextPageAnchor;
	public ListSendPayslipDetailsResponse() {

	}

	public ListSendPayslipDetailsResponse(List<PayslipDetailDTO> details, List<String> titiles, Long nextPageAnchor) {
		super();
		this.details = details;
		this.titiles = titiles;
		this.nextPageAnchor = nextPageAnchor;
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

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
}
