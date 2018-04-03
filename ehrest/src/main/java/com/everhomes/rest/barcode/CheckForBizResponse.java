package com.everhomes.rest.barcode;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>result: result</li>
 *     <li>body: body {@link com.everhomes.rest.barcode.BarcodeDTO}</li>
 * </ul>
 */
public class CheckForBizResponse {
    private boolean result;
    private BarcodeDTO body;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public BarcodeDTO getBody() {
        return body;
    }

    public void setBody(BarcodeDTO body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
