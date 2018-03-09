// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li> ul_printer: 解锁打印机</li>
 * <li> ul_client: 驱动扫码</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintScanTarget {
	UL_PRINTER("ul_printer"),UL_CLIENT("ul_client");
	
	private String code;

	private PrintScanTarget(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static PrintScanTarget fromCode(Byte code) {
		if(code == null)
			return null;
		for (PrintScanTarget t : PrintScanTarget.values()) {
			if (t.code.equals(code)) {
				return t;
			}
		}

		return null;
	}
}
