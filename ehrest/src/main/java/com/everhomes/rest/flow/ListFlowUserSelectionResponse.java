package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>selections: selections {@link com.everhomes.rest.flow.FlowUserSelectionDTO}</li>
 * </ul>
 */
public class ListFlowUserSelectionResponse {
    @ItemType(FlowUserSelectionDTO.class)
    private List<FlowUserSelectionDTO> selections;

    public ListFlowUserSelectionResponse() {
        selections = new ArrayList<FlowUserSelectionDTO>();
    }

    public List<FlowUserSelectionDTO> getSelections() {
        return selections;
    }

    public void setSelections(List<FlowUserSelectionDTO> selections) {
        this.selections = selections;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
