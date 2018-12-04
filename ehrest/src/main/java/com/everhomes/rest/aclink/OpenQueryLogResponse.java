package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class OpenQueryLogResponse {
    private Long nextPageAnchor;
    
    @ItemType(OpenAclinkLogDTO.class)
    private List<OpenAclinkLogDTO> dtos;
    
    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }
    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
    public List<OpenAclinkLogDTO> getDtos() {
		return dtos;
	}
	public void setDtos(List<OpenAclinkLogDTO> dtos) {
		this.dtos = dtos;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
