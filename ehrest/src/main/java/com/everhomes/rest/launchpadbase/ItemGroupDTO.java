package com.everhomes.rest.launchpadbase;

import com.everhomes.util.StringHelper;

import java.util.List;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
