package com.everhomes.asset;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.contract.FindContractCommand;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.user.admin.ImportDataResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface AssetService {

    List<AssetBillTemplateFieldDTO> listAssetBillTemplate(ListAssetBillTemplateCommand cmd);
    ListSimpleAssetBillsResponse listSimpleAssetBills(ListSimpleAssetBillsCommand cmd);
    HttpServletResponse exportAssetBills(ListSimpleAssetBillsCommand cmd,HttpServletResponse response);
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

    void upodateBillStatusOnContractStatusChange(Long contractId,String targetStatus);

    PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd);

    void exportRentalExcelTemplate(HttpServletResponse response);

    FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd);

    void updateBillsToSettled(UpdateBillsToSettled cmd);

    GetAreaAndAddressByContractDTO getAreaAndAddressByContract(FindContractCommand cmd);

    PaymentBillItems findBillItemById(Long billItemId);

    PaymentExemptionItems findExemptionItemById(Long ExemptionItemId);

//    void synchronizeZJGKBill();
}
