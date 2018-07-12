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
 * <li>ownerToken: (必填)公司/园区访客注册地址标识</li>
 * <li>pmId: (选填)管理公司id</li>
 * <li>ownerToken: (必填)公司/园区访客注册地址标识</li>
 * <li>visitorPhone: (必填)电话号码</li>
 * </ul>
 */
public class SendSMSVerificationCodeForWebCommand extends BaseVisitorsysCommand{
    private String visitorPhone;

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
