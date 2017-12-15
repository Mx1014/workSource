package com.everhomes.workReport;

import com.everhomes.comment.CommentHandler;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.comment.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.WebTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(CommentHandler.COMMENT_OBJ_RESOLVER_PREFIX + OwnerTypeConstants.WORK_REPORT)
public class WorkReportCommentHandler implements CommentHandler {

    @Autowired
    WorkReportService workReportService;

    @Autowired
    WorkReportValProvider workReportValProvider;

    @Autowired
    ContentServerService contentServerService;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public CommentDTO addComment(AddCommentCommand cmd) {
        OwnerTokenDTO ownerTokenDTO = WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);

        WorkReportVal reportVal = workReportValProvider.getWorkReportValById(ownerTokenDTO.getId());
        if (reportVal != null) {
            CommentDTO commentDTO = this.dbProvider.execute((TransactionStatus status) -> {
                WorkReportValComment comment = processComment(cmd, reportVal);
                Long commentId = workReportValProvider.createWorkReportValComment(comment);
                List<WorkReportValCommentAttachment> attachments = processCommentAttachments(cmd, commentId, comment);
                for (WorkReportValCommentAttachment attachment : attachments)
                    workReportValProvider.createWorkReportValCommentAttachment(attachment);

                CommentDTO dto = new CommentDTO();
                dto.setCreatorNickName(workReportService.fixUpUserName(comment.getCreatorUserId()));
                dto.setCreatorAvatarUrl(workReportService.getUserAvatar(comment.getCreatorUserId()));
                dto.setCreatorUid(comment.getCreatorUserId());
                dto.setContent(comment.getContent());
                dto.setContentType(comment.getContentType());
                return dto;
            });
            return commentDTO;
        }
        return null;
    }

    private WorkReportValComment processComment(AddCommentCommand cmd, WorkReportVal reportVal) {
        User user = UserContext.current().getUser();
        WorkReportValComment comment = new WorkReportValComment();
        comment.setNamespaceId(user.getNamespaceId());
        comment.setOwnerId(reportVal.getOwnerId());
        comment.setOwnerType(reportVal.getOwnerType());
        comment.setOwnerType(reportVal.getOwnerType());
        comment.setReportValId(reportVal.getId());
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
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20);
        List<WorkReportValComment> results = workReportValProvider.listWorkReportValComments(namespaceId, ownerTokenDto.getId(), cmd.getPageAnchor(), cmd.getPageSize());
        if(results != null && results.size() >0) {

            if(results.size() > cmd.getPageSize()){
                results.remove(results.size() - 1);
                nextPageAnchor = results.get(results.size() - 1).getId();
            }

            List<Long> commentIds = results.stream().map(r -> r.getId()).collect(Collectors.toList());
            Map<Long, List<AttachmentDTO>> attachments = listWorkReportValCommentAttachments(namespaceId, commentIds);
            results.forEach(r -> {
                CommentDTO dto = ConvertHelper.convert(r, CommentDTO.class);
                dto.setCreatorUid(r.getCreatorUserId());
                dto.setCreatorNickName(workReportService.fixUpUserName(dto.getCreatorUid()));
                dto.setCreatorAvatarUrl(workReportService.getUserAvatar(dto.getCreatorUid()));
                dto.setAttachments(attachments.get(dto.getId()));
                comments.add(dto);
            });
        }

        response.setNextPageAnchor(nextPageAnchor);
        response.setCommentDtos(comments);
        response.setCommentCount(Long.valueOf(comments.size()));
        return response;
    }

    private Map<Long, List<AttachmentDTO>> listWorkReportValCommentAttachments(Integer namespaceId, List<Long> commentIds) {
        Map<Long, List<AttachmentDTO>> res = new HashMap<>();
        List<WorkReportValCommentAttachment> results = workReportValProvider.listWorkReportValCommentAttachments(namespaceId, commentIds);
        if(results != null && results.size()>0){
            List<AttachmentDTO> attachmentDTOS = results.stream().map(r ->{
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
