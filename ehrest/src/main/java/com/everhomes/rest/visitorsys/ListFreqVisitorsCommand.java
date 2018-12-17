package com.everhomes.rest.visitorsys;


/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>ownerToken: (必填)公司/园区访客注册地址标识</li>
 * <li>pmId: (选填)管理公司id</li>
 * <li>communityType: (必填)园区类型 {@link com.everhomes.rest.community.CommunityType}</li>
 * </ul>
 */
public class ListFreqVisitorsCommand extends BaseVisitorsysCommand {
    private String visitorPhone;

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }
}
