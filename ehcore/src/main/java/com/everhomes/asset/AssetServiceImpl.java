
package com.everhomes.asset;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

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

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.aclink.DoorAccessProvider;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.chargingitem.AssetChargingItemProvider;
import com.everhomes.asset.group.AssetGroupProvider;
import com.everhomes.asset.standard.AssetStandardProvider;
import com.everhomes.asset.zjgkVOs.PaymentStatus;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.contract.ContractServiceImpl;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.filedownload.TaskService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.naming.NameMapper;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.pay.order.SourceType;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.ListServiceModulefunctionsCommand;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.aclink.DoorAuthStatus;
import com.everhomes.rest.aclink.DoorLicenseType;
import com.everhomes.rest.aclink.UpdateFormalAuthByCommunityCommand;
import com.everhomes.rest.aclink.UpdateFormalAuthByCommunityResponse;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.asset.AssetSourceType.AssetSourceTypeEnum;
import com.everhomes.rest.common.AssetModuleNotifyConstants;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.contract.CMBill;
import com.everhomes.rest.contract.CMContractHeader;
import com.everhomes.rest.contract.CMContractUnit;
import com.everhomes.rest.contract.CMDataObject;
import com.everhomes.rest.contract.CMSyncObject;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.contract.NamespaceContractType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.portal.AssetServiceModuleAppDTO;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.ui.user.ListUserRelatedScenesCommand;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserNotificationTemplateCode;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhAssetAppCategories;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillItems;
import com.everhomes.server.schema.tables.pojos.EhPaymentBills;
import com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards;
import com.everhomes.server.schema.tables.pojos.EhPaymentContractReceiver;
import com.everhomes.server.schema.tables.pojos.EhPaymentNoticeConfig;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.CalculatorUtil;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DecimalUtils;
import com.everhomes.util.IntegerUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.ExcelUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//import com.everhomes.contract.ContractService;

/**
 * Created by Wentian on 2017/2/20.
 */
@Component
public class AssetServiceImpl implements AssetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetServiceImpl.class);

    static final List<Character> operators = Arrays.asList('*','/','+','-');
    final String downloadDir ="\\download\\";

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private CommunityProvider communityProvider;

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

//    @Autowired
//    private GroupProvider groupProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

//    @Autowired
//    private FamilyProvider familyProvider;

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

//    @Autowired
//    private ScheduleProvider scheduleProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

