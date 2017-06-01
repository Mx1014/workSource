// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * <ul>
 *     <li>commentDtos: commentDtos {@link com.everhomes.rest.comment.CommentDTO}</li>
 * </ul>
 */
public class ListCommentsResponse {

	@ItemType(CommentDTO.class)
	List<CommentDTO> commentDtos;

	public List<CommentDTO> getCommentDtos() {
		return commentDtos;
	}

	public void setCommentDtos(List<CommentDTO> commentDtos) {
		this.commentDtos = commentDtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}