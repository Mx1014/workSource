// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>statusFlag: (选填)接受消息状态 0不接受，1接受，参考 {@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 * </ul>
 */
public class GetMessageReceiverForManageResponse{
    private Byte statusFlag;

    public GetMessageReceiverForManageResponse() {
    }

    public GetMessageReceiverForManageResponse(Byte statusFlag) {
        this.statusFlag = statusFlag;
    }

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
