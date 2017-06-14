package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowSubjects;
import com.everhomes.util.StringHelper;

public class FlowSubject extends EhFlowSubjects {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5020988711127444590L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
