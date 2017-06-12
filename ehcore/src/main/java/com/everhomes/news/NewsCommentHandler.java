// @formatter:off
package com.everhomes.news;
import com.everhomes.comment.CommentHandler;
import com.everhomes.rest.comment.*;
import com.everhomes.rest.comment.AttachmentDTO;
import com.everhomes.rest.comment.DeleteCommonCommentCommand;
import com.everhomes.rest.news.*;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.rest.ui.news.AddNewsCommentBySceneCommand;
import com.everhomes.rest.ui.news.AddNewsCommentBySceneResponse;
import com.everhomes.rest.ui.news.DeleteNewsCommentBySceneCommand;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(CommentHandler.COMMENT_OBJ_RESOLVER_PREFIX + OwnerTypeConstants.NEWS)
public class NewsCommentHandler implements CommentHandler {
	private static final Logger LOGGER=LoggerFactory.getLogger(NewsCommentHandler.class);

	@Autowired
	private NewsService newsService;

	@Override
	public CommentDTO addComment(AddCommentCommand cmd) {
		AddNewsCommentBySceneCommand newsCmd = toAddNewsCommentBySceneCommand(cmd);

		AddNewsCommentBySceneResponse newsCommentDTO = newsService.addNewsCommentByScene(newsCmd);
		//AddNewsCommentResponse newsCommentDTO = newsService.addNewsComment(newsCmd);

		CommentDTO commentDto = toCommentDTO(newsCommentDTO);
		commentDto.setOwnerToken(cmd.getOwnerToken());

		return commentDto;
	}

	@Override
	public ListCommentsResponse listComments(ListCommentsCommand cmd) {
		ListNewsCommentCommand newsCmd =  ConvertHelper.convert(cmd, ListNewsCommentCommand.class);
		newsCmd.setNewsToken(cmd.getOwnerToken());
		ListNewsCommentResponse listNewsCommentResponse = newsService.listNewsComment(newsCmd);

		List<CommentDTO> responseCommentDto = new ArrayList<CommentDTO>();
		if(listNewsCommentResponse != null && listNewsCommentResponse.getCommentList() != null&&listNewsCommentResponse.getCommentList().size()>0){
			listNewsCommentResponse.getCommentList().forEach(c -> {
				CommentDTO commentDto = toCommentDTO(c);
				commentDto.setOwnerToken(cmd.getOwnerToken());
				responseCommentDto.add(commentDto);
			});
		}


		ListCommentsResponse response = new ListCommentsResponse();
		response.setCommentCount(listNewsCommentResponse.getCommentCount());
		response.setNextPageAnchor(listNewsCommentResponse.getNextPageAnchor());
		response.setCommentDtos(responseCommentDto);

		return response;
	}

	@Override
	public void deleteComment(DeleteCommonCommentCommand cmd) {
		DeleteNewsCommentBySceneCommand newsCmd = ConvertHelper.convert(cmd, DeleteNewsCommentBySceneCommand.class);
		newsCmd.setNewsToken(cmd.getOwnerToken());
		newsService.deleteNewsCommentByScene(newsCmd);
	}

	private AddNewsCommentBySceneCommand toAddNewsCommentBySceneCommand(AddCommentCommand cmd){

		AddNewsCommentBySceneCommand newsCmd = ConvertHelper.convert(cmd, AddNewsCommentBySceneCommand.class);
		newsCmd.setNewsToken(cmd.getOwnerToken());

		if(cmd != null && cmd.getAttachments() != null){

			newsCmd.setAttachments(new ArrayList<AttachmentDescriptor>());

			cmd.getAttachments().forEach(r -> {
				AttachmentDescriptor attachment = ConvertHelper.convert(r, AttachmentDescriptor.class);
				newsCmd.getAttachments().add(attachment);
			});
		}
		return newsCmd;
	}

	private CommentDTO toCommentDTO(AddNewsCommentBySceneResponse newsComment){
		CommentDTO commentDto = ConvertHelper.convert(newsComment, CommentDTO.class);
		commentDto.setOwnerToken(newsComment.getNewsToken());
		if(newsComment.getAttachments() != null && newsComment.getAttachments().size() > 0){
			commentDto.setAttachments(new ArrayList<AttachmentDTO>());

			newsComment.getAttachments().forEach(r -> {
				AttachmentDTO attachment = ConvertHelper.convert(r, AttachmentDTO.class);
				commentDto.getAttachments().add(attachment);
			});
		}
		return commentDto;
	}

	private CommentDTO toCommentDTO(NewsCommentDTO newsCommentDTO){
		CommentDTO commentDto = ConvertHelper.convert(newsCommentDTO, CommentDTO.class);
		commentDto.setOwnerToken(newsCommentDTO.getNewsToken());
		if(newsCommentDTO != null && newsCommentDTO.getAttachments() != null){
			commentDto.setAttachments(new ArrayList<AttachmentDTO>());

			newsCommentDTO.getAttachments().forEach(r -> {
				AttachmentDTO attachment = ConvertHelper.convert(r, AttachmentDTO.class);
				commentDto.getAttachments().add(attachment);
			});
		}
		return commentDto;
	}
}
