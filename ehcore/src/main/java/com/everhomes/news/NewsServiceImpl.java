// @formatter:off
package com.everhomes.news;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.family.FamilyProvider;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.news.*;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
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
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.SearchTypes;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLike;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
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

	@Autowired
	private UserService userService ;
	
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
	private FamilyProvider familyProvider;

	@Override
	public CreateNewsResponse createNews(CreateNewsCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();

		// 检查参数等信息
		checkNewsParameter(userId, cmd);

		//黑名单权限校验 by sfyan20161213
		checkBlacklist(null, null);

		Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());

		News news = processNewsCommand(userId, namespaceId, cmd);

		dbProvider.execute((TransactionStatus status) -> {
			newsProvider.createNews(news);

			if (null != cmd.getCommunityIds()) {
				cmd.getCommunityIds().forEach(m -> {
					NewsCommunity newsCommunity = new NewsCommunity();
					newsCommunity.setNewsId(news.getId());
					newsCommunity.setCommunityId(m);
					newsProvider.createNewsCommunity(newsCommunity);
				});
			}
			return null;
		});

		syncNews(news.getId());

		CreateNewsResponse response = ConvertHelper.convert(news, CreateNewsResponse.class);
		response.setNewsToken(WebTokenGenerator.getInstance().toWebToken(news.getId()));
		return response;
	}

	private void checkBlacklist(String ownerType, Long ownerId){
		ownerType = StringUtils.isEmpty(ownerType) ? "" : ownerType;
		ownerId = null == ownerId ? 0L : ownerId;
		Long userId = UserContext.current().getUser().getId();
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserBlacklistAuthority(userId, ownerType, ownerId, PrivilegeConstants.BLACKLIST_NEWS);
	}

	private News processNewsCommand(Long userId, Integer namespaceId, CreateNewsCommand cmd) {
		News news = ConvertHelper.convert(cmd, News.class); 
		news.setNamespaceId(namespaceId);
		news.setOwnerType(cmd.getOwnerType().toLowerCase());
		news.setContentType(NewsContentType.RICH_TEXT.getCode());
		news.setTopIndex(0L);
		news.setTopFlag(NewsTopFlag.NONE.getCode());
		news.setStatus(NewsStatus.ACTIVE.getCode());
		news.setCreatorUid(userId);
		news.setDeleterUid(0L);
		if (StringUtils.isEmpty(news.getContentAbstract())) {
			news.setContentAbstract(news.getContent().substring(0,
					news.getContent().length() > 100 ? 100 : news.getContent().length()));
		}
		if (cmd.getPublishTime() != null) {
			news.setPublishTime(new Timestamp(cmd.getPublishTime()));
		}
		return news;
	}

	private void checkNewsParameter(Long userId, CreateNewsCommand cmd) {
		if (StringUtils.isEmpty(cmd.getTitle()) || StringUtils.isEmpty(cmd.getContent())) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}

		if (null == cmd.getCommunityIds() || cmd.getCommunityIds().isEmpty()) {
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE, NewsServiceErrorCode.ERROR_NEWS_VISIBLE_INVALID,
					"Invalid parameters");
		}
	}
	
	private void checkNewsParameterOfImport(Long userId, CreateNewsCommand cmd) {
		if (StringUtils.isEmpty(cmd.getTitle()) || StringUtils.isEmpty(cmd.getContent())) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_PROCESS_EXCEL_ERROR,
					"Invalid parameters");
		}
	}

	private Integer checkOwner(Long userId, Long ownerId, String ownerType) {
		if (ownerId == null || StringUtils.isEmpty(ownerType)) {
			LOGGER.error(
					"Invalid parameters, operatorId=" + userId + ", ownerType=" + ownerType + ", ownerId=" + ownerId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
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
		}else {
			Community community = communityProvider.findCommunityById(ownerId);
			if (community == null) {
				LOGGER.error(
						"Invalid owner id, operatorId=" + userId + ", ownerType=" + ownerType + ", ownerId=" + ownerId);
				throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
						NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID, "Invalid owner id");
			}
			return community.getNamespaceId();
		}

//		return organization;
	}

	@Override
	public void importNews(ImportNewsCommand cmd, MultipartFile[] files) {
		Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());
		// 读取Excel数据
		List<News> newsList = getNewsFromExcel(userId, namespaceId, cmd, files);

		dbProvider.execute(s -> {
			newsProvider.createNewsList(newsList);
			//导入时，默认全部园区可见
			List<OrganizationCommunity> organizationCommunities = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
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
			LOGGER.error("process Excel error, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_PROCESS_EXCEL_ERROR, "process Excel error");
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
					command.setOwnerId(cmd.getOwnerId());
					command.setOwnerType(cmd.getOwnerType());
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
					newsList.add(processNewsCommand(userId, namespaceId, command));
				}
			}
			return newsList;
		}
		LOGGER.error("excel data format is not correct.rowCount=" + resultList.size());
		throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
				NewsServiceErrorCode.ERROR_NEWS_PROCESS_EXCEL_ERROR,
				"excel data format is not correct");
	}

	private Long covertStringToLongTime(String string){
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
					NewsServiceErrorCode.ERROR_NEWS_PROCESS_EXCEL_ERROR,
					"date format error, date: " + string);
		}
	}
	
	@Override
	public ListNewsResponse listNews(ListNewsCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();

		final Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());

		NewsOwnerType newsOwnerType = NewsOwnerType.fromCode(cmd.getOwnerType());

		if (newsOwnerType == NewsOwnerType.ORGANIZATION) {
			return listNews(userId, namespaceId, null, cmd.getCategoryId(), cmd.getPageAnchor(), cmd.getPageSize());
		}else {
			return listNews(userId, namespaceId, cmd.getOwnerId(), cmd.getCategoryId(), cmd.getPageAnchor(), cmd.getPageSize());
		}
	}

	@Override
	public ListNewsResponse listNewsForWeb(ListNewsCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Integer namespaceId = UserContext.getCurrentNamespaceId();
		return listNews(userId, namespaceId, null, cmd.getCategoryId(), cmd.getPageAnchor(), cmd.getPageSize());
	}
	private ListNewsResponse listNews(Long userId, Integer namespaceId, Long communityId, Long categoryId,Long pageAnchor, Integer pageSize) {
		return listNews(userId, namespaceId, communityId, categoryId, pageAnchor, pageSize, false);
	}

	private ListNewsResponse listNews(Long userId, Integer namespaceId, Long communityId, Long categoryId, Long pageAnchor, Integer pageSize,
			boolean isScene) {
		if(null == categoryId)
			categoryId = 0L;
		pageSize = PaginationConfigHelper.getPageSize(configurationProvider, pageSize);
		pageAnchor = pageAnchor == null ? 0 : pageAnchor;
		Long from = pageAnchor * pageSize;

		Boolean commentForbiddenFlag = newsProvider.getCommentForbiddenFlag(categoryId, namespaceId);

		List<BriefNewsDTO> list = newsProvider.listNews(communityId, categoryId, namespaceId, from, pageSize + 1).stream()
				.map(news -> convertNewsToBriefNewsDTO(userId, news, isScene, commentForbiddenFlag)).collect(Collectors.toList());

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

	private BriefNewsDTO convertNewsToBriefNewsDTO(Long userId, News news, boolean isScene, Boolean commentForbiddenFlag) {
		BriefNewsDTO newsDTO = ConvertHelper.convert(news, BriefNewsDTO.class);
		newsDTO.setNewsToken(WebTokenGenerator.getInstance().toWebToken(news.getId()));
		if (!isScene) {
			newsDTO.setLikeFlag(getUserLikeFlag(userId, news.getId()).getCode());
		}
		if(newsDTO.getCoverUri() == null ){
			NewsCategory category = this.newsProvider.findNewsCategoryById(news.getCategoryId());
			newsDTO.setCoverUri(this.contentServerService.parserUri(category.getLogoUri(), EntityType.USER.getCode(), UserContext.current()
				.getUser().getId()));
		}
		newsDTO.setNewsUrl(getNewsUrl(news.getNamespaceId(), newsDTO.getNewsToken()));

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

	@Override
	public SearchNewsResponse searchNews(SearchNewsCommand cmd) {
		if (StringUtils.isEmpty(cmd.getKeyword())) {
			return ConvertHelper.convert(listNews(ConvertHelper.convert(cmd, ListNewsCommand.class)),
					SearchNewsResponse.class);
		}
		final Long userId = UserContext.current().getUser().getId();
		final Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());
		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();

		NewsOwnerType newsOwnerType = NewsOwnerType.fromCode(cmd.getOwnerType());

		if (newsOwnerType == NewsOwnerType.ORGANIZATION) {
			return searchNews(null, userId, namespaceId, cmd.getCategoryId(), cmd.getKeyword(), pageAnchor, pageSize);

		}else {
			return searchNews(cmd.getOwnerId(), userId, namespaceId, cmd.getCategoryId(), cmd.getKeyword(), pageAnchor, pageSize);
		}
	}
 
	/**
	 * 拼接搜索串的部分移出来并增加highlight部分，以便后续处理
	 * xiongying
	 */
	private String getSearchJson(Long communityId, Long userId, Integer namespaceId, Long categoryId, String keyword, Long pageAnchor,
			Integer pageSize) {
		Long from = pageAnchor * pageSize;

		// {\"from\":0,\"size\":15,\"sort\":[],\"query\":{\"filtered\":{\"query\":{},\"filter\":{\"bool\":{\"must\":[],\"should\":[]}}}}}
		JSONObject json = JSONObject.parseObject(
				"{\"from\":0,\"size\":0,\"sort\":[],\"query\":{\"filtered\":{\"query\":{},\"filter\":{\"bool\":{\"must\":[]}}}},\"highlight\":{\"fragment_size\":60,\"number_of_fragments\":8,\"fields\":{\"title\":{},\"content\":{},\"sourceDesc\":{}}}}");
		// 设置from和size
		json.put("from", from);
		json.put("size", pageSize + 1);

		// 设置排序条件
		JSONArray sort = json.getJSONArray("sort");
		sort.add(JSONObject.parseObject("{\"topIndex\":{\"order\":\"desc\"}}"));
		sort.add(JSONObject.parseObject("{\"createTime\":{\"order\":\"desc\"}}"));

		
		// 设置查询关键字
		JSONObject query = json.getJSONObject("query").getJSONObject("filtered").getJSONObject("query");
		query.put("query_string",
				JSONObject.parse("{\"query\":\"" + keyword + "\",\"fields\":[\"title\",\"content\"]}"));

		// 设置条件
		JSONArray must = json.getJSONObject("query").getJSONObject("filtered").getJSONObject("filter")
				.getJSONObject("bool").getJSONArray("must");
		must.add(JSONObject.parse("{\"term\":{\"namespaceId\":" + namespaceId + "}}"));
		must.add(JSONObject.parse("{\"term\":{\"status\":" + NewsStatus.ACTIVE.getCode() + "}}"));
		if (null != communityId) {
			must.add(JSONObject.parse("{\"term\":{\"communityIds\":" + communityId + "}}"));
		}

		//设置过滤条件
		if(null != categoryId){
			must.add(JSONObject.parse("{ \"term\": { \"categoryId\": "+categoryId+"}} "));
		}
 
		
		return json.toJSONString();
	}
	

	private SearchNewsResponse searchNews(Long communityId, Long userId, Integer namespaceId, Long categoryId, String keyword, Long pageAnchor,
			Integer pageSize) {
		

		String jsonString = getSearchJson(communityId, userId, namespaceId,categoryId, keyword, pageAnchor, pageSize);
 
		// 需要查询的字段
		String fields = "id,title,publishTime,author,sourceDesc,coverUri,contentAbstract,likeCount,childCount,topFlag,communityIds,visibleType";

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
			newsDTO.setLikeFlag(getUserLikeFlag(userId, o.getLong("id")).getCode());
			newsDTO.setCategoryId(o.getLong("categoryId"));
			newsDTO.setVisibleType(o.getString("visibleType"));

			newsDTO.setCommentFlag(NewsNormalFlag.ENABLED.getCode());
			if (commentForbiddenFlag) {
				newsDTO.setCommentFlag(NewsNormalFlag.DISABLED.getCode());
			}

			newsDTO.setProjectDTOS(setProjectDTOs(o.getLong("id")));

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
		final List<News> list = new ArrayList<>();
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {
			News news = findNewsById(userId, newsId);
			news.setViewCount(news.getViewCount() + 1L);
			newsProvider.updateNews(news);
			list.add(news);
			return null;
		});
		return convertNewsToNewsDTO(userId, list.get(0));
	}

	private GetNewsDetailInfoResponse convertNewsToNewsDTO(Long userId, News news) {
		GetNewsDetailInfoResponse newsDTO = ConvertHelper.convert(news, GetNewsDetailInfoResponse.class);
		newsDTO.setNewsToken(WebTokenGenerator.getInstance().toWebToken(news.getId()));
		newsDTO.setContent(null);
		newsDTO.setContentUrl(getContentUrl(news.getNamespaceId(), newsDTO.getNewsToken()));
		if(news.getCoverUri() != null)
			newsDTO.setCoverUri(news.getCoverUri());
		else{
			NewsCategory category = this.newsProvider.findNewsCategoryById(news.getCategoryId());
			newsDTO.setCoverUri(this.contentServerService.parserUri(category.getLogoUri(), EntityType.USER.getCode(), UserContext.current()
				.getUser().getId()));
		}
		newsDTO.setNewsUrl(getNewsUrl(news.getNamespaceId(), newsDTO.getNewsToken()));
		newsDTO.setLikeFlag(getUserLikeFlag(userId, news.getId()).getCode());// 未登录用户id为0

		Boolean commentForbiddenFlag = newsProvider.getCommentForbiddenFlag(news.getCategoryId(), news.getNamespaceId());

		newsDTO.setCommentFlag(NewsNormalFlag.ENABLED.getCode());
		if (commentForbiddenFlag) {
			newsDTO.setCommentFlag(NewsNormalFlag.DISABLED.getCode());
		}
		return newsDTO;
	}

	private String getNewsUrl(Integer namespaceId, String newsToken) {
		String homeUrl = configurationProvider.getValue(namespaceId, ConfigConstants.HOME_URL, "");
		String contentUrl = configurationProvider.getValue(namespaceId, ConfigConstants.NEWS_PAGE_URL, "");
		if (homeUrl.length() == 0 || contentUrl.length() == 0) {
			LOGGER.error("Invalid home url or news page url, homeUrl=" + homeUrl + ", contentUrl=" + contentUrl);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_CONTENT_URL_INVALID, "Invalid home url or content url");
		} else {
			return homeUrl + contentUrl  + newsToken;
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
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {

			News news = findNewsById(userId, newsId);
			if (NewsTopFlag.fromCode(news.getTopFlag()) == NewsTopFlag.NONE) {
				news.setTopFlag(NewsTopFlag.TOP.getCode());
				news.setTopIndex(newsProvider.getMaxTopIndex(namespaceId).longValue() + 1L);
			} else {
				news.setTopFlag(NewsTopFlag.NONE.getCode());
				news.setTopIndex(0L);
			}
			newsProvider.updateNews(news);
			return null;
		});

		syncNews(newsId);
	}

	private News findNewsById(Long userId, Long newsId) {
		News news = newsProvider.findNewsById(newsId);
		if (news == null) {
			LOGGER.error("News not found, operatorId=" + userId + ", newsId=" + newsId);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID, "News not found");
		}
		return news;
	}

	private Long checkNewsToken(Long userId, String newsToken) {
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
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_NEWS.getCode()).enter(() -> {
			News news = findNewsById(userId, newsId);
			news.setDeleterUid(userId);
			news.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			news.setStatus(NewsStatus.INACTIVE.getCode());
			newsProvider.updateNews(news);
			return null;
		});

		syncNewsWhenDelete(newsId);
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

		//黑名单权限校验 by sfyan20161213
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
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
		return processComment(userId,newsId,newCmd);
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
	public ListNewsBySceneResponse listNewsByScene(ListNewsBySceneCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		SceneTokenDTO sceneTokenDTO = getNamespaceFromSceneToken(userId, cmd.getSceneToken());
		Integer namespaceId = sceneTokenDTO.getNamespaceId();

		SceneType sceneType = SceneType.fromCode(sceneTokenDTO.getScene());

		Long communityId = null;

		switch(sceneType) {
			case DEFAULT:
			case PARK_TOURIST:
				communityId = sceneTokenDTO.getEntityId();

				break;
			case FAMILY:
				FamilyDTO family = familyProvider.getFamilyById(sceneTokenDTO.getEntityId());
				Community community = null;
				if(family != null) {
					community = communityProvider.findCommunityById(family.getCommunityId());
				} else {
					if(LOGGER.isWarnEnabled()) {
						LOGGER.warn("Family not found, sceneToken=" + sceneTokenDTO);
					}
				}
				if(community != null) {
					communityId = community.getId();
				}

				break;
			case PM_ADMIN:// 无小区ID
			case ENTERPRISE: // 增加两场景，与园区企业保持一致
			case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致
				OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.
						getOrganizationCommunityRequestByOrganizationId(sceneTokenDTO.getEntityId());
				if(null != organizationCommunityRequest){
					communityId = organizationCommunityRequest.getCommunityId();
				}
				break;
			default:
				LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDTO);
				break;
		}

		return ConvertHelper.convert(listNews(userId, namespaceId, communityId, cmd.getCategoryId(), cmd.getPageAnchor(), cmd.getPageSize(), true),
				ListNewsBySceneResponse.class);
	}

	private SceneTokenDTO getNamespaceFromSceneToken(Long userId, String sceneToken) {
		if (StringUtils.isEmpty(sceneToken)) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", sceneToken=" + sceneToken);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
		SceneTokenDTO sceneTokenDTO = WebTokenGenerator.getInstance().fromWebToken(sceneToken, SceneTokenDTO.class);
		if (sceneTokenDTO == null || sceneTokenDTO.getNamespaceId() == null) {
			LOGGER.error("scene token invalid, operatorId=" + userId + ", sceneToken=" + sceneToken);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_SCENETOKEN_INVALID, "scene token invalid");
		}

		//检查游客是否能继续访问此场景 by sfyan 20161009
		userService.checkUserScene(SceneType.fromCode(sceneTokenDTO.getScene()));
		return sceneTokenDTO;
	}

	@Override
	public void setNewsLikeFlagByScene(SetNewsLikeFlagBySceneCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		getNamespaceFromSceneToken(userId, cmd.getSceneToken());
		setNewsLikeFlag(userId, cmd.getNewsToken());
	}

	@Override
	public AddNewsCommentBySceneResponse addNewsCommentByScene(AddNewsCommentBySceneCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		// 检查参数
		checkCommentParameter(userId, cmd);

		//黑名单权限校验 by sfyan20160213
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


		//填充创建者信息   add by yanjun 20170606
		populatePostUserInfo(userId, commentDTO);
		return commentDTO;
	}

	/**
	 * 填充创建者信息
	 * @param userId
	 * @param commentDTO
	 */
	private void populatePostUserInfo(Long userId, AddNewsCommentBySceneResponse commentDTO){
		if(userId == null || commentDTO == null){
			return;
		}
		User creator = userProvider.findUserById(userId);
		if(creator != null) {
			commentDTO.setCreatorNickName(creator.getNickName());

			String creatorAvatar = creator.getAvatar();
			commentDTO.setCreatorAvatar(creatorAvatar);

			if(StringUtils.isEmpty(creatorAvatar)) {
				creatorAvatar = configProvider.getValue(creator.getNamespaceId(), "user.avatar.default.url", "");
			}

			if(creatorAvatar != null && creatorAvatar.length() > 0){
				String avatarUrl = getResourceUrlByUir(userId, creatorAvatar,
						EntityType.USER.getCode(), userId);
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
		//新的统一评论接口没有scenetoken，不需要常见检查  add by yanjun 20170602
		// 检查namespace是否存在
		//getNamespaceFromSceneToken(userId, cmd.getSceneToken());

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
		//新的统一评论接口没有scenetoken，不需要常见检查  add by yanjun 20170602
		//getNamespaceFromSceneToken(userId, cmd.getSceneToken());
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

				news.setChildCount(news.getChildCount() - 1L);
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
		if (comment.getCreatorUid().longValue() != userId.longValue()) {
			LOGGER.error("userId and commentId not match, operatorId=" + userId + ", userId=" + userId + ", commentId"
					+ commentId);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_NEWSID_COMMENTID_NOT_MATCH, "userId and commentId not match");
		}
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

						//正则表达式去掉content中的富文本内容 modified by xiongying20160908
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

        Pattern p_script = Pattern.compile(regEx_script,
                Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");

        Pattern p_style = Pattern
                .compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");

        Pattern p_space = Pattern
                .compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(" ");

        return htmlStr;

    }

	private void syncNews(Long id) {
		News news = newsProvider.findNewsById(id);
		if (news != null) {
			news.setCommunityIds(newsProvider.listNewsCommunities(id));
			//正则表达式去掉content中的富文本内容 modified by xiongying20160908
			String content = news.getContent();
			content = removeTag(content);
			news.setContent(content);
			searchProvider.insertOrUpdate(SearchUtils.NEWS, news.getId().toString(), JSONObject.toJSONString(news));
		}
	}

	private void syncNewsWhenDelete(Long id) {
		searchProvider.deleteById(SearchUtils.NEWS, id.toString());
	} 
	
	@Override
	public SearchContentsBySceneReponse searchNewsByScene(
			SearchContentsBySceneCommand cmd) {
		SearchContentsBySceneReponse response = new SearchContentsBySceneReponse();
		SceneTokenDTO sceneTokenDto = WebTokenGenerator.getInstance().fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
		final Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = sceneTokenDto.getNamespaceId();;
		SearchTypes searchType = userActivityProvider.findByContentAndNamespaceId(namespaceId, SearchContentType.NEWS.getCode());
		if (StringUtils.isEmpty(cmd.getKeyword())) {
			ListNewsBySceneCommand command = new ListNewsBySceneCommand();
			
			command.setSceneToken(cmd.getSceneToken());
			command.setPageAnchor(cmd.getPageAnchor());
			command.setPageSize(cmd.getPageSize());
			ListNewsBySceneResponse news = listNewsByScene(command);
			if(news != null) {
				response.setNextPageAnchor(news.getNextPageAnchor());
				if(news.getNewsList() != null && news.getNewsList().size() > 0) {
					List<ContentBriefDTO> dtos  = new ArrayList<ContentBriefDTO>();
					for (BriefNewsDTO briefNews : news.getNewsList()) {
						ContentBriefDTO dto = new ContentBriefDTO();
						dto.setNewsToken(briefNews.getNewsToken());
						dto.setContent(briefNews.getContentAbstract());
						dto.setSubject(briefNews.getTitle());
						dto.setPostUrl(briefNews.getCoverUri());
						if(searchType != null) {
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

		String jsonString = getSearchJson(null, userId, namespaceId,null, cmd.getKeyword(), pageAnchor, pageSize);
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

		//处理返回的topHit
		List<ContentBriefDTO> dtos  = new ArrayList<ContentBriefDTO>();
		for (int i = 0; i < result.size(); i++) {
			ContentBriefDTO dto = new ContentBriefDTO();
			JSONObject highlight = result.getJSONObject(i).getJSONObject("highlight");
			JSONObject source = result.getJSONObject(i).getJSONObject("_source");

			if(StringUtils.isEmpty(highlight.getString("title"))){
				dto.setSubject(source.getString("title"));
			} else {
				dto.setSubject(highlight.getString("title"));
			}
			
			if(StringUtils.isEmpty(highlight.getString("content"))){
				dto.setContent(source.getString("content"));
			} else {
				dto.setContent(highlight.getString("content"));
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
			
			if(StringUtils.isEmpty(highlight.getString("sourceDesc"))){
				footNote.setSourceDesc(source.getString("sourceDesc"));
			} else {
				footNote.setSourceDesc(highlight.getString("sourceDesc"));
			}
			
			dto.setFootnoteJson(StringHelper.toJsonString(footNote));
			
			dtos.add(dto);
		}

		response.setDtos(dtos);
		response.setNextPageAnchor(nextPageAnchor);
		return response;
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
 
}
