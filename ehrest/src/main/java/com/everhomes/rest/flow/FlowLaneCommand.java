package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 创建时不传，更新时传</li>
 *     <li>displayName: 泳道名称</li>
 *     <li>laneLevel: 泳道level</li>
 *     <li>flowNodeLevel: 第一个节点level</li>
 *     <li>identifierNodeLevel: 申请人按钮组的默认节点设置值</li>
 * </ul>
 */
public class FlowLaneCommand {

    private Long id;
    private String displayName;
    private Integer laneLevel;
    private Integer flowNodeLevel;
    private Integer identifierNodeLevel;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLaneLevel() {
        return laneLevel;
    }

    public void setLaneLevel(Integer laneLevel) {
        this.laneLevel = laneLevel;
    }

    public Integer getFlowNodeLevel() {
        return flowNodeLevel;
    }

    public void setFlowNodeLevel(Integer flowNodeLevel) {
        this.flowNodeLevel = flowNodeLevel;
    }

    public Integer getIdentifierNodeLevel() {
        return identifierNodeLevel;
    }

    public void setIdentifierNodeLevel(Integer identifierNodeLevel) {
        this.identifierNodeLevel = identifierNodeLevel;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
