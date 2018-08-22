package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.util.StringHelper;
/**
* 
* <ul>
* <li>nextPageAnchor : 下一页锚点</li>
* <li>dtos : 工作流list{@link com.everhomes.rest.flow.FlowDTO.FlowDTO}</li>
* </ul>
**/
public class GetWorkFlowListResponse {
	
	private Long nextPageAnchor;
	private  List<FlowDTO> dtos;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<FlowDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<FlowDTO> dtos) {
		this.dtos = dtos;
	}

}
