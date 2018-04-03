// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowId: 工作流id</li>
 *     <li>itemId: itemId</li>
 *     <li>name: name</li>
 *     <li>inputFlag: inputFlag</li>
 * </ul>
 */
public class CreateFlowEvaluateItemCommand {

    private Long flowId;
    private Long itemId;
    private String name;
    private Byte inputFlag;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getName() {
        return name;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
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
