package com.everhomes.richtext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.richtext.GetRichTextByTokenCommand;
import com.everhomes.rest.richtext.GetRichTextCommand;
import com.everhomes.rest.richtext.RichTextDTO;
import com.everhomes.rest.richtext.RichTextTokenDTO;
import com.everhomes.rest.richtext.UpdateRichTextCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.WebTokenGenerator;

@Component
public class RichTextServiceImpl implements RichTextService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RichTextServiceImpl.class);
	
	@Autowired
	private RichTextProvider richTextProvider;

	@Override
	public void updateRichText(UpdateRichTextCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		RichText richText = richTextProvider.findRichText(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getResourceType(), namespaceId);

		if(null == richText) {
			RichText rt = ConvertHelper.convert(cmd, RichText.class);
			rt.setNamespaceId(namespaceId);
			richTextProvider.createRichText(rt);
		} else {
			RichText rt = ConvertHelper.convert(cmd, RichText.class);
			rt.setId(richText.getId());
			rt.setNamespaceId(namespaceId);
			rt.setCreateTime(richText.getCreateTime());
			richTextProvider.updateRichText(rt);
		}
	}

	@Override
	public RichTextDTO getRichText(GetRichTextCommand cmd) {

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		RichTextDTO dto = getRichText(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getResourceType(), namespaceId);
		return dto;
	}

	@Override
	public RichTextDTO getRichTextByToken(GetRichTextByTokenCommand cmd) {
		
		WebTokenGenerator webToken = WebTokenGenerator.getInstance();
		RichTextTokenDTO richTextToken = webToken.fromWebToken(cmd.getRtToken(), RichTextTokenDTO.class);
		
		RichTextDTO dto = getRichText(richTextToken.getOwnerType(), richTextToken.getOwnerId(), richTextToken.getResourceType(), richTextToken.getNamespaceId());
		return dto;
	}
	
	private RichTextDTO getRichText(String ownerType, Long ownerId, String resourceType, Integer namespaceId) {
		
		RichText richText = richTextProvider.findRichText(ownerType, ownerId, resourceType, namespaceId);
		if(null == richText) {
			richText = richTextProvider.getDefaultRichText(resourceType);
		}
		
		richText.setOwnerId(ownerId);
		richText.setOwnerType(ownerType);
		richText.setResourceType(resourceType);
		richText.setNamespaceId(namespaceId);
		RichTextDTO dto = ConvertHelper.convert(richText, RichTextDTO.class);
		
		return dto;
	}

}
