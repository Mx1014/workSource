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
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.forum.PostStatus;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.NewsContentType;
import com.everhomes.rest.news.NewsOwnerType;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.rest.news.NewsTopFlag;
import com.everhomes.rest.organization.PrivateFlag;
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
	private DbProvider dbProvider;

	@Override
	public BriefNewsDTO createNews(CreateNewsCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();

		// 检查参数等信息
		checkNewsParameter(userId, cmd);
		Organization organization = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());

		News news = processNewsCommand(userId, organization, cmd);
		// 创建一条新闻记录，会返回新闻Id，稍后更新到帖子表中
		newsProvider.createNews(news);

		return ConvertHelper.convert(news, BriefNewsDTO.class);
	}

	private News processNewsCommand(Long userId, Organization organization, CreateNewsCommand cmd) {
		News news = ConvertHelper.convert(cmd, News.class);
		news.setNamespaceId(organization.getNamespaceId());
		news.setOwnerType(cmd.getOwnerType().toLowerCase());
		news.setContentType(NewsContentType.RICH_TEXT.getCode());
		news.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		news.setPublishTime(news.getCreateTime());
		news.setTopIndex(0L);
		news.setTopFlag(NewsTopFlag.NONE.getCode());
		news.setStatus(NewsStatus.ACTIVE.getCode());
		news.setCreatorUid(userId);
		news.setDeleterUid(0L);
		return news;
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