//    @Autowired
//    private NamespaceResourceService namespaceResourceService;

    @Autowired
    private LocaleTemplateProvider localeTemplateProvider;


    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;

    @Autowired
    protected LocaleStringService localeStringService;

    @Autowired
	protected TaskService taskService;

    @Autowired
    private AssetGroupProvider assetGroupProvider;

    @Autowired
    private AssetChargingItemProvider assetChargingItemProvider;

    @Autowired
    private AssetStandardProvider assetStandardProvider;
    
    @Autowired
    DoorAccessProvider doorAccessProvider;
    
    @Autowired
    private ScheduleProvider scheduleProvider;
    
    @Autowired
    private DoorAccessService doorAccessService;

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
         // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        //房源招商新增的映射逻辑
        if(cmd.getModuleId() != null && cmd.getModuleId().longValue() != ServiceModuleConstants.ASSET_MODULE){
            // 转换
             Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l,cmd.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
             cmd.setCategoryId(assetCategoryId);
         }
        //物业缴费V6.0（UE优化） 将“ 账单查看、筛选”的权限去掉，因为有此模块权限的用户默认就会有查看和筛选的权限；
        //checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(), PrivilegeConstants.ASSET_MANAGEMENT_VIEW, cmd.getOrganizationId());
        ListBillsResponse response = new ListBillsResponse();
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        List<ListBillsDTO> list = handler.listBills(UserContext.getCurrentNamespaceId(),response, cmd);
        response.setListBillsDTOS(list);
        return response;
    }

    private void checkAssetPriviledgeForPropertyOrg(Long communityId, Long priviledgeId,Long currentOrgId) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), currentOrgId, priviledgeId, PrivilegeConstants.ASSET_MODULE_ID, (byte)13, null, null, communityId);
    }

    @Override
    public ListBillItemsResponse listBillItems(ListBillItemsCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        ListBillItemsResponse response = new ListBillItemsResponse();
        Integer pageOffSet = cmd.getPageAnchor()==null?null:cmd.getPageAnchor().intValue();
        List<BillDTO> billDTOS = handler.listBillItems(cmd.getTargetType(),cmd.getBillId(),cmd.getTargetName(),pageOffSet,cmd.getPageSize(),cmd.getOwnerId(),response, cmd.getBillGroupId());
        response.setBillDTOS(billDTOS);
        return response;
    }

    @Override
    public void selectNotice(SelectedNoticeCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_NOTICE,cmd.getOrganizationId());
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        List<NoticeInfo> noticeInfos = handler.listNoticeInfoByBillId(cmd.getBillIdAndTypes(), cmd.getBillGroupId());
        if(noticeInfos.size()<1) return;
        NoticeWithTextAndMessage(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getBillIdAndTypes(), noticeInfos);
    }

    private void NoticeWithTextAndMessage(String ownerType,Long ownerId,List<BillIdAndType> billIdAndTypes, List<NoticeInfo> noticeInfos) {
        List<Long> uids = new ArrayList<>();
                //客户在系统内，把需要推送的uid放在list中
        List<NoticeInfo> infos = new ArrayList<>();
        for (int i = 0; i < noticeInfos.size(); i++) {
            NoticeInfo noticeInfo = noticeInfos.get(i);
            Long targetId = noticeInfo.getTargetId();
            if (targetId != null && targetId != 0l) {
                if (noticeInfo.getTargetType().equals(AssetTargetType.USER.getCode())) {
                    uids.add(noticeInfo.getTargetId());
                } else if (noticeInfo.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
                    ListServiceModuleAdministratorsCommand tempCmd = new ListServiceModuleAdministratorsCommand();
                    tempCmd.setOwnerId(ownerId);
                    tempCmd.setOwnerType(ownerType);
                    tempCmd.setOrganizationId(noticeInfo.getTargetId());
                    //企业超管是1005？不是1001
                    List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(tempCmd);
                    for (int j = 0; j < organizationContactDTOS.size(); j++) {
                        uids.add(organizationContactDTOS.get(j).getTargetId());
                        //新增一个noticeInfo
                        NoticeInfo n = ConvertHelper.convert(noticeInfo, NoticeInfo.class);
                        //只更改电话信息为企业联系人的contact token
                        n.setPhoneNums(organizationContactDTOS.get(j).getContactToken());
                        infos.add(n);
                    }
                    LOGGER.info("notice uids found = {}"+uids.size());
                }
            }
        }
        noticeInfos.addAll(infos);

        try {
            for (int i = 0; i < noticeInfos.size(); i++) {
                NoticeInfo noticeInfo = noticeInfos.get(i);
                //收集短信的信息
                String phoneNums = noticeInfo.getPhoneNums();
                if(phoneNums == null){
                    continue;
                }
                String[] telNOs = phoneNums.split(",");
                List<Tuple<String, Object>> variables = new ArrayList<>();
                Integer nameSpaceId = UserContext.getCurrentNamespaceId();
                //携带了namespaceId
                if(noticeInfo.getNamespaceId() != null){
                    nameSpaceId = noticeInfo.getNamespaceId();
                }
                injectSmsVars(noticeInfo, variables,nameSpaceId);
                String templateLocale = UserContext.current().getUser().getLocale();
                //phoneNums make it fake during test

//                nameSpaceId = 999971;
                smsProvider.sendSms(nameSpaceId, telNOs, SmsTemplateCode.SCOPE, SmsTemplateCode.PAYMENT_NOTICE_CODE, templateLocale, variables);
            }
        } catch(Exception e){
            LOGGER.error("YZX MAIL SEND FAILED");
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//                    "YZX MAIL SEND FAILED");
            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.MESSAGE_SEND_FAILED,
            		"YZX MAIL SEND FAILED");
        }

        for (int k = 0; k < uids.size(); k++) {
            try {
                MessageDTO messageDto = new MessageDTO();
                messageDto.setAppId(AppConstants.APPID_MESSAGING);
//                    messageDto.setSenderUid(User.SYSTEM_UID);
                messageDto.setSenderUid(2L);
                messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uids.get(k).toString()));
                messageDto.setBodyType(MessageBodyType.TEXT.getCode());
                //insert into eh_locale_template values(@xx+1,user_notification,3?,zh_CN,物业账单通知用户,text,999985)
                //这个逻辑是张江高科的， 但为了测试统一，999971先改为999985用华润测试
                Map<String, Object> map = new HashMap<>();
                User targetUser = userProvider.findUserById(uids.get(k));
                map.put("targetName", targetUser.getNickName());
                String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(null, UserNotificationTemplateCode.SCOPE, UserNotificationTemplateCode.USER_PAYMENT_NOTICE, UserContext.current().getUser().getLocale(), map, "");
                messageDto.setBody(notifyTextForApplicant);
                messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
                if (!notifyTextForApplicant.trim().equals("")) {
                    messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                            uids.get(k).toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
                }
            } catch (Exception e) {
                LOGGER.error(e.toString());
                LOGGER.error("WUYE BILL SENDING MESSAGE FAILED");
            }
        }
        if (UserContext.getCurrentNamespaceId() != 999971) {
            //催缴次数加1
//            List<BillIdAndType> billIdAndTypes = cmd.getBillIdAndTypes();
            List<Long> billIds = new ArrayList<>();
            for (int i = 0; i < billIdAndTypes.size(); i++) {
                billIds.add(Long.parseLong(billIdAndTypes.get(i).getBillId()));
            }
            assetProvider.increaseNoticeTime(billIds);
        }
    }

    public void injectSmsVars(NoticeInfo noticeInfo, List<Tuple<String, Object>> variables,Integer namespaceId) {
        //催缴的所有的变量的注入
        smsProvider.addToTupleList(variables, "targetName", noticeInfo.getTargetName());
        smsProvider.addToTupleList(variables, "dateStr", StringUtils.isBlank(noticeInfo.getDateStr())?"等信息请于应用内查看":noticeInfo.getDateStr());
        smsProvider.addToTupleList(variables, "amount", noticeInfo.getAmountOwed().toString());
        smsProvider.addToTupleList(variables, "appName", noticeInfo.getAppName());
    }

    @Override
    public ShowCreateBillDTO showCreateBill(BillGroupIdCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.showCreateBill(cmd.getBillGroupId());
    }

    @Override
    public ListBillsDTO createBill(CreateBillCommand cmd) {
    	//物业缴费V6.0 将“新增账单”改为“新增账单、批量导入”权限；
    	checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_CREATE,cmd.getOrganizationId());
         // set category default is 0 representing the old data
        // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.createBill(cmd);
    }

    @Override
    public void OneKeyNotice(OneKeyNoticeCommand cmd) {
         // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        //校验催缴的权限
        checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_NOTICE, cmd.getOrganizationId());
        ListBillsCommand convertedCmd = ConvertHelper.convert(cmd, ListBillsCommand.class);
        if(UserContext.getCurrentNamespaceId()!=999971){
            convertedCmd.setPageAnchor(0l);
//            convertedCmd.setPageAnchor(1l);
        }else{
            convertedCmd.setPageAnchor(1l);
        }
        if(convertedCmd.getDateStrEnd()==null){
            Calendar now = newClearedCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String dateStrEnd = sdf.format(now.getTime());
            convertedCmd.setDateStrEnd(dateStrEnd);
        }
        if(convertedCmd.getPageSize()==null){
            convertedCmd.setPageSize(Integer.MAX_VALUE/10);
        }
        convertedCmd.setStatus((byte)1);
        convertedCmd.setBillStatus((byte)0);
        //listBills has already distributed the requests according to namespaces;
        ListBillsResponse convertedResponse = listBills(convertedCmd);
        List<ListBillsDTO> listBillsDTOS = convertedResponse.getListBillsDTOS();
        Map<OwnerEntity,List<String>> noticeObjects = new HashMap<>();
        for(int i = 0; i < listBillsDTOS.size(); i ++) {
            ListBillsDTO convertedDto = listBillsDTOS.get(i);
            OwnerEntity entity = new OwnerEntity();
            entity.setOwnerId(cmd.getOwnerId());
            entity.setOwnerType(convertedDto.getOwnerType());
            if(noticeObjects.containsKey(entity)){
                noticeObjects.get(entity).add(convertedDto.getBillId());
            }else{
                List<String> ids = new ArrayList<>();
                ids.add(convertedDto.getBillId());
                noticeObjects.put(entity,ids);
            }
        }
        for(Map.Entry<OwnerEntity,List<String>> entry : noticeObjects.entrySet()){
            SelectedNoticeCommand requestCmd = new SelectedNoticeCommand();
            requestCmd.setOwnerType(entry.getKey().getOwnerType());
            requestCmd.setOwnerId(entry.getKey().getOwnerId());
            List<BillIdAndType> billIdAndTypes = new ArrayList<>();
            List<String> value = entry.getValue();
            for(int i = 0; i < value.size(); i++){
                BillIdAndType bit = new BillIdAndType();
                bit.setBillId(value.get(i));
                bit.setTargetType(cmd.getTargetType());
                billIdAndTypes.add(bit);
            }
            requestCmd.setBillIdAndTypes(billIdAndTypes);
            String appName = assetProvider.findAppName(UserContext.getCurrentNamespaceId());
//            if(appName==null || appName.trim().length()<1){
//                appName="张江高科推荐";
//            }
            if(UserContext.getCurrentNamespaceId()==999971){
                List<NoticeInfo> list = new ArrayList<>();
                List<ListBillsDTO> listBillsDTOS1 = convertedResponse.getListBillsDTOS();
                //已经处理过的审核中的账单不进行催缴
                for(int k = 0; k < listBillsDTOS1.size(); k++){
                    ListBillsDTO dto = listBillsDTOS1.get(k);
                    if(dto.getPayStatus().equals(PaymentStatus.IN_PROCESS.getCode())){
                        listBillsDTOS1.remove(k);
                        k--;
                    }
                }
                for(int i = 0; i < listBillsDTOS1.size(); i++){
                    ListBillsDTO dto = listBillsDTOS1.get(i);
                    NoticeInfo info = new NoticeInfo();
                    info.setAppName(appName);
                    info.setPhoneNums(String.join(",", dto.getNoticeTelList()));
                    info.setAmountRecevable(dto.getAmountReceivable());
                    info.setAmountOwed(dto.getAmountOwed());
                    info.setDateStr(dto.getDateStr());
                    info.setNamespaceId(999971);
                    Long tid = 0l;
                    String targeType=null;
                    Long uid  = assetProvider.findTargetIdByIdentifier(dto.getTargetId());
                    Long oid = assetProvider.findOrganizationIdByIdentifier(dto.getTargetId());
                    if(uid ==null && oid !=null){
                        tid = oid;
                        targeType=AssetTargetType.ORGANIZATION.getCode();
                    }
                    else if(uid !=null && oid ==null){
                        tid = uid;
                        targeType = AssetTargetType.USER.getCode();
                    }else {
                        LOGGER.info("NOTICE USER IS NOT IN ZUOLIN APP, USER IS {}",dto.getTargetName());
                    }
                    info.setTargetId(tid);
                    info.setTargetType(targeType);
                    info.setTargetName(dto.getTargetName());
                    list.add(info);
                }
                NoticeWithTextAndMessage(requestCmd.getOwnerType(),requestCmd.getOwnerId(),requestCmd.getBillIdAndTypes(),list);
                return;
            }
            selectNotice(requestCmd);
        }
    }

    @Override
    public ListBillDetailResponse listBillDetail(ListBillDetailCommandStr cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        ListBillDetailCommand ncmd = new ListBillDetailCommand();
        try{
            ncmd.setBillId(Long.valueOf(cmd.getBillId()));
        }catch (Exception e){
            //todo
        }
        return handler.listBillDetail(ncmd);
    }

    @Override
    public List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd) {
        //校验是否有查看账单统计的权限
        checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_STATISTICS_VIEW,cmd.getOrganizationId());
         // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.listBillStatics(cmd);
    }

    @Override
    public void modifyBillStatus(BillIdCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_CHANGE_STATUS,cmd.getOrganizationId());
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.modifyBillStatus(cmd);
    }

    public void exportOrders(ListPaymentBillCmd cmd, HttpServletResponse response) {
        if(cmd.getPageSize()==null||cmd.getPageSize()>5000){
            cmd.setPageSize(Long.parseLong("5000"));
        }
        ListPaymentBillResp result = listPaymentBill(cmd);
        List<PaymentOrderBillDTO> dtos = result.getPaymentOrderBillDTOs();
        exportOrdersUtil(dtos, cmd, response);
    }



    @Override
    public List<ListChargingStandardsDTO> listChargingStandards(ListChargingStandardsCommand cmd) {
        if(cmd.getOwnerId()==null){
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
        }
        // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        if(cmd.getModuleId() != null && cmd.getModuleId().longValue() != ServiceModuleConstants.ASSET_MODULE){
            // 转换
             Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l,cmd.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
             cmd.setCategoryId(assetCategoryId);
         }
        return assetProvider.listChargingStandards(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getChargingItemId()
        , cmd.getCategoryId(), cmd.getBillGroupId());
    }

    @Override
    public void modifyNotSettledBill(ModifyNotSettledBillCommand cmd) {
    	checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_MODIFY,cmd.getOrganizationId());
    	assetProvider.modifyNotSettledBill(cmd);
    }

    @Override
    public ListSettledBillExemptionItemsResponse listBillExemptionItems(listBillExemtionItemsCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.listBillExemptionItems(cmd);
    }

    @Override
    public String deleteBill(BillIdCommand cmd) {
    	checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_DELETE,cmd.getOrganizationId());
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        String result = "OK";
        handler.deleteBill(cmd.getBillId(), cmd.getBillGroupId());
        return result;
    }

    @Override
    public void deleteBill(PaymentBillItems billItem) {
        this.assetProvider.updatePaymentBill(billItem.getBillId(),billItem.getAmountReceivable(),billItem.getAmountReceived(),billItem.getAmountOwed());
    }

    @Override
    public void deleteBill(PaymentExemptionItems exemItem) {
        this.assetProvider.updatePaymentBillByExemItemChanges(exemItem.getBillId(),exemItem.getAmount());
    }

    @Override
    public String deleteBillItem(BillItemIdCommand cmd) {
        String result = "OK";
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.deleteBillItem(cmd);
        return result;
    }

    @Override
    public String deletExemptionItem(ExemptionItemIdCommand cmd) {
        String result = "OK";
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.deletExemptionItem(cmd);
        return result;
    }

    /**
     * @author wentian
     * 费用计算方法(重构过，老的方法不再使用或支持，在5.6.0版本删除; 建议下一个重构时使用jodatime或者localDateTime代替Calendar）
     * 数据来源 1：公式和日期期限的数字来自于调用者； 2：日期的设置来自于rule，公式设置来自于standard
     */
    @Override
    public void paymentExpectanciesCalculate(PaymentExpectanciesCommand cmd) {
        LOGGER.info("cmd for paymentExpectancies is : " + cmd.toString());
        List<Long> categoryIdList = assetProvider.getOriginIdFromMappingAppForEnergy(cmd.getModuleId(), cmd.getCategoryId(), PrivilegeConstants.ASSET_MODULE_ID, cmd.getNamesapceId());
        // 转categoryId
        for(Long categoryId : categoryIdList) {
        	if(categoryId == null){
                categoryId = 0l;
            }
            Long contractId = cmd.getContractId();
            String contractNum = cmd.getContractNum();
            // generated a record in eh_payment_contract_receiver to indicate that the process is in working
            assetProvider.setInworkFlagInContractReceiver(contractId,contractNum);
            try{
                SimpleDateFormat sdf_dateStrD = new SimpleDateFormat("yyyy-MM-dd");
                //获得所有计价条款包裹
                List<FeeRules> feesRules = cmd.getFeesRules();
                //list pre defined for bills items, bills, contract receivers etc. billItemExpectancies
                // are for the creation of bill items, while uniquerRecorder are for bills only.
                List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
                List<EhPaymentBills> billList = new ArrayList<>();
                List<EhPaymentContractReceiver> contractDateList = new ArrayList<>();
                List<BillItemsExpectancy> billItemsExpectancies = new ArrayList<>();
                Map<BillDateAndGroupId,BillItemsExpectancy> uniqueRecorder = new HashMap<>();
                //遍历计价条款包裹
                feeRule:for(int i = 0; i < feesRules.size(); i++) {
                    //获取单一包裹
                    FeeRules rule = feesRules.get(i);
                    //获得包裹中的地址包裹, named var1
                    List<ContractProperty> var1 = rule.getProperties();
                    //获得标准, inside formula can be found, together with variables in feerule, amont of bills can be calculated
                    EhPaymentChargingStandards standard = assetStandardProvider.findChargingStandardById(rule.getChargingStandardId());
                    PaymentChargingItemScope itemScope = assetProvider.findChargingItemScope(rule.getChargingItemId(),cmd.getOwnerType(),cmd.getOwnerId());
                    Set<String> varIdens = new HashSet<>();
                    //获得formula的额外内容,阶梯和区间公式的补充
                    List<PaymentFormula> formulaCondition = null;
                    if(standard.getFormulaType()==3 || standard.getFormulaType() == 4){
                        formulaCondition = assetStandardProvider.getFormulas(standard.getId());
                        for(int m = 0; m < formulaCondition.size(); m ++){
                            varIdens.add(formulaCondition.get(m).getConstraintVariableIdentifer());
                        }
                    }
                    //获得standard公式, and find all the variables inside this formula, store inside a set named varIdens
                    String formula = null;
                    if(standard.getFormulaType()==1 || standard.getFormulaType() == 2){
                        formulaCondition = assetStandardProvider.getFormulas(standard.getId());
                        if(formulaCondition!=null){
                            if(formulaCondition.size()>1){
                                LOGGER.error("the bill standard id is ={}, formula size is ={} which is more than one!"
                                       , standard.getId(), formulaCondition.size());
                            }
                            PaymentFormula paymentFormula = formulaCondition.get(0);
                            formula = paymentFormula.getFormulaJson();
                        }else{
//                            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER
//                                    ,"formula cannot be found, standard id is "+standard.getId()+"");
                        	throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.FORMULA_CANNOT_BE_FOUND,
                        			"formula cannot be found, standard id is "+standard.getId()+"");
                        }
                    }
                    char[] formularChars = formula.toCharArray();
                    int index = 0;
                    int start = 0;
                    while(index < formularChars.length){
                        if(formularChars[index]=='+'||formularChars[index]=='-'||formularChars[index]=='*'
                                ||formularChars[index]=='/'||index == formularChars.length-1){
                            String var = formula.substring(start,index==formula.length()-1?index+1:index);
                            if(!IntegerUtil.hasDigit(var)){
                                varIdens.add(var);
                            }
                            start = index+1;
                        }
                        index++;
                    }
                    //判断是否公式中存在的var，计价条款却没给，此时可能时滞后计算（例如能耗无法签约时给出用量），不进行计算
                    int varIdenNum = 0;
                    List<VariableIdAndValue> variableIdAndValueList = rule.getVariableIdAndValueList();
                    for ( int k = 0; k < variableIdAndValueList.size(); k++){
                        VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(k);
                        if(variableIdAndValue.getVariableValue() == null){
                            continue feeRule;
                        }
                        if(varIdens.contains(variableIdAndValue.getVaribleIdentifier())){
                            varIdenNum++;
                        }
                    }
                    if(varIdenNum!=varIdens.size()){
                        continue feeRule;
                    }
                    //获得包裹中的数据包
                    List<VariableIdAndValue> var2 = rule.getVariableIdAndValueList();
                    //获得standard时间设置, reference enum BillingCycle.java
                    Byte billingCycle = standard.getBillingCycle();
                    //获得groupRule的时间设置, this time stands for the timing of charging items to be generated
//                    /**
//                     * 这个获得groupRule的逻辑是建立在一个收费项只能在一个账单组存在, 而且所属的billgroup必须等于应用的categoryId
//                     */
//                    PaymentBillGroupRule groupRule = null;
//                    PaymentBillGroup group = null;
//                    List<PaymentBillGroupRule> groupRules = assetProvider.getBillGroupRule(rule.getChargingItemId()
//                            ,rule.getChargingStandardId(),cmd.getOwnerType(),cmd.getOwnerId());
//                    //获得group on which bill will be generted. Group defined billing cycle, bills day etc.
//                    for(PaymentBillGroupRule pgr : groupRules){
//                        group = assetProvider.getBillGroupById(pgr.getBillGroupId());
//                        if(group.getCategoryId() != null && group.getCategoryId().longValue() == categoryId.longValue()){
//                            groupRule = pgr;
//                            break;
//                        }
//                    }
                    /**
                     * 将一个费项只能进入一个账单组的限制去掉，因为存在一种场景，一个园区下签的租金合同，部分客户是按月收，部分客户按季收。需建多个租金账单组，将2种计价条款的产生的租金费用分别进入2个组内。
                     */
                    PaymentBillGroupRule groupRule = null;
                    PaymentBillGroup group = null;
                    List<PaymentBillGroupRule> groupRules = assetProvider.getBillGroupRule(rule.getChargingItemId()
                    		,rule.getChargingStandardId(),cmd.getOwnerType(),cmd.getOwnerId(),rule.getBillGroupId());
                    if(groupRules != null && groupRules.size() !=0) {
                    	groupRule = groupRules.get(0);
                    }
                    group = assetGroupProvider.getBillGroupById(rule.getBillGroupId());

                    if(group == null || groupRule == null){
                        throw new RuntimeException("bill group or grouprule is null");
                    }
                    Byte balanceDateType = group.getBalanceDateType();
                    //开始循环地址包裹
                    for(int j = 0; j < var1.size(); j ++){
                        List<BillItemsExpectancy> billItemsExpectancies_inner = new ArrayList<>();
                        //从地址包裹中获得一个地址
                        ContractProperty property = var1.get(j);
                        //按照收费标准的计费周期分为按月，按季，按年，均有固定和自然两种情况
                        BillingCycle standardBillingCycle = BillingCycle.fromCode(billingCycle);
                        if(standardBillingCycle == null || standardBillingCycle == BillingCycle.DAY){
                                assetProvider.deleteContractPayment(contractId);
//                                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
//                                        ,ErrorCodes.ERROR_INVALID_PARAMETER,"目前计费周期只支持按月，按季，按年");
                                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.STANDARD_BILLING_CYCLE_NOT_FOUND,
                                		"目前计费周期只支持按月，按季，按年");
                        }
                        // #31113 by-dinfjianmin
    					if (property.getAddressId() != null) {
    						Double apartmentChargeArea = assetProvider.getApartmentInfo(property.getAddressId(), contractId);
    						for (int k = 0; k < var2.size(); k++) {
    							VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(k);
    							if (variableIdAndValue != null && variableIdAndValue.getVaribleIdentifier() != null
    									&& variableIdAndValue.getVaribleIdentifier().equals("mj")) {
    								variableIdAndValue.setVariableValue(apartmentChargeArea);
    							}
    							var2.set(k, variableIdAndValue);
    						}
    					}
                        //calculate the bill items expectancies for each of the address
                        assetFeeHandler(billItemsExpectancies_inner,var2,formula,groupRule,group,rule,standardBillingCycle,cmd,property
                                ,standard,formulaCondition,billingCycle,itemScope);
                        billItemsExpectancies.addAll(billItemsExpectancies_inner);
                    }

                    //按照收费标准的计费周期分为按月，按季，按年，均有固定和自然两种情况
                    BillingCycle balanceBillingCycle = BillingCycle.fromCode(balanceDateType);
                    if(balanceBillingCycle == null || balanceBillingCycle == BillingCycle.DAY){
                            assetProvider.deleteContractPayment(contractId);
//                            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
//                                    ,ErrorCodes.ERROR_INVALID_PARAMETER,"目前计费周期只支持按月，按季，按年");
                            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.STANDARD_BILLING_CYCLE_NOT_FOUND,
                            		"目前计费周期只支持按月，按季，按年");
                    }

                    assetFeeHandlerForBillCycles(uniqueRecorder,groupRule,group,rule,balanceBillingCycle,standard,billingCycle,itemScope);
                }
                //先算出所有的item
                for(int g = 0; g < billItemsExpectancies.size(); g++){
                    BillItemsExpectancy exp = billItemsExpectancies.get(g);
                    // build a billItem
                    PaymentBillItems item = new PaymentBillItems();
                    item.setCategoryId(categoryId);
                    ContractProperty property = exp.getProperty();
                    PaymentBillGroup group = exp.getGroup();
                    PaymentChargingItemScope itemScope = exp.getItemScope();
                    PaymentBillGroupRule groupRule = exp.getGroupRule();
                    List<VariableIdAndValue> variableIdAndValueList = exp.getVariableIdAndValueList();//获取计价条款包裹
                    //资产
                    item.setAddressId(property.getAddressId());
                    item.setBuildingName(property.getBuldingName());
                    item.setApartmentName(property.getApartmentName());
                    item.setPropertyIdentifer(property.getPropertyName());
                    //金额
                    //add #30669 增加负数校验

                    item.setAmountOwed(DecimalUtils.negativeValueFilte(exp.getAmountReceivable()));
                    item.setAmountReceivable(DecimalUtils.negativeValueFilte(exp.getAmountReceivable()));
                    item.setAmountReceived(new BigDecimal("0"));
                    //关联和显示
                    item.setBillGroupId(group.getId());
                    item.setChargingItemName(itemScope != null ?itemScope.getProjectLevelName() == null?groupRule.getChargingItemName():itemScope.getProjectLevelName():groupRule.getChargingItemName());
                    item.setChargingItemsId(groupRule.getChargingItemId());
                    //滞纳金id关联
                    item.setLateFineStandardId(exp.getLateFineStandardId());
                    //日期
                    item.setDateStr(exp.getBillDateStr());
                    item.setDateStrBegin(sdf_dateStrD.format(exp.getDateStrBegin()));
                    item.setDateStrEnd(sdf_dateStrD.format(exp.getDateStrEnd()));
                    item.setDateStrDue(exp.getBillDateDue());
                    item.setDueDayDeadline(exp.getBillDateDeadline());
                    item.setDateStrGeneration(exp.getBillDateGeneration());
                    //归档字段
                    item.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentBillItems.class)));
                    item.setBillGroupRuleId(groupRule.getId());
                    item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    item.setCreatorUid(UserContext.currentUserId());
                    item.setNamespaceId(cmd.getNamesapceId());
                    item.setOwnerType(cmd.getOwnerType());
                    item.setOwnerId(cmd.getOwnerId());
                    item.setTargetType(cmd.getTargetType());
                    item.setTargetId(cmd.getTargetId());
                    item.setContractId(cmd.getContractId());
                    item.setContractIdType(cmd.getContractIdType());
                    item.setContractNum(cmd.getContractNum());
                    item.setTargetName(cmd.getTargetName());
                    item.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    //保存用量 ：物业缴费6.1 add by 杨崇鑫
                	for (int k = 0; k < variableIdAndValueList.size(); k++){
                		VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(k);
                		if(variableIdAndValue != null && variableIdAndValue.getVaribleIdentifier() != null && variableIdAndValue.getVaribleIdentifier().equals("yl")) {
                			item.setEnergyConsume(variableIdAndValue.getVariableValue() != null ? variableIdAndValue.getVariableValue().toString() : null);
                		}
                		if(variableIdAndValue != null && variableIdAndValue.getVaribleIdentifier() != null && variableIdAndValue.getVaribleIdentifier().equals("taxRate")) {
                			//不含税金额=含税金额/（1+税率）    不含税金额=1000/（1+10%）=909.09
                			BigDecimal taxRate = new BigDecimal(variableIdAndValue.getVariableValue() != null ? variableIdAndValue.getVariableValue().toString() : "0");
                			item.setTaxRate(taxRate);
                			taxRate = taxRate.divide(new BigDecimal(100));
                			BigDecimal amountReceivableWithoutTax = BigDecimal.ZERO;
                			amountReceivableWithoutTax = item.getAmountReceivable().divide(BigDecimal.ONE.add(taxRate), 2, BigDecimal.ROUND_HALF_UP);
                			item.setAmountReceivableWithoutTax(amountReceivableWithoutTax);
                			//税额=含税金额-不含税金额       税额=1000-909.09=90.91
                			BigDecimal taxAmount = item.getAmountReceivable().subtract(amountReceivableWithoutTax);
                			item.setTaxAmount(taxAmount);
                			item.setAmountOwedWithoutTax(amountReceivableWithoutTax);
                			item.setAmountReceivedWithoutTax(BigDecimal.ZERO);
                		}
                	}
                	if(cmd.getModuleId().equals(PrivilegeConstants.CONTRACT_MODULE)) {
                		//物业缴费V6.0（UE优化) 账单区分数据来源
                		item.setSourceType(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType());
                		item.setSourceId(cmd.getCategoryId());
                    	LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.CONTRACT_CODE, "zh_CN");
                    	item.setSourceName(localeString.getText());
                    	//物业缴费V6.0：如果来源于合同，支持删除账单/修改账单的减免增收等配置，不支持删除/修改费项
                    	item.setCanDelete((byte)0);
                    	item.setCanModify((byte)0);
                	}else if(cmd.getModuleId().equals(PrivilegeConstants.ENERGY_MODULE)) {
                		item.setSourceType(AssetSourceTypeEnum.ENERGY_MODULE.getSourceType());
                		item.setSourceId(cmd.getCategoryId());
                    	LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.ENERGY_CODE, "zh_CN");
                    	item.setSourceName(localeString.getText());
                    	//物业缴费V6.0 ：如果来源于能耗，不支持删除、不支持修改
                    	item.setCanDelete((byte)0);
                    	item.setCanModify((byte)0);
                	}
                	//物业缴费V6.0 账单、费项表增加是否删除状态字段
                	item.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
                	//瑞安CM对接 账单、费项表增加是否是只读字段
                	item.setIsReadonly((byte)0);//只读状态：0：非只读；1：只读
                    //放到数组中去
                    billItemsList.add(item);
                }
                //再算bill
                for(Map.Entry<BillDateAndGroupId, BillItemsExpectancy> entry : uniqueRecorder.entrySet()){
                    BillItemsExpectancy exp = entry.getValue();
                    EhPaymentBills newBill = new PaymentBills();
                    Long nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILLS.getClass()));
                    if(nextBillId == 0){
                        nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILLS.getClass()));
                    }
                    newBill.setId(nextBillId);
                    newBill.setCategoryId(categoryId);
                    PaymentBillGroup group = exp.getGroup();
                    //周期时间
                    newBill.setDateStr(exp.getBillDateStr());
                    newBill.setDateStrBegin(exp.getBillCycleStart());
                    newBill.setDateStrEnd(exp.getBillCycleEnd());
                    newBill.setDateStrDue(exp.getBillDateDue());
                    newBill.setDueDayDeadline(exp.getBillDateDeadline());
                    //归档字段
                    newBill.setBillGroupId(group.getId());
                    newBill.setNamespaceId(cmd.getNamesapceId());
                    newBill.setNoticetel(cmd.getNoticeTel());
                    newBill.setOwnerId(cmd.getOwnerId());
                    newBill.setContractId(cmd.getContractId());
                    newBill.setContractIdType(cmd.getContractIdType());
                    newBill.setContractNum(cmd.getContractNum());
                    newBill.setTargetName(cmd.getTargetName());
                    newBill.setOwnerType(cmd.getOwnerType());
                    newBill.setTargetType(cmd.getTargetType());
                    newBill.setTargetId(cmd.getTargetId());
                    newBill.setCreatTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    newBill.setCreatorId(UserContext.currentUserId());
                    newBill.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    newBill.setNoticeTimes(0);
                    //初始化 钱
                    newBill.setAmountOwed(new BigDecimal("0"));
                    newBill.setAmountReceivable(new BigDecimal("0"));
                    newBill.setAmountReceived(new BigDecimal("0"));
                    newBill.setAmountSupplement(new BigDecimal("0"));
                    newBill.setAmountExemption(new BigDecimal("0"));
                    //这儿生成的状态的状态都是无效，（注意，入场时添加判断时间，从3变成已出而非未出）
                    newBill.setSwitch((byte)3);
                    //初始化账单状态（未缴，缴清）
                    newBill.setStatus((byte)0);
                    //设定缴费状态（正常，欠费）
                    Date today = newClearedCalendar().getTime();
                    Date x_v = null;
                    try{
                        String dueDayDeadline = newBill.getDueDayDeadline();
                        x_v = sdf_dateStrD.parse(dueDayDeadline);
                        if(today.compareTo(x_v)!=-1){
                            newBill.setChargeStatus((byte)1);
                        }else{
                            newBill.setChargeStatus((byte)0);
                        }
                    }catch (Exception e){
                        newBill.setChargeStatus((byte)0);
                        LOGGER.error("date str parse failed, parsed object is due day deadline from newBill = {}, e={}", newBill,e);
                    }
                    try{
                        x_v = sdf_dateStrD.parse(newBill.getDateStrDue());
                        if(today.compareTo(x_v)!=-1){
                            newBill.setNextSwitch((byte)1);
                            if(cmd.getIsEffectiveImmediately().byteValue() == (byte)1){
                                newBill.setSwitch((byte)1);
                            }
                        }else{
                            if(cmd.getIsEffectiveImmediately().byteValue() == (byte)1){
                                newBill.setSwitch((byte)0);
                                newBill.setNextSwitch((byte)1);
                            }else{
                                newBill.setNextSwitch((byte)0);
                            }
                        }
                    }catch (Exception e){
                        newBill.setNextSwitch((byte)0);
                        LOGGER.error("date str parse failed, parsed object is DateStrDue from newBill = {}, e={}", newBill,e);
                    }
                    for(int k = 0 ; k < billItemsList.size(); k ++){
                        EhPaymentBillItems item = billItemsList.get(k);
                        Date dateGeneration = null;
                        Date billCycleEnd = null;
                        Date billCycleStart = null;
                        try{
                            dateGeneration = sdf_dateStrD.parse(item.getDateStrGeneration());
                            billCycleEnd = sdf_dateStrD.parse(newBill.getDateStrEnd());
                            billCycleStart = sdf_dateStrD.parse(newBill.getDateStrBegin());
                        }catch (Exception e){
                            LOGGER.error("date parsed error for bill item list , e= {}",e);
                            continue;
                        }
                        //费用产生时分要比账单产生的时分要早, 费用产生周期要在周期内,item还没有billId，item符合group和账期的要求
                        if(entry.getKey().getBillGroupId().longValue() == item.getBillGroupId() &&
                                (
                                        dateGeneration.compareTo(billCycleEnd)!=1 && dateGeneration.compareTo(billCycleStart) != -1
                                ) &&
                                item.getBillId()==null )
                        {
                            newBill.setAmountOwed(newBill.getAmountOwed().add(item.getAmountOwed()));
                            newBill.setAmountReceivable(newBill.getAmountReceivable().add(item.getAmountReceivable()));
                            item.setBillId(newBill.getId());
                        }
                    }
                    //更新状态
                    if(newBill.getAmountOwed().compareTo(new BigDecimal("0"))==0){
                        newBill.setStatus((byte)1);
                    }
                    //物业缴费V6.0（UE优化) 账单区分数据来源
                	if(cmd.getModuleId().equals(PrivilegeConstants.CONTRACT_MODULE)) {
                		newBill.setSourceType(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType());
                		newBill.setSourceId(cmd.getCategoryId());
                    	LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.CONTRACT_CODE, "zh_CN");
                    	newBill.setSourceName(localeString.getText());
                    	//物业缴费V6.0：如果来源于合同，支持删除账单/修改账单的减免增收等配置，不支持删除/修改费项
                    	newBill.setCanDelete((byte)1);
                    	newBill.setCanModify((byte)1);
                	}else if(cmd.getModuleId().equals(PrivilegeConstants.ENERGY_MODULE)) {
                		newBill.setSourceType(AssetSourceTypeEnum.ENERGY_MODULE.getSourceType());
                		newBill.setSourceId(cmd.getCategoryId());
                    	LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.ENERGY_CODE, "zh_CN");
                    	newBill.setSourceName(localeString.getText());
                    	//物业缴费V6.0 ：如果来源于能耗，不支持删除、不支持修改
                    	newBill.setCanDelete((byte)0);
                    	newBill.setCanModify((byte)0);
                	}
                	//物业缴费V6.0 账单、费项表增加是否删除状态字段
                	newBill.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
                	//瑞安CM对接 账单、费项表增加是否是只读字段
                	newBill.setIsReadonly((byte)0);//只读状态：0：非只读；1：只读
                    billList.add(newBill);
                }
                //创建一个 contract——receiver，只用来保留状态和记录合同，其他都不干
                PaymentContractReceiver entity = new PaymentContractReceiver();
                entity.setContractId(cmd.getContractId());
                entity.setContractIdType(cmd.getContractIdType());
                entity.setContractNum(cmd.getContractNum());


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
                contractDateList.add(entity);


                if(billItemsList.size()<1 || contractDateList.size()<1){
                    upodateBillStatusOnContractStatusChange(cmd.getContractId(), AssetPaymentConstants.CONTRACT_CANCEL);
                    return;
                }

                LOGGER.error("Asset Fee calculated！ bill list length={}，item length = {}",billList.size(),billItemsList.size());
                if(billList.size()<1 || billItemsList.size()<1 || contractDateList.size()<1){
                    upodateBillStatusOnContractStatusChange(cmd.getContractId(), AssetPaymentConstants.CONTRACT_CANCEL);
                    return;
                }
                this.coordinationProvider.getNamedLock(contractId.toString()).enter(() -> {

                    this.dbProvider.execute((TransactionStatus status) -> {
                        assetProvider.saveBillItems(billItemsList);
                        assetProvider.saveBills(billList);
                        assetProvider.saveContractVariables(contractDateList);
                        return null;
                    });
                    return null;
                });
                LOGGER.error("插入完成");
                assetProvider.setInworkFlagInContractReceiverWell(contractId);
                // 重新计算
                for(EhPaymentBills bill : billList){
                    assetProvider.reCalBillById(bill.getId());
                }
                LOGGER.error("工作flag完成");
                //得到金额总和并更新到eh_contracts表中 by steve
                BigDecimal totalAmount = assetProvider.getBillExpectanciesAmountOnContract(cmd.getContractNum(),cmd.getContractId(), null, null);
                assetProvider.setRent(cmd.getContractId(),totalAmount);

            }catch(Exception e){
                assetProvider.deleteContractPayment(contractId);
                LOGGER.error("failed calculated bill expectancies, failed contract id = {}", contractId);
                LOGGER.error("failed calculation", e);
            }
        }
    }

    @Override
    public ListAutoNoticeConfigResponse listAutoNoticeConfig(ListAutoNoticeConfigCommand cmd) {
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_NOTICE,cmd.getOrganizationId());
        ListAutoNoticeConfigResponse response = new ListAutoNoticeConfigResponse();
        List<NoticeConfig> configsInRet = new ArrayList<>();
        List<PaymentNoticeConfig> configs = assetProvider.listAutoNoticeConfig(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCategoryId());
        Gson gson = new Gson();
        for(PaymentNoticeConfig config : configs){
            NoticeConfig cir = new NoticeConfig();
            cir.setAppNoticeTemplateId(config.getNoticeAppId());
            cir.setDayType(config.getNoticeDayType());
            if(config.getNoticeDayType() != null){
                if(config.getNoticeDayType().byteValue() == NoticeDayType.BEFORE.getCode()){
                    cir.setDayRespectToDueDay(String.valueOf(config.getNoticeDayBefore()));
                }else if(config.getNoticeDayType().byteValue() == NoticeDayType.AFTER.getCode()){
                    cir.setDayRespectToDueDay(String.valueOf(config.getNoticeDayAfter()));
                }
            }
            cir.setMsgNoticeTemplateId(config.getNoticeMsgId());
            cir.setNoticeObjs(gson.fromJson(config.getNoticeObjs(), new TypeToken<List<NoticeObj>>(){}.getType()));
            configsInRet.add(cir);
        }
        response.setConfigs(configsInRet);
        // 通知模板动态从eh_local_templates获得 by wentian @2018/5/10
        List<MsgTemplate> retMsgTemplates = new ArrayList<>();
        List<AppTemplate> retAppTemplates = new ArrayList<>();
        List<LocaleTemplate> appTemplates = localeTemplateProvider.findLocaleTemplateByCode(UserNotificationTemplateCode.ASSET_APP_NOTICE_SCOPE);
        List<LocaleTemplate> msgTemplates = localeTemplateProvider.findLocaleTemplateByCode(SmsTemplateCode.ASSET_MSG_SCOPE);
        for(LocaleTemplate m : appTemplates){
            AppTemplate appTemplate = new AppTemplate(m.getCode().longValue(), m.getText());
            retAppTemplates.add(appTemplate);
        }
        for(LocaleTemplate m : msgTemplates){
            MsgTemplate msgTemplate = new MsgTemplate(m.getCode().longValue(), m.getText());
            retMsgTemplates.add(msgTemplate);
        }
        response.setAppTemplates(retAppTemplates);
        response.setMsgTemplates(retMsgTemplates);
        return response;
    }

    @Override
    public void autoNoticeConfig(AutoNoticeConfigCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_NOTICE,cmd.getOrganizationId());
        checkNullProhibit("所属者类型",cmd.getOwnerType());
        checkNullProhibit("园区id",cmd.getOwnerId());
        checkNullProhibit("域空间",cmd.getNamespaceId());
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        //这里存储催缴的对象和模板使用信息
        List<EhPaymentNoticeConfig> toSaveConfigs = new ArrayList<>();
        List<NoticeConfig> configs = cmd.getConfigs();
        for(NoticeConfig config : configs){
            PaymentNoticeConfig noticeConfig = new PaymentNoticeConfig();
            long nextPaymentNoticeId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentNoticeConfig.class));
            noticeConfig.setId(nextPaymentNoticeId);
            noticeConfig.setOwnerType(cmd.getOwnerType());
            noticeConfig.setOwnerId(cmd.getOwnerId());
            //add categoryId
            noticeConfig.setCategoryId(cmd.getCategoryId());
            noticeConfig.setNamespaceId(cmd.getNamespaceId());
            noticeConfig.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            noticeConfig.setCreateUid(UserContext.currentUserId());
            noticeConfig.setNoticeDayType(config.getDayType());
            if(config.getDayType() != null && config.getDayType().byteValue() == NoticeDayType.BEFORE.getCode()){
                noticeConfig.setNoticeDayBefore(Integer.parseInt(config.getDayRespectToDueDay()));
            }else if(config.getDayType() != null && config.getDayType().byteValue() == NoticeDayType.AFTER.getCode()){
                noticeConfig.setNoticeDayAfter(Integer.parseInt(config.getDayRespectToDueDay()));
            }
            noticeConfig.setNoticeAppId(config.getAppNoticeTemplateId());
            noticeConfig.setNoticeMsgId(config.getMsgNoticeTemplateId());
            noticeConfig.setNoticeObjs(StringHelper.toJsonString(config.getNoticeObjs()));
            toSaveConfigs.add(noticeConfig);
        }
        assetProvider.autoNoticeConfig(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCategoryId(), toSaveConfigs);
    }

    private void assetFeeHandler(List<BillItemsExpectancy> list,List<VariableIdAndValue> var2, String formula, PaymentBillGroupRule groupRule, PaymentBillGroup group, FeeRules rule,BillingCycle cycle,PaymentExpectanciesCommand cmd,ContractProperty property,EhPaymentChargingStandards standard,List<PaymentFormula> formulaCondition,Byte billingCycle,PaymentChargingItemScope itemScope) {
        SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
        //计算的时间区间, it's the overall time period in which several bill cycles exists
        Calendar dateStrBegin = newClearedCalendar();
        if(rule.getDateStrBegin() != null){
            dateStrBegin.setTime(rule.getDateStrBegin());
        }
        Calendar dateStrEnd = newClearedCalendar();
        if(rule.getDateStrEnd() == null){
            dateStrEnd.set(Calendar.DAY_OF_MONTH,dateStrEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        }else{
            dateStrEnd.setTime(rule.getDateStrEnd());
        }
        //先算开始a which stands for the start of one cycle
        Calendar a = newClearedCalendar(Calendar.DATE);
        a.setTime(dateStrBegin.getTime());
        timeLoop:while(a.compareTo(dateStrEnd)<0){
            //d stands for the end of a cycle i.e. d = a+cycle
            Calendar d = newClearedCalendar();
             // without limit, a full cycle should have a end of time - dWithoutLimit
            Calendar dWithoutLimit = newClearedCalendar();
            Calendar aWithoutLimit = newClearedCalendar();
            if(billingCycle.byteValue() == (byte) 5){
                // in this case, 5 stands for the one time pay mode
                d.setTime(dateStrEnd.getTime());
            } else {
                // the end of a cycle -- d now should also react to contract cycle by wentian @ 1018/5/16
                d.setTime(a.getTime());
                dWithoutLimit.setTime(a.getTime());
                aWithoutLimit.setTime(a.getTime());
                if(!cycle.isContract()){
                    aWithoutLimit.set(Calendar.DAY_OF_MONTH, aWithoutLimit.getActualMinimum(Calendar.DAY_OF_MONTH));
                    d.add(Calendar.MONTH,cycle.getMonthOffset());
                    d.set(Calendar.DAY_OF_MONTH,d.getActualMaximum(Calendar.DAY_OF_MONTH));
                    dWithoutLimit.setTime(d.getTime());
                 }else{
                    // #32243  check if the next day is beyond the maximum day of the next month
                    int prevDay = d.get(Calendar.DAY_OF_MONTH);
                    d.add(Calendar.MONTH, cycle.getMonthOffset()+1);
                    int maximumDay = d.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if(prevDay <= maximumDay){
                        d.add(Calendar.DAY_OF_MONTH, -1);
                    }
                    dWithoutLimit.setTime(d.getTime());
                }
            }

            //the end of a cycle -- d cannot beyond the upper limit of its fee rule
            if(d.compareTo(dateStrEnd)==1){
                d.setTime(dateStrEnd.getTime());
            }
            //计算费用产生的日期
            Calendar d1 = newClearedCalendar();
            d1.setTime(d.getTime());
            // 费项产生月份第x月第x日
            if(groupRule.getBillItemMonthOffset()!=null){
                d1.add(Calendar.MONTH,groupRule.getBillItemMonthOffset());
                if(groupRule.getBillItemDayOffset()==null){
                    d1.set(Calendar.DAY_OF_MONTH,d1.getActualMaximum(Calendar.DAY_OF_MONTH));
                }else{
                    d1.set(Calendar.DAY_OF_MONTH,groupRule.getBillItemDayOffset());
                }
            }
            // 第x月的数据没有的话，认为费项产生日期周期的最后一天
            else{
                // d1（费项产生日期）等于d就好
            }

            //费项time -- d1不能超过计价条款的两个边
            if(d1.compareTo(dateStrBegin)==-1){
                d1.setTime(dateStrBegin.getTime());
            }
            if(d1.compareTo(dateStrEnd)==1){
                d1.setTime(dateStrEnd.getTime());
            }
            //计算系数r，系数r = （d-a)'s 天数/d所在月往未来一周期的天数,  如果符合一个周期，那么 r = 1；
            String r = "1";
            Integer days = null;
            // calculate r if cycle is not one-off deal
            if(billingCycle.byteValue() != (byte) 5){

                boolean b = checkCycle(d, a, cycle.getMonthOffset()+1);
                int divided = daysBetween(dWithoutLimit,aWithoutLimit);
                days = divided;
                if(!b){
                    // period of this cycle
                    int divider = daysBetween(d, a);
                    r = String.valueOf(divider+"/" + divided);
                }
            }
            BigDecimal amount = calculateFee(var2, days, formula, r,standard, formulaCondition);
            //组装对象
            BillItemsExpectancy obj = new BillItemsExpectancy();
            obj.setVariableIdAndValueList(var2);//保存计价条款的包裹，以便后面使用
            obj.setProperty(property);
            obj.setGroupRule(groupRule);
            obj.setGroup(group);
            obj.setStandard(standard);
            //滞纳金标准id
            obj.setLateFineStandardId(rule.getLateFeeStandardId());
            obj.setItemScope(itemScope);
            obj.setAmountReceivable(amount);
            obj.setAmountOwed(amount);
            obj.setDateStrBegin(a.getTime());
            obj.setDateStrEnd(d.getTime());
            obj.setDateStrFakeEnd(dWithoutLimit.getTime());
            if(d1.compareTo(dateStrEnd) ==-1){
                obj.setBillDateGeneration(yyyyMMdd.format(d1.getTime()));
            }else{
                obj.setBillDateGeneration(yyyyMMdd.format(dateStrEnd.getTime()));
            }
            obj.setBillGroupId(group.getId());
            obj.setBillDateStr(yyyyMM.format(a.getTime()));
            // calculate due day. Due day stands for the day in witch bill will ben switched to be validate(switch = 1)
            Calendar due = newClearedCalendar();
            BillsDayType billsDayType = BillsDayType.fromCode(group.getBillsDayType());
            if(billsDayType == null){
                billsDayType = BillsDayType.FIRST_MONTH_NEXT_PERIOD;
            }
            switch (billsDayType){
                case FIRST_MONTH_NEXT_PERIOD:
                    due.setTime(d.getTime());
                    due.add(Calendar.DAY_OF_MONTH,group.getBillsDay());
                    break;
                case BEFORE_THIS_PERIOD:
                    due.setTime(a.getTime());
                    due.add(Calendar.DAY_OF_MONTH, -group.getBillsDay());
                    break;
                case AFTER_THIS_PERIOD:
                    due.setTime(a.getTime());
                    due.add(Calendar.DAY_OF_MONTH, group.getBillsDay() - 1);
                    break;
                case END_THIS_PERIOD:
                    due.setTime(d.getTime());
                    break;
                default:
                    LOGGER.error("unexpeced bills day type when cal due day, day type = {}, group id ={}", group.getBillsDayType(), group.getId());
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER
                    , "unexpeced bills day type when cal due day, day type = {}, group id ={}", group.getBillsDayType(), group.getId());
            }
            obj.setBillDateDue(yyyyMMdd.format(due.getTime()));
            // calculate the dealine of the bill. Deadline according to the design (by jinlan wang), showld after the due day a specific months or days, usually in day.
            Calendar deadline = newClearedCalendar();
            deadline.setTime(due.getTime());
            //日
            if(group.getDueDayType()==1){
                deadline.add(Calendar.DAY_OF_MONTH,group.getDueDay());
            }
            //月
            else if(group.getDueDayType() == 2){
                deadline.add(Calendar.MONTH,group.getDueDay());
            }else{
                LOGGER.info("Group due day type can only be 1 or 2, but now type = {}",group.getBalanceDateType());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER
                        ,"due day type is wrong!, group id ={}",group.getId());
            }
            obj.setBillDateDeadline(yyyyMMdd.format(deadline.getTime()));
            obj.setBillCycleStart(yyyyMMdd.format(a.getTime()));
            obj.setBillCycleEnd(yyyyMMdd.format(d.getTime()));
            list.add(obj);
            // 如果是一次性，则停止循环
            if(billingCycle == 5){
                break timeLoop;
            }
            // d which stands for the end of this cycle, if d goes forward one day
            // , that would be the start of the start date of the next cycle
            d.add(Calendar.DAY_OF_MONTH,1);
            a.setTime(d.getTime());
        }

        //拆卸调租的包裹
        List<RentAdjust> rentAdjusts = cmd.getRentAdjusts();
        if(rentAdjusts!=null){
            outter:for(int i = 0; i < rentAdjusts.size(); i ++){
                RentAdjust rent = rentAdjusts.get(i);
                //是否对应一个资源和收费项，不对应则不进行调租
                List<ContractProperty> rentProperties = rent.getProperties();
                //是否对应同一个账单组ID，不对应不进行调租(物业缴费V6.3 签合同选择计价条款前，先选择账单组)
                Long rentBillGroupId = rent.getBillGroupId();
                Long feeBillGroupId = rule.getBillGroupId();
                if(!rentBillGroupId.equals(feeBillGroupId)){
                	continue outter;
                }
                Long rentChargingItemId = rent.getChargingItemId();
                Long feeChargingItemId = rule.getChargingItemId();
                if(feeChargingItemId != rentChargingItemId){
                    continue outter;
                }
                inner:for(int j = 0; j < rentProperties.size(); j ++){
                    if(rentProperties.get(j).getAddressId().equals(property.getAddressId())){
                        break inner;
                    }
                    if(j == rentProperties.size()-1){
                        continue outter;
                    }
                }
                //进行调租
                //调租的时间区间,收费项的计费时间区间为
                Calendar start = newClearedCalendar();
                start.setTime(rent.getStart());
                Calendar end = newClearedCalendar();
                end.setTime(rent.getEnd());
                if(end.compareTo(dateStrEnd)!=-1){
                    end.setTime(dateStrEnd.getTime());
                }
                //算出哪些时间区间是需要调的，（调整幅度放到计算里去)
                Byte seperationTypeByte = rent.getSeperationType();
                Float separationTime = rent.getSeparationTime();
                List<Calendar> insertTimes = new ArrayList<>();
                SeperationType seperationType = SeperationType.fromCode(seperationTypeByte);
                switch (seperationType){
                    case DAY:
                        Calendar start_copy = getCopyCalendar(start);
                        if(separationTime<1) separationTime = 1f;
                        start_copy.add(Calendar.DAY_OF_MONTH,separationTime.intValue());
                        while(start_copy.compareTo(end)==-1){
                            Calendar start_copy_copy =newClearedCalendar();
                            start_copy_copy.setTime(start_copy.getTime());
                            insertTimes.add(start_copy_copy);
                            start_copy.add(Calendar.DAY_OF_MONTH,separationTime.intValue());
                        }
                        break;
                    case MONTH:
                        Calendar start_copy_1 = getCopyCalendar(start);
                        Object[] interAndFloat = IntegerUtil.getIntegerAndFloatPartFromFloat(separationTime);
                        start_copy_1.add(Calendar.MONTH,(Integer) interAndFloat[0]);
                        start_copy_1.add(Calendar.DAY_OF_MONTH,(int)((float) start_copy_1.getActualMaximum(Calendar.DAY_OF_MONTH) * (float) interAndFloat[1]));
                        while(start_copy_1.compareTo(end) == -1){
                            Calendar start_copy_1_copy =newClearedCalendar();
                            start_copy_1_copy.setTime(start_copy_1.getTime());
                            insertTimes.add(start_copy_1_copy);
                            start_copy_1.add(Calendar.MONTH,(Integer) interAndFloat[0]);
                            start_copy_1.add(Calendar.DAY_OF_MONTH,(int)((float) start_copy_1.getActualMaximum(Calendar.DAY_OF_MONTH) * (float) interAndFloat[1]));
                        }
                        break;
                    case YEAR:
                        Calendar start_copy_2 = getCopyCalendar(start);
                        Object[] interAndFloat_1 = IntegerUtil.getIntegerAndFloatPartFromFloat(separationTime);
                        Object[] interAndFloat_2 = IntegerUtil.getIntegerAndFloatPartFromFloat(((float) interAndFloat_1[1]) * 12f);
                        start_copy_2.add(Calendar.YEAR,(Integer) interAndFloat_1[0]);
                        start_copy_2.add(Calendar.MONTH,(Integer) interAndFloat_2[0]);
                        start_copy_2.add(Calendar.DAY_OF_MONTH,(int)((float) start_copy_2.getActualMaximum(Calendar.DAY_OF_MONTH) * (float) interAndFloat_2[1]));
                        while(start_copy_2.compareTo(end)==-1){
                            Calendar start_copy_2_copy =newClearedCalendar();
                            start_copy_2_copy.setTime(start_copy_2.getTime());
                            insertTimes.add(start_copy_2_copy);

                            start_copy_2.add(Calendar.YEAR,(Integer) interAndFloat_1[0]);
                            start_copy_2.add(Calendar.MONTH,(Integer) interAndFloat_2[0]);
                            start_copy_2.add(Calendar.DAY_OF_MONTH,(int)((float) start_copy_2.getActualMaximum(Calendar.DAY_OF_MONTH) * (float) interAndFloat_2[1]));
                        }
                        break;
                }

                //insertTimes means the timings at which date the fee would have been adjusted.
                // Spear of Longinus references to a weapon once have the blood of Gesus on it.
                longinusBase:for(int m = 0; m < insertTimes.size(); m ++){
                    Calendar longinus = insertTimes.get(m);
                    for(int k = 0; k < list.size(); k++){
                        BillItemsExpectancy item = list.get(k);
                        // a stands for the start, while d stands for the end of the same bill cycle of a item
                        a.setTime(item.getDateStrBegin());
                        Calendar d2 = newClearedCalendar();
                        d2.setTime(item.getDateStrEnd());
                        if(d2.compareTo(longinus)!=-1){
                            // longinus stands for the fee adjusting timing.
                            // If the period of an item hava this longinus inside, the after part and later items will
                            // have an adjust on the fee.
                            reCalFee(longinus,d2,item,rent);
                            reCalFee(list,k+1,rent);
                            continue longinusBase;
                        }
                    }
                }
                //调租完毕
            }
        }
        //拆卸免租的包裹
        List<RentFree> rentFrees = cmd.getRentFrees();
        if(rentFrees!=null){
            //是否对应一个资源和收费项，不对应则不进行调组
            outter:for(int i = 0; i < rentFrees.size(); i ++){
                RentFree rent = rentFrees.get(i);
                List<ContractProperty> rentProperties = rent.getProperties();
                //是否对应同一个账单组ID，不对应不进行调租(物业缴费V6.3 签合同选择计价条款前，先选择账单组)
                Long rentBillGroupId = rent.getBillGroupId();
                Long feeBillGroupId = rule.getBillGroupId();
                if(!rentBillGroupId.equals(feeBillGroupId)){
                	continue outter;
                }
                Long rentChargingItemId = rent.getChargingItemId();
                Long feeChargingItemId = rule.getChargingItemId();
                if(feeChargingItemId != rentChargingItemId){
                    continue outter;
                }
                inner:for(int j = 0; j < rentProperties.size(); j ++){
                    if(rentProperties.get(j).getAddressId().equals( property.getAddressId())){
                        break inner;
                    }
                    if(j == rentProperties.size()-1){
                        continue outter;
                    }
                }
                //开始免租
                // the 'f' stands for how much the proportion of the intersection of the free period and the period of a bill cycle of item
                // is with respect to free period.
                Date start = rent.getStartDate();
                Date end = rent.getEndDate();
                BigDecimal amount_free = rent.getAmount();
                for(int j = 0; j < list.size(); j ++){
                    BillItemsExpectancy item = list.get(j);
                    Date item_start = item.getDateStrBegin();
                    Date item_end = item.getDateStrEnd();
                    //完全没有交集的kick out
                    if(end.compareTo(item_start) != 1 || start.compareTo(item_end) != -1){
                        continue;
                    }
                    Calendar minEnd = newClearedCalendar();
                    minEnd.setTime(end.compareTo(item_end) == -1 ? end : item_end);
                    Calendar maxStart = newClearedCalendar();
                    maxStart.setTime(start.compareTo(item_start)==1 ? start : item_start);
                    float f = (float)daysBetween(maxStart, minEnd) / (float) daysBetween_date(start, end);
                    item.setAmountReceivable(item.getAmountReceivable().subtract(amount_free.multiply(new BigDecimal(f))));
                    item.setAmountOwed(item.getAmountReceivable());
                }
                //免租结束
            }
        }
    }

    private Calendar newClearedCalendar(Integer level) {
        Calendar instance = Calendar.getInstance();
        instance.clear(Calendar.DATE);
        return instance;
    }
    private Calendar newClearedCalendar() {
        Calendar instance = Calendar.getInstance();
        instance.clear(Calendar.DATE);
        return instance;
    }

    private void assetFeeHandlerForBillCycles(Map<BillDateAndGroupId,BillItemsExpectancy> uniqueRecorder
            , PaymentBillGroupRule groupRule, PaymentBillGroup group, FeeRules rule,BillingCycle cycle
            , EhPaymentChargingStandards standard,Byte billingCycle,PaymentChargingItemScope itemScope ) {
        SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

        //计算的时间区间
        Calendar dateStrBegin = newClearedCalendar();
        if(rule.getDateStrBegin() != null){
            dateStrBegin.setTime(rule.getDateStrBegin());
        }
        Calendar dateStrEnd = newClearedCalendar();
        if(rule.getDateStrEnd() == null){
            dateStrEnd.set(Calendar.DAY_OF_MONTH,dateStrEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        }else{
            dateStrEnd.setTime(rule.getDateStrEnd());
        }


        //先算开始a
        Calendar a = newClearedCalendar(Calendar.DATE);
        a.setTime(dateStrBegin.getTime());

        timeLoop:while(a.compareTo(dateStrEnd)<0){
            //d stands for the end of a cycle i.e. d = a+cycle
            Calendar d = newClearedCalendar();
            if(billingCycle.byteValue() == (byte) 5){
                // in this case, 5 stands for the one time pay mode
                d.setTime(dateStrEnd.getTime());
            } else {
                // the end of a cycle -- d now should also react to contract cycle by wentian @ 1018/5/16
                d.setTime(a.getTime());
                if(!cycle.isContract()){
                    d.add(Calendar.MONTH,cycle.getMonthOffset());
                    d.set(Calendar.DAY_OF_MONTH,d.getActualMaximum(Calendar.DAY_OF_MONTH));
                }else{
                    // #32243  check if the next day is beyond the maximum day of the next month
                    int prevDay = d.get(Calendar.DAY_OF_MONTH);
                    d.add(Calendar.MONTH, cycle.getMonthOffset()+1);
                    int maximumDay = d.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if(prevDay <= maximumDay){
                        d.add(Calendar.DAY_OF_MONTH, -1);
                    }
                }
            }

            //the end of a cycle -- d cannot beyond the upper limit of its fee rule
            if(d.compareTo(dateStrEnd)>0){
                d.setTime(dateStrEnd.getTime());
            }
            //组装对象
            BillItemsExpectancy obj = new BillItemsExpectancy();
            obj.setGroupRule(groupRule);
            obj.setGroup(group);
            obj.setStandard(standard);
            obj.setItemScope(itemScope);
            //滞纳金id跟着细项
            obj.setDateStrBegin(a.getTime());
            obj.setDateStrEnd(d.getTime());
            obj.setBillGroupId(group.getId());
            obj.setBillDateStr(yyyyMM.format(a.getTime()));
            BillDateAndGroupId dag = new BillDateAndGroupId();
            dag.setBillGroupId(group.getId());
            dag.setDateStr(obj.getBillDateStr());
            // calculate due day. Due day stands for the day in witch bill will ben switched to be validate(switch = 1)
            Calendar due = newClearedCalendar();
            BillsDayType billsDayType = BillsDayType.fromCode(group.getBillsDayType());
            if(billsDayType == null){
                billsDayType = BillsDayType.FIRST_MONTH_NEXT_PERIOD;
            }
            switch (billsDayType){
                case FIRST_MONTH_NEXT_PERIOD:
                    due.setTime(d.getTime());
                    due.add(Calendar.DAY_OF_MONTH,group.getBillsDay());
                    break;
                case BEFORE_THIS_PERIOD:
                    due.setTime(a.getTime());
                    due.add(Calendar.DAY_OF_MONTH, -group.getBillsDay());
                    break;
                case AFTER_THIS_PERIOD:
                    due.setTime(a.getTime());
                    due.add(Calendar.DAY_OF_MONTH, group.getBillsDay() - 1);
                    break;
                case END_THIS_PERIOD:
                    due.setTime(d.getTime());
                    break;
                default:
                    LOGGER.error("unexpeced bills day type when cal due day, day type = {}, group id ={}", group.getBillsDayType(), group.getId());
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER
                            , "unexpeced bills day type when cal due day, day type = {}, group id ={}", group.getBillsDayType(), group.getId());
            }
            obj.setBillDateDue(yyyyMMdd.format(due.getTime()));
            // calculate the dealine of the bill. Deadline according to the design (by jinlan wang), showld after the due day a specific months or days, usually in day.
            Calendar deadline = newClearedCalendar();
            deadline.setTime(due.getTime());
            //日
            if(group.getDueDayType()==1){
                deadline.add(Calendar.DAY_OF_MONTH,group.getDueDay());
            }
            //月
            else if(group.getDueDayType() == 2){
                deadline.add(Calendar.MONTH,group.getDueDay());
            }else{
                LOGGER.info("Group due day type can only be 1 or 2, but now type = {}",group.getBalanceDateType());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER
                        ,"due day type is wrong!, group id ={}",group.getId());
            }

            obj.setBillDateDeadline(yyyyMMdd.format(deadline.getTime()));
            obj.setBillCycleStart(yyyyMMdd.format(a.getTime()));
            obj.setBillCycleEnd(yyyyMMdd.format(d.getTime()));

            if(!uniqueRecorder.containsKey(dag)){
                //未存在改账单（未更新价格），就加入
                uniqueRecorder.put(dag,obj);
            }else{
                //已存在，则更新为时间范围更广的
                BillItemsExpectancy prev = uniqueRecorder.get(dag);
                if(prev.getDateStrBegin().compareTo(obj.getDateStrBegin()) == 1){
                    prev.setDateStrBegin(obj.getDateStrBegin());
                    prev.setBillCycleStart(obj.getBillCycleStart());
                }
                if(prev.getDateStrEnd().compareTo(obj.getDateStrEnd()) == -1){
                    prev.setDateStrEnd(obj.getDateStrEnd());
                    prev.setBillCycleEnd(obj.getBillCycleEnd());
                    prev.setBillDateDeadline(obj.getBillDateDeadline());
                    prev.setBillDateDue(obj.getBillDateDue());
                }
            }
            if(billingCycle == 5){
                break timeLoop;
            }
            d.add(Calendar.DAY_OF_MONTH,1);
            a.setTime(d.getTime());
            //继续循环
        }
    }

    private void reCalFee(List<BillItemsExpectancy> list, int k, RentAdjust rent) {
        Byte adjustTypeByte = rent.getAdjustType();
        BigDecimal adjustAmplitude = rent.getAdjustAmplitude();
        if(k > list.size()){
            return;
        }
        for(int i = k; i < list.size(); i ++) {
            BillItemsExpectancy item = list.get(i);
            BigDecimal amount = item.getAmountOwed();
            //拿到本周期的结束和开始
            float preD = 1f;
            Calendar fakeEnd = newClearedCalendar();
            Calendar realStart = newClearedCalendar();
            fakeEnd.setTime(item.getDateStrFakeEnd());
            realStart.setTime(item.getDateStrBegin());
            preD = (float)daysBetween_date(item.getDateStrBegin(), item.getDateStrEnd()) / (float)daysBetween(realStart,fakeEnd);
            BigDecimal d = new BigDecimal(preD);
            AdjustType adjustType = AdjustType.fromCode(adjustTypeByte);
            switch (adjustType){
                case INCREASE_QUANTITY:
                    //按金额递增
                    item.setAmountOwed(amount.add(adjustAmplitude.multiply(d)));
                    item.setAmountReceivable(amount.add(adjustAmplitude.multiply(d)));
                    break;
                case DECREASE_QUANTITY:
                    //按金额递减
                    item.setAmountOwed(amount.subtract(adjustAmplitude.multiply(d)));
                    item.setAmountReceivable(amount.subtract(adjustAmplitude.multiply(d)));
                    break;
                case INCREASE_PROPORTION:
                    //按比例递增
                    BigDecimal amount_before_adjust = item.getAmountOwed();
                    // a * ((b * 0.01)+1) * d
                    BigDecimal changedAmount_2 = amount_before_adjust.multiply((adjustAmplitude.multiply(new BigDecimal("0.01"))).add(new BigDecimal("1"))).multiply(d);
                    // amount_at_d - (amount_before * d)
                    item.setAmountReceivable(item.getAmountReceivable().add(changedAmount_2.subtract(amount_before_adjust.multiply(d))));
                    item.setAmountOwed(item.getAmountReceivable());
                    break;
                case DECREASE_PROPORTION:
                    //按比例递减
                    BigDecimal amount_before_adjust_1 = item.getAmountOwed();
                    BigDecimal one = new BigDecimal("1");
                    // a * (1- (b * 0.01)) * d
                    BigDecimal changedAmount_3 = amount_before_adjust_1.multiply(one.subtract(adjustAmplitude.multiply(new BigDecimal("0.01")))).multiply(d);
                    item.setAmountReceivable(item.getAmountReceivable().subtract(amount_before_adjust_1.multiply(d).subtract(changedAmount_3)));
                    item.setAmountOwed(item.getAmountReceivable());
                    break;
            }
        }
    }

    private void reCalFee(Calendar longinus, Calendar d2, BillItemsExpectancy item, RentAdjust rent) {
        float i = 0f;
        //拿到本周期的结束和开始
        Calendar fakeEnd = newClearedCalendar();
        Calendar realStart = newClearedCalendar();
        realStart.setTime(item.getDateStrBegin());
        fakeEnd.setTime(item.getDateStrFakeEnd());
        i = (float)daysBetween(longinus, d2) / (float)daysBetween(realStart,fakeEnd);
        BigDecimal d = new BigDecimal(String.valueOf(i));
        Byte adjustTypeByte = rent.getAdjustType();
        BigDecimal adjustAmplitude = rent.getAdjustAmplitude();
        AdjustType adjustType = AdjustType.fromCode(adjustTypeByte);
        switch (adjustType){
            case INCREASE_QUANTITY:
                //按金额递增
                BigDecimal changedAmount = adjustAmplitude.multiply(d);
                item.setAmountReceivable(item.getAmountReceivable().add(changedAmount));
                item.setAmountOwed(item.getAmountReceivable());
                break;
            case DECREASE_QUANTITY:
                BigDecimal changedAmount_1 = adjustAmplitude.multiply(d);
                item.setAmountReceivable(item.getAmountReceivable().subtract(changedAmount_1));
                item.setAmountOwed(item.getAmountReceivable());
                //按金额递减
                break;
            case INCREASE_PROPORTION:
                //按比例递增
                BigDecimal amount_before_adjust = item.getAmountOwed();
                // a * ((b * 0.01)+1) * d
                BigDecimal changedAmount_2 = amount_before_adjust.multiply((adjustAmplitude.multiply(new BigDecimal("0.01"))).add(new BigDecimal("1"))).multiply(d);
                // amount_at_d - (amount_before * d)
                item.setAmountReceivable(item.getAmountReceivable().add(changedAmount_2.subtract(amount_before_adjust.multiply(d))));
                item.setAmountOwed(item.getAmountReceivable());
                break;
            case DECREASE_PROPORTION:
                //按比例递减
                BigDecimal amount_before_adjust_1 = item.getAmountOwed();
                BigDecimal one = new BigDecimal("1");
                // a * (1- (b * 0.01)) * d
                BigDecimal changedAmount_3 = amount_before_adjust_1.multiply(one.subtract(adjustAmplitude.multiply(new BigDecimal("0.01")))).multiply(d);
                item.setAmountReceivable(item.getAmountReceivable().subtract(amount_before_adjust_1.multiply(d).subtract(changedAmount_3)));
                item.setAmountOwed(item.getAmountReceivable());
                break;
        }
    }

    private Calendar getCopyCalendar(Calendar raw) {
        Calendar copy = newClearedCalendar(null);
        copy.clear();
        copy.setTime(raw.getTime());
        return copy;
    }

    private boolean checkCycle(Calendar end, Calendar start, Integer cycle) {
        Calendar a_assist = newClearedCalendar();
        a_assist.setTime(start.getTime());
        a_assist.add(Calendar.MONTH,cycle);
        a_assist.add(Calendar.DAY_OF_MONTH,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.format(a_assist.getTime());
        sdf.format(start.getTime());
        sdf.format(end.getTime());
        int i = daysBetween(a_assist, start);
        int i1 = daysBetween(end, start);
        if(i == i1){
            return true;
        }
        return false;
    }

    private int daysBetween(Calendar c1,Calendar c2)
    {
        SimpleDateFormat ez = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ez.parse(ez.format(c2.getTime()));
            return daysBetween_date(ez.parse(ez.format(c2.getTime())), ez.parse(ez.format(c1.getTime())));
        }catch (Exception e){

            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, "no way to lose");
        }
    }
    private int daysBetween_date(Date c1,Date c2)
    {
        long time1 = c1.getTime();
        long time2 = c2.getTime();
        Long between_days=Math.abs(time2-time1)/(1000*3600*24);
        return between_days.intValue() + 1;
    }

    @SuppressWarnings("unused")
	private void NaturalMonthHandler(List<PaymentExpectancyDTO> dtos1, FeeRules rule, List<VariableIdAndValue> variableIdAndValueList, String formula, String chargingItemName, Integer billDay, List<PaymentExpectancyDTO> dtos2, ContractProperty property) {
        String propertyName = property.getPropertyName();
        Date dateStrBegin = rule.getDateStrBegin();
        Date dateStrEnd = rule.getDateStrEnd();
        Calendar c1 = newClearedCalendar(null);
        Calendar c2 = newClearedCalendar(null);
        // c1 is the start of the contract
        c1.setTime(dateStrBegin);
        // c2 is the end date of the contract
        c2.setTime(dateStrEnd);
        Calendar c3 = newClearedCalendar(null);
        // c3 starts as the begin of the contract
        c3.setTime(dateStrBegin);
        //define duration for cal
        float duration = 0;
        //define the end of the date the calculation should take as multiply
        Calendar c5 = newClearedCalendar(null);
        //first to check if the whole period is less than one month

        Calendar c7 = newClearedCalendar(null);
        Calendar c8 = newClearedCalendar(null);
//        c7.setTime(c1.getTime());
//        c8.setTime(c8.getTime());
        if(c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR)&&c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH)){
            duration = ((float)c2.get(Calendar.DAY_OF_MONTH)-(float)c1.get(Calendar.DAY_OF_MONTH)+1f)/(float)c1.getActualMaximum(Calendar.DAY_OF_MONTH);
            c5.setTime(c2.getTime());
            if(duration <= 0){
                throw new RuntimeException("日期错误,结束日期需要大于开始日期");
            }
        }else{
            //calculate the per cent of month from c1 to the end of the month c1 is at
            duration = ((float)c1.getActualMaximum(Calendar.DAY_OF_MONTH) - (float)c1.get(Calendar.DAY_OF_MONTH)+1f)/(float)c1.getActualMaximum(Calendar.DAY_OF_MONTH);
            c5.setTime(c3.getTime());
            c5.set(Calendar.DAY_OF_MONTH,c5.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        BigDecimal tempDuration = new BigDecimal(duration);
        tempDuration = tempDuration.setScale(2,BigDecimal.ROUND_CEILING);
        if(duration != 0){
            if(c5.compareTo(c3)==0){
            }else{
                addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c5, c3, tempDuration.floatValue(),billDay);
            }
        }
        //C3 即账期前进一个月
        c3.add(Calendar.MONTH,1);
        c3.set(Calendar.DAY_OF_MONTH,c3.getActualMinimum(Calendar.DAY_OF_MONTH));
        Calendar c4 = newClearedCalendar();
        c4.setTime(c3.getTime());
        //c4 must be ahead of c3 for one month
        c4.add(Calendar.MONTH,1);


        while(c4.compareTo(c2) == -1 || c4.compareTo(c2) == 0) {
            //each month exactly
            duration = 1;
            c5.setTime(c3.getTime());
            c5.set(Calendar.DAY_OF_MONTH,c5.getActualMaximum(Calendar.DAY_OF_MONTH));
            addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c5, c3, duration,billDay);
            c3.add(Calendar.MONTH,1);
            c3.set(Calendar.DAY_OF_MONTH,c3.getActualMinimum(Calendar.DAY_OF_MONTH));
            c4.add(Calendar.MONTH,1);
            c4.set(Calendar.DAY_OF_MONTH,c4.getActualMinimum(Calendar.DAY_OF_MONTH));
        }
        if(c2.compareTo(c4) < 0 && c3.compareTo(c2) < 0){
            //less than one month
            duration = ((float)c2.get(Calendar.DAY_OF_MONTH)-(float)c2.getActualMinimum(Calendar.DAY_OF_MONTH))/(float)c2.getActualMaximum(Calendar.DAY_OF_MONTH);
            addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c2, c3, duration,billDay);
        }
        dtos1.addAll(dtos2);
    }

