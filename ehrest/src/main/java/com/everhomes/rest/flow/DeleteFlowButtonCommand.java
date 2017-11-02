// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowButtonId: 按钮id</li>
 * </ul>
 */
public class DeleteFlowButtonCommand {

    private Long flowButtonId;

    public Long getFlowButtonId() {
        return flowButtonId;
    }

    public void setFlowButtonId(Long flowButtonId) {
        this.flowButtonId = flowButtonId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
