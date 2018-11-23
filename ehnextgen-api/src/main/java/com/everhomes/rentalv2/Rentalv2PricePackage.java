package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2PricePackages;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
public class Rentalv2PricePackage extends EhRentalv2PricePackages {

    private static final long serialVersionUID = 5802634622897128231L;

    private List<RentalPriceClassification> priceClassification;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<RentalPriceClassification> getPriceClassification() {
        return priceClassification;
    }

    public void setPriceClassification(List<RentalPriceClassification> priceClassification) {
        this.priceClassification = priceClassification;
    }
}
