package com.everhomes.rest.ems;

import java.util.List;

import com.everhomes.util.StringHelper;

public class TrackBillResponse {
	private List<TrackBillDTO> traces;

	public List<TrackBillDTO> getTraces() {
		return traces;
	}

	public void setTraces(List<TrackBillDTO> traces) {
		this.traces = traces;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
