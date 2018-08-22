package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.util.StringHelper;
/**
* <ul>
* <li>nextPageAnchor : 下一页锚点</li>
* <li>dtos : 表单信息{@link com.everhomes.rest.general_approval.GeneralFormDTO}</li>
* </ul>
**/
public class GetFormListResponse {
	
	private Long nextPageAnchor;
	private  List<GeneralFormDTO> dtos;
	
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

	public List<GeneralFormDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<GeneralFormDTO> dtos) {
		this.dtos = dtos;
	}
}


