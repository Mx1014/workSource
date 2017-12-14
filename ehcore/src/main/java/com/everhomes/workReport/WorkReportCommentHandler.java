package com.everhomes.workReport;

import com.everhomes.comment.CommentHandler;
import com.everhomes.rest.comment.*;
import com.everhomes.util.WebTokenGenerator;
import org.springframework.stereotype.Component;

@Component(CommentHandler.COMMENT_OBJ_RESOLVER_PREFIX + OwnerTypeConstants.WORK_REPORT)
public class WorkReportCommentHandler implements CommentHandler{
    @Override
    public CommentDTO addComment(AddCommentCommand cmd) {
        OwnerTokenDTO ownerTokenDTO = WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);


        return null;
    }

    @Override
    public ListCommentsResponse listComments(ListCommentsCommand cmd) {
        return null;
    }

    @Override
    public void deleteComment(DeleteCommonCommentCommand cmd) {

    }
}