//    private void FixedAtContractStartHandler(List<PaymentExpectancyDTO> dtos1, FeeRules rule, List<VariableIdAndValue> variableIdAndValueList, String formula, String chargingItemName, Integer billDay, List<PaymentExpectancyDTO> dtos2, ContractProperty property) {
//        if(true){
//            throw new RuntimeException("暂不支持按照固定日期进行账单结算的模式");
//        }
//        String propertyName = property.getPropertyName();
//        Date dateStrBegin = rule.getDateStrBegin();
//        Date dateStrEnd = rule.getDateStrEnd();
//        Calendar c1 = newClearedCalendar();
//        Calendar c2 = newClearedCalendar();
//        // c1 is the start of the contract
//        c1.setTime(dateStrBegin);
//        // c2 is the end date of the contract
//        c2.setTime(dateStrEnd);
//        Calendar c3 = newClearedCalendar();
//        // c3 starts as the begin of the contract
//        c3.setTime(dateStrBegin);
//        int day = c3.get(Calendar.DAY_OF_MONTH);
//
//        Calendar c4 = newClearedCalendar();
//        c4.setTime(c3.getTime());
//        c4.add(Calendar.MONTH,1);
//        if(c4.getActualMaximum(Calendar.DAY_OF_MONTH)<day){
//            c4.set(Calendar.DAY_OF_MONTH,c4.getActualMaximum(Calendar.DAY_OF_MONTH));
//        }else{
//            c4.set(Calendar.DAY_OF_MONTH,day);
//        }
//
//        if(c4.compareTo(c2) == 0){
//            // one month
//            // get dto and add to dtos
//            float duration = 1;
//            addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c2, c3, duration,billDay);
//        }else{
//            while(c4.compareTo(c2) != 1) {
//                //each month
//                float duration = 1;
//                Calendar c5 = newClearedCalendar();
//                c5.setTime(c3.getTime());
//                if(c5.getActualMaximum(Calendar.DAY_OF_MONTH)<day){
//                    c5.set(Calendar.DAY_OF_MONTH,c5.getActualMaximum(Calendar.DAY_OF_MONTH));
//                }else{
//                    c5.set(Calendar.DAY_OF_MONTH,day);
//                }
//                addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c5, c3, duration,billDay);
//                c3.add(Calendar.MONTH,1);
//                if(c3.getActualMaximum(Calendar.DAY_OF_MONTH)<day){
//                    c3.set(Calendar.DAY_OF_MONTH,c3.getActualMaximum(Calendar.DAY_OF_MONTH));
//                }else{
//                    c3.set(Calendar.DAY_OF_MONTH,day);
//                }
//                c4.add(Calendar.MONTH,1);
//                if(c4.getActualMaximum(Calendar.DAY_OF_MONTH)<day){
//                    c4.set(Calendar.DAY_OF_MONTH,c4.getActualMaximum(Calendar.DAY_OF_MONTH));
//                }else{
//                    c4.set(Calendar.DAY_OF_MONTH,day);
//                }
//            }
//            if(c4.compareTo(c2) == 1 && c2.compareTo(c3) == 1){
//                //less than one month
//                int c2day = c2.get(Calendar.DAY_OF_MONTH);
//                int c3day = c3.get(Calendar.DAY_OF_MONTH);
//                int distance = 0;
//                if(c2day>c3day){
//                    distance = c2day+c3day;
//                }else{
//                    distance = c3.getActualMaximum(Calendar.DAY_OF_MONTH)-c2day+c2day;
//                }
//                float duration = (float)distance/(float)c4.getActualMaximum(Calendar.DAY_OF_MONTH);
//                addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c2, c3, duration,billDay);
//            }
//        }
//        dtos1.addAll(dtos2);
//    }

    @Override
    public void upodateBillStatusOnContractStatusChange(Long contractId,String targetStatus) {
        if(targetStatus.equals(AssetPaymentConstants.CONTRACT_SAVE)){
            assetProvider.changeBillStatusOnContractSaved(contractId);
        }else if(targetStatus.equals(AssetPaymentConstants.CONTRACT_CANCEL)){
            assetProvider.deleteContractPayment(contractId);
        }
    }

    @Override
    public PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.listBillExpectanciesOnContract(cmd);
    }

    @Override
    public void exportRentalExcelTemplate(HttpServletResponse response) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.exportRentalExcelTemplate(response);
    }

    @Override
    public FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        //企业用户的话判断是否为企业管理员
        out:{
            if(cmd.getTargetType().equals(AssetPaymentStrings.EH_ORGANIZATION)){
                Long userId = UserContext.currentUserId();
                ListServiceModuleAdministratorsCommand cmd1 = new ListServiceModuleAdministratorsCommand();
                cmd1.setOrganizationId(cmd.getTargetId());
                cmd1.setActivationFlag((byte)1);
                cmd1.setOwnerType("EhOrganizations");
                cmd1.setOwnerId(null);
                LOGGER.info("organization manager check for bill display, cmd = "+ cmd1.toString());
                List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(cmd1);
                LOGGER.info("organization manager check for bill display, orgContactsDTOs are = "+ organizationContactDTOS.toString());
                LOGGER.info("organization manager check for bill display, userId = "+ userId);
                for(OrganizationContactDTO dto : organizationContactDTOS){
                    Long targetId = dto.getTargetId();
                    if(targetId.longValue() == userId.longValue()){
                        break out;
                    }
                }
                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.NOT_CORP_MANAGER,
                        "not valid corp manager");
            }
        }
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vendor = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendor);
        return handler.findUserInfoForPayment(cmd);

    }

    @Override
    public void updateBillsToSettled(UpdateBillsToSettled cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.updateBillsToSettled(cmd);
    }

    @Override
    public GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vendor = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendor);
        return handler.getAreaAndAddressByContract(cmd);



    }

    @Override
    public PaymentBillItems findBillItemById(Long billItemId) {
        return assetProvider.findBillItemById(billItemId);
    }

    @Override
    public PaymentExemptionItems findExemptionItemById(Long ExemptionItemId) {
        return assetProvider.findExemptionItemById(ExemptionItemId);
    }

