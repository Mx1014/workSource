package com.everhomes.asset;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.asset.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
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

    List<AssetBill> listUnpaidBillsGroupByTenant(Long ownerId, String ownerType, Long targetId, String targetType);

    int countNotifyRecords(Long ownerId, String ownerType, Long targetId, String targetType, Timestamp startTime, Timestamp endTime);

    AssetBillNotifyRecords getLastAssetBillNotifyRecords(Long ownerId, String ownerType, Long targetId, String targetType);

    AssetVendor findAssetVendorByOwner(String ownerType,Long ownerId);

    List<AssetBill> listUnpaidBills(String tenantType, Long tenantId, Long addressId);

    AssetBill findAssetBill(Long ownerId, String ownerType, Long targetId, String targetType, String dateStr, Long tenantId, String tenantType, Long addressId);

    List<ListBillsDTO> listBills(Integer currentNamespaceId, Long ownerId, String ownerType, String buildingName,String apartmentName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName, Byte status);

    List<BillDTO> listBillItems(Long billId, String targetName, int pageOffSet, Integer pageSize);

    List<NoticeInfo> listNoticeInfoByBillId(List<Long> billIds);

    List<BillDetailDTO> listBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill);

    ShowBillDetailForClientResponse getBillDetailForClient(Long billId);

    List<ListBillGroupsDTO> listBillGroups(Long ownerId, String ownerType);

    ShowCreateBillDTO showCreateBill(Long billGroupId);

    ShowBillDetailForClientResponse getBillDetailByDateStr(Long ownerId, String ownerType, Long targetId, String targetType, String dateStr);

    void creatPropertyBill(List<AddressIdAndName> addressIdAndNames, BillGroupDTO billGroupDTO, String dateStr, Byte isSettled, String noticeTel, Long ownerId, String ownerType, String targetName,Long targetId,String targetType,String buildingName,String apartmentName);

    ListBillDetailVO listBillDetail(Long billId);

    List<BillStaticsDTO> listBillStaticsByDateStrs(String beginLimit, String endLimit, Long ownerId, String ownerType);

    List<BillStaticsDTO> listBillStaticsByChargingItems(String ownerType, Long ownerId);

    List<BillStaticsDTO> listBillStaticsByCommunities(Integer currentNamespaceId);

    void modifyBillStatus(Long billId);

    List<ListChargingItemsDTO> listChargingItems(String ownerType, Long ownerId);

    List<ListChargingStandardsDTO> listChargingStandards(String ownerType, Long ownerId, Long chargingItemId);

    void modifyNotSettledBill(Long billId, BillGroupDTO billGroupDTO,String targetType,Long targetId,String targetName);

    List<ListBillExemptionItemsDTO> listBillExemptionItems(Long billId, int pageOffSet, Integer pageSize, String dateStr, String targetName);

    void deleteBill(Long billId);

    void deleteBillItem(Long billItemId);

    void deletExemptionItem(Long exemptionItemId);

    String findFormulaByChargingStandardId(Long chargingStandardId);

    String findChargingItemNameById(Long chargingItemId);

    void saveContractVariables(String apartmentName, String buldingName, String contractNum, Long namesapceId, String noticeTel, Long ownerId, String ownerType, Long targetId, String targetType, String json,Long chargingStandardId,String targetName);

    List<VariableIdAndValue> findPreInjectedVariablesForCal(Long chargingStandardId);

    void increaseNoticeTime(List<Long> billIds);

    List<PaymentContractReceiver> findContractReceiverByContractNumAndTimeLimit(String contractNum);
}
