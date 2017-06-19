// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>startTime : 开始统计时间</li>
 * <li>endTime : 结束统计时间</li>
 * <li>orderStatus : 订单状态，全部则为空, 参考 {@link com.everhomes.rest.print.PrintOrderStatusType}</li>
 * <li>jobType : 任务类型，全部则为空, 参考 {@link com.everhomes.rest.print.PrintJobTypeType}</li>
 * <li>keywords: 关键字</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintRecordsCommand {

	private Byte orderStatus;
	private Byte jobType;
	private String keywords;
}
