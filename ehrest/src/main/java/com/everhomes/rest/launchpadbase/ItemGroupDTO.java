package com.everhomes.rest.launchpadbase;

import com.everhomes.rest.portal.TitleSize;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>groupId: groupId</li>
 *     <li>groupName: groupName</li>
 *     <li>widget: widget</li>
 *     <li>instanceConfig: instanceConfig 根据widget参考com.everhomes.rest.launchpadbase.groupinstanceconfig包下的组件</li>
 *     <li>style: style</li>
 *     <li>defaultOrder: defaultOrder</li>
 *     <li>separatorFlag: separatorFlag</li>
 *     <li>separatorHeight: separatorHeight</li>
 *     <li>columnCount: columnCount</li>
 *     <li>title: 标题名称</li>
 *     <li>titleUri: 标题图标uri</li>
 *     <li>titleUrl: 标题图标</li>
 *     <li>titleFlag: 是否有标题，5.8.4之后：0-无，1-居左，2-居中, 参考{@link com.everhomes.rest.portal.TitleFlag}</li>
 *     <li>titleStyle: 样式，0-无标题，101,102,103,104为居左样式，201,202,203为居中样式，参考{@link com.everhomes.rest.portal.TitleStyle}</li>
 *     <li>subTitle: subTitle</li>
 *     <li>titleSize: 标题大小，1-小，2-中，3-大，参考{@link TitleSize}</li>
 *     <li>titleMoreFlag: 标题中是否带了“更多”，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ItemGroupDTO {

    private Long groupId;
    private String groupName;
    private String widget;
    private Object instanceConfig;
    private String style;
    private Integer defaultOrder;
    private Integer separatorFlag;
    private Integer separatorHeight;
    private Integer columnCount;

    private String title;
    private String titleUri;
    private String titleUrl;

    private Byte titleFlag;

    private Integer titleStyle;

    private String subTitle;

    private Byte titleSize;

    private Byte titleMoreFlag;


    public String getTitleUri() {
        return titleUri;
    }

    public void setTitleUri(String titleUri) {
        this.titleUri = titleUri;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public Object getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(Object instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

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

    public Integer getSeparatorHeight() {
        return separatorHeight;
    }

    public void setSeparatorHeight(Integer separatorHeight) {
        this.separatorHeight = separatorHeight;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public Byte getTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(Byte titleFlag) {
        this.titleFlag = titleFlag;
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
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