//    private void coverVariables(List<VariableIdAndValue> var1, List<VariableIdAndValue> var2) {
//        for(int i = 0 ; i < var1.size(); i++){
//            VariableIdAndValue v1 = var1.get(i);
//            String id1 = (String)v1.getVaribleIdentifier();
//            for(int j = 0; j< var2.size(); j++){
//                VariableIdAndValue v2 = var2.get(j);
//                String id2 = (String)v2.getVaribleIdentifier();
//                if(id1.equals(id2)){
//                    v2.setVariableValue(v1.getVariableValue());
//                }
//
//            }
//        }
//    }

    private void addFeeDTO(List<PaymentExpectancyDTO> dtos, String formula, String chargingItemName, String propertyName, List<VariableIdAndValue> variableIdAndValueList, Calendar c5, Calendar c3, float duration,Integer billDay) {
        PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
        BigDecimal amountReceivable = calculateFee(variableIdAndValueList,formula,duration);
        dto.setAmountReceivable(amountReceivable);
        dto.setChargingItemName(chargingItemName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dto.setDateStrBegin(sdf.format(c3.getTime()));
//        dto.setDateStrEnd(sdf.format(c2.getTime()));
        Calendar c6 = newClearedCalendar();
        c6.setTime(c3.getTime());
        c6.add(Calendar.MONTH,1);
        c6.set(Calendar.DAY_OF_MONTH,billDay);
        dto.setDueDateStr(sdf.format(c6.getTime()));
        dto.setDateStrEnd(sdf.format(c5.getTime()));
        dto.setPropertyIdentifier(propertyName);
        dtos.add(dto);
    }

    private BigDecimal calculateFee(List<VariableIdAndValue> variableIdAndValueList, String formula, float duration) {
        HashMap<String,String> map = new HashMap<String,String>();
        for(int i = 0; i < variableIdAndValueList.size(); i++){
            VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
            map.put((String)variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
        }
        formula = formula.trim();
        char[] preChars = formula.toCharArray();
        List<Character> chars = new ArrayList<>();
        for(Character c : preChars){
            if(!StringUtils.isBlank(c.toString())){
                chars.add(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder variable = new StringBuilder();
        for(int i = 0; i < chars.size(); i++){
            Character c = chars.get(i);
            if(operators.contains(c)){
                if(variable.length() > 0){
                    if(map.containsKey(variable.toString())){
                        sb.append(map.get(variable.toString()));
                    }else{
                        sb.append(variable.toString());
                    }
                }
                sb.append(c);
                variable = new StringBuilder();
            }else{
                variable.append(c);
                if(i == chars.size() - 1){
                    if(map.containsKey(variable.toString())){
                        sb.append(map.get(variable.toString()));
                    }else{
                        sb.append(variable.toString());
                    }
                }
            }
        }
        formula = sb.toString();
        for(char i : formula.toCharArray()){
            if ((i >= 'a' && i <= 'z') || (i >= 'A' && i <= 'Z')){
                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, ErrorCodes.ERROR_INVALID_PARAMETER,"wrong formula" + formula);
            }
        }
        formula += "*"+duration;
        BigDecimal response = CalculatorUtil.arithmetic(formula);
        response.setScale(2,BigDecimal.ROUND_CEILING);

        return response;
    }
    private BigDecimal calculateFee(List<VariableIdAndValue> variableIdAndValueList, String formula) {
        HashMap<String,String> map = new HashMap<String,String>();
        for(int i = 0; i < variableIdAndValueList.size(); i++){
            VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
            map.put((String)variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
        }
        formula = formula.trim();
        char[] preChars = formula.toCharArray();
        List<Character> chars = new ArrayList<>();
        for(Character c : preChars){
            if(!StringUtils.isBlank(c.toString())){
                chars.add(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder variable = new StringBuilder();
        for(int i = 0; i < chars.size(); i++){
            Character c = chars.get(i);
            if(operators.contains(c)){
                if(variable.length() > 0){
                    if(map.containsKey(variable.toString())){
                        sb.append(map.get(variable.toString()));
                    }else{
                        sb.append(variable.toString());
                    }
                }
                sb.append(c);
                variable = new StringBuilder();
            }else{
                variable.append(c);
                if(i == chars.size() - 1){
                    if(map.containsKey(variable.toString())){
                        sb.append(map.get(variable.toString()));
                    }else{
                        sb.append(variable.toString());
                    }
                }
            }
        }
        formula = sb.toString();
        for(char i : formula.toCharArray()){
            if ((i >= 'a' && i <= 'z') || (i >= 'A' && i <= 'Z')){
                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, ErrorCodes.ERROR_INVALID_PARAMETER,"wrong formula" + formula);
            }
        }
        BigDecimal response = CalculatorUtil.arithmetic(formula);
        response.setScale(2,BigDecimal.ROUND_CEILING);

        return response;
    }
    private BigDecimal calculateFee(List<VariableIdAndValue> variableIdAndValueList, Integer days
            , String formula, String duration,EhPaymentChargingStandards standard,List<PaymentFormula> formulaCondition) {
        Byte formulaType = standard.getFormulaType();
        Byte unitPriceType = standard.getPriceUnitType();
        if(unitPriceType != null && unitPriceType.byteValue() == (byte)1){
            formula =relaceDjWithDays(formula, days);
            for(PaymentFormula conf : formulaCondition){
                conf.setFormulaJson(relaceDjWithDays(conf.getFormulaJson(), days));
            }
        }
        BigDecimal result = new BigDecimal("0");
        if(formulaType == 1 || formulaType ==2){
            HashMap<String,String> map = new HashMap<String,String>();
            for(int i = 0; i < variableIdAndValueList.size(); i++){
                VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
                map.put((String)variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
            }
            formula = formula.trim();
            char[] preChars = formula.toCharArray();
            List<Character> chars = new ArrayList<>();
            for(Character c : preChars){
                if(!StringUtils.isBlank(c.toString())){
                    chars.add(c);
                }
            }
            StringBuilder sb = new StringBuilder();
            StringBuilder variable = new StringBuilder();
            for(int i = 0; i < chars.size(); i++){
                Character c = chars.get(i);
                if(operators.contains(c)){
                    if(variable.length() > 0){
                        if(map.containsKey(variable.toString())){
                            sb.append(map.get(variable.toString()));
                        }else{
                            sb.append(variable.toString());
                        }
                    }
                    sb.append(c);
                    variable = new StringBuilder();
                }else{
                    variable.append(c);
                    if(i == chars.size() - 1){
                        if(map.containsKey(variable.toString())){
                            sb.append(map.get(variable.toString()));
                        }else{
                            sb.append(variable.toString());
                        }
                    }
                }
            }
            formula = sb.toString();
            for(char i : formula.toCharArray()){
                if ((i >= 'a' && i <= 'z') || (i >= 'A' && i <= 'Z')){
                    throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, ErrorCodes.ERROR_INVALID_PARAMETER,"wrong formula" + formula);
                }
            }
            formula += "*"+duration;
            result = CalculatorUtil.arithmetic(formula);
            result.setScale(2,BigDecimal.ROUND_FLOOR);
        }
        else if(formulaType == 3 || formulaType == 4){
            //阶梯或者区间
            //解开条件计算
            BigDecimal conditonedAmount = new BigDecimal("0");
            for( int i = 0 ; i < formulaCondition.size(); i ++){
                PaymentFormula condition = formulaCondition.get(i);
                // 3:斜率跟着变量区间总体变化()
                //此时命中：符合条件即可
                // 斜面，最后一个命中的变量为决定者

                // ;4:斜率在不同变量区间取值不同（阶梯）（楼梯，）
                // 每条记录均默认为命中，对于每个变量，有个总值，每条对应这个变量的命中取 value 为条件与变量取值范围的交集，计算公式，叠加
                // 每个命中均计算，若变量值大于命中区间
                if(formulaType == 3){
                    conditonedAmount  = (getConditionedAmount(variableIdAndValueList,condition,formulaType==3));
                }else if(formulaType == 4){
                    conditonedAmount.add((getConditionedAmount(variableIdAndValueList,condition,formulaType==3)));
                }
            }
            result = conditonedAmount;
        }
        return result;
    }

    private String relaceDjWithDays(String formula, Integer days) {
        formula = formula.trim();
        char[] preChars = formula.toCharArray();
        List<Character> chars = new ArrayList<>();
        for(Character c : preChars){
            if(!StringUtils.isBlank(c.toString())){
                chars.add(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder variable = new StringBuilder();
        for(int i = 0; i < chars.size(); i++){
            Character c = chars.get(i);
            if(operators.contains(c)){
                if(variable.length() > 0){
                    if(AssetVariable.UNIT_PRICE.getIdentifier().equals(variable.toString())){
                        variable.append("*");
                        variable.append(String.valueOf(days));
                        sb.append(variable);
                    }else{
                        sb.append(variable.toString());
                    }
                }
                sb.append(c);
                variable = new StringBuilder();
            }else{
                variable.append(c);
                if(i == chars.size() - 1){
                    if(AssetVariable.UNIT_PRICE.getIdentifier().equals(variable.toString())){
                        variable.append("*");
                        variable.append(String.valueOf(days));
                    }
                    sb.append(variable.toString());
                }
            }
        }
       return sb.toString();
    }

    private BigDecimal getConditionedAmount(List<VariableIdAndValue> variableIdAndValueList, PaymentFormula condition,boolean isSlope) {
        BigDecimal result = new BigDecimal("0");
        if (isSlope) {
            //斜面计算
            for (int i = 0; i < variableIdAndValueList.size(); i++) {
                VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
                String varibleIdentifier = (String)variableIdAndValue.getVaribleIdentifier();
                BigDecimal variableValue = new BigDecimal(variableIdAndValue.getVariableValue().toString());
                if (!condition.getConstraintVariableIdentifer().equals(varibleIdentifier)) {
                    continue;
                }
                Byte startConstraint = condition.getStartConstraint();
                Byte endConstraint = condition.getEndConstraint();
                BigDecimal startNum = condition.getStartNum();
                BigDecimal endNum = condition.getEndNum();
                String formulaJson = condition.getFormulaJson();

                if (startConstraint == null && endConstraint == null) {

                }
                if (startConstraint != null && endConstraint == null) {
                    result = checkVariableConditon(startConstraint, variableValue, startNum, variableIdAndValueList, formulaJson);
                }
                if (startConstraint == null && endConstraint != null) {
                    result = checkVariableConditon(endConstraint, variableValue, endNum, variableIdAndValueList, formulaJson);
                }
                if (startConstraint != null && endConstraint != null) {
                    switch (startConstraint) {
                        case 1:
                            if (variableValue.compareTo(startNum) == 1) {
                                result = checkVariableConditon(endConstraint, variableValue, endNum, variableIdAndValueList, formulaJson);
                            }
                        case 2:
                            if (variableValue.compareTo(startNum) != -1) {
                                result = checkVariableConditon(endConstraint, variableValue, endNum, variableIdAndValueList, formulaJson);
                            }
                        case 3:
                            if (variableValue.compareTo(startNum) == -1) {
                                result = checkVariableConditon(endConstraint, variableValue, endNum, variableIdAndValueList, formulaJson);
                            }
                        case 4:
                            if (variableValue.compareTo(startNum) != 1) {
                                result = checkVariableConditon(endConstraint, variableValue, endNum, variableIdAndValueList, formulaJson);
                            }
                        default:
                            LOGGER.error("公式id为" + condition.getId() + ",斜面的区间关系只允许大于，大于等于，小于，小于等于");
                    }
                }
            }
        } else {
            //阶梯
            for (int i = 0; i < variableIdAndValueList.size(); i++) {
                VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
                String varibleIdentifier = (String)variableIdAndValue.getVaribleIdentifier();
                BigDecimal variableValue = new BigDecimal(variableIdAndValue.getVariableValue().toString());
                if (!condition.getConstraintVariableIdentifer().equals(varibleIdentifier)) {
                    continue;
                }
                BigDecimal startNum = condition.getStartNum();
                BigDecimal endNum = condition.getEndNum();
                String formulaJson = condition.getFormulaJson();
                BigDecimal variableStartNum = new BigDecimal("0");
                List<VariableIdAndValue> copy = new ArrayList<>();
                for (int k = 0; k < variableIdAndValueList.size(); k++) {
                    VariableIdAndValue var_temp_orig = variableIdAndValueList.get(k);
                    BigDecimal realValue = new BigDecimal(var_temp_orig.getVariableValue().toString());
                    if (var_temp_orig.getVaribleIdentifier().equals(varibleIdentifier)) {
                        realValue = IntegerUtil.getIntersectionDecimal(startNum, endNum, variableStartNum, variableValue);
                    }
                    VariableIdAndValue var_temp = new VariableIdAndValue(var_temp_orig.getVariableId(), realValue, var_temp_orig.getVaribleIdentifier());
                    copy.add(var_temp);
                }
                result.add(calculateFee(copy, formulaJson));
            }
        }
        return result;
    }

    private BigDecimal checkVariableConditon(Byte startConstraint, BigDecimal variableValue, BigDecimal targetNum,List<VariableIdAndValue> variableIdAndValueList, String formulaJson) {
        switch (startConstraint){
            case 1:
                if(variableValue.compareTo(targetNum) == 1){
                    return calculateFee(variableIdAndValueList,formulaJson);
                }
                return new BigDecimal("0");
            case 2:
                if(variableValue.compareTo(targetNum) != -1){
                    return calculateFee(variableIdAndValueList,formulaJson);
                }
                return new BigDecimal("0");
            case 3:
                if(variableValue.compareTo(targetNum) == -1){
                    return calculateFee(variableIdAndValueList,formulaJson);
                }
                return new BigDecimal("0");
            case 4:
                if(variableValue.compareTo(targetNum) != 1){
                    return calculateFee(variableIdAndValueList,formulaJson);
                }
                return new BigDecimal("0");
        }
        return new BigDecimal("0");
    }

    @Override
    public CheckEnterpriseHasArrearageResponse checkEnterpriseHasArrearage(CheckEnterpriseHasArrearageCommand cmd) {
        CheckEnterpriseHasArrearageResponse response = new CheckEnterpriseHasArrearageResponse();
        List<PaymentBills> assetArrearage = assetProvider.findAssetArrearage(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getOrganizationId());
        response.setHasArrearage(assetArrearage.size() > 0 ? (byte) 1 : (byte) 0);
        return response;
    }

    @Override
    public List<ListLateFineStandardsDTO> listLateFineStandards(ListLateFineStandardsCommand cmd) {
        Long ownerId = cmd.getOwnerId();
        String ownerType = cmd.getOwnerType();
        Integer namespaceId = cmd.getNamespaceId();
        checkNullProhibit("communityId",cmd.getOwnerId());
        // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        if(cmd.getModuleId() != null && cmd.getModuleId().longValue() != ServiceModuleConstants.ASSET_MODULE){
            // 转换
            Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l,cmd.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
            cmd.setCategoryId(assetCategoryId);
         }
        return assetProvider.listLateFineStandards(ownerId,ownerType,namespaceId, cmd.getCategoryId());
    }

    /**
     * @param cmd includes communityId and namespaceId for locating the address and checking privileges, and billGroupId
     *            for get bill items column.
     * @param response
     */
    @Override
    public void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.exportBillTemplates(cmd, response);
    }

    /**
     * 批量导入账单
     * @param cmd {@link BatchImportBillsCommand}
     * @param file
     * @return
     */
    @Override
    public BatchImportBillsResponse batchImportBills(BatchImportBillsCommand cmd, MultipartFile file) {
    	//物业缴费V6.0 将“新增账单”改为“新增账单、批量导入”权限；
    	checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.ASSET_MANAGEMENT_CREATE,cmd.getOrganizationId());
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.batchImportBills(cmd, file);
    }

    /**
     *
     * @param code 参考 {@link AssetTargetType}
     * @param ownerUid 个人或者公司的id
     * @param token 个人的手机号或者公司的名称
     */
    @Override
    public void linkCustomerToBill(String code, Long ownerUid, String token) {
        if(code.equals(AssetTargetType.USER.getCode())){
            assetProvider.linkIndividualUserToBill(ownerUid, token);
        }else if(code.equals(AssetTargetType.ORGANIZATION.getCode())){
            assetProvider.linkOrganizationToBill(ownerUid, token);
        }else{
            LOGGER.error("link customer to bill failed, code={}, token = {}", code, token);
        }
    }

    @Override
    public ListPaymentBillResp listBillRelatedTransac(listBillRelatedTransacCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.listBillRelatedTransac(cmd);
    }
    @Override
    public void reCalBill(ReCalBillCommand cmd) {
        List<Long> ids = assetProvider.findbillIdsByOwner(cmd.getNamespaceId(),cmd.getOwnerType(), cmd.getOwnerId());
        for(Long id : ids){
            assetProvider.reCalBillById(id);
        }
    }
    @Override
    public void modifySettledBill(ModifySettledBillCommand cmd) {
        assetProvider.modifySettledBill(cmd.getBillId(), cmd.getInvoiceNum(), cmd.getNoticeTel());
    }

//    @Override
//    public void noticeTrigger(Integer namespaceId) {
//        autoBillNotice(namespaceId);
//    }

    @Override
    public long getNextCategoryId(Integer namespaceId, Long aLong, String instanceConfig) {
        EhAssetAppCategories c = new AssetAppCategory();
        c.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        c.setCreateUid(aLong);
        c.setNamespaceId(namespaceId);
        long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAssetAppCategories.class));
        c.setId(nextSequence);
        c.setCategoryId(nextSequence);
        try{
            assetProvider.insertAssetCategory(c);
            return nextSequence;
        }catch (Exception e){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_SQL_EXCEPTION, "category constraints voilated");
        }
    }

    @Override
    public void saveInstanceConfig(long categoryId, String ret) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        dslContext.update(Tables.EH_ASSET_APP_CATEGORIES)
                .set(Tables.EH_ASSET_APP_CATEGORIES.INSTANCE_FLAG, ret)
                .where(Tables.EH_ASSET_APP_CATEGORIES.CATEGORY_ID.eq(categoryId))
                .execute();
    }

//    // 冗余代码，为了测试
//    public void autoBillNotice(Integer namespaceId){
//        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
//            this.coordinationProvider.getNamedLock("asset_auto_notice").tryEnter(() -> {
//                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
//                Calendar today = newClearedCalendar();
//                List<PaymentNoticeConfig> configs = assetProvider.listAllNoticeConfigsByNameSpaceId(namespaceId);
//                Map<Long, List<PaymentNoticeConfig>> noticeConfigs = new HashMap<>();
//                for (int i = 0; i < configs.size(); i++) {
//                    PaymentNoticeConfig config = configs.get(i);
//                    if (noticeConfigs.containsKey(config.getOwnerId())) {
//                        noticeConfigs.get(config.getOwnerId()).add(config);
//                    } else {
//                        List<PaymentNoticeConfig> configList = new ArrayList<>();
//                        configList.add(config);
//                        noticeConfigs.put(config.getOwnerId(), configList);
//                    }
//                }
//                Map<Long, PaymentBills> needNoticeBills = new HashMap<>();
//                Map<Long, PaymentNoticeConfig> noticeConfigMap = new HashMap<>();
//                // noticeConfig map中存有communityid和notice days
//                for (Map.Entry<Long, List<PaymentNoticeConfig>> map : noticeConfigs.entrySet()) {
//                    List<PaymentBills> bills = assetProvider.getAllBillsByCommunity(namespaceId,map.getKey());
//                    for (int i = 0; i < bills.size(); i++) {
//                        PaymentBills bill = bills.get(i);
//                        if (!needNoticeBills.containsKey(bill.getId())) {催缴短信发送失败
//                            List<PaymentNoticeConfig> days = map.getValue();
//                            for (int j = 0; j < days.size(); j++) {
//                                PaymentNoticeConfig day = days.get(j);
//                                String dueDayDeadline = bill.getDueDayDeadline();
//                                try {
//                                    //比较此config与账单的时间，看是否应该催缴
//                                    Calendar deadline = newClearedCalendar();
//                                    deadline.setTime(yyyyMMdd.parse(dueDayDeadline));
//                                    if(day.getNoticeDayType() != null && day.getNoticeDayType().byteValue() == NoticeDayType.AFTER.getCode()){
//                                        deadline.add(Calendar.DAY_OF_MONTH, day.getNoticeDayAfter());
//                                    }else{
//                                        deadline.add(Calendar.DAY_OF_MONTH, -day.getNoticeDayBefore());
//                                    }
//                                    //符合催缴的日期设定就催缴
//                                    String todayInDate = yyyyMMdd.format(today.getTime());
//                                    String deadlineInDate = yyyyMMdd.format(deadline.getTime());
//                                    if (todayInDate.equalsIgnoreCase(deadlineInDate)) {
//                                        needNoticeBills.put(bill.getId(), bill);
//                                        noticeConfigMap.put(bill.getId(), day);
//                                    }
//                                } catch (Exception e) {
//                                    continue;
//                                }
//                            }
//                        }
//                    }
//                }
//                for (Map.Entry<Long, PaymentBills> entry : needNoticeBills.entrySet()) {
//                    List<Long> billIds = new ArrayList<>();
//                    Set<NoticeInfo> noticeInfoList = new HashSet<>();
//                    PaymentBills b = entry.getValue();
//                    billIds.add(b.getId());
//                    NoticeInfo info = new NoticeInfo();
//                    info.setDateStr(b.getDateStr());
//                    info.setTargetName(b.getTargetName());
//                    info.setAmountOwed(b.getAmountOwed());
//                    info.setAmountRecevable(b.getAmountReceivable());
//                    info.setAppName(assetProvider.findAppName(b.getNamespaceId()));
//                    info.setOwnerId(b.getOwnerId());
//                    info.setOwnerType(b.getOwnerType());
//                    //增加模板id by wentian sama @ 2018.5.9   u lies to me~
//                    info.setUseTemplate(true);
//                    PaymentNoticeConfig specificConfig = noticeConfigMap.get(entry.getKey());
//                    info.setMsgTemplateId(specificConfig.getNoticeMsgId());
//                    info.setAppTemplateId(specificConfig.getNoticeAppId());
//                    if(info.getMsgTemplateId() == null && info.getAppTemplateId() == null){
//                        continue;
//                    }
//                    @SuppressWarnings("unchecked")
//					List<NoticeObj> noticeObjs = (List<NoticeObj>)new Gson()
//                            .fromJson(specificConfig.getNoticeObjs(), new TypeToken<List<NoticeObj>>() {
//                            }.getType());
//                    if(noticeObjs == null){
//                        noticeObjs = new ArrayList<>();
//                    }
////                    info.setNoticeObjs(noticeObjs);
//                    //根据催缴账单催缴
//                    info.setPhoneNums(b.getNoticetel());
//                    info.setTargetType(b.getTargetType());
//                    info.setTargetId(b.getTargetId());
//                    // 增加域空间信息
//                    info.setNamespaceId(b.getNamespaceId());
//                    noticeInfoList.add(info);
//                    //待发送人员如如果是定义好的，之类就转成个人，再来一个info
//                    List<NoticeMemberIdAndContact> userIds = new ArrayList<>();
//                    for (NoticeObj obj : noticeObjs) {
//                        Long noticeObjId = obj.getNoticeObjId();
//                        String noticeObjType = obj.getNoticeObjType();
//                        FlowUserSourceType sourceTypeA = FlowUserSourceType.fromCode(noticeObjType);
//                        if(sourceTypeA == null){
//                            LOGGER.error("sourceType faild to fromcode, noticeObjType={},noticeConfigMap.getConfigId={}"
//                                    ,noticeObjType, noticeConfigMap.get(b.getId()).getId());
//                            continue;
//                        }
//                        switch (sourceTypeA) {
//                            // 具体部门
//                            case SOURCE_DEPARTMENT:
//                                userIds.addAll(getAllMembersFromDepartment(noticeObjId, "USER","UNTRACK"));
//                                break;
//                            case SOURCE_USER:
//                                NoticeMemberIdAndContact c = new NoticeMemberIdAndContact();
//                                c.setTargetId(noticeObjId);
//                                c.setContactToken(userProvider.findUserTokenOfUser(noticeObjId));
//                                userIds.add(c);
//                                break;
//							default:
//								break;
//                        }
//                    }
//
//                    //组织架构中选择的部门或者个人用户也进行发送短信，注意，概念上来讲这些是通知对象，不是催缴对象 by wentian @2018/5/10
//                    for(NoticeMemberIdAndContact uid : userIds){
//                        try {
//                            if(uid.getTargetId() == info.getTargetId()){
//                                continue;
//                            }
//                            NoticeInfo newInfo = CopyUtils.deepCopy(info);
//                            newInfo.setTargetId(uid.getTargetId());
//                            newInfo.setPhoneNums(uid.getContactToken());
//                            newInfo.setTargetType(AssetPaymentStrings.EH_USER);
//                            noticeInfoList.add(newInfo);
//                        } catch (Exception e) {
//                            LOGGER.error("failed to have a new notice info, new info is ={}",info,e);
//                        }
//                    }
//                    //一个一个发, billIds的size只有一
//                    LOGGER.info("billIds size should be one, now = {}",billIds.size());
//                    NoticeWithTextAndMessage(billIds, new ArrayList<>(noticeInfoList));
//                }
//                LOGGER.info("done");
//            });
//        }
//    }


    @Override
    public PreOrderDTO placeAnAssetOrder(CreatePaymentBillOrderCommand cmd) {
        AssetVendor vendor = checkAssetVendor(cmd.getNamespaceId(),0);
        AssetVendorHandler handler = getAssetVendorHandler(vendor.getVendorName());
        cmd.setSourceType(SourceType.MOBILE.getCode());
        return handler.createOrder(cmd);
    }

    @Override
    public List<ListChargingItemsDTO> listAvailableChargingItems(OwnerIdentityCommand cmd) {
    	if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
        }
        if(cmd.getOwnerType() == null) {
        	cmd.setOwnerType("community");
        }
        // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        if(cmd.getModuleId() != null && cmd.getModuleId().longValue() != ServiceModuleConstants.ASSET_MODULE){
           // 转换
            Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l, cmd.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
            cmd.setCategoryId(assetCategoryId);
        }
        return assetProvider.listAvailableChargingItems(cmd);
    }

    public List<Long> getAllCommunity(Integer namespaceId, Long organizationId, Long appId, boolean includeNamespace) {
        List<Long> communityIds = new ArrayList<>();
        communityIds =  organizationService.getOrganizationProjectIdsByAppId(organizationId, appId);
        if(includeNamespace){
            communityIds.add(namespaceId.longValue());
        }
        return communityIds;
    }

//    private boolean checkSafeDeleteId(String name, Long chargingStandardId,String ownerType,Long ownerId) {
//        Boolean safe = false;
//        if(Tables.EH_PAYMENT_CHARGING_STANDARDS.getName().equals(name)){
//            boolean exist = assetProvider.cheackGroupRuleExistByChargingStandard(chargingStandardId,ownerType,ownerId);
//            if(!exist) safe = true;
//        }
//        return safe;
//    }

    @Override
    public List<ListAvailableVariablesDTO> listAvailableVariables(ListAvailableVariablesCommand cmd) {
        return assetProvider.listAvailableVariables(cmd);
    }

//    private void setCmdCategoryDefault(Object obj, Long v){
//        Class clz = obj.getClass();
//        try {
//            Method method = clz.getDeclaredMethod("setCategoryId", new Class[]{Long.class});
//            method.invoke(obj, v);
//        } catch (Exception  e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void adjustBillGroupOrder(AdjustBillGroupOrderCommand cmd) {
        assetProvider.adjustBillGroupOrder(cmd.getSubjectBillGroupId(),cmd.getTargetBillGroupId());
    }

    @Override
    public ListChargingItemsForBillGroupResponse listChargingItemsForBillGroup(BillGroupIdCommand cmd) {
        ListChargingItemsForBillGroupResponse response = new ListChargingItemsForBillGroupResponse();
        if(cmd.getPageSize()==null){
            cmd.setPageSize(100);
        }
        if(cmd.getPageAnchor()== null){
            cmd.setPageAnchor(0l);
        }
        List<ListChargingItemsForBillGroupDTO> list =  assetProvider.listChargingItemsForBillGroup(cmd.getBillGroupId(),cmd.getPageAnchor(),cmd.getPageSize());
        if(list.size() > cmd.getPageSize()){
            response.setNextPageAnchor(cmd.getPageAnchor()+cmd.getPageSize().longValue());
            list.remove(list.size()-1);
        }else{
            response.setNextPageAnchor(null);
        }
        response.setList(list);
        return response;
    }

    @Override
    public DeleteChargingItemForBillGroupResponse deleteChargingItemForBillGroup(BillGroupRuleIdCommand cmd) {
        //获得此rule，ownerid = namesapceid，则在全部操作页面, 修改全部的+bro；如果ownerid != namespaceid，则在single，需要改变，并 不需要 解耦所有账单组合rule
        EhPaymentBillGroupsRules rule = assetGroupProvider.findBillGroupRuleById(cmd.getBillGroupRuleId());
        //1、物业缴费V6.6统一账单：如果该账单组中的费项被其他模块应用选中了，则不允许删除
        boolean workFlag = assetProvider.checkIsUsedByGeneralBill(rule.getBillGroupId(), rule.getChargingItemId());
        if(workFlag) {
        	DeleteChargingItemForBillGroupResponse response = new DeleteChargingItemForBillGroupResponse();
        	response.setFailCause(AssetPaymentConstants.DELETE_GROUP_RULE_UNSAFE);
        	return response;
        }
        //2、缴费模块原来的判断是否可以删除逻辑
        byte deCouplingFlag = 1;
        if(rule.getOwnerid().intValue() == rule.getNamespaceId().intValue()){
            //全部
            deCouplingFlag = 0;
            return assetGroupProvider.deleteBillGroupRuleById(cmd.getBillGroupRuleId(),deCouplingFlag);
        }else{
            return assetGroupProvider.deleteBillGroupRuleById(cmd.getBillGroupRuleId(),deCouplingFlag);
        }
    }

    @Override
    public ListChargingItemDetailForBillGroupDTO listChargingItemDetailForBillGroup(BillGroupRuleIdCommand cmd) {
        return assetProvider.listChargingItemDetailForBillGroup(cmd.getBillGroupRuleId());
    }

//    private boolean isInWorkGroup(Long billGroupId, boolean b) {
//        return assetProvider.checkBillsByBillGroupId(billGroupId);
//    }
//
//    private boolean isInWorkGroupRule(EhPaymentBillGroupsRules rule, boolean b) {
//        return assetProvider.isInWorkGroupRule(rule);
//    }

    public void checkNullProhibit(String name , Object object) {
        if(object == null) {
            LOGGER.error(name + " cannot be null");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    name+ " cannot be null");
        }
    }

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

