package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;
/**
* <ul>  
* <li>enterpriseId：企业Id</li>
* <li>userId: 用户id </li>
 * <li>operateType: 审核类型,请参考{@link com.everhomes.rest.community.admin.OperateType}</li>
* </ul>
*/
public class ApproveContactCommand {
    private Long enterpriseId;
    
    private Long userId;

    private Byte operateType;

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
