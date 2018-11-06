// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.common.OwnerType;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class LaunchPadConfigServiceImpl implements LaunchPadConfigService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LaunchPadConfigServiceImpl.class);

	@Autowired
	private LaunchPadConfigProvider launchPadConfigProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Override
	public LaunchPadConfigDTO updateLaunchPadConfig(UpdateLaunchPadConfigCommand cmd) {


		if(OwnerType.fromCode(cmd.getOwnerType()) == null || cmd.getOwnerId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter, cmd = {}", cmd);
		}

		LaunchPadConfig launchPadConfig = launchPadConfigProvider.findLaunchPadConfig(cmd.getOwnerType(), cmd.getOwnerId());

		if(launchPadConfig == null){
			launchPadConfig = ConvertHelper.convert(cmd, LaunchPadConfig.class);
			launchPadConfig.setCreateTime(new Timestamp(System.currentTimeMillis()));
			launchPadConfig.setUpdateTime(new Timestamp(System.currentTimeMillis()));

			launchPadConfigProvider.createLaunchPadConfig(launchPadConfig);

		}else {
			launchPadConfig.setNavigatorAllIconUri(cmd.getNavigatorAllIconUri());
			launchPadConfig.setUpdateTime(new Timestamp(System.currentTimeMillis()));

			launchPadConfigProvider.updateLaunchPadConfig(launchPadConfig);
		}


		LaunchPadConfigDTO dto = toDto(launchPadConfig);

		return dto;
	}

	@Override
	public LaunchPadConfigDTO findLaunchPadConfig(FindLaunchPadConfigCommand cmd) {
		if(OwnerType.fromCode(cmd.getOwnerType()) == null && cmd.getOwnerId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter, cmd = {}", cmd);
		}

		LaunchPadConfig launchPadConfig = launchPadConfigProvider.findLaunchPadConfig(cmd.getOwnerType(), cmd.getOwnerId());

		LaunchPadConfigDTO dto = toDto(launchPadConfig);

		return dto;

	}



	@Override
	public void deleteLaunchPadConfig(DeleteLaunchPadConfigCommand cmd) {
		if(cmd.getId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter, cmd = {}", cmd);
		}

		launchPadConfigProvider.deleteById(cmd.getId());
	}

	private LaunchPadConfigDTO toDto(LaunchPadConfig launchPadConfig){

		if(launchPadConfig == null){
			return null;
		}

		LaunchPadConfigDTO dto = ConvertHelper.convert(launchPadConfig, LaunchPadConfigDTO.class);

		if(dto.getNavigatorAllIconUri() != null){
			String url = contentServerService.parserUri(dto.getNavigatorAllIconUri(), dto.getClass().getSimpleName(), dto.getId());
			dto.setNavigatorAllIconUrl(url);
		}

		return dto;
	}
}
