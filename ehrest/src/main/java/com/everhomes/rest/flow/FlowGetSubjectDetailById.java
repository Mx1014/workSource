package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowGetSubjectDetailById {
	Long subjectId;

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
