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
//import com.everhomes.contract.ContractService;
import com.everhomes.contract.ContractScheduleJob;
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
import com.everhomes.rest.contract.FindContractCommand;
import com.everhomes.rest.contract.ListCustomerContractsCommand;

import com.everhomes.rest.contract.*;

import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.quality.QualityServiceErrorCode;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserNotificationTemplateCode;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.*;
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
import org.apache.commons.collections.list.AbstractLinkedList;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.DSLContext;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;
import scala.Char;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTMLDocument;

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
 * Created by Wentian on 2017/2/20.
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

//    @Autowired
//    private ContractService contractService;

    @Autowired
    private UserService userService;

//    @Autowired
//    private ZhangjianggaokeAssetVendor handler;

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
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        ListBillsResponse response = new ListBillsResponse();
        if (cmd.getPageAnchor() == null || cmd.getPageAnchor() < 1) {
            if(UserContext.getCurrentNamespaceId()!=999971){
                cmd.setPageAnchor(0l);
//                cmd.setPageAnchor(1l);
            }else{
                cmd.setPageAnchor(1l);
            }
        }
        if(cmd.getPageSize() == null || cmd.getPageSize() < 1 || cmd.getPageSize() > Integer.MAX_VALUE/10){
            cmd.setPageSize(20);
        }
        int pageOffSet = cmd.getPageAnchor().intValue();
        List<ListBillsDTO> list = handler.listBills(cmd.getCommunityIdentifier(),cmd.getContractNum(),UserContext.getCurrentNamespaceId(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getBuildingName(),cmd.getApartmentName(),cmd.getAddressId(),cmd.getBillGroupName(),cmd.getBillGroupId(),cmd.getBillStatus(),cmd.getDateStrBegin(),cmd.getDateStrEnd(),pageOffSet,cmd.getPageSize(),cmd.getTargetName(),cmd.getStatus(),cmd.getTargetType(), response);

        if(list.size() <= cmd.getPageSize() && UserContext.getCurrentNamespaceId()!=999971){
            response.setNextPageAnchor(null);

        }else if(UserContext.getCurrentNamespaceId()!=999971){
            response.setNextPageAnchor(((Integer)(pageOffSet+cmd.getPageSize())).longValue());
            list.remove(list.size()-1);
        }
        response.setListBillsDTOS(list);
        return response;
    }

    @Override
    public ListBillItemsResponse listBillItems(ListBillItemsCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        ListBillItemsResponse response = new ListBillItemsResponse();
        if (cmd.getPageAnchor() == null || cmd.getPageAnchor() < 1) {
            if(UserContext.getCurrentNamespaceId()!=999971){
                cmd.setPageAnchor(0l);
//                cmd.setPageAnchor(1l);
            }else{
                cmd.setPageAnchor(1l);
            }
        }
        if(cmd.getPageSize() == null){
            cmd.setPageSize(20);
        }
        Integer pageOffSet = cmd.getPageAnchor().intValue();
        List<BillDTO> billDTOS = handler.listBillItems(cmd.getTargetType(),cmd.getBillId(),cmd.getTargetName(),pageOffSet,cmd.getPageSize());
        if(billDTOS.size() <= cmd.getPageSize() && UserContext.getCurrentNamespaceId()!=99971) {
            response.setNextPageAnchor(null);
        }else if(UserContext.getCurrentNamespaceId()!=99971){
            response.setNextPageAnchor(((Integer)(pageOffSet+cmd.getPageSize())).longValue());
            billDTOS.remove(billDTOS.size()-1);
        }
        response.setBillDTOS(billDTOS);
        return response;
    }

    @Override
    public void selectNotice(SelectedNoticeCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        List<NoticeInfo> noticeInfos = handler.listNoticeInfoByBillId(cmd.getBillIdAndTypes());
        if(noticeInfos.size()<1) return;
        NoticeWithTextAndMessage(cmd, noticeInfos);
    }

    private void NoticeWithTextAndMessage(SelectedNoticeCommand cmd, List<NoticeInfo> noticeInfos) {
        List<Long> uids = new ArrayList<>();
        //"{targetName}先生/女士，您好，您的账单已出，应付{amount1}元，待缴{amount2}元，下载"{appName} APP"可及时查看账单并支持在线付款,还可体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。"
        //短信： 54	物业费催缴	王闻天	{1-> targetName}先生/女士，您好，您的物业账单已出，账期{2 dateStr}，使用"{3 appName} APP"可及时查看账单并支持在线付款。
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
                smsProvider.addToTupleList(variables, "targetName", noticeInfo.getTargetName());
                //模板改了，所以这个也要改
//                smsProvider.addToTupleList(variables, "dateStr", noticeInfo.getDateStr());
                smsProvider.addToTupleList(variables, "dateStr", StringUtils.isBlank(noticeInfo.getDateStr())?"应用内查看":noticeInfo.getDateStr());
//            smsProvider.addToTupleList(variables,"amount2",noticeInfo.getAmountOwed());
                smsProvider.addToTupleList(variables, "appName", noticeInfo.getAppName());
                String templateLocale = UserContext.current().getUser().getLocale();
                //phoneNums make it fake during test
                Integer nameSpaceId = UserContext.getCurrentNamespaceId();
                nameSpaceId = 999971;
                smsProvider.sendSms(nameSpaceId, telNOs, SmsTemplateCode.SCOPE, SmsTemplateCode.PAYMENT_NOTICE_CODE, templateLocale, variables);
            }
        } catch(Exception e){
            LOGGER.error("YZX MAIL SEND FAILED");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "YZX MAIL SEND FAILED");
        }
        //客户在系统内，把需要推送的uid放在list中
        for (int i = 0; i < noticeInfos.size(); i++) {
            NoticeInfo noticeInfo = noticeInfos.get(i);
            Long targetId = noticeInfo.getTargetId();
            if (targetId != null && targetId != 0l) {
                if (noticeInfo.getTargetType().equals(AssetTargetType.USER.getCode())) {
                    uids.add(noticeInfo.getTargetId());
                } else if (noticeInfo.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
                    ListServiceModuleAdministratorsCommand tempCmd = new ListServiceModuleAdministratorsCommand();
                    tempCmd.setOwnerId(cmd.getOwnerId());
                    tempCmd.setOwnerType(cmd.getOwnerType());
                    tempCmd.setOrganizationId(noticeInfo.getTargetId());
                    //企业超管是1005？不是1001
                    List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(tempCmd);
                    for (int j = 0; i < organizationContactDTOS.size(); i++) {
                        uids.add(organizationContactDTOS.get(0).getId());
                    }
                    LOGGER.info("notice uids found = {}"+uids.size());
                }
            }
        }
        try {
            for (int k = 0; k < uids.size(); k++) {
                MessageDTO messageDto = new MessageDTO();
                messageDto.setAppId(AppConstants.APPID_MESSAGING);
                messageDto.setSenderUid(User.SYSTEM_UID);
                messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uids.get(k).toString()));
                messageDto.setBodyType(MessageBodyType.TEXT.getCode());
                //insert into eh_locale_template values(@xx+1,user_notification,3?,zh_CN,物业账单通知用户,text,999985)
                //这个逻辑是张江高科的， 但为了测试统一，999971先改为999985用华润测试
                Map<String, Object> map = new HashMap<>();
                User targetUser = userProvider.findUserById(uids.get(k));
                map.put("targetName", targetUser.getNickName());
                String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(UserContext.getCurrentNamespaceId(), UserNotificationTemplateCode.SCOPE, UserNotificationTemplateCode.USER_PAYMENT_NOTICE, UserContext.current().getUser().getLocale(), map, "");
                messageDto.setBody(notifyTextForApplicant);
                messageDto.setMetaAppId(AppConstants.APPID_USER);
                if (!notifyTextForApplicant.trim().equals("")) {
                    messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                            uids.get(k).toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
                }
            }
        } catch (Exception e) {
            LOGGER.error("MAIL SEND SUCCESSFULLY，app SENDING MESSAGE FAILED");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "MAIL SEND SUCCESSFULLY，app SENDING MESSAGE FAILED");
        }
        if (UserContext.getCurrentNamespaceId() != 999971) {
            //催缴次数加1
            List<BillIdAndType> billIdAndTypes = cmd.getBillIdAndTypes();
            List<Long> billIds = new ArrayList<>();
            for (int i = 0; i < billIdAndTypes.size(); i++) {
                billIds.add(Long.parseLong(billIdAndTypes.get(i).getBillId()));
            }
            assetProvider.increaseNoticeTime(billIds);
        }
    }


    @Override
    public ShowBillForClientDTO showBillForClient(ClientIdentityCommand cmd) {
        //app用户的权限还未判断，是否可以查看账单
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendorName);
        return handler.showBillForClient(cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetType(),cmd.getTargetId(),cmd.getBillGroupId(),cmd.getIsOnlyOwedBill(),cmd.getContractId());
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(BillIdCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendorName);
        return handler.getBillDetailForClient(cmd.getBillId(),cmd.getTargetType());
    }

    @Override
    public List<ListBillGroupsDTO> listBillGroups(OwnerIdentityCommand cmd) {
        return assetProvider.listBillGroups(cmd.getOwnerId(),cmd.getOwnerType());
    }

    @Override
    public ShowCreateBillDTO showCreateBill(BillGroupIdCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.showCreateBill(cmd.getBillGroupId());
    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(ListBillDetailOnDateChangeCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vendorName = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendorName);
        if(cmd.getTargetType().equals("eh_user")) {
            cmd.setTargetId(UserContext.currentUserId());
        }
        return handler.listBillDetailOnDateChange(cmd.getBillStatus(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetType(),cmd.getTargetId(),cmd.getDateStr(),cmd.getContractId());
    }

    @Override
    public ListBillsDTO createBill(CreateBillCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.createBill(cmd);
    }

    @Override
    public void OneKeyNotice(OneKeyNoticeCommand cmd) {
        ListBillsCommand convertedCmd = ConvertHelper.convert(cmd, ListBillsCommand.class);
        if(UserContext.getCurrentNamespaceId()!=999971){
            convertedCmd.setPageAnchor(0l);
//            convertedCmd.setPageAnchor(1l);
        }else{
            convertedCmd.setPageAnchor(1l);
        }
        if(convertedCmd.getDateStrEnd()==null){
            Calendar now = Calendar.getInstance();
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
            if(appName==null || appName.trim().length()<1){
                appName="张江高科推荐";
            }
            if(UserContext.getCurrentNamespaceId()==999971){
                List<NoticeInfo> list = new ArrayList<>();
                List<ListBillsDTO> listBillsDTOS1 = convertedResponse.getListBillsDTOS();
                for(int i = 0; i < listBillsDTOS1.size(); i++){
                    ListBillsDTO dto = listBillsDTOS1.get(i);
                    NoticeInfo info = new NoticeInfo();
                    info.setAppName(appName);
                    info.setPhoneNums(dto.getNoticeTel());
                    info.setAmountRecevable(dto.getAmountReceivable());
                    info.setAmountOwed(dto.getAmountOwed());
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
                NoticeWithTextAndMessage(requestCmd,list);
                return;
            }
            selectNotice(requestCmd);
        }
    }

    @Override
    public ListBillDetailResponse listBillDetail(ListBillDetailCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.listBillDetail(cmd);
    }

    @Override
    public List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.listBillStatics(cmd);
    }

    @Override
    public void modifyBillStatus(BillIdCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.modifyBillStatus(cmd);
    }

    @Override
    public void exportPaymentBills(ListBillsCommand cmd, HttpServletResponse response) {
        if(cmd.getPageSize()==null||cmd.getPageSize()>5000){
            cmd.setPageSize(5000);
        }
        //has already distributed
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
//            detail.setApartmentName(dto.getApartmentName());
//            detail.setBuildingName(dto.getBuildingName());
            detail.setContractNum(dto.getContractNum());
            detail.setBillGroupName(dto.getBillGroupName());
            detail.setNoticeTel(dto.getNoticeTel());
            if(UserContext.getCurrentNamespaceId()!=999971){
                detail.setNoticeTimes(String.valueOf(dto.getNoticeTimes()));
            }else{
                detail.setNoticeTimes("");
            }
            detail.setStatus(dto.getBillStatus()==1?"已缴":"待缴");
            detail.setTargetName(dto.getTargetName());
            detail.setDateStr(dto.getDateStr());
            dataList.add(detail);
        }
//        String[] propertyNames = {"dateStr","billGroupName","targetName","buildingName","apartmentName","noticeTel","amountReceivable","amountReceived","amountOwed","status","noticeTimes"};
        String[] propertyNames = {"dateStr","billGroupName","targetName","contractNum","noticeTel","amountReceivable","amountReceived","amountOwed","status","noticeTimes"};
//        Field[] declaredFields = ListBillsDTO.class.getDeclaredFields();
//        String[] propertyNames = new String[declaredFields.length];
        String[] titleName ={"账期","账单组","客户名称","合同编号","催缴手机号","应收(元)","已收(元)","欠收(元)","缴费状态","催缴次数"};
        int[] titleSize = {20,20,20,20,20,20,20,20,20,20};
//        for(int i = 0; i < declaredFields.length; i++){
//            propertyNames[i] = declaredFields[i].getName();
//        }
        ExcelUtils excel = new ExcelUtils(response,fileName,"sheet1");
        excel.writeExcel(propertyNames,titleName,titleSize,dataList);
    }

    @Override
    public List<ListChargingItemsDTO> listChargingItems(OwnerIdentityCommand cmd) {
        if(cmd.getOwnerId()==null){
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
        }
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
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.listBillExemptionItems(cmd);
    }

    @Override
    public String deleteBill(BillIdCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        String result = "OK";
        handler.deleteBill(cmd.getBillId());
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
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.deleteBillItem(cmd);
        return result;
    }

    @Override
    public String deletExemptionItem(ExemptionItemIdCommand cmd) {
        String result = "OK";
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.deletExemptionItem(cmd);
        return result;
    }

    @Override
    public PaymentExpectanciesResponse paymentExpectancies(PaymentExpectanciesCommand cmd) {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //calculate the details of payment expectancies
        PaymentExpectanciesResponse response = new PaymentExpectanciesResponse();
        List<PaymentExpectancyDTO> dtos = new ArrayList<>();
        List<FeeRules> feesRules = cmd.getFeesRules();
        HashMap<BillIdentity,PaymentBills> map = new HashMap<>();
        String json = "";
        List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
        List<EhPaymentBills> billList = new ArrayList<>();
        List<EhPaymentContractReceiver> contractDateList = new ArrayList<>();
        for(int i = 0; i < feesRules.size(); i++) {
            List<PaymentExpectancyDTO> dtos1 = new ArrayList<>();
            FeeRules rule = feesRules.get(i);
            List<ContractProperty> var1 = rule.getProperties();
            List<VariableIdAndValue> variableIdAndValueList = assetProvider.findPreInjectedVariablesForCal(rule.getChargingStandardId(),cmd.getOwnerId(),cmd.getOwnerType());
            List<VariableIdAndValue> var2 = rule.getVariableIdAndValueList();
            coverVariables(var2,variableIdAndValueList);
            String formula = assetProvider.findFormulaByChargingStandardId(rule.getChargingStandardId());
            String chargingItemName = assetProvider.findChargingItemNameById(rule.getChargingItemId());
            Byte billingCycle = assetProvider.findBillyCycleById(rule.getChargingStandardId());
            List<Object> billConf = assetProvider.getBillDayAndCycleByChargingItemId(rule.getChargingStandardId(),rule.getChargingItemId(),cmd.getOwnerType(),cmd.getOwnerId());
            Integer billDay = (Integer)billConf.get(0);
            Byte balanceType = (Byte)billConf.get(1);
            PaymentBillGroupRule groupRule = assetProvider.getBillGroupRule(rule.getChargingItemId(),rule.getChargingStandardId(),cmd.getOwnerType(),cmd.getOwnerId());
            Long billGroupId = groupRule.getBillGroupId();
            for(int j = 0; j < var1.size(); j ++){
                List<PaymentExpectancyDTO> dtos2 = new ArrayList<>();
                ContractProperty property = var1.get(j);
                //如果收费项目的计费周期是按照固定日期，以合同开始日为计费周期
                if(billingCycle==AssetPaymentStrings.CONTRACT_BEGIN_DATE_AS_FIXED_DAY_OF_MONTH){
                    FixedAtContractStartHandler(dtos1, rule, variableIdAndValueList, formula, chargingItemName, billDay, dtos2, property);
                }
                //自然月的计费方式
                else if(billingCycle == AssetPaymentStrings.NATRUAL_MONTH){
                    NaturalMonthHandler(dtos1, rule, variableIdAndValueList, formula, chargingItemName, billDay, dtos2, property);
                }else{
                    LOGGER.info("failed to run natural mode, dtos2 length = {}",dtos2.size());
                }
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
                    item.setBuildingName(property.getBuldingName());
                    item.setApartmentName(property.getApartmentName());
                    item.setPropertyIdentifer(property.getPropertyName());
                    item.setAmountOwed(dto.getAmountReceivable());
                    item.setAmountReceivable(dto.getAmountReceivable());
                    item.setAmountReceived(new BigDecimal("0"));
                    item.setBillGroupId(billGroupId);
                    item.setBillId(nextBillId);
                    item.setChargingItemName(groupRule.getChargingItemName());
//                    item.setChargingItemsId(rule.getChargingItemId());
                    item.setChargingItemsId(groupRule.getChargingItemId());
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
                            try {
                                Date parse = sdf.parse(item.getDateStrEnd());
                                if(parse.compareTo(now.getTime()) != 1){
                                    newBill.setStatus((byte)1);
                                }else{
                                    newBill.setStatus((byte)0);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                                newBill.setStatus((byte)0);
                            }
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

                            newBill.setSwitch((byte)3);
                            map.put(identity,newBill);
                        }
                        //if the billing cycle is on quarter or year, just change the way how the billIdentity defines that muliti bills should be merged as one or be independently
                    }else{
                        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"Only natural mode is supported now");
                    }
                }

            }
            dtos.addAll(dtos1);
            // contract receiver added with status being set as 0 i.e. inactive
            Gson gson = new Gson();
            Map<String,String> variableMap = new HashMap<>();
            for(int k = 0; k< variableIdAndValueList.size(); k++){
                VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(k);
                variableMap.put((String)variableIdAndValue.getVaribleIdentifier(),((BigDecimal)variableIdAndValue.getVariableValue()).toString());
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
            if(billList.size()<1 || billItemsList.size()<1 || contractDateList.size()<1){
                return null;
            }
            assetProvider.saveBillItems(billItemsList);
            assetProvider.saveBills(billList);
            assetProvider.saveContractVariables(contractDateList);
            return null;
        });
        response.setList(dtos);
        return response;
    }


    /**
     * 重构费用计算方法
     * 数据来源 1：公式和日期期限的数字来自于调用者； 2：日期的设置来自于rule，公式设置来自于standard
     */
    @Override
    public void paymentExpectancies_re_struct(PaymentExpectanciesCommand cmd) {
        SimpleDateFormat sdf_dateStrD = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sdf_dateStr = new SimpleDateFormat("yyyy-MM");
        Gson gson = new Gson();
        //获得所有计价条款包裹（内有标准，数据，和住址）

        List<FeeRules> feesRules = cmd.getFeesRules();
        //定义了一个账单组不重复的哈希map，用来叠加收费项产生的账单
        HashMap<BillIdentity,EhPaymentBills> map = new HashMap<>();
        String json = "";
        //收费项明细列表
        List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
        //账单的列表，现在的定义，如果有明细则必然有账单，无论时间
        List<EhPaymentBills> billList = new ArrayList<>();
        List<EhPaymentContractReceiver> contractDateList = new ArrayList<>();

        //遍历计价条款包裹
        for(int i = 0; i < feesRules.size(); i++) {
            List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> innerBillItemsList = new ArrayList<>();
            //获取单一包裹
            FeeRules rule = feesRules.get(i);
            //获得包裹中的地址包裹
            List<ContractProperty> var1 = rule.getProperties();

            //获得标准
            EhPaymentChargingStandards standard = assetProvider.findChargingStandardById(rule.getChargingStandardId());

            //获得formula的额外内容
            List<PaymentFormula> formulaCondition = null;
            if(standard.getFormulaType()==3 || standard.getFormulaType() == 4){
                formulaCondition = assetProvider.getFormulas(standard.getId());
            }
            //获得standard公式
            String formula = null;
            if(standard.getFormulaType()==1 || standard.getFormulaType() == 2){
                formulaCondition = assetProvider.getFormulas(standard.getId());
                if(formulaCondition!=null){
                    if(formulaCondition.size()>0){
                        LOGGER.error("普通公式的标准的id为"+standard.getId()+",对应了"+formulaCondition.size()+"条公式!");
                    }
                    PaymentFormula paymentFormula = formulaCondition.get(0);
                    formula = paymentFormula.getFormulaJson();
                }else{
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"找不到公式,标准的id为"+standard.getId()+"");
                }
            }

            //获得包裹中的数据包

            List<VariableIdAndValue> var2 = rule.getVariableIdAndValueList();


            //获得standard时间设置
            Byte billingCycle = standard.getBillingCycle();
            //获得groupRule的时间设置
            PaymentBillGroupRule groupRule = assetProvider.getBillGroupRule(rule.getChargingItemId(),rule.getChargingStandardId(),cmd.getOwnerType(),cmd.getOwnerId());
            Integer monthOffset = groupRule.getBillItemMonthOffset();
            Integer dayOffset = groupRule.getBillItemDayOffset();

            //获得group
            PaymentBillGroup group = assetProvider.getBillGroupById(groupRule.getBillGroupId());

            //开始循环地址包裹
            for(int j = 0; j < var1.size(); j ++){
                //从地址包裹中获得一个地址
                ContractProperty property = var1.get(j);
            //按照收费标准的计费周期分为按月，按季，按年，均有固定和自然两种情况
                Integer cycle = 0;
                switch (billingCycle){
                    case 2:
                        cycle = 0;
                        break;
                    case 3:
                        cycle = 2;
                        break;
                    case 4:
                        cycle = 11;
                        break;
                    default:
                        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"目前计费周期只支持按月，按季，按年");
                }
                //计算
                List<BillItemsExpectancy> billItemsExpectancies = assetFeeHandler(var2,formula,groupRule,group,rule,cycle,cmd,property,standard,formulaCondition);

                long nextBillItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILL_ITEMS.getClass()), billItemsExpectancies.size());
                long currentBillItemSeq = nextBillItemBlock - billItemsExpectancies.size() + 1;
                if(currentBillItemSeq == 0){
                    currentBillItemSeq = currentBillItemSeq+1;
                    this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILL_ITEMS.getClass()));
                }

                //先算出所有的item
                for(int g = 0; g < billItemsExpectancies.size(); g++){
                    BillItemsExpectancy exp = billItemsExpectancies.get(g);
                    // build a billItem
                    PaymentBillItems item = new PaymentBillItems();
                    //资产
                    item.setAddressId(property.getAddressId());
                    item.setBuildingName(property.getBuldingName());
                    item.setApartmentName(property.getApartmentName());
                    item.setPropertyIdentifer(property.getPropertyName());
                    //金额
                    item.setAmountOwed(exp.getAmountReceivable());
                    item.setAmountReceivable(exp.getAmountReceivable());
                    item.setAmountReceived(new BigDecimal("0"));
                    //关联和显示
                    item.setBillGroupId(groupRule.getBillGroupId());
                    item.setChargingItemName(groupRule.getChargingItemName());
                    item.setChargingItemsId(groupRule.getChargingItemId());
                    //日期
                    item.setDateStr(exp.getBillDateStr());
                    item.setDateStrBegin(sdf_dateStrD.format(exp.getDateStrBegin()));
                    item.setDateStrEnd(sdf_dateStrD.format(exp.getDateStrEnd()));
                    item.setDateStrDue(exp.getBillDateDue());
                    item.setDueDayDeadline(exp.getBillDateDeadline());
                    item.setDateStrGeneration(exp.getBillDateGeneration());
                    //归档字段
                    item.setId(currentBillItemSeq);
                    currentBillItemSeq += 1;
                    item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    item.setCreatorUid(UserContext.currentUserId());
                    item.setNamespaceId(cmd.getNamesapceId());
                    item.setOwnerType(cmd.getOwnerType());
                    item.setOwnerId(cmd.getOwnerId());
                    item.setTargetType(cmd.getTargetType());
                    item.setTargetId(cmd.getTargetId());
                    item.setContractId(cmd.getContractId());
                    item.setContractNum(cmd.getContractNum());
                    item.setTargetName(cmd.getTargetName());
                    item.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    //放到数组中去
                    billItemsList.add(item);
                    innerBillItemsList.add(item);
                }
                //再算bill
                for(int g = 0; g< billItemsExpectancies.size(); g++){
                    BillItemsExpectancy exp = billItemsExpectancies.get(g);


                    //每一个周期的bill,先判断是否是此周期的bill已经建立了
                    BillIdentity identity = new BillIdentity();
                    identity.setDateStr(exp.getBillDateStr());
                    identity.setBillGroupId(exp.getBillGroupId());
                    EhPaymentBills newBill = null;
                    if(map.containsKey(identity)){
                        newBill = map.get(identity);
                    }else{
                        //没有在map中找到则新建
                        newBill = new PaymentBills();
                        Long nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILLS.getClass()));
                        if(nextBillId == 0){
                            nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILLS.getClass()));
                        }
                        newBill.setId(nextBillId);
                        //资产
                        newBill.setAddressId(property.getAddressId());
                        newBill.setBuildingName(property.getBuldingName());
                        newBill.setApartmentName(property.getApartmentName());
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
                        String today = sdf_dateStrD.format(Calendar.getInstance().getTime());
                        if(today.compareTo(exp.getBillDateDeadline())==1){
                            newBill.setChargeStatus((byte)1);
                        }else{
                            newBill.setChargeStatus((byte)0);
                        }
                    }
