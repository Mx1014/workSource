// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

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
	private List<PrintRecordDTO> printRecordsList;

}
