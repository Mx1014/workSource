package com.everhomes.asset;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.techpark.rental.RentalServiceImpl;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/2/20.
 */
@Component
public class AssetServiceImpl implements AssetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetServiceImpl.class);

    final String downloadDir ="\\download\\";

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Override
    public List<AssetBillTemplateFieldDTO> listAssetBillTemplate(ListAssetBillTemplateCommand cmd) {

        List<AssetBillTemplateFieldDTO> dtos = new ArrayList<>();
        Long templateVersion = assetProvider.getTemplateVersion(cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetId(),cmd.getTargetType());
        if(templateVersion == 0L) {
            dtos = assetProvider.findTemplateFieldByTemplateVersion(0L, cmd.getOwnerType(), 0L, cmd.getTargetType(), 0L);
        } else {
            dtos = assetProvider.findTemplateFieldByTemplateVersion(
                    cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType(), templateVersion);
        }

        return dtos;
    }

    @Override
    public ListSimpleAssetBillsResponse listSimpleAssetBills(ListSimpleAssetBillsCommand cmd) {

        List<Long> tenantIds = new ArrayList<>();
        String tenantType = null;
        if(cmd.getTenant() != null) {
            Community community = communityProvider.findCommunityById(cmd.getTargetId());
            if(community != null) {
                //园区 查公司表
                if(CommunityType.COMMERCIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
                    tenantType = TenantType.ENTERPRISE.getCode();
                    SearchOrganizationCommand command = new SearchOrganizationCommand();
                    command.setKeyword(cmd.getTenant());
                    command.setCommunityId(cmd.getTargetId());
                    GroupQueryResult result = organizationSearcher.query(command);

                    if(result != null) {
                        tenantIds.addAll(result.getIds());
                    }
                }

                //小区 查用户所在门牌
                else if(CommunityType.RESIDENTIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
                    tenantType = TenantType.FAMILY.getCode();
                }

            }
        }

        CrossShardListingLocator locator=new CrossShardListingLocator();
        if(cmd.getPageAnchor()!=null){
            locator.setAnchor(cmd.getPageAnchor());
        }
        List<AssetBill> bills  = assetProvider.listAssetBill(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType(),
                tenantIds, tenantType, cmd.getAddressId(), cmd.getStatus(), cmd.getStartTime(),cmd.getEndTime(), locator, cmd.getPageSize() + 1);


        ListSimpleAssetBillsResponse response = new ListSimpleAssetBillsResponse();
        if (bills.size() > cmd.getPageSize()) {
            response.setNextPageAnchor(locator.getAnchor());
            bills = bills.subList(0, cmd.getPageSize());
        }

        List<SimpleAssetBillDTO> dtos = convertAssetBillToSimpleDTO(bills);
        response.setBills(dtos);
        return response;
    }

    private List<SimpleAssetBillDTO> convertAssetBillToSimpleDTO(List<AssetBill> bills) {
        List<SimpleAssetBillDTO> dtos =  bills.stream().map(bill -> {
            SimpleAssetBillDTO dto = ConvertHelper.convert(bill, SimpleAssetBillDTO.class);
            Address address = addressProvider.findAddressById(bill.getAddressId());
            if(address != null) {
                dto.setBuildingName(address.getBuildingName());
                dto.setApartmentName(address.getApartmentName());
            }

            // 当月的账单要把滞纳金和往期未付的账单一起计入总计应收
            if(compareMonth(bill.getAccountPeriod()) == 0) {

                List<BigDecimal> unpaidAmounts = assetProvider.listPeriodUnpaidAccountAmount(bill.getOwnerId(), bill.getOwnerType(),
                        bill.getTargetId(), bill.getTargetType(), bill.getAddressId(), bill.getTenantType(), bill.getTenantId(),bill.getAccountPeriod());

                if(unpaidAmounts != null && unpaidAmounts.size() > 0) {
                    BigDecimal pastUnpaid = bill.getLateFee();
                    for(BigDecimal unpaid : unpaidAmounts) {
                        pastUnpaid = pastUnpaid.add(unpaid);
                    }

                    dto.setPeriodAccountAmount(dto.getPeriodAccountAmount().add(pastUnpaid));
                }
            }

            return dto;
        }).collect(Collectors.toList());

        return dtos;
    }

    private int compareMonth(Timestamp compareValue) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Timestamp(System.currentTimeMillis()));
        int monthNow = c.get(Calendar.MONTH);

        c.setTime(compareValue);
        int monthCompare = c.get(Calendar.MONTH);

        return (monthNow-monthCompare);
    }

    @Override
    public HttpServletResponse exportAssetBills(ListSimpleAssetBillsCommand cmd, HttpServletResponse response) {
        Integer pageSize = Integer.MAX_VALUE;
        cmd.setPageSize(pageSize);

        ListSimpleAssetBillsResponse billResponse = listSimpleAssetBills(cmd);
        List<SimpleAssetBillDTO> dtos = billResponse.getBills();

        URL rootPath = RentalServiceImpl.class.getResource("/");
        String filePath =rootPath.getPath() + this.downloadDir ;
        File file = new File(filePath);
        if(!file.exists())
            file.mkdirs();
        filePath = filePath + "AssetBills"+System.currentTimeMillis()+".xlsx";
        //新建了一个文件
        this.createAssetBillsBook(filePath, dtos);

        return download(filePath,response);
    }

    public void createAssetBillsBook(String path,List<SimpleAssetBillDTO> dtos) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("assetBills");

        this.createAssetBillsBookSheetHead(sheet);
        for (SimpleAssetBillDTO dto : dtos ) {
            this.setNewAssetBillsBookRow(sheet, dto);
        }

        try {
            FileOutputStream out = new FileOutputStream(path);

            wb.write(out);
            wb.close();
            out.close();

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw RuntimeErrorException.errorWith(AssetServiceErrorCode.SCOPE,
                    AssetServiceErrorCode.ERROR_CREATE_EXCEL,
                    e.getLocalizedMessage());
        }

    }

    private void createAssetBillsBookSheetHead(Sheet sheet){
        Row row = sheet.createRow(sheet.getLastRowNum());
        int i =-1 ;
        row.createCell(++i).setCellValue("序号");
        row.createCell(++i).setCellValue("账期");
        row.createCell(++i).setCellValue("楼栋");
        row.createCell(++i).setCellValue("门牌号");
        row.createCell(++i).setCellValue("催缴手机号");
        row.createCell(++i).setCellValue("总计应收");
        row.createCell(++i).setCellValue("状态");
    }

    private void setNewAssetBillsBookRow(Sheet sheet ,SimpleAssetBillDTO dto){
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getId());
        if(null != dto.getAccountPeriod()) {
            row.createCell(++i).setCellValue(dto.getAccountPeriod().toString());
        } else {
            row.createCell(++i).setCellValue("");
        }

        row.createCell(++i).setCellValue(dto.getBuildingName());
        row.createCell(++i).setCellValue(dto.getApartmentName());
        row.createCell(++i).setCellValue(dto.getContactNo());
        row.createCell(++i).setCellValue(dto.getPeriodAccountAmount().toString());
        row.createCell(++i).setCellValue(AssetBillStatus.fromStatus(dto.getStatus()).getName());

    }

    public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

            // 读取完成删除文件
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw RuntimeErrorException.errorWith(QualityServiceErrorCode.SCOPE,
                    QualityServiceErrorCode.ERROR_DOWNLOAD_EXCEL,
                    ex.getLocalizedMessage());

        }
        return response;
    }

    @Override
    public ImportDataResponse importAssetBills(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
        return null;
    }

    //非当月则没有滞纳金 所以数据库里面不加lateFee
    private void getTotalAmount(AssetBill bill) {
        BigDecimal periodAccountAmount = bill.getRental().add(bill.getPropertyManagementFee()).add(bill.getUnitMaintenanceFund())
                .add(bill.getPrivateWaterFee()).add(bill.getPrivateElectricityFee()).add(bill.getPublicWaterFee())
                .add(bill.getPublicElectricityFee()).add(bill.getWasteDisposalFee()).add(bill.getPollutionDischargeFee())
                .add(bill.getExtraAirConditionFee()).add(bill.getCoolingWaterFee()).add(bill.getWeakCurrentSlotFee())
                .add(bill.getDepositFromLease()).add(bill.getMaintenanceFee()).add(bill.getGasOilProcessFee())
                .add(bill.getHatchServiceFee()).add(bill.getPressurizedFee()).add(bill.getParkingFee()).add(bill.getOther());

        bill.setPeriodAccountAmount(periodAccountAmount);
        bill.setPeriodUnpaidAccountAmount(bill.getPeriodAccountAmount());

    }

    @Override
    public AssetBillTemplateValueDTO creatAssetBill(CreatAssetBillCommand cmd) {
        AssetBill bill = ConvertHelper.convert(cmd, AssetBill.class);
        bill.setCreatorUid(UserContext.current().getUser().getId());
        getTotalAmount(bill);
        assetProvider.creatAssetBill(bill);

        FindAssetBillCommand command = new FindAssetBillCommand();
        command.setId(bill.getId());
        command.setOwnerId(bill.getOwnerId());
        command.setOwnerType(bill.getOwnerType());
        command.setTargetId(bill.getTargetId());
        command.setTargetType(bill.getTargetType());
        command.setTemplateVersion(bill.getTemplateVersion());
        AssetBillTemplateValueDTO dto = findAssetBill(command);

        return dto;
    }

    @Override
    public AssetBillTemplateValueDTO findAssetBill(FindAssetBillCommand cmd) {
        AssetBillTemplateValueDTO dto = new AssetBillTemplateValueDTO();

        AssetBill bill = getAssetBill(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());
        dto.setId(bill.getId());
        dto.setNamespaceId(bill.getNamespaceId());
        dto.setOwnerType(bill.getOwnerType());
        dto.setOwnerId(bill.getOwnerId());
        dto.setTargetType(bill.getTargetType());
        dto.setTargetId(bill.getTargetId());
        dto.setTemplateVersion(cmd.getTemplateVersion());

        List<AssetBillTemplateFieldDTO> templateFields = assetProvider.findTemplateFieldByTemplateVersion(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType(), cmd.getTemplateVersion());
        if(templateFields != null && templateFields.size() > 0) {
            List<FieldValueDTO> valueDTOs = new ArrayList<>();
            Field[] fields = bill.getClass().getDeclaredFields();
            for(AssetBillTemplateFieldDTO fieldDTO : templateFields) {
                FieldValueDTO valueDTO = new FieldValueDTO();
                if(fieldDTO.getFieldCustomName() != null) {
                    valueDTO.setFieldDisplayName(fieldDTO.getFieldCustomName());
                } else {
                    valueDTO.setFieldDisplayName(fieldDTO.getFieldDisplayName());
                }
                valueDTO.setFieldName(fieldDTO.getFieldName());
                valueDTO.setFieldType(fieldDTO.getFieldType());

                for (Field requestField : fields) {
                    requestField.setAccessible(true);
                    // private类型
                    if (requestField.getModifiers() == 2) {
                        if(requestField.getName().equals(fieldDTO.getFieldName())){
                            // 字段值
                            try {
                                if(requestField.get(bill) != null)
                                    valueDTO.setFieldValue(requestField.get(bill).toString());

                                break;
                            } catch (IllegalArgumentException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }

                valueDTOs.add(valueDTO);
            }
            dto.setDtos(valueDTOs);
        }
        return dto;
    }

    private AssetBill getAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType) {
        AssetBill bill = assetProvider.findAssetBill(id, ownerId, ownerType, targetId, targetType);

        if (bill == null) {
            LOGGER.error("cannot find asset bill. bill: id = " + id + ", ownerId = " + ownerId
                    + ", ownerType = " + ownerType + ", targetId = " + targetId + ", targetType = " + targetType);
            throw RuntimeErrorException.errorWith(AssetServiceErrorCode.SCOPE,
                    AssetServiceErrorCode.ASSET_BILL_NOT_EXIST,
                    "账单不存在");
        }

        return bill;
    }

    @Override
    public AssetBillTemplateValueDTO updateAssetBill(UpdateAssetBillCommand cmd) {
        AssetBill bill = getAssetBill(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());

        bill = ConvertHelper.convert(cmd, AssetBill.class);

        bill.setUpdateUid(UserContext.current().getUser().getId());
        getTotalAmount(bill);
        assetProvider.updateAssetBill(bill);

        FindAssetBillCommand command = new FindAssetBillCommand();
        command.setId(bill.getId());
        command.setOwnerId(bill.getOwnerId());
        command.setOwnerType(bill.getOwnerType());
        command.setTargetId(bill.getTargetId());
        command.setTargetType(bill.getTargetType());
        command.setTemplateVersion(bill.getTemplateVersion());
        AssetBillTemplateValueDTO dto = findAssetBill(command);

        return dto;
    }

    @Override
    public void notifyUnpaidBillsContact(NotifyUnpaidBillsContactCommand cmd) {


    }

    @Override
    public void setBillsStatus(BillIdListCommand cmd, AssetBillStatus status) {
        if(cmd.getIds() != null && cmd.getIds().size() > 0) {
            for(Long id : cmd.getIds()) {
                AssetBill bill = assetProvider.findAssetBill(id, cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());

                if(bill != null) {
                    bill.setStatus(status.getCode());
                    bill.setUpdateUid(UserContext.current().getUser().getId());
                    assetProvider.updateAssetBill(bill);
                }

            }
        }

    }

    @Override
    public void deleteBill(DeleteBillCommand cmd) {
        AssetBill bill = getAssetBill(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());

        bill.setStatus(AssetBillStatus.INACTIVE.getCode());
        bill.setUpdateUid(UserContext.current().getUser().getId());
        bill.setDeleteUid(UserContext.current().getUser().getId());
        bill.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        assetProvider.updateAssetBill(bill);
    }

    @Override
    public List<AssetBillTemplateFieldDTO> updateAssetBillTemplate(UpdateAssetBillTemplateCommand cmd) {

        if(cmd.getDtos() != null && cmd.getDtos().size() > 0) {
            for(AssetBillTemplateFieldDTO dto : cmd.getDtos()) {
                AssetBillTemplateFields field = ConvertHelper.convert(dto, AssetBillTemplateFields.class);
                field.setTemplateVersion(field.getTemplateVersion() + 1);
                assetProvider.creatTemplateField(field);
            }
        }

        ListAssetBillTemplateCommand command = new ListAssetBillTemplateCommand();
        command.setOwnerType(cmd.getDtos().get(0).getOwnerType());
        command.setOwnerId(cmd.getDtos().get(0).getOwnerId());
        command.setTargetType(cmd.getDtos().get(0).getTargetType());
        command.setTargetId(cmd.getDtos().get(0).getTargetId());

        List<AssetBillTemplateFieldDTO> dtos = listAssetBillTemplate(command);
        return dtos;
    }
}
