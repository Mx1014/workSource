package com.everhomes.menu;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.menu.WebMenuCategory;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class MenuTest  extends LoginAuthTestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuTest.class);
	
    @Configuration
    @ComponentScan(basePackages = {
        "com.everhomes"
    })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
        })
    static class ContextConfiguration {
    }

    @Autowired
    WebMenuService webMenuService;
    
	@Autowired
	WebMenuPrivilegeProvider webMenuProvider;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAdd() {
    	WebMenu obj = new WebMenu();
    	obj.setModuleId(0l);
    	obj.setDataType("test");
    	obj.setIconUrl("http://test.icon");
    	obj.setType(WebMenuType.ZUOLIN.getCode());
    	obj.setLeafFlag((byte)0);
    	obj.setParentId(0l);
    	obj.setId(webMenuProvider.nextId());
    	obj.setPath(String.format("%d/%d", obj.getParentId(), obj.getId()));
    	webMenuProvider.createWebMenu(obj);
    	
    	ListWebMenuResponse res = webMenuService.listZuolinAdminWebMenu();
    	Assert.assertTrue(res.getMenus().size() > 0);
    	
    	webMenuProvider.deleteWebMenu(obj);
    }
    
    @Test
    public void testAddMenus() {
//    	MenuBuilder mb = new MenuBuilder(webMenuProvider, null, null);
//    	mb.setName("test01").Child()
//    	.setName("testchild01")
//    	.Child().setName("test0003")
//    	.Parent().Parent().Child()
//    	.setName("testchild02");
//    	
//    	List<WebMenu> menus = mb.getWebMenus();
//    	webMenuProvider.createWebMenus(menus);
    	
    	ListWebMenuResponse res = webMenuService.listZuolinAdminWebMenu();
    	LOGGER.info(StringHelper.toJsonString(res));
    }
    
    @Test
    public void testCreateAdminMenu() {
    	MenuBuilder mb = new MenuBuilder(webMenuProvider, null, null);
    	mb.setName("系统管理").setDataType("system-managerment").Child()
    	
    	.setName("管理员管理").setDataType("system-supers").setCategory(WebMenuCategory.MODULE.getCode()).Child()
    	.setName("管理员列表").setDataType("system-operators").setCategory(WebMenuCategory.PAGE.getCode())
    	.Parent().Child().setName("管理员权限与角色").setDataType("system-roles").setCategory(WebMenuCategory.PAGE.getCode())
    	.Parent()
    	
    	.Parent().Child().setName("基础信息配置").Child()
    	.Parent().Child().setName("城市与区县管理")
    	.Parent().Child().setName("基础服务分类管理")
    	.Parent().Child().setName("通用消息模板配置")
    	.Parent().Child().setName("通用业务模块配置")
    	.Parent().Child().setName("导航栏配置模板管理")
    	.Parent().Child().setName("服务门户界面模板管理")
    	.Parent().Child().setName("内容营运菜单模板管理")
    	.Parent()
    	
    	.Parent().Child().setName("版本升级与服务器配置").Child()
    	.Parent().Child().setName("系统升级配置")
    	.Parent().Child().setName("服务器配置")
    	.Parent();
    	
    	List<WebMenu> menus = mb.getWebMenus();
    	webMenuProvider.createWebMenus(menus);
    	
    	//基础数据
    	mb = new MenuBuilder(webMenuProvider, null, null);
    	mb.setName("基础数据管理").Child()
    	
    	.setName("社区数据管理").Child()
    	.Parent().Child().setName("小区管理")
    	.Parent().Child().setName("园区管理")
    	.Parent()
    	
    	.Parent().Child().setName("用户管理")
    	
    	.Parent().Child().setName("企业管理").Child()
    	.Parent().Child().setName("社区管理公司管理")
    	.Parent().Child().setName("企业管理")
    	.Parent();
    	
    	menus = mb.getWebMenus();
    	webMenuProvider.createWebMenus(menus);
    	
    	//域空间
    	mb = new MenuBuilder(webMenuProvider, null, null);
    	mb.setName("域空间管理").Child()
    	
    	.setName("App配置").Child()
    	.Parent().Child().setName("导航栏配置")
    	.Parent().Child().setName("服务门户界面配置")
    	.Parent().Child().setName("内容营运菜单模板配置")
    	.Parent().Child().setName("内容营运规则配置")
    	.Parent().Child().setName("业务模块配置")
    	.Parent()
    	
    	.Parent().Child().setName("后台菜单配置")
    	.Parent().Child().setName("关联客户查询")
    	.Parent().Child().setName("关联园区查询")
    	.Parent();
    	
    	menus = mb.getWebMenus();
    	webMenuProvider.createWebMenus(menus);
    	
    	//运营
    	mb = new MenuBuilder(webMenuProvider, null, null);
    	mb.setName("运营业务模块管理").Child()
    	
    	.setName("园区服务模块管理").Child()
    	.Parent().Child().setName("招商入驻")
    	.Parent().Child().setName("园区活动")
    	.Parent().Child().setName("园区论坛")
    	.Parent().Child().setName("园区公告")
    	
    	.Parent().Child().setName("能耗管理").setDataType("energy-managerment").setCategory(WebMenuCategory.MODULE.getCode())
    	
    	.Child().setName("表计管理").setDataType("meter-managerment")
    	.Parent().Child().setName("抄表记录").setDataType("meter-log")
    	.Parent().Child().setName("统计信息").setDataType("meter-statistics")
    	.Parent().Child().setName("参数设置").setDataType("meter-configuration")
    	.Parent()
    	
    	.Parent().Child().setName("公共门禁")
    	
    	.Child().setName("设备管理")
    	.Parent().Child().setName("用户授权")
    	.Parent()
    	
    	.Parent().Child().setName("停车充值")
    	.Parent().Child().setName("物业报修")
    	.Parent().Child().setName("物业缴费")
    	.Parent().Child().setName("公共会议室预订")
    	.Parent().Child().setName("VIP车位预订")
    	.Parent().Child().setName("电子屏预订")
    	.Parent().Child().setName("服务联盟")
    	.Parent().Child().setName("园区公告")
    	.Parent().Child().setName("园区快讯")
    	.Parent().Child().setName("园区热线")
    	.Parent().Child().setName("园区企业")
    	.Parent().Child().setName("俱乐部")
    	.Parent().Child().setName("工位预订")
    	.Parent().Child().setName("创客空间")
    	.Parent()
    	
    	.Parent().Child().setName("企业行政模块管理").Child()
    	.Parent().Child().setName("企业通信录")
    	.Parent().Child().setName("企业门禁")
    	.Parent().Child().setName("打卡考勤")
    	.Parent().Child().setName("审批")
    	.Parent().Child().setName("视频会议")
    	.Parent()
    	
    	.Parent().Child().setName("物业模块管理").Child()
    	.Parent().Child().setName("资产管理")
    	.Parent().Child().setName("客户管理")
    	.Parent().Child().setName("能耗管理")
    	.Parent().Child().setName("设备巡检")
    	.Parent().Child().setName("品质核查")
    	.Parent().Child().setName("采购管理")
    	.Parent().Child().setName("库存管理")
    	.Parent()
    	
    	.Parent().Child().setName("服务号接入管理")
    	;
    	
    	menus = mb.getWebMenus();
    	webMenuProvider.createWebMenus(menus);
    	
    	//
    	mb = new MenuBuilder(webMenuProvider, null, null);
    	mb.setName("内容运营管理").Child()
    	
    	.setName("消息推送")
    	.Parent().Child().setName("推广运营管理")
    	.Parent().Child().setName("用户运营管理")
    	.Parent().Child().setName("内容运营管理");
    	
    	menus = mb.getWebMenus();
    	webMenuProvider.createWebMenus(menus);
    	
    	mb = new MenuBuilder(webMenuProvider, null, null);
    	mb.setName("内容运营管理").Child()
    	
    	.setName("运营统计")
    	.Parent().Child().setName("应用活跃统计")
    	.Parent().Child().setName("模块访问统计")
    	.Parent().Child().setName("订单统计");
    	
    	//获取所有菜单
    	ListWebMenuResponse res = webMenuService.listZuolinAdminWebMenu();
    	LOGGER.info(StringHelper.toJsonString(res));
    }
    
    @Test
    public void testAes() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    	//byte[] key = Base64.decodeBase64("sh4lsdrHOxEseJf0dDrnVA==");
