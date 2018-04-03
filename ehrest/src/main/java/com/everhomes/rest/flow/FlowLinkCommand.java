package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>displayName: 显示名称</li>
 *     <li>linkLevel: 连接level</li>
 *     <li>fromNodeLevel: 起始节点level</li>
 *     <li>fromNodeId: 起始节点id</li>
 *     <li>toNodeLevel: 结束节点level</li>
 *     <li>toNodeId: 结束节点id</li>
 * </ul>
 */
public class FlowLinkCommand {

    private String displayName;
    private Integer linkLevel;
    private Integer fromNodeLevel;
    private Long fromNodeId;
    private Integer toNodeLevel;
    private Long toNodeId;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(Long fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public Long getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(Long toNodeId) {
        this.toNodeId = toNodeId;
    }

    public Integer getFromNodeLevel() {
        return fromNodeLevel;
    }

    public void setFromNodeLevel(Integer fromNodeLevel) {
        this.fromNodeLevel = fromNodeLevel;
    }

    public Integer getToNodeLevel() {
        return toNodeLevel;
    }

    public void setToNodeLevel(Integer toNodeLevel) {
        this.toNodeLevel = toNodeLevel;
    }

    public Integer getLinkLevel() {
        return linkLevel;
    }

    public void setLinkLevel(Integer linkLevel) {
        this.linkLevel = linkLevel;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
