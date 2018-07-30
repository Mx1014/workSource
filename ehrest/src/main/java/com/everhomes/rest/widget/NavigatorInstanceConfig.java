package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class NavigatorInstanceConfig implements Serializable  {

	/**
	 *
	 */
	private static final long serialVersionUID = -5152885247968357254L;

	private Byte cssStyleFlag;

    private String itemGroup;

	private Integer paddingTop;

	private Integer paddingLeft;

	private Integer paddingBottom;

	private Integer paddingRight;

	private Integer lineSpacing;

	private Integer columnSpacing;

	private String backgroundColor;

	private Byte allOrMoreFlag;

	private Byte allOrMoreType;

	private String allOrMoreName;

	private String allOrMoreIconUri;

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}

	public Integer getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(Integer paddingTop) {
		this.paddingTop = paddingTop;
	}

	public Integer getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(Integer paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public Integer getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(Integer paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public Integer getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(Integer paddingRight) {
		this.paddingRight = paddingRight;
	}

	public Integer getLineSpacing() {
		return lineSpacing;
	}

	public void setLineSpacing(Integer lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

	public Integer getColumnSpacing() {
		return columnSpacing;
	}

	public void setColumnSpacing(Integer columnSpacing) {
		this.columnSpacing = columnSpacing;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Byte getCssStyleFlag() {
		return cssStyleFlag;
	}

	public void setCssStyleFlag(Byte cssStyleFlag) {
		this.cssStyleFlag = cssStyleFlag;
	}

	public Byte getAllOrMoreFlag() {
		return allOrMoreFlag;
	}

	public void setAllOrMoreFlag(Byte allOrMoreFlag) {
		this.allOrMoreFlag = allOrMoreFlag;
	}

	public Byte getAllOrMoreType() {
		return allOrMoreType;
	}

	public void setAllOrMoreType(Byte allOrMoreType) {
		this.allOrMoreType = allOrMoreType;
	}

	public String getAllOrMoreName() {
		return allOrMoreName;
	}

	public void setAllOrMoreName(String allOrMoreName) {
		this.allOrMoreName = allOrMoreName;
	}

	public String getAllOrMoreIconUri() {
		return allOrMoreIconUri;
	}

	public void setAllOrMoreIconUri(String allOrMoreIconUri) {
		this.allOrMoreIconUri = allOrMoreIconUri;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
