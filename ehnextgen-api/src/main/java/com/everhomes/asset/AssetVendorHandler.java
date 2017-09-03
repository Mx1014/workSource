package com.everhomes.asset;

import com.everhomes.rest.asset.*;

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

    List<ListBillsDTO> listBills(String contractNum,Integer currentNamespaceId, Long ownerId, String ownerType,String buildingName,String apartmentName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName, Byte status,String targetType);

    List<BillDTO> listBillItems(Long billId, String targetName, int pageOffSet, Integer pageSize);

    List<NoticeInfo> listNoticeInfoByBillId(List<Long> billIds);

    ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOnlyOwedBill,String contractNum);

    ShowBillDetailForClientResponse getBillDetailForClient(Long billId);

    ShowBillDetailForClientResponse listBillDetailOnDateChange(Long ownerId, String ownerType, String targetType, Long targetId, String dateStr);
}
