package com.everhomes.asset;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.order.PaymentAccount;
import com.everhomes.order.PaymentServiceConfig;
import com.everhomes.order.PaymentUser;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.statistic.BuildingStatisticParam;
import com.everhomes.rest.asset.statistic.CommunityStatisticParam;
import com.everhomes.server.schema.tables.pojos.*;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Wentian on 2017/2/20.
 */
public interface AssetProvider {

//    void creatAssetBill(AssetBill bill);
//    void updateAssetBill(AssetBill bill);
    AssetBill findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType);
//
    List<AssetBillTemplateFieldDTO> findTemplateFieldByTemplateVersion(Long ownerId, String ownerType, Long targetId, String targetType, Long templateVersion);
    	Long getTemplateVersion(Long ownerId, String ownerType, Long targetId, String targetType);
//
//    void creatTemplateField(AssetBillTemplateFields field);
//
    List<AssetBill> listAssetBill(Long ownerId, String ownerType, Long targetId, String targetType, List<Long> tenantIds, String tenantType,
                                  Long addressId, Byte status, Long startTime, Long endTime, CrossShardListingLocator locator, Integer pageSize);

    List<BigDecimal> listPeriodUnpaidAccountAmount(Long ownerId, String ownerType, Long targetId, String targetType, Long addressId,
                                                   String tenantType, Long tenantId, Timestamp currentAccountPeriod);
//
//    List<AssetBill> listUnpaidBillsGroupByTenant(Long ownerId, String ownerType, Long targetId, String targetType);
//
//    int countNotifyRecords(Long ownerId, String ownerType, Long targetId, String targetType, Timestamp startTime, Timestamp endTime);
//
//    AssetBillNotifyRecords getLastAssetBillNotifyRecords(Long ownerId, String ownerType, Long targetId, String targetType);
//
    AssetVendor findAssetVendorByOwner(String ownerType,Long ownerId);

    List<AssetBill> listUnpaidBills(String tenantType, Long tenantId, Long addressId);

    AssetBill findAssetBill(Long ownerId, String ownerType, Long targetId, String targetType, String dateStr, Long tenantId, String tenantType, Long addressId);

//    List<ListBillsDTO> listBills(String contractNum,Integer currentNamespaceId, Long ownerId, String ownerType, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName, Byte status,String targetType, ListBillsCommand cmd);

    List<ListBillsDTO> listBills(Integer currentNamespaceId, Integer pageOffSet, Integer pageSize, ListBillsCommand cmd);

    List<BillDTO> listBillItems(Long billId, String targetName, int pageOffSet, Integer pageSize);

    List<NoticeInfo> listNoticeInfoByBillId(List<Long> billIds);

    List<BillDetailDTO> listBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill,Long contractId,String contractNum);

    ShowBillDetailForClientResponse getBillDetailForClient(Long billId);



    ShowCreateBillDTO showCreateBill(Long billGroupId);

    ShowBillDetailForClientResponse getBillDetailByDateStr(Byte billStatus,Long ownerId, String ownerType, Long targetId, String targetType, String dateStr,Long contractId);

    ListBillsDTO creatPropertyBill(CreateBillCommand cmd, Long billId);

