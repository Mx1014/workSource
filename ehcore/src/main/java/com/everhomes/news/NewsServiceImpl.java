// @formatter:off
package com.everhomes.news;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.family.FamilyProvider;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.common.TagSearchItem;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.community.CommunityFetchType;
import com.everhomes.rest.enterprise.GetAuthOrgByProjectIdAndAppIdCommand;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.news.*;
import com.everhomes.rest.news.open.CreateOpenNewsCommand;
import com.everhomes.rest.news.open.ListOpenNewsCommand;
import com.everhomes.rest.news.open.ListOpenNewsResponse;
import com.everhomes.rest.news.open.OpenBriefNewsDTO;
import com.everhomes.rest.news.open.UpdateOpenNewsCommand;
import com.everhomes.rest.news.open.CreateOpenNewsCommand;
import com.everhomes.rest.news.open.ListOpenNewsCommand;
import com.everhomes.rest.news.open.ListOpenNewsResponse;
import com.everhomes.rest.news.open.OpenBriefNewsDTO;
import com.everhomes.rest.news.open.UpdateOpenNewsCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.NewsInstanceConfig;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.user.*;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalItemGroup;
import com.everhomes.portal.PortalItemGroupProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.ui.news.AddNewsCommentBySceneCommand;
import com.everhomes.rest.ui.news.AddNewsCommentBySceneResponse;
import com.everhomes.rest.ui.news.DeleteNewsCommentBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneResponse;
import com.everhomes.rest.ui.news.SetNewsLikeFlagBySceneCommand;
import com.everhomes.rest.ui.user.ContentBriefDTO;
import com.everhomes.rest.ui.user.NewsFootnote;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneReponse;
import com.everhomes.rest.user.UserLikeType;
import com.everhomes.search.SearchProvider;
import com.everhomes.search.SearchUtils;
import com.everhomes.server.schema.tables.pojos.EhNewsAttachments;
import com.everhomes.server.schema.tables.pojos.EhNewsComment;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

