package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.flow.FlowServiceTypeDTO}</li>
 * </ul>
 */
public class ListFlowModuleAppServiceTypesResponse {

    private List<FlowServiceTypeDTO> dtos;

    public List<FlowServiceTypeDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FlowServiceTypeDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
