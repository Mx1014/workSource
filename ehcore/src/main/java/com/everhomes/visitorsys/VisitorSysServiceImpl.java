// @formatter:off
package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.aclink.DoorAccess;
import com.everhomes.aclink.DoorAccessProvider;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.messaging.MessagingService;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.parking.handler.Utils;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.OfficialActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.enterprise.FindEnterpriseDetailCommand;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.organization.OfficeSiteDTO;
import com.everhomes.rest.organization.OrganizationAndDetailDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.OrganizationQueryResult;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.rest.visitorsys.ui.*;
import com.everhomes.search.FreqVisitorSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.search.VisitorsysSearcher;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/27 15:18
 */
@Component
public class VisitorSysServiceImpl implements VisitorSysService{
    /**
     * 检查必填参数
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorSysServiceImpl.class);
    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final String[] checkMustFillField = {"visitorName","visitorPhone",
            "enterpriseId","enterpriseName","officeLocationId","officeLocationName",
            "visitReasonId","visitReason"};
    //提取字符串前的数字
    final Pattern numExtract = Pattern.compile("^([\\d]+).*");
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    public VisitorSysVisitorProvider visitorSysVisitorProvider;
    @Autowired
    public VisitorSysActionProvider visitorSysActionProvider;
    @Autowired
    public VisitorSysOfficeLocationProvider visitorSysOfficeLocationProvider;
    @Autowired
    public VisitorSysVisitReasonProvider visitorSysVisitReasonProvider;
    @Autowired
    public VisitorSysCodingProvider visitorSysCodingProvider;
    @Autowired
    public VisitorSysOwnerCodeProvider visitorSysOwnerCodeProvider;
    @Autowired
    public VisitorSysConfigurationProvider visitorSysConfigurationProvider;
    @Autowired
    public ConfigurationProvider configurationProvider;
    @Autowired
    public CommunityProvider communityProvider;
    @Autowired
    public OrganizationProvider organizationProvider;
    @Autowired
    public AddressProvider addressProvider;
    @Autowired
    public VisitorsysSearcher visitorsysSearcher;
    @Autowired
    public OrganizationSearcher organizationSearcher;
    @Autowired
    public OrganizationService organizationService;
    @Autowired
    public ContentServerService contentServerService;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    public SmsProvider smsProvider;
    @Autowired
    public BigCollectionProvider bigCollectionProvider;
    @Autowired
    public VisitorSysDeviceProvider visitorSysDeviceProvider;
    @Autowired
    private VisitorSysBlackListProvider visitorSysBlackListProvider;
    @Autowired
    private GeneralFormProvider generalFormProvider;
    @Autowired
    private DoorAccessService doorAccessService;
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
    @Autowired
    private VisitorSysMessageReceiverProvider messageReceiverProvider;
    @Autowired
    private PortalVersionProvider portalVersionProvider;
    @Autowired
    private ServiceModuleAppProvider serviceModuleAppProvider;
    @Autowired
    private AppNamespaceMappingProvider appNamespaceMappingProvider;
    @Autowired
    private AppProvider appProvider;
    @Autowired
    private VisitorSysDoorAccessProvider visitorSysDoorAccessProvider;
    @Autowired
    private DoorAccessProvider doorAccessProvider;
    @Autowired
    private VisitorSysHKWSUtil HKWSUtil;
    @Autowired
    private VisitorSysDingFengHuiUtil DFHUtil;
    @Autowired
    private FreqVisitorSearcher freqVisitorSearcher;

    @Override
    public ListBookedVisitorsResponse listBookedVisitors(ListBookedVisitorsCommand cmd) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(cmd.getOwnerType());
        VisitorsysSearchFlagType searchFlagType = checkSearchFlag(cmd.getSearchFlag());
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY && searchFlagType==VisitorsysSearchFlagType.BOOKING_MANAGEMENT) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_BOOKING_MANAGEMENT, cmd.getAppId(), null, cmd.getOwnerId());
        }else if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY && searchFlagType==VisitorsysSearchFlagType.VISITOR_MANAGEMENT){
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_VISITOR_MANAGEMENT, cmd.getAppId(), null, cmd.getOwnerId());
        }
//        else if(visitorsysOwnerType == VisitorsysOwnerType.ENTERPRISE && searchFlagType==VisitorsysSearchFlagType.BOOKING_MANAGEMENT){
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_BOOKING_MANAGEMENT_ENT, cmd.getAppId(), cmd.getOwnerId(), null);
//        }else if(visitorsysOwnerType == VisitorsysOwnerType.ENTERPRISE && searchFlagType==VisitorsysSearchFlagType.VISITOR_MANAGEMENT){
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_VISITOR_MANAGEMENT_ENT, cmd.getAppId(), cmd.getOwnerId(), null);
//        }
        return listBookedVisitorsWithOutACL(cmd);
    }

    private ListBookedVisitorsResponse listBookedVisitorsWithOutACL(ListBookedVisitorsCommand cmd) {
        checkOwnerType(cmd.getOwnerType());
        checkSearchFlag(cmd.getSearchFlag());

        Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        Long pageAnchor = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();
        ListBookedVisitorParams params = ConvertHelper.convert(cmd, ListBookedVisitorParams.class);
        params.setPageSize(pageSize);
        params.setPageAnchor(pageAnchor);

        return visitorsysSearcher.searchVisitors(params);
    }

    @Override
    public GetBookedVisitorByIdResponse getBookedVisitorById(GetBookedVisitorByIdCommand cmd) {
        beforePostForWeb(cmd);
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        if(visitor==null)
            return null;
        GetBookedVisitorByIdResponse response = ConvertHelper.convert(visitor, GetBookedVisitorByIdResponse.class);
        response.setVisitorPicUrl(contentServerService.parserUri(visitor.getVisitorPicUri()));
        response.setVisitorSignUrl(contentServerService.parserUri(visitor.getVisitorSignUri()));
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(visitor.getOwnerType());
        VisitorSysVisitor relatedVisitor = getRelatedVisitor(visitor);
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            response.setCommunityFormValues(JSONObject.parseObject(visitor.getFormJsonValue(),
                    new TypeReference<List<VisitorsysApprovalFormItem>>() {}));
            response.setEnterpriseFormValues(JSONObject.parseObject(relatedVisitor.getFormJsonValue(),
                    new TypeReference<List<VisitorsysApprovalFormItem>>() {}));
        }else{
            response.setEnterpriseFormValues(JSONObject.parseObject(visitor.getFormJsonValue(),
                    new TypeReference<List<VisitorsysApprovalFormItem>>() {}));
            response.setCommunityFormValues(JSONObject.parseObject(relatedVisitor.getFormJsonValue(),
                    new TypeReference<List<VisitorsysApprovalFormItem>>() {}));
        }
        response.setVisitorActionList(generateActionList(visitor,relatedVisitor));
        return response;
    }

    /**
     * 生成事件列表
     * @param visitor
     * @param relatedVisitor
     * @return
     */
    private List<BaseVisitorActionDTO> generateActionList(VisitorSysVisitor visitor, VisitorSysVisitor relatedVisitor) {
        List<BaseVisitorActionDTO> list = new ArrayList<>(6);
        VisitorsysOwnerType ownerType = checkOwnerType(visitor.getOwnerType());
        if(ownerType == VisitorsysOwnerType.COMMUNITY){
            addActionDto(list,visitor);
            addActionDto(list,relatedVisitor);
        }else{
            addActionDto(list,relatedVisitor);
            addActionDto(list,visitor);
        }
        return list;
    }

    /**
     * 将visitor中的时间发送组装到list中
     * @param list
     * @param visitor
     */
    private void addActionDto(List<BaseVisitorActionDTO> list, VisitorSysVisitor visitor){
        List<VisitorSysAction> actionList = visitorSysActionProvider.listVisitorSysActionByVisitorId(visitor.getId());
        list.addAll(actionList.stream().map(r->{
            BaseVisitorActionDTO dto = new BaseVisitorActionDTO();
            dto.setActionType(r.getActionType());
            dto.setTime(r.getCreateTime());
            dto.setUid(r.getCreatorUid());
            dto.setUname(r.getCreatorName());
            return dto;
        }).collect(Collectors.toList()));
    }

    /**
     * 获取关联的访客预约
     * @param visitor
     * @return
     */
    private VisitorSysVisitor getRelatedVisitor(VisitorSysVisitor visitor) {
        if(visitor.getParentId()==null)
            return null;
        if(visitor.getParentId()==0L){
            return visitorSysVisitorProvider.findVisitorSysVisitorByParentId(visitor.getNamespaceId(), visitor.getId());
        }
        return visitorSysVisitorProvider.findVisitorSysVisitorById(visitor.getNamespaceId(), visitor.getParentId());
    }

    @Override
    public ListOfficeLocationsResponse listOfficeLocations(ListOfficeLocationsCommand cmd) {
        VisitorsysOwnerType ownerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

        Integer pageSize = PaginationConfigHelper.getMaxPageSize(configurationProvider, cmd.getPageSize());
        Long pageAnchor = cmd.getPageAnchor() == null ? Long.MAX_VALUE : cmd.getPageAnchor();//倒序使用long的最大值，正序使用0
        List<VisitorSysOfficeLocation> visitorSysOfficeLocations = visitorSysOfficeLocationProvider.listVisitorSysOfficeLocation(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), pageSize, pageAnchor);
        ListOfficeLocationsResponse response = new ListOfficeLocationsResponse();
        if(visitorSysOfficeLocations!=null && visitorSysOfficeLocations.size()==pageSize){
            response.setNextPageAnchor(visitorSysOfficeLocations.get(pageSize-1).getId());
        }
        if(visitorSysOfficeLocations.size()==0) {
            List<BaseOfficeLocationDTO> officeLocationList = new ArrayList<>();
            VisitorSysOfficeLocation location = ConvertHelper.convert(cmd, VisitorSysOfficeLocation.class);
            if (ownerType.ENTERPRISE == ownerType) {
                Organization organization = organizationProvider.findOrganizationById(cmd.getOwnerId());
                location.setOfficeLocationName(organization.getName());
            } else {
                Community community = communityProvider.findCommunityById(cmd.getOwnerId());
                location.setOfficeLocationName(community.getName());
            }
            Tuple<VisitorSysOfficeLocation, Boolean> enter = coordinationProvider.getNamedLock(CoordinationLocks.VISITOR_SYS_LOCATION
                    + cmd.getOwnerType()
                    + cmd.getOwnerId()).enter(() -> {
                List<VisitorSysOfficeLocation> locations = visitorSysOfficeLocationProvider.listVisitorSysOfficeLocation(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), pageSize, pageAnchor);
                if(locations.size()==0) {
                    location.setStatus(CommonStatus.ACTIVE.getCode());
                    visitorSysOfficeLocationProvider.createVisitorSysOfficeLocation(location);
                    return location;
                }
                return locations.get(0);
            });
            officeLocationList.add(ConvertHelper.convert(enter.first(), BaseOfficeLocationDTO.class));
            response.setOfficeLocationList(officeLocationList);
        }else {
            response.setOfficeLocationList(visitorSysOfficeLocations.stream().map(r -> ConvertHelper.convert(r, BaseOfficeLocationDTO.class)).collect(Collectors.toList()));
        }
        return response;
    }

