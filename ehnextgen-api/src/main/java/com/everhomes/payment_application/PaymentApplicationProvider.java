package com.everhomes.payment_application;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;
import java.util.Set;

/**
 * Created by ying.xiong on 2017/12/27.
 */
public interface PaymentApplicationProvider {

    void createPaymentApplication(PaymentApplication application);
    void updatePaymentApplication(PaymentApplication application);
    PaymentApplication findPaymentApplication(Long id);

    List<PaymentApplication> listPaymentApplications(CrossShardListingLocator locator, Integer pageSize);

    Set<Long> findPaymentApplicationNamespace();

    String findPaymentApplicationMenuName();
}
