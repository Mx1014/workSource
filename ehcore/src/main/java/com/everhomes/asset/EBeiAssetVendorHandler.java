package com.everhomes.asset;

import com.everhomes.pmkexing.PmKeXingBillService;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.pmkexing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