//    @Override
//    public ListOfficeLocationsResponse listOfficeLocations(ListOfficeLocationsCommand cmd) {
//        VisitorsysOwnerType ownerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
//        ListOfficeLocationsResponse response = new ListOfficeLocationsResponse();
//        Integer pageSize = PaginationConfigHelper.getMaxPageSize(configurationProvider, cmd.getPageSize());
//        Long pageAnchor = cmd.getPageAnchor() == null ? Long.MAX_VALUE : cmd.getPageAnchor();//倒序使用long的最大值，正序使用0
//        List<VisitorSysOfficeLocation> visitorSysOfficeLocations = visitorSysOfficeLocationProvider.listVisitorSysOfficeLocation(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), pageSize, pageAnchor);
//
//        if(ownerType.getCode().equals(VisitorsysOwnerType.ENTERPRISE.getCode())){
//
////          同步数据
//            FindEnterpriseDetailCommand orgCmd = new FindEnterpriseDetailCommand();
//            orgCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
//            orgCmd.setOrganizationId(cmd.getOwnerId());
//            OrganizationAndDetailDTO orgDetail = organizationService.getOrganizationDetailByOrgId(orgCmd);
//            List<OfficeSiteDTO> list = orgDetail.getOfficeSites();
//
//            if(null != list && list.size() > 0){
//                for (OfficeSiteDTO r : list){
//                    boolean isExist = false;
//                    for (VisitorSysOfficeLocation r1 : visitorSysOfficeLocations){
//                        if(r.getId().equals(r1.getReferId())){
//                            r1.setOfficeLocationName(r.getSiteName());
//                            isExist = true;
//                            visitorSysOfficeLocationProvider.updateVisitorSysOfficeLocation(r1);
//                            break;
//                        }
//                    }
//                    if(!isExist){
//                        VisitorSysOfficeLocation location = ConvertHelper.convert(cmd, VisitorSysOfficeLocation.class);
//                        location.setOfficeLocationName(r.getSiteName());
//                        location.setReferId(r.getId());
//                        location.setReferType("EhOrganizationWorkplaces");
//                        location.setStatus(CommonStatus.ACTIVE.getCode());
//                        visitorSysOfficeLocationProvider.createVisitorSysOfficeLocation(location);
//                    }
//                }
//            }
//
//            List<VisitorSysOfficeLocation> resultlist = visitorSysOfficeLocationProvider.listVisitorSysOfficeLocation(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), pageSize, pageAnchor);
//            response.setOfficeLocationList(resultlist.stream().map(r->ConvertHelper.convert(r,BaseOfficeLocationDTO.class)).collect(Collectors.toList()));
//        } else {
//            if(visitorSysOfficeLocations.size() == 0){
//                VisitorSysOfficeLocation location = ConvertHelper.convert(cmd, VisitorSysOfficeLocation.class);
//                Community community = communityProvider.findCommunityById(cmd.getOwnerId());
//                location.setOfficeLocationName(community.getName());
//                location.setStatus(CommonStatus.ACTIVE.getCode());
//                visitorSysOfficeLocationProvider.createVisitorSysOfficeLocation(location);
//
//                visitorSysOfficeLocations = visitorSysOfficeLocationProvider.listVisitorSysOfficeLocation(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), pageSize, pageAnchor);
//            }
//
//            response.setOfficeLocationList(visitorSysOfficeLocations.stream().map(r -> ConvertHelper.convert(r, BaseOfficeLocationDTO.class)).collect(Collectors.toList()));
//        }
//        return response;
//    }

    @Override
    public ListCommunityOrganizationsResponse listCommunityOrganizations(ListCommunityOrganizationsCommand cmd) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        if(visitorsysOwnerType==VisitorsysOwnerType.ENTERPRISE){
            Organization organization = organizationProvider.findOrganizationById(cmd.getOwnerId());
            return new ListCommunityOrganizationsResponse(0L,
                    new ArrayList(Arrays.asList(new BaseVisitorEnterpriseDTO(cmd.getOwnerId(),organization.getName()))));
        }
        SearchOrganizationCommand orgCmd = ConvertHelper.convert(cmd, SearchOrganizationCommand.class);
        orgCmd.setKeyword(cmd.getKeyWords());
        orgCmd.setCommunityId(cmd.getOwnerId());
        orgCmd.setSimplifyFlag((byte)1);
        OrganizationQueryResult organizationQueryResult = organizationSearcher.fuzzyQueryOrganizationByName(orgCmd);
        if(organizationQueryResult==null || organizationQueryResult.getDtos()==null){
            return null;
        }
        ListCommunityOrganizationsResponse response = new ListCommunityOrganizationsResponse();
        response.setNextPageAnchor(organizationQueryResult.getPageAnchor());
        response.setVisitorEnterpriseList(organizationQueryResult.getDtos().stream().map(r->{
            BaseVisitorEnterpriseDTO dto = new BaseVisitorEnterpriseDTO();
            dto.setEnterpriseId(r.getId());
            dto.setEnterpriseName(r.getName());
            return dto;
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public ListVisitReasonsResponse listVisitReasons(BaseVisitorsysCommand cmd) {
        beforePostForWeb(cmd);
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        List<VisitorSysVisitReason> visitorSysVisitReasons = visitorSysVisitReasonProvider.listVisitorSysVisitReason(cmd.getNamespaceId(),cmd.getCommunityType());
        ListVisitReasonsResponse response = new ListVisitReasonsResponse();
        response.setVisitorReasonList(visitorSysVisitReasons.stream().map(r->{
            BaseVisitorReasonDTO convert = new BaseVisitorReasonDTO();
            convert.setVisitReason(r.getVisitReason());
            convert.setVisitReasonId(r.getId());
            return convert;
        }).collect(Collectors.toList()));
        return response;
    }

    /**
     * 创建或者更新访客
     * @param cmd
     * @return
     */
    @Override
    public GetBookedVisitorByIdResponse createOrUpdateVisitor(CreateOrUpdateVisitorCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        checkBlackList(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getVisitorPhone(),cmd.getEnterpriseId());
        VisitorSysVisitor visitor = null;
        boolean hasSendMessage = false;
        if(cmd.getId()==null) {
            visitor = createVisitor(cmd);
        }else{
            VisitorSysVisitor oldVisitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(),cmd.getId());
            hasSendMessage = oldVisitor!=null && VisitorsysStatus.HAS_VISITED == VisitorsysStatus.fromCode(oldVisitor.getVisitStatus());
            checkConcurrentModify(cmd,oldVisitor);
            visitor = updateVisitor(cmd,oldVisitor);
            callbackToQLFK(visitor);//回调访客机
        }
        sendVisitorSms(visitor,VisitorsysFlagType.fromCode(cmd.getSendSmsFlag()));//发送访客邀请函
        if(!hasSendMessage)
            sendMessageInviter(visitor);//发送消息给邀请者
        GetBookedVisitorByIdResponse convert = ConvertHelper.convert(visitor, GetBookedVisitorByIdResponse.class);
        convert.setVisitorPicUrl(contentServerService.parserUri(convert.getVisitorPicUri()));
        convert.setVisitorSignUrl(contentServerService.parserUri(convert.getVisitorSignUri()));
        return convert;
    }

    private void callbackToQLFK(VisitorSysVisitor visitor) {
        VisitorsysStatus visitStatus = checkVisitStatus(visitor.getVisitStatus());
        VisitorsysOwnerType ownerType = checkOwnerType(visitor.getOwnerType());
        VisitorsysSourceType sourceType = VisitorsysSourceType.fromCode(visitor.getSource());
        VisitorsysNotifyThirdType visitorsysNotifyThirdType = VisitorsysNotifyThirdType.fromCode(visitor.getNotifyThirdSuccessFlag());
        if(VisitorsysStatus.HAS_VISITED != visitStatus
                || ownerType!=VisitorsysOwnerType.COMMUNITY
                || sourceType != VisitorsysSourceType.OUTER
                || visitorsysNotifyThirdType == VisitorsysNotifyThirdType.CALLBACK_SUCCESS){
            return;
        }
        String callbackurl = configurationProvider.getValue(visitor.getNamespaceId(),"visitorsys.lufu.callback", "");
        Map<String,String> params = new HashMap();
        params.put("visitorToken", WebTokenGenerator.getInstance().toWebToken(visitor.getId()));
        params.put("status", visitor.getVisitStatus()+"");
        AppNamespaceMapping mapping = appNamespaceMappingProvider.findAppNamespaceMappingByNamespaceId(visitor.getNamespaceId());
        App app = appProvider.findAppByKey(mapping.getAppKey());
        params.put("appKey", app.getAppKey());
        String s = SignatureHelper.computeSignature(params, app.getSecretKey());
        params.put("signature", s);
        int i = 0;
        while(i<3) {
            try {
                String result = Utils.post(callbackurl, params);
                LOGGER.info(callbackurl+" result:"+result);
                RestResponse o = (RestResponse) StringHelper.fromJsonString(result, RestResponse.class);
                if (o.getErrorCode() == 200) {
                    visitor.setNotifyThirdSuccessFlag(VisitorsysNotifyThirdType.CALLBACK_SUCCESS.getCode());
                    visitorSysVisitorProvider.updateVisitorSysVisitor(visitor);
                    break;
                }
            }catch (Exception e){
                LOGGER.error("visitorsys post error:{}",e);
            }
            i++;
        }


    }

    private void sendMessageToAdmin(VisitorSysVisitor visitor,CreateOrUpdateVisitorCommand cmd) {
        VisitorsysStatus visitStatus = checkVisitStatus(visitor.getVisitStatus());
        VisitorsysOwnerType ownerType = checkOwnerType(visitor.getOwnerType());
        if(VisitorsysStatus.WAIT_CONFIRM_VISIT != visitStatus){
            return;
        }

        Long appId = cmd.getAppId();
        if(appId == null) {
            PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(visitor.getNamespaceId());
            List<ServiceModuleApp> serviceModuleApps = serviceModuleAppProvider.listServiceModuleApp(visitor.getNamespaceId(), releaseVersion == null ? null : releaseVersion.getId(), VisitorsysConstant.COMMUNITY_MODULE_ID);
            if (serviceModuleApps != null && serviceModuleApps.size() > 0) {
                appId = serviceModuleApps.get(serviceModuleApps.size()-1).getOriginId();
            }
        }
        String homeurl = configurationProvider.getValue(ConfigConstants.HOME_URL,"");
        String contextUrl = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_ADMIN_ROUNTE, "%s/visitor-appointment/build/index.html?ns=%s&ownerType=%s&detailId=%s&appId=%s&status=%s#/visitor-detail#sign_suffix");

        String url = String.format(contextUrl, homeurl,visitor.getNamespaceId(),visitor.getOwnerType(),visitor.getId(),appId,visitor.getVisitStatus());
        List<VisitorSysMessageReceiver> list = messageReceiverProvider.listVisitorSysMessageReceiverByOwner(visitor.getNamespaceId(),visitor.getOwnerType(),visitor.getOwnerId());
        if(list == null || list.size()==0)
            return;
        for (VisitorSysMessageReceiver receiver : list) {
            try {
                // 组装路由
                OfficialActionData actionData = new OfficialActionData();
                actionData.setUrl(url);

                String uri = RouterBuilder.build(Router.BROWSER_I, actionData);
                RouterMetaObject metaObject = new RouterMetaObject();
                metaObject.setUrl(uri);

                Map<String, String> meta = new HashMap<String, String>();
                meta.put(MessageMetaConstant.MESSAGE_SUBJECT, configurationProvider.getValue(VisitorsysConstant.VISITORSYS_ADMIN_TITLE, "待确认访客"));
                meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
                meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));

                String detail = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_INVITER_DETAIL, "你有一个访客等待确认");
                MessageDTO messageDto = new MessageDTO();
                messageDto.setAppId(AppConstants.APPID_MESSAGING);
                messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
                messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), receiver.getCreatorUid().toString()));
                messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
                messageDto.setBodyType(MessageBodyType.TEXT.getCode());
                messageDto.setBody(detail);
                messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
                if(null != meta && meta.size() > 0) {
                    messageDto.getMeta().putAll(meta);
                }
//                if(ownerType == VisitorsysOwnerType.ENTERPRISE){
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                        receiver.getCreatorUid().toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
//                }
//                else {
//                    boolean hasPrivilege = userPrivilegeMgr.checkUserPrivilege(receiver.getCreatorUid(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_MODILE_MAMAGEMENT, appId, null, cmd.getOwnerId());
//                    if (hasPrivilege) {
//                    messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
//                            receiver.getCreatorUid().toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());

//                    }
//                }
            } catch (Exception e) {
                LOGGER.error("visitorsys {} send message error", receiver, e);
            }
        }

    }

    //更新访客状态的时候，检查状态是否已经被提前更新
    private void checkConcurrentModify(CreateOrUpdateVisitorCommand cmd, VisitorSysVisitor oldVisitor) {
        if(cmd == null || cmd.getVisitStatus()==null || cmd.getBookingStatus() == null){
            return ;
        }

        if((cmd.getBookingStatus()== VisitorsysStatus.REJECTED_VISIT.getCode()
                && cmd.getVisitStatus()== VisitorsysStatus.REJECTED_VISIT.getCode()) ||
                (cmd.getBookingStatus()== VisitorsysStatus.HAS_VISITED.getCode()
                        && cmd.getVisitStatus()== VisitorsysStatus.HAS_VISITED.getCode())){
            if(oldVisitor.getVisitStatus() == VisitorsysStatus.REJECTED_VISIT.getCode()
                    || oldVisitor.getVisitStatus()  == VisitorsysStatus.HAS_VISITED.getCode()){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_HAS_UPDATE_VISITOR,
                        "此访客记录已被处理");
            }
        }
    }

    /**
     * 更新访客
     * @param cmd
     */
    private VisitorSysVisitor updateVisitor(CreateOrUpdateVisitorCommand cmd,VisitorSysVisitor oldVisitor) {
        String oldFormatVisitorTime = null;
        if(oldVisitor !=null && oldVisitor.getPlannedVisitTime()!=null) {
            oldFormatVisitorTime = oldVisitor.getPlannedVisitTime().toLocalDateTime().format(YYYYMMDD);
        }
        VisitorSysVisitor visitor = checkUpdateVisitor(cmd,oldVisitor);
        String newFormatVisitorTime = null;
        if(visitor !=null && visitor.getPlannedVisitTime()!=null) {
            newFormatVisitorTime = visitor.getPlannedVisitTime().toLocalDateTime().format(YYYYMMDD);
        }
        VisitorsysVisitorType type = checkVisitorType(visitor.getVisitorType());
        //如果计划到访时间修改为不是当天，那么重新生成邀请码，生成唯一的邀请码,锁的对象是owner，因为邀请码会有区分owner的部分
        if(type == VisitorsysVisitorType.BE_INVITED
                && newFormatVisitorTime!=null
                && !newFormatVisitorTime.equals(oldFormatVisitorTime)) {
            coordinationProvider.getNamedLock(CoordinationLocks.VISITOR_SYS_GEN_IN_NO.getCode()
                    +visitor.getOwnerType()+visitor.getOwnerId()).enter(()-> {
                visitor.setInvitationNo(generateInvitationNo(visitor));
                return null;
            });
        }
        dbProvider.execute(r -> updateVisitorSysVisitor(visitor, cmd));
        return visitor;
    }

    /**
     * 创建预约/访客
     * @param cmd
     */
    private VisitorSysVisitor createVisitor(CreateOrUpdateVisitorCommand cmd) {
        //检查参数，生成访客实体
        VisitorSysVisitor visitor = checkCreateVisitor(cmd);
        //这里上锁的目的是，生成唯一的邀请码,锁的对象是owner，因为邀请码会有区分owner的部分
        coordinationProvider.getNamedLock(CoordinationLocks.VISITOR_SYS_GEN_IN_NO.getCode()
                +visitor.getOwnerType()+visitor.getOwnerId()).enter(()-> {
            visitor.setInvitationNo(generateInvitationNo(visitor));
            return null;
        });
        //这里上事务的原因是，需要同时创建园区下公司的访客/预约记录和同步es
        dbProvider.execute(action ->createVisitorSysVisitor(visitor,cmd));
        return visitor;
    }

    /**
     * 给邀请预约到访访客的邀请者这发送消息
     * @param visitor
     */
    private void sendMessageInviter(VisitorSysVisitor visitor) {
        VisitorsysVisitorType visitorType = checkVisitorType(visitor.getVisitorType());
        VisitorsysStatus visitStatus = checkVisitStatus(visitor.getVisitStatus());
        VisitorsysOwnerType ownerType = checkOwnerType(visitor.getOwnerType());
        VisitorsysFlagType sendInviterFlag = checkVisitorsysFlag(visitor.getSendMessageInviterFlag());
        if(sendInviterFlag == null
                || sendInviterFlag == VisitorsysFlagType.NO
                || ownerType == VisitorsysOwnerType.COMMUNITY
                || visitorType == VisitorsysVisitorType.TEMPORARY
                || VisitorsysStatus.HAS_VISITED != visitStatus){
            return;
        }
        String homeurl = configurationProvider.getValue(ConfigConstants.HOME_URL,"");
        String contextUrl = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_INVITER_ROUNTE, "");
        //http://10.1.10.84/visitor-appointment/build/index.html?id=460&ns=1000000#/appointment-detail#sign_suffix
        String url = String.format(contextUrl, homeurl,visitor.getId(),visitor.getNamespaceId());

        // 组装路由
        OfficialActionData actionData = new OfficialActionData();
        actionData.setUrl(url);

        String uri = RouterBuilder.build(Router.BROWSER_I, actionData);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(uri);

        Map<String, String> meta = new HashMap<String, String>();
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, configurationProvider.getValue(VisitorsysConstant.VISITORSYS_INVITER_TITLE, "访客到访通知"));
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));

        String detail = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_INVITER_DETAIL, "你邀请的访客 %s %s 已到访你的企业。");
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), visitor.getInviterId().toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(String.format(detail,visitor.getVisitorName(),visitor.getVisitorPhone()));
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
        }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                visitor.getInviterId().toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    /**
     * 检查黑名单
     * @param namespaceId
     * @param ownerType
     * @param ownerId
     * @param visitorPhone
     * @param enterpriseId
     */
    private void checkBlackList(Integer namespaceId, String ownerType, Long ownerId, String visitorPhone,Long enterpriseId) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(ownerType);
        VisitorSysBlackList blackList = visitorSysBlackListProvider.findVisitorSysBlackListByPhone(namespaceId, ownerType, ownerId, visitorPhone);
        if(blackList!=null){
            if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INBLACKLIST_PHONE_COMMUNITY,
                        "black list " + ownerType + ", phone = " + visitorPhone);
            }else {
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INBLACKLIST_PHONE_ENTERPRISE,
                        "black list " + ownerType + ", phone = " + visitorPhone);
            }
        }
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY){
            blackList = visitorSysBlackListProvider.findVisitorSysBlackListByPhone(namespaceId, VisitorsysOwnerType.ENTERPRISE.getCode(),
                    enterpriseId, visitorPhone);
            if(blackList!=null){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INBLACKLIST_PHONE_ENTERPRISE,
                        "black list enterprise, phone = " + visitorPhone);
            }
        }else{
            Long communityId = organizationService.getOrganizationActiveCommunityId(ownerId);
            blackList = visitorSysBlackListProvider.findVisitorSysBlackListByPhone(namespaceId, VisitorsysOwnerType.COMMUNITY.getCode(),
                    communityId, visitorPhone);
            if(blackList!=null){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INBLACKLIST_PHONE_COMMUNITY,
                        "black list community, phone = " + visitorPhone);
            }
        }
    }

    /**
     * 创建访客
     * @param visitor
     * @param cmd
     * @return
     */
    private Object createVisitorSysVisitor(VisitorSysVisitor visitor, CreateOrUpdateVisitorCommand cmd) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        visitorSysVisitorProvider.createVisitorSysVisitor(visitor);
        visitorsysSearcher.syncVisitor(visitor);
        createVisitorActions(visitor);
        sendMessageToAdmin(visitor,cmd);//发送消息给应用管理员，系统管理员，超级管理员，让管理员确认
        VisitorSysVisitor relatedVisitor = null;
        if (visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            relatedVisitor = generateRelatedVisitor(visitor,cmd.getEnterpriseFormValues());
        }else {
            relatedVisitor = generateRelatedVisitor(visitor,cmd.getCommunityFormValues());
        }
        checkDoorGuard(relatedVisitor);
        visitorSysVisitorProvider.createVisitorSysVisitor(relatedVisitor);
        visitorsysSearcher.syncVisitor(relatedVisitor);
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        VisitorsysVisitorType visitorType = checkVisitorType(visitor.getVisitorType());
        if(visitorType == VisitorsysVisitorType.BE_INVITED) {
            if(namespaceId == 999925){
//          上海金茂对接
                HKWSUtil.addAppointment(visitor);
            } else if (namespaceId == 999951){
//          鼎峰汇对接
                DFHUtil.doInvite(visitor);
            }
        }
