-- 初始化range字段 by st.zheng
update `eh_service_alliances` set `range`=owner_id where owner_type='community';

update `eh_service_alliances` set `range`='all' where owner_type='orgnization';

-- 将原来的审批置为删除 by.st.zheng
update `eh_service_alliance_jump_module` set `signal`=0 where module_name = '审批';

set @eh_service_alliance_jump_id = (select max(id) from eh_service_alliance_jump_module);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `module_name`,`signal`, `module_url`, `parent_id`)
VALUES (@eh_service_alliance_jump_id + 1, '审批', 2,'zl://approval/create?approvalId={}&sourceId={}', '0');

-- 更改菜单 by.st.zheng
UPDATE `eh_web_menus` SET `data_type`='react:/approval-management/approval-list/40500/EhOrganizations' WHERE `id`='40541' and name='审批列表';

--
-- 用户输入内容变量 add by xq.tian  2017/08/05
--
SELECT MAX(id) FROM `eh_flow_variables` INTO @flow_variables_id;
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_msg_input_content', '文本备注内容', 'text_button_msg', 'bean_id', 'flow-variable-text-button-msg-user-input-content', 1);




-- merge from msg-2.1
-- 二维码页面  add by yanjun 20170727
SET @id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanJoin.url','/mobile/static/message/src/addGroup.html','group.scanJoin.url','0',NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanDownload.url','/mobile/static/qq_down/index.html','group.scanDownload.url','0',NULL);

-- 群聊默认名称   add by yanjun 20170801
SET @id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','50','zh_CN','你邀请用户加入群','你邀请${userNameList}加入了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','51','zh_CN','其他用户邀请用户加入群','${inviterName}邀请${userNameList}加入了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','52','zh_CN','被邀请入群','${inviterName}邀请你加入了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','53','zh_CN','退出群聊','${userName}已退出群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','54','zh_CN','退出群聊','群聊名称已修改为“${groupName}”','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','55','zh_CN','删除成员','你将${userNameList}移出了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','56','zh_CN','删除成员','你被${userName}移出了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','57','zh_CN','解散群','你加入的群聊“${groupName}”已解散','0');

SET @id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@id := @id + 1),'group','20001','zh_CN','群聊');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@id := @id + 1),'group','20002','zh_CN','你已成为新群主');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@id := @id + 1),'group','20003','zh_CN','你通过扫描二维码加入了群聊');

-- 活动分享页app信息栏-volgo更新为“工作中的生活态度”  add by yanjun 20170815
UPDATE eh_app_urls set description = '工作中的生活态度' where namespace_id = 1;

-- 更新老数据，以前如果一个地址有人加入后新建group时取的是0域空间。现在要跟新过来，使用实际的域空间  add by yanjun 20170816
update eh_groups set namespace_id = 999994 where id =   179181 and namespace_id =   0 ;
update eh_groups set namespace_id = 999994 where id =   179184 and namespace_id =   0 ;
update eh_groups set namespace_id = 999994 where id =   179185 and namespace_id =   0 ;
update eh_groups set namespace_id = 999994 where id =   179187 and namespace_id =   0 ;
update eh_groups set namespace_id = 999994 where id =   179218 and namespace_id =   0 ;
update eh_groups set namespace_id = 999988 where id =  1004270 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1007151 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1009351 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1010196 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1020489 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1021332 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1041837 and namespace_id =   0 ;
update eh_groups set namespace_id = 999975 where id =  1041838 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1041989 and namespace_id =   0 ;

-- 更新数据，老数据将企业群设置为公有圈，现将其为私有。 add by yanjun 20170817
UPDATE  eh_groups set private_flag = 1 where discriminator = 'enterprise';



-- 黑名单 add by sfyan 20170817
delete from `eh_acl_privileges` where id in (10079, 10090);
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values('30600','黑名单管理','30000','/30000/30600','1','2','2','0',now());
set @service_module_scope_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
insert into `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) values((@service_module_scope_id := @service_module_scope_id + 1), 999983, 30600, 2);
insert into `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) values(10090,'0','black.list.super','黑名单超级权限');
set @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'30600','1','10090','黑名单管理 管理员','0',now());

-- 【嘉定新城】公司内部门禁移到大堂门禁 add by sfyan 20170821
update `eh_door_access` set owner_type = 0,owner_id = 240111044332059749 where owner_type = 1 and owner_id = 1023967;
DELETE FROM `eh_web_menu_scopes` WHERE `owner_id` = 999974 AND `menu_id` in (50800, 50810, 50820, 50830, 50840, 50850, 50860, 508000, 508100, 508200, 508300, 508400, 508500, 508600);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41010,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41020,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41030,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41040,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41050,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41060,'', 'EhNamespaces', 999974,2);



