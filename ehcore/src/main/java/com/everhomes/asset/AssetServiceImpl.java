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
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
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
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.community.CommunityType;
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
import com.everhomes.sms.SmsProvider;
import com.everhomes.techpark.rental.RentalServiceImpl;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;



import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

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
            cmd.setPageAnchor(1l);
        }
        if(cmd.getPageSize() == null || cmd.getPageSize() < 1 || cmd.getPageSize() > Integer.MAX_VALUE/10){
            cmd.setPageSize(20);
        }
        int pageOffSet = (cmd.getPageAnchor().intValue()-1)*cmd.getPageSize();
        List<ListBillsDTO> list = handler.listBills(UserContext.getCurrentNamespaceId(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getAddressName(),cmd.getAddressId(),cmd.getBillGroupName(),cmd.getBillGroupId(),cmd.getBillStatus(),cmd.getDateStrBegin(),cmd.getDateStrEnd(),pageOffSet,cmd.getPageSize(),cmd.getTargetName(),cmd.getStatus());
        response.setListBillsDTOS(list);
        if(list.size() <= cmd.getPageSize()){
            response.setNextPageAnchor(cmd.getPageAnchor());
        }else{
            response.setNextPageAnchor(cmd.getPageAnchor()+1);
        }
        return response;
    }

    @Override
    public ListBillItemsResponse listBillItems(ListBillItemsCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(cmd.getOwnerType(),cmd.getOwnerId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        ListBillItemsResponse response = new ListBillItemsResponse();
        if (cmd.getPageAnchor() == null || cmd.getPageAnchor() < 1) {
            cmd.setPageAnchor(1l);
        }
        if(cmd.getPageSize() == null){
            cmd.setPageSize(20);
        }
        int pageOffSet = (cmd.getPageAnchor().intValue()-1)*cmd.getPageSize();
        List<BillDTO> billDTOS = handler.listBillItems(cmd.getBillId(),cmd.getTargetName(),pageOffSet,cmd.getPageSize());
        response.setBillDTOS(billDTOS);
        if(billDTOS.size() <= cmd.getPageSize()) {
            response.setNextPageAnchor(cmd.getPageAnchor());
        }else{
            response.setNextPageAnchor(cmd.getPageAnchor()+1);
        }
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
        for(int i = 0; i<noticeInfos.size(); i++) {
            NoticeInfo noticeInfo = noticeInfos.get(i);
            //收集短信的信息
            List<Tuple<String, Object>> variables = new ArrayList<>();
            smsProvider.addToTupleList(variables,"1",noticeInfo.getTargetName());
            //模板改了，所以这个也要改
            smsProvider.addToTupleList(variables,"2","2017-05");
//            smsProvider.addToTupleList(variables,"amount2",noticeInfo.getAmountOwed());
            smsProvider.addToTupleList(variables,"3",noticeInfo.getAppName());
            String phoneNums = noticeInfo.getPhoneNum();
            String templateLocale = UserContext.current().getUser().getLocale();
            smsProvider.sendSms(999971, phoneNums, SmsTemplateCode.SCOPE, SmsTemplateCode.PAYMENT_NOTICE_CODE, templateLocale, variables);
            //客户在系统内，把需要推送的uid放在list中
            if(noticeInfo.getTargetId()!=0l){
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


    }

    @Override
    public ShowBillForClientDTO showBillForClient(ClientIdentityCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(cmd.getOwnerType(),cmd.getOwnerId());
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendorName);
        return handler.showBillForClient(cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetType(),cmd.getTargetId(),cmd.getBillGroupId(),cmd.getIsOnlyOwedBill());
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
    public void createBill(CreateBillCommand cmd) {
        if(!cmd.getOwnerType().equals("community")){
            throw new RuntimeException("保存账单不在一个园区");
        }
        List<AddressIdAndName> addressByPossibleName = addressProvider.findAddressByPossibleName(UserContext.getCurrentNamespaceId(), cmd.getOwnerId(), cmd.getBuildingName(), cmd.getApartmentName());
        assetProvider.creatPropertyBill(addressByPossibleName,cmd.getBillGroupDTO(),cmd.getDateStr(),cmd.getIsSettled(),cmd.getNoticeTel(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetName(),cmd.getTargetId(),cmd.getTargetType());
    }

    @Override
    public void OneKeyNotice(OneKeyNoticeCommand cmd) {
        ListBillsCommand convertedCmd = ConvertHelper.convert(cmd, ListBillsCommand.class);
        convertedCmd.setStatus((byte)1);
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
        response.setBuildingName(cmd.getBuildingName());
        response.setApartmentName(cmd.getApartmentName());
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
    public HttpServletResponse exportPaymentBills(ListBillsCommand cmd, HttpServletResponse response) {
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
        String[] propertyNames = new String[10];
        String[] titleName = new String[10];
        int[] titleSize = new int[10];
        List<?> dataList = new ArrayList<>();
        ExcelUtils excel = new ExcelUtils(response,fileName,"sheet1");
        excel.writeExcel(propertyNames,titleName,titleSize,dataList);
        return null;
    }

    @Override
    public List<ListChargingItemsDTO> listChargingItems(OwnerIdentityCommand cmd) {
        return assetProvider.listChargingItems(cmd.getOwnerType(),cmd.getOwnerId());
    }

    @Override
    public List<ListChargingStandardsDTO> listChargingStandards(ListChargingStandardsCommand cmd) {
        return assetProvider.listChargingStandards(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getChargingItemId());
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
