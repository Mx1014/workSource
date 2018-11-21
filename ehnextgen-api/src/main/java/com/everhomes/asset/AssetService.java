
package com.everhomes.asset;

import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.rest.contract.CMSyncObject;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.portal.AssetServiceModuleAppDTO;
import com.everhomes.util.Tuple;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface AssetService {

//	List<AssetBillTemplateFieldDTO> listAssetBillTemplate(ListAssetBillTemplateCommand cmd);
//
	ListSimpleAssetBillsResponse listSimpleAssetBills(ListSimpleAssetBillsCommand cmd);
//
//	HttpServletResponse exportAssetBills(ListSimpleAssetBillsCommand cmd, HttpServletResponse response);
//
//	ImportDataResponse importAssetBills(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);
//
//	AssetBillTemplateValueDTO creatAssetBill(CreatAssetBillCommand cmd);
//
	AssetBillTemplateValueDTO findAssetBill(FindAssetBillCommand cmd);
//
//	AssetBillTemplateValueDTO updateAssetBill(UpdateAssetBillCommand cmd);
//
//	void notifyUnpaidBillsContact(NotifyUnpaidBillsContactCommand cmd);
//
//	void setBillsStatus(BillIdListCommand cmd, AssetBillStatus status);
//
//	void deleteBill(DeleteBillCommand cmd);
//
//	List<AssetBillTemplateFieldDTO> updateAssetBillTemplate(UpdateAssetBillTemplateCommand cmd);
//
	Boolean checkTokenRegister(CheckTokenRegisterCommand cmd);
//
//	NotifyTimesResponse notifyTimes(ImportOwnerCommand cmd);
//
	AssetBillStatDTO getAssetBillStat(GetAssetBillStatCommand cmd);
//
	List<ListOrganizationsByPmAdminDTO> listOrganizationsByPmAdmin();

	//=============================================================================
	// wentian's controlls for payment module（从这里开始的接口都是基于新的eh_payment_*表开头的）
	//=============================================================================

	ListBillsResponse listBills(ListBillsCommand cmd);

	ListBillItemsResponse listBillItems(ListBillItemsCommand cmd);

	void selectNotice(SelectedNoticeCommand cmd);

	ShowCreateBillDTO showCreateBill(BillGroupIdCommand cmd);

	ListBillsDTO createBill(CreateBillCommand cmd);

	void OneKeyNotice(OneKeyNoticeCommand cmd);

	ListBillDetailResponse listBillDetail(ListBillDetailCommandStr cmd);

	List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd);

	void modifyBillStatus(BillIdCommand cmd);

	//void exportPaymentBills(ListBillsCommand cmd, HttpServletResponse response); -- by djm 对接下载中心

	List<ListChargingStandardsDTO> listChargingStandards(ListChargingStandardsCommand cmd);

	void modifyNotSettledBill(ModifyNotSettledBillCommand cmd);

	ListSettledBillExemptionItemsResponse listBillExemptionItems(listBillExemtionItemsCommand cmd);

	String deleteBill(BillIdCommand cmd);

	void deleteBill(PaymentBillItems billItem);

	void deleteBill(PaymentExemptionItems exemItem);

	String deleteBillItem(BillItemIdCommand cmd);

	String deletExemptionItem(ExemptionItemIdCommand cmd);

	void upodateBillStatusOnContractStatusChange(Long contractId, String targetStatus);

	PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd);

	void exportRentalExcelTemplate(HttpServletResponse response);

	FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd);

	void updateBillsToSettled(UpdateBillsToSettled cmd);

	GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd);

	PaymentBillItems findBillItemById(Long billItemId);

	PaymentExemptionItems findExemptionItemById(Long ExemptionItemId);

	List<ListAvailableVariablesDTO> listAvailableVariables(ListAvailableVariablesCommand cmd);

	void adjustBillGroupOrder(AdjustBillGroupOrderCommand cmd);

	ListChargingItemsForBillGroupResponse listChargingItemsForBillGroup(BillGroupIdCommand cmd);

	DeleteChargingItemForBillGroupResponse deleteChargingItemForBillGroup(BillGroupRuleIdCommand cmd);

	ListChargingItemDetailForBillGroupDTO listChargingItemDetailForBillGroup(BillGroupRuleIdCommand cmd);

	PreOrderDTO placeAnAssetOrder(CreatePaymentBillOrderCommand cmd);

	List<ListChargingItemsDTO> listAvailableChargingItems(OwnerIdentityCommand cmd);

	void paymentExpectanciesCalculate(PaymentExpectanciesCommand cmd);

	ListAutoNoticeConfigResponse listAutoNoticeConfig(ListAutoNoticeConfigCommand cmd);

	void autoNoticeConfig(AutoNoticeConfigCommand cmd);

	CheckEnterpriseHasArrearageResponse checkEnterpriseHasArrearage(CheckEnterpriseHasArrearageCommand cmd);

	List<ListLateFineStandardsDTO> listLateFineStandards(ListLateFineStandardsCommand cmd);

	void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response);

	BatchImportBillsResponse batchImportBills(BatchImportBillsCommand cmd, MultipartFile file);

	void linkCustomerToBill(String code, Long ownerUid, String identifierToken);

	ListPaymentBillResp listBillRelatedTransac(listBillRelatedTransacCommand cmd);

    void reCalBill(ReCalBillCommand cmd);

    void modifySettledBill(ModifySettledBillCommand cmd);

    UploadCertificateInfoDTO uploadCertificate(UploadCertificateCommand cmd);

	UploadCertificateInfoDTO listUploadCertificates(ListUploadCertificatesCommand cmd);

    JudgeAppShowPayResponse judgeAppShowPay(JudgeAppShowPayCommand cmd);
    
    void exportOrders(ListPaymentBillCmd cmd, HttpServletResponse response);

