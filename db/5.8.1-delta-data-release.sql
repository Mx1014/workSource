-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR: liangqishi 20180823
-- REMARK: 不用为统一订单申请专有域名，使用各系统的默认域名(独立部署的也一样)

-- AUTHOR: 黄良铭 20180905
-- REMARK: coreserver 配置中需加入kafka的配置信息.具体请见(http://serverdoc.lab.everhomes.com/docs/faq/baseline-21535076011)
--         网址中的 "修改服务器配置" 一项

-- AUTHOR: 黄明波 20180906
-- REMARK: 只在----深圳湾----调用/yellowPage/syncSARequestInfo 接口

-- AUTHOR: 梁家声 20180914
-- REMARK: 修改content配置，将auth改为3

-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇
-- REMARK: 防止模板表单占用
update `eh_general_form_templates` set namespace_id = 0, id = 2500001 where module_id = 25000;

-- AUTHOR: liangqishi 20180810
-- REMARK: 增加统一订单错误码
SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'pay', '10013', 'zh_CN', '支付回调链接配置为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'asset', '10013', 'zh_CN', '支付回调链接配置为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10009', 'zh_CN', '帐单组不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10010', 'zh_CN', '没有配置收款方帐号');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10011', 'zh_CN', '缴费订单创建失败');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.default.personal_bind_phone', '12000001802', '支付个人帐号默认的绑定手机号', 0, NULL, 0);
	
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.system_id', '10', '由支付系统分配的业务系统ID', 0, NULL, 0);

-- AUTHOR: 马世亨 20180817
-- REMARK: 增加统一订单错误码
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
	VALUES ((@locale_string_id := @locale_string_id + 1), 'pmtask', '10019', 'zh_CN', '找不到账单');

-- AUTHOR: huangmingbo
-- REMARK: 增加统一订单配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('pay.v2.callback.url.siyinprint', '/siyinprint/notifySiyinprintOrderPaymentV2', '打印缴费新支付回调接口', 0, NULL, 0);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('pay.v2.callback.url.express', '/express/notifyExpressOrderPaymentV2', '快递缴费新支付回调接口', 0, NULL, 0);
	
	


SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.secretKey','OMtTBDhmVQSIP6oJBZ+mw+9i8+wnS1WAwsEVRoFvGXfNmCokOamwScJLdilQ3CuCXYb5J7HK+aua8sifKcEsiQ==','the point secretKey','0',NULL,NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.appKey','476fba87-dd1b-4ab9-ad6a-ca598a889c91','the point appKey','0',NULL,NULL);


INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(16022500 ,'积分银行',16020000,NULL,'integral-bank',1,2,'/16000000/16020000/16022500','zuolin',120,4800,3,'system','module',NULL);
INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(16023500 ,'积分池',16020000,NULL,'integral-pool',1,2,'/16000000/16020000/16023500','zuolin',130,4900,3,'system','module',NULL);

INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(16022600 ,'积分银行',42000000,NULL,'integral-bank',1,2,'/40000040/42000000/16022600','park',120,4800,3,'system','module',NULL);
INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(16023600 ,'积分池',42000000,NULL,'integral-pool',1,2,'/40000040/42000000/16023600','park',130,4900,3,'system','module',NULL);

INSERT INTO eh_service_modules(id ,NAME , parent_id,path ,TYPE ,LEVEL ,STATUS ,default_order ,menu_auth_flag,category,operator_uid,creator_uid) VALUES(4800,'积分银行',80000,'/200/80000/4800',1,3,2,110,1,'module',0,0);
INSERT INTO eh_service_modules(id ,NAME , parent_id,path ,TYPE ,LEVEL ,STATUS ,default_order ,menu_auth_flag,category,operator_uid,creator_uid) VALUES(4900,'积分池',80000,'/200/80000/4900',1,3,2,120,1,'module',0,0);

