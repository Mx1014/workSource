// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>nextPageAnchor: 分页的锚点，下一页开始取数据的位置</li>
 * <li>printRecordsList: 打印记录列表，参考{@link com.everhomes.rest.print.PrintRecordDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintRecordsResponse {
	private Long nextPageAnchor;
	@ItemType(PrintRecordDTO.class)
	private List<PrintRecordDTO> printRecordsList;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<PrintRecordDTO> getPrintRecordsList() {
		return printRecordsList;
	}
	public void setPrintRecordsList(List<PrintRecordDTO> printRecordsList) {
		this.printRecordsList = printRecordsList;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
