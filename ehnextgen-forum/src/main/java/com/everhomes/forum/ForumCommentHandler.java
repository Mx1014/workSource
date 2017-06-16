// @formatter:off
package com.everhomes.forum;
import com.everhomes.comment.CommentHandler;
import com.everhomes.rest.comment.*;
import com.everhomes.rest.comment.AttachmentDTO;
import com.everhomes.rest.comment.DeleteCommonCommentCommand;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.util.ConvertHelper;
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

	@Override
	public CommentDTO addComment(AddCommentCommand cmd) {
		OwnerTokenDTO ownerTokenDto =  WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);

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

		return commentDto;
	}

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
	}


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
