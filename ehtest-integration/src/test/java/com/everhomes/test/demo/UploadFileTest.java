package com.everhomes.test.demo;
import java.io.File;

import org.junit.Test;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class UploadFileTest extends BaseLoginAuthTestCase{
	
	private void logon() {
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		logon(null, userIdentifier, plainTexPassword);
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
		
//		DSLContext context = dbProvider.getDslContext();
//		Integer count = (Integer)context.selectCount().from(Tables.EH_NEWS).fetchOne().getValue(0);
//		assertTrue("the count should be 5", count.intValue() == 5);
	}
	
	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/3.4.x-test-data-news-organization-160627.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}
}
