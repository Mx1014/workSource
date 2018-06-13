// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>visitorId: (必填)访客id</li>
 * <li>ownerToken: (必填)公司/园区访客注册地址标识</li>
 * </ul>
 */
public class GetBookedVisitorByIdCommand extends BaseVisitorsysCommand{
    private Long visitorId;

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