//    @Override
//    public List<AssetBillTemplateFieldDTO> listAssetBillTemplate(ListAssetBillTemplateCommand cmd) {
//
//        List<AssetBillTemplateFieldDTO> dtos = new ArrayList<>();
//        Long templateVersion = assetProvider.getTemplateVersion(cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetId(),cmd.getTargetType());
//        if(templateVersion == 0L) {
//            dtos = assetProvider.findTemplateFieldByTemplateVersion(0L, cmd.getOwnerType(), 0L, cmd.getTargetType(), 0L);
//        } else {
//            dtos = assetProvider.findTemplateFieldByTemplateVersion(
//                    cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType(), templateVersion);
//        }
//
//        return dtos;
//    }

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

    public AssetVendor checkAssetVendor(Integer namespaceId,Integer defaultNamespaceId){
        if(null == namespaceId) {
            LOGGER.error("checkAssetVendor namespaceId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "checkAssetVendor namespaceId cannot be null.");
        }
        AssetVendor assetVendor = assetProvider.findAssetVendorByNamespace(namespaceId);
        if(null == assetVendor && defaultNamespaceId!=null)  assetVendor = assetProvider.findAssetVendorByNamespace(defaultNamespaceId);
        if(null == assetVendor) {
            LOGGER.error("assetVendor not found, assetVendor namespaceId={}, targetId={}", namespaceId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "assetVendor not found");
        }
        return assetVendor;
    }

    public AssetVendorHandler getAssetVendorHandler(String vendorName) {
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

//    @Override
//    public HttpServletResponse exportAssetBills(ListSimpleAssetBillsCommand cmd, HttpServletResponse response) {
//        Integer pageSize = Integer.MAX_VALUE;
//        cmd.setPageSize(pageSize);
//
//        ListSimpleAssetBillsResponse billResponse = listSimpleAssetBills(cmd);
//        List<SimpleAssetBillDTO> dtos = billResponse.getBills();
//
//        URL rootPath = RentalServiceImpl.class.getResource("/");
//        String filePath =rootPath.getPath() + this.downloadDir ;
//        File file = new File(filePath);
//        if(!file.exists())
//            file.mkdirs();
//        filePath = filePath + "AssetBills"+System.currentTimeMillis()+".xlsx";
//        //新建了一个文件
//        this.createAssetBillsBook(filePath, dtos);
//
//        return download(filePath,response);
//    }

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
            //String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

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

//    @Override
//    public ImportDataResponse importAssetBills(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
//        ListAssetBillTemplateCommand command = ConvertHelper.convert(cmd, ListAssetBillTemplateCommand.class);
//        Map<Long,List<Field>> fieldMap = getTemplateFields(command);
//        Long templateVersion = 0L;
//        List<Field> fields = new ArrayList<Field>();
//        if(fieldMap.keySet().size() > 0) {
//            templateVersion = fieldMap.keySet().iterator().next();
//            fields = fieldMap.get(templateVersion);
//        }
//
//
//        ImportDataResponse importDataResponse = new ImportDataResponse();
//        try {
//            //解析excel
//            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());
//
//            if(null == resultList || resultList.isEmpty()){
//                LOGGER.error("File content is empty，userId="+userId);
//                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
//                        "File content is empty");
//            }
//            LOGGER.debug("Start import data...,total:" + resultList.size());
//
//            List<String> errorDataLogs = importAssetBills(cmd, convertToStrList(resultList), fields, userId, templateVersion);
//
//
//            LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
//            if(null == errorDataLogs || errorDataLogs.isEmpty()){
//                LOGGER.debug("Data import all success...");
//            }else{
//                //记录导入错误日志
//                for (String log : errorDataLogs) {
//                    LOGGER.error(log);
//                }
//            }
//
//            importDataResponse.setTotalCount((long)resultList.size()-1);
//            importDataResponse.setFailCount((long)errorDataLogs.size());
//            importDataResponse.setLogs(errorDataLogs);
//        } catch (IOException e) {
//            LOGGER.error("File can not be resolved...");
//            e.printStackTrace();
//        }
//        return importDataResponse;
//    }

//    private List<String> convertToStrList(List list) {
//        List<String> result = new ArrayList<String>();
//        boolean firstRow = true;
//        for (Object o : list) {
//            if(firstRow){
//                firstRow = false;
//                continue;
//            }
//            RowResult r = (RowResult)o;
//            StringBuffer sb = new StringBuffer();
//            sb.append(r.getA()).append("||");
//            sb.append(r.getB()).append("||");
//            sb.append(r.getC()).append("||");
//            sb.append(r.getD()).append("||");
//            sb.append(r.getE()).append("||");
//            sb.append(r.getF()).append("||");
//            sb.append(r.getG()).append("||");
//            sb.append(r.getH()).append("||");
//            sb.append(r.getI()).append("||");
//            sb.append(r.getJ()).append("||");
//            sb.append(r.getK()).append("||");
//            sb.append(r.getL()).append("||");
//            sb.append(r.getM()).append("||");
//            sb.append(r.getN()).append("||");
//            sb.append(r.getO()).append("||");
//            sb.append(r.getP()).append("||");
//            sb.append(r.getQ()).append("||");
//            sb.append(r.getR()).append("||");
//            sb.append(r.getS()).append("||");
//            sb.append(r.getT()).append("||");
//            sb.append(r.getU()).append("||");
//            sb.append(r.getV()).append("||");
//            sb.append(r.getW()).append("||");
//            sb.append(r.getX()).append("||");
//
//
//            result.add(sb.toString());
//        }
//        return result;
//    }

//    private List<String> importAssetBills(ImportOwnerCommand cmd, List<String> list, List<Field> fields, Long userId, Long templateVersion){
//        List<String> errorDataLogs = new ArrayList<String>();
//
////        Integer namespaceId = UserContext.getCurrentNamespaceId();
//        for (String str : list) {
//            String[] s = str.split("\\|\\|");
//            dbProvider.execute((TransactionStatus status) -> {
//                CreatAssetBillCommand bill = new CreatAssetBillCommand();
//                bill.setOwnerId(cmd.getOwnerId());
//                bill.setOwnerType(cmd.getOwnerType());
//                bill.setTargetId(cmd.getTargetId());
//                bill.setTargetType(cmd.getTargetType());
//                bill.setTemplateVersion(templateVersion);
//                bill.setSource(AssetBillSource.THIRD_PARTY.getCode());
//                int i = 0;
//                for(Field field : fields) {
//                    try {
//                        field.setAccessible(true);
//                        if("class java.sql.Timestamp".equals(field.getType().toString())) {
//                            field.set(bill, covertStrToTimestamp(s[i]));
//                        } else if("class java.math.BigDecimal".equals(field.getType().toString())) {
//                            if(s[i] != null && !"null".equals(s[i])) {
//                                field.set(bill, new BigDecimal(s[i]));
//                            }
//
//                        } else {
//                            field.set(bill, field.getType().getConstructor(field.getType()).newInstance(s[i]));
//                        }
//
//                        i++;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                creatAssetBill(bill);
//                return null;
//            });
//        }
//        return errorDataLogs;
//
//    }

//    private Timestamp covertStrToTimestamp(String str) {
//        String formatStr = configurationProvider.getValue("asset.accountperiod.format", "yyyyMMdd");
//        SimpleDateFormat format = new SimpleDateFormat(formatStr);
//        try {
//            Date date=format.parse(str);
//            return new Timestamp(date.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    //非当月则没有滞纳金 所以数据库里面不加lateFee
//    private void getTotalAmount(AssetBill bill) {
//        BigDecimal rental = bill.getRental() == null ? new BigDecimal(0) : bill.getRental();
//        BigDecimal propertyManagementFee = bill.getPropertyManagementFee() == null ? new BigDecimal(0) : bill.getPropertyManagementFee();
//        BigDecimal unitMaintenanceFund = bill.getUnitMaintenanceFund() == null ? new BigDecimal(0) : bill.getUnitMaintenanceFund();
//        BigDecimal privateWaterFee = bill.getPrivateWaterFee() == null ? new BigDecimal(0) : bill.getPrivateWaterFee();
//        BigDecimal privateElectricityFee = bill.getPrivateElectricityFee() == null ? new BigDecimal(0) : bill.getPrivateElectricityFee();
//        BigDecimal publicWaterFee = bill.getPublicWaterFee() == null ? new BigDecimal(0) : bill.getPublicWaterFee();
//        BigDecimal publicElectricityFee = bill.getPublicElectricityFee() == null ? new BigDecimal(0) : bill.getPublicElectricityFee();
//        BigDecimal wasteDisposalFee = bill.getWasteDisposalFee() == null ? new BigDecimal(0) : bill.getWasteDisposalFee();
//        BigDecimal pollutionDischargeFee = bill.getPollutionDischargeFee() == null ? new BigDecimal(0) : bill.getPollutionDischargeFee();
//        BigDecimal extraAirConditionFee = bill.getExtraAirConditionFee() == null ? new BigDecimal(0) : bill.getExtraAirConditionFee();
//        BigDecimal coolingWaterFee = bill.getCoolingWaterFee() == null ? new BigDecimal(0) : bill.getCoolingWaterFee();
//        BigDecimal weakCurrentSlotFee = bill.getWeakCurrentSlotFee() == null ? new BigDecimal(0) : bill.getWeakCurrentSlotFee();
//        BigDecimal depositFromLease = bill.getDepositFromLease() == null ? new BigDecimal(0) : bill.getDepositFromLease();
//        BigDecimal maintenanceFee = bill.getMaintenanceFee() == null ? new BigDecimal(0) : bill.getMaintenanceFee();
//        BigDecimal gasOilProcessFee = bill.getGasOilProcessFee() == null ? new BigDecimal(0) : bill.getGasOilProcessFee();
//        BigDecimal hatchServiceFee = bill.getHatchServiceFee() == null ? new BigDecimal(0) : bill.getHatchServiceFee();
//        BigDecimal pressurizedFee = bill.getPressurizedFee() == null ? new BigDecimal(0) : bill.getPressurizedFee();
//        BigDecimal parkingFee = bill.getParkingFee() == null ? new BigDecimal(0) : bill.getParkingFee();
//        BigDecimal other = bill.getOther() == null ? new BigDecimal(0) : bill.getOther();
//
//        BigDecimal periodAccountAmount = rental.add(propertyManagementFee).add(unitMaintenanceFund)
//                .add(privateWaterFee).add(privateElectricityFee).add(publicWaterFee)
//                .add(publicElectricityFee).add(wasteDisposalFee).add(pollutionDischargeFee)
//                .add(extraAirConditionFee).add(coolingWaterFee).add(weakCurrentSlotFee)
//                .add(depositFromLease).add(maintenanceFee).add(gasOilProcessFee)
//                .add(hatchServiceFee).add(pressurizedFee).add(parkingFee).add(other);
//
//        bill.setPeriodAccountAmount(periodAccountAmount);
//        bill.setPeriodUnpaidAccountAmount(bill.getPeriodAccountAmount());
//
//    }

//    @Override
//    public AssetBillTemplateValueDTO creatAssetBill(CreatAssetBillCommand cmd) {
//        //校验创建账单的权限
//        checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(), PrivilegeConstants.ASSET_MANAGEMENT_CREATE,cmd.getOrganizationId());
//        AssetBill bill = ConvertHelper.convert(cmd, AssetBill.class);
//        bill.setAccountPeriod(new Timestamp(cmd.getAccountPeriod()));
//        bill.setSource(AssetBillSource.MANUAL.getCode());
//        bill.setCreatorUid(UserContext.current().getUser().getId());
//        getTotalAmount(bill);
//
//        Integer namespaceId = UserContext.getCurrentNamespaceId();
//        bill.setNamespaceId(namespaceId);
//        if(cmd.getAddressId() == null) {
//            Address address = addressProvider.findApartmentAddress(namespaceId, cmd.getTargetId(), cmd.getBuildingName(), cmd.getApartmentName());
//            cmd.setAddressId(address.getId());
//            bill.setAddressId(address.getId());
//        }
//        Community community = communityProvider.findCommunityById(cmd.getTargetId());
//        //园区 查公司表
//        if(CommunityType.COMMERCIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
//
//            OrganizationAddress organizationAddress = organizationProvider.findActiveOrganizationAddressByAddressId(cmd.getAddressId());
//            if(organizationAddress != null) {
//                bill.setTenantId(organizationAddress.getOrganizationId());
//                bill.setTenantType(TenantType.ENTERPRISE.getCode());
//            }
//        }
//        //小区 查家庭
//        else if(CommunityType.RESIDENTIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
//            Family family = familyProvider.findFamilyByAddressId(cmd.getAddressId());
//            if(family != null) {
//                bill.setTenantId(family.getId());
//                bill.setTenantType(TenantType.FAMILY.getCode());
//            }
//        }
//        assetProvider.creatAssetBill(bill);
//
//        FindAssetBillCommand command = new FindAssetBillCommand();
//        command.setId(bill.getId());
//        command.setOwnerId(bill.getOwnerId());
//        command.setOwnerType(bill.getOwnerType());
//        command.setTargetId(bill.getTargetId());
//        command.setTargetType(bill.getTargetType());
//        command.setTemplateVersion(bill.getTemplateVersion());
//        AssetBillTemplateValueDTO dto = findAssetBill(command);
//
//        return dto;
//    }

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

//    private AssetBill getAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType) {
//        AssetBill bill = assetProvider.findAssetBill(id, ownerId, ownerType, targetId, targetType);
//
//        if (bill == null) {
//            LOGGER.error("cannot find asset bill. bill: id = " + id + ", ownerId = " + ownerId
//                    + ", ownerType = " + ownerType + ", targetId = " + targetId + ", targetType = " + targetType);
//            throw RuntimeErrorException.errorWith(AssetServiceErrorCode.SCOPE,
//                    AssetServiceErrorCode.ASSET_BILL_NOT_EXIST,
//                    "账单不存在");
//        }
//
//        return bill;
//    }

//    @Override
//    public AssetBillTemplateValueDTO updateAssetBill(UpdateAssetBillCommand cmd) {
//        AssetBill bill = getAssetBill(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());
//
//        bill = ConvertHelper.convert(cmd, AssetBill.class);
//
//        bill.setAccountPeriod(new Timestamp(cmd.getAccountPeriod()));
//        bill.setUpdateUid(UserContext.current().getUser().getId());
//        getTotalAmount(bill);
//        assetProvider.updateAssetBill(bill);
//
//        FindAssetBillCommand command = new FindAssetBillCommand();
//        command.setId(bill.getId());
//        command.setOwnerId(bill.getOwnerId());
//        command.setOwnerType(bill.getOwnerType());
//        command.setTargetId(bill.getTargetId());
//        command.setTargetType(bill.getTargetType());
//        command.setTemplateVersion(bill.getTemplateVersion());
//        AssetBillTemplateValueDTO dto = findAssetBill(command);
//
//        return dto;
//    }

//    @Override
//    public void notifyUnpaidBillsContact(NotifyUnpaidBillsContactCommand cmd) {
//        //只要有未缴账单就推送 但根据租户信息 一个租户在一个园区多月未缴 有多个地址未缴 只推一条
//
//        List<AssetBill> bills = assetProvider.listUnpaidBillsGroupByTenant(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());
//
//        if(bills != null && bills.size() > 0) {
//            Integer namespaceId = UserContext.getCurrentNamespaceId();
//            LocaleString localeString = localeStringProvider.find(AssetServiceErrorCode.SCOPE, AssetServiceErrorCode.NOTIFY_FEE,
//                    "zh_CN");
//            String content = localeString.getText();
//            if(LOGGER.isDebugEnabled()) {
//                LOGGER.debug("unpaid bills = {}", bills);
//            }
//
//            for(AssetBill bill : bills) {
//                if (bill.getContactNo() != null) {
//                    UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(namespaceId, bill.getContactNo());
//                    if (identifier != null) {
//                        sendMessageToUser(identifier.getOwnerUid(), content);
//                    }
//                } else {
//                    //没有contactNo的家庭 通知所有家庭成员
//                    if (TenantType.FAMILY.equals(TenantType.fromCode(bill.getTenantType()))) {
//                        List<GroupMember> groupMembers = groupProvider.findGroupMemberByGroupId(bill.getTenantId());
//                        if (groupMembers != null && groupMembers.size() > 0) {
//                            for(GroupMember groupMember : groupMembers) {
//                                if (EntityType.USER.equals(EntityType.fromCode(groupMember.getMemberType()))) {
//                                    sendMessageToUser(groupMember.getMemberId(), content);
//                                }
//                            }
//                        }
//                    }
//
//                    //没有contactNo的企业 通知所有企业管理员
//                    if (TenantType.ENTERPRISE.equals(TenantType.fromCode(bill.getTenantType()))) {
//                        ListServiceModuleAdministratorsCommand command = new ListServiceModuleAdministratorsCommand();
//                        command.setOwnerId(bill.getTenantId());
//                        command.setOwnerType(EntityType.ORGANIZATIONS.getCode());
//                        command.setOrganizationId(bill.getTenantId());
//                        List<OrganizationContactDTO> orgContact = rolePrivilegeService.listOrganizationAdministrators(command);
//                        if (orgContact != null && orgContact.size() > 0) {
//                            for(OrganizationContactDTO contact : orgContact) {
//                                if (OrganizationMemberTargetType.USER.equals(OrganizationMemberTargetType.fromCode(contact.getTargetType()))) {
//                                    sendMessageToUser(contact.getTargetId(), content);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//    private void sendMessageToUser(Long userId, String content) {
//        if(LOGGER.isDebugEnabled()) {
//            LOGGER.debug("notify asset bills: userId = {}, content = {}", userId, content);
//        }
//
//        MessageDTO messageDto = new MessageDTO();
//        messageDto.setAppId(AppConstants.APPID_MESSAGING);
//        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
//        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
//        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
//        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
//        messageDto.setBody(content);
//        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
//
//        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
//                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
//    }

//    @Override
//    public void setBillsStatus(BillIdListCommand cmd, AssetBillStatus status) {
//        if(cmd.getIds() != null && cmd.getIds().size() > 0) {
//            for(Long id : cmd.getIds()) {
//                AssetBill bill = assetProvider.findAssetBill(id, cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());
//
//                if(bill != null) {
//                    bill.setStatus(status.getCode());
//                    bill.setUpdateUid(UserContext.current().getUser().getId());
//                    assetProvider.updateAssetBill(bill);
//                }
//
//            }
//        }
//
//    }

//    @Override
//    public void deleteBill(DeleteBillCommand cmd) {
//        AssetBill bill = getAssetBill(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());
//
//        bill.setStatus(AssetBillStatus.INACTIVE.getCode());
//        bill.setUpdateUid(UserContext.current().getUser().getId());
//        bill.setDeleteUid(UserContext.current().getUser().getId());
//        bill.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//
//        assetProvider.updateAssetBill(bill);
//    }

//    @Override
//    public List<AssetBillTemplateFieldDTO> updateAssetBillTemplate(UpdateAssetBillTemplateCommand cmd) {
//
//        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ASSET_BILL_TEMPLATE.getCode()).tryEnter(()-> {
//            if(cmd.getDtos() != null && cmd.getDtos().size() > 0) {
//                for(AssetBillTemplateFieldDTO dto : cmd.getDtos()) {
//                    AssetBillTemplateFields field = ConvertHelper.convert(dto, AssetBillTemplateFields.class);
//                    field.setTemplateVersion(field.getTemplateVersion() + 1);
//                    assetProvider.creatTemplateField(field);
//                }
//            }
//        });
//
//        ListAssetBillTemplateCommand command = new ListAssetBillTemplateCommand();
//        command.setOwnerType(cmd.getDtos().get(0).getOwnerType());
//        command.setOwnerId(cmd.getDtos().get(0).getOwnerId());
//        command.setTargetType(cmd.getDtos().get(0).getTargetType());
//        command.setTargetId(cmd.getDtos().get(0).getTargetId());
//
//        List<AssetBillTemplateFieldDTO> dtos = listAssetBillTemplate(command);
//        return dtos;
//    }

    @Override
    public Boolean checkTokenRegister(CheckTokenRegisterCommand cmd) {
        UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), cmd.getContactNo());

        if(identifier == null) {
            return false;
        }

        return true;
    }

//    @Override
//    public NotifyTimesResponse notifyTimes(ImportOwnerCommand cmd) {
//        NotifyTimesResponse response = new NotifyTimesResponse();
//        long startTime = getTimesMonthmorning();
//        long endTime = getTimesMonthnight();
//
//        int count = assetProvider.countNotifyRecords(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType(),
//                new Timestamp(startTime), new Timestamp(endTime));
//        response.setNotifyTimes(count);
//
//        AssetBillNotifyRecords lastRecord = assetProvider.getLastAssetBillNotifyRecords(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());
//
//        if(lastRecord != null) {
//            response.setLastNotifyTime(lastRecord.getCreateTime());
//        }
//        return response;
//    }

    @Override
    public AssetBillStatDTO getAssetBillStat(GetAssetBillStatCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(cmd.getTargetType(), cmd.getTargetId());

        String vendor = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendor);

        AssetBillStatDTO dto = handler.getAssetBillStat(cmd.getTenantType(), cmd.getTenantId(), cmd.getAddressId());
        return dto;
    }

//    //获得本月第一天0点时间
//    private long getTimesMonthmorning(){
//        Calendar cal = newClearedCalendar();
//        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
//        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
//        return cal.getTimeInMillis();
//    }
//    //获得本月最后一天24点时间
//    private long getTimesMonthnight() {
//        Calendar cal = newClearedCalendar();
//        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//        cal.set(Calendar.HOUR_OF_DAY, 24);
//        return cal.getTimeInMillis();
//    }

