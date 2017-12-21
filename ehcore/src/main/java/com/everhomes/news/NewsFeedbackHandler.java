package com.everhomes.news;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.rest.user.FeedbackCommand;
import com.everhomes.rest.user.FeedbackDTO;
import com.everhomes.rest.user.FeedbackHandleType;
import com.everhomes.rest.user.UpdateFeedbackCommand;
import com.everhomes.user.Feedback;
import com.everhomes.user.FeedbackHandler;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

@Component(FeedbackHandler.FEEDBACKHANDLER + FeedbackHandler.NEWS)
public class NewsFeedbackHandler implements FeedbackHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsFeedbackHandler.class);

	@Autowired
	public NewsProvider newsProvider;

	@Autowired
	public NewsService newsService;

	@Override
	public void beforeAddFeedback(FeedbackCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterAddFeedback(Feedback feedback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void populateFeedbackDTO(FeedbackDTO dto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeUpdateFeedback(UpdateFeedbackCommand cmd) {

	}

	@Override
	public void afterUpdateFeedback(Feedback feedback) {
		if (FeedbackHandleType.fromStatus(feedback.getHandleType()) != FeedbackHandleType.DELETE) {
			return;
		}
		Long userId = UserContext.current().getUser().getId();
		Long newsId = checkNewsToken(userId, feedback.getTargetParam());
		News news = newsService.findNewsById(userId, newsId);
		if (news == null) {
			return;
		}
		news.setDeleterUid(userId);
		news.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		news.setStatus(NewsStatus.INACTIVE.getCode());
		newsProvider.updateNews(news);
		newsService.syncNewsWhenDelete(newsId);

	}

}
