package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>dtos: 审批列表(此处只需传递id与defaultOrder) {@link com.everhomes.rest.general_approval.GeneralApprovalDTO}</li>
 * </ul>
 */
public class OrderGeneralApprovalsCommand {

    @ItemType(GeneralApprovalDTO.class)
    private List<GeneralApprovalDTO> dtos;

    public OrderGeneralApprovalsCommand() {
    }

    public List<GeneralApprovalDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<GeneralApprovalDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
