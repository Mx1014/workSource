package com.everhomes.notice;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.filemanagement.FileService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.notice.CancelEnterpriseNoticeCommand;
import com.everhomes.rest.notice.CreateEnterpriseNoticeCommand;
import com.everhomes.rest.notice.DeleteEnterpriseNoticeCommand;
import com.everhomes.rest.notice.EnterpriseNoticeAttachmentContentType;
import com.everhomes.rest.notice.EnterpriseNoticeAttachmentDTO;
import com.everhomes.rest.notice.EnterpriseNoticeContentType;
import com.everhomes.rest.notice.EnterpriseNoticeDTO;
import com.everhomes.rest.notice.EnterpriseNoticeDetailActionData;
import com.everhomes.rest.notice.EnterpriseNoticeErrorCode;
import com.everhomes.rest.notice.EnterpriseNoticePreviewDTO;
import com.everhomes.rest.notice.EnterpriseNoticeReceiverDTO;
import com.everhomes.rest.notice.EnterpriseNoticeReceiverType;
import com.everhomes.rest.notice.EnterpriseNoticeSecretFlag;
import com.everhomes.rest.notice.EnterpriseNoticeShowType;
import com.everhomes.rest.notice.EnterpriseNoticeStatus;
import com.everhomes.rest.notice.EnterpriseNoticeStickFlag;
import com.everhomes.rest.notice.GetCurrentUserContactInfoCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeAdminCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeAdminResponse;
import com.everhomes.rest.notice.ListEnterpriseNoticeCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeResponse;
import com.everhomes.rest.notice.StickyEnterpriseNoticeCommand;
import com.everhomes.rest.notice.UnStickyEnterpriseNoticeCommand;
import com.everhomes.rest.notice.UpdateEnterpriseNoticeCommand;
import com.everhomes.rest.notice.UserContactSimpleInfoDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EnterpriseNoticeServiceImpl implements EnterpriseNoticeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseNoticeServiceImpl.class);

    private static final int TITLE_MAX_LENGTH = 40;
    private static final int CONTENT_MAX_LENGTH = 2000;
    private static final int PUBLISHER_MAX_LENGTH = 40;
    private static final int SUMMARY_CONTENT_MAX_SIZE = 140;

    @Autowired
    private EnterpriseNoticeProvider enterpriseNoticeProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private FileService fileService;

    @Autowired
    private OrganizationService organizationService;

    private ExecutorService bgThreadPool = Executors.newFixedThreadPool(1);


    @Override
    public EnterpriseNoticeDTO getEnterpriseNoticeDetailInfo(Long enterpriseNoticeId) {
        EnterpriseNotice enterpriseNotice = enterpriseNoticeProvider.findById(enterpriseNoticeId);
        if (enterpriseNotice == null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("EnterpriseNotice not found , id = {}", enterpriseNoticeId);
            }
            return null;
        }
        EnterpriseNoticeDTO enterpriseNoticeDTO = ConvertHelper.convert(enterpriseNotice, EnterpriseNoticeDTO.class);
        List<EnterpriseNoticeAttachment> attachments = enterpriseNoticeProvider.findEnterpriseNoticeAttachmentsByNoticeId(enterpriseNoticeId);
        List<EnterpriseNoticeReceiver> receivers = enterpriseNoticeProvider.findEnterpriseNoticeReceiversByNoticeId(enterpriseNoticeId);
        if (!CollectionUtils.isEmpty(attachments)) {
            Map<String, String> fileIconUrlMap = fileService.getFileIconUrl();
            List<EnterpriseNoticeAttachmentDTO> enterpriseNoticeAttachmentDTOS = new ArrayList<>(attachments.size());
            attachments.forEach(enterpriseNoticeAttachment -> {
                EnterpriseNoticeAttachmentDTO enterpriseNoticeAttachmentDTO = ConvertHelper.convert(enterpriseNoticeAttachment, EnterpriseNoticeAttachmentDTO.class);
                enterpriseNoticeAttachmentDTO.setName(enterpriseNoticeAttachmentDTO.getContentName() + "." + enterpriseNoticeAttachmentDTO.getContentSuffix());
                String contentUrl = getResourceUrlByUir(enterpriseNoticeAttachment.getContentUri(),
                        EntityType.USER.getCode(), enterpriseNoticeAttachment.getCreatorUid());
                enterpriseNoticeAttachmentDTO.setContentUrl(contentUrl);
                enterpriseNoticeAttachmentDTO.setIconUrl(fileIconUrlMap.get(enterpriseNoticeAttachment.getContentSuffix()));
                enterpriseNoticeAttachmentDTOS.add(enterpriseNoticeAttachmentDTO);
            });
            enterpriseNoticeDTO.setAttachments(enterpriseNoticeAttachmentDTOS);
        }
        if (!CollectionUtils.isEmpty(receivers)) {
            List<EnterpriseNoticeReceiverDTO> enterpriseNoticeReceiverDTOS = new ArrayList<>(receivers.size());
            receivers.forEach(receiver -> {
                enterpriseNoticeReceiverDTOS.add(ConvertHelper.convert(receiver, EnterpriseNoticeReceiverDTO.class));
            });
            enterpriseNoticeDTO.setReceivers(enterpriseNoticeReceiverDTOS);
        }
        if (EnterpriseNoticeSecretFlag.PRIVATE != EnterpriseNoticeSecretFlag.fromCode(enterpriseNotice.getSecretFlag())) {
            enterpriseNoticeDTO.setWebShareUrl(getNoticeWebShareUrl(enterpriseNoticeId));
        }
        return enterpriseNoticeDTO;
    }

    private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
        String url = null;
        if (null != uri && uri.length() > 0) {
            try {
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            } catch (Exception e) {
                LOGGER.error("Failed to parse uri, uri=, ownerType=, ownerId=", uri, ownerType, ownerId, e);
            }
        }

        return url;
    }

    @Override
    public EnterpriseNoticePreviewDTO createEnterpriseNotice(CreateEnterpriseNoticeCommand cmd) {
        if (EnterpriseNoticeStatus.fromCode(cmd.getStatus()) == null) {
            cmd.setStatus(EnterpriseNoticeStatus.DRAFT.getCode());
        }
        if (EnterpriseNoticeContentType.fromCode(cmd.getContentType()) == null) {
            cmd.setContentType(EnterpriseNoticeContentType.TEXT.getCode());
        }
        cmd.setTitle(splitLongString(cmd.getTitle(), TITLE_MAX_LENGTH, true));
        cmd.setContent(splitLongString(cmd.getContent(), CONTENT_MAX_LENGTH, false));
        cmd.setSummary(splitLongString(cmd.getSummary(), SUMMARY_CONTENT_MAX_SIZE, true));
        cmd.setPublisher(splitLongString(cmd.getPublisher(), PUBLISHER_MAX_LENGTH, true));
        EnterpriseNotice sendEnterpriseNotice = dbProvider.execute((TransactionStatus status) -> {
            Integer namespaceId = UserContext.getCurrentNamespaceId();
            if (namespaceId == null) {
                namespaceId = 0;
            }
            EnterpriseNotice enterpriseNotice = ConvertHelper.convert(cmd, EnterpriseNotice.class);
            if (EnterpriseNoticeStatus.ACTIVE == EnterpriseNoticeStatus.fromCode(cmd.getStatus())) {
                enterpriseNotice.setSummary(formatSummaryFromContent(enterpriseNotice.getSummary(), enterpriseNotice.getContent(), SUMMARY_CONTENT_MAX_SIZE));
            }
            enterpriseNotice.setNamespaceId(namespaceId);
            enterpriseNotice.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            enterpriseNotice.setOwnerId(cmd.getOrganizationId());
            enterpriseNotice.setOperatorName(getUserContactNameByUserId(UserContext.currentUserId()));
            enterpriseNoticeProvider.createEnterpriseNotice(enterpriseNotice);
            createEnterpriseNoticeAttachments(enterpriseNotice, cmd.getAttachments());
            createEnterpriseNoticeReceivers(enterpriseNotice, cmd.getReceivers());
            return enterpriseNotice;
        });

        sendMessageAfterCreateEnterpriseNoticeOnBackground(cmd.getReceivers(), sendEnterpriseNotice);

        return buildEnterpriseNoticePreviewDTO(sendEnterpriseNotice);
    }

    private EnterpriseNoticePreviewDTO buildEnterpriseNoticePreviewDTO(EnterpriseNotice enterpriseNotice) {
        EnterpriseNoticeDetailActionData actionData = new EnterpriseNoticeDetailActionData();
        actionData.setBulletinId(enterpriseNotice.getId());
        actionData.setBulletinTitle(StringUtils.hasText(enterpriseNotice.getTitle()) ? enterpriseNotice.getTitle() : "公告预览");
        actionData.setShowType(EnterpriseNoticeShowType.PREVIEW.getCode());
        String url = RouterBuilder.build(Router.ENTERPRISE_NOTICE_DETAIL, actionData);

        EnterpriseNoticePreviewDTO enterpriseNoticePreviewDTO = new EnterpriseNoticePreviewDTO();
        enterpriseNoticePreviewDTO.setId(enterpriseNotice.getId());
        enterpriseNoticePreviewDTO.setUrl(url);
        return enterpriseNoticePreviewDTO;
    }

    private void sendMessageAfterCreateEnterpriseNoticeOnBackground(List<EnterpriseNoticeReceiverDTO> receivers, EnterpriseNotice sendEnterpriseNotice) {
        if (sendEnterpriseNotice.getStatus() == null || EnterpriseNoticeStatus.ACTIVE != EnterpriseNoticeStatus.fromCode(sendEnterpriseNotice.getStatus()) || CollectionUtils.isEmpty(receivers)) {
            return;
        }
        bgThreadPool.execute(() -> {
            List<String> groupTypeList = new ArrayList<String>();
            groupTypeList.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
            groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
            groupTypeList.add(OrganizationGroupType.GROUP.getCode());
            Set<Long> userIds = new HashSet<>();
            for (EnterpriseNoticeReceiverDTO receiver : receivers) {
                if (EnterpriseNoticeReceiverType.ORGANIZATIONS == EnterpriseNoticeReceiverType.fromCode(receiver.getReceiverType())) {
                    Organization organization = organizationProvider.findOrganizationById(receiver.getReceiverId());
                    if (organization == null) {
                        LOGGER.info("Organization not found , id = {}", receiver.getReceiverId());
                        continue;
                    }
                    List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(organization.getPath(), groupTypeList, "");
                    if (CollectionUtils.isEmpty(members)) {
                        continue;
                    }
                    for (OrganizationMember member : members) {
                        if (member.getTargetId() != null && member.getTargetId() > 0) {
                            userIds.add(member.getTargetId());
                        }
                    }
                } else {
                    OrganizationMemberDetails details = organizationProvider.findOrganizationMemberDetailsByDetailId(receiver.getReceiverId());
                    if (details != null || details.getId() != null && details.getTargetId() != null && details.getTargetId() > 0) {
                        userIds.add(details.getTargetId());
                    }
                }
            }
            for (Long userId : userIds) {
                sendMessageAfterSendNotice(sendEnterpriseNotice, userId);
            }
        });
    }

    private void createEnterpriseNoticeAttachments(EnterpriseNotice enterpriseNotice, List<EnterpriseNoticeAttachmentDTO> attachments) {
        if (CollectionUtils.isEmpty(attachments)) {
            return;
        }
        attachments.forEach(attachmentDTO -> {
            EnterpriseNoticeAttachment attachment = ConvertHelper.convert(attachmentDTO, EnterpriseNoticeAttachment.class);
            attachment.setContentType(EnterpriseNoticeAttachmentContentType.FILE.getCode());
            attachment.setNamespaceId(enterpriseNotice.getNamespaceId());
            attachment.setNoticeId(enterpriseNotice.getId());
            enterpriseNoticeProvider.createEnterpriseNoticeAttachment(attachment);
        });
    }

    private void createEnterpriseNoticeReceivers(EnterpriseNotice enterpriseNotice, List<EnterpriseNoticeReceiverDTO> receivers) {
        if (CollectionUtils.isEmpty(receivers)) {
            return;
        }
        receivers.forEach(receiverDTO -> {
            EnterpriseNoticeReceiver receiver = ConvertHelper.convert(receiverDTO, EnterpriseNoticeReceiver.class);
            receiver.setNamespaceId(enterpriseNotice.getNamespaceId());
            receiver.setNoticeId(enterpriseNotice.getId());
            enterpriseNoticeProvider.createEnterpriseNoticeReceiver(receiver);
        });
    }

    @Override
    public EnterpriseNoticePreviewDTO updateEnterpriseNotice(UpdateEnterpriseNoticeCommand cmd) {
        EnterpriseNotice updateEnterpriseNotice = enterpriseNoticeProvider.findById(cmd.getId());
        if (updateEnterpriseNotice == null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("EnterpriseNotice not found , id = {}", cmd.getId());
            }
            return null;
        }
        cmd.setTitle(splitLongString(cmd.getTitle(), TITLE_MAX_LENGTH, true));
        cmd.setContent(splitLongString(cmd.getContent(), CONTENT_MAX_LENGTH, false));
        cmd.setSummary(splitLongString(cmd.getSummary(), SUMMARY_CONTENT_MAX_SIZE, true));
        cmd.setPublisher(splitLongString(cmd.getPublisher(), PUBLISHER_MAX_LENGTH, true));
        dbProvider.execute((TransactionStatus status) -> {
            updateEnterpriseNotice.setTitle(cmd.getTitle());
            updateEnterpriseNotice.setSummary(cmd.getSummary());
            updateEnterpriseNotice.setContent(cmd.getContent());
            updateEnterpriseNotice.setContentType(cmd.getContentType());
            updateEnterpriseNotice.setStatus(cmd.getStatus());
            updateEnterpriseNotice.setPublisher(cmd.getPublisher());
            updateEnterpriseNotice.setSecretFlag(cmd.getSecretFlag());
            updateEnterpriseNotice.setOperatorName(getUserContactNameByUserId(UserContext.currentUserId()));
            if (EnterpriseNoticeStatus.ACTIVE == EnterpriseNoticeStatus.fromCode(cmd.getStatus())) {
                updateEnterpriseNotice.setSummary(formatSummaryFromContent(updateEnterpriseNotice.getSummary(), updateEnterpriseNotice.getContent(), SUMMARY_CONTENT_MAX_SIZE));
            }
            enterpriseNoticeProvider.updateEnterpriseNotice(updateEnterpriseNotice);

            enterpriseNoticeProvider.deleteEnterpriseNoticeAttachmentsByNoticeId(updateEnterpriseNotice.getId());
            enterpriseNoticeProvider.deleteEnterpriseNoticeReceiversByNoticeId(updateEnterpriseNotice.getId());

            createEnterpriseNoticeAttachments(updateEnterpriseNotice, cmd.getAttachments());
            createEnterpriseNoticeReceivers(updateEnterpriseNotice, cmd.getReceivers());
            return null;
        });

        sendMessageAfterCreateEnterpriseNoticeOnBackground(cmd.getReceivers(), updateEnterpriseNotice);
        return buildEnterpriseNoticePreviewDTO(updateEnterpriseNotice);
    }

    private void sendMessageAfterSendNotice(EnterpriseNotice notice, Long receiverId) {
        if (receiverId == null || receiverId == 0) {
            return;
        }
        // set the message
        StringBuilder content = new StringBuilder();
        if (StringUtils.hasText(notice.getPublisher())) {
            content.append(notice.getPublisher());
            content.append(": ");
        }
        content.append(EnterpriseNoticeSecretFlag.PRIVATE == EnterpriseNoticeSecretFlag.fromCode(notice.getSecretFlag()) ? "[保密] " : "");
        content.append(notice.getTitle());

        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content.toString());
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiverId)));

        //  set the route
        EnterpriseNoticeDetailActionData actionData = new EnterpriseNoticeDetailActionData();
        actionData.setOrganizationId(notice.getOwnerId());
        actionData.setBulletinId(notice.getId());
        actionData.setBulletinTitle(notice.getTitle());
        actionData.setShowType(EnterpriseNoticeShowType.SHOW.getCode());
        String url = RouterBuilder.build(Router.ENTERPRISE_NOTICE_DETAIL, actionData);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(url);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, "公告");
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        message.setMeta(meta);
        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(receiverId),
                message,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );
    }

    @Override
    public void deleteEnterpriseNotice(DeleteEnterpriseNoticeCommand cmd) {
        EnterpriseNotice enterpriseNotice = enterpriseNoticeProvider.findById(cmd.getId());
        if (enterpriseNotice == null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("EnterpriseNotice not found , id = {}", cmd.getId());
            }
            return;
        }
        dbProvider.execute((TransactionStatus status) -> {
            enterpriseNotice.setStatus(EnterpriseNoticeStatus.DELETED.getCode());
            enterpriseNotice.setOperatorName(getUserContactNameByUserId(UserContext.currentUserId()));
            enterpriseNoticeProvider.updateEnterpriseNotice(enterpriseNotice);
            enterpriseNoticeProvider.logicDeleteEnterpriseNoticeAttachmentsByNoticeId(enterpriseNotice.getId());
            enterpriseNoticeProvider.logicDeleteEnterpriseNoticeReceiversByNoticeId(enterpriseNotice.getId());
            return null;
        });
    }

    @Override
    public void cancelEnterpriseNotice(CancelEnterpriseNoticeCommand cmd) {
        EnterpriseNotice enterpriseNotice = enterpriseNoticeProvider.findById(cmd.getId());
        if (enterpriseNotice == null || EnterpriseNoticeStatus.INACTIVE == EnterpriseNoticeStatus.fromCode(enterpriseNotice.getStatus())) {
            return;
        }
        enterpriseNotice.setStatus(EnterpriseNoticeStatus.INACTIVE.getCode());
        enterpriseNotice.setOperatorName(getUserContactNameByUserId(UserContext.currentUserId()));
        enterpriseNoticeProvider.updateEnterpriseNotice(enterpriseNotice);
    }

    @Override
    public ListEnterpriseNoticeAdminResponse listEnterpriseNoticesByNamespaceId(ListEnterpriseNoticeAdminCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (namespaceId == null) {
            namespaceId = 0;
        }

        ListEnterpriseNoticeAdminResponse response = new ListEnterpriseNoticeAdminResponse();

        List<EnterpriseNotice> enterpriseNotices = enterpriseNoticeProvider.listEnterpriseNoticesByNamespaceId(namespaceId, cmd.getOrganizationId(), offset, pageSize);

        if (enterpriseNotices != null && enterpriseNotices.size() > 0) {
            List<EnterpriseNoticeDTO> enterpriseNoticeDTOS = new ArrayList<>(enterpriseNotices.size());
            enterpriseNotices.forEach(enterpriseNotice -> {
                enterpriseNoticeDTOS.add(ConvertHelper.convert(enterpriseNotice, EnterpriseNoticeDTO.class));
            });
            response.setDtos(enterpriseNoticeDTOS);
        }
        response.setPageSize(pageSize);
        response.setTotalCount(enterpriseNoticeProvider.totalCountEnterpriseNoticesByNamespaceId(namespaceId, cmd.getOrganizationId()));
        return response;
    }

    @Override
    public ListEnterpriseNoticeResponse listEnterpriseNoticesByUserId(ListEnterpriseNoticeCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (namespaceId == null) {
            namespaceId = 0;
        }
        ListEnterpriseNoticeResponse response = new ListEnterpriseNoticeResponse();

        List<EnterpriseNotice> enterpriseNotices = enterpriseNoticeProvider.listEnterpriseNoticesByOwnerId(parseCurrentReceivers(cmd.getOrganizationId()), namespaceId, offset, pageSize);

        if (enterpriseNotices != null && enterpriseNotices.size() > 0) {
            List<EnterpriseNoticeDTO> enterpriseNoticeDTOS = new ArrayList<>(enterpriseNotices.size());
            enterpriseNotices.forEach(enterpriseNotice -> {
                enterpriseNoticeDTOS.add(ConvertHelper.convert(enterpriseNotice, EnterpriseNoticeDTO.class));
            });
            response.setDtos(enterpriseNoticeDTOS);
        }
        if (enterpriseNotices != null && enterpriseNotices.size() == pageSize) {
            response.setNextPageOffset(pageOffset + 1);
        }

        return response;
    }

    /**
     * 发给自己的、发给本部门的或者发给上级部门的公告本人均可见
     */
    private List<EnterpriseNoticeReceiver> parseCurrentReceivers(Long organizationId) {
        OrganizationMemberDetails details = organizationProvider.findOrganizationMemberDetailsByTargetId(UserContext.currentUserId(), organizationId);
        if (details == null || details.getId() == null) {
            return Collections.emptyList();
        }

        List<EnterpriseNoticeReceiver> receivers = new ArrayList<>();
        EnterpriseNoticeReceiver receiver = new EnterpriseNoticeReceiver();
        receiver.setReceiverId(details.getId());
        receiver.setReceiverType(EnterpriseNoticeReceiverType.MEMBER_DETAIL.getCode());
        receivers.add(receiver);

        Long currentOrganizationId = organizationService.getDepartmentByDetailId(details.getId());

        if (currentOrganizationId != null && currentOrganizationId > 0) {
            Organization organization = organizationProvider.findOrganizationById(currentOrganizationId);
            if (organization != null && StringUtils.hasText(organization.getPath())) {
                for (String orgId : organization.getPath().split("/")) {
                    if (StringUtils.hasText(orgId)) {
                        receiver = new EnterpriseNoticeReceiver();
                        receiver.setReceiverId(Long.valueOf(orgId));
                        receiver.setReceiverType(EnterpriseNoticeReceiverType.ORGANIZATIONS.getCode());
                        receivers.add(receiver);
                    }
                }
            }
        }
        return receivers;
    }

    private static String formatSummaryFromContent(String summary, String content, int maxSize) {
        if (StringUtils.hasText(summary)) {
            return summary.replaceAll("\\s|\t|\r|\n", "");
        }
        if (StringUtils.hasText(content)) {
            String c = content.replaceAll("\\s|\t|\r|\n", "");
            return c.substring(0, Math.min(c.length(), maxSize));
        }
        return null;
    }

    @Override
    public UserContactSimpleInfoDTO getCurrentUserContactSimpleInfo(GetCurrentUserContactInfoCommand cmd) {
        User user = UserContext.current().getUser();
        List<OrganizationMember> members = this.organizationProvider.findOrganizationMembersByOrgIdAndUId(user.getId(), cmd.getOrganizationId());
        if (members == null || members.size() == 0)
            return null;
        else {
            OrganizationMemberDetails detail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(members.get(0).getDetailId());
            if (detail == null)
                return null;
            UserContactSimpleInfoDTO dto = new UserContactSimpleInfoDTO();
            dto.setUserId(user.getId());
            dto.setDetailId(detail.getId());
            dto.setContactName(detail.getContactName());
            dto.setContactToken(detail.getContactToken());
            return dto;
        }
    }

    @Override
    public String getUserContactNameByUserId(Long userId) {
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(userId);
        if (members != null && members.size() > 0)
            return members.get(0).getContactName();
        return "";
    }

    @Override
    public boolean isNoticeSendToCurrentUser(Long organizationId, Long enterpriseNoticeId) {
        List<EnterpriseNoticeReceiver> currentReceivers = parseCurrentReceivers(organizationId);
        if (CollectionUtils.isEmpty(currentReceivers)) {
            return false;
        }
        List<EnterpriseNoticeReceiver> receivers = enterpriseNoticeProvider.findEnterpriseNoticeReceiversByNoticeId(enterpriseNoticeId);
        if (CollectionUtils.isEmpty(receivers)) {
            return false;
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (namespaceId == null) {
            namespaceId = 0;
        }
        for (EnterpriseNoticeReceiver current : currentReceivers) {
            for (EnterpriseNoticeReceiver receiver : receivers) {
                if (namespaceId.equals(receiver.getNamespaceId())
                        && current.getReceiverType().equals(receiver.getReceiverType()) && current.getReceiverId().compareTo(receiver.getReceiverId()) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public EnterpriseNoticeDTO getSharedEnterpriseNoticeDetailInfo(String enterpriseNoticeToken) {
        Long enterpriseNoticeId = checkNoticeToken(enterpriseNoticeToken);
        EnterpriseNotice enterpriseNotice = enterpriseNoticeProvider.findById(enterpriseNoticeId);
        if (enterpriseNotice == null) {
            throw RuntimeErrorException.errorWith(EnterpriseNoticeErrorCode.SCOPE,
                    EnterpriseNoticeErrorCode.NOTICE_NOT_FOUND_ERROR, "");
        }
        EnterpriseNoticeDTO enterpriseNoticeDTO = ConvertHelper.convert(enterpriseNotice, EnterpriseNoticeDTO.class);
        List<EnterpriseNoticeAttachment> attachments = enterpriseNoticeProvider.findEnterpriseNoticeAttachmentsByNoticeId(enterpriseNoticeId);

        if (!CollectionUtils.isEmpty(attachments)) {
            Map<String, String> fileIconUrlMap = fileService.getFileIconUrl();
            List<EnterpriseNoticeAttachmentDTO> enterpriseNoticeAttachmentDTOS = new ArrayList<>(attachments.size());
            attachments.forEach(enterpriseNoticeAttachment -> {
                EnterpriseNoticeAttachmentDTO enterpriseNoticeAttachmentDTO = ConvertHelper.convert(enterpriseNoticeAttachment, EnterpriseNoticeAttachmentDTO.class);
                enterpriseNoticeAttachmentDTO.setName(enterpriseNoticeAttachmentDTO.getContentName() + "." + enterpriseNoticeAttachmentDTO.getContentSuffix());
                enterpriseNoticeAttachmentDTO.setIconUrl(fileIconUrlMap.get(enterpriseNoticeAttachment.getContentSuffix()));
                enterpriseNoticeAttachmentDTOS.add(enterpriseNoticeAttachmentDTO);
            });
            enterpriseNoticeDTO.setAttachments(enterpriseNoticeAttachmentDTOS);
        }

        if (EnterpriseNoticeSecretFlag.PRIVATE == EnterpriseNoticeSecretFlag.fromCode(enterpriseNoticeDTO.getSecretFlag())) {
            throw RuntimeErrorException.errorWith(EnterpriseNoticeErrorCode.SCOPE,
                    EnterpriseNoticeErrorCode.SHARED_NOTICE_PRIVATE_ERROR, "permisstion deny");
        }
        return enterpriseNoticeDTO;
    }

    // 当字符串超过最大限制时，将此截取到最大长度
    private String splitLongString(String original, int maxLength, boolean trim) {
        if (!StringUtils.hasText(original)) {
            return null;
        }
        String newString = trim ? original.trim() : original;
        if (newString.length() <= maxLength) {
            return newString;
        }
        return newString.substring(0, maxLength);
    }

    private Long checkNoticeToken(String noticeToken) {
        Long noticeId = WebTokenGenerator.getInstance().fromWebToken(noticeToken, Long.class);
        if (noticeId == null) {
            LOGGER.error("Invalid noticeToken, noticeToken={}", noticeToken);
            throw RuntimeErrorException.errorWith(EnterpriseNoticeErrorCode.SCOPE,
                    EnterpriseNoticeErrorCode.SHARED_NOTICE_TOKEN_INVALID, "Invalid token");
        }
        return noticeId;
    }

    private String getNoticeWebShareUrl(Long enterpriseNoticeId) {
        String noticeToken = WebTokenGenerator.getInstance().toWebToken(enterpriseNoticeId);
        String homeUrl = configurationProvider.getValue(0, ConfigConstants.HOME_URL, "");
        String webUri = configurationProvider.getValue(0, ConfigConstants.ENTERPRISE_NOTICE_WEB_SHARE_URL,

                "/announcement/build/index.html?ns=%s&noticeToken=%s");
        if (!StringUtils.hasText(homeUrl) || !StringUtils.hasText(webUri)) {
            LOGGER.error("Invalid home url or share uri, homeUrl={}, shareUrl={}", homeUrl, webUri);
            throw RuntimeErrorException.errorWith(EnterpriseNoticeErrorCode.SCOPE,
                    EnterpriseNoticeErrorCode.NOTICE_SHARE_URL_INVALID, "Invalid home url or share url");
        }

        return homeUrl + String.format(webUri, UserContext.getCurrentNamespaceId(), noticeToken);
    }

	@Override
	public void stickyEnterpriseNotice(StickyEnterpriseNoticeCommand cmd) {
		EnterpriseNotice enterpriseNotice = enterpriseNoticeProvider.findById(cmd.getId());
        enterpriseNotice.setStickFlag(EnterpriseNoticeStickFlag.STICK.getCode());
        enterpriseNotice.setStickTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        enterpriseNotice.setOperatorName(getUserContactNameByUserId(UserContext.currentUserId()));
        enterpriseNoticeProvider.updateEnterpriseNotice(enterpriseNotice);
	}

	@Override
	public void unStickyEnterpriseNotice(UnStickyEnterpriseNoticeCommand cmd) {

		EnterpriseNotice enterpriseNotice = enterpriseNoticeProvider.findById(cmd.getId());
        enterpriseNotice.setStickFlag(EnterpriseNoticeStickFlag.UN_STICK.getCode());
        //最晚取消置顶的也要排序在前面所以更新createTime并且取消stickTime
        enterpriseNotice.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        enterpriseNotice.setStickTime(null);
        enterpriseNotice.setOperatorName(getUserContactNameByUserId(UserContext.currentUserId()));
        enterpriseNoticeProvider.updateEnterpriseNotice(enterpriseNotice);
	}

}