//        createVisitorActions(relatedVisitor);
        sendMessageToAdmin(relatedVisitor,cmd);//发送消息给应用管理员，系统管理员，超级管理员，让管理员确认
        return null;
    }

    /**
     * 创建事件
     * @param visitor
     */
    private void createVisitorActions(VisitorSysVisitor visitor) {
        VisitorsysStatus visitStatus = checkVisitStatus(visitor.getVisitStatus());
        VisitorsysOwnerType ownerType = checkOwnerType(visitor.getOwnerType());
        VisitorSysAction action = null;
        Byte actionFlag = null;
        switch (visitStatus){
            case WAIT_CONFIRM_VISIT:
                actionFlag = ownerType==VisitorsysOwnerType.COMMUNITY?(byte)1:(byte)4;
                action = visitorSysActionProvider.findVisitorSysActionByAction(visitor.getId(),actionFlag);
                break;
            case HAS_VISITED:
                actionFlag = ownerType==VisitorsysOwnerType.COMMUNITY?(byte)2:(byte)5;
                action = visitorSysActionProvider.findVisitorSysActionByAction(visitor.getId(),actionFlag);
                break;
            case REJECTED_VISIT:
                actionFlag = ownerType==VisitorsysOwnerType.COMMUNITY?(byte)3:(byte)6;
                action = visitorSysActionProvider.findVisitorSysActionByAction(visitor.getId(),actionFlag);
                break;
            default:
                return;
        }
//      自助登记状态置为已到访,活动记录为自助登记
        VisitorSysConfiguration config = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(visitor.getNamespaceId(),visitor.getOwnerType(),visitor.getOwnerId());
        if(null == config.getBaseConfig() || TrueOrFalseFlag.FALSE.getCode().equals(config.getBaseConfig().getVisitorConfirmFlag())){
            if(null != visitor.getFromDevice() && TrueOrFalseFlag.TRUE.getCode().equals(visitor.getFromDevice())){
                actionFlag = ownerType==VisitorsysOwnerType.COMMUNITY?(byte)1:(byte)4;
                action = visitorSysActionProvider.findVisitorSysActionByAction(visitor.getId(),actionFlag);
            }
        }
        if(action == null){
            action = new VisitorSysAction();
            action.setActionType(actionFlag);
            User user = UserContext.current().getUser();
            action.setCreatorUid((user==null || user.getId()==0)?-1:user.getId());
            action.setCreatorName((user==null || user.getId()==0)?visitor.getVisitorName():user.getNickName());
            action.setCreateTime(new Timestamp(System.currentTimeMillis()));
            action.setNamespaceId(visitor.getNamespaceId());
            action.setVisitorId(visitor.getId());
            visitorSysActionProvider.createVisitorSysAction(action);
        }
    }

    /**
     * 更新访客
     * @param visitor
     * @param cmd
     * @return
     */
    private Object updateVisitorSysVisitor(VisitorSysVisitor visitor, CreateOrUpdateVisitorCommand cmd) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        visitorSysVisitorProvider.updateVisitorSysVisitor(visitor);
        visitorsysSearcher.syncVisitor(visitor);
        createVisitorActions(visitor);
        sendMessageToAdmin(visitor,cmd);//发送消息给应用管理员，系统管理员，超级管理员，让管理员确认
        VisitorSysVisitor relatedVisitor = null;
        if (visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            relatedVisitor = generateRelatedVisitor(visitor,cmd.getEnterpriseFormValues());
        }else {
            relatedVisitor = generateRelatedVisitor(visitor,cmd.getCommunityFormValues());
        }
        checkDoorGuard(relatedVisitor);
        visitorSysVisitorProvider.updateVisitorSysVisitor(relatedVisitor);
        visitorsysSearcher.syncVisitor(relatedVisitor);
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        if(namespaceId == 999925){
//          上海金茂对接
            HKWSUtil.delAppointment(visitor);
            HKWSUtil.addAppointment(visitor);
        } else if (namespaceId == 999951){
//          鼎峰汇对接
            DFHUtil.cancelInvite(visitor);
            DFHUtil.doInvite(visitor);
        }
//        createVisitorActions(relatedVisitor);
        sendMessageToAdmin(relatedVisitor,cmd);//发送消息给应用管理员，系统管理员，超级管理员，让管理员确认
        return null;
    }

    @Override
    public void sendVisitorSMS(GetBookedVisitorByIdCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        if(visitor==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_NOT_EXIST,
                    "unknow visitorid = " + cmd.getVisitorId());
        }
        sendVisitorSms(visitor,VisitorsysFlagType.YES);
    }

    /**
     * 发送访客邀请函短信，条件为 预约访客，未到访，电话不为空，短信标识为YES
     * @param visitor
     */
    private void sendVisitorSms(VisitorSysVisitor visitor, VisitorsysFlagType sendSmsFlag) {
        LOGGER.info("visitorsys sms is sending,visitor={}",visitor.toString());
        VisitorsysVisitorType visitorType = checkVisitorType(visitor.getVisitorType());
        if(visitor.getVisitorPhone()==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_NOT_EXIST,
                    "not support visitor phone = " + visitor.getVisitorPhone());
        }

        if(sendSmsFlag == VisitorsysFlagType.NO || sendSmsFlag==null) {
           return;
        }

        if(visitorType==VisitorsysVisitorType.TEMPORARY){//临时访客
            List<Tuple<String, Object>> variables = new ArrayList();
            smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_ENTERPRISE_ORLOCATION_NAME, visitor.getEnterpriseName());
            smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_INVITATIONLINK, generateInviationLink(visitor.getId()));
            String templateLocale = UserContext.current().getUser().getLocale();
            smsProvider.sendSms(visitor.getNamespaceId(), visitor.getVisitorPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.VISITORSYS_TEMP_INVITATION_LETTER, templateLocale, variables);
        }else if(visitorType==VisitorsysVisitorType.BE_INVITED){//预约访客
            List<Tuple<String, Object>> variables = new ArrayList();
            smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_APPNAME, "");
            smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_VISITENTERPRISENAME, visitor.getEnterpriseName());
            smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_INVITATIONLINK, generateInviationLink(visitor.getId()));
            String templateLocale = UserContext.current().getUser().getLocale();
            smsProvider.sendSms(visitor.getNamespaceId(), visitor.getVisitorPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.VISITORSYS_INVT_INVITATION_LETTER, templateLocale, variables);
        }
    }

    /**
     * 访客管理只删除当前访客，不删除对应的园区访客或者企业访客
     * @param cmd
     */
    @Override
    public void deleteVisitor(GetBookedVisitorByIdCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        if(cmd.getVisitorId()==null){
            return;
        }

        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        if(visitor==null){
            visitorsysSearcher.deleteById(cmd.getVisitorId());
            return;
        }
        dbProvider.execute(r->{
            visitorSysVisitorProvider.deleteVisitorSysVisitor(cmd.getNamespaceId(),cmd.getVisitorId());
            visitorsysSearcher.syncVisitor(cmd.getNamespaceId(),cmd.getVisitorId());
            createVisitorActions(cmd.getNamespaceId(),cmd.getVisitorId());
            return null;
        });

    }

    /**
     * 创建事件
     * @param namespaceId
     * @param visitorId
     */
    private void createVisitorActions(Integer namespaceId, Long visitorId) {
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(namespaceId, visitorId);
        if(visitor==null) {
            return;
        }
        createVisitorActions(visitor);
    }

    /**
     * 删除预约，会删除关联的公司、园区的预约和访客记录
     * @param cmd
     */
    @Override
    public void deleteVisitorAppoint(GetBookedVisitorByIdCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        if(cmd.getVisitorId()==null){
            return;
        }
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        if(visitor==null){
            visitorsysSearcher.deleteById(cmd.getVisitorId());
            return;
        }

        dbProvider.execute(r->{
            visitorSysVisitorProvider.deleteVisitorSysVisitorAppoint(cmd.getNamespaceId(),cmd.getVisitorId());
            visitorsysSearcher.syncVisitor(cmd.getNamespaceId(),cmd.getVisitorId());
            createVisitorActions(cmd.getNamespaceId(),cmd.getVisitorId());
            VisitorSysVisitor relatedVisitor = getRelatedVisitor(visitor);
            visitorSysVisitorProvider.deleteVisitorSysVisitorAppoint(relatedVisitor.getNamespaceId(),relatedVisitor.getId());
            visitorsysSearcher.syncVisitor(relatedVisitor.getNamespaceId(),relatedVisitor.getId());
            createVisitorActions(relatedVisitor.getNamespaceId(),relatedVisitor.getId());
            return null;
        });
    }

    @Override
    public void confirmVisitor(CreateOrUpdateVisitorCommand cmd) {
        if(cmd.getId()==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS,
                    "unknown id = null");
        }
        cmd.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
        cmd.setVisitStatus(VisitorsysStatus.HAS_VISITED.getCode());
        this.createOrUpdateVisitor(cmd);
    }

    /**
     * 将访客状态转为已确认到访
     * @param visitor
     * @return
     */
    private VisitorSysVisitor generateConfirmVisitor(VisitorSysVisitor visitor,VisitorsysVisitorType visitorType) {
        if(visitorType == VisitorsysVisitorType.BE_INVITED) {
            visitor.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
        }
        visitor.setVisitStatus(VisitorsysStatus.HAS_VISITED.getCode());
        return visitor;
    }

    /**
     * 将访客状态转为已拒绝
     * @param visitor
     * @return
     */
    private VisitorSysVisitor generateRejectVisitor(VisitorSysVisitor visitor) {
        VisitorsysVisitorType visitorType = checkVisitorType(visitor.getVisitorType());
        if(visitorType == VisitorsysVisitorType.BE_INVITED) {
            visitor.setBookingStatus(VisitorsysStatus.REJECTED_VISIT.getCode());
        }else{
            visitor.setBookingStatus(null);
        }
        visitor.setVisitStatus(VisitorsysStatus.REJECTED_VISIT.getCode());
        return visitor;
    }

    @Override
    public GetStatisticsResponse getStatistics(GetStatisticsCommand cmd) {
        return visitorsysSearcher.getStatistics(cmd);
    }

    @Override
    public AddDeviceResponse addDevice(AddDeviceCommand cmd) {
        VisitorsysOwnerType ownerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        if(ownerType == VisitorsysOwnerType.COMMUNITY) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_DEV_MANAGEMENT, cmd.getAppId(), null, cmd.getOwnerId());
        }
//        else if(ownerType == VisitorsysOwnerType.ENTERPRISE){
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_DEV_MANAGEMENT_ENT, cmd.getAppId(),cmd.getOwnerId(), null);
//        }
        if(cmd.getPairingCode()==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS,
                    "unknown pairingCode "+cmd.getPairingCode());
        }
        AddDeviceResponse response = addDeviceByPairingCode(cmd);
        return response;
    }

    /**
     * 配对码验证成功后，添加设备到设备表
     * @param cmd
     * @return
     */
    private AddDeviceResponse addDeviceByPairingCode(AddDeviceCommand cmd) {
        String pairingCode = cmd.getPairingCode();
        String key = generatePairingCodeKey(pairingCode);
        String jsonValue = null;
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        jsonValue = valueOperations.get(key);
        if (jsonValue != null && jsonValue.length() > 0) {
            VisitorSysDevice sysDevice = JSONObject.parseObject(jsonValue, VisitorSysDevice.class);
            sysDevice.setOwnerId(cmd.getOwnerId());
            sysDevice.setNamespaceId(cmd.getNamespaceId());
            sysDevice.setOwnerType(cmd.getOwnerType());
            sysDevice.setStatus(CommonStatus.ACTIVE.getCode());
            sysDevice.setPairingCode(pairingCode);
            visitorSysDeviceProvider.createVisitorSysDevice(sysDevice);
            return ConvertHelper.convert(sysDevice,AddDeviceResponse.class);
        }
        throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_PAIRING_CODE,
                "unknown pairingCode "+cmd.getPairingCode());
    }

    private String generatePairingCodeKey(String pairingCode){
        return VisitorsysConstant.VISITORSYS_PAIRINGCODE_+pairingCode;
    }

    /**
     * 获取key在redis操作的valueOperations
     */
    private ValueOperations<String, String> getValueOperations(String key) {
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations;
    }

    /**
     * 清除redis中key的缓存
     */
    private void deleteValueOperations(String key) {
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        redisTemplate.delete(key);
    }

    @Override
    public ListDevicesResponse listDevices(BaseVisitorsysCommand cmd) {
        VisitorsysOwnerType ownerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        if(ownerType == VisitorsysOwnerType.COMMUNITY) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_DEV_MANAGEMENT, cmd.getAppId(), null, cmd.getOwnerId());
        }
