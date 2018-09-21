// @formatter:off
package com.everhomes.rest.print;

import java.util.Map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>sourceType : ul_printer 解锁打印机/驱动扫码 {@link com.everhomes.rest.print.PrintScanTarget}</li>
 * <li>printerName : 打印机名称，获取打印列表时需要传打印机名称</li>
 * </ul>
 *
 *  @author:dengs 2018年2月11日
 */
public class UnlockPrinterResponse {
	@Deprecated
	private String url;
	@Deprecated
	@ItemType(String.class)
	private Map<String,String> params;
	private String sourceType;
	private String printerName; 
	
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public UnlockPrinterResponse(String url, Map<String, String> params) {
		super();
		this.url = url;
		this.params = params;
	}
	public UnlockPrinterResponse() {
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getPrinterName() {
		return printerName;
	}
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	
}
