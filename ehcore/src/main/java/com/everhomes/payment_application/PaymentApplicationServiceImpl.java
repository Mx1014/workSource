package com.everhomes.payment_application;

import com.everhomes.rest.payment_application.*;
import com.everhomes.search.PaymentApplicationSearcher;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ying.xiong on 2017/12/27.
 */
@Component
public class PaymentApplicationServiceImpl implements PaymentApplicationService {

    @Autowired
    private PaymentApplicationProvider paymentApplicationProvider;

    @Autowired
    private PaymentApplicationSearcher paymentApplicationSearcher;

    @Override
    public PaymentApplicationDTO createPaymentApplication(CreatePaymentApplicationCommand cmd) {
        PaymentApplication application = ConvertHelper.convert(cmd, PaymentApplication.class);
        paymentApplicationProvider.createPaymentApplication(application);
        return toPaymentApplicationDTO(application);
    }

    @Override
    public PaymentApplicationDTO getPaymentApplication(GetPaymentApplicationCommand cmd) {
        PaymentApplication application = paymentApplicationProvider.findPaymentApplication(cmd.getId());
        return toPaymentApplicationDTO(application);
    }

    @Override
    public SearchPaymentApplicationResponse searchPaymentApplications(SearchPaymentApplicationCommand cmd) {
        return paymentApplicationSearcher.query(cmd);
    }

    private PaymentApplicationDTO toPaymentApplicationDTO(PaymentApplication application) {
        return null;
    }
}