-- 【保集e智谷】 配置服务广场item add by sfyan 20170821
SET @namespace_id = 999973;
SET @parent_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
SET @parent_id = @parent_id + 1;
SET @community_id = 240111044331050393;
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`); 
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '投融资对接', '投融资对接', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '投融资对接', '投融资对接', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'EBOILL_TRZDJ', '投融资对接', 'cs://1/image/aW1hZ2UvTVRwa016SmhPR1JpTXpoalpUSTJNVE00TVdGaE5UUXdZamxrWlRSallUUXpaZw', '1', '1', 33,CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "tab"}'), '15', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');    
	

SET @resource_type_id = (SELECT MAX(id) FROM `eh_rentalv2_resource_types`);
SET @resource_type_id = @resource_type_id + 1;
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES(@resource_type_id, '场地预定', 0, NULL, 2, @namespace_id);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'EBOILL_SITE_RESERVE', '场地预定', 'cs://1/image/aW1hZ2UvTVRvME1tSXlZamd3TW1Zd01EQmtOemcwTkRBeE5tWTFPV05rWkRJMU1qazNNZw', '1', '1', 49,CONCAT('{"resourceTypeId":',@resource_type_id,',"pageType": 0}'), '35', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

UPDATE `eh_launch_pad_items` SET  `delete_flag` = 0 WHERE `item_name` = 'MORE_BUTTON' AND `namespace_id` = @namespace_id;

set @service_module_scope_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
insert into `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) values((@service_module_scope_id := @service_module_scope_id + 1), @namespace_id, 40400, 2);

--【volgo】服务广场配置 add by sfyan 20170822

SET @eh_biz_serverURL = "http://biz.zuolin.com"; -- 取具体环境连接core server的链接
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`); 

UPDATE `eh_launch_pad_layouts` SET `layout_json` = '{"versionCode":"20170822804","versionName":"3.5.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"OPPushBiz","entityCount":6,"subjectHeight":1,"descriptionHeight":0},"style":"HorizontalScrollView","defaultOrder":1,"separatorFlag":1,"separatorHeight":16,"columnCount":0},{"groupName":"","widget":"Navigator","instanceConfig":{"cssStyleFlag":1,"itemGroup":"Bizs1","paddingTop":1,"paddingLeft":0,"paddingBottom":0,"paddingRight":0,"lineSpacing":1,"columnSpacing":1,"backgroundColor":"#EFEFF4"},"style":"Gallery","defaultOrder":5,"columnCount":2,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"OPPushActivity","entityCount":3,"subjectHeight":1,"descriptionHeight":0},"style":"ListView","defaultOrder":6,"separatorFlag":0,"separatorHeight":0,"columnCount":1}]}', version_code = "20170822804" WHERE `namespace_id` = 1 AND `name` = 'ServiceMarketLayout';

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),'1','0','0','0','/home','Bizs1','NewArrivals','周边商家','cs://1/image/aW1hZ2UvTVRwa01EQTVaak13WW1FNFpEYzVORGcyWVdFek56ZzJObUpsTm1FMk5XRmpaQQ
','1','1','13',CONCAT('{"url":"',@eh_biz_serverURL,'/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3fisfromindex%3d0%23%2fmicroshop%2fhome%3f_k%3dzlbiz#sign_suffix"}'),'1','0','1','1',NULL,'0',NULL,NULL,NULL,'1','park_tourist','1',NULL,NULL,'2',NULL);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),'1','0','0','0','/home','Bizs1','NewArrivals','周边商家','cs://1/image/aW1hZ2UvTVRwa01EQTVaak13WW1FNFpEYzVORGcyWVdFek56ZzJObUpsTm1FMk5XRmpaQQ
','1','1','13',CONCAT('{"url":"',@eh_biz_serverURL,'/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3fisfromindex%3d0%23%2fmicroshop%2fhome%3f_k%3dzlbiz#sign_suffix"}'),'1','0','1','1',NULL,'0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'2',NULL);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),'1','0','0','0','/home','Bizs1','GasStation','能量加油站','cs://1/image/aW1hZ2UvTVRveVlqVXlPRGhsTlRVM05tWTROVGt6WlRZMllXVTRaamsyTldVMU9EUmlOdw','1','1','13',CONCAT('{"url":"',@eh_biz_serverURL,'/zl-ec/rest/service/front/logon?hideNavigationBar=1&mallId=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%23%2fstore%2fdetails%2f14803348590903554653%3F_k%3Dzlbiz#sign_suffix"}'),'2','0','1','1',NULL,'0',NULL,NULL,NULL,'1','park_tourist','1',NULL,NULL,'2',NULL);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),'1','0','0','0','/home','Bizs1','GasStation','能量加油站','cs://1/image/aW1hZ2UvTVRveVlqVXlPRGhsTlRVM05tWTROVGt6WlRZMllXVTRaamsyTldVMU9EUmlOdw','1','1','13',CONCAT('{"url":"',@eh_biz_serverURL,'/zl-ec/rest/service/front/logon?hideNavigationBar=1&mallId=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%23%2fstore%2fdetails%2f14803348590903554653%3F_k%3Dzlbiz#sign_suffix"}'),'2','0','1','1',NULL,'0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'2',NULL);

UPDATE `eh_launch_pad_items` SET action_data = CONCAT('{"url":"',@eh_biz_serverURL,'/zl-ec/rest/service/front/logon?hideNavigationBar=1&mallId=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%3Fclientrecommend%3D1%23%2Frecommend%3F_k%3Dzlbiz#sign_suffix"}') WHERE item_name = 'Biz' AND item_group = 'OPPushBiz';

-- 【星商汇园区】模块配置 add by sfyan 20170823
SET @namespace_id = 999981;
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
insert into `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) values((@service_module_scope_id := @service_module_scope_id + 1), @namespace_id, 35000, 2);   
insert into `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) values((@service_module_scope_id := @service_module_scope_id + 1), @namespace_id, 40400, 2);   
insert into `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) values((@service_module_scope_id := @service_module_scope_id + 1), @namespace_id, 40500, 2);   
insert into `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) values((@service_module_scope_id := @service_module_scope_id + 1), @namespace_id, 41300, 2);
insert into `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) values((@service_module_scope_id := @service_module_scope_id + 1), @namespace_id, 41100, 2);

