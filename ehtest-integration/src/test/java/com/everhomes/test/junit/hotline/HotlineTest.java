package com.everhomes.test.junit.hotline;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.servicehotline.AddHotlineCommand;
import com.everhomes.rest.servicehotline.DeleteHotlineCommand;
import com.everhomes.rest.servicehotline.GetHotlineListCommand;
import com.everhomes.rest.servicehotline.GetHotlineListRestResponse;
import com.everhomes.rest.servicehotline.GetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.GetHotlineSubjectRestResponse;
import com.everhomes.rest.servicehotline.HotlineSubject;
import com.everhomes.rest.servicehotline.ServiceType;
import com.everhomes.rest.servicehotline.SetHotlineSubjectCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhServiceHotlines;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class HotlineTest extends BaseLoginAuthTestCase {

	private static final Integer NAMESPACEID = 0; 
	
	private static final String SET_HOTLINE_SUBJECT = "/hotline/setHotlineSubject";
	private static final String DELETE_SA_CATEGORY_URI = "/yellowPage/deleteServiceAllianceCategory";
	private static final String GET_SA_ENTERPRISE_DETAIL_URI = "/yellowPage/getServiceAllianceEnterpriseDetail";
	private static final String GET_SA_URI = "/yellowPage/getServiceAlliance";
	private static final String DELETE_HOTLINE = "/hotline/deleteHotline";
	private static final String ADD_HOTLINE = "/hotline/addHotline";
	private static final String GET_HOTLINE_LIST = "/hotline/getHotlineList";
	private static final String GET_HOTLINE_SUBJECT= "/hotline/getHotlineSubject";

	String ownerType = "community";
	Long ownerId = 240111044331048623L;
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	
	@Override
	protected void initCustomData() { 
		
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
	}
	@Test
	public void testAddHotline(){


		Integer namespaceId = 0;
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		
		AddHotlineCommand cmd = new AddHotlineCommand();
		cmd.setName("ksom");
		cmd.setContact("32423422");
		cmd.setDefaultOrder(21);
		cmd.setDescription("descdsfa");
		cmd.setNamespaceId(0);
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setServiceType(2);
		
		addHotline(cmd);
		
		DSLContext context = dbProvider.getDslContext();
		List<EhServiceHotlines> result = new ArrayList<>();
		context.select()
				.from(Tables.EH_SERVICE_HOTLINES) 
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhServiceHotlines.class));
					return null;
				});
		assertEquals(1, result.size());
		
		assertEquals(cmd.getName(), result.get(0).getName());
		assertEquals(cmd.getContact(), result.get(0).getContact());
		assertEquals(cmd.getDefaultOrder(), result.get(0).getDefaultOrder());
		assertEquals(cmd.getServiceType(), result.get(0).getServiceType());
		assertEquals(cmd.getOwnerType(), result.get(0).getOwnerType());
		assertEquals(cmd.getOwnerId(), result.get(0).getOwnerId());

		deleteHotline(result.get(0).getId());
		
	}
	
	public void addHotline(AddHotlineCommand cmd){
		
		RestResponseBase response = httpClientService.restPost(ADD_HOTLINE,
				cmd, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 
		
	}
	public void deleteHotline(Long id){
		DeleteHotlineCommand cmd = new DeleteHotlineCommand();
		cmd.setId(id);

		RestResponseBase response = httpClientService.restPost(DELETE_HOTLINE,
				cmd, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 
		
	}
	@Test
	public void testSubject(){

		Integer namespaceId = 0;
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		SetHotlineSubjectCommand cmd1 = new SetHotlineSubjectCommand();
		cmd1.setDisplayName("显示为1吧");
		cmd1.setOwnerId(ownerId);
		cmd1.setOwnerType(ownerType);
		cmd1.setServiceType(ServiceType.ZHUANSHU_SERVICE.getCode().intValue());
		cmd1.setSwitchFlag(NormalFlag.NEED.getCode());


		RestResponseBase response = httpClientService.restPost(SET_HOTLINE_SUBJECT,
				cmd1, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 
		
		GetHotlineSubjectCommand cmd2 = new GetHotlineSubjectCommand();

		cmd2.setOwnerType(ownerType);
		cmd2.setOwnerId(ownerId); 
		GetHotlineSubjectRestResponse   response2 = httpClientService.restPost(GET_HOTLINE_SUBJECT,
				cmd2, GetHotlineSubjectRestResponse.class, context);
		assertNotNull("The reponse of may not be null", response2);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response2),
				httpClientService.isReponseSuccess(response2)); 
		assertEquals(2, response2.getResponse().getSubjects().size());
		for(HotlineSubject subject : response2.getResponse().getSubjects()){
			if(subject.getServiceType().equals(cmd1.getServiceType())){
				assertEquals(cmd1.getDisplayName(),subject.getTitle());
			}
		}
		cmd1.setSwitchFlag(NormalFlag.NONEED.getCode());

		response = httpClientService.restPost(SET_HOTLINE_SUBJECT,
				cmd1, RestResponseBase.class, context);
		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response)); 
		
		response2 = httpClientService.restPost(GET_HOTLINE_SUBJECT,
				cmd2, GetHotlineSubjectRestResponse.class, context);
		assertNotNull("The reponse of may not be null", response2);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response2),
				httpClientService.isReponseSuccess(response2)); 
		assertEquals(1, response2.getResponse().getSubjects().size());
		
		
	}
	public void addHotlineList(){
		AddHotlineCommand cmd = new AddHotlineCommand();
		cmd.setName("热线1");
		cmd.setContact("110");
		cmd.setDefaultOrder(21);
		cmd.setDescription("备注");
		cmd.setNamespaceId(NAMESPACEID);
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setServiceType(ServiceType.SERVICE_HOTLINE.getCode().intValue());
		addHotline(cmd);

		cmd.setName("热线2");
		cmd.setContact("119");
		addHotline(cmd);
		cmd.setName("热线3");
		cmd.setContact("911");
		addHotline(cmd);
		cmd.setName("人员1");
		cmd.setContact("3323");
		cmd.setUserId(10001L);
		cmd.setDescription("洗厕所的");
		cmd.setServiceType(ServiceType.ZHUANSHU_SERVICE.getCode().intValue());
		addHotline(cmd);
		cmd.setName("人员2");
		cmd.setContact("ppap");
		cmd.setUserId(10002L);
		cmd.setDescription("扫地的");
		cmd.setServiceType(ServiceType.ZHUANSHU_SERVICE.getCode().intValue());
		addHotline(cmd);
		
	}
	@Test
	public void testHotlineList(){
 
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		
		addHotlineList();
		GetHotlineListCommand cmd = new GetHotlineListCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setServiceType(ServiceType.ZHUANSHU_SERVICE.getCode());
		GetHotlineListRestResponse   response2 = httpClientService.restPost(GET_HOTLINE_LIST,
				cmd, GetHotlineListRestResponse.class, context);
		assertNotNull("The reponse of may not be null", response2);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response2),
				httpClientService.isReponseSuccess(response2)); 
		assertEquals(2, response2.getResponse().getHotlines().size());
		
	}
}
