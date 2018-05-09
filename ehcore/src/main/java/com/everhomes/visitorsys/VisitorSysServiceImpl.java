// @formatter:off
package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/27 15:18
 */
@Component
public class VisitorSysServiceImpl implements VisitorSysService{
    /**
     * 检查动态字段必填情况
     */
//    private static final String[] checkField = {"invalidTime","plateNo","idNumber",
//            "visitFloor","visitAddresses"};
    /**
     * 检查必填参数
     */
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
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        if(visitor==null)
            return null;
        GetBookedVisitorByIdResponse response = ConvertHelper.convert(visitor, GetBookedVisitorByIdResponse.class);
        response.setVisitorPicUrl(contentServerService.parserUri(visitor.getVisitorPicUri()));
        response.setVisitorSignUrl(contentServerService.parserUri(visitor.getVisitorSignUri()));
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(visitor.getOwnerType());
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            response.setCommunityFormValues(JSONObject.parseObject(visitor.getFormJsonValue(),
                    new TypeReference<List<PostApprovalFormItem>>() {}));
        }else{
            response.setEnterpriseFormValues(JSONObject.parseObject(visitor.getFormJsonValue(),
                    new TypeReference<List<PostApprovalFormItem>>() {}));
        }

        return response;
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
        //检查参数，生成访客实体
        VisitorSysVisitor visitor = checkCreateOrUpdateVisitorCommand(cmd);
        if(cmd.getId()==null) {
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
            checkUpdateVisitor(visitor);
            dbProvider.execute(r->updateVisitorSysVisitor(visitor,cmd));
        }
        GetBookedVisitorByIdResponse convert = ConvertHelper.convert(visitor, GetBookedVisitorByIdResponse.class);
        convert.setVisitorPicUrl(contentServerService.parserUri(convert.getVisitorPicUri()));
        convert.setVisitorSignUrl(contentServerService.parserUri(convert.getVisitorSignUri()));
        return convert;
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
        VisitorSysVisitor subVisitor = null;
        if (visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            subVisitor = generateSubVisitor(visitor,cmd.getEnterpriseFormValues());
        }else {
            subVisitor = generateSubVisitor(visitor,cmd.getCommunityFormValues());
        }
        visitorSysVisitorProvider.createVisitorSysVisitor(subVisitor);
        visitorsysSearcher.syncVisitor(subVisitor);
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
        VisitorSysVisitor subVisitor = null;
        if (visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            subVisitor = generateSubVisitor(visitor,cmd.getEnterpriseFormValues());
        }else {
            subVisitor = generateSubVisitor(visitor,cmd.getCommunityFormValues());
        }
        visitorSysVisitorProvider.updateVisitorSysVisitor(subVisitor);
        visitorsysSearcher.syncVisitor(subVisitor);
        return null;
    }

    @Override
    public void sendVisitorSMS(GetBookedVisitorByIdCommand cmd) {
        checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(cmd.getNamespaceId(), cmd.getVisitorId());
        VisitorsysVisitorType visitorType = checkVisitorType(visitor.getVisitorType());
        if(visitorType==VisitorsysVisitorType.TEMPORARY){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "temporary visitor not support send sms");
        }
        VisitorsysVisitStatus status = checkStatus(visitor.getVisitStatus());
        if(VisitorsysVisitStatus.NOT_VISIT!=status){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "not support visitor status = "+visitor.getVisitStatus());
        }
        if(visitor.getVisitorPhone()==null){
            return;
        }

        List<Tuple<String, Object>> variables =  new ArrayList();
        smsProvider.addToTupleList(variables, VisitorsysErrorCode.SMS_APPNAME, "");
        smsProvider.addToTupleList(variables, VisitorsysErrorCode.SMS_VISITENTERPRISENAME, visitor.getEnterpriseName());
        smsProvider.addToTupleList(variables, VisitorsysErrorCode.SMS_INVITATIONLINK, generateInviationLink(visitor.getId()));
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
            if(visitor.getParentId()==0){
                VisitorSysVisitor subvisitor = visitorSysVisitorProvider.findVisitorSysVisitorByParentId(cmd.getNamespaceId(), visitor.getId());
                visitorSysVisitorProvider.deleteVisitorSysVisitor(subvisitor.getNamespaceId(),subvisitor.getId());
                visitorsysSearcher.syncVisitor(subvisitor.getNamespaceId(),subvisitor.getId());
            }else{
                visitorSysVisitorProvider.deleteVisitorSysVisitor(visitor.getNamespaceId(),visitor.getParentId());
                visitorsysSearcher.syncVisitor(visitor.getNamespaceId(),visitor.getParentId());
            }
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
            return null;
        });
    }

    @Override
    public void confirmVisitor(CreateOrUpdateVisitorCommand cmd) {

    }

    @Override
    public GetStatisticsResponse getStatistics(GetStatisticsCommand cmd) {
        return null;
    }

    @Override
    public AddDeviceResponse addDevice(AddDeviceCommand cmd) {
        return null;
    }

    @Override
    public ListDevicesResponse listDevices(BaseVisitorsysCommand cmd) {
        return null;
    }

    @Override
    public void deleteDevice(DeleteDeviceCommand cmd) {

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

    }

    @Override
    public GetConfigurationResponse getConfiguration(BaseVisitorsysCommand cmd) {
        return null;
    }

    @Override
    public GetConfigurationResponse updateConfiguration(UpdateConfigurationCommand cmd) {
        return null;
    }

    @Override
    public ListBlackListsResponse listBlackLists(ListBlackListsCommand cmd) {
        return null;
    }

    @Override
    public void deleteBlackList(DeleteBlackListCommand cmd) {

    }

    @Override
    public CreateOrUpdateBlackListResponse createOrUpdateBlackList(CreateOrUpdateBlackListCommand cmd) {
        return null;
    }

    @Override
    public ListDoorGuardsResponse listDoorGuards(BaseVisitorsysCommand cmd) {
        return null;
    }

    @Override
    public void rejectVisitor(GetBookedVisitorByIdCommand cmd) {

    }

    @Override
    public void syncVisitor(BaseVisitorsysCommand cmd) {
        visitorsysSearcher.syncVisitorsFromDb(cmd.getNamespaceId());
    }

    @Override
    public GetInvitationLetterForWebResponse getInvitationLetterForWeb(GetInvitationLetterForWebCommand cmd) {
        return null;
    }

    @Override
    public GetPairingCodeResponse getPairingCode(GetPairingCodeCommand cmd) {
        return null;
    }

    @Override
    public DeferredResult<RestResponse> confirmPairingCode(ConfirmPairingCodeCommand cmd) {
        return null;
    }

    @Override
    public GetConfigurationResponse getUIConfiguration(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public CreateOrUpdateVisitorUIResponse createOrUpdateUIVisitor(CreateOrUpdateVisitorUICommand cmd) {
        return null;
    }

    @Override
    public ListUIOfficeLocationsResponse listUIOfficeLocations(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public ListUICommunityOrganizationsResponse listUICommunityOrganizations(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public ListUIVisitReasonsResponse listUIVisitReasons(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public void sendSMSVerificationCode(SendSMSVerificationCodeCommand cmd) {

    }

    @Override
    public void confirmVerificationCode(ConfirmVerificationCodeCommand cmd) {

    }

    @Override
    public GetHomePageConfigurationResponse getHomePageConfiguration() {
        return null;
    }

    @Override
    public GetEnterpriseFormResponse getEnterpriseForm(GetEnterpriseFormCommand cmd) {
        return null;
    }

    @Override
    public GetEnterpriseFormForWebResponse getEnterpriseFormForWeb(GetEnterpriseFormForWebCommand cmd) {
        return null;
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
    private VisitorSysVisitor checkCreateOrUpdateVisitorCommand(CreateOrUpdateVisitorCommand cmd) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(cmd.getOwnerType());
        //检查园区表单
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY) {
            checkFormConfiguration(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityFormValues());
        }
        //检查公司表单
        checkFormConfiguration(cmd.getNamespaceId(),VisitorsysOwnerType.ENTERPRISE.getCode(),cmd.getEnterpriseId(),cmd.getEnterpriseFormValues());
        VisitorsysVisitorType visitorsysVisitorType = checkVisitorType(cmd.getVisitorType());
        VisitorsysVisitStatus visitStatus = checkInvaildStatus(cmd.getVisitStatus());
        checkInvaildStatus(cmd.getBookingStatus());
        for (String s : checkMustFillField) {
            checkMustFillParams(cmd,s);
        }
        //如果是预约用户，那么就必须传计划到访时间
        if(visitorsysVisitorType==VisitorsysVisitorType.BE_INVITED){
            checkMustFillParams(cmd,"plannedVisitTime");
        }

        VisitorSysVisitor convert = ConvertHelper.convert(cmd, VisitorSysVisitor.class);
        convert.setParentId(0L);
        //访客状态是等待确认，那么必须设置到访时间
        if(visitStatus==VisitorsysVisitStatus.WAIT_CONFIRM_VISIT){
            convert.setVisitTime(new Timestamp(System.currentTimeMillis()));
        }

        Map<String, PostApprovalFormItem> map =null;
        if(visitorsysOwnerType == VisitorsysOwnerType.COMMUNITY && cmd.getCommunityFormValues()!=null) {
            map = cmd.getCommunityFormValues().stream().collect(Collectors.toMap(PostApprovalFormItem::getFieldName, x -> x));
            convert.setFormJsonValue(cmd.getCommunityFormValues().toString());
        }else if(cmd.getEnterpriseFormValues()!=null){
            map = cmd.getEnterpriseFormValues().stream().collect(Collectors.toMap(PostApprovalFormItem::getFieldName, x -> x));
            convert.setFormJsonValue(cmd.getEnterpriseFormValues().toString());
        }
        if(map!=null) {
            setVisitorFormValues(convert, map);
        }
        return convert;

    }

    /**
     * 设置表单五个字段的值
     * @param convert
     * @param map
     */
    private void setVisitorFormValues(VisitorSysVisitor convert, Map<String, PostApprovalFormItem> map) {
        convert.setInvalidTime(new Timestamp(Long.valueOf(map.get("invalidTime").getFieldValue())));
        convert.setPlateNo(map.get("plateNo").getFieldValue());
        convert.setIdNumber(map.get("idNumber").getFieldValue());
        convert.setVisitFloor(map.get("visitFloor").getFieldValue());
        convert.setVisitAddresses(map.get("visitAddresses").getFieldValue());
    }

    /**
     * 必填参数检查是否非空
     * @param cmd
     * @param paramName
     */
    private void checkMustFillParams(CreateOrUpdateVisitorCommand cmd, String paramName) {
        String methodName = "get"+ paramName.substring(0,1).toUpperCase()+paramName.substring(1,paramName.length());
        Method method = null;
        try {
            method = cmd.getClass().getMethod(methodName);
            Object result = method.invoke(cmd);
            if(result==null){
                throw RuntimeErrorException.errorWith(VisitorsysErrorCode.SCOPE
                        , VisitorsysErrorCode.ERROR_MUST_FILL, paramName+" should not be null");
            }
        } catch (NoSuchMethodException e) {
            throw RuntimeErrorException.errorWith(VisitorsysErrorCode.SCOPE
                    , VisitorsysErrorCode.ERROR_MUST_FILL, "",e);
        } catch (IllegalAccessException e) {
            throw RuntimeErrorException.errorWith(VisitorsysErrorCode.SCOPE
                    , VisitorsysErrorCode.ERROR_MUST_FILL, "",e);
        } catch (InvocationTargetException e) {
            throw RuntimeErrorException.errorWith(VisitorsysErrorCode.SCOPE
                    , VisitorsysErrorCode.ERROR_MUST_FILL, "",e);
        }
    }

    /**
     * 检查预约状态不是删除状态
     * @param visitStatus
     */
    private VisitorsysVisitStatus checkInvaildStatus(Byte visitStatus) {
        VisitorsysVisitStatus status = checkStatus(visitStatus);
        //以下两种状态，不能在创建用户的时候传入
        if(status == VisitorsysVisitStatus.DELETED
                || status == VisitorsysVisitStatus.REJECTED_VISIT){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "invaild visitor status = "+visitStatus);
        }
        return status;
    }

    /**
     * 检查预约状态枚举
     * @param visitStatus
     * @return
     */
    private VisitorsysVisitStatus checkStatus(Byte visitStatus) {
        if(visitStatus==null){
            return null;
        }
        VisitorsysVisitStatus status = VisitorsysVisitStatus.fromCode(visitStatus);
        if(status==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor status = "+visitStatus);
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
     * @param namespaceId
     * @param ownerType
     * @param ownerId
     * @param formValues
     * @return
     */
    private boolean checkFormConfiguration(Integer namespaceId, String ownerType, Long ownerId, List<PostApprovalFormItem> formValues) {
        VisitorSysConfiguration owner = visitorSysConfigurationProvider.findVisitorSysConfigurationByOwner(namespaceId, ownerType, ownerId);
        if(owner==null || owner.getConfigFormJson()==null){
            return false;
        }
        if(formValues==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "form value is null");
        }
        Map<String, PostApprovalFormItem> map = formValues.stream().collect(Collectors.toMap(PostApprovalFormItem::getFieldName, x -> x));
        JSONArray jsonArray = JSONObject.parseArray( owner.getConfigFormJson());
        for (Object o : jsonArray) {
            GeneralFormFieldDTO dto = JSONObject.parseObject(o.toString(),GeneralFormFieldDTO.class);
            if(dto.getRequiredFlag() == 1 && map.get(dto.getFieldName())==null){
                throw RuntimeErrorException.errorWith(VisitorsysErrorCode.SCOPE
                        , VisitorsysErrorCode.ERROR_MUST_FILL, dto.getFieldDisplayName()+" should not be null");
            }

        }
        return true;
    }

    /**
     * 更新visitor检查，包括域空间,owner,访客类型等，不能被更新
     * @param visitor
     */
    private void checkUpdateVisitor(VisitorSysVisitor visitor) {
        VisitorSysVisitor oldVisitor = visitorSysVisitorProvider.findVisitorSysVisitorById(visitor.getNamespaceId(),visitor.getId());
        if(oldVisitor==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor id = "+visitor.getId()+", namespaceId = "+visitor.getNamespaceId()+"");
        }
        if(oldVisitor.getOwnerId().longValue()!=visitor.getOwnerId()
                || !oldVisitor.getOwnerType().equals(visitor.getOwnerType())
                || oldVisitor.getVisitorType().byteValue()!=visitor.getVisitorType().byteValue()){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor "+visitor);
        }
        visitor.setCreateTime(oldVisitor.getCreateTime());
        visitor.setCreatorUid(oldVisitor.getCreatorUid());
        visitor.setInvitationNo(oldVisitor.getInvitationNo());
    }

    /**
     * 根据园区访客，生成企业访客实体
     * @param parentVisitor
     * @param formValues
     * @return
     */
    private VisitorSysVisitor generateSubVisitor(VisitorSysVisitor parentVisitor, List<PostApprovalFormItem> formValues) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(parentVisitor.getOwnerType());
//        if(visitorsysOwnerType == visitorsysOwnerType.ENTERPRISE){
//            return null;
//        }
        checkOwner(visitorsysOwnerType.ENTERPRISE.getCode(),parentVisitor.getEnterpriseId());
        VisitorSysVisitor sonVisitor = visitorSysVisitorProvider.findVisitorSysVisitorByParentId(parentVisitor.getNamespaceId(),parentVisitor.getId());
        VisitorSysVisitor convert = ConvertHelper.convert(parentVisitor, VisitorSysVisitor.class);
        //新建访客/预约
        if(sonVisitor!=null){
            convert.setId(sonVisitor.getId());
        }
        if(visitorsysOwnerType == visitorsysOwnerType.COMMUNITY) {
            convert.setParentId(parentVisitor.getId());
            convert.setOwnerId(parentVisitor.getEnterpriseId());
            convert.setOwnerType(visitorsysOwnerType.ENTERPRISE.getCode());
            convert.setEnterpriseName(parentVisitor.getEnterpriseName());
            if (formValues != null) {
                setVisitorFormValues(convert, formValues.stream().collect(Collectors.toMap(PostApprovalFormItem::getFieldName, x -> x)));
                convert.setFormJsonValue(formValues.toString());
            }
        }else {
            Long communityId = organizationService.getOrganizationActiveCommunityId(parentVisitor.getOwnerId());
            convert.setParentId(parentVisitor.getId());
            convert.setOwnerId(communityId==null?0L:communityId);
            convert.setOwnerType(visitorsysOwnerType.COMMUNITY.getCode());
            convert.setEnterpriseName(parentVisitor.getEnterpriseName());
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
        String randomCode = generateRandomCode();
        while(visitorSysCodingProvider.findVisitorSysCodingByRandomCode(randomCode)!=null){
            randomCode = generateRandomCode();
        }
        visitorSysCoding.setRandomCode(randomCode);
        visitorSysCoding.setSerialCode(0);
        visitorSysCoding.setStatus((byte)2);
        visitorSysCodingProvider.createVisitorSysCoding(visitorSysCoding);
        return visitorSysCoding;
    }

    /**
     * 生成固定位数的项目辨识码
     * @return
     */
    private String generateRandomCode() {
        int length = configurationProvider.getIntValue(VisitorsysErrorCode.VISITORSYS_RANDOMCODE_LENGTH,4);
//        int length = 4;
        String randomCode = "";
        while(randomCode.length()<length){
            int i = (int) ((Math.random() * 36 + 65));
//            System.out.print(i+" ");
            if(i<91){
                randomCode+=(char)i;
            }else{
                randomCode+=i-91;
            }
        }
        return randomCode;
    }

    /**
     * 将serialCode 转换为4位流水码
     * @param serialCode
     * @return
     */
    private String generateSerialCode(String serialCode) {
        if(serialCode==null)
            return null;
        int length = configurationProvider.getIntValue(VisitorsysErrorCode.VISITORSYS_SERIALCODE_LENGTH,4);
        while(serialCode.length()<length){
            serialCode="0"+serialCode;
        }
        while(serialCode.length()>length){
            serialCode=serialCode.substring(1,serialCode.length());
        }
        return serialCode;
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
     * 生成访客邀请码连接
     * @param id 访客id
     * @return
     */
    private String generateInviationLink(Long id) {
        String invitationLinkTemp = configurationProvider.getValue(VisitorsysErrorCode.VISITORSYS_INVITATION_LINK,"%s/r?token=%s");
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


//    public static void main(String[] args) {
//        for (int i = 0; i <10000; i++) {
//            System.out.println(new VisitorSysServiceImpl().generateRandomCode());
//        }
//    }
}
