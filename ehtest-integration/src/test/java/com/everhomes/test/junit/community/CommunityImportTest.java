package com.everhomes.test.junit.community;

import org.junit.Test;

import com.everhomes.rest.community.admin.CreateCommunityCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;

public class CommunityImportTest extends BaseLoginAuthTestCase {
	
	@Test
	public void testCreateCommunity(){
		CreateCommunityCommand command = new CreateCommunityCommand();
		command.setName("唐彤测试园区快讯创建");
		command.setAliasName("我是别名");
		command.setAddress("我是地址");
		command.setDescription("我是描述 ");
		command.setCommunityType((byte)0);
		command.setProvinceName("广东省");
		command.setCityName("深圳市");
		command.setAreaName("南山区");
		command.setLatitude(113.953716);
		command.setLongitude(22.551986);
		
	}
	
	@Test
	public void testImportCommunity(){
		
	}
	
	@Test
	public void testListCommunityByNamespaceId(){
		
	}
	
}
