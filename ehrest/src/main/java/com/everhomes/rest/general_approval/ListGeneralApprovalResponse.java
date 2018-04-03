package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>dtos : 审批列表{@link com.everhomes.rest.general_approval.GeneralApprovalDTO}</li>
 * </ul>
 * */
public class ListGeneralApprovalResponse {
	@ItemType(GeneralApprovalDTO.class)
	List<GeneralApprovalDTO> dtos;

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
