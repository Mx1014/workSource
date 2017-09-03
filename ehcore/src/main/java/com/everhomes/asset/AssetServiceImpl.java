package com.everhomes.asset;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contract.ContractService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ListCustomerContractsCommand;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserNotificationTemplateCode;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPaymentBills;
import com.everhomes.server.schema.tables.pojos.EhPaymentContractReceiver;
import com.everhomes.sms.SmsProvider;
import com.everhomes.techpark.rental.RentalServiceImpl;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.DSLContext;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;
import scala.Char;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private LocaleStringProvider localeStringProvider;

    @Autowired
    private GroupProvider groupProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private FamilyProvider familyProvider;

    @Autowired
    private DbProvider dbProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private CacheProvider cacheProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SmsProvider smsProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private ContractService contractService;

    @Override
    public List<ListOrganizationsByPmAdminDTO> listOrganizationsByPmAdmin() {
        List<ListOrganizationsByPmAdminDTO> dtoList = new ArrayList<>();

        final Long userId = UserContext.current().getUser().getId();
        // 拿到用户所在的公司列表
        List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(UserContext.getCurrentNamespaceId(),
                userId, OrganizationGroupType.ENTERPRISE);

        if (organizationList == null || organizationList.isEmpty()) {
            return dtoList;
        }

        ListServiceModuleAdministratorsCommand cmd = new ListServiceModuleAdministratorsCommand();
        for (OrganizationDTO organization : organizationList) {
            cmd.setOrganizationId(organization.getId());
            // 获取当前用户为超级管理员的公司
            List<OrganizationContactDTO> organizationAdmins = rolePrivilegeService.listOrganizationSuperAdministrators(cmd);
            if (organizationAdmins != null && organizationAdmins.size() > 0) {
                boolean isAdmin = organizationAdmins.stream().mapToLong(OrganizationContactDTO::getTargetId).anyMatch(adminId -> adminId == userId);
                if (isAdmin) {
                    dtoList.add(toOrganizationsByPmAdminDTO(organization));
                    continue;
                }
            }
            // 获取当前用户为企业管理员的公司
            List<OrganizationContactDTO> organizationPms = rolePrivilegeService.listOrganizationAdministrators(cmd);
            if (organizationPms != null && organizationPms.size() > 0) {
                boolean isPm = organizationPms.stream().mapToLong(OrganizationContactDTO::getTargetId).anyMatch(pmId -> pmId == userId);
                if (isPm) {
                    dtoList.add(toOrganizationsByPmAdminDTO(organization));
                }
            }
        }
        this.processLatestSelectedOrganization(dtoList);
        return dtoList;
    }

    @Override
    public ListBillsResponse listBills(ListBillsCommand cmd) {
        String dateStrBegin = cmd.getDateStrBegin();
        String dateStrEnd = cmd.getDateStrEnd();
        AssetVendor assetVendor = checkAssetVendor(cmd.getOwnerType(),cmd.getOwnerId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        ListBillsResponse response = new ListBillsResponse();
        if (cmd.getPageAnchor() == null || cmd.getPageAnchor() < 1) {
            cmd.setPageAnchor(0l);
        }
        if(cmd.getPageSize() == null || cmd.getPageSize() < 1 || cmd.getPageSize() > Integer.MAX_VALUE/10){
            cmd.setPageSize(20);
        }
        int pageOffSet = cmd.getPageAnchor().intValue();
        List<ListBillsDTO> list = handler.listBills(UserContext.getCurrentNamespaceId(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getBuildingName(),cmd.getApartmentName(),cmd.getAddressId(),cmd.getBillGroupName(),cmd.getBillGroupId(),cmd.getBillStatus(),cmd.getDateStrBegin(),cmd.getDateStrEnd(),pageOffSet,cmd.getPageSize(),cmd.getTargetName(),cmd.getStatus());
        if(list.size() <= cmd.getPageSize()){
            response.setNextPageAnchor(null);
        }else{
            response.setNextPageAnchor(((Integer)(pageOffSet+cmd.getPageSize())).longValue());
            list.remove(list.size()-1);
        }
        response.setListBillsDTOS(list);
        return response;
    }

    @Override
    public ListBillItemsResponse listBillItems(ListBillItemsCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(cmd.getOwnerType(),cmd.getOwnerId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        ListBillItemsResponse response = new ListBillItemsResponse();
        if (cmd.getPageAnchor() == null || cmd.getPageAnchor() < 1) {
            cmd.setPageAnchor(0l);
        }
        if(cmd.getPageSize() == null){
            cmd.setPageSize(20);
        }
        int pageOffSet = cmd.getPageAnchor().intValue();
        List<BillDTO> billDTOS = handler.listBillItems(cmd.getBillId(),cmd.getTargetName(),pageOffSet,cmd.getPageSize());
        if(billDTOS.size() <= cmd.getPageSize()) {
            response.setNextPageAnchor(null);
        }else{
            response.setNextPageAnchor(((Integer)(pageOffSet+cmd.getPageSize())).longValue());
            billDTOS.remove(billDTOS.size()-1);
        }
        response.setBillDTOS(billDTOS);
        return response;
    }

    @Override
    public void selectNotice(SelectedNoticeCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(cmd.getOwnerType(),cmd.getOwnerId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        //张江高科的厂商的接口，还未写
        List<NoticeInfo> noticeInfos = handler.listNoticeInfoByBillId(cmd.getBillIds());
        if(noticeInfos.size()<1) return;
        List<Long> uids = new ArrayList<>();
        //"{targetName}先生/女士，您好，您的账单已出，应付{amount1}元，待缴{amount2}元，下载"{appName} APP"可及时查看账单并支持在线付款,还可体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。"
        //短信： 54	物业费催缴	王闻天	{1-> targetName}先生/女士，您好，您的物业账单已出，账期{2 dateStr}，使用"{3 appName} APP"可及时查看账单并支持在线付款。
        for(int i = 0; i<noticeInfos.size(); i++) {
            NoticeInfo noticeInfo = noticeInfos.get(i);
            //收集短信的信息
            List<Tuple<String, Object>> variables = new ArrayList<>();
            smsProvider.addToTupleList(variables,"targetName",noticeInfo.getTargetName());
            //模板改了，所以这个也要改
            smsProvider.addToTupleList(variables,"dateStr","2017-05");
//            smsProvider.addToTupleList(variables,"amount2",noticeInfo.getAmountOwed());
            smsProvider.addToTupleList(variables,"appName",noticeInfo.getAppName());
            String phoneNums = noticeInfo.getPhoneNum();
            String templateLocale = UserContext.current().getUser().getLocale();
            smsProvider.sendSms(999971, phoneNums, SmsTemplateCode.SCOPE, SmsTemplateCode.PAYMENT_NOTICE_CODE, templateLocale, variables);
            //客户在系统内，把需要推送的uid放在list中
            Long targetId = noticeInfo.getTargetId();
            if(targetId!=null && targetId!=0l){
                if (noticeInfo.getTargetType().equals("eh_user")) {
                    uids.add(noticeInfo.getTargetId());
                } else if(noticeInfo.getTargetType().equals("eh_organization")) {
                    ListServiceModuleAdministratorsCommand tempCmd = new ListServiceModuleAdministratorsCommand();
                    tempCmd.setOwnerId(cmd.getOwnerId());
                    tempCmd.setOwnerType(cmd.getOwnerType());
                    tempCmd.setOrganizationId(noticeInfo.getTargetId());
                    //企业超管是1005？不是1001
                    List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(tempCmd);
                    for(int j =0 ; i < organizationContactDTOS.size(); i++){
                        uids.add(organizationContactDTOS.get(0).getId());
                    }
                }
            }
        }
        //测试闫杨的账号
//        uids.add(238716l);
        //对所有的符合推送资格的用户推送账单已出信息
        for(int k = 0; k < uids.size() ; k++) {
            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uids.get(k).toString()));
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
            //insert into eh_locale_template values(@xx+1,user_notification,3?,zh_CN,物业账单通知用户,text,999985)
            //这个逻辑是张江高科的， 但为了测试统一，999971先改为999985用华润测试
            Map<String,Object> map = new HashMap<>();
            User targetUser = userProvider.findUserById(uids.get(k));
            map.put("targetName",targetUser.getNickName());
            // targetName没有被替换
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(UserContext.getCurrentNamespaceId(),UserNotificationTemplateCode.SCOPE, UserNotificationTemplateCode.USER_PAYMENT_NOTICE, UserContext.current().getUser().getLocale(), map, "");
            notifyTextForApplicant.replace("targetName","南宫");
            messageDto.setBody(notifyTextForApplicant);
            messageDto.setMetaAppId(AppConstants.APPID_USER);
            if(!notifyTextForApplicant.trim().equals("")){
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                        uids.get(k).toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
            }
        }
        //催缴次数加1
        assetProvider.increaseNoticeTime(cmd.getBillIds());

    }

    @Override
    public ShowBillForClientDTO showBillForClient(ClientIdentityCommand cmd) {
        //app用户的权限还未判断，是否可以查看账单
        AssetVendor assetVendor = checkAssetVendor(cmd.getOwnerType(),cmd.getOwnerId());
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendorName);
        return handler.showBillForClient(cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetType(),cmd.getTargetId(),cmd.getBillGroupId(),cmd.getIsOnlyOwedBill(),cmd.getContractNum());
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(BillIdCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(cmd.getOwnerType(),cmd.getOwnerId());
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendorName);
        return handler.getBillDetailForClient(cmd.getBillId());
    }

    @Override
    public List<ListBillGroupsDTO> listBillGroups(OwnerIdentityCommand cmd) {
        return assetProvider.listBillGroups(cmd.getOwnerId(),cmd.getOwnerType());
    }

    @Override
    public ShowCreateBillDTO showCreateBill(BillGroupIdCommand cmd) {
        return assetProvider.showCreateBill(cmd.getBillGroupId());
    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(ListBillDetailOnDateChangeCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(cmd.getOwnerType(),cmd.getOwnerId());
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendorName);
        if(cmd.getTargetType().equals("eh_user")) {
            cmd.setTargetId(UserContext.currentUserId());
        }
        return handler.listBillDetailOnDateChange(cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetType(),cmd.getTargetId(),cmd.getDateStr());
    }

    @Override
    public ListBillsDTO createBill(CreateBillCommand cmd) {
        if(!cmd.getOwnerType().equals("community")){
            throw new RuntimeException("保存账单不在一个园区");
        }
//        List<AddressIdAndName> addressByPossibleName = addressProvider.findAddressByPossibleName(UserContext.getCurrentNamespaceId(), cmd.getOwnerId(), cmd.getBuildingName(), cmd.getApartmentName());
        return assetProvider.creatPropertyBill(cmd.getBillGroupDTO(),cmd.getDateStr(),cmd.getIsSettled(),cmd.getNoticeTel(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetName(),cmd.getTargetId(),cmd.getTargetType(),cmd.getContractNum());
    }

    @Override
    public void OneKeyNotice(OneKeyNoticeCommand cmd) {
        ListBillsCommand convertedCmd = ConvertHelper.convert(cmd, ListBillsCommand.class);
        convertedCmd.setPageAnchor(0l);
        convertedCmd.setPageSize(999999);
        convertedCmd.setStatus((byte)1);
        convertedCmd.setBillStatus((byte)0);
        ListBillsResponse convertedResponse = listBills(convertedCmd);
        List<ListBillsDTO> listBillsDTOS = convertedResponse.getListBillsDTOS();
        Map<OwnerEntity,List<Long>> noticeObjects = new HashMap<>();
        for(int i = 0; i < listBillsDTOS.size(); i ++) {
            ListBillsDTO convertedDto = listBillsDTOS.get(i);
            OwnerEntity entity = new OwnerEntity();
            entity.setOwnerId(convertedDto.getOwnerId());
            entity.setOwnerType(convertedDto.getOwnerType());
            if(noticeObjects.containsKey(entity)){
                noticeObjects.get(entity).add(convertedDto.getBillId());
            }else{
                List<Long> ids = new ArrayList<>();
                ids.add(convertedDto.getBillId());
                noticeObjects.put(entity,ids);
            }
        }
        for(Map.Entry<OwnerEntity,List<Long>> entry : noticeObjects.entrySet()){
            SelectedNoticeCommand requestCmd = new SelectedNoticeCommand();
            requestCmd.setOwnerType(entry.getKey().getOwnerType());
            requestCmd.setOwnerId(entry.getKey().getOwnerId());
            requestCmd.setBillIds(entry.getValue());
            selectNotice(requestCmd);
        }
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
    public List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd) {
        List<BillStaticsDTO> list = new ArrayList<>();
        Byte dimension = cmd.getDimension();
        if(dimension==1){
            list = assetProvider.listBillStaticsByDateStrs(cmd.getBeginLimit(),cmd.getEndLimit(),cmd.getOwnerId(),cmd.getOwnerType());
        }else if(dimension==2){
            list = assetProvider.listBillStaticsByChargingItems(cmd.getOwnerType(),cmd.getOwnerId());
        }else if(dimension==3){
            list = assetProvider.listBillStaticsByCommunities(UserContext.getCurrentNamespaceId());
        }
        return list;
    }

    @Override
    public void modifyBillStatus(BillIdCommand cmd) {
        assetProvider.modifyBillStatus(cmd.getBillId());
    }

    @Override
    public void exportPaymentBills(ListBillsCommand cmd, HttpServletResponse response) {
        cmd.setPageSize(100000);
        ListBillsResponse bills = listBills(cmd);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String fileName = "bill"+"/"+year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second;
        List<ListBillsDTO> dtos = bills.getListBillsDTOS();

        List<exportPaymentBillsDetail> dataList = new ArrayList<>();
        //组装datalist来确定propertyNames的值

        for(int i = 0; i < dtos.size(); i++) {
            ListBillsDTO dto = dtos.get(i);
            exportPaymentBillsDetail detail = new exportPaymentBillsDetail();
            detail.setAmountOwed(dto.getAmountOwed().toString());
            detail.setAmountReceivable(dto.getAmountReceivable().toString());
            detail.setAmountReceived(dto.getAmountReceived().toString());
            detail.setApartmentName(dto.getApartmentName());
            detail.setBillGroupName(dto.getBillGroupName());
            detail.setBuildingName(dto.getBuildingName());
            detail.setNoticeTel(dto.getNoticeTel());
            detail.setNoticeTimes(String.valueOf(dto.getNoticeTimes()));
            detail.setStatus(dto.getBillStatus()==1?"已缴":"待缴");
            detail.setTargetName(dto.getTargetName());
            detail.setDateStr(dto.getDateStr());
            dataList.add(detail);
        }
        String[] propertyNames = {"dateStr","billGroupName","targetName","buildingName","apartmentName","noticeTel","amountReceivable","amountReceived","amountOwed","status","noticeTimes"};
//        Field[] declaredFields = ListBillsDTO.class.getDeclaredFields();
//        String[] propertyNames = new String[declaredFields.length];
        String[] titleName ={"账期","账单组","客户名称","楼栋","门牌","催缴手机号","应收(元)","已收(元)","欠收(元)","缴费状态","催缴次数"};
        int[] titleSize = {20,20,20,20,20,20,20,20,20,20,20};
//        for(int i = 0; i < declaredFields.length; i++){
//            propertyNames[i] = declaredFields[i].getName();
//        }
        ExcelUtils excel = new ExcelUtils(response,fileName,"sheet1");
        excel.writeExcel(propertyNames,titleName,titleSize,dataList);
    }

    @Override
    public List<ListChargingItemsDTO> listChargingItems(OwnerIdentityCommand cmd) {
        return assetProvider.listChargingItems(cmd.getOwnerType(),cmd.getOwnerId());
    }

    @Override
    public List<ListChargingStandardsDTO> listChargingStandards(ListChargingStandardsCommand cmd) {
        return assetProvider.listChargingStandards(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getChargingItemId());
    }

    @Override
    public void modifyNotSettledBill(ModifyNotSettledBillCommand cmd) {
        assetProvider.modifyNotSettledBill(cmd.getBillId(),cmd.getBillGroupDTO(),cmd.getTargetType(),cmd.getTargetId(),cmd.getTargetName());
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
            response.setNextPageAnchor(0l);
        }else{
            response.setNextPageAnchor(((Integer)(pageOffSet+cmd.getPageSize())).longValue());
            list.remove(list.size()-1);
        }
        response.setBillDTOS(list);
        return response;
    }

    @Override
    public void deleteBill(BillIdCommand cmd) {
        assetProvider.deleteBill(cmd.getBillId());
    }

    @Override
    public void deleteBillItem(BillItemIdCommand cmd) {
        assetProvider.deleteBillItem(cmd.getBillItemId());
    }

    @Override
    public void deletExemptionItem(ExemptionItemIdCommand cmd) {
        assetProvider.deletExemptionItem(cmd.getExemptionItemId());
    }

    @Override
    public PaymentExpectanciesResponse paymentExpectancies(PaymentExpectanciesCommand cmd) {
        //calculate the details of payment expectancies
        PaymentExpectanciesResponse response = new PaymentExpectanciesResponse();
        List<PaymentExpectancyDTO> dtos = new ArrayList<>();
        List<FeeRules> feesRules = cmd.getFeesRules();
        HashMap<BillIdentity,PaymentBills> map = new HashMap<>();
        String json = "";
        List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
        List<EhPaymentBills> billList = new ArrayList<>();
        List<EhPaymentContractReceiver> contractDateList = new ArrayList<>();
        //查一下出账单日
        for(int i = 0; i < feesRules.size(); i++) {
            List<PaymentExpectancyDTO> dtos1 = new ArrayList<>();
            FeeRules rule = feesRules.get(i);
//            List<String> var1 = rule.getPropertyName();
            List<ContractProperty> var1 = rule.getProperties();
            List<VariableIdAndValue> variableIdAndValueList = assetProvider.findPreInjectedVariablesForCal(rule.getChargingStandardId());
            List<VariableIdAndValue> var2 = rule.getVariableIdAndValueList();
            coverVariables(var2,variableIdAndValueList);
            String formula = assetProvider.findFormulaByChargingStandardId(rule.getChargingStandardId());
            String chargingItemName = assetProvider.findChargingItemNameById(rule.getChargingItemId());
            Byte billingCycle = assetProvider.findBillyCycleById(rule.getChargingStandardId());
            List<Object> billConf = assetProvider.getBillDayAndCycleByChargingItemId(rule.getChargingStandardId(),rule.getChargingItemId(),cmd.getOwnerType(),cmd.getOwnerId());
            Integer billDay = (Integer)billConf.get(0);
            Byte balanceType = (Byte)billConf.get(1);
            PaymentBillGroupRule groupRule = assetProvider.getBillGroupRule(rule.getChargingStandardId(),rule.getChargingStandardId(),cmd.getOwnerType(),cmd.getOwnerId());
            Long billGroupId = groupRule.getBillGroupId();
            for(int j = 0; j < var1.size(); j ++){
                List<PaymentExpectancyDTO> dtos2 = new ArrayList<>();
                ContractProperty property = var1.get(j);
                //如果收费项目的计费周期是按照固定日期，以合同开始日为计费周期
                if(billingCycle==AssetPaymentStrings.CONTRACT_BEGIN_DATE_AS_FIXED_DAY_OF_MONTH){
                    FixedAtContractStartHandler(dtos1, rule, variableIdAndValueList, formula, chargingItemName, billDay, dtos2, property);
                }
                //自然月的计费方式
                if(billingCycle == AssetPaymentStrings.NATRUAL_MONTH){
                    NaturalMonthHandler(dtos1, rule, variableIdAndValueList, formula, chargingItemName, billDay, dtos2, property);
                }
                // redis的这个会rollback吗？不会当然不影响功能
                long nextBillItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILL_ITEMS.getClass()), dtos2.size());
                long currentBillItemSeq = nextBillItemBlock - dtos2.size() + 1;
                if(currentBillItemSeq == 0){
                    currentBillItemSeq = currentBillItemSeq+1;
                    this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILL_ITEMS.getClass()));
                }
                for(int g = 0; g< dtos2.size(); g++) {
                    PaymentExpectancyDTO dto = dtos2.get(g);
                    BillIdentity identity = new BillIdentity();
                    identity.setBillGroupId(groupRule.getBillGroupId());
                    identity.setContract(cmd.getContractNum());
                    String dateStr = dto.getDateStrBegin().substring(0,dto.getDateStrBegin().lastIndexOf("-"));
                    identity.setDateStr(dateStr);
                    // define a billId for billItem and bill to set
                    long nextBillId = 0l;
                    if(map.containsKey(identity)){
                        nextBillId = map.get(identity).getId();
                    }else{
                        nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILLS.getClass()));
                        if(nextBillId == 0){
                            nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILLS.getClass()));
                        }
                    }
                    // build a billItem
                    PaymentBillItems item = new PaymentBillItems();
                    item.setAddressId(property.getAddressId());
                    item.setPropertyIdentifer(property.getPropertyName());
                    item.setAmountOwed(dto.getAmountReceivable());
                    item.setAmountReceivable(dto.getAmountReceivable());
                    item.setAmountReceived(new BigDecimal("0"));
                    item.setBillGroupId(billGroupId);
                    item.setBillId(nextBillId);
                    item.setChargingItemName(groupRule.getChargingItemName());
                    item.setChargingItemsId(rule.getChargingItemId());
                    item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    item.setCreatorUid(UserContext.currentUserId());
                    item.setDateStr(dateStr);
                    item.setDateStrBegin(dto.getDateStrBegin());
                    item.setDateStrEnd(dto.getDateStrEnd());
                    item.setDateStrDue(dto.getDueDateStr());
                    item.setId(currentBillItemSeq);
                    currentBillItemSeq += 1;
                    item.setNamespaceId(cmd.getNamesapceId());
                    item.setOwnerType(cmd.getOwnerType());
                    item.setOwnerId(cmd.getOwnerId());
                    item.setTargetType(cmd.getTargetType());
                    item.setTargetId(cmd.getTargetId());
                    item.setContractId(cmd.getContractId());
                    item.setContractNum(cmd.getContractNum());
                    item.setTargetName(cmd.getTargetName());
                    item.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    billItemsList.add(item);
                    if(balanceType == AssetPaymentStrings.BALANCE_ON_MONTH) {
                        // create a new bill or update a bean according to whether the corresponding contract bill exists
                        if(map.containsKey(identity)){
                            PaymentBills bill = map.get(identity);
                            bill.setAmountReceivable(bill.getAmountReceivable().add(item.getAmountReceivable()));
                            bill.setAmountOwed(bill.getAmountOwed().add(item.getAmountOwed()));
                            bill.setAmountReceived(bill.getAmountReceived().add(item.getAmountReceived()));
                        }else{
                            PaymentBills newBill = new PaymentBills();
                            //账单只存第一个资产信息，收费项目中对应多个资产,根据地址查询账单
                            //一是直接查账单表，二是确定用户信息，拿到targetId
                            newBill.setAddressId(property.getAddressId());
                            newBill.setBuildingName(property.getBuldingName());
                            newBill.setApartmentName(property.getApartmentName());
                            newBill.setAmountOwed(item.getAmountOwed());
                            newBill.setAmountReceivable(item.getAmountReceivable());
                            newBill.setAmountReceived(item.getAmountReceived());
                            newBill.setAmountSupplement(new BigDecimal("0"));
                            newBill.setAmountExemption(new BigDecimal("0"));
                            newBill.setBillGroupId(billGroupId);
                            // identity中最小的那个设置为datestr
                            newBill.setDateStr(item.getDateStr());
                            newBill.setId(nextBillId);
                            newBill.setNamespaceId(cmd.getNamesapceId());
                            newBill.setNoticetel(cmd.getNoticeTel());
                            newBill.setOwnerId(cmd.getOwnerId());
                            newBill.setContractId(cmd.getContractId());
                            newBill.setContractNum(cmd.getContractNum());
                            newBill.setTargetName(cmd.getTargetName());
                            newBill.setOwnerType(cmd.getOwnerType());
                            newBill.setTargetType(cmd.getTargetType());
                            newBill.setTargetId(cmd.getTargetId());
                            newBill.setCreatTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                            newBill.setCreatorId(UserContext.currentUserId());
                            newBill.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                            newBill.setNoticeTimes(0);
                            newBill.setStatus((byte)0);
                            newBill.setSwitch((byte)3);
                            map.put(identity,newBill);
                        }
                        //if the billing cycle is on quarter or year, just change the way how the billIdentity defines that muliti bills should be merged as one or be independently
                    }else{
                        throw new RuntimeException("暂只支持按月计费，请联系左邻在账单组设置");
                    }
                }

            }
            dtos.addAll(dtos1);
            // contract receiver added with status being set as 0 i.e. inactive
            Gson gson = new Gson();
            Map<String,String> variableMap = new HashMap<>();
            for(int k = 0; k< variableIdAndValueList.size(); k++){
                VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(k);
                variableMap.put((String)variableIdAndValue.getVariableId(),((BigDecimal)variableIdAndValue.getVariableValue()).toString());
            }
            json = gson.toJson(variableMap, Map.class);
            PaymentContractReceiver entity = new PaymentContractReceiver();
            StringBuilder addressIds = new StringBuilder();
            for(int l =0 ; l < var1.size(); l++) {
                Long addressId = var1.get(l).getAddressId();
                if(addressId!=null){
                    if(l == var1.size()-1){
                        addressIds.append(var1.get(l).getPropertyName());
                        break;
                    }
                    addressIds.append(var1.get(l).getPropertyName()+",");
                }
            }