//        else if(ownerType == VisitorsysOwnerType.ENTERPRISE){
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_DEV_MANAGEMENT_ENT, cmd.getAppId(), cmd.getOwnerId(), null);
//        }

        List<VisitorSysDevice> deviceList = visitorSysDeviceProvider.listVisitorSysDeviceByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
        ListDevicesResponse devicesResponse = new ListDevicesResponse();
        devicesResponse.setDeviceList(deviceList.stream().map(r->ConvertHelper.convert(r,BaseDeviceDTO.class)).collect(Collectors.toList()));
        return devicesResponse;
    }

    @Override
    public void deleteDevice(DeleteDeviceCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        visitorSysDeviceProvider.deleteDevice(cmd.getNamespaceId(),cmd.getId());
    }

    @Override
    public BaseOfficeLocationDTO createOrUpdateOfficeLocation(CreateOrUpdateOfficeLocationCommand cmd) {
        VisitorSysOfficeLocation convert = checkOfficeLocation(cmd);

        if(cmd.getId()==null) {
            visitorSysOfficeLocationProvider.createVisitorSysOfficeLocation(convert);
        }else{
            visitorSysOfficeLocationProvider.updateVisitorSysOfficeLocation(convert);
        }
        return ConvertHelper.convert(convert,BaseOfficeLocationDTO.class);
    }

    private VisitorSysOfficeLocation checkOfficeLocation(CreateOrUpdateOfficeLocationCommand cmd) {
        checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        VisitorSysOfficeLocation convert = ConvertHelper.convert(cmd, VisitorSysOfficeLocation.class);
        convert.setStatus(CommonStatus.ACTIVE.getCode());
        if(cmd.getId()==null){
            return convert;
        }
        else{
            VisitorSysOfficeLocation old = visitorSysOfficeLocationProvider.findVisitorSysOfficeLocationById(cmd.getId());
            if(old==null){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_NOT_EXIST,
                        "unknown officeLoation id "+cmd.getId());
            }
            convert.setOwnerType(old.getOwnerType());
            convert.setOwnerId(old.getOwnerId());
            convert.setNamespaceId(old.getNamespaceId());
            convert.setCreateTime(old.getCreateTime());
            convert.setCreatorUid(old.getCreatorUid());
            convert.setOperatorUid(old.getOperatorUid());
            convert.setOperateTime(old.getOperateTime());
        }
        return convert;
    }

    @Override
    public void deleteOfficeLocation(DeleteOfficeLocationCommand cmd) {
        checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        visitorSysOfficeLocationProvider.deleteVisitorSysOfficeLocation(cmd.getNamespaceId(),cmd.getId());
    }

    @Override
    public GetConfigurationResponse getConfiguration(GetConfigurationCommand cmd) {
       checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
       VisitorSysConfiguration configuration = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(cmd.getNamespaceId(),cmd.getOwnerType(), cmd.getOwnerId());
        if(configuration==null){
            Tuple<VisitorSysConfiguration, Boolean> enter = coordinationProvider.getNamedLock
                    (CoordinationLocks.VISITOR_SYS_CONFIG.getCode() + cmd.getOwnerType() + cmd.getOwnerId()).enter(() -> {
                VisitorSysConfiguration newConfiguration = ConvertHelper.convert(cmd, VisitorSysConfiguration.class);
                newConfiguration.setConfigVersion(0L);
                newConfiguration.setOwnerToken(generateConfigOwnerToken());
                visitorSysConfigurationProvider.createVisitorSysConfiguration(newConfiguration);
                return newConfiguration;
            });
            if(enter.second()) {
                configuration = enter.first();
            }
        }
        GetConfigurationResponse configurationResponse = getDefaultConfiguration();
        configurationResponse = VisitorSysUtils.copyAllNotNullProperties(configuration,configurationResponse);
        configurationResponse.setSelfRegisterQrcodeUrl(generateQrcodeUrl(configuration.getOwnerToken()));
        afterGetConfiguration(configurationResponse);
        return configurationResponse;
    }

    @Override
    public void transferQrcode(TransferQrcodeCommand qrcode, HttpServletResponse resp) {
        BufferedOutputStream bos = null;
        ByteArrayOutputStream out = null;
        try {
            Integer width = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_QRCODE_WIDTH,300);
            Integer height = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_QRCODE_HEIGHT,300);
            BufferedImage image = QRCodeEncoder.createQrCodeWithOutWhite(qrcode.getQrcode(), QRCodeConfig.CHARSET_UTF8,
                    width, height, null,QRCodeConfig.FORMAT_PNG,QRCodeConfig.BLACK,0xEEFFFFFF);
            out = new ByteArrayOutputStream();
            ImageIO.write(image, QRCodeConfig.FORMAT_PNG, out);

            String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + "." + QRCodeConfig.FORMAT_PNG;
            resp.setContentType("application/octet-stream;");
            resp.setHeader("Content-disposition", "attachment; filename="
                    + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            resp.setHeader("Content-Length", String.valueOf(out.size()));

            bos = new BufferedOutputStream(resp.getOutputStream());
            ImageIO.write(image, QRCodeConfig.FORMAT_PNG, bos);
        } catch (Exception e) {
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_DOWNLOAD_QRCODE,
                    "Failed to download the package file");
        } finally {
            FileHelper.closeOuputStream(out);
            FileHelper.closeOuputStream(bos);
        }
    }


    private GetConfigurationResponse getDefaultConfiguration() {
        GetConfigurationResponse response = new GetConfigurationResponse();
        GeneralForm forms = generalFormProvider.getActiveGeneralFormByName(VisitorsysConstant.COMMUNITY_MODULE_ID, 0L, "visitorsys", "visitorsys");
        if(forms!=null) {
            List<VisitorsysApprovalFormItem> formConfig = JSONObject.parseObject(forms.getTemplateText(),
                    new TypeReference<List<VisitorsysApprovalFormItem>>() {
                    });
            for(VisitorsysApprovalFormItem form : formConfig){
                if("invalidTime".equals(form.getFieldName())){
                    formConfig.remove(form);
                    break;
                }
            }
            response.setFormConfig(formConfig);
        }
        VisitorsysBaseConfig visitorsysBaseConfig = new VisitorsysBaseConfig();
        visitorsysBaseConfig.generateDefaultValue();
        response.setBaseConfig(visitorsysBaseConfig);
        response.setPassCardConfig(new VisitorsysPassCardConfig());
        return response;
    }

    @Override
    public GetConfigurationResponse updateConfiguration(UpdateConfigurationCommand cmd) {
        checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        Tuple<VisitorSysConfiguration, Boolean> enter = coordinationProvider.getNamedLock
                (CoordinationLocks.VISITOR_SYS_CONFIG.getCode() + cmd.getOwnerType() + cmd.getOwnerId()).enter(() -> {
            VisitorSysConfiguration configuration = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
            if (configuration == null) {
                configuration = ConvertHelper.convert(cmd, VisitorSysConfiguration.class);
                visitorSysConfigurationProvider.createVisitorSysConfiguration(configuration);
            } else {
                configuration = VisitorSysUtils.copyNotNullProperties(cmd, configuration);
                visitorSysConfigurationProvider.updateVisitorSysConfiguration(configuration);
            }
            return configuration;
        });
        VisitorSysConfiguration configuration = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
        GetConfigurationResponse response = ConvertHelper.convert(configuration,GetConfigurationResponse.class);
        response.setSelfRegisterQrcodeUrl(generateQrcodeUrl(enter.first().getOwnerToken()));
        afterGetConfiguration(response);
        return response;
    }

    private void afterGetConfiguration(GetConfigurationResponse response){
        response.setLogoUrl(contentServerService.parserUri(response.getLogoUri()));
        response.setWelcomePicUrl(contentServerService.parserUri(response.getWelcomePicUri()));
    }

    private String generateQrcodeUrl(String ownerToken) {
        String invitationLinkTemp = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_SELFREGISTER_LINK,"%s/vsregister/dist/i.html?t=%s");
        String homeUrl = configurationProvider.getValue("home.url","");
        return String.format(invitationLinkTemp,homeUrl,ownerToken);
    }

    private String generateConfigOwnerToken() {
        String ownerToken = null;
        VisitorSysConfiguration exist = null;
        do{
            ownerToken = VisitorSysUtils.generateLetterNumCode(16);
            exist = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwnerToken(ownerToken);
        }
        while (exist!=null);
        return ownerToken;
    }


    @Override
    public ListBlackListsResponse listBlackLists(ListBlackListsCommand cmd) {
        checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

        Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        Long pageAnchor = cmd.getPageAnchor() == null ? Integer.MAX_VALUE : cmd.getPageAnchor();

        List<VisitorSysBlackList> blackListList = visitorSysBlackListProvider
                .listVisitorSysBlackList(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId()
                        ,cmd.getKeyWords(),pageSize,pageAnchor);

        ListBlackListsResponse response = new ListBlackListsResponse();
        if(blackListList!=null && blackListList.size() == pageSize){
            response.setNextPageAnchor(blackListList.get(blackListList.size()-1).getId());
        }
        response.setBlackListList(blackListList.stream().map(r->ConvertHelper.convert(r,BaseBlackListDTO.class)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public void deleteBlackList(DeleteBlackListCommand cmd) {
        checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        visitorSysBlackListProvider.deleteBlackListById(cmd.getNamespaceId(),cmd.getId());
    }

    @Override
    public CreateOrUpdateBlackListResponse createOrUpdateBlackList(CreateOrUpdateBlackListCommand cmd) {
        checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        checkMustFillParams(cmd,"visitorPhone");
        if(cmd.getId()==null) {
            VisitorSysBlackList convert = ConvertHelper.convert(cmd, VisitorSysBlackList.class);
            VisitorSysBlackList blackList = visitorSysBlackListProvider.findVisitorSysBlackListByPhone(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getVisitorPhone());
            if(blackList!=null){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_REPEAT_PHONE,
                        "repeat phone");
            }
            visitorSysBlackListProvider.createVisitorSysBlackList(convert);
        }else {
            VisitorSysBlackList blackList = visitorSysBlackListProvider.findVisitorSysBlackListById(cmd.getId());
            if(blackList==null){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_NOT_EXIST,
                        "unknown blackList id "+cmd.getId());
            }
            blackList.setReason(cmd.getReason());
            blackList.setVisitorName(cmd.getVisitorName());
            blackList.setVisitorPhone(cmd.getVisitorPhone());
            visitorSysBlackListProvider.updateVisitorSysBlackList(blackList);
        }
        return ConvertHelper.convert(cmd,CreateOrUpdateBlackListResponse.class);
    }

    @Override
    public ListDoorGuardsResponse listDoorGuards(ListDoorGuardsCommand cmd) {
        VisitorsysOwnerType ownerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        ListDoorAccessGroupCommand doorcmd = ConvertHelper.convert(cmd,ListDoorAccessGroupCommand.class);
        doorcmd.setSearch(cmd.getKeyWords());
        if(ownerType==VisitorsysOwnerType.COMMUNITY) {
            doorcmd.setOwnerType((byte) 0);
        }else if(ownerType==VisitorsysOwnerType.ENTERPRISE){
            doorcmd.setOwnerType((byte) 1);
        }
        ListDoorAccessResponse listDoorAccessResponse = doorAccessService.listDoorAccessGroup(doorcmd);
        if(listDoorAccessResponse==null || listDoorAccessResponse.getDoors()==null){
            return null;
        }
        List<VisitorSysDoorAccess> defaultConfigs = visitorSysDoorAccessProvider.listVisitorSysDoorAccessByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
        for (VisitorSysDoorAccess d1 : defaultConfigs) {
            for (DoorAccessDTO d : listDoorAccessResponse.getDoors()) {
                if (d1.getDoorAccessId().equals(d.getId())) {
                    listDoorAccessResponse.getDoors().remove(d);
                    break;
                }
            }
        }
        ListDoorGuardsResponse response = new ListDoorGuardsResponse();
        response.setDoorGuardList(listDoorAccessResponse.getDoors().stream().map(r->{
            BaseDoorGuardDTO dto = new BaseDoorGuardDTO();
            dto.setDoorGuardId(String.valueOf(r.getId()));
            dto.setDoorGuardName(r.getName());
            dto.setHardwareId(r.getHardwareId());
            dto.setMaxDuration(r.getMaxDuration());
            dto.setMaxCount(r.getMaxCount());
            dto.setEnableAmount(r.getEnableAmount());
            dto.setEnableDuration(r.getEnableDuration());
            return dto;
        }).collect(Collectors.toList()));
        return response;
    }

    /**
     * 拒绝访客，不管他的预约状态
     * @param cmd
     */
    @Override
    public void rejectVisitor(CreateOrUpdateVisitorCommand cmd) {
        cmd.setVisitStatus(VisitorsysStatus.REJECTED_VISIT.getCode());
        cmd.setBookingStatus(VisitorsysStatus.REJECTED_VISIT.getCode());
        createOrUpdateVisitor(cmd);
//        checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
//        VisitorSysVisitor oldVisitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getId());
//        if(oldVisitor == null){
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "unknown visitor "+cmd.getId());
//        }
//        String oldFormatVisitorTime = null;
//        if(oldVisitor !=null && oldVisitor.getPlannedVisitTime()!=null) {
//            oldFormatVisitorTime = oldVisitor.getPlannedVisitTime().toLocalDateTime().format(YYYYMMDD);
//        }
//        VisitorSysVisitor visitor = checkUpdateVisitor(cmd,oldVisitor);
//        String newFormatVisitorTime = null;
//        if(visitor !=null && visitor.getPlannedVisitTime()!=null) {
//            newFormatVisitorTime = visitor.getPlannedVisitTime().toLocalDateTime().format(YYYYMMDD);
//        }
//        VisitorsysVisitorType type = checkVisitorType(visitor.getVisitorType());
//        //如果计划到访时间修改为不是当天，那么重新生成邀请码，生成唯一的邀请码,锁的对象是owner，因为邀请码会有区分owner的部分
//        if(type == VisitorsysVisitorType.BE_INVITED
//                && newFormatVisitorTime!=null
//                && !newFormatVisitorTime.equals(oldFormatVisitorTime)) {
//            coordinationProvider.getNamedLock(CoordinationLocks.VISITOR_SYS_GEN_IN_NO.getCode()
//                    +visitor.getOwnerType()+visitor.getOwnerId()).enter(()-> {
//                visitor.setInvitationNo(generateInvitationNo(visitor));
//                return null;
//            });
//        }
//        dbProvider.execute(r->{
//            generateRejectVisitor(visitor);
//            visitorSysVisitorProvider.updateVisitorSysVisitor(visitor);
//            visitorsysSearcher.syncVisitor(visitor);
//            createVisitorActions(visitor);
//            return null;
//        });
    }

    @Override
    public void syncVisitor(BaseVisitorsysCommand cmd) {
        visitorsysSearcher.syncVisitorsFromDb(cmd.getNamespaceId());
    }

    @Override
    public GetInvitationLetterForWebResponse getInvitationLetterForWeb(GetInvitationLetterForWebCommand cmd) {
        Long visitorId = checkInviationToken(cmd.getVisitorToken());
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(visitorId);
        if(visitor==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS,
                    "unknown visitorToken "+cmd.getVisitorToken());
        }

        GetConfigurationResponse configuration = getConfiguration(ConvertHelper.convert(visitor, GetConfigurationCommand.class));
        VisitorSysVisitor relatedVisitor = getRelatedVisitor(visitor);
        GetConfigurationResponse relatedConfiguration = getConfiguration(ConvertHelper.convert(relatedVisitor, GetConfigurationCommand.class));
        VisitorsysOwnerType ownerType = checkOwner(visitor.getOwnerType(), visitor.getOwnerId());
        GetInvitationLetterForWebResponse response = null;
        if(ownerType == VisitorsysOwnerType.COMMUNITY) {
            response = setInvitationLetter(configuration, relatedConfiguration, visitor, relatedVisitor);
        }else {
            response = setInvitationLetter(relatedConfiguration, configuration, relatedVisitor, visitor);
        }
        response.getBaseConfig().setAllowPassportQrcodeFlag(configuration.getBaseConfig().getAllowPassportQrcodeFlag());
        return response;
    }

    private GetInvitationLetterForWebResponse setInvitationLetter(GetConfigurationResponse communityConfig, GetConfigurationResponse enterpriseConfig,
                                                                  VisitorSysVisitor communityVisitor, VisitorSysVisitor enterpriseVisitor) {
        GetInvitationLetterForWebResponse response = new GetInvitationLetterForWebResponse();
        //根据设置，决定是否设置二维码
        VisitorsysStatus visitStatus = checkVisitStatus(communityVisitor.getVisitStatus());
        //读取公司访客二维码是否显示
        VisitorsysFlagType visitorQrcodeFlag = VisitorsysFlagType.fromCode(enterpriseConfig.getBaseConfig().getVisitorQrcodeFlag());
        //读取园区门禁配置
        VisitorsysFlagType doorGuardsFlag = VisitorsysFlagType.fromCode(communityConfig.getBaseConfig().getDoorGuardsFlag());
        VisitorsysFlagType validAfterConfirmedFlag = VisitorsysFlagType.fromCode(communityConfig.getBaseConfig().getDoorGuardsValidAfterConfirmedFlag());
        boolean showQrcode =
//                communityConfig.getBaseConfig().getDoorGuardId()!=null //配置的门禁和申请的门禁授权的门禁id必须一致
//                && communityConfig.getBaseConfig().getDoorGuardId().equals(communityVisitor.getDoorGuardId())
//                &&
        (
                        (
                                doorGuardsFlag == VisitorsysFlagType.YES //园区开启门禁对接
                                && validAfterConfirmedFlag == VisitorsysFlagType.YES//门禁二维码需要在访客确认后生效
                                && visitStatus==VisitorsysStatus.HAS_VISITED//访客已经是到访状态
                        )
                        ||
                        (
                                doorGuardsFlag == VisitorsysFlagType.YES //园区开启门禁对接
                                && validAfterConfirmedFlag == VisitorsysFlagType.NO //门禁二维码不需要需要在访客确认后生效
                        )
                )
                && visitStatus!=VisitorsysStatus.DELETED //访客状态不是已删除
                && visitStatus!=VisitorsysStatus.REJECTED_VISIT //访客状态不是已拒绝
                && visitStatus!=VisitorsysStatus.HIDDEN; //访客状态不是隐藏
//                && visitorQrcodeFlag == VisitorsysFlagType.YES; //公司访客设置的邀请函二维码显示
        showQrcode = showQrcode &&
                (communityVisitor.getDoorGuardEndTime()==null //门禁失效时间为空，则门禁二维码永久有效
                        || communityVisitor.getDoorGuardEndTime().getTime()>System.currentTimeMillis());//门禁失效时间未到，则二维码有效
        if(showQrcode){
            response.setQrcode(communityVisitor.getDoorGuardQrcode());
        }
        //目前只有公司访客邀请函，所以以下设置基于公司访客邀请函的设置
        response = VisitorSysUtils.copyAllNotNullProperties(enterpriseConfig, response);
        response.setLogoUrl(contentServerService.parserUri(response.getLogoUri()));
        VisitorSysOfficeLocation location = visitorSysOfficeLocationProvider.findVisitorSysOfficeLocationById(enterpriseVisitor.getOfficeLocationId());
        VisitorsysFlagType visitorInfoFlag = VisitorsysFlagType.fromCode(enterpriseConfig.getBaseConfig().getVisitorInfoFlag());
        if(visitorInfoFlag == VisitorsysFlagType.YES) {
            BaseVisitorInfoDTO convert = ConvertHelper.convert(enterpriseVisitor, BaseVisitorInfoDTO.class);
            convert.setEnterpriseFormValues(enterpriseVisitor.getFormJsonValue()==null?null:JSONObject.parseObject(enterpriseVisitor.getFormJsonValue(),
                    new TypeReference<List<VisitorsysApprovalFormItem>>() {}));
            convert.setCommunityFormValues(communityVisitor.getFormJsonValue()==null?null:JSONObject.parseObject(communityVisitor.getFormJsonValue(),
                    new TypeReference<List<VisitorsysApprovalFormItem>>() {}));
            response.setVisitorInfoDTO(convert);
        }else{
            BaseVisitorInfoDTO dto = new BaseVisitorInfoDTO();
            dto.setVisitorType(enterpriseVisitor.getVisitorType());
            response.setVisitorInfoDTO(dto);
        }
        VisitorsysFlagType visitorsysFlagType = VisitorsysFlagType.fromCode(enterpriseConfig.getBaseConfig().getTrafficGuidanceFlag());
        if(visitorsysFlagType == VisitorsysFlagType.YES) {
            response.setOfficeLocationDTO(ConvertHelper.convert(location, BaseOfficeLocationDTO.class));
        }
        response.setIpadThemeRgb(enterpriseConfig.getIpadThemeRgb());
        return response;
    }

    @Override
    public GetBookedVisitorByIdResponse createOrUpdateVisitorForWeb(CreateOrUpdateVisitorCommand cmd) {
        beforePostForWeb(cmd);
        cmd.setFromDevice(TrueOrFalseFlag.TRUE.getCode());
        return createOrUpdateVisitor(cmd);

    }

    @Override
    public ListBookedVisitorsResponse listBookedVisitorsForWeb(ListBookedVisitorsCommand cmd) {
        beforePostForWeb(cmd);
        cmd.setSearchFlag(VisitorsysSearchFlagType.CLIENT_BOOKING.getCode());
        cmd.setVisitorType(VisitorsysVisitorType.BE_INVITED.getCode());
        checkBookingStatus(cmd.getBookingStatus());
        return listBookedVisitorsWithOutACL(cmd);
    }

    @Override
    public GetBookedVisitorByIdResponse getBookedVisitorByIdForWeb(GetBookedVisitorByIdCommand cmd) {
        beforePostForWeb(cmd);
        return getBookedVisitorById(cmd);
    }

    @Override
    public ListVisitReasonsResponse listVisitReasonsForWeb(BaseVisitorsysCommand cmd) {
        beforePostForWeb(cmd);
        return listVisitReasons(cmd);
    }

    @Override
    public void deleteVisitorAppointForWeb(GetBookedVisitorByIdCommand cmd) {
        beforePostForWeb(cmd);
        deleteVisitorAppoint(cmd);

    }

    @Override
    public ListOfficeLocationsResponse listOfficeLocationsForWeb(ListOfficeLocationsCommand cmd) {
        beforePostForWeb(cmd);
        return listOfficeLocations(cmd);
    }

    @Override
    public ListCommunityOrganizationsResponse listCommunityOrganizationsForWeb(ListCommunityOrganizationsCommand cmd) {
        beforePostForWeb(cmd);
        return listCommunityOrganizations(cmd);
    }

    @Override
    public GetConfigurationResponse getConfigurationForWeb(GetConfigurationForWebCommand cmd) {
        beforePostForWeb(cmd);
        GetConfigurationResponse configuration = getConfiguration(ConvertHelper.convert(cmd, GetConfigurationCommand.class));
        setConfigurationEnterpriseName(configuration);//enterprise情况下，设置公司名称
        ListOfficeLocationsResponse response = listOfficeLocations(ConvertHelper.convert(cmd, ListOfficeLocationsCommand.class));
        List<BaseOfficeLocationDTO> officeLocationList = response.getOfficeLocationList();
        if(officeLocationList!=null && officeLocationList.size()>0){
            configuration.setOfficeLocationName(officeLocationList.get(0).getOfficeLocationName());
        }
        return configuration;
    }

    @Override
    public GetPairingCodeResponse getPairingCode(GetPairingCodeCommand cmd) {
        VisitorSysDevice device = ConvertHelper.convert(cmd, VisitorSysDevice.class);
        VisitorSysDevice exist = visitorSysDeviceProvider.findVisitorSysDeviceByDeviceId(device.getDeviceType(), device.getDeviceId());
        if(exist!=null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_REGISTED_IPAD,
                    "registed device");
        }
        String pairingCode = generateUnrepeatPairingCode();
        String key = generatePairingCodeKey(pairingCode);
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        int pairingCodelive = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_PAIRINGCODE_LIVE, 60);
        valueOperations.set(key,String.valueOf(device),pairingCodelive,TimeUnit.SECONDS);
        GetPairingCodeResponse response = new GetPairingCodeResponse();
        response.setPairingCode(pairingCode);
        response.setVaildTime(new Timestamp(System.currentTimeMillis()+pairingCodelive*1000));
        return response;
    }

    @Override
    public void confirmPairingCode(ConfirmPairingCodeCommand cmd) {
        VisitorSysDevice device = visitorSysDeviceProvider.findVisitorSysDeviceByDeviceId(cmd.getDeviceType(),cmd.getDeviceId());
        if(device!=null){
           return;
        }
        String key = generatePairingCodeKey(cmd.getPairingCode());
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String jsonValue = valueOperations.get(key);
        if(jsonValue == null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_PAIRING_CODE,
                    "unknown pairingCode "+cmd.getPairingCode());
        }
        VisitorSysDevice sysDevice = JSONObject.parseObject(jsonValue, VisitorSysDevice.class);
        checkDevice(sysDevice.getDeviceType(),sysDevice.getDeviceId());
        deleteValueOperations(key);
    }

    @Override
    public GetConfigurationResponse getUIConfiguration(BaseVisitorsysUICommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(),cmd.getDeviceId());
        GetConfigurationResponse configuration = getConfiguration(ConvertHelper.convert(device, GetConfigurationCommand.class));
        setConfigurationEnterpriseName(configuration);
        ListOfficeLocationsResponse response = listOfficeLocations(ConvertHelper.convert(device, ListOfficeLocationsCommand.class));
        List<BaseOfficeLocationDTO> officeLocationList = response.getOfficeLocationList();
        if(officeLocationList!=null && officeLocationList.size()>0){
            configuration.setOfficeLocationName(officeLocationList.get(0).getOfficeLocationName());
        }
        return configuration;
    }

    private void setConfigurationEnterpriseName(GetConfigurationResponse configuration) {
        VisitorsysOwnerType ownerType = checkOwnerType(configuration.getOwnerType());
        if(ownerType==VisitorsysOwnerType.ENTERPRISE){
            Organization organization = organizationProvider.findOrganizationById(configuration.getOwnerId());
            configuration.setEnterpriseName(organization==null?"":organization.getName());
        }
    }

    private VisitorSysDevice checkDevice(String deviceType, String deviceId) {
        if(deviceType == null || deviceId==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_DEVICE_NOT_FIND,
                    "unknow device "+deviceType+":"+deviceId);
        }
        VisitorSysDevice device = visitorSysDeviceProvider.findVisitorSysDeviceByDeviceId(deviceType,deviceId);
        if(device==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_DEVICE_NOT_FIND,
                    "unknow device "+deviceType+":"+deviceId);
        }
        return device;
    }

    @Override
    public CreateOrUpdateVisitorUIResponse createOrUpdateUIVisitor(CreateOrUpdateVisitorUICommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        CreateOrUpdateVisitorCommand createOrUpdateVisitorCommand = ConvertHelper.convert(cmd, CreateOrUpdateVisitorCommand.class);
        createOrUpdateVisitorCommand = VisitorSysUtils.copyNotNullProperties(device, createOrUpdateVisitorCommand,new ArrayList(Arrays.asList("getId","setId")));
        createOrUpdateVisitorCommand.setFromDevice(TrueOrFalseFlag.TRUE.getCode());
        GetBookedVisitorByIdResponse orUpdateVisitor = createOrUpdateVisitor(createOrUpdateVisitorCommand);
        return ConvertHelper.convert(orUpdateVisitor,CreateOrUpdateVisitorUIResponse.class);
    }

    @Override
    public ListUIOfficeLocationsResponse listUIOfficeLocations(ListUIOfficeLocationsCommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        ListOfficeLocationsCommand locationsCommand = new ListOfficeLocationsCommand();
        locationsCommand.setNamespaceId(device.getNamespaceId());
        locationsCommand.setOwnerType(VisitorsysOwnerType.ENTERPRISE.getCode());
        locationsCommand.setOwnerId(cmd.getEnterpriseId());
        locationsCommand.setKeyWords(cmd.getKeyWords());
        ListOfficeLocationsResponse response = listOfficeLocations(locationsCommand);
        return ConvertHelper.convert(response,ListUIOfficeLocationsResponse.class);
    }

    @Override
    public ListUICommunityOrganizationsResponse listUICommunityOrganizations(ListUICommunityOrganizationsCommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        ListCommunityOrganizationsCommand convert = ConvertHelper.convert(cmd, ListCommunityOrganizationsCommand.class);
        convert = VisitorSysUtils.copyAllNotNullProperties(device, convert);
        ListCommunityOrganizationsResponse response = listCommunityOrganizations(convert);
        return ConvertHelper.convert(response,ListUICommunityOrganizationsResponse.class);
    }

    @Override
    public ListUIVisitReasonsResponse listUIVisitReasons(BaseVisitorsysUICommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        ListVisitReasonsResponse response = listVisitReasons(ConvertHelper.convert(device, BaseVisitorsysCommand.class));
        return ConvertHelper.convert(response,ListUIVisitReasonsResponse.class);
    }

    @Override
    public void sendSMSVerificationCode(SendSMSVerificationCodeCommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        sendSMSVerificationCode(device.getNamespaceId(),cmd.getVisitorPhone());
    }


    /**
     * 发送验证码短信
     * @param namespaceId
     * @param visitorPhone
     */
    private void sendSMSVerificationCode(Integer namespaceId, String visitorPhone) {
        List<Tuple<String, Object>> variables =  new ArrayList();
        smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_MODLUENAME, configurationProvider.getValue(VisitorsysConstant.VISITORSYS_MODLUENAME, ""));
        smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_VERIFICATIONCODE, generateVerificationCode(visitorPhone));
        String templateLocale = UserContext.current().getUser().getLocale();
        smsProvider.sendSms(namespaceId, visitorPhone, SmsTemplateCode.SCOPE, SmsTemplateCode.VISITORSYS_VERIFICATION_CODER, templateLocale, variables);
    }
    /**
     * 生成验证码,并存储在redis
     * @param visitorPhone
     * @return
     */
    private String generateVerificationCode(String visitorPhone) {
        String value = null;
        String verificationCode = null;
        int pairingCodeLength = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_VERIFICATIONCODE_LENGTH, 6);
        do{
            verificationCode = VisitorSysUtils.generateNumCode(pairingCodeLength);
            String key = generateVerificationKey(verificationCode,visitorPhone);
            ValueOperations<String, String> valueOperations = getValueOperations(key);
            value = valueOperations.get(key);
        }while (value!=null);

        String key = generateVerificationKey(verificationCode,visitorPhone);
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        int live = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_VERIFICATIONCODE_LIVE, 900);
        valueOperations.set(key,verificationCode, live,TimeUnit.SECONDS);
        return verificationCode;
    }

    private String generateVerificationKey(String verificationCode,String visitorPhone) {
       return VisitorsysConstant.VISITORSYS_VERIFICATIONCODE_+verificationCode+"_"+visitorPhone;
    }

    @Override
    public ListBookedVisitorsResponse confirmVerificationCode(ConfirmVerificationCodeCommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        if(cmd.getVerificationCode()==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS,
                    "unknown verificatcode "+cmd.getVerificationCode());
        }
        return confirmVerificationCode(device.getNamespaceId(),device.getOwnerType(),device.getOwnerId(),cmd.getVerificationCode(),cmd.getVisitorPhone());

    }


    /**
     * 确认验证码,并返回数据
     * @param namespaceId
     * @param ownerType
     * @param ownerId
     * @param vCode
     * @param vPhone
     * @return
     */
    private ListBookedVisitorsResponse confirmVerificationCode(Integer namespaceId,String ownerType,Long ownerId,String vCode,String vPhone) {
        if(vCode==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_NOT_EXIST,
                    "unknown verificatcode "+vCode);
        }
        String key = generateVerificationKey(vCode,vPhone);
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String value = valueOperations.get(key);
        if(value==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_ILLEGAL_VERIFICATIONCODE,
                    "unknown verificatcode "+vCode);
        }
        long now = System.currentTimeMillis();
        ListBookedVisitorParams params = new ListBookedVisitorParams();
        params.setNamespaceId(namespaceId);
        params.setOwnerType(ownerType);
        params.setOwnerId(ownerId);
        params.setVisitorPhone(vPhone);
        params.setStartPlannedVisitTime(VisitorSysUtils.getStartOfDay(now).getTime());
        params.setEndPlannedVisitTime(VisitorSysUtils.getEndOfDay(now).getTime());
        params.setBookingStatus(VisitorsysStatus.NOT_VISIT.getCode());
        params.setVisitorType(VisitorsysVisitorType.BE_INVITED.getCode());
        params.setEndPlannedVisitTime(VisitorSysUtils.getEndOfDay(now).getTime());
        params.setSearchFlag(VisitorsysSearchFlagType.BOOKING_MANAGEMENT.getCode());
        params.setPageSize(100);
        params.setPageAnchor(0L);
        return visitorsysSearcher.searchVisitors(params);
    }

    @Override
    public GetHomePageConfigurationResponse getHomePageConfiguration() {
        String ipadHomePageConfig = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_IPAD_CONFIG,"uri,欢迎使用左邻访客,开始配置,0");
        String[] split = ipadHomePageConfig.split(",");
        if(split!=null && split.length==4) {
            String welcomeDocument = split[1];
            String buttonName = split[2];
            String version = split[3];
            String zlLogoUrl = contentServerService.parserUri(split[0]);
            return new GetHomePageConfigurationResponse(split[0],zlLogoUrl,welcomeDocument,buttonName,Long.valueOf(version));
        }
        return new GetHomePageConfigurationResponse();
    }

    @Override
    public GetFormResponse getForm(GetFormCommand cmd) {
        return getForm(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getEnterpriseId());
    }

    private GetFormResponse getForm(Integer namespaceId,String ownerType,Long ownerId,Long enterpriseId) {
        GetConfigurationCommand command = new GetConfigurationCommand();
        command.setNamespaceId(namespaceId);
//        VisitorsysOwnerType visitorsysOwnerType = checkOwner(ownerType, ownerId);
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(ownerType);
        if(visitorsysOwnerType ==VisitorsysOwnerType.ENTERPRISE){
            command.setOwnerType(VisitorsysOwnerType.ENTERPRISE.getCode());
            if(enterpriseId != null)
                command.setOwnerId(enterpriseId);
            else
                command.setOwnerId(ownerId);
        }else{
//            Long communityId = organizationService.getOrganizationActiveCommunityId(ownerId);
            Long communityId = ownerId;
            command.setOwnerType(VisitorsysOwnerType.COMMUNITY.getCode());
            command.setOwnerId(communityId);
        }
        return new GetFormResponse(getConfiguration(command).getFormConfig());
    }
    @Override
    public GetFormResponse getUIForm(GetUIFormCommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        return getForm(device.getNamespaceId(),device.getOwnerType(),device.getOwnerId(),cmd.getEnterpriseId());
    }

    /**
     * 转换ownerToke为namespace和Owner，并设置到cmd参数中
     * @param cmd
     */
    private void beforePostForWeb(BaseVisitorsysCommand cmd){
        if(cmd.getOwnerToken()==null){
            return;
        }
        VisitorSysConfiguration configuration = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwnerToken(cmd.getOwnerToken());
        if(configuration!=null){
            cmd.setNamespaceId(configuration.getNamespaceId());
            cmd.setOwnerType(configuration.getOwnerType());
            cmd.setOwnerId(configuration.getOwnerId());
        }else{
            LOGGER.error("beforePostForWeb is fail. {}",cmd.getOwnerToken());
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS,
                    "unknown ownerToken "+cmd.getOwnerToken());
        }
        if(cmd.getNamespaceId()==null){
            LOGGER.error("namespaceId is null. {}",cmd);
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS,
                    "unknown namespaceId is null");
        }
    }

    @Override
    public GetFormForWebResponse getFormForWeb(GetFormForWebCommand cmd) {
        beforePostForWeb(cmd);
        GetConfigurationCommand command = new GetConfigurationCommand();
        command.setNamespaceId(cmd.getNamespaceId());
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        if(visitorsysOwnerType ==VisitorsysOwnerType.ENTERPRISE) {
            command.setOwnerType(VisitorsysOwnerType.ENTERPRISE.getCode());
            if(cmd.getEnterpriseId() != null)
                command.setOwnerId(cmd.getEnterpriseId());
            else
                command.setOwnerId(cmd.getOwnerId());
        } else{
//            Long communityId = organizationService.getOrganizationActiveCommunityId(cmd.getOwnerId());
            Long communityId = cmd.getOwnerId();
            command.setOwnerType(VisitorsysOwnerType.COMMUNITY.getCode());
            command.setOwnerId(communityId);
         }
        return new GetFormForWebResponse(getConfiguration(command).getFormConfig());
    }

    @Override
    public GetBookedVisitorByIdResponse getUIBookedVisitorById(GetUIBookedVisitorByIdCommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        GetBookedVisitorByIdCommand convert = ConvertHelper.convert(cmd, GetBookedVisitorByIdCommand.class);
        convert = VisitorSysUtils.copyAllNotNullProperties(device, convert);
        GetBookedVisitorByIdResponse response = getBookedVisitorById(convert);
        return response;
    }

    @Override
    public void exportBookedVisitors(ListBookedVisitorsCommand cmd,HttpServletResponse resp) {
        cmd.setPageSize(Integer.MAX_VALUE);
        ListBookedVisitorsResponse response = listBookedVisitors(cmd);
        if(response==null || response.getVisitorDtoList()==null)
            return;

        List<BaseVisitorDTO> visitorDtoList = response.getVisitorDtoList();

        Workbook wb = new XSSFWorkbook();

        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);
        CellStyle style = wb.createCellStyle();
        style.setFont(font);

        VisitorsysSearchFlagType searchFlagType = VisitorsysSearchFlagType.fromCode(cmd.getSearchFlag());
        Sheet sheet = null;
        if(searchFlagType == VisitorsysSearchFlagType.BOOKING_MANAGEMENT){
            sheet = wb.createSheet("预约管理导出");
        }else {
            sheet = wb.createSheet("访客管理导出");
        }
        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeightInPoints(20);
        Row row = sheet.createRow(0);
        if(searchFlagType == VisitorsysSearchFlagType.BOOKING_MANAGEMENT) {
            row.createCell(0).setCellValue("访客姓名");
            row.createCell(1).setCellValue("手机号码");
            row.createCell(2).setCellValue("来访事由");
            row.createCell(3).setCellValue("邀请人");
            row.createCell(4).setCellValue("到访状态");
            row.createCell(5).setCellValue("计划到访时间");
        }else{
            row.createCell(0).setCellValue("访客姓名");
            row.createCell(1).setCellValue("手机号码");
            row.createCell(2).setCellValue("访客类型");
            row.createCell(3).setCellValue("来访事由");
            row.createCell(4).setCellValue("到访企业时间");
            row.createCell(5).setCellValue("状态");
        }

        DateTimeFormatter datetimeSF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        for(int i = 0, size = visitorDtoList.size(); i < size; i++){
            Row tempRow = sheet.createRow(i + 1);
            BaseVisitorDTO visitorDTO = visitorDtoList.get(i);
            tempRow.createCell(0).setCellValue(visitorDTO.getVisitorName());
            tempRow.createCell(1).setCellValue(visitorDTO.getVisitorPhone());
            if(searchFlagType == VisitorsysSearchFlagType.BOOKING_MANAGEMENT) {
                tempRow.createCell(2).setCellValue(visitorDTO.getVisitReason());
                tempRow.createCell(3).setCellValue(visitorDTO.getInviterName());
                VisitorsysStatus status = VisitorsysStatus.fromBookingCode(visitorDTO.getBookingStatus());
                tempRow.createCell(4).setCellValue(status==null?"未知":status.getDesc());
                tempRow.createCell(5).setCellValue(visitorDTO.getPlannedVisitTime().toLocalDateTime().format(datetimeSF));
            }else{
                VisitorsysVisitorType type = VisitorsysVisitorType.fromCode((visitorDTO.getVisitorType()));
                tempRow.createCell(2).setCellValue(type.getDesc());
                tempRow.createCell(3).setCellValue(visitorDTO.getVisitReason());
                tempRow.createCell(4).setCellValue(visitorDTO.getVisitTime().toLocalDateTime().format(datetimeSF));
                VisitorsysStatus status = VisitorsysStatus.fromCode(visitorDTO.getVisitStatus());
                tempRow.createCell(5).setCellValue(status==null?"未知":status.getDesc());
            }

        }

        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            wb.write(out);
            DownloadUtils.download(out, resp);
        } catch (IOException e) {
            LOGGER.error("exportBookedVisitors is fail. {}",e);
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_FILE_EXPORT_FAIL,
                    "exportBookedVisitors is fail.");
        }

    }

    @Override
    public ListBookedVisitorsResponse confirmVerificationCodeForWeb(ConfirmVerificationCodeForWebCommand cmd) {
        beforePostForWeb(cmd);
        return confirmVerificationCode(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getVerificationCode(),cmd.getVisitorPhone());
    }

    @Override
    public void sendSMSVerificationCodeForWeb(SendSMSVerificationCodeForWebCommand cmd) {
        beforePostForWeb(cmd);
        sendSMSVerificationCode(cmd.getNamespaceId(),cmd.getVisitorPhone());
    }

    @Override
    public GetUploadFileTokenResponse getUploadFileToken(GetUIUploadFileTokenCommand cmd) {
        checkDevice(cmd.getDeviceType(),cmd.getDeviceId());
        return getUploadFileToken();
    }

    private GetUploadFileTokenResponse getUploadFileToken() {
        LoginToken token = new LoginToken(Long.valueOf(VisitorSysUtils.generateNumCode(6)),
                Integer.valueOf(VisitorSysUtils.generateNumCode(6)),
                Integer.valueOf(VisitorSysUtils.generateNumCode(6)),
                Long.valueOf(VisitorSysUtils.generateNumCode(6)));
        String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

        return new GetUploadFileTokenResponse(contentServerService.getContentServer(),tokenString);
    }

    @Override
    public GetUploadFileTokenResponse getUploadFileTokenForWeb(GetUploadFileTokenCommand cmd) {
        beforePostForWeb(cmd);
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        return getUploadFileToken();
    }

    @Override
    public void checkBlackList(CheckBlackListCommand cmd) {
        VisitorSysDevice sysDevice = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        checkBlackList(sysDevice.getNamespaceId(),sysDevice.getOwnerType(),sysDevice.getOwnerId(),cmd.getVisitorPhone(),cmd.getEnterpriseId());
    }

    @Override
    public void checkBlackListForWeb(CheckBlackListForWebCommand cmd) {
        beforePostForWeb(cmd);
        checkBlackList(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getVisitorPhone(),cmd.getEnterpriseId());
    }

    public void checkMoblieManagePrivilege(BaseVisitorsysCommand cmd){
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(cmd.getOwnerType());
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY){
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_MODILE_MAMAGEMENT, cmd.getAppId(), null, cmd.getOwnerId());
        }
