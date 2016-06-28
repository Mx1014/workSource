package com.everhomes.test.junit.news;

import java.io.File;
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
import com.everhomes.rest.news.ImportNewsCommand;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhNews;
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

	private void logon() {
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		logon(null, userIdentifier, plainTexPassword);
	}
	
	@Ignore
	@Test
	public void testCreateNews() {
		String uri = "/news/createNews";
		createNewsWithoutLogon(uri);
		logon();
		createNewsCorrect(uri);
		deleteAllNews();
		createNewsWithoutAbstractShortContent(uri);
		deleteAllNews();
		createNewsWithoutAbstractLongContent(uri);
		deleteAllNews();
		createNewsWithTitleNull(uri);
		deleteAllNews();
		createNewsWithContentNull(uri);
		deleteAllNews();
		createNewsWithOwnerIdNull(uri);
		deleteAllNews();
		createNewsWithOwnerTypeNull(uri);
		deleteAllNews();
		createNewsWithOwnerTypeError(uri);
		deleteAllNews();
		createNewsWithOwnerIdError(uri);
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
		assertTrue("errorCode should be 401", response.getErrorCode().intValue()==UserServiceErrorCode.ERROR_UNAUTHENTITICATION);
		assertTrue("errorScope should be user", UserServiceErrorCode.SCOPE.equals(response.getErrorScope()));
	}

	private void createNewsCorrect(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
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
		assertEquals("the author of db and response must equal", dbNews.get(0).getAuthor(), newsResponse.getAuthor());
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
				dbNews.get(0).getContent().substring(0, dbNews.get(0).getContent().length()>100?100:dbNews.get(0).getContent().length()));
	}
	
	private void createNewsWithoutAbstractLongContent(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setContentAbstract(null);
		cmd.setContent("唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文唐彤测试新闻创建正文");

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
				dbNews.get(0).getContent().substring(0, dbNews.get(0).getContent().length()>100?100:dbNews.get(0).getContent().length()));
	}

	private void createNewsWithTitleNull(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setTitle(null);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 506", response.getErrorCode().intValue()==ErrorCodes.ERROR_INVALID_PARAMETER);
		assertTrue("errorScope should be general", ErrorCodes.SCOPE_GENERAL.equals(response.getErrorScope()));
	}

	private void createNewsWithContentNull(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setContent(null);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 506", response.getErrorCode().intValue()==ErrorCodes.ERROR_INVALID_PARAMETER);
		assertTrue("errorScope should be general", ErrorCodes.SCOPE_GENERAL.equals(response.getErrorScope()));
	}

	private void createNewsWithOwnerIdNull(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setOwnerId(null);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 506", response.getErrorCode().intValue()==ErrorCodes.ERROR_INVALID_PARAMETER);
		assertTrue("errorScope should be general", ErrorCodes.SCOPE_GENERAL.equals(response.getErrorScope()));
	}

	private void createNewsWithOwnerTypeNull(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setOwnerType(null);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 506", response.getErrorCode().intValue()==ErrorCodes.ERROR_INVALID_PARAMETER);
		assertTrue("errorScope should be general", ErrorCodes.SCOPE_GENERAL.equals(response.getErrorScope()));
	}

	private void createNewsWithOwnerTypeError(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setOwnerType("community");

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 10001", response.getErrorCode().intValue()==NewsServiceErrorCode.ERROR_NEWS_OWNER_TYPE_INVALID);
		assertTrue("errorScope should be general", NewsServiceErrorCode.SCOPE.equals(response.getErrorScope()));
	}

	private void createNewsWithOwnerIdError(String uri) {
		CreateNewsCommand cmd = getNewsCmd();
		cmd.setOwnerId(9999999999L);

		CreateNewsRestResponse response = httpClientService.restGet(uri, cmd, CreateNewsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), !httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 10002", response.getErrorCode().intValue()==NewsServiceErrorCode.ERROR_NEWS_OWNER_ID_INVALID);
		assertTrue("errorScope should be general", NewsServiceErrorCode.SCOPE.equals(response.getErrorScope()));
	}

	private void deleteAllNews() {
		DSLContext context = dbProvider.getDslContext();
		context.truncate(Tables.EH_NEWS).execute();
	}

	@Test
	public void testImportNews(){
		logon();
		String uri = "/news/importNews";
		ImportNewsCommand cmd = new ImportNewsCommand();
		cmd.setOwnerId(1L);
		cmd.setOwnerType("organization");
		File file = new File("E:\\news_template.xlsx");
		RestResponseBase response = httpClientService.postFile(uri, cmd, file, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		assertTrue("errorCode should be 200", response.getErrorCode().intValue()==ErrorCodes.SUCCESS);
	}
	
	@Ignore
	@Test
	public void testClearType(){
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
}
