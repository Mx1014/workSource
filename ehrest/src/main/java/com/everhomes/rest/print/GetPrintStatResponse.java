// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li>printStat : 打印统计， 参考 {@link com.everhomes.rest.print.PrintStatDTO}</li>
 * <li>copyStat : 复印统计， 参考 {@link com.everhomes.rest.print.PrintStatDTO}</li>
 * <li>scanStat : 扫描统计， 参考 {@link com.everhomes.rest.print.PrintStatDTO}</li>
 * <li>allStat : 所有统计总和， 参考 {@link com.everhomes.rest.print.PrintStatDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class GetPrintStatResponse {
	private PrintStatDTO printStat;
	private PrintStatDTO copyStat;
	private PrintStatDTO scanStat;
	private PrintStatDTO allStat;
}
