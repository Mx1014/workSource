// @formatter:off
package com.everhomes.yellowPage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.comment.CommentHandler;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.Attachment;
import com.everhomes.forum.Post;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.comment.AddCommentCommand;
import com.everhomes.rest.comment.AttachmentDTO;
import com.everhomes.rest.comment.AttachmentDescriptor;
import com.everhomes.rest.comment.CommentDTO;
import com.everhomes.rest.comment.DeleteCommonCommentCommand;
import com.everhomes.rest.comment.ListCommentsCommand;
import com.everhomes.rest.comment.ListCommentsResponse;
import com.everhomes.rest.comment.OwnerTokenDTO;
import com.everhomes.rest.comment.OwnerTypeConstants;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceOwnerType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.WebTokenGenerator;

@Component(CommentHandler.COMMENT_OBJ_RESOLVER_PREFIX + OwnerTypeConstants.SERVICEALLIANCE)
public class ServiceAllianceCommentHandler implements CommentHandler {
	private static final Logger LOGGER=LoggerFactory.getLogger(ServiceAllianceCommentHandler.class);
	
	@Autowired
	public ServiceAllianceCommentProvider commentProvider;
	
	@Autowired
	public ServiceAllianceCommentAttachmentProvider commentAttachmentProvider;
	
	@Autowired
	public ConfigurationProvider configurationProvider;
	
	@Autowired
	public UserProvider userProvider;
	
	@Autowired
	public ContentServerService contentServerService;
	
	@Autowired
	private DbProvider dbProvider;
	

	@Override
	public CommentDTO addComment(AddCommentCommand cmd) {
		OwnerTokenDTO ownerTokenDto =  WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);
		
		//创建这个什么评论
		ServiceAllianceComment createdComment = this.dbProvider.execute(r->{
			ServiceAllianceComment comment = this.processComments(cmd,ownerTokenDto);
			commentProvider.createServiceAllianceComment(comment);
			List<ServiceAllianceCommentAttachment> atts = this.processCommentAttachments(cmd,ownerTokenDto,comment);
			for (ServiceAllianceCommentAttachment att : atts) {
				commentAttachmentProvider.createServiceAllianceCommentAttachment(att);
			}
			comment.setAtts(atts);
			return comment;
		});
		
		List<CommentDTO> list = processDtos(new ArrayList<ServiceAllianceComment>(Arrays.asList(new ServiceAllianceComment[]{createdComment})),createdComment.getAtts(), cmd.getOwnerToken());
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	private List<ServiceAllianceCommentAttachment> processCommentAttachments(AddCommentCommand cmd,
			OwnerTokenDTO ownerTokenDto,ServiceAllianceComment comment) {
		List<ServiceAllianceCommentAttachment> list = new ArrayList<ServiceAllianceCommentAttachment>();
		if(cmd.getAttachments() == null){
			return list;
		}
		for (AttachmentDescriptor descriptor : cmd.getAttachments()) {
			ServiceAllianceCommentAttachment att = ConvertHelper.convert(descriptor, ServiceAllianceCommentAttachment.class);
			att.setNamespaceId(UserContext.getCurrentNamespaceId());
			att.setOwnerId(comment.getId());
			att.setStatus(CommonStatus.ACTIVE.getCode());
			list.add(att);
		}
		return list;
	}

	private ServiceAllianceComment processComments(AddCommentCommand cmd, OwnerTokenDTO ownerTokenDto) {
		ServiceAllianceComment comment = ConvertHelper.convert(cmd, ServiceAllianceComment.class);
		comment.setNamespaceId(UserContext.getCurrentNamespaceId());
		comment.setOwnerId(ownerTokenDto.getId());
		comment.setOwnerType(ServiceAllianceOwnerType.SERVICE_ALLIANCE.getCode());
		comment.setStatus(CommonStatus.ACTIVE.getCode());
		return comment;
	}

	@Override
	public ListCommentsResponse listComments(ListCommentsCommand cmd) {
		OwnerTokenDTO ownerTokenDto =  WebTokenGenerator.getInstance().fromWebToken(cmd.getOwnerToken(), OwnerTokenDTO.class);
		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		//评论集合
		List<ServiceAllianceComment> list = commentProvider.listServiceAllianceCommentByOwner(UserContext.getCurrentNamespaceId(), ServiceAllianceOwnerType.SERVICE_ALLIANCE.getCode(),
				ownerTokenDto.getId(),cmd.getPageAnchor(),pageSize);
		
		//评论附件
		List<Long> commentIds = list.stream().map(r->r.getId()).collect(Collectors.toList());
		List<ServiceAllianceCommentAttachment> attachments = commentAttachmentProvider.listServiceAllianceCommentAttachment(UserContext.getCurrentNamespaceId(),commentIds);
		
		ListCommentsResponse response = new ListCommentsResponse();
		// 评论数量
		String key = String.valueOf(ownerTokenDto.getId());
		Map<String,Integer> countmap = commentProvider.listServiceAllianceCommentCountByOwner(UserContext.getCurrentNamespaceId(), ServiceAllianceOwnerType.SERVICE_ALLIANCE.getCode(),
				new ArrayList<Long>(Arrays.asList(new Long[]{ownerTokenDto.getId()})));
		response.setCommentCount(Long.valueOf(countmap.get(key)==null?0:countmap.get(key)));
		//锚点
		if(list!=null && list.size()>=pageSize){
			response.setNextPageAnchor(list.get(pageSize-1).getId());
		}
		response.setCommentDtos(processDtos(list,attachments,cmd.getOwnerToken()));
		return response;
	}

