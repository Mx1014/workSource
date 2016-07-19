// @formatter:off
package com.everhomes.news;

import java.util.List;

public interface CommentProvider {
	void createComment(Class<?> tableClass, Comment comment);

	Comment findCommentById(Class<?> pojoClass, Long id);

	void deleteComment(Class<?> pojoClass, Long id);

	void updateComment(Class<?> pojoClass, Comment comment);
	
	/**
	 * <p>按ownerId列出所有的评论，不区分状态，不分页，不排序</p>
	 */
	List<Comment> listCommentByOwnerId(Class<?> pojoClass, Long ownerId);

	/**
	 * <p>按ownerId列出评论，只列出status=2的，且按锚点分页，按时间倒序</p>
	 */
	List<Comment> listCommentByOwnerIdWithPage(Class<?> pojoClass, Long ownerId, Long pageAnchor, Integer pageSize);
}
