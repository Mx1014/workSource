package com.everhomes.rest.portal;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>id: 门户itemGroup的id</li>
 * <li>layoutId: 门户layout的id</li>
 * <li>label: 门户itemGroup名称</li>
 * <li>description: 门户itemGroup描述</li>
 * <li>separatorFlag: 底部隔栏flag</li>
 * <li>separatorHeight: 底部隔栏高度</li>
 * <li>widget: 组件</li>
 * <li>style: 组件样式</li>
 * <li>instanceConfig: 组件的配置参数，具体参数具体定义</li>
 * <li>createTime: 创建时间</li>
 * <li>updateTime: 修改时间</li>
 * <li>creatorUid: 创建人id</li>
 * <li>creatorUName: 创建人名称</li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorUName: 操作人名称</li>
 * </ul>
 */
public class PortalItemGroupDTO {

    private Long id;
    private Long layoutId;
    private String label;
    private Byte separatorFlag;
    private BigDecimal separatorHeight;
    private String widget;
    private String style;
    private String instanceConfig;
    private Integer defaultorder;
    private Long createTime;
    private Long updateTime;
    private Long operatorUid;
    private Long creatorUid;
    private String creatorUName;
    private String operatorUName;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Byte getSeparatorFlag() {
        return separatorFlag;
    }

    public void setSeparatorFlag(Byte separatorFlag) {
        this.separatorFlag = separatorFlag;
    }

    public BigDecimal getSeparatorHeight() {
        return separatorHeight;
    }

    public void setSeparatorHeight(BigDecimal separatorHeight) {
        this.separatorHeight = separatorHeight;
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

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    public Integer getDefaultorder() {
        return defaultorder;
    }

    public void setDefaultorder(Integer defaultorder) {
        this.defaultorder = defaultorder;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreatorUName() {
        return creatorUName;
    }

    public void setCreatorUName(String creatorUName) {
        this.creatorUName = creatorUName;
    }

    public String getOperatorUName() {
        return operatorUName;
    }

    public void setOperatorUName(String operatorUName) {
        this.operatorUName = operatorUName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
