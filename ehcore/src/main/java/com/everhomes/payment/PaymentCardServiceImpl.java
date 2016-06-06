package com.everhomes.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentCardServiceImpl implements PaymentCardService{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardServiceImpl.class);

    @Autowired
    private PaymentCardProvider paymentCardProvider;
    
    
}
