package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowNodes;
import com.everhomes.util.StringHelper;

public class FlowNode extends EhFlowNodes {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7408368827319673502L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlowNode && this.getId().equals(((FlowNode) obj).getId());
    }
}
