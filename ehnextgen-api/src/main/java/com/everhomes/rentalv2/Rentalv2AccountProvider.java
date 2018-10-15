package com.everhomes.rentalv2;

import com.everhomes.listing.ListingLocator;
import org.springframework.stereotype.Component;

import java.util.List;

public interface Rentalv2AccountProvider {

    List<Rentalv2PayAccount> listPayAccounts(Integer namespaceId, Long communityId, String resourceType,Long resourceTypeId, String sourceType,
                                             Long sourceId, ListingLocator locator, Integer pageSize);

    void deletePayAccount(Long id,Long communityId,String sourceType,Long sourceId);

    void createPayAccount(Rentalv2PayAccount account);

    Rentalv2PayAccount getAccountById(Long id);

    void updatePayAccount(Rentalv2PayAccount account);

    void createOrderRecord(Rentalv2OrderRecord record);

    void updateOrderRecord(Rentalv2OrderRecord record);

    Rentalv2OrderRecord getOrderRecordByOrderNo(Long orderNo);

    Rentalv2OrderRecord getOrderRecordByBizOrderNo(String bizOrderNo);

    Rentalv2OrderRecord getOrderRecordByMerchantOrderId(Long merchantOrderId);

    void deleteOrderRecordByOrderNo(Long orderNo);


}
