package com.everhomes.asset;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contract.ContractService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public List<ListBillsDTO> listBills(String communityIdentifier,String contractNum,Integer currentNamespaceId, Long ownerId, String ownerType, String buildingName,String apartmentName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName, Byte status,String targetType,ListBillsResponse response) {
        List<ListBillsDTO> list = assetProvider.listBills(contractNum,currentNamespaceId,ownerId,ownerType, billGroupName,billGroupId,billStatus,dateStrBegin,dateStrEnd,pageOffSet,pageSize,targetName,status,targetType);
        return list;
    }

    @Override
    public List<BillDTO> listBillItems(String targetType, String billId, String targetName, int pageOffSet, Integer pageSize) {
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
