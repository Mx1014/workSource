// @formatter:off
package com.everhomes.comment;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.comment.*;
import com.everhomes.util.RuntimeErrorException;

public interface CommentHandler {
    String COMMENT_OBJ_RESOLVER_PREFIX = "Comment-";

    CommentDTO addComment(AddCommentCommand cmd);
    ListCommentsResponse listComments(ListCommentsCommand cmd);
    void deleteComment(DeleteCommonCommentCommand cmd);
    
    default GetCommentsResponse getComment(GetCommentCommand cmd) {
    	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
    			ErrorCodes.ERROR_UNSUPPORTED_USAGE, "this method is not supported by current module");
    }
}
