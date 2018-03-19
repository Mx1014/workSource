// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class GetPrintQrcodeCommand {
	private String data;
	private Integer height;
	private Integer width;
	
	
	
	public GetPrintQrcodeCommand() {
		super();
	}

	public GetPrintQrcodeCommand(String data, Integer height, Integer width) {
		super();
		this.data = data;
		this.height = height;
		this.width = width;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
