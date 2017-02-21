package com.everhomes.pmtask;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
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
import org.springframework.transaction.TransactionStatus;

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
    @Autowired
    private CategoryProvider categoryProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private OrganizationProvider organizationProvider;

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

    void sendMessageForCreateTask(List<PmTaskTarget> targets, String requestorName, String requestorPhone,
                                        String taskCategoryName, User user) {
        List<String> phones = new ArrayList<>();

        //消息推送
        String scope = PmTaskNotificationTemplateCode.SCOPE;
        String locale = PmTaskNotificationTemplateCode.LOCALE;
        for(PmTaskTarget p: targets) {
            UserIdentifier sender = userProvider.findClaimedIdentifierByOwnerAndType(p.getTargetId(), IdentifierType.MOBILE.getCode());
            phones.add(sender.getIdentifierToken());
            //消息推送
            Map<String, Object> map = new HashMap<>();
            map.put("creatorName", requestorName);
            map.put("creatorPhone", requestorPhone);
            map.put("categoryName", taskCategoryName);
            int code = PmTaskNotificationTemplateCode.CREATE_PM_TASK;
            String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendMessageToUser(p.getTargetId(), text);
        }
        int num = phones.size();
        if(num > 0) {
            String[] s = new String[num];
            phones.toArray(s);
            List<Tuple<String, Object>> variables = smsProvider.toTupleList("operatorName", requestorName);
            smsProvider.addToTupleList(variables, "operatorPhone", requestorPhone);
            smsProvider.addToTupleList(variables, "categoryName", taskCategoryName);
            smsProvider.sendSms(user.getNamespaceId(), s, SmsTemplateCode.SCOPE,
                    SmsTemplateCode.PM_TASK_CREATOR_CODE, user.getLocale(), variables);
        }
    }

    PmTask createTask(CreateTaskCommand cmd, Long requestorUid, String requestorName, String requestorPhone) {
        if(null == cmd.getAddressType()){
            LOGGER.error("Invalid addressType parameter.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid addressType parameter.");
        }

        User user = UserContext.current().getUser();
        Integer namespaceId = user.getNamespaceId();
        String ownerType = cmd.getOwnerType();
        Long ownerId = cmd.getOwnerId();
        Long taskCategoryId = cmd.getTaskCategoryId();
        Long categoryId = cmd.getCategoryId();
        String content = cmd.getContent();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        checkCreateTaskParam(ownerType, ownerId, taskCategoryId, content);
        Category taskCategory = checkCategory(taskCategoryId);

        final PmTask task = new PmTask();

        //设置门牌地址,楼栋地址,服务地点
        setPmTaskAddressInfo(cmd, task);

        task.setNamespaceId(namespaceId);
        task.setOwnerId(ownerId);
        task.setOwnerType(ownerType);

        task.setTaskCategoryId(taskCategoryId);
        task.setCategoryId(categoryId);
        task.setContent(content);
        task.setStatus(PmTaskStatus.UNPROCESSED.getCode());
        task.setUnprocessedTime(now);
        task.setCreatorUid(user.getId());
        task.setCreateTime(now);
        if(null != cmd.getReserveTime())
            task.setReserveTime(new Timestamp(cmd.getReserveTime()));
        task.setPriority(cmd.getPriority());
        task.setSourceType(cmd.getSourceType() == null ? PmTaskSourceType.APP.getCode() : cmd.getSourceType());

        task.setOrganizationId(cmd.getOrganizationId());
        task.setRequestorName(requestorName);
        task.setRequestorPhone(requestorPhone);

        pmTaskProvider.createTask(task);
        //附件
        addAttachments(cmd.getAttachments(), user.getId(), task.getId(), PmTaskAttachmentType.TASK.getCode());

        PmTaskLog pmTaskLog = new PmTaskLog();
        pmTaskLog.setNamespaceId(task.getNamespaceId());
        pmTaskLog.setOperatorTime(now);
        pmTaskLog.setOperatorUid(requestorUid);
        pmTaskLog.setOwnerId(task.getOwnerId());
        pmTaskLog.setOwnerType(task.getOwnerType());
        pmTaskLog.setStatus(task.getStatus());
        pmTaskLog.setTaskId(task.getId());
        pmTaskProvider.createTaskLog(pmTaskLog);

        //查询园区执行人，发消息
        List<PmTaskTarget> targets = pmTaskProvider.listTaskTargets(cmd.getOwnerType(), cmd.getOwnerId(),
                PmTaskOperateType.EXECUTOR.getCode(), null, null);
        int size = targets.size();
        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Create pmtask and send message, size={}, cmd={}", size, cmd);
        if(size > 0){
            sendMessageForCreateTask(targets, requestorName, requestorPhone, taskCategory.getName(), user);
        }

        return task;
    }

    Category checkCategory(Long id){
        Category category = categoryProvider.findCategoryById(id);
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
}
