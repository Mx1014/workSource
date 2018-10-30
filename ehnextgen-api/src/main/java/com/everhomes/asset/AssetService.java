
package com.everhomes.asset;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.asset.AdjustBillGroupOrderCommand;
import com.everhomes.rest.asset.AssetBillStatDTO;
import com.everhomes.rest.asset.AssetBillTemplateValueDTO;
import com.everhomes.rest.asset.AutoNoticeConfigCommand;
import com.everhomes.rest.asset.BatchImportBillsCommand;
import com.everhomes.rest.asset.BatchImportBillsResponse;
import com.everhomes.rest.asset.BatchModifyBillSubItemCommand;
import com.everhomes.rest.asset.BatchUpdateBillsToPaidCmd;
import com.everhomes.rest.asset.BatchUpdateBillsToSettledCmd;
import com.everhomes.rest.asset.BillGroupIdCommand;
import com.everhomes.rest.asset.BillGroupRuleIdCommand;
import com.everhomes.rest.asset.BillIdCommand;
import com.everhomes.rest.asset.BillItemIdCommand;
import com.everhomes.rest.asset.BillStaticsCommand;
import com.everhomes.rest.asset.BillStaticsDTO;
import com.everhomes.rest.asset.CalculateRentCommand;
import com.everhomes.rest.asset.CancelGeneralBillCommand;
import com.everhomes.rest.asset.CheckEnterpriseHasArrearageCommand;
import com.everhomes.rest.asset.CheckEnterpriseHasArrearageResponse;
import com.everhomes.rest.asset.CheckTokenRegisterCommand;
import com.everhomes.rest.asset.CreateBillCommand;
import com.everhomes.rest.asset.CreateChargingItemCommand;
import com.everhomes.rest.asset.CreateGeneralBillCommand;
import com.everhomes.rest.asset.CreatePaymentBillOrderCommand;
import com.everhomes.rest.asset.DeleteChargingItemForBillGroupResponse;
import com.everhomes.rest.asset.ExemptionItemIdCommand;
import com.everhomes.rest.asset.ExportBillTemplatesCommand;
import com.everhomes.rest.asset.FindAssetBillCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentResponse;
import com.everhomes.rest.asset.GetAreaAndAddressByContractCommand;
import com.everhomes.rest.asset.GetAreaAndAddressByContractDTO;
import com.everhomes.rest.asset.GetAssetBillStatCommand;
import com.everhomes.rest.asset.GetPayBillsForEntResultResp;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.IsUserExistInAddressCmd;
import com.everhomes.rest.asset.IsUserExistInAddressResponse;
import com.everhomes.rest.asset.JudgeAppShowPayCommand;
import com.everhomes.rest.asset.JudgeAppShowPayResponse;
import com.everhomes.rest.asset.ListAutoNoticeConfigCommand;
import com.everhomes.rest.asset.ListAutoNoticeConfigResponse;
import com.everhomes.rest.asset.ListAvailableVariablesCommand;
import com.everhomes.rest.asset.ListAvailableVariablesDTO;
import com.everhomes.rest.asset.ListBillDetailCommandStr;
import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.asset.ListBillExpectanciesOnContractCommand;
import com.everhomes.rest.asset.ListBillItemsCommand;
import com.everhomes.rest.asset.ListBillItemsResponse;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.ListBillsCommandForEnt;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListBillsResponse;
import com.everhomes.rest.asset.ListChargingItemDetailForBillGroupDTO;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.asset.ListChargingItemsForBillGroupResponse;
import com.everhomes.rest.asset.ListChargingStandardsCommand;
import com.everhomes.rest.asset.ListChargingStandardsDTO;
import com.everhomes.rest.asset.ListGeneralBillsDTO;
import com.everhomes.rest.asset.ListLateFineStandardsCommand;
import com.everhomes.rest.asset.ListLateFineStandardsDTO;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.asset.ListPaymentBillCmd;
import com.everhomes.rest.asset.ListPaymentBillResp;
import com.everhomes.rest.asset.ListSettledBillExemptionItemsResponse;
import com.everhomes.rest.asset.ListSimpleAssetBillsCommand;
import com.everhomes.rest.asset.ListSimpleAssetBillsResponse;
import com.everhomes.rest.asset.ListUploadCertificatesCommand;
import com.everhomes.rest.asset.ModifyNotSettledBillCommand;
import com.everhomes.rest.asset.ModifySettledBillCommand;
import com.everhomes.rest.asset.OneKeyNoticeCommand;
import com.everhomes.rest.asset.OwnerIdentityCommand;
import com.everhomes.rest.asset.PaymentExpectanciesCommand;
import com.everhomes.rest.asset.PaymentExpectanciesResponse;
import com.everhomes.rest.asset.PublicTransferBillCmdForEnt;
import com.everhomes.rest.asset.PublicTransferBillRespForEnt;
import com.everhomes.rest.asset.ReCalBillCommand;
import com.everhomes.rest.asset.SelectedNoticeCommand;
import com.everhomes.rest.asset.ShowCreateBillDTO;
import com.everhomes.rest.asset.ShowCreateBillSubItemListCmd;
import com.everhomes.rest.asset.ShowCreateBillSubItemListDTO;
import com.everhomes.rest.asset.UploadCertificateCommand;
import com.everhomes.rest.asset.UploadCertificateInfoDTO;
import com.everhomes.rest.asset.listBillExemtionItemsCommand;
import com.everhomes.rest.asset.listBillRelatedTransacCommand;
import com.everhomes.rest.contract.CMSyncObject;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.portal.AssetServiceModuleAppDTO;
import com.everhomes.util.Tuple;

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
	
	default void exportAssetListByParams(Object cmd){}

	void cancelGeneralBill(CancelGeneralBillCommand cmd);

	void injectSmsVars(NoticeInfo noticeInfo, List<Tuple<String, Object>> variables,Integer namespaceId);

	List<Long> getAllCommunity(Integer namespaceId, Long organizationId, Long appId, boolean includeNamespace);

	void checkNullProhibit(String name , Object object);

	AssetVendor checkAssetVendor(Integer namespaceId,Integer defaultNamespaceId);

	AssetVendorHandler getAssetVendorHandler(String vendorName);
	
}
