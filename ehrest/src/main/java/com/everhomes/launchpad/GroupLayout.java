package com.everhomes.launchpad;

import com.google.gson.JsonObject;

/**
 * <ul>
 * <li>groupName: 组名字</li>
 * <li>widget: 组件类型，参考{@link com.everhomes.launchpad.Widget}</li>
 * <li>instanceConfig:  json格式，说明：widget实例相关的配置，不需要时为空，如Default、GovAgencies、Bizs、GaActions、CallPhones、ActionBars</li>
 * <li>style: 样式风格参考{@link com.everhomes.launchpad.Style}</li>
 * <li>defaultOrder: 排序</li>
 * <li>separatorFlag: 组底部是否有分隔条，0: no, 1: yes</li>
 * <li>separatorHeight: 间隔高度</li>
 * <li>columnCount: 组件一行显示的图标数，目前针对Navigator</li>
 * </ul>
 */
public class GroupLayout {
    private String groupName;
    private String widget;
    private JsonObject instanceConfig;
    private String style;
    private Integer defaultOrder;
    private Integer separatorFlag;
    private Double separatorHeight;
    private Integer columnCount;
    
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
    public JsonObject getInstanceConfig() {
        return instanceConfig;
    }
    public void setInstanceConfig(JsonObject instanceConfig) {
        this.instanceConfig = instanceConfig;
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
    
}
