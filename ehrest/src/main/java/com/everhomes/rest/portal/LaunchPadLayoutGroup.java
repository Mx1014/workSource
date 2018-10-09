// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.rest.launchpad.EditFlagType;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <ul>
 *     <li>id: group id</li>
 *     <li>groupId: groupId</li>
 *     <li>groupName: 分组显示名称，用于显示，若显示时不需要名称则留空</li>
 *     <li>widget: 组内控件，如Navigator、Banners、Coupons、Posts"</li>
 *     <li>instanceConfig: json格式，说明：widget实例相关的配置，不需要时为空，如Default、GovAgencies、Bizs、GaActions、CallPhones、ActionBars</li>
 *     <li>style: 组内控件风格，如Default、Win8"</li>
 *     <li>defaultOrder: 组排列顺序</li>
 *     <li>separatorFlag: 组底部是否有分隔条，0: no, 1: yes"</li>
 *     <li>separatorHeight: 组底部分隔条高度</li>
 *     <li>columnCount: 组件一行显示的图标数，目前针对Navigator</li>
 *     <li>editFlag: 0-不可编辑,1-可编辑 详情{@link EditFlagType}</li>
 *     <li>title: 标题</li>
 *     <li>iconUrl: 标题图片</li>
 *     <li>align: align</li>
 *     <li>titleFlag: 是否有标题，5.8.4之后：0-无，1-居左，2-居中, 参考{@link TitleFlag}</li>
 *     <li>titleUrl: titleUrl</li>
 *     <li>titleStyle: 样式，0-无标题，101,102,103,104为居左样式，201,202,203为居中样式，参考{@link TitleStyle}</li>
 *     <li>subTitle: subTitle</li>
 *     <li>titleSize: 标题大小，0-小，1-中，2-大，参考{@link TitleSize}</li>
 *     <li>titleMoreFlag: 标题中是否带了“更多”，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class LaunchPadLayoutGroup {
    private Long id;
    private Long groupId;
    private String groupName;
    private String widget;
    private Object instanceConfig;
    private String style;
    private Integer defaultOrder;
    private Integer separatorFlag;
    private Double separatorHeight;
    private Integer columnCount;
    private Byte editFlag;
    private String title;
    private String iconUrl;
    private String align;

    private Byte titleFlag;

    private String titleUrl;

    private Integer titleStyle;

    private String subTitle;

    private Byte titleSize;

    private Byte titleMoreFlag;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }
//
//    public String getInstanceConfig() {
//        return instanceConfig;
//    }
//
//    public void setInstanceConfig(String instanceConfig) {
//        this.instanceConfig = instanceConfig;
//    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Integer getSeparatorFlag() {
        return separatorFlag;
    }

    public void setSeparatorFlag(Integer separatorFlag) {
        this.separatorFlag = separatorFlag;
    }

    public Double getSeparatorHeight() {
        return separatorHeight;
    }

    public void setSeparatorHeight(Double separatorHeight) {
        this.separatorHeight = separatorHeight;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
    }

    public Byte getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(Byte editFlag) {
        this.editFlag = editFlag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public Object getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(Object instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    public Byte getTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(Byte titleFlag) {
        this.titleFlag = titleFlag;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public Integer getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(Integer titleStyle) {
        this.titleStyle = titleStyle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Byte getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(Byte titleSize) {
        this.titleSize = titleSize;
    }

    public Byte getTitleMoreFlag() {
        return titleMoreFlag;
    }

    public void setTitleMoreFlag(Byte titleMoreFlag) {
        this.titleMoreFlag = titleMoreFlag;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