//    	byte[] key = "0123456789012345".getBytes();
    	byte[] key = {0x0000,0x0001,0x0002,0x0003,0x0004,0x0005,0x0006,0x0007,0x0008,0x0009,0x0000,0x0001,0x0002,0x0003,0x0004,0x0005};
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	String ivText = "614146a825e5a67448f22a27ae8aa778";
    	byte[] ivData = StringHelper.fromHexString(ivText);
    	IvParameterSpec iv = new IvParameterSpec(ivData);
    	cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), iv);
    	String data = "abcd1234";
		byte[] b = cipher.doFinal(data.getBytes("utf-8"));
		LOGGER.info("key=" + key.length);
		LOGGER.info(StringHelper.toHexString(b));

    }
    
    @Test
    public void testAes2() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    	byte[] key = "0123456789012345".getBytes();
    	String data = "04743015ee22f445e2a09141400d1822";
//    	String data = "39e4a020e90c79b5fb6026b94f69ae4c"; //ok
    	String ivText = "614146a825e5a67448f22a27ae8aa778";
    	byte[] input = StringHelper.fromHexString(data);
    	byte[] biv = StringHelper.fromHexString(ivText);
    	IvParameterSpec iv = new IvParameterSpec(biv);
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	Key inputKey = new SecretKeySpec(key, "AES");
    	cipher.init(Cipher.DECRYPT_MODE, inputKey, iv);
    	LOGGER.info("input=" + input.length);
		byte[] b = cipher.doFinal(input);
		LOGGER.info(new String(b));
    }
    
}