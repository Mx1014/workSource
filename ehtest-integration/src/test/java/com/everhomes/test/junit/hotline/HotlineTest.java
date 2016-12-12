package com.everhomes.test.junit.hotline;

import org.junit.Before;
import org.junit.Test;

import com.everhomes.test.core.base.BaseLoginAuthTestCase;

public class HotlineTest extends BaseLoginAuthTestCase {
	
	private static final String UPDATE_SA_CATEGORY_URI = "/yellowPage/updateServiceAllianceCategory";
	private static final String DELETE_SA_CATEGORY_URI = "/yellowPage/deleteServiceAllianceCategory";
	private static final String GET_SA_ENTERPRISE_DETAIL_URI = "/yellowPage/getServiceAllianceEnterpriseDetail";
	private static final String GET_SA_URI = "/yellowPage/getServiceAlliance";
	private static final String delete_hotline = "/hotline/deleteHotline";
	private static final String add_hotline = "/hotline/addHotlineList";
	private static final String DELETE_SA_ENTERPRISE_URI = "/hotline/getHotlineList";
	private static final String UPDATE_SA_ENTERPRISE_URI = "/hotline/getHotlineSubject";

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
	
	
	public void addHotline(){
		
	}
	
}
