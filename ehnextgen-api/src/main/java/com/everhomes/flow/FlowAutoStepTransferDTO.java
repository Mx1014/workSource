package com.everhomes.flow;

import com.everhomes.rest.flow.*;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>transferOut: 从谁那里转出</li>
 *     <li>transferIn: 转交给谁</li>
 * </ul>
 */
public class FlowAutoStepTransferDTO extends FlowAutoStepDTO {

    private List<FlowEntitySel> transferOut;
    private List<FlowEntitySel> transferIn;

    public List<FlowEntitySel> getTransferOut() {
        return transferOut;
    }

    public void setTransferOut(List<FlowEntitySel> transferOut) {
        this.transferOut = transferOut;
    }

    public List<FlowEntitySel> getTransferIn() {
        return transferIn;
    }

    public void setTransferIn(List<FlowEntitySel> transferIn) {
        this.transferIn = transferIn;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
