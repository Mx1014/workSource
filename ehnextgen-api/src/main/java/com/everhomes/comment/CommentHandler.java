// @formatter:off
package com.everhomes.comment;

import com.everhomes.rest.comment.*;

import java.util.List;

public interface CommentHandler {
    String COMMENT_OBJ_RESOLVER_PREFIX = "Comment-";

    CommentDTO addComment(AddCommentCommand cmd);
    ListCommentsResponse listComments(ListCommentsCommand cmd);
    void deleteComment(DeleteCommonCommentCommand cmd);
}
