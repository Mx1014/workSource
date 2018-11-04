package com.everhomes.pmtask;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.pmtask.*;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
class PmTaskCommonServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmTaskCommonServiceImpl.class);

    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private PmTaskProvider pmTaskProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private SmsProvider smsProvider;
    @Autowired
    private AddressProvider addressProvider;
//    @Autowired
//    private CategoryProvider categoryProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private ConfigurationProvider configProvider;

    List<PmTaskAttachmentDTO> convertAttachmentDTO(List<PmTaskAttachment> attachments) {
        return attachments.stream().map(r -> {
            PmTaskAttachmentDTO dto = ConvertHelper.convert(r, PmTaskAttachmentDTO.class);
            String contentUrl = getResourceUrlByUir(r.getContentUri(),
                    EntityType.USER.getCode(), r.getCreatorUid());
            dto.setContentUrl(contentUrl);
            return dto;
        }).collect(Collectors.toList());

    }

    private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
        String url = null;
        if(uri != null && uri.length() > 0) {
            try{
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            }catch(Exception e){
                LOGGER.error("Failed to parse uri, uri=, ownerType=, ownerId=", uri, ownerType, ownerId, e);
            }
        }

        return url;
    }
    //发消息
    void sendMessageToUser(Long userId, String content) {

        MessageDTO message = new MessageDTO();
        message.setAppId(AppConstants.APPID_MESSAGING);
        message.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        message.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()),
                new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_MESSAGING);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), message, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    void addAttachments(List<AttachmentDescriptor> list, Long userId, Long ownerId, String targetType){
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(ad -> {
                if(null != ad){
                    PmTaskAttachment attachment = new PmTaskAttachment();
                    attachment.setContentType(ad.getContentType());
                    attachment.setContentUri(ad.getContentUri());
                    attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    attachment.setCreatorUid(userId);
                    attachment.setOwnerId(ownerId);
                    attachment.setOwnerType(targetType);
                    pmTaskProvider.createTaskAttachment(attachment);
                }
            });
        }
    }

    /**
     * 注册用户申请报修时，填充昵称和手机号
     */
