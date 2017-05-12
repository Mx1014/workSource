package com.everhomes.rest.widget;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

public class NavigatorDesignConfig implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private Integer paddingTop;

	private Integer paddingLeft;

	private Integer paddingBottom;

	private Integer paddingRight;

	private Integer lineSpacing;

	private Integer columnSpacing;

	private String backgroundColor;

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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
