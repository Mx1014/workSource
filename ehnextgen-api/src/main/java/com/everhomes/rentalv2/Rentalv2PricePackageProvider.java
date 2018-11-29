package com.everhomes.rentalv2;

import com.everhomes.rest.rentalv2.MaxMinPrice;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
public interface Rentalv2PricePackageProvider {

    void deletePricePackageByOwnerId(String resourceType, String ownerType, Long ownerId);

    Long createRentalv2PricePackage(Rentalv2PricePackage rentalv2PricePackage);

    List<Rentalv2PricePackage> listPricePackageByOwner(String resourceType, String ownerType, Long ownerId,
                                                       Byte rentalType,String packageName);

    Rentalv2PricePackage findPricePackageById(Long id);

    MaxMinPrice findMaxMinPrice(List<Long> packageIds,Byte rentalType,String packageName);

    List<Long> listPricePackageIdsByCellPackages(List<Long> packageIds,String packageName);

    void deletePricePackageByRentalTypes(String resourceType, String ownerType, Long ownerId, List<Byte> rentalTypes);
}
