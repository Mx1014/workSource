// @formatter:off
package com.everhomes.forum;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.comment.CommentHandler;
import com.everhomes.rest.comment.*;
import com.everhomes.rest.comment.AttachmentDTO;
import com.everhomes.rest.comment.DeleteCommonCommentCommand;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(CommentHandler.COMMENT_OBJ_RESOLVER_PREFIX + OwnerTypeConstants.FORUM)
public class ForumCommentHandler implements CommentHandler {
	private static final Logger LOGGER=LoggerFactory.getLogger(ForumCommentHandler.class);

	@Autowired
	private ForumService forumService;
	@Autowired
	private ForumProvider forumProvider;

	@Override
	public CommentDTO addComment(AddCommentCommand cmd) {
		OwnerTokenDTO ownerTokenDto =  WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);

		Post post = this.forumProvider.findPostById(ownerTokenDto.getId());

		Byte interactFlag = forumService.getInteractFlagByPost(post);

		//不支持评论
		if(interactFlag == null || InteractFlag.fromCode(interactFlag) == InteractFlag.UNSUPPORT){
			User user = UserContext.current().getUser();
			forumService.sendMessageToUserWhenCommentNotSupport(user);

			throw RuntimeErrorException.errorWith(ForumLocalStringCode.SCOPE, ForumLocalStringCode.POST_COMMENT_NOT_SUPPORT,
					"comment not support");
		}

		NewCommentCommand forumCmd  = ConvertHelper.convert(cmd, NewCommentCommand.class);
		forumCmd.setTopicId(ownerTokenDto.getId());
		if(cmd.getAttachments() != null && cmd.getAttachments().size() > 0){

			forumCmd.setAttachments(new ArrayList<AttachmentDescriptor>());

			cmd.getAttachments().forEach(r -> {
				AttachmentDescriptor attachment = ConvertHelper.convert(r, AttachmentDescriptor.class);
				forumCmd.getAttachments().add(attachment);
			});
		}

		PostDTO postDTO = this.forumService.createComment(forumCmd);

		CommentDTO commentDto = toCommentDTO(postDTO);
		commentDto.setOwnerToken(cmd.getOwnerToken());

		//评论对接积分  add by yanjun 20171211
		// createPostCommentPoints(post.getId(), post.getModuleType(), post.getModuleCategoryId());

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(UserContext.currentUserId());
            context.setNamespaceId(UserContext.getCurrentNamespaceId());
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(postDTO.getId());
            event.setEventName(SystemEvent.FORM_COMMENT_CREATE.suffix(postDTO.getModuleType(), postDTO.getModuleCategoryId()));
        });
		return commentDto;
	}

	/*private void createPostCommentPoints(Long parentPostId, Byte parentModuleType, Long moduleCategoryId){
		String eventName = null;
		switch (ForumModuleType.fromCode(parentModuleType)){
			case FORUM:
				eventName = SystemEvent.FORM_COMMENT_CREATE.suffix(moduleCategoryId);
				break;
			case ACTIVITY:
				eventName = SystemEvent.ACTIVITY_COMMENT_CREATE.suffix(moduleCategoryId);
				break;
			case ANNOUNCEMENT:
				break;
			case CLUB:
				break;
			case GUILD:
				break;
			case FEEDBACK:
				break;
		}
		if(eventName == null){
			return;
		}

		final String finalEventName = eventName;

		Long  userId = UserContext.currentUserId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		LocalEventBus.publish(event -> {
			LocalEventContext context = new LocalEventContext();
			context.setUid(userId);
			context.setNamespaceId(namespaceId);
			event.setContext(context);

			event.setEntityType(EhForumPosts.class.getSimpleName());
			event.setEntityId(parentPostId);
			event.setEventName(finalEventName);
		});
	}*/

	@Override
	public ListCommentsResponse listComments(ListCommentsCommand cmd) {
		OwnerTokenDTO ownerTokenDto =  WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);
		ListTopicCommentCommand topicCmd = ConvertHelper.convert(cmd, ListTopicCommentCommand.class);
		topicCmd.setTopicId(ownerTokenDto.getId());


		ListPostCommandResponse cmdResponse = this.forumService.listTopicComments(topicCmd);

		List<CommentDTO> responseCommentDto = new ArrayList<CommentDTO>();
		if(cmdResponse != null && cmdResponse.getPosts() != null){
			cmdResponse.getPosts().forEach(p -> {

				CommentDTO commentDto = toCommentDTO(p);
				commentDto.setOwnerToken(cmd.getOwnerToken());
				responseCommentDto.add(commentDto);
			});
		}

		ListCommentsResponse response = new ListCommentsResponse();
		response.setCommentCount(cmdResponse.getCommentCount());
		response.setNextPageAnchor(cmdResponse.getNextPageAnchor());
		response.setCommentDtos(responseCommentDto);

		return response;
	}

	@Override
	public void deleteComment(DeleteCommonCommentCommand cmd) {
		this.forumService.deletePost(null, cmd.getId(), null, null, null);

		//删除评论对接积分 add by yanjun 20171211
		// deletePostCommentPoints(cmd.getId());

        Post tempPost = forumProvider.findPostById(cmd.getId());
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(UserContext.currentUserId());
            context.setNamespaceId(UserContext.getCurrentNamespaceId());
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(tempPost.getId());
            event.setEventName(SystemEvent.FORM_COMMENT_DELETE.suffix(tempPost.getModuleType(), tempPost.getModuleCategoryId()));
        });
	}


	/*private void deletePostCommentPoints(Long postCommentId){
		Post commentPost = forumProvider.findPostById(postCommentId);
		if(commentPost == null){
			return;
		}

		final Post post = forumProvider.findPostById(commentPost.getParentPostId());
		if(post == null){
			return;
		}


		String eventName = null;
		switch (ForumModuleType.fromCode(post.getModuleType())){
			case FORUM:
				eventName = SystemEvent.FORM_COMMENT_DELETE.suffix(post.getModuleCategoryId());
				break;
			case ACTIVITY:
				eventName = SystemEvent.ACTIVITY_COMMENT_DELETE.suffix(post.getModuleCategoryId());
				break;
			case ANNOUNCEMENT:
				break;
			case CLUB:
				break;
			case GUILD:
				break;
			case FEEDBACK:
				break;
		}
		if(eventName == null){
			return;
		}

		final String finalEventName = eventName;

		Long  userId = UserContext.currentUserId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		LocalEventBus.publish(event -> {
			LocalEventContext context = new LocalEventContext();
			context.setUid(userId);
			context.setNamespaceId(namespaceId);
			event.setContext(context);

			event.setEntityType(EhForumPosts.class.getSimpleName());
			event.setEntityId(post.getId());
			event.setEventName(finalEventName);
		});
	}*/

	private CommentDTO toCommentDTO(PostDTO postDTO){
		CommentDTO commentDto = ConvertHelper.convert(postDTO, CommentDTO.class);
		commentDto.setParentCommentId(postDTO.getParentPostId());
		if(postDTO.getAttachments() != null && postDTO.getAttachments().size() > 0){
			commentDto.setAttachments(new ArrayList<AttachmentDTO>());

			postDTO.getAttachments().forEach(r -> {
				AttachmentDTO attachment = ConvertHelper.convert(r, AttachmentDTO.class);
				attachment.setOwnerId(r.getPostId());
				commentDto.getAttachments().add(attachment);
			});
		}
		return commentDto;
	}
}
