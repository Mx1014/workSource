package com.everhomes.asset;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.order.PaymentAccount;
import com.everhomes.order.PaymentServiceConfig;
import com.everhomes.order.PaymentUser;
import com.everhomes.rest.asset.*;
import com.everhomes.server.schema.tables.pojos.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Wentian on 2017/2/20.
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

    List<ListBillsDTO> listBills(String contractNum,Integer currentNamespaceId, Long ownerId, String ownerType, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName, Byte status,String targetType);

    List<BillDTO> listBillItems(Long billId, String targetName, int pageOffSet, Integer pageSize);

    List<NoticeInfo> listNoticeInfoByBillId(List<Long> billIds);

    List<BillDetailDTO> listBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill,Long contractId,String contractNum);

    ShowBillDetailForClientResponse getBillDetailForClient(Long billId);

    List<ListBillGroupsDTO> listBillGroups(Long ownerId, String ownerType);

    ShowCreateBillDTO showCreateBill(Long billGroupId);

    ShowBillDetailForClientResponse getBillDetailByDateStr(Byte billStatus,Long ownerId, String ownerType, Long targetId, String targetType, String dateStr,Long contractId);

    ListBillsDTO creatPropertyBill(BillGroupDTO billGroupDTO,String dateStr, Byte isSettled, String noticeTel, Long ownerId, String ownerType, String targetName,Long targetId,String targetType,String contractNum,Long contractId);

    ListBillDetailVO listBillDetail(Long billId);

    List<BillStaticsDTO> listBillStaticsByDateStrs(String beginLimit, String endLimit, Long ownerId, String ownerType);

    List<BillStaticsDTO> listBillStaticsByChargingItems(String ownerType, Long ownerId,String beginLimit, String endLimit);

    List<BillStaticsDTO> listBillStaticsByCommunities(String dateStrBegin,String dateStrEnd,Integer currentNamespaceId);

    void modifyBillStatus(Long billId);

    List<ListChargingItemsDTO> listChargingItems(String ownerType, Long ownerId);

    List<ListChargingStandardsDTO> listChargingStandards(String ownerType, Long ownerId, Long chargingItemId);

    void modifyNotSettledBill(Long billId, BillGroupDTO billGroupDTO,String targetType,Long targetId,String targetName);

    List<ListBillExemptionItemsDTO> listBillExemptionItems(String billId, int pageOffSet, Integer pageSize, String dateStr, String targetName);

    void deleteBill(Long billId);

    void deleteBillItem(Long billItemId);

    void deletExemptionItem(Long exemptionItemId);

    String findFormulaByChargingStandardId(Long chargingStandardId);

    String findChargingItemNameById(Long chargingItemId);

    void saveContractVariables(List<EhPaymentContractReceiver> contractDateList);

    List<VariableIdAndValue> findPreInjectedVariablesForCal(Long chargingStandardId,Long ownerId,String ownerType);

    void increaseNoticeTime(List<Long> billIds);

    List<PaymentContractReceiver> findContractReceiverByContractNumAndTimeLimit(String contractNum);

    String getStandardNameById(Long chargingStandardId);

    List<Object> getBillDayAndCycleByChargingItemId(Long chargingStandardId, Long chargingItemId,String ownerType, Long ownerId);

    PaymentBillGroupRule getBillGroupRule(Long chargingItemId, Long chargingStandardId, String ownerType, Long ownerId);

    void saveBillItems(List<EhPaymentBillItems> billItemsList);

    void saveBills(List<EhPaymentBills> billList);

    Byte findBillyCycleById(Long chargingStandardId);

    void changeBillStatusOnContractSaved(Long contractId);

    void deleteContractPayment(Long contractId);

    List<PaymentExpectancyDTO> listBillExpectanciesOnContract(String contractNum, Integer pageOffset, Integer pageSize,Long contractId);

    void updateBillsToSettled(Long contractId, String ownerType, Long ownerId);

    void updatePaymentBill(Long billId, BigDecimal amountReceivable, BigDecimal amountReceived, BigDecimal amountOwed);

    PaymentBillItems findBillItemById(Long billItemId);

    PaymentExemptionItems findExemptionItemById(Long exemptionItemId);

    void updatePaymentBillByExemItemChanges(Long billId, BigDecimal amount);

    List<PaymentBillGroup> listAllBillGroups();

    void updateBillSwitchOnTime(String billDateStr);

    String findZjgkCommunityIdentifierById(Long ownerId);

    Long findTargetIdByIdentifier(String customerIdentifier);

    String findAppName(Integer currentNamespaceId);

    Long findOrganizationIdByIdentifier(String targetId);

    AssetVendor findAssetVendorByNamespace(Integer namespaceId);

    String findIdentifierByUid(Long aLong);


    Long saveAnOrderCopy(String payerType, String payerId, String amountOwed,  String clientAppName, Long communityId, String contactNum, String openid, String payerName, Long expireTimePeriod,Integer namespaceId,String orderType);

    Long findAssetOrderByBillIds(List<String> billIds);

    void saveOrderBills(List<BillIdAndAmount> bills, Long orderId);

    AssetPaymentOrder findAssetPaymentById(Long orderId);

    List<AssetPaymentOrderBills> findBillsById(Long orderId);

    void changeOrderStaus(Long orderId, Byte finalOrderStatus);

    void changeBillStatusOnOrder(Map<String, Integer> billStatuses,Long orderId);


    PaymentUser findByOwner(String userType, Long id);

    PaymentAccount findPaymentAccount();

    void changeBillStatusOnPaiedOff(List<Long> billIds);


    void configChargingItems(List<ConfigChargingItems> configChargingItems, Long communityId,String ownerType, Integer namespaceId,List<Long> communityIds);

    void createChargingStandard(EhPaymentChargingStandards c, EhPaymentChargingStandardsScopes s, List<EhPaymentFormula> f);

    void modifyChargingStandard(Long chargingStandardId,String chargingStandardName,String instruction,byte deCouplingFlag,String ownerType,Long ownerId);

    GetChargingStandardDTO getChargingStandardDetail(GetChargingStandardCommand cmd);

    void deleteChargingStandard(Long chargingStandardId, Long ownerId, String ownerType,byte deCouplingFlag);

    List<ListAvailableVariablesDTO> listAvailableVariables(ListAvailableVariablesCommand cmd);

    String getVariableIdenfitierById(Long variableId);

    String getVariableIdenfitierByName(String targetStr);

    Long createBillGroup(CreateBillGroupCommand cmd,byte deCouplingFlag,Long brotherGroupId);

    void modifyBillGroup(ModifyBillGroupCommand cmd,byte deCouplingFlag);

    List<ListChargingStandardsDTO> listOnlyChargingStandards(ListChargingStandardsCommand cmd);

    void adjustBillGroupOrder(Long subjectBillGroupId, Long targetBillGroupId);

    List<ListChargingItemsForBillGroupDTO> listChargingItemsForBillGroup(Long billGroupId,Long pageAnchor,Integer pageSize);

    Long addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd,Long brotherRuleId,byte deCouplingFlag);

    EhPaymentBillGroupsRules findBillGroupRuleById(Long billGroupRuleId);

    boolean isInWorkGroupRule(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule);

    DeleteChargingItemForBillGroupResponse deleteBillGroupRuleById(Long billGroupRuleId,byte deCouplingFlag);

    EhPaymentChargingStandards findChargingStandardById(Long chargingStandardId);

    PaymentBillGroup getBillGroupById(Long billGroupId);

    boolean checkBillsByBillGroupId(Long billGroupId);

    DeleteBillGroupReponse deleteBillGroupAndRules(Long billGroupId,byte deCouplingFlag,String ownerType,Long ownerId);

    ListChargingItemDetailForBillGroupDTO listChargingItemDetailForBillGroup(Long billGroupRuleId);

    List<ListChargingItemsDTO> listAvailableChargingItems(OwnerIdentityCommand cmd);

    List<PaymentFormula> getFormulas(Long id);

    boolean cheackGroupRuleExistByChargingStandard(Long chargingStandardId,String ownerType,Long ownerId);

    void setInworkFlagInContractReceiver(Long contractId,String contractNum);

    void setInworkFlagInContractReceiverWell(Long contractId);

    Boolean checkContractInWork(Long contractId,String contractNum);

    void updateChargingStandardByCreating(String standardName,String instruction, Long chargingStandardId, Long ownerId, String ownerType);

    boolean checkCoupledChargingStandard(Long cid);

    void deCoupledForChargingItem(Long ownerId, String ownerType);

    List<EhPaymentBillGroupsRules> getBillGroupRuleByCommunity(Long ownerId, String ownerType);

    PaymentChargingItemScope findChargingItemScope(Long chargingItemId, String ownerType, Long ownerId);

    List<Integer> listAutoNoticeConfig(Integer namespaceId, String ownerType, Long ownerId);

    void autoNoticeConfig(Integer namespaceId, String ownerType, Long ownerId, List<Integer> configDays);

    AssetPaymentOrder getOrderById(Long orderId);

    String getBillSource(String billId);

    List<PaymentNoticeConfig> listAllNoticeConfigs();

    List<PaymentBills> getAllBillsByCommunity(Long key);


    List<PaymentBills> findAssetArrearage(Integer namespaceId, Long communityId, Long organizationId);




    List<EhPaymentBillGroupsRules> getBillGroupRuleByCommunityWithBro(Long ownerId, String ownerType, boolean b);

    List<PaymentBills> findSettledBillsByContractIds(List<Long> contractIds);

    String getbillGroupNameById(Long billGroupId);

    Collection<? extends Long> getAddressIdByBillId(Long id);

    String getAddressStrByIds(List<Long> collect);

    BigDecimal getBillExpectanciesAmountOnContract(String contractNum, Long contractId);

    List<ListAllBillsForClientDTO> listAllBillsForClient(Integer namespaceId, String ownerType, Long ownerId, String targetType, Long targetId);

    PaymentServiceConfig findServiceConfig(Integer namespaceId);

}
