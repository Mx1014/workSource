// @formatter:off
package com.everhomes.comment;


import com.everhomes.rest.comment.*;

import java.util.List;

public interface CommentService {
	CommentDTO addComment(AddCommentCommand cmd);

	List<CommentDTO> listComments(ListCommentsCommand cmd);

	void deleteComment(DeleteCommentCommand cmd);
}
