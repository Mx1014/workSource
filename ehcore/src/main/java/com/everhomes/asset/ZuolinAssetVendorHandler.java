package com.everhomes.asset;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contract.ContractService;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/4/11.
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX + "ZUOLIN")
public class ZuolinAssetVendorHandler implements AssetVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZuolinAssetVendorHandler.class);
    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AssetService assetService;

//    @Autowired
//    private ContractService contractService;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private UserService userService;

    @Override
    public ListSimpleAssetBillsResponse listSimpleAssetBills(Long ownerId, String ownerType, Long targetId, String targetType, Long organizationId, Long addressId, String tenant, Byte status, Long startTime, Long endTime, Long pageAnchor, Integer pageSize) {
        List<Long> tenantIds = new ArrayList<>();
        String tenantType = null;
        if(tenant != null) {
            Community community = communityProvider.findCommunityById(targetId);
            if(community != null) {
                //园区 查公司表
                if(CommunityType.COMMERCIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
                    tenantType = TenantType.ENTERPRISE.getCode();
                    SearchOrganizationCommand command = new SearchOrganizationCommand();
                    command.setKeyword(tenant);
                    command.setCommunityId(targetId);
                    GroupQueryResult result = organizationSearcher.query(command);

                    if(result != null) {
                        tenantIds.addAll(result.getIds());
                    }
                }

                //小区 查用户所属家庭
                else if(CommunityType.RESIDENTIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
                    tenantType = TenantType.FAMILY.getCode();
                    List<User> users = userProvider.listUserByNickName(tenant);

                    if(users != null && users.size() > 0) {
                        for(User user : users) {
                            List<UserGroup> list = userProvider.listUserActiveGroups(user.getId(), GroupDiscriminator.FAMILY.getCode());
                            if(list != null && list.size() > 0) {
                                for(UserGroup userGroup : list) {
                                    tenantIds.add(userGroup.getGroupId());
                                }
                            }
                        }
                    }

                }

            }
        }

        CrossShardListingLocator locator=new CrossShardListingLocator();
        if(pageAnchor!=null){
            locator.setAnchor(pageAnchor);
        }

        pageSize = PaginationConfigHelper.getPageSize(configurationProvider, pageSize);
        if(organizationId != null) {
            tenantIds.add(organizationId);
        }

        List<AssetBill> bills  = assetProvider.listAssetBill(ownerId, ownerType, targetId, targetType,
                tenantIds, tenantType, addressId, status, startTime,endTime, locator, pageSize + 1);


        ListSimpleAssetBillsResponse response = new ListSimpleAssetBillsResponse();
        if (bills.size() > pageSize) {
            response.setNextPageAnchor(locator.getAnchor());
            bills = bills.subList(0, pageSize);
        }

        List<SimpleAssetBillDTO> dtos = convertAssetBillToSimpleDTO(bills);
        response.setBills(dtos);
        return response;
    }

    @Override
    public AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType,
                    Long templateVersion, Long organizationId, String dateStr, Long tenantId, String tenantType, Long addressId) {
        AssetBillTemplateValueDTO dto = new AssetBillTemplateValueDTO();
        AssetBill bill = null;
        if(id != null) {
            bill = assetProvider.findAssetBill(id, ownerId, ownerType, targetId, targetType);
        } else {
            bill = assetProvider.findAssetBill(ownerId, ownerType, targetId, targetType, dateStr, tenantId, tenantType, addressId);
        }


        if (bill == null) {
            LOGGER.error("cannot find asset bill. bill: id = " + id + ", ownerId = " + ownerId
                    + ", ownerType = " + ownerType + ", targetId = " + targetId + ", targetType = " + targetType);
            throw RuntimeErrorException.errorWith(AssetServiceErrorCode.SCOPE,
                    AssetServiceErrorCode.ASSET_BILL_NOT_EXIST,
                    "账单不存在");
        }

        dto.setId(bill.getId());
        dto.setNamespaceId(bill.getNamespaceId());
        dto.setOwnerType(bill.getOwnerType());
        dto.setOwnerId(bill.getOwnerId());
        dto.setTargetType(bill.getTargetType());
        dto.setTargetId(bill.getTargetId());
        dto.setTemplateVersion(bill.getTemplateVersion());
        dto.setTenantId(bill.getTenantId());
        dto.setTenantType(bill.getTenantType());
        dto.setAddressId(bill.getAddressId());
        List<AssetBillTemplateFieldDTO> templateFields = null;
        if(bill.getTemplateVersion() == 0L) {
            templateFields = assetProvider.findTemplateFieldByTemplateVersion(0L, ownerType, 0L, targetType, 0L);
        } else {
            templateFields = assetProvider.findTemplateFieldByTemplateVersion(ownerId, ownerType, targetId, targetType, bill.getTemplateVersion());
        }

        if(templateFields != null && templateFields.size() > 0) {
            List<FieldValueDTO> valueDTOs = new ArrayList<>();
            Field[] fields = EhAssetBills.class.getDeclaredFields();
            for(AssetBillTemplateFieldDTO fieldDTO : templateFields) {
                if(AssetBillTemplateSelectedFlag.SELECTED.equals(AssetBillTemplateSelectedFlag.fromCode(fieldDTO.getSelectedFlag()))) {
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
            }

            BigDecimal totalAmounts = bill.getPeriodAccountAmount();
            // 当月的账单要把滞纳金和往期未付的账单一起计入总计应收
            if(compareMonth(bill.getAccountPeriod()) == 0) {
                BigDecimal pastUnpaid = new BigDecimal(0);
                List<BigDecimal> unpaidAmounts = assetProvider.listPeriodUnpaidAccountAmount(bill.getOwnerId(), bill.getOwnerType(),
                        bill.getTargetId(), bill.getTargetType(), bill.getAddressId(), bill.getTenantType(), bill.getTenantId(),bill.getAccountPeriod());

                if(unpaidAmounts != null && unpaidAmounts.size() > 0) {

                    for(BigDecimal unpaid : unpaidAmounts) {
                        pastUnpaid = pastUnpaid.add(unpaid);
                    }

                    FieldValueDTO unpaidAmountsDTO = new FieldValueDTO();
                    unpaidAmountsDTO.setFieldDisplayName("往期欠费");
                    unpaidAmountsDTO.setFieldType("BigDecimal");
                    unpaidAmountsDTO.setFieldValue(pastUnpaid.toString());

                    valueDTOs.add(unpaidAmountsDTO);

                }
                totalAmounts = totalAmounts.add(pastUnpaid);
                totalAmounts = totalAmounts.add(bill.getLateFee());
            }

            FieldValueDTO totalAmountsDTO = new FieldValueDTO();
            totalAmountsDTO.setFieldDisplayName("总计应收");
            totalAmountsDTO.setFieldType("BigDecimal");
            totalAmountsDTO.setFieldValue(totalAmounts.toString());

            valueDTOs.add(totalAmountsDTO);

            dto.setDtos(valueDTOs);
            dto.setPeriodAccountAmount(totalAmounts);
            if(AssetBillStatus.PAID.equals(AssetBillStatus.fromStatus(bill.getStatus()))) {
                dto.setUnpaidPeriodAccountAmount(BigDecimal.ZERO);
            }

            if(AssetBillStatus.UNPAID.equals(AssetBillStatus.fromStatus(bill.getStatus()))) {
                dto.setUnpaidPeriodAccountAmount(totalAmounts);
            }

        }
        return dto;
    }

    @Override
    public AssetBillStatDTO getAssetBillStat(String tenantType, Long tenantId, Long addressId) {
        List<AssetBill> bills = assetProvider.listUnpaidBills(tenantType, tenantId, addressId);
        AssetBillStatDTO dto = new AssetBillStatDTO();
        dto.setUnpaidAmount(BigDecimal.ZERO);
        Set<Timestamp> accountPeriod = new HashSet<>();
        bills.forEach(bill -> {
        	dto.setUnpaidAmount(dto.getUnpaidAmount().add(bill.getPeriodAccountAmount()));
            accountPeriod.add(bill.getAccountPeriod());
        });
        BigDecimal unpaidMonth = new BigDecimal(accountPeriod.size());
        dto.setUnpaidMonth(unpaidMonth);
        return dto;
    }

    @Override
    public List<ListBillsDTO> listBills(String communityIdentifier,String contractNum,Integer currentNamespaceId, Long ownerId, String ownerType, String buildingName,String apartmentName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, Integer pageOffSet, Integer pageSize, String targetName, Byte status,String targetType,ListBillsResponse response) {
        List<ListBillsDTO> list = assetProvider.listBills(contractNum,currentNamespaceId,ownerId,ownerType, billGroupName,billGroupId,billStatus,dateStrBegin,dateStrEnd,pageOffSet,pageSize,targetName,status,targetType);
        return list;
    }

    @Override
    public List<BillDTO> listBillItems(String targetType, String billId, String targetName, Integer pageOffSet, Integer pageSize) {
        List<BillDTO> list = assetProvider.listBillItems(Long.parseLong(billId),targetName,pageOffSet,pageSize);
        return list;
    }

    @Override
    public List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes) {
        List<Long> billIds = new ArrayList<>();
        for(int i = 0; i < billIdAndTypes.size(); i++){
            BillIdAndType billIdAndType = billIdAndTypes.get(i);
            billIds.add(Long.parseLong(billIdAndType.getBillId()));
        }
        return assetProvider.listNoticeInfoByBillId(billIds);
    }

    @Override
    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill,String contractId) {
        ShowBillForClientDTO response = new ShowBillForClientDTO();
        if(targetType.equals("eh_user")) {
            targetId = UserContext.current().getUser().getId();
        }
        Long cid = Long.parseLong(contractId);
        List<BillDetailDTO> billDetailDTOList = assetProvider.listBillForClient(ownerId,ownerType,targetType,targetId,billGroupId,isOwedBill,cid);
        HashSet<String> dateStrFilter = new HashSet<>();
        BigDecimal amountOwed = new BigDecimal("0");
        for(int i = 0; i < billDetailDTOList.size(); i++) {
            BillDetailDTO dto = billDetailDTOList.get(i);
            dateStrFilter.add(dto.getDateStr());
            amountOwed.add(dto.getAmountOwed());
        }
        response.setAmountOwed(amountOwed);
        response.setBillPeriodMonths(dateStrFilter.size());
        response.setBillDetailDTOList(billDetailDTOList);
        return response;
    }

    @Override
    public FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        FindUserInfoForPaymentResponse res = new FindUserInfoForPaymentResponse();
        List<FindUserInfoForPaymentDTO> list = new ArrayList<>();
        String targeType = cmd.getTargetType();
        ListCustomerContractsCommand cmd1 = new ListCustomerContractsCommand();
        cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
        cmd1.setCommunityId(cmd.getCommunityId());
        if(targeType.equals(AssetPaymentStrings.EH_USER)){
            cmd1.setTargetId(UserContext.currentUserId());
            cmd1.setTargetType(CustomerType.INDIVIDUAL.getCode());
            res.setCustomerName(UserContext.current().getUser().getNickName());
        }else if(targeType.equals(AssetPaymentStrings.EH_ORGANIZATION)){
            cmd1.setTargetId(cmd.getTargetId());
            cmd1.setTargetType(CustomerType.ENTERPRISE.getCode());
            OrganizationDTO organizationById = organizationService.getOrganizationById(cmd.getTargetId());
            res.setCustomerName(organizationById.getName());
        }else{
            throw new RuntimeException("用户类型错误");
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String handler = configurationProvider.getValue(namespaceId, "contractService", "");
        ContractService contractService = PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
        List<ContractDTO> dtos = contractService.listCustomerContracts(cmd1);
        for(int i = 0; i < dtos.size(); i++){
            FindUserInfoForPaymentDTO dto = new FindUserInfoForPaymentDTO();
            dto.setContractNum(dtos.get(i).getContractNumber());
            dto.setContractId(String.valueOf(dtos.get(i).getId()));
            list.add(dto);
        }
        res.setContractList(list);
        if(dtos.size()>0){
            ContractDTO contractDTO = dtos.get(0);
            FindContractCommand cmd2 = new FindContractCommand();
            cmd2.setId(contractDTO.getId());
            cmd2.setContractNumber(contractDTO.getContractNumber());
            cmd2.setCommunityId(cmd.getCommunityId());
            cmd2.setPartyAId(contractDTO.getPartyAId());
            GetAreaAndAddressByContractCommand cmd3 = new GetAreaAndAddressByContractCommand();
            cmd3.setCommunityId(cmd2.getCommunityId());
            cmd3.setContractId(String.valueOf(cmd2.getId()));
            cmd3.setContractNumber(cmd2.getContractNumber());
            cmd3.setNamespaceId(cmd2.getNamespaceId());
            cmd3.setOwnerType("community");
            cmd3.setPartyAId(null);
            cmd3.setTargetType(cmd.getTargetType());
            GetAreaAndAddressByContractDTO areaAndAddressByContract = assetService.getAreaAndAddressByContract(cmd3);
            res.setAddressNames(areaAndAddressByContract.getAddressNames());
            res.setAreaSizesSum(areaAndAddressByContract.getAreaSizesSum());
        }
        return res;
    }

    @Override
    public GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd1) {
        FindContractCommand cmd = new FindContractCommand();
        cmd.setPartyAId(cmd1.getPartyAId());
        cmd.setCommunityId(cmd1.getCommunityId());
        cmd.setContractNumber(cmd1.getContractNumber());
        cmd.setId(Long.parseLong(cmd1.getContractId()));
        cmd.setNamespaceId(cmd1.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd1.getNamespaceId());
        GetAreaAndAddressByContractDTO dto = new GetAreaAndAddressByContractDTO();
        List<String> addressNames = new ArrayList<>();
        Double areaSize = 0d;
        Integer namespaceId = cmd.getNamespaceId()==null? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
        String handler = configurationProvider.getValue(namespaceId, "contractService", "");
        ContractService contractService = PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
        ContractDetailDTO contract = contractService.findContract(cmd);
        List<BuildingApartmentDTO> apartments = contract.getApartments();
        for(int i = 0; i < apartments.size(); i++) {
            BuildingApartmentDTO building = apartments.get(i);
            String addressName;
            addressName = building.getBuildingName()+building.getApartmentName();
            addressNames.add(addressName);
            if(building.getChargeArea()!=null){
                areaSize += building.getChargeArea();
            }
        }
        dto.setAddressNames(addressNames);
        dto.setAreaSizesSum(String.valueOf(areaSize));
        return dto;
    }

    @Override
    public ListBillDetailResponse listBillDetail(ListBillDetailCommand cmd) {
        ListBillDetailVO vo = assetProvider.listBillDetail(cmd.getBillId());
        ListBillDetailResponse response = ConvertHelper.convert(vo, ListBillDetailResponse.class);
        List<ExemptionItemDTO> dtos = response.getBillGroupDTO().getExemptionItemDTOList();
        for(int i = 0; i< dtos.size(); i ++) {
            ExemptionItemDTO dto = dtos.get(i);
            if(dto.getAmount().compareTo(new BigDecimal("0"))==-1) {
                dto.setIsPlus((byte)0);
                dto.setAmount(dto.getAmount().divide(new BigDecimal("-1")));
            }else{
                dto.setIsPlus((byte)1);
            }
        }
        return response;
    }

    @Override
    public void deleteBill(String l) {
        assetProvider.deleteBill(Long.parseLong(l));
    }

    @Override
    public void deleteBillItem(BillItemIdCommand cmd) {
        this.dbProvider.execute((TransactionStatus status) ->{
            PaymentBillItems billItem = assetService.findBillItemById(cmd.getBillItemId());
            assetService.deleteBill(billItem);
            assetProvider.deleteBillItem(cmd.getBillItemId());
            return null;
        });
    }

    @Override
    public void deletExemptionItem(ExemptionItemIdCommand cmd) {
        this.dbProvider.execute((TransactionStatus status) ->{
            PaymentExemptionItems exemItem = assetService.findExemptionItemById(cmd.getExemptionItemId());
            assetService.deleteBill(exemItem);
            assetProvider.deletExemptionItem(cmd.getExemptionItemId());
            return null;
        });

    }

    @Override
    public ShowCreateBillDTO showCreateBill(Long billGroupId) {
        return assetProvider.showCreateBill(billGroupId);
    }

    @Override
    public ListBillsDTO createBill(CreateBillCommand cmd) {
        if(!cmd.getOwnerType().equals("community")){
            throw new RuntimeException("保存账单不在一个园区");
        }
        TargetDTO targetDto = userService.findTargetByNameAndAddress(cmd.getContractNum(), cmd.getTargetName(), cmd.getOwnerId(), cmd.getNoticeTel(), cmd.getOwnerType(), cmd.getTargetType());
        if(targetDto!=null){
            cmd.setContractId(targetDto.getContractId());
            cmd.setTargetId(targetDto.getTargetId());
        }
//        List<AddressIdAndName> addressByPossibleName = addressProvider.findAddressByPossibleName(UserContext.getCurrentNamespaceId(), cmd.getOwnerId(), cmd.getBuildingName(), cmd.getApartmentName());
        return assetProvider.creatPropertyBill(cmd.getBillGroupDTO(),cmd.getDateStr(),cmd.getIsSettled(),cmd.getNoticeTel(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetName(),cmd.getTargetId(),cmd.getTargetType(),cmd.getContractNum(),cmd.getContractId());
    }

    @Override
    public void modifyBillStatus(BillIdCommand cmd) {
        assetProvider.modifyBillStatus(Long.parseLong(cmd.getBillId()));
    }

    @Override
    public ListSettledBillExemptionItemsResponse listBillExemptionItems(listBillExemtionItemsCommand cmd) {
        ListSettledBillExemptionItemsResponse response = new ListSettledBillExemptionItemsResponse();

        if (cmd.getPageAnchor() == null || cmd.getPageAnchor() < 1) {
            cmd.setPageAnchor(0l);
        }
        if(cmd.getPageSize() == null){
            cmd.setPageSize(20);
        }
        int pageOffSet = cmd.getPageAnchor().intValue();
        List<ListBillExemptionItemsDTO> list = assetProvider.listBillExemptionItems(cmd.getBillId(),pageOffSet,cmd.getPageSize(),cmd.getDateStr(),cmd.getTargetName());
        for(int i = 0; i < list.size(); i++){
            ListBillExemptionItemsDTO dto = list.get(i);
            if(dto.getAmount().compareTo(new BigDecimal("0"))==-1){
                dto.setIsPlus((byte)0);
            }else if(dto.getAmount().compareTo(new BigDecimal("0"))==1 || dto.getAmount().compareTo(new BigDecimal("0"))==0){
                dto.setIsPlus((byte)1);
            }
        }
        if(list.size() <= cmd.getPageSize()) {
//            response.setNextPageAnchor(0l);
        }else{
            response.setNextPageAnchor(((Integer)(pageOffSet+cmd.getPageSize())).longValue());
            list.remove(list.size()-1);
        }
        response.setBillDTOS(list);
        return response;
    }

    @Override
    public List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd) {
        List<BillStaticsDTO> list = new ArrayList<>();
        Byte dimension = cmd.getDimension();
        if(dimension==1){
            list = assetProvider.listBillStaticsByDateStrs(cmd.getBeginLimit(),cmd.getEndLimit(),cmd.getOwnerId(),cmd.getOwnerType());
        }else if(dimension==2){
            list = assetProvider.listBillStaticsByChargingItems(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getBeginLimit(),cmd.getEndLimit());
        }else if(dimension==3){
            list = assetProvider.listBillStaticsByCommunities(cmd.getBeginLimit(),cmd.getEndLimit(),UserContext.getCurrentNamespaceId());
        }
        return list;
    }

    @Override
    public PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd) {
        PaymentExpectanciesResponse response = new PaymentExpectanciesResponse();
        if(cmd.getPageSize()==null ||cmd.getPageSize()<1||cmd.getPageSize()>Integer.MAX_VALUE){
            cmd.setPageSize(20);
        }
        if(cmd.getPageOffset()==null||cmd.getPageOffset()<0){
            cmd.setPageSize(0);
        }
        List<PaymentExpectancyDTO> dtos = assetProvider.listBillExpectanciesOnContract(cmd.getContractNum(),cmd.getPageOffset(),cmd.getPageSize());
        if(dtos.size() <= cmd.getPageSize()){
//            response.setNextPageOffset(cmd.getPageOffset());
        }else{
            response.setNextPageOffset(cmd.getPageOffset()+cmd.getPageSize());
            dtos.remove(dtos.size()-1);
        }
        response.setList(dtos);
        return response;
    }

    @Override
    public void exportRentalExcelTemplate(HttpServletResponse response) {
        String[] propertyNames = {"dateStr","buildingName","apartmentName","targetType","targetName","contractNum","noticeTel","amountReceivable","amountReceived","amountOwed","exemption","exemptionRemark","supplement","supplementRemark"};
//        Field[] declaredFields = ListBillsDTO.class.getDeclaredFields();
//        String[] propertyNames = new String[declaredFields.length];
        String[] titleName ={"账期","楼栋","门牌","客户类型","客户名称","合同编号","催缴电话","应收(元)","已收(元)","欠收(元)","减免金额（元）","减免备注","增收金额（元）","增收备注"};
        int[] titleSize = {20,20,20,20,20,20,20,20,20,20,20,20,20,20};
//        for(int i = 0; i < declaredFields.length; i++){
//            propertyNames[i] = declaredFields[i].getName();
//        }
        List<RentalExcelTemplate> list = new ArrayList<>();
        RentalExcelTemplate data = new RentalExcelTemplate();
        data.setDateStr("2018-02");
        data.setTargetType("个人客户/企业客户");
        data.setTargetName("李四/xx公司");
        list.add(data);
        ExcelUtils excel = new ExcelUtils(response,"租金账单模板","sheet1");
        excel.writeExcel(propertyNames,titleName,titleSize,list);
    }

    @Override
    public void updateBillsToSettled(UpdateBillsToSettled cmd) {
        assetProvider.updateBillsToSettled(cmd.getContractId(),cmd.getOwnerType(),cmd.getOwnerId());
    }

    @Override
    public PreOrderDTO placeAnAssetOrder(PlaceAnAssetOrderCommand cmd) {
        return null;
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(String billId,String targetType) {
        ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
        response =  assetProvider.getBillDetailForClient(Long.parseLong(billId));
        return response;

    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus,Long ownerId, String ownerType, String targetType, Long targetId, String dateStr,String contractId) {
        ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
        Long conId = Long.parseLong(contractId);
        response =  assetProvider.getBillDetailByDateStr( billStatus,ownerId,ownerType,targetId,targetType,dateStr,conId);
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

            BigDecimal totalAmount = bill.getPeriodAccountAmount();
            // 当月的账单要把滞纳金和往期未付的账单一起计入总计应收
            if(compareMonth(bill.getAccountPeriod()) == 0) {

                List<BigDecimal> unpaidAmounts = assetProvider.listPeriodUnpaidAccountAmount(bill.getOwnerId(), bill.getOwnerType(),
                        bill.getTargetId(), bill.getTargetType(), bill.getAddressId(), bill.getTenantType(), bill.getTenantId(),bill.getAccountPeriod());

                if(unpaidAmounts != null && unpaidAmounts.size() > 0) {
                    BigDecimal pastUnpaid = BigDecimal.ZERO;
                    if(bill.getLateFee() != null) {
                        pastUnpaid = bill.getLateFee();
                    }
                    for(BigDecimal unpaid : unpaidAmounts) {
                        if(unpaid != null) {
                            pastUnpaid = pastUnpaid.add(unpaid);
                        }
                    }

                    totalAmount.add(pastUnpaid);

                }
            }

            dto.setPeriodAccountAmount(totalAmount);
            if(AssetBillStatus.PAID.equals(AssetBillStatus.fromStatus(dto.getStatus()))) {
                dto.setUnpaidPeriodAccountAmount(BigDecimal.ZERO);
            }

            if(AssetBillStatus.UNPAID.equals(AssetBillStatus.fromStatus(dto.getStatus()))) {
                dto.setUnpaidPeriodAccountAmount(totalAmount);
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
}
