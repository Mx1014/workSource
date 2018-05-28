
package com.everhomes.asset;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.asset.AddOrModifyRuleForBillGroupCommand;
import com.everhomes.rest.asset.AdjustBillGroupOrderCommand;
import com.everhomes.rest.asset.AssetBillStatDTO;
import com.everhomes.rest.asset.AssetBillStatus;
import com.everhomes.rest.asset.AssetBillTemplateFieldDTO;
import com.everhomes.rest.asset.AssetBillTemplateValueDTO;
import com.everhomes.rest.asset.AutoNoticeConfigCommand;
import com.everhomes.rest.asset.BatchImportBillsCommand;
import com.everhomes.rest.asset.BatchImportBillsResponse;
import com.everhomes.rest.asset.BillGroupIdCommand;
import com.everhomes.rest.asset.BillGroupRuleIdCommand;
import com.everhomes.rest.asset.BillIdCommand;
import com.everhomes.rest.asset.BillIdListCommand;
import com.everhomes.rest.asset.BillItemIdCommand;
import com.everhomes.rest.asset.BillStaticsCommand;
import com.everhomes.rest.asset.BillStaticsDTO;
import com.everhomes.rest.asset.CheckEnterpriseHasArrearageCommand;
import com.everhomes.rest.asset.CheckEnterpriseHasArrearageResponse;
import com.everhomes.rest.asset.CheckTokenRegisterCommand;
import com.everhomes.rest.asset.ClientIdentityCommand;
import com.everhomes.rest.asset.ConfigChargingItemsCommand;
import com.everhomes.rest.asset.CreatAssetBillCommand;
import com.everhomes.rest.asset.CreateBillCommand;
import com.everhomes.rest.asset.CreateBillGroupCommand;
import com.everhomes.rest.asset.CreateChargingStandardCommand;
import com.everhomes.rest.asset.CreateFormulaCommand;
import com.everhomes.rest.asset.DeleteBillCommand;
import com.everhomes.rest.asset.DeleteBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupReponse;
import com.everhomes.rest.asset.DeleteChargingItemForBillGroupResponse;
import com.everhomes.rest.asset.DeleteChargingStandardCommand;
import com.everhomes.rest.asset.DeleteChargingStandardDTO;
import com.everhomes.rest.asset.ExemptionItemIdCommand;
import com.everhomes.rest.asset.ExportBillTemplatesCommand;
import com.everhomes.rest.asset.FindAssetBillCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentResponse;
import com.everhomes.rest.asset.FunctionDisableListCommand;
import com.everhomes.rest.asset.FunctionDisableListDto;
import com.everhomes.rest.asset.GetAreaAndAddressByContractCommand;
import com.everhomes.rest.asset.GetAreaAndAddressByContractDTO;
import com.everhomes.rest.asset.GetAssetBillStatCommand;
import com.everhomes.rest.asset.GetChargingStandardCommand;
import com.everhomes.rest.asset.GetChargingStandardDTO;
import com.everhomes.rest.asset.ImportOwnerCommand;
import com.everhomes.rest.asset.ListAllBillsForClientCommand;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ListAssetBillTemplateCommand;
import com.everhomes.rest.asset.ListAutoNoticeConfigCommand;
import com.everhomes.rest.asset.ListAutoNoticeConfigResponse;
import com.everhomes.rest.asset.ListAvailableVariablesCommand;
import com.everhomes.rest.asset.ListAvailableVariablesDTO;
import com.everhomes.rest.asset.ListBillDetailCommand;
import com.everhomes.rest.asset.ListBillDetailOnDateChangeCommand;
import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.asset.ListBillExpectanciesOnContractCommand;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ListBillItemsCommand;
import com.everhomes.rest.asset.ListBillItemsResponse;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListBillsResponse;
import com.everhomes.rest.asset.ListChargingItemDetailForBillGroupDTO;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.asset.ListChargingItemsForBillGroupResponse;
import com.everhomes.rest.asset.ListChargingStandardsCommand;
import com.everhomes.rest.asset.ListChargingStandardsDTO;
import com.everhomes.rest.asset.ListChargingStandardsResponse;
import com.everhomes.rest.asset.ListLateFineStandardsCommand;
import com.everhomes.rest.asset.ListLateFineStandardsDTO;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.asset.ListPaymentBillResp;
import com.everhomes.rest.asset.ListSettledBillExemptionItemsResponse;
import com.everhomes.rest.asset.ListSimpleAssetBillsCommand;
import com.everhomes.rest.asset.ListSimpleAssetBillsResponse;
import com.everhomes.rest.asset.ModifyBillGroupCommand;
import com.everhomes.rest.asset.ModifyChargingStandardCommand;
import com.everhomes.rest.asset.ModifyNotSettledBillCommand;
import com.everhomes.rest.asset.ModifySettledBillCommand;
import com.everhomes.rest.asset.NotifyTimesResponse;
import com.everhomes.rest.asset.NotifyUnpaidBillsContactCommand;
import com.everhomes.rest.asset.OneKeyNoticeCommand;
import com.everhomes.rest.asset.OwnerIdentityCommand;
import com.everhomes.rest.asset.PaymentExpectanciesCommand;
import com.everhomes.rest.asset.PaymentExpectanciesResponse;
import com.everhomes.rest.asset.PlaceAnAssetOrderCommand;
import com.everhomes.rest.asset.ReCalBillCommand;
import com.everhomes.rest.asset.SelectedNoticeCommand;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.rest.asset.ShowBillForClientV2Command;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;
import com.everhomes.rest.asset.ShowCreateBillDTO;
import com.everhomes.rest.asset.UpdateAssetBillCommand;
import com.everhomes.rest.asset.UpdateAssetBillTemplateCommand;
import com.everhomes.rest.asset.listBillExemtionItemsCommand;
import com.everhomes.rest.asset.listBillRelatedTransacCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.server.schema.tables.pojos.EhPaymentFormula;

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

	ListBillDetailResponse listBillDetail(ListBillDetailCommand cmd);

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

	PaymentExpectanciesResponse paymentExpectancies(PaymentExpectanciesCommand cmd);

	void generateBillsOnContractSigned(String contractNum);

	void upodateBillStatusOnContractStatusChange(Long contractId, String targetStatus);

	PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd);

	void exportRentalExcelTemplate(HttpServletResponse response);

	FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd);

	void updateBillsToSettled(UpdateBillsToSettled cmd);

	GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd);

	PaymentBillItems findBillItemById(Long billItemId);

	PaymentExemptionItems findExemptionItemById(Long ExemptionItemId);

	void updateBillSwitchOnTime();

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

	void paymentExpectancies_re_struct(PaymentExpectanciesCommand cmd);

	ListAutoNoticeConfigResponse listAutoNoticeConfig(ListAutoNoticeConfigCommand cmd);

	void autoNoticeConfig(AutoNoticeConfigCommand cmd);

	void activeAutoBillNotice();

	CheckEnterpriseHasArrearageResponse checkEnterpriseHasArrearage(CheckEnterpriseHasArrearageCommand cmd);

	List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd);

	List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd);

	FunctionDisableListDto functionDisableList(FunctionDisableListCommand cmd);

	void syncCustomer(Integer namespaceId);

	List<ListLateFineStandardsDTO> listLateFineStandards(ListLateFineStandardsCommand cmd);

	void activeLateFine();

	void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response);

	BatchImportBillsResponse batchImportBills(BatchImportBillsCommand cmd, MultipartFile file);

	void linkCustomerToBill(String code, Long ownerUid, String identifierToken);

	ListPaymentBillResp listBillRelatedTransac(listBillRelatedTransacCommand cmd);

    void reCalBill(ReCalBillCommand cmd);

    void modifySettledBill(ModifySettledBillCommand cmd);
    
    List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd);
}
