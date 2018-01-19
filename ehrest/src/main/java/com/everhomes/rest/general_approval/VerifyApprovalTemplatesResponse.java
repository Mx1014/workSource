package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>result: 0-未启用 1-已启用</li>
 * </ul>
 */
public class VerifyApprovalTemplatesResponse {

    private Byte result;

    public VerifyApprovalTemplatesResponse() {
    }

    public Byte getResult() {
        return result;
    }

    public void setResult(Byte result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
