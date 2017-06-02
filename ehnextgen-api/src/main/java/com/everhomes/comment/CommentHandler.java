// @formatter:off
package com.everhomes.comment;

import com.everhomes.rest.comment.AddCommentCommand;
import com.everhomes.rest.comment.CommentDTO;
import com.everhomes.rest.comment.DeleteCommonCommentCommand;
import com.everhomes.rest.comment.ListCommentsCommand;

import java.util.List;

public interface CommentHandler {
    String COMMENT_OBJ_RESOLVER_PREFIX = "Comment-";

    CommentDTO addComment(AddCommentCommand cmd);
    List<CommentDTO> listComments(ListCommentsCommand cmd);
    void deleteComment(DeleteCommonCommentCommand cmd);
}
