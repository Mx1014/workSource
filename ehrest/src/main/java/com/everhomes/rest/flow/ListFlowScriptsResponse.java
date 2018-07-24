// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 脚本列表 {@@link com.everhomes.rest.flow.FlowScriptDTO}</li>
 *     <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListFlowScriptsResponse {

    private List<FlowScriptDTO> dtos;
    private Long nextPageAnchor;

    public List<FlowScriptDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FlowScriptDTO> dtos) {
        this.dtos = dtos;
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
