
package com.everhomes.asset;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.zjgkVOs.ZjgkPaymentConstants;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contract.ContractService;
import com.everhomes.contract.ContractServiceImpl;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.order.PayService;
import com.everhomes.organization.*;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RegularExpressionUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.poi.hssf.util.HSSFColor;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/4/11.
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX + "ZUOLIN")
public class ZuolinAssetVendorHandler extends AssetVendorHandler {
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
    private PaymentService paymentService;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ContractServiceImpl contractService;

    @Autowired
    private PayService payService;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ContractProvider contractProvider;

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
    public List<ListBillsDTO> listBills(Integer currentNamespaceId,ListBillsResponse response, ListBillsCommand cmd) {
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
        return assetProvider.creatPropertyBill(cmd);
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
//            return response;
            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.ERROR_IN_GENERATING,"Mission in processStat");
        }
        List<PaymentExpectancyDTO> dtos = assetProvider.listBillExpectanciesOnContract(cmd.getContractNum(),cmd.getPageOffset(),cmd.getPageSize(),cmd.getContractId());
        if(dtos.size() <= cmd.getPageSize()){
//            response.setNextPageOffset(cmd.getPageOffset());
            response.setNextPageOffset(null);
        }else{
            response.setNextPageOffset(cmd.getPageOffset()+cmd.getPageSize());
            dtos.remove(dtos.size()-1);
        }
        BigDecimal totalAmount = assetProvider.getBillExpectanciesAmountOnContract(cmd.getContractNum(),cmd.getContractId());
        response.setList(dtos);
        response.setTotalAmount(totalAmount.toString());
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
        List<BillIdAndAmount> bills = cmd.getBills();
        List<String> billIds = new ArrayList<>();
        Long amountsInCents = 0l;
        for(BillIdAndAmount billIdAndAmount : bills){
            billIds.add(billIdAndAmount.getBillId());
            String amountOwed = billIdAndAmount.getAmountOwed();
            Float amountOwedInCents = Float.parseFloat(amountOwed)*100f;
            amountsInCents += amountOwedInCents.longValue();
        }
        //对左邻的用户，直接检查bill的状态即可
        checkHasPaidBills(billIds);
        //这种检查的逻辑是不对的
//        Long checkedOrderId = assetProvider.findAssetOrderByBillIds(billIds);
//        if(checkedOrderId !=null){
//            //重复下单的返回
//            return null;
//        }
        //如果账单为新的，则进行存储
        Long orderId  = assetProvider.saveAnOrderCopy(cmd.getPayerType(),cmd.getPayerId(),String.valueOf(amountsInCents/100l),cmd.getClientAppName(),cmd.getCommunityId(),cmd.getContactNum(),cmd.getOpenid(),cmd.getPayerName(),ZjgkPaymentConstants.EXPIRE_TIME_15_MIN_IN_SEC, cmd.getNamespaceId(),OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
        assetProvider.saveOrderBills(bills,orderId);
        Long payerId = Long.parseLong(cmd.getPayerId());
        //检查下单人的类型和id，不能为空
        if(cmd.getPayerType().equals(AssetTargetType.USER.getCode())){
//            if(Long.parseLong(cmd.getPayerId())==UserContext.currentUserId()){
//                payerId = Long.parseLong(cmd.getPayerId());
//            }else{
//                LOGGER.error("individual make asset order failed, the given uid = {}, but the online uid is = {}",cmd.getPayerId(),UserContext.currentUserId());
//                throw new RuntimeErrorException("individual make asset order failed");
//            }
            payerId = UserContext.currentUserId();
        }

        //组装command ， 请求支付模块的下预付单
        PreOrderCommand cmd2pay = new PreOrderCommand();
        cmd2pay.setAmount(amountsInCents);
        cmd2pay.setCommunityId(cmd.getCommunityId());
//        cmd2pay.setAmount(1l);
        cmd2pay.setClientAppName(cmd.getClientAppName());
        cmd2pay.setExpiration(ZjgkPaymentConstants.EXPIRE_TIME_15_MIN_IN_SEC);
        cmd2pay.setNamespaceId(cmd.getNamespaceId());
        cmd2pay.setOpenid(cmd.getOpenid());
        cmd2pay.setOrderId(orderId);
        cmd2pay.setOrderType(OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
        cmd2pay.setPayerId(payerId);

        //不填写paymentType，支持所有除了微信公众号的支付手段
//        cmd2pay.setPaymentType(PaymentType.WECHAT_APPPAY.getCode());

        //这个参数组装有什么用？
//        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
//        paymentParamsDTO.setPayType("no_credit");
//        User user = UserContext.current().getUser();
//        paymentParamsDTO.setAcct(user.getNamespaceUserToken());
//        cmd2pay.setPaymentParams(paymentParamsDTO);

        PreOrderDTO preOrder = payService.createPreOrder(cmd2pay);
//        response.setAmount(String.valueOf(preOrder.getAmount()));
//        response.setExpiredIntervalTime(15l*60l);
//        response.setOrderCommitNonce(preOrder.getOrderCommitNonce());
//        response.setOrderCommitTimestamp(preOrder.getOrderCommitTimestamp());
//        response.setOrderCommitToken(preOrder.getOrderCommitToken());
//        response.setOrderCommitUrl(preOrder.getOrderCommitUrl());
//        response.setPayMethod(preOrder.getPayMethod());

        return preOrder;
    }

    private void checkHasPaidBills(List<String> billIds) {
        List<PaymentBills> paidBills = assetProvider.findPaidBillsByIds(billIds);
        if( paidBills.size() >0 ) throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.HAS_PAID_BILLS,"this is bills have been paid,please refresh");
    }


    /**
     * method implementation:
     * in this method, contracts will be found for the customer. And divided into groups by contractId and billGroupId
     * being the key , while having the bills to be values.
     *
     * @active mehtod implementation:
     * 寻找用户，这里不关心合同
     */
    @Override
    public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
        checkCustomerParameter(cmd.getTargetType(), cmd.getTargetId());
        List<ShowBillForClientV2DTO> tabBills = new ArrayList<>();