//                    for(){
//                        //减免项目，用有序列表和时间控制循环次数
//                    }
                    for(int k = 0 ; k < innerBillItemsList.size(); k ++){
                        EhPaymentBillItems item = innerBillItemsList.get(k);
                        String dateGeneration = item.getDateStrGeneration();
                        String billCycleStart = newBill.getDateStrBegin();
                        String billCycleEnd = newBill.getDateStrEnd();
                        //费用产生时分要比账单产生的时分要早, 费用产生周期要在周期内
                        if((dateGeneration.compareTo(billCycleEnd)!=1 //闭区间
                                && dateGeneration.compareTo(billCycleStart) != -1)&&item.getBillId()==null){

                            newBill.setAmountOwed(newBill.getAmountOwed().add(item.getAmountOwed()));
                            newBill.setAmountReceivable(newBill.getAmountReceivable().add(item.getAmountReceivable()));
                            item.setBillId(newBill.getId());
                        }
                    }
                    //更新状态
                    if(newBill.getAmountOwed().compareTo(new BigDecimal("0"))==0){
                        newBill.setStatus((byte)1);
                    }
                    //更新到map中
                    map.put(identity,newBill);
                }
            }
            //创建一个 contract——receiver
            PaymentContractReceiver entity = new PaymentContractReceiver();
            Map<String,String> variableMap = new HashMap<>();
            for(int k = 0; k< var2.size(); k++){
                VariableIdAndValue variableIdAndValue = var2.get(k);
//                variableMap.put(variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
                variableMap.put((String)variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
            }
            json = gson.toJson(variableMap, Map.class);
            entity.setVariablesJsonString(json);

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
            contractDateList.add(entity);
        }
        for(Map.Entry<BillIdentity,EhPaymentBills> entry : map.entrySet()){
            billList.add(entry.getValue());
        }

        LOGGER.debug("Asset Fee calculated！ bill list length={}，item length = {}",billList.size(),billItemsList.size());

        this.dbProvider.execute((TransactionStatus status) -> {
            if(billList.size()<1 || billItemsList.size()<1 || contractDateList.size()<1){
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"Bills generation failed, "+billList.size()+" bill generated, "+billItemsList.size()+" billItem generated, "+contractDateList.size()+" contract receiver generated before store");
            }
            assetProvider.saveBillItems(billItemsList);
            assetProvider.saveBills(billList);
            assetProvider.saveContractVariables(contractDateList);
            return null;
        });
    }

    private List<BillItemsExpectancy> assetFeeHandler(List<VariableIdAndValue> var2, String formula, PaymentBillGroupRule groupRule, PaymentBillGroup group, FeeRules rule,Integer cycle,PaymentExpectanciesCommand cmd,ContractProperty property,EhPaymentChargingStandards standard,List<PaymentFormula> formulaCondition) {
        //返回的列表
        List<BillItemsExpectancy> list = new ArrayList<>();
        //计算的时间区间
        Calendar dateStrBegin = Calendar.getInstance();
        dateStrBegin.setTime(rule.getDateStrBegin());
        Calendar dateStrEnd = Calendar.getInstance();
        dateStrEnd.setTime(rule.getDateStrEnd());

        SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
        int time = 0;

        //先算开始a
        Calendar a = Calendar.getInstance();
        a.setTime(dateStrBegin.getTime());
        while(a.compareTo(dateStrEnd)<0){
            //计算费用产生月d d = a+cycle
            Calendar d = Calendar.getInstance();
            d.setTime(a.getTime());
            d.set(Calendar.DAY_OF_MONTH,d.getActualMaximum(Calendar.DAY_OF_MONTH));
            d.add(Calendar.MONTH,cycle);
            d.set(Calendar.DAY_OF_MONTH,d.getActualMaximum(Calendar.DAY_OF_MONTH));
            //计算费用产生的日期
            Calendar d1 = Calendar.getInstance();
            d1.setTime(d.getTime());

            if(groupRule.getBillItemDayOffset()!=null && time != 0){
                d1.add(Calendar.MONTH,groupRule.getBillItemMonthOffset());
            }
            if(groupRule.getBillItemDayOffset()==null){
                d1.set(Calendar.DAY_OF_MONTH,d1.getActualMaximum(Calendar.DAY_OF_MONTH));
            }else{
                // ?
                d1.set(Calendar.DAY_OF_MONTH,groupRule.getBillItemMonthOffset());
            }
            //比较d和d1
            Calendar d2 = Calendar.getInstance();
            if(d.compareTo(d1)<0){
                d2.setTime(d.getTime());
            }else if(d.compareTo(d1)>=0){
                d2.setTime(d1.getTime());
            }
            if(d2.compareTo(dateStrEnd)>0){
                d2.setTime(dateStrEnd.getTime());
            }
            //计算
            //计算系数r，系数r = （d2-a）天数/d2所在月往未来一周期的天数,  如果符合一个周期，那么 r = 1；
            boolean b = checkCycle(d2, a, cycle+1);
            float r = 1;
            if(!b){
                float divider = daysBetween(d2, a);
                Calendar d_assist = Calendar.getInstance();
                d_assist.setTime(d2.getTime());
//                d_assist.set(Calendar.MONTH,d_assist.get(Calendar.MONTH)+cycle+1);
                d_assist.add(Calendar.MONTH,cycle+1);
                float divided = daysBetween(d2,d_assist);
                r = divider/divided;
            }
            BigDecimal amount = calculateFee(var2, formula, r,standard,formulaCondition);
            //组装对象
            BillItemsExpectancy obj = new BillItemsExpectancy();
            obj.setAmountReceivable(amount);
            obj.setAmountOwed(amount);
            obj.setDateStrBegin(a.getTime());
            obj.setDateStrEnd(d2.getTime());
            if(d1.compareTo(dateStrEnd) ==-1){
                obj.setBillDateGeneration(yyyyMMdd.format(d1.getTime()));
            }else{
                obj.setBillDateGeneration(yyyyMMdd.format(dateStrEnd.getTime()));
            }
            //根据时间这里计算滞纳金并规定状态为已出账单,并校验调组，免租
            /**
             *
             */
//            Byte billStatus = checkBillStatus(group,d1); //not payemntStatus
//            obj.setStatus(billStatus);
            obj.setBillGroupId(group.getId());
            obj.setBillDateStr(yyyyMM.format(a.getTime()));
            Calendar due = Calendar.getInstance();
            due.setTime(d.getTime());
//            due.set(Calendar.MONTH,due.get(Calendar.MONTH)+1);
            due.add(Calendar.MONTH,1);
            due.set(Calendar.DAY_OF_MONTH,group.getBillsDay());
            obj.setBillDateDue(yyyyMMdd.format(due.getTime()));
            Calendar deadline = Calendar.getInstance();
            deadline.setTime(due.getTime());
            if(group.getDueDayType()==1){
                //日
//                deadline.set(Calendar.DAY_OF_MONTH,deadline.get(Calendar.DAY_OF_MONTH)+group.getDueDay());
                deadline.add(Calendar.DAY_OF_MONTH,group.getDueDay());
            }else if(group.getDueDayType() == 2){
                //月
                deadline.add(Calendar.MONTH,group.getDueDay());
            }else{
                LOGGER.info("Group due day type can only be 1 or 2, but now type = {}",group.getBalanceDateType());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"账单组最迟付款设置只能是日或月，数据库被篡改，请联系管理员");
            }
            obj.setBillDateDeadline(yyyyMMdd.format(deadline.getTime()));
            obj.setBillCycleStart(yyyyMMdd.format(a.getTime()));
            if(d.compareTo(dateStrEnd) ==-1){
                obj.setBillCycleEnd(yyyyMMdd.format(d.getTime()));
            }else{
                obj.setBillCycleEnd(yyyyMMdd.format(dateStrEnd.getTime()));
            }

            list.add(obj);
            //更改a的值
//            d2.set(Calendar.DAY_OF_MONTH,d2.get(Calendar.DAY_OF_MONTH)+1);
            d2.add(Calendar.DAY_OF_MONTH,1);
            a.setTime(d2.getTime());
            //继续循环
            time++;
        }
        LOGGER.info("账单产生了"+time+"项目,真实为"+list.size());
        //拆卸调组的包裹
        List<RentAdjust> rentAdjusts = cmd.getRentAdjusts();
        if(rentAdjusts!=null){

            outter:for(int i = 0; i < rentAdjusts.size(); i ++){
                RentAdjust rent = rentAdjusts.get(i);

                //是否对应一个资源和收费项，不对应则不进行调组
                List<ContractProperty> rentProperties = rent.getProperties();
                Long rentChargingItemId = rent.getChargingItemId();
                Long feeChargingItemId = rule.getChargingItemId();
                if(feeChargingItemId != rentChargingItemId){
                    break outter;
                }
                inner:for(int j = 0; j < rentProperties.size(); j ++){
                    if(rentProperties.get(j).getAddressId().equals(property.getAddressId())){
                        break inner;
                    }
                    if(j == rentProperties.size()-1){
                        break outter;
                    }
                }
                //进行调组

                //调组的时间区间,收费项的计费时间区间为 a —— d2
                Calendar start = Calendar.getInstance();
                start.setTime(rent.getStart());
                Calendar end = Calendar.getInstance();
                end.setTime(rent.getEnd());
                if(end.compareTo(dateStrEnd)!=-1){
                    end.setTime(dateStrEnd.getTime());
                }
                //算出哪些时间区间是需要调的，（调整幅度放到计算里去)
                Byte seperationType = rent.getSeperationType();
                Float separationTime = rent.getSeparationTime();
                List<Calendar> insertTimes = new ArrayList<>();
                switch (seperationType){
                    case 1:
                        Calendar start_copy = getCopyCalendar(start);
                        if(separationTime<1) separationTime = 1f;
                        start_copy.add(Calendar.DAY_OF_MONTH,separationTime.intValue());
                        while(start_copy.compareTo(end)==-1){
                            Calendar start_copy_copy =Calendar.getInstance();
                            start_copy_copy.setTime(start_copy.getTime());
                            insertTimes.add(start_copy_copy);
                            start_copy.add(Calendar.DAY_OF_MONTH,separationTime.intValue());
                        }
                        break;
                    case 2:
                        Calendar start_copy_1 = getCopyCalendar(start);
                        Object[] interAndFloat = IntegerUtil.getIntegerAndFloatPartFromFloat(separationTime);

                        start_copy_1.add(Calendar.MONTH,(Integer) interAndFloat[0]);
                        start_copy_1.add(Calendar.DAY_OF_MONTH,(int)((float) start_copy_1.getActualMaximum(Calendar.DAY_OF_MONTH) * (float) interAndFloat[1]));

//                        String test1 = yyyyMMdd.format(start_copy_1.getTime());
//                        String test2 = yyyyMMdd.format(end.getTime());

                        while(start_copy_1.compareTo(end)==-1){
                            Calendar start_copy_1_copy =Calendar.getInstance();
                            start_copy_1_copy.setTime(start_copy_1.getTime());
                            insertTimes.add(start_copy_1_copy);
                            start_copy_1.add(Calendar.MONTH,(Integer) interAndFloat[0]);
                            start_copy_1.add(Calendar.DAY_OF_MONTH,(int)((float) start_copy_1.getActualMaximum(Calendar.DAY_OF_MONTH) * (float) interAndFloat[1]));
                        }
                        break;
                    case 3:
                        Calendar start_copy_2 = getCopyCalendar(start);

                        Object[] interAndFloat_1 = IntegerUtil.getIntegerAndFloatPartFromFloat(separationTime);
                        start_copy_2.add(Calendar.YEAR,(Integer) interAndFloat_1[0]);
                        Object[] interAndFloat_2 = IntegerUtil.getIntegerAndFloatPartFromFloat(((float) interAndFloat_1[1]) * 12f);
                        start_copy_2.add(Calendar.MONTH,(Integer) interAndFloat_2[0]);
                        start_copy_2.add(Calendar.DAY_OF_MONTH,(int)((float) start_copy_2.getActualMaximum(Calendar.DAY_OF_MONTH) * (float) interAndFloat_2[1]));

                        while(start_copy_2.compareTo(end)==-1){
                            Calendar start_copy_2_copy =Calendar.getInstance();
                            start_copy_2_copy.setTime(start_copy_2.getTime());
                            insertTimes.add(start_copy_2_copy);

                            start_copy_2.add(Calendar.YEAR,(Integer) interAndFloat_1[0]);
                            start_copy_2.add(Calendar.MONTH,(Integer) interAndFloat_2[0]);
                            start_copy_2.add(Calendar.DAY_OF_MONTH,(int)((float) start_copy_2.getActualMaximum(Calendar.DAY_OF_MONTH) * (float) interAndFloat_2[1]));
                        }
                        break;
                }

                //查找开始插入，没插一次，更新插入点后的所有数据，并且continue外层循环
                longinusBase:for(int m = 0; m < insertTimes.size(); m ++){
                    Calendar longinus = insertTimes.get(m);
                    for(int k = 0; k < list.size(); k++){
                        BillItemsExpectancy item = list.get(k);
                        a.setTime(item.getDateStrBegin());
                        Calendar d2 = Calendar.getInstance();
                        d2.setTime(item.getDateStrEnd());

                        String test1 = yyyyMMdd.format(d2.getTime());
                        String test2 = yyyyMMdd.format(longinus.getTime());

                        if(d2.compareTo(longinus)!=-1){
                            //插中了！获得 插入点 到 整个计价标准的结束
                            reCalFee(longinus,d2,item,rent);
                            reCalFee(list,m+1,rent);
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
                Long rentChargingItemId = rent.getChargingItemId();
                Long feeChargingItemId = rule.getChargingItemId();
                if(feeChargingItemId != rentChargingItemId){
                    break outter;
                }
                inner:for(int j = 0; j < rentProperties.size(); j ++){
                    if(rentProperties.get(j).getAddressId().equals( property.getAddressId())){
                        break inner;
                    }
                    if(j == rentProperties.size()-1){
                        break outter;
                    }
                }
                //开始免租
//            //此计价条款在此资产上的终止时间
//            Date feeEnd = list.get(list.size() - 1).getDateStrEnd();
//            Calendar start = Calendar.getInstance();
//            start.setTime(rent.getStartDate());
//            Calendar end = Calendar.getInstance();
//            end.setTime(rent.getEndDate());
                Date start = rent.getStartDate();
                Date end = rent.getEndDate();
                BigDecimal amount_free = rent.getAmount();
                for(int j = 0; j < list.size(); j ++){
                    BillItemsExpectancy item = list.get(j);
                    Date item_start = item.getDateStrBegin();
                    Date item_end = item.getDateStrEnd();
                    //全包裹
                    if(start.compareTo(item_start)!=1 && end.compareTo(item_end)!= -1){
                        item.setAmountReceivable(item.getAmountReceivable().subtract(amount_free));
                        item.setAmountOwed(item.getAmountOwed().subtract(amount_free));
                    }
                    //左包不全，右包全
                    else if(start.compareTo(item_start)==1 && start.compareTo(item_end) == -1 && end.compareTo(item_end)!=-1){
                        //代码不能重用，提取没有意义,overfitting
                        Calendar item_start_c = Calendar.getInstance();
                        item_start_c.setTime(item_start);
                        Float f = (float)daysBetween_date(start,item_end)/ (float)item_start_c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        BigDecimal f_c = new BigDecimal(f);
                        BigDecimal amount_free_real = amount_free.multiply(f_c);
                        item.setAmountReceivable(item.getAmountReceivable().subtract(amount_free_real));
                        item.setAmountOwed(item.getAmountOwed().subtract(amount_free_real));
                    }
                    //左包全，右包不全
                    else if(start.compareTo(item_start)!=1 && end.compareTo(start) == 1 &&end.compareTo(item_end)==-1){
                        Calendar item_start_c = Calendar.getInstance();
                        item_start_c.setTime(item_start);
                        Float f = (float)daysBetween_date(end,item_start)/ (float)item_start_c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        BigDecimal f_c = new BigDecimal(f);
                        BigDecimal amount_free_real = amount_free.multiply(f_c);
                        item.setAmountReceivable(item.getAmountReceivable().subtract(amount_free_real));
                        item.setAmountOwed(item.getAmountOwed().subtract(amount_free_real));
                    }
                    //左保不全，右也包步全
                    else if(start.compareTo(item_start)==1 && end.compareTo(item_end)==-1){
                        Calendar item_start_c = Calendar.getInstance();
                        item_start_c.setTime(item_start);
                        Float f = (float)daysBetween_date(end,start)/ (float)item_start_c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        BigDecimal f_c = new BigDecimal(f);
                        BigDecimal amount_free_real = amount_free.multiply(f_c);
                        item.setAmountReceivable(item.getAmountReceivable().subtract(amount_free_real));
                        item.setAmountOwed(item.getAmountOwed().subtract(amount_free_real));
                    }
                }
                //免租结束

                //需要记录免租和调租的话，需要定义或者修改减免项目
            }
        }

        
        return list;
    }

    private void reCalFee(List<BillItemsExpectancy> list, int m, RentAdjust rent) {
        Byte adjustType = rent.getAdjustType();
        BigDecimal adjustAmplitude = rent.getAdjustAmplitude();
        if(m >= list.size()-1){
            return;
        }
        for(int i = m; i < list.size(); i ++) {
            BillItemsExpectancy item = list.get(m);
            BigDecimal amount = item.getAmountOwed();
            switch (adjustType){
                case 1:
                    //按金额递增
                    item.setAmountOwed(amount.add(adjustAmplitude));
                    item.setAmountReceivable(amount.add(adjustAmplitude));
                    break;
                case 2:
                    //按金额递减
                    item.setAmountOwed(amount.subtract(adjustAmplitude));
                    item.setAmountReceivable(amount.subtract(adjustAmplitude));
                    break;
                case 3:
                    //按比例递增
                    BigDecimal one = new BigDecimal("1");
                    BigDecimal amount_changed = amount.multiply(one.add(adjustAmplitude.multiply(new BigDecimal("0.01"))));
                    item.setAmountOwed(amount.add(amount_changed));
                    item.setAmountReceivable(amount.add(amount_changed));
                    break;
                case 4:
                    //按比例递减去
                    BigDecimal one_1 = new BigDecimal("1");
                    BigDecimal amount_changed_1 = amount.multiply(one_1.add(adjustAmplitude.multiply(new BigDecimal("0.01"))));
                    item.setAmountOwed(amount.add(amount_changed_1));
                    item.setAmountReceivable(amount.add(amount_changed_1));
                    break;
            }
        }
    }

    private void reCalFee(Calendar longinus, Calendar d2, BillItemsExpectancy item, RentAdjust rent) {
        float i = (float)daysBetween(longinus, d2) / (float)longinus.getActualMaximum(Calendar.DAY_OF_MONTH);
        BigDecimal d = new BigDecimal(String.valueOf(i));
        Byte adjustType = rent.getAdjustType();
        BigDecimal adjustAmplitude = rent.getAdjustAmplitude();

        switch (adjustType){
            case 1:
                //按金额递增
                BigDecimal changedAmount = adjustAmplitude.multiply(d);
                item.setAmountReceivable(item.getAmountReceivable().add(changedAmount));
                item.setAmountOwed(item.getAmountReceivable());
                break;
            case 2:
                BigDecimal changedAmount_1 = adjustAmplitude.multiply(d);
                item.setAmountReceivable(item.getAmountReceivable().subtract(changedAmount_1));
                item.setAmountOwed(item.getAmountReceivable());
                //按金额递减
                break;
            case 3:
                //按比例递增
                BigDecimal amount_before_adjust = item.getAmountOwed();
                // a * ((b * 0.01)+1) * d
                BigDecimal changedAmount_2 = amount_before_adjust.multiply((adjustAmplitude.multiply(new BigDecimal("0.01"))).add(new BigDecimal("1"))).multiply(d);
                // amount_at_d - (amount_before * d)
                item.setAmountReceivable(item.getAmountReceivable().add(changedAmount_2.subtract(amount_before_adjust.multiply(d))));
                item.setAmountOwed(item.getAmountReceivable());
                break;
            case 4:
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
        Calendar copy = Calendar.getInstance();
        copy.setTime(raw.getTime());
        return copy;
    }

    private boolean checkCycle(Calendar d2, Calendar a, Integer cycle) {
        Calendar a_assist = Calendar.getInstance();
        a_assist.setTime(a.getTime());
        a_assist.set(Calendar.MONTH,a_assist.get(Calendar.MONTH)+cycle);
        a_assist.set(Calendar.DAY_OF_MONTH,a_assist.get(Calendar.DAY_OF_MONTH)-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.format(a_assist.getTime());
        sdf.format(a.getTime());
        sdf.format(d2.getTime());
        int i = daysBetween(a_assist, a);
        int i1 = daysBetween(d2, a);
        if(i == i1){
            return true;
        }
        return false;
    }

    private int daysBetween(Calendar c1,Calendar c2)
    {
        long time1 = c1.getTimeInMillis();
        long time2 = c2.getTimeInMillis();
        long between_days=Math.abs(time2-time1)/(1000*3600*24);
//        return Integer.parseInt(String.valueOf(between_days));
        return Integer.parseInt(String.valueOf(between_days))+1;
    }
    private int daysBetween_date(Date c1,Date c2)
    {
        long time1 = c1.getTime();
        long time2 = c2.getTime();
        long between_days=Math.abs(time2-time1)/(1000*3600*24);
//        return Integer.parseInt(String.valueOf(between_days));
        return Integer.parseInt(String.valueOf(between_days))+1;
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
        //first to check if the whole period is less than one month

        Calendar c7 = Calendar.getInstance();
        Calendar c8 = Calendar.getInstance();
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
        Calendar c4 = Calendar.getInstance();
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

    private void FixedAtContractStartHandler(List<PaymentExpectancyDTO> dtos1, FeeRules rule, List<VariableIdAndValue> variableIdAndValueList, String formula, String chargingItemName, Integer billDay, List<PaymentExpectancyDTO> dtos2, ContractProperty property) {
        if(true){
            throw new RuntimeException("暂不支持按照固定日期进行账单结算的模式");
        }
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
        int day = c3.get(Calendar.DAY_OF_MONTH);

        Calendar c4 = Calendar.getInstance();
        c4.setTime(c3.getTime());
        c4.add(Calendar.MONTH,1);
        if(c4.getActualMaximum(Calendar.DAY_OF_MONTH)<day){
            c4.set(Calendar.DAY_OF_MONTH,c4.getActualMaximum(Calendar.DAY_OF_MONTH));
        }else{
            c4.set(Calendar.DAY_OF_MONTH,day);
        }

        if(c4.compareTo(c2) == 0){
            // one month
            // get dto and add to dtos
            float duration = 1;
            addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c2, c3, duration,billDay);
        }else{
            while(c4.compareTo(c2) != 1) {
                //each month
                float duration = 1;
                Calendar c5 = Calendar.getInstance();
                c5.setTime(c3.getTime());
                if(c5.getActualMaximum(Calendar.DAY_OF_MONTH)<day){
                    c5.set(Calendar.DAY_OF_MONTH,c5.getActualMaximum(Calendar.DAY_OF_MONTH));
                }else{
                    c5.set(Calendar.DAY_OF_MONTH,day);
                }
                addFeeDTO(dtos2, formula, chargingItemName, propertyName, variableIdAndValueList, c5, c3, duration,billDay);
                c3.add(Calendar.MONTH,1);
                if(c3.getActualMaximum(Calendar.DAY_OF_MONTH)<day){
                    c3.set(Calendar.DAY_OF_MONTH,c3.getActualMaximum(Calendar.DAY_OF_MONTH));
                }else{
                    c3.set(Calendar.DAY_OF_MONTH,day);
                }
                c4.add(Calendar.MONTH,1);
                if(c4.getActualMaximum(Calendar.DAY_OF_MONTH)<day){
                    c4.set(Calendar.DAY_OF_MONTH,c4.getActualMaximum(Calendar.DAY_OF_MONTH));
                }else{
                    c4.set(Calendar.DAY_OF_MONTH,day);
                }
            }
            if(c4.compareTo(c2) == 1 && c2.compareTo(c3) == 1){
                //less than one month
                int c2day = c2.get(Calendar.DAY_OF_MONTH);
                int c3day = c3.get(Calendar.DAY_OF_MONTH);
                int distance = 0;
                if(c2day>c3day){
                    distance = c2day+c3day;
                }else{
                    distance = c3.getActualMaximum(Calendar.DAY_OF_MONTH)-c2day+c2day;
                }
                float duration = (float)distance/(float)c4.getActualMaximum(Calendar.DAY_OF_MONTH);
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
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        return handler.listBillExpectanciesOnContract(cmd);

    }

    @Override
    public void exportRentalExcelTemplate(HttpServletResponse response) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.exportRentalExcelTemplate(response);
    }

    @Override
    public FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vendor = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vendor);
        return handler.findUserInfoForPayment(cmd);

    }

    @Override
    public void updateBillsToSettled(UpdateBillsToSettled cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = getAssetVendorHandler(vender);
        handler.updateBillsToSettled(cmd);
    }

    @Override
    public GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd) {
        AssetVendor assetVendor = checkAssetVendor(UserContext.getCurrentNamespaceId());
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

    private void coverVariables(List<VariableIdAndValue> var1, List<VariableIdAndValue> var2) {
        for(int i = 0 ; i < var1.size(); i++){
            VariableIdAndValue v1 = var1.get(i);
            String id1 = (String)v1.getVaribleIdentifier();
            for(int j = 0; j< var2.size(); j++){
                VariableIdAndValue v2 = var2.get(j);
                String id2 = (String)v2.getVaribleIdentifier();
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
        c6.add(Calendar.MONTH,1);
        c6.set(Calendar.DAY_OF_MONTH,billDay);
        dto.setDueDateStr(sdf.format(c6.getTime()));
        dto.setDateStrEnd(sdf.format(c5.getTime()));
        dto.setPropertyIdentifier(propertyName);
        dtos.add(dto);
    }

    private BigDecimal calculateFee(List<VariableIdAndValue> variableIdAndValueList, String formula, float duration) {

        HashMap<String,String> map = new HashMap();
        for(int i = 0; i < variableIdAndValueList.size(); i++){
            VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
//            map.put(variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
            map.put((String)variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
        }
        for(Map.Entry<String,String> entry : map.entrySet()){
            formula = formula.replace(entry.getKey(),entry.getValue());
        }
        formula += "*"+duration;
        BigDecimal response = CalculatorUtil.arithmetic(formula);
        response.setScale(2,BigDecimal.ROUND_CEILING);

        return response;
    }
    private BigDecimal calculateFee(List<VariableIdAndValue> variableIdAndValueList, String formula) {

        HashMap<String,String> map = new HashMap();
        for(int i = 0; i < variableIdAndValueList.size(); i++){
            VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
            map.put((String)variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
//            map.put(variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
        }
        for(Map.Entry<String,String> entry : map.entrySet()){
            formula = formula.replace(entry.getKey(),entry.getValue());
        }
        BigDecimal response = CalculatorUtil.arithmetic(formula);
        response.setScale(2,BigDecimal.ROUND_CEILING);

        return response;
    }
    private BigDecimal calculateFee(List<VariableIdAndValue> variableIdAndValueList, String formula, float duration,EhPaymentChargingStandards standard,List<PaymentFormula> formulaCondition) {
        Byte formulaType = standard.getFormulaType();
        BigDecimal result = new BigDecimal("0");
        if(formulaType == 1 || formulaType ==2){

            HashMap<String,String> map = new HashMap();
            for(int i = 0; i < variableIdAndValueList.size(); i++){
                VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
                map.put(variableIdAndValue.getVaribleIdentifier(),variableIdAndValue.getVariableValue().toString());
            }
            for(Map.Entry<String,String> entry : map.entrySet()){
                formula = formula.replace(entry.getKey(),entry.getValue());
            }
            formula += "*"+duration;
            result = CalculatorUtil.arithmetic(formula);
            result.setScale(2,BigDecimal.ROUND_CEILING);

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

    private BigDecimal getConditionedAmount(List<VariableIdAndValue> variableIdAndValueList, PaymentFormula condition,boolean isSlope) {
        BigDecimal result = new BigDecimal("0");
        if (isSlope) {
            //斜面计算
            for (int i = 0; i < variableIdAndValueList.size(); i++) {
                VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
//                String varibleIdentifier = variableIdAndValue.getVaribleIdentifier();
//                BigDecimal variableValue = variableIdAndValue.getVariableValue();
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
//                String varibleIdentifier = variableIdAndValue.getVaribleIdentifier();
//                BigDecimal variableValue = variableIdAndValue.getVariableValue();

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
//                    BigDecimal realValue = var_temp_orig.getVariableValue();
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

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void updateBillSwitchOnTime() {
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag())==RunningFlag.TRUE) {
            coordinationProvider.getNamedLock(CoordinationLocks.BILL_STATUS_UPDATE.getCode()).tryEnter(() -> {
                List<PaymentBillGroup> list = assetProvider.listAllBillGroups();
                //获取当前时间，如果是5号，则将之前的账单的switch装为1
                for (int i = 0; i < list.size(); i++) {
                    PaymentBillGroup paymentBillGroup = list.get(i);
                    Calendar c = Calendar.getInstance();
                    if (c.get(Calendar.DAY_OF_MONTH) == paymentBillGroup.getBillsDay()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                        String billDateStr = sdf.format(c.getTime());
                        assetProvider.updateBillSwitchOnTime(billDateStr);
                    }
                }
            });
        }

    }

    @Override
    public PreOrderDTO placeAnAssetOrder(PlaceAnAssetOrderCommand cmd) {
        AssetVendor vendor = checkAssetVendor(cmd.getNamespaceId());
        AssetVendorHandler handler = getAssetVendorHandler(vendor.getVendorName());
        return handler.placeAnAssetOrder(cmd);
    }

    @Override
    public List<ListChargingItemsDTO> listAvailableChargingItems(OwnerIdentityCommand cmd) {
        return assetProvider.listAvailableChargingItems(cmd);
    }

    @Override
    public void configChargingItems(ConfigChargingItemsCommand cmd) {
        assetProvider.configChargingItems(cmd.getChargingItemConfigs(),cmd.getOwnerId(),cmd.getOwnerType(),cmd.getNamespaceId());
    }

    @Override
    public void createChargingStandard(CreateChargingStandardCommand cmd) {
        if(cmd.getFormulaType() == 1 || cmd.getFormulaType() == 2){
            String formula_no_quote = cmd.getFormula();
            formula_no_quote = formula_no_quote.replace("[[","");
            formula_no_quote = formula_no_quote.replace("]]","");
            cmd.setFormula(formula_no_quote);
        }
        com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards c = new PaymentChargingStandards();
        com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandardsScopes s = new PaymentChargingStandardScope();
        // create a chargingstandard
        c.setBillingCycle(cmd.getBillingCycle());
        c.setChargingItemsId(cmd.getChargingItemId());
        c.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        c.setCreatorUid(0l);
        c.setFormula(cmd.getFormula());
        c.setFormulaJson(cmd.getFormulaJson());
        c.setFormulaType(cmd.getFormulaType());
        long nextStandardId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_CHARGING_STANDARDS.getClass()));
        c.setId(nextStandardId);
        c.setName(cmd.getChargingStandardName());
        c.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        c.setInstruction(cmd.getInstruction());
        c.setSuggestUnitPrice(cmd.getSuggestUnitPrice());
        c.setAreaSizeType(cmd.getAreaSizeType());

        // create formula that fits the standard
        CreateFormulaCommand cmd1 = ConvertHelper.convert(cmd,CreateFormulaCommand.class);
        cmd1.setChargingStandardId(nextStandardId);
        List<EhPaymentFormula> f = createFormula(cmd1);


        // create a scope corresponding to the chargingstandard just created
        s.setChargingStandardId(nextStandardId);
        s.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        s.setCreatorUid(0l);
        s.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.getClass())));
        s.setOwnerType(cmd.getOwnerType());
        s.setOwnerId(cmd.getOwnerId());
        s.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        assetProvider.createChargingStandard(c,s,f);

    }

    @Override
    public void modifyChargingStandard(ModifyChargingStandardCommand cmd) {
        checkNullProhibit("chargingStandardId",cmd.getChargingStandardId());
        checkNullProhibit("new chargingStandardName",cmd.getChargingStandardName());

        assetProvider.modifyChargingStandard(cmd);
    }

    @Override
    public GetChargingStandardDTO getChargingStandardDetail(GetChargingStandardCommand cmd) {
        return assetProvider.getChargingStandardDetail(cmd);
    }

    /**
     * 删除一个收费标准，删除之前，查询其是否已经被引用
     * 1. 是否已经被处于有效期的合同引用
     */
    @Override
    public DeleteChargingStandardDTO deleteChargingStandard(DeleteChargingStandardCommand cmd) {
        DeleteChargingStandardDTO dto = new DeleteChargingStandardDTO();
        Object obj = null;
        //通过熊颖的接口获得收费标准是否已经被引用
        if(obj == null){
            assetProvider.deleteChargingStandard(cmd.getChargingStandardId(),cmd.getOwnerId(),cmd.getOwnerType());
            dto.setMessage("success");
        }else{
            dto.setMessage("failed");
        }
        return dto;
    }

    @Override
    public List<ListAvailableVariablesDTO> listAvailableVariables(ListAvailableVariablesCommand cmd) {
        return assetProvider.listAvailableVariables(cmd);
    }

    /**
     * 解析公式，传来的公式可以是identifer构成的，也可以是汉字构成的，先按照后者解析
     * 返回值
     *  1. formula公式名称，将解析符号去掉即可
     *  普通公式时：
     *      2. formulaJson公式解析式，去掉解析符号将汉字替换为对应的标识
     *      3. formulaType，将传来的返回
     *  阶梯公式时：
     *      2. 需要自己组装公式，从公式表中得到条件与公式并存储
     *      3.
     *  梯度公式时：
     *      2. 需要自己组装公式，从公式表中得到条件与公式并存储
     *      3. 返回id
     */
    @Override
    public List<EhPaymentFormula> createFormula(CreateFormulaCommand cmd) {
//        CreateFormulaDTO dto = new CreateFormulaDTO();
//        List<Long> formulaIds = new ArrayList<>();
        List<EhPaymentFormula> list = new ArrayList<>();
        Byte formulaType = cmd.getFormulaType();
//        dto.setFormulaType(formulaType);
        if (formulaType == 1 ) {
            EhPaymentFormula paymentFormula = new PaymentFormula();
            long nextPaymentFormulaId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_FORMULA.getClass()));
            paymentFormula.setId(nextPaymentFormulaId);
            paymentFormula.setChargingStandardId(cmd.getChargingStandardId());
            paymentFormula.setCreatorUid(0l);
            paymentFormula.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            paymentFormula.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            paymentFormula.setName(cmd.getFormula());
            paymentFormula.setFormulaType(formulaType);
            paymentFormula.setFormula(cmd.getFormula());
            paymentFormula.setFormulaJson("gdje");
            list.add(paymentFormula);
//            formulaIds.add(nextPaymentFormulaId);
        } else if (formulaType == 2) {
            //普通公式时
            String str = cmd.getFormula();
            List<String> formulaAndJson = setFormula(str);

            EhPaymentFormula paymentFormula = new PaymentFormula();
            long nextPaymentFormulaId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_FORMULA.getClass()));
            paymentFormula.setId(nextPaymentFormulaId);
            paymentFormula.setChargingStandardId(cmd.getChargingStandardId());
            paymentFormula.setCreatorUid(0l);
            paymentFormula.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            paymentFormula.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            paymentFormula.setName(formulaAndJson.get(0));
            paymentFormula.setFormulaType(formulaType);
            paymentFormula.setFormula(formulaAndJson.get(0));
            paymentFormula.setFormulaJson(formulaAndJson.get(1));
            list.add(paymentFormula);
//            formulaIds.add(nextPaymentFormulaId);
        } else if (formulaType == 3 || formulaType == 4) {
            //存储条件
            List<VariableConstraints> envelop = cmd.getStepValuePairs();
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < envelop.size(); i++) {
                VariableConstraints variableConstraints = envelop.get(i);
                EhPaymentFormula paymentFormula = new PaymentFormula();

                String eachFormula = variableConstraints.getFormula();
                List<String> formularAndJson = setFormula(eachFormula);

                paymentFormula.setFormulaType(cmd.getFormulaType());
                paymentFormula.setFormula(formularAndJson.get(0));
                paymentFormula.setFormulaJson(formularAndJson.get(1));
                long nextPaymentFormulaId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_FORMULA.getClass()));
                paymentFormula.setId(nextPaymentFormulaId);
                paymentFormula.setChargingStandardId(cmd.getChargingStandardId());
                //获得一个区间5个约束条件
                paymentFormula.setConstraintVariableIdentifer(variableConstraints.getVariableIdentifier());
                paymentFormula.setStartConstraint(variableConstraints.getStartConstraint());
                if(variableConstraints.getStartNum()!=null){
                    paymentFormula.setStartNum(new BigDecimal(variableConstraints.getStartNum()));
                }
                paymentFormula.setEndConstraint(variableConstraints.getEndConstraint());
                if(variableConstraints.getEndNum()!=null){
                    paymentFormula.setEndNum(new BigDecimal(variableConstraints.getEndNum()));
                }


                paymentFormula.setCreatorUid(0l);
                paymentFormula.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                paymentFormula.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                list.add(paymentFormula);
                name.append(formularAndJson.get(0) + "+");
//                formulaIds.add(nextPaymentFormulaId);
            }
            if(name.lastIndexOf("+")==name.length()-1) name.deleteCharAt(name.length()-1);
            for(int i = 0 ; i < list.size(); i ++){
                list.get(i).setName(name.toString());
            }
        }
        return list;
