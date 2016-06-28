// @formatter:off
package com.everhomes.news;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.news.AddNewsCommentBySceneCommand;
import com.everhomes.rest.news.AddNewsCommentBySceneResponse;
import com.everhomes.rest.news.AddNewsCommentCommand;
import com.everhomes.rest.news.AddNewsCommentResponse;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.rest.news.CommentStatus;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.CreateNewsResponse;
import com.everhomes.rest.news.DeleteNewsCommand;
import com.everhomes.rest.news.DeleteNewsCommentBySceneCommand;
import com.everhomes.rest.news.DeleteNewsCommentCommand;
import com.everhomes.rest.news.GetNewsContentCommand;
import com.everhomes.rest.news.GetNewsContentResponse;
import com.everhomes.rest.news.GetNewsDetailInfoCommand;
import com.everhomes.rest.news.GetNewsDetailInfoResponse;
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.ListNewsBySceneCommand;
import com.everhomes.rest.news.ListNewsBySceneResponse;
import com.everhomes.rest.news.ListNewsCommand;
import com.everhomes.rest.news.ListNewsCommentCommand;
import com.everhomes.rest.news.ListNewsCommentResponse;
import com.everhomes.rest.news.ListNewsResponse;
import com.everhomes.rest.news.NewsAttachmentDTO;
import com.everhomes.rest.news.NewsCommentContentType;
import com.everhomes.rest.news.NewsCommentDTO;
import com.everhomes.rest.news.NewsContentType;
import com.everhomes.rest.news.NewsLikeFlag;
import com.everhomes.rest.news.NewsOwnerType;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.rest.news.NewsTopFlag;
import com.everhomes.rest.news.SearchNewsCommand;
import com.everhomes.rest.news.SearchNewsResponse;
import com.everhomes.rest.news.SetNewsLikeFlagBySceneCommand;
import com.everhomes.rest.news.SetNewsLikeFlagCommand;
import com.everhomes.rest.news.SetNewsTopFlagCommand;
import com.everhomes.rest.news.SyncNewsCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.search.SearchProvider;
import com.everhomes.search.SearchUtils;
import com.everhomes.server.schema.tables.pojos.EhNewsAttachments;
import com.everhomes.server.schema.tables.pojos.EhNewsComment;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLike;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