INSERT INTO `eh_apps` ( `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`)
 VALUES('1','d80e06ca-3766-11e5-b18f-b083fe4e159f','g1JOZUM3BYzWpZD5Q7p3z+i/z0nj2TcokTFx2ic53FCMRIKbMhSUCi7fSu9ZklFCZ9tlj68unxur9qmOji4tNg==','server.core.url','server.core.url','1',NULL,NULL,NULL);

-- AUTHOR: 梁燕龙
-- REMARK: 导入模板备注文字
INSERT into `eh_locale_strings`(`scope`,`code`,`locale`,`text`) VALUES ('activity','27','zh_CN','填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）
1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照提示输入。
2、请在表格里面逐行录入数据，建议一次导入不超过500条信息。
3、请不要随意复制单元格，这样会破坏字段规则校验。
4、日期输入格式：yyyy-MM-dd（2017-01-01）或 yyyy/MM/dd（2017/01/01）
5、红色字段为必填项
6、请注意：手机号码是唯一的，若重复添加，则自动更新覆盖（仅更新不为空值的字段信息）');

SET @id = (SELECT max(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@id:=@id+1), 'activity.notification', '21', 'zh_CN', '活动报名成功', '您在「${postName}」的报名已提交。', '0');


-- AUTHOR: 严军
-- REMARK: 更新收款账户模块的权限控制类型你给。
UPDATE eh_service_modules SET path = '/200/140000/58000', module_control_type = 'community_control' WHERE id = 58000;

-- AUTHOR: 梁燕龙
-- REMARK: 个人中心初始化数据
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('personal.wallet.home.url', 'https://payv2.zuolin.com', '个人中心我的钱包跳转URL域名', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('personal.order.home.url', 'https://biz.zuolin.com', '个人中心我的订单跳转URL域名', '0', NULL, '1');

set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'会员等级','会员等级',0,1,0,1,0,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'积分','积分',0,2,0,1,1,2,'',1,1,'/integral-management/build/index.html?systemId=%s&ehnavigatorstyle=2#/home#sign_suffix');



-- AUTHOR: 黄鹏宇
-- REMARK: 一键转为资质客户权限

SET @id = (SELECT IFNULL(MAX(id),1) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:= @id +1, 21110, 0, 21116, '一键转为资质客户', 13, SYSDATE());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21116, null, '客户管理 一键转为资质客户权限', '客户管理 业务模块权限', NULL);

update `eh_general_form_templates` set `template_text` = '[{
	"dynamicFlag": 0,
	"fieldDesc": "客户名称",
	"fieldDisplayName": "客户名称",
	"fieldExtra": "{}",
	"fieldName": "客户名称",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"remark": "系统自动获取客户管理中该项目下所有客户信息供用户选择；",
	"disabled": true,
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
},
{
	"dynamicFlag": 0,
	"fieldDesc": "楼栋门牌",
	"fieldDisplayName": "楼栋门牌",
	"fieldExtra": "{}",
	"fieldName": "楼栋门牌",
	"fieldType": "SINGLE_LINE_TEXT",
	"remark": "系统自动获取资产管理中该项目下所有待租门牌供用户选择；",
	"disabled": true,
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
},
{
	"dynamicFlag": 0,
	"fieldDesc": "审批状态",
	"fieldDisplayName": "审批状态",
	"fieldExtra": "{}",
	"fieldName": "审批状态",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"remark": "系统自动根据不同的触发不同的操作；",
	"disabled": true,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
}]' where id = 2500001;

update eh_service_modules set action_type = 14 where id = 25000;
update eh_service_modules set instance_config = '{"url":"${home.url}/requisition/build/index.html?hideNavigationBar=1#/home#sign_suffix"}' where id=25000;


-- END

-- AUTHOR: xq.tian  20180906
-- REMARK: 电商相关的配置都允许后台修改 add
UPDATE eh_configurations
SET is_readonly = 0
WHERE name IN ('user.order.url', 'apply.shop.url', 'manage.shop.url', 'user.coupon.url', 'business.detail.url', 'prefix.url');
-- END

