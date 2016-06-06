package com.everhomes.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;


@Component
public class PaymentCardProviderImpl implements PaymentCardProvider{
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired 
    private SequenceProvider sequenceProvider;
    
    
}
