// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>ownerToken: (必填)公司/园区访客注册地址标识</li>
 * <li>pmId: (选填)管理公司id</li>
 * <li>statusFlag: (选填)接受消息状态 0不接受，1接受，参考 {@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 * </ul>
 */
public class UpdateMessageReceiverCommand  extends  BaseVisitorsysCommand{
    private Byte statusFlag;

    public Byte getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Byte statusFlag) {
        this.statusFlag = statusFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
