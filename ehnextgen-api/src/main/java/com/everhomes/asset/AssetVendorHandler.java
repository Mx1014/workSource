package com.everhomes.asset;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.asset.AssetBillStatDTO;
import com.everhomes.rest.asset.AssetBillTemplateValueDTO;
import com.everhomes.rest.asset.BatchImportBillsCommand;
import com.everhomes.rest.asset.BatchImportBillsResponse;
import com.everhomes.rest.asset.BillDTO;
import com.everhomes.rest.asset.BillIdAndType;
import com.everhomes.rest.asset.BillIdCommand;
import com.everhomes.rest.asset.BillItemIdCommand;
import com.everhomes.rest.asset.BillStaticsCommand;
import com.everhomes.rest.asset.BillStaticsDTO;
import com.everhomes.rest.asset.CreateBillCommand;
import com.everhomes.rest.asset.CreatePaymentBillOrderCommand;
import com.everhomes.rest.asset.ExemptionItemIdCommand;
import com.everhomes.rest.asset.ExportBillTemplatesCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentResponse;
import com.everhomes.rest.asset.GetAreaAndAddressByContractCommand;
import com.everhomes.rest.asset.GetAreaAndAddressByContractDTO;
import com.everhomes.rest.asset.ListAllBillsForClientCommand;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ListBillDetailCommand;
import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.asset.ListBillExpectanciesOnContractCommand;
import com.everhomes.rest.asset.ListBillItemsResponse;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.ListBillsCommandForEnt;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListBillsResponse;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.asset.ListPaymentBillResp;
import com.everhomes.rest.asset.ListSettledBillExemptionItemsResponse;
import com.everhomes.rest.asset.ListSimpleAssetBillsResponse;
import com.everhomes.rest.asset.PaymentExpectanciesResponse;
import com.everhomes.rest.asset.PlaceAnAssetOrderCommand;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.rest.asset.ShowBillForClientV2Command;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;
import com.everhomes.rest.asset.ShowCreateBillDTO;
import com.everhomes.rest.asset.ShowCreateBillSubItemListCmd;
import com.everhomes.rest.asset.ShowCreateBillSubItemListDTO;
import com.everhomes.rest.asset.listBillExemtionItemsCommand;
import com.everhomes.rest.asset.listBillRelatedTransacCommand;
import com.everhomes.rest.asset.bill.ListOpenBillsCommand;
import com.everhomes.rest.contract.CMSyncObject;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.util.RuntimeErrorException;

/**
 * Created by ying.xiong on 2017/4/11.
 */
public abstract class AssetVendorHandler {
    static final String ASSET_VENDOR_PREFIX = "AssetVendor-";
    public static final Logger LOGGER = LoggerFactory.getLogger(AssetVendorHandler.class);

    ListSimpleAssetBillsResponse listSimpleAssetBills(Long ownerId, String ownerType, Long targetId, String targetType, Long organizationId,
        Long addressId, String tenant, Byte status, Long startTime, Long endTime, Long pageAnchor, Integer pageSize){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
        "Insufficient privilege");
        };

    AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType,
             Long templateVersion, Long organizationId, String dateStr, Long tenantId, String tenantType, Long addressId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    AssetBillStatDTO getAssetBillStat(String tenantType, Long tenantId, Long addressId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    List<ListBillsDTO> listBills(Integer currentNamespaceId,ListBillsResponse response, ListBillsCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    List<BillDTO> listBillItems(String targetType,String billId, String targetName, Integer pageOffSet, Integer pageSize, Long ownerId, ListBillItemsResponse response, Long billGroupId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes, Long billGroupId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOnlyOwedBill,String contractId, Integer namespaceId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    public ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId,String targetType, Long organizationId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    public ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus,Long ownerId, String ownerType, String targetType, Long targetId, String dateStr,String contractId, Long billGroupId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    ListBillDetailResponse listBillDetail(ListBillDetailCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    void deleteBill(String billId, Long billGroupId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    void deleteBillItem(BillItemIdCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    void deletExemptionItem(ExemptionItemIdCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    ShowCreateBillDTO showCreateBill(Long billGroupId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    ListBillsDTO createBill(CreateBillCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    void modifyBillStatus(BillIdCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    ListSettledBillExemptionItemsResponse listBillExemptionItems(listBillExemtionItemsCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    void exportRentalExcelTemplate(HttpServletResponse response){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    void updateBillsToSettled(UpdateBillsToSettled cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    PreOrderDTO placeAnAssetOrder(PlaceAnAssetOrderCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    BatchImportBillsResponse batchImportBills(BatchImportBillsCommand cmd, MultipartFile file){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }
    ListPaymentBillResp listBillRelatedTransac(listBillRelatedTransacCommand cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }
    List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd) {
    	LOGGER.error("Insufficient privilege, handler listPayeeAccounts");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }
    void payNotify(OrderPaymentNotificationCommand cmd) {
    	LOGGER.error("Insufficient privilege, handler payNotify");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }
    
    public ShowCreateBillSubItemListDTO showCreateBillSubItemList(ShowCreateBillSubItemListCmd cmd) {
		LOGGER.error("Insufficient privilege, handler showCreateBillSubItemList");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
	}

	public PreOrderDTO payBillsForEnt(PlaceAnAssetOrderCommand cmd) {
		LOGGER.error("Insufficient privilege, handler payBillsForEnt");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
	}
	
	public PreOrderDTO createOrder(CreatePaymentBillOrderCommand cmd) {
    	LOGGER.error("Insufficient privilege, handler createOrder");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    protected PreOrderCommand preparePaymentBillOrder(CreatePaymentBillOrderCommand cmd) {
		LOGGER.error("Insufficient privilege, handler preparePaymentBillOrder");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
	}

	public List<ListBizPayeeAccountDTO> listBizPayeeAccounts(Long orgnizationId, String... tags) {
		LOGGER.error("Insufficient privilege, handler listBizPayeeAccounts");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
	}
	
	public void syncRuiAnCMBillToZuolin(CMSyncObject cmSyncObject, Integer namespaceId){
		LOGGER.error("Insufficient privilege, handler syncRuiAnCMBillToZuolin");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
	}

	public List<ListBillsDTO> listOpenBills(ListOpenBillsCommand cmd) {
		LOGGER.error("Insufficient privilege, handler listOpenBills");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
	}

}
