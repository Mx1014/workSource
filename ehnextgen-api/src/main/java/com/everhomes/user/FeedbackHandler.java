package com.everhomes.user;

import com.everhomes.rest.user.FeedbackCommand;
import com.everhomes.rest.user.FeedbackDTO;
import com.everhomes.rest.user.UpdateFeedbackCommand;

public interface FeedbackHandler {

    String FEEDBACKHANDLER = "FeedbackHandler-";
    String POST = "1";
    
    String NEWS = "4";
    String NEWS_COMMENT = "41";  //园区快讯评论
    
    String ALLIANCE = "5"; //占位，暂不用
    String ALLIANCE_COMMENT = "51";

    /**
     * 举报前置方法
     * @param cmd
     */
    void beforeAddFeedback(FeedbackCommand cmd);

    /**
     * 举报后置方法
     * @param feedback
     */
    void afterAddFeedback(Feedback feedback);

    /**
     * 举报DTO对象的填充
     * @param dto
     */
    void populateFeedbackDTO(FeedbackDTO dto);

    /**
     * 举报处理的前置方法
     * @param cmd
     */
    void beforeUpdateFeedback(UpdateFeedbackCommand cmd);

    /**
     * 举报处理的后置方法
     * @param feedback
     */
    void afterUpdateFeedback(Feedback feedback);

    /**
     * 举报被处理后的触发事件（比如积分被减少等）
     * @param feedback
     */
    void feedbackEvent(Feedback feedback);
}
