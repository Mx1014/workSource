package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>forms: forms {@link com.everhomes.rest.flow.FlowFormDTO}</li>
 * </ul>
 */
public class ListFlowFormsResponse {

    @ItemType(FlowFormDTO.class)
    private List<FlowFormDTO> forms;

    public List<FlowFormDTO> getForms() {
        return forms;
    }

    public void setForms(List<FlowFormDTO> forms) {
        this.forms = forms;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
