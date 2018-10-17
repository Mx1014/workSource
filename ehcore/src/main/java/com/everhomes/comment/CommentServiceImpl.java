// @formatter:off
package com.everhomes.comment;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.controller.XssCleaner;
import com.everhomes.rest.comment.*;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class CommentServiceImpl implements CommentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

	@Override
	public CommentDTO addComment(AddCommentCommand cmd) {
		//xss过滤
		String content = XssCleaner.clean(cmd.getContent());
		cmd.setContent(content);

		OwnerTokenDTO ownerTokenDto =getOwnerTokenDTO(cmd.getOwnerToken());

		CommentHandler handler =  getCommentHandler(ownerTokenDto.getType());

		CommentDTO commentDto = null;
		if(handler != null){
			commentDto = handler.addComment(cmd);
		}

		return commentDto;
	}

	@Override
	public ListCommentsResponse listComments(ListCommentsCommand cmd) {
		OwnerTokenDTO ownerTokenDto =getOwnerTokenDTO(cmd.getOwnerToken());

		CommentHandler handler =  getCommentHandler(ownerTokenDto.getType());

		ListCommentsResponse listCommentsResponse = new ListCommentsResponse();
		if(handler != null){
			listCommentsResponse = handler.listComments(cmd);
		}

		return listCommentsResponse;
	}

	@Override
	public void deleteComment(DeleteCommonCommentCommand cmd) {
		OwnerTokenDTO ownerTokenDto =getOwnerTokenDTO(cmd.getOwnerToken());

		CommentHandler handler =  getCommentHandler(ownerTokenDto.getType());
		if(handler != null){
			handler.deleteComment(cmd);
		}
	}
	
	@Override
	public GetCommentsResponse getComment(GetCommentCommand cmd) {
		
		GetCommentsResponse resp = new GetCommentsResponse();
		
		OwnerTokenDTO ownerTokenDto =getOwnerTokenDTO(cmd.getOwnerToken());
		CommentHandler handler =  getCommentHandler(ownerTokenDto.getType());
		if(handler != null){
			resp = handler.getComment(cmd);
		}
		
		return resp;
	}

	/**
	 * 老的类型中ownerToken是Long类型的id，如快讯；新的类型封装的OwnerTokenDTO，包括type和id，如论坛。 add by yanjun 20170601
	 * @param ownerToken
	 * @return
	 */
	private OwnerTokenDTO getOwnerTokenDTO(String ownerToken){
		OwnerTokenDTO ownerTokenDto = new OwnerTokenDTO();
		try{
			ownerTokenDto =  WebTokenGenerator.getInstance().fromWebToken(ownerToken, OwnerTokenDTO.class);
		}catch (Exception e){
			Long ownerTokenId =  WebTokenGenerator.getInstance().fromWebToken(ownerToken, Long.class);
			ownerTokenDto.setType(OwnerType.NEWS.getCode());
			ownerTokenDto.setId(ownerTokenId);
		}
		return ownerTokenDto;
	}

	private CommentHandler getCommentHandler(Byte ownerTypeCode) {
		CommentHandler handler = null;

		if(ownerTypeCode != null) {
			String handlerPrefix = CommentHandler.COMMENT_OBJ_RESOLVER_PREFIX;
			handler = PlatformContext.getComponent(handlerPrefix + ownerTypeCode);
		}
		return handler;
	}
}
