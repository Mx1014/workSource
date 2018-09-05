// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingCardRequests;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

public class ParkingCardRequest extends EhParkingCardRequests {
    private static final long serialVersionUID = 325431255481411415L;

    public ParkingCardRequest() {
    }
    
    private Integer ranking;

    private Timestamp anchor;
    
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

    public Timestamp getAnchor() {
        return anchor;
    }

    public void setAnchor(Timestamp anchor) {
        this.anchor = anchor;
    }
}
