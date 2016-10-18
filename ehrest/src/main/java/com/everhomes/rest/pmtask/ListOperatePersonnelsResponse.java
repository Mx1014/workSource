package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationMemberDTO;

public class ListOperatePersonnelsResponse {
	
	private Integer nextPageOffset;
	private Long nextPageAnchor;
	
	@ItemType(OrganizationMemberDTO.class)
    private List<OrganizationMemberDTO> members;
}
