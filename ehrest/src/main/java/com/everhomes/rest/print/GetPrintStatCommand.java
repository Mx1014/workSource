// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>startTime : 开始统计时间</li>
 * <li>endTime : 结束统计时间</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class GetPrintStatCommand {
	private String ownerType;
	private Long ownerId;
    private Long startTime;
    private Long endTime;
}