//    void setPmTaskRequestorInfo(PmTaskDTO dto) {
//        if(null == dto.getOrganizationId() || dto.getOrganizationId() ==0 ){
//            User user = userProvider.findUserById(dto.getCreatorUid());
//            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
//            dto.setRequestorName(user.getNickName());
//            dto.setRequestorPhone(userIdentifier.getIdentifierToken());
//        }
//    }

    void setPmTaskAddressInfo(CreateTaskCommand cmd, final PmTask task) {

        //先设置传入的值，如果门牌不为空，则取门牌楼栋地址
        task.setBuildingName(cmd.getBuildingName());
        task.setAddress(cmd.getAddress());
        if(cmd.getAddressType().equals(PmTaskAddressType.ORGANIZATION.getCode())) {
            if(null == cmd.getAddressOrgId()){
                LOGGER.error("Invalid addressOrgId parameter.");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "Invalid addressOrgId parameter.");
            }
            task.setAddressOrgId(cmd.getAddressOrgId());

            Organization organization = organizationProvider.findOrganizationById(task.getAddressOrgId());
            Address address = addressProvider.findAddressById(cmd.getAddressId());
            String addr = "";
            if(null != organization)
                addr = organization.getName();
            if(null != address)
                addr = addr + address.getAddress();
            task.setAddress(addr);
        }

        if (null != cmd.getAddressId()) {
            Address address = addressProvider.findAddressById(cmd.getAddressId());
            if (null != address) {
                task.setBuildingName(address.getBuildingName());

                if(cmd.getAddressType().equals(PmTaskAddressType.FAMILY.getCode())) {
                    Community community = communityProvider.findCommunityById(address.getCommunityId());
                    task.setAddress(address.getCityName() + address.getAreaName() + community.getName() + address.getAddress());
                }
            }
        }
        task.setAddressType(cmd.getAddressType());
        task.setAddressId(cmd.getAddressId());

    }

    PmTask createTask(CreateTaskCommand cmd, Long requestorUid, String requestorName, String requestorPhone) {
        if(null == cmd.getAddressType()){
            LOGGER.error("Invalid addressType parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid addressType parameter.");
        }

        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        String ownerType = cmd.getOwnerType();
        Long ownerId = cmd.getOwnerId();
        Long taskCategoryId = cmd.getTaskCategoryId();
        Long categoryId = cmd.getCategoryId();
        String content = cmd.getContent();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        checkCreateTaskParam(ownerType, ownerId, taskCategoryId, content);
        PmTaskCategory taskCategory = checkCategory(taskCategoryId);

        final PmTask task = new PmTask();

        task.setNamespaceId(namespaceId);
        task.setOwnerId(ownerId);
        task.setOwnerType(ownerType);

        task.setTaskCategoryId(taskCategoryId);
        task.setCategoryId(categoryId);
        task.setContent(content);
        task.setUnprocessedTime(now);
        task.setCreatorUid(user.getId());
        task.setCreateTime(now);
        //代发，设置创建者为被代发的人（如果是注册用户）userId
        if (null != cmd.getOrganizationId()) {
            if (null!=requestorUid)
              task.setCreatorUid(requestorUid);
            task.setOrganizationUid(user.getId());
        }
        if(null != cmd.getReserveTime())
            task.setReserveTime(new Timestamp(cmd.getReserveTime()));
        task.setPriority(cmd.getPriority());
        task.setSourceType(cmd.getSourceType() == null ? PmTaskSourceType.APP.getCode() : cmd.getSourceType());

        task.setOrganizationId(cmd.getOrganizationId());
        task.setRequestorName(requestorName);
        task.setRequestorPhone(requestorPhone);
        task.setOrganizationName(cmd.getOrganizationName());
        task.setIfUseFeelist((byte)0);
        task.setReferType(cmd.getReferType());
        task.setReferId(cmd.getReferId());

//      新增需求人企业Id用于物业线根据企业查询报修任务
        task.setEnterpriseId(cmd.getEnterpriseId());

        //设置门牌地址,楼栋地址,服务地点

//        String handle = configProvider.getValue(PmTaskServiceImpl.HANDLER + namespaceId, PmTaskHandle.FLOW);
//        if(PmTaskHandle.YUE_KONG_JIAN.equals(handle)){
//            task.setAddress(cmd.getAddress());
//            task.setAddressType(cmd.getAddressType());
//        }
//        else {
            setPmTaskAddressInfo(cmd, task);
//        }
//      多应用标识
        task.setAppId(cmd.getAppId());
        pmTaskProvider.createTask(task);
        //附件
        addAttachments(cmd.getAttachments(), user.getId(), task.getId(), PmTaskAttachmentType.TASK.getCode());

        return task;
    }

    PmTaskCategory checkCategory(Long id){
        PmTaskCategory category = pmTaskProvider.findCategoryById(id);
        if(null == category) {
            LOGGER.error("Category not found, categoryId={}", id);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Category not found.");
        }
        return category;
    }

    void checkCreateTaskParam(String ownerType, Long ownerId, Long taskCategoryId, String content){
        checkOwnerIdAndOwnerType(ownerType, ownerId);
        if(null == taskCategoryId) {
            LOGGER.error("Invalid taskCategoryId parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid taskCategoryId parameter.");
        }

        if(StringUtils.isBlank(content)) {
            LOGGER.error("Invalid content parameter.");
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CONTENT_NULL,
                    "Invalid content parameter.");
        }

    }

    void checkOwnerIdAndOwnerType(String ownerType, Long ownerId){
        if(null == ownerId) {
            LOGGER.error("Invalid ownerId parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid ownerId parameter.");
        }

        if(StringUtils.isBlank(ownerType)) {
            LOGGER.error("Invalid ownerType parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid ownerType parameter.");
        }
    }

    Byte convertFlowStatus(String nodeType) {

//        if(StringUtils.isBlank(params)) {
//            LOGGER.error("Invalid flowNode param.");
//            throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
//                    "Invalid flowNode param.");
//        }
//
//        JSONObject paramJson = JSONObject.parseObject(params);
//        String nodeType = paramJson.getString("nodeType");

        switch (nodeType) {
            case "ACCEPTING": return PmTaskFlowStatus.ACCEPTING.getCode();
            case "ASSIGNING": return PmTaskFlowStatus.ASSIGNING.getCode();
            case "PROCESSING": return PmTaskFlowStatus.PROCESSING.getCode();
            case "COMPLETED": return PmTaskFlowStatus.COMPLETED.getCode();
            case "HANDOVER": return PmTaskFlowStatus.PROCESSING.getCode();
            case "FLOWCOMPLETED": return PmTaskFlowStatus.COMPLETED.getCode();
            case "CONFIRMFEE": return  PmTaskFlowStatus.CONFIRM.getCode();
            case "MOTIFYFEE":return PmTaskFlowStatus.MOTIFY.getCode();
            default: return null;
        }
    }

    PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd, Boolean flag) {

        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
        checkId(cmd.getId());
        PmTask task = checkPmTask(cmd.getId());

        PmTaskDTO dto  = ConvertHelper.convert(task, PmTaskDTO.class);

        //查询服务类型
        PmTaskCategory category = pmTaskProvider.findCategoryById(task.getCategoryId());
        PmTaskCategory taskCategory = checkCategory(task.getTaskCategoryId());
        if(null != category)
            dto.setCategoryName(category.getName());
        dto.setTaskCategoryName(taskCategory.getName());

        //查询图片
        List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(task.getId(), PmTaskAttachmentType.TASK.getCode());
        List<PmTaskAttachmentDTO> attachmentDtos = convertAttachmentDTO(attachments);
        dto.setAttachments(attachmentDtos);

        if (flag) {
            //查询task log
//            List<PmTaskLogDTO> taskLogDtos = listPmTaskLogs(dto);
//            dto.setTaskLogs(taskLogDtos);
        }

        return dto;
    }

