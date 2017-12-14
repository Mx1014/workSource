package com.everhomes.workReport;

import com.everhomes.comment.CommentHandler;
import com.everhomes.rest.comment.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.WebTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(CommentHandler.COMMENT_OBJ_RESOLVER_PREFIX + OwnerTypeConstants.WORK_REPORT)
public class WorkReportCommentHandler implements CommentHandler{

    @Autowired
    WorkReportValProvider workReportValProvider;

    @Override
    public CommentDTO addComment(AddCommentCommand cmd) {
        OwnerTokenDTO ownerTokenDTO = WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);

        WorkReportVal reportVal = workReportValProvider.getWorkReportValById(ownerTokenDTO.getId());
        if(reportVal != null){

        }
        return null;
    }

    private WorkReportValComment processComments(AddCommentCommand cmd, WorkReportVal reportVal){
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

    @Override
    public ListCommentsResponse listComments(ListCommentsCommand cmd) {
        return null;
    }

    @Override
    public void deleteComment(DeleteCommonCommentCommand cmd) {

    }
}
