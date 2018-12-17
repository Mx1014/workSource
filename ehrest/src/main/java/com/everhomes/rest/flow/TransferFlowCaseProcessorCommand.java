package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>transfers: 转交列表 {@link com.everhomes.rest.flow.TransferFlowCaseProcessor}</li>
 * </ul>
 */
public class TransferFlowCaseProcessorCommand {

    @NotNull
    private List<TransferFlowCaseProcessor> transfers;

    public List<TransferFlowCaseProcessor> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<TransferFlowCaseProcessor> transfers) {
        this.transfers = transfers;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
