// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingCardRequests;
import com.everhomes.util.StringHelper;

public class ParkingCardRequest extends EhParkingCardRequests {
    private static final long serialVersionUID = 325431255481411415L;

    public ParkingCardRequest() {
    }
    
    private Integer ranking; 
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
}
