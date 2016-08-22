package com.everhomes.test.junit.community;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.community.admin.CreateCommunityCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;

public class CommunityImportTest extends BaseLoginAuthTestCase {
	@Before
	public void setUp() {
		super.setUp();
		logon();
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

	@Test
	public void testListVersionRealm(){
		
	}
	
	@Test
	public void testListVersionInfo(){
		
	}
	
	@Test
	public void testCreateVersion(){
		
	}
	
	@Test
	public void testUpdateVersion(){
		
	}
	
	@Test
	public void testDeleteVersionById(){
		
	}
}