//        assetProvider.savePaymentFormula(list);
//        dto.setFormulaIds(formulaIds);
//        return dto;
    }

    @Override
    public void createBillGroup(CreateBillGroupCommand cmd) {
        assetProvider.createBillGroup(cmd);
    }

    @Override
    public void modifyBillGroup(ModifyBillGroupCommand cmd) {
        assetProvider.modifyBillGroup(cmd);
    }

    @Override
    public ListChargingStandardsResponse listOnlyChargingStandards(ListChargingStandardsCommand cmd) {
        ListChargingStandardsResponse response = new ListChargingStandardsResponse();
        if(cmd.getPageSize() ==null){
            cmd.setPageSize(20);
        }
        if(cmd.getPageAnchor() == null){
            cmd.setPageAnchor(0l);
        }
        List<ListChargingStandardsDTO> list =  assetProvider.listOnlyChargingStandards(cmd);
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
    public void adjustBillGroupOrder(AdjustBillGroupOrderCommand cmd) {
        assetProvider.adjustBillGroupOrder(cmd.getSubjectBillGroupId(),cmd.getTargetBillGroupId());
    }

    @Override
    public ListChargingItemsForBillGroupResponse listChargingItemsForBillGroup(BillGroupIdCommand cmd) {
        ListChargingItemsForBillGroupResponse response = new ListChargingItemsForBillGroupResponse();
        if(cmd.getPageSize()==null){
            cmd.setPageSize(20);
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
    public AddOrModifyRuleForBillGroupResponse addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd) {
        return assetProvider.addOrModifyRuleForBillGroup(cmd);
    }

    @Override
    public void deleteChargingItemForBillGroup(BillGroupRuleIdCommand cmd) {
        EhPaymentBillGroupsRules rule = assetProvider.findBillGroupRuleById(cmd.getBillGroupRuleId());
        boolean workFlag = isInWorkGroupRule(rule,false);
        if(workFlag){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_ACCESS_DENIED,"已关联合同，不能删除");
        }
        assetProvider.deleteBillGroupRuleById(cmd.getBillGroupRuleId());
    }

    /**
     * 如果已经关联合同（不论状态）或者新增账单被使用，则不能修改或删除
     */
    @Override
    public void deleteBillGroup(DeleteBillGroupCommand cmd) {
        boolean workFlag = isInWorkGroup(cmd.getBillGroupId(),false);
        if(workFlag){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_ACCESS_DENIED,"账单组已经在工作，不能删除");
        }
        assetProvider.deleteBillGroupAndRules(cmd.getBillGroupId());
    }

    @Override
    public ListChargingItemDetailForBillGroupDTO listChargingItemDetailForBillGroup(BillGroupRuleIdCommand cmd) {
        return assetProvider.listChargingItemDetailForBillGroup(cmd.getBillGroupRuleId());
    }

    private boolean isInWorkGroup(Long billGroupId, boolean b) {
        return assetProvider.checkBillsByBillGroupId(billGroupId);
    }

    private boolean isInWorkGroupRule(EhPaymentBillGroupsRules rule, boolean b) {
        return assetProvider.isInWorkGroupRule(rule,b);
    }


    private List<String> setFormula( String str) {
        List<String> formulaAndJson = new ArrayList<>();
        String formula = str;
        formula = formula.replace("[[","");
        formula = formula.replace("]]","");

        formulaAndJson.add(formula);
        Set<String> replaces = new HashSet<>();
        String formulaJson = formula;
        char[] formularChars = formulaJson.toCharArray();
        int index = 0;
        int start = 0;
        while(index < formularChars.length){
            if(formularChars[index]=='+'||formularChars[index]=='-'||formularChars[index]=='*'||formularChars[index]=='/'||index == formularChars.length-1){
                replaces.add(formulaJson.substring(start,index==formulaJson.length()-1?index+1:index));
                start = index+1;
            }
            index++;
        }
        Iterator<String> iterator = replaces.iterator();
        while(iterator.hasNext()){
            String targetStr = iterator.next();
            String substitute = assetProvider.getVariableIdenfitierByName(targetStr);
            if(!org.apache.commons.lang.StringUtils.isEmpty(substitute)){
                formulaJson = formulaJson.replace(targetStr,substitute);
            }
        }

        formulaAndJson.add(formulaJson);
        return formulaAndJson;
    }

    private void checkNullProhibit(String name , Object object) {
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

    private AssetVendor checkAssetVendor(Integer namespaceId){
        if(null == namespaceId) {
            LOGGER.error("checkAssetVendor namespaceId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "checkAssetVendor namespaceId cannot be null.");
        }
        AssetVendor assetVendor = assetProvider.findAssetVendorByNamespace(namespaceId);
        if(null == assetVendor) {
            LOGGER.error("assetVendor not found, assetVendor namespaceId={}, targetId={}", namespaceId);
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
