package com.everhomes.yellowPage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.user.FeedbackCommand;
import com.everhomes.rest.user.FeedbackDTO;
import com.everhomes.rest.user.FeedbackHandleType;
import com.everhomes.rest.user.UpdateFeedbackCommand;
import com.everhomes.user.Feedback;
import com.everhomes.user.FeedbackHandler;
import com.everhomes.util.RuntimeErrorException;

@Component(FeedbackHandler.FEEDBACKHANDLER + FeedbackHandler.ALLIANCE_COMMENT)
public class AllianceCommentFeedbackHandler implements FeedbackHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(AllianceCommentFeedbackHandler.class);
	
	
	@Autowired
	public ServiceAllianceCommentProvider commentProvider;

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
		
		//进行评论删除
		ServiceAllianceComment comment = commentProvider.findServiceAllianceCommentById(feedback.getTargetId());
		comment.setStatus(CommonStatus.INACTIVE.getCode());
		//这里其实是更新状态。
		commentProvider.deleteServiceAllianceComment(comment);
	}

	@Override
	public void feedbackEvent(Feedback feedback) {

	}
}
