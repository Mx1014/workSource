// @formatter:off
package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bus.*;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.aclink.ListDoorAccessGroupCommand;
import com.everhomes.rest.aclink.ListDoorAccessResponse;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.OrganizationQueryResult;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.rest.visitorsys.ui.*;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.search.VisitorsysSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

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
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
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
    public static final String[] checkMustFillField = {"visitorName","visitorPhone",
            "enterpriseId","enterpriseName","officeLocationId","officeLocationName",
            "visitReasonId","visitReason"};
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    public VisitorSysVisitorProvider visitorSysVisitorProvider;
    @Autowired
    public VisitorSysOfficeLocationProvider visitorSysOfficeLocationProvider;
    @Autowired
    public VisitorSysVisitReasonProvider visitorSysVisitReasonProvider;
    @Autowired
    public VisitorSysCodingProvider visitorSysCodingProvider;
    @Autowired
    public VisitorSysConfigurationProvider visitorSysConfigurationProvider;
    @Autowired
    public ConfigurationProvider configurationProvider;
    @Autowired
    public CommunityProvider communityProvider;
    @Autowired
    public OrganizationProvider organizationProvider;
    @Autowired
    public VisitorsysSearcher visitorsysSearcher;
    @Autowired
    public OrganizationSearcher organizationSearcher;
    @Autowired
    public OrganizationService organizationService;
    @Autowired
    public ContentServerService contentServerService;
    @Autowired
    public SmsProvider smsProvider;
    @Autowired
    public BigCollectionProvider bigCollectionProvider;
    @Autowired
    public VisitorSysDeviceProvider visitorSysDeviceProvider;
    @Autowired
    private BusBridgeProvider busBridgeProvider;
    @Autowired
    private LocalBus localBus;
    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;
    @Autowired
    private VisitorSysBlackListProvider visitorSysBlackListProvider;
    @Autowired
    private DoorAccessService doorAccessService;
    @Override
    public ListBookedVisitorsResponse listBookedVisitors(ListBookedVisitorsCommand cmd) {
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
                    new TypeReference<List<PostApprovalFormItem>>() {}));
            response.setEnterpriseFormValues(JSONObject.parseObject(relatedVisitor.getFormJsonValue(),
                    new TypeReference<List<PostApprovalFormItem>>() {}));
        }else{
            response.setEnterpriseFormValues(JSONObject.parseObject(visitor.getFormJsonValue(),
                    new TypeReference<List<PostApprovalFormItem>>() {}));
            response.setCommunityFormValues(JSONObject.parseObject(relatedVisitor.getFormJsonValue(),
                    new TypeReference<List<PostApprovalFormItem>>() {}));
        }

        return response;
    }

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
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());

        Integer pageSize = PaginationConfigHelper.getMaxPageSize(configurationProvider, cmd.getPageSize());
        Long pageAnchor = cmd.getPageAnchor() == null ? Long.MAX_VALUE : cmd.getPageAnchor();//倒序使用long的最大值，正序使用0
        List<VisitorSysOfficeLocation> visitorSysOfficeLocations = visitorSysOfficeLocationProvider.listVisitorSysOfficeLocation(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), pageSize, pageAnchor);
        ListOfficeLocationsResponse response = new ListOfficeLocationsResponse();
        if(visitorSysOfficeLocations!=null && visitorSysOfficeLocations.size()==pageSize){
            response.setNextPageAnchor(visitorSysOfficeLocations.get(pageSize-1).getId());
        }
        response.setOfficeLocationList(visitorSysOfficeLocations.stream().map(r->ConvertHelper.convert(r,BaseOfficeLocationDTO.class)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public ListCommunityOrganizationsResponse listCommunityOrganizations(ListCommunityOrganizationsCommand cmd) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        if(visitorsysOwnerType==VisitorsysOwnerType.ENTERPRISE){
            return null;
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
        List<VisitorSysVisitReason> visitorSysVisitReasons = visitorSysVisitReasonProvider.listVisitorSysVisitReason(cmd.getNamespaceId());
        ListVisitReasonsResponse response = new ListVisitReasonsResponse();
        response.setVisitorReasonList(visitorSysVisitReasons.stream().map(r->{
            BaseVisitorReasonDTO convert = new BaseVisitorReasonDTO();
            convert.setVisitReason(r.getVisitReason());
            convert.setVisitReasonId(r.getId());
            return convert;
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public GetBookedVisitorByIdResponse createOrUpdateVisitor(CreateOrUpdateVisitorCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        checkBlackList(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getVisitorPhone(),cmd.getEnterpriseId());
        if(cmd.getId()==null) {
            //检查参数，生成访客实体
            VisitorSysVisitor visitor = checkCreateVisitor(cmd);
            //这里上锁的目的是，生成唯一的邀请码
            coordinationProvider.getNamedLock(CoordinationLocks.VISITOR_SYS_GEN_IN_NO.getCode()+visitor.getOwnerType()+visitor.getOwnerId()).enter(
                    new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            visitor.setInvitationNo(generateInvitationNo(visitor.getNamespaceId(),visitor.getOwnerType(),visitor.getOwnerId()));
                            //这里上事务的原因是，需要同时创建园区下公司的访客/预约记录和同步es
                            dbProvider.execute(action ->createVisitorSysVisitor(visitor,cmd));
                            return null;
                        }
                    }
          );
        }else{
            VisitorSysVisitor visitor = checkUpdateVisitor(cmd);
            dbProvider.execute(r->updateVisitorSysVisitor(visitor,cmd));
        }
        GetBookedVisitorByIdResponse convert = ConvertHelper.convert(cmd, GetBookedVisitorByIdResponse.class);
        convert.setVisitorPicUrl(contentServerService.parserUri(convert.getVisitorPicUri()));
        convert.setVisitorSignUrl(contentServerService.parserUri(convert.getVisitorSignUri()));
        return convert;
    }

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
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INBLACKLIST_PHONE_ENTERPRISE,
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
        VisitorSysVisitor relatedVisitor = null;
        if (visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            relatedVisitor = generateRelatedVisitor(visitor,cmd.getEnterpriseFormValues());
        }else {
            relatedVisitor = generateRelatedVisitor(visitor,cmd.getCommunityFormValues());
        }
        visitorSysVisitorProvider.createVisitorSysVisitor(relatedVisitor);
        visitorsysSearcher.syncVisitor(relatedVisitor);
        return null;
    }

    /**
     * 更新访客
     * @param visitor
     * @param cmd
     * @return
     */
    private Object updateVisitorSysVisitor(VisitorSysVisitor visitor, CreateOrUpdateVisitorCommand cmd) {visitorSysVisitorProvider.updateVisitorSysVisitor(visitor);
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        visitorSysVisitorProvider.updateVisitorSysVisitor(visitor);
        visitorsysSearcher.syncVisitor(visitor);
        VisitorSysVisitor related = null;
        if (visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            related = generateRelatedVisitor(visitor,cmd.getEnterpriseFormValues());
        }else {
            related = generateRelatedVisitor(visitor,cmd.getCommunityFormValues());
        }
        visitorSysVisitorProvider.updateVisitorSysVisitor(related);
        visitorsysSearcher.syncVisitor(related);
        return null;
    }

    @Override
    public void sendVisitorSMS(GetBookedVisitorByIdCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        if(visitor==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "unknow visitorid = " + cmd.getVisitorId());
        }
        VisitorsysVisitorType visitorType = checkVisitorType(visitor.getVisitorType());
        if(visitorType==VisitorsysVisitorType.TEMPORARY){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "temporary visitor not support send sms");
        }
        VisitorsysStatus status = checkBookingStatus(visitor.getBookingStatus());
        if(VisitorsysStatus.NOT_VISIT!=status){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "not support visitor status = "+visitor.getVisitStatus());
        }
        if(visitor.getVisitorPhone()==null){
            return;
        }

        List<Tuple<String, Object>> variables =  new ArrayList();
        smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_APPNAME, "");
        smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_VISITENTERPRISENAME, visitor.getEnterpriseName());
        smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_INVITATIONLINK, generateInviationLink(visitor.getId()));
        String templateLocale = UserContext.current().getUser().getLocale();
        smsProvider.sendSms(visitor.getNamespaceId(), visitor.getVisitorPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.VISITORSYS_INVITATION_LETTER, templateLocale, variables);

    }

    @Override
    public void deleteVisitor(GetBookedVisitorByIdCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        if(cmd.getVisitorId()==null){
            return;
        }

        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        if(visitor==null){
            return;
        }
        dbProvider.execute(r->{
            visitorSysVisitorProvider.deleteVisitorSysVisitor(cmd.getNamespaceId(),cmd.getVisitorId());
            visitorsysSearcher.syncVisitor(cmd.getNamespaceId(),cmd.getVisitorId());
            VisitorSysVisitor relatedVisitor = getRelatedVisitor(visitor);
            visitorSysVisitorProvider.deleteVisitorSysVisitor(relatedVisitor.getNamespaceId(),relatedVisitor.getId());
            visitorsysSearcher.syncVisitor(relatedVisitor.getNamespaceId(),relatedVisitor.getId());
            return null;
        });

    }

    @Override
    public void deleteVisitorAppoint(GetBookedVisitorByIdCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        if(cmd.getVisitorId()==null){
            return;
        }
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        if(visitor==null){
            return;
        }

        dbProvider.execute(r->{
            visitorSysVisitorProvider.deleteVisitorSysVisitorAppoint(cmd.getNamespaceId(),cmd.getVisitorId());
            visitorsysSearcher.syncVisitor(cmd.getNamespaceId(),cmd.getVisitorId());
            VisitorSysVisitor relatedVisitor = getRelatedVisitor(visitor);
            visitorSysVisitorProvider.deleteVisitorSysVisitorAppoint(relatedVisitor.getNamespaceId(),relatedVisitor.getId());
            visitorsysSearcher.syncVisitor(relatedVisitor.getNamespaceId(),relatedVisitor.getId());
            return null;
        });
    }

    @Override
    public void confirmVisitor(CreateOrUpdateVisitorCommand cmd) {
        if(cmd.getId()==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
    private VisitorSysVisitor generateConfirmVisitor(VisitorSysVisitor visitor,VisitorsysVisitorType visitorsysVisitorType) {
        if(visitorsysVisitorType == VisitorsysVisitorType.BE_INVITED) {
            visitor.setBookingStatus(VisitorsysStatus.HAS_VISITED.getCode());
        }
        visitor.setVisitStatus(VisitorsysStatus.HAS_VISITED.getCode());
        visitor.setConfirmTime(new Timestamp(System.currentTimeMillis()));
        User user = UserContext.current().getUser();
        visitor.setConfirmUid(user==null?-1:user.getId());
        visitor.setConfirmUname(user==null?"anonymous":user.getAccountName());
        return visitor;
    }

    @Override
    public GetStatisticsResponse getStatistics(GetStatisticsCommand cmd) {
        return visitorsysSearcher.getStatistics(cmd);
    }

    @Override
    public AddDeviceResponse addDevice(AddDeviceCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        if(cmd.getPairingCode()==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
        try {
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
            return null;
        }finally {
            LOGGER.info("key = {}, jsonValue = {}", key, jsonValue);
            ExecutorUtil.submit(()->{
                    try {
                        LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
                        localBusSubscriber.onLocalBusMessage(null, VisitorsysConstant.VISITORSYS_SUBJECT + "." + pairingCode, String.valueOf(VisitorsysFlagType.YES.getCode()), null);
                    } catch (Exception e) {
                        LOGGER.error("submit LocalBusSubscriber {}.{} got excetion", VisitorsysConstant.VISITORSYS_SUBJECT, pairingCode, e);
                    }
                    localBus.publish(null, VisitorsysConstant.VISITORSYS_SUBJECT + "." + pairingCode, String.valueOf(VisitorsysFlagType.YES.getCode()));
                }
          );
        }
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
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());

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
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
//            BufferedImage image = QRCodeEncoder.createQrCode(qrcode.getQrcode(),
//                    width, height, null);
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Failed to download the package file");
        } finally {
            FileHelper.closeOuputStream(out);
            FileHelper.closeOuputStream(bos);
        }
    }


    private GetConfigurationResponse getDefaultConfiguration() {
        List<GeneralFormFieldDTO> formConfig = JSONObject.parseObject(VisitorsysConstant.DEFAULT_FORM_JSON,
                new TypeReference<List<GeneralFormFieldDTO>>() {});
        GetConfigurationResponse response = new GetConfigurationResponse();
        response.setFormConfig(formConfig);
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
        String invitationLinkTemp = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_SELFREGISTER_LINK,"%s/selfregister?token=%s");
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
            visitorSysBlackListProvider.createVisitorSysBlackList(convert);
            VisitorSysBlackList blackList = visitorSysBlackListProvider.findVisitorSysBlackListByPhone(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getVisitorPhone());
            if(blackList!=null){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_REPEAT_PHONE,
                        "repeat phone");
            }
        }else {
            VisitorSysBlackList blackList = visitorSysBlackListProvider.findVisitorSysBlackListById(cmd.getId());
            if(blackList==null){
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
        checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        ListDoorAccessGroupCommand doorcmd = ConvertHelper.convert(cmd,ListDoorAccessGroupCommand.class);
        doorcmd.setSearch(cmd.getKeyWords());
        doorcmd.setOwnerType((byte)0);
        ListDoorAccessResponse listDoorAccessResponse = doorAccessService.listDoorAccessGroup(doorcmd);
        if(listDoorAccessResponse==null || listDoorAccessResponse.getDoors()==null){
            return null;
        }
        ListDoorGuardsResponse response = new ListDoorGuardsResponse();
        response.setDoorGuardList(listDoorAccessResponse.getDoors().stream().map(r->{
            BaseDoorGuardDTO dto = new BaseDoorGuardDTO();
            dto.setDoorGuardId(String.valueOf(r.getId()));
            dto.setDoorGuardName(r.getName());
            return dto;
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public void rejectVisitor(GetBookedVisitorByIdCommand cmd) {
        checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        if(visitor == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "unknown visitor "+cmd.getVisitorId());
        }
        visitor.setVisitStatus(VisitorsysStatus.REJECTED_VISIT.getCode());
        VisitorsysVisitorType type = checkVisitorType(visitor.getVisitorType());
        dbProvider.execute(r->{
            VisitorSysVisitor relatedVisitor = getRelatedVisitor(visitor);
            relatedVisitor.setVisitStatus(VisitorsysStatus.REJECTED_VISIT.getCode());
            if(type == VisitorsysVisitorType.BE_INVITED){
                visitor.setBookingStatus(VisitorsysStatus.REJECTED_VISIT.getCode());
                relatedVisitor.setBookingStatus(VisitorsysStatus.REJECTED_VISIT.getCode());
            }
            visitorSysVisitorProvider.updateVisitorSysVisitor(visitor);
            visitorsysSearcher.syncVisitor(visitor);
            visitorSysVisitorProvider.updateVisitorSysVisitor(relatedVisitor);
            visitorsysSearcher.syncVisitor(relatedVisitor);
            return null;
        });
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "unknown visitorToken "+cmd.getVisitorToken());
        }

        GetConfigurationResponse configuration = getConfiguration(ConvertHelper.convert(visitor, GetConfigurationCommand.class));
        GetInvitationLetterForWebResponse response = new GetInvitationLetterForWebResponse();
        response = VisitorSysUtils.copyAllNotNullProperties(configuration, response);
        response.setLogoUrl(contentServerService.parserUri(response.getLogoUri()));
        VisitorSysOfficeLocation location = visitorSysOfficeLocationProvider.findVisitorSysOfficeLocationById(visitor.getOfficeLocationId());
        response.setOfficeLocationDTO(ConvertHelper.convert(location,BaseOfficeLocationDTO.class));
        response.setVisitorInfoDTO(ConvertHelper.convert(visitor,BaseVisitorInfoDTO.class));
        return response;
    }

    @Override
    public GetBookedVisitorByIdResponse createOrUpdateVisitorForWeb(CreateOrUpdateVisitorCommand cmd) {
        beforePostForWeb(cmd);
        return createOrUpdateVisitor(cmd);

    }

    @Override
    public ListBookedVisitorsResponse listBookedVisitorsForWeb(ListBookedVisitorsCommand cmd) {
        beforePostForWeb(cmd);
        cmd.setSearchFlag(VisitorsysSearchFlagType.CLIENT_BOOKING.getCode());
        cmd.setVisitorType(VisitorsysVisitorType.BE_INVITED.getCode());
        checkBookingStatus(cmd.getBookingStatus());
        return listBookedVisitors(cmd);
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
        return getConfiguration(ConvertHelper.convert(cmd,GetConfigurationCommand.class));
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
    public DeferredResult<RestResponse> confirmPairingCode(ConfirmPairingCodeCommand cmd) {
        String key = generatePairingCodeKey(cmd.getPairingCode());
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String jsonValue = valueOperations.get(key);
        if(jsonValue == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "unknown pairingCode "+cmd.getPairingCode());
        }

        DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
        RestResponse response =  new RestResponse();
        int pairingCodelive = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_PAIRINGCODE_LIVE, 60);
        localBusSubscriberBuilder.build(VisitorsysConstant.VISITORSYS_SUBJECT + "." + cmd.getPairingCode(), new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                                            Object logonResponse, String path) {
                //这里可以清掉redis存的配对码信息，即对应#getPairingCode()存的设备信息
                String key = generatePairingCodeKey(cmd.getPairingCode());
                deleteValueOperations(key);
                String response = (String)logonResponse;
                deferredResult.setResult(JSONObject.parseObject(response, RestResponse.class));
                return null;
            }
            @Override
            public void onLocalBusListeningTimeout() {
                response.setResponseObject("pairing time out");
                response.setErrorCode(VisitorsysConstant.ERROR_PAIRING_TIMEOUT);
                deferredResult.setResult(response);
            }

        }).setTimeout(pairingCodelive*1000).create();

        return deferredResult;
    }

    @Override
    public GetConfigurationResponse getUIConfiguration(BaseVisitorsysUICommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(),cmd.getDeviceId());
        GetConfigurationResponse configuration = getConfiguration(ConvertHelper.convert(device, GetConfigurationCommand.class));
        return configuration;
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
//        long now = System.currentTimeMillis();
//        List<VisitorSysVisitor> list = visitorSysVisitorProvider.listVisitorSysVisitorByVisitorPhone
//                (device.getNamespaceId(),device.getOwnerType(),device.getOwnerId(),cmd.getVisitorPhone()
//                        ,VisitorSysUtils.getStartOfDay(now),VisitorSysUtils.getEndOfDay(now));
//        if(list==null || list.size()==0){
//            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_VISITOR_NOT_FIND,
//                    "visitor not find.");
//        }
        List<Tuple<String, Object>> variables =  new ArrayList();
        smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_MODLUENAME, configurationProvider.getValue(VisitorsysConstant.VISITORSYS_MODLUENAME, VisitorsysConstant.SMS_MODLUENAME_CN));
        smsProvider.addToTupleList(variables, VisitorsysConstant.SMS_VERIFICATIONCODE, generateVerificationCode(cmd.getVisitorPhone()));
        String templateLocale = UserContext.current().getUser().getLocale();
        smsProvider.sendSms(device.getNamespaceId(), cmd.getVisitorPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.VISITORSYS_VERIFICATION_CODER, templateLocale, variables);
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "unknown verificatcode "+cmd.getVerificationCode());
        }
        String key = generateVerificationKey(cmd.getVerificationCode(),cmd.getVisitorPhone());
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String value = valueOperations.get(key);
        if(value==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_ILLEGAL_VERIFICATIONCODE,
                    "unknown verificatcode "+cmd.getVerificationCode());
        }
        long now = System.currentTimeMillis();
        List<VisitorSysVisitor> list = visitorSysVisitorProvider.listVisitorSysVisitorByVisitorPhone
                (device.getNamespaceId(),device.getOwnerType(),device.getOwnerId(),cmd.getVisitorPhone()
                        ,VisitorSysUtils.getStartOfDay(now),VisitorSysUtils.getEndOfDay(now));
        ListBookedVisitorsResponse response = new ListBookedVisitorsResponse();
        response.setVisitorDtoList(list.stream().map(r->ConvertHelper.convert(r,BaseVisitorDTO.class)).collect(Collectors.toList()));
        return response;
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
        GetConfigurationCommand command = new GetConfigurationCommand();
        command.setNamespaceId(cmd.getNamespaceId());
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        if(visitorsysOwnerType ==VisitorsysOwnerType.COMMUNITY){
            command.setOwnerType(VisitorsysOwnerType.ENTERPRISE.getCode());
            command.setOwnerId(cmd.getEnterpriseId());
        }else{
            Long communityId = organizationService.getOrganizationActiveCommunityId(cmd.getOwnerId());
            command.setOwnerType(VisitorsysOwnerType.COMMUNITY.getCode());
            command.setOwnerId(communityId);
        }
        return new GetFormResponse(getConfiguration(command).getFormConfig());
    }

    @Override
    public GetFormResponse getUIForm(GetUIFormCommand cmd) {
        VisitorSysDevice device = checkDevice(cmd.getDeviceType(), cmd.getDeviceId());
        GetConfigurationCommand command = new GetConfigurationCommand();
        command.setNamespaceId(device.getNamespaceId());
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(device.getOwnerType(), device.getOwnerId());
        if(visitorsysOwnerType ==VisitorsysOwnerType.COMMUNITY){
            command.setOwnerType(VisitorsysOwnerType.ENTERPRISE.getCode());
            command.setOwnerId(cmd.getEnterpriseId());
        }else{
            Long communityId = organizationService.getOrganizationActiveCommunityId(device.getOwnerId());
            command.setOwnerType(VisitorsysOwnerType.COMMUNITY.getCode());
            command.setOwnerId(communityId);
        }
        return new GetFormResponse(getConfiguration(command).getFormConfig());
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
            return;
        }
        LOGGER.error("beforePostForWeb is fail. {}",cmd.getOwnerToken());
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                "unknown ownerToken "+cmd.getOwnerToken());
    }

    @Override
    public GetFormForWebResponse getFormForWeb(GetFormForWebCommand cmd) {
        beforePostForWeb(cmd);
        GetConfigurationCommand command = new GetConfigurationCommand();
        command.setNamespaceId(cmd.getNamespaceId());
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
        if(visitorsysOwnerType ==VisitorsysOwnerType.COMMUNITY) {
            command.setOwnerType(VisitorsysOwnerType.ENTERPRISE.getCode());
            command.setOwnerId(cmd.getEnterpriseId());
        } else{
            Long communityId = organizationService.getOrganizationActiveCommunityId(cmd.getOwnerId());
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
                VisitorsysStatus status = VisitorsysStatus.fromVisitStatusCode(visitorDTO.getVisitStatus());
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "exportBookedVisitors is fail.");
        }

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
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "unknown ownerId "+ownerId);
                }
                break;
            case ENTERPRISE:
                Organization organization = organizationProvider.findOrganizationById(ownerId);
                if(organization==null){
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
    private void setVisitorFormValues(VisitorSysVisitor convert, Map<String, PostApprovalFormItem> map) {
        convert.setInvalidTime(map.get("invalidTime").getFieldValue());
        convert.setPlateNo(map.get("plateNo").getFieldValue());
        convert.setIdNumber(map.get("idNumber").getFieldValue());
        convert.setVisitFloor(map.get("visitFloor").getFieldValue());
        convert.setVisitAddresses(map.get("visitAddresses").getFieldValue());
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
     * 检查非法预约状态
     * @param bookingStatus
     */
    private VisitorsysStatus checkInvaildBookingStatus(Byte bookingStatus) {
        VisitorsysStatus status = checkBookingStatus(bookingStatus);
        //以下两种状态，不能在创建用户的时候传入
        if(status == VisitorsysStatus.DELETED
                || status == VisitorsysStatus.REJECTED_VISIT){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "invaild visitor bookingStatus = "+bookingStatus);
        }
        return status;
    }

    /**
     * 检查非法访客状态
     * @param visitStatus
     */
    private VisitorsysStatus checkInvaildVisitStatus(Byte visitStatus) {
        VisitorsysStatus status = checkVisitStatus(visitStatus);
        //以下两种状态，不能在创建用户的时候传入
        if(status == VisitorsysStatus.DELETED
                || status == VisitorsysStatus.REJECTED_VISIT){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "invaild visitor visitStatus = "+visitStatus);
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor bookingStatus = "+bookingStatus);
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor visitStatus = "+ visitStatus);
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor type = "+visitorType);
        }
        return visitorType;
    }

    /**
     * 检查表单必填项
     * @param owner
     * @param formValues
     * @return
     */
    private boolean checkFormConfiguration(GetConfigurationResponse owner, List<PostApprovalFormItem> formValues) {
        if(owner==null || owner.getFormConfig()==null){
            return false;
        }
        if(formValues==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "form value is null");
        }
        Map<String, PostApprovalFormItem> map = formValues.stream().collect(Collectors.toMap(PostApprovalFormItem::getFieldName, x -> x));
        for (GeneralFormFieldDTO dto : owner.getFormConfig()) {
            if(dto.getRequiredFlag() ==null) {
                continue;
            }
            if(dto.getRequiredFlag() == 1 && map.get(dto.getFieldName())==null){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE
                        , VisitorsysConstant.ERROR_MUST_FILL, dto.getFieldDisplayName()+" should not be null");
            }

        }
        return true;
    }

    /**
     * 生成更新访客的实体
     * @param cmd
     * @return
     */
    private VisitorSysVisitor checkUpdateVisitor(CreateOrUpdateVisitorCommand cmd) {
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(),cmd.getId());
        if(visitor==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor id = "+cmd.getId()+", namespaceId = "+cmd.getNamespaceId()+"");
        }
        if(visitor.getOwnerId().longValue()!=cmd.getOwnerId()
                || !visitor.getOwnerType().equals(cmd.getOwnerType())
                || visitor.getVisitorType().byteValue()!=cmd.getVisitorType().byteValue()){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor "+cmd);
        }
        visitor = VisitorSysUtils.copyNotNullProperties(cmd, visitor);
        checkVisitor(visitor);
        return visitor;
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
        VisitorsysOwnerType visitorsysOwnerType = checkOwner(visitor.getOwnerType(),visitor.getOwnerId());
        checkOwner(VisitorsysOwnerType.ENTERPRISE.getCode(),visitor.getEnterpriseId());
        GetConfigurationResponse configuration = getConfiguration(ConvertHelper.convert(visitor, GetConfigurationCommand.class));
        //检查园区表单
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            checkFormConfiguration(configuration, visitor.getCommunityFormValues());
        }
        //检查公司表单
        checkFormConfiguration(visitor.getNamespaceId(),VisitorsysOwnerType.ENTERPRISE.getCode(),visitor.getEnterpriseId(),visitor.getEnterpriseFormValues());
        VisitorsysVisitorType visitorsysVisitorType = checkVisitorType(visitor.getVisitorType());
        VisitorsysStatus visitStatus = checkInvaildVisitStatus(visitor.getVisitStatus());

        //设置创建预约访客属性
        if(visitorsysVisitorType==VisitorsysVisitorType.BE_INVITED){
            checkMustFillParams(visitor, "plannedVisitTime");
            if(visitor.getPlannedVisitTime().getTime()<System.currentTimeMillis()){
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                        , ErrorCodes.ERROR_INVALID_PARAMETER, "invalid passed plannedVisitTime "+visitor.getPlannedVisitTime());
            }
            if(visitor.getId()==null) {
                visitor.setVisitStatus(VisitorsysStatus.NOT_VISIT.getCode());
            }
            visitor.setBookingStatus(visitor.getVisitStatus());
            if(visitStatus == VisitorsysStatus.NOT_VISIT) {
                visitor.setInviterId(UserContext.current().getUser().getId());
                visitor.setInviterName(UserContext.current().getUser().getAccountName());
            }
        }else{
            if(visitStatus == VisitorsysStatus.NOT_VISIT){
                visitStatus = VisitorsysStatus.WAIT_CONFIRM_VISIT;
                visitor.setVisitStatus(VisitorsysStatus.WAIT_CONFIRM_VISIT.getCode());
            }
            visitor.setBookingStatus(null);
            visitor.setInviterId(null);
            visitor.setInviterName(null);
        }
        //访客状态是等待确认，那么必须设置到访时间
        if(visitStatus== VisitorsysStatus.WAIT_CONFIRM_VISIT){
            visitor.setVisitTime(new Timestamp(System.currentTimeMillis()));
        }else if(visitStatus == VisitorsysStatus.HAS_VISITED){//如果已到访状态
            generateConfirmVisitor(visitor,visitorsysVisitorType);
        }
        Map<String, PostApprovalFormItem> map =null;
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY && visitor.getCommunityFormValues()!=null) {
            map = visitor.getCommunityFormValues().stream().collect(Collectors.toMap(PostApprovalFormItem::getFieldName, x -> x));
            visitor.setFormJsonValue(visitor.getCommunityFormValues().toString());
        }else if(visitor.getEnterpriseFormValues()!=null){
            map = visitor.getEnterpriseFormValues().stream().collect(Collectors.toMap(PostApprovalFormItem::getFieldName, x -> x));
            visitor.setFormJsonValue(visitor.getEnterpriseFormValues().toString());
        }
        if(map!=null) {
            setVisitorFormValues(visitor, map);
        }
    }

    private void checkFormConfiguration(Integer namespaceId, String ownerType, Long ownerId, List<PostApprovalFormItem> enterpriseFormValues) {
        GetConfigurationCommand cmd = new GetConfigurationCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setOwnerType(ownerType);
        cmd.setOwnerId(ownerId);
        GetConfigurationResponse response = getConfiguration(cmd);
        checkFormConfiguration(response,enterpriseFormValues);
    }

    /**
     * 根据访客，生成关联的（公司/园区）访客实体
     * @param visitor
     * @param formValues
     * @return
     */
    private VisitorSysVisitor generateRelatedVisitor(VisitorSysVisitor visitor, List<PostApprovalFormItem> formValues) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(visitor.getOwnerType());
        VisitorSysVisitor relatedVisitor = getRelatedVisitor(visitor);
        VisitorSysVisitor convert = ConvertHelper.convert(visitor, VisitorSysVisitor.class);
        //新建访客/预约
        if(relatedVisitor !=null){
            convert.setId(relatedVisitor.getId());
        }
        if(visitorsysOwnerType == visitorsysOwnerType.COMMUNITY) {
            convert.setParentId(relatedVisitor ==null?visitor.getId(): relatedVisitor.getParentId());
            convert.setOwnerId(visitor.getEnterpriseId());
            convert.setOwnerType(visitorsysOwnerType.ENTERPRISE.getCode());
            convert.setEnterpriseName(visitor.getEnterpriseName());
            if (formValues != null) {
                setVisitorFormValues(convert, formValues.stream().collect(Collectors.toMap(PostApprovalFormItem::getFieldName, x -> x)));
                convert.setFormJsonValue(formValues.toString());
            }
        }else {
            Long communityId = organizationService.getOrganizationActiveCommunityId(visitor.getOwnerId());
            convert.setParentId(relatedVisitor ==null?visitor.getId(): relatedVisitor.getParentId());
            convert.setOwnerId(communityId==null?0L:communityId);
            convert.setOwnerType(visitorsysOwnerType.COMMUNITY.getCode());
            convert.setEnterpriseName(visitor.getEnterpriseName());
            if (formValues != null) {
                setVisitorFormValues(convert, formValues.stream().collect(Collectors.toMap(PostApprovalFormItem::getFieldName, x -> x)));
                convert.setFormJsonValue(formValues.toString());
            }
        }
        return convert;
        //更新子访客/预约
    }

    /**
     * 生成邀请码 RG+项目辨识码+预约日期+四位流水号
     * @param namespaceId
     * @param ownerType
     * @param ownerId
     * @return
     */
    private String generateInvitationNo(Integer namespaceId, String ownerType, Long ownerId) {
        VisitorSysCoding visitorSysCoding = visitorSysCodingProvider.findVisitorSysCodingByOwner(namespaceId,ownerType,ownerId);
        if(visitorSysCoding==null){
            visitorSysCoding = createNewVisitorSysCoding(namespaceId,ownerType,ownerId);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String currentPoint = format.format(System.currentTimeMillis());
        String updatePoint = format.format(visitorSysCoding.getOperateTime());
        if(!currentPoint.equals(updatePoint)){//如果邀请码配置的更新时间（按天）不是今天了，流水码从0开始计算
            visitorSysCoding.setOperateTime(new Timestamp(System.currentTimeMillis()));
            visitorSysCoding.setSerialCode(0);
        }
        visitorSysCoding.setSerialCode(visitorSysCoding.getSerialCode()+1);
        String serialCode = generateSerialCode(visitorSysCoding.getSerialCode()+"");
        String invitationNo = "RG"+visitorSysCoding.getRandomCode()+currentPoint+serialCode;
        visitorSysCodingProvider.updateVisitorSysCoding(visitorSysCoding);
        return invitationNo;
    }

    /**
     * 初始化邀请码配置
     * @param namespaceId
     * @param ownerType
     * @param ownerId
     * @return
     */
    private VisitorSysCoding createNewVisitorSysCoding(Integer namespaceId, String ownerType, Long ownerId) {
        VisitorSysCoding visitorSysCoding = new VisitorSysCoding();
        visitorSysCoding.setNamespaceId(namespaceId);
        visitorSysCoding.setOwnerId(ownerId);
        visitorSysCoding.setOwnerType(ownerType);
        int length = configurationProvider.getIntValue(VisitorsysConstant.VISITORSYS_RANDOMCODE_LENGTH,4);
        String randomCode = VisitorSysUtils.generateLetterNumCode(length);
        while(visitorSysCodingProvider.findVisitorSysCodingByRandomCode(randomCode)!=null){
            randomCode = VisitorSysUtils.generateLetterNumCode(length);
        }
        visitorSysCoding.setRandomCode(randomCode);
        visitorSysCoding.setSerialCode(0);
        visitorSysCoding.setStatus((byte)2);
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor search flag = "+searchFlag);
        }
        return searchFlagType;
    }

    /**
     * 检查flag参数
     * @param flag
     * @return
     */
    private VisitorsysFlagType checkVisitorsysFlag(Byte flag) {
        VisitorsysFlagType flagType = VisitorsysFlagType.fromCode(flag);
        if(flagType ==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor flag = "+flag);
        }
        return flagType;
    }


    /**
     * 生成访客邀请码连接
     * @param id 访客id
     * @return
     */
    private String generateInviationLink(Long id) {
        String invitationLinkTemp = configurationProvider.getValue(VisitorsysConstant.VISITORSYS_INVITATION_LINK,"%s/r?token=%s");
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "invaild token "+token);
        }
        return id;
    }
}