//    private Map<Long, List<Field>> getTemplateFields(ListAssetBillTemplateCommand cmd) {
//        List<Field> fields = new ArrayList<>();
//        Map<Long, List<Field>> fieldMap = new HashMap<Long, List<Field>>();
//        List<AssetBillTemplateFieldDTO> dtos = listAssetBillTemplate(cmd);
//        if(dtos != null && dtos.size() > 0) {
//            Class c=CreatAssetBillCommand.class;
//            try {
//                Long templateVersion = 0L;
//                for(AssetBillTemplateFieldDTO dto : dtos) {
//                    if(AssetBillTemplateSelectedFlag.SELECTED.equals(AssetBillTemplateSelectedFlag.fromCode(dto.getSelectedFlag()))) {
//                        templateVersion = dto.getTemplateVersion();
//                        Field field = c.getDeclaredField(dto.getFieldName());
//                        fields.add(field);
//                    }
//                }
//                fieldMap.put(templateVersion, fields);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return fieldMap;
//    }

	//线下缴费场景，显示付费凭证图片
	@Override
	public UploadCertificateInfoDTO listUploadCertificates(ListUploadCertificatesCommand cmd) {
		UploadCertificateInfoDTO uploadCertificateInfoDTO = new UploadCertificateInfoDTO();
		//获取账单对应的各凭证图片信息
		List<PaymentBillCertificate> paymentBillCertificateList = assetProvider.listUploadCertificates(cmd.getBillId());
		List<UploadCertificateDTO> uploadCertificateDTOList = paymentBillCertificateList.stream().map(r->{
			UploadCertificateDTO dto = new UploadCertificateDTO();
			dto.setUri(r.getCertificateUri());
			//根据uri来获取url
			String url = contentServerService.parserUri(dto.getUri(),EntityType.DOMAIN.getCode(),cmd.getBillId());
			dto.setUrl(url);
			return dto;
		}).collect(Collectors.toList());
		uploadCertificateInfoDTO.setUploadCertificateDTOList(uploadCertificateDTOList);
		//获取账单对应的上传凭证时的留言
		String certificateNote = assetProvider.getCertificateNote(cmd.getBillId());
		uploadCertificateInfoDTO.setCertificateNote(certificateNote);
		
		return uploadCertificateInfoDTO;
	}
	
	//线下缴费场景，上传缴费凭证
	@Override
	public UploadCertificateInfoDTO uploadCertificate(UploadCertificateCommand cmd) {
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("upload certificate for bill: billId = {}", cmd.getBillId());
        }
		//除去前端传过来的空uri字符串
		if (cmd.getCertificateUris()!=null) {
			List<String> uris = cmd.getCertificateUris();
			Iterator<String> it = uris.iterator();
			while (it.hasNext()) {
				String uri = (String) it.next();
				if (org.springframework.util.StringUtils.isEmpty(uri)) {
					it.remove();
				}
			}
		}
		//一次最多上传6张缴费凭证图片(范围为1-6张)
		if(cmd.getCertificateUris().size()==0 || cmd.getCertificateUris().size()>6){
			throw RuntimeErrorException.errorWith(AssetServiceErrorCode.SCOPE, 
					AssetServiceErrorCode.UPLOAD_CERTIFICATES_NUM_ERROR, "upload certificates num is out of range");
		}
		//判断该账单（paymentBill）已存在，若账单不存在则不做操作
		PaymentBills paymentBill = assetProvider.findPaymentBillById(cmd.getBillId());
		if (paymentBill==null) {
			throw RuntimeErrorException.errorWith(AssetServiceErrorCode.SCOPE, 
					AssetServiceErrorCode.ASSET_BILL_NOT_EXIST, "the bill does not exist");
		}
		//更新数据库中缴费凭证的相关信息
		assetProvider.updatePaymentBillCertificates(cmd.getBillId(),cmd.getCertificateNote(),cmd.getCertificateUris());
		//重新查询更新后的缴费凭证图片的uri和url传给前端
		ListUploadCertificatesCommand listUploadCertificatesCommand = new ListUploadCertificatesCommand();
		listUploadCertificatesCommand.setBillId(cmd.getBillId());
		UploadCertificateInfoDTO uploadCertificateInfoDTO = listUploadCertificates(listUploadCertificatesCommand);		
		
		return uploadCertificateInfoDTO;	
	}
	
	public JudgeAppShowPayResponse judgeAppShowPay(JudgeAppShowPayCommand cmd) {
		JudgeAppShowPayResponse judgeAppShowPayResponse = new JudgeAppShowPayResponse();
		String appShowPay = configurationProvider.getValue(cmd.getNamespaceId(), ConfigConstants.ASSET_DINGFENGHUI_APPSHOWPAY,""); 
		//如果根据域空间找不到，那么默认为2：全部缴
		if(appShowPay.isEmpty()) {
			judgeAppShowPayResponse.setAppShowPay(new Byte("2"));
		}else {
			judgeAppShowPayResponse.setAppShowPay(new Byte(appShowPay));
		}
		return judgeAppShowPayResponse;
	}
	
	//add by tangcen 2018年6月21日17:02:54
	/**
	 * costGenerationMethod:账单处理方式，0：按计费周期，1：按实际天数
	 * contractId:要处理的合同id
	 * endTime:合同实际结束时间
	 */
	@Override
	public void deleteUnsettledBillsOnContractId(Byte costGenerationMethod,Long contractId,Timestamp endTime) {
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
		String endTimeStr = yyyyMMdd.format(endTime);

		// 36363 账单为空，则不需要删除账单
		PaymentBills bill = assetProvider.findLastBill(contractId);
		if (bill == null) {
			return;
		}
		if (costGenerationMethod == (byte)0) {//按计费周期
			//PaymentBills bill = assetProvider.findLastBill(contractId);
			PaymentBillItems firstBillItemToDelete = assetProvider.findFirstBillItemToDelete(contractId, endTimeStr);
			//如果退约/变更日期产生的billitem刚好落在最后一个bill中，需要对最后一个bill进行重新计算，而不是直接删除
			//对应缴费项的生成规则：超过合同结束时间的billitem，不论billitem的开始时间是什么，全都会落在最后一个bill中
			if (firstBillItemToDelete.getBillId().equals(bill.getId())) {
				assetProvider.deleteBillItemsAfterDate(contractId, endTimeStr);
				List<PaymentBillItems> billItems = assetProvider.findBillItemsByBillId(bill.getId());
				dealUnsettledBillItems(billItems,endTime);
				bill.setAmountReceivable(new BigDecimal(0));
				bill.setAmountReceived(new BigDecimal(0));
				bill.setAmountOwed(new BigDecimal(0));
				for (PaymentBillItems billItem : billItems) {
					bill.setAmountReceivable(bill.getAmountReceivable().add(billItem.getAmountReceivable()));
					bill.setAmountReceived(bill.getAmountReceived().add(billItem.getAmountReceived()));
					bill.setAmountOwed(bill.getAmountOwed().add(billItem.getAmountOwed()));
				}
				assetProvider.updatePaymentBills(bill);
			}else {
				assetProvider.deleteUnsettledBills(contractId,endTimeStr);
			}
		}else if (costGenerationMethod == (byte)1) {//按实际天数
			//PaymentBills bill = assetProvider.findLastBill(contractId);
			PaymentBillItems firstBillItemToDelete = assetProvider.findFirstBillItemToDelete(contractId, endTimeStr);
			//如果退约/变更日期产生的billitem刚好落在最后一个bill中，需要对最后一个bill进行重新计算，而不是直接删除
			//对应缴费项的生成规则：超过合同结束时间的billitem，不论billitem的开始时间是什么，全都会落在最后一个bill中
			if (firstBillItemToDelete != null && firstBillItemToDelete.getBillId().equals(bill.getId())) {
				assetProvider.deleteBillItemsAfterDate(contractId, endTimeStr);
				List<PaymentBillItems> billItems = assetProvider.findBillItemsByBillId(bill.getId());
				dealUnsettledBillItems(billItems,endTime);
				bill.setAmountReceivable(new BigDecimal(0));
				bill.setAmountReceived(new BigDecimal(0));
				bill.setAmountOwed(new BigDecimal(0));
				for (PaymentBillItems billItem : billItems) {
					bill.setAmountReceivable(bill.getAmountReceivable().add(billItem.getAmountReceivable()));
					bill.setAmountReceived(bill.getAmountReceived().add(billItem.getAmountReceived()));
					bill.setAmountOwed(bill.getAmountOwed().add(billItem.getAmountOwed()));
					assetProvider.updatePaymentItem(billItem);
				}
				assetProvider.updatePaymentBills(bill);
			}else {
				assetProvider.deleteUnsettledBills(contractId,endTimeStr);
				PaymentBills lastBill = assetProvider.findLastBill(contractId);
				List<PaymentBillItems> billItems = assetProvider.findBillItemsByBillId(lastBill.getId());
				dealUnsettledBillItems(billItems,endTime);
				lastBill.setAmountReceivable(new BigDecimal(0));
				lastBill.setAmountReceived(new BigDecimal(0));
				lastBill.setAmountOwed(new BigDecimal(0));
				for (PaymentBillItems billItem : billItems) {
					lastBill.setAmountReceivable(lastBill.getAmountReceivable().add(billItem.getAmountReceivable()));
					lastBill.setAmountReceived(lastBill.getAmountReceived().add(billItem.getAmountReceived()));
					lastBill.setAmountOwed(lastBill.getAmountOwed().add(billItem.getAmountOwed()));
					assetProvider.updatePaymentItem(billItem);
				}
				assetProvider.updatePaymentBills(lastBill);
			}
		}
	}
	//计算截断后最近的一条未出账单明细  add by tangcen 2018年6月12日
	private void dealUnsettledBillItems(List<PaymentBillItems> billItems,Timestamp endTime){
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
		for (PaymentBillItems billItem : billItems) {
			try {
				Date dateBegin = yyyyMMdd.parse(billItem.getDateStrBegin());
				Date dateEnd = yyyyMMdd.parse(billItem.getDateStrEnd());
				if (dateEnd.getTime() >= endTime.getTime()) {
					int agreedPeriod = daysBetween_date(dateBegin, dateEnd);
					int actualPeriod = daysBetween_date(dateBegin,endTime);
					billItem.setAmountReceivable(calculateFee(agreedPeriod,actualPeriod,billItem.getAmountReceivable()));
					billItem.setAmountReceived(calculateFee(agreedPeriod,actualPeriod,billItem.getAmountReceived()));
					billItem.setAmountOwed(calculateFee(agreedPeriod,actualPeriod,billItem.getAmountOwed()));
					String actualDateStrEnd = yyyyMMdd.format(endTime);
					billItem.setDateStrEnd(actualDateStrEnd);
				}
			} catch (ParseException e) {
				if(LOGGER.isDebugEnabled()) {
		            LOGGER.error("parse date error!");
		        }
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
	                    "parse date error once");
			}
		}
	}
	//add by tangcen 2018年6月12日
	private BigDecimal calculateFee(int agreedPeriod, int actualPeriod, BigDecimal amount) {
		BigDecimal factor1 = new BigDecimal(actualPeriod);
		BigDecimal factor2 = new BigDecimal(agreedPeriod);
		BigDecimal factor = factor1.divide(factor2,4,BigDecimal.ROUND_FLOOR);
		return amount.multiply(factor).setScale(2, BigDecimal.ROUND_FLOOR);
	}

	public List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        //调接口从电商获取收款方账户
    	if(cmd.getCommunityId() == null || cmd.getCommunityId().equals(-1L)){
    		return handler.listBizPayeeAccounts(cmd.getOrganizationId(), "0");
    	}else {
    		return handler.listBizPayeeAccounts(cmd.getOrganizationId(), "0", String.valueOf(cmd.getCommunityId()));
    	}
    }

	public void payNotify(OrderPaymentNotificationCommand cmd) {
		AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.payNotify(cmd);
	}
	
	public ListPaymentBillResp listPaymentBill(ListPaymentBillCmd cmd){
		//权限校验
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getOrganizationId(), PrivilegeConstants.ASSET_DEAL_VIEW, PrivilegeConstants.ASSET_MODULE_ID, (byte)13, null, null, cmd.getCommunityId());
        ListPaymentBillResp response = new ListPaymentBillResp();
        //由于目前支付那边没有办法判断支付异常的情况，但是前端的订单状态查询条件大师说保留，所以如果查询订单异常默认返回空
        if(cmd.getPaymentStatus() != null && cmd.getPaymentStatus().equals(0)) {//订单状态：1：已完成，0：订单异常
        	response.setPaymentOrderBillDTOs(new ArrayList<PaymentOrderBillDTO>());
        	return response;
        }
        if(cmd.getNamespaceId() == null){
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }
        //修改传递参数为一个对象，卸货
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize;
        //卸货完毕
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(cmd.getPageSize() == null){
            pageSize = 20;
        }else {
        	pageSize = cmd.getPageSize().intValue();
        }
        Integer pageOffSet = pageAnchor.intValue();
        
        List<PaymentOrderBillDTO> list = assetProvider.listBillsForOrder(cmd.getNamespaceId(), pageOffSet, pageSize, cmd);
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor+pageSize.longValue());
            list.remove(list.size()-1);
        }
        response.setPaymentOrderBillDTOs(list);
        return response;
	}
	
	public IsProjectNavigateDefaultResp isProjectNavigateDefault(IsProjectNavigateDefaultCmd cmd) {
		IsProjectNavigateDefaultResp response = new IsProjectNavigateDefaultResp();
		if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){//ownerId为-1代表选择的是全部
			response.setDefaultStatus((byte)1);//1：代表使用的是默认配置，0：代表有做过个性化的修改
			return response;
        }
		if(cmd.getModuleType() != null && cmd.getModuleType().equals(AssetModuleType.CHARGING_ITEM.getCode())) {
			//收费项配置
			response = assetChargingItemProvider.isChargingItemsForJudgeDefault(cmd);
		}else if(cmd.getModuleType() != null && cmd.getModuleType().equals(AssetModuleType.CHARGING_STANDARDS.getCode())) {
			//收费项计算规则
			response = assetStandardProvider.isChargingStandardsForJudgeDefault(cmd);
		}else if(cmd.getModuleType() != null && cmd.getModuleType().equals(AssetModuleType.GROUPS.getCode())) {
			//账单组
			response = assetGroupProvider.isBillGroupsForJudgeDefault(cmd);
		}
		return response;
	}

	//issue 31594,计算天企汇历史合同的租赁总额字段
	@Override
	public void calculateRentForContract(CalculateRentCommand cmd){
		List<Contract> contractList = contractProvider.listContractByNamespaceId(cmd.getNamespaceId());
		if (contractList!=null && contractList.size()>0) {
			for (Contract contract : contractList) {
				if (contract.getRent()==null) {
					BigDecimal totalAmount = assetProvider.getBillExpectanciesAmountOnContract(contract.getContractNumber(),contract.getId(), null, null);
			        assetProvider.setRent(contract.getId(),totalAmount);
				}
			}
		}
	}

	public IsUserExistInAddressResponse isUserExistInAddress(IsUserExistInAddressCmd cmd) {
		String nickName = cmd.getUsername();
		Integer namespaceId = cmd.getNamespaceId();
		IsUserExistInAddressResponse response = new IsUserExistInAddressResponse();
		response.setIsExist((byte)0);//初始化
		CrossShardListingLocator locator = new CrossShardListingLocator();
		//通过个人客户名称以及域空间，查找出所有的个人客户
		List<User> users = userProvider.listUserByKeyword(null, null, null, nickName, null, namespaceId, locator, 9999999);
		//查找个人客户所有关联的楼栋门牌
		for(User user : users) {
			UserContext.setCurrentNamespaceId(cmd.getNamespaceId());
			UserContext.current().setUser(user);
			ListUserRelatedScenesCommand listUserRelatedScenesCommand = new ListUserRelatedScenesCommand();
        	List<SceneDTO> sceneDtoList = userService.listUserRelatedScenes(listUserRelatedScenesCommand);
        	List<Long> addressIds = new ArrayList<>();
        	//获取到个人客户所有关联的楼栋门牌
        	for(SceneDTO sceneDTO : sceneDtoList) {
        		//修复issue-32510 个人客户场景，审核中的门牌，该门牌关联了账单，期望不可以看到账单
        		if(sceneDTO != null && sceneDTO.getStatus() != null && sceneDTO.getStatus().equals((byte)3)) {//2:审核中，3：已认证
        			FamilyDTO familyDTO = (FamilyDTO) StringHelper.fromJsonString(sceneDTO.getEntityContent(), FamilyDTO.class);
            		addressIds.add(familyDTO.getAddressId());
        		}
        	}
        	//如果新建账单时，所有关联的楼栋门牌都被个人客户包含了，说明符合新增的规则
        	if(addressIds != null && cmd.getAddressIds() != null && addressIds.containsAll(cmd.getAddressIds())) {
        		response.setIsExist((byte)1);
        		break;
        	}
		}
		return response;
	}

    public PublicTransferBillRespForEnt publicTransferBillForEnt(PublicTransferBillCmdForEnt cmd){
//    	List<BillIdAndAmount> bills = new ArrayList<BillIdAndAmount>();
    	List<PaymentBillRequest> paymentBillRequests = cmd.getBillList();
        List<String> billIds = new ArrayList<>();
        Long amountsInCents = 0l;
        Float sumAmountOwed = 0f;
        StringBuffer orderExplain = new StringBuffer();//订单说明，如：2017-06物业费、2017-06租金
        for(PaymentBillRequest paymentBillRequest : paymentBillRequests){
            billIds.add(String.valueOf(paymentBillRequest.getBillId()));
            String amountOwed = String.valueOf(paymentBillRequest.getAmountOwed());
            Float amountOwedInCents = Float.parseFloat(amountOwed)*100f;
            amountsInCents += amountOwedInCents.longValue();
            sumAmountOwed += Float.parseFloat(amountOwed);
            orderExplain.append(paymentBillRequest.getDateStr() + paymentBillRequest.getBillGroupName() + "、");
        }
        //对左邻的用户，直接检查bill的状态即可
        checkHasPaidBills(billIds);
        //如果账单为新的，则进行存储
//        String payerType = cmd.getTargetType();//支付者的类型，eh_user为个人，eh_organization为企业
//        String clientAppName = "Web对公转账";
//        Long communityId = null;
//        String contactNum = cmd.getContractNum();
//        String openid = null;
//        String payerName = cmd.getPayerName();
//        AssetPaymentOrder order  = assetProvider.saveAnOrderCopyForEnt(payerType,null,String.valueOf(amountsInCents/100l),clientAppName,
//        		communityId,contactNum,openid,cmd.getPayerName(),ZjgkPaymentConstants.EXPIRE_TIME_15_MIN_IN_SEC,
//        		cmd.getNamespaceId(),OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
//        assetProvider.saveOrderBills(bills,order.getId());
        PublicTransferBillRespForEnt publicTransferBillRespForEnt = new PublicTransferBillRespForEnt();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        String date = format.format(new Timestamp(DateHelper.currentGMTTime().getTime()));
		publicTransferBillRespForEnt.setOrderCreateTime(date);
        //paymentOrderNum：订单编号，没点“去支付”按钮之前，没有办法获取订单编号，所以此处只能展示我们业务系统的orderNo支付流水号
        //publicTransferBillRespForEnt.setPaymentOrderNum(String.valueOf(order.getOrderNo()));
        //publicTransferBillRespForEnt.setBusinessType("业务类型：待产品决定");
        publicTransferBillRespForEnt.setOrderSource(cmd.getOrderSource());
        if(orderExplain != null && orderExplain.length() != 0) {
        	publicTransferBillRespForEnt.setOrderExplain(orderExplain.substring(0, orderExplain.length() - 1));
        }
        publicTransferBillRespForEnt.setOrderAmount(BigDecimal.valueOf(sumAmountOwed).setScale(2, BigDecimal.ROUND_HALF_UP));
        return publicTransferBillRespForEnt;
    }
    
    private void checkHasPaidBills(List<String> billIds) {
        List<PaymentBills> paidBills = assetProvider.findPaidBillsByIds(billIds);
        if( paidBills.size() >0 ) throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.HAS_PAID_BILLS,"this is bills have been paid,please refresh");
    }

	public ListBillsResponse listBillsForEnt(ListBillsCommandForEnt cmd) {
		ListBillsResponse response = new ListBillsResponse();
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        //转换为正确的命令
        ListBillsCommand converCmd = ConvertHelper.convert(cmd, ListBillsCommand.class);
        converCmd.setTargetIdForEnt(cmd.getTargetId());//对公转账的专有参数
        converCmd.setStatus((byte) 1);//0:未出账单;1:已出账单,普通企业客户只能查询已出账单
        List<ListBillsDTO> list = handler.listBills(UserContext.getCurrentNamespaceId(),response, converCmd);
        response.setListBillsDTOS(list);
        return response;
    }

	public void exportOrdersForEnt(ListPaymentBillCmd cmd, HttpServletResponse response) {
        if(cmd.getPageSize()==null||cmd.getPageSize()>5000){
            cmd.setPageSize(Long.parseLong("5000"));
        }
        ListPaymentBillResp result = listPaymentBillForEnt(cmd);
        List<PaymentOrderBillDTO> dtos = result.getPaymentOrderBillDTOs();
        exportOrdersUtil(dtos, cmd, response);
	}

	public ListPaymentBillResp listPaymentBillForEnt(ListPaymentBillCmd cmd) {
		//初始化参数
		cmd.setOrganizationId(cmd.getTargetId());
        cmd.setCommunityId(cmd.getOwnerId());
        //业务系统：paymentType：支付方式，0:微信，1：支付宝，2：对公转账
        //电商系统：paymentType： 支付类型:1:"微信APP支付",2:"网关支付",7:"微信扫码支付",8:"支付宝扫码支付",9:"微信公众号支付",10:"支付宝JS支付",
        //12:"微信刷卡支付（被扫）",13:"支付宝刷卡支付(被扫)",15:"账户余额",21:"微信公众号js支付"
    	cmd.setPaymentType(2);//2代表对公转账
        return listPaymentBill(cmd);
	}
	
	public void exportOrdersUtil(List<PaymentOrderBillDTO> dtos, ListPaymentBillCmd cmd, HttpServletResponse response) {
    	try {
	        Calendar c = newClearedCalendar();
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH);
	        int date = c.get(Calendar.DATE);
	        int hour = c.get(Calendar.HOUR_OF_DAY);
	        int minute = c.get(Calendar.MINUTE);
	        int second = c.get(Calendar.SECOND);
	        String fileName = "payment"+year+month+date+hour+minute+ second;
	        List<exportPaymentOrdersDetail> dataList = new ArrayList<>();
	        //组装datalist来确定propertyNames的值
	        for(int i = 0; i < dtos.size(); i++) {
	        	PaymentOrderBillDTO dto = dtos.get(i);
	        	if(dto != null) {
		        	exportPaymentOrdersDetail detail = new exportPaymentOrdersDetail();
		            detail.setDateStr(dto.getDateStrBegin() + "~" + dto.getDateStrEnd());
		            detail.setBillGroupName(dto.getBillGroupName());
		            //组装所有的收费项信息
		            List<BillItemDTO> billItemDTOList = dto.getBillItemDTOList();
		            String billItemListMsg = "";
		            if(billItemDTOList != null) {
		            	for(int k = 0; k < billItemDTOList.size();k++) {
		            		BillItemDTO billItemDTO = billItemDTOList.get(k);
		            		billItemListMsg += billItemDTO.getBillItemName() + " : " + billItemDTO.getAmountReceivable() + "\r\n";
		            	}
		            }
		            detail.setBillItemListMsg(billItemListMsg);
		            detail.setTargetName(dto.getTargetName());
		            detail.setTargetType(dto.getTargetType().equals("eh_user") ? "个人客户" : "企业客户");
		            //detail.setPaymentStatus(dto.getPaymentStatus()==1 ? "已完成":"订单异常");
		            detail.setPaymentStatus("已完成");
		            if(dto.getPaymentType() != null) {
		            	switch (dto.getPaymentType()) {
						case 0:
							detail.setPaymentType("微信");
							break;
						case 1:
							detail.setPaymentType("支付宝");
							break;
						case 2:
							detail.setPaymentType("对公转账");
							break;
						default:
							break;
		            	}
		            }
		            detail.setAmountReceived(dto.getAmountReceived());
		            detail.setAmountReceivable(dto.getAmountReceivable());
		            detail.setAmoutExemption(dto.getAmountExemption());
		            detail.setAmountSupplement(dto.getAmountSupplement());
		            detail.setPaymentOrderNum(dto.getPaymentOrderNum());
		            detail.setPayTime(dto.getPayTime());
		            detail.setPayerTel(dto.getPayerTel());
		            detail.setPayerName(dto.getPayerName());
		            detail.setAddresses(dto.getAddresses());
		            dataList.add(detail);
	        	}
	        }
	        String[] propertyNames = {"dateStr","billGroupName","billItemListMsg","targetName","targetType","paymentStatus","paymentType",
	        		"amountReceived","amountReceivable","amoutExemption","amountSupplement","paymentOrderNum","payTime","payerTel","payerName","addresses"};
	        String[] titleName ={"账单时间","账单组","收费项信息","客户名称","客户类型","订单状态","支付方式",
	        		"实收金额","应收金额","减免","增收","订单编号","缴费时间","缴费人电话","缴费人","楼栋门牌"};
	        int[] titleSize = {40,20,20,20,20,20,20,20,20,20,20,30,20,20,20,40};
	        ExcelUtils excel = new ExcelUtils(response,fileName,"sheet1");
	        excel.writeExcel(propertyNames,titleName,titleSize,dataList);
		} catch (Exception e) {
			LOGGER.error("exportOrdersUtil cmd={}, Exception={}", cmd, e);
		}
    }

    //merge conflict

//    public void exportPaymentBillsUtil(List<ListBillsDTO> dtos, Long billGroupId, HttpServletResponse response, Integer namespaceId, Long communityId, Long moduleId){
//
//        Calendar c = newClearedCalendar();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int date = c.get(Calendar.DATE);
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//        int second = c.get(Calendar.SECOND);
//        String fileName = "bill"+year+month+date+hour+minute+ second;
//
//        //初始化
//        List<String> propertyNames = new ArrayList<String>();
//        List<String> titleName = new ArrayList<String>();
//        List<Integer> titleSize = new ArrayList<Integer>();
//        propertyNames.add("dateStrBegin");
//        titleName.add("账单开始时间");
//        titleSize.add(20);
//        propertyNames.add("dateStrEnd");
//        titleName.add("账单结束时间");
//        titleSize.add(20);
//        propertyNames.add("targetName");
//        titleName.add("客户名称");
//        titleSize.add(20);
//        propertyNames.add("contractNum");
//        titleName.add("合同编号");
//        titleSize.add(20);
//        propertyNames.add("customerTel");
//        titleName.add("客户手机号");
//        titleSize.add(20);
//        propertyNames.add("noticeTel");
//        titleName.add("催缴手机号");
//        titleSize.add(20);
//        //增加收费项的导出
//        ShowCreateBillDTO webPage = assetProvider.showCreateBill(billGroupId);
//        List<BillItemDTO> billItemDTOList = webPage.getBillItemDTOList();
//        for(BillItemDTO billItemDTO : billItemDTOList){
//            if(billItemDTO.getBillItemId() != null) {
//                propertyNames.add(billItemDTO.getBillItemId().toString());
//                titleName.add(billItemDTO.getBillItemName()+"(含税)");
//                titleSize.add(20);
//                propertyNames.add(billItemDTO.getBillItemId().toString() + "-withoutTax");
//                titleName.add(billItemDTO.getBillItemName()+"(不含税)");
//                titleSize.add(20);
//                propertyNames.add(billItemDTO.getBillItemId().toString() + "-taxAmount");
//                titleName.add(billItemDTO.getBillItemName()+"税额");
//                titleSize.add(20);
//                //修复issue-34181 执行一些sql页面没有“用量”，但是导入的模板和导出Excel都有“用量”字段
//                if(isShowEnergy(namespaceId, communityId, moduleId)) {
//                    //判断该域空间下是否显示用量
//                    if(billItemDTO.getBillItemId().equals(AssetEnergyType.personWaterItem.getCode())
//                            || billItemDTO.getBillItemId().equals(AssetEnergyType.publicWaterItem.getCode())) {
//                        //eh_payment_charging_items 4:自用水费  7：公摊水费
//                        propertyNames.add(billItemDTO.getBillItemId().toString() + "-energyConsume");
//                        titleName.add("用量（吨）");
//                        titleSize.add(20);
//                    }else if (billItemDTO.getBillItemId().equals(AssetEnergyType.personElectricItem.getCode())
//                            || billItemDTO.getBillItemId().equals(AssetEnergyType.publicElectricItem.getCode())) {
//                        //eh_payment_charging_items 5:自用电费   8：公摊电费
//                        propertyNames.add(billItemDTO.getBillItemId().toString() + "-energyConsume");
//                        titleName.add("用量（度）");
//                        titleSize.add(20);
//                    }
//                }
//                //增加收费项对应滞纳金的导出（不管是否产生了滞纳金）
//                propertyNames.add(billItemDTO.getBillItemId().toString() + "LateFine");
//                titleName.add(billItemDTO.getBillItemName()+"滞纳金"+"(元)");
//                titleSize.add(30);
//            }
//        }
//        propertyNames.add("addresses");
//        titleName.add("楼栋/门牌");
//        titleSize.add(20);
//        propertyNames.add("dueDayCount");
//        titleName.add("欠费天数");
//        titleSize.add(20);
//        propertyNames.add("amountExemption");
//        titleName.add("减免金额");
//        titleSize.add(20);
//        propertyNames.add("remarkExemption");
//        titleName.add("减免备注");
//        titleSize.add(20);
//        propertyNames.add("amountSupplement");
//        titleName.add("增收金额");
//        titleSize.add(20);
//        propertyNames.add("remarkSupplement");
//        titleName.add("增收备注");
//        titleSize.add(20);
//        propertyNames.add("invoiceNum");
//        titleName.add("发票单号");
//        titleSize.add(20);
//        List<Map<String, String>> dataList = new ArrayList<>();
//        //组装datalist来确定propertyNames的值
//        for(int i = 0; i < dtos.size(); i++) {
//            ListBillsDTO dto = dtos.get(i);
//            Map<String, String> detail = new HashMap<String, String>();
//            detail.put("dateStrBegin", dto.getDateStrBegin());
//            detail.put("dateStrEnd", dto.getDateStrEnd());
//            detail.put("targetName", dto.getTargetName());
//            detail.put("contractNum", dto.getContractNum());
//            detail.put("customerTel", dto.getCustomerTel());
//            detail.put("noticeTel", dto.getNoticeTel());
//            //导出增加费项列
//            List<BillItemDTO> billItemDTOs = new ArrayList<>();
//            List<ExemptionItemDTO> exemptionItemDTOs = new ArrayList<>();
//            ListBillDetailVO listBillDetailVO = new ListBillDetailVO();
//            if(dto.getBillId() != null) {
//                listBillDetailVO = assetProvider.listBillDetail(Long.parseLong(dto.getBillId()));
//                if(listBillDetailVO != null && listBillDetailVO.getBillGroupDTO() != null) {
//                    billItemDTOs = listBillDetailVO.getBillGroupDTO().getBillItemDTOList();
//                    exemptionItemDTOs = listBillDetailVO.getBillGroupDTO().getExemptionItemDTOList();
//                }
//            }
//            for(BillItemDTO billItemDTO: billItemDTOList){//收费项类型
//                BigDecimal amountRecivable = BigDecimal.ZERO;
//                BigDecimal amountRecivableWithoutTax = BigDecimal.ZERO;//导出增加不含税字段
//                BigDecimal taxAmount = BigDecimal.ZERO;//导出增加税额字段
//                BigDecimal lateFineAmount = BigDecimal.ZERO;
//                BigDecimal energyConsume = BigDecimal.ZERO;//增加用量
//                for(BillItemDTO billItemDTO2 : billItemDTOs) {//实际账单的收费项信息
//                    //如果费项ID相等
//                    if(billItemDTO.getBillItemId() != null && billItemDTO.getBillItemId().equals(billItemDTO2.getChargingItemsId())) {
//                        //如果费项ID相等，并且费项名称相等，那么说明是费项本身，如果不相等，则说明是费项产生的滞纳金
//                        //if(billItemDTO.getBillItemName() != null && billItemDTO.getBillItemName().equals(billItemDTO2.getBillItemName())) {
//                        //修复issue-34464 新增一条收费项为“租金”的账单，再去修改收费项为“租金1”，再导出，发现金额错位了
//                        if(billItemDTO2.getBillItemName() != null && !billItemDTO2.getBillItemName().startsWith("滞纳金") && billItemDTO2.getBillItemName().contains("滞纳金")){
//                            lateFineAmount = lateFineAmount.add(billItemDTO2.getAmountReceivable());
//                            //减免费项的id，存的都是charging_item_id，因为滞纳金是跟着费项走，所以可以通过subtraction_type类型，判断是否减免费项滞纳金
//                            long billId = Long.parseLong(dto.getBillId());
//                            Long chargingItemId = billItemDTO.getBillItemId();
//                            //根据减免费项配置重新计算待收金额
//                            Boolean isConfigSubtraction = assetProvider.isConfigLateFineSubtraction(billId, chargingItemId);//用于判断该滞纳金是否配置了减免费项
//                            if(isConfigSubtraction) {//如果滞纳金配置了减免费项，那么导出金额置为0
//                                lateFineAmount = BigDecimal.ZERO;//修复issue-33462
//                            }
//                        }else {
//                            //如果费项是一样的，那么导出的时候，对费项做相加
//                            amountRecivable = amountRecivable.add(billItemDTO2.getAmountReceivable());
//                            if(billItemDTO2.getAmountReceivableWithoutTax() != null) {
//                                amountRecivableWithoutTax = amountRecivableWithoutTax.add(billItemDTO2.getAmountReceivableWithoutTax());//导出增加不含税字段
//                            }
//                            if(billItemDTO2.getTaxAmount() != null) {
//                                taxAmount = taxAmount.add(billItemDTO2.getTaxAmount());//导出增加税额字段
//                            }
//                            //根据减免费项配置重新计算待收金额
//                            long billId = Long.parseLong(dto.getBillId());
//                            Long charingItemId = billItemDTO.getBillItemId();
//                            Boolean isConfigSubtraction = assetProvider.isConfigItemSubtraction(billId, charingItemId);//用于判断该费项是否配置了减免费项
//                            if(isConfigSubtraction) {//如果费项配置了减免费项，那么导出金额置为0
//                                amountRecivable = BigDecimal.ZERO;//修复issue-33462
//                            }
//                        }
//                        //增加用量
//                        if(billItemDTO2.getEnergyConsume() != null) {
//                            energyConsume = energyConsume.add(new BigDecimal(billItemDTO2.getEnergyConsume()));
//                        }
//                    }
//                }
//                detail.put(billItemDTO.getBillItemId().toString(), amountRecivable.toString());
//                detail.put(billItemDTO.getBillItemId().toString() + "-withoutTax", amountRecivableWithoutTax.toString());
//                detail.put(billItemDTO.getBillItemId().toString() + "-taxAmount", taxAmount.toString());
//                detail.put(billItemDTO.getBillItemId().toString() + "-energyConsume", energyConsume.toString());//增加用量
//                //导出增加收费项对应滞纳金的导出（不管是否产生了滞纳金）
//                detail.put(billItemDTO.getBillItemId().toString() + "LateFine", lateFineAmount.toString());
//            }
//            detail.put("addresses", dto.getAddresses());
//            detail.put("dueDayCount", dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
//            //导出增加减免（总和）、减免备注、增收（总和）、增收备注
//            if(listBillDetailVO != null) {
//                detail.put("amountExemption", listBillDetailVO.getAmoutExemption() != null ? listBillDetailVO.getAmoutExemption().toString() : "");
//                detail.put("amountSupplement", listBillDetailVO.getAmountSupplement() != null ? listBillDetailVO.getAmountSupplement().toString() : "");
//            }
//            StringBuffer remarkExemption = new StringBuffer();//减免备注
//            StringBuffer remarkSupplement = new StringBuffer();//增收备注
//            for(ExemptionItemDTO exemptionItemDTO : exemptionItemDTOs) {
//                if(exemptionItemDTO.getAmount().compareTo(new BigDecimal("0"))==-1) {
//                    remarkExemption = remarkExemption.append(exemptionItemDTO.getRemark() + ",");
//                }else{
//                    remarkSupplement = remarkSupplement.append(exemptionItemDTO.getRemark() + ",");
//                }
//            }
//            if(remarkExemption.length() != 0) {
//                detail.put("remarkExemption", remarkExemption.substring(0, remarkExemption.length() - 1));
//            }
//            if(remarkSupplement.length() != 0) {
//                detail.put("remarkSupplement", remarkSupplement.substring(0, remarkSupplement.length() - 1));
//            }
//            detail.put("invoiceNum", dto.getInvoiceNum());
//            dataList.add(detail);
//        }
//        ExcelUtils excel = new ExcelUtils(response,fileName,"sheet1");
//        excel.writeExcel(propertyNames,titleName,titleSize,dataList);
//    }

	public ShowCreateBillSubItemListDTO showCreateBillSubItemList(ShowCreateBillSubItemListCmd cmd) {
		AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId(),0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.showCreateBillSubItemList(cmd);
	}

	public void batchModifyBillSubItem(BatchModifyBillSubItemCommand cmd) {
		checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_MODIFY_BILL_SUBITEM,cmd.getOrganizationId());
		assetProvider.batchModifyBillSubItem(cmd);
	}
	
//	/**
//     * 手动修改系统时间，从而触发滞纳金产生（仅用于测试）
//     */
//    public void testLateFine(TestLateFineCommand cmd){
//    	if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
//    		/**
//             * 1. 遍历所有的账单（所有维度），更新账单的欠费状态
//             * 2. 遍历所有的账单（所有维度），拿到所有欠费的账单，拿到所有的billItem的itemd
//             * 3. 遍历所有的billItem和他们对应的滞纳规则，计算，然后新增滞纳的数据,update bill, 不用锁，一条一条走
//             */
//            //获得账单,分页一次最多10000个，防止内存不够
//            int pageSize = 10000;
//            long pageAnchor = 1l;
//            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
//            //Date today = new Date();
//			try {
//				Date today = yyyyMMdd.parse(cmd.getDate());
//				coordinationProvider.getNamedLock("update_bill_and_late_fine").tryEnter(()->{
//	                Long nextPageAnchor = 0l;
//	                while(nextPageAnchor != null){
//	                    List<Long> overdueBillIds = new ArrayList<>();
//	                    SettledBillRes res = assetProvider.getSettledBills(pageSize,pageAnchor);
//	                    List<PaymentBills> bills = res.getBills();
//	                    //更新账单
//	                    for(PaymentBills bill : bills){
//	                        String dueDayDeadline = bill.getDueDayDeadline();
//	                        try{
//	                            Date deadline = yyyyMMdd.parse(dueDayDeadline);
////	                        if(bill.getChargeStatus().byteValue() == 0 && deadline.compareTo(today) != 1) {  兼容以前的没有正常欠费状态的账单
//	                            if(deadline.compareTo(today) != 1) {
//	                                assetProvider.changeBillToDue(bill.getId());
//	                                bill.setChargeStatus((byte)1);
//	                            }
//	                            if(bill.getChargeStatus().byteValue() == (byte)1) overdueBillIds.add(bill.getId());
//	                        } catch (Exception e){ continue; };
//	                    }
//	                    nextPageAnchor = res.getNextPageAnchor();
//	                    //这10000个账单中欠费的billItem
//	                    List<PaymentBillItems> billItems = assetProvider.getBillItemsByBillIds(overdueBillIds);
//	                    for(int i = 0; i < billItems.size(); i++){
//	                        PaymentBillItems item = billItems.get(i);
//	                        //没有关联滞纳金标准的不计算，剔除出更新队列
//	                        if(item.getLateFineStandardId() == null){
//	                            billItems.remove(i--);
//	                            continue;
//	                        }
//	                        //计算滞纳金金额
//	                        //获得欠费的钱
//	                        BigDecimal amountOwed = new BigDecimal("0");
//	                        if(item.getAmountOwed() !=null){
//	                            amountOwed = amountOwed.add(item.getAmountOwed());
//	                        }else{
//	                            item.setAmountOwed(new BigDecimal("0"));
//	                            assetProvider.updatePaymentItem(item);
//	                        }
//	                        amountOwed = amountOwed.add(assetProvider.getLateFineAmountByItemId(item.getId()));
//	                        List<PaymentFormula> formulas = assetProvider.getFormulas(item.getLateFineStandardId());
//	                        if(formulas.size() != 1) {
//	                            LOGGER.error("late fine cal error, the corresponding formula is more than one or less than one, the bill item id is "+item.getId());
//	                        }
//	                        String formulaJson = formulas.get(0).getFormulaJson();
//	                        if(formulaJson.contains("qf")) {//issue-34468 【物业缴费】执行接口，报错
//	                        	formulaJson = formulaJson.replace("qf",amountOwed.toString());
//		                        BigDecimal fineAmount = CalculatorUtil.arithmetic(formulaJson);
//		                        //开始构造一条滞纳金记录
//		                        //查看item是否已经有滞纳金产生了
//		                        PaymentLateFine fine = assetProvider.findLastedFine(item.getId());
//		                        boolean isInsert = false;
//		                        if(fine == null){
//		                            isInsert = true;
//		                            fine = new PaymentLateFine();
//		                            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentLateFine.class));
//		                            fine.setId(nextSequence);
//		                            fine.setName(item.getChargingItemName() + "滞纳金");
//		                            fine.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		                            fine.setBillId(item.getBillId());
//		                            fine.setBillItemId(item.getId());
//		                            fine.setCommunityId(item.getOwnerId());
//		                            fine.setNamespaceId(item.getNamespaceId());
//		                            fine.setCustomerId(item.getTargetId());
//		                            fine.setCustomerType(item.getTargetType());
//		                        }
//		                        fine.setAmount(fineAmount);
//		                        fine.setUpateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		                        assetProvider.updateLateFineAndBill(fine,fineAmount,item.getBillId(), isInsert);
//		                        // 重新计算下账单
//		                        assetProvider.reCalBillById(item.getBillId());
//	                        }
//	                    }
//	                }
//	            });
//			} catch (ParseException e1) {
//				LOGGER.error("please input yyyy-MM-dd");
//			}
//    	}
//    }

    //merge conflict
