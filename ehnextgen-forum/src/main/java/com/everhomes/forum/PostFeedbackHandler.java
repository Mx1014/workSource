package com.everhomes.forum;

import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.search.SearchContentConstants;
import com.everhomes.rest.ui.user.ContentBriefDTO;
import com.everhomes.rest.ui.user.TopicFootnote;
import com.everhomes.rest.user.*;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.user.Feedback;
import com.everhomes.user.FeedbackHandler;
import com.everhomes.user.UserActivityServiceImpl;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Component(FeedbackHandler.FEEDBACKHANDLER + FeedbackHandler.POST)
public class PostFeedbackHandler implements FeedbackHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserActivityServiceImpl.class);
	@Autowired
	private ForumProvider forumProvider;

	@Autowired
	private ForumService forumService;

	@Override
	public void beforeAddFeedback(FeedbackCommand cmd) {
		//如果post已经被删除，则不能在举报了。 add by yanjun 20170510
		Post post = forumProvider.findPostById(cmd.getTargetId());
		if(post != null && post.getStatus() != PostStatus.ACTIVE.getCode()){
			LOGGER.error("Forum post already deleted, " + "topicId=" + cmd.getTargetId());
			throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
					ForumServiceErrorCode.ERROR_FORUM_TOPIC_DELETED, "post was deleted");
		}
	}

	@Override
	public void afterAddFeedback(Feedback feedback) {
//		NewTopicCommand ntc = new NewTopicCommand();
//		ntc.setForumId(ForumConstants.FEEDBACK_FORUM);
//		ntc.setCreatorTag(EntityType.USER.getCode());
//		ntc.setTargetTag(EntityType.USER.getCode());
//		ntc.setContentCategory(CategoryConstants.CATEGORY_ID_TOPIC_COMMON);
//		ntc.setSubject(feedback.getSubject());
//		if(feedback.getProofResourceUri() == null || "".equals(feedback.getProofResourceUri()))
//			ntc.setContentType(PostContentType.TEXT.getCode());
//		else{
//			ntc.setContentType(PostContentType.IMAGE.getCode());
//		}
//		ntc.setContent(feedback.getContent());
//		ntc.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
//		ntc.setVisibleRegionId(0L);
//
//		forumService.createTopic(ntc);
	}

	@Override
	public void populateFeedbackDTO(FeedbackDTO dto) {
		//获取被举报对象的标题，默认取post，其他类型不管。
		Post post = forumProvider.findPostById(dto.getTargetId());
		if(post != null){
			dto.setTargetSubject(post.getSubject());
			dto.setForumId(post.getForumId());
			dto.setTargetStatus(post.getStatus());
		}
	}

	@Override
	public void beforeUpdateFeedback(UpdateFeedbackCommand cmd) {

	}

	@Override
	public void afterUpdateFeedback(Feedback feedback) {
		//当前只对post类型的举报做实际处理，处理的方式只有删除
		if(feedback.getHandleType() == FeedbackHandleType.DELETE.getCode()){
			Post post = forumProvider.findPostById(feedback.getTargetId());
			if(post != null){
				forumService.deletePost(post.getForumId(), post.getId(), null, null, null);
			}
		}
	}

	@Override
	public void feedbackEvent(Feedback feedback) {
		if (FeedbackVerifyType.fromStatus(feedback.getVerifyType()) == FeedbackVerifyType.FALSE) {
			return;
		}
		Post post = forumProvider.findPostById(feedback.getTargetId());
		if(post == null){
			return;
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId();

		LocalEventBus.publish(event -> {
			LocalEventContext context = new LocalEventContext();
			context.setUid(post.getCreatorUid());
			context.setNamespaceId(namespaceId);
			event.setContext(context);

			event.setEntityType(EhForumPosts.class.getSimpleName());
			event.setEntityId(post.getId());
			event.setEventName(SystemEvent.FORUM_POST_REPORT.suffix(
					post.getContentCategory(), post.getModuleType(), post.getModuleCategoryId()));
		});
	}
}