//        else if(visitorsysOwnerType == VisitorsysOwnerType.ENTERPRISE){
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getPmId(), PrivilegeConstants.VISITORSYS_MODILE_MAMAGEMENT_ENT, cmd.getAppId(),cmd.getOwnerId(), null);
//        }
    }
    @Override
    public ListBookedVisitorsResponse listBookedVisitorsForManage(ListBookedVisitorsCommand cmd) {
        cmd.setSearchFlag(VisitorsysSearchFlagType.VISITOR_MANAGEMENT.getCode());
        checkMoblieManagePrivilege(cmd);
        return listBookedVisitorsWithOutACL(cmd);
    }

    @Override
    public GetBookedVisitorByIdResponse getBookedVisitorByIdForManage(GetBookedVisitorByIdCommand cmd) {
        checkMoblieManagePrivilege(cmd);
        return getBookedVisitorById(cmd);
    }

    @Override
    public ListVisitReasonsResponse listVisitReasonsForManage(BaseVisitorsysCommand cmd) {
        checkMoblieManagePrivilege(cmd);
        return listVisitReasons(cmd);
    }

    @Override
    public GetBookedVisitorByIdResponse createOrUpdateVisitorForManage(CreateOrUpdateVisitorCommand cmd) {
        checkMoblieManagePrivilege(cmd);
        return createOrUpdateVisitor(cmd);
    }

    @Override
    public void confirmVisitorForManage(CreateOrUpdateVisitorCommand cmd) {
        checkMoblieManagePrivilege(cmd);
        confirmVisitor(cmd);
    }

    @Override
    public void rejectVisitorForManage(CreateOrUpdateVisitorCommand cmd) {
        checkMoblieManagePrivilege(cmd);
        rejectVisitor(cmd);
    }

    @Override
    public void updateMessageReceiverForManage(UpdateMessageReceiverCommand cmd) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        //这里考虑加锁，其实没有什么并发量也没必要
        VisitorsysFlagType statusFlag = VisitorsysFlagType.fromCode(cmd.getStatusFlag());
        if(statusFlag == VisitorsysFlagType.NO){
            messageReceiverProvider.deleteMessageReceiverByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),UserContext.current().getUser().getId());
        }else {
            VisitorSysMessageReceiver receiver = messageReceiverProvider.findMessageReceiverByOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), UserContext.current().getUser().getId());
            if (receiver == null) {
                receiver=ConvertHelper.convert(cmd,VisitorSysMessageReceiver.class);
                messageReceiverProvider.createVisitorSysMessageReceiver(receiver);
            }
        }
    }

    @Override
    public GetMessageReceiverForManageResponse getMessageReceiverForManage(BaseVisitorsysCommand cmd) {
        VisitorSysMessageReceiver receiver = messageReceiverProvider.findMessageReceiverByOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), UserContext.current().getUser().getId());
        if (receiver == null) {
            return new GetMessageReceiverForManageResponse(VisitorsysFlagType.NO.getCode());
        }
        return new GetMessageReceiverForManageResponse(VisitorsysFlagType.YES.getCode());
    }

    @Override
    public OpenApiListOrganizationsResponse openApiListOrganizations(OpenApiListOrganizationsCommand cmd) {
        SearchOrganizationCommand orgCmd = new SearchOrganizationCommand();
        orgCmd.setKeyword(cmd.getKeyWords());
        orgCmd.setCommunityId(cmd.getOwnerId());
        orgCmd.setSimplifyFlag((byte)1);
        orgCmd.setPageSize(configurationProvider.getIntValue("visitorsys.organizationlist.pagesize",20));
        OrganizationQueryResult organizationQueryResult = organizationSearcher.fuzzyQueryOrganizationByName(orgCmd);
        if(organizationQueryResult==null || organizationQueryResult.getDtos()==null){
            return null;
        }
        OpenApiListOrganizationsResponse response = new OpenApiListOrganizationsResponse();
        response.setNextPageAnchor(organizationQueryResult.getPageAnchor());

        List<OrganizationAddress> orgAddress= organizationProvider.findOrganizationAddressByOrganizationIds(organizationQueryResult.getDtos().stream().map(r -> r.getId()).collect(Collectors.toList()));
        List<Address> address = addressProvider.listAddressOnlyByIds(orgAddress.stream().map(r -> r.getAddressId()).collect(Collectors.toList()));
        //addressid -> 地址详情
        Map<Long,Address> mapAddress = address.stream().collect(Collectors.toMap(Address::getId,r->r));

        //公司id->地址列表
        Map<Long,List<OrganizationAddress>> orgAddressMap = new HashMap<>();
        for (OrganizationAddress organizationAddress : orgAddress) {
            List<OrganizationAddress> addresses = orgAddressMap.get(organizationAddress.getOrganizationId());
            if(addresses == null){
                addresses = new ArrayList<>();
                orgAddressMap.put(organizationAddress.getOrganizationId(),addresses);
            }
            addresses.add(organizationAddress);

        }
        response.setVisitorEnterpriseList(organizationQueryResult.getDtos().stream().map(r->{
            BaseVisitorEnterpriseDTO dto = new BaseVisitorEnterpriseDTO();
            dto.setEnterpriseId(r.getId());
            dto.setEnterpriseName(r.getName());
            List<OrganizationAddress> addresses = orgAddressMap.get(r.getId());
            if(addresses!=null && addresses.size()>0) {
                //设置楼栋门牌
                dto.setBuildings(addresses.stream().map(addr->{
                    VisitorSysBuilding building = new VisitorSysBuilding();
                    Address address1 = mapAddress.get(addr.getAddressId());
                    if(address1!=null){
                        building.setDoorplate(address1.getApartmentName());
                    }
                    building.setBuilding(addr.getBuildingName());
                    return building;
                }).collect(Collectors.toList()));
            }
            return dto;
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public OpenApiCreateVisitorResponse openApiCreateVisitor(OpenApiCreateVisitorCommand cmd) {
        CreateOrUpdateVisitorCommand createCmd = ConvertHelper.convert(cmd,CreateOrUpdateVisitorCommand.class);
        if(cmd.getVisitorToken()!=null) {
            Long visitorId = null;
            try {
                visitorId = WebTokenGenerator.getInstance().fromWebToken(cmd.getVisitorToken(), Long.class);
            } catch (Exception e) {
                LOGGER.error("visitorToken transform error , token = {}", cmd.getVisitorToken());
            }
            createCmd.setId(visitorId);
        }
        //设置namespaceid
        AppNamespaceMapping appNamespaceMapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(cmd.getAppKey());
        createCmd.setNamespaceId(appNamespaceMapping.getNamespaceId());

        ListOfficeLocationsCommand officeLocationsCommand = new ListOfficeLocationsCommand();
        officeLocationsCommand.setOwnerType(VisitorsysOwnerType.ENTERPRISE.getCode());
        officeLocationsCommand.setOwnerId(cmd.getEnterpriseId());
        officeLocationsCommand.setNamespaceId(appNamespaceMapping.getNamespaceId());
        ListOfficeLocationsResponse listOfficeLocationsResponse = this.listOfficeLocations(officeLocationsCommand);

        //设置办公地点
        if(listOfficeLocationsResponse!=null
                && listOfficeLocationsResponse.getOfficeLocationList()!=null
                && listOfficeLocationsResponse.getOfficeLocationList().size()>0){
            createCmd.setOfficeLocationId(listOfficeLocationsResponse.getOfficeLocationList().get(0).getId());
            createCmd.setOfficeLocationName(listOfficeLocationsResponse.getOfficeLocationList().get(0).getOfficeLocationName());
        }
        createCmd.setVisitStatus(VisitorsysStatus.WAIT_CONFIRM_VISIT.getCode());
        createCmd.setSource(VisitorsysSourceType.OUTER.getCode());

        GetBookedVisitorByIdResponse orUpdateVisitor = this.createOrUpdateVisitor(createCmd);
        OpenApiCreateVisitorResponse convert = ConvertHelper.convert(orUpdateVisitor, OpenApiCreateVisitorResponse.class);
        String token = WebTokenGenerator.getInstance().toWebToken(orUpdateVisitor.getId());
        convert.setVisitorToken(token);
        convert.setBuildings(cmd.getBuildings());//第三方要求的楼栋门牌，我们这边貌似存了也没什么用，直接返回给第三方
        return convert;
    }




    /**
     * 检查owerid是否在系统中存在
     * @param ownerType
     * @param ownerId
     */
    private VisitorsysOwnerType checkOwner(String ownerType, Long ownerId) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(ownerType);
        switch (visitorsysOwnerType){
            case COMMUNITY:
                Community community = communityProvider.findCommunityById(ownerId);
                if(community==null){
                    throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_NOT_EXIST,
                            "unknown ownerId "+ownerId);
                }
                break;
            case ENTERPRISE:
                Organization organization = organizationProvider.findOrganizationById(ownerId);
                if(organization==null){
                    throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_NOT_EXIST,
                            "unknown ownerId "+ownerId);
                }
                break;
        }
        return visitorsysOwnerType;

    }

    /**
     * 检查所属枚举
     * @param ownerType
     * @return
     */
    private VisitorsysOwnerType checkOwnerType(String ownerType) {
        VisitorsysOwnerType visitorsysOwnerType = VisitorsysOwnerType.fromCode(ownerType);
        if(visitorsysOwnerType==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS,
                    "unknown ownerType "+ownerType);
        }
        return visitorsysOwnerType;
    }

    /**
     * 检查更新、新建预约参数
     * @param cmd
     * @return
     */
    private VisitorSysVisitor checkCreateVisitor(CreateOrUpdateVisitorCommand cmd) {
        VisitorSysVisitor convert = ConvertHelper.convert(cmd, VisitorSysVisitor.class);
        convert.setParentId(0L);
        checkVisitor(convert);
        return convert;

    }

    /**
     * 设置表单五个字段的值
     * @param convert
     * @param map
     */
    private void setVisitorFormValues(VisitorSysVisitor convert, Map<String, VisitorsysApprovalFormItem> map) {
        if(map==null){
            return;
        }
        VisitorsysApprovalFormItem invalidTime = map.get("invalidTime");
        if(invalidTime!=null) {
            convert.setInvalidTime(invalidTime.getFieldValue());
        }
        VisitorsysApprovalFormItem plateNo = map.get("plateNo");
        if(plateNo!=null) {
            convert.setPlateNo(plateNo.getFieldValue());
        }
        VisitorsysApprovalFormItem idNumber = map.get("idNumber");
        if(idNumber!=null) {
            convert.setIdNumber(idNumber.getFieldValue());
        }
        VisitorsysApprovalFormItem visitFloor = map.get("visitFloor");
        if(visitFloor!=null) {
            convert.setVisitFloor(visitFloor.getFieldValue());
        }
        VisitorsysApprovalFormItem visitAddresses = map.get("visitAddresses");
        if(visitAddresses!=null) {
            convert.setVisitAddresses(visitAddresses.getFieldValue());
        }
    }

    /**
     * 必填参数检查是否非空
     * @param checkObject
     * @param paramName
     */
    private void checkMustFillParams(Object checkObject, String paramName) {
        String methodName = "get"+ paramName.substring(0,1).toUpperCase()+paramName.substring(1,paramName.length());
        Method method = null;
        try {
            method = checkObject.getClass().getMethod(methodName);
            Object result = method.invoke(checkObject);
            if(result==null){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE
                        , VisitorsysConstant.ERROR_MUST_FILL, paramName+" should not be null");
            }
        } catch (NoSuchMethodException e) {
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE
                    , VisitorsysConstant.ERROR_MUST_FILL, "",e);
        } catch (IllegalAccessException e) {
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE
                    , VisitorsysConstant.ERROR_MUST_FILL, "",e);
        } catch (InvocationTargetException e) {
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE
                    , VisitorsysConstant.ERROR_MUST_FILL, "",e);
        }
    }

    /**
     * 检查非法访客状态
     * @param visitStatus
     */
    private VisitorsysStatus checkInvaildVisitStatus(Byte visitStatus) {
        VisitorsysStatus status = checkVisitStatus(visitStatus);
        //以下两种状态，不能在创建用户的时候传入
        if(status == VisitorsysStatus.DELETED){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "invaild visitor visitStatus = "+visitStatus);
        }
        return status;
    }

    /**
     * 检查预约状态枚举
     * @param bookingStatus
     * @return
     */
    private VisitorsysStatus checkBookingStatus(Byte bookingStatus) {
        if(bookingStatus==null){
            return null;
        }
        VisitorsysStatus status = VisitorsysStatus.fromBookingCode(bookingStatus);
        if(status==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "unknow visitor bookingStatus = "+bookingStatus);
        }
        return status;
    }

    /**
     * 检查访客状态枚举
     * @param visitStatus
     * @return
     */
    private VisitorsysStatus checkVisitStatus(Byte visitStatus) {
        if(visitStatus ==null){
            return null;
        }
        VisitorsysStatus status = VisitorsysStatus.fromCode(visitStatus);
        if(status==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "unknow visitor visitStatus = "+ visitStatus);
        }
        return status;
    }


    /**
     * 检查访客类型枚举
     * @param bytetype
     */
    private VisitorsysVisitorType checkVisitorType(Byte bytetype) {
        VisitorsysVisitorType visitorType = VisitorsysVisitorType.fromCode(bytetype);
        if(visitorType==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "unknow visitor type = "+visitorType);
        }
        return visitorType;
    }

    /**
     * 检查表单必填项
     * @param owner
     * @param formValues
     * @return
     */
    private void checkFormConfiguration(GetConfigurationResponse owner, List<VisitorsysApprovalFormItem> formValues) {
        if(owner==null || owner.getFormConfig()==null){
            return ;
        }
        if(formValues==null){
            return ;
        }
        Map<String, VisitorsysApprovalFormItem> map = formValues.stream().collect(Collectors.toMap(VisitorsysApprovalFormItem::getFieldName, x -> x));
        for (VisitorsysApprovalFormItem dto : owner.getFormConfig()) {
            if(dto.getRequiredFlag() ==null) {
                continue;
            }
            if(dto.getRequiredFlag() == 1 && map.get(dto.getFieldName())==null){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE
                        , VisitorsysConstant.ERROR_MUST_FILL, dto.getFieldDisplayName()+" should not be null");
            }

        }
        return ;
    }

    /**
     * 生成更新访客的实体
     * @param cmd
     * @return
     */
    private VisitorSysVisitor checkUpdateVisitor(CreateOrUpdateVisitorCommand cmd,VisitorSysVisitor oldVisitor) {
        if(oldVisitor==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "unknow visitor id = "+cmd.getId()+", namespaceId = "+cmd.getNamespaceId()+"");
        }
        if(oldVisitor.getOwnerId().longValue()!=cmd.getOwnerId()
                || !oldVisitor.getOwnerType().equals(cmd.getOwnerType())
                || oldVisitor.getVisitorType().byteValue()!=cmd.getVisitorType().byteValue()){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "unknow visitor "+cmd);
        }
        oldVisitor = VisitorSysUtils.copyNotNullProperties(cmd, oldVisitor);
        oldVisitor.setVisitorPicUri(cmd.getVisitorPicUri());
        checkVisitor(oldVisitor);
        return oldVisitor;
    }

    /**
     * 检查一些访客非法的字段，矛盾的字段，或设置为正确的值，
     * 或抛出异常
     * @param visitor
     */
    private void checkVisitor(VisitorSysVisitor visitor) {
        for (String s : checkMustFillField) {
            checkMustFillParams(visitor,s);
        }
        CommunityType communitType = CommunityType.fromCode(visitor.getCommunityType());
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(visitor.getOwnerType(),visitor.getOwnerId());
        if(CommunityType.COMMERCIAL.equals(communitType))
            checkOwner(VisitorsysOwnerType.ENTERPRISE.getCode(),visitor.getEnterpriseId());
        GetConfigurationResponse configuration = getConfiguration(ConvertHelper.convert(visitor, GetConfigurationCommand.class));
        //检查园区表单
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            checkFormConfiguration(configuration, visitor.getCommunityFormValues());
        }
        //检查公司表单
        if(CommunityType.COMMERCIAL.equals(communitType))
            checkFormConfiguration(visitor.getNamespaceId(),VisitorsysOwnerType.ENTERPRISE.getCode(),visitor.getEnterpriseId(),visitor.getEnterpriseFormValues());

        VisitorsysVisitorType visitorType = checkVisitorType(visitor.getVisitorType());
        VisitorsysStatus visitStatus = checkInvaildVisitStatus(visitor.getVisitStatus());

        //设置创建预约访客属性
        if(visitorType==VisitorsysVisitorType.BE_INVITED){
            if(visitor.getId()==null) {//创建预约访客
                checkMustFillParams(visitor, "plannedVisitTime");//必填计划到访时间
                if(visitor.getPlannedVisitTime().getTime()<System.currentTimeMillis()){//计划到访时间必须晚于当前时间
                    throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE
                            , VisitorsysConstant.ERROR_PLANNED_VISITTIME, "invalid passed plannedVisitTime "+visitor.getPlannedVisitTime());
                }
                visitor.setVisitStatus(VisitorsysStatus.NOT_VISIT.getCode());//创建预约必须是未到访状态
                visitStatus = VisitorsysStatus.NOT_VISIT;
            }
            if(visitStatus == VisitorsysStatus.NOT_VISIT) {//预约访客未到访,设置邀请人
                User user = UserContext.current().getUser();
                visitor.setInviterId((user==null?-1:user.getId()));
                visitor.setInviterName(user==null?"anonymous":user.getNickName());
                visitor.setBookingStatus(VisitorsysStatus.NOT_VISIT.getCode());
            }
            if(visitStatus == VisitorsysStatus.HAS_VISITED){//
                visitor.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
            }
        }else{
            if(visitStatus == VisitorsysStatus.NOT_VISIT){//临时访客是未到访状态
                visitStatus = VisitorsysStatus.WAIT_CONFIRM_VISIT;//设置状态为等待确认
                visitor.setVisitStatus(VisitorsysStatus.WAIT_CONFIRM_VISIT.getCode());
            }
            //清理临时访客不需要的属性
            visitor.setBookingStatus(null);
            visitor.setInviterId(null);
            visitor.setInviterName(null);
        }
        //访客状态是等待确认，那么必须设置到访时间
        if(visitStatus== VisitorsysStatus.WAIT_CONFIRM_VISIT){
            visitor.setVisitTime(new Timestamp(System.currentTimeMillis()));
        }else if(visitStatus == VisitorsysStatus.HAS_VISITED){//如果已到访状态
            generateConfirmVisitor(visitor,visitorType);
        }

