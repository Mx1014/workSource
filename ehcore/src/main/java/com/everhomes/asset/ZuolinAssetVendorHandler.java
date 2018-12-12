package com.everhomes.asset;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.group.AssetGroupProvider;
import com.everhomes.asset.util.MerchantOrderIdHelper;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contract.ContractService;
import com.everhomes.contract.ContractServiceImpl;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.asset.AssetSourceType.AssetSourceTypeEnum;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.promotion.order.NotifyBillHasBeenPaidCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.ui.user.ListUserRelatedScenesCommand;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBillItems;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.poi.hssf.util.HSSFColor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/4/11.
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX + "ZUOLIN")
public class ZuolinAssetVendorHandler extends DefaultAssetVendorHandler{
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
    private AssetService assetService;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ContractServiceImpl contractService;

    @Autowired
    private com.everhomes.paySDK.api.PayService payServiceV2;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ContractProvider contractProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private LocaleStringProvider localeStringProvider;
    
    @Autowired
    private AssetGroupProvider assetGroupProvider;

    @Autowired
    private MerchantOrderIdHelper merchantOrderIdHelper;

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
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
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
    public List<ListBillsDTO> listBills(Integer currentNamespaceId,ListBillsResponse response, ListBillsCommand cmd) {
    	LOGGER.info("ZuolinAssetVendorHandler listBills currentNamespaceId={}, cmd={}", currentNamespaceId, cmd.toString());
        //修改传递参数为一个对象，卸货
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        //卸货完毕
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillsDTO> list = assetProvider.listBills(currentNamespaceId,pageOffSet,pageSize, cmd);
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor+pageSize.longValue());
            list.remove(list.size()-1);
        }
        return list;
    }
    
    @Override
    public List<BillDTO> listBillItems(String targetType, String billId, String targetName, Integer pageNum, Integer pageSize,Long ownerId, ListBillItemsResponse response, Long billGroupId) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        List<BillDTO> list = assetProvider.listBillItems(Long.parseLong(billId),targetName,pageNum,pageSize);
        if(list.size() <= pageSize) {
            response.setNextPageAnchor(null);
        }else {
            Integer nextPageNum = pageNum + 1;
            response.setNextPageAnchor(nextPageNum.longValue());
            list.remove(list.size()-1);
        }
        return list;
    }

    @Override
    public List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes, Long billGroupId) {
        List<Long> billIds = new ArrayList<>();
        for(int i = 0; i < billIdAndTypes.size(); i++){
            BillIdAndType billIdAndType = billIdAndTypes.get(i);
            billIds.add(Long.parseLong(billIdAndType.getBillId()));
        }
        return assetProvider.listNoticeInfoByBillId(billIds);
    }
    //这个UI页面希望有合同筛选
    @Override
    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill,String contractId, Integer namespaceId) {
        ShowBillForClientDTO response = new ShowBillForClientDTO();
        checkCustomerParameter(targetType, targetId);
        //获得必要条件 客户
        if(targetType.equals("eh_user")) {
            targetId = UserContext.current().getUser().getId();
        }
        //获得筛选条件 contractId和contractNum
        String contractNum = null;
        Long cid = null;
        try{
            cid = Long.parseLong(contractId);
        }catch (Exception e){
            cid = null;
            contractNum = contractId;
        }
        List<BillDetailDTO> billDetailDTOList = assetProvider.listBillForClient(ownerId,ownerType,targetType,targetId,billGroupId,isOwedBill,cid,contractNum);
        HashSet<String> dateStrFilter = new HashSet<>();
        BigDecimal amountOwed = new BigDecimal("0");
        if(isOwedBill.byteValue() == (byte)1){
            for(int i = 0; i < billDetailDTOList.size(); i++) {
                BillDetailDTO dto = billDetailDTOList.get(i);
                if(dto.getStatus().byteValue() == (byte)2 || dto.getStatus().byteValue() == (byte)0){
                    dateStrFilter.add(dto.getDateStr());
                    amountOwed = amountOwed.add(dto.getAmountOwed());
                }
            }
        }else if(isOwedBill.byteValue() == (byte)0){
            for(int i = 0; i < billDetailDTOList.size(); i++) {
                BillDetailDTO dto = billDetailDTOList.get(i);
                if(dto.getStatus().byteValue() != (byte)1){
                    dateStrFilter.add(dto.getDateStr());
                    amountOwed = amountOwed.add(dto.getAmountOwed());
                }
            }
        }
        response.setAmountOwed(amountOwed);
        response.setBillPeriodMonths(dateStrFilter.size());
        response.setBillDetailDTOList(billDetailDTOList);
        return response;
    }

    private void checkCustomerParameter(String targetType, Long targetId) {
        if(!targetType.equals(AssetPaymentConstants.EH_USER) && !targetType.equals(AssetPaymentConstants.EH_ORGANIZATION)){
            LOGGER.error("target type is neither eh_user nor eh_organization");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"target type is neither eh_user nor eh_organization");
        }
        if(targetId == null){
            LOGGER.error("customer id cannot be null!");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"customer id cannot be null!");
        }
    }

    @Override
    public FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        FindUserInfoForPaymentResponse res = new FindUserInfoForPaymentResponse();
        List<FindUserInfoForPaymentDTO> list = new ArrayList<>();
//        String targeType = cmd.getTargetType();
//        ListCustomerContractsCommand cmd1 = new ListCustomerContractsCommand();
//        cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
//        cmd1.setCommunityId(cmd.getCommunityId());
//        if(targeType.equals(AssetPaymentConstants.EH_USER)){
//            cmd1.setTargetId(UserContext.currentUserId());
//            cmd1.setTargetType(CustomerType.INDIVIDUAL.getCode());
//            res.setCustomerName(UserContext.current().getUser().getNickName());
//        }else if(targeType.equals(AssetPaymentConstants.EH_ORGANIZATION)){
//            cmd1.setTargetId(cmd.getTargetId());
//            cmd1.setTargetType(CustomerType.ENTERPRISE.getCode());
//            OrganizationDTO organizationById = organizationService.getOrganizationById(cmd.getTargetId());
//            res.setCustomerName(organizationById.getName());
//        }else{
//            throw new RuntimeException("用户类型错误");
//        }
//        Integer namespaceId = UserContext.getCurrentNamespaceId();
//        String handler = configurationProvider.getValue(namespaceId, "contractService", "");
//        ContractService contractService = PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
//        List<ContractDTO> dtos = contractService.listCustomerContracts(cmd1);
        List<ContractDTO> dtos = listCustomerContracts(cmd.getTargetType(),cmd.getTargetId(),UserContext.getCurrentNamespaceId(),cmd.getCommunityId());
        if(dtos == null) dtos = new ArrayList<>();
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
            res.setCustomerName(contractDTO.getCustomerName());
        }else{
            String targeType = cmd.getTargetType();
            if(targeType.equals(AssetPaymentConstants.EH_USER)){
                res.setCustomerName(UserContext.current().getUser().getNickName());
            }else if(targeType.equals(AssetPaymentConstants.EH_ORGANIZATION)){
                String organizationNameById = organizationProvider.getOrganizationNameById(cmd.getTargetId());
                res.setCustomerName(organizationNameById);
            }
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
        ListBillDetailResponse response = assetProvider.listBillDetail(cmd.getBillId());
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
        //返回时增加缴费凭证留言和缴费凭证图片的信息（add by tangcen）
        ListUploadCertificatesCommand listUploadCertificatesCommand = new ListUploadCertificatesCommand();
        listUploadCertificatesCommand.setBillId(cmd.getBillId());
        UploadCertificateInfoDTO uploadCertificateInfoDTO = assetService.listUploadCertificates(listUploadCertificatesCommand);
        response.setCertificateNote(uploadCertificateInfoDTO.getCertificateNote());
        response.setUploadCertificateDTOList(uploadCertificateInfoDTO.getUploadCertificateDTOList());
        return response;
    }

    @Override
    public void deleteBill(String billId, Long billGroupId ) {
        assetProvider.deleteBill(Long.parseLong(billId));
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
    	//物业缴费V6.0（UE优化) 账单区分数据来源
    	cmd.setSourceType(AssetSourceTypeEnum.ASSET_MODULE.getSourceType());
    	cmd.setSourceId(AssetPaymentBillSourceId.CREATE.getCode());
    	LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.ASSET_CREATE_CODE, "zh_CN");
    	cmd.setSourceName(localeString.getText());
    	//物业缴费V6.0 ：手动新增的未出账单及已出未缴账单需支持修改和删除（修改和删除分为：修改和删除整体）
    	cmd.setCanDelete((byte)1);
    	cmd.setCanModify((byte)1);
        return assetProvider.creatPropertyBill(cmd, null);
    }
    
    public ListBillsDTO createBillFromImport(CreateBillCommand cmd) {
    	//物业缴费V6.0（UE优化) 账单区分数据来源
    	cmd.setSourceType(AssetSourceTypeEnum.ASSET_MODULE.getSourceType());
    	cmd.setSourceId(AssetPaymentBillSourceId.IMPORT.getCode());
    	LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.ASSET_IMPORT_CODE, "zh_CN");
    	cmd.setSourceName(localeString.getText());
    	//物业缴费V6.0 ：批量导入的未出账单及已出未缴账单需支持修改和删除（修改和删除分为：修改和删除整体）
    	cmd.setCanDelete((byte)1);
    	cmd.setCanModify((byte)1);
        return assetProvider.creatPropertyBill(cmd, null);
    }

    @Override
    public void modifyBillStatus(BillIdCommand cmd) {
        assetProvider.modifyBillStatus(Long.parseLong(cmd.getBillId()));
        //物业缴费V6.6统一账单：账单状态改变回调接口
//        ListBillDetailCommand ncmd = new ListBillDetailCommand();
//        ncmd.setBillId(Long.valueOf(cmd.getBillId()));
//        ListBillDetailResponse billDetail = listBillDetail(ncmd);
        //core-server这边直接调用统一订单的notifyBillHasBeenPaid的回调接口
        //使用billId拿到所有明细，获取每个明细的merchantOrderId并去重，然后回调
        NotifyBillHasBeenPaidCommand notifyBillHasBeenPaidCommand = new NotifyBillHasBeenPaidCommand();
        PaymentBillsCommand PBCmd = new PaymentBillsCommand();
        PBCmd.setBillId(Long.valueOf(cmd.getBillId()));
        for (String merchantOrderId: merchantOrderIdHelper.getAllMerchantOrderIdByBillId(PBCmd)){
            notifyBillHasBeenPaidCommand.setMerchantOrderId(merchantOrderId);
            orderService.notifyBillHasBeenPaid(notifyBillHasBeenPaidCommand);
        }

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
            list = assetProvider.listBillStaticsByDateStrs(cmd.getBeginLimit(),cmd.getEndLimit(),cmd.getOwnerId(),cmd.getOwnerType(), cmd.getCategoryId());
        }else if(dimension==2){
            list = assetProvider.listBillStaticsByChargingItems(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getBeginLimit(),cmd.getEndLimit(),cmd.getCategoryId());
        }else if(dimension==3){
            list = assetProvider.listBillStaticsByCommunities(cmd.getBeginLimit(),cmd.getEndLimit(),UserContext.getCurrentNamespaceId(),cmd.getCategoryId());
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
            cmd.setPageOffset(0);

        }
        //先查看任务
        Boolean inWork = assetProvider.checkContractInWork(cmd.getContractId(),cmd.getContractNum());
        if(inWork){
        	response.setGenerated((byte)0);
            return response;
            //throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.ERROR_IN_GENERATING,"Mission in processStat");
        }
        //根据合同应用的categoryId去查找对应的缴费应用的categoryId
		cmd.setCategoryId(assetProvider.getOriginIdFromMappingApp(21200l,cmd.getCategoryId(), ServiceModuleConstants.ASSET_MODULE));
        List<PaymentExpectancyDTO> dtos = assetProvider.listBillExpectanciesOnContract(cmd.getContractNum(),cmd.getPageOffset(),cmd.getPageSize(),cmd.getContractId(),cmd.getCategoryId(),cmd.getNamespaceId());
        
        Contract contract = contractProvider.findContractById(cmd.getContractId());
        for (PaymentExpectancyDTO dto : dtos) {
        	//根据合同应用的categoryId去查找对应的缴费应用的categoryId
			Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l,contract.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
			//显示客户自定义的收费项名称，需要使用缴费应用的categoryId来查
			String projectChargingItemName = assetProvider.findProjectChargingItemNameByCommunityId(contract.getCommunityId(),contract.getNamespaceId(),assetCategoryId,dto.getChargingItemId());
			dto.setChargingItemName(projectChargingItemName);
		}
        
        if(dtos.size() <= cmd.getPageSize()){
//            response.setNextPageOffset(cmd.getPageOffset());
            response.setNextPageOffset(null);
        }else{
            response.setNextPageOffset(cmd.getPageOffset()+cmd.getPageSize());
            dtos.remove(dtos.size()-1);
        }
        BigDecimal totalAmount = assetProvider.getBillExpectanciesAmountOnContract(cmd.getContractNum(),cmd.getContractId(),cmd.getCategoryId(),cmd.getNamespaceId());
        response.setList(dtos);
        response.setTotalAmount(totalAmount.toString());
        response.setGenerated((byte)1);
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
    
