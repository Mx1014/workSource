package com.everhomes.news;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.category.CategoryProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.NewsContentType;
import com.everhomes.rest.news.NewsOwnerType;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class NewsServiceImpl implements NewsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private NewsProvider newsProvider;

	@Autowired
	private CategoryProvider categoryProvider;

	@Autowired
	private ForumProvider forumProvider;

	@Autowired
	private DbProvider dbProvider;

	@Override
	public BriefNewsDTO createNews(CreateNewsCommand cmd) {
		long startTime = System.currentTimeMillis();

		Long userId = UserContext.current().getUser().getId();

		// 检查参数等信息
		checkNewsParameter(userId, cmd);
		Organization organization = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());

		News news = processNewsCommand(userId, organization, cmd);
		Post post = processNewsPost(userId, organization, news);

		dbProvider.execute(s -> {
			// 创建一条新闻记录，会返回新闻Id，稍后更新到帖子表中
			newsProvider.createNews(news);

			// 创建一条帖子记录，会返回帖子id，稍后更新到新闻表中
			post.setEmbeddedId(news.getId());
			forumProvider.createPost(post);

			// 更新新闻表中的帖子id
			news.setPostId(post.getId());
			newsProvider.updateNews(news);

			return true;
		});

		BriefNewsDTO newsDTO = ConvertHelper.convert(news, BriefNewsDTO.class);

		long endTime = System.currentTimeMillis();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Create a new news, userId=" + userId + ", newsId=" + news.getId() + ", elapse="
					+ (endTime - startTime));
		}
		return newsDTO;
	}

	private News processNewsCommand(Long userId, Organization organization, CreateNewsCommand cmd) {
		News news = ConvertHelper.convert(cmd, News.class);
		news.setNamespaceId(organization.getNamespaceId());
		news.setOwnerType(cmd.getOwnerType().toLowerCase());
		news.setContentType(NewsContentType.RICH_TEXT.getCode());
		news.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		news.setPublishTime(news.getCreateTime());
		news.setTopIndex(0L);
		news.setTopFlag((byte) 0);
		news.setStatus((byte) 2);
		news.setCreatorUid(userId);
		news.setDeleterUid(0L);
		return news;
	}

	private Post processNewsPost(Long userId, Organization organization, News news) {
		Post post = new Post();
		post.setAppId(2L);
		post.setForumId(0L); // 待定
		post.setParentPostId(0L);
		post.setCreatorUid(userId);
		post.setCreatorTag(PostEntityTag.PM.getCode());
		post.setTargetTag(PostEntityTag.USER.getCode());
		post.setVisibleRegionType(VisibleRegionType.REGION.getCode());
		post.setVisibleRegionId(organization.getId());
		Long categoryId = 1110L;
		post.setCategoryId(categoryId);
		post.setCategoryPath(categoryProvider.findCategoryById(categoryId).getPath());
		post.setChildCount(0L);
		post.setForwardCount(0L);
		post.setLikeCount(0L);
		post.setViewCount(0L);
		post.setSubject(news.getTitle());
		post.setContentType(PostContentType.TEXT.getCode());
		post.setContent(news.getContentAbstract());
		post.setContentAbstract(news.getContentAbstract());
		post.setEmbeddedAppId(AppConstants.APPID_NEWS);
		post.setEmbeddedId(news.getId());
		News newsWithoutContent = ConvertHelper.convert(news, News.class);
		newsWithoutContent.setContent(news.getContentAbstract());
		post.setEmbeddedJson(newsWithoutContent.toString());
		post.setEmbeddedVersion(1);
		post.setPrivateFlag((byte) 0);
		post.setAssignedFlag((byte) 0);
		post.setFloorNumber(0L);
		post.setStatus((byte) 2);
		post.setCreateTime(news.getCreateTime());
		post.setUpdateTime(news.getCreateTime());
		post.setDeleterUid(0L);
		return post;
	}

	private void checkNewsParameter(Long userId, CreateNewsCommand cmd) {
		if (cmd.getOwnerId() == null || StringUtils.isEmpty(cmd.getOwnerType()) || StringUtils.isEmpty(cmd.getTitle())
				|| StringUtils.isEmpty(cmd.getContent())) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
	}

	private Organization checkOwner(Long userId, Long ownerId, String ownerType) {
		NewsOwnerType newsOwnerType = NewsOwnerType.fromCode(ownerType);
		if (newsOwnerType == null) {
			LOGGER.error("Invalid owner type, operatorId=" + userId + ", ownerType=" + ownerType);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_OWNER_TYPE_INVALID, "Invalid owner type");
		}

		// 目前只有一种ownerType即organization，所以不做判断了，如果后面改成了多个，可以把返回值改成Object，然后在调用者中逐个判断
		Organization organization = organizationProvider.findOrganizationById(ownerId);
		if (organization == null) {
			LOGGER.error(
					"Invalid owner id, operatorId=" + userId + ", ownerType=" + ownerType + ", ownerId=" + ownerId);
			throw RuntimeErrorException.errorWith(NewsServiceErrorCode.SCOPE,
					NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID, "Invalid owner id");
		}

		return organization;
	}

}
