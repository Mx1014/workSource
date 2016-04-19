package com.everhomes.rest.community.admin;

import java.util.List;

import com.everhomes.discover.ItemType;


public class CommunityUserAddressResponse {

	private Long nextPageAnchor;
	
	@ItemType(CommunityUserAddressDTO.class)
	private List<CommunityUserAddressDTO> dtos;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<CommunityUserAddressDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<CommunityUserAddressDTO> dtos) {
		this.dtos = dtos;
	}
	
	
	
}