-- --------------------- SECTION END ---------------------------------------------------------



-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: liangqishi 20180810
-- REMARK: 海岸的支付回调URL是定制的，但不需要用另外一个key，只需要按域空间分即可；
-- REMARK: 删除海岸域空间特殊的回调URL定制key，而补充该域空间的脚本；
DELETE FROM `eh_configurations` WHERE `name`='pay.v2.callback.url.pmsy';
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('pay.v2.callback.url.asset', '/pmsy/payNotify', '物业缴费新支付回调接口', 999993, NULL, 0);

-- AUTHOR: liangqishi 20180811
-- REMARK: 增加连接统一订单服务器的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.connect_url', 'https://core.zuolin.com/gorder', '连接统一订单服务器的链接', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_key', '69ee0cb3-5afb-4d83-ae12-ef493de48de2', '连接统一订单服务器的appkey', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_secret', 'T6PEjA9GBAVMBmlBYDs9RkoQMurrH5XQjFoP1v+oGomKeIdsqVhwpTVv8AHPLWo/I09IudgxR4/zjvM9YYwxzg==', '连接统一订单服务器的appsecret', 0, NULL, 0);

-- AUTHOR: 黄良铭
-- REMARK: 积分所需配置表 及菜单
SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.url','https://core.zuolin.com','the point url','0',NULL,NULL);
 
-- AUTHOR: 梁燕龙
-- REMARK: 个人中心初始化数据
set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://1/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://1/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://1/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://1/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://1/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://1/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://1/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://1/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://1/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://1/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://1/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);
-- --------------------- SECTION END ---------------------------------------------------------





-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本

-- AUTHOR: liangqishi 20180811
-- REMARK: 增加连接统一订单服务器的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.connect_url', 'https://zijing.lihekefu.com/gorder', '连接统一订单服务器的链接', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_key', 'fc11e221-29d6-4f07-b069-9dd79e077b6a', '连接统一订单服务器的appkey', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_secret', 'Z9VdCw9PEvui6tPTihExybCrJJHRSfNAPKuutK978NgxCpmCu+ztk0alVqTMN5JcBZe0thya7zK/5a/qe2fANA==', '连接统一订单服务器的appsecret', 0, NULL, 0);

-- AUTHOR: 黄良铭
-- REMARK: 积分所需配置表 及菜单
SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.url','https://zijing.lihekefu.com','the point url','0',NULL,NULL);
 
-- AUTHOR: 梁燕龙
-- REMARK: 个人中心初始化数据
set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://1/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet?systemId=14#/');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://1/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://1/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://1/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://1/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://1/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://1/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://1/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://1/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://1/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://1/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);
-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本

-- AUTHOR: liangqishi 20180811
-- REMARK: 增加连接统一订单服务器的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.connect_url', 'https://core.gd-we.com/gorder', '连接统一订单服务器的链接', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_key', 'fa0e2f1f-01b9-447b-824d-a91b7157c976', '连接统一订单服务器的appkey', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_secret', 'xNTG+ggO1qIg01zqRTxqbjl+jPAAS5L3Q3TgxefXWpLoiQ0v9+8VUXR6xyqhOEBcFQFp4AbVa08daxHtaOZOrw==', '连接统一订单服务器的appsecret', 0, NULL, 0);

-- AUTHOR: 黄良铭
-- REMARK: 积分所需配置表 及菜单
SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.url','https://core.gd-we.com','the point url','0',NULL,NULL);
 
 
-- AUTHOR: 梁燕龙
-- REMARK: 个人中心初始化数据
set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://1/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet?systemId=12#/');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://1/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://1/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://1/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://1/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://1/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://1/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://1/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://1/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://1/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://1/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);


-- --------------------- SECTION END ---------------------------------------------------------





-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本



