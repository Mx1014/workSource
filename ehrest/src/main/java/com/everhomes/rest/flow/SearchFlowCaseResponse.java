// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>flowCases: flowCase列表{@link com.everhomes.rest.flow.FlowCaseDTO}</li>
 *     <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class SearchFlowCaseResponse {
    @ItemType(FlowCaseDTO.class)
    private List<FlowCaseDTO> flowCases;
    private Long nextPageAnchor;

    public List<FlowCaseDTO> getFlowCases() {
        return flowCases;
    }

    public void setFlowCases(List<FlowCaseDTO> flowCases) {
        this.flowCases = flowCases;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
