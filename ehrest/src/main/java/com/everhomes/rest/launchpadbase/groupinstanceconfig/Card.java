package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>cssStyleFlag: cssStyleFlag</li>
 *     <li>itemGroup: itemGroup</li>
 *     <li>paddingTop: paddingTop</li>
 *     <li>paddingLeft: paddingLeft</li>
 *     <li>paddingBottom: paddingBottom</li>
 *     <li>paddingRight: paddingRight</li>
 *     <li>lineSpacing: lineSpacing</li>
 *     <li>columnSpacing: columnSpacing</li>
 *     <li>backgroundColor: backgroundColor</li>
 *     <li>appType: 内容类型 0-oa，1-community，2-服务应用 参考{@link ServiceModuleAppType}</li>
 * </ul>
 */
public class Card {

    private Integer cssStyleFlag;
    private String itemGroup;
    private Integer paddingTop;
    private Integer paddingLeft;
    private Integer paddingBottom;
    private Integer paddingRight;
    private Integer lineSpacing;
    private Integer columnSpacing;
    private String backgroundColor;
    private Byte appType;

    public Integer getCssStyleFlag() {
        return cssStyleFlag;
    }

    public void setCssStyleFlag(Integer cssStyleFlag) {
        this.cssStyleFlag = cssStyleFlag;
    }

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

    public Byte getAppType() {
        return appType;
    }

    public void setAppType(Byte appType) {
        this.appType = appType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
