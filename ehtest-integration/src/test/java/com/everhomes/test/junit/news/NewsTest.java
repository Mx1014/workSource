package com.everhomes.test.junit.news;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.news.CreateNewsCommand;
import com.everhomes.rest.news.CreateNewsResponse;
import com.everhomes.rest.news.CreateNewsRestResponse;
import com.everhomes.rest.news.DeleteNewsCommand;
import com.everhomes.rest.news.GetNewsContentCommand;
import com.everhomes.rest.news.GetNewsContentResponse;
import com.everhomes.rest.news.GetNewsContentRestResponse;
import com.everhomes.rest.news.GetNewsDetailInfoCommand;
import com.everhomes.rest.news.GetNewsDetailInfoResponse;
import com.everhomes.rest.news.GetNewsDetailInfoRestResponse;
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.ListNewsBySceneCommand;
import com.everhomes.rest.news.ListNewsCommand;
import com.everhomes.rest.news.ListNewsResponse;
import com.everhomes.rest.news.ListNewsRestResponse;
import com.everhomes.rest.news.NewsOwnerType;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.rest.news.NewsTopFlag;
import com.everhomes.rest.news.SearchNewsCommand;
import com.everhomes.rest.news.SearchNewsResponse;
import com.everhomes.rest.news.SearchNewsRestResponse;
import com.everhomes.rest.news.SetNewsLikeFlagCommand;
import com.everhomes.rest.news.SetNewsTopFlagCommand;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.rest.user.UserLikeType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.server.schema.tables.pojos.EhUserLikes;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchConstant;
import com.everhomes.test.core.search.SearchProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NewsTest extends BaseLoginAuthTestCase {

	@Autowired
	private SearchProvider searchProvider;

	@Before
	public void setUp() {
		super.setUp();
	}

	@Ignore
	@Test
	public void testCreateNews() {
		String uri = "/news/createNews";
		createNewsWithoutLogon(uri);
		logon();
		createNewsCorrect(uri);
		deleteNews();
		createNewsWithoutAbstractShortContent(uri);
		deleteNews();
		createNewsWithoutAbstractLongContent(uri);
		deleteNews();
		createNewsWithTitleNull(uri);
		deleteNews();
		createNewsWithContentNull(uri);
		deleteNews();
		createNewsWithOwnerIdNull(uri);
		deleteNews();
		createNewsWithOwnerTypeNull(uri);
		deleteNews();
		createNewsWithOwnerTypeError(uri);
		deleteNews();
		createNewsWithOwnerIdError(uri);
	}

	@Ignore
	@Test
	public void testImportNews() {
		logon();
		ImportNews();
	}

	@Ignore
	@Test
	public void testListNews() {
		String uri = "/news/listNews";
		logon();
		ImportNews();
		ListNewsCommand cmd = new ListNewsCommand();
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
		cmd.setPageAnchor(0L);
		cmd.setPageSize(3);
		//第一次请求
		ListNewsRestResponse response = httpClientService.restPost(uri, cmd, ListNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListNewsResponse listNewsResponse = response.getResponse();
		assertNotNull(listNewsResponse);
		assertTrue("nextPageAnchor should be 1", 1L == listNewsResponse.getNextPageAnchor().longValue());
		assertTrue("list size should be 3", 3 == listNewsResponse.getNewsList().size());
		
		//第二次请求
		cmd.setPageAnchor(listNewsResponse.getNextPageAnchor());
		response = httpClientService.restPost(uri, cmd, ListNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		listNewsResponse = response.getResponse();
		assertNotNull(listNewsResponse);
		assertTrue("nextPageAnchor should be null", null == listNewsResponse.getNextPageAnchor());
		assertTrue("list size should be 2", 2 == listNewsResponse.getNewsList().size());
	}

	@Ignore
	@Test
	public void testSearchNews() {
		testClearType();
		String uri = "/news/searchNews";
		logon();
		ImportNews();
		SearchNewsCommand cmd = new SearchNewsCommand();
		cmd.setKeyword("闻2");
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
		SearchNewsRestResponse response = httpClientService.restPost(uri, cmd, SearchNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		SearchNewsResponse searchNewsResponse = response.getResponse();
		assertNotNull(searchNewsResponse);
		assertTrue("nextPageAnchor should be null", null == searchNewsResponse.getNextPageAnchor());
		assertTrue("list size should be 1", 1 == searchNewsResponse.getNewsList().size());
	}
	
	@Ignore
	@Test
	public void testGetNewsDetailInfo(){
		String uri = "/news/getNewsDetailInfo";
		logon();
		String newsToken = createNewsCorrect("/news/createNews");
		
		GetNewsDetailInfoCommand cmd = new GetNewsDetailInfoCommand();
		cmd.setNewsToken(newsToken);
		GetNewsDetailInfoRestResponse response = httpClientService.restPost(uri, cmd, GetNewsDetailInfoRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		GetNewsDetailInfoResponse detailInfoResponse = response.getResponse();
		assertNotNull(detailInfoResponse);
		assertTrue("new title should be 唐彤测试新闻创建","唐彤测试新闻创建".equals(detailInfoResponse.getTitle()));
		assertTrue("view count should be 1", 1L == detailInfoResponse.getViewCount().longValue());
	}
	
	@Ignore
	@Test
	public void testSetNewsTopFlag(){
		String uri = "/news/setNewsTopFlag";
		logon();
		String newsToken = createNewsCorrect("/news/createNews");
		
		SetNewsTopFlagCommand cmd = new SetNewsTopFlagCommand();
		cmd.setNewsToken(newsToken);
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
		
		//第一次请求
		RestResponseBase response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		List<EhNews> dbNews = getNews();
		assertEquals(1, dbNews.size());
		EhNews news = dbNews.get(0);
		assertTrue("topFlag should be 1", news.getTopFlag().byteValue() == NewsTopFlag.TOP.getCode());
		assertTrue("topIndex should be 1", news.getTopIndex().longValue() == 1L);
		
		//第二次请求
		response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		dbNews = getNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("topFlag should be 0", news.getTopFlag().byteValue() == NewsTopFlag.NONE.getCode());
		assertTrue("topIndex should be 0", news.getTopIndex().longValue() == 0L);
	}
	
	@Ignore
	@Test
	public void testGetNewsContent(){
		String uri = "/news/getNewsContent";
		logon();
		String newsToken = createNewsCorrect("/news/createNews");
		
		GetNewsContentCommand cmd = new GetNewsContentCommand();
		cmd.setNewsToken(newsToken);
		GetNewsContentRestResponse response = httpClientService.restPost(uri, cmd, GetNewsContentRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		GetNewsContentResponse contentResponse = response.getResponse();
		assertNotNull(contentResponse);
		assertTrue("content should be 唐彤测试新闻创建正文", "唐彤测试新闻创建正文".equals(contentResponse.getContent()));
	}
	
	@Ignore
	@Test
	public void testDeleteNews(){
		String uri = "/news/deleteNews";
		logon();
		String newsToken = createNewsCorrect("/news/createNews");
		
		DeleteNewsCommand cmd = new DeleteNewsCommand();
		cmd.setNewsToken(newsToken);
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
		RestResponseBase response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		List<EhNews> dbNews = getNews();
		assertEquals(1, dbNews.size());
		EhNews news = dbNews.get(0);
		assertTrue("status should be 0", news.getStatus().byteValue() == NewsStatus.INACTIVE.getCode());
		assertTrue("deleterUid should be 1", news.getDeleterUid() == cmd.getOwnerId());
	}
	
	@Ignore
	@Test
	public void testSetNewsLikeFlag(){
		String uri = "/news/setNewsLikeFlag";
		logon();
		String newsToken = createNewsCorrect("/news/createNews");
		
		SetNewsLikeFlagCommand cmd = new SetNewsLikeFlagCommand();
		cmd.setNewsToken(newsToken);
		cmd.setOwnerId(1L);
		cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
		
		//第一次请求
		RestResponseBase response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		List<EhNews> dbNews = getNews();
		assertEquals(1, dbNews.size());
		EhNews news = dbNews.get(0);
		assertTrue("likeCount should be 1", news.getLikeCount().longValue() == 1L);
		
		List<EhUserLikes> dbUserLikes = getUserLikes();
		assertEquals(1, dbUserLikes.size());
		EhUserLikes userLikes = dbUserLikes.get(0);
		assertTrue("ownerUid should be 1", userLikes.getOwnerUid().longValue() == 1L);
		assertTrue("targetType should be EhNews", "EhNews".equals(userLikes.getTargetType()));
		assertTrue("likeType should be 1", userLikes.getLikeType()==UserLikeType.LIKE.getCode());
		
		//第二次请求
		response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		dbNews = getNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("likeCount should be 0", news.getLikeCount().longValue() == 0L);
		
		dbUserLikes = getUserLikes();
		assertEquals(1, dbUserLikes.size());
		userLikes = dbUserLikes.get(0);
		assertTrue("ownerUid should be 1", userLikes.getOwnerUid().longValue() == 1L);
		assertTrue("targetType should be EhNews", "EhNews".equals(userLikes.getTargetType()));
		assertTrue("likeType should be 0", userLikes.getLikeType()==UserLikeType.NONE.getCode());
		
		//第三次请求
		response = httpClientService.restPost(uri, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		dbNews = getNews();
		assertEquals(1, dbNews.size());
		news = dbNews.get(0);
		assertTrue("likeCount should be 1", news.getLikeCount().longValue() == 1L);
		
		dbUserLikes = getUserLikes();
		assertEquals(1, dbUserLikes.size());
		userLikes = dbUserLikes.get(0);
		assertTrue("ownerUid should be 1", userLikes.getOwnerUid().longValue() == 1L);
		assertTrue("targetType should be EhNews", "EhNews".equals(userLikes.getTargetType()));
		assertTrue("likeType should be 2", userLikes.getLikeType()==UserLikeType.LIKE.getCode());
	}
	
	@Test
	public void testListNewsByScene(){
		String uri = "/ui/news/listNewsByScene";
		logon();
		ImportNews();
		String sceneToken = getSceneToken();
		ListNewsBySceneCommand cmd = new ListNewsBySceneCommand();
		cmd.setSceneToken(sceneToken);
		
		
		
		
	}
	
	
	
	
	@Ignore
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

		List<EhNews> dbNews = getNews();
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

	private List<EhNews> getNews(){
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_NEWS).fetch()
				.map(r -> ConvertHelper.convert(r, EhNews.class));
	}
	
	private List<EhUserLikes> getUserLikes(){
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_USER_LIKES).fetch()
				.map(r -> ConvertHelper.convert(r, EhUserLikes.class));
	}
	
	private void deleteNews() {
		DSLContext context = dbProvider.getDslContext();
		context.truncate(Tables.EH_NEWS).execute();
	}

	private void ImportNews() {
		try {
			String uri = "/news/importNews";
			ImportNewsCommand cmd = new ImportNewsCommand();
			cmd.setOwnerId(1L);
			cmd.setOwnerType(NewsOwnerType.ORGANIZATION.getCode());
			File file;
			file = new File(new File("").getCanonicalPath() + "\\src\\test\\data\\excel\\news_template.xlsx");
			RestResponseBase response = httpClientService.postFile(uri, cmd, file, RestResponseBase.class);
			assertNotNull(response);
			assertTrue("response= " + StringHelper.toJsonString(response),
					httpClientService.isReponseSuccess(response));
			assertTrue("errorCode should be 200", response.getErrorCode().intValue() == ErrorCodes.SUCCESS);

			DSLContext context = dbProvider.getDslContext();
			Integer count = (Integer) context.selectCount().from(Tables.EH_NEWS).fetchOne().getValue(0);
			assertTrue("the count should be 5", count.intValue() == 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getSceneToken(){
		String uri = "/ui/user/listUserRelatedScenes";
		UserListUserRelatedScenesRestResponse response = httpClientService.restPost(uri, null, UserListUserRelatedScenesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		List<SceneDTO> list = response.getResponse();
		assertTrue("list size should be greater than 0", list!=null&&list.size()>0);
		
		return list.get(0).getSceneToken();
	}
}
