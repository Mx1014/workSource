package com.everhomes.payment_application;

import com.everhomes.rest.payment_application.*;
import org.springframework.stereotype.Component;

/**
 * Created by ying.xiong on 2017/12/27.
 */
@Component
public class PaymentApplicationServiceImpl implements PaymentApplicationService {
    @Override
    public PaymentApplicationDTO createPaymentApplication(CreatePaymentApplicationCommand cmd) {
        return null;
    }

    @Override
    public PaymentApplicationDTO getPaymentApplication(GetPaymentApplicationCommand cmd) {
        return null;
    }

    @Override
    public ListPaymentApplicationResponse listPaymentApplications(ListPaymentApplicationCommand cmd) {
        return null;
    }

    private PaymentApplicationDTO toPaymentApplicationDTO() {
        return null;
    }
}
