package com.everhomes.news.open;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.everhomes.rest.community.ListCommunitiesByKeywordResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.App;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.namespace.NamespaceResource;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.news.News;
import com.everhomes.news.NewsProvider;
import com.everhomes.news.NewsService;
import com.everhomes.news.NewsTag;
import com.everhomes.news.NewsTagVals;
import com.everhomes.news.open.NewsOpenService;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.rest.community.admin.ListComunitiesByKeywordAdminCommand;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.news.GetNewsDetailInfoCommand;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.news.open.CreateOpenNewsCommand;
import com.everhomes.rest.news.open.CreateNewsResponse;
import com.everhomes.rest.news.open.DeleteNewsCommand;
import com.everhomes.rest.news.open.GetOpenNewsDetailCommand;
import com.everhomes.rest.news.open.GetOpenNewsDetailResponse;
import com.everhomes.rest.news.open.ListOpenNewsCommand;
import com.everhomes.rest.news.open.ListOpenNewsResponse;
import com.everhomes.rest.news.open.ListNewsTagsCommand;
import com.everhomes.rest.news.open.TagDTO;
import com.everhomes.rest.news.open.UpdateOpenNewsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.NewsInstanceConfig;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;

@Component
public class NewsOpenServiceImpl implements NewsOpenService {

	private final long NEWS_MODULE_ID = 10800L;

	@Autowired
	NewsService newsService;

	@Autowired
	NewsProvider newsProvider;

	@Autowired
	PortalService portalService;

	@Autowired
	CommunityService communityService;

	@Autowired
	AppNamespaceMappingProvider appNspaceProvider;

	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	ServiceModuleService serviceModuleService;

	@Override
	public CreateNewsResponse createNews(CreateOpenNewsCommand cmd) {

		// 获取namespaceId
		Integer namespaceId = getNamespaceId();

		// 获取categoryId
		cmd.setCategoryId(getCategoryId(namespaceId, cmd.getCategoryId()));
		
		// 校验ownerId
		checkOwnerId(namespaceId, cmd.getOwnerId());

		// 获取创建数据
		News news = newsService.createNewsByOpenApi(namespaceId, cmd);

		// 组装返回数据
		CreateNewsResponse resp = new CreateNewsResponse();
		resp.setNewsToken(createIdToken(news.getId()));
		return resp;
	}

	@Override
	public void updateNews(UpdateOpenNewsCommand cmd) {

		// 获取原快讯
		News originNews = getNewsByToken(cmd.getNewsToken());
		if (null == originNews) {
			throwErrorCode(NewsServiceErrorCode.ERROR_NEWS_NOT_FOUND, "news not found");
		}

		// 更新
		newsService.updateNewsByOpenApi(originNews, cmd);
	}

	@Override
	public void deleteNews(DeleteNewsCommand cmd) {

		// 获取原快讯
		News news = getNewsByToken(cmd.getNewsToken());
		if (null == news) {
			throwErrorCode(NewsServiceErrorCode.ERROR_NEWS_NOT_FOUND, "news not found");
		}

		newsService.deleteNews(null, news);
	}

	@Override
	public ListOpenNewsResponse listNews(ListOpenNewsCommand cmd) {

		Integer namespaceId = getNamespaceId();
		cmd.setCategoryId(getCategoryId(namespaceId, cmd.getCategoryId()));
		return newsService.listNewsOpenApi(cmd, namespaceId);
	}

	@Override
	public GetOpenNewsDetailResponse getNewsDetail(GetOpenNewsDetailCommand cmd) {
		News news = getNewsByToken(cmd.getNewsToken());
		if (null == news) {
			throwErrorCode(NewsServiceErrorCode.ERROR_NEWS_NOT_FOUND, "news not found");
		}

		// 获取可见范围
		List<Long> projectIds = getNewsVisibaleProjects(news.getId());

		// 获取当前标签
		List<Long> newsTagIds = getNewsTagIds(news.getId());

		GetOpenNewsDetailResponse resp = ConvertHelper.convert(news, GetOpenNewsDetailResponse.class);
		resp.setCoverUrl(news.getCoverUri());
		resp.setPublishTime(news.getPublishTime().getTime());
		resp.setProjectIds(projectIds);
		resp.setNewsTagIds(newsTagIds);
		return resp;
	}