//    private void checkHasPaidBills(List<String> billIds) {
//        List<PaymentBills> paidBills = assetProvider.findPaidBillsByIds(billIds);
//        if( paidBills.size() >0 ) throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.HAS_PAID_BILLS,"this is bills have been paid,please refresh");
//    }


    /**
     * method implementation:
     * in this method, contracts will be found for the customer. And divided into groups by contractId and billGroupId
     * being the key , while having the bills to be values.
     *
     * @active mehtod implementation:
     * 寻找用户，这里不关心合同
     */
    @SuppressWarnings({ "unused", "unchecked" })
	@Override
    public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
    	if(cmd.getTargetType() != null && !cmd.getTargetType().equals(AssetPaymentConstants.EH_USER) && !cmd.getTargetType().equals(AssetPaymentConstants.EH_ORGANIZATION)){
            LOGGER.error("target type is neither eh_user nor eh_organization");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"target type is neither eh_user nor eh_organization");
        }
        List<ShowBillForClientV2DTO> tabBills = new ArrayList<>();
        List<PaymentBills> paymentBills = new ArrayList<PaymentBills>();
        if(cmd.getTargetType().equals(AssetPaymentStrings.EH_ORGANIZATION)){
        	checkCustomerParameter(cmd.getTargetType(), cmd.getTargetId());
        	//企业客户是所有企业管理员可以查看和支付，校验企业管理员的权限
            paymentBills = assetProvider.findSettledBillsByCustomer(cmd.getTargetType(),cmd.getTargetId(),cmd.getOwnerType(),cmd.getOwnerId());
        }else {
        	//个人客户所属家庭全员可以查看和支付（新增个人客户时会关联楼栋门牌，导入个人客户账单时，找到此门牌关联的所有个人客户，无论是任何身份，均可以查看和支付）
        	ListUserRelatedScenesCommand listUserRelatedScenesCommand = new ListUserRelatedScenesCommand();
        	List<SceneDTO> sceneDtoList = userService.listUserRelatedScenes(listUserRelatedScenesCommand);
        	//List<Long> addressIds = new ArrayList<>();
        	List<String> addressList = new ArrayList<>();
        	//获取到当前个人客户所有关联的楼栋门牌
        	for(SceneDTO sceneDTO : sceneDtoList) {
        		//修复issue-32510 个人客户场景，审核中的门牌，该门牌关联了账单，期望不可以看到账单
        		if(sceneDTO != null && sceneDTO.getStatus() != null && sceneDTO.getStatus().equals((byte)3)) {//2:审核中，3：已认证
        			FamilyDTO familyDTO = (FamilyDTO) StringHelper.fromJsonString(sceneDTO.getEntityContent(), FamilyDTO.class);
            		//addressIds.add(familyDTO.getAddressId());
        			addressList.add(familyDTO.getBuildingName() + "/" + familyDTO.getApartmentName());
        		}
        	}
        	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        	EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
            EhPaymentBillItems t2 = Tables.EH_PAYMENT_BILL_ITEMS.as("t2");
            SelectQuery<Record> query = context.selectQuery();
            query.addSelect(t.ID,t.BUILDING_NAME,t.APARTMENT_NAME,t.AMOUNT_OWED,t.AMOUNT_RECEIVED,t.AMOUNT_RECEIVABLE,t.STATUS,t.NOTICETEL,t.NOTICE_TIMES,
                    t.DATE_STR,t.TARGET_NAME,t.TARGET_ID,t.TARGET_TYPE,t.OWNER_ID,t.OWNER_TYPE,t.CONTRACT_NUM,t.CONTRACT_ID,t.BILL_GROUP_ID,
                    t.INVOICE_NUMBER,t.PAYMENT_TYPE,t.DATE_STR_BEGIN,t.DATE_STR_END,t.CUSTOMER_TEL,t.THIRD_PAID);
            query.addFrom(t, t2);
            query.addConditions(t.ID.eq(t2.BILL_ID));
            query.addConditions(t.TARGET_TYPE.eq(cmd.getTargetType()));
            query.addConditions(t.SWITCH.eq((byte)1));//账单的状态，0:未出账单;1:已出账单
            query.addConditions(t.STATUS.eq((byte)0));//账单状态，0:待缴;1:已缴
            query.addConditions(t.OWNER_ID.eq(cmd.getOwnerId()));
            query.addConditions(t.OWNER_TYPE.eq(cmd.getOwnerType()));        
            query.addConditions(t.NAMESPACE_ID.eq(cmd.getNamespaceId()));
            query.addConditions(t.AMOUNT_OWED.greaterThan(BigDecimal.ZERO));//web端新增修改都展示成未缴，除非有人手动去改状态才会改变，APP全部那边也是未缴，唯一特殊的是首页不展示待缴为0的       
            query.addConditions(DSL.concat(t2.BUILDING_NAME,DSL.val("/"), t2.APARTMENT_NAME).in(addressList));
            query.addConditions(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
            query.addConditions(t2.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
            query.addGroupBy(t.ID);
            paymentBills = query.fetchInto(PaymentBills.class);
        }
        //进行分类，冗杂代码，用空间换时间， 字符串操作+类型转换  vs  新建对象; 对象隐式指定最大寿命
        List<Map<?,?>> maps = new ArrayList<>();
        tryMakeCategory:{
            //同样contract和billGroup一样的在一起，没有contract视为同样的contract。 首先，比较contractid+billGroup先拿人得到n个tab，contract+billgroup继续
            //剩下的按照billGroup再分tab
            Map<ContractIdBillGroup, List<PaymentBills>> idMap = new HashMap<>();
            Map<ContractNumBillGroup, List<PaymentBills>> numMap = new HashMap<>();
            Map<String, List<PaymentBills>> groupMap = new HashMap<>();
            for (int i = 0; i < paymentBills.size(); i++) {
                PaymentBills bill = paymentBills.get(i);
                if (bill.getContractId() != null) {
                    ContractIdBillGroup idIden = new ContractIdBillGroup();
                    idIden.setBillGroupId(bill.getBillGroupId());
                    idIden.setContractId(bill.getContractId());
                    if (idMap.containsKey(idIden)) {
                        idMap.get(idIden).add(bill);
                    } else {
                        List<PaymentBills> idList = new ArrayList<>();
                        idList.add(bill);
                        idMap.put(idIden, idList);
                    }
                    continue;
                }
                else if (bill.getContractNum() != null) {
                    ContractNumBillGroup numIden = new ContractNumBillGroup();
                    numIden.setBillGroupId(bill.getBillGroupId());
                    numIden.setContractNum(bill.getContractNum());
                    if (numMap.containsKey(numIden)) {
                        numMap.get(numIden).add(bill);
                    } else {
                        List<PaymentBills> idList = new ArrayList<>();
                        idList.add(bill);
                        numMap.put(numIden, idList);
                    }
                    continue;
                }else{
                    if (groupMap.containsKey(bill.getBillGroupId() + "")) {
                    	//物业缴费V7.4：若用户在第三方线下支付部分账单金额，则需要同步剩余应缴金额在APP端显示，账单状态为“未支付”，且剩余金额不支持线上缴纳。
                    	if(bill.getThirdPaid() != null && bill.getThirdPaid().equals(AssetPaymentBillStatus.PAID.getCode())) {
                    		if(groupMap.containsKey(bill.getBillGroupId() + "thirdPaid")) {
                    			groupMap.get(bill.getBillGroupId() + "thirdPaid").add(bill);
                    		}else {
                    			List<PaymentBills> idList = new ArrayList<>();
                                idList.add(bill);
                                groupMap.put(bill.getBillGroupId() + "thirdPaid", idList);
                    		}
                    	}else {
                    		groupMap.get(bill.getBillGroupId() + "").add(bill);
                    	}
                    } else {
                        List<PaymentBills> idList = new ArrayList<>();
                        idList.add(bill);
                        String key = bill.getBillGroupId() + "";
                        groupMap.put(key, idList);
                    }
                }
            }
            maps.add(idMap);
            maps.add(numMap);
            maps.add(groupMap);
        }

        assemble:
        {
            for (int j = 0; j < maps.size(); j++) {
                Map<?, ?> map = maps.get(j);
                if (map.size() < 1) continue;
                for (HashMap.Entry<?, ?> entry : map.entrySet()) {
                    ShowBillForClientV2DTO dto = new ShowBillForClientV2DTO();
                    List<PaymentBills> enclosedBills = (List<PaymentBills>) entry.getValue();
                    if (enclosedBills.size() > 0){
                        Long billGroupId = enclosedBills.get(0).getBillGroupId();
                        Byte thirdPaid = enclosedBills.get(0).getThirdPaid();
                        dto.setBillGroupName(assetGroupProvider.getbillGroupNameById(billGroupId));
                        dto.setBillGroupId(billGroupId);
                        //物业缴费V7.4：若用户在第三方线下支付部分账单金额，则需要同步剩余应缴金额在APP端显示，账单状态为“未支付”，且剩余金额不支持线上缴纳。
                        dto.setThirdPaid(thirdPaid);
                        //新增收款方账户类型、账户ID字段
                        PaymentBillGroup paymentBillGroup = assetGroupProvider.getBillGroupById(billGroupId);
                        if(paymentBillGroup != null) {
                        	dto.setBizPayeeId(paymentBillGroup.getBizPayeeId());
                        	dto.setBizPayeeType(paymentBillGroup.getBizPayeeType());
                        	//由于收款方账户名称可能存在修改的情况，故重新请求电商
                            if(LOGGER.isDebugEnabled()) {
                                LOGGER.debug("showBillForClientV2(request), cmd={}", paymentBillGroup.getBizPayeeId());
                            }
                            List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(Arrays.asList(paymentBillGroup.getBizPayeeId()));
                            if(LOGGER.isDebugEnabled()) {
                                LOGGER.debug("showBillForClientV2(response), response={}", payUserDTOs);
                            }
                            if(payUserDTOs != null && payUserDTOs.size() != 0) {
                            	PayUserDTO payUserDTO = payUserDTOs.get(0);
                    			// 企业账户：0未审核 1审核通过  ; 个人帐户：0 未绑定手机 1 绑定手机
                                Integer registerStatus = payUserDTO.getRegisterStatus();
                                if(registerStatus != null && registerStatus.intValue() == 1) {
                                	dto.setRegisterStatus(PaymentUserStatus.ACTIVE.getCode());
                                } else {
                                	dto.setRegisterStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
                                }
                            }
                        }
                        if(enclosedBills.get(0).getContractId() != null){
                            dto.setContractId(String.valueOf(enclosedBills.get(0).getContractId()));
                        }
                        if(enclosedBills.get(0).getContractNum()!=null){
                            dto.setContractNum(String.valueOf(enclosedBills.get(0).getContractNum()));
                        }
                    }
                    //组装
                    List<BillForClientV2> list = new ArrayList<>();
                    Set<Long> addressIds = new HashSet<>();
                    StringBuilder addresses = new StringBuilder();
                    BigDecimal owedMoney = new BigDecimal("0");
                    for (int i = 0; i < enclosedBills.size(); i++) {
                        PaymentBills bill = enclosedBills.get(i);
                        BillForClientV2 v2 = new BillForClientV2();
                        owedMoney = owedMoney.add(enclosedBills.get(i).getAmountOwed());
                        addressIds.addAll(assetProvider.getAddressIdByBillId(bill.getId()));
                        // 从bill相关的billItem种拿楼栋门牌，为了兼容以前的数据
                        String addressByBillId = assetProvider.getAddressByBillId(bill.getId());
                        if(!StringUtils.isBlank(addressByBillId)){
                            if(i == enclosedBills.size() - 1){
                                addresses.append(addressByBillId);
                            }else{
                                addresses.append(addressByBillId + ",");
                            }
                        }
                        v2.setAmountOwed(bill.getAmountOwed().toString());
                        v2.setAmountReceivable(bill.getAmountReceivable().toString());
                        v2.setBillDuration(bill.getDateStrBegin() + "至" + bill.getDateStrEnd());
                        v2.setBillId(String.valueOf(bill.getId()));
                        //物业缴费V7.4：若用户在第三方线下支付部分账单金额，则需要同步剩余应缴金额在APP端显示，账单状态为“未支付”，且剩余金额不支持线上缴纳。
                        v2.setThirdPaid(bill.getThirdPaid());
                        list.add(v2);
                    }
                    dto.setAddressStr(assetProvider.getAddressStrByIds(addressIds.stream().collect(Collectors.toList())));
                    if (dto.getAddressStr().lastIndexOf(",") != -1) {
                        dto.setAddressStr(dto.getAddressStr().substring(0, dto.getAddressStr().length() - 1));
                    }
                    //如果没有查到，直接用bill自带的 by wentian
                    if(StringUtils.isBlank(dto.getAddressStr())){
                        dto.setAddressStr(addresses.toString());
                    }
                    // list进行排序，按照倒序 #28390 said by 王锦兰 by wentian
                    list.sort(new Comparator<BillForClientV2>() {
                        @Override
                        public int compare(BillForClientV2 o1, BillForClientV2 o2) {
                            try{
                                return -o1.getBillDuration().substring(0, o1.getBillDuration().indexOf("至")).compareTo(
                                        o2.getBillDuration().substring(0, o2.getBillDuration().indexOf("至"))
                                );
                            }catch (Exception e){
                                return 1;
                            }
                        }
                    });
                    dto.setBills(list);
                    dto.setOverAllAmountOwed(owedMoney.toString());
                    // stringbuilder append null results in 'null' str
                    // , thus hereby I use emtpy string to replace 'null' str by wentian @2018.5.11
                    if(!StringUtils.isBlank(dto.getAddressStr())){
                        dto.setAddressStr(dto.getAddressStr().replace("null","").trim());
                    }else{
                        dto.setAddressStr("");
                    }
                    tabBills.add(dto);
                }
            }
        }
        return tabBills;
    }

    @Override
    public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
        Byte status = null;
        if(cmd.getIsOnlyOwedBill().byteValue() == (byte)1){
            status= 0;
        }
        List<ListAllBillsForClientDTO> listAllBillsForClientDTOs = new ArrayList<>();
        if(cmd.getTargetType().equals(AssetPaymentStrings.EH_ORGANIZATION)){
        	//企业客户是所有企业管理员可以查看和支付，校验企业管理员的权限
        	listAllBillsForClientDTOs = assetProvider.listAllBillsForClient(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),
        			cmd.getTargetType(),cmd.getTargetId(), status, cmd.getBillGroupId());
        }else {
        	//个人客户所属家庭全员可以查看和支付（新增个人客户时会关联楼栋门牌，导入个人客户账单时，找到此门牌关联的所有个人客户，无论是任何身份，均可以查看和支付）
        	ListUserRelatedScenesCommand listUserRelatedScenesCommand = new ListUserRelatedScenesCommand();
        	List<SceneDTO> sceneDtoList = userService.listUserRelatedScenes(listUserRelatedScenesCommand);
        	//List<Long> addressIds = new ArrayList<>();
        	List<String> addressList = new ArrayList<>();
        	//获取到当前个人客户所有关联的楼栋门
        	for(SceneDTO sceneDTO : sceneDtoList) {
        		//修复issue-32510 个人客户场景，审核中的门牌，该门牌关联了账单，期望不可以看到账单
        		if(sceneDTO != null && sceneDTO.getStatus() != null && sceneDTO.getStatus().equals((byte)3)) {//2:审核中，3：已认证
        			FamilyDTO familyDTO = (FamilyDTO) StringHelper.fromJsonString(sceneDTO.getEntityContent(), FamilyDTO.class);
            		//addressIds.add(familyDTO.getAddressId());
        			addressList.add(familyDTO.getBuildingName() + "/" + familyDTO.getApartmentName());
        		}
        	}
        	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        	ArrayList<Long> groupIds = new ArrayList<>();
        	EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
            EhPaymentBillItems t2 = Tables.EH_PAYMENT_BILL_ITEMS.as("t2");
            SelectQuery<Record> query = context.selectQuery();
            query.addSelect(t.ID,t.BUILDING_NAME,t.APARTMENT_NAME,t.AMOUNT_OWED,t.AMOUNT_RECEIVED,t.AMOUNT_RECEIVABLE,t.STATUS,t.NOTICETEL,t.NOTICE_TIMES,
                    t.DATE_STR,t.TARGET_NAME,t.TARGET_ID,t.TARGET_TYPE,t.OWNER_ID,t.OWNER_TYPE,t.CONTRACT_NUM,t.CONTRACT_ID,t.BILL_GROUP_ID,
                    t.INVOICE_NUMBER,t.PAYMENT_TYPE,t.DATE_STR_BEGIN,t.DATE_STR_END,t.CUSTOMER_TEL,
            		DSL.groupConcatDistinct(DSL.concat(t2.BUILDING_NAME,DSL.val("/"), t2.APARTMENT_NAME)).as("addresses"));
            query.addFrom(t, t2);
            query.addConditions(t.ID.eq(t2.BILL_ID));
            query.addConditions(t.TARGET_TYPE.eq(cmd.getTargetType()));
            query.addConditions(t.SWITCH.eq((byte)1));//账单的状态，0:未出账单;1:已出账单
            if(status != null){
                query.addConditions(t.STATUS.eq(status));
            }
            if(cmd.getBillGroupId() != null){
                query.addConditions(t.BILL_GROUP_ID.eq(cmd.getBillGroupId()));
            }
            query.addConditions(t.OWNER_ID.eq(cmd.getOwnerId()));
            query.addConditions(t.OWNER_TYPE.eq(cmd.getOwnerType()));        
            query.addConditions(t.NAMESPACE_ID.eq(cmd.getNamespaceId()));
            query.addConditions(DSL.concat(t2.BUILDING_NAME,DSL.val("/"), t2.APARTMENT_NAME).in(addressList));
            query.addConditions(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
            query.addConditions(t2.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
            query.addGroupBy(t.ID);
            query.addOrderBy(t.DATE_STR.desc());
            List<ListAllBillsForClientDTO> list = new ArrayList<>();
            query.fetch().map(r -> {
                ListAllBillsForClientDTO dto = new ListAllBillsForClientDTO();
                groupIds.add(r.getValue(t.BILL_GROUP_ID));
                dto.setAmountOwed(r.getValue(t.AMOUNT_OWED).toString());
                dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE).toString());
                dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
                dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                dto.setChargeStatus(r.getValue(t.STATUS));
                dto.setBillId(String.valueOf(r.getValue(t.ID)));
                list.add(dto);
                return null;
            });
		    Map<Long,String> groupNames = assetProvider.getGroupNames(groupIds);
		    for(int i = 0 ; i < list.size(); i ++){
		    	list.get(i).setBillGroupName(groupNames.get(groupIds.get(i)));
		    }
		    listAllBillsForClientDTOs = list;
        }
        return listAllBillsForClientDTOs;
    }

    @Override
    public void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response) {
		ShowCreateBillDTO webPage = assetProvider.showCreateBill(cmd.getBillGroupId());
        List<String> headList = new ArrayList<>();
        List<Integer> mandatoryIndex = new ArrayList<>();
        Integer cur = -1;
        headList.add("账单开始时间");
        cur++;
        mandatoryIndex.add(1);//账期开始时间置为必填
        headList.add("账单结束时间");
        cur++;
        mandatoryIndex.add(1);//账期结束时间置为必填
        headList.add("客户名称");
        cur++;
        if(cmd.getTargetType() != null && cmd.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
        	mandatoryIndex.add(1);//企业客户：客户名称为必填
        }else {
        	mandatoryIndex.add(0);//个人客户：客户名称为非必填
        }
        headList.add("合同编号");
        cur++;
        mandatoryIndex.add(0);
        headList.add("客户手机号");
        cur++;
        mandatoryIndex.add(0);
        headList.add("催缴手机号");
        cur++;
        mandatoryIndex.add(0);
        //可变标题 , 需要后期excel加字段？ not likely    在数据库中能获得这些字段的展示名称吗？ not likely   写死中文？ 导出功能较多时，考虑建立导出设置表统一管理
        List<BillItemDTO> billItemDTOList = webPage.getBillItemDTOList();
        for(BillItemDTO dto : billItemDTOList){
            headList.add(dto.getBillItemName()+"(元)");
            cur++;
            mandatoryIndex.add(1);//收费项为必填
            //修复issue-34181 执行一些sql页面没有“用量”，但是导入的模板和导出Excel都有“用量”字段
            if(assetService.isShowEnergy(cmd.getNamespaceId(), cmd.getCommunityId(), ServiceModuleConstants.ASSET_MODULE)) {//判断该域空间下是否显示用量
            	if(dto.getBillItemId() != null) {
                	if(dto.getBillItemId().equals(AssetEnergyType.personWaterItem.getCode()) 
                			|| dto.getBillItemId().equals(AssetEnergyType.publicWaterItem.getCode())) {
                		//eh_payment_charging_items 4:自用水费  7：公摊水费
                		headList.add("用量（吨）");
                        cur++;
                        mandatoryIndex.add(0);//用量非必填
                	}else if (dto.getBillItemId().equals(AssetEnergyType.personElectricItem.getCode()) 
                			|| dto.getBillItemId().equals(AssetEnergyType.publicElectricItem.getCode())) {
                		//eh_payment_charging_items 5:自用电费   8：公摊电费
                		headList.add("用量（度）");
                        cur++;
                        mandatoryIndex.add(0);//用量非必填
    				}
                }
            }
        }
        headList.add("楼栋/门牌");
        cur++;
        if(cmd.getTargetType() != null && cmd.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
        	mandatoryIndex.add(0);//企业客户：楼栋/门牌为非必填
        }else {
        	mandatoryIndex.add(1);//个人客户：楼栋/门牌为必填
        }
        headList.add("减免金额(元)");
        cur++;
        mandatoryIndex.add(0);
        headList.add("减免备注");
        cur++;
        mandatoryIndex.add(0);
        headList.add("增收金额(元)");
        cur++;
        mandatoryIndex.add(0);
        headList.add("增收备注");
        cur++;
        mandatoryIndex.add(0);
        //发票
        headList.add("发票单号");
        cur++;
        mandatoryIndex.add(0);
        String[] headers = headList.toArray(new String[headList.size()]);
        String fileName = webPage.getBillGroupName();
        if(fileName == null) fileName = "";
        if(cmd.getTargetType() != null && cmd.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
        	fileName = "企业客户账单导入模板" + fileName;
        }else {
        	fileName = "个人客户账单导入模板" + fileName;
        }
        new ExcelUtils(response,fileName+System.currentTimeMillis(),fileName+"模板")
                .setNeedMandatoryTitle(true)
                .setMandatoryTitle(mandatoryIndex)
                .setNeedTitleRemark(true)
                .setTitleRemarkColorIndex(HSSFColor.YELLOW.index)
                .setHeaderColorIndex(HSSFColor.YELLOW.index)
                .setTitleRemark("填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）\n" +
                        "1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照要求输入。\n" +
                        "2、请在表格里面逐行录入数据，建议一次最多导入400条信息。\n" +
                        "3、请不要随意复制单元格，这样会破坏字段规则校验。\n" +
                        "4、带有星号（*）的红色字段为必填项。\n" +
                        "5、收费项以导出的为准，不可修改，修改后将导致导入不成功。\n" +
                        "6、企业客户需填写与系统内客户管理一致的企业名称，个人客户需填写与系统内个人客户资料一致的楼栋门牌，否则会导致无法定位客户。\n" +
                        "7、账单开始时间，账单结束时间的格式只能为 2018-01-12,2018/01/12。\n" +
                        "8、导入账单时若组内有自用水电费、公摊水电费，且初始化时配置需要显示用量，则需录入用量字段，该字段非必填，不填则不显示。\n" +
                        "9、各费项所填的金额都为含税金额", (short)13, (short)3000)
                .setNeedSequenceColumn(false)
                .setIsCellStylePureString(true)
                .writeExcel(null, headers, true, null, null);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    BatchImportBillsResponse batchImportBills(BatchImportBillsCommand cmd, MultipartFile file) {
        BatchImportBillsResponse response = new BatchImportBillsResponse();
        ArrayList resultList = null;
        try{
            resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());
            if(null == resultList || resultList.isEmpty()){
                LOGGER.error("bill import: File content is empty, userId");
                //不恰当的使用了组织架构的scope，潜在可能造成拆分障碍 by wentian
                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
                        "File content is empty");
            }else {
            	//修复ISSUE-32519 : 已经填写了内容，右键删除后，导入总是提示有一行失败，且失败原因“账单开始时间格式错误,请参考说明进行填写”
            	Iterator iterator = resultList.iterator();
            	while(iterator.hasNext()){
            		RowResult currentRow = (RowResult) iterator.next();
                    if(currentRow != null && currentRow.toString().equals("RowResult: [{}]")) {
                    	iterator.remove();
                    }
                }
            }
        }catch (IOException exception){
            LOGGER.error("file resolve failed in batchImportBills", exception);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE
                    , "file illegal");
        }
        // get categoryid
        Long categoryId =assetProvider.findCategoryIdFromBillGroup(cmd.getBillGroupId());

        //准备任务
        ImportFileTask task = new ImportFileTask();
        task.setOwnerType(EntityType.COMMUNITY.getCode());
        task.setOwnerId(cmd.getCommunityId());
        task.setType(ImportFileTaskType.BUILDING.getCode());
        task.setCreatorUid(UserContext.currentUserId());
        ArrayList finalResultList = resultList;
        task = importFileService.executeTask(() -> {
            ImportFileResponse importTaskResponse = new ImportFileResponse();
            Map<List<CreateBillCommand>, List<ImportFileResultLog<List<String>>>> map = handleImportBillData(finalResultList, cmd.getBillGroupId(), cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getBillSwitch(), cmd.getTargetType());
            List<CreateBillCommand> createBillCommands = new ArrayList<>();
            List<ImportFileResultLog<List<String>>> datas = new ArrayList<>();
            for(Map.Entry<List<CreateBillCommand>, List<ImportFileResultLog<List<String>>>> entry : map.entrySet()){
                createBillCommands = entry.getKey();
                datas = entry.getValue();
            }

            // 原来的实现方式是对于每条excel数据，都进行帐单和费项的比较来判断帐单是否需要覆盖，这使得查询、比较、删除、插入次数过多，处理一条数据需要15秒以上，
            // 使得数据很难导进去，故先对已存在的数据进行处理成一个标识（字符串），方便后面比较 by lqs 20181207
            Map<String, String> existBills = prepareUserBillIdentifiers(createBillCommands);

            long updateStartTime = System.currentTimeMillis();
            long roundStartTime = updateStartTime;
            LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.ASSET_IMPORT_CODE, "zh_CN");
            for(int i = 0; i < createBillCommands.size(); i++){
                CreateBillCommand command = createBillCommands.get(i);
                command.setCategoryId(categoryId);
                command.setSourceName(localeString.getText());
                List<Boolean> isCreate = new ArrayList<Boolean>();
            	isCreate.add(true);
            	if(cmd.getTargetType() != null && cmd.getTargetType().equals(AssetTargetType.USER.getCode())){
            		//个人客户时，以账单时间、账单组、楼栋门牌3条信息定位账单的唯一性。若再次导入同一账单，均认为覆盖原账单，而不是新增账单。除定位账单的字段外其余字段均覆盖。
            		modifyBillForUser(command,isCreate, existBills);
            	}else {
            		//企业客户时，以账单时间、账单组、企业名称3条信息定位账单的唯一性。若再次导入同一账单，均认为覆盖原账单，而不是新增账单。除定位账单的字段外其余字段均覆盖。
            		modifyBillForOrg(command,isCreate);
            	}
            	if(isCreate.get(0)) {
            		createBillFromImport(command);//物业缴费V6.0（UE优化) 账单区分数据来源
            	}

            	// 由于插入/更新数据非常慢，打印一下前两条的时间
            	if(((i % 500) == 0 || i == 1) && LOGGER.isInfoEnabled()) {
                    long roundEndTime = System.currentTimeMillis();
                    LOGGER.info("Process bill importing data, create or update bills roundly, index={}, dataSize={}, elapse={}", i, createBillCommands.size(), (roundEndTime - roundStartTime));
                    roundStartTime = roundEndTime;
            	}
            }
            if(LOGGER.isInfoEnabled()) {
                long updateEndTime = System.currentTimeMillis();
                int cmdSize = (createBillCommands == null) ? 0 : createBillCommands.size();
                int dataSize = (datas == null) ? 0 : datas.size();
                LOGGER.info("Process bill importing data, create or update bills end, cmdSize={}, dataSize={}, elapse={}", cmdSize, dataSize, (updateEndTime - updateStartTime));
            }
            //设置导出报错的结果excel的标
            importTaskResponse.setTitle(datas.get(0).getData());
            datas.remove(0);
            importTaskResponse.setTotalCount((long)(finalResultList.size() - 2));
            importTaskResponse.setFailCount((long)datas.size());
            importTaskResponse.setLogs(datas);
            return importTaskResponse;
        }, task);
        response.setId(task.getId());
        return response;
    }
    
    private void modifyBillForUser(CreateBillCommand command, List<Boolean> isCreate, Map<String, String> existBills){
    	//物业缴费V6.0（UE优化) 账单区分数据来源
    	command.setSourceType(AssetSourceTypeEnum.ASSET_MODULE.getSourceType());
    	command.setSourceId(AssetPaymentBillSourceId.IMPORT.getCode());
    	//物业缴费V6.0 ：批量导入的未出账单及已出未缴账单需支持修改和删除（修改和删除分为：修改和删除整体）
    	command.setCanDelete((byte)1);
    	command.setCanModify((byte)1);

        // 使用该方式进行帐单和费项的比较来判断帐单是否需要覆盖，这使得查询、比较、删除、插入次数过多，处理一条数据需要15秒以上，
        // 耗时过长，使得帐单无法导入，故修改比较方式 by lqs 20181207
//    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
//        EhPaymentBillItems t2 = Tables.EH_PAYMENT_BILL_ITEMS.as("t2");
//    	List<Long> addressIds = new ArrayList<>();
//        for(BillItemDTO billItemDTO : command.getBillGroupDTO().getBillItemDTOList()) {
//        	if(!addressIds.contains(billItemDTO.getAddressId())) {
//        		addressIds.add(billItemDTO.getAddressId());//一个账单可能有多个楼栋门牌
//        	}
//        }
//        context.selectDistinct(t.ID)
//            .from(t)
//            .leftOuterJoin(t2)
//            .on(t.ID.eq(t2.BILL_ID))
//            .where(t.DATE_STR_BEGIN.eq(command.getDateStrBegin()))
//            .and(t.DATE_STR_END.eq(command.getDateStrEnd()))
//            .and(t.BILL_GROUP_ID.eq(command.getBillGroupDTO().getBillGroupId()))//已在导入做非空校验
//            .and(t.STATUS.eq(new Byte("0")))//账单状态，0:待缴;1:已缴（如果已缴，不允许覆盖，所以是新增账单）
//            .and(t.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))//不可以跨域空间
//            .and(t.OWNER_ID.eq(command.getOwnerId()))//不可以跨园区/项目
//            .and(t.SWITCH.eq(new Byte(command.getIsSettled())))//账单的状态，0:未出账单;1:已出账单
//            .and(t.TARGET_TYPE.eq(AssetTargetType.USER.getCode()))//个人客户情况下
//            .fetch()
//        	.forEach(r ->{
//            //根据账单时间、账单组查询出来的账单id再次查询找到该账单下所有费项的楼栋门牌（可能多个）
//            List<Long> queryAddressIds = new ArrayList<>();
//        	context.selectDistinct(t2.ADDRESS_ID)
//        		.from(t2)
//        		.where(t2.BILL_ID.eq(r.getValue(t.ID)))
//        		.fetch()
//            	.forEach(r2 ->{
//            		queryAddressIds.add(r2.getValue(t2.ADDRESS_ID));//一个账单可能有多个楼栋门牌
//            });
//        	if(addressIds.containsAll(queryAddressIds) && queryAddressIds.containsAll(addressIds)) {
//        		//个人客户时，以账单时间、账单组、楼栋门牌3条信息定位账单的唯一性。若再次导入同一账单，均认为覆盖原账单，而不是新增账单。除定位账单的字段外其余字段均覆盖。
//        		Long billId = r.getValue(t.ID);
//        		assetProvider.modifyBillForImport(billId, command);
//        		isCreate.set(0, false);
//        	}
//        });
    	String identifier = genUserBillIdentifier(command);
    	String billIdStr = existBills.get(identifier);
    	if(billIdStr != null) {
    	    assetProvider.modifyBillForImport(Long.parseLong(billIdStr), command);
    	    isCreate.set(0, false);
    	}
    }

    private String genUserBillIdentifier(CreateBillCommand command) {
        String identifier = null;

        Long billGroupId = command.getBillGroupDTO().getBillGroupId();
        String dateStrBegin = command.getDateStrBegin();
        String dateStrEnd = command.getDateStrEnd();
        identifier = concateUserBillIdentifier(billGroupId, dateStrBegin, dateStrEnd);

        List<Long> addressIds = new ArrayList<>();
        for (BillItemDTO billItemDTO : command.getBillGroupDTO().getBillItemDTOList()) {
            if (billItemDTO.getAddressId() != null && !addressIds.contains(billItemDTO.getAddressId())) {
                addressIds.add(billItemDTO.getAddressId());// 一个账单可能有多个楼栋门牌
            }
        }
        Collections.sort(addressIds);

        for(Long addressId : addressIds) {
            identifier = identifier + "_" + String.valueOf(addressId);
        }

        return identifier;
    }

    /**
     * <p>个人客户的帐单以账单时间、账单组、楼栋门牌3条信息定位账单的唯一性（这三个信息构成唯一标识)，导入的时候需要比较是否已经存在相同标识的帐单，
     * 如果相同标识的帐单已经存在，则需要先删除再重新建。而这个比较过程，原来是把eh_payment_bills表与eh_payment_bill_items进行联查，其中帐单时间
     * 和帐单组在eh_payment_bills表，而门牌信息却在eh_payment_bill_items表里，联查出来的数据数目与items的数量一样，而更新是按这个数量来进行更新，
     * 实际只需要按帐单更新即可，这是费时的点一；比较的时候，每次查完，再跟excel数据里的门牌进行比较，使得比较次数也很多，比较很慢，这是费时点二；</p>
     * <p>为了加快速度，先把excel数据里出现的时间段，把数据库已有的帐单分批遍历出来（分批是为了减少内存的占用），然后组装出帐单组和帐单时间的标识；
     * 再把用到的帐单分批把其关联的费项明细遍历出来，把对应的门牌信息组装到标识里，形成完整的帐单标识；比较的时候，把excel里的数据也按同样的规则组装
     * 出标识；</p>
     * <p>标识在组装的过程中是使用一个map保存，key为帐单ID，value为标识；为了在比较的时候可以快速找到标识，把该map反转一下，也就是key是标识，value
     * 是帐单ID（注意：如果数据中有两个帐单标识一样，则会被覆盖掉），这样在比较时可以用map的查找速度来得到，而不是每次查找都遍历所有标识才能找到；
     * 两个标识比较如果相同则认为帐单已存在，先删除再插入。</p>
     * <p>帐单标识格式：<b>帐单组_账单开始时间_帐单结束时间</b></p>
     * @param billDatas excel表中的数据列表
     * @return 帐单标识map
     */
    private Map<String, String> prepareUserBillIdentifiers(List<CreateBillCommand> billDatas) {
        long startTime = System.currentTimeMillis();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        // 最小的开始日期、最大的结束日期，它们共同构成限制数据查询的范围，减少数据量加载和比较
        String minBeginDateStr = null;
        String maxEndDateStr = null;

        long calDateStartTime = System.currentTimeMillis();
        Long communityId = null; // 假定billDatas时里的所有数据都是同一个小区的，只提取其中一个
        for(int i = 0; i < billDatas.size(); i++){
            CreateBillCommand command = billDatas.get(i);
            if(communityId == null && command.getOwnerId() != null) {
                communityId = command.getOwnerId();
            }

            // 取开始日期最小值
            String dateStrBegin = command.getDateStrBegin();
            if((minBeginDateStr == null) || (dateStrBegin != null && dateStrBegin.compareTo(minBeginDateStr) < 0)) {
                minBeginDateStr = dateStrBegin;
            }

            // 取结束日期最大值
            String dateStrEnd = command.getDateStrEnd();
            if((maxEndDateStr == null) || (dateStrEnd  != null && dateStrEnd.compareTo(maxEndDateStr) > 0)) {
                maxEndDateStr =  dateStrEnd;
            }
        }
        long calDateEndTime = System.currentTimeMillis();

        long prepareBillStartTime = System.currentTimeMillis();
        Map<Long, String> map = prepareUserBillIdentifierFromBill(namespaceId, communityId, minBeginDateStr, maxEndDateStr);
        populateUserBillIdentifierWithAddress(map);
        long prepareBillEndTime = System.currentTimeMillis();

        long mapRevertStartTime = System.currentTimeMillis();
        Map<String, String> billIdentifiers = new HashMap<String, String>();
        Iterator<Entry<Long, String>> iterator = map.entrySet().iterator();
        Entry<Long, String> entry = null;
        while(iterator.hasNext()) {
            entry = iterator.next();
            if(entry.getValue() != null) {
                billIdentifiers.put(entry.getValue(), String.valueOf(entry.getKey()));
            }
        }
        long mapRevertEndTime = System.currentTimeMillis();

        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Process bill importing data, prepare user bill identifiers, namespaceId={}, communityId={}, minBeginDateStr={}, maxEndDateStr={}, "
                    + "totalCount={}, elapse={}, calDateElapse={}, prepareBillElapse={}, mapRevertElapse={}",
                    namespaceId, communityId, minBeginDateStr, maxEndDateStr, billDatas.size(), (endTime - startTime),
                    (calDateEndTime - calDateStartTime), (prepareBillEndTime - prepareBillStartTime), (mapRevertEndTime - mapRevertStartTime));
        }

        return billIdentifiers;
    }

    private Map<Long, String> prepareUserBillIdentifierFromBill(Integer namespaceId, Long communityId, String dateStrBegin, String dateStrEnd) {
        long startTime = System.currentTimeMillis();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        Condition conditions = Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId);
        conditions = conditions.and(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(communityId));
        conditions = conditions.and(Tables.EH_PAYMENT_BILLS.STATUS.eq(new Byte("0"))); // 账单状态，0:待缴;1:已缴（如果已缴，不允许覆盖，所以是新增账单）
        conditions = conditions.and(Tables.EH_PAYMENT_BILLS.TARGET_TYPE.eq(AssetTargetType.USER.getCode())); // 个人客户
        // 加上时间段限制，减少数据量
        if(StringHelper.hasContent(dateStrBegin)) {
            conditions = conditions.and(Tables.EH_PAYMENT_BILLS.DATE_STR_BEGIN.greaterOrEqual(dateStrBegin));
        }
        if(StringHelper.hasContent(dateStrEnd)) {
            conditions = conditions.and(Tables.EH_PAYMENT_BILLS.DATE_STR_END.lessOrEqual(dateStrEnd));
        }

        // 个人客户时，以账单时间、账单组、楼栋门牌3条信息定位账单的唯一性，先从帐单里提取出账单组、账单开始时间、帐单结束时间作为帐单标识
        // map用于存储billId与帐单标识的映射关系，其中帐单标识格式：帐单组_账单开始时间_帐单结束时间
        // 由于后面还需要拼地址数据，所以标识保留StringBuilder结果
        Map<Long, String> map = new HashMap<Long, String>();
        Integer totalCount = context.selectCount().from(Tables.EH_PAYMENT_BILLS).where(conditions).fetchOneInto(Integer.class);
        if(totalCount == null) {
            totalCount = 0;
        }
        int pageSize = 1000;
        int pageCount = totalCount / pageSize;
        if((totalCount % pageSize) > 0) {
            pageCount++;
        }
        int offset = 0;
        for(int i = 0; i < pageCount; i++) {
            offset = i * pageSize;
            context.select().from(Tables.EH_PAYMENT_BILLS).where(conditions).limit(pageSize).offset(offset).fetch()
            .forEach(r ->{
                PaymentBills bill = ConvertHelper.convert(r, PaymentBills.class);
                map.put(bill.getId(), concateUserBillIdentifier(bill.getBillGroupId(), bill.getDateStrBegin(), bill.getDateStrEnd()));
            });
        }

        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Process bill importing data, prepare user bill identifiers from eh_payment_bills, namespaceId={}, communityId={}, dateStrBegin={}, dateStrEnd={}, totalCount={}, elapse={}",
                    namespaceId, communityId, dateStrBegin, dateStrEnd, totalCount, (endTime - startTime));
        }

        return map;
    }

    private void populateUserBillIdentifierWithAddress(Map<Long, String> map) {
        long startTime = System.currentTimeMillis();
        // 分批取收费明细数据避免一下子加载大量数据而引起内存占用过多问题
        int batchCount = 1000;

        Iterator<Entry<Long, String>> iterator = map.entrySet().iterator();
        Entry<Long, String> entry = null;
        List<Long> billIdList = new ArrayList<Long>(batchCount);
        int index = 0;
        while(iterator.hasNext()) {
            index++;
            entry = iterator.next();
            billIdList.add(entry.getKey());

            if((index % batchCount) == 0) {
                populateUserBillIdentifierWithAddress(map, billIdList);
                billIdList.clear();
            }
        }

        if(billIdList.size() > 0) {
            populateUserBillIdentifierWithAddress(map, billIdList);
            billIdList.clear();
        }

        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Process bill importing data, prepare user bill identifiers with bill item, billSize={}, itemCount={}, elapse={}",
                    map.size(), index, (endTime - startTime));
        }
    }

    private void populateUserBillIdentifierWithAddress(Map<Long, String> map, List<Long> billIdList) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        context.select().from(Tables.EH_PAYMENT_BILL_ITEMS).where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.in(billIdList))
        .orderBy(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID, Tables.EH_PAYMENT_BILL_ITEMS.ADDRESS_ID)
        .fetch()
        .forEach(r ->{
            PaymentBillItems item = ConvertHelper.convert(r, PaymentBillItems.class);
            String identifier = map.get(item.getBillId());
            if(identifier != null) {
                String addressIdStr = (item.getAddressId() == null) ? "" : String.valueOf(item.getAddressId());
                identifier = identifier + "_" + addressIdStr;
                map.put(item.getBillId(), identifier);
            }
        });
    }

    private String concateUserBillIdentifier(Long billGroupId, String dateStrBegin, String dateStrEnd) {
        StringBuilder strBuilder = new StringBuilder();

        if(billGroupId == null) {
            strBuilder.append("unknown");
        } else {
            strBuilder.append(billGroupId);
        }

        strBuilder.append("_");
        if(dateStrBegin == null) {
            strBuilder.append("unknown");
        } else {
            strBuilder.append(dateStrBegin);
        }

        strBuilder.append("_");
        if(dateStrEnd == null) {
            strBuilder.append("unknown");
        } else {
            strBuilder.append(dateStrEnd);
        }

        return strBuilder.toString();
    }
    
    private void modifyBillForOrg(CreateBillCommand command, List<Boolean> isCreate){
    	//物业缴费V6.0（UE优化) 账单区分数据来源
    	command.setSourceType(AssetSourceTypeEnum.ASSET_MODULE.getSourceType());
    	command.setSourceId(AssetPaymentBillSourceId.IMPORT.getCode());
    	//物业缴费V6.0 ：批量导入的未出账单及已出未缴账单需支持修改和删除（修改和删除分为：修改和删除整体）
    	command.setCanDelete((byte)1);
    	command.setCanModify((byte)1);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentBillItems t2 = Tables.EH_PAYMENT_BILL_ITEMS.as("t2");
        //企业客户时，以账单时间、账单组、企业名称3条信息定位账单的唯一性。若再次导入同一账单，均认为覆盖原账单，而不是新增账单。除定位账单的字段外其余字段均覆盖。
        context.selectDistinct(t.ID)
	        .from(t)
	        .leftOuterJoin(t2)
	        .on(t.ID.eq(t2.BILL_ID))
	        .where(t.DATE_STR_BEGIN.eq(command.getDateStrBegin())) 
	        .and(t.DATE_STR_END.eq(command.getDateStrEnd()))
	        .and(t.BILL_GROUP_ID.eq(command.getBillGroupDTO().getBillGroupId()))//已在导入做非空校验
	        .and(t.STATUS.eq(new Byte("0")))//账单状态，0:待缴;1:已缴（如果已缴，不允许覆盖，所以是新增账单）
	        .and(t.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))//不可以跨域空间
	        .and(t.OWNER_ID.eq(command.getOwnerId()))//不可以跨园区/项目
	        .and(t.SWITCH.eq(new Byte(command.getIsSettled())))//账单的状态，0:未出账单;1:已出账单
	        .and(t.TARGET_TYPE.eq(AssetTargetType.ORGANIZATION.getCode()))//企业客户情况下
	        //.and(t.TARGET_ID.eq(command.getTargetId()))
	        .and(t.TARGET_NAME.eq(command.getTargetName()))//修复缺陷 #32336
	        .fetch()
	    	.forEach(r ->{
	    		Long billId = r.getValue(t.ID);
        		assetProvider.modifyBillForImport(billId, command);
        		isCreate.set(0, false);
    	});
    }

    @SuppressWarnings("rawtypes")
	private Map<List<CreateBillCommand>, List<ImportFileResultLog<List<String>>>> handleImportBillData(ArrayList resultList, Long billGroupId, Integer namespaceId, Long ownerId, Byte billSwitch, String targetType) {
        long startTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Process bill importing data(start), namespaceId={}, billGroupId={}, targetType={}, size={}", namespaceId, billGroupId, targetType, resultList.size());
        }
        Map<List<CreateBillCommand>, List<ImportFileResultLog<List<String>>>> map = new HashMap<>();
        List<ImportFileResultLog<List<String>>> datas = new ArrayList<>();
        List<CreateBillCommand> cmds = new ArrayList<>();
        //假设了第一行为标题
        RowResult headerRow = (RowResult) resultList.get(1);
        String[] headers = getOrderedCellValues(headerRow, null);
        //datas的第一行
        ImportFileResultLog<List<String>> headLog = new ImportFileResultLog<>(AssetBillImportErrorCodes.SCOPE);
        headLog.setData(Arrays.asList(headers));
        datas.add(headLog);
        int itemStartIndex = 0;//费项开始列下标（费项的个数是动态的，不是固定的）
        int itemEndIndex = 0;//费项结束列下标（费项的个数是动态的，不是固定的）
        int addressIndex = 0;//楼栋/门牌列下标
        int dateStrBeginIndex = 0;//账单开始时间列下标
        int dateStrEndIndex = 0;//账单结束时间列下标
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].contains("催缴手机号")) itemStartIndex = i + 1;
            else if (headers[i].contains("楼栋/门牌")) {
            	itemEndIndex = i - 1;
                addressIndex = i;
            }
            else if(headers[i].contains("账单开始时间")) dateStrBeginIndex = i;
            else if(headers[i].contains("账单结束时间")) dateStrEndIndex = i;
        }

        long roundStartTime = System.currentTimeMillis();
        long fieldRoundElapse = 0L;
        bill:for (int i = 2; i < resultList.size(); i++) {
            RowResult currentRow = (RowResult) resultList.get(i);
            String[] data = getOrderedCellValues(currentRow, headers.length);
            //放置log
            ImportFileResultLog<List<String>> log = new ImportFileResultLog<>(AssetBillImportErrorCodes.SCOPE);
            log.setData(Arrays.asList(data));

            CreateBillCommand cmd = new CreateBillCommand();
            cmd.setTargetType(targetType);//客户属性
            BillGroupDTO billGroupDTO = new BillGroupDTO(cmd);
            List<BillItemDTO> billItemDTOList = new ArrayList<BillItemDTO>();
            List<ExemptionItemDTO> exemptionItemDTOList = new ArrayList<>();
            ExemptionItemDTO exemptionItemDTO = null;
            ExemptionItemDTO increaseItemDTO = null;
            
            //账单开始时间和账单结束时间被依赖
            String dateStrBegin = DateUtils.guessDateTimeFormatAndFormatIt(data[dateStrBeginIndex], "yyyy-MM-dd");
            if(StringUtils.isBlank(dateStrBegin)){
                log.setErrorLog("账单开始时间格式错误,请参考说明进行填写");
                log.setCode(AssetBillImportErrorCodes.DATE_STR_BEGIN_EMPTY_ERROR);
                datas.add(log);
                continue bill;
            }
            cmd.setDateStrBegin(dateStrBegin);
            
            String dateStrEnd = DateUtils.guessDateTimeFormatAndFormatIt(data[dateStrEndIndex], "yyyy-MM-dd");
            if(StringUtils.isBlank(dateStrEnd)){
                log.setErrorLog("账单结束时间格式错误,请参考说明进行填写");
                log.setCode(AssetBillImportErrorCodes.DATE_STR_END_EMPTY_ERROR);
                datas.add(log);
                continue bill;
            }
            cmd.setDateStrEnd(dateStrEnd);
            
            //楼栋门牌被依赖
            String address = data[addressIndex];
            cmd.setAddresses(address);
        	if(targetType.equals(AssetTargetType.USER.getCode())){
        		if(StringUtils.isBlank(address)){
                    log.setErrorLog("个人客户情况下，楼栋门牌必填");
                    log.setCode(AssetBillImportErrorCodes.ADDRESS_EMPTY_ERROR);
                    datas.add(log);
                    continue bill;
                }
        	}
        	String buildingName = null,apartmentName = null;
        	Long addressId = null;
        	if(address != null && address.trim() != "") {
        		if(address.indexOf("/") != -1) {
            		buildingName = address.split("/")[0];
            		apartmentName = address.split("/")[1];
            		Address addressByBuildingApartmentName = addressProvider.findAddressByBuildingApartmentName(namespaceId, ownerId, buildingName, apartmentName);
            		if(addressByBuildingApartmentName != null) {
            			addressId = addressByBuildingApartmentName.getId();
            		} else {
            		    log.setErrorLog("楼栋/门牌找不到");
                        log.setCode(AssetBillImportErrorCodes.ADDRESS_INCORRECT);
                        datas.add(log);
                        continue bill;
            		}
            	}else {
            		log.setErrorLog("楼栋/门牌格式错误，楼栋门牌要以/分开");
            		log.setCode(AssetBillImportErrorCodes.ADDRESS_INCORRECT);
                    datas.add(log);
                    continue bill;
            	}
        	}
            
        	long fieldStartTime = System.currentTimeMillis();
            for(int j = 0; j < data.length; j++){
                BillItemDTO item = new BillItemDTO();
                if(headers[j].contains("客户名称")){
                    if(targetType.equals(AssetTargetType.ORGANIZATION.getCode())){//企业客户
                    	if(StringUtils.isBlank(data[j])) {
                    		log.setErrorLog("企业客户情况下，客户名称必填");
                            log.setCode(AssetBillImportErrorCodes.CUSTOM_NAME_EMPTY_ERROR);
                            datas.add(log);
                            continue bill;
                    	}else {
                    		cmd.setTargetName(data[j]);
                    		//通过客户名称（企业名称）查询关联的企业id
                    		Organization organizationByName = organizationProvider.findOrganizationByName(data[j], namespaceId);
                            if(organizationByName != null){
                                cmd.setTargetId(organizationByName.getId());
                            }
                    	}
                    }else {//个人客户
                    	cmd.setTargetName(data[j]);
                    }
                }else if(headers[j].contains("客户手机号")){
                	if(data[j] != null && data[j] != "") {
                		if(!RegularExpressionUtils.isValidChinesePhone(data[j])){
                            log.setErrorLog("客户手机号码格式不正确");
                            log.setCode(AssetBillImportErrorCodes.USER_CUSTOMER_TEL_ERROR);
                            datas.add(log);
                            continue bill;
                        }
                    	UserIdentifier claimedIdentifierByToken = userProvider.findClaimedIdentifierByToken(namespaceId, data[j]);
                        if(claimedIdentifierByToken!=null){
                            cmd.setTargetId(claimedIdentifierByToken.getOwnerUid());
                        }
                    	cmd.setCustomerTel(data[j]);
                	}
                }else if(headers[j].contains("合同编号")){
                    cmd.setContractNum(data[j]);
                    List<Long> list = contractProvider.SimpleFindContractByNumber(data[j]);
                    if(list.size() != 1){
                        LOGGER.warn("SimpleFindContractByNumber find more than 1, contract Id is={}", list);
                    }else{
                        cmd.setContractId(list.get(0));
                    }
                }else if(headers[j].contains("催缴手机号")){
                	List<String> list = new ArrayList<>();
                	list = Arrays.asList(data[j].split(","));
                	for (int k = 0; k < list.size(); k++) {
                		if(list.get(k) != null && list.get(k) != "") {
    	                    if(!RegularExpressionUtils.isValidChinesePhone(list.get(k))){
    	                        log.setErrorLog("催缴手机号码格式不正确");
    	                        log.setCode(AssetBillImportErrorCodes.USER_CUSTOMER_TEL_ERROR);
    	                        datas.add(log);
    	                        continue bill;
    	                    }
                    	}
					}
                	cmd.setNoticeTelList(list);
                	/*if(data[j] != null && data[j] != "") {
	                    if(!RegularExpressionUtils.isValidChinesePhone(data[j])){
	                        log.setErrorLog("催缴手机号码格式不正确");
	                        log.setCode(AssetBillImportErrorCodes.USER_CUSTOMER_TEL_ERROR);
	                        datas.add(log);
	                        continue bill;
	                    }
                	}
                    cmd.setNoticeTel(data[j]);*/
                }else if(j >= itemStartIndex && j <= itemEndIndex){// 收费项目
                    PaymentChargingItem itemPojo = getBillItemByName(namespaceId, ownerId, "community", billGroupId, handlerChargingItemName(headers[j]));
                	if(itemPojo == null){
                        log.setErrorLog("没有找到收费项目");
                        log.setCode(AssetBillImportErrorCodes.CHARGING_ITEM_NAME_ERROR);
                        datas.add(log);
                        continue bill;
                    }
                	BigDecimal amountReceivable = BigDecimal.ZERO;//应收含税
                	BigDecimal amountReceivableWithoutTax = BigDecimal.ZERO;//应收不含税
                	BigDecimal taxAmount = BigDecimal.ZERO;//税额
                	BigDecimal taxRate = BigDecimal.ZERO;//税率
                	
                    if(StringUtils.isBlank(data[j])){
                        log.setErrorLog("收费项目:"+headers[j]+"必填");
                        log.setCode(AssetBillImportErrorCodes.MANDATORY_BLANK_ERROR);
                        datas.add(log); 
                        continue bill;
                    }try{
                        amountReceivable = new BigDecimal(data[j]);
                        //issue-35830 【物业缴费6.5】收费项初始不设置税率，批量导入一条账单，修改税率，导出这条账单不含税和税额项为空
                        //后台直接查费项对应的税率
                        taxRate = assetProvider.getBillItemTaxRate(billGroupId, itemPojo.getId());
                        BigDecimal taxRateDiv = taxRate.divide(new BigDecimal(100));
            			amountReceivableWithoutTax = amountReceivable.divide(BigDecimal.ONE.add(taxRateDiv), 2, BigDecimal.ROUND_HALF_UP);
            			//税额=含税金额-不含税金额       税额=1000-909.09=90.91
            			taxAmount = amountReceivable.subtract(amountReceivableWithoutTax);
                    }catch (Exception e){
                        log.setErrorLog("收费项目:" + headers[j] + "数值格式不正确，应该填写保留两位小数的数字");
                        log.setCode(AssetBillImportErrorCodes.AMOUNT_INCORRECT);
                        datas.add(log);
                        continue bill;
                    }
                    item.setBillItemId(itemPojo.getId());
                    item.setBillItemName(itemPojo.getName());//解决导入的时候费项名称多了*的bug
                    item.setAmountReceivable(amountReceivable);
                    item.setAmountReceivableWithoutTax(amountReceivableWithoutTax);//应收不含税
                    item.setTaxAmount(taxAmount);//税额
                    item.setTaxRate(taxRate);//税率
                    item.setBuildingName(buildingName);
                    item.setApartmentName(apartmentName);
                    item.setAddressId(addressId);
                    //修复issue-34181 执行一些sql页面没有“用量”，但是导入的模板和导出Excel都有“用量”字段
                    if(assetService.isShowEnergy(namespaceId, ownerId, ServiceModuleConstants.ASSET_MODULE)) {
                    	//判断该域空间下是否显示用量  
                    	//如果费项是属于自用水电费、公摊水电费，那么会有用量
                    	if(itemPojo.getId().equals(AssetEnergyType.personWaterItem.getCode()) 
                        		|| itemPojo.getId().equals(AssetEnergyType.personElectricItem.getCode())
                        		|| itemPojo.getId().equals(AssetEnergyType.publicWaterItem.getCode())
                        		|| itemPojo.getId().equals(AssetEnergyType.publicElectricItem.getCode())) {
                    		item.setEnergyConsume(data[++j]);
                        }
                    }
                	billItemDTOList.add(item);
                }else if(headers[j].contains("减免金额")){
                    //减免项
                    try{
                        if(!StringUtils.isBlank(data[j])){
                            exemptionItemDTO = new ExemptionItemDTO();
                            exemptionItemDTO.setAmount(new BigDecimal(data[j]).multiply(new BigDecimal("-1")));
                            exemptionItemDTO.setIsPlus((byte)0);
                            //exemptionItemDTO.setDateStr(dateStr);//在创建的时候统一设置
                            exemptionItemDTOList.add(exemptionItemDTO);
                        }
                    }catch(Exception e){
                        log.setErrorLog("exemption amount error");
                        log.setCode(AssetBillImportErrorCodes.AMOUNT_INCORRECT);
                        datas.add(log);
                        continue bill;
                    }
                }else if(headers[j].contains("减免备注")){
                    if(exemptionItemDTO != null){
                        exemptionItemDTO.setRemark(data[j]);
                    }
                }else if(headers[j].contains("增收金额")){
                    try{
                        if(!StringUtils.isBlank(data[j])){
                            increaseItemDTO = new ExemptionItemDTO();
                            increaseItemDTO.setAmount(new BigDecimal(data[j]));
                            increaseItemDTO.setIsPlus((byte)1);
                            //increaseItemDTO.setDateStr(dateStr);//在创建的时候统一设置
                            exemptionItemDTOList.add(increaseItemDTO);
                        }
                    }catch(Exception e){
                        log.setErrorLog("amption amount error");
                        log.setCode(AssetBillImportErrorCodes.AMOUNT_INCORRECT);
                        datas.add(log);
                        continue bill;
                    }
                }else if(headers[j].contains("增收备注")){
                    if(increaseItemDTO != null){
                        increaseItemDTO.setRemark(data[j]);
                    }
                }else if(headers[j].contains("发票")){
                    cmd.setInvoiceNum(data[j]);
                }
            }
            fieldRoundElapse += System.currentTimeMillis() - fieldStartTime;

            billGroupDTO.setBillGroupId(billGroupId);
            billGroupDTO.setBillItemDTOList(billItemDTOList);
            billGroupDTO.setExemptionItemDTOList(exemptionItemDTOList);
            billGroupDTO.setBillGroupName(assetProvider.findBillGroupNameById(billGroupId));
            cmd.setBillGroupDTO(billGroupDTO);
            if(cmd.getTargetId() == null){
                // 没有找到用户，也可以导入
            }
            cmd.setOwnerType("community");
            cmd.setOwnerId(ownerId);
            cmd.setIsSettled(billSwitch);
            //个人客户时，若一次导入同一客户的同一账单时间的不同门牌费项明细，需以客户维度将几条合为一个账单出到该客户。
            if(targetType.equals(AssetTargetType.USER.getCode())){
            	for(int m = 0;m < cmds.size();m++) {
            		CreateBillCommand createBillCommand = cmds.get(m);
            		if(cmd.getTargetName().equals(createBillCommand.getTargetName()) 
        				&& cmd.getDateStrBegin().equals(createBillCommand.getDateStrBegin())
        				&& cmd.getDateStrEnd().equals(createBillCommand.getDateStrEnd())) {
            			//个人客户时，以账单时间、账单组、楼栋门牌3条信息定位账单的唯一性。若再次导入同一账单，均认为覆盖原账单，而不是新增账单。除定位账单的字段外其余字段均覆盖。
        				if(createBillCommand.getAddresses().equals(cmd.getAddresses())) {
        					cmds.set(m, cmd);
                			continue bill;
        				}else if(!createBillCommand.getAddresses().contains(cmd.getAddresses())){//未合并为一个账单之前的情况，如：456、457， 新的数据是456，是不同的数据，所以是新增。
        					//个人客户时，若一次导入同一客户的同一账单时间的不同门牌费项明细，需以客户维度将几条合为一个账单出到该客户。
        					BillGroupDTO newBillGroupDTO = createBillCommand.getBillGroupDTO();
                			List<BillItemDTO> newBillItemDTOList = newBillGroupDTO.getBillItemDTOList();
                			List<ExemptionItemDTO> newExemptionItemDTOList = newBillGroupDTO.getExemptionItemDTOList();
                			newBillItemDTOList.addAll(billItemDTOList);
                			newExemptionItemDTOList.addAll(exemptionItemDTOList);
                			newBillGroupDTO.setBillItemDTOList(newBillItemDTOList);
                			newBillGroupDTO.setExemptionItemDTOList(newExemptionItemDTOList);
                			createBillCommand.setBillGroupDTO(newBillGroupDTO);
                			createBillCommand.setAddresses(cmd.getAddresses() + "," + createBillCommand.getAddresses());
                			cmds.set(m, createBillCommand);
                			continue bill;
        				}
            		}
            	}
            }
            cmds.add(cmd);

            // 打印阶段日志和时间，方便定位性能问题 by lqs 20181206
            if((i % 500) == 0 && LOGGER.isInfoEnabled()) {
                long roundEndTime = System.currentTimeMillis();
                int cmdSize = (cmds == null) ? 0 : cmds.size();
                int dataSize = (datas == null) ? 0 : datas.size();
                LOGGER.info("Process bill importing data, parse excel data roundly, index={}, namespaceId={}, billGroupId={}, targetType={}, cmdSize={}, dataSize={}, roundElapse={}, fieldRoundElapse={}",
                        i, namespaceId, billGroupId, targetType, cmdSize, dataSize, (roundEndTime - startTime), fieldRoundElapse);
                roundStartTime = roundEndTime;
                fieldRoundElapse = 0;
            }
        }
        map.put(cmds, datas);

        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            int cmdSize = (cmds == null) ? 0 : cmds.size();
            int dataSize = (datas == null) ? 0 : datas.size();
            LOGGER.info("Process bill importing data, parse excel data end, namespaceId={}, billGroupId={}, targetType={}, cmdSize={}, dataSize={}, elapse={}",
                    namespaceId, billGroupId, targetType, cmdSize, dataSize, (endTime - startTime));
        }

        return map;
    }

    private PaymentChargingItem getBillItemByName(Integer namespaceId, Long ownerId, String ownerType, Long billGroupId, String projectLevelName) {
        return assetProvider.getBillItemByName(namespaceId,  ownerId,  ownerType, billGroupId, projectLevelName);
    }

    private String handlerChargingItemName(String value) {
        return value.replace("*","")
                .replace("(","")
                .replace(")","")
                .replace("元","");
    }

    private String[] getOrderedCellValues(RowResult header, Integer limit) {
//        // 指定了初始容量的hashmap的遍历并不慢
//        HashMap<String,String> cellsMap = new HashMap<>(27);
        int count = 1;
        int init = header.getCells().size();
        if(limit != null){
            init = limit;
        }else{
            limit = init;
        }
        String[] data = new String[init];

        if(header.getA() == null && count <= limit){
            header.setA("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getA();
        count++;
        if(header.getB() == null&& count <= limit){
            header.setB("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getB();
        count++;
        if(header.getC() == null&& count <= limit){
            header.setC("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getC();
        count++;
        if(header.getD() == null&& count <= limit){
            header.setD("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getD();
        count++;
        if(header.getE() == null&& count <= limit){
            header.setE("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getE();
        count++;
        if(header.getF() == null&& count <= limit){
            header.setF("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getF();
        count++;
        if(header.getG() == null&& count <= limit){
            header.setG("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getG();
        count++;
        if(header.getH() == null&& count <= limit){
            header.setH("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getH();
        count++;
        if(header.getI() == null&& count <= limit){
            header.setI("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getI();
        count++;
        if(header.getJ() == null&& count <= limit){
            header.setJ("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getJ();
        count++;

        if(header.getK() == null&& count <= limit){
            header.setK("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getK();
        count++;

        if(header.getL() == null&& count <= limit){
            header.setL("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getL();
        count++;

        // M
        if(header.getM() == null&& count <= limit){
            header.setM("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getM();
        count++;
        if(header.getN() == null&& count <= limit){
            header.setN("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getN();
        count++;
        if(header.getO() == null&& count <= limit){
            header.setO("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getO();
        count++;
        if(header.getP() == null&& count <= limit){
            header.setP("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getP();
        count++;
        if(header.getQ() == null&& count <= limit){
            header.setQ("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getQ();
        count++;
        if(header.getR() == null&& count <= limit){
            header.setR("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getR();
        count++;
        if(header.getS() == null&& count <= limit){
            header.setS("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getS();
        count++;
        if(header.getT() == null&& count <= limit){
            header.setT("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getT();
        count++;
        if(header.getU() == null&& count <= limit){
            header.setU("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getU();
        count++;
        if(header.getV() == null&& count <= limit){
            header.setV("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getV();
        count++;
        if(header.getW() == null&& count <= limit){
            header.setW("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getW();
        count++;
        if(header.getX() == null&& count <= limit){
            header.setX("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getX();
        count++;
        if(header.getY() == null&& count <= limit){
            header.setY("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getY();
        count++;
        if(header.getZ() == null&& count <= limit){
            header.setZ("");
        }
        if(count - 1 >= init){
            return data;
        }
        data[count-1] = header.getZ();
//        count++;
        return data;
//         此方法不能支持index
////        Map<String, String> cells = header.getCells();
//        cellsMap.putAll(cells);
////        Iterator<Map.Entry<String, String>> iterator = cellsMap.entrySet().iterator();
////        // 迭代器删除
////        while(iterator.hasNext()){
////            Map.Entry<String, String> next = iterator.next();
////            if(org.apache.commons.lang.StringUtils.isEmpty(next.getValue())){
////                cellsMap.remove(next.getKey());
////            }
////        }
//        TreeMap<String, String> treeMap = new TreeMap<>();
//        treeMap.putAll(cellsMap);
//        return treeMap.values().toArray(new String[treeMap.size()]);
    }
//    @Override
//    public ListPaymentBillResp listBillRelatedTransac(listBillRelatedTransacCommand cmd) {
//        Long billId = cmd.getBillId();
//        List<AssetPaymentOrder> assetOrders = assetProvider.findAssetOrderByBillId(String.valueOf(billId));
//        PaymentBills bill = assetProvider.findPaymentBillById(billId);
//        if(bill == null || assetOrders.size() < 1){
//            return new ListPaymentBillResp();
//        }
//        ListPaymentBillCmd paycmd = new ListPaymentBillCmd();
//        paycmd.setNamespaceId(bill.getNamespaceId());
//        paycmd.setOrderType("wuyecode");
//        paycmd.setPageAnchor(cmd.getPageAnchor());
//        paycmd.setPageSize(cmd.getPageSize());
//        if(cmd.getCommunityId() == null){
//            paycmd.setCommunityId(bill.getOwnerId());
//        }else{
//            paycmd.setCommunityId(cmd.getCommunityId());
//        }
//        paycmd.setUserType(cmd.getUserType());
//        paycmd.setUserId(cmd.getUserId());
//        paycmd.setOrderIds(assetOrders.stream().map(r -> r.getId()).collect(Collectors.toList()));
//        try {
//            return paymentService.listPaymentBill(paycmd);
//        } catch (Exception e) {
//            LOGGER.error("list payment bills failed, paycmd={}",paycmd);
//            return new ListPaymentBillResp();
//        }
//    }


    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId,String targetType,Long organizationId) {
        return assetProvider.getBillDetailForClient(Long.parseLong(billId));
    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus,Long ownerId, String ownerType, String targetType, Long targetId, String dateStr,String contractId, Long billGroupId) {
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
    private List<ContractDTO> listCustomerContracts(String targetType,Long targetId,Integer namespaceId,Long communityId){
        ListCustomerContractsCommand cmd = new ListCustomerContractsCommand();
        if(targetType.equals(AssetPaymentConstants.EH_ORGANIZATION)){
            cmd.setTargetType(CustomerType.ENTERPRISE.getCode());
            cmd.setTargetId(targetId);
        }else if(targetType.equals(AssetPaymentConstants.EH_USER)){
            cmd.setTargetType(CustomerType.INDIVIDUAL.getCode());
            cmd.setTargetId(UserContext.currentUserId());
        }
        //合同方面新增了cmd的默认参数
        cmd.setAdminFlag((byte)1);
        cmd.setNamespaceId(namespaceId);
        cmd.setCommunityId(communityId);
        return contractService.listCustomerContracts(cmd);
    }
    
    public ShowCreateBillSubItemListDTO showCreateBillSubItemList(ShowCreateBillSubItemListCmd cmd) {
    	return assetProvider.showCreateBillSubItemList(cmd);
    }        
}