-- AUTHOR: liangqishi 20180811
-- REMARK: 增加连接统一订单服务器的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.connect_url', 'https://park.szbay.com/gorder', '连接统一订单服务器的链接', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_key', '2ecedecc-3592-4edd-bfff-205b71fde95c', '连接统一订单服务器的appkey', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_secret', 'T1nIY3vNMK83dkJNcD/DIk9xx2rZup9KDjGwITiUnVFMVGVVwnmk5XHBHSe7BOm5Ex4TKflryC5IEURNQWHDIg==', '连接统一订单服务器的appsecret', 0, NULL, 0);

-- AUTHOR: 黄良铭
-- REMARK: 积分所需配置表 及菜单
SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.url','https://park.szbay.com','the point url','0',NULL,NULL);

-- AUTHOR: 梁燕龙
-- REMARK: 个人中心初始化数据
set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://1/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet?systemId=11#/');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://1/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://1/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://1/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1,'http://payv2.zuolin.com/app/appinvoice');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://1/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://1/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://1/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://1/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://1/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://1/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://1/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);


-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:

-- AUTHOR: 梁燕龙
-- REMARK: 个人中心初始化数据
set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://1/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet?systemId=13#/');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://1/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://1/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://1/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://1/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://1/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://1/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://1/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://1/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://1/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://1/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);

-- --------------------- SECTION END ---------------------------------------------------------





-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本

-- AUTHOR: liangqishi 20180811
-- REMARK: 增加连接统一订单服务器的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.connect_url', 'https://park.huimenggj.cn/gorder', '连接统一订单服务器的链接', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_key', '31128781-55bf-48ca-8e4d-002be862909f', '连接统一订单服务器的appkey', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_secret', 'ZF+K+ovW6J1uICefPRUC1bBxF9cdxUk9KLbzbnGzRw9+dVwSCZoP/m9qIUitOgoo0GG9YWDzyu8MDnPKxLKSIA==', '连接统一订单服务器的appsecret', 0, NULL, 0);

-- AUTHOR: 黄良铭
-- REMARK: 积分所需配置表 及菜单
SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.url','https://park.huimenggj.cn','the point url','0',NULL,NULL);
 
 
-- AUTHOR: 梁燕龙
-- REMARK: 个人中心初始化数据
set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://1/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet?systemId=13#/');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://1/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://1/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://1/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://1/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://1/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://1/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://1/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://1/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://1/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://1/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);


-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: nanshanquzhengfu
-- DESCRIPTION: 此SECTION只在南山区政府-999931执行的脚本

-- AUTHOR: 梁燕龙
-- REMARK: 个人中心初始化数据
set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://3/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet?systemId=13#/');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://3/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://3/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://3/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://3/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://3/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://3/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://3/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://3/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://3/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://3/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本

-- AUTHOR: 梁燕龙
-- REMARK: 个人中心初始化数据
set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://3/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet?systemId=13#/');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://3/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://3/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://3/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://3/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://3/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://3/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://3/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://3/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://3/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://3/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本

-- AUTHOR: liangqishi 20180811
-- REMARK: 增加连接统一订单服务器的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.connect_url', 'https://inno.xintiandi.com/gorder', '连接统一订单服务器的链接', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_key', '7477cabb-71ec-4372-a484-4c436f787ab8', '连接统一订单服务器的appkey', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_secret', '3EaZQ7LV+MwxGm1mrYbEBWr6x0FbAZjiR7m/I4u1V49E6H1XtZOJwkDIWLAHUOJh6PCwQ5mYdwMlSPh0MXTkuw==', '连接统一订单服务器的appsecret', 0, NULL, 0);

-- AUTHOR: 黄良铭
-- REMARK: 积分所需配置表 及菜单
SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.url','https://inno.xintiandi.com','the point url','0',NULL,NULL);
 
 
-- AUTHOR: 梁燕龙
-- REMARK: 个人中心隐私配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('personal.show.private.flag', 'true', '个人中心是否展示隐私设置', '999929', NULL, '1');

set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://3/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet?systemId=13#/');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://3/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://3/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://3/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://3/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://3/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://3/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://3/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://3/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://3/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://3/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);

-- --------------------- SECTION END ---------------------------------------------------------

