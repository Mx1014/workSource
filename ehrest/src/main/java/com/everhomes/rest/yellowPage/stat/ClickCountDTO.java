package com.everhomes.rest.yellowPage.stat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>clickType: 点击类型：1-首页点击服务 3-进入详情 4-点击提交 5-点击咨询 6-点击分享 {@link com.everhomes.rest.yellowPage.stat.StatClickOrSortType}</li>
 * <li>clickName: 点击类型名称</li>
 * <li>showValue: 显示次数/转化率 </li>
 * </ul>
 */
public class ClickCountDTO {
	
	private Byte clickType;
	
	private String clickName;
	
	private String showValue;
	
	private Long showLongValue; //计算过程中使用，前端/客户端不需要使用
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getClickName() {
		return clickName;
	}

	public void setClickName(String clickName) {
		this.clickName = clickName;
	}

	public Byte getClickType() {
		return clickType;
	}

	public void setClickType(Byte clickType) {
		this.clickType = clickType;
	}

	public Long getShowLongValue() {
		return showLongValue;
	}

	public void setShowLongValue(Long showLongValue) {
		this.showLongValue = showLongValue;
	}

	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	public String getShowValue() {
		return showValue;
	}
}
