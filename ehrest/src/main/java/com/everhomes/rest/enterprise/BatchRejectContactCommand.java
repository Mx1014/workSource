package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rejectText：拒绝说明</li>
 * <li>rejectInfo: 拒绝信息{@link com.everhomes.rest.enterprise.RejectContactCommand}</li> 
 * </ul>
 */
public class BatchRejectContactCommand { 
    @ItemType(RejectContactCommand.class)
    private List<RejectContactCommand> rejectInfo;
    
    private String rejectText;
 
  
    public String getRejectText() {
        return rejectText;
    }

    public void setRejectText(String rejectText) {
        this.rejectText = rejectText;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<RejectContactCommand> getRejectInfo() {
		return rejectInfo;
	}

	public void setRejectInfo(List<RejectContactCommand> rejectInfo) {
		this.rejectInfo = rejectInfo;
	} 
}
