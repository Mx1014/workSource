package com.everhomes.user;

import com.everhomes.rest.user.FeedbackCommand;
import com.everhomes.rest.user.FeedbackDTO;
import com.everhomes.rest.user.UpdateFeedbackCommand;

public interface FeedbackHandler {

    String FEEDBACKHANDLER = "FeedbackHandler-";
    String POST = "1";
    String NEWS = "4";

    void beforeAddFeedback(FeedbackCommand cmd);

    void afterAddFeedback(Feedback feedback);

    void populateFeedbackDTO(FeedbackDTO dto);

    void beforeUpdateFeedback(UpdateFeedbackCommand cmd);

    void afterUpdateFeedback(Feedback feedback);
}