//    public void createOrUpdateAnAppMapping(CreateAnAppMappingCommand cmd) {
//        AssetModuleAppMapping mapping = new AssetModuleAppMapping();
//        long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAssetModuleAppMappings.class));
//        mapping.setId(nextSequence);
//        if(cmd.getAssetCategoryId() == null || cmd.getContractCategoryId() == null){
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "either assetCategoryId or contractCategory is null");
//        }
//        boolean existAsset = assetProvider.checkExistAsset(cmd.getAssetCategoryId());
//        if(existAsset){
//        	//如果已经存在就是更新
//        	UpdateAnAppMappingCommand updateAnAppMappingCommand = ConvertHelper.convert(cmd, UpdateAnAppMappingCommand.class);
//        	updateAnAppMapping(updateAnAppMappingCommand);
//        }else {
//        	//如果不存在就是新增
//        	createAnAppMapping(cmd);
//        }
//    }

	public void batchUpdateBillsToSettled(BatchUpdateBillsToSettledCmd cmd) {
		checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_TO_SETTLED,cmd.getOrganizationId());
		assetProvider.updatePaymentBillSwitch(cmd);
	}

	public void batchUpdateBillsToPaid(BatchUpdateBillsToPaidCmd cmd) {
		checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_TO_PAID,cmd.getOrganizationId());
		assetProvider.updatePaymentBillStatus(cmd);
	}

	//判断该域空间下是否显示用量
	public boolean isShowEnergy(Integer namespaceId, Long communityId, long moduleId) {
    	//修复issue-34181 执行一些sql页面没有“用量”，但是导入的模板和导出Excel都有“用量”字段
        ListServiceModulefunctionsCommand cmd = new ListServiceModulefunctionsCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setCommunityId(communityId);
        cmd.setModuleId(moduleId);
        List<Long> serviceModulefunctionList = serviceModuleService.listServiceModulefunctions(cmd);
        if(serviceModulefunctionList.contains(101L)) {//判断是否显示用量
        	return true;
        }else {
        	return false;
        }
    }

	public PreOrderDTO payBillsForEnt(CreatePaymentBillOrderCommand cmd) {
		AssetVendor vendor = checkAssetVendor(cmd.getNamespaceId(),0);
        AssetVendorHandler handler = getAssetVendorHandler(vendor.getVendorName());
        cmd.setSourceType(SourceType.PC.getCode());
        PreOrderDTO preOrderDTO = handler.createOrder(cmd);
        return preOrderDTO;
	}

	public GetPayBillsForEntResultResp getPayBillsForEntResult(PaymentOrderRecord cmd) {
		//查询eh_payment_bill_orders表，获取支付状态
		GetPayBillsForEntResultResp response = assetProvider.getPayBillsResultByOrderId(cmd.getPaymentOrderId());
		return response;
	}

	/**
	 * 获取费项配置的税率
	 * @param billGroupId
	 * @return
	 */
	public BigDecimal getBillItemTaxRate(Long billGroupId, Long billItemId) {
		return assetProvider.getBillItemTaxRate(billGroupId, billItemId);
	}

//	public void createOrUpdateAnAppMapping(CreateAnAppMappingCommand cmd) {
//        //1、判断缴费是否已经存在关联合同的记录
//        cmd.setSourceType(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType());
//        AssetMapContractConfig config = new AssetMapContractConfig();
//    	config.setContractOriginId(cmd.getContractOriginId());
//    	config.setContractChangeFlag(cmd.getContractChangeFlag());
//    	cmd.setConfig(config.toString());
//        boolean existAssetMapContract = assetProvider.checkExistAssetMapContract(cmd.getAssetCategoryId());
//        if(existAssetMapContract){
//        	//如果已经存在就是更新
//        	AssetModuleAppMapping mapping = ConvertHelper.convert(cmd, AssetModuleAppMapping.class);
//        	mapping.setSourceId(cmd.getContractCategoryId());
//        	assetProvider.updateAssetMapContract(mapping);
//        }else {
//        	//如果不存在就是新增
//        	AssetModuleAppMapping mapping = ConvertHelper.convert(cmd, AssetModuleAppMapping.class);;
//        	mapping.setSourceId(cmd.getContractCategoryId());
//        	assetProvider.insertAppMapping(mapping);
//        }
//        //2、判断缴费是否已经存在关联能耗的记录
//        cmd.setSourceId(null);
//        cmd.setSourceType(AssetSourceTypeEnum.ENERGY_MODULE.getSourceType());
//        AssetMapEnergyConfig energyConfig = new AssetMapEnergyConfig();
//        energyConfig.setEnergyFlag(cmd.getEnergyFlag());
//    	cmd.setConfig(energyConfig.toString());
//        boolean existAssetMapEnergy = assetProvider.checkExistAssetMapEnergy(cmd.getAssetCategoryId());
//        if(existAssetMapEnergy){
//        	//如果已经存在就是更新
//        	AssetModuleAppMapping mapping = ConvertHelper.convert(cmd, AssetModuleAppMapping.class);
//        	assetProvider.updateAssetMapEnergy(mapping);
//        }else {
//        	//如果不存在就是新增
//        	AssetModuleAppMapping mapping = ConvertHelper.convert(cmd, AssetModuleAppMapping.class);;
//        	assetProvider.insertAppMapping(mapping);
//        }
//    }

	/**
	 * 物业缴费V6.6（对接统一账单） 业务应用新增缴费映射关系接口
	 */
//	public AssetModuleAppMapping createOrUpdateAssetMapping(AssetModuleAppMapping assetModuleAppMapping) {
//		AssetGeneralBillMappingCmd cmd = ConvertHelper.convert(assetModuleAppMapping, AssetGeneralBillMappingCmd.class);
//		boolean existGeneralBillAssetMapping = assetProvider.checkExistGeneralBillAssetMapping(cmd);
//		if(existGeneralBillAssetMapping) {
//			//如果根据namespaceId、ownerId、ownerType、sourceType、sourceId这五个参数能在映射表查到数据，那判断为更新
//			return assetProvider.updateGeneralBillAssetMapping(assetModuleAppMapping);
//		}else {
//			//如果根据namespaceId、ownerId、ownerType、sourceType、sourceId这五个参数在映射表查不到数据，那判断为新增
//			return assetProvider.insertAppMapping(assetModuleAppMapping);
//		}
//	}

    public Long getOriginIdFromMappingApp(Long moduleId, Long originId, long targetModuleId) {
        return assetProvider.getOriginIdFromMappingApp(moduleId, originId, targetModuleId);
    }

	public List<AssetServiceModuleAppDTO> listAssetModuleApps(Integer namespaceId){
		List<AssetServiceModuleAppDTO> dtos = new ArrayList<AssetServiceModuleAppDTO>();
		//1、根据域空间ID查询出所有的缴费应用eh_asset_app_categories
		List<AppAssetCategory> appAssetCategorieList = assetProvider.listAssetAppCategory(namespaceId);
		for(AppAssetCategory appAssetCategory : appAssetCategorieList) {
			//2、根据categoryId查询eh_service_module_apps表的custom_tag，取出应用名称
			Long categoryId = appAssetCategory.getCategoryId();
			AssetServiceModuleAppDTO dto = new AssetServiceModuleAppDTO();
			dto.setCategoryId(categoryId);
			if(categoryId != null) {
				ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleApp(
						namespaceId, null, ServiceModuleConstants.ASSET_MODULE, categoryId.toString());
				if(serviceModuleApp != null) {
					dto.setName(serviceModuleApp.getName());
					dtos.add(dto);//只有categoryId，没有缴费应用名称，说明该缴费应用已在公共平台那边删除
				}
			}
		}
		return dtos;
	}