//        //查询合同，用来聚类
//        List<ContractDTO> contracts = listCustomerContracts(cmd.getTargetType(), cmd.getTargetId(), cmd.getNamespaceId(), cmd.getOwnerId());
//        if(contracts == null || contracts.size() < 1){
//            return null;
//        }
//        List<Long> contractIds = new ArrayList<>();
//        Map<Long,ContractDTO> contractMap = new HashMap<>();
//        contracts.stream().forEach(r -> contractMap.put(r.getId(),r));
//        contracts.stream().forEach(r -> contractIds.add(r.getId()));
//        List<PaymentBills> bills = assetProvider.findSettledBillsByContractIds(contractIds);

        //定位用户，如果是个人用户，前端拿不到用户id，从会话中获得
        if(cmd.getTargetType().equals(AssetPaymentStrings.EH_USER)){
            cmd.setTargetId(UserContext.currentUserId());
        }
        //获得此用户的所有账单
        List<PaymentBills> paymentBills = assetProvider.findSettledBillsByCustomer(cmd.getTargetType(),cmd.getTargetId(),cmd.getOwnerType(),cmd.getOwnerId());
        //进行分类，冗杂代码，用空间换时间， 字符串操作+类型转换  vs  新建对象; 对象隐式指定最大寿命
        List<Map<?,?>> maps = new ArrayList<>();
        tryMakeCategory:{
            //同样contract和billGroup一样的在一起，没有contract视为同样的contract。 首先，比较contractid+billGroup先拿人得到n个tab，contract+billgroup继续
            //剩下的按照billGroup再分tab
            Map<ContractIdBillGroup, List<PaymentBills>> idMap = new HashMap<>();
            Map<ContractNumBillGroup, List<PaymentBills>> numMap = new HashMap<>();
            Map<Long, List<PaymentBills>> groupMap = new HashMap<>();
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
                    if (groupMap.containsKey(bill.getBillGroupId())) {
                        groupMap.get(bill.getBillGroupId()).add(bill);
                    } else {
                        List<PaymentBills> idList = new ArrayList<>();
                        idList.add(bill);
                        groupMap.put(bill.getBillGroupId(), idList);
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
                        dto.setBillGroupName(assetProvider.getbillGroupNameById(billGroupId));
                        dto.setBillGroupId(billGroupId);
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
                    try{
                        //获得contract的categoryId
                        if(!StringUtils.isBlank(dto.getContractId())){
                            Long categoryId = contractService.findContractCategoryIdByContractId(Long.valueOf(dto.getContractId()));
                            dto.setCategoryId(categoryId);
                        }
                    }catch (Exception e){
                        LOGGER.error("failed to get category id, contractId is={}",dto.getContractId(), e);
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
        return assetProvider.listAllBillsForClient(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getTargetType(),cmd.getOwnerType().equals(AssetPaymentConstants.EH_USER)?UserContext.currentUserId():cmd.getTargetId(), status, cmd.getBillGroupId());
    }

    @Override
    public void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response) {
        ShowCreateBillDTO webPage = assetProvider.showCreateBill(cmd.getBillGroupId());
        List<String> headList = new ArrayList<>();
        List<Integer> mandatoryIndex = new ArrayList<>();
        Integer cur = -1;
        //批量导入的字段去掉账期
        //headList.add("账期");
        //cur++;
        //mandatoryIndex.add(1);
        headList.add("账单开始时间");
        cur++;
        mandatoryIndex.add(1);//账期开始时间置为必填
        headList.add("账单结束时间");
        cur++;
        mandatoryIndex.add(1);//账期结束时间置为必填
        headList.add("客户属性");
        cur++;
        mandatoryIndex.add(1);
        headList.add("客户名称");
        cur++;
        mandatoryIndex.add(1);
        headList.add("合同编号");
        cur++;
        mandatoryIndex.add(0);
        headList.add("客户手机号(个人客户必填)");
        cur++;
        mandatoryIndex.add(0);
        headList.add("催缴手机号");
        cur++;
        mandatoryIndex.add(1);
        //可变标题 , 需要后期excel加字段？ not likely    在数据库中能获得这些字段的展示名称吗？ not likely   写死中文？ 导出功能较多时，考虑建立导出设置表统一管理
        List<BillItemDTO> billItemDTOList = webPage.getBillItemDTOList();
        for(BillItemDTO dto : billItemDTOList){
            headList.add(dto.getBillItemName()+"(元)");
            cur++;
            mandatoryIndex.add(1);
        }
        headList.add("楼栋");
        cur++;
        mandatoryIndex.add(0);
        headList.add("门牌");
        cur++;
        mandatoryIndex.add(0);
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
        new ExcelUtils(response,"账单导入模板"+fileName+System.currentTimeMillis(),fileName+"模板")
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
                        "5、账单、收费项以导出的为准，不可修改，修改后将导致导入不成功。\n" +
                        "6、企业客户需填写与系统内客户管理一致的准企业名称，个人客户需填写与系统内个人客户资料一致的手机号，否则会导致无法定位客户。\n" +
                        "7、客户属性为个人客户时，手机号为唯一身份识别标识，客户手机号必填。\n" +
                        "8、账单开始时间，账单结束时间的格式只能为 2018-01-12,2018/01/12", (short)13, (short)2500)
                .setNeedSequenceColumn(false)
                .setIsCellStylePureString(true)
                .writeExcel(null, headers, true, null, null);
    }

    @Override
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
            Map<List<CreateBillCommand>, List<ImportFileResultLog<List<String>>>> map = handleImportBillData(finalResultList, cmd.getBillGroupId(), cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getBillSwitch());
            List<CreateBillCommand> createBillCommands = new ArrayList<>();
            List<ImportFileResultLog<List<String>>> datas = new ArrayList<>();
            for(Map.Entry<List<CreateBillCommand>, List<ImportFileResultLog<List<String>>>> entry : map.entrySet()){
                createBillCommands = entry.getKey();
                datas = entry.getValue();
            }
            for(CreateBillCommand command : createBillCommands){
                command.setCategoryId(categoryId);
                createBill(command);
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

    private Map<List<CreateBillCommand>, List<ImportFileResultLog<List<String>>>> handleImportBillData(ArrayList resultList, Long billGroupId, Integer namespaceId, Long ownerId, Byte billSwitch) {
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
        int itemStart = 8;
        int itemEnd = 0;
        int buildingIndex = 0;
        int apartmentIndex = 0;
        int dateStrIndex = 0;
        int targetTypeIndex = 0;
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase("*催缴手机号")) itemStart = i + 1;
            else if (headers[i].equalsIgnoreCase("楼栋")) {
                itemEnd = i - 1;
                buildingIndex = i;
            }
            else if (headers[i].equalsIgnoreCase("门牌")) apartmentIndex = i;
            else if (headers[i].contains("客户属性")) targetTypeIndex = i;
            else if(headers[i].contains("账期")) dateStrIndex = i;
        }
        bill:for (int i = 2; i < resultList.size(); i++) {
            RowResult currentRow = (RowResult) resultList.get(i);
            String[] data = getOrderedCellValues(currentRow, headers.length);
            //放置log
            ImportFileResultLog<List<String>> log = new ImportFileResultLog<>(AssetBillImportErrorCodes.SCOPE);
            log.setData(Arrays.asList(data));


            CreateBillCommand cmd = new CreateBillCommand();
            BillGroupDTO billGroupDTO = new BillGroupDTO(cmd);
            List<BillItemDTO> billItemDTOList = new ArrayList<>();
            List<ExemptionItemDTO> exemptionItemDTOList = new ArrayList<>();
//            RowResult dataRow = (RowResult) resultList.get(i);
//            String[] data = getOrderedCellValues(dataRow, headers.length);
            ExemptionItemDTO exemptionItemDTO = null;
            ExemptionItemDTO increaseItemDTO = null;
            //账期被依赖
            String dateStr = DateUtils.guessDateTimeFormatAndFormatIt(data[dateStrIndex], "yyyy-MM");
            if(StringUtils.isBlank(dateStr)){
                log.setErrorLog("日期格式错误,请参考说明进行填写");
                log.setCode(AssetBillImportErrorCodes.DATE_STR_EMPTY_ERROR);
                datas.add(log);
                continue bill;
            }
            cmd.setDateStr(dateStr);
            //楼栋门牌也是
            String building = data[buildingIndex];
            String apartment = data[apartmentIndex];
            //客户属性也是
            switch (data[targetTypeIndex]){
                case "企业客户":
                    cmd.setTargetType(AssetTargetType.ORGANIZATION.getCode());
                    break;
                case "个人客户":
                    cmd.setTargetType(AssetTargetType.USER.getCode());
                    break;
                default:
                    //构造log然后离开这一行的处理
                    log.setErrorLog("客户属性错误，只允许填写个人客户或者企业客户");
                    log.setCode(AssetBillImportErrorCodes.CUSTOM_TYPE_ERROR);
                    datas.add(log);
                    continue bill;
            }
            for(int j = 0; j < data.length; j++){

                BillItemDTO item = new BillItemDTO();
                if(headers[j].contains("客户名称")){
                    if(StringUtils.isBlank(data[j])){
                        log.setErrorLog("customer name cannot be empty");
                        log.setCode(AssetBillImportErrorCodes.CUSTOM_NAME_EMPTY_ERROR);
                        datas.add(log);
                        continue bill;
                    }
                    cmd.setTargetName(data[j]);
                    if(cmd.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())){
                        Organization organizationByName = organizationProvider.findOrganizationByName(data[j], namespaceId);
                        if(organizationByName != null){
                            cmd.setTargetId(organizationByName.getId());
                        }
                        // 找不到用户也可以导入
//                        if(organizationProvider.findOrganizationByName(data[j], namespaceId).getId() == null){
//                            log.setErrorLog("customer Id cannot be found， org name might be wrong");
//                            log.setCode(AssetBillImportErrorCodes.CUSTOM_TYPE_ERROR);
//                            datas.add(log);
//                            continue bill;
//                        };

                    }
                }else if(headers[j].contains("客户手机号")){
                    if(cmd.getTargetType().equals(AssetTargetType.USER.getCode())){
                        if(StringUtils.isBlank(data[j])){
                            log.setErrorLog("个人客户情况下，客户手机号不能为空");
                            log.setCode(AssetBillImportErrorCodes.USER_CUSTOMER_TEL_ERROR);
                            datas.add(log);
                            continue bill;
                        }

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
                    }
                    cmd.setCustomerTel(data[j]);
                }
                else if(headers[j].equals("*账单开始时间")){
                    cmd.setDateStrBegin(DateUtils.guessDateTimeFormatAndFormatIt(data[j], "yyyy-MM-dd"));
                }else if(headers[j].equals("*账单结束时间")){
                    cmd.setDateStrEnd(DateUtils.guessDateTimeFormatAndFormatIt(data[j], "yyyy-MM-dd"));
                }else if(headers[j].equals("合同编号")){
                    cmd.setContractNum(data[j]);
                    List<Long> list = contractProvider.SimpleFindContractByNumber(data[j]);
                    if(list.size() != 1){
                        LOGGER.warn("SimpleFindContractByNumber find more than 1, contract Id is={}", list);
                    }else{
                        cmd.setContractId(list.get(0));
                    }
                }else if(headers[j].contains("催缴手机号")){
                    if(StringUtils.isBlank(data[j])){
                        log.setErrorLog("notice tel cannot be emtpy");
                        log.setCode(AssetBillImportErrorCodes.NOTICE_TEL_EMPTY_ERROR);
                        datas.add(log);
                        continue bill;
                    }
                    if(!RegularExpressionUtils.isValidChinesePhone(data[j])){
                        log.setErrorLog("催缴手机号码格式不正确");
                        log.setCode(AssetBillImportErrorCodes.USER_CUSTOMER_TEL_ERROR);
                        datas.add(log);
                        continue bill;
                    }
                    cmd.setNoticeTel(data[j]);
                }
                // 收费项目
                else if(j >= itemStart && j <= itemEnd){
                    PaymentChargingItem itemPojo = getBillItemByName(namespaceId, ownerId, "community", billGroupId, handlerChargingItemName(headers[j]));
                    if(itemPojo == null){
                        log.setErrorLog("charging Item not found");
                        log.setCode(AssetBillImportErrorCodes.CHARGING_ITEM_NAME_ERROR);
                        datas.add(log);
                        continue bill;
                    }
                    BigDecimal amountReceivable = null;
                    if(StringUtils.isBlank(data[j])){
                        log.setErrorLog("收费项目:"+headers[j]+"必填");
                        log.setCode(AssetBillImportErrorCodes.MANDATORY_BLANK_ERROR);
                        datas.add(log);
                        continue bill;
                    }try{
                        amountReceivable = new BigDecimal(data[j]);
                    }catch (Exception e){
                        log.setErrorLog("收费项目:" + headers[j] + "数值格式不正确，应该填写保留两位小数的数字");
                        log.setCode(AssetBillImportErrorCodes.AMOUNT_INCORRECT);
                        datas.add(log);
                        continue bill;
                    }

                    // id , name, groupRuleId, amount, 楼栋，门牌，addressId
                    item.setBillItemId(itemPojo.getId());
                    item.setBillItemName(headers[j]);
                    item.setAmountReceivable(amountReceivable);
                    item.setBuildingName(building);
                    item.setApartmentName(apartment);
                    billItemDTOList.add(item);
                }else if(headers[j].contains("减免金额")){
                    //减免项
                    try{
                        if(!StringUtils.isBlank(data[j])){
                            exemptionItemDTO = new ExemptionItemDTO();
                            exemptionItemDTO.setAmount(new BigDecimal(data[j]).multiply(new BigDecimal("-1")));
                            exemptionItemDTO.setIsPlus((byte)0);
                            exemptionItemDTO.setDateStr(dateStr);
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
                            increaseItemDTO.setDateStr(dateStr);
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
            billGroupDTO.setBillGroupId(billGroupId);
            billGroupDTO.setBillItemDTOList(billItemDTOList);
            billGroupDTO.setExemptionItemDTOList(exemptionItemDTOList);
            billGroupDTO.setBillGroupName(assetProvider.findBillGroupNameById(billGroupId));
            if(cmd.getTargetId() == null){
                // 没有找到用户，也可以导入
            }
            cmd.setOwnerType("community");
            cmd.setOwnerId(ownerId);
            cmd.setIsSettled(billSwitch);
            cmds.add(cmd);
        }
        map.put(cmds, datas);
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
    @Override
    public ListPaymentBillResp listBillRelatedTransac(listBillRelatedTransacCommand cmd) {
        Long billId = cmd.getBillId();
        List<AssetPaymentOrder> assetOrders = assetProvider.findAssetOrderByBillId(String.valueOf(billId));
        PaymentBills bill = assetProvider.findPaymentBillById(billId);
        if(bill == null || assetOrders.size() < 1){
            return new ListPaymentBillResp();
        }
        ListPaymentBillCmd paycmd = new ListPaymentBillCmd();
        paycmd.setNamespaceId(bill.getNamespaceId());
        paycmd.setOrderType("wuyecode");
        paycmd.setPageAnchor(cmd.getPageAnchor());
        paycmd.setPageSize(cmd.getPageSize());
        if(cmd.getCommunityId() == null){
            paycmd.setCommunityId(bill.getOwnerId());
        }else{
            paycmd.setCommunityId(cmd.getCommunityId());
        }
        paycmd.setUserType(cmd.getUserType());
        paycmd.setUserId(cmd.getUserId());
        paycmd.setOrderIds(assetOrders.stream().map(r -> r.getId()).collect(Collectors.toList()));
        try {
            return paymentService.listPaymentBill(paycmd);
        } catch (Exception e) {
            LOGGER.error("list payment bills failed, paycmd={}",paycmd);
            return new ListPaymentBillResp();
        }
    }


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
}

