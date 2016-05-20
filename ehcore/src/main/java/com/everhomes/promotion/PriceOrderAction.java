package com.everhomes.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PriceOrderAction implements Runnable {

    private final Long id;
    private final Long price;
    
    @Autowired
    PromotionService promotionService;
    
    @Override
    public void run() {
        this.promotionService.onNewOrderPriceJob(id, price);
    }
    
    public PriceOrderAction(final String idStr, final String priceStr) {
        this.id = Long.parseLong(idStr);
        this.price = Long.parseLong(priceStr);
    }
}
