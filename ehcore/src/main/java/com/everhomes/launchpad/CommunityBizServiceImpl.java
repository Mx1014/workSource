// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.common.TrueOrFalseFlag;
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

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private OrganizationProvider organizationProvider;


	@Override
	public CommunityBizDTO CreateCommunityBiz(CreateCommunityBizCommand cmd) {


		if(cmd.getCommunityId() == null && cmd.getOrganizationId() == null){
			throw RuntimeErrorException.errorWith("communityBiz", 1001,
					"organizationId and communityId is null");
		}

		if(cmd.getOrganizationId() != null && cmd.getCommunityId() != null){
			throw RuntimeErrorException.errorWith("communityBiz", 1002,
					"organizationId and communityId duplicate");
		}

		CommunityBiz communityBiz = ConvertHelper.convert(cmd, CommunityBiz.class);

		CommunityBiz oldCommunityBiz = communityBizProvider.findCommunityBiz(cmd.getOrganizationId(), cmd.getCommunityId(), null);



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
	public CommunityBizDTO updateCommunityBiz(UpdateCommunityBizCommand cmd) {


		if(cmd.getCommunityId() == null && cmd.getOrganizationId() == null){
			throw RuntimeErrorException.errorWith("communityBiz", 1001,
					"organizationId and communityId is null");
		}

		if(cmd.getOrganizationId() != null && cmd.getCommunityId() != null){
			throw RuntimeErrorException.errorWith("communityBiz", 1002,
					"organizationId and communityId duplicate");
		}

		CommunityBiz communityBiz = ConvertHelper.convert(cmd, CommunityBiz.class);


		//查询已有的电商配置
		CommunityBiz oldCommunityBiz = communityBizProvider.getCommunityBizById(cmd.getId());
		//查询已有的电商配置
		if(oldCommunityBiz == null && cmd.getCommunityId() != null){
			oldCommunityBiz = communityBizProvider.findCommunityBiz(cmd.getOrganizationId(), cmd.getCommunityId(), null);
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
	public void deleteCommunityBiz(DeleteCommunityBizCommand cmd) {

		communityBizProvider.deleteCommunityBiz(cmd.getId());
	}

	@Override
	public CommunityBizDTO findCommunityBiz(FindCommunityBizCommand cmd) {

		if(cmd.getCommunityId() == null && cmd.getOrganizationId() == null){
			throw RuntimeErrorException.errorWith("communityBiz", 1001,
					"organizationId and communityId is null");
		}

		if(cmd.getOrganizationId() != null && cmd.getCommunityId() != null){
			throw RuntimeErrorException.errorWith("communityBiz", 1002,
					"organizationId and communityId duplicate");
		}


		CommunityBiz communityBiz = communityBizProvider.findCommunityBiz(cmd.getOrganizationId(), cmd.getCommunityId(), null);
		CommunityBizDTO dto = toDto(communityBiz);

		return dto;
	}


	private CommunityBizDTO toDto(CommunityBiz communityBiz){

		if(communityBiz == null){
			return null;
		}

		CommunityBizDTO dto = ConvertHelper.convert(communityBiz, CommunityBizDTO.class);

		if(dto.getLogoUri() != null){
			String url = contentServerService.parserUri(dto.getLogoUri(), dto.getClass().getSimpleName(), dto.getId());
			dto.setLogoUrl(url);
		}

		return dto;
	}


	@Override
	public CommunityBizDTO findCommunityBizForApp(FindCommunityBizForAppCommand cmd) {

		Community community = communityProvider.findCommunityById(cmd.getCommunityId());

		if(community == null){
			throw RuntimeErrorException.errorWith("communityBiz", 1003,
					"invalid communityId");
		}

		//判断使用默认的还是园区独立配置的
		Long communityId = community.getId();
		Long organizationId = null;
		if(TrueOrFalseFlag.fromCode(community.getAppSelfConfigFlag()) == TrueOrFalseFlag.FALSE){
			OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationProperty(communityId);
			if(organizationCommunity != null){
				communityId = null;
				organizationId  = organizationCommunity.getOrganizationId();
			}
		}

		CommunityBiz communityBiz = communityBizProvider.findCommunityBiz(organizationId, communityId, CommunityBizStatus.ENABLE.getCode());
		CommunityBizDTO dto = toDto(communityBiz);
		return dto;
	}
}
