package com.everhomes.yellowPage;

import com.everhomes.rest.yellowPage.AllianceTagDTO;
import com.everhomes.server.schema.tables.pojos.EhAllianceTagVals;

public class AllianceTagVal extends EhAllianceTagVals{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3235120033425164422L;
	
	private AllianceTagDTO tagDto; //筛选（子筛选）

	public AllianceTagDTO getTagDto() {
		return tagDto;
	}

	public void setTagDto(AllianceTagDTO tagDto) {
		this.tagDto = tagDto;
	}


}
