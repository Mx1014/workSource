package com.everhomes.asset;

import com.everhomes.rest.asset.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by ying.xiong on 2017/4/11.
 */
public interface AssetVendorHandler {
    String ASSET_VENDOR_PREFIX = "AssetVendor-";

    ListSimpleAssetBillsResponse listSimpleAssetBills(Long ownerId, String ownerType, Long targetId, String targetType, Long organizationId,
        Long addressId, String tenant, Byte status, Long startTime, Long endTime, Long pageAnchor, Integer pageSize);

    AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType,
             Long templateVersion, Long organizationId, String dateStr, Long tenantId, String tenantType, Long addressId);

    AssetBillStatDTO getAssetBillStat(String tenantType, Long tenantId, Long addressId);

    List<ListSettledBillDTO> listSettledBill(Integer currentNamespaceId, Long ownerId, String ownerType, String addressName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName);

    List<SettledBillDTO> listSettledBillItems(Long billId, String targetName, Long pageAnchor, Integer pageSize);
}
