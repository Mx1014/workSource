package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>params: 参数列表 {@link com.everhomes.rest.flow.FlowPredefinedParamDTO}</li>
 * </ul>
 */
public class ListFlowPredefinedParamResponse {

    @ItemType(FlowPredefinedParamDTO.class)
    private List<FlowPredefinedParamDTO> params;

    public List<FlowPredefinedParamDTO> getParams() {
        return params;
    }

    public void setParams(List<FlowPredefinedParamDTO> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
