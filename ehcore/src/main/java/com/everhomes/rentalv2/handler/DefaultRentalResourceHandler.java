package com.everhomes.rentalv2.handler;

import com.everhomes.rentalv2.RentalResource;
import com.everhomes.rentalv2.RentalResourceHandler;
import com.everhomes.rentalv2.Rentalv2Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX + "default")
public class DefaultRentalResourceHandler implements RentalResourceHandler {

    @Autowired
    private Rentalv2Provider rentalv2Provider;

    @Override
    public RentalResource getRentalResourceById(Long id) {

        RentalResource rs =this.rentalv2Provider.getRentalSiteById(id);


        return rs;
    }
}