	@Override
	public List<IdNameDTO> listProjects() {

		ListComunitiesByKeywordAdminCommand cmd = new ListComunitiesByKeywordAdminCommand();
		List<CommunityDTO> communities = null;

		cmd.setNamespaceId(getNamespaceId());
		ListCommunitiesByKeywordResponse resp = communityService.listCommunitiesByKeyword(cmd);
		if (null == resp || CollectionUtils.isEmpty(communities = resp.getRequests())) {
			return null;
		}

		return communities.stream().map(r -> {
			IdNameDTO dto = new IdNameDTO();
			dto.setId(r.getId());
			dto.setName(r.getName());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<TagDTO> listNewsTags(ListNewsTagsCommand cmd) {
		Integer namespaceId = getNamespaceId();
		Long categoryId = getCategoryId(namespaceId, cmd.getCategoryId());
		List<NewsTag> allTags = newsProvider.listNewsTag(getNamespaceId(), null, null, null, null, null, null, categoryId);
		if (CollectionUtils.isEmpty(allTags)) {
			return null;
		}

		// 对父节点进行索引
		Map<Long, TagDTO> dtos = new LinkedHashMap<>(10);
		for (NewsTag tag : allTags) {
			if (0 != tag.getParentId()) {
				continue;
			}

			TagDTO dto = new TagDTO();
			dto.setId(tag.getId());
			dto.setName(tag.getValue());
			dto.setChilds(new ArrayList<IdNameDTO>(10));
			dtos.put(tag.getId(), dto);
		}

		// 如果是空直接返回
		if (dtos.isEmpty()) {
			return null;
		}

		// 进行配对
		for (NewsTag tag : allTags) {

			TagDTO dto = null;
			if (0 == tag.getParentId() || null == (dto = dtos.get(tag.getParentId()))) {
				continue;
			}

			IdNameDTO child = new IdNameDTO();
			child.setId(tag.getId());
			child.setName(tag.getValue());
			dto.getChilds().add(child);
		}

		return new ArrayList<TagDTO>(dtos.values());
	}

	private Integer getNamespaceId() {

		// 获取appKey
		App app = UserContext.current().getCallerApp();
		if (null == app) {
			throwErrorCode(NewsServiceErrorCode.ERROR_NOT_LEGAL_REQUEST, "not legal request");
		}

		// 获取namesapceid
		AppNamespaceMapping map = appNspaceProvider.findAppNamespaceMappingByAppKey(app.getAppKey());
		if (null == map) {
			throwErrorCode(NewsServiceErrorCode.ERROR_APPKEY_NAMESPACE_MAP_NOT_FOUND, "namespace not found");
		}

		return map.getNamespaceId();
	}

	private void throwErrorCode(int errorCode, String errorMsg) {
		throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE, errorCode, errorMsg);
	}

	private String createIdToken(Long newsId) {
		return WebTokenGenerator.getInstance().toWebToken(newsId);
	}

	private Long getIdByToken(String newsToken) {
		return WebTokenGenerator.getInstance().fromWebToken(newsToken, Long.class);
	}

	@Override
	public List<IdNameDTO> listApplications() {
		return listApplications(getNamespaceId());
	}

	private List<IdNameDTO> listApplications(Integer namespaceId) {
		List<ServiceModuleAppDTO> apps = getModuleApps(namespaceId);
		if (null == apps) {
			return null;
		}

		return apps.stream().map(r -> {
			NewsInstanceConfig config = (NewsInstanceConfig) StringHelper.fromJsonString(r.getInstanceConfig(),
					NewsInstanceConfig.class);
			IdNameDTO dto = new IdNameDTO();
			dto.setId(config.getCategoryId());
			dto.setName(r.getName());
			return dto;

		}).collect(Collectors.toList());
	}

	private List<ServiceModuleAppDTO> getModuleApps(Integer namespaceId) {
		ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setModuleId(NEWS_MODULE_ID);
		ListServiceModuleAppsResponse resp = portalService.listServiceModuleApps(cmd);
		if (null == resp || CollectionUtils.isEmpty(resp.getServiceModuleApps())) {
			return null;
		}

		return resp.getServiceModuleApps();
	}

	private Long getCategoryId(Integer namespaceId, Long categoryId) {

		// 获取当前的所有应用
		List<IdNameDTO> modules = listApplications(namespaceId);
		if (null == modules) {
			throwErrorCode(NewsServiceErrorCode.ERROR_NEWS_MODULE_NOT_FOUND, "news application not found");
		}
		
		// 如果未传 直接返回第一个
		if (null == categoryId) {
			return modules.get(0).getId();
		}
		
		// 校验是否正确
		for (IdNameDTO module : modules) {
			if (categoryId.equals(module.getId())) {
				return categoryId;
			}
		}

		throwErrorCode(NewsServiceErrorCode.ERROR_NEWS_MODULE_NOT_FOUND, "news application not found");
		return null;
		
	}

	private News getNewsByToken(String token) {

		// 解析newsId
		Long newsId = getIdByToken(token);

		// 获取原快讯
		return newsProvider.findNewsByNamespaceAndId(getNamespaceId(), newsId);
	}

	private List<Long> getNewsTagIds(Long newsId) {
		List<NewsTagVals> vals = newsProvider.listNewsTagVals(newsId);
		if (CollectionUtils.isEmpty(vals)) {
			return null;
		}

		return vals.stream().map(r -> {
			return r.getNewsTagId();
		}).collect(Collectors.toList());
	}

	private List<Long> getNewsVisibaleProjects(Long newsId) {
		return newsProvider.listNewsCommunities(newsId);
	}

	private void checkOwnerId(Integer namespaceId, Long ownerId) {
		Community community = communityProvider.findCommunityById(ownerId);
		if (null == community || !namespaceId.equals(community.getNamespaceId())) {
			throwErrorCode(NewsServiceErrorCode.ERROR_NEWS_OWNER_NOT_FOUND, "owner Id not valid");
		}
	}


}
