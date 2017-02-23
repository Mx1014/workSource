package com.everhomes.asset;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.asset.AssetBillTemplateFieldDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface AssetProvider {

    void creatAssetBill(AssetBill bill);
    void updateAssetBill(AssetBill bill);
    AssetBill findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType);

    List<AssetBillTemplateFieldDTO> findTemplateFieldByTemplateVersion(Long ownerId, String ownerType, Long targetId, String targetType, Long templateVersion);
    Long getTemplateVersion(Long ownerId, String ownerType, Long targetId, String targetType);

    void creatTemplateField(AssetBillTemplateFields field);

    List<AssetBill> listAssetBill(Long ownerId, String ownerType, Long targetId, String targetType, List<Long> tenantIds, String tenantType,
                                  Long addressId, Byte status, Long startTime, Long endTime, CrossShardListingLocator locator, Integer pageSize);

    List<BigDecimal> listPeriodUnpaidAccountAmount(Long ownerId, String ownerType, Long targetId, String targetType, Long addressId,
                                                   String tenantType, Long tenantId, Timestamp currentAccountPeriod);

    List<AssetBill> listCurrentPeriodUnpaidBills(Long ownerId, String ownerType, Long targetId, String targetType);
}
