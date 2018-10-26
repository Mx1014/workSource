package com.everhomes.rest.organization.pm.reportForm;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 * 	<li>resultList：结果集</li>
 *	<li>nextPageAnchor</li>
 *</ul>
 */
public class ListCommunityReportFormResponse {
	
	private List<CommunityReportFormDTO> resultList;
	private Long nextPageAnchor;
	
	public List<CommunityReportFormDTO> getResultList() {
		return resultList;
	}
	public void setResultList(List<CommunityReportFormDTO> resultList) {
		this.resultList = resultList;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
