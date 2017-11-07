package com.everhomes.rentalv2;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
public interface Rentalv2PricePackageProvider {

    void deletePricePackageByOwnerId(String ownerType, Long ownerId);

    void createRentalv2PricePackage(Rentalv2PricePackage rentalv2PricePackage);

    List<Rentalv2PricePackage> listPricePackageByOwner(String ownerType, Long ownerId, Byte rentalType);
}