	private List<CommentDTO> processDtos(List<ServiceAllianceComment> list,
			List<ServiceAllianceCommentAttachment> attachments,String ownerToken) {
		Map<String,List<ServiceAllianceCommentAttachment>> map = new HashMap<String,List<ServiceAllianceCommentAttachment>>();
		attachments.stream().forEach(r->{
			List<ServiceAllianceCommentAttachment> atts = map.get(String.valueOf(r.getId()));
			if(atts == null){
				atts = new ArrayList<ServiceAllianceCommentAttachment>();
				map.put(String.valueOf(r.getId()), atts);
			}
			atts.add(r);
		});
		
		List<CommentDTO> dtos = list.stream().map(r->{
			CommentDTO dto = ConvertHelper.convert(r, CommentDTO.class);
			dto.setOwnerToken(ownerToken);
			
			// 设置头像等,这里有待优化
			User creator = userProvider.findUserById(r.getCreatorUid());
			dto.setCreatorAvatar(creator.getAvatar());
			dto.setCreatorNickName(creator.getNickName());
			dto.setCreatorAvatar(creator.getAvatar());
			String creatorAvatar = creator.getAvatar();
	        if(StringUtils.isEmpty(creatorAvatar)) {
	        	creatorAvatar = configurationProvider.getValue(creator.getNamespaceId(), "user.avatar.default.url", "");
	        }
	        if(creatorAvatar != null && creatorAvatar.length() > 0) {
	            String avatarUrl = getResourceUrlByUir(r.getCreatorUid(), creatorAvatar, 
	                EntityType.USER.getCode(), r.getCreatorUid());
	            dto.setCreatorAvatarUrl(avatarUrl);
	        }
	        
	        // 设置附件等。
	        List<ServiceAllianceCommentAttachment> atts = map.get(String.valueOf(r.getId()));
	        if(atts!=null){
	        	dto.setAttachments(atts.stream().map(att -> {
	        		populatePostAttachement(r.getCreatorUid(), r.getOwnerId(), att);
	        		AttachmentDTO attdto =  ConvertHelper.convert(att, AttachmentDTO.class);
	        		return attdto;
	        	}).collect(Collectors.toList()));
	        }
			return dto;
			
		}).collect(Collectors.toList());
		return dtos;
	}
	
    private void populatePostAttachement(long userId, Long serviceAllianceId, ServiceAllianceCommentAttachment attachment) {
        if(attachment == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post attachment is null, userId=" + userId + ", serviceAllianceId=" + serviceAllianceId);
            }
        } else {
            String contentUri = attachment.getContentUri();
            if(contentUri != null && contentUri.length() > 0) {
                try{
                    String url = contentServerService.parserUri(contentUri, EntityType.TOPIC.getCode(), serviceAllianceId);
                    attachment.setContentUrl(url);
                    
                    ContentServerResource resource = contentServerService.findResourceByUri(contentUri);
                    if(resource != null) {
                        attachment.setSize(resource.getResourceSize());
                        attachment.setMetadata(resource.getMetadata());
                    }
                }catch(Exception e){
                    LOGGER.error("Failed to parse attachment uri, userId=" + userId 
                        + ", serviceAllianceId=" + serviceAllianceId + ", attachmentId=" + attachment.getId(), e);
                }
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("The content uri is empty, userId=" + userId + ", attchmentId=" + attachment.getId());
                }
            }
        }
    }
	
	  private String getResourceUrlByUir(long userId, String uri, String ownerType, Long ownerId) {
	        String url = null;
	        if(uri != null && uri.length() > 0) {
	            try{
	                url = contentServerService.parserUri(uri, ownerType, ownerId);
	            }catch(Exception e){
	                LOGGER.error("Failed to parse uri, userId=" + userId + ", uri=" + uri 
	                    + ", ownerType=" + ownerType + ", ownerId=" + ownerId, e);
	            }
	        }
	        
	        return url;
	    }

	@Override
	public void deleteComment(DeleteCommonCommentCommand cmd) {
		ServiceAllianceComment comment = commentProvider.findServiceAllianceCommentById(cmd.getId());
		comment.setStatus(CommonStatus.INACTIVE.getCode());
		//这里其实是更新状态。
		this.commentProvider.deleteServiceAllianceComment(comment);
	}
}