//    private List<PmTaskLogDTO> listPmTaskLogs(PmTaskDTO task) {
//
//        List<PmTaskLog> taskLogs = pmTaskProvider.listPmTaskLogs(task.getId(), null);
//        return taskLogs.stream().map(r -> {
//
//            PmTaskLogDTO pmTaskLogDTO = ConvertHelper.convert(r, PmTaskLogDTO.class);
//
//            Map<String, Object> map = new HashMap<>();
//
//            String scope = PmTaskNotificationTemplateCode.SCOPE;
//            String locale = PmTaskNotificationTemplateCode.LOCALE;
//
//            if(r.getStatus().equals(PmTaskStatus.UNPROCESSED.getCode())){
//
//                if(null == task.getOrganizationId() || task.getOrganizationId() == 0){
//                    setParam(map, task.getCreatorUid(), pmTaskLogDTO);
//                }else{
//                    map.put("operatorName", task.getRequestorName());
//                    map.put("operatorPhone", task.getRequestorPhone());
//                }
//
//                int code = PmTaskNotificationTemplateCode.UNPROCESS_TASK_LOG;
//                String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//                pmTaskLogDTO.setText(text);
//
//            }else if(r.getStatus().equals(PmTaskStatus.PROCESSING.getCode())){
//                setParam(map, r.getOperatorUid(), pmTaskLogDTO);
//                User target = userProvider.findUserById(r.getTargetId());
//                UserIdentifier targetIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(target.getId(), IdentifierType.MOBILE.getCode());
//                map.put("targetName", target.getNickName());
//                map.put("targetPhone", targetIdentifier.getIdentifierToken());
//
//                int code = PmTaskNotificationTemplateCode.PROCESSING_TASK_LOG;
//                String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//                pmTaskLogDTO.setText(text);
//
//            }else if(r.getStatus().equals(PmTaskStatus.PROCESSED.getCode())){
//                setParam(map, r.getOperatorUid(), pmTaskLogDTO);
//                int code = PmTaskNotificationTemplateCode.PROCESSED_TASK_LOG;
//                String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//                pmTaskLogDTO.setText(text);
//                List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(r.getId(), PmTaskAttachmentType.TASKLOG.getCode());
//                List<PmTaskAttachmentDTO> attachmentDtos = convertAttachmentDTO(attachments);
//                pmTaskLogDTO.setAttachments(attachmentDtos);
//
//            }else if(r.getStatus().equals(PmTaskStatus.CLOSED.getCode())){
//                setParam(map, r.getOperatorUid(), pmTaskLogDTO);
//                int code = PmTaskNotificationTemplateCode.CLOSED_TASK_LOG;
//                String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//                pmTaskLogDTO.setText(text);
//            }else {
//                setParam(map, r.getOperatorUid(), pmTaskLogDTO);
//                int code = PmTaskNotificationTemplateCode.REVISITED_TASK_LOG;
//                String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//                pmTaskLogDTO.setText(text);
//            }
//
//            return pmTaskLogDTO;
//        }).collect(Collectors.toList());
//    }

//    private void setParam(Map<String, Object> map, Long userId, PmTaskLogDTO dto) {
//        User user = userProvider.findUserById(userId);
//        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
//        map.put("operatorName", user.getNickName());
//        map.put("operatorPhone", userIdentifier.getIdentifierToken());
//        dto.setOperatorName(user.getNickName());
//        dto.setOperatorPhone(userIdentifier.getIdentifierToken());
//    }

    void checkId(Long id){
        if(null == id) {
            LOGGER.error("Invalid id parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid id parameter.");
        }
    }

    PmTask checkPmTask(Long id){
        PmTask pmTask = pmTaskProvider.findTaskById(id);
        if(null == pmTask) {
            LOGGER.error("PmTask not found, id={}", id);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "PmTask not found.");
        }
        return pmTask;
    }

    void handoverTaskToTrd(PmTask task, String content, List<String> attachments) {
        String handlerPrefix = HandoverTaskHandler.HANDOVER_VENDOR_PREFIX;
        HandoverTaskHandler handler = PlatformContext.getComponent(handlerPrefix + task.getNamespaceId());
        if(handler != null)
            handler.handoverTaskToTrd(task, content, attachments);
    }

}
