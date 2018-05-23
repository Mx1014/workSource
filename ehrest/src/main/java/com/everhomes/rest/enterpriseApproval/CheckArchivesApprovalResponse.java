package com.everhomes.rest.enterpriseApproval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flag: 0-否 1-是</li>
 * </ul>
 */
public class CheckArchivesApprovalResponse {

    private Byte flag;

    public CheckArchivesApprovalResponse() {
    }

    public Byte getFlag() {
        return flag;
    }

    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
