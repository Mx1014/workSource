package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlows;
import com.everhomes.util.StringHelper;

public class Flow extends EhFlows {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7283749249293245544L;
	
	/**
	 * 总是获取最开始版本的 ID
	 * @return
	 */
	public Long getTopId() {
		if(this.getFlowMainId() == null || this.getFlowMainId().equals(0l)) {
			return this.getId();
		}
		
		return this.getFlowMainId();
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