//      自助登记状态置为已到访
        if(null != visitor.getFromDevice() && TrueOrFalseFlag.TRUE.getCode().equals(visitor.getFromDevice())){
//          企业预约访客
            if(visitor.getId()==null && !visitor.getVisitorType().equals(VisitorsysVisitorType.BE_INVITED.getCode())) {
                if (visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
                    VisitorSysConfiguration config = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(visitor.getNamespaceId(), VisitorsysOwnerType.COMMUNITY.getCode(), visitor.getOwnerId());
                    if (TrueOrFalseFlag.FALSE.getCode().equals(config.getBaseConfig().getVisitorConfirmFlag())) {
                            visitor.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
                            visitor.setVisitStatus(VisitorsysStatus.HAS_VISITED.getCode());
                    }
                } else {
                    VisitorSysConfiguration config = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(visitor.getNamespaceId(), VisitorsysOwnerType.ENTERPRISE.getCode(), visitor.getEnterpriseId());
                    if (null == config.getBaseConfig() || TrueOrFalseFlag.FALSE.getCode().equals(config.getBaseConfig().getVisitorConfirmFlag())) {
                            visitor.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
                            visitor.setVisitStatus(VisitorsysStatus.HAS_VISITED.getCode());
                    }
                }
            }
//          企业自助登记
            if(visitor.getId() != null && visitorsysOwnerType == VisitorsysOwnerType.ENTERPRISE){
                VisitorSysConfiguration config = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(visitor.getNamespaceId(), VisitorsysOwnerType.ENTERPRISE.getCode(), visitor.getEnterpriseId());
                if (null == config.getBaseConfig() || TrueOrFalseFlag.FALSE.getCode().equals(config.getBaseConfig().getVisitorConfirmFlag())) {
                    visitor.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
                    visitor.setVisitStatus(VisitorsysStatus.HAS_VISITED.getCode());
                }
            }
        }

        Map<String, VisitorsysApprovalFormItem> map =null;
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY && visitor.getCommunityFormValues()!=null) {
            map = visitor.getCommunityFormValues().stream().collect(Collectors.toMap(VisitorsysApprovalFormItem::getFieldName, x -> x));
            visitor.setFormJsonValue(visitor.getCommunityFormValues().toString());
        }else if(visitor.getEnterpriseFormValues()!=null){
            map = visitor.getEnterpriseFormValues().stream().collect(Collectors.toMap(VisitorsysApprovalFormItem::getFieldName, x -> x));
            visitor.setFormJsonValue(visitor.getEnterpriseFormValues().toString());
        }
        if(map!=null) {
            //将表单中的值展开
            setVisitorFormValues(visitor, map);
        }
        //设置门禁相关的二维码，门禁的id
        checkDoorGuard(configuration,visitor);
