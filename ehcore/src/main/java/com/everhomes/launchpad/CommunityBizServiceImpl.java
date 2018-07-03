// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.banner.BannerService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.business.Business;
import com.everhomes.business.BusinessProvider;
import com.everhomes.business.BusinessService;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.core.AppConfig;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceDetail;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalService;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.business.BusinessDTO;
import com.everhomes.rest.business.BusinessTargetType;
import com.everhomes.rest.business.CancelFavoriteBusinessCommand;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.common.BizDetailActionData;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.launchpad.admin.*;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.OPPush;
import com.everhomes.rest.launchpadbase.indexconfigjson.Container;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.organization.GetOrgDetailCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.pm.ListPropCommunityContactCommand;
import com.everhomes.rest.organization.pm.PropCommunityContactDTO;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.statistics.transaction.SettlementErrorCode;
import com.everhomes.rest.ui.launchpad.*;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.user.*;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.scene.SceneService;
import com.everhomes.scene.SceneTypeInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.statistics.transaction.BizBusinessInfo;
import com.everhomes.statistics.transaction.ListBusinessInfoResponse;
import com.everhomes.statistics.transaction.ListModelInfoResponse;
import com.everhomes.statistics.transaction.StatTransactionConstant;
import com.everhomes.user.*;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.rest.ui.user.SceneType.PARK_TOURIST;
import static com.everhomes.rest.ui.user.SceneType.PM_ADMIN;

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
	public CommunityBizDTO findCommunityBizByCommunityId(FindCommunityBizByCommunityId cmd) {

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
