package com.everhomes.point.rpc;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.point.PointScoreDTO;

public class PointScoreResponse extends RestResponseBase {

	private PointScoreDTO pointScoreDTO ;

	public PointScoreDTO getPointScoreDTO() {
		return pointScoreDTO;
	}

	public void setPointScoreDTO(PointScoreDTO pointScoreDTO) {
		this.pointScoreDTO = pointScoreDTO;
	}
	
}
