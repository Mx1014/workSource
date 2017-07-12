package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
* <ul>  
* <li>approveInfo：每一个审批的对象{@link com.everhomes.rest.enterprise.ApproveContactCommand}</li> 
* </ul>
*/
public class BatchApproveContactCommand { 
    @ItemType(ApproveContactCommand.class)
    private List<ApproveContactCommand> approveInfo;
 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<ApproveContactCommand> getApproveInfo() {
		return approveInfo;
	}

	public void setApproveInfo(List<ApproveContactCommand> approveInfo) {
		this.approveInfo = approveInfo;
	}
 

}
