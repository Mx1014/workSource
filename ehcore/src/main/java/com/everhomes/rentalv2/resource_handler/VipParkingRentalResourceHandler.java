package com.everhomes.rentalv2.resource_handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingSpace;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DownloadUtils;
import com.everhomes.util.RuntimeErrorException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX + "vip_parking")
public class VipParkingRentalResourceHandler implements RentalResourceHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(VipParkingRentalResourceHandler.class);

    @Autowired
    private ParkingProvider parkingProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private Rentalv2Service rentalv2Service;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;

    private ThreadLocal<SimpleDateFormat> datetimeSF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private ThreadLocal<SimpleDateFormat> timeSF = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));

    @Override
    public RentalResource getRentalResourceById(Long id) {

        ParkingLot parkingLot = parkingProvider.findParkingLotById(id);
        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(parkingLot.getNamespaceId(),
                RentalV2ResourceType.VIP_PARKING.getCode());

        List<String> spaces = rentalv2Provider.listOverTimeSpaces(parkingLot.getNamespaceId(), type.getId(),
                RentalV2ResourceType.VIP_PARKING.getCode(), parkingLot.getId());

        Integer count = parkingProvider.countParkingSpace(parkingLot.getNamespaceId(), parkingLot.getOwnerType(),
                parkingLot.getOwnerId(), parkingLot.getId(), spaces);

        RentalResource resource = new RentalResource();
        resource.setId(parkingLot.getId());
        resource.setResourceName(parkingLot.getName());
        resource.setResourceType(RentalV2ResourceType.VIP_PARKING.getCode());
        resource.setResourceTypeId(type.getId());
        resource.setResourceCounts(Double.valueOf(count));
        resource.setAutoAssign(NormalFlag.NONEED.getCode());
        resource.setMultiUnit(NormalFlag.NONEED.getCode());
        resource.setCommunityId(parkingLot.getOwnerId());

        return resource;
    }

    @Override
    public void updateRentalResource(String resourceJson) {

    }

    @Override
    public void buildDefaultRule(AddDefaultRuleAdminCommand addCmd) {
        addCmd.setPriceRules(rentalCommonService.buildDefaultPriceRule(Collections.singletonList(RentalType.HOUR.getCode())));
        addCmd.setRentalTypes(Collections.singletonList(RentalType.HOUR.getCode()));
        //设置按小时模式 每天开放时间
        TimeIntervalDTO timeIntervalDTO = new TimeIntervalDTO();
        timeIntervalDTO.setTimeStep(0.5D);
        timeIntervalDTO.setBeginTime(8D);
        timeIntervalDTO.setEndTime(22D);
        addCmd.setTimeIntervals(Collections.singletonList(timeIntervalDTO));
    }

    @Override
    public void setRuleOwnerTypeByResource(QueryDefaultRuleAdminCommand queryRuleCmd, RentalResource resource) {
        queryRuleCmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
        queryRuleCmd.setOwnerId(resource.getCommunityId());
    }



    @Override
    public void exportRentalBills(SearchRentalOrdersCommand cmd, HttpServletResponse response) {
        Integer pageSize = Integer.MAX_VALUE;
		Integer pageNum = 1;
        if (cmd.getPageNum() != null) {
        	pageNum = cmd.getPageNum();
        }
        List<RentalOrder> bills = rentalv2Provider.searchRentalOrders(cmd.getResourceTypeId(), cmd.getResourceType(),
                cmd.getResourceId(), cmd.getBillStatus(), cmd.getStartTime(), cmd.getEndTime(),cmd.getTag1(),
                cmd.getTag2(),null,cmd.getKeyword(), cmd.getPageAnchor(), pageSize,pageNum);

        if(null == bills){
            bills = new ArrayList<>();
        }
        List<RentalOrderDTO> dtos = new ArrayList<>();
        for (RentalOrder bill : bills) {
            RentalOrderDTO dto = ConvertHelper.convert(bill, RentalOrderDTO.class);
            rentalv2Service.convertRentalOrderDTO(dto, bill);
            dtos.add(dto);
        }
        ByteArrayOutputStream out = createRentalBillsStream(dtos);

        DownloadUtils.download(out, response);
    }

    private ByteArrayOutputStream createRentalBillsStream(List<RentalOrderDTO> dtos) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (null == dtos || dtos.isEmpty()) {
            return out;
        }
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("rentalBill");
        this.createRentalBillsBookSheetHead(sheet);
        for (RentalOrderDTO dto : dtos ) {
            this.setNewRentalBillsBookRow(sheet, dto);
        }
        try {
            wb.write(out);
            wb.close();

        } catch (IOException e) {
            LOGGER.error("export is fail", e);
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CREATE_EXCEL,
                    "export is fail.");
        }
        return out;
    }

    private void createRentalBillsBookSheetHead(Sheet sheet){

        Row row = sheet.createRow(sheet.getLastRowNum());
        int i =-1 ;
        row.createCell(++i).setCellValue("序号");
        row.createCell(++i).setCellValue("预约人");
        row.createCell(++i).setCellValue("手机号");
        row.createCell(++i).setCellValue("预约车牌");
        row.createCell(++i).setCellValue("预约车位");
        row.createCell(++i).setCellValue("订单时间");
        row.createCell(++i).setCellValue("预约时间");
        row.createCell(++i).setCellValue("订单金额（元）");
        row.createCell(++i).setCellValue("支付方式");
        row.createCell(++i).setCellValue("订单状态");
    }

    private void setNewRentalBillsBookRow(Sheet sheet ,RentalOrderDTO dto){
        VipParkingUseInfoDTO parkingInfo = JSONObject.parseObject(dto.getCustomObject(), VipParkingUseInfoDTO.class);
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        int i = -1;
        //序号
        row.createCell(++i).setCellValue(row.getRowNum());
        //预约人
        row.createCell(++i).setCellValue(dto.getUserName());
        //手机号
        row.createCell(++i).setCellValue(dto.getUserPhone());
        //预约车牌
        row.createCell(++i).setCellValue(parkingInfo.getPlateNumber());
        //预约车位
        row.createCell(++i).setCellValue(parkingInfo.getSpaceNo());
        //订单时间
        row.createCell(++i).setCellValue(datetimeSF.get().format(new Timestamp(dto.getCreateTime())));
        StringBuilder sb = new StringBuilder();
        long dayTime =  24*3600*1000l;
        //预约时间
        if(null!=dto.getStartTime()){
            sb.append(datetimeSF.get().format(new Timestamp(dto.getStartTime())));
            if ((dto.getStartTime() / dayTime) == (dto.getEndTime() / dayTime)) //同一天
                sb.append("-").append(timeSF.get().format(new Timestamp(dto.getEndTime())));
            else
                sb.append("-").append(datetimeSF.get().format(new Timestamp(dto.getEndTime())));
        }
        row.createCell(++i).setCellValue(sb.toString());
        //总价
        if(null != dto.getTotalAmount())
            row.createCell(++i).setCellValue(dto.getTotalAmount().toString());
        else
            row.createCell(++i).setCellValue("0");

        //支付方式
        if(null != dto.getVendorType())
            row.createCell(++i).setCellValue(VendorType.fromCode(dto.getVendorType()).getDescribe());
        else
            row.createCell(++i).setCellValue("无");
        //订单状态
        if(dto.getStatus() != null)
            row.createCell(++i).setCellValue(statusToString(dto.getStatus()));
        else
            row.createCell(++i).setCellValue("");

    }

    private String statusToString(Byte status) {

        SiteBillStatus siteBillStatus = SiteBillStatus.fromCode(status);
        return null != siteBillStatus ? siteBillStatus.getDescribe() : "";
    }

}
