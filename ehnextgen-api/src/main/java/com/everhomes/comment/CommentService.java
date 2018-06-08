// @formatter:off
package com.everhomes.comment;

import com.everhomes.rest.comment.*;


public interface CommentService {
	CommentDTO addComment(AddCommentCommand cmd);

	ListCommentsResponse listComments(ListCommentsCommand cmd);

	void deleteComment(DeleteCommonCommentCommand cmd);
	
    GetCommentsResponse getComment(GetCommentCommand cmd);
	
	
}
