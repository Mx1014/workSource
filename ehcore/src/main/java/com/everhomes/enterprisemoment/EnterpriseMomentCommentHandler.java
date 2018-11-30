package com.everhomes.enterprisemoment;

import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.comment.CommentHandler;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.comment.AddCommentCommand;
import com.everhomes.rest.comment.AttachmentDTO;
import com.everhomes.rest.comment.AttachmentDescriptor;
import com.everhomes.rest.comment.CommentDTO;
import com.everhomes.rest.comment.DeleteCommonCommentCommand;
import com.everhomes.rest.comment.ListCommentsCommand;
import com.everhomes.rest.comment.ListCommentsResponse;
import com.everhomes.rest.comment.OwnerTokenDTO;
import com.everhomes.rest.comment.OwnerTypeConstants;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentComments;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMoments;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component(CommentHandler.COMMENT_OBJ_RESOLVER_PREFIX + OwnerTypeConstants.ENTERPRISE_MOMENT)
public class EnterpriseMomentCommentHandler implements CommentHandler {
    @Autowired
    EnterpriseMomentProvider momentProvider;
    @Autowired
    EnterpriseMomentCommentProvider commentProvider;
    @Autowired
    EnterpriseMomentCommentAttachmentProvider commentAttachmentProvider;
    @Autowired
    EnterpriseMomentMessageProvider messageProvider;
    @Autowired
    EnterpriseMomentService enterpriseMomentService;
    @Autowired
    OrganizationProvider organizationProvider;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    ContentServerService contentServerService;
    @Autowired
    ConfigurationProvider configurationProvider;
    @Autowired
    LocaleStringService localeStringService;
    @Autowired
    DbProvider dbProvider;


    @Override
    public CommentDTO addComment(AddCommentCommand cmd) {
        // 说明1：不做可见范围的验证，即用户从可见变成不可见时，只要页面不刷新，均可评论成功
        // 说明2：不管动态是否被删除，都可以评论成功
        // 说明3：动态如果被删除，评论成功后不创建相关消息记录
        OwnerTokenDTO ownerTokenDTO = WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);

        EnterpriseMoment moment = momentProvider.findEnterpriseMomentById(ownerTokenDTO.getId());
        // 系统默认的动态不支持评论
        if (User.SYSTEM_UID == moment.getCreatorUid().longValue()) {
            return null;
        }
        EnterpriseMomentComment comment = new EnterpriseMomentComment(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId());
        comment.setContentType(cmd.getContentType());
        comment.setContent(cmd.getContent());
        comment.setReplyToCommentId(cmd.getParentCommentId());
        comment.setCreatorUid(UserContext.currentUserId());
        comment.setCreatorName(organizationService.fixUpUserName(UserContext.current().getUser(), moment.getOrganizationId()));
        comment.setOperatorUid(UserContext.currentUserId());

        if (cmd.getParentCommentId() != null) {
            EnterpriseMomentComment replyComment = commentProvider.findEnterpriseMomentCommentById(cmd.getParentCommentId());
            if (replyComment != null) {
                comment.setReplyToName(replyComment.getCreatorName());
                comment.setReplyToUserId(replyComment.getCreatorUid());
            }
        }

        dbProvider.execute(transactionStatus -> {
            commentProvider.createEnterpriseMomentComment(comment);
            commentAttachmentProvider.batchCreateAttachments(processCommentAttachments(cmd, comment.getId()));
            momentProvider.incrCommentCount(moment.getId(), moment.getNamespaceId(), moment.getOrganizationId(), 1);
            return null;
        });

        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setCreatorNickName(comment.getCreatorName());
        dto.setCreatorAvatarUrl(contentServerService.parserUri(UserContext.current().getUser().getAvatar()));
        dto.setCreatorUid(comment.getCreatorUid());
        dto.setContent(comment.getContent());
        dto.setContentType(comment.getContentType());
        dto.setCreateTime(comment.getCreateTime());
        dto.setOwnerToken(cmd.getOwnerToken());
        dto.setParentCommentId(comment.getReplyToCommentId());
        dto.setReplyToUserId(comment.getReplyToUserId());
        dto.setAttachments(new ArrayList<>(0));
        if (!CollectionUtils.isEmpty(cmd.getAttachments())) {
            List<AttachmentDTO> attachmentDTOS = cmd.getAttachments().stream().map(r -> {
                AttachmentDTO attachmentDTO = ConvertHelper.convert(r, AttachmentDTO.class);
                attachmentDTO.setOwnerId(comment.getId());
                attachmentDTO.setContentUrl(contentServerService.parserUri(r.getContentUri()));
                return attachmentDTO;
            }).collect(Collectors.toList());
            dto.setAttachments(attachmentDTOS);
        }

