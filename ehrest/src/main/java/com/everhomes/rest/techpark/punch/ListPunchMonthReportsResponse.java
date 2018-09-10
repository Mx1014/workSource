// @formatter:off
package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>monthReports: 月报表列表参考 {@link com.everhomes.rest.techpark.punch.PunchMonthReportDTO}</li>
 * </ul>
 */
public class ListPunchMonthReportsResponse {

	private Long nextPageAnchor;

	@ItemType(PunchMonthReportDTO.class)
	private List<PunchMonthReportDTO> monthReports;

	public ListPunchMonthReportsResponse() {

	}

	public ListPunchMonthReportsResponse(Long nextPageAnchor, List<PunchMonthReportDTO> monthReports) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.monthReports = monthReports;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<PunchMonthReportDTO> getMonthReports() {
		return monthReports;
	}

	public void setMonthReports(List<PunchMonthReportDTO> monthReports) {
		this.monthReports = monthReports;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
