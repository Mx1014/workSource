package com.everhomes.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PriceOrderAction implements Runnable {

    private Long id;
    private Long price;
    
    @Autowired
    PromotionService promotionService;
    
    @Override
    public void run() {
        this.promotionService.onNewOrderPriceJob(id, price);
    }
    
    public PriceOrderAction(String idStr, String priceStr) {
        this.id = Long.parseLong(idStr);
        this.price = Long.parseLong(priceStr);
    }
}
