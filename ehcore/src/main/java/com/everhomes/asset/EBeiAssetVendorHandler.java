package com.everhomes.asset;

import com.everhomes.asset.group.AssetGroupProvider;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.pmkexing.PmKeXingBillService;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.contract.NamespaceContractType;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmkexing.*;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ying.xiong on 2017/4/12.
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX + "EBEI")
public class EBeiAssetVendorHandler extends AssetVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(EBeiAssetVendorHandler.class);

    @Autowired
    private PmKeXingBillService keXingBillService;

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private ContractProvider contractProvider;
    
    @Autowired
    private AssetGroupProvider assetGroupProvider;

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
    public List<ListBillsDTO> listBills(Integer currentNamespaceId, ListBillsResponse response, ListBillsCommand cmd) {
        //修改传递参数为一个对象，卸货
        String contractNum = cmd.getContractNum();
        Long ownerId = cmd.getOwnerId();
        String ownerType = cmd.getOwnerType();
        String buildingName = cmd.getBuildingName();
        String apartmentName = cmd.getApartmentName();
        Long addressId = cmd.getAddressId();
        String billGroupName = cmd.getBillGroupName();
        Long billGroupId = cmd.getBillGroupId();
        Byte billStatus = cmd.getBillStatus();
        String dateStrBegin = cmd.getDateStrBegin();
        String dateStrEnd = cmd.getDateStrEnd();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        String targetName = cmd.getTargetName();
        Byte status = cmd.getStatus();
        String targetType = cmd.getTargetType();
        //卸货完毕
        List<ListBillsDTO> list = new ArrayList<>();
        if(targetType!=null && targetType.equals(AssetPaymentConstants.EH_USER)) {
            return list;
        }
        if(pageAnchor==null || pageAnchor == 0l){
            pageAnchor = 1l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        String fiProperty = null;
        if(billGroupId != null){
            PaymentBillGroup group = assetGroupProvider.getBillGroupById(billGroupId);
            if(group!=null){
                fiProperty = getFiPropertyName(group.getName());
            }
        }
        Long targetId = null;
        Integer pageOffSet = pageAnchor.intValue();
        GetLeaseContractBillOnFiPropertyRes res = keXingBillService.getFiPropertyBills(ownerId,contractNum,dateStrBegin,dateStrEnd,fiProperty,billStatus,targetName,targetId,pageSize,pageOffSet);
        //处理responseCode
        if(!res.getResponseCode().equals("200")){
            LOGGER.error("http get for ke xing failed, reason is : {}",res.getErrorMsg());
            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.HTTP_EBEI_ERROR,"http to eBei error");
        }
        //处理数据
        List<GetLeaseContractBillOnFiPropertyData> data = res.getData();
        for(int i = 0; i < data.size(); i ++){
            GetLeaseContractBillOnFiPropertyData source = data.get(i);
            ListBillsDTO dto = new ListBillsDTO();
            //dto.setNoticeTel(source.getNoticeTels());
            dto.setNoticeTelList(Arrays.asList(source.getNoticeTels().split(",")));
            dto.setOwnerType("community");
            dto.setDateStr(source.getChargePeriod());
            dto.setDateStrBegin(source.getChargePeriod());//为了兼容账单开始时间
            dto.setDateStrEnd(source.getChargePeriod());//为了兼容账单结束时间
            dto.setTargetName(source.getCustomerName());
//            dto.setTargetId();
            dto.setTargetType("eh_organization");
            dto.setContractNum(source.getContractNum());
            dto.setContractId(source.getContractId());
            dto.setAmountReceived(new BigDecimal(source.getAmountReceived()));
            dto.setAmountReceivable(new BigDecimal(source.getShouldMoney()));
            dto.setAmountOwed(new BigDecimal(source.getActualMoney()));
            dto.setBuildingName(source.getBuildingRename());
//            dto.setApartmentName();
            dto.setBillGroupName(getFiPropertyName(source.getFiProperty()));
            dto.setBillStatus(source.getIsPay().equals("已缴纳")?(byte)1:(byte)0);
            dto.setBillId(source.getBillId());
            list.add(dto);
        }
        if(res.getHasNextPag().equals("1")){
            response.setNextPageAnchor(pageOffSet.longValue()+1l);
        }else{
            response.setNextPageAnchor(null);
        }
        return list;
    }

    private String getFiPropertyName(String name) {
        if(name == null) return null;
        if(name.equals("开发商")){
            return "租金";
        }else if(name.equals("物业")){
            return "物业管理费";
        }else if(name.equals("租金")){
            return "开发商";
        }else if(name.equals("物业管理费")){
            return "物业";
        }
        return null;
    }

    @Override
    public List<BillDTO> listBillItems(String targetType, String billId, String targetName, Integer pageOffSet, Integer pageSize,Long ownerId, ListBillItemsResponse response, Long billGroupid) {
        List<BillDTO> list = new ArrayList<>();
        if (pageOffSet == null) {
            pageOffSet = 1;
        }
        if(pageSize == null){
            pageSize = 100;
        }
        GetLeaseContractReceivableRes res = keXingBillService.listFiCategoryBills(billId,ownerId,pageOffSet,pageSize);
        //处理responseCode
        if(!res.getResponseCode().equals("200")){
            LOGGER.error("http get for ke xing failed, reason is : {}",res.getErrorMsg());
            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.HTTP_EBEI_ERROR,"http to eBei error");
        }
        List<GetLeaseContractReceivableData> data =  res.getData();
        for(int i = 0; i < data.size(); i++){
            GetLeaseContractReceivableData source = data.get(i);
            BillDTO dto = new BillDTO();
            dto.setBillItemName(source.getFiCategory());
            dto.setTargetName(source.getCustomerName());
            dto.setDateStr(source.getChargePeriod());
            dto.setBillStatus(source.getIsPay().equals("已缴纳")?(byte)1:(byte)0);
            dto.setAmountReceived(new BigDecimal(source.getReceivedMoney()));
            dto.setAmountReceivable(new BigDecimal(source.getShouldMoney()));
            dto.setAmountOwed(new BigDecimal(source.getActualMoney()));
            dto.setApartmentName(source.getBuildingRename());
            dto.setBuildingName(source.getBuildingRename());
            dto.setTargetType("eh_organization");
            list.add(dto);
        }
        if(res.getHasNextPag().equals("1")){
            response.setNextPageAnchor(pageOffSet.longValue()+1l);
        }
        return list;
    }

    @Override
    public List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes, Long billGroupId) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId, Byte isOnlyOwedBill, String contractId, Integer namespaceId) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId, String targetType,Long organizationId) {
        ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
        BigDecimal amountReceivable = new BigDecimal("0");
        BigDecimal amountOwed = new BigDecimal("0");
//        TreeSet<Date> dateSet = new TreeSet<>();
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
        List<ShowBillDetailForClientDTO> list = new ArrayList<>();

        Integer pageOffSet = 1;
        Integer pageSize = 100;
        List<GetLeaseContractReceivableData> allData = new ArrayList<>();
        int count = 0;
        while(true){
            count++;
            GetLeaseContractReceivableRes res = keXingBillService.listFiCategoryBills(billId,ownerId,pageOffSet,pageSize);
            if(res.getResponseCode().equals("200")){
                allData.addAll(res.getData());
            }
            if(res.getHasNextPag().equals("0") || count > 1000){
                break;
            }
        }
        for(int i = 0; i < allData.size(); i++){
            GetLeaseContractReceivableData source = allData.get(i);
            ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();

//            try {
//                dateSet.add(yyyyMMdd.parse(source.getDateStrBegin()));
//                dateSet.add(yyyyMMdd.parse(source.getDateStrEnd()));
//            } catch (ParseException e) {}
            dto.setDateStrBegin(source.getDateStrBegin());
            dto.setDateStrEnd(source.getDateStrEnd());
            dto.setDateStr(source.getChargePeriod());
            BigDecimal amunt1 = new BigDecimal(source.getShouldMoney());
            dto.setAmountReceivable(amunt1);
            amountReceivable = amountReceivable.add(amunt1);
            BigDecimal amount2 = new BigDecimal(source.getActualMoney());
            dto.setAmountOwed(amount2);
            amountOwed = amountOwed.add(amount2);
            dto.setBillItemName(source.getFiCategory());
            dto.setAddressName(source.getBuildingRename()==null?"":source.getBuildingRename());
            list.add(dto);
        }
        response.setShowBillDetailForClientDTOList(list);
        response.setAmountReceivable(amountReceivable);
        //正中会要的是账期
//        response.setDatestr(yyyyMMdd.format(dateSet.pollFirst())+"~"+ yyyyMMdd.format(dateSet.pollLast()));
        if(list.size()>0){
            response.setDatestr(list.get(0).getDateStr());
        }
        //改成receivable,来自正中会需求 2018/1/23
//        response.setAmountOwed(amountOwed);
        response.setAmountOwed(amountReceivable);
        return response;
    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus, Long ownerId, String ownerType, String targetType, Long targetId, String dateStr, String contractId, Long billGroupId) {
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
    public void deleteBill(String billId, Long billGroupId) {
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
        List<BillStaticsDTO> list = new ArrayList<>();
        GetLeaseContractReceivableGroupForStatisticsRes res = keXingBillService.listBillStatistics(cmd.getOwnerId(),cmd.getDimension(),cmd.getBeginLimit(),cmd.getEndLimit());
        if(res.getResponseCode().equals("200")){
            List<GetLeaseContractReceivableGroupForStatisticsData> data = res.getData();
            for(int i = 0; i < data.size(); i++){
                GetLeaseContractReceivableGroupForStatisticsData source = data.get(i);
                BillStaticsDTO dto = new BillStaticsDTO();
                dto.setValueOfX(source.getHorizationalAxisName());
                dto.setAmountReceived(new BigDecimal(source.getAlreadyMoney()));
                dto.setAmountReceivable(new BigDecimal(source.getAmountReceivable()));
                dto.setAmountOwed(new BigDecimal(source.getArrearageMoney()));
                list.add(dto);
            }
        }
        return list;
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
    public PreOrderDTO placeAnAssetOrder(PlaceAnAssetOrderCommand cmd) {
        return null;
    }

    /**
     *
     * @modify 2018/1/16 start to change the filter condition. Present + unpaid -> present only
     */
    @Override
    public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
        List<ShowBillForClientV2DTO> list = new ArrayList<>();
        SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
        String endMonth = yyyyMM.format(new Date());
        String beginMonth = null;
        //非未来+未缴纳
        GetLeaseContractBillOnFiPropertyRes res = keXingBillService.getAllFiPropertyBills(cmd.getNamespaceId(),cmd.getOwnerId(),cmd.getTargetId(),cmd.getTargetType(),(byte)0,beginMonth,endMonth);
        List<GetLeaseContractBillOnFiPropertyData> data = res.getData();
        //没查到，则改为本月已经缴纳的
        if(data == null ||data.size() < 1){
            beginMonth = endMonth;
            res = keXingBillService.getAllFiPropertyBills(cmd.getNamespaceId(),cmd.getOwnerId(),cmd.getTargetId(),cmd.getTargetType(),(byte)1,beginMonth,endMonth);
        }
        data = res.getData();
        Map<String,List<GetLeaseContractBillOnFiPropertyData>> tabs = new HashMap<>();
        if(data == null) return list;
        for(int i = 0; i < data.size(); i++){
            GetLeaseContractBillOnFiPropertyData source = data.get(i);
            String key = source.getContractId()+"$"+source.getFiProperty();
            if(tabs.containsKey(key)){
                tabs.get(key).add(source);
            }else{
                ArrayList<GetLeaseContractBillOnFiPropertyData> values = new ArrayList<>();
                values.add(source);
                tabs.put(key,values);
            }
        }
        for(Map.Entry<String,List<GetLeaseContractBillOnFiPropertyData>> entry : tabs.entrySet()){
            LOGGER.info("tab key is = {}",entry.getKey());
            String[] fiPropertyAndContractId = entry.getKey().split("\\$");
            ShowBillForClientV2DTO dto = new ShowBillForClientV2DTO(getFiPropertyName(fiPropertyAndContractId[1]),fiPropertyAndContractId[0]);
            //把正中会传过来的contratId转为左邻存储的contractId
            String zuolinContractId = contractProvider.findContractIdByThirdPartyId(dto.getContractId(), NamespaceContractType.EBEI.getCode());
            dto.setContractId(zuolinContractId);
            List<GetLeaseContractBillOnFiPropertyData> values = entry.getValue();
            List<BillForClientV2> bills = new ArrayList<>();
            BigDecimal overallOwedAmount = new BigDecimal("0");
            Set<String> addresses = new HashSet<>();
            for(int i = 0; i < values.size(); i++){
                GetLeaseContractBillOnFiPropertyData value = values.get(i);
                BillForClientV2 bill = new BillForClientV2();
                bill.setBillId(value.getBillId());
                bill.setAmountOwed(value.getActualMoney());
                overallOwedAmount = overallOwedAmount.add(new BigDecimal(value.getActualMoney()));
                bill.setAmountReceivable(value.getShouldMoney());
//                bill.setBillDuration(value.getDateStrBegin()+"至"+value.getDateStrEnd());
                //改为账期
                bill.setBillDuration(value.getChargePeriod());
                addresses.add(value.getBuildingRename());
                bills.add(bill);
            }
//            dto.setBillGroupName(getFiPropertyName(values.get(0).getFiProperty()));
            dto.setBills(bills);
            dto.setOverAllAmountOwed(overallOwedAmount.toString());
            Iterator<String> it = addresses.iterator();
            StringBuilder sb = new StringBuilder();
            while(it.hasNext()){
                sb.append(it.next()+",");
            }
            sb.deleteCharAt(sb.length()-1);
            dto.setAddressStr(sb.toString());
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
        List<ListAllBillsForClientDTO> list = new ArrayList<>();
        SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
        String endMonth = yyyyMM.format(new Date());
        GetLeaseContractBillOnFiPropertyRes res = keXingBillService.getAllFiPropertyBills(cmd.getNamespaceId(),cmd.getOwnerId(),cmd.getTargetId(),cmd.getTargetType(),null,null,endMonth);
        List<GetLeaseContractBillOnFiPropertyData> data = res.getData();
        if(data == null) return new ArrayList<>();
        for(int i = 0; i < data.size(); i ++){
            GetLeaseContractBillOnFiPropertyData source = data.get(i);
            ListAllBillsForClientDTO dto = new ListAllBillsForClientDTO();
            dto.setBillGroupName(getFiPropertyName(source.getFiProperty()));
//            dto.setDateStrEnd(source.getDateStrEnd());
//            dto.setDateStrBegin(source.getDateStrBegin());
            //改为账期
            dto.setDateStrBegin(source.getChargePeriod());
            dto.setAmountReceivable(source.getShouldMoney());
            dto.setAmountOwed(source.getActualMoney());
            dto.setBillId(source.getBillId());
            dto.setChargeStatus(source.getIsPay().equals("已缴纳")?(byte)1:(byte)0);
            dto.setDateStr(source.getChargePeriod());
            String chargePeriod = source.getChargePeriod();
            try {
                Date parse = yyyyMM.parse(chargePeriod);
                dto.setDate(parse);
            } catch (ParseException e) {
                LOGGER.error("2.2.4.15 response chargePeriod pattern incorrect, chargePeriod = {},request pattern is yyyyMM",chargePeriod);
            }
            list.add(dto);
        }
        //按照时间降序排序
        Collections.sort(list, new Comparator<ListAllBillsForClientDTO>() {
            @Override
            public int compare(ListAllBillsForClientDTO o1, ListAllBillsForClientDTO o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return list;
    }

    @Override
    public void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ListPaymentBillResp listBillRelatedTransac(listBillRelatedTransacCommand cmd) {
        LOGGER.error("Insufficient privilege, EBeiAssetHandler");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
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

