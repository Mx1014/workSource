package com.everhomes.workReport;

import com.everhomes.comment.CommentHandler;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.comment.*;
import com.everhomes.server.schema.tables.pojos.EhWorkReportValComments;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component(CommentHandler.COMMENT_OBJ_RESOLVER_PREFIX + OwnerTypeConstants.WORK_REPORT)
public class WorkReportCommentHandler implements CommentHandler {

    @Autowired
    WorkReportService workReportService;

    @Autowired
    WorkReportValProvider workReportValProvider;

    @Autowired
    WorkReportProvider workReportProvider;

    @Autowired
    WorkReportMessageService workReportMessageService;

    @Autowired
    ContentServerService contentServerService;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private MessagingService messagingService;

    @Override
    public CommentDTO addComment(AddCommentCommand cmd) {
        OwnerTokenDTO ownerTokenDTO = WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);
        User user = UserContext.current().getUser();


        WorkReportVal reportVal = workReportValProvider.getWorkReportValById(ownerTokenDTO.getId());
        if (reportVal != null) {
            WorkReport report = workReportProvider.getWorkReportById(reportVal.getReportId());
            CommentDTO commentDTO = this.dbProvider.execute((TransactionStatus status) -> {
                WorkReportValComment comment = processComment(cmd, reportVal, user);
                Long commentId = workReportValProvider.createWorkReportValComment(comment);
                List<WorkReportValCommentAttachment> attachments = processCommentAttachments(cmd, commentId, comment);
                for (WorkReportValCommentAttachment attachment : attachments)
                    workReportValProvider.createWorkReportValCommentAttachment(attachment);

                //  send message to the corresponding receiver.
                workReportMessageService.workReportCommentMessage(user, reportVal, report, cmd.getParentCommentId());

                //  return the comment back.
                CommentDTO dto = new CommentDTO();
                dto.setCreatorNickName(workReportService.fixUpUserName(comment.getCreatorUserId(), reportVal.getOwnerId()));
                dto.setCreatorAvatarUrl(contentServerService.parserUri(workReportService.getUserAvatar(comment.getCreatorUserId())));
                dto.setCreatorUid(comment.getCreatorUserId());
                dto.setContent(comment.getContent());
                dto.setContentType(comment.getContentType());
                if (cmd.getAttachments() != null && cmd.getAttachments().size() > 0) {
                    List<AttachmentDTO> attachmentDTOS = cmd.getAttachments().stream().map(r -> {
                        AttachmentDTO attachmentDTO = ConvertHelper.convert(r, AttachmentDTO.class);
                        attachmentDTO.setContentUrl(contentServerService.parserUri(r.getContentUri()));
                        return attachmentDTO;
                    }).collect(Collectors.toList());
                    dto.setAttachments(attachmentDTOS);
                }
                dto.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                return dto;
            });
            return commentDTO;
        }
        return null;
    }

    private WorkReportValComment processComment(AddCommentCommand cmd, WorkReportVal reportVal, User user) {
        WorkReportValComment comment = new WorkReportValComment();
        comment.setNamespaceId(user.getNamespaceId());
        comment.setOwnerId(reportVal.getOwnerId());
        comment.setOwnerType(reportVal.getOwnerType());
        comment.setOwnerType(reportVal.getOwnerType());
        comment.setReportValId(reportVal.getId());
        comment.setParentCommentId(cmd.getParentCommentId());
        comment.setContentType(cmd.getContentType());
        comment.setContent(cmd.getContent());
        comment.setCreatorUserId(user.getId());
        return comment;
    }

    private List<WorkReportValCommentAttachment> processCommentAttachments(AddCommentCommand cmd, Long commentId, WorkReportValComment comment) {
        User user = UserContext.current().getUser();
        List<WorkReportValCommentAttachment> attachments = new ArrayList<>();
        if (cmd.getAttachments() != null) {
            for (AttachmentDescriptor des : cmd.getAttachments()) {
                WorkReportValCommentAttachment attachment = new WorkReportValCommentAttachment();
                attachment.setNamespaceId(user.getNamespaceId());
                attachment.setCommentId(commentId);
                attachment.setContentType(des.getContentType());
                attachment.setContentUri(des.getContentUri());
                attachment.setCreatorUserId(user.getId());
                attachments.add(attachment);
            }
        }
        return attachments;
    }

    @Override
    public ListCommentsResponse listComments(ListCommentsCommand cmd) {
        ListCommentsResponse response = new ListCommentsResponse();
        List<CommentDTO> comments = new ArrayList<>();
        Long nextPageAnchor = null;


        OwnerTokenDTO ownerTokenDto = WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(1L);
        Integer pageOffset = cmd.getPageAnchor().intValue();
        Integer offset = (pageOffset - 1) * (cmd.getPageSize());

        List<WorkReportValComment> results = workReportValProvider.listWorkReportValComments(namespaceId, ownerTokenDto.getId(), offset, cmd.getPageSize());
        if (results != null && results.size() > 0) {

            if (results.size() > cmd.getPageSize()) {
                results.remove(results.size() - 1);
                nextPageAnchor = (long) (pageOffset + 1);
            }

            List<Long> commentIds = results.stream().map(EhWorkReportValComments::getId).collect(Collectors.toList());
            Map<Long, List<AttachmentDTO>> attachments = listWorkReportValCommentAttachments(namespaceId, commentIds);
            results.forEach(r -> {
                CommentDTO dto = ConvertHelper.convert(r, CommentDTO.class);
                dto.setCreatorUid(r.getCreatorUserId());
                dto.setCreatorNickName(workReportService.fixUpUserName(dto.getCreatorUid(), r.getOwnerId()));
                dto.setCreatorAvatarUrl(contentServerService.parserUri(workReportService.getUserAvatar(dto.getCreatorUid())));
                dto.setAttachments(attachments.get(dto.getId()));
                dto.setCreateTime(r.getCreateTime());
                comments.add(dto);
            });
        }

        response.setNextPageAnchor(nextPageAnchor);
        response.setCommentDtos(comments);
        response.setCommentCount(Long.valueOf(workReportValProvider.countWorkReportValComments(namespaceId, ownerTokenDto.getId())));
        return response;
    }

    private Map<Long, List<AttachmentDTO>> listWorkReportValCommentAttachments(Integer namespaceId, List<Long> commentIds) {
        Map<Long, List<AttachmentDTO>> res = new HashMap<>();
        List<WorkReportValCommentAttachment> results = workReportValProvider.listWorkReportValCommentAttachments(namespaceId, commentIds);
        if (results != null && results.size() > 0) {
            List<AttachmentDTO> attachmentDTOS = results.stream().map(r -> {
                AttachmentDTO dto = ConvertHelper.convert(r, AttachmentDTO.class);
                dto.setOwnerId(r.getCommentId());
                dto.setContentUrl(contentServerService.parserUri(r.getContentUri()));
                return dto;
            }).collect(Collectors.toList());
            res = attachmentDTOS.stream().collect(Collectors.groupingBy
                    (AttachmentDTO::getOwnerId));
        }
        return res;
    }

    @Override
    public void deleteComment(DeleteCommonCommentCommand cmd) {
        WorkReportValComment comment = workReportValProvider.getWorkReportValCommentById(cmd.getId());
        dbProvider.execute((TransactionStatus status) -> {
            workReportValProvider.deleteWorkReportValComment(comment);
            workReportValProvider.deleteCommentAttachmentsByCommentId(UserContext.getCurrentNamespaceId(), comment.getId());
            return null;
        });
    }
}
