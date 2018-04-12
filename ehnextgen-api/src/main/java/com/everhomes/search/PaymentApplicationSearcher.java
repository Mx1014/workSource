package com.everhomes.search;

import com.everhomes.payment_application.PaymentApplication;
import com.everhomes.rest.payment_application.SearchPaymentApplicationCommand;
import com.everhomes.rest.payment_application.SearchPaymentApplicationResponse;

import java.util.List;

/**
 * Created by ying.xiong on 2017/12/27.
 */
public interface PaymentApplicationSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<PaymentApplication> applications);
    void feedDoc(PaymentApplication application);
    void syncFromDb();
    SearchPaymentApplicationResponse query(SearchPaymentApplicationCommand cmd);
}
