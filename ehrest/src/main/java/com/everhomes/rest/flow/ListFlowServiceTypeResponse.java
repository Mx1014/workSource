package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>serviceTypes: 业务类别 {@link com.everhomes.rest.flow.FlowServiceTypeDTO}</li>
 * </ul>
 */
public class ListFlowServiceTypeResponse {

    @ItemType(FlowServiceTypeDTO.class)
    private List<FlowServiceTypeDTO> serviceTypes;

    public List<FlowServiceTypeDTO> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<FlowServiceTypeDTO> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
