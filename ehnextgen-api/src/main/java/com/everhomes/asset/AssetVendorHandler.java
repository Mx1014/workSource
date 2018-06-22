package com.everhomes.asset;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.List;

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

    ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOnlyOwedBill,String contractId, Integer namespaceId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId,String targetType, Long organizationId){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus,Long ownerId, String ownerType, String targetType, Long targetId, String dateStr,String contractId, Long billGroupId){
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

    List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd){
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    };

    List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd){
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

	public ShowCreateBillSubItemListDTO showCreateBillSubItemList(Long billGroupId) {
		LOGGER.error("Insufficient privilege, handler showCreateBillSubItemList");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
	}
}