//    ListBillsDTO creatPropertyBillForCommunity( CreateBillCommand cmd);

    ListBillDetailResponse listBillDetail(Long billId);

    List<BillStaticsDTO> listBillStaticsByDateStrs(String beginLimit, String endLimit, Long ownerId, String ownerType, Long categoryId);

    List<BillStaticsDTO> listBillStaticsByChargingItems(String ownerType, Long ownerId,String beginLimit, String endLimit, Long categoryId);

    List<BillStaticsDTO> listBillStaticsByCommunities(String dateStrBegin,String dateStrEnd,Integer currentNamespaceId, Long categoryId);

    void modifyBillStatus(Long billId);

    List<ListChargingStandardsDTO> listChargingStandards(String ownerType, Long ownerId, Long chargingItemId, Long categoryId, Long billGroupId);

    void modifyNotSettledBill(ModifyNotSettledBillCommand cmd);

    List<ListBillExemptionItemsDTO> listBillExemptionItems(String billId, int pageOffSet, Integer pageSize, String dateStr, String targetName);

    void deleteBill(Long billId,String merchantOrderId);

    void deleteBill(Long billId);

    void deleteBillItem(Long billItemId);

    void deleteBillItems(Long billId ,String merchantOrderId);

    void deletExemptionItem(Long exemptionItemId);

    String findFormulaByChargingStandardId(Long chargingStandardId);

    String findChargingItemNameById(Long chargingItemId);

    void saveContractVariables(List<EhPaymentContractReceiver> contractDateList);

    List<VariableIdAndValue> findPreInjectedVariablesForCal(Long chargingStandardId,Long ownerId,String ownerType);

    void increaseNoticeTime(List<Long> billIds);

    List<PaymentContractReceiver> findContractReceiverByContractNumAndTimeLimit(String contractNum);

    String getStandardNameById(Long chargingStandardId);

    List<Object> getBillDayAndCycleByChargingItemId(Long chargingStandardId, Long chargingItemId,String ownerType, Long ownerId);

    List<PaymentBillGroupRule> getBillGroupRule(Long chargingItemId, Long chargingStandardId, String ownerType, Long ownerId, Long billGroupId);

    void saveBillItems(List<EhPaymentBillItems> billItemsList);

    void saveBills(List<EhPaymentBills> billList);

    Byte findBillyCycleById(Long chargingStandardId);

    void changeBillStatusOnContractSaved(Long contractId);

    void deleteContractPayment(Long contractId);
    
    /**
     * 合同更新/根据合同id,自动刷新合同账单
     * @param contractId
     */
    void deleteContractPaymentByContractId(Long contractId);

    List<PaymentExpectancyDTO> listBillExpectanciesOnContract(String contractNum, Integer pageOffset, Integer pageSize,Long contractId, Long categoryId, Integer namespaceId);

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

    Long findAssetOrderByBillIds(List<String> billIds);
    
    void createBillOrderMaps(List<PaymentBillOrder> billOrderList);

    AssetPaymentOrder findAssetPaymentById(Long orderId);

    List<AssetPaymentOrderBills> findBillsById(Long orderId);

    void changeOrderStaus(Long orderId, Byte finalOrderStatus);

    void changeBillStatusOnOrder(Map<String, Integer> billStatuses,Long orderId);
    
    void changeOrderPaymentType(Long orderId, Integer paymentType);

    PaymentUser findByOwner(String userType, Long id);

    PaymentAccount findPaymentAccount();

    void createChargingStandard(EhPaymentChargingStandards c, EhPaymentChargingStandardsScopes s, List<EhPaymentFormula> f);

    GetChargingStandardDTO getChargingStandardDetail(GetChargingStandardCommand cmd);

    List<ListAvailableVariablesDTO> listAvailableVariables(ListAvailableVariablesCommand cmd);

    String getVariableIdenfitierById(Long variableId);

    String getVariableIdenfitierByName(String targetStr);

    void adjustBillGroupOrder(Long subjectBillGroupId, Long targetBillGroupId);

    List<ListChargingItemsForBillGroupDTO> listChargingItemsForBillGroup(Long billGroupId,Long pageAnchor,Integer pageSize);

    ListChargingItemDetailForBillGroupDTO listChargingItemDetailForBillGroup(Long billGroupRuleId);

    List<ListChargingItemsDTO> listAvailableChargingItems(OwnerIdentityCommand cmd);

    boolean cheackGroupRuleExistByChargingStandard(Long chargingStandardId,String ownerType,Long ownerId);

    void setInworkFlagInContractReceiver(Long contractId,String contractNum);

    void setInworkFlagInContractReceiverWell(Long contractId);

    Boolean checkContractInWork(Long contractId,String contractNum);

    void updateChargingStandardByCreating(String standardName,String instruction, Long chargingStandardId, Long ownerId, String ownerType);

    boolean checkCoupledChargingStandard(Long cid, Long categoryId);

    void deCoupledForChargingItem(Long ownerId, String ownerType, Long categoryId);


    List<EhPaymentBillGroupsRules> getBillGroupRuleByCommunity(Long ownerId, String ownerType);

    PaymentChargingItemScope findChargingItemScope(Long chargingItemId, String ownerType, Long ownerId);

    List<PaymentNoticeConfig> listAutoNoticeConfig(Integer namespaceId, String ownerType, Long ownerId, Long categoryId);


    void autoNoticeConfig(Integer namespaceId, String ownerType, Long ownerId, Long categoryId, List<com.everhomes.server.schema.tables.pojos.EhPaymentNoticeConfig> toSaveConfigs);

    AssetPaymentOrder getOrderById(Long orderId);

    String getBillSource(String billId);

    List<PaymentNoticeConfig> listAllNoticeConfigs();

    List<PaymentBills> getAllBillsByCommunity(Integer namespaceId, Long key);

    List<PaymentBills> findAssetArrearage(Integer namespaceId, Long communityId, Long organizationId);

    List<EhPaymentBillGroupsRules> getBillGroupRuleByCommunityWithBro(Long ownerId, String ownerType, boolean b);

    List<PaymentBills> findSettledBillsByContractIds(List<Long> contractIds);

    Collection<? extends Long> getAddressIdByBillId(Long id);

    String getAddressStrByIds(List<Long> collect);

    BigDecimal getBillExpectanciesAmountOnContract(String contractNum, Long contractId, Long categoryId, Integer namespaceId);

    List<ListAllBillsForClientDTO> listAllBillsForClient(Integer namespaceId, String ownerType, Long ownerId, String targetType, Long targetId, Byte status, Long billGroupId);

    PaymentServiceConfig findServiceConfig(Integer namespaceId);

    List<PaymentBills> findSettledBillsByCustomer(String targetType, Long targetId,String ownerType,Long ownerId);

    List<PaymentBills> findPaidBillsByIds(List<String> billIds);

    void reCalBillById(long billId);

    SettledBillRes getSettledBills(int pageSize, long pageAnchor);

    void changeBillToDue(Long id);

    List<PaymentBillItems> getBillItemsByBillIds(List<Long> overdueBillIds);

    void updatePaymentItem(PaymentBillItems item);

    BigDecimal getLateFineAmountByItemId(Long id);

    void createLateFine(PaymentLateFine fine);

    void updateBillAmountOwedDueToFine(BigDecimal fineAmount, Long billId);

    List<ListLateFineStandardsDTO> listLateFineStandards(Long ownerId, String ownerType, Integer namespaceId, Long categoryId);

    void updateLateFineAndBill(PaymentLateFine fine, BigDecimal fineAmount, Long billId, boolean isInsert);

    PaymentChargingItem getBillItemByName(Integer namespaceId, Long ownerId, String ownerType, Long billGroupId, String projectLevelName);

    String findBillGroupNameById(Long billGroupId);

    void linkIndividualUserToBill(Long ownerUid, String token);

    void linkOrganizationToBill(Long ownerUid, String orgName);

    List<AssetPaymentOrder> findAssetOrderByBillId(String billId);

    PaymentBills findPaymentBillById(Long billId);

    PaymentBills findPaymentBill(Integer namespaceId, String sourceType, Long sourceId, String thirdBillId);

    //add by Meng qianxiang
    List<PaymentBills> findPaymentBills(PaymentBillsCommand PBCmd);

    List<PaymentBillItems> findPaymentBillItems(Integer namespaceId,Long ownerId,String ownerType,Long billId);

    List<PaymentBillItems> findPaymentBillItems(Integer namespaceId, String sourceType, Long sourceId, String thirdBillId);

    List<Long> findbillIdsByOwner(Integer namespaceId, String ownerType, Long ownerId);

    void deletExemptionItem(DSLContext context,Long billID,String merchantOrderId);

    //add by tangcen
	String findProjectChargingItemNameByCommunityId(Long ownerId, Integer namespaceId, Long categoryId, Long chargingItemId);

    void modifySettledBill(Long billId, String invoiceNum, String noticeTel);

    boolean checkBillExistById(Long billId);

    String getAddressByBillId(Long id);

    List<PaymentAppView> findAppViewsByNamespaceIdOrRemark(Integer namespaceId, Long communityId, String targetType, String ownerType, String billGroupName, String billGroupName1, Boolean[] remarkCheckList);

    List<PaymentNoticeConfig> listAllNoticeConfigsByNameSpaceId(Integer namespaceId);

    Long findCategoryIdFromBillGroup(Long billGroupId);

    void insertAssetCategory(EhAssetAppCategories c);

    boolean checkBillByCategory(Long billId, Long categoryId);
    
	void changeBillStatusAndPaymentTypeOnPaiedOff(List<Long> billIds, Integer paymentType);
	
	List<PaymentBillCertificate> listUploadCertificates(Long billId);
	
	String getCertificateNote(Long billId);
	
	void updatePaymentBillCertificates(Long billId, String certificateNote, List<String> certificateUris);

	void setRent(Long contractId, BigDecimal rent);
	
	void deleteUnsettledBillsOnContractId(Long contractId, List<Long> billIds);
	
	PaymentBills getFirstUnsettledBill(Long id);
	
	List<PaymentBillItems> findBillItemsByBillId(Long billId);
	
	void updatePaymentBills(PaymentBills bill);
	
	List<PaymentBills> getUnsettledBillBeforeEndtime(Long contractId, String endTimeStr);
	
	void deleteUnsettledBills(Long contractId, String endTimeStr);
	
	PaymentBills findLastBill(Long contractId);
	
	String findEndTimeByPeriod(String endTimeStr, Long contractId, Long chargingItemId);
    
    PaymentLateFine findLastedFine(Long id);
    
    List<PaymentOrderBillDTO> listBillsForOrder(Integer currentNamespaceId, Integer pageOffSet, Integer pageSize, ListPaymentBillCmd cmd);

    Long getOriginIdFromMappingApp(Long moduleId, Long originId, long targetModuleId, Integer namespaceId);

    Long getOriginIdFromMappingApp(Long moduleId, Long originId, long targetModuleId);

    AssetModuleAppMapping insertAppMapping(AssetModuleAppMapping mapping);

    /**
     * 判断缴费是否已经存在关联合同的记录
     * @param assetCategoryId
     * @return
     */
