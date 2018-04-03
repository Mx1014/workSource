// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 评价项名称</li>
 *     <li>flowMainId: 工作流id</li>
 *     <li>flowVersion: 工作流版本</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>inputFlag: 是否允许输入评价内容 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class FlowEvaluateItemDTO {

    private Long id;
    private String name;
    private Long flowMainId;
    private Integer flowVersion;
    private Integer namespaceId;
    private Byte inputFlag;

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getInputFlag() {
        return inputFlag;
    }

    public void setInputFlag(Byte inputFlag) {
        this.inputFlag = inputFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