@Component
public class NewsServiceImpl implements NewsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);

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
	private ContentServerService contentServerService;

	@Autowired
	private SearchProvider searchProvider;

	@Override
	public CreateNewsResponse createNews(CreateNewsCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();

		// 检查参数等信息
		checkNewsParameter(userId, cmd);
		Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType()).getNamespaceId();

		News news = processNewsCommand(userId, namespaceId, cmd);
		newsProvider.createNews(news);
		
		syncNews(news.getId());
		
		CreateNewsResponse response = ConvertHelper.convert(news, CreateNewsResponse.class);
		response.setNewsToken(WebTokenGenerator.getInstance().toWebToken(news));
		return response;
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
			news.setContentAbstract(news.getContent().substring(0, news.getContent().length()>100?100:news.getContent().length()));
		}
		return news;
	}

	private void checkNewsParameter(Long userId, CreateNewsCommand cmd) {
		if (StringUtils.isEmpty(cmd.getTitle()) || StringUtils.isEmpty(cmd.getContent())) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
	}

	private Organization checkOwner(Long userId, Long ownerId, String ownerType) {
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
		Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType()).getNamespaceId();
		// 读取Excel数据
		List<News> newsList = getNewsFromExcel(userId, namespaceId, cmd, files);
		newsProvider.createNewsList(newsList);
		
		newsList.forEach(n->syncNews(n.getId()));
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
			resultList.forEach(r -> {
				RowResult result = (RowResult) r;
				String title = RowResult.trimString(result.getA());
				String contentAbstract = RowResult.trimString(result.getB());
				String coverUri = RowResult.trimString(result.getC());
				String content = RowResult.trimString(result.getD());
				String author = RowResult.trimString(result.getE());
				String sourceDesc = RowResult.trimString(result.getF());
				String sourceUrl = RowResult.trimString(result.getG());

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
					command.setCoverUri(coverUri);
					command.setAuthor(author);
					command.setSourceDesc(sourceDesc);
					command.setSourceUrl(sourceUrl);
					checkNewsParameter(userId, command);
					newsList.add(processNewsCommand(userId, namespaceId, command));
				}
			});
			return newsList;
		}
		LOGGER.error("excel data format is not correct.rowCount=" + resultList.size());
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"excel data format is not correct");
	}

	@Override
	public ListNewsResponse listNews(ListNewsCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType()).getNamespaceId();
		return listNews(userId, namespaceId, cmd.getPageAnchor(), cmd.getPageSize());
	}

	private ListNewsResponse listNews(Long userId, Integer namespaceId, Long pageAnchor, Integer pageSize) {
		return listNews(userId, namespaceId, pageAnchor, pageSize, false);
	}

	private ListNewsResponse listNews(Long userId, Integer namespaceId, Long pageAnchor, Integer pageSize,
			boolean isScene) {
		pageSize = PaginationConfigHelper.getPageSize(configurationProvider, pageSize);
		pageAnchor = pageAnchor == null ? 0 : pageAnchor;
		Long from = pageAnchor * pageSize;
		List<BriefNewsDTO> list = newsProvider.listNews(namespaceId, from, pageSize + 1).stream()
				.map(news -> convertNewsToBriefNewsDTO(userId, news, isScene)).collect(Collectors.toList());

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

	private BriefNewsDTO convertNewsToBriefNewsDTO(Long userId, News news, boolean isScene) {
		BriefNewsDTO newsDTO = ConvertHelper.convert(news, BriefNewsDTO.class);
		newsDTO.setNewsToken(WebTokenGenerator.getInstance().toWebToken(news));
		if (!isScene) {
			newsDTO.setLikeFlag(getUserLikeFlag(userId, news.getId()).getCode());
		}
		return newsDTO;
	}

	private NewsLikeFlag getUserLikeFlag(Long userId, Long newsId) {
		if (userId.longValue() == 0) {
			return NewsLikeFlag.NONE;
		}
		UserLike userLike = findUserLike(userId, newsId);
		if (userLike == null) {
			return NewsLikeFlag.NONE;
		}
		if (userLike.getLikeType().byteValue() == NewsLikeFlag.LIKE.getCode()) {
			return NewsLikeFlag.LIKE;
		}
		return NewsLikeFlag.NONE;
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
		final Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType()).getNamespaceId();
		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();

		return searchNews(userId, namespaceId, cmd.getKeyword(), pageAnchor, pageSize);
	}

	private SearchNewsResponse searchNews(Long userId, Integer namespaceId, String keyword, Long pageAnchor,
			Integer pageSize) {
		Long from = pageAnchor * pageSize;

		// {\"from\":0,\"size\":15,\"sort\":[],\"query\":{\"filtered\":{\"query\":{},\"filter\":{\"bool\":{\"must\":[],\"should\":[]}}}}}
		JSONObject json = JSONObject.parseObject(
				"{\"from\":0,\"size\":0,\"sort\":[],\"query\":{\"filtered\":{\"query\":{},\"filter\":{\"bool\":{\"must\":[]}}}}}");
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

		// 需要查询的字段
		String fields = "id,title,publishTime,author,sourceDesc,coverUri,contentAbstract,likeCount,childCount,topFlag";

		// 从es查询
		JSONArray result = searchProvider.query(SearchUtils.NEWS, json.toJSONString(), fields);

		// 处理分页
		Long nextPageAnchor = null;
		if (result.size() > pageSize) {
			result.remove(result.size() - 1);
			nextPageAnchor = pageAnchor + 1;
		}

		// 转换结果到返回值
		List<BriefNewsDTO> list = result.stream().map(r -> {
			JSONObject o = (JSONObject) r;
			BriefNewsDTO newsDTO = new BriefNewsDTO();
			newsDTO.setNewsToken(WebTokenGenerator.getInstance().toWebToken(new News(o.getLong("id"))));
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
			return newsDTO;
		}).collect(Collectors.toList());

		SearchNewsResponse response = new SearchNewsResponse();
		response.setNextPageAnchor(nextPageAnchor);
		response.setNewsList(list);

		return response;
	}

	@Override
	public GetNewsDetailInfoResponse getNewsDetailInfo(GetNewsDetailInfoCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		News news = findNewsById(userId, newsId);
		news.setViewCount(news.getViewCount() + 1L);
		newsProvider.updateNews(news);
		return convertNewsToNewsDTO(userId, news);
	}

	private GetNewsDetailInfoResponse convertNewsToNewsDTO(Long userId, News news) {
		GetNewsDetailInfoResponse newsDTO = ConvertHelper.convert(news, GetNewsDetailInfoResponse.class);
		newsDTO.setNewsToken(WebTokenGenerator.getInstance().toWebToken(news));
		newsDTO.setContent(null);
		newsDTO.setLikeFlag(getUserLikeFlag(userId, news.getId()).getCode());// 未登录用户id为0
		return newsDTO;
	}

	@Override
	public void setNewsTopFlag(SetNewsTopFlagCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		Integer namespaceId = checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType()).getNamespaceId();
		Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		News news = findNewsById(userId, newsId);
		if (NewsTopFlag.fromCode(news.getTopFlag()) == NewsTopFlag.NONE) {
			news.setTopFlag(NewsTopFlag.TOP.getCode());
			news.setTopIndex(newsProvider.getMaxTopIndex(namespaceId).longValue() + 1L);
		} else {
			news.setTopFlag(NewsTopFlag.NONE.getCode());
			news.setTopIndex(0L);
		}
		newsProvider.updateNews(news);
		
		syncNews(news.getId());
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
		Long newsId = WebTokenGenerator.getInstance().fromWebToken(newsToken, News.class).getId();
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
		News news = findNewsById(userId, newsId);
		news.setDeleterUid(userId);
		news.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		news.setStatus(NewsStatus.INACTIVE.getCode());
		newsProvider.updateNews(news);
		
		syncNewsWhenDelete(news.getId());
	}

	@Override
	public void setNewsLikeFlag(SetNewsLikeFlagCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());
		setNewsLikeFlag(userId, cmd.getNewsToken());
	}

	private void setNewsLikeFlag(Long userId, String newsToken) {
		Long newsId = checkNewsToken(userId, newsToken);
		News news = findNewsById(userId, newsId);
		UserLike userLike = findUserLike(userId, newsId);
		if (userLike == null) {
			news.setLikeCount(news.getLikeCount() + 1L);
			newsProvider.updateNews(news);
			userLike = new UserLike();
			userLike.setLikeType(NewsLikeFlag.LIKE.getCode());
			userLike.setOwnerUid(userId);
			userLike.setTargetId(newsId);
			userLike.setTargetType(EntityType.NEWS.getCode());
			userProvider.createUserLike(userLike);
		} else if (userLike.getLikeType() == NewsLikeFlag.NONE.getCode()) {
			news.setLikeCount(news.getLikeCount() + 1L);
			newsProvider.updateNews(news);
			userLike.setLikeType(NewsLikeFlag.LIKE.getCode());
			userProvider.updateUserLike(userLike);
		} else {
			news.setLikeCount(news.getLikeCount() - 1L);
			newsProvider.updateNews(news);
			userLike.setLikeType(NewsLikeFlag.NONE.getCode());
			userProvider.updateUserLike(userLike);
		}
		
		syncNews(news.getId());
	}

	@Override
	public AddNewsCommentResponse addNewsComment(final AddNewsCommentCommand cmd) {
		final Long userId = UserContext.current().getUser().getId();
		final Long newsId = checkNewsToken(userId, cmd.getNewsToken());
		// 检查参数
		News news = checkCommentParameter(userId, newsId, cmd);

		final List<Comment> comments = new ArrayList<>();
		final List<Attachment> attachments = new ArrayList<>();
		dbProvider.execute(s -> {
			// 创建评论
			Comment comment = processComment(userId, newsId, cmd);
			comments.add(comment);
			commentProvider.createComment(EhNewsComment.class, comment);

			// 创建附件
			if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
				attachments.addAll(processAttachments(userId, comment.getId(), cmd.getAttachments()));
				attachmentProvider.createAttachments(EhNewsAttachments.class, attachments);
			}
			return true;
		});
		
		AddNewsCommentResponse commentDTO = ConvertHelper.convert(comments.get(0), AddNewsCommentResponse.class);
		commentDTO.setAttachments(attachments.stream().map(a -> ConvertHelper.convert(a, NewsAttachmentDTO.class))
				.collect(Collectors.toList()));
		
		news.setChildCount(news.getChildCount()+1L);
		newsProvider.updateNews(news);

		return commentDTO;
	}

	private News checkCommentParameter(Long userId, Long newsId, AddNewsCommentCommand cmd) {
		// 检查owner是否存在
		checkOwner(userId, cmd.getOwnerId(), cmd.getOwnerType());
		// 检查News是否存在
		News news = findNewsById(userId, newsId);
		// 检查评论类型
		checkCommentType(userId, cmd.getContentType());
		// 检查附件类型
		if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
			cmd.getAttachments().forEach(a -> checkCommentType(userId, a.getContentType()));
		}
		
		return news;
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
			commentMap.get(a.getOwnerId()).getAttachments().add(attachmentDTO);
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
		Integer namespaceId = getNamespaceFromSceneToken(userId, cmd.getSceneToken());
		return ConvertHelper.convert(listNews(userId, namespaceId, cmd.getPageAnchor(), cmd.getPageSize(), true),
				ListNewsBySceneResponse.class);
	}

	private Integer getNamespaceFromSceneToken(Long userId, String sceneToken) {
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
		return sceneTokenDTO.getNamespaceId();
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
		News news = checkCommentParameter(userId, newsId, cmd);

		final List<Comment> comments = new ArrayList<>();
		final List<Attachment> attachments = new ArrayList<>();
		dbProvider.execute(s -> {
			// 创建评论
			Comment comment = processComment(userId, newsId, cmd);
			comments.add(comment);
			commentProvider.createComment(EhNewsComment.class, comment);

			// 创建附件
			if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
				attachments.addAll(processAttachments(userId, comment.getId(), cmd.getAttachments()));
				attachmentProvider.createAttachments(EhNewsAttachments.class, attachments);
			}
			return true;
		});

		AddNewsCommentBySceneResponse commentDTO = ConvertHelper.convert(comments.get(0),
				AddNewsCommentBySceneResponse.class);
		commentDTO.setAttachments(attachments.stream().map(a -> ConvertHelper.convert(a, NewsAttachmentDTO.class))
				.collect(Collectors.toList()));

		news.setChildCount(news.getChildCount()+1L);
		newsProvider.updateNews(news);
		
		return commentDTO;
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

	private News checkCommentParameter(Long userId, Long newsId, AddNewsCommentBySceneCommand cmd) {
		// 检查namespace是否存在
		getNamespaceFromSceneToken(userId, cmd.getSceneToken());
		// 检查News是否存在
		News news = findNewsById(userId, newsId);
		// 检查评论类型
		checkCommentType(userId, cmd.getContentType());
		// 检查附件类型
		if (cmd.getAttachments() != null && cmd.getAttachments().size() != 0) {
			cmd.getAttachments().forEach(a -> checkCommentType(userId, a.getContentType()));
		}
		return news;
	}

	@Override
	public void deleteNewsCommentByScene(DeleteNewsCommentBySceneCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		getNamespaceFromSceneToken(userId, cmd.getSceneToken());
		deleteNewsComment(userId, cmd.getNewsToken(), cmd.getId());
	}

	private void deleteNewsComment(Long userId, String newsToken, Long commentId) {
		Long newsId = checkNewsToken(userId, newsToken);
		News news = findNewsById(userId, newsId);
		Comment comment = commentProvider.findCommentById(EhNewsComment.class, commentId);
		if (comment.getOwnerId().longValue() != newsId.longValue()) {
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
		comment.setDeleterUid(userId);
		comment.setStatus(CommentStatus.INACTIVE.getCode());
		comment.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		commentProvider.updateComment(EhNewsComment.class, comment);
		
		news.setChildCount(news.getChildCount()-1L);
		newsProvider.updateNews(news);
	}

	@Override
	public void syncNews(SyncNewsCommand cmd) {
		if (cmd.getId() != null) {
			syncNews(cmd.getId());
		} else {
			// 一次同步200条，防止一次同步太多内存被压爆
			Integer pageSize = 200;
			int i = 0;
			while (true) {
				Long from = (long) (i * pageSize);
				List<News> list = newsProvider.findAllActiveNewsByPage(from, pageSize);
				if (list != null && list.size() > 0) {
					StringBuilder sb = new StringBuilder();
					list.forEach(n->{
						sb.append("{\"index\":{\"_id\":\"").append(n.getId()).append("\"}}\n").append(JSONObject.toJSONString(n)).append("\n");
					});
					
					searchProvider.bulk(SearchUtils.NEWS, sb.toString());
					
					if (list.size()<pageSize) {
						break;
					}
					i++;
				}else {
					break;
				}
			}
		}
	}
	
	private void syncNews(Long id){
		News news = newsProvider.findNewsById(id);
		if (news != null) {
			searchProvider.insertOrUpdate(SearchUtils.NEWS, news.getId().toString(), JSONObject.toJSONString(news));
		}
	}

	private void syncNewsWhenDelete(Long id){
		searchProvider.deleteById(SearchUtils.NEWS, id.toString());
	}
}
