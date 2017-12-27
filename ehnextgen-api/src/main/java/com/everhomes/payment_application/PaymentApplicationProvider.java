package com.everhomes.payment_application;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

/**
 * Created by ying.xiong on 2017/12/27.
 */
public interface PaymentApplicationProvider {

    void createPaymentApplication(PaymentApplication application);
    PaymentApplication findPaymentApplication(Long id);

    List<PaymentApplication> listPaymentApplications(CrossShardListingLocator locator, Integer pageSize);
}
