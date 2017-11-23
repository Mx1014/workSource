// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>jobTypeList : 任务类型列表，参考 {@link com.everhomes.rest.print.PrintJobTypeType} </li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintJobTypesResponse {
	@ItemType(Byte.class)
	private List<Byte> jobTypeList;
	
	public ListPrintJobTypesResponse() {
	}
	
	public ListPrintJobTypesResponse(List<Byte> jobTypeList) {
		this.jobTypeList = jobTypeList;
	}

	public List<Byte> getJobTypeList() {
		return jobTypeList;
	}

	public void setJobTypeList(List<Byte> jobTypeList) {
		this.jobTypeList = jobTypeList;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