@Component
public class NewsServiceImpl implements NewsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);

	private static final Long NEWS_MODULE_ID = 10800L;

	private static final Integer NEWS_CONTENT_ABSTRACT_DEFAULT_LEN = 100;

	private final byte TYPE_CREATE_BY_ADMIN = 0; //后台更新
	private final byte TYPE_CREATE_BY_THIRD_PARTY = 1; //第三方更新 见NewsOpenController

	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private NewsProvider newsProvider;

	@Autowired
	private CommentProvider commentProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private AttachmentProvider attachmentProvider;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private SearchProvider searchProvider;

	@Autowired
	private UserActivityProvider userActivityProvider;

	@Autowired
	private ConfigurationProvider configProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;

	@Autowired
	private ServiceModuleService serviceModuleService;

	@Autowired
	private PortalVersionProvider portalVersionProvider;

	@Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;
	
	@Autowired
	private PortalItemGroupProvider portalItemGroupProvider;

	@Autowired
	PortalService portalService;

	@Autowired
	OrganizationService organizationService;

	@Autowired
	private FamilyProvider familyProvider;

	@Override
	public CreateNewsResponse createNews(CreateNewsCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null
				&& configProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(),
					1080010800L, cmd.getAppId(), null, cmd.getOwnerId());// 全部权限
		}

		final Long userId = UserContext.current().getUser().getId();

		// 检查参数等信息
		// checkNewsParameter(userId, cmd);

		// 黑名单权限校验 by sfyan20161213
		checkBlacklist(null, null);

		Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());

		// 检查用户的项目权限是否合法
		checkUserProjectLegal(userId, cmd.getCurrentPMId(), cmd.getOwnerId(), cmd.getAppId());

		// 创建新闻
		News news = createNewsByAdmin(namespaceId, cmd);

		CreateNewsResponse response = ConvertHelper.convert(news, CreateNewsResponse.class);
		response.setNewsToken(WebTokenGenerator.getInstance().toWebToken(news.getId()));
		return response;
	}

	private News createNewsByAdmin(Integer namespaceId, CreateNewsCommand cmd) {
		return createNews(namespaceId, cmd, TYPE_CREATE_BY_ADMIN);
	}

	@Override
	public News createNewsByOpenApi(Integer namespaceId, CreateOpenNewsCommand cmd) {
		return createNews(namespaceId, cmd, TYPE_CREATE_BY_THIRD_PARTY);
	}

	private News createNews(Integer namespaceId, Object cmdObject, byte createType) {

		// 准备创建的News
		News news = buildCreateNews(namespaceId, cmdObject, createType);

		// 准备创建的tags
		List<Long> newsTagIds = null;
		List<Long> communityIds = null;
		if (TYPE_CREATE_BY_ADMIN == createType) {
			CreateNewsCommand cmd = (CreateNewsCommand) cmdObject;
			if (null != cmd.getNewsTagVals()) {
				newsTagIds = cmd.getNewsTagVals().stream().map(r -> {
					return r.getNewsTagId();
				}).collect(Collectors.toList());
			}

			communityIds = cmd.getCommunityIds();
		} else {
			CreateOpenNewsCommand cmd = (CreateOpenNewsCommand) cmdObject;
			newsTagIds = cmd.getNewsTagIds();
			communityIds = cmd.getProjectIds();
		}

		//标准版时默认communityId为ownerId
		if (StringUtils.isEmpty(communityIds)) {
			communityIds = Arrays.asList(news.getOwnerId());
		}

		createNews(news, communityIds, newsTagIds, createType);
		return news;
	}

	private void createNews(News news, List<Long> communityIds, List<Long> newsTagIds, byte createType) {

		news.setCreateType(createType);

		dbProvider.execute((TransactionStatus status) -> {
			newsProvider.createNews(news);
			createCommunityIds(news.getId(), communityIds);
			createNewsTagVals(news.getId(), newsTagIds);
			return null;

		});

		syncNews(news.getId());
	}

	private void createNewsTagVals(Long newsId, List<Long> newsTagIds) {
		processNewsTagVals(newsId, newsTagIds, false);
	}

	private void processNewsTagVals(Long newsId, List<Long> newsTagIds, boolean isUpdate) {
		if (CollectionUtils.isEmpty(newsTagIds)) {
			return;
		}

		if (isUpdate) {
			newsProvider.deletNewsTagVals(newsId);
		}

		newsTagIds.forEach(r -> {
			if (r != null) {
				NewsTagVals val = new NewsTagVals();
				val.setNewsTagId(r);
				val.setNewsId(newsId);
				newsProvider.createNewsTagVals(val);
			}
		});
	}

	private void createCommunityIds(Long newsId, List<Long> communityIds) {
		processCommunityIds(newsId, communityIds, false);
	}

	private void processCommunityIds(Long newsId, List<Long> communityIds, boolean isUpdate) {
		if (CollectionUtils.isEmpty(communityIds)) {
			return;
		}

		if (isUpdate) {
			newsProvider.deleteNewsCommunity(newsId);
		}

		communityIds.forEach(m -> {
			NewsCommunity newsCommunity = new NewsCommunity();
			newsCommunity.setNewsId(newsId);
			newsCommunity.setCommunityId(m);
			newsProvider.createNewsCommunity(newsCommunity);
		});
	}

	@Override
	public void updateNews(UpdateNewsCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null
				&& configProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(),
					1080010800L, cmd.getAppId(), null, cmd.getOwnerId());// 全部权限
		}

		// 获取要修改的news
		News news = newsProvider.findNewsById(cmd.getId());
		if (null == news) {
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID, "news is not exist");
		}

		// 进行权限判断
		final Long userId = UserContext.current().getUser().getId();
		checkUserProjectLegal(userId, cmd.getCurrentPMId(), news.getOwnerId(), cmd.getAppId());

		// 更新操作
		updateNewsByAdmin(news, cmd);
	}

	private void updateNewsByAdmin(News originNews, UpdateNewsCommand cmd) {
		updateNews(originNews, cmd, TYPE_CREATE_BY_ADMIN);
	}

	@Override
	public void updateNewsByOpenApi(News originNews, UpdateOpenNewsCommand cmd) {
		updateNews(originNews, cmd, TYPE_CREATE_BY_THIRD_PARTY);
	}

	public void updateNews(News originNews, Object cmdObject, byte createType) {

		// 准备创建的News
		buildUpdateNews(originNews, cmdObject, createType);

		// 准备创建的tags
		List<Long> newsTagIds = null;
		List<Long> communityIds = null;
		if (TYPE_CREATE_BY_ADMIN == createType) {
			UpdateNewsCommand cmd = (UpdateNewsCommand) cmdObject;
			if (null != cmd.getNewsTagVals()) {
				newsTagIds = cmd.getNewsTagVals().stream().map(r -> {
					return r.getNewsTagId();
				}).collect(Collectors.toList());
			}

			communityIds = cmd.getCommunityIds();
		} else {
			UpdateOpenNewsCommand cmd = (UpdateOpenNewsCommand) cmdObject;
			newsTagIds = cmd.getNewsTagIds();
			communityIds = cmd.getProjectIds();
		}

		// 实际更新
		updateNews(originNews, communityIds, newsTagIds);

		return;
	}

	private void buildUpdateNews(News originNews, Object cmdObject, byte createType) {

		UpdateNewsCommand cmd = null;
		if (TYPE_CREATE_BY_ADMIN == createType) {
			cmd = (UpdateNewsCommand) cmdObject;
		} else {
			UpdateOpenNewsCommand cmd2 = (UpdateOpenNewsCommand) cmdObject;
			cmd = ConvertHelper.convert(cmd2, UpdateNewsCommand.class);
			cmd.setCoverUri(cmd2.getCoverUrl());
		}

		// 设置新的值
		originNews.setTitle(cmd.getTitle());
		originNews.setAuthor(cmd.getAuthor());
		originNews.setCoverUri(cmd.getCoverUri());
		originNews.setContentAbstract(cmd.getContentAbstract());
		// news.setCategoryId(cmd.getCategoryId());
		originNews.setContent(cmd.getContent());
		originNews.setSourceDesc(cmd.getSourceDesc());
		originNews.setSourceUrl(cmd.getSourceUrl());
		originNews.setPhone(cmd.getPhone());
//		originNews.setVisibleType(cmd.getVisibleType());
		originNews.setStatus(generateNewsStatus(cmd.getStatus()));

		// 调整摘要
		adjustNewsContentAbstract(originNews);

		return;
	}


	public void updateNews(News news, List<Long> communityIds, List<Long> newsTagIds) {

		dbProvider.execute((TransactionStatus status) -> {
			newsProvider.updateNews(news);
			updateCommunityIds(news.getId(), communityIds);
			updateNewsTagVals(news.getId(), newsTagIds);
			return null;
		});

		syncNews(news.getId());
	}

	private void updateNewsTagVals(Long newsId, List<Long> newsTagIds) {
		processNewsTagVals(newsId, newsTagIds, true);

	}

	private void updateCommunityIds(Long newsId, List<Long> communityIds) {
		processCommunityIds(newsId, communityIds, true);
	}

	private void checkBlacklist(String ownerType, Long ownerId) {
		ownerType = StringUtils.isEmpty(ownerType) ? "" : ownerType;
		ownerId = null == ownerId ? 0L : ownerId;
		Long userId = UserContext.current().getUser().getId();
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserBlacklistAuthority(userId, ownerType, ownerId, PrivilegeConstants.BLACKLIST_NEWS);
	}

	private News buildCreateNewsAdmin(Integer namespaceId, CreateNewsCommand cmd) {
		return buildCreateNews(namespaceId, cmd, TYPE_CREATE_BY_ADMIN);
	}

	private News buildCreateNews(Integer namespaceId,  Object cmdObject, byte createType) {
		News news = null;
		Long publishTime = null;
		if (TYPE_CREATE_BY_ADMIN == createType) {
			CreateNewsCommand cmd = (CreateNewsCommand)cmdObject;
			news = ConvertHelper.convert(cmd, News.class);
			publishTime = cmd.getPublishTime();
		} else {
			CreateOpenNewsCommand cmd = (CreateOpenNewsCommand)cmdObject;
			news = ConvertHelper.convert(cmd, News.class);
			publishTime = cmd.getPublishTime();
			news.setCoverUri(cmd.getCoverUrl());
		}

		news.setNamespaceId(namespaceId);
		news.setOwnerType(NewsOwnerType.COMMUNITY.getCode());
		news.setContentType(NewsContentType.RICH_TEXT.getCode());
		news.setStatus(generateNewsStatus(news.getStatus()));
		news.setCreateType(createType);
		if (publishTime != null) {
			news.setPublishTime(new Timestamp(publishTime));
		}
		
		//调整摘要
		adjustNewsContentAbstract(news);
		return news;
	}

	// 根据web提交的状态，重新生成新闻的状态
	private Byte generateNewsStatus(byte status) {
		NewsStatus nStatus = NewsStatus.fromCode(status);
		if (nStatus != NewsStatus.ACTIVE) {
			return NewsStatus.DRAFT.getCode();
		}
		return NewsStatus.ACTIVE.getCode();
	}

	private void checkNewsParameter(Long userId, CreateNewsCommand cmd) {
		if (StringUtils.isEmpty(cmd.getTitle()) || StringUtils.isEmpty(cmd.getContent())) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_INPUT_PARAM_INVALID, "Invalid parameters");
		}

		if (null == cmd.getCommunityIds() || cmd.getCommunityIds().isEmpty()) {
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_VISIBLE_INVALID, "Invalid parameters");
		}

		if (null == cmd.getCurrentPMId()) {
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE, NewsServiceErrorCode.ERROR_PM_ID_INVALID,
					"Invalid PM id");
		}

		if (null == cmd.getCurrentProjectId()) {
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_PROJECT_ID_INVALID, "Invalid project id");
		}
	}

	private void checkNewsParameterOfImport(Long userId, CreateNewsCommand cmd) {
		if (StringUtils.isEmpty(cmd.getTitle()) || StringUtils.isEmpty(cmd.getContent())) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_PROCESS_EXCEL_ERROR, "Invalid parameters");
		}
	}

	private Integer checkOwner(Long userId, Long ownerId, String ownerType) {
		if (ownerId == null || StringUtils.isEmpty(ownerType)) {
			LOGGER.error(
					"Invalid parameters, operatorId=" + userId + ", ownerType=" + ownerType + ", ownerId=" + ownerId);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_INPUT_PARAM_INVALID, "Invalid parameters");
		}

		NewsOwnerType newsOwnerType = NewsOwnerType.fromCode(ownerType);
		if (newsOwnerType == null) {
			LOGGER.error("Invalid owner type, operatorId=" + userId + ", ownerType=" + ownerType);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_OWNER_TYPE_INVALID, "Invalid owner type");
		}

		// 目前只有一种ownerType即organization，所以不做判断了，如果后面改成了多个，可以把返回值改成Object，然后在调用者中逐个判断
		if (newsOwnerType == NewsOwnerType.ORGANIZATION) {
			Organization organization = organizationProvider.findOrganizationById(ownerId);
			if (organization == null) {
				LOGGER.error(
						"Invalid owner id, operatorId=" + userId + ", ownerType=" + ownerType + ", ownerId=" + ownerId);
				throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
						NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID, "Invalid owner id");
			}

			return organization.getNamespaceId();
		} else {
			Community community = communityProvider.findCommunityById(ownerId);
			if (community == null) {
				LOGGER.error(
						"Invalid owner id, operatorId=" + userId + ", ownerType=" + ownerType + ", ownerId=" + ownerId);
				throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
						NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID, "Invalid owner id");
			}
			return community.getNamespaceId();
		}

		// return organization;
	}

	@Override
	public void importNews(ImportNewsCommand cmd, MultipartFile[] files) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null
				&& configProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(),
					1080010800L, cmd.getAppId(), null, cmd.getOwnerId());// 全部权限
		}
		Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());

		// 权限核实
		// 这里就不用projectId了，冗余了
		checkUserProjectLegal(userId, cmd.getCurrentPMId(), cmd.getOwnerId(), cmd.getAppId());

		// 读取Excel数据
		List<News> newsList = getNewsFromExcel(userId, namespaceId, cmd, files);

		dbProvider.execute(s -> {
			newsProvider.createNewsList(newsList);
			// 导入时，默认全部园区可见
			List<OrganizationCommunity> organizationCommunities = organizationProvider
					.listOrganizationCommunities(cmd.getOwnerId());
			newsList.forEach(n -> {
				if (null != organizationCommunities) {
					organizationCommunities.forEach(m -> {
						NewsCommunity newsCommunity = new NewsCommunity();
						newsCommunity.setNewsId(n.getId());
						newsCommunity.setCommunityId(m.getCommunityId());
						newsProvider.createNewsCommunity(newsCommunity);
					});
				}
			});
			return null;
		});

		newsList.forEach(n -> syncNews(n.getId()));
	}

	@SuppressWarnings("unchecked")
	private List<News> getNewsFromExcel(Long userId, Integer namespaceId, ImportNewsCommand cmd,
			MultipartFile[] files) {
		List<RowResult> resultList = null;
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			LOGGER.error("processStat Excel error, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_PROCESS_EXCEL_ERROR, "processStat Excel error");
		}

		if (resultList != null && resultList.size() > 0) {
			final List<News> newsList = new ArrayList<>();
			for (int i = 1, len = resultList.size(); i < len; i++) {
				RowResult result = resultList.get(i);
				String title = RowResult.trimString(result.getA());
				String contentAbstract = RowResult.trimString(result.getB());
				String coverUri = RowResult.trimString(result.getC());
				String content = RowResult.trimString(result.getD());
				String author = RowResult.trimString(result.getE());
				String publishTime = RowResult.trimString(result.getF());
				String sourceDesc = RowResult.trimString(result.getG());
				String sourceUrl = RowResult.trimString(result.getH());

				// 判断有效行，有一个单元格不为空即为有效行
				if (!StringUtils.isEmpty(title) || !StringUtils.isEmpty(contentAbstract)
						|| !StringUtils.isEmpty(coverUri) || !StringUtils.isEmpty(content)
						|| !StringUtils.isEmpty(author) || !StringUtils.isEmpty(sourceDesc)
						|| !StringUtils.isEmpty(sourceUrl)) {
					CreateNewsCommand command = new CreateNewsCommand();
					command.setOwnerId(cmd.getCurrentProjectId());
					command.setOwnerType(NewsOwnerType.COMMUNITY.getCode());
					command.setTitle(title);
					command.setContentAbstract(contentAbstract);
					command.setContent(content);
					command.setCoverUri(coverUri);
					command.setAuthor(author);
					command.setPublishTime(covertStringToLongTime(publishTime));
					command.setSourceDesc(sourceDesc);
					command.setSourceUrl(sourceUrl);
					command.setCategoryId(cmd.getCategoryId());
					checkNewsParameterOfImport(userId, command);
					newsList.add(buildCreateNewsAdmin(namespaceId, command));
				}
			}
			return newsList;
		}
		LOGGER.error("excel data format is not correct.rowCount=" + resultList.size());
		throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
				NewsServiceErrorCode.ERROR_NEWS_PROCESS_EXCEL_ERROR, "excel data format is not correct");
	}

	private Long covertStringToLongTime(String string) {
		if (StringUtils.isEmpty(string)) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(string);
			return date.getTime();
		} catch (ParseException e) {
			LOGGER.error("date format error, date: " + string);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_PROCESS_EXCEL_ERROR, "date format error, date: " + string);
		}
	}

	@Override
	public ListNewsResponse listNewsForWeb(ListNewsCommand cmd) {
		cmd.setStatus(NewsStatus.ACTIVE.getCode());
		return listNews(cmd, true);
	}

	@Override
	public ListNewsResponse listNews(ListNewsCommand cmd, boolean isScene) {

		// 先进行权限验证
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && cmd.getCurrentProjectId() != null
				&& configProvider.getBooleanValue("privilege.community.checkflag", true)) {

			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(),
					1080010800L, cmd.getAppId(), null, cmd.getCurrentProjectId());// 全部权限
		}

		// 如果是在场景中获取时，不需要登录
		Long userId = isScene ? null : UserContext.current().getUser().getId();
		Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());
		if (null == cmd.getCategoryId()) {
			cmd.setCategoryId(0L);
		}

		// 获取能够搜索项目所属范围，即用户只能搜索授权的项目。
		List<Long> authProjectIds = getAuthSearchProjectIds(namespaceId, cmd, isScene);
		if(CollectionUtils.isEmpty(authProjectIds)) {
			return new ListNewsResponse();
		}

		// 若无关键字查询，直接返回简单查询
		if (StringUtils.isEmpty(cmd.getKeyword()) && cmd.getTagIds() == null) {
			return listNews(userId, namespaceId, cmd.getOwnerId(), authProjectIds, cmd.getCategoryId(), cmd.getPageAnchor(), cmd.getPageSize(),
					isScene, cmd.getStatus());
		}

		// 有关键字的查询
		SearchNewsResponse response = searchNews(cmd.getOwnerId(), authProjectIds, userId, namespaceId, cmd.getCategoryId(),
				cmd.getKeyword(), cmd.getTagIds(), cmd.getPageAnchor(), cmd.getPageSize(), isScene, cmd.getStatus());
		return ConvertHelper.convert(response, ListNewsResponse.class);
	}

	private List<Long> getAuthSearchProjectIds(Integer namespaceId, ListNewsCommand cmd, boolean isScene) {
		if (isScene) {
			return getAuthSearchProjectIdsByScene(namespaceId, cmd.getCategoryId(), cmd.getOwnerId());
		}

		return getAuthSearchProjectIdsByAdmin(cmd.getCurrentPMId(), cmd.getCurrentProjectId(), cmd.getAppId());
	}

	private List<Long> getAuthSearchProjectIdsByAdmin(Long currentPMId, Long currentProjectId, Long appId) {
		if (null != currentProjectId) {
			return Arrays.asList(currentProjectId);
		}

		// 未携带id时，需要查询当前用户所属项目
		return organizationService.getOrganizationProjectIdsByAppId(currentPMId, appId);
	}

	private List<Long> getAuthSearchProjectIdsByScene(Integer namespaceId, Long categoryId, Long communityId) {

		if (null == categoryId) {
			return null;
		}

		return organizationService.getProjectIdsByCommunityAndModuleApps(namespaceId, communityId, NEWS_MODULE_ID,
				r -> {

					NewsInstanceConfig config = (NewsInstanceConfig) StringHelper.fromJsonString(r,
							NewsInstanceConfig.class);
					if (null == config) {
						return false;
					}

					return categoryId.equals(config.getCategoryId());
				});
	}

	@Override
	public ListOpenNewsResponse listNewsOpenApi(ListOpenNewsCommand cmd, Integer namespaceId) {

		// 若无关键字查询，直接返回简单查询
		Long userId = 0L;
		List<BriefNewsDTO> newsList = null;
		Long nextPageAnchor = null;
		List<Long> authProjectIds = null == cmd.getOwnerId() ? null : Arrays.asList(cmd.getOwnerId());

		if (StringUtils.isEmpty(cmd.getKeyword()) && CollectionUtils.isEmpty(cmd.getNewsTagIds())) {
			ListNewsResponse resp = listNews(userId, namespaceId, null, authProjectIds, cmd.getCategoryId(), cmd.getPageAnchor(),
					cmd.getPageSize(), false, cmd.getStatus());
			newsList = resp.getNewsList();
			nextPageAnchor = resp.getNextPageAnchor();
		} else {
			SearchNewsResponse resp = searchNews(null, authProjectIds, userId, namespaceId, cmd.getCategoryId(),
					cmd.getKeyword(), cmd.getNewsTagIds(), cmd.getPageAnchor(), cmd.getPageSize(), false,
					cmd.getStatus());
			newsList = resp.getNewsList();
			nextPageAnchor = resp.getNextPageAnchor();
		}

		ListOpenNewsResponse resp = new ListOpenNewsResponse();
		resp.setNextPageAnchor(nextPageAnchor);
		resp.setNewsList(convertToOpenNewsList(newsList));
		return resp;
	}


	private List<OpenBriefNewsDTO> convertToOpenNewsList(List<BriefNewsDTO> newsList) {
		if (CollectionUtils.isEmpty(newsList)) {
			return null;
		}

		return newsList.stream().map(r->{
			OpenBriefNewsDTO openNewsDto = ConvertHelper.convert(r, OpenBriefNewsDTO.class);
			openNewsDto.setPublishTime(r.getPublishTime().getTime());
			openNewsDto.setCoverUrl(r.getCoverUri());
			return openNewsDto;
		}).collect(Collectors.toList());
	}

	private ListNewsResponse listNews(Long userId, Integer namespaceId, Long communityId, List<Long> authProjectIds, Long categoryId,
			Long pageAnchor, Integer pageSize, boolean isScene, Byte status) {

		pageSize = PaginationConfigHelper.getPageSize(configurationProvider, pageSize);
		pageAnchor = pageAnchor == null ? 0 : pageAnchor;
		Long from = pageAnchor * pageSize;

		Boolean commentForbiddenFlag = newsProvider.getCommentForbiddenFlag(categoryId, namespaceId);

		List<BriefNewsDTO> list = newsProvider
				.listNews(communityId, authProjectIds, categoryId, namespaceId, from, pageSize + 1, isScene, status).stream()
				.map(news -> convertNewsToBriefNewsDTO(userId, news, isScene, commentForbiddenFlag))
				.collect(Collectors.toList());

		if (list.size() > pageSize) {
			pageAnchor += 1;
			list.remove(list.size() - 1);
		} else {
			pageAnchor = null;
		}

		ListNewsResponse response = new ListNewsResponse();
		response.setNextPageAnchor(pageAnchor);
		response.setNewsList(list);

		return response;
	}

	private BriefNewsDTO convertNewsToBriefNewsDTO(Long userId, News news, boolean isScene,
			Boolean commentForbiddenFlag) {
		BriefNewsDTO newsDTO = ConvertHelper.convert(news, BriefNewsDTO.class);
		newsDTO.setNewsToken(WebTokenGenerator.getInstance().toWebToken(news.getId()));
		if (!isScene) {
			newsDTO.setLikeFlag(getUserLikeFlag(userId, news.getId()).getCode());
		}
		if (newsDTO.getCoverUri() == null) {
			NewsCategory category = this.newsProvider.findNewsCategoryById(news.getCategoryId());
			if (null != category) {
				newsDTO.setCoverUri(this.contentServerService.parserUri(category.getLogoUri(), EntityType.USER.getCode(),
						UserContext.current().getUser().getId()));
			}
		}
		
		newsDTO.setNewsUrl(getNewsWebUrl(news.getNamespaceId(), newsDTO.getNewsToken()));

		newsDTO.setCommentFlag(NewsNormalFlag.ENABLED.getCode());
		if (commentForbiddenFlag) {
			newsDTO.setCommentFlag(NewsNormalFlag.DISABLED.getCode());
		}

		newsDTO.setProjectDTOS(setProjectDTOs(news.getId()));

		return newsDTO;
	}

	private List<ProjectDTO> setProjectDTOs(Long newsId) {
		List<Long> communityIds = newsProvider.listNewsCommunities(newsId);
		Map<Long, Community> temp = communityProvider.listCommunitiesByIds(communityIds);
		return temp.values().stream().map(r -> {
			ProjectDTO projectDTO = new ProjectDTO();
			projectDTO.setProjectId(r.getId());
			projectDTO.setProjectName(r.getName());

			return projectDTO;
		}).collect(Collectors.toList());
	}

	private UserLikeType getUserLikeFlag(Long userId, Long newsId) {
		if (userId.longValue() == 0) {
			return UserLikeType.NONE;
		}
		UserLike userLike = findUserLike(userId, newsId);
		if (userLike == null) {
			return UserLikeType.NONE;
		}
		if (userLike.getLikeType().byteValue() == UserLikeType.LIKE.getCode()) {
			return UserLikeType.LIKE;
		}
		return UserLikeType.NONE;
	}

	private UserLike findUserLike(Long userId, Long newsId) {
		return userProvider.findUserLike(userId, EntityType.NEWS.getCode(), newsId);
	}


	/**
	 * 拼接搜索串的部分移出来并增加highlight部分，以便后续处理 xiongying
	 */
	private String getSearchJson(Long communityId, List<Long> authProjectIds, Long userId, Integer namespaceId, Long categoryId, String keyword,
			List<Long> tagIds, Long pageAnchor, Integer pageSize, Byte status, boolean isScene) {
		Long from = pageAnchor * pageSize;

		// {\"from\":0,\"size\":15,\"sort\":[],\"query\":{\"filtered\":{\"query\":{},\"filter\":{\"bool\":{\"must\":[],\"should\":[]}}}}}
		JSONObject json = JSONObject.parseObject(
				"{\"from\":0,\"size\":0,\"sort\":[],\"query\":{\"filtered\":{\"query\":{},\"filter\":{\"bool\":"
						+ "{\"must\":[]}}}},\"highlight\":{\"fields\":{\"title\":{\"fragment_size\":60,\"number_of_fragments\":0},"
						+ "\"content\":{\"fragment_size\":34,\"number_of_fragments\":1}}}}");
		// 设置from和size
		json.put("from", from);
		json.put("size", pageSize + 1);

		// 设置排序条件
		JSONArray sort = json.getJSONArray("sort");
		// sort.add(JSONObject.parseObject("{\"status\":{\"order\":\"asc\"}}"));
		sort.add(JSONObject.parseObject("{\"topIndex\":{\"order\":\"desc\"}}"));
		sort.add(JSONObject.parseObject("{\"publishTime\":{\"order\":\"desc\"}}"));

		// 设置查询关键字
		JSONObject query = json.getJSONObject("query").getJSONObject("filtered").getJSONObject("query");
		if (!StringUtils.isEmpty(keyword))
			query.put("query_string",
					JSONObject.parse("{\"query\":\"" + keyword + "\",\"fields\":[\"title\",\"content\"]}"));
		else
			query.put("match_all", JSONObject.parse("{}"));

		// 设置条件
		JSONArray must = json.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter")
				.getJSONObject("bool").getJSONArray("must");
		must.add(JSONObject.parse("{\"term\":{\"namespaceId\":" + namespaceId + "}}"));
		NewsStatus enumStatus = NewsStatus.fromCode(status);
		if (enumStatus == NewsStatus.ACTIVE) {
			must.add(JSONObject.parse("{\"term\":{\"status\":" + NewsStatus.ACTIVE.getCode() + "}}"));
		} else if (enumStatus == NewsStatus.DRAFT) {
			must.add(JSONObject.parse("{\"term\":{\"status\":" + NewsStatus.DRAFT.getCode() + "}}"));
		}

		// 同一个tag对子tag取交集，不同的tag取并集
		List<TagSearchItem> items = getTagSearchItems(namespaceId, categoryId, tagIds);
		boolean containItems = !CollectionUtils.isEmpty(items);
		if (containItems) {
			for (TagSearchItem item : items) {
				String idList = Joiner.on(",").join(item.getChildTagIds());
				must.add(JSONObject.parse("{\"terms\":{\"tag\":[" + idList + "]}}"));
			}
		}

		if (null != communityId && isScene) {
			if (isScene) {
				must.add(JSONObject.parse("{\"term\":{\"communityIds\":" + communityId + "}}"));
			} else {
				must.add(JSONObject.parse("{\"term\":{\"ownerId\":" + communityId + "}}"));
			}
		}

		if (CollectionUtils.isEmpty(authProjectIds)) {
			String authIdList = Joiner.on(",").join(authProjectIds);
			must.add(JSONObject.parse("{\"terms\":{\"ownerId\":[" + authIdList + "]}}"));
		}

		// 设置过滤条件
		if (null != categoryId) {
			must.add(JSONObject.parse("{ \"term\": { \"categoryId\": " + categoryId + "}} "));
		}
		// 设置高亮
		JSONObject highLight = json.getJSONObject("highlight");
		JSONArray preTags = new JSONArray();
		preTags.add("<b class=\"news-keyword\">");
		highLight.put("pre_tags", preTags);
		JSONArray postTags = new JSONArray();
		postTags.add("</b>");
		highLight.put("post_tags", postTags);
		System.out.println(json.toJSONString());
		return json.toJSONString();
	}

	private SearchNewsResponse searchNews(Long communityId, List<Long> authProjectIds, Long userId, Integer namespaceId, Long categoryId,
			String keyword, List<Long> tagIds, Long pageAnchor, Integer pageSize, boolean isScene, Byte status) {

		pageSize = PaginationConfigHelper.getPageSize(configurationProvider, pageSize);
		pageAnchor = pageAnchor == null ? 0 : pageAnchor;

		String jsonString = getSearchJson(communityId, authProjectIds, userId, namespaceId, categoryId, keyword, tagIds, pageAnchor,
				pageSize, status, isScene);

		// 需要查询的字段
		String fields = "id,title,publishTime,autho r,sourceDesc,coverUri,contentAbstract,likeCount,childCount,topFlag,communityIds,visibleType,tag,status,ownerId,categoryId,phone";

		// 从es查询
		JSONArray result = searchProvider.query(SearchUtils.NEWS, jsonString, fields);

		// 处理分页
		Long nextPageAnchor = null;
		if (result.size() > pageSize) {
			result.remove(result.size() - 1);
			nextPageAnchor = pageAnchor + 1;
		}

		Boolean commentForbiddenFlag = newsProvider.getCommentForbiddenFlag(categoryId, namespaceId);
		// 转换结果到返回值

		List<BriefNewsDTO> list = result.stream().map(r -> {
			JSONObject o = (JSONObject) r;
			BriefNewsDTO newsDTO = new BriefNewsDTO();
			newsDTO.setNewsToken(WebTokenGenerator.getInstance().toWebToken(o.getLong("id")));
			newsDTO.setTitle(o.getString("title"));
			newsDTO.setPublishTime(o.getTimestamp("publishTime"));
			newsDTO.setAuthor(o.getString("author"));
			newsDTO.setSourceDesc(o.getString("sourceDesc"));
			newsDTO.setCoverUri(o.getString("coverUri"));
			newsDTO.setContentAbstract(o.getString("contentAbstract"));
			newsDTO.setLikeCount(o.getLong("likeCount"));
			newsDTO.setChildCount(o.getLong("childCount"));
			newsDTO.setTopFlag(o.getByte("topFlag"));
			newsDTO.setOwnerId(o.getLong("ownerId"));
			newsDTO.setCategoryId(o.getLong("categoryId"));
			newsDTO.setPhone(o.getString("phone"));
			if (!isScene) {
				newsDTO.setLikeFlag(getUserLikeFlag(userId, o.getLong("id")).getCode());
			}
			newsDTO.setCategoryId(o.getLong("categoryId"));
			newsDTO.setVisibleType(o.getString("visibleType"));
			// es存在的bug，原本的title 测试点赞
			// 高亮过来是 {"title": ["测试点<b class=\"news-keyword\">测试</b>赞"]}

			if (o.getJSONObject("highlight") != null) {
				newsDTO.setHighlightFields(o.getJSONObject("highlight").toJSONString());
				if (newsDTO.getHighlightFields().matches("^.*测试点.*测试.*赞.*$")) {
					newsDTO.setHighlightFields("{\"title\": [\"<b class=\\\"news-keyword\\\">测试</b>点赞\"]}");
				}
			}

			newsDTO.setCommentFlag(NewsNormalFlag.ENABLED.getCode());
			if (commentForbiddenFlag) {
				newsDTO.setCommentFlag(NewsNormalFlag.DISABLED.getCode());
			}

			newsDTO.setProjectDTOS(setProjectDTOs(o.getLong("id")));
			newsDTO.setStatus(Byte.valueOf(String.valueOf(o.getLong("status"))));

			return newsDTO;
		}).collect(Collectors.toList());

		SearchNewsResponse response = new SearchNewsResponse();
		response.setNextPageAnchor(nextPageAnchor);
		response.setNewsList(list);

		return response;
	}

	@Override
	public GetNewsDetailInfoResponse getNewsDetailInfo(GetNewsDetailInfoCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Long newsId = checkNewsToken(userId, cmd.getNewsToken());

		News news = findNewsById(userId, newsId);
		newsProvider.increaseViewCount(newsId, news.getViewCount());
		news.setViewCount((news.getViewCount() + 1L) / 2);// web端一次浏览，调用了两次接口。这里除以2

		List<NewsTagVals> list = newsProvider.listNewsTagVals(newsId);
		list.forEach(r -> {
			NewsTag newsTag = newsProvider.findNewsTagById(r.getNewsTagId());
			if (newsTag == null)
				return;
			if (newsTag.getDeleteFlag() != (byte) 1)// 未删除
				r.setValue(newsTag.getValue());

			newsTag = newsProvider.findNewsTagById(newsTag.getParentId());

			if (newsTag.getDeleteFlag() != (byte) 1)
				r.setName(newsTag.getValue());
		});
		GetNewsDetailInfoResponse response = convertNewsToNewsDTO(userId, news);
		response.setTags(list.stream().map(r -> {
			if (StringUtils.isEmpty(r.getName()))
				return null;
			else
				return ConvertHelper.convert(r, NewsTagValsDTO.class);
		}).filter(r -> r != null).collect(Collectors.toList()));
		return response;
	}

	@Override
	public GetNewsDetailResponse getNewsDetail(GetNewsDetailInfoCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		News news = findNewsById(userId, newsId);
		GetNewsDetailResponse response = ConvertHelper.convert(news, GetNewsDetailResponse.class);
		List<Long> communityIds = newsProvider.listNewsCommunities(newsId);
		response.setCommunityIds(communityIds.stream().map(r -> r.toString()).collect(Collectors.toList()));
		response.setPublishTime(news.getPublishTime().getTime());
		List<NewsTag> parentTags = newsProvider.listNewsTag(news.getNamespaceId(),null,null, null, 0l, null, null,
				news.getCategoryId());
		List<NewsTagDTO> newsTags = parentTags.stream().map(r -> ConvertHelper.convert(r, NewsTagDTO.class))
				.collect(Collectors.toList());
		List<NewsTagVals> newsTagVals = newsProvider.listNewsTagVals(newsId);

		final Map<Long, List<Long>> map = new HashMap();
		newsTagVals.stream().map(r -> { // 创建旧新闻的父标签-子标签id映射
			NewsTag newsTag = newsProvider.findNewsTagById(r.getNewsTagId());
			NewsTagVals t = new NewsTagVals();
			if (newsTag == null) {
				return t;
			}
			if (newsTag.getDeleteFlag() != (byte) 1) // 没被删除
				t.setNewsTagId(newsTag.getId()); // 子标签id
			newsTag = newsProvider.findNewsTagById(newsTag.getParentId());
			if (newsTag.getDeleteFlag() != (byte) 1) // 没被删除
				t.setId(newsTag.getId()); // 父标签id
			return t;
		}).filter(r -> r.getId() != null).forEach(r -> {
			List<Long> list = map.get(r.getId());
			if (list == null) {
				list = new ArrayList<Long>();
				map.put(r.getId(), list);
			}
			list.add(r.getNewsTagId());
		});
		// System.out.println(StringHelper.toJsonString(map));

		// Map<Long,Long> map = newsTagVals.stream().map(r->{
		// //创建旧新闻的父标签-子标签id映射
		// NewsTag newsTag = newsProvider.findNewsTagById(r.getNewsTagId());
		// NewsTagVals t= new NewsTagVals();
		// if (newsTag.getDeleteFlag()!=(byte)1) //没被删除
		// t.setNewsTagId(newsTag.getId()); //子标签id
		// newsTag = newsProvider.findNewsTagById(newsTag.getParentId());
		// if (newsTag.getDeleteFlag()!=(byte)1) //没被删除
		// t.setId(newsTag.getId()); //父标签id
		// return t;
		// }).filter(r->
		// r.getId()!=null).collect(Collectors.toMap(NewsTagVals::getId,NewsTagVals::getNewsTagId));

		newsTags.forEach(r -> {
			List<NewsTag> tags = newsProvider.listNewsTag(news.getNamespaceId() ,null,null, null, r.getId(), null, null,
					r.getCategoryId());
			List<NewsTagDTO> list = tags.stream().map(t -> ConvertHelper.convert(t, NewsTagDTO.class)).map(t -> {
				// if (map.get(r.getId())!=null)
				t.setIsDefault((byte) 0);
				List<Long> childrentags = map.get(r.getId());
				if (childrentags != null && childrentags.contains(t.getId()))
					t.setIsDefault((byte) 1);
				return t;
			}).collect(Collectors.toList());

			r.setChildTags(JSONObject.toJSONString(list));
		});
		response.setNewsTags(newsTags);
		return response;
	}

	private GetNewsDetailInfoResponse convertNewsToNewsDTO(Long userId, News news) {
		GetNewsDetailInfoResponse newsDTO = ConvertHelper.convert(news, GetNewsDetailInfoResponse.class);
		newsDTO.setNewsToken(WebTokenGenerator.getInstance().toWebToken(news.getId()));
		newsDTO.setContent(null);
		newsDTO.setContentUrl(getContentUrl(news.getNamespaceId(), newsDTO.getNewsToken()));
		if (news.getCoverUri() != null)
			newsDTO.setCoverUri(news.getCoverUri());
		else {
			NewsCategory category = this.newsProvider.findNewsCategoryById(news.getCategoryId());
			newsDTO.setCoverUri(this.contentServerService.parserUri(category.getLogoUri(), EntityType.USER.getCode(),
					UserContext.current().getUser().getId()));
		}
		newsDTO.setNewsUrl(getNewsWebUrl(news.getNamespaceId(), newsDTO.getNewsToken()));
		newsDTO.setNewsWebShareUrl(getNewsWebUrl(news.getNamespaceId(), newsDTO.getNewsToken()));
		newsDTO.setLikeFlag(getUserLikeFlag(userId, news.getId()).getCode());// 未登录用户id为0

		Boolean commentForbiddenFlag = newsProvider.getCommentForbiddenFlag(news.getCategoryId(),
				news.getNamespaceId());

		newsDTO.setCommentFlag(NewsNormalFlag.ENABLED.getCode());
		if (commentForbiddenFlag) {
			newsDTO.setCommentFlag(NewsNormalFlag.DISABLED.getCode());
		}
		return newsDTO;
	}

	// private String getNewsUrl(Integer namespaceId, String newsToken) {
	// String homeUrl = configurationProvider.getValue(namespaceId,
	// ConfigConstants.HOME_URL, "");
	// String contentUrl = configurationProvider.getValue(namespaceId,
	// ConfigConstants.NEWS_PAGE_URL, "");
	// if (homeUrl.length() == 0 || contentUrl.length() == 0) {
	// LOGGER.error("Invalid home url or news page url, homeUrl=" + homeUrl + ",
	// contentUrl=" + contentUrl);
	// throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
	// NewsServiceErrorCode.ERROR_NEWS_CONTENT_URL_INVALID, "Invalid home url or
	// content url");
	// } else {
	// return homeUrl + contentUrl + newsToken;
	// }
	// }

	private String getNewsWebUrl(Integer namespaceId, String newsToken) {
		String homeUrl = configurationProvider.getValue(namespaceId, ConfigConstants.HOME_URL, "");
		String contenWebtUrl = configurationProvider.getValue(namespaceId, ConfigConstants.NEWS_PAGE_URL,
				"/park-news-web/build/index.html?ns=%s&isFS=1&widget=News&timeWidgetStyle=time/#/newsDetail?newsToken=%s");
		if (homeUrl.length() == 0 || contenWebtUrl.length() == 0) {
			LOGGER.error("Invalid home url or news page url, homeUrl=" + homeUrl + ", contentUrl=" + contenWebtUrl);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_CONTENT_URL_INVALID, "Invalid home url or content url");
		} else {
			return homeUrl + String.format(contenWebtUrl, namespaceId, newsToken);
		}
	}

	private String getContentUrl(Integer namespaceId, String newsToken) {
		String homeUrl = configurationProvider.getValue(namespaceId, ConfigConstants.HOME_URL, "");
		String contentUrl = configurationProvider.getValue(namespaceId, ConfigConstants.NEWS_CONTENT_URL, "");
		if (homeUrl.length() == 0 || contentUrl.length() == 0) {
			LOGGER.error("Invalid home url or content url, homeUrl=" + homeUrl + ", contentUrl=" + contentUrl);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_CONTENT_URL_INVALID, "Invalid home url or content url");
		} else {
			return homeUrl + contentUrl + "?newsToken=" + newsToken;
		}
	}

	@Override
	public void setNewsTopFlag(SetNewsTopFlagCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());
		final Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		final News news = findNewsById(userId, newsId);
		if (NewsStatus.fromCode(news.getStatus()) == NewsStatus.DRAFT) {
			LOGGER.error("News status is 1 draft, cat be top, operatorId=" + userId + ", newsId=" + newsId);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID, "News status is 1 draft, cat be top");
		}
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {

			if (NewsTopFlag.fromCode(news.getTopFlag()) == NewsTopFlag.NONE) {
				news.setTopFlag(NewsTopFlag.TOP.getCode());
				LOGGER.info("ns = {}", namespaceId);
				Long maxId = newsProvider.getMaxTopIndex(namespaceId);
				news.setTopIndex((maxId == null ? 0 : maxId) + 1L);
			} else {
				news.setTopFlag(NewsTopFlag.NONE.getCode());
				news.setTopIndex(0L);
			}
			newsProvider.updateNews(news);
			return null;
		});

		syncNews(newsId);
	}

	public News findNewsById(Long userId, Long newsId) {
		News news = newsProvider.findNewsById(newsId);
		if (news == null) {
			LOGGER.error("News not found, operatorId=" + userId + ", newsId=" + newsId);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID, "News not found");
		}
		return news;
	}

	public Long checkNewsToken(Long userId, String newsToken) {
		Long newsId = WebTokenGenerator.getInstance().fromWebToken(newsToken, Long.class);
		if (newsId == null) {
			LOGGER.error("Invalid newsToken, operatorId=" + userId + ", newsToken=" + newsToken);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_NEWSTOKEN_INVALID, "Invalid newsToken");
		}
		return newsId;
	}

	@Override
	public GetNewsContentResponse getNewsContent(GetNewsContentCommand cmd) {
		String content = newsProvider
				.findNewsById(checkNewsToken(UserContext.current().getUser().getId(), cmd.getNewsToken())).getContent();
		return new GetNewsContentResponse(content);
	}

	@Override
	public void deleteNews(DeleteNewsCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());
		Long newsId = checkNewsToken(userId, cmd.getNewsToken());

		News newsChk = findNewsById(userId, newsId);
		if (null == newsChk) {
			return;
		}

		// 权限核实
		checkUserProjectLegal(userId, cmd.getCurrentPMId(), newsChk.getOwnerId(), cmd.getAppId());

		//删除
		deleteNews(userId, newsChk);
	}



	@Override
	public void deleteNews(Long userId, News news) {

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {
			news.setDeleterUid(userId);
			news.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			news.setStatus(NewsStatus.INACTIVE.getCode());
			newsProvider.updateNews(news);
			return null;
		});

		syncNewsWhenDelete(news.getId());
	}

	@Override
	public void setNewsLikeFlag(SetNewsLikeFlagCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());
		setNewsLikeFlag(userId, cmd.getNewsToken());
	}

	private void setNewsLikeFlag(Long userId, String newsToken) {
		final Long newsId = checkNewsToken(userId, newsToken);

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {
			dbProvider.execute(s -> {
				News news = findNewsById(userId, newsId);
				UserLike userLike = findUserLike(userId, newsId);
				if (userLike == null) {
					news.setLikeCount(news.getLikeCount() + 1L);
					userLike = new UserLike();
					userLike.setLikeType(UserLikeType.LIKE.getCode());
					userLike.setOwnerUid(userId);
					userLike.setTargetId(newsId);
					userLike.setTargetType(EntityType.NEWS.getCode());
					userProvider.createUserLike(userLike);
				} else if (userLike.getLikeType() == UserLikeType.NONE.getCode()) {
					news.setLikeCount(news.getLikeCount() + 1L);
					userLike.setLikeType(UserLikeType.LIKE.getCode());
					userProvider.updateUserLike(userLike);
				} else {
					news.setLikeCount(news.getLikeCount() - 1L);
					userLike.setLikeType(UserLikeType.NONE.getCode());
					userProvider.updateUserLike(userLike);
				}
				newsProvider.updateNews(news);
				return true;
			});
			return null;
		});

		syncNews(newsId);
	}

	@Override
	public AddNewsCommentResponse addNewsComment(final AddNewsCommentCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		// 检查参数
		checkCommentParameter(userId, cmd);

		// 黑名单权限校验 by sfyan20161213
		checkBlacklist(null, null);

		final List<Comment> comments = new ArrayList<>();
		final List<Attachment> attachments = new ArrayList<>();

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {
			dbProvider.execute(s -> {
				News news = findNewsById(userId, newsId);
				// 创建评论
				Comment comment = processComment(userId, newsId, cmd);
				comments.add(comment);
				commentProvider.createComment(EhNewsComment.class, comment);

				// 创建附件
				if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
					attachments.addAll(processAttachments(userId, comment.getId(), cmd.getAttachments()));
					attachmentProvider.createAttachments(EhNewsAttachments.class, attachments);
				}

				news.setChildCount(news.getChildCount() + 1L);
				newsProvider.updateNews(news);
				return true;
			});
			return null;
		});

		AddNewsCommentResponse commentDTO = ConvertHelper.convert(comments.get(0), AddNewsCommentResponse.class);
		commentDTO.setAttachments(attachments.stream().map(a -> ConvertHelper.convert(a, NewsAttachmentDTO.class))
				.collect(Collectors.toList()));

		return commentDTO;
	}

	private void checkCommentParameter(Long userId, AddNewsCommentCommand cmd) {
		// 检查owner是否存在
		checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());

		// 检查评论类型
		checkCommentType(userId, cmd.getContentType());
		// 检查附件类型
		if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
			cmd.getAttachments().forEach(a -> checkCommentType(userId, a.getContentType()));
		}
	}

	private void checkCommentType(Long userId, String contentType) {
		if (StringUtils.isEmpty(contentType)) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", contentType=" + contentType);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_INPUT_PARAM_INVALID, "Invalid parameters");
		}
		NewsCommentContentType commentContentType = NewsCommentContentType.fromCode(contentType);
		if (commentContentType == null) {
			LOGGER.error("Invalid content type, operatorId=" + userId + ", contentType=" + contentType);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_COMMENT_CONTENT_TYPE_INVALID, "Invalid content type");
		}
	}

	private Comment processComment(Long userId, Long newsId, AddNewsCommentCommand cmd) {
		Comment comment = new Comment();
		comment.setOwnerId(newsId);
		comment.setContentType(cmd.getContentType().toLowerCase());
		comment.setContent(cmd.getContent());
		comment.setStatus(CommentStatus.ACTIVE.getCode());
		comment.setCreatorUid(userId);
		return comment;
	}

	private Comment processComment(Long userId, Long newsId, AddNewsCommentForWebCommand cmd) {
		AddNewsCommentCommand newCmd = ConvertHelper.convert(cmd, AddNewsCommentCommand.class);
		return processComment(userId, newsId, newCmd);
	}

	private List<Attachment> processAttachments(Long userId, Long commentId, List<AttachmentDescriptor> attachments) {
		if (attachments == null || attachments.size() == 0) {
			return null;
		}
		return attachments.stream().map(attachmentDescriptor -> {
			Attachment attachment = ConvertHelper.convert(attachmentDescriptor, Attachment.class);
			attachment.setOwnerId(commentId);
			attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			attachment.setCreatorUid(userId);
			return attachment;
		}).collect(Collectors.toList());
	}

	@Override
	public ListNewsCommentResponse listNewsComment(ListNewsCommentCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();

		List<Comment> comments = commentProvider.listCommentByOwnerIdWithPage(EhNewsComment.class, newsId, pageAnchor,
				pageSize + 1);
		ListNewsCommentResponse response = new ListNewsCommentResponse();
		News news = newsProvider.findNewsById(newsId);
		response.setCommentCount(news.getChildCount());

		if (comments != null && comments.size() > 0) {
			if (comments.size() > pageSize) {
				comments.remove(comments.size() - 1);
				pageAnchor = comments.get(comments.size() - 1).getId();
			} else {
				pageAnchor = null;
			}

			List<NewsCommentDTO> list = new ArrayList<>();
			populateCommentAttachment(userId, comments, list);
			populateCommentUser(userId, list);

			response.setNextPageAnchor(pageAnchor);
			response.setCommentList(list);
		}

		return response;
	}

	@Override
	public GetNewsCommentResponse getNewsComment(GetNewsCommentCommand cmd) {

		// 获取评论
		Comment comment = commentProvider.findCommentById(EhNewsComment.class, cmd.getCommentId());
		if (null == comment) {
			return new GetNewsCommentResponse();
		}

		List<NewsCommentDTO> dtos = new ArrayList<>();
		List<Comment> comments = new ArrayList<>(1);
		comments.add(comment);

		// 添加附件
		populateCommentAttachment(comment.getCreatorUid(), comments, dtos);

		// 添加头像等信息
		populateCommentUser(comment.getCreatorUid(), dtos);

		// 获取评论数
		Long commentCount = 0L;
		News news = newsProvider.findNewsById(comment.getOwnerId());
		if (null != news) {
			commentCount = news.getChildCount();
		}

		// 组装返回信息
		GetNewsCommentResponse resp = new GetNewsCommentResponse();
		if (!CollectionUtils.isEmpty(dtos)) {
			resp.setComment(dtos.get(0));
		}
		resp.setCommentCount(commentCount);
		return resp;

	}

	private void populateCommentUser(Long userId, List<NewsCommentDTO> list) {
		list.forEach(c -> {
			User creator = userProvider.findUserById(c.getCreatorUid());
			if (creator != null) {
				String creatorNickName = creator.getNickName();
				String creatorAvatar = creator.getAvatar();
				c.setCreatorNickName(creatorNickName);
				c.setCreatorAvatar(creatorAvatar);
				if (creatorAvatar != null && creatorAvatar.length() > 0) {
					String avatarUrl = getResourceUrlByUir(userId, creatorAvatar, EntityType.USER.getCode(),
							c.getCreatorUid());
					c.setCreatorAvatarUrl(avatarUrl);
				}
			}
			c.setDeleteFlag(userId.longValue() == c.getCreatorUid().longValue() ? NewsCommentDeleteFlag.DELETE.getCode()
					: NewsCommentDeleteFlag.NONE.getCode());
		});
	}

	private void populateCommentAttachment(Long userId, List<Comment> comments, List<NewsCommentDTO> list) {
		Map<Long, NewsCommentDTO> commentMap = new HashMap<>();
		List<Long> commentIds = new ArrayList<>();
		list.addAll(comments.stream().map(c -> {
			NewsCommentDTO commentDTO = ConvertHelper.convert(c, NewsCommentDTO.class);
			commentMap.put(c.getId(), commentDTO);
			commentIds.add(c.getId());
			return commentDTO;
		}).collect(Collectors.toList()));

		attachmentProvider.listAttachmentByOwnerIds(EhNewsAttachments.class, commentIds).forEach(a -> {
			NewsAttachmentDTO attachmentDTO = convertAttachmentToNewsAttachmentDTO(userId, a);
			NewsCommentDTO commentDTO = commentMap.get(a.getOwnerId());
			List<NewsAttachmentDTO> attachments = commentDTO.getAttachments();
			if (attachments == null) {
				commentDTO.setAttachments(new ArrayList<>());
			}
			commentDTO.getAttachments().add(attachmentDTO);
		});
	}

	private NewsAttachmentDTO convertAttachmentToNewsAttachmentDTO(Long userId, Attachment a) {
		NewsAttachmentDTO attachment = ConvertHelper.convert(a, NewsAttachmentDTO.class);

		String contentUri = attachment.getContentUri();
		if (contentUri != null && contentUri.length() > 0) {
			try {
				String url = contentServerService.parserUri(contentUri, EntityType.NEWS.getCode(),
						attachment.getOwnerId());
				attachment.setContentUrl(url);

				ContentServerResource resource = contentServerService.findResourceByUri(contentUri);
				if (resource != null) {
					attachment.setSize(resource.getResourceSize());
					attachment.setMetadata(resource.getMetadata());
				}
			} catch (Exception e) {
				LOGGER.error("Failed to parse attachment uri, userId=" + userId + ", commentId="
						+ attachment.getOwnerId() + ", attachmentId=" + attachment.getId(), e);
			}
		} else {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("The content uri is empty, userId=" + userId + ", attchmentId=" + attachment.getId());
			}
		}
		return attachment;
	}

	private String getResourceUrlByUir(long userId, String uri, String ownerType, Long ownerId) {
		String url = null;
		if (uri != null && uri.length() > 0) {
			try {
				url = contentServerService.parserUri(uri, ownerType, ownerId);
			} catch (Exception e) {
				LOGGER.error("Failed to parse uri, userId=" + userId + ", uri=" + uri + ", ownerType=" + ownerType
						+ ", ownerId=" + ownerId, e);
			}
		}

		return url;
	}

	@Override
	public void deleteNewsComment(DeleteNewsCommentCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());
		deleteNewsComment(userId, cmd.getNewsToken(), cmd.getId());
	}

	@Override
	public void updateNewsTag(UpdateNewsTagCommand cmd) {
		NewsTag parentTag = ConvertHelper.convert(cmd, NewsTag.class);
		parentTag.setParentId(0l);
		dbProvider.execute((TransactionStatus status) -> {
			Long parentId = 0l;
			if (parentTag.getId() == null) {
				parentId = newsProvider.createNewsTag(parentTag);
			} else {
				parentId = parentTag.getId();
				newsProvider.updateNewsTag(parentTag);
			}

			List<NewsTagDTO> tags = cmd.getTags();
			long order = 0L;
			if (tags != null)
				for (NewsTagDTO dto : tags) {
					NewsTag tag = ConvertHelper.convert(dto, NewsTag.class);

					tag.setDefaultOrder(order++);

					tag.setParentId(parentId);
					tag.setNamespaceId(parentTag.getNamespaceId());
					tag.setOwnerType(parentTag.getOwnerType());
					tag.setOwnerId(parentTag.getOwnerId());
					tag.setCategoryId(cmd.getCategoryId());
					if (tag.getId() == null) {
						newsProvider.createNewsTag(tag);
					} else
						newsProvider.updateNewsTag(tag);
				}
			return null;
		});
	}

	@Override
	public GetNewsTagResponse getNewsTag(GetNewsTagCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		if (cmd.getPageSize() == null)
			pageSize = 9999999;
		List<NewsTag> parentTags = newsProvider.listNewsTag(UserContext.getCurrentNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getIsSearch(), 0l,
				cmd.getPageAnchor(), pageSize + 1, cmd.getCategoryId());
		List<NewsTagDTO> result = parentTags.stream().map(r -> ConvertHelper.convert(r, NewsTagDTO.class))
				.collect(Collectors.toList());
		result.stream().forEach(r -> {
			List<NewsTag> tags = newsProvider.listNewsTag(UserContext.getCurrentNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), null, r.getId(), null,
					null, r.getCategoryId());
			List<NewsTagDTO> list = tags.stream().map(t -> ConvertHelper.convert(t, NewsTagDTO.class))
					.collect(Collectors.toList());
			r.setChildTags(JSONObject.toJSONString(list));
		});
		GetNewsTagResponse response = new GetNewsTagResponse();
		response.setTags(result);
		if (result.size() > pageSize) {
			response.setPageAnchor(result.get(result.size() - 1).getId());
			response.getTags().remove(result.size() - 1);
		} else
			response.setPageAnchor(null);
		return response;
	}

	@Override
	public ListNewsBySceneResponse listNewsByScene(ListNewsBySceneCommand cmd) {
		ListNewsCommand listCmd = new ListNewsCommand();
		listCmd.setOwnerType(NewsOwnerType.COMMUNITY.getCode());
		listCmd.setOwnerId(getCommunityIdByAppContext());
		listCmd.setCategoryId(cmd.getCategoryId());
		listCmd.setPageAnchor(cmd.getPageAnchor());
		listCmd.setPageSize(cmd.getPageSize());
		listCmd.setStatus(NewsStatus.ACTIVE.getCode());
		ListNewsBySceneResponse response = ConvertHelper.convert(listNews(listCmd, true),
				ListNewsBySceneResponse.class);

		// 填充app端点击“查看更多”返回页面
		filledRenderUrl(response, UserContext.getCurrentNamespaceId(), cmd.getGroupId(), cmd.getWidget(),
				cmd.getCategoryId());

		return response;

	}

	private SceneTokenDTO getNamespaceFromSceneToken(Long userId, String sceneToken) {
		if (StringUtils.isEmpty(sceneToken)) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", sceneToken=" + sceneToken);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_INPUT_PARAM_INVALID, "Invalid parameters");
		}
		SceneTokenDTO sceneTokenDTO = WebTokenGenerator.getInstance().fromWebToken(sceneToken, SceneTokenDTO.class);
		if (sceneTokenDTO == null || sceneTokenDTO.getNamespaceId() == null) {
			LOGGER.error("scene token invalid, operatorId=" + userId + ", sceneToken=" + sceneToken);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_SCENETOKEN_INVALID, "scene token invalid");
		}

		// 检查游客是否能继续访问此场景 by sfyan 20161009
		userService.checkUserScene(SceneType.fromCode(sceneTokenDTO.getScene()));
		return sceneTokenDTO;
	}

	@Override
	public void setNewsLikeFlagByScene(SetNewsLikeFlagBySceneCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		//TODO，标准版不用校验场景了
		//getNamespaceFromSceneToken(userId, cmd.getSceneToken());
		setNewsLikeFlag(userId, cmd.getNewsToken());
	}

	@Override
	public AddNewsCommentBySceneResponse addNewsCommentByScene(AddNewsCommentBySceneCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		// 检查参数
		checkCommentParameter(userId, cmd);

		// 黑名单权限校验 by sfyan20160213
		checkBlacklist(null, null);
		final List<Comment> comments = new ArrayList<>();
		final List<Attachment> attachments = new ArrayList<>();

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {
			dbProvider.execute(s -> {
				News news = findNewsById(userId, newsId);
				// 创建评论
				Comment comment = processComment(userId, newsId, cmd);
				comments.add(comment);
				commentProvider.createComment(EhNewsComment.class, comment);

				// 创建附件
				if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
					attachments.addAll(processAttachments(userId, comment.getId(), cmd.getAttachments()));
					attachmentProvider.createAttachments(EhNewsAttachments.class, attachments);
				}

				news.setChildCount(news.getChildCount() + 1L);
				newsProvider.updateNews(news);

				return true;
			});
			return null;
		});

		AddNewsCommentBySceneResponse commentDTO = ConvertHelper.convert(comments.get(0),
				AddNewsCommentBySceneResponse.class);
		commentDTO.setAttachments(attachments.stream().map(a -> ConvertHelper.convert(a, NewsAttachmentDTO.class))
				.collect(Collectors.toList()));

		// 填充创建者信息 add by yanjun 20170606
		populatePostUserInfo(userId, commentDTO);
		return commentDTO;
	}

	/**
	 * 填充创建者信息
	 * 
	 * @param userId
	 * @param commentDTO
	 */
	private void populatePostUserInfo(Long userId, AddNewsCommentBySceneResponse commentDTO) {
		if (userId == null || commentDTO == null) {
			return;
		}
		User creator = userProvider.findUserById(userId);
		if (creator != null) {
			commentDTO.setCreatorNickName(creator.getNickName());

			String creatorAvatar = creator.getAvatar();
			commentDTO.setCreatorAvatar(creatorAvatar);

			if (StringUtils.isEmpty(creatorAvatar)) {
				creatorAvatar = configProvider.getValue(creator.getNamespaceId(), "user.avatar.default.url", "");
			}

			if (creatorAvatar != null && creatorAvatar.length() > 0) {
				String avatarUrl = getResourceUrlByUir(userId, creatorAvatar, EntityType.USER.getCode(), userId);
				commentDTO.setCreatorAvatarUrl(avatarUrl);
			}
		}
	}

	private Comment processComment(Long userId, Long newsId, AddNewsCommentBySceneCommand cmd) {
		Comment comment = new Comment();
		comment.setOwnerId(newsId);
		comment.setContentType(cmd.getContentType().toLowerCase());
		comment.setContent(cmd.getContent());
		comment.setStatus(CommentStatus.ACTIVE.getCode());
		comment.setCreatorUid(userId);
		return comment;
	}

	private void checkCommentParameter(Long userId, AddNewsCommentBySceneCommand cmd) {
		// 新的统一评论接口没有scenetoken，不需要常见检查 add by yanjun 20170602
		// 检查namespace是否存在
		// getNamespaceFromSceneToken(userId, cmd.getSceneToken());

		// 检查评论类型
		checkCommentType(userId, cmd.getContentType());
		// 检查附件类型
		if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
			cmd.getAttachments().forEach(a -> checkCommentType(userId, a.getContentType()));
		}
	}

	@Override
	public void deleteNewsCommentByScene(DeleteNewsCommentBySceneCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		// 新的统一评论接口没有scenetoken，不需要常见检查 add by yanjun 20170602
		// getNamespaceFromSceneToken(userId, cmd.getSceneToken());
		deleteNewsComment(userId, cmd.getNewsToken(), cmd.getId());
	}

	private void deleteNewsComment(Long userId, String newsToken, Long commentId) {
		Long newsId = checkNewsToken(userId, newsToken);
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {
			dbProvider.execute(s -> {
				News news = findNewsById(userId, newsId);
				Comment comment = commentProvider.findCommentById(EhNewsComment.class, commentId);
				checkIfCommentCorrect(userId, newsId, commentId, comment);
				comment.setDeleterUid(userId);
				comment.setStatus(CommentStatus.INACTIVE.getCode());
				comment.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				commentProvider.updateComment(EhNewsComment.class, comment);

				news.setChildCount(news.getChildCount() <= 0 ? 0 : news.getChildCount() - 1L);
				newsProvider.updateNews(news);
				return true;
			});
			return null;
		});
	}

	private void checkIfCommentCorrect(Long userId, Long newsId, Long commentId, Comment comment) {
		if (comment == null || comment.getOwnerId().longValue() != newsId.longValue()) {
			LOGGER.error("newsId and commentId not match, operatorId=" + userId + ", newsId=" + newsId + ", commentId"
					+ commentId);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_NEWSID_COMMENTID_NOT_MATCH, "newsId and commentId not match");
		}
		// if (comment.getCreatorUid().longValue() != userId.longValue()) {
		// LOGGER.error("userId and commentId not match, operatorId=" + userId +
		// ", userId=" + userId + ", commentId"
		// + commentId);
		// throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
		// NewsServiceErrorCode.ERROR_NEWS_NEWSID_COMMENTID_NOT_MATCH, "userId
		// and commentId not match");
		// }
	}

	@Override
	public void syncNews(SyncNewsCommand cmd) {
		if (cmd.getId() != null) {
			syncNews(cmd.getId());
		} else {
			searchProvider.clearType(SearchUtils.NEWS);
			// 一次同步200条，防止一次同步太多内存被压爆
			Integer pageSize = 200;
			int i = 0;
			while (true) {
				Long from = (long) (i * pageSize);
				List<News> list = newsProvider.findAllActiveNewsByPage(from, pageSize);
				if (list != null && list.size() > 0) {
					StringBuilder sb = new StringBuilder();
					list.forEach(n -> {
						n.setCommunityIds(newsProvider.listNewsCommunities(n.getId()));
						n.setTag(newsProvider.listNewsTagVals(n.getId()).stream().map(r -> r.getNewsTagId())
								.collect(Collectors.toList()));
						// 正则表达式去掉content中的富文本内容 modified by xiongying20160908
						String content = n.getContent();
						content = removeTag(content);
						n.setContent(content);
						sb.append("{\"index\":{\"_id\":\"").append(n.getId()).append("\"}}\n")
								.append(JSONObject.toJSONString(n)).append("\n");
					});

					searchProvider.bulk(SearchUtils.NEWS, sb.toString());

					if (list.size() < pageSize) {
						break;
					}
					i++;
				} else {
					break;
				}
			}
		}
	}

	public static String removeTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // script
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // style
		String regEx_html = "<[^>]+>"; // HTML tag
		String regEx_space = "\\s+|\t|\r|\n";// other characters

		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll("");

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll("");

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll("");

		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(" ");

		return htmlStr;

	}

	private void syncNews(Long id) {
		News news = newsProvider.findNewsById(id);
		if (news != null) {
			news.setCommunityIds(newsProvider.listNewsCommunities(id));
			news.setTag(
					newsProvider.listNewsTagVals(id).stream().map(r -> r.getNewsTagId()).collect(Collectors.toList()));
			// 正则表达式去掉content中的富文本内容 modified by xiongying20160908
			String content = news.getContent();
			content = removeTag(content);
			news.setContent(content);
			searchProvider.insertOrUpdate(SearchUtils.NEWS, news.getId().toString(), JSONObject.toJSONString(news));
		}
	}

	public void syncNewsWhenDelete(Long id) {
		searchProvider.deleteById(SearchUtils.NEWS, id.toString());
	}

	@Override
	public SearchContentsBySceneReponse searchNewsByScene(SearchContentsBySceneCommand cmd) {
		SearchContentsBySceneReponse response = new SearchContentsBySceneReponse();
		final Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		SearchTypes searchType = userService.getSearchTypes(namespaceId, SearchContentType.NEWS.getCode());
		if (StringUtils.isEmpty(cmd.getKeyword())) {
			ListNewsBySceneCommand command = new ListNewsBySceneCommand();

			command.setSceneToken(cmd.getSceneToken());
			command.setPageAnchor(cmd.getPageAnchor());
			command.setPageSize(cmd.getPageSize());
			ListNewsBySceneResponse news = listNewsByScene(command);
			if (news != null) {
				response.setNextPageAnchor(news.getNextPageAnchor());
				if (news.getNewsList() != null && news.getNewsList().size() > 0) {
					List<ContentBriefDTO> dtos = new ArrayList<ContentBriefDTO>();
					for (BriefNewsDTO briefNews : news.getNewsList()) {
						ContentBriefDTO dto = new ContentBriefDTO();
						dto.setNewsToken(briefNews.getNewsToken());
						dto.setContent(briefNews.getContentAbstract());
						dto.setSubject(briefNews.getTitle());
						dto.setPostUrl(briefNews.getCoverUri());
						if (searchType != null) {
							dto.setSearchTypeId(searchType.getId());
							dto.setSearchTypeName(searchType.getName());
						}

						NewsFootnote footNote = new NewsFootnote();
						footNote.setAuthor(briefNews.getAuthor());
						footNote.setCreateTime(briefNews.getPublishTime().toString());
						footNote.setSourceDesc(briefNews.getSourceDesc());
						footNote.setNewsToken(briefNews.getNewsToken());
						dto.setFootnoteJson(StringHelper.toJsonString(footNote));

						dtos.add(dto);
					}
					response.setDtos(dtos);
				}
			}

			return response;
		}

		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();

		String jsonString = getSearchJson(getCommunityIdByAppContext(), null, userId, namespaceId, null, cmd.getKeyword(), null, pageAnchor, pageSize,
				NewsStatus.ACTIVE.getCode(), true);
		// 需要查询的字段
		String fields = "id,title,publishTime,author,sourceDesc,coverUri,contentAbstract,likeCount,childCount,topFlag";

		// 从es查询
		JSONArray result = searchProvider.queryTopHits(SearchUtils.NEWS, jsonString, fields);

		// 处理分页
		Long nextPageAnchor = null;
		if (result.size() > pageSize) {
			result.remove(result.size() - 1);
			nextPageAnchor = pageAnchor + 1;
		}

		// 处理返回的topHit
		List<ContentBriefDTO> dtos = new ArrayList<ContentBriefDTO>();
		for (int i = 0; i < result.size(); i++) {
			ContentBriefDTO dto = new ContentBriefDTO();
			JSONObject highlight = result.getJSONObject(i).getJSONObject("highlight");
			JSONObject source = result.getJSONObject(i).getJSONObject("_source");

			if (StringUtils.isEmpty(highlight.getString("title"))) {
				dto.setSubject(source.getString("title"));
			} else {
				String subject = getFromHighlight(highlight, "title");
				dto.setSubject(subject);
			}

			if (StringUtils.isEmpty(highlight.getString("content"))) {
				dto.setContent(source.getString("content"));
			} else {
				String content = getFromHighlight(highlight, "content");
				dto.setContent(content);
			}

			dto.setPostUrl(source.getString("coverUri"));

			dto.setSearchTypeId(searchType.getId());
			dto.setSearchTypeName(searchType.getName());
			dto.setContentType(searchType.getContentType());
			NewsFootnote footNote = new NewsFootnote();
			footNote.setAuthor(source.getString("author"));
			footNote.setCreateTime(timeToStr(source.getTimestamp("publishTime")));
			footNote.setNewsToken(WebTokenGenerator.getInstance().toWebToken(source.getLong("id")));

			dto.setNewsToken(footNote.getNewsToken());

			if (StringUtils.isEmpty(highlight.getString("sourceDesc"))) {
				footNote.setSourceDesc(source.getString("sourceDesc"));
			} else {
				String sourceDesc = getFromHighlight(highlight, "sourceDesc");
				footNote.setSourceDesc(sourceDesc);
			}

			dto.setFootnoteJson(StringHelper.toJsonString(footNote));

			dtos.add(dto);
		}

		response.setDtos(dtos);
		response.setNextPageAnchor(nextPageAnchor);
		return response;
	}

	// 原来直接使用getString，若是数据则会前后多出"[]"。此处先当jsonArray处理，有异常则使用原始的方式。 edit by
	// yanjun 20170720
	private String getFromHighlight(JSONObject highlight, String key) {
		try {
			JSONArray jsonarr = JSONArray.parseArray(highlight.getString(key));
			return jsonarr.getString(0);
		} catch (Exception ex) {
			return highlight.getString(key);
		}

	}

	private String timeToStr(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(time);
	}

	@Override
	public AddNewsCommentResponse addNewsForWebComment(AddNewsCommentForWebCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		// 检查参数
		// 检查评论类型
		checkCommentType(userId, cmd.getContentType());
		// 检查附件类型
		if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
			cmd.getAttachments().forEach(a -> checkCommentType(userId, a.getContentType()));
		}

		final List<Comment> comments = new ArrayList<>();
		final List<Attachment> attachments = new ArrayList<>();

		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {
			dbProvider.execute(s -> {
				News news = findNewsById(userId, newsId);
				// 创建评论
				Comment comment = processComment(userId, newsId, cmd);
				comments.add(comment);
				commentProvider.createComment(EhNewsComment.class, comment);

				// 创建附件
				if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
					attachments.addAll(processAttachments(userId, comment.getId(), cmd.getAttachments()));
					attachmentProvider.createAttachments(EhNewsAttachments.class, attachments);
				}

				news.setChildCount(news.getChildCount() + 1L);
				newsProvider.updateNews(news);
				return true;
			});
			return null;
		});

		AddNewsCommentResponse commentDTO = ConvertHelper.convert(comments.get(0), AddNewsCommentResponse.class);
		commentDTO.setAttachments(attachments.stream().map(a -> ConvertHelper.convert(a, NewsAttachmentDTO.class))
				.collect(Collectors.toList()));

		return commentDTO;
	}

	@Override
	public void setNewsLikeFlagForWeb(SetNewsLikeFlagForWebCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		setNewsLikeFlag(userId, cmd.getNewsToken());
	}

	@Override
	public GetCategoryIdByEntryIdResponse getCategoryIdByEntryId(GetCategoryIdByEntryIdCommand cmd) {
		NewsCategory category = newsProvider.getCategoryIdByEntryId(cmd.getEntryId(),
				UserContext.getCurrentNamespaceId());
		if (category == null) {
			return new GetCategoryIdByEntryIdResponse(0L);
		}
		return new GetCategoryIdByEntryIdResponse(category.getId());
	}

	@Override
	public void publishNews(publishNewsCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		if (cmd.getNewsToken() == null) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_INPUT_PARAM_INVALID, "Invalid parameters");
		}
		final Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		News news = findNewsById(userId, newsId);
		if (null == news) {
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID, "news is not exist");
		}

		// 权限限制
		checkUserProjectLegal(userId, cmd.getCurrentPMId(), news.getOwnerId(), cmd.getAppId());

		NewsStatus eStatus = NewsStatus.fromCode(news.getStatus());
		if (eStatus == NewsStatus.DRAFT) {
			news.setStatus(NewsStatus.ACTIVE.getCode());
			newsProvider.updateNews(news);

			syncNews(news.getId());
		}
	}

	private List<Long> getUserOwnProjectIds(Long organizationId, Long originAppId) {
		ListUserRelatedProjectByModuleCommand listCmd = new ListUserRelatedProjectByModuleCommand();
		listCmd.setOrganizationId(organizationId);
		listCmd.setModuleId(NEWS_MODULE_ID);
		listCmd.setUserId(UserContext.currentUserId());
		listCmd.setAppId(originAppId);
		listCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
		List<ProjectDTO> projectDtos = serviceModuleService.listUserRelatedProjectByModuleId(listCmd);

		return projectDtos.stream().map(r -> {
			return r.getProjectId();
		}).collect(Collectors.toList());
	}

	/**
	 * @Function: NewsServiceImpl.java
	 * @Description: 查询该用户是否拥有给定的项目权限
	 *
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年4月19日 下午6:25:41
	 *
	 */
	private void checkUserProjectLegal(Long userId, Long organizationId, Long projectId, Long appId) {

		ListUserRelatedProjectByModuleCommand listCmd = new ListUserRelatedProjectByModuleCommand();
		listCmd.setOrganizationId(organizationId);
		listCmd.setModuleId(NEWS_MODULE_ID);
		listCmd.setUserId(userId);
		listCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
		listCmd.setAppId(appId);
		List<ProjectDTO> projectIds = serviceModuleService.listUserRelatedProjectByModuleId(listCmd);
		if (null == projectIds || projectIds.isEmpty()) {
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_USER_PROJECT_NOT_LEGAL, "current user is unauthorized in this project");
		}

		for (ProjectDTO project : projectIds) {
			if (project.getProjectId().equals(projectId)) {
				return;
			}
		}

		throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
				NewsServiceErrorCode.ERROR_USER_PROJECT_NOT_LEGAL, "current user is unauthorized in this project");
	}

	/**
	 * @Function: NewsServiceImpl.java
	 * @Description:填充app端新闻“查看更多”跳转地址 园区快讯v1.8 #issue-26479
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年4月20日 下午3:45:54
	 *
	 */
	private void filledRenderUrl(ListNewsBySceneResponse response, Integer namespaceId, Long groupId, String widget, Long inputCategoryId) {

		//旧版本可能传空
		Long categoryId = inputCategoryId == null ? 0L : inputCategoryId;
		
		//旧版本无法获得groupId
		if (null == groupId) {
			filledRenderUrl(response, namespaceId, widget, categoryId);
			return;
		}
		
		//获取item_group
		PortalItemGroup group = portalItemGroupProvider.findPortalItemGroupById(groupId);
		if (null == group) {
			return;
		}
		
		//获取模块
		JSONObject json = JSONObject.parseObject(group.getInstanceConfig());
		Long moduleAppId = json.getLong("moduleAppId");
		ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(moduleAppId);
		if (null == moduleApp) {
			return;
		}
		
		// 拼装renderUrl
		JSONObject moduleJson = JSONObject.parseObject(moduleApp.getInstanceConfig());
		String timeWidgetStyle = moduleJson.getString("timeWidgetStyle");
		String title = moduleApp.getName();
		String renderUrl = getNewsRenderUrl(namespaceId, categoryId, title, group.getWidget(), timeWidgetStyle);

		// 设置response
		response.setNeedUseUrl(NewsNormalFlag.ENABLED.getCode());
		response.setRenderUrl(renderUrl);
		response.setTitle(title);
	}
	
	
	/**   
	* @Function: NewsServiceImpl.java
	* @Description: 旧版本app端无法传送group id，使用该方法
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年4月24日 下午3:44:29 
	*
	*/
	private void filledRenderUrl(ListNewsBySceneResponse response, Integer namespaceId, String widget,
			Long categoryId) {

		// 获取最新的版本
		PortalVersion maxBigVersion = portalVersionProvider.findMaxBigVersion(namespaceId);
		if (null == maxBigVersion) {
			return;
		}

		// 获取相应应用
		ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleApp(namespaceId, maxBigVersion.getId(),
				NEWS_MODULE_ID, "" + categoryId);
		if (null == moduleApp) {
			return;
		}

		// 拼装renderUrl
		JSONObject json = JSONObject.parseObject(moduleApp.getInstanceConfig());
		String timeWidgetStyle = json.getString("timeWidgetStyle");
		String title = moduleApp.getName();
		String renderUrl = getNewsRenderUrl(namespaceId, categoryId, title, widget, timeWidgetStyle);

		// 设置response
		response.setNeedUseUrl(NewsNormalFlag.ENABLED.getCode());
		response.setRenderUrl(renderUrl);
		response.setTitle(title);
	}


	@Override
	public String getNewsRenderUrl(Integer namespaceId, Long categoryId, String title, String widget,
			String timeWidgetStyle) {

		String encodeTitile = null;
		if (null != title) {
			try {
				// 标题为中文时需要转码
				encodeTitile = URLEncoder.encode(title, "utf-8");
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("news title name not can't be encoded :" + title);
			}
		}
	
		String homeUrl = configProvider.getValue(0, "home.url", "core.zuolin.com");
		StringBuilder renderUrl = new StringBuilder(homeUrl);
		renderUrl.append("/park-news-web/build/index.html?");
		renderUrl.append("categoryId=" + categoryId);
		
		if (null != title) {
			renderUrl.append("&title=" + encodeTitile);
		}
		
		if (null == widget) {
			renderUrl.append("&widget=NewsFlash");
		} else {
			renderUrl.append("&widget=" + widget);
		}
		renderUrl.append("&timeWidgetStyle=" + timeWidgetStyle);
		renderUrl.append("&ns=" + namespaceId);
		renderUrl.append("#/newsList#sign_suffix");
		return renderUrl.toString();
	}

	/**
	 * @see com.everhomes.news.NewsService#listNews(com.everhomes.rest.news.ListNewsCommand)
	 * @Function: NewsServiceImpl.java
	 * @Description: 用于后台显示新闻列表
	 *
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年4月23日 下午1:14:30
	 *
	 */
	public ListNewsResponse listNews(ListNewsCommand cmd) {
		return listNews(cmd, false);
	}

	/**
	 * @Function: NewsServiceImpl.java
	 * @Description: 在更新或新增新闻时，对摘要进行调整，避免摘要为空
	 *
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年4月24日 下午1:25:53
	 *
	 */
	private void adjustNewsContentAbstract(News news) {

		if (!StringUtils.isEmpty(news.getContentAbstract())) {
			return;
		}

		//对html文本进行解析
		String contentAbstract = NewsUtils.Html2Text(news.getContent());
		contentAbstract = StringUtils.isEmpty(contentAbstract) ? news.getTitle() : contentAbstract;

		//文字较短，直接赋值
		if (contentAbstract.length() < NEWS_CONTENT_ABSTRACT_DEFAULT_LEN) {
			news.setContentAbstract(contentAbstract);
			return;
		}
		
		//文字较长，截取默认长度
		news.setContentAbstract(contentAbstract.substring(0, NEWS_CONTENT_ABSTRACT_DEFAULT_LEN));
	}

	/**
	 * 前端或app可能会传来父标签Id，作为查询一整个类型的条件
	 * 这时需要将子标签都获取，作为查询条件。
	 * 且不同的类型是需要做交集的。
	 * @param tagIds
	 * @return
	 */
	private List<TagSearchItem> getTagSearchItems(Integer namespaceId, Long categoryId, List<Long> tagIds) {

		if (null == tagIds || tagIds.size() <= 0) {
			return null;
		}

		// 1.去重
		List<Long> newTagIds = new ArrayList<Long>(new HashSet<Long>(tagIds));


		Map<Long, List<Long>> itemMap = new HashMap<>(10); //key为parentId, value为childIdList
		// 获取所有的标签
		List<NewsTag> newsTags = getAllNewsTags(namespaceId, categoryId);
		if (CollectionUtils.isEmpty(newsTags)) {
			return null;
		}

		// 保存tagIds至itemMap
		for (Long tagId : newTagIds) {

			if (null == tagId || tagId <= 0) {
				continue;
			}

			// 获取该tagId的标签信息
			for (NewsTag newsTag : newsTags) {
				if (!tagId.equals(newsTag.getId())) {
					continue;
				}

				formTagIdMap(itemMap, newsTag, newsTags);
				break;
			}
		}

		// 根据tagIdMap来组装筛选条件
		return formTagSearchItems(itemMap);
	}

	/**
	 * 获取当前域名和版本下的所有标签
	 *
	 * @param namespaceId
	 * @return
	 */
	private List<NewsTag> getAllNewsTags(Integer namespaceId, Long categoryId) {
		return newsProvider.listNewsTag(namespaceId,null, null, null, null, null, null, categoryId);
	}

	/**
	* @Function: NewsServiceImpl.java
	* @Description: 构造以父节点为key，孩子节点为valueList的map
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年6月4日 下午4:01:07
	*
	*/
	private void formTagIdMap(Map<Long, List<Long>> itemMap, NewsTag newsTag, List<NewsTag> newsTags) {

		List<Long> valueList = null;

		// 如果是父亲节点，将所有孩子节点都找出来
		if (isParentTag(newsTag)) {

			if (null == itemMap.get(newsTag.getId())) {
				valueList = new ArrayList<Long>(10);
			}

			// 将所有孩子节点添加上去
			for (NewsTag tag : newsTags) {
				if (newsTag.getId().equals(tag.getParentId())) {
					valueList.add(tag.getId());
				}
			}

			itemMap.put(newsTag.getId(), valueList);
			return;
		}

		// 孩子节点
		valueList = itemMap.get(newsTag.getParentId());
		if (null == valueList) {
			valueList = new ArrayList<Long>(10);
		}

		valueList.add(newsTag.getId());
		itemMap.put(newsTag.getParentId(), valueList);
	}

	/**
	* @Function: NewsServiceImpl.java
	* @Description: 根据tagId map生成查询项
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年6月4日 下午4:25:58
	*
	*/
	private List<TagSearchItem> formTagSearchItems(Map<Long, List<Long>> itemMap) {
		if (itemMap.isEmpty()) {
			return null;
		}

		List<TagSearchItem> searchItems = new ArrayList<>(10);
		for (Long parentId : itemMap.keySet()) {
			TagSearchItem item = new TagSearchItem();
			item.setParentTagId(parentId);
			item.setChildTagIds(itemMap.get(parentId));
			searchItems.add(item);
		}

		return searchItems;
	}

	private boolean isParentTag(NewsTag tag) {
		return 0 == tag.getParentId();
	}

	@Override
	public String getNewsQR(UpdateNewsCommand cmd) {
        String url = this.configProvider.getValue("home.url","localhost") + "/html/news_text_preview.html?newsToken=";
		String token;
//		if(null == cmd.getId()){
//			CreateNewsResponse result = this.createNews(ConvertHelper.convert(cmd,CreateNewsCommand.class));
//			token = result.getNewsToken();
//		} else {
//			this.updateNews(cmd);
//			token = WebTokenGenerator.getInstance().toWebToken(cmd.getId());
//		}
		News news = processNewsCommand(UserContext.currentUserId(),UserContext.getCurrentNamespaceId(),ConvertHelper.convert(cmd,CreateNewsCommand.class));
		Long id = this.newsProvider.createNewPreview(news);
		token = WebTokenGenerator.getInstance().toWebToken(id);
		url += token;

		return url;
	}



	private News processNewsCommand(Long userId, Integer namespaceId, CreateNewsCommand cmd) {
		News news = ConvertHelper.convert(cmd, News.class);
		news.setNamespaceId(namespaceId);
		news.setOwnerType(cmd.getOwnerType());
		news.setContentType(NewsContentType.RICH_TEXT.getCode());
		news.setTopIndex(0L);
		news.setTopFlag(NewsTopFlag.NONE.getCode());
		news.setStatus(generateNewsStatus(cmd.getStatus()));
		news.setCreatorUid(userId);
		news.setDeleterUid(0L);
		news.setPhone(cmd.getPhone());

		//调整摘要
		adjustNewsContentAbstract(news);

		if (cmd.getPublishTime() != null) {
			news.setPublishTime(new Timestamp(cmd.getPublishTime()));
		}

		return news;
	}

	@Override
	public GetNewsDetailInfoResponse getNewsPreview(GetNewsContentCommand cmd) {
		Long userId = UserContext.currentUserId();
		Long newsId = WebTokenGenerator.getInstance().fromWebToken(cmd.getNewsToken(),Long.class);
		News news = this.newsProvider.findNewPreview(newsId);
		String content = news.getContent();
		GetNewsDetailInfoResponse response = convertNewsToNewsDTO(userId, news);
		response.setContent(content);
		return response;
	}

	@Override
	public void enableSelfDefinedConfig(GetSelfDefinedStateCommand cmd) {
		updateSelfDefinedConfig(cmd, true);
	}

	@Override
	public void disableSelfDefinedConfig(GetSelfDefinedStateCommand cmd) {
		updateSelfDefinedConfig(cmd, false);
	}

	private void updateSelfDefinedConfig(GetSelfDefinedStateCommand cmd, boolean enable) {

		if (!isIdValid(cmd.getCategoryId())) {
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_CATEGORY_ID_NOT_VALID, "Invalid category id");
		}

		Byte state = getSelfDefinedState(cmd.getProjectId(), cmd.getCategoryId());
		if (enable) {
			// 如果需要开启，而且当前是关闭状态，才进行创建
			if (TrueOrFalseFlag.FALSE.getCode().equals(state)) {
				createSelfDefinedConfig(cmd.getCategoryId(), cmd.getProjectId(), cmd.getCurrentPMId());
			}
			return;
		}

		if (TrueOrFalseFlag.TRUE.getCode().equals(state)) {
			// 关闭时删除所有该项目下的所有自定义配置
			deleteSelfDefinedConfig(cmd.getCategoryId(), cmd.getProjectId());
		}

	}

	private void deleteSelfDefinedConfig(Long categoryId, Long projectId) {
		// 删除tag
		newsProvider.deleteProjectNewsTags(projectId, categoryId);

		// 删除tagVal
	}

	private void createSelfDefinedConfig(Long categoryId, Long projectId, Long organizationId) {

		// 获取所有标签
		List<NewsTag> allTags = newsProvider.listNewsTag(
				UserContext.getCurrentNamespaceId(), NewsOwnerType.ORGANIZATION.getCode(), organizationId,
				null, null, null, null, categoryId);
		if (CollectionUtils.isEmpty(allTags)) {
			return;
		}

		//获取父子标签
		List<NewsTag> pTags = new ArrayList<>();
		List<NewsTag> cTags = new ArrayList<>();
		for (NewsTag tag : allTags) {
			if (0 == tag.getParentId()) {
				pTags.add(tag);
				continue;
			}
			cTags.add(tag);
		}

		if (0 == pTags.size()) {
			return;
		}

		dbProvider.execute(r -> {
			//创建父亲标签
			Map<Long,Long> parentIdDiffMap = new HashMap<>();
			for (NewsTag tag : pTags) {
				tag.setOwnerType(NewsOwnerType.COMMUNITY.getCode());
				tag.setOwnerId(projectId);
				parentIdDiffMap.put(tag.getId(), newsProvider.createNewsTag(tag));
			}

			//创建子标签
			for (NewsTag tag : cTags) {
				Long newParentId = parentIdDiffMap.get(tag.getParentId());
				if (null == newParentId) {
					continue;
				}

				tag.setOwnerType(NewsOwnerType.COMMUNITY.getCode());
				tag.setOwnerId(projectId);
				tag.setParentId(newParentId);
				newsProvider.createNewsTag(tag);
			}

			return null;
		});

	}

	private boolean isIdValid(Long id) {
		if (null == id || id < 0) {
			return false;
		}

		return true;
	}

	private Byte getSelfDefinedState(Long projectId, Long categoryId) {
		List<NewsTag> tags = getProjectParentTags(projectId, categoryId);
		return CollectionUtils.isEmpty(tags) ? TrueOrFalseFlag.FALSE.getCode() : TrueOrFalseFlag.TRUE.getCode();
	}

	@Override
	public GetSelfDefinedStateResponse getSelfDefinedState(GetSelfDefinedStateCommand cmd) {
		GetSelfDefinedStateResponse resp = new GetSelfDefinedStateResponse();
		resp.setIsOpen(getSelfDefinedState(cmd.getProjectId(), cmd.getCategoryId()));
		return resp;
	}

	private List<NewsTag> getProjectParentTags(Long projectId, Long categoryId) {
		return newsProvider.listParentTags(NewsOwnerType.COMMUNITY.getCode(), projectId, categoryId);
	}

	private List<NewsTag> getParentTags(String ownerType, Long ownerId, Long organizationId, Long categoryId) {

		List<NewsTag> tags = newsProvider.listParentTags(ownerType, ownerId, categoryId);
		if (!CollectionUtils.isEmpty(tags)) {
			return tags;
		}

		if (NewsOwnerType.COMMUNITY.getCode().equals(ownerType)) {
			return newsProvider.listParentTags(NewsOwnerType.ORGANIZATION.getCode(), organizationId, categoryId);
		}

		return null;
	}
	
	private Long getCommunityIdByAppContext() {
		AppContext appContext = UserContext.current().getAppContext();
		if (null != appContext.getCommunityId()) {
			return appContext.getCommunityId();
		}


		//旧版本的公司场景没有communityId跪了
		if(appContext != null && appContext.getCommunityId() == null && appContext.getOrganizationId() != null){
			List<OrganizationCommunityRequest> requests = organizationProvider.listOrganizationCommunityRequestsByOrganizationId(appContext.getOrganizationId());
			if(requests != null && requests.size() > 0){
				appContext.setCommunityId(requests.get(0).getCommunityId());
			}
		}

		//旧版本的家庭场景没有communityId，又跪了
		if(appContext != null && appContext.getCommunityId() == null && appContext.getFamilyId() != null){
			FamilyDTO family = familyProvider.getFamilyById(appContext.getFamilyId());
			if(family != null){
				appContext.setCommunityId(family.getCommunityId());
			}
		}
		 
		return appContext.getCommunityId();
	}


}
