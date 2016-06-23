package com.everhomes.news;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.news.CommentStatus;
import com.everhomes.server.schema.tables.pojos.EhNewsComment;
import com.everhomes.util.DateHelper;

public class CommentTest extends CoreServerTestCase {
	@Autowired
	private CommentProvider commentProvider;
	
	/**
	 * 测试通过 
	 */
	@Test
	public void testCreateComment(){
		Comment comment = new Comment();
		comment.setOwnerId(3L);
		comment.setContentType("text");
		comment.setContent("创建评论测试创建评论测试创建评论测试创建评论测试创建评论测试创建评论测试创建评论测试");
		comment.setStatus(CommentStatus.ACTIVE.getCode());
		comment.setCreatorUid(1L);
		comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		commentProvider.createComment(EhNewsComment.class, comment);
	}
	
	/**
	 * 测试通过 
	 */
	@Test
	public void testUpdateComment(){
		Comment comment = commentProvider.findCommentById(EhNewsComment.class, 1L);
		comment.setContent("更新评论测试更新评论测试更新评论测试更新评论测试更新评论测试更新评论测试更新评论测试更新评论测试");
		commentProvider.updateComment(EhNewsComment.class, comment);
	}
	
	/**
	 * 测试通过 
	 */
	@Test
	public void testListComment(){
		System.err.println("testListComment=============================");
		List<Comment> list = commentProvider.listCommentByOwnerId(EhNewsComment.class, 3L);
		list.forEach(c->System.err.println(c));
		System.err.println("testListComment=============================");
	}
	
	/**
	 * 测试通过 
	 */
	@Test
	public void testDeleteComment(){
		commentProvider.deleteComment(EhNewsComment.class, 1L);
	}
	
}