//	public List<ListGeneralBillsDTO> createGeneralBill(CreateGeneralBillCommand cmd) {
//		List<ListGeneralBillsDTO> dtos = new ArrayList<ListGeneralBillsDTO>();
//		AssetModuleAppMapping mapping = new AssetModuleAppMapping();
//		//1、根据namespaceId、ownerId、ownerType、sourceType、sourceId这五个参数在映射表查询相关配置
//		List<AssetModuleAppMapping> records = assetProvider.findAssetModuleAppMapping(cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSourceId(), cmd.getSourceType());
//		if(records.size() > 0) {
//			//如果根据namespaceId、ownerId、ownerType、sourceType、sourceId这五个参数在映射表查询的到相关配置，说明配置的是按园区走的
//			mapping = records.get(0);
//			Long categoryId = mapping.getAssetCategoryId();
//			Long billGroupId = mapping.getBillGroupId();
//			Long charingItemId = mapping.getChargingItemId();
//			ListGeneralBillsDTO dto = createGeneralBillForCommunity(cmd, categoryId, billGroupId, charingItemId);
//			dtos.add(dto);
//		}else {
//			//如果根据namespaceId、ownerId、ownerType、sourceType、sourceId这五个参数在映射表查询不到相关配置，说明配置的是按默认配置走的，需要转译成园区
//			records = assetProvider.findAssetModuleAppMapping(cmd.getNamespaceId(), null, null, cmd.getSourceId(), cmd.getSourceType());
//			if(records.size() > 0) {
//				mapping = records.get(0);
//				Long categoryId = mapping.getAssetCategoryId();
//				Long brotherGroupId = mapping.getBillGroupId();//这里的账单组ID实际上是默认配置里面的账单组ID
//				Long charingItemId = mapping.getChargingItemId();
//				//如果找的到数据，并且ownerId是空，那么需要再往下找与其有继承关系的园区账单组ID
//				PaymentBillGroup commnuityGroup = assetProvider.getBillGroup(cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(),
//						categoryId, brotherGroupId);
//				Long billGroupId = commnuityGroup.getId();//实际园区的账单组ID
//				ListGeneralBillsDTO dto = createGeneralBillForCommunity(cmd, categoryId, billGroupId, charingItemId);
//				dtos.add(dto);
//			}else {
//				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//	                    "can not find asset mapping");
//			}
//		}
//		return dtos;
//	}

	public List<ListGeneralBillsDTO> createGeneralBill(CreateGeneralBillCommand cmd) {
		List<ListGeneralBillsDTO> dtos = new ArrayList<ListGeneralBillsDTO>();
		//1、根据namespaceId、ownerId、ownerType、sourceType、sourceId这五个参数在映射表查询相关配置
		AssetGeneralBillMappingCmd assetGeneralBillMappingCmd = ConvertHelper.convert(cmd, AssetGeneralBillMappingCmd.class);
		GeneralBillHandler generalBillHandler = getGeneralBillHandler(cmd.getSourceType());
		if(generalBillHandler == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "can not find asset mapping");
		}
		List<AssetModuleAppMapping> records = generalBillHandler.findAssetModuleAppMapping(assetGeneralBillMappingCmd);
		if(records.size() > 0) {
			//如果根据namespaceId、ownerId、ownerType、sourceType、sourceId这五个参数在映射表查询的到相关配置，说明配置的是按园区走的
			AssetModuleAppMapping mapping = records.get(0);
			Long categoryId = mapping.getAssetCategoryId();
			Long billGroupId = mapping.getBillGroupId();
			Long charingItemId = mapping.getChargingItemId();
			//统一账单不支持重复记账
			PaymentBills paymentBill = assetProvider.findPaymentBill(cmd.getNamespaceId(), cmd.getSourceType(), cmd.getSourceId(), cmd.getMerchantOrderId());
			if(paymentBill != null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
	                    "This bill is exist, namespaceId={" + cmd.getNamespaceId() + "}, sourceType={" + cmd.getSourceType() + "}, "
	                    		+ "sourceId={" + cmd.getSourceId() + "}, merchantOrderId={" + cmd.getMerchantOrderId() + "}");
			}
			ListGeneralBillsDTO dto = createGeneralBillForCommunity(cmd, categoryId, billGroupId, charingItemId);
			dtos.add(dto);
		}else {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "can not find asset mapping");
		}
		return dtos;
	}

	public ListGeneralBillsDTO createGeneralBillForCommunity(CreateGeneralBillCommand cmd, Long categoryId, Long billGroupId, Long charingItemId) {
		PaymentBillGroup group = assetGroupProvider.getBillGroupById(billGroupId);
		String billItemName = assetProvider.findChargingItemNameById(charingItemId);
		BigDecimal taxRate = assetProvider.getBillItemTaxRate(billGroupId, charingItemId);//后台直接查费项对应的税率
		//组装创建账单请求
		CreateBillCommand createBillCommand = ConvertHelper.convert(cmd, CreateBillCommand.class);
		createBillCommand.setCategoryId(categoryId);
		//物业缴费V7.1： 统一账单默认没有删除和修改权限
		createBillCommand.setCanDelete((byte)0);
		createBillCommand.setCanModify((byte)0);

		BillGroupDTO billGroupDTO = new BillGroupDTO();
		billGroupDTO.setBillGroupId(billGroupId);
		billGroupDTO.setBillGroupName(group.getName());

		//1、新增费项
		List<BillItemDTO> billItemDTOList = new ArrayList<>();
		for(GoodDTO goodDTO : cmd.getGoodDTOList()) {
			BillItemDTO billItemDTO = new BillItemDTO();
			billItemDTO.setBillItemId(charingItemId);
			billItemDTO.setBillItemName(billItemName);
			BigDecimal amountReceivable = BigDecimal.ZERO;
			if(goodDTO.getTotalPrice() != null) {
				amountReceivable = goodDTO.getTotalPrice();
			}
			BigDecimal taxRateDiv = taxRate.divide(new BigDecimal(100));
			BigDecimal amountReceivableWithoutTax = amountReceivable.divide(BigDecimal.ONE.add(taxRateDiv), 2, BigDecimal.ROUND_HALF_UP);
			//税额=含税金额-不含税金额       税额=1000-909.09=90.91
			BigDecimal taxAmount = amountReceivable.subtract(amountReceivableWithoutTax);
			billItemDTO.setAmountReceivable(amountReceivable);
			billItemDTO.setAmountReceivableWithoutTax(amountReceivableWithoutTax);
			billItemDTO.setTaxAmount(taxAmount);
			//组装商品信息
			billItemDTO.setGoodsServeType(goodDTO.getServeType());
			billItemDTO.setGoodsNamespace(goodDTO.getNamespace());
			billItemDTO.setGoodsTag1(goodDTO.getTag1());
			billItemDTO.setGoodsTag2(goodDTO.getTag2());
			billItemDTO.setGoodsTag3(goodDTO.getTag3());
			billItemDTO.setGoodsTag4(goodDTO.getTag4());
			billItemDTO.setGoodsTag5(goodDTO.getTag5());
			billItemDTO.setGoodsServeApplyName(goodDTO.getServeApplyName());
			billItemDTO.setGoodsTag(goodDTO.getGoodTag());
			billItemDTO.setGoodsName(goodDTO.getGoodName());
			billItemDTO.setGoodsDescription(goodDTO.getGoodDescription());
			billItemDTO.setGoodsCounts(goodDTO.getCounts());
			billItemDTO.setGoodsPrice(goodDTO.getPrice());
			billItemDTO.setGoodsTotalPrice(goodDTO.getTotalPrice());
			billItemDTOList.add(billItemDTO);
		}

		//2、新增优惠/减免金额
		List<ExemptionItemDTO> exemptionItemDTOList = new ArrayList<>();
		BigDecimal exemptionAmount = cmd.getExemptionAmount();
		if(exemptionAmount != null) {
			ExemptionItemDTO exemptionItemDTO = new ExemptionItemDTO();
			exemptionAmount = exemptionAmount.multiply(new BigDecimal(-1));//优惠减免金额需要转换成相应的负数
			exemptionItemDTO.setAmount(exemptionAmount);
			exemptionItemDTO.setRemark(cmd.getExemptionRemark());
			exemptionItemDTOList.add(exemptionItemDTO);
		}

		billGroupDTO.setBillItemDTOList(billItemDTOList);
		billGroupDTO.setExemptionItemDTOList(exemptionItemDTOList);
		createBillCommand.setBillGroupDTO(billGroupDTO);

		ListBillsDTO dto = assetProvider.creatPropertyBill(createBillCommand, null);
		//主要是把以前缴费这边为了兼容对接使用的String类型的billId全部换成Long类型的billId，因为创建统一账单都是在缴费这边的表，都是Long
		ListGeneralBillsDTO convertDTO = ConvertHelper.convert(dto, ListGeneralBillsDTO.class);
		Long billId = Long.parseLong(dto.getBillId());
		convertDTO.setBillId(billId);

		return convertDTO;
	}

	public void cancelGeneralBill(CancelGeneralBillCommand cmd) {
		List<Long> billIdList = cmd.getBillIdList();
		//1、先校验是否可以正常取消
		for(Long billId : billIdList) {
			PaymentBills paymentBill = assetProvider.findPaymentBillById(billId);
			if(null != paymentBill) {
				if(null != cmd.getMerchantOrderId()) {
					if(!cmd.getMerchantOrderId().equals(paymentBill.getMerchantOrderId())) {
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
			                    "merchantOrderId valid error, merchantOrderId={" + cmd.getMerchantOrderId() + "}, "
			                    		+ "merchantOrderIdFindByBillId={" + paymentBill.getMerchantOrderId() + "}");
					}
				}else {
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
		                    "merchantOrderId can not be null");
				}
			}else {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
	                    "can not find bill by billId={" + billId + "}");
			}
		}
		//2、校验通过，取消账单
		for(Long billId : billIdList) {
			assetProvider.deleteBill(billId);
		}
	}

	/**
	 * 物业缴费V6.6 统一账单：账单状态改变回调各个业务系统接口
	 * @param sourceType
	 * @return
	 */
	public GeneralBillHandler getGeneralBillHandler(String sourceType){
		GeneralBillHandler handler = null;
        if(sourceType != null) {
        	String handlerPrefix = GeneralBillHandler.GENERALBILL_PREFIX;
            try {
            	handler = PlatformContext.getComponent(handlerPrefix + sourceType);
			}catch (Exception ex){
				LOGGER.info("GeneralBillHandler not exist sourceType = {}", sourceType);
			}
        }
        return handler;
    }

	public void createChargingItem(CreateChargingItemCommand cmd) {
		Long communityId = cmd.getOwnerId();
        Integer namespaceId = cmd.getNamespaceId();
        List<Long> communityIds = new ArrayList<>();
		//如果communityId是空或者-1的话，那么会把communityId设置成域空间ID，代表全部
        if(communityId == null || communityId == -1){
        	cmd.setOwnerId(cmd.getNamespaceId().longValue());
            communityIds = getAllCommunity(namespaceId,cmd.getOrganziationId(),cmd.getAppId(),true);
        }
        // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        assetChargingItemProvider.createChargingItem(cmd, communityIds);
	}

	/**
	 * 同步瑞安CM的账单数据到左邻的数据库表中
	 */
	public void syncRuiAnCMBillToZuolin(List<CMSyncObject> cmSyncObjectList, Integer namespaceId, Long contractCategoryId){
		if(cmSyncObjectList != null) {
			for(CMSyncObject cmSyncObject : cmSyncObjectList) {
				List<CMDataObject> data = cmSyncObject.getData();
				if(data != null) {
					for(CMDataObject cmDataObject : data) {
						CMContractHeader contractHeader = cmDataObject.getContractHeader();
						//1、根据propertyId获取左邻communityId
						Long communityId = null;
						Community community = addressProvider.findCommunityByThirdPartyId("ruian_cm", contractHeader.getPropertyID());
						if(community != null) {
							communityId = community.getId();
						}
						//2、获取左邻客户ID
						Long targetId = null;
						targetId = cmDataObject.getCustomerId();
						//3、获取左邻楼栋单元地址ID
						Long addressId = null;
						if(cmDataObject.getContractUnit() != null && cmDataObject.getContractUnit().size() != 0) {
							CMContractUnit cmContractUnit = cmDataObject.getContractUnit().get(0);
							Address address = addressProvider.findApartmentByThirdPartyId("ruian_cm", cmContractUnit.getUnitID());
							if(address != null) {
								addressId = address.getId();
							}
						}

						//获取左邻合同ID、合同编号
						Long contractId = null;
						String contractNum = null;
						String rentalID = "";
						if(cmDataObject.getContractHeader() != null) {
							rentalID = cmDataObject.getContractHeader().getRentalID();//瑞安CM定义的合同ID
							try {
								Long namespaceContractToken = Long.parseLong(rentalID);
								Contract contract = contractProvider.findContractByNamespaceToken(namespaceId, NamespaceContractType.RUIAN_CM.getCode(),
										namespaceContractToken, contractCategoryId);
								if(contract != null) {
									contractId = contract.getId();
									contractNum = contract.getContractNumber();
								}
							}catch (Exception e){
					            LOGGER.error(e.toString());
					        }
						}
						//获取左邻缴费应用categoryId
						Long categoryId = null;
						try {
							List<AssetServiceModuleAppDTO> assetServiceModuleAppDTOs = listAssetModuleApps(namespaceId);
							if(assetServiceModuleAppDTOs != null && assetServiceModuleAppDTOs.get(0) != null){
								categoryId = assetServiceModuleAppDTOs.get(0).getCategoryId();
							}
						}catch (Exception e){
				            LOGGER.error(e.toString());
				        }
						List<CMBill> cmBills = cmDataObject.getBill();
						for(CMBill cmBill : cmBills) {
							BigDecimal amountOwed = BigDecimal.ZERO;//待收(含税 元)
							BigDecimal amountOwedWithoutTax = BigDecimal.ZERO;//待收(不含税 元)
							BigDecimal amountReceivable = BigDecimal.ZERO;//应收含税
							BigDecimal amountReceivableWithoutTax = BigDecimal.ZERO;//应收不含税
							BigDecimal amountReceived = BigDecimal.ZERO;//待收含税
							BigDecimal amountReceivedWithoutTax = BigDecimal.ZERO;//待收不含税
							BigDecimal taxAmount = BigDecimal.ZERO;//税额
							try{
								amountOwed = new BigDecimal(cmBill.getBalanceAmt());
					        }catch (Exception e){
					            LOGGER.error(e.toString());
					        }
							try{
								amountOwedWithoutTax = new BigDecimal(cmBill.getBalanceAmt());
					        }catch (Exception e){
					            LOGGER.error(e.toString());
					        }
							try{
								amountReceivable = new BigDecimal(cmBill.getDocumentAmt());
					        }catch (Exception e){
					            LOGGER.error(e.toString());
					        }
							try{
								amountReceivableWithoutTax = new BigDecimal(cmBill.getChargeAmt());
					        }catch (Exception e){
					            LOGGER.error(e.toString());
					        }
							//已收=账单金额（应收）-账单欠款金额（待收）
							amountReceived = amountReceivable.subtract(amountOwed);
							amountReceivedWithoutTax = amountReceived;
							try{
								taxAmount = new BigDecimal(cmBill.getTaxAmt());
					        }catch (Exception e){
					            LOGGER.error(e.toString());
					        }

							PaymentBills paymentBills = new PaymentBills();
							paymentBills.setNamespaceId(namespaceId);
							paymentBills.setOwnerId(communityId);
							paymentBills.setOwnerType("community");
							paymentBills.setCategoryId(categoryId);
							//通过园区ID获取到对应的默认账单组ID
							PaymentBillGroup group = assetProvider.getBillGroup(namespaceId, communityId, null, null, null, (byte)1);
							paymentBills.setBillGroupId(group.getId());
							paymentBills.setTargetType(AssetTargetType.ORGANIZATION.getCode());//全部默认是企业级别的
							paymentBills.setTargetId(targetId);
							if(cmDataObject.getContractHeader() != null) {
								paymentBills.setTargetName(cmDataObject.getContractHeader().getAccountName());//客户名称
							}
							paymentBills.setContractId(contractId);
							paymentBills.setContractNum(contractNum);
							paymentBills.setDateStrBegin(cmBill.getStartDate());
							paymentBills.setDateStrEnd(cmBill.getEndDate());
							String dateStr = "";
							SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
							try{
					            // 如果传递了计费开始时间
					            if(cmBill.getStartDate() != null){
					            	dateStr = yyyyMM.format(yyyyMM.parse(cmBill.getStartDate()));//账期取的是账单开始时间的yyyy-MM
					            }
					        }catch (Exception e){
					            LOGGER.error(e.toString());
					        }
							paymentBills.setDateStr(dateStr);//账期取的是账单开始时间的yyyy-MM
							if(cmBill.getStatus() != null) {
								if(cmBill.getStatus().equals("已出账单")) {//已出未缴
									paymentBills.setSwitch((byte) 1);
									paymentBills.setStatus(AssetPaymentBillStatus.UNPAID.getCode());
								}else if(cmBill.getStatus().equals("已缴账单")){//已出已缴
									paymentBills.setSwitch((byte) 1);
									paymentBills.setStatus(AssetPaymentBillStatus.PAID.getCode());
								}else {//未出未缴
									paymentBills.setSwitch((byte) 0);
									paymentBills.setStatus(AssetPaymentBillStatus.UNPAID.getCode());
								}
							}else {
								paymentBills.setSwitch((byte) 0);//默认为未出
								paymentBills.setStatus(AssetPaymentBillStatus.UNPAID.getCode());//默认为未缴
							}
							paymentBills.setAmountReceivable(amountReceivable);
							paymentBills.setAmountReceivableWithoutTax(amountReceivableWithoutTax);
							paymentBills.setAmountReceived(amountReceived);
							paymentBills.setAmountReceivedWithoutTax(amountReceivedWithoutTax);
							paymentBills.setAmountOwed(amountOwed);
							paymentBills.setAmountOwedWithoutTax(amountOwedWithoutTax);
							paymentBills.setTaxAmount(taxAmount);
							paymentBills.setAddressId(addressId);
							//物业缴费V6.6（对接统一账单） 账单要增加来源
							paymentBills.setSourceType(AssetModuleNotifyConstants.ASSET_CM_MODULE);
							LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.ASSET_CM_CREATE_CODE, "zh_CN");
							paymentBills.setSourceName(localeString.getText());
				            //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
							paymentBills.setCanDelete((byte)0);
							paymentBills.setCanModify((byte)0);
				            //物业缴费V6.0 账单、费项表增加是否删除状态字段
							paymentBills.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
				            //瑞安CM对接 账单、费项表增加是否是只读字段
							paymentBills.setIsReadonly((byte)1);//只读状态：0：非只读；1：只读
							//瑞安CM对接 账单表增加第三方唯一标识字段
							paymentBills.setThirdBillId(cmBill.getBillScheduleID());
							paymentBills.setCreatTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

							PaymentBillItems items = new PaymentBillItems();
							items.setNamespaceId(namespaceId);
							items.setOwnerId(communityId);
							items.setOwnerType("community");
							items.setCategoryId(categoryId);
							items.setBillGroupId(group.getId());
							items.setTargetType(AssetTargetType.ORGANIZATION.getCode());//全部默认是企业级别的
							items.setTargetId(targetId);
							if(cmDataObject.getContractHeader() != null) {
								items.setTargetName(cmDataObject.getContractHeader().getAccountName());//客户名称
							}
							items.setContractId(contractId);
							items.setContractNum(contractNum);
							items.setDateStrBegin(cmBill.getStartDate());
							items.setDateStrEnd(cmBill.getEndDate());
							items.setDateStr(dateStr);//账期取的是账单开始时间的yyyy-MM
							items.setChargingItemName(cmBill.getBillItemName());
							items.setAmountReceivable(amountReceivable);
							items.setAmountReceivableWithoutTax(amountReceivableWithoutTax);
							items.setAmountReceived(amountReceived);
							items.setAmountReceivedWithoutTax(amountReceivedWithoutTax);
							items.setAmountOwed(amountOwed);
							items.setAmountOwedWithoutTax(amountOwedWithoutTax);
							items.setTaxAmount(taxAmount);
							items.setAddressId(addressId);
							//物业缴费V6.6（对接统一账单） 账单要增加来源
							items.setSourceType(AssetModuleNotifyConstants.ASSET_CM_MODULE);
							items.setSourceName(localeString.getText());
				            //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
							items.setCanDelete((byte)0);
							items.setCanModify((byte)0);
				            //物业缴费V6.0 账单、费项表增加是否删除状态字段
							items.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
				            //瑞安CM对接 账单、费项表增加是否是只读字段
							items.setIsReadonly((byte)1);//只读状态：0：非只读；1：只读
							items.setStatus(paymentBills.getStatus());
							items.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

							PaymentBills existCmBill = assetProvider.getCMBillByThirdBillId(namespaceId, communityId, cmBill.getBillScheduleID());
							if(existCmBill != null) {
								//如果账单的唯一标识存在，那么是更新
								Long billId = existCmBill.getId();
								paymentBills.setId(billId);
								assetProvider.updateCMBill(paymentBills);
								PaymentBillItems existCmBillItem = assetProvider.getCMBillItemByBillId(billId);
								if(existCmBillItem != null) {
									items.setId(existCmBillItem.getId());
									assetProvider.updateCMBillItem(items);
								}
							}else {
								//如果账单的唯一标识不存在，那么是新增
								Long billId = assetProvider.createCMBill(paymentBills);//创建账单并返回账单ID
								items.setBillId(billId);
								assetProvider.createCMBillItem(items);
							}

						}
					}
				}
			}
		}
	}

	//对接下载中心的导出账单列表
	@Override
	public void exportAssetListByParams(Object cmd) {
		Map<String, Object> params = new HashMap<>();
		params.put("UserContext", UserContext.current().getUser());
		Long communityId;
		String moduleName = "";
		if (cmd instanceof ListBillsCommand) {
			ListBillsCommand ListBillsCMD = (ListBillsCommand) cmd;
			params.put("ListBillsCMD", ListBillsCMD);
			communityId = ListBillsCMD.getCommunityId();
			moduleName="缴费";
		} else if (cmd instanceof ListBillsCommandForEnt) {
			ListBillsCommandForEnt ListBillsCMDForEnt = (ListBillsCommandForEnt) cmd;
			params.put("ListBillsCMDForEnt", ListBillsCMDForEnt);
			communityId = ListBillsCMDForEnt.getOwnerId();
			moduleName="对公转账";
		} else {
			LOGGER.error("exportAssetListByParams is error.");
			throw errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_DOWNLOAD, "exportAssetListByParams is error.");
		}
		Community community = communityProvider.findCommunityById(communityId);
		if (community == null) {
			LOGGER.error("Community is not exist.");
			throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, "Community is not exist.");
		}
		String fileName = String.format(moduleName+"信息_%s", community.getName(), com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH))+ ".xlsx";
		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), AssetExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}

	//对接下载中心
	@Override
    public OutputStream exportOutputStreamAssetListByContractList(Object cmd, Long taskId){
		//公用字段
		Long communityId;
		List<ListBillsDTO> dtos ;
		ListFieldCommand command ;
		Long billGroupId ;
		Integer namespaceId;
		Long categoryId = null;
		String moduleName = "";
		taskService.updateTaskProcess(taskId, 10);
		if (cmd instanceof ListBillsCommand) {
			//缴费下载
			ListBillsCommand ListBillsCMD = (ListBillsCommand) cmd;
			communityId = ListBillsCMD.getCommunityId();
			ListBillsCMD.setPageSize(100000);
			namespaceId = UserContext.getCurrentNamespaceId(ListBillsCMD.getNamespaceId());
			dtos = listBills(ListBillsCMD).getListBillsDTOS();
			command = ConvertHelper.convert(ListBillsCMD, ListFieldCommand.class);
			command.setModuleName("asset");
			command.setGroupPath(null);

			billGroupId = ListBillsCMD.getBillGroupId();
			categoryId = ListBillsCMD.getCategoryId();
			moduleName="缴费";

		} else if (cmd instanceof ListBillsCommandForEnt) {
			//对公转账下载
			ListBillsCommandForEnt ListBillsCMDForEnt = (ListBillsCommandForEnt) cmd;
			communityId = ListBillsCMDForEnt.getOwnerId();
			ListBillsCMDForEnt.setPageSize(100000);
			namespaceId = UserContext.getCurrentNamespaceId(ListBillsCMDForEnt.getNamespaceId());
			dtos = listBillsForEnt(ListBillsCMDForEnt).getListBillsDTOS();
			command = ConvertHelper.convert(ListBillsCMDForEnt, ListFieldCommand.class);
			command.setModuleName("asset");
			command.setGroupPath(null);

			billGroupId = ListBillsCMDForEnt.getBillGroupId();
			moduleName="对公转账";

		} else {
			LOGGER.error("exportAssetListByParams is error.");
			throw errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_DOWNLOAD, "exportAssetListByParams is error.");
		}

		taskService.updateTaskProcess(taskId, 25);

		//初始化 字段信息
        List<String> propertyNames = new ArrayList<String>();
        List<String> titleName = new ArrayList<String>();
        List<Integer> titleSize = new ArrayList<Integer>();
        propertyNames.add("dateStrBegin");
        titleName.add("账单开始时间");
        titleSize.add(20);
        propertyNames.add("dateStrEnd");
        titleName.add("账单结束时间");
        titleSize.add(20);
        propertyNames.add("targetName");
        titleName.add("客户名称");
        titleSize.add(20);
        propertyNames.add("contractNum");
        titleName.add("合同编号");
        titleSize.add(20);
        propertyNames.add("customerTel");
        titleName.add("客户手机号");
        titleSize.add(20);
        propertyNames.add("noticeTel");
        titleName.add("催缴手机号");
        titleSize.add(20);
        //增加收费项的导出
        ShowCreateBillDTO webPage = assetProvider.showCreateBill(billGroupId);
        List<BillItemDTO> billItemDTOList = webPage.getBillItemDTOList();
        for(BillItemDTO billItemDTO : billItemDTOList){
        	if(billItemDTO.getBillItemId() != null) {
        		propertyNames.add(billItemDTO.getBillItemId().toString());
                titleName.add(billItemDTO.getBillItemName()+"(含税)");
                titleSize.add(20);
                propertyNames.add(billItemDTO.getBillItemId().toString() + "-withoutTax");
                titleName.add(billItemDTO.getBillItemName()+"(不含税)");
                titleSize.add(20);
                propertyNames.add(billItemDTO.getBillItemId().toString() + "-taxAmount");
                titleName.add(billItemDTO.getBillItemName()+"税额");
                titleSize.add(20);
                //修复issue-34181 执行一些sql页面没有“用量”，但是导入的模板和导出Excel都有“用量”字段
                if(isShowEnergy(namespaceId, categoryId, ServiceModuleConstants.ASSET_MODULE)) {
                	//判断该域空间下是否显示用量
                	if(billItemDTO.getBillItemId().equals(AssetEnergyType.personWaterItem.getCode())
                			|| billItemDTO.getBillItemId().equals(AssetEnergyType.publicWaterItem.getCode())) {
                		//eh_payment_charging_items 4:自用水费  7：公摊水费
                        propertyNames.add(billItemDTO.getBillItemId().toString() + "-energyConsume");
                        titleName.add("用量（吨）");
                        titleSize.add(20);
                	}else if (billItemDTO.getBillItemId().equals(AssetEnergyType.personElectricItem.getCode())
                			|| billItemDTO.getBillItemId().equals(AssetEnergyType.publicElectricItem.getCode())) {
                		//eh_payment_charging_items 5:自用电费   8：公摊电费
                		propertyNames.add(billItemDTO.getBillItemId().toString() + "-energyConsume");
                        titleName.add("用量（度）");
                        titleSize.add(20);
    				}
                }
				//增加收费项对应滞纳金的导出（不管是否产生了滞纳金）
                propertyNames.add(billItemDTO.getBillItemId().toString() + "LateFine");
                titleName.add(billItemDTO.getBillItemName()+"滞纳金"+"(元)");
                titleSize.add(30);
        	}
        }
        taskService.updateTaskProcess(taskId, 40);
        propertyNames.add("addresses");
        titleName.add("楼栋/门牌");
        titleSize.add(20);
        propertyNames.add("dueDayCount");
        titleName.add("欠费天数");
        titleSize.add(20);
        propertyNames.add("amountExemption");
        titleName.add("减免金额");
        titleSize.add(20);
        propertyNames.add("remarkExemption");
        titleName.add("减免备注");
        titleSize.add(20);
        propertyNames.add("amountSupplement");
        titleName.add("增收金额");
        titleSize.add(20);
        propertyNames.add("remarkSupplement");
        titleName.add("增收备注");
        titleSize.add(20);
        propertyNames.add("invoiceNum");
        titleName.add("发票单号");
        titleSize.add(20);

        List<Map<String, String>> dataList = new ArrayList<>();
        taskService.updateTaskProcess(taskId, 65);
        //组装datalist来确定propertyNames的值
        for(int i = 0; i < dtos.size(); i++) {
            ListBillsDTO dto = dtos.get(i);
            Map<String, String> detail = new HashMap<String, String>();
            detail.put("dateStrBegin", dto.getDateStrBegin());
            detail.put("dateStrEnd", dto.getDateStrEnd());
            detail.put("targetName", dto.getTargetName());
            detail.put("contractNum", dto.getContractNum());
            detail.put("customerTel", dto.getCustomerTel());
            if (dto.getNoticeTelList() != null) {
            	detail.put("noticeTel", String.join(",", dto.getNoticeTelList()));
			}

            //导出增加费项列
            List<BillItemDTO> billItemDTOs = new ArrayList<>();
            List<ExemptionItemDTO> exemptionItemDTOs = new ArrayList<>();
            ListBillDetailResponse listBillDetailResponse = new ListBillDetailResponse();
            if(dto.getBillId() != null) {
            	listBillDetailResponse = assetProvider.listBillDetail(Long.parseLong(dto.getBillId()));
    			if(listBillDetailResponse != null && listBillDetailResponse.getBillGroupDTO() != null) {
    				billItemDTOs = listBillDetailResponse.getBillGroupDTO().getBillItemDTOList();
    				exemptionItemDTOs = listBillDetailResponse.getBillGroupDTO().getExemptionItemDTOList();
    			}
    		}
            for(BillItemDTO billItemDTO: billItemDTOList){//收费项类型
            	BigDecimal amountRecivable = BigDecimal.ZERO;
            	BigDecimal amountRecivableWithoutTax = BigDecimal.ZERO;//导出增加不含税字段
            	BigDecimal taxAmount = BigDecimal.ZERO;//导出增加税额字段
            	BigDecimal lateFineAmount = BigDecimal.ZERO;
            	BigDecimal energyConsume = BigDecimal.ZERO;//增加用量
        		for(BillItemDTO billItemDTO2 : billItemDTOs) {//实际账单的收费项信息
        			//如果费项ID相等
        			if(billItemDTO.getBillItemId() != null && billItemDTO.getBillItemId().equals(billItemDTO2.getChargingItemsId())) {
        				//如果费项ID相等，并且费项名称相等，那么说明是费项本身，如果不相等，则说明是费项产生的滞纳金
        				//if(billItemDTO.getBillItemName() != null && billItemDTO.getBillItemName().equals(billItemDTO2.getBillItemName())) {
        				//修复issue-34464 新增一条收费项为“租金”的账单，再去修改收费项为“租金1”，再导出，发现金额错位了
        				if(billItemDTO2.getBillItemName() != null && !billItemDTO2.getBillItemName().startsWith("滞纳金") && billItemDTO2.getBillItemName().contains("滞纳金")){
                            lateFineAmount = lateFineAmount.add(billItemDTO2.getAmountReceivable());
        					//减免费项的id，存的都是charging_item_id，因为滞纳金是跟着费项走，所以可以通过subtraction_type类型，判断是否减免费项滞纳金
        					long billId = Long.parseLong(dto.getBillId());
        					Long chargingItemId = billItemDTO.getBillItemId();
                        	//根据减免费项配置重新计算待收金额
                            Boolean isConfigSubtraction = assetProvider.isConfigLateFineSubtraction(billId, chargingItemId);//用于判断该滞纳金是否配置了减免费项
                            if(isConfigSubtraction) {//如果滞纳金配置了减免费项，那么导出金额置为0
                            	lateFineAmount = BigDecimal.ZERO;//修复issue-33462
                            }
        				}else {
        					//如果费项是一样的，那么导出的时候，对费项做相加
            				amountRecivable = amountRecivable.add(billItemDTO2.getAmountReceivable());
            				if(billItemDTO2.getAmountReceivableWithoutTax() != null) {
            					amountRecivableWithoutTax = amountRecivableWithoutTax.add(billItemDTO2.getAmountReceivableWithoutTax());//导出增加不含税字段
            				}
            				if(billItemDTO2.getTaxAmount() != null) {
            					taxAmount = taxAmount.add(billItemDTO2.getTaxAmount());//导出增加税额字段
            				}
            				//根据减免费项配置重新计算待收金额
            				long billId = Long.parseLong(dto.getBillId());
                            Long charingItemId = billItemDTO.getBillItemId();
                            Boolean isConfigSubtraction = assetProvider.isConfigItemSubtraction(billId, charingItemId);//用于判断该费项是否配置了减免费项
                            if(isConfigSubtraction) {//如果费项配置了减免费项，那么导出金额置为0
                            	amountRecivable = BigDecimal.ZERO;//修复issue-33462
                            }
        				}
        				//增加用量
        				if(billItemDTO2.getEnergyConsume() != null) {
        					energyConsume = energyConsume.add(new BigDecimal(billItemDTO2.getEnergyConsume()));
        				}
        			}
        		}
        		detail.put(billItemDTO.getBillItemId().toString(), amountRecivable.toString());
        		detail.put(billItemDTO.getBillItemId().toString() + "-withoutTax", amountRecivableWithoutTax.toString());
        		detail.put(billItemDTO.getBillItemId().toString() + "-taxAmount", taxAmount.toString());
        		detail.put(billItemDTO.getBillItemId().toString() + "-energyConsume", energyConsume.toString());//增加用量
        		//导出增加收费项对应滞纳金的导出（不管是否产生了滞纳金）
        		detail.put(billItemDTO.getBillItemId().toString() + "LateFine", lateFineAmount.toString());
            }
            detail.put("addresses", dto.getAddresses());
            detail.put("dueDayCount", dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
            //导出增加减免（总和）、减免备注、增收（总和）、增收备注
            if(listBillDetailResponse != null) {
            	detail.put("amountExemption", listBillDetailResponse.getAmoutExemption() != null ? listBillDetailResponse.getAmoutExemption().toString() : "");
            	detail.put("amountSupplement", listBillDetailResponse.getAmountSupplement() != null ? listBillDetailResponse.getAmountSupplement().toString() : "");
            }
            StringBuffer remarkExemption = new StringBuffer();//减免备注
            StringBuffer remarkSupplement = new StringBuffer();//增收备注
            for(ExemptionItemDTO exemptionItemDTO : exemptionItemDTOs) {
            	if(exemptionItemDTO.getAmount().compareTo(new BigDecimal("0"))==-1) {
            		remarkExemption = remarkExemption.append(exemptionItemDTO.getRemark() + ",");
                }else{
                	remarkSupplement = remarkSupplement.append(exemptionItemDTO.getRemark() + ",");
                }
            }
            if(remarkExemption.length() != 0) {
            	detail.put("remarkExemption", remarkExemption.substring(0, remarkExemption.length() - 1));
            }
            if(remarkSupplement.length() != 0) {
            	detail.put("remarkSupplement", remarkSupplement.substring(0, remarkSupplement.length() - 1));
            }
            detail.put("invoiceNum", dto.getInvoiceNum());
            dataList.add(detail);
        }
		Community community = communityProvider.findCommunityById(communityId);
		taskService.updateTaskProcess(taskId, 90);
		if (community == null) {
			LOGGER.error("Community is not exist.");
			throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not exist.");
		}
		if (dtos != null && dtos.size() > 0) {
			String fileName = String.format(moduleName+"信息_%s", community.getName(), com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(null, fileName, moduleName+"信息");
			taskService.updateTaskProcess(taskId, 80);
			return excelUtils.getOutputStream(propertyNames, titleName, titleSize, dataList);
		} else {
			throw errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_NO_DATA, "no data");
		}
	}

	//djm 公司欠费关闭相应的门禁权限
	@Override
	public void setDoorAccessParam(SetDoorAccessParamCommand cmd) {
		AssetDooraccessParam assetDooraccessParam = ConvertHelper.convert(cmd, AssetDooraccessParam.class);
		assetDooraccessParam.setOwnerType(EntityType.COMMUNITY.getCode());
		// 更新
		if (cmd.getId() != null) {
			assetDooraccessParam = assetProvider.findDoorAccessParamById(cmd.getId());
			assetDooraccessParam.setFreezeDays(cmd.getFreezeDays());
			assetDooraccessParam.setUnfreezeDays(cmd.getUnfreezeDays());
			/*
			 * if (cmd.getOwnerId() != null) {
			 * assetDooraccessParam.setOrgId(null); }
			 */
			assetProvider.updateDoorAccessParam(assetDooraccessParam);
		} else {
			/*
			 * if (cmd.getOwnerId() != null) {
			 * assetDooraccessParam.setOrgId(null); }
			 */
			assetProvider.createDoorAccessParam(assetDooraccessParam);
		}
	}

	@Override
	public ListDoorAccessParamResponse getDoorAccessParam(GetDoorAccessParamCommand cmd) {

		List<AssetDooraccessParam> list = assetProvider.listDooraccessParams(cmd);
		/*
		 * if (list.size() < 1 && cmd.getOrgId() != null) {
		 * cmd.setOwnerId(null); list = assetProvider.listDooraccessParams(cmd);
		 * }
		 */
		ListDoorAccessParamResponse response = new ListDoorAccessParamResponse();
		if (list.size() > 0) {
			List<DoorAccessParamDTO> resultList = list.stream().map((c) -> {
				DoorAccessParamDTO dto = ConvertHelper.convert(c, DoorAccessParamDTO.class);
				return dto;
			}).collect(Collectors.toList());
			response.setDoorAccessParamDTOs(resultList);
		}
		return response;
	}

	/**
	 * djm 
	 * 缴费欠费，对接门禁
	 */
	@Override
	public void meterAutoReading(Boolean createPlansFlag) {
		// 关闭门禁
		LOGGER.debug("starting auto closeAssetDoorAccess...");
		closeAssetDoorAccess();
		LOGGER.debug("ending auto closeAssetDoorAccess...");
		// 开启门禁
		LOGGER.debug("starting auto openAssetDoorAccess...");
		openAssetDoorAccess();
		LOGGER.debug("ending auto openAssetDoorAccess...");
	}
	
	private void openAssetDoorAccess() {
		// 双机判断
		if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
			LOGGER.info("start openAssetDoorAccess ...");
			// 获得账单,分页一次最多10000个，防止内存不够
			int pageSize = 10000;
			long pageAnchor = 1l;
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
			Date today = new Date();
			coordinationProvider.getNamedLock(CoordinationLocks.BILL_DUEDAYCOUNT_UPDATE.getCode()).tryEnter(() -> {
				// 根据账单组的最晚还款日（eh_payment_bills ： due_day_deadline）以及当前时间计算欠费天数
				Long nextPageAnchor = 0l;
				while (nextPageAnchor != null) {
					// 0，表示未缴，1 已缴
					Byte status = AssetBillStatusType.PAID.getCode();
					SettledBillRes res = assetProvider.getAssetDoorAccessBills(pageSize, pageAnchor, status);
					List<PaymentBills> bills = res.getBills();

					// 更新账单
					for (PaymentBills bill : bills) {
						Long ownerId = bill.getOwnerId();
						Long targetId = null;
						String targetType = bill.getTargetType();
						Integer paymentType = bill.getPaymentType();
						Byte DooraccessTargetType = 0;
						// 企业
						if ("eh_organization".equals(targetType)) {
							targetId = bill.getTargetId();
							DooraccessTargetType = DoorLicenseType.ORG_COMMUNITY.getCode();
						} else if ("eh_user".equals(targetType)) {
							// assetDooraccessLog.setOwnerId(targetId);
							targetId = bill.getTargetId();
							DooraccessTargetType = DoorLicenseType.USER.getCode();
						}

						GetDoorAccessParamCommand getDoorAccessParamCommand = new GetDoorAccessParamCommand();
						// 先去查询设置的参数
						getDoorAccessParamCommand.setNamespaceId(bill.getNamespaceId());
						getDoorAccessParamCommand.setCategoryId(bill.getCategoryId());
						getDoorAccessParamCommand.setOwnerId(ownerId);
						getDoorAccessParamCommand.setOwnerType(bill.getOwnerType());
						DoorAccessParamDTO DoorAccessParam = null;
						// 线下缴费不用去查配置
						if (paymentType != 0) {
							ListDoorAccessParamResponse listDoorAccessParamResponse = getDoorAccessParam(
									getDoorAccessParamCommand);
							List<DoorAccessParamDTO> listDoorAccessParam = listDoorAccessParamResponse
									.getDoorAccessParamDTOs();
							if (listDoorAccessParam != null) {
								DoorAccessParam = listDoorAccessParam.get(0);
							} else {
								continue;
							}
						}

						// 查询logs获取那些公司门禁已经关闭
						// 禁门禁之前记录下
						AssetDooraccessLog assetDooraccessLog = ConvertHelper.convert(getDoorAccessParamCommand, AssetDooraccessLog.class);
						assetDooraccessLog.setProjectId(ownerId);
						assetDooraccessLog.setProjectType(EntityType.COMMUNITY.getCode());
						assetDooraccessLog.setOwnerId(targetId);
						assetDooraccessLog.setOwnerType(targetType);
						if (DoorAccessParam != null) {
							assetDooraccessLog.setOrgId(DoorAccessParam.getOrgId());
						}
						assetDooraccessLog.setDooraccessStatus(DoorAuthStatus.VALID.getCode());// 已禁用
						// 查询该企业是否已经禁用了门禁，为空是还没有禁用门禁，不为空，已存在，已禁用，跳过
						// 过滤，发起过的关闭的公司
						AssetDooraccessLog existDooraccessLog = assetProvider.getDooraccessLog(assetDooraccessLog);
						if (existDooraccessLog != null) {
							continue;
						}

						try {
							// 非线下支付，需要获取支付时间
							if (paymentType != 0) {
								PaymentBillOrder paymentBillOrder = assetProvider.getPaymentBillOrderByBillId(bill.getId().toString());
								if (paymentBillOrder != null && paymentBillOrder.getPaymentTime() != null) {
									DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									Date deadline = yyyyMMdd.parse(sdf.format(paymentBillOrder.getPaymentTime()));

									Long dueDayCount = (today.getTime() - deadline.getTime()) / ((1000 * 3600 * 24));

									// 缴费完成时间大于设置的缴费多长时间开启门禁
									if (dueDayCount.compareTo(DoorAccessParam.getUnfreezeDays()) >= 0) {
										// 欠费时间大于等于设置的时间，禁用门禁
										// 项目id,状态，管理公司id ,公司id, 类型 调用门禁接口
										// 0表示关门禁

										UpdateFormalAuthByCommunityCommand updateFormalAuthByCommunityCommand = new UpdateFormalAuthByCommunityCommand();

										updateFormalAuthByCommunityCommand.setCommunityId(ownerId);
										updateFormalAuthByCommunityCommand.setOperateOrgId(DoorAccessParam.getOrgId());
										updateFormalAuthByCommunityCommand.setStatus(DoorAuthStatus.VALID.getCode());
										updateFormalAuthByCommunityCommand.setTargetId(targetId);
										updateFormalAuthByCommunityCommand.setTargetType(DooraccessTargetType);

										UpdateFormalAuthByCommunityResponse updateFormalAuthByCommunityResponse = doorAccessService
												.updateFormalAuthByCommunity(updateFormalAuthByCommunityCommand);

										assetDooraccessLog.setDooraccessStatus(DoorAuthStatus.INVALID.getCode());
										existDooraccessLog = assetProvider.getDooraccessLog(assetDooraccessLog);

										// 更新门禁为开启
										// 更新门禁为开启
										if (existDooraccessLog == null) {
											assetDooraccessLog.setDooraccessStatus(DoorAuthStatus.VALID.getCode());// 开启门禁
											assetProvider.createDoorAccessLog(assetDooraccessLog);
										} else {
											existDooraccessLog.setDooraccessStatus(DoorAuthStatus.VALID.getCode());// 开启门禁
											assetProvider.updateDoorAccessLog(existDooraccessLog);
										}
									}
								}
							} else {
								// 直接开启门禁
								UpdateFormalAuthByCommunityCommand updateFormalAuthByCommunityCommand = new UpdateFormalAuthByCommunityCommand();

								updateFormalAuthByCommunityCommand.setCommunityId(ownerId);
								if (DoorAccessParam != null) {
									updateFormalAuthByCommunityCommand.setOperateOrgId(DoorAccessParam.getOrgId());
								}
								updateFormalAuthByCommunityCommand.setStatus(DoorAuthStatus.VALID.getCode());
								updateFormalAuthByCommunityCommand.setTargetId(targetId);
								updateFormalAuthByCommunityCommand.setTargetType(DooraccessTargetType);

								UpdateFormalAuthByCommunityResponse updateFormalAuthByCommunityResponse = doorAccessService
										.updateFormalAuthByCommunity(updateFormalAuthByCommunityCommand);

								assetDooraccessLog.setDooraccessStatus(DoorAuthStatus.INVALID.getCode());
								existDooraccessLog = assetProvider.getDooraccessLog(assetDooraccessLog);

								// 更新门禁为开启
								if (existDooraccessLog == null) {
									assetDooraccessLog.setDooraccessStatus(DoorAuthStatus.VALID.getCode());// 开启门禁
									assetProvider.createDoorAccessLog(assetDooraccessLog);
								} else {
									existDooraccessLog.setDooraccessStatus(DoorAuthStatus.VALID.getCode());// 开启门禁
									assetProvider.updateDoorAccessLog(existDooraccessLog);
								}
							}
						} catch (Exception e) {
							continue;
						}
					}
					nextPageAnchor = res.getNextPageAnchor();
				}
			});
		}
	}
	
	private void closeAssetDoorAccess() {
		// 双机判断
		if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
			LOGGER.info("read energy meter reading ...");
			// 获得账单,分页一次最多10000个，防止内存不够
			int pageSize = 10000;
			long pageAnchor = 1l;
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
			Date today = new Date();
			coordinationProvider.getNamedLock(CoordinationLocks.BILL_DUEDAYCOUNT_UPDATE.getCode()).tryEnter(() -> {
				// 根据账单组的最晚还款日（eh_payment_bills ： due_day_deadline）以及当前时间计算欠费天数
				Long nextPageAnchor = 0l;
				while (nextPageAnchor != null) {
					// 0，表示未缴，1 已缴
					Byte status = AssetBillStatusType.UNPAID.getCode();
					
					SettledBillRes res = assetProvider.getAssetDoorAccessBills(pageSize, pageAnchor,status);
					List<PaymentBills> bills = res.getBills();

					// 更新账单
					for (PaymentBills bill : bills) {
						String dueDayDeadline = bill.getDueDayDeadline();
						Long ownerId = bill.getOwnerId();
						Long targetId = null;
						String targetType = bill.getTargetType();
						Byte DooraccessTargetType = 0;
						// 企业
						if ("eh_organization".equals(targetType)) {
							targetId = bill.getTargetId();
							DooraccessTargetType = DoorLicenseType.ORG_COMMUNITY.getCode();
						} else if ("eh_user".equals(targetType)) {
							// assetDooraccessLog.setOwnerId(targetId);
							DooraccessTargetType = DoorLicenseType.USER.getCode();
						}

						// 先去查询设置的参数
						GetDoorAccessParamCommand getDoorAccessParamCommand = new GetDoorAccessParamCommand();
						getDoorAccessParamCommand.setNamespaceId(bill.getNamespaceId());
						getDoorAccessParamCommand.setCategoryId(bill.getCategoryId());
						getDoorAccessParamCommand.setOwnerId(ownerId);
						getDoorAccessParamCommand.setOwnerType(bill.getOwnerType());
						ListDoorAccessParamResponse listDoorAccessParamResponse = getDoorAccessParam(getDoorAccessParamCommand);
						List<DoorAccessParamDTO> listDoorAccessParam = listDoorAccessParamResponse.getDoorAccessParamDTOs();
						DoorAccessParamDTO DoorAccessParam = null;
						if (listDoorAccessParam != null) {
							DoorAccessParam = listDoorAccessParam.get(0);
						} else {
							continue;
						}

						// 查询logs获取那些公司门禁已经关闭
						// 禁门禁之前记录下
						AssetDooraccessLog assetDooraccessLog = ConvertHelper.convert(getDoorAccessParamCommand, AssetDooraccessLog.class);
						assetDooraccessLog.setProjectId(ownerId);
						assetDooraccessLog.setProjectType(EntityType.COMMUNITY.getCode());
						assetDooraccessLog.setOwnerId(targetId);
						assetDooraccessLog.setOwnerType(targetType);
						assetDooraccessLog.setOrgId(DoorAccessParam.getOrgId());
						assetDooraccessLog.setDooraccessStatus(DoorAuthStatus.INVALID.getCode());// 已禁用
						// 查询该企业是否已经禁用了门禁，为空是还没有禁用门禁，不为空，已存在，已禁用，跳过
						// 过滤，发起过的关闭的公司
						AssetDooraccessLog existDooraccessLog = assetProvider.getDooraccessLog(assetDooraccessLog);
						if (existDooraccessLog != null) {
							continue;
						}

						try {
							Date deadline = yyyyMMdd.parse(dueDayDeadline);
							Long dueDayCount = (today.getTime() - deadline.getTime()) / ((1000 * 3600 * 24));

							if (dueDayCount.compareTo(DoorAccessParam.getFreezeDays()) >= 0) {
								// 欠费时间大于等于设置的时间，禁用门禁
								// 项目id,状态，管理公司id ,公司id, 类型 调用门禁接口 0表示关门禁

								UpdateFormalAuthByCommunityCommand updateFormalAuthByCommunityCommand = new UpdateFormalAuthByCommunityCommand();

								updateFormalAuthByCommunityCommand.setCommunityId(ownerId);
								updateFormalAuthByCommunityCommand.setOperateOrgId(DoorAccessParam.getOrgId());
								updateFormalAuthByCommunityCommand.setStatus(DoorAuthStatus.INVALID.getCode());
								updateFormalAuthByCommunityCommand.setTargetId(targetId);
								updateFormalAuthByCommunityCommand.setTargetType(DooraccessTargetType);

								UpdateFormalAuthByCommunityResponse updateFormalAuthByCommunityResponse = doorAccessService
										.updateFormalAuthByCommunity(updateFormalAuthByCommunityCommand);

								//原来的门禁开启状态，欠费后更新门禁状态，不存在则新增
								assetDooraccessLog.setDooraccessStatus(DoorAuthStatus.VALID.getCode());
								existDooraccessLog = assetProvider.getDooraccessLog(assetDooraccessLog);
								
								//更新门禁为开启
								
								if (existDooraccessLog == null) {
									assetDooraccessLog.setDooraccessStatus(DoorAuthStatus.INVALID.getCode());// 禁用门禁
									assetProvider.createDoorAccessLog(assetDooraccessLog);
								}else {
									existDooraccessLog.setDooraccessStatus(DoorAuthStatus.INVALID.getCode());// 禁用门禁
									assetProvider.updateDoorAccessLog(existDooraccessLog);
								}
							}
						} catch (Exception e) {
							continue;
						}
					}
					nextPageAnchor = res.getNextPageAnchor();
				}
			});
		}
	}

	@Override
	public AssetDooraccessLog getDoorAccessInfo(GetDoorAccessInfoCommand cmd) {
		AssetDooraccessLog assetDooraccessLog = ConvertHelper.convert(cmd, AssetDooraccessLog.class);
		assetDooraccessLog.setProjectType(EntityType.COMMUNITY.getCode());
		AssetDooraccessLog existDooraccessLog = assetProvider.getDooraccessLog(assetDooraccessLog);
		if (existDooraccessLog.getDooraccessStatus() == 0) {
			existDooraccessLog.setMsg("该企业或公司门禁处于全部关闭");
		} else {
			existDooraccessLog.setMsg("该企业或公司门禁处于开启状态");
		}
		return existDooraccessLog;
	}
	
}
