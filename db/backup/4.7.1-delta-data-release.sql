-- by dengs,司印服务器ip地址,二维码时间，默认打印价格，生成打印二维码的url 20170615
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.server.url', 'http://siyin.zuolin.com:8119', '司印服务器ip地址', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.timeout', '10', '二维码的identifierToken在redis存在的时间', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.timeout.unit', 'MINUTES', '秒 SECONDS/分 MINUTES/小时 HOURS', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.default.price', '0.1', '打印默认价格', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.inform.url', 'http://core.zuolin.com/evh/siyinprint/informPrint', '二维码url地址', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.logon.scan.timout', '10000', '二维码是否被扫描检测的延迟时间,单位毫秒', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.pattern', '1', '1:司印方配置成不解锁打印机，直接打印的模式，2:司印方配置成发送文档到打印机，需要解锁再打印的模式。', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.job.count.timeout', '10', '用户正在打印任务数量放到redis中的，设置一个默认超时时间 10 单位分钟', '0', NULL);

-- by dengs,默认打印教程、默认复印/扫描教程 20170615
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'print_course_list', 'zh_CN', '在电脑上打开需要打印的文档，点击打印，\n 设置好打印参数，点击确定，电脑上出现二维码 | 打开APP，点击右上角扫一扫按钮，或者进入云打印界面，\n 点击右上角扫一扫按钮，扫描电脑出现的二维码 | APP上出现提示，询问是否立即打印，点击立即打印，\n 打印任务将发送给打印机，等待打印完成即可去打印机上\n 取回打印资料（为了确保打印的资料不被他人取走，您可以\n 走到打印机前再点击“立即打印”） | 打印完成后，APP端生成一个待付款的订单，您可以立即\n 支付，也可以等待下次打印时再进行支付（注意每次打印\n 必须保证上次的订单已付款，否则无法进行打印）\n ');
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'scan_copy_course_list', 'zh_CN', '准备好需要复印/扫描的资料，前往打印机处 |  打开APP，进入云打印界面，点击右上角扫一扫按钮，\n扫描打印机上的二维码，填写扫描件接受邮箱 |按照打印机上的指引进行操作，点击扫描/复印，进行\n扫描/复印参数设置，设置好参数进行对应的操作即可 |扫描/复印完成后，点击打印机屏幕右上角的登出按钮\n结束任务（或者直接离开，系统自动超时结束任务）|APP端生成待付款的订单，您可以立即支付，\n也可以等待下次扫描/复印时再进行支付\n（注意每次扫描/复印前必须保证上次的订单已付款，否则无法进行扫描/复印）');
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'print_subject', 'zh_CN', '打印订单');
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'print_surface', 'zh_CN', '面');

-- by dengs,添加打印机信息，目前只添加了公司的打印机 20170615
select IFNULL(max(id),-1) into @id from eh_siyin_print_printers;
INSERT INTO `eh_siyin_print_printers` (`id`, `namespace_id`, `owner_type`, `owner_id`, `reader_name`, `module_port`, `login_context`, `trademark`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, '0', NULL, NULL, 'TC101154727022', '8119', '/xeroxmfp/directLogin', '施乐', '2', NULL, NULL, NULL, NULL);

-- by dengs,云打印菜单配置 20170626
-- 添加菜单 -- 按照产品要求，添加菜单到左邻域

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `category`) 
VALUES (41400, '云打印', '40000', NULL, NULL, '1', '2', '/40000/41400', 'park', '499', 41400, 'module');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `category`) 
VALUES (41410, '打印记录', 41400, NULL, 'react:/cloud-print/record', '0', '2', '/40000/41400/41410', 'park', '500', 41400, 'module');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `category`) 
VALUES (41420, '打印统计', 41400, NULL, 'react:/cloud-print/count', '0', '2', '/40000/41400/41420', 'park', '501', 41400, 'module');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `category`) 
VALUES (41430, '打印价格', 41400, NULL, 'react:/cloud-print/setting', '0', '2', '/40000/41400/41430', 'park', '502', 41400, 'module');

-- 添加权限云打印
set @eh_acl_privileges_id = (select max(id) from eh_acl_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@eh_acl_privileges_id := @eh_acl_privileges_id+1), 0, '云打印', '云打印 全部权限', NULL);

-- 菜单对应的权限
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41400, '云打印', 1, 1, '云打印  全部权限', 499);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41410, '打印记录', 1, 1, '打印记录  全部权限', 500);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41420, '打印统计', 1, 1, '打印统计  全部权限', 501);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41430, '打印价格', 1, 1, '打印价格  全部权限', 502);

-- 角色对应的菜单权限
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @eh_acl_privileges_id, 1001, 'EhAclRoles', 0, 1, NOW());

-- 菜单的范围 --volog中
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41400, '', 'EhNamespaces', 1, 2);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41410, '', 'EhNamespaces', 1, 2);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41420, '', 'EhNamespaces', 1, 2);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41430, '', 'EhNamespaces', 1, 2);

-- 模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES (41400, '云打印', 40000, '/40000/41400', 0, 2, 2, 0, UTC_TIMESTAMP());

-- 模块权限
SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41400, 1, @eh_acl_privileges_id, NULL, '0', UTC_TIMESTAMP());

-- 模块权限范围
SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 0, 41400, '云打印', NULL, NULL, NULL, 2);

-- by dengs 添加菜单 end


-- merge from activty-3.2.0  by yanjun 20170710 start


