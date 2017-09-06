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
    private Map<String,String> paymentParams;
}
