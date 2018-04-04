package com.everhomes.payment_application;

import com.everhomes.rest.payment_application.*;

/**
 * Created by ying.xiong on 2017/12/27.
 */
public interface PaymentApplicationService {
    PaymentApplicationDTO createPaymentApplication(CreatePaymentApplicationCommand cmd);
    PaymentApplicationDTO getPaymentApplication(GetPaymentApplicationCommand cmd);
    SearchPaymentApplicationResponse searchPaymentApplications(SearchPaymentApplicationCommand cmd);
    String generatePaymentApplicationNumber();
    ListPaymentApplicationByContractResponse listPaymentApplicationByContract(ListPaymentApplicationByContractCommand cmd);
}
