package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class TrackBillResponse {
	@ItemType(ExpressTraceDTO.class)
	private List<ExpressTraceDTO> traces;

	public List<ExpressTraceDTO> getTraces() {
		return traces;
	}

	public void setTraces(List<ExpressTraceDTO> traces) {
		this.traces = traces;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
