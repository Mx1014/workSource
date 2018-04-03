package com.everhomes.test.junit.news;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.news.AddNewsCommentCommand;
import com.everhomes.rest.news.AddNewsCommentResponse;
import com.everhomes.rest.news.AddNewsCommentRestResponse;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.rest.news.CommentStatus;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.CreateNewsResponse;
import com.everhomes.rest.news.CreateNewsRestResponse;
import com.everhomes.rest.news.DeleteNewsCommand;
import com.everhomes.rest.news.DeleteNewsCommentCommand;
import com.everhomes.rest.news.GetNewsContentCommand;
import com.everhomes.rest.news.GetNewsContentResponse;
import com.everhomes.rest.news.GetNewsContentRestResponse;
import com.everhomes.rest.news.GetNewsDetailInfoCommand;
import com.everhomes.rest.news.GetNewsDetailInfoResponse;
import com.everhomes.rest.news.GetNewsDetailInfoRestResponse;
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.ListNewsCommand;
import com.everhomes.rest.news.ListNewsCommentCommand;
import com.everhomes.rest.news.ListNewsCommentResponse;
import com.everhomes.rest.news.ListNewsCommentRestResponse;
import com.everhomes.rest.news.ListNewsResponse;
import com.everhomes.rest.news.ListNewsRestResponse;
import com.everhomes.rest.news.NewsCommentContentType;
import com.everhomes.rest.news.NewsOwnerType;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.rest.news.NewsTopFlag;
import com.everhomes.rest.news.SearchNewsCommand;
import com.everhomes.rest.news.SearchNewsResponse;
import com.everhomes.rest.news.SearchNewsRestResponse;
import com.everhomes.rest.news.SetNewsLikeFlagCommand;
import com.everhomes.rest.news.SetNewsTopFlagCommand;
import com.everhomes.rest.ui.news.AddNewsCommentBySceneCommand;
import com.everhomes.rest.ui.news.DeleteNewsCommentBySceneCommand;
import com.everhomes.rest.ui.news.ListNewsBySceneCommand;
import com.everhomes.rest.ui.news.SetNewsLikeFlagBySceneCommand;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.rest.user.UserLikeType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.server.schema.tables.pojos.EhNewsAttachments;
import com.everhomes.server.schema.tables.pojos.EhNewsComment;
import com.everhomes.server.schema.tables.pojos.EhUserLikes;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchConstant;
import com.everhomes.test.core.search.SearchProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NewsTest extends BaseLoginAuthTestCase {
	private static final String CREATE_NEWS_URI = "/news/createNews";
	private static final String IMPORT_NEWS_URI = "/news/importNews";
	private static final String LIST_NEWS_URI = "/news/listNews";
	private static final String SEARCH_NEWS_URI = "/news/searchNews";
	private static final String GET_NEWS_DETAIL_INFO_URI = "/news/getNewsDetailInfo";
	private static final String SET_NEWS_TOP_FLAG_URI = "/news/setNewsTopFlag";
	private static final String GET_NEWS_CONTENT_URI = "/news/getNewsContent";
	private static final String DELETE_NEWS_URI = "/news/deleteNews";
	private static final String SET_NEWS_LIKE_FLAG_URI = "/news/setNewsLikeFlag";
	private static final String ADD_NEWS_COMMENT_URI = "/news/addNewsComment";
	private static final String LIST_NEWS_COMMENT_URI = "/news/listNewsComment";
	private static final String DELETE_NEWS_COMMENT_URI = "/news/deleteNewsComment";
	private static final String LIST_NEWS_BY_SCENE_URI = "/ui/news/listNewsByScene";
	private static final String SET_NEWS_LIKE_FLAG_BY_SCENE_URI = "/ui/news/setNewsLikeFlagByScene";
	private static final String ADD_NEWS_COMMENT_BY_SCENE_URI = "/ui/news/addNewsCommentByScene";
	private static final String DELETE_NEWS_COMMENT_BY_SCENE_URI = "/ui/news/deleteNewsCommentByScene";
	private static final Long DEFAULT_CATEGORY_ID = 1L;
	@Autowired
	private SearchProvider searchProvider;

	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testCreateNews() {
		String uri = CREATE_NEWS_URI;
		createNewsWithoutLogon(uri);
		logon();
		createNewsCorrect(uri);
		deleteDbNews();
		createNewsWithoutAbstractShortContent(uri);
		deleteDbNews();
		createNewsWithoutAbstractLongContent(uri);
		deleteDbNews();
		createNewsWithTitleNull(uri);
		deleteDbNews();
		createNewsWithContentNull(uri);
		deleteDbNews();
		createNewsWithOwnerIdNull(uri);
		deleteDbNews();
		createNewsWithOwnerTypeNull(uri);
		deleteDbNews();
		createNewsWithOwnerTypeError(uri);
		deleteDbNews();
		createNewsWithOwnerIdError(uri);
	}

	@Test
	public void testImportNews() {
		logon();
		ImportNews(DEFAULT_CATEGORY_ID);
	}

	@Test
	public void testListNews() {
		String uri = LIST_NEWS_URI;
		logon();
		ImportNews(DEFAULT_CATEGORY_ID);
		ImportNews(DEFAULT_CATEGORY_ID+1120);
		ListNewsCommand cmd = new ListNewsCommand();
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
		cmd.setPageAnchor(0L);
		cmd.setPageSize(3);
		cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		// 第一次请求
		ListNewsRestResponse response = httpClientService.restPost(uri, cmd, ListNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListNewsResponse listNewsResponse = response.getResponse();
		assertNotNull(listNewsResponse);
		assertTrue("nextPageAnchor should be 1", 1L == listNewsResponse.getNextPageAnchor().longValue());
		assertTrue("list size should be 3", 3 == listNewsResponse.getNewsList().size());

		// 第二次请求
		cmd.setPageAnchor(listNewsResponse.getNextPageAnchor());
		response = httpClientService.restPost(uri, cmd, ListNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		listNewsResponse = response.getResponse();
		assertNotNull(listNewsResponse);
		assertTrue("nextPageAnchor should be null", null == listNewsResponse.getNextPageAnchor());
		assertTrue("list size should be 2", 2 == listNewsResponse.getNewsList().size());
	}

	@Test
	public void testSearchNews() {
		String uri = SEARCH_NEWS_URI;
		logon();
		testClearType();
		//一个import到默认的类型里
		ImportNews(DEFAULT_CATEGORY_ID);
		//一个import到随便的类型里
		ImportNews(21312123L);
		//睡眠1秒钟等待数据同步到elasticsearch中
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SearchNewsCommand cmd = new SearchNewsCommand();
		cmd.setKeyword("闻2");
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
		cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		SearchNewsRestResponse response = httpClientService.restPost(uri, cmd, SearchNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		SearchNewsResponse searchNewsResponse = response.getResponse();
		System.err.println(searchNewsResponse);
		assertNotNull(searchNewsResponse);
		assertTrue("nextPageAnchor should be null", null == searchNewsResponse.getNextPageAnchor());
		assertTrue("list size should be 1", 1 == searchNewsResponse.getNewsList().size());
		
		cmd.setCategoryId(2L);
		response = httpClientService.restPost(uri, cmd, SearchNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		searchNewsResponse = response.getResponse();
		System.err.println(searchNewsResponse);
		assertEquals(0, searchNewsResponse.getNewsList().size());
//		assertNotNull(searchNewsResponse);
//		assertTrue("nextPageAnchor should be null", null == searchNewsResponse.getNextPageAnchor());
//		assertTrue("list size should be 1", 0 == searchNewsResponse.getNewsList().size());
		
		//不带category应该查所有匹配的-有2个
		cmd.setCategoryId(null);
		response = httpClientService.restPost(uri, cmd, SearchNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		searchNewsResponse = response.getResponse();
		System.err.println(searchNewsResponse);
		assertTrue("nextPageAnchor should be null", null == searchNewsResponse.getNextPageAnchor());
		assertTrue("list size should be 2", 2 == searchNewsResponse.getNewsList().size());
	}

	@Test
	public void testGetNewsDetailInfo() {
		String uri = GET_NEWS_DETAIL_INFO_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);

		GetNewsDetailInfoCommand cmd = new GetNewsDetailInfoCommand();
		cmd.setNewsToken(newsToken);
		GetNewsDetailInfoRestResponse response = httpClientService.restPost(uri, cmd,
				GetNewsDetailInfoRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetNewsDetailInfoResponse detailInfoResponse = response.getResponse();
		assertNotNull(detailInfoResponse);
		assertTrue("new title should be 唐彤测试新闻创建", "唐彤测试新闻创建".equals(detailInfoResponse.getTitle()));
		assertTrue("view count should be 1", 1L == detailInfoResponse.getViewCount().longValue());
	}

	@Test
	public void testSetNewsTopFlag() {
		String uri = SET_NEWS_TOP_FLAG_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);

		SetNewsTopFlagCommand cmd = new SetNewsTopFlagCommand();
		cmd.setNewsToken(newsToken);
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());

		// 第一次请求
		RestResponseBase response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		List<EhNews> dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		EhNews news = dbNews.get(0);
		assertTrue("topFlag should be 1", news.getTopFlag().byteValue() == NewsTopFlag.TOP.getCode());
		assertTrue("topIndex should be 1", news.getTopIndex().longValue() == 1L);

		// 第二次请求
		response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("topFlag should be 0", news.getTopFlag().byteValue() == NewsTopFlag.NONE.getCode());
		assertTrue("topIndex should be 0", news.getTopIndex().longValue() == 0L);
	}

	@Test
	public void testGetNewsContent() {
		String uri = GET_NEWS_CONTENT_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);

		GetNewsContentCommand cmd = new GetNewsContentCommand();
		cmd.setNewsToken(newsToken);
		GetNewsContentRestResponse response = httpClientService.restPost(uri, cmd, GetNewsContentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetNewsContentResponse contentResponse = response.getResponse();
		assertNotNull(contentResponse);
		assertTrue("content should be 唐彤测试新闻创建正文", "唐彤测试新闻创建正文".equals(contentResponse.getContent()));
	}

	@Test
	public void testDeleteNews() {
		String uri = DELETE_NEWS_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);

		DeleteNewsCommand cmd = new DeleteNewsCommand();
		cmd.setNewsToken(newsToken);
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
		RestResponseBase response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		List<EhNews> dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		EhNews news = dbNews.get(0);
		assertTrue("status should be 0", news.getStatus().byteValue() == NewsStatus.INACTIVE.getCode());
		assertTrue("deleterUid should be 1", news.getDeleterUid() == cmd.getOwnerId());
	}

	@Test
	public void testSetNewsLikeFlag() {
		String uri = SET_NEWS_LIKE_FLAG_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);

		SetNewsLikeFlagCommand cmd = new SetNewsLikeFlagCommand();
		cmd.setNewsToken(newsToken);
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());

		// 第一次请求
		RestResponseBase response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		List<EhNews> dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		EhNews news = dbNews.get(0);
		assertTrue("likeCount should be 1", news.getLikeCount().longValue() == 1L);

		List<EhUserLikes> dbUserLikes = getDbUserLikes();
		assertEquals(1, dbUserLikes.size());
		EhUserLikes userLikes = dbUserLikes.get(0);
		assertTrue("ownerUid should be 1", userLikes.getOwnerUid().longValue() == 1L);
		assertTrue("targetType should be EhNews", "EhNews".equals(userLikes.getTargetType()));
		assertTrue("likeType should be 2", userLikes.getLikeType() == UserLikeType.LIKE.getCode());

		// 第二次请求
		response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("likeCount should be 0", news.getLikeCount().longValue() == 0L);

		dbUserLikes = getDbUserLikes();
		assertEquals(1, dbUserLikes.size());
		userLikes = dbUserLikes.get(0);
		assertTrue("ownerUid should be 1", userLikes.getOwnerUid().longValue() == 1L);
		assertTrue("targetType should be EhNews", "EhNews".equals(userLikes.getTargetType()));
		assertTrue("likeType should be 0", userLikes.getLikeType() == UserLikeType.NONE.getCode());

		// 第三次请求
		response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("likeCount should be 1", news.getLikeCount().longValue() == 1L);

		dbUserLikes = getDbUserLikes();
		assertEquals(1, dbUserLikes.size());
		userLikes = dbUserLikes.get(0);
		assertTrue("ownerUid should be 1", userLikes.getOwnerUid().longValue() == 1L);
		assertTrue("targetType should be EhNews", "EhNews".equals(userLikes.getTargetType()));
		assertTrue("likeType should be 2", userLikes.getLikeType() == UserLikeType.LIKE.getCode());
	}

	@Test
	public void testAddNewsComment() {
		String uri = ADD_NEWS_COMMENT_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);

		AddNewsCommentCommand cmd = getCommentCmd(newsToken);
		createCommentCorrect(cmd, uri);
	}

	@Test
	public void testAddNewsCommentWithAttachment() {
		String uri = ADD_NEWS_COMMENT_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);
		AddNewsCommentCommand cmd = getCommentCmd(newsToken);
		createCommentWithAttachment(cmd, uri);
	}

	@Test
	public void testListNewsComment() {
		String uri = LIST_NEWS_COMMENT_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);
		createCommentWithAttachment(getCommentCmd(newsToken), ADD_NEWS_COMMENT_URI);

		ListNewsCommentCommand cmd = new ListNewsCommentCommand();
		cmd.setNewsToken(newsToken);
		cmd.setPageSize(2);

		// 第一次请求
		ListNewsCommentRestResponse response = httpClientService.restPost(uri, cmd, ListNewsCommentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListNewsCommentResponse commentResponse = response.getResponse();
		assertNotNull(response);
		assertTrue("commentRespose nextPageAchor should not be null", commentResponse.getNextPageAnchor() != null);
		assertTrue("comment list size should be 2",
				commentResponse.getCommentList() != null && commentResponse.getCommentList().size() == 2);
		assertTrue("the first comment's attachments size should be 2",
				commentResponse.getCommentList().get(0).getAttachments().size() == 2);
		assertTrue("the second comment's attachments size should be 2",
				commentResponse.getCommentList().get(1).getAttachments().size() == 2);

		// 第二次请求
		cmd.setPageAnchor(commentResponse.getNextPageAnchor());
		response = httpClientService.restPost(uri, cmd, ListNewsCommentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		commentResponse = response.getResponse();
		assertNotNull(response);
		assertTrue("commentRespose nextPageAchor should be null", commentResponse.getNextPageAnchor() == null);
		assertTrue("comment list size should be 1",
				commentResponse.getCommentList() != null && commentResponse.getCommentList().size() == 1);
		assertTrue("the comment's attachments size should be 2",
				commentResponse.getCommentList().get(0).getAttachments().size() == 2);
	}

	@Test
	public void testDeleteNewsComment() {
		String uri = DELETE_NEWS_COMMENT_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);
		Long id = createCommentCorrect(getCommentCmd(newsToken), ADD_NEWS_COMMENT_URI);

		DeleteNewsCommentCommand cmd = new DeleteNewsCommentCommand();
		cmd.setId(id);
		cmd.setNewsToken(newsToken);
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());

		deleteNewsCommentCorrect(id, cmd, uri);
		cmd.setId(9999L);
		deleteNewsCommentError(cmd, uri);
	}

	@Test
	public void testListNewsByScene() {
		String uri = LIST_NEWS_BY_SCENE_URI;
		logon();
		ImportNews(DEFAULT_CATEGORY_ID);
		String sceneToken = getSceneToken();
		ListNewsBySceneCommand cmd = new ListNewsBySceneCommand();
		cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		cmd.setSceneToken(sceneToken);
		cmd.setPageSize(3);

		// 第一次请求-第一页
		ListNewsRestResponse response = httpClientService.restPost(uri, cmd, ListNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListNewsResponse listNewsResponse = response.getResponse();
		assertNotNull(listNewsResponse);
		assertTrue("nextPageAnchor should be 1", 1L == listNewsResponse.getNextPageAnchor().longValue());
		assertTrue("list size should be 3", 3 == listNewsResponse.getNewsList().size());

		// 第二次请求-第二页
		cmd.setPageAnchor(listNewsResponse.getNextPageAnchor());
		response = httpClientService.restPost(uri, cmd, ListNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		listNewsResponse = response.getResponse();
		assertNotNull(listNewsResponse);
		assertTrue("nextPageAnchor should be null", null == listNewsResponse.getNextPageAnchor());
		assertTrue("list size should be 2", 2 == listNewsResponse.getNewsList().size());
	}

	@Test
	public void testSetNewsLikeFlagByScene() {
		String uri = SET_NEWS_LIKE_FLAG_BY_SCENE_URI;
		logon();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);
		String sceneToken = getSceneToken();

		SetNewsLikeFlagBySceneCommand cmd = new SetNewsLikeFlagBySceneCommand();
		cmd.setNewsToken(newsToken);
		cmd.setSceneToken(sceneToken);

		// 第一次请求
		RestResponseBase response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		List<EhNews> dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		EhNews news = dbNews.get(0);
		assertTrue("likeCount should be 1", news.getLikeCount().longValue() == 1L);

		List<EhUserLikes> dbUserLikes = getDbUserLikes();
		assertEquals(1, dbUserLikes.size());
		EhUserLikes userLikes = dbUserLikes.get(0);
		assertTrue("ownerUid should be 1", userLikes.getOwnerUid().longValue() == 1L);
		assertTrue("targetType should be EhNews", "EhNews".equals(userLikes.getTargetType()));
		assertTrue("likeType should be 2", userLikes.getLikeType() == UserLikeType.LIKE.getCode());

		// 第二次请求
		response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("likeCount should be 0", news.getLikeCount().longValue() == 0L);

		dbUserLikes = getDbUserLikes();
		assertEquals(1, dbUserLikes.size());
		userLikes = dbUserLikes.get(0);
		assertTrue("ownerUid should be 1", userLikes.getOwnerUid().longValue() == 1L);
		assertTrue("targetType should be EhNews", "EhNews".equals(userLikes.getTargetType()));
		assertTrue("likeType should be 0", userLikes.getLikeType() == UserLikeType.NONE.getCode());

		// 第三次请求
		response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("likeCount should be 1", news.getLikeCount().longValue() == 1L);

		dbUserLikes = getDbUserLikes();
		assertEquals(1, dbUserLikes.size());
		userLikes = dbUserLikes.get(0);
		assertTrue("ownerUid should be 1", userLikes.getOwnerUid().longValue() == 1L);
		assertTrue("targetType should be EhNews", "EhNews".equals(userLikes.getTargetType()));
		assertTrue("likeType should be 2", userLikes.getLikeType() == UserLikeType.LIKE.getCode());
	}

	@Test
	public void testAddNewsCommentByScene() {
		String uri = ADD_NEWS_COMMENT_BY_SCENE_URI;
		logon();
		String sceneToken = getSceneToken();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);
		AddNewsCommentBySceneCommand cmd = new AddNewsCommentBySceneCommand();
		cmd.setSceneToken(sceneToken);
		cmd.setContentType(NewsCommentContentType.TEXT.getCode());
		cmd.setContent("唐彤测试评论创建");
		cmd.setNewsToken(newsToken);
		createCommentWithAttachment(cmd, uri);
	}

	@Test
	public void testDeleteNewsCommentByScene() {
		String uri = DELETE_NEWS_COMMENT_BY_SCENE_URI;
		logon();
		String sceneToken = getSceneToken();
		String newsToken = createNewsCorrect(CREATE_NEWS_URI);
		Long id = createCommentCorrect(getCommentCmd(newsToken), ADD_NEWS_COMMENT_URI);

		DeleteNewsCommentBySceneCommand cmd = new DeleteNewsCommentBySceneCommand();
		cmd.setId(id);
		cmd.setNewsToken(newsToken);
		cmd.setSceneToken(sceneToken);

		deleteNewsCommentCorrect(id, cmd, uri);
		cmd.setId(9999L);
		deleteNewsCommentError(cmd, uri);
	}

	@Test
	public void testClearType() {
		searchProvider.clearType(SearchConstant.NEWS);
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/3.4.x-test-data-news-organization-160627.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}

	@After
	public void tearDown() {
		logoff();
	}

	private void logon() {
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		logon(null, userIdentifier, plainTexPassword);
	}

	private CreateNewsCommand getNewsCmd() {
		CreateNewsCommand cmd = new CreateNewsCommand();
		cmd.setOwnerType("organization");
		cmd.setOwnerId(1L);
		cmd.setCategoryId(DEFAULT_CATEGORY_ID);
		cmd.setTitle("唐彤测试新闻创建");
		cmd.setContentAbstract("唐彤测试新闻摘要");
		cmd.setContent("唐彤测试新闻创建正文");
		cmd.setCoverUri("http://img1.gtimg.com/news/pics/hv1/173/49/2086/135654818.jpg");
		cmd.setAuthor("唐彤");
		cmd.setSourceDesc("百度新闻");
		cmd.setSourceUrl("http://www.baidu.com");
		return cmd;
	}

	private void createNewsWithoutLogon(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 401",
				response.getErrorCode().intValue() == UserServiceErrorCode.ERROR_UNAUTHENTITICATION);
		assertTrue("errorScope should be user", UserServiceErrorCode.SCOPE.equals(response.getErrorScope()));
	}

	private String createNewsCorrect(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateNewsResponse newsResponse = response.getResponse();
		assertNotNull(newsResponse);

		List<EhNews> dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		assertEquals("the title of db and response must equal", dbNews.get(0).getTitle(), newsResponse.getTitle());
		assertEquals("the author of db and response must equal", dbNews.get(0).getAuthor(), newsResponse.getAuthor());
		return newsResponse.getNewsToken();
	}

	private void createNewsWithoutAbstractShortContent(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setContentAbstract(null);
		cmd.setContent("唐彤测试新闻创建正文");

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateNewsResponse newsResponse = response.getResponse();
		assertNotNull(newsResponse);

		DSLContext context = dbProvider.getDslContext();
		List<EhNews> dbNews = context.select().from(Tables.EH_NEWS).fetch()
				.map(r -> ConvertHelper.convert(r, EhNews.class));
		assertEquals(1, dbNews.size());
		assertEquals("the title of db and response must equal", dbNews.get(0).getTitle(), newsResponse.getTitle());
		assertEquals("the abstract should be the pre 100 char of content", dbNews.get(0).getContentAbstract(),
				dbNews.get(0).getContent().substring(0,
						dbNews.get(0).getContent().length() > 100 ? 100 : dbNews.get(0).getContent().length()));
	}

	private void createNewsWithoutAbstractLongContent(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setContentAbstract(null);
		cmd.setContent(
				"唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文");

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateNewsResponse newsResponse = response.getResponse();
		assertNotNull(newsResponse);

		DSLContext context = dbProvider.getDslContext();
		List<EhNews> dbNews = context.select().from(Tables.EH_NEWS).fetch()
				.map(r -> ConvertHelper.convert(r, EhNews.class));
		assertEquals(1, dbNews.size());
		assertEquals("the title of db and response must equal", dbNews.get(0).getTitle(), newsResponse.getTitle());
		assertEquals("the abstract should be the pre 100 char of content", dbNews.get(0).getContentAbstract(),
				dbNews.get(0).getContent().substring(0,
						dbNews.get(0).getContent().length() > 100 ? 100 : dbNews.get(0).getContent().length()));
	}

	private void createNewsWithTitleNull(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setTitle(null);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 506", response.getErrorCode().intValue() == ErrorCodes.ERROR_INVALID_PARAMETER);
		assertTrue("errorScope should be general", ErrorCodes.SCOPE_GENERAL.equals(response.getErrorScope()));
	}

	private void createNewsWithContentNull(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setContent(null);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 506", response.getErrorCode().intValue() == ErrorCodes.ERROR_INVALID_PARAMETER);
		assertTrue("errorScope should be general", ErrorCodes.SCOPE_GENERAL.equals(response.getErrorScope()));
	}

	private void createNewsWithOwnerIdNull(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setOwnerId(null);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 506", response.getErrorCode().intValue() == ErrorCodes.ERROR_INVALID_PARAMETER);
		assertTrue("errorScope should be general", ErrorCodes.SCOPE_GENERAL.equals(response.getErrorScope()));
	}

	private void createNewsWithOwnerTypeNull(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setOwnerType(null);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 506", response.getErrorCode().intValue() == ErrorCodes.ERROR_INVALID_PARAMETER);
		assertTrue("errorScope should be general", ErrorCodes.SCOPE_GENERAL.equals(response.getErrorScope()));
	}

	private void createNewsWithOwnerTypeError(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setOwnerType("community");

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 10001",
				response.getErrorCode().intValue() == NewsServiceErrorCode.ERROR_NEWS_OWNER_TYPE_INVALID);
		assertTrue("errorScope should be general", NewsServiceErrorCode.SCOPE.equals(response.getErrorScope()));
	}

	private void createNewsWithOwnerIdError(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setOwnerId(9999999999L);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 10002",
				response.getErrorCode().intValue() == NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID);
		assertTrue("errorScope should be general", NewsServiceErrorCode.SCOPE.equals(response.getErrorScope()));
	}

	private List<EhNews> getDbNews() {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_NEWS).fetch().map(r -> ConvertHelper.convert(r, EhNews.class));
	}

	private List<EhUserLikes> getDbUserLikes() {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_USER_LIKES).fetch()
				.map(r -> ConvertHelper.convert(r, EhUserLikes.class));
	}

	private void deleteDbNews() {
		DSLContext context = dbProvider.getDslContext();
		context.truncate(Tables.EH_NEWS).execute();
	}

	private void ImportNews(Long categoryId) {
		try {
			String uri = IMPORT_NEWS_URI;
			ImportNewsCommand cmd = new ImportNewsCommand();
			cmd.setOwnerId(1L);
			cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
			if(null == categoryId)
				cmd.setCategoryId(1L);
			else
				cmd.setCategoryId(categoryId);
			
			File file;
			file = new File(new File("").getCanonicalPath() + "/src/test/data/excel/news_template.xlsx");
			RestResponseBase response = httpClientService.postFile(uri, cmd, file, RestResponseBase.class);
			assertNotNull(response);
			assertTrue("response= " + StringHelper.toJsonString(response),
					httpClientService.isReponseSuccess(response));
			assertTrue("errorCode should be 200", response.getErrorCode().intValue() == ErrorCodes.SUCCESS);

			DSLContext context = dbProvider.getDslContext();
			Integer count = (Integer) context.selectCount().from(Tables.EH_NEWS).fetchOne().getValue(0);
//			assertTrue("the count should be 5", count.intValue() == 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getSceneToken() {
		String uri = "/ui/user/listUserRelatedScenes";
		UserListUserRelatedScenesRestResponse response = httpClientService.restPost(uri, null,
				UserListUserRelatedScenesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		List<SceneDTO> list = response.getResponse();
		assertTrue("list size should be greater than 0", list != null && list.size() > 0);

		return list.get(0).getSceneToken();
	}

	private List<EhNewsComment> getDbComments(Long newsId) {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_NEWS_COMMENT).where(Tables.EH_NEWS_COMMENT.OWNER_ID.eq(newsId))
				.orderBy(Tables.EH_NEWS_COMMENT.ID.desc()).fetch()
				.map(r -> ConvertHelper.convert(r, EhNewsComment.class));
	}

	private AddNewsCommentCommand getCommentCmd(String newsToken) {
		AddNewsCommentCommand cmd = new AddNewsCommentCommand();
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
		cmd.setContentType(NewsCommentContentType.TEXT.getCode());
		cmd.setContent("唐彤测试评论创建");
		cmd.setNewsToken(newsToken);
		return cmd;
	}

	private Long createCommentCorrect(Object cmd, String uri) {
		// 第一次请求
		AddNewsCommentRestResponse response = httpClientService.restPost(uri, cmd, AddNewsCommentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		AddNewsCommentResponse commentResponse = response.getResponse();
		assertNotNull(commentResponse);

		List<EhNews> dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		EhNews news = dbNews.get(0);
		assertTrue("the news' childCount should be 1", 1L == news.getChildCount().longValue());

		List<EhNewsComment> dbComments = getDbComments(news.getId());
		assertEquals(1, dbComments.size());
		EhNewsComment comment = dbComments.get(0);
		assertTrue("the comment's ownerId should be equal to newsId",
				comment.getOwnerId().longValue() == news.getId().longValue());
		assertTrue("the comment's status should be 2",
				comment.getStatus().byteValue() == CommentStatus.ACTIVE.getCode());
		assertTrue("the comment's contentType should be TEXT",
				NewsCommentContentType.TEXT.getCode().equals(comment.getContentType()));
		assertTrue("the comment's createUid should be 1", 1L == comment.getCreatorUid());

		checkAttachment(cmd, comment);

		// 第二次请求
		response = httpClientService.restPost(uri, cmd, AddNewsCommentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		commentResponse = response.getResponse();
		assertNotNull(commentResponse);

		dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("the news' childCount should be 2", 2L == news.getChildCount().longValue());

		dbComments = getDbComments(news.getId());
		assertEquals(2, dbComments.size());
		comment = dbComments.get(0);

		checkAttachment(cmd, comment);

		// 第三次请求
		response = httpClientService.restPost(uri, cmd, AddNewsCommentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		commentResponse = response.getResponse();
		assertNotNull(commentResponse);

		dbNews = getDbNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("the news' childCount should be 3", 3L == news.getChildCount().longValue());

		dbComments = getDbComments(news.getId());
		assertEquals(3, dbComments.size());
		comment = dbComments.get(0);

		checkAttachment(cmd, comment);

		return comment.getId();
	}

	private void checkAttachment(Object cmd, EhNewsComment comment) {
		if (cmd instanceof AddNewsCommentCommand) {
			AddNewsCommentCommand command = (AddNewsCommentCommand) cmd;
			if (command.getAttachments() != null && command.getAttachments().size() > 0) {
				List<EhNewsAttachments> dbAttachments = getDbAttachments(comment.getId());
				assertEquals(command.getAttachments().size(), dbAttachments.size());
				EhNewsAttachments attachment = dbAttachments.get(0);
				assertTrue("the attachment's ownerId should be equal to the comment's id",
						attachment.getOwnerId().longValue() == comment.getId().longValue());
				assertTrue("the attachment's creatorUid should be 1", attachment.getCreatorUid().longValue() == 1L);
			}
		} else if (cmd instanceof AddNewsCommentBySceneCommand) {
			AddNewsCommentBySceneCommand command = (AddNewsCommentBySceneCommand) cmd;
			if (command.getAttachments() != null && command.getAttachments().size() > 0) {
				List<EhNewsAttachments> dbAttachments = getDbAttachments(comment.getId());
				assertEquals(command.getAttachments().size(), dbAttachments.size());
				EhNewsAttachments attachment = dbAttachments.get(0);
				assertTrue("the attachment's ownerId should be equal to the comment's id",
						attachment.getOwnerId().longValue() == comment.getId().longValue());
				assertTrue("the attachment's creatorUid should be 1", attachment.getCreatorUid().longValue() == 1L);
			}
		}
	}

	@SuppressWarnings("unused")
	private void deleteDbComments() {
		DSLContext context = dbProvider.getDslContext();
		context.truncate(Tables.EH_NEWS_COMMENT).execute();
	}

	private List<AttachmentDescriptor> getAttachmentsCmd() {
		List<AttachmentDescriptor> attachments = new ArrayList<>();
		AttachmentDescriptor attachment1 = new AttachmentDescriptor();
		attachment1.setContentType(NewsCommentContentType.AUDIO.getCode());
		attachment1
				.setContentUri("cs://1/image/aW1hZ2UvTVRwbU1qTmlaR0V3TVRsaVlUZ3paV0V4TXpWak9XUXlNVGcyTmpVek1ETmlOZw");
		attachments.add(attachment1);

		AttachmentDescriptor attachment2 = new AttachmentDescriptor();
		attachment2.setContentType(NewsCommentContentType.VIDEO.getCode());
		attachment2
				.setContentUri("cs://1/image/aW1hZ2UvTVRwbU1qTmlaR0V3TVRsaVlUZ3paV0V4TXpWak9XUXlNVGcyTmpVek1ETmlOZw");
		attachments.add(attachment2);

		return attachments;
	}

	private List<EhNewsAttachments> getDbAttachments(Long ownerId) {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_NEWS_ATTACHMENTS).where(Tables.EH_NEWS_ATTACHMENTS.OWNER_ID.eq(ownerId))
				.fetch().map(r -> ConvertHelper.convert(r, EhNewsAttachments.class));
	}

	private void createCommentWithAttachment(Object cmd, String uri) {
		List<AttachmentDescriptor> attachments = getAttachmentsCmd();
		if (cmd instanceof AddNewsCommentCommand) {
			((AddNewsCommentCommand) cmd).setAttachments(attachments);
		} else if (cmd instanceof AddNewsCommentBySceneCommand) {
			((AddNewsCommentBySceneCommand) cmd).setAttachments(attachments);
		}
		createCommentCorrect(cmd, uri);
	}

	private EhNewsComment getDbComment(Long id) {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_NEWS_COMMENT).where(Tables.EH_NEWS_COMMENT.ID.eq(id)).fetchOne()
				.map(r -> ConvertHelper.convert(r, EhNewsComment.class));
	}

	private void deleteNewsCommentCorrect(Long id, Object cmd, String uri) {
		RestResponseBase response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		EhNewsComment comment = getDbComment(id);
		assertNotNull(comment);
		assertTrue("the comment's status should be 0",
				comment.getStatus().byteValue() == CommentStatus.INACTIVE.getCode());
		assertTrue("the comment's deleterUid should be 1", comment.getDeleterUid().longValue() == 1L);
	}

	private void deleteNewsCommentError(Object cmd, String uri) {
		RestResponseBase response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		//如果是在lambla函数里面抛出的异常，则最后不会转化为实际抛出的那个异常，会变成Callable Exception
//		assertTrue("errorScope should be news", NewsServiceErrorCode.SCOPE.equals(response.getErrorScope()));
//		assertTrue("errorCode should be 10005",
//				NewsServiceErrorCode.ERROR_NEWS_NEWSID_COMMENTID_NOT_MATCH == response.getErrorCode().intValue());
	}

}
