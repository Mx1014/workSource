// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.user.*;
import com.everhomes.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommunityBizServiceImpl implements CommunityBizService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommunityBizServiceImpl.class);

	@Autowired
	private CommunityBizProvider communityBizProvider;

	@Autowired
	private ContentServerService contentServerService;


	@Override
	public CommunityBizDTO CreateCommunityBiz(CreateCommunityBiz cmd) {

		CommunityBiz communityBiz = ConvertHelper.convert(cmd, CommunityBiz.class);

		CommunityBiz oldCommunityBiz = communityBizProvider.findCommunityBiz(cmd.getCommunityId(), null);
		if(oldCommunityBiz != null){
			communityBiz.setId(oldCommunityBiz.getId());
			communityBizProvider.updateCommunityBiz(communityBiz);
		}else {
			communityBizProvider.createCommunityBiz(communityBiz);
		}

		CommunityBizDTO dto = toDto(communityBiz);

		return dto;
	}

	@Override
	public CommunityBizDTO updateCommunityBiz(updateCommunityBiz cmd) {

		CommunityBiz communityBiz = ConvertHelper.convert(cmd, CommunityBiz.class);

		CommunityBiz oldCommunityBiz = communityBizProvider.getCommunityBizById(cmd.getId());

		if(oldCommunityBiz == null && cmd.getCommunityId() != null){
			oldCommunityBiz = communityBizProvider.findCommunityBiz(cmd.getCommunityId(), null);
		}

		if(oldCommunityBiz != null){
			communityBiz.setId(oldCommunityBiz.getId());
			communityBizProvider.updateCommunityBiz(communityBiz);
		}else {
			communityBizProvider.createCommunityBiz(communityBiz);
		}

		CommunityBizDTO dto = toDto(communityBiz);

		return dto;
	}

	@Override
	public void deleteCommunityBiz(DeleteCommunityBiz cmd) {

		communityBizProvider.deleteCommunityBiz(cmd.getId());
	}

	@Override
	public CommunityBizDTO findCommunityBiz(FindCommunityBiz cmd) {

		if(cmd.getCommunityId() == null){
			return null;
		}

		CommunityBiz communityBiz = communityBizProvider.findCommunityBiz(cmd.getCommunityId(), null);
		CommunityBizDTO dto = toDto(communityBiz);

		return dto;
	}


	private CommunityBizDTO toDto(CommunityBiz communityBiz){
		CommunityBizDTO dto = ConvertHelper.convert(communityBiz, CommunityBizDTO.class);

		if(dto.getLogoUri() != null){
			String url = contentServerService.parserUri(dto.getLogoUri(), dto.getClass().getSimpleName(), dto.getId());
			dto.setLogoUrl(url);
		}

		return dto;
	}


	@Override
	public CommunityBizDTO findCommunityBizForApp() {

		AppContext appContext = UserContext.current().getAppContext();
		CommunityBiz communityBiz = communityBizProvider.findCommunityBiz(appContext.getCommunityId(), CommunityBizStatus.ENABLE.getCode());
		CommunityBizDTO dto = toDto(communityBiz);
		return dto;
	}
}