-- 【昌智汇】服务联盟配置 add by sfyan 20170822
SET @namespace_id = 999969;
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`); 
SET @parent_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
SET @community_id = 240111044331050362;
SET @skip_rule_id = IFNULL((SELECT MAX(id) FROM `eh_service_alliance_skip_rule`), 1);
UPDATE `eh_launch_pad_items` SET item_label = '服务体系' WHERE item_name = 'SERVICE_ALLIANCE' AND `namespace_id` = @namespace_id;
UPDATE `eh_launch_pad_items` SET item_label = '回+青创汇' WHERE item_name = 'MAKER_SPACE' AND `namespace_id` = @namespace_id;
UPDATE `eh_service_alliance_categories` SET `name` = '服务体系' WHERE `parent_id` = 0 AND `namespace_id` = @namespace_id AND `name` = '服务联盟';
UPDATE `eh_service_alliances` SET `name` = '服务体系' WHERE `owner_type` = 'community' AND `owner_id` = @community_id AND `name` = '服务联盟';

SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '智慧政申', '智慧政申', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '智慧政申', '智慧政申', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@skip_rule_id := @skip_rule_id + 1), @namespace_id, @parent_id);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','SmartApplication','智慧政申','cs://1/image/aW1hZ2UvTVRveE5qZGpNakExTkRoaU9EUXdPRGcyTURWa01UTmtNemczTWpOaE9UUTFNUQ
','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'5','0','1','0',NULL,'0',NULL,NULL,NULL,'1','park_tourist','1',NULL,NULL,'31',NULL);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','SmartApplication','智慧政申','cs://1/image/aW1hZ2UvTVRveE5qZGpNakExTkRoaU9EUXdPRGcyTURWa01UTmtNemczTWpOaE9UUTFNUQ
','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'5','0','1','0',NULL,'0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'31',NULL);


SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '科技金融服务', '科技金融服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '科技金融服务', '科技金融服务', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@skip_rule_id := @skip_rule_id + 1), @namespace_id, @parent_id);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','TechnoFinance','科技金融服务','cs://1/image/aW1hZ2UvTVRveFpUSTFaVFF5TkRRME1XRTVNalUzWVRGalkyUmtaV00yWmpnMU5tVmhPUQ
','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'6','0','1','0',NULL,'0',NULL,NULL,NULL,'1','park_tourist','1',NULL,NULL,'31',NULL);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','TechnoFinance','科技金融服务','cs://1/image/aW1hZ2UvTVRveFpUSTFaVFF5TkRRME1XRTVNalUzWVRGalkyUmtaV00yWmpnMU5tVmhPUQ
','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'6','0','1','0',NULL,'0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'31',NULL);


SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '技术创新业务', '技术创新业务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '技术创新业务', '技术创新业务', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@skip_rule_id := @skip_rule_id + 1), @namespace_id, @parent_id);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','TechnoInnocation','技术创新业务','cs://1/image/aW1hZ2UvTVRwa09UVXpPVGMxTURnMFlUYzNPRGt5TUdSbE5UVTJZVEprTkdRek1qUTNNUQ
','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'7','0','1','0',NULL,'0',NULL,NULL,NULL,'1','park_tourist','1',NULL,NULL,'31',NULL);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','TechnoInnocation','技术创新业务','cs://1/image/aW1hZ2UvTVRwa09UVXpPVGMxTURnMFlUYzNPRGt5TUdSbE5UVTJZVEprTkdRek1qUTNNUQ
','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'7','0','1','0',NULL,'0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'31',NULL);


SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '人才关爱', '人才关爱', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '人才关爱', '人才关爱', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@skip_rule_id := @skip_rule_id + 1), @namespace_id, @parent_id);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','CareForTalent','人才关爱','cs://1/image/aW1hZ2UvTVRwak5qQTROemRoTURFME9EWTRZVE5rWm1abFpEbG1NVEUyTldZMVpEZ3dOQQ','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'8','0','1','0',NULL,'0',NULL,NULL,NULL,'1','park_tourist','1',NULL,NULL,'31',NULL);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','CareForTalent','人才关爱','cs://1/image/aW1hZ2UvTVRwak5qQTROemRoTURFME9EWTRZVE5rWm1abFpEbG1NVEUyTldZMVpEZ3dOQQ','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'8','0','1','0',NULL,'0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'31',NULL);


SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '特色产业服务', '特色产业服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '特色产业服务', '特色产业服务', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@skip_rule_id := @skip_rule_id + 1), @namespace_id, @parent_id);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','InnovaMarkeService','特色产业服务','cs://1/image/aW1hZ2UvTVRwaFpHTTVOVEprT1RGbE5HWTRNREpsTm1WbE9USTJOV0ZpWVRBMllqa3pOQQ
','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'9','0','1','0',NULL,'0',NULL,NULL,NULL,'1','park_tourist','1',NULL,NULL,'31',NULL);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','InnovaMarkeService','特色产业服务','cs://1/image/aW1hZ2UvTVRwaFpHTTVOVEprT1RGbE5HWTRNREpsTm1WbE9USTJOV0ZpWVRBMllqa3pOQQ','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'9','0','1','0',NULL,'0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'31',NULL);

SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '国际交流', '国际交流', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '国际交流', '国际交流', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@skip_rule_id := @skip_rule_id + 1), @namespace_id, @parent_id);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','InterCommu','国际交流','cs://1/image/aW1hZ2UvTVRveFpHRXhNelV4T0Rka01XWTVNV1ZpTlRNek9HWTVZamN6T1dRME1XUTJZZw','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'10','0','1','0',NULL,'0',NULL,NULL,NULL,'1','park_tourist','1',NULL,NULL,'31',NULL);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@item_id := @item_id + 1),@namespace_id,'0','0','0','/home','Bizs','InterCommu','国际交流','cs://1/image/aW1hZ2UvTVRveFpHRXhNelV4T0Rka01XWTVNV1ZpTlRNek9HWTVZamN6T1dRME1XUTJZZw','1','1','33',CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'),'10','0','1','0',NULL,'0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'31',NULL);


-- 【光大we谷】 服务联盟配置 add by sfyan 20170823
SET @namespace_id = 999979;
SET @parent_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
SET @community_id = 240111044331056800;
SET @skip_rule_id = IFNULL((SELECT MAX(id) FROM `eh_service_alliance_skip_rule`), 1);

SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '人才公寓', '人才公寓', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '人才公寓', '人才公寓', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@skip_rule_id := @skip_rule_id + 1), @namespace_id, @parent_id);
UPDATE `eh_launch_pad_items` SET action_type = 33, action_data = CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}') WHERE item_name = '人才公寓' AND `namespace_id` = @namespace_id;


SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '周边租房', '周边租房', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '周边租房', '周边租房', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@skip_rule_id := @skip_rule_id + 1), @namespace_id, @parent_id);
UPDATE `eh_launch_pad_items` SET action_type = 33, action_data = CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}') WHERE item_name = '周边租房' AND `namespace_id` = @namespace_id;


SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '松山湖酒店', '松山湖酒店', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', @community_id, '松山湖酒店', '松山湖酒店', @parent_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@skip_rule_id := @skip_rule_id + 1), @namespace_id, @parent_id);
UPDATE `eh_launch_pad_items` SET action_type = 33, action_data = CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}') WHERE item_name = '松山湖酒店' AND `namespace_id` = @namespace_id;