//    void noticeTrigger(Integer namespaceId);
    
    List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd);
    
    /**
     * 用于接受支付系统的回调信息
     * @param cmd
     */
    void payNotify(OrderPaymentNotificationCommand cmd);
    	
    ListPaymentBillResp listPaymentBill(ListPaymentBillCmd cmd);
    
	IsProjectNavigateDefaultResp isProjectNavigateDefault(IsProjectNavigateDefaultCmd cmd);
	
    long getNextCategoryId(Integer namespaceId, Long aLong, String instanceConfig);

	void saveInstanceConfig(long categoryId, String ret);
	
	void deleteUnsettledBillsOnContractId(Byte costGenerationMethod, Long contractId, Timestamp endTime);
	
    //add by tangcen
	void calculateRentForContract(CalculateRentCommand calculateRentCommand);

	Long getOriginIdFromMappingApp(Long moduleId, Long originId, long targetModuleId);

    IsUserExistInAddressResponse isUserExistInAddress(IsUserExistInAddressCmd cmd);
    
    ListBillsResponse listBillsForEnt(ListBillsCommandForEnt cmd);
    
    //void exportSettledBillsForEnt(ListBillsCommandForEnt cmd, HttpServletResponse response); -- by djm 对接下载中心
    
    void exportOrdersForEnt(ListPaymentBillCmd cmd,HttpServletResponse response);
    
    public PublicTransferBillRespForEnt publicTransferBillForEnt(PublicTransferBillCmdForEnt cmd);
    
    ListPaymentBillResp listPaymentBillForEnt(ListPaymentBillCmd cmd);

	ShowCreateBillSubItemListDTO showCreateBillSubItemList(ShowCreateBillSubItemListCmd cmd);

	void batchModifyBillSubItem(BatchModifyBillSubItemCommand cmd);

//	void testLateFine(TestLateFineCommand cmd);
	
	void batchUpdateBillsToSettled(BatchUpdateBillsToSettledCmd cmd);

	void batchUpdateBillsToPaid(BatchUpdateBillsToPaidCmd cmd);

	boolean isShowEnergy(Integer namespaceId, Long communityId, long moduleId);

	PreOrderDTO payBillsForEnt(CreatePaymentBillOrderCommand cmd);

	GetPayBillsForEntResultResp getPayBillsForEntResult(PaymentOrderRecord cmd);
    
    //void createOrUpdateAnAppMapping(CreateAnAppMappingCommand cmd);
	
	public BigDecimal getBillItemTaxRate(Long billGroupId, Long billItemId);

	/**
	 * 物业缴费V6.6（对接统一账单） 获取缴费应用列表接口
	 */
	public List<AssetServiceModuleAppDTO> listAssetModuleApps(Integer namespaceId);

	/**
	 * 物业缴费V6.6（对接统一账单） 业务应用新增缴费映射关系接口
	 */
	//public AssetModuleAppMapping createOrUpdateAssetMapping(AssetModuleAppMapping assetModuleAppMapping);
	
	/**
	 * 物业缴费V6.6（对接统一账单） 创建统一账单接口
	 */
	public List<ListGeneralBillsDTO> createGeneralBill(CreateGeneralBillCommand cmd);

	GeneralBillHandler getGeneralBillHandler(String sourceType);

	void createChargingItem(CreateChargingItemCommand cmd);

	void syncRuiAnCMBillToZuolin(List<CMSyncObject> cmSyncObjectList, Integer namespaceId, Long contractCategoryId);

	default OutputStream exportOutputStreamAssetListByContractList(Object cmd, Long taskId){return null;}

	default OutputStream exportOutputStreamOrdersList(Object cmd, Long taskId){return null;}

	default void exportAssetListByParams(Object cmd){}

	default void exportOrdersListByParams(Object cmd){}

	void cancelGeneralBill(CancelGeneralBillCommand cmd);

	void injectSmsVars(NoticeInfo noticeInfo, List<Tuple<String, Object>> variables,Integer namespaceId);

	List<Long> getAllCommunity(Integer namespaceId, Long organizationId, Long appId, boolean includeNamespace);

	void checkNullProhibit(String name , Object object);

	AssetVendor checkAssetVendor(Integer namespaceId,Integer defaultNamespaceId);

	AssetVendorHandler getAssetVendorHandler(String vendorName);
	
	//缴费对接门禁
	void setDoorAccessParam(SetDoorAccessParamCommand cmd);
	ListDoorAccessParamResponse getDoorAccessParam(GetDoorAccessParamCommand cmd);
	void meterAutoReading(Boolean createPlanFlag);
	AssetDooraccessLog getDoorAccessInfo(GetDoorAccessInfoCommand cmd);
	List<AssetDooraccessParam> getDoorAccessParamList(byte status);
	
	void checkAssetPriviledgeForPropertyOrg(Long communityId, Long priviledgeId,Long currentOrgId);
	
	PaymentOrderBillDTO listPaymentBillDetail(ListPaymentBillDetailCmd cmd);
}
