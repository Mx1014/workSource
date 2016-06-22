// @formatter:off
package com.everhomes.news;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.news.AddNewsCommentBySceneCommand;
import com.everhomes.rest.news.AddNewsCommentCommand;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.rest.news.CommentStatus;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.DeleteNewsCommand;
import com.everhomes.rest.news.DeleteNewsCommentBySceneCommand;
import com.everhomes.rest.news.DeleteNewsCommentCommand;
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.ListNewsBySceneCommand;
import com.everhomes.rest.news.ListNewsCommand;
import com.everhomes.rest.news.ListNewsCommentCommand;
import com.everhomes.rest.news.ListNewsCommentResponse;
import com.everhomes.rest.news.NewsCommentDTO;
import com.everhomes.rest.news.NewsContentDTO;
import com.everhomes.rest.news.NewsContentType;
import com.everhomes.rest.news.NewsDTO;
import com.everhomes.rest.news.NewsDetailInfoCommand;
import com.everhomes.rest.news.NewsListResponse;
import com.everhomes.rest.news.NewsOwnerType;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.rest.news.NewsTopFlag;
import com.everhomes.rest.news.SearchNewsCommand;
import com.everhomes.rest.news.SetNewsLikeFlagBySceneCommand;
import com.everhomes.rest.news.SetNewsLikeFlagCommand;
import com.everhomes.rest.news.SetNewsTopFlagCommand;
import com.everhomes.server.schema.tables.pojos.EhNewsComment;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;

@Component
public class NewsServiceImpl implements NewsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private NewsProvider newsProvider;

	@Autowired
	private CommentProvider commentProvider;

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

	@Override
	public void importNews(ImportNewsCommand cmd, MultipartFile[] files) {

	}

	@Override
	public NewsListResponse listNews(ListNewsCommand cmd) {

		return null;
	}

	@Override
	public NewsListResponse searchNews(SearchNewsCommand cmd) {

		return null;
	}

	@Override
	public NewsDTO getNewsDetailInfo(NewsDetailInfoCommand cmd) {

		return null;
	}

	@Override
	public void setNewsTopFlag(SetNewsTopFlagCommand cmd) {

	}

	@Override
	public NewsContentDTO getNewsContent(NewsDetailInfoCommand cmd) {

		return null;
	}

	@Override
	public void deleteNews(DeleteNewsCommand cmd) {

	}

	@Override
	public void setNewsLikeFlag(SetNewsLikeFlagCommand cmd) {

	}

	@Override
	public NewsCommentDTO addNewsComment(AddNewsCommentCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Long newsId = WebTokenGenerator.getInstance().fromWebToken(cmd.getNewsToken(), News.class).getId();
		
		//检查参数
		
		
		Comment comment = processComment(userId, newsId, cmd);
		commentProvider.createComment(EhNewsComment.class, comment);
		
		
		return null;
	}

	private Comment processComment(Long userId, Long newsId, AddNewsCommentCommand cmd) {
		Comment comment = new Comment();
		comment.setOwnerId(newsId);
		comment.setContentType(cmd.getContentType().toLowerCase());
		comment.setContent(cmd.getContent());
		comment.setStatus(CommentStatus.ACTIVE.getCode());
		comment.setCreatorUid(userId);
		comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		return comment;
	}

	@Override
	public ListNewsCommentResponse listNewsComment(ListNewsCommentCommand cmd) {

		return null;
	}

	@Override
	public void deleteNewsComment(DeleteNewsCommentCommand cmd) {

	}

	@Override
	public NewsListResponse listNewsByScene(ListNewsBySceneCommand cmd) {

		return null;
	}

	@Override
	public void setNewsLikeFlagByScene(SetNewsLikeFlagBySceneCommand cmd) {

	}

	@Override
	public NewsCommentDTO addNewsCommentByScene(AddNewsCommentBySceneCommand cmd) {

		return null;
	}

	@Override
	public void deleteNewsCommentByScene(DeleteNewsCommentBySceneCommand cmd) {

	}

}
