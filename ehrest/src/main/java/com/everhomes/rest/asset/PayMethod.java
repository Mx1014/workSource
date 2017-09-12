//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.rest.order.PaymentParamsDTO;

/**
 * Created by Wentian Wang on 2017/9/12.
 */

public class PayMethod {
    private Integer paymentType;
    private String paymentName;
    private String paymentLogo;
    private PaymentParamsDTO paymentParams;
    private ExtendInfo extendInfo;
}