//        visitor.setSendSmsFlag(null);
    }

    /**
     * 设置门禁二维码
     * @param visitor
     */
    private void checkDoorGuard(VisitorSysVisitor visitor) {
        GetConfigurationResponse configuration = getConfiguration(ConvertHelper.convert(visitor, GetConfigurationCommand.class));
        checkDoorGuard(configuration,visitor);
    }
    /**
     * 设置门禁二维码
     * @param configuration
     * @param visitor
     */
    private void checkDoorGuard(GetConfigurationResponse configuration, VisitorSysVisitor visitor) {
        //门禁id为空
//        if(configuration.getBaseConfig()==null || configuration.getBaseConfig().getDoorGuardId()==null){
//            return;
//        }
        if(StringUtils.isEmpty(visitor.getDoorGuardId())){
            return;
        }
        VisitorsysFlagType doorGuardsFlag = VisitorsysFlagType.fromCode(configuration.getBaseConfig().getDoorGuardsFlag());
        if(doorGuardsFlag== VisitorsysFlagType.NO){//未开启门禁
            return;
        }
        VisitorsysFlagType doorGuardsConfirmedFlag = VisitorsysFlagType.fromCode(configuration.getBaseConfig().getDoorGuardsValidAfterConfirmedFlag());
        VisitorsysStatus bookingStatus = checkBookingStatus(visitor.getBookingStatus());
//        if(VisitorsysFlagType.YES == doorGuardsConfirmedFlag && bookingStatus==VisitorsysStatus.NOT_VISIT){//确认到访才发放门禁二维码
//            return;
//        }
        //申请门禁二维码，并且设置到visitor中
        CreateLocalVistorCommand doorCmd = new CreateLocalVistorCommand();
        doorCmd.setPhone(visitor.getVisitorPhone());
        doorCmd.setDoorId(Long.valueOf(visitor.getDoorGuardId()));
        doorCmd.setNamespaceId(visitor.getNamespaceId());
        doorCmd.setUserName(visitor.getVisitorName());
        doorCmd.setDescription(visitor.getVisitReason());
//      两种授权方式
        doorCmd.setAuthRuleType((byte)0);
        long now = System.currentTimeMillis();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(now);
        doorCmd.setValidFromMs(now);
        if(null != visitor.getDoorAccessAuthDuration()){
            if(visitor.getDoorAccessAuthDurationType().equals((byte)0)){
                instance.add(Calendar.DAY_OF_MONTH,visitor.getDoorAccessAuthDuration());
            }else{
                instance.add(Calendar.HOUR_OF_DAY,visitor.getDoorAccessAuthDuration());
            }
            visitor.setDoorAccessEndTime(instance.getTimeInMillis());
            doorCmd.setValidEndMs(instance.getTimeInMillis());
            visitor.setDoorAccessEndTime(instance.getTimeInMillis());
        }
        if(null != visitor.getDoorAccessAuthCount() && visitor.getDoorAccessEnableAuthCount().equals((byte)1)){
            doorCmd.setAuthRuleType((byte)1);
            doorCmd.setTotalAuthAmount(visitor.getDoorAccessAuthCount());
        }

        if(visitor.getVisitorPicUri()!=null && visitor.getVisitorPicUri().length()>0){
            doorCmd.setHeadImgUri(visitor.getVisitorPicUri());
        }
        DoorAuthDTO localVisitorAuth = null;
        try {
            localVisitorAuth = doorAccessService.createLocalVisitorAuth(doorCmd);
        } catch (Exception e) {
            LOGGER.error("error invoke dooraccess,stacktrace = {}",e);
        }
        if(localVisitorAuth!=null){
            visitor.setDoorGuardId(""+localVisitorAuth.getDoorId());
            visitor.setDoorGuardQrcode(localVisitorAuth.getQrString());
            if(localVisitorAuth.getValidEndMs()!=null) {
                visitor.setDoorGuardEndTime(new Timestamp(localVisitorAuth.getValidEndMs()));
            }
        }else{
//          门禁授权返回空
            LOGGER.info("DoorAccess Auth,cmd = {}",doorCmd.toString());
        }
    }

    /**
     * 获取表单配置
     * @param namespaceId
     * @param ownerType
     * @param ownerId
     * @param enterpriseFormValues
     */
    private void checkFormConfiguration(Integer namespaceId, String ownerType, Long ownerId, List<VisitorsysApprovalFormItem> enterpriseFormValues) {
        GetConfigurationCommand cmd = new GetConfigurationCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setOwnerType(ownerType);
        cmd.setOwnerId(ownerId);
        GetConfigurationResponse response = getConfiguration(cmd);
        checkFormConfiguration(response,enterpriseFormValues);
    }

    /**
     * 根据访客，生成关联的（公司/园区）访客
     * @param visitor
     * @param formValues
     * @return
     */
    private VisitorSysVisitor generateRelatedVisitor(VisitorSysVisitor visitor, List<VisitorsysApprovalFormItem> formValues) {
        VisitorsysOwnerType ownerType = checkOwnerType(visitor.getOwnerType());
        VisitorSysVisitor relatedVisitor = getRelatedVisitor(visitor);
        VisitorSysVisitor convert = ConvertHelper.convert(visitor, VisitorSysVisitor.class);
        //新建访客/预约
        if(relatedVisitor !=null){
            convert = relatedVisitor;
            convert.setEnterpriseId(visitor.getEnterpriseId());
            convert.setVisitorName(visitor.getVisitorName());
            convert.setVisitorPhone(visitor.getVisitorPhone());
            convert.setVisitorSignUri(visitor.getVisitorSignUri());
            convert.setVisitorSignCharacter(visitor.getVisitorSignCharacter());
            convert.setVisitorPicUri(visitor.getVisitorPicUri());
            convert.setPlannedVisitTime(visitor.getPlannedVisitTime());
            convert.setVisitReason(visitor.getVisitReason());
            convert.setVisitReasonId(visitor.getVisitReasonId());
            convert.setFollowUpNumbers(visitor.getFollowUpNumbers());
        }
        if(ownerType == VisitorsysOwnerType.COMMUNITY) {
            convert.setParentId(relatedVisitor ==null?visitor.getId(): relatedVisitor.getParentId());
            convert.setOwnerId(visitor.getEnterpriseId());
            convert.setOwnerType(VisitorsysOwnerType.ENTERPRISE.getCode());
            if (formValues != null) {
                setVisitorFormValues(convert, formValues.stream().collect(Collectors.toMap(VisitorsysApprovalFormItem::getFieldName, x -> x)));
                convert.setFormJsonValue(formValues.toString());
            }
        }else {
            Long communityId = organizationService.getOrganizationActiveCommunityId(visitor.getOwnerId());
            convert.setParentId(relatedVisitor ==null?visitor.getId(): relatedVisitor.getParentId());
            convert.setOwnerId(communityId==null?0L:communityId);
            convert.setOwnerType(VisitorsysOwnerType.COMMUNITY.getCode());

            if (formValues != null) {
                setVisitorFormValues(convert, formValues.stream().collect(Collectors.toMap(VisitorsysApprovalFormItem::getFieldName, x -> x)));
                convert.setFormJsonValue(formValues.toString());
            }
        }
        //2018.05.24 与产品 李磊,何智辉商定如此
        //创建园区临时访客，如果状态是已到访，那么设置对应企业访客状态为待确认
        //创建园区临时访客，如果状态是等待确认，那么设置对应企业访客状态为隐藏
        //创建企业临时访客，如果状态是已到访，那么设置对应园区访客状态为为隐藏
        //创建企业临时访客，如果状态是等待确认，那么设置对应园区访客状态为隐藏
        VisitorsysVisitorType visitorType = checkVisitorType(visitor.getVisitorType());
        VisitorsysStatus visitStatus = checkVisitStatus(visitor.getVisitStatus());

        if(relatedVisitor == null
                && visitorType == VisitorsysVisitorType.TEMPORARY){
            convert.setBookingStatus(null);
            if(ownerType == VisitorsysOwnerType.COMMUNITY && visitStatus == VisitorsysStatus.HAS_VISITED){
                convert.setVisitStatus(VisitorsysStatus.WAIT_CONFIRM_VISIT.getCode());
            }else if(ownerType == VisitorsysOwnerType.ENTERPRISE &&visitStatus == VisitorsysStatus.HAS_VISITED){
                convert.setVisitStatus(VisitorsysStatus.HIDDEN.getCode());
            }else if(visitStatus == VisitorsysStatus.WAIT_CONFIRM_VISIT){
                convert.setVisitStatus(VisitorsysStatus.HIDDEN.getCode());
            }
        }
        //更新园区临时访客，如果状态是已到访，那么设置对应企业访客状态为待确认
        //更新园区临时访客，如果状态是等待确认，那么保持企业访客状态
        //更新企业临时访客，如果状态是已到访，那么保持园区访客状态
        //更新企业临时访客，如果状态是等待确认，那么保持园区访客状态
        else if(relatedVisitor!=null && visitorType == VisitorsysVisitorType.TEMPORARY){
            convert.setBookingStatus(null);
            //园区临时访客已经在后台登记了，然后设置对应的企业访客为等待确认
            if(ownerType == VisitorsysOwnerType.COMMUNITY && visitStatus == VisitorsysStatus.HAS_VISITED){
                convert.setVisitStatus(VisitorsysStatus.WAIT_CONFIRM_VISIT.getCode());
            }else{
                convert.setVisitStatus(relatedVisitor.getVisitStatus());
            }
        }
        else {
            convert.setBookingStatus(relatedVisitor == null ? visitor.getBookingStatus() : relatedVisitor.getBookingStatus());
            convert.setVisitStatus(relatedVisitor == null ? visitor.getVisitStatus() : relatedVisitor.getVisitStatus());
        }
        convert.setEnterpriseName(visitor.getEnterpriseName());
        //门禁授权信息不需要拷贝，如果为空则为空
        convert.setDoorGuardId(relatedVisitor ==null?null:relatedVisitor.getDoorGuardId());
        convert.setDoorGuardQrcode(relatedVisitor ==null?null:relatedVisitor.getDoorGuardQrcode());
        convert.setDoorGuardEndTime(relatedVisitor ==null?null:relatedVisitor.getDoorGuardEndTime());
        //自助登记，确认到访，拒绝，删除 园区访客和企业访客都是独立处理的
        if(checkVisitStatus(convert.getVisitStatus())== VisitorsysStatus.WAIT_CONFIRM_VISIT){
            convert.setVisitTime(new Timestamp(System.currentTimeMillis()));
        }else {
            convert.setVisitTime(relatedVisitor == null ? null : relatedVisitor.getVisitTime());
        }

        //      是否需要到访确认
//
//        if(ownerType == VisitorsysOwnerType.COMMUNITY){
//            VisitorSysConfiguration entConfig = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(visitor.getNamespaceId(),VisitorsysOwnerType.ENTERPRISE.getCode(),visitor.getEnterpriseId());
//            if(null != entConfig.getBaseConfig() && TrueOrFalseFlag.FALSE.getCode().equals(entConfig.getBaseConfig().getVisitorConfirmFlag())){
//                if(relatedVisitor == null
//                        && visitorType == VisitorsysVisitorType.TEMPORARY){
//                    convert.setVisitStatus(VisitorsysStatus.HAS_VISITED.getCode());
//                }else{
//                    convert.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
//                }
//            } else {
//                if(relatedVisitor == null
//                        && visitorType == VisitorsysVisitorType.TEMPORARY){
//                    convert.setVisitStatus(VisitorsysStatus.WAIT_CONFIRM_VISIT.getCode());
//                }else{
//                    convert.setBookingStatus(VisitorsysStatus.NOT_VISIT.getCode());
//                }
//            }
//        } else {
//            VisitorSysConfiguration config = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(visitor.getNamespaceId(),VisitorsysOwnerType.COMMUNITY.getCode(),convert.getOwnerId());
//            if(TrueOrFalseFlag.FALSE.getCode().equals(config.getBaseConfig().getVisitorConfirmFlag())){
//                if(relatedVisitor == null
//                        && visitorType == VisitorsysVisitorType.TEMPORARY){
//                    convert.setVisitStatus(VisitorsysStatus.HAS_VISITED.getCode());
//                }else{
//                    convert.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
//                }
//            } else {
//                if(relatedVisitor == null
//                        && visitorType == VisitorsysVisitorType.TEMPORARY){
//                    convert.setVisitStatus(VisitorsysStatus.WAIT_CONFIRM_VISIT.getCode());
//                }else{
//                    convert.setBookingStatus(VisitorsysStatus.NOT_VISIT.getCode());
//                }
//            }
//        }
        Community community = null;
        if(ownerType == VisitorsysOwnerType.COMMUNITY){
            community = communityProvider.findCommunityById(visitor.getOwnerId());
        }

//        园区确认预约访客，企业的关联 和 园区登记临时访客，企业关联
        if(community == null || CommunityType.COMMERCIAL.equals(community.getCommunityType())){
            if ((convert.getId() != null && convert.getVisitorType().equals(VisitorsysVisitorType.BE_INVITED.getCode())) ||
                    convert.getVisitorType().equals(VisitorsysVisitorType.TEMPORARY.getCode())) {
                if (ownerType == VisitorsysOwnerType.COMMUNITY) {
                    VisitorSysConfiguration config = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(convert.getNamespaceId(), VisitorsysOwnerType.ENTERPRISE.getCode(), convert.getEnterpriseId());
                    if (null != config.getBaseConfig() && TrueOrFalseFlag.FALSE.getCode().equals(config.getBaseConfig().getVisitorConfirmFlag())) {
                        convert.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
                        convert.setVisitStatus(VisitorsysStatus.HAS_VISITED.getCode());
                    }
                }
            }
        }

        convert.setSendMessageInviterFlag(relatedVisitor ==null?visitor.getSendMessageInviterFlag():relatedVisitor.getSendMessageInviterFlag());
        convert.setSendSmsFlag(relatedVisitor ==null?visitor.getSendSmsFlag():relatedVisitor.getSendSmsFlag());
        return convert;
        //更新子访客/预约
    }

    /**
     * 生成邀请码 RG+项目辨识码+预约日期+四位流水号
     * @param visitor
     * @return
     */
    private String generateInvitationNo(VisitorSysVisitor visitor) {
        VisitorsysVisitorType type = checkVisitorType(visitor.getVisitorType());
        if(type == VisitorsysVisitorType.TEMPORARY){
            return null;
        }
        String dayRemark = visitor.getPlannedVisitTime().toLocalDateTime().format(YYYYMMDD);
        VisitorSysCoding visitorSysCoding = visitorSysCodingProvider.findVisitorSysCodingByOwner(visitor.getNamespaceId(),visitor.getOwnerType(),visitor.getOwnerId(),dayRemark);
        VisitorSysOwnerCode visitorSysOwnerCode = visitorSysOwnerCodeProvider.findVisitorSysCodeByOwner(visitor.getNamespaceId(),visitor.getOwnerType(),visitor.getOwnerId());
        if(visitorSysOwnerCode==null){
            visitorSysOwnerCode = createSysOwnerCode(visitor.getNamespaceId(),visitor.getOwnerType(),visitor.getOwnerId());
        }
        if(visitorSysCoding==null){
            visitorSysCoding = createNewVisitorSysCoding(visitor.getNamespaceId(),visitor.getOwnerType(),visitor.getOwnerId(),dayRemark);
        }
        visitorSysCoding.setSerialCode(visitorSysCoding.getSerialCode()+1);
        String serialCode = generateSerialCode(visitorSysCoding.getSerialCode()+"");
        String invitationNo = "RG"+visitorSysOwnerCode.getRandomCode()+visitorSysCoding.getDayMark()+serialCode;
        visitorSysCodingProvider.updateVisitorSysCoding(visitorSysCoding);
        return invitationNo;
    }

    /**
     * 构建owner的标识吗，用于生成邀请编码
     * @param namespaceId
     * @param ownerType
     * @param ownerId
     * @return
     */
    private VisitorSysOwnerCode createSysOwnerCode(Integer namespaceId, String ownerType, Long ownerId) {
        VisitorSysOwnerCode ownerCode = new VisitorSysOwnerCode();
        int length = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_RANDOMCODE_LENGTH,4);
        String randomCode = VisitorSysUtils.generateLetterNumCode(length);
        ownerCode.setNamespaceId(namespaceId);
        ownerCode.setOwnerType(ownerType);
        ownerCode.setOwnerId(ownerId);
        while(visitorSysOwnerCodeProvider.findVisitorSysCodingByRandomCode(randomCode)!=null){
            randomCode = VisitorSysUtils.generateLetterNumCode(length);
        }
        ownerCode.setRandomCode(randomCode);
        visitorSysOwnerCodeProvider.createVisitorSysOwnerCode(ownerCode);
        return ownerCode;
    }

    /**
     * 初始化邀请码配置
     * @param namespaceId
     * @param ownerType
     * @param ownerId
     * @return
     */
    private VisitorSysCoding createNewVisitorSysCoding(Integer namespaceId, String ownerType, Long ownerId,String dayRemark) {
        VisitorSysCoding visitorSysCoding = new VisitorSysCoding();
        visitorSysCoding.setNamespaceId(namespaceId);
        visitorSysCoding.setOwnerId(ownerId);
        visitorSysCoding.setOwnerType(ownerType);
        visitorSysCoding.setSerialCode(0);
        visitorSysCoding.setStatus((byte)2);
        visitorSysCoding.setDayMark(dayRemark);
        visitorSysCodingProvider.createVisitorSysCoding(visitorSysCoding);
        return visitorSysCoding;
    }

    /**
     * 将serialCode 转换为4位流水码
     * @param serialCode
     * @return
     */
    private String generateSerialCode(String serialCode) {
        if(serialCode==null)
            return null;
        int length = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_SERIALCODE_LENGTH,4);
        while(serialCode.length()<length){
            serialCode="0"+serialCode;
        }
        while(serialCode.length()>length){
            serialCode=serialCode.substring(1,serialCode.length());
        }
        return serialCode;
    }


    /**
     * 生成不重复随机配对码
     * @return 6位配对码
     */
    private String generateUnrepeatPairingCode() {
        String jsonValue = null;
        String pairingCode = null;
        int pairingCodeLength = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_PAIRINGCODE_LENGTH, 6);
        do{
            pairingCode = VisitorSysUtils.generateNumCode(pairingCodeLength);
            String key = generatePairingCodeKey(pairingCode);
            ValueOperations<String, String> valueOperations = getValueOperations(key);
            jsonValue = valueOperations.get(key);
        }while (jsonValue!=null);
        return pairingCode;
    }

    /**
     * 检查searchFlag参数
     * @param searchFlag
     * @return
     */
    private VisitorsysSearchFlagType checkSearchFlag(Byte searchFlag) {
        VisitorsysSearchFlagType searchFlagType = VisitorsysSearchFlagType.fromCode(searchFlag);
        if(searchFlagType==null || VisitorsysSearchFlagType.SYNCHRONIZATION==searchFlagType){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "unknow visitor search flag = "+searchFlag);
        }
        return searchFlagType;
    }

    /**
     * 检查flag参数
     * @param flag
     * @return
     */
    private VisitorsysFlagType checkVisitorsysFlag(Byte flag) {
        if(flag==null){
            return null;
        }
        VisitorsysFlagType flagType = VisitorsysFlagType.fromCode(flag);
        if(flagType ==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "unknow visitor flag = "+flag);
        }
        return flagType;
    }


    /**
     * 生成访客邀请码连接
     * @param id 访客id
     * @return
     */
    private String generateInviationLink(Long id) {
//        String invitationLinkTemp = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_INVITATION_LINK,"%s/visitor-appointment/build/invitation.html?visitorToken=%s");
        String invitationLinkTemp = "%s/evh/visitorsys/v?visitorToken=%s";
        String homeUrl = configurationProvider.getValue("home.url","");
        String token = WebTokenGenerator.getInstance().toWebToken(id);
        return String.format(invitationLinkTemp,homeUrl,token);
    }

    /**
     * 校验访客token
     * @param token
     * @return
     */
    private Long checkInviationToken(String token){
        Long id = WebTokenGenerator.getInstance().fromWebToken(token, Long.class);
        if(id==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "invaild token "+token);
        }
        return id;
    }

    @Override
    public IdentifierCardDTO getIDCardInfo() {
        IdentifierCardDTO idCard;
        String url = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"visitorsys.hardware.cardreader","http://localhost:9225/actionapi/ReadCard/Read");
        Map<String,String> params = new HashMap<>();
        String json = null;
        try {
            json = HttpUtils.post(url,params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IdentifierCardResponse resp = JSONObject.parseObject(json,new TypeReference<IdentifierCardResponse>(){});
        if(TrueOrFalseFlag.FALSE.getCode().toString().equals(resp.getCode())){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_READ_CARD,
                    "读卡错误");
        }
        idCard = resp.getCardInfo();
        return idCard;
    }

    @Override
    public List<VisitorSysDoorAccessDTO> listDoorAccess(BaseVisitorsysCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        if(null == cmd.getNamespaceId())
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        List<VisitorSysDoorAccess> results = visitorSysDoorAccessProvider.listVisitorSysDoorAccessByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
        return results.stream().map(r->{
            VisitorSysDoorAccessDTO dto = ConvertHelper.convert(r,VisitorSysDoorAccessDTO.class);
            DoorAccess result = doorAccessProvider.getDoorAccessById(dto.getDoorAccessId());
            dto.setMaxCount(result.getMaxCount());
            dto.setMaxDuration(result.getMaxDuration());
            dto.setEnableAmount(result.getEnableAmount());
            dto.setEnableDuration(result.getEnableDuration());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void createDoorAccess(CreateOrUpdateDoorAccessCommand cmd) {
        if(null == cmd.getId()){
            List<VisitorSysDoorAccess> results = visitorSysDoorAccessProvider.listVisitorSysDoorAccess(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getDoorAccessId());
            if(null != results && results.size() > 0){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE,VisitorsysConstant.ERROR_ALREADY_EXIST,"Record Already exist.");
            }
            VisitorSysDoorAccess bean = ConvertHelper.convert(cmd,VisitorSysDoorAccess.class);
            if(null == cmd.getNamespaceId())
                bean.setNamespaceId(UserContext.getCurrentNamespaceId());
            bean.setDefaultDoorAccessFlag(TrueOrFalseFlag.FALSE.getCode());
            visitorSysDoorAccessProvider.createVisitorSysDoorAccess(bean);
//          如果是第一个置为默认值
            List<VisitorSysDoorAccess> results1 = visitorSysDoorAccessProvider.listVisitorSysDoorAccess(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),null);
            if(results1.size() == 1){
                cmd.setId(bean.getId());
                setDefaultAccess(cmd);
            }
        } else {
            VisitorSysDoorAccess bean = visitorSysDoorAccessProvider.findVisitorSysDoorAccess(cmd.getId());
            if(null != cmd.getDefaultAuthDurationType())
                bean.setDefaultAuthDurationType(cmd.getDefaultAuthDurationType());
            if(null != cmd.getDefaultAuthDuration())
                bean.setDefaultAuthDuration(cmd.getDefaultAuthDuration());
            if(null != cmd.getDefaultEnableAuthCount())
                bean.setDefaultEnableAuthCount(cmd.getDefaultEnableAuthCount());
            if(null != cmd.getDefaultAuthCount())
                bean.setDefaultAuthCount(cmd.getDefaultAuthCount());
            visitorSysDoorAccessProvider.updateVisitorSysDoorAccess(bean);
        }
    }

    @Override
    public void deleteDoorAccess(DeleteDoorAccessCommand cmd) {
        if(null == cmd.getId()){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS,"invaild param id");
        }
        VisitorSysDoorAccess bean = visitorSysDoorAccessProvider.findVisitorSysDoorAccess(cmd.getId());
        if(null != bean)
            visitorSysDoorAccessProvider.deleteVisitorSysDoorAccesss(bean.getId());
        if(bean.getDefaultDoorAccessFlag().equals((byte)1)){
            List<VisitorSysDoorAccess> dooraccesses = visitorSysDoorAccessProvider.listVisitorSysDoorAccessByOwner(bean.getNamespaceId(),bean.getOwnerType(),bean.getOwnerId());
            if(null != dooraccesses && dooraccesses.size() > 0){
                VisitorSysDoorAccess newdefault = dooraccesses.get(0);
                CreateOrUpdateDoorAccessCommand newdefaultCmd = new CreateOrUpdateDoorAccessCommand();
                newdefaultCmd.setId(newdefault.getId());
                newdefaultCmd.setNamespaceId(newdefault.getNamespaceId());
                newdefaultCmd.setOwnerType(newdefault.getOwnerType());
                newdefaultCmd.setOwnerId(newdefault.getOwnerId());
                setDefaultAccess(newdefaultCmd);
            }
        }
    }

    @Override
    public void setDefaultAccess(CreateOrUpdateDoorAccessCommand cmd) {
        if(null == cmd.getId()){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS,"invaild param id");
        }
        List<VisitorSysDoorAccess> results = visitorSysDoorAccessProvider.listVisitorSysDoorAccessByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
        results.forEach(r ->{
            if(r.getId().equals(cmd.getId())){
                r.setDefaultDoorAccessFlag(TrueOrFalseFlag.TRUE.getCode());
                visitorSysDoorAccessProvider.updateVisitorSysDoorAccess(r);
            }else{
                if(TrueOrFalseFlag.TRUE.getCode().equals(r.getDefaultDoorAccessFlag())){
                    r.setDefaultDoorAccessFlag(TrueOrFalseFlag.FALSE.getCode());
                    visitorSysDoorAccessProvider.updateVisitorSysDoorAccess(r);
                }

            }
        });
    }

    @Override
    public void removeInvalidTime() {
        List<VisitorSysConfiguration> configs = visitorSysConfigurationProvider.listVisitorSysConfiguration();
    }

    @Override
    public GetConfigurationResponse getConfigurationForManage(GetConfigurationCommand cmd) {
        checkMoblieManagePrivilege(cmd);
        return getConfiguration(cmd);
    }

    @Override
    public List<VisitorSysDoorAccessDTO> listDoorAccessForManage(BaseVisitorsysCommand cmd) {
        checkMoblieManagePrivilege(cmd);
        return listDoorAccess(cmd);
    }

    @Override
    public void syncHKWSUsers() {
        HKWSUtil.syncHKWSUsers(null);
    }

    @Override
    public void HKWSTest(BaseVisitorsysCommand cmd) {
        VisitorSysVisitor bean = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getPmId());
        if(cmd.getAppId() == 1L){
            HKWSUtil.addAppointment(bean);
        } else if (cmd.getAppId() == 2L) {
            HKWSUtil.delAppointment(bean);
        }
    }

    @Override
    public ListFreqVisitorsResponse listFreqVisitors(ListFreqVisitorsCommand cmd) {
        return freqVisitorSearcher.searchVisitors(cmd);
    }

    @Override
    public void syncFreqVisitors(BaseVisitorsysCommand cmd) {
        freqVisitorSearcher.syncVisitorsFromDb(cmd.getNamespaceId());
    }
}
