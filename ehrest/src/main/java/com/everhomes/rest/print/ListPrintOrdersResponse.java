// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>nextPageAnchor: 分页的锚点，下一页开始取数据的位置</li>
 * <li>printOrdersList: 打印记录列表，参考{@link com.everhomes.rest.print.PrintOrderDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintOrdersResponse {
	private Long nextPageAnchor;
	@ItemType(PrintOrderDTO.class)
	private List<PrintOrderDTO> printOrdersList;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<PrintOrderDTO> getPrintOrdersList() {
		return printOrdersList;
	}
	public void setPrintOrdersList(List<PrintOrderDTO> printOrdersList) {
		this.printOrdersList = printOrdersList;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
