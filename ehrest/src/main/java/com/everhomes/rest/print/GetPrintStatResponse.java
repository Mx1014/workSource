// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

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
	public PrintStatDTO getPrintStat() {
		return printStat;
	}
	public void setPrintStat(PrintStatDTO printStat) {
		this.printStat = printStat;
	}
	public PrintStatDTO getCopyStat() {
		return copyStat;
	}
	public void setCopyStat(PrintStatDTO copyStat) {
		this.copyStat = copyStat;
	}
	public PrintStatDTO getScanStat() {
		return scanStat;
	}
	public void setScanStat(PrintStatDTO scanStat) {
		this.scanStat = scanStat;
	}
	public PrintStatDTO getAllStat() {
		return allStat;
	}
	public void setAllStat(PrintStatDTO allStat) {
		this.allStat = allStat;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return StringHelper.toJsonString(this);
	}
}
