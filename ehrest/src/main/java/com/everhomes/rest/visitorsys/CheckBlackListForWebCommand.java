// @formatter:off
package com.everhomes.rest.visitorsys;


import com.everhomes.rest.visitorsys.BaseVisitorsysCommand;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>deviceId: (必填)设备唯一标识</li>
 * <li>enterpriseId: (必填)公司id</li>
 * </ul>
 */
public class CheckBlackListForWebCommand extends BaseVisitorsysCommand{
    private String visitorPhone;

    private Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
