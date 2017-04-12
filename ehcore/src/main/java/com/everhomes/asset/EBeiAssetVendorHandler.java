package com.everhomes.asset;

import com.everhomes.pmkexing.PmKeXingBillService;
import com.everhomes.rest.asset.AssetBillTemplateValueDTO;
import com.everhomes.rest.asset.ListSimpleAssetBillsResponse;
import com.everhomes.rest.asset.SimpleAssetBillDTO;
import com.everhomes.rest.pmkexing.GetPmKeXingBillCommand;
import com.everhomes.rest.pmkexing.ListPmKeXingBillsCommand;
import com.everhomes.rest.pmkexing.ListPmKeXingBillsResponse;
import com.everhomes.rest.pmkexing.PmKeXingBillDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
        command.setBillStatus(status);
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
                dto.setStatus(Byte.parseByte(bill.getBillStatus()));
                dto.setPeriodAccountAmount(bill.getReceivableAmount());

                dtos.add(dto);
            });
        }

        response.setBills(dtos);
        return response;
    }

    @Override
    public AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType, Long templateVersion) {
        GetPmKeXingBillCommand command = new GetPmKeXingBillCommand();
        PmKeXingBillDTO bill = keXingBillService.getPmKeXingBill(command);
        return null;
    }

    private Timestamp covertStrToTimestamp(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        try {
            Date date=format.parse(str);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
