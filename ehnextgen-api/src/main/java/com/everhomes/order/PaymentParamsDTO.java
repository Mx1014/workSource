//@formatter:off
package com.everhomes.order;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by Wentian Wang on 2017/9/6.
 */

public class PaymentParamsDTO {
    @NotNull
    private String payType;
    private String acct;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }
}