//    boolean checkExistAssetMapContract(Long assetCategoryId);
    
//    boolean checkExistAssetMapEnergy(Long assetCategoryId);
    
//    void updateAssetMapContract(AssetModuleAppMapping mapping);
    
//    void updateAssetMapEnergy(AssetModuleAppMapping mapping);

    void modifyBillForImport(Long billId, CreateBillCommand cmd);
    
    String getProjectNameByBillID(Long billId);
    
    ListBillDetailVO listBillDetailForPaymentForEnt(Long billId, ListPaymentBillCmd cmd);
    
    ShowCreateBillSubItemListDTO showCreateBillSubItemList(ShowCreateBillSubItemListCmd cmd);
	
	void batchModifyBillSubItem(BatchModifyBillSubItemCommand cmd);
	
	Boolean isConfigItemSubtraction(Long billId, Long charingItemId);
	
	Boolean isConfigLateFineSubtraction(Long billId, Long charingItemId);

	Double getApartmentInfo(Long addressId, Long contractId);

	void updatePaymentBillSwitch(BatchUpdateBillsToSettledCmd cmd);
	
	void updatePaymentBillStatus(BatchUpdateBillsToPaidCmd cmd);
	
	List<Long> getOriginIdFromMappingAppForEnergy(final Long moduleId, final Long originId, long targetModuleId, Integer namespaceId);
	
	Map<Long, String> getGroupNames(ArrayList<Long> groupIds);
    
	GetPayBillsForEntResultResp getPayBillsResultByOrderId(Long orderId);
	
	public List<PaymentBills> findBillsByIds(List<String> billIds);
	
	List<PaymentBillOrder> findPaymentBillOrderRecordByOrderNum(String bizOrderNum);
	
	void updatePaymentBillOrder(String bizOrderNum, Integer paymentStatus, Integer paymentType, Timestamp payDatetime, Integer paymentChannel);
	public BigDecimal getBillItemTaxRate(Long billGroupId, Long billItemId);
	
	void updateBillDueDayCount(Long billId, Long dueDayCount);
	
    PaymentBillItems findFirstBillItemToDelete(Long contractId, String endTimeStr);
    
	PaymentBills findBillById(Long billId);
	
	void deleteBillItemsAfterDate(Long contractId, String endTimeStr);
	
	boolean isInWorkChargingStandard(Integer namespaceId, Long chargingStandardId);

	List<AppAssetCategory> listAssetAppCategory(Integer namespaceId);
	
