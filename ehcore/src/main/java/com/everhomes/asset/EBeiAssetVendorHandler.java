package com.everhomes.asset;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.pmkexing.PmKeXingBillService;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.pmkexing.*;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ying.xiong on 2017/4/12.
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX + "EBEI")
public class EBeiAssetVendorHandler implements AssetVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(EBeiAssetVendorHandler.class);

    @Autowired
    private PmKeXingBillService keXingBillService;

    private static ZuolinAssetVendorHandler zuolinAssetVendorHandler = new ZuolinAssetVendorHandler();

    @Override
    public ListSimpleAssetBillsResponse listSimpleAssetBills(Long ownerId, String ownerType, Long targetId, String targetType, Long organizationId, Long addressId, String tenant, Byte status, Long startTime, Long endTime, Long pageAnchor, Integer pageSize) {

        ListPmKeXingBillsCommand command = new ListPmKeXingBillsCommand();

        command.setOrganizationId(organizationId);
        Byte kexingStatus = null;
        if(status == null) {
            kexingStatus = null;
        }
        else if(AssetBillStatus.PAID.equals(AssetBillStatus.fromStatus(status))) {
            kexingStatus = PmKeXingBillStatus.PAID.getCode();
        }
        else if(AssetBillStatus.UNPAID.equals(AssetBillStatus.fromStatus(status))) {
            kexingStatus = PmKeXingBillStatus.UNPAID.getCode();
        }
        if(kexingStatus != null) {
            command.setBillStatus(kexingStatus);
        }

        if(pageAnchor == null) {
            pageAnchor = 0L;
        }
        command.setPageOffset(pageAnchor.intValue());
        command.setPageSize(pageSize);
        ListPmKeXingBillsResponse keXingBills = keXingBillService.listPmKeXingBills(command);

        ListSimpleAssetBillsResponse response = new ListSimpleAssetBillsResponse();
        if(keXingBills.getNextPageOffset() != null) {
            response.setNextPageAnchor(keXingBills.getNextPageOffset().longValue());
        }

        List<SimpleAssetBillDTO> dtos = new ArrayList<>();
        if(keXingBills.getBills() != null) {
            keXingBills.getBills().forEach(bill -> {
                SimpleAssetBillDTO dto = new SimpleAssetBillDTO();
                dto.setAccountPeriod(covertStrToTimestamp(bill.getBillDate()));

                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("bill status: {}", bill.getBillStatus());
                }
                if(PmKeXingBillStatus.PAID.getCode().equals(bill.getBillStatus())) {
                    dto.setStatus(AssetBillStatus.PAID.getCode());
                } else if(PmKeXingBillStatus.UNPAID.getCode().equals(bill.getBillStatus())) {
                    dto.setStatus(AssetBillStatus.UNPAID.getCode());
                }
                dto.setPeriodAccountAmount(bill.getReceivableAmount());
                dto.setUnpaidPeriodAccountAmount(bill.getUnpaidAmount());

                dtos.add(dto);
            });
        }

        response.setBills(dtos);
        return response;
    }

    @Override
    public AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType,
                    Long templateVersion, Long organizationId, String dateStr, Long tenantId, String tenantType, Long addressId) {
        GetPmKeXingBillCommand command = new GetPmKeXingBillCommand();
        command.setOrganizationId(organizationId);
        command.setDateStr(dateStr);
        PmKeXingBillDTO bill = keXingBillService.getPmKeXingBill(command);
        AssetBillTemplateValueDTO dto = new AssetBillTemplateValueDTO();
        dto.setPeriodAccountAmount(bill.getReceivableAmount());
        dto.setUnpaidPeriodAccountAmount(bill.getUnpaidAmount());
        List<FieldValueDTO> dtos = new ArrayList<>();
        if(bill.getItems() != null && bill.getItems().size() > 0) {
            bill.getItems().forEach(item -> {
                FieldValueDTO value = new FieldValueDTO();
                value.setFieldDisplayName(item.getName());
                value.setFieldValue(item.getAmount().toString());
                dtos.add(value);
            });
        }
        dto.setDtos(dtos);
        return dto;
    }

    @Override
    public AssetBillStatDTO getAssetBillStat(String tenantType, Long tenantId, Long addressId) {
        GetPmKeXingBillStatCommand command = new GetPmKeXingBillStatCommand();
        command.setOrganizationId(tenantId);
        PmKeXingBillStatDTO statDTO = keXingBillService.getPmKeXingBillStat(command);

        AssetBillStatDTO dto = new AssetBillStatDTO();
        dto.setUnpaidAmount(statDTO.getUnpaidAmount());
        dto.setUnpaidMonth(statDTO.getUnpaidMonth());
        return dto;
    }

    @Override
    public List<ListBillsDTO> listBills(String communityIdentifier, String contractNum, Integer currentNamespaceId, Long ownerId, String ownerType, String buildingName, String apartmentName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, Integer pageOffSet, Integer pageSize, String targetName, Byte status, String targetType, ListBillsResponse response) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public List<BillDTO> listBillItems(String targetType, String billId, String targetName, Integer pageOffSet, Integer pageSize) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId, Byte isOnlyOwedBill, String contractId) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(String billId, String targetType) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus, Long ownerId, String ownerType, String targetType, Long targetId, String dateStr, String contractId) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ListBillDetailResponse listBillDetail(ListBillDetailCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void deleteBill(String l) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void deleteBillItem(BillItemIdCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void deletExemptionItem(ExemptionItemIdCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ShowCreateBillDTO showCreateBill(Long billGroupId) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ListBillsDTO createBill(CreateBillCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void modifyBillStatus(BillIdCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ListSettledBillExemptionItemsResponse listBillExemptionItems(listBillExemtionItemsCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void exportRentalExcelTemplate(HttpServletResponse response) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void updateBillsToSettled(UpdateBillsToSettled cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public PlaceAnAssetOrderResponse placeAnAssetOrder(PlaceAnAssetOrderCommand cmd) {
        return null;
    }


    private Timestamp covertStrToTimestamp(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            Date date=format.parse(str);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