//            entity.setApartmentName(property.getApartmentName());
//            entity.setBuildingName(property.getBuldingName());
            entity.setAddressIdsJson(addressIds.toString());
            entity.setContractId(cmd.getContractId());
            entity.setContractNum(cmd.getContractNum());
            entity.setEhPaymentChargingItemId(rule.getChargingItemId());
            entity.setEhPaymentChargingStandardId(rule.getChargingStandardId());
            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_CONTRACT_RECEIVER.getClass()));
            if(nextSequence==0l){
                nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_CONTRACT_RECEIVER.getClass()));
            }
            entity.setId(nextSequence);
            entity.setNamespaceId(cmd.getNamesapceId());
            entity.setNoticeTel(cmd.getNoticeTel());
            entity.setOwnerId(cmd.getOwnerId());
            entity.setOwnerType(cmd.getOwnerType());
            entity.setStatus((byte)0);
            entity.setTargetId(cmd.getTargetId());
            entity.setTargetType(cmd.getTargetType());
            entity.setTargetName(cmd.getTargetName());
            entity.setVariablesJsonString(json);
            contractDateList.add(entity);
        }
        for(Map.Entry entry : map.entrySet()){
            billList.add((PaymentBills)entry.getValue());
        }
        this.dbProvider.execute((TransactionStatus status) -> {
            assetProvider.saveBillItems(billItemsList);
            assetProvider.saveBills(billList);
            assetProvider.saveContractVariables(contractDateList);
            return null;
        });
        response.setList(dtos);
        return response;
    }

    private void NaturalMonthHandler(List<PaymentExpectancyDTO> dtos1, FeeRules rule, List<VariableIdAndValue> variableIdAndValueList, String formula, String chargingItemName, Integer billDay, List<PaymentExpectancyDTO> dtos2, ContractProperty property) {
        String propertyName = property.getPropertyName();
        Date dateStrBegin = rule.getDateStrBegin();
        Date dateStrEnd = rule.getDateStrEnd();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        // c1 is the start of the contract
        c1.setTime(dateStrBegin);
        // c2 is the end date of the contract
        c2.setTime(dateStrEnd);
        Calendar c3 = Calendar.getInstance();
        // c3 starts as the begin of the contract
        c3.setTime(dateStrBegin);
        //define duration for cal
        float duration = 0;
        //define the end of the date the calculation should take as multiply
        Calendar c5 = Calendar.getInstance();
        //calculate the per cent of month from c1 to the end of the month c1 is at
        duration = ((float)c1.getActualMaximum(Calendar.DAY_OF_MONTH) - (float)c1.get(Calendar.DAY_OF_MONTH))/(float)c1.getActualMaximum(Calendar.DAY_OF_MONTH);
        BigDecimal tempDuration = new BigDecimal(duration);
        tempDuration = tempDuration.setScale(2,BigDecimal.ROUND_CEILING);
        if(duration != 0){
            c5.setTime(c3.getTime());
            c5.set(Calendar.DAY_OF_MONTH,c5.getActualMaximum(Calendar.DAY_OF_MONTH));
            addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c5, c3, tempDuration.floatValue(),billDay);
        }
        c3.set(Calendar.MONTH,c3.get(Calendar.MONTH)+1);
        c3.set(Calendar.DAY_OF_MONTH,c3.getActualMinimum(Calendar.DAY_OF_MONTH));
        Calendar c4 = Calendar.getInstance();
        c4.setTime(c3.getTime());

        while(c4.compareTo(c2) == -1 || c4.compareTo(c2) == 0) {
            //each month exactly
            duration = 1;
            c5.setTime(c3.getTime());
            c5.set(Calendar.DAY_OF_MONTH,c5.getActualMaximum(Calendar.DAY_OF_MONTH));
            addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c5, c3, duration,billDay);
            c3.set(Calendar.MONTH,c3.get(Calendar.MONTH)+1);
            c3.set(Calendar.DAY_OF_MONTH,c3.getActualMinimum(Calendar.DAY_OF_MONTH));
            c4.set(Calendar.MONTH,c4.get(Calendar.MONTH)+1);
            c4.set(Calendar.DAY_OF_MONTH,c4.getActualMinimum(Calendar.DAY_OF_MONTH));
        }
        if(c4.compareTo(c2) < 0){
            //less than one month
            duration = c2.get(Calendar.DAY_OF_MONTH)-c2.getActualMinimum(Calendar.DAY_OF_MONTH)/c2.getActualMaximum(Calendar.DAY_OF_MONTH);
            addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c2, c3, duration,billDay);
        }
        dtos1.addAll(dtos2);
    }

    private void FixedAtContractStartHandler(List<PaymentExpectancyDTO> dtos1, FeeRules rule, List<VariableIdAndValue> variableIdAndValueList, String formula, String chargingItemName, Integer billDay, List<PaymentExpectancyDTO> dtos2, ContractProperty property) {
        String propertyName = property.getPropertyName();
        Date dateStrBegin = rule.getDateStrBegin();
        Date dateStrEnd = rule.getDateStrEnd();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        // c1 is the start of the contract
        c1.setTime(dateStrBegin);
        // c2 is the end date of the contract
        c2.setTime(dateStrEnd);
        Calendar c3 = Calendar.getInstance();
        // c3 starts as the begin of the contract
        c3.setTime(dateStrBegin);

        c3.set(Calendar.MONTH,c3.get(Calendar.MONTH)-1);
        Calendar c4 = Calendar.getInstance();
        c4.setTime(c3.getTime());
        c4.set(Calendar.MONTH,c4.get(Calendar.MONTH)+1);

        if(c4.compareTo(c2) == 0){
            // one month
            // get dto and add to dtos
            float duration = 1;
            c3.set(Calendar.MONTH,c3.get(Calendar.MONTH)-1);
            addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c2, c3, duration,billDay);
        }else{
            while(c4.compareTo(c2) != 1) {
                //each month
                float duration = 1;
                Calendar c5 = Calendar.getInstance();
                c5.setTime(c3.getTime());
                c5.set(Calendar.DAY_OF_MONTH,c5.getActualMaximum(Calendar.DAY_OF_MONTH));
                addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c5, c3, duration,billDay);
                c3.set(Calendar.MONTH,c3.get(Calendar.MONTH)+1);
                c4.set(Calendar.MONTH,c4.get(Calendar.MONTH)+1);
            }
            if(c4.compareTo(c2) != 0){
                //less than one month
                int c2day = c2.get(Calendar.DAY_OF_MONTH);
                int c3day = c3.get(Calendar.DAY_OF_MONTH);
                int distance = 0;
                if(c2day>c3day){
                    distance = c2day+c3day;
                }else{
                    distance = c3.getActualMaximum(Calendar.DAY_OF_MONTH)-c2day+c2day;
                }
                float duration = distance/c4.getActualMaximum(Calendar.DAY_OF_MONTH);
                addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c2, c3, duration,billDay);
            }
        }
        dtos1.addAll(dtos2);
    }

    @Override
    public void generateBillsOnContractSigned(String contractNum) {
        //保存合同，改变状态
//        List<PaymentContractReceiver> materials = assetProvider.findContractReceiverByContractNumAndTimeLimit(contractNum);
//        for(int i = 0; i < materials.size(); i++) {
//            PaymentContractReceiver p = materials.get(i);
//
//        }
//        String variablesJsonString = m_1.getVariablesJsonString();
//        String formula = assetProvider.findFormulaByChargingStandardId();
//        calculateFee()
    }

    @Override
    public void upodateBillStatusOnContractStatusChange(Long contractId,String targetStatus) {
        if(targetStatus.equals(AssetPaymentStrings.CONTRACT_SAVE)){
            assetProvider.changeBillStatusOnContractSaved(contractId);
        }else if(targetStatus.equals(AssetPaymentStrings.CONTRACT_CANCEL)){
            assetProvider.deleteContractPayment(contractId);
        }
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
            response.setNextPageOffset(cmd.getPageOffset());
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
    public FindUserInfoForPaymentDTO findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        FindUserInfoForPaymentDTO response = new FindUserInfoForPaymentDTO();
        String targeType = cmd.getTargeType();
        ListCustomerContractsCommand cmd1 = new ListCustomerContractsCommand();
        cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
        cmd1.setTargetId(cmd.getTargetId());
        if(targeType.equals(AssetPaymentStrings.EH_USER)){
            cmd1.setTargetId(UserContext.currentUserId());
            cmd1.setTargetType(CustomerType.INDIVIDUAL.getCode());
        }else if(targeType.equals(AssetPaymentStrings.EH_ORGANIZATION)){
            cmd1.setCommunityId(cmd.getTargetId());
            cmd1.setTargetType(CustomerType.ENTERPRISE.getCode());
        }else{
            throw new RuntimeException("用户类型错误");
        }
        List<ContractDTO> dtos = contractService.listCustomerContracts(cmd1);
        if(dtos!= null && dtos.size() > 0){
            ContractDTO dto = dtos.get(0);
            if(dtos.size()>1){
                response.setHasMoreContract((byte)1);
            }else{
                response.setHasMoreContract((byte)0);
            }
            List<BuildingApartmentDTO> buildings = dto.getBuildings();
            List<String> addressNames = new ArrayList<>();
            Double areaSizeSum = 0d;
            if(buildings!=null){
                for(int i = 0; i < buildings.size(); i++){
                    String addressName;
                    BuildingApartmentDTO building = buildings.get(i);
                    addressName = building.getBuildingName()+building.getApartmentName();
                    addressNames.add(addressName);
                    areaSizeSum += building.getAreaSize();
                }
                response.setAddressNames(addressNames);
                response.setAreaSizesSum(areaSizeSum);
            }
            response.setContractNum(dto.getContractNumber());
            response.setTargetName(dto.getOrganizationName());
        }
        return response;
    }

    @Override
    public void updateBillsToSettled(UpdateBillsToSettled cmd) {
        assetProvider.updateBillsToSettled(cmd.getContractId(),cmd.getOwnerType(),cmd.getOwnerId());
    }

    private void coverVariables(List<VariableIdAndValue> var1, List<VariableIdAndValue> var2) {
        for(int i = 0 ; i < var1.size(); i++){
            VariableIdAndValue v1 = var1.get(i);
            String id1 = (String)v1.getVariableId();
            for(int j = 0; j< var2.size(); j++){
                VariableIdAndValue v2 = var2.get(j);
                String id2 = (String)v2.getVariableId();
                if(id1.equals(id2)){
                    v2.setVariableValue(v1.getVariableValue());
                }

            }
        }
    }

    private void addFeeDTO(List<PaymentExpectancyDTO> dtos, String formula, String chargingItemName, String propertyName, List<VariableIdAndValue> variableIdAndValueList, Calendar c5, Calendar c3, float duration,Integer billDay) {
        PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
        BigDecimal amountReceivable = calculateFee(variableIdAndValueList,formula,duration);
        dto.setAmountReceivable(amountReceivable);
        dto.setChargingItemName(chargingItemName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dto.setDateStrBegin(sdf.format(c3.getTime()));
//        dto.setDateStrEnd(sdf.format(c2.getTime()));
        Calendar c6 = Calendar.getInstance();
        c6.setTime(c3.getTime());
        c6.set(Calendar.MONTH,c3.get(Calendar.MONTH)+1);
        c6.set(Calendar.DAY_OF_MONTH,billDay);
        dto.setDueDateStr(sdf.format(c6.getTime()));
        dto.setDateStrEnd(sdf.format(c5.getTime()));
        dto.setPropertyIdentifier(propertyName);
        dtos.add(dto);
    }

    private BigDecimal calculateFee(List<VariableIdAndValue> variableIdAndValueList, String formula, float duration) {
        Gson gson = new Gson();
        HashMap<String,String> map = new HashMap();
        for(int i = 0; i < variableIdAndValueList.size(); i++){
            VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
            map.put((String)variableIdAndValue.getVariableId(),((BigDecimal)variableIdAndValue.getVariableValue()).toString());
        }
        for(Map.Entry<String,String> entry : map.entrySet()){
            formula = formula.replace(entry.getKey(),entry.getValue());
            formula += "*"+duration;
        }
        BigDecimal response = CalculatorUtil.arithmetic(formula);
        response.setScale(2);
        return response;
    }
//    @Scheduled(cron = "0 0 23 * * ?")
//    @Override
//    public void synchronizeZJGKBill() {
//        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag())==RunningFlag.TRUE){
//            coordinationProvider.getNamedLock(CoordinationLocks.BILL_SYNC.getCode()).tryEnter(() ->{
//                //调取张江高科的接口获取菜单，可是为什么要同步呢？
//            });
//        }
//    }

    private void processLatestSelectedOrganization(List<ListOrganizationsByPmAdminDTO> dtoList) {
        CacheAccessor accessor = cacheProvider.getCacheAccessor(null);
        String key = String.format("pmbill:kexing:latest-selected-organization: %s:%s", UserContext.getCurrentNamespaceId(), UserContext.current().getUser().getId());
        Long latestSelectedOrganizationId = accessor.get(key);
        if (latestSelectedOrganizationId != null) {
            dtoList.parallelStream()
                    .filter(r -> Objects.equals(r.getOrganizationId(), latestSelectedOrganizationId))
                    .forEach(r -> r.setLatestSelected(TrueOrFalseFlag.TRUE.getCode()));
        }
    }

    private ListOrganizationsByPmAdminDTO toOrganizationsByPmAdminDTO(OrganizationDTO organization) {
        ListOrganizationsByPmAdminDTO dto = new ListOrganizationsByPmAdminDTO();
        dto.setOrganizationId(organization.getId());
        dto.setOrganizationName(organization.getName());
        dto.setAddresses(this.getOrganizationAddresses(organization.getId()));
        dto.setAreaSize(0.0);
        if(dto.getAddresses() != null && dto.getAddresses().size() > 0) {
            dto.getAddresses().forEach(address -> {
                if(address != null && address.getAreaSize() != null) {
                    dto.setAreaSize(address.getAreaSize()+dto.getAreaSize());
                }
            });

        }

        return dto;
    }

    private List<AddressDTO> getOrganizationAddresses(Long organizationId) {
        List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(organizationId);
        if (organizationAddresses != null) {
            return organizationAddresses.stream().map(r -> {
                Address address = addressProvider.findAddressById(r.getAddressId());
                return ConvertHelper.convert(address, AddressDTO.class);
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

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

    private AssetVendor checkAssetVendor(String targetType,Long targetId){
        if(null == targetId) {
            LOGGER.error("checkAssetVendor targetId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "checkAssetVendor targetId cannot be null.");
        }

        if(StringUtils.isBlank(targetType)) {
            LOGGER.error("checkAssetVendor targetType cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "checkAssetVendor targetType cannot be null.");
        }

        AssetVendor assetVendor = assetProvider.findAssetVendorByOwner(targetType, targetId);
        if(null == assetVendor) {
            LOGGER.error("assetVendor not found, assetVendor targetType={}, targetId={}", targetType, targetId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "assetVendor not found");
        }

        return assetVendor;
    }

    private AssetVendorHandler getAssetVendorHandler(String vendorName) {
        AssetVendorHandler handler = null;

        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = AssetVendorHandler.ASSET_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }

        return handler;
    }

    private void putLatestSelectedOrganizationToCache(Long organizationId) {
        CacheAccessor accessor = cacheProvider.getCacheAccessor(null);
        String key = String.format("pmbill:kexing:latest-selected-organization: %s:%s", UserContext.getCurrentNamespaceId(), UserContext.current().getUser().getId());
        accessor.put(key, organizationId);
    }

    @Override
    public ListSimpleAssetBillsResponse listSimpleAssetBills(ListSimpleAssetBillsCommand cmd) {

        AssetVendor assetVendor = checkAssetVendor(cmd.getTargetType(), cmd.getTargetId());

        String vendor = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendor);
        putLatestSelectedOrganizationToCache(cmd.getOrganizationId());
        ListSimpleAssetBillsResponse response = handler.listSimpleAssetBills(cmd.getOwnerId(), cmd.getOwnerType(),
                cmd.getTargetId(), cmd.getTargetType(), cmd.getOrganizationId(),  cmd.getAddressId(), cmd.getTenant(), cmd.getStatus(),
                cmd.getStartTime(), cmd.getEndTime(), cmd.getPageAnchor(), cmd.getPageSize());
        return response;
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
        int i = 1;
        for (SimpleAssetBillDTO dto : dtos ) {
            this.setNewAssetBillsBookRow(sheet, dto, i++);
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

    private void setNewAssetBillsBookRow(Sheet sheet ,SimpleAssetBillDTO dto, int id){
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        int i = -1;
//        row.createCell(++i).setCellValue(dto.getId());
        row.createCell(++i).setCellValue(id);
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
        ListAssetBillTemplateCommand command = ConvertHelper.convert(cmd, ListAssetBillTemplateCommand.class);
        Map<Long,List<Field>> fieldMap = getTemplateFields(command);
        Long templateVersion = 0L;
        List<Field> fields = new ArrayList<Field>();
        if(fieldMap.keySet().size() > 0) {
        	templateVersion = fieldMap.keySet().iterator().next();
        	fields = fieldMap.get(templateVersion);
        }
        

        ImportDataResponse importDataResponse = new ImportDataResponse();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if(null == resultList || resultList.isEmpty()){
                LOGGER.error("File content is empty。userId="+userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        "File content is empty");
            }
            LOGGER.debug("Start import data...,total:" + resultList.size());

            List<String> errorDataLogs = importAssetBills(cmd, convertToStrList(resultList), fields, userId, templateVersion);


            LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
            if(null == errorDataLogs || errorDataLogs.isEmpty()){
                LOGGER.debug("Data import all success...");
            }else{
                //记录导入错误日志
                for (String log : errorDataLogs) {
                    LOGGER.error(log);
                }
            }

            importDataResponse.setTotalCount((long)resultList.size()-1);
            importDataResponse.setFailCount((long)errorDataLogs.size());
            importDataResponse.setLogs(errorDataLogs);
        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        return importDataResponse;
    }

    private List<String> convertToStrList(List list) {
		List<String> result = new ArrayList<String>();
		boolean firstRow = true;
		for (Object o : list) {
			if(firstRow){
				firstRow = false;
				continue;
			}
			RowResult r = (RowResult)o;
			StringBuffer sb = new StringBuffer();
			sb.append(r.getA()).append("||");
			sb.append(r.getB()).append("||");
			sb.append(r.getC()).append("||");
			sb.append(r.getD()).append("||");
			sb.append(r.getE()).append("||");
			sb.append(r.getF()).append("||");
			sb.append(r.getG()).append("||");
			sb.append(r.getH()).append("||");
			sb.append(r.getI()).append("||");
			sb.append(r.getJ()).append("||");
			sb.append(r.getK()).append("||");
			sb.append(r.getL()).append("||");
			sb.append(r.getM()).append("||");
			sb.append(r.getN()).append("||");
			sb.append(r.getO()).append("||");
			sb.append(r.getP()).append("||");
			sb.append(r.getQ()).append("||");
			sb.append(r.getR()).append("||");
			sb.append(r.getS()).append("||");
			sb.append(r.getT()).append("||");
			sb.append(r.getU()).append("||");
			sb.append(r.getV()).append("||");
			sb.append(r.getW()).append("||");
			sb.append(r.getX()).append("||");
				
			
			result.add(sb.toString());
		}
		return result;
	}
    
    private List<String> importAssetBills(ImportOwnerCommand cmd, List<String> list, List<Field> fields, Long userId, Long templateVersion){
        List<String> errorDataLogs = new ArrayList<String>();

//        Integer namespaceId = UserContext.getCurrentNamespaceId();
        for (String str : list) {
            String[] s = str.split("\\|\\|");
            dbProvider.execute((TransactionStatus status) -> {
                CreatAssetBillCommand bill = new CreatAssetBillCommand();
                bill.setOwnerId(cmd.getOwnerId());
                bill.setOwnerType(cmd.getOwnerType());
                bill.setTargetId(cmd.getTargetId());
                bill.setTargetType(cmd.getTargetType());
                bill.setTemplateVersion(templateVersion);
                bill.setSource(AssetBillSource.THIRD_PARTY.getCode());
                int i = 0;
                for(Field field : fields) {
                    try {
                    	field.setAccessible(true);
                    	if("class java.sql.Timestamp".equals(field.getType().toString())) {
                    		field.set(bill, covertStrToTimestamp(s[i]));
                    	} else if("class java.math.BigDecimal".equals(field.getType().toString())) {
                    		if(s[i] != null && !"null".equals(s[i])) {
                    			field.set(bill, new BigDecimal(s[i]));
                    		}
                    			
                    	} else {
                    		field.set(bill, field.getType().getConstructor(field.getType()).newInstance(s[i]));
                    	}
                        
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                creatAssetBill(bill);
                return null;
            });
        }
        return errorDataLogs;

    }
    
    private Timestamp covertStrToTimestamp(String str) {
    	String formatStr = configurationProvider.getValue("asset.accountperiod.format", "yyyyMMdd");
    	SimpleDateFormat format = new SimpleDateFormat(formatStr);
    	try {
			Date date=format.parse(str);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }

    //非当月则没有滞纳金 所以数据库里面不加lateFee
    private void getTotalAmount(AssetBill bill) {
    	BigDecimal rental = bill.getRental() == null ? new BigDecimal(0) : bill.getRental();
    	BigDecimal propertyManagementFee = bill.getPropertyManagementFee() == null ? new BigDecimal(0) : bill.getPropertyManagementFee();
    	BigDecimal unitMaintenanceFund = bill.getUnitMaintenanceFund() == null ? new BigDecimal(0) : bill.getUnitMaintenanceFund();
    	BigDecimal privateWaterFee = bill.getPrivateWaterFee() == null ? new BigDecimal(0) : bill.getPrivateWaterFee();
    	BigDecimal privateElectricityFee = bill.getPrivateElectricityFee() == null ? new BigDecimal(0) : bill.getPrivateElectricityFee();
    	BigDecimal publicWaterFee = bill.getPublicWaterFee() == null ? new BigDecimal(0) : bill.getPublicWaterFee();
    	BigDecimal publicElectricityFee = bill.getPublicElectricityFee() == null ? new BigDecimal(0) : bill.getPublicElectricityFee();
    	BigDecimal wasteDisposalFee = bill.getWasteDisposalFee() == null ? new BigDecimal(0) : bill.getWasteDisposalFee();
    	BigDecimal pollutionDischargeFee = bill.getPollutionDischargeFee() == null ? new BigDecimal(0) : bill.getPollutionDischargeFee();
    	BigDecimal extraAirConditionFee = bill.getExtraAirConditionFee() == null ? new BigDecimal(0) : bill.getExtraAirConditionFee();
    	BigDecimal coolingWaterFee = bill.getCoolingWaterFee() == null ? new BigDecimal(0) : bill.getCoolingWaterFee();
    	BigDecimal weakCurrentSlotFee = bill.getWeakCurrentSlotFee() == null ? new BigDecimal(0) : bill.getWeakCurrentSlotFee();
    	BigDecimal depositFromLease = bill.getDepositFromLease() == null ? new BigDecimal(0) : bill.getDepositFromLease();
    	BigDecimal maintenanceFee = bill.getMaintenanceFee() == null ? new BigDecimal(0) : bill.getMaintenanceFee();
    	BigDecimal gasOilProcessFee = bill.getGasOilProcessFee() == null ? new BigDecimal(0) : bill.getGasOilProcessFee();
    	BigDecimal hatchServiceFee = bill.getHatchServiceFee() == null ? new BigDecimal(0) : bill.getHatchServiceFee();
    	BigDecimal pressurizedFee = bill.getPressurizedFee() == null ? new BigDecimal(0) : bill.getPressurizedFee();
    	BigDecimal parkingFee = bill.getParkingFee() == null ? new BigDecimal(0) : bill.getParkingFee();
    	BigDecimal other = bill.getOther() == null ? new BigDecimal(0) : bill.getOther();
        
    	BigDecimal periodAccountAmount = rental.add(propertyManagementFee).add(unitMaintenanceFund)
                .add(privateWaterFee).add(privateElectricityFee).add(publicWaterFee)
                .add(publicElectricityFee).add(wasteDisposalFee).add(pollutionDischargeFee)
                .add(extraAirConditionFee).add(coolingWaterFee).add(weakCurrentSlotFee)
                .add(depositFromLease).add(maintenanceFee).add(gasOilProcessFee)
                .add(hatchServiceFee).add(pressurizedFee).add(parkingFee).add(other);

        bill.setPeriodAccountAmount(periodAccountAmount);
        bill.setPeriodUnpaidAccountAmount(bill.getPeriodAccountAmount());
        
    }

    @Override
    public AssetBillTemplateValueDTO creatAssetBill(CreatAssetBillCommand cmd) {
        AssetBill bill = ConvertHelper.convert(cmd, AssetBill.class);
        bill.setAccountPeriod(new Timestamp(cmd.getAccountPeriod()));
        bill.setSource(AssetBillSource.MANUAL.getCode());
        bill.setCreatorUid(UserContext.current().getUser().getId());
        getTotalAmount(bill);

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        bill.setNamespaceId(namespaceId);
        if(cmd.getAddressId() == null) {
            Address address = addressProvider.findApartmentAddress(namespaceId, cmd.getTargetId(), cmd.getBuildingName(), cmd.getApartmentName());
            cmd.setAddressId(address.getId());
            bill.setAddressId(address.getId());
        }
        Community community = communityProvider.findCommunityById(cmd.getTargetId());
        //园区 查公司表
        if(CommunityType.COMMERCIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {

            OrganizationAddress organizationAddress = organizationProvider.findActiveOrganizationAddressByAddressId(cmd.getAddressId());
            if(organizationAddress != null) {
                bill.setTenantId(organizationAddress.getOrganizationId());
                bill.setTenantType(TenantType.ENTERPRISE.getCode());
            }
        }
        //小区 查家庭
        else if(CommunityType.RESIDENTIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
            Family family = familyProvider.findFamilyByAddressId(cmd.getAddressId());
            if(family != null) {
                bill.setTenantId(family.getId());
                bill.setTenantType(TenantType.FAMILY.getCode());
            }
        }
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
        AssetVendor assetVendor = checkAssetVendor(cmd.getTargetType(), cmd.getTargetId());

        String vendor = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendor);

        AssetBillTemplateValueDTO dto = handler.findAssetBill(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(),
                cmd.getTargetId(), cmd.getTargetType(), cmd.getTemplateVersion(), cmd.getOrganizationId(), cmd.getDateStr(),
                cmd.getTenantId(), cmd.getTenantType(), cmd.getAddressId());
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

        bill.setAccountPeriod(new Timestamp(cmd.getAccountPeriod()));
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
    //只要有未缴账单就推送 但根据租户信息 一个租户在一个园区多月未缴 有多个地址未缴 只推一条

        List<AssetBill> bills = assetProvider.listUnpaidBillsGroupByTenant(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());

        if(bills != null && bills.size() > 0) {
            Integer namespaceId = UserContext.getCurrentNamespaceId();
            LocaleString localeString = localeStringProvider.find(AssetServiceErrorCode.SCOPE, AssetServiceErrorCode.NOTIFY_FEE,
                    "zh_CN");
            String content = localeString.getText();
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("unpaid bills = {}", bills);
            }

            for(AssetBill bill : bills) {
                if (bill.getContactNo() != null) {
                    UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(namespaceId, bill.getContactNo());
                    if (identifier != null) {
                        sendMessageToUser(identifier.getOwnerUid(), content);
                    }
                } else {
                    //没有contactNo的家庭 通知所有家庭成员
                    if (TenantType.FAMILY.equals(TenantType.fromCode(bill.getTenantType()))) {
                        List<GroupMember> groupMembers = groupProvider.findGroupMemberByGroupId(bill.getTenantId());
                        if (groupMembers != null && groupMembers.size() > 0) {
                            for(GroupMember groupMember : groupMembers) {
                                if (EntityType.USER.equals(EntityType.fromCode(groupMember.getMemberType()))) {
                                    sendMessageToUser(groupMember.getMemberId(), content);
                                }
                            }
                        }
                    }

                    //没有contactNo的企业 通知所有企业管理员
                    if (TenantType.ENTERPRISE.equals(TenantType.fromCode(bill.getTenantType()))) {
                        ListServiceModuleAdministratorsCommand command = new ListServiceModuleAdministratorsCommand();
                        command.setOwnerId(bill.getTenantId());
                        command.setOwnerType(EntityType.ORGANIZATIONS.getCode());
                        command.setOrganizationId(bill.getTenantId());
                        List<OrganizationContactDTO> orgContact = rolePrivilegeService.listOrganizationAdministrators(command);
                        if (orgContact != null && orgContact.size() > 0) {
                            for(OrganizationContactDTO contact : orgContact) {
                                if (OrganizationMemberTargetType.USER.equals(OrganizationMemberTargetType.fromCode(contact.getTargetType()))) {
                                    sendMessageToUser(contact.getTargetId(), content);
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private void sendMessageToUser(Long userId, String content) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("notify asset bills: userId = {}, content = {}", userId, content);
        }

        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
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

        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ASSET_BILL_TEMPLATE.getCode()).tryEnter(()-> {
            if(cmd.getDtos() != null && cmd.getDtos().size() > 0) {
                for(AssetBillTemplateFieldDTO dto : cmd.getDtos()) {
                    AssetBillTemplateFields field = ConvertHelper.convert(dto, AssetBillTemplateFields.class);
                    field.setTemplateVersion(field.getTemplateVersion() + 1);
                    assetProvider.creatTemplateField(field);
                }
            }
        });

        ListAssetBillTemplateCommand command = new ListAssetBillTemplateCommand();
        command.setOwnerType(cmd.getDtos().get(0).getOwnerType());
        command.setOwnerId(cmd.getDtos().get(0).getOwnerId());
        command.setTargetType(cmd.getDtos().get(0).getTargetType());
        command.setTargetId(cmd.getDtos().get(0).getTargetId());

        List<AssetBillTemplateFieldDTO> dtos = listAssetBillTemplate(command);
        return dtos;
    }

    @Override
    public Boolean checkTokenRegister(CheckTokenRegisterCommand cmd) {
        UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), cmd.getContactNo());

        if(identifier == null) {
            return false;
        }

        return true;
    }

    @Override
    public NotifyTimesResponse notifyTimes(ImportOwnerCommand cmd) {
        NotifyTimesResponse response = new NotifyTimesResponse();
        long startTime = getTimesMonthmorning();
        long endTime = getTimesMonthnight();

        int count = assetProvider.countNotifyRecords(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType(),
                new Timestamp(startTime), new Timestamp(endTime));
        response.setNotifyTimes(count);

        AssetBillNotifyRecords lastRecord = assetProvider.getLastAssetBillNotifyRecords(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());

        if(lastRecord != null) {
            response.setLastNotifyTime(lastRecord.getCreateTime());
        }
        return response;
    }

    @Override
    public AssetBillStatDTO getAssetBillStat(GetAssetBillStatCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(cmd.getTargetType(), cmd.getTargetId());

        String vendor = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendor);

        AssetBillStatDTO dto = handler.getAssetBillStat(cmd.getTenantType(), cmd.getTenantId(), cmd.getAddressId());
        return dto;
    }

    //获得本月第一天0点时间
    private long getTimesMonthmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }
    //获得本月最后一天24点时间
    private long getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTimeInMillis();
    }

    private Map<Long, List<Field>> getTemplateFields(ListAssetBillTemplateCommand cmd) {
        List<Field> fields = new ArrayList<>();
        Map<Long, List<Field>> fieldMap = new HashMap<Long, List<Field>>();
        List<AssetBillTemplateFieldDTO> dtos = listAssetBillTemplate(cmd);
        if(dtos != null && dtos.size() > 0) {
            Class c=CreatAssetBillCommand.class;
            try {
            	Long templateVersion = 0L;
                for(AssetBillTemplateFieldDTO dto : dtos) {
                    if(AssetBillTemplateSelectedFlag.SELECTED.equals(AssetBillTemplateSelectedFlag.fromCode(dto.getSelectedFlag()))) {
                        templateVersion = dto.getTemplateVersion();
                        Field field = c.getDeclaredField(dto.getFieldName());
                        fields.add(field);
                    }
                }
                fieldMap.put(templateVersion, fields);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fieldMap;
    }
}
