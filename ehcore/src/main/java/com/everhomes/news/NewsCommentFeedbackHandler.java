package com.everhomes.news;


import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.comment.DeleteCommonCommentCommand;
import com.everhomes.rest.news.CommentStatus;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.rest.user.FeedbackCommand;
import com.everhomes.rest.user.FeedbackDTO;
import com.everhomes.rest.user.FeedbackHandleType;
import com.everhomes.rest.user.UpdateFeedbackCommand;
import com.everhomes.server.schema.tables.pojos.EhNewsComment;
import com.everhomes.user.Feedback;
import com.everhomes.user.FeedbackHandler;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(FeedbackHandler.FEEDBACKHANDLER + FeedbackHandler.NEWS_COMMENT)
public class NewsCommentFeedbackHandler implements FeedbackHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsCommentFeedbackHandler.class);
	
	@Autowired 
	CommentProvider commentProvider;
	
	@Autowired 
	NewsProvider newsProvider;

	@Override
	public void beforeAddFeedback(FeedbackCommand cmd) {
		if(cmd.getTargetParam() == null){
			LOGGER.error("Invalid parameters, cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
	}

	@Override
	public void afterAddFeedback(Feedback feedback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void populateFeedbackDTO(FeedbackDTO dto) {
		dto.setTargetSubject(dto.getSubject());
	}

	@Override
	public void beforeUpdateFeedback(UpdateFeedbackCommand cmd) {

	}

	@Override
	public void afterUpdateFeedback(Feedback feedback) {
		if (FeedbackHandleType.fromStatus(feedback.getHandleType()) != FeedbackHandleType.DELETE) {
			return;
		}
		
		// 获取评论
		Long commentId = feedback.getTargetId();
		Comment comment = commentProvider.findCommentById(EhNewsComment.class, commentId);
		if (null == comment) {
			return;
		}
		
		// 获取对应新闻
		Long newsId = comment.getOwnerId();
		News news = newsProvider.findNewsById(newsId);
		if (null == news) {
			return;
		}
		
		// 删除评论
		comment.setDeleterUid(UserContext.currentUserId());
		comment.setStatus(CommentStatus.INACTIVE.getCode());
		comment.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		commentProvider.updateComment(EhNewsComment.class, comment);

		// 更新评论的个数
		news.setChildCount(news.getChildCount() <= 0 ? 0 : news.getChildCount() - 1L);
		newsProvider.updateNews(news);
	}

	@Override
	public void feedbackEvent(Feedback feedback) {

	}
}