-- 活动公众号报名使用新的页面，并且增加默认公众号。生成公众号支付的订单接口   add by yanjun 20170620
UPDATE eh_configurations SET VALUE = '/share-activity/build/index.html#/detail' WHERE NAME = 'activity.share.url';
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'wx.offical.account.appid','wx5db5ebf00a251407','默认公众号开发者AppId-左邻平台','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'wx.offical.account.secret','cc0a483e3f50a14ed795d7ebea947f4c','默认公众号开发者AppId-左邻平台','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'pay.zuolin.wechatJs','POST /EDS_PAY/rest/pay_common/payInfo_record/createWechatJsPayOrder','生成公众号订单的api','0',NULL);

-- 生成公众号订单异常信息
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@eh_locale_strings_id := @eh_locale_strings_id + 1),'activity','10026','zh_CN','生成公众号订单异常');

-- merge from activty-3.2.0  by yanjun 20170710 end

-- 园区入驻1.3 add by sw 20170711
UPDATE eh_news set visible_type = 'ALL';
SET @id = (SELECT IFNULL(MAX(id),1) FROM `eh_news_communities`);
INSERT INTO `eh_news_communities` (`id`, `news_id`, `community_id`, `creator_uid`, `create_time`) 
	SELECT (@id := @id + 1), eh_news.id, community_id, 1, NOW() from eh_organization_communities join eh_news on eh_news.owner_id = eh_organization_communities.organization_id;
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) 
	VALUES ('news', '10009', 'zh_CN', '请选择范围');
UPDATE eh_web_menus set `name` = '园区快讯' where id = 10800;

-- merge from pmtaskprivilege by xiongying 20170711
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20140,'任务列表',20100,'/20000/20100/20140','1','3','2','0',NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20150,'服务录入',20100,'/20000/20100/20150','1','3','2','0',NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20155,'设置',20100,'/20000/20100/20155','1','3','0','0',NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(20190,'统计',20100,'/20000/20100/20190','1','3','2','0',NOW());

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '20100', '1', '10008', '物业报修管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '20100', '2', '10008', '物业报修全部权限', '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30090, '0', '物业报修 任务查看权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20140','0',30090,'物业报修 任务查看权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30091, '0', '物业报修 服务录入代发权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20150','0',30091,'物业报修 服务录入代发权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30092, '0', '物业报修 新增服务类型权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20155','0',30092,'物业报修 新增服务类型权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30093, '0', '物业报修 删除服务类型权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20155','0',30093,'物业报修 删除服务类型权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30094, '0', '物业报修 新增分类权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20155','0',30094,'物业报修 新增分类权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30095, '0', '物业报修 删除分类权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20155','0',30095,'物业报修 删除分类权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30096, '0', '物业报修 服务统计查看权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20190','0',30096,'物业报修 服务统计查看权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30097, '0', '物业报修 查看全部项目报表权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20190','0',30097,'物业报修 查看全部项目报表权限','0',NOW());   

-- 新配置wifi菜单 add by sfyan 20170713
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41100,'', 'EhNamespaces', 999974,2);	
    

-- 更换web页面
update eh_web_menus  set data_type = 'react:/repair-management/task-list' where id  = 20210;
update eh_web_menus  set data_type = 'react:/repair-management/task-list/202564' where id  = 20240;

-- 资源预约 add by sw 20170711
UPDATE eh_rentalv2_resources set default_order = id;

-- 重新同步user_organization表
DELETE FROM eh_user_organizations;
SET @user_organization_id = 0;
INSERT INTO eh_user_organizations (
id,
namespace_id,
user_id,
organization_id,
status,
group_type,
group_path,
create_time,
update_time,
visible_flag
) SELECT
(@user_organization_id := @user_organization_id + 1),
ifnull(eom.namespace_id, 0),
eom.target_id,
eom.organization_id,
eom.status,
eom.group_type,
eom.group_path,
eom.create_time,
eom.update_time,
eom.visible_flag
FROM
eh_organization_members eom
LEFT JOIN eh_organizations eo ON eom.organization_id = eo.id
WHERE
eom.group_type = 'ENTERPRISE'
AND
eom.target_type = 'USER'
GROUP BY
eom.organization_id,
eom.contact_token
ORDER BY
eom.id;


-- 补充紫荆数据 add by sfyan 20170714
SET @namespace_resource_id = (select max(id) from eh_namespace_resources) + 1; 
SET @community_id = (select id from eh_communities where name = '南海科技园' and namespace_id = 999984); 
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(@namespace_resource_id, 999984, 'COMMUNITY', @community_id, UTC_TIMESTAMP());

-- 科兴场地预约支付模式修改 add by sfyan 20170714
update `eh_rentalv2_resource_types` set pay_mode = 1 where name = '场地预约' and namespace_id = 999983;
update `eh_launch_pad_items` set action_data = replace(action_data, '"payMode":2', '"payMode":1') where item_label = '场地预约' and namespace_id = 999983;
	
-- 荣超股份增加考勤统计菜单 add by sfyan 20170714
SET @menu_scope_id = (SELECT max(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1),50660,'', 'EhNamespaces', 999975,2);	
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1),506600,'', 'EhNamespaces', 999975,2);	
	
-- 康利item url修改 add by sfyan 20170714
update `eh_launch_pad_items` set `action_data` = '{"url":"http://alpha.vrbrowserextern.bqlnv.com.cn/vreditor/view/64997823126520945"}' where `item_label` = '园区3D图' and namespace_id = 999978;
	
-- 添加id为0的用户 add by sw 20170714
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (0, UUID(), 269862, 'ANNONYMOUS_LOGIN', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 0);

