
package com.everhomes.asset;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.servicemoduleapp.CreateAnAppMappingCommand;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.server.schema.tables.pojos.EhPaymentFormula;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface AssetService {

	List<AssetBillTemplateFieldDTO> listAssetBillTemplate(ListAssetBillTemplateCommand cmd);

	ListSimpleAssetBillsResponse listSimpleAssetBills(ListSimpleAssetBillsCommand cmd);

	HttpServletResponse exportAssetBills(ListSimpleAssetBillsCommand cmd, HttpServletResponse response);

	ImportDataResponse importAssetBills(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);

	AssetBillTemplateValueDTO creatAssetBill(CreatAssetBillCommand cmd);

	AssetBillTemplateValueDTO findAssetBill(FindAssetBillCommand cmd);

	AssetBillTemplateValueDTO updateAssetBill(UpdateAssetBillCommand cmd);

	void notifyUnpaidBillsContact(NotifyUnpaidBillsContactCommand cmd);

	void setBillsStatus(BillIdListCommand cmd, AssetBillStatus status);

	void deleteBill(DeleteBillCommand cmd);

	List<AssetBillTemplateFieldDTO> updateAssetBillTemplate(UpdateAssetBillTemplateCommand cmd);

	Boolean checkTokenRegister(CheckTokenRegisterCommand cmd);

	NotifyTimesResponse notifyTimes(ImportOwnerCommand cmd);

	AssetBillStatDTO getAssetBillStat(GetAssetBillStatCommand cmd);

	List<ListOrganizationsByPmAdminDTO> listOrganizationsByPmAdmin();

	ListBillsResponse listBills(ListBillsCommand cmd);

	ListBillItemsResponse listBillItems(ListBillItemsCommand cmd);

	void selectNotice(SelectedNoticeCommand cmd);

	ShowBillForClientDTO showBillForClient(ClientIdentityCommand cmd);

	ShowBillDetailForClientResponse getBillDetailForClient(BillIdCommand cmd);

	List<ListBillGroupsDTO> listBillGroups(OwnerIdentityCommand cmd);

	ShowCreateBillDTO showCreateBill(BillGroupIdCommand cmd);

	ShowBillDetailForClientResponse listBillDetailOnDateChange(ListBillDetailOnDateChangeCommand cmd);

	ListBillsDTO createBill(CreateBillCommand cmd);

	void OneKeyNotice(OneKeyNoticeCommand cmd);

	ListBillDetailResponse listBillDetail(ListBillDetailCommandStr cmd);

	List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd);

	void modifyBillStatus(BillIdCommand cmd);

	void exportPaymentBills(ListBillsCommand cmd, HttpServletResponse response);

	List<ListChargingItemsDTO> listChargingItems(OwnerIdentityCommand cmd);

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

	ListChargingStandardsResponse listOnlyChargingStandards(ListChargingStandardsCommand cmd);

	void configChargingItems(ConfigChargingItemsCommand cmd);

	void createChargingStandard(CreateChargingStandardCommand cmd);

	void modifyChargingStandard(ModifyChargingStandardCommand cmd);

	GetChargingStandardDTO getChargingStandardDetail(GetChargingStandardCommand cmd);

	DeleteChargingStandardDTO deleteChargingStandard(DeleteChargingStandardCommand cmd);

	List<ListAvailableVariablesDTO> listAvailableVariables(ListAvailableVariablesCommand cmd);

	List<EhPaymentFormula> createFormula(CreateFormulaCommand cmd);

	void createBillGroup(CreateBillGroupCommand cmd);

	void modifyBillGroup(ModifyBillGroupCommand cmd);

	void adjustBillGroupOrder(AdjustBillGroupOrderCommand cmd);

	ListChargingItemsForBillGroupResponse listChargingItemsForBillGroup(BillGroupIdCommand cmd);

	void addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd);

	DeleteChargingItemForBillGroupResponse deleteChargingItemForBillGroup(BillGroupRuleIdCommand cmd);

	DeleteBillGroupReponse deleteBillGroup(DeleteBillGroupCommand cmd);

	ListChargingItemDetailForBillGroupDTO listChargingItemDetailForBillGroup(BillGroupRuleIdCommand cmd);

	PreOrderDTO placeAnAssetOrder(PlaceAnAssetOrderCommand cmd);

	List<ListChargingItemsDTO> listAvailableChargingItems(OwnerIdentityCommand cmd);

	void paymentExpectanciesCalculate(PaymentExpectanciesCommand cmd);

	ListAutoNoticeConfigResponse listAutoNoticeConfig(ListAutoNoticeConfigCommand cmd);

	void autoNoticeConfig(AutoNoticeConfigCommand cmd);

	CheckEnterpriseHasArrearageResponse checkEnterpriseHasArrearage(CheckEnterpriseHasArrearageCommand cmd);

	List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd);

	List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd);

	FunctionDisableListDto functionDisableList(FunctionDisableListCommand cmd);

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

    void noticeTrigger(Integer namespaceId);
    
    List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd);
    
    /**
     * 用于接受支付系统的回调信息
     * @param cmd
     */
    void payNotify(OrderPaymentNotificationCommand cmd);
    	
    ListPaymentBillResp listPaymentBill(ListPaymentBillCmd cmd);
    
    ListBillsDTO createTestZJGKBill();

	IsProjectNavigateDefaultResp isProjectNavigateDefault(IsProjectNavigateDefaultCmd cmd);
	
	void transferOrderPaymentType();

    long getNextCategoryId(Integer namespaceId, Long aLong, String instanceConfig);

	void saveInstanceConfig(long categoryId, String ret);
	
    //add by tangcen
	void calculateRentForContract(CalculateRentCommand calculateRentCommand);

	Long getOriginIdFromMappingApp(Long moduleId, Long originId, long targetModuleId);

	void createAnAppMapping(CreateAnAppMappingCommand cmd);

	void updateAnAppMapping(UpdateAnAppMappingCommand cmd);
    
    IsUserExistInAddressResponse isUserExistInAddress(IsUserExistInAddressCmd cmd);
	
	ShowCreateBillSubItemListDTO showCreateBillSubItemList(ShowCreateBillSubItemListCmd cmd);

	void batchModifyBillSubItem(BatchModifyBillSubItemCommand cmd);
}
