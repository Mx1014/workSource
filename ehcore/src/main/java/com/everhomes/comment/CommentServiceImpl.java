// @formatter:off
package com.everhomes.comment;

import com.everhomes.rest.comment.AddCommentCommand;
import com.everhomes.rest.comment.CommentDTO;
import com.everhomes.rest.comment.DeleteCommentCommand;
import com.everhomes.rest.comment.ListCommentsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CommentServiceImpl implements CommentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

	@Override
	public CommentDTO addComment(AddCommentCommand cmd) {
		return null;
	}

	@Override
	public List<CommentDTO> listComments(ListCommentsCommand cmd) {
		return null;
	}

	@Override
	public void deleteComment(DeleteCommentCommand cmd) {

	}
}
