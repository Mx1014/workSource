package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 配置列表 {@link com.everhomes.rest.flow.FlowScriptConfigDTO}</li>
 * </ul>
 */
public class ListFlowScriptConfigsResponse {

    private List<FlowScriptConfigDTO> dtos;

    public List<FlowScriptConfigDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FlowScriptConfigDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
