// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
import java.util.List;


/**
 * <ul>
 *     <li>commentDtos: 评论列表，参考 {@link com.everhomes.rest.comment.CommentDTO}</li>
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