//	boolean checkExistGeneralBillAssetMapping(AssetGeneralBillMappingCmd cmd);
//
//	AssetModuleAppMapping updateGeneralBillAssetMapping(AssetModuleAppMapping assetModuleAppMapping);
	
	/**
	 * 物业缴费V6.6统一账单：如果该账单组中的费项被其他模块应用选中了，则不允许删除
	 * @param billGroupId
	 * @param chargingItemId
	 * @return
	 */
	boolean checkIsUsedByGeneralBill(Long billGroupId, Long chargingItemId);
	
	List<AssetModuleAppMapping> findAssetModuleAppMapping(AssetGeneralBillMappingCmd cmd);
	
	PaymentBillGroup getBillGroup(Integer namespaceId, Long ownerId, String ownerType, Long categoryId, Long brotherGroupId, Byte isDefault);

	PaymentBills getCMBillByThirdBillId(Integer namespaceId, Long ownerId, String thirdBillId);

	PaymentBillItems getCMBillItemByBillId(Long billId);

	Long createCMBill(PaymentBills paymentBills);

	void createCMBillItem(PaymentBillItems items);

	void updateCMBill(PaymentBills paymentBills);

	void updateCMBillItem(PaymentBillItems items);

	void createOrUpdateAssetModuleAppMapping(AssetModuleAppMapping mapping);
	
	/**
	 * 取出eh_payment_bills表中dateStr（年月）
	 */
	List<CommunityStatisticParam> getPaymentBillsDateStr();
	
	/**
	 * 取出eh_payment_bill_statistic_community表中dateStr（年月）
	 */
	List<CommunityStatisticParam> getStatisticCommunityDateStr();
	
	/**
	 * 取出eh_payment_bill_items表中dateStr（年月）
	 */
	List<BuildingStatisticParam> getPaymentBillItemsDateStr();
	
	/**
	 * 取出eh_payment_bill_statistic_building表中dateStr（年月）
	 */
	List<BuildingStatisticParam> getStatisticBuildingDateStr();
	
	/**
	 * 获取费项的备注名称
	 * @param namespaceId
	 * @param ownerId
	 * @param ownerType
	 * @param chargingItemId
	 * @param categoryId
	 * @return
	 */
	String getProjectChargingItemName(Integer namespaceId, Long ownerId, String ownerType, Long chargingItemId,
			Long categoryId);
	
	//缴费对接门禁
	void createDoorAccessParam(AssetDooraccessParam asseDooraccessParam);
	List<AssetDooraccessParam> listDooraccessParams(GetDoorAccessParamCommand cmd);
	AssetDooraccessParam findDoorAccessParamById(Long id);
	void updateDoorAccessParam(AssetDooraccessParam assetDooraccessParam);
	Long createDoorAccessLog(AssetDooraccessLog assetDooraccessLog);
	void updateDoorAccessLog(AssetDooraccessLog assetDooraccessLog);
	AssetDooraccessLog getDooraccessLog(AssetDooraccessLog assetDooraccessLog);
	PaymentBillOrder getPaymentBillOrderByBillId(String billId);
	AssetDooraccessParam findDoorAccessParamByParams(SetDoorAccessParamCommand cmd);
	List<AssetDooraccessParam> listDooraccessParamsList(byte status);
	SettledBillRes getAssetDoorAccessBills(int pageSize, long pageAnchor, byte status, AssetDooraccessParam doorAccessParam);
	void deleteAllDoorAccessLog(AssetDooraccessLog assetDooraccessLog);
	SettledBillRes getAssetDoorAccessBillsUNPAID(int pageSize, long pageAnchor, byte status, AssetDooraccessParam doorAccessParam);
	List<AssetDooraccessLog> getDooraccessLogInStatus(AssetDooraccessParam doorAccessParamInStatus);
	
	PaymentOrderBillDTO listPaymentBillDetail(Long billId);

	AssetBillDateDTO generateBillDate(Long billGroupId, String dateStrBegin, String dateStrEnd);
	
	void deleteBillItemByBillId(Long billId);
	
	List<String> findDateStr(List<String> billIds);
	
	List<PaymentBillOrder> findPaymentBillOrderByGeneralOrderId(Long merchantOrderId);
	
	void updatePaymentBillOrder(Long merchantOrderId, Integer paymentStatus, Integer paymentType,
			Timestamp paymentSucessTime, Integer paymentChannel);
	
}
