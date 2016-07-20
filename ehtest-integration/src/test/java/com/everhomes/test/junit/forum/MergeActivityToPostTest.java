package com.everhomes.test.junit.forum;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.QueryOrganizationTopicCommand;
import com.everhomes.rest.organization.ListOrgTopicsRestResponse;
import com.everhomes.rest.organization.pm.ListPropTopicStatisticCommandResponse;
import com.everhomes.rest.realestate.PostRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class MergeActivityToPostTest  extends BaseLoginAuthTestCase {
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@After
	public void tearDown() {
		logoff();
	}
	
	@Override 
	protected void initCustomData() {
		String jsonFilePath = "data/json/3.7.1-test-data-merge-activity-to-post-160713.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}
	
	private void logon() {
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		logon(null, userIdentifier, plainTexPassword);
	}
	
	@Test@Ignore
	public void testNewOrgTopic(){
		logon();
		createOfficialActivity();
	}
	
	private void createOfficialActivity(){
		NewTopicCommand cmd = new NewTopicCommand();
		cmd.setForumId(1L);
		cmd.setSubject("aaaaaa");
		cmd.setContentType("text");
		cmd.setContent("aaaaaaaaaaaaaaaa");
		cmd.setEmbeddedJson("{\"subject\":\"aaa\",\"officialFlag\":1,\"location\":\"深圳市(深圳市)\",\"startTime\":\"2016-07-14 00:00:00\",\"endTime\":\"2016-07-16 00:00:00\",\"checkinFlag\":0,\"confirmFlag\":1,\"groupId\":null,\"longitude\":\"114.066112\",\"latitude\":\"22.548515\",\"guest\":\"aa\",\"tag\":\"科技大讲堂\"}");
		cmd.setEmbeddedAppId(3L);
		cmd.setVisibleRegionType((byte) 0);
		cmd.setVisibleRegionId(24210090697425925L);
		cmd.setCreatorTag("PM");
		cmd.setTargetTag("USER");
		cmd.setContentCategory(1010L); 
		cmd.setOfficialFlag((byte) 1);
		
		PostRestResponse response = httpClientService.restPost("/org/newOrgTopic", cmd, PostRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

//		Long postId = response.getResponse().getId();
//		DSLContext context = dbProvider.getDslContext();
//		EhForumPosts posts = context.select().from(Tables.EH_FORUM_POSTS).where(Tables.EH_FORUM_POSTS.ID.eq(postId)).fetchOne().map(r->ConvertHelper.convert(r, EhForumPosts.class));
//		assertTrue("officialFlag should be 1", posts.getOfficialFlag().byteValue() == (byte)1);
	}
	
	private void createNotOfficialActivity(){
		NewTopicCommand cmd = new NewTopicCommand();
		cmd.setForumId(1L);
		cmd.setSubject("aaaaaa");
		cmd.setContentType("text");
		cmd.setContent("aaaaaaaaaaaaaaaa");
		cmd.setEmbeddedJson("{\"subject\":\"aaa\",\"officialFlag\":1,\"location\":\"深圳市(深圳市)\",\"startTime\":\"2016-07-14 00:00:00\",\"endTime\":\"2016-07-16 00:00:00\",\"checkinFlag\":0,\"confirmFlag\":1,\"groupId\":null,\"longitude\":\"114.066112\",\"latitude\":\"22.548515\",\"guest\":\"aa\",\"tag\":\"科技大讲堂\"}");
		cmd.setEmbeddedAppId(3L);
		cmd.setVisibleRegionType((byte) 0);
		cmd.setVisibleRegionId(24210090697425925L);
		cmd.setCreatorTag("PM");
		cmd.setTargetTag("USER");
		cmd.setContentCategory(1010L); 
		
		PostRestResponse response = httpClientService.restPost("/org/newOrgTopic", cmd, PostRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	
	@Test
	public void testListOrgTopics(){
		logon();
		createOfficialActivity();
		createNotOfficialActivity();
		QueryOrganizationTopicCommand cmd = new QueryOrganizationTopicCommand();
		cmd.setOrganizationId(1L);
		cmd.setCommunityId(24210090697425925L);
		cmd.setContentCategory(1010L);
		cmd.setForumId(1L);
		cmd.setOfficialFlag((byte) 1);
		cmd.setEmbeddedAppId(3L);
		
		ListOrgTopicsRestResponse response= httpClientService.restPost("/org/listOrganizationTopics", cmd, ListOrgTopicsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		List<PostDTO> list = response.getResponse().getPosts();
		assertTrue("list size should be 1", list.size() == 1);
	}
}
