package com.everhomes.rentalv2.resource_handler;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminCommand;
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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX + "default")
public class DefaultRentalResourceHandler implements RentalResourceHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRentalResourceHandler.class);

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private Rentalv2Service rentalv2Service;

    private ThreadLocal<SimpleDateFormat> datetimeSF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Override
    public RentalResource getRentalResourceById(Long id) {

        RentalResource rs =this.rentalv2Provider.getRentalSiteById(id);


        return rs;
    }

    @Override
    public void updateRentalResource(String resourceJson) {

    }

    @Override
    public void setRuleOwnerTypeByResource(QueryDefaultRuleAdminCommand queryRuleCmd, RentalResource resource) {
        queryRuleCmd.setOwnerType(RentalOwnerType.ORGANIZATION.getCode());
        queryRuleCmd.setOwnerId(resource.getOrganizationId());
    }

    @Override
    public void exportRentalBills(ListRentalBillsCommand cmd, HttpServletResponse response) {
        Integer pageSize = Integer.MAX_VALUE;
        List<RentalOrder> bills = rentalv2Provider.listRentalBills(cmd.getResourceTypeId(), cmd.getOrganizationId(), cmd.getCommunityId(),
                cmd.getRentalSiteId(), new CrossShardListingLocator(), cmd.getBillStatus(), cmd.getVendorType(), pageSize, cmd.getStartTime(), cmd.getEndTime(),
                null, null);
        if(null == bills){
            bills = new ArrayList<>();
        }
        List<RentalBillDTO> dtos = new ArrayList<>();
        for (RentalOrder bill : bills) {
            // 在转换bill到dto的时候统一先convert一下  modify by wuhan 20160804
            RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
            rentalv2Service.mappingRentalBillDTO(dto, bill, null);
            dto.setSiteItems(new ArrayList<>());
            List<RentalItemsOrder> rentalSiteItems = rentalv2Provider
                    .findRentalItemsBillBySiteBillId(dto.getRentalBillId(), bill.getResourceType());
            if(null != rentalSiteItems)
                for (RentalItemsOrder rib : rentalSiteItems) {
                    SiteItemDTO siDTO = new SiteItemDTO();
                    siDTO.setCounts(rib.getRentalCount());

                    siDTO.setItemName(rib.getItemName());
                    siDTO.setItemPrice(rib.getTotalMoney());
                    dto.getSiteItems().add(siDTO);
                }

            dtos.add(dto);
        }

//		URL rootPath = Rentalv2ServiceImpl.class.getResource("/");
//		String filePath =rootPath.getPath() + downloadDir ;
//		File file = new File(filePath);
//		if(!file.exists())
//			file.mkdirs();
//		filePath = filePath + "RentalBills"+System.currentTimeMillis()+".xlsx";
        //新建了一个文件
        ByteArrayOutputStream out = createRentalBillsStream(dtos);

        DownloadUtils.download(out, response);

//		return download(filePath,response);
    }


    private ByteArrayOutputStream createRentalBillsStream(List<RentalBillDTO> dtos) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (null == dtos || dtos.isEmpty()) {
            return out;
        }
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("rentalBill");

        this.createRentalBillsBookSheetHead(sheet);
        for (RentalBillDTO dto : dtos ) {
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
        row.createCell(++i).setCellValue("名称");
        row.createCell(++i).setCellValue("下单时间");
        row.createCell(++i).setCellValue("使用详情");
        row.createCell(++i).setCellValue("预订人");
        row.createCell(++i).setCellValue("总价");
        row.createCell(++i).setCellValue("支付方式");
        row.createCell(++i).setCellValue("订单状态");
    }

    private void setNewRentalBillsBookRow(Sheet sheet ,RentalBillDTO dto){
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        int i = -1;
        //序号
        row.createCell(++i).setCellValue(row.getRowNum());
        //名称 - 资源名称
        row.createCell(++i).setCellValue(dto.getSiteName());
        //下单时间
        if(null!=dto.getReserveTime())
            row.createCell(++i).setCellValue(datetimeSF.get().format(new Timestamp(dto.getReserveTime())));
        else
            row.createCell(++i).setCellValue("");
        //使用详情
        row.createCell(++i).setCellValue(dto.getUseDetail());
        //预约人
        row.createCell(++i).setCellValue(dto.getUserName());
        //总价
        if(null != dto.getTotalPrice())
            row.createCell(++i).setCellValue(dto.getTotalPrice().toString());
        else
            row.createCell(++i).setCellValue("0");
        //支付方式
        if(null != dto.getVendorType())
            row.createCell(++i).setCellValue(VendorType.fromCode(dto.getVendorType()).getDescribe());
        else
            row.createCell(++i).setCellValue("");
        //订单状态
        if(dto.getStatus() != null)
            row.createCell(++i).setCellValue(statusToString(dto.getStatus(),dto.getPaidPrice()));
        else
            row.createCell(++i).setCellValue("");

    }

    private String statusToString(Byte status, BigDecimal paidPrice) {
        SiteBillStatus siteBillStatus = SiteBillStatus.fromCode(status);
        if (siteBillStatus == SiteBillStatus.FAIL && paidPrice.compareTo(new BigDecimal(0))>0)
            return SiteBillStatus.FAIL_PAID.getDescribe();
        else if (siteBillStatus == SiteBillStatus.FAIL)
            return "已取消(未支付)";
        return null != siteBillStatus ? siteBillStatus.getDescribe() : "";
    }


}