        createNewMessageAfterDoComment(moment, comment, !CollectionUtils.isEmpty(cmd.getAttachments()));
        return dto;
    }

    private List<EnterpriseMomentCommentAttachment> processCommentAttachments(AddCommentCommand cmd, Long commentId) {
        if (CollectionUtils.isEmpty(cmd.getAttachments())) {
            return Collections.emptyList();
        }

        User user = UserContext.current().getUser();
        List<EnterpriseMomentCommentAttachment> attachments = new ArrayList<>();
        for (AttachmentDescriptor des : cmd.getAttachments()) {
            EnterpriseMomentCommentAttachment attachment = new EnterpriseMomentCommentAttachment();
            attachment.setNamespaceId(user.getNamespaceId());
            attachment.setCommentId(commentId);
            attachment.setContentType(des.getContentType());
            attachment.setContentUri(des.getContentUri());
            attachment.setCreatorUid(user.getId());
            attachments.add(attachment);
        }
        return attachments;
    }

    @Override
    public ListCommentsResponse listComments(ListCommentsCommand cmd) {
        // 说明1：不做可见范围的验证
        OwnerTokenDTO ownerTokenDTO = WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        EnterpriseMoment moment = momentProvider.findEnterpriseMomentById(ownerTokenDTO.getId());
        ListCommentsResponse response = new ListCommentsResponse();
        response.setCommentDtos(new ArrayList<>(0));
        response.setCommentCount(0L);
        if (moment == null) {
            return response;
        }
        List<EnterpriseMomentComment> comments = commentProvider.listEnterpriseMomentCommentsDesc(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId(), cmd.getPageAnchor(), pageSize + 1);
        if (CollectionUtils.isEmpty(comments)) {
            return response;
        }

        if (comments.size() > pageSize) {
            response.setNextPageAnchor(comments.get(pageSize).getId());
            comments.remove(pageSize);
        }

        response.setCommentDtos(enterpriseMomentService.buildCommentDTO(moment.getNamespaceId(), moment.getOrganizationId(), comments));
        response.setCommentCount(Long.valueOf(commentProvider.countEnterpriseMomentComments(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId())));
        return response;
    }

    @Override
    public void deleteComment(DeleteCommonCommentCommand cmd) {
        // 说明1：不做可见范围的验证，即用户从可见变成不可见时，只要页面不刷新，均可删除评论成功
        EnterpriseMomentComment comment = commentProvider.findEnterpriseMomentCommentById(cmd.getId());
        if (comment == null) {
            return;
        }
        // 不能删除其他人的评论
        if (!UserContext.currentUserId().equals(comment.getCreatorUid())) {
            throw RuntimeErrorException.errorWith(EnterpriseMomentConstants.ERROR_SCOPE, EnterpriseMomentConstants.ERROR_WHEN_DELETE_COMMENT_OF_OTHERS, "不能删除其他人的评论");
        }
        dbProvider.execute((TransactionStatus status) -> {
            commentProvider.deleteEnterpriseMomentComment(comment);
            commentAttachmentProvider.deleteAttachmentsByCommentId(comment.getNamespaceId(), comment.getId());
            messageProvider.markSourceDeleteBySourceId(comment.getNamespaceId(), comment.getOrganizationId(), comment.getEnterpriseMomentId(), EhEnterpriseMomentComments.class.getSimpleName(), comment.getId());
            momentProvider.incrCommentCount(comment.getEnterpriseMomentId(), comment.getNamespaceId(), comment.getOrganizationId(), -1);
            return null;
        });
    }

    private void createNewMessageAfterDoComment(EnterpriseMoment moment, EnterpriseMomentComment comment, boolean hasImage) {
        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String showImage = localeStringService.getLocalizedString(EnterpriseMomentConstants.ENTERPRISE_DEFAULT_MOMENT, EnterpriseMomentConstants.ENTERPRISE_MOMENT_MESSAGE_SHOW_IMAGE,
                UserContext.current().getUser().getLocale(), "图片");
        // 如果该动态已删除，则不创建消息记录
        if (TrueOrFalseFlag.TRUE == TrueOrFalseFlag.fromCode(moment.getDeleteFlag())) {
            return;
        }
        ExecutorUtil.submit(() -> {
            LocalEventBus.publish(event -> {
                LocalEventContext context = new LocalEventContext();
                context.setUid(userId);
                context.setNamespaceId(namespaceId);
                event.setContext(context);

                event.setEntityType(EhEnterpriseMoments.class.getSimpleName());
                event.setEntityId(moment.getId());

                event.setEventName(SystemEvent.ENTERPRISE_MOMENT_DO_COMMENT.getCode());
                event.addParam("commentId", String.valueOf(comment.getId()));

                // 没有评论内容，有评论图片时消息显示  [图片]
                String messageContent = StringUtils.hasText(comment.getContent()) ? comment.getContent() : "";
                messageContent = !StringUtils.hasText(messageContent) && hasImage ? String.format("[%s]", showImage) : messageContent;
                event.addParam("messageContent", messageContent);
            });
        });
    }

}
