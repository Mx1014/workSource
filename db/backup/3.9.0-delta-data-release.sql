-- merge from community-import-2.0.0-delta-data-debug.sql 20160826
-- 升级使用的url
set @configuration_id = (select max(id) from eh_configurations) +1;
INSERT INTO `eh_configurations` VALUES (@configuration_id, 'upgrade.url', '/management/views/upgrade.html', 'upgrade url', 0, NULL);


-- merge from organization-delta-data-release.sql 20160826
-- 支持多部门，需要队之前的数据模型进行处理  by sfyan 20160812
SET @organization_member_id = (SELECT MAX(`id`) FROM `eh_organization_members`);
INSERT INTO `eh_organization_members` (`id`,`organization_id`,`target_type`,`target_id`,`member_group`,`contact_name`,`contact_type`,`contact_token`,`contact_description`,`status`,`employee_no`,`avatar`,`group_id`,`group_path`,`gender`,`update_time`,`create_time`,`integral_tag1`,`integral_tag2`,`integral_tag3`,`integral_tag4`,`integral_tag5`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`namespace_id`)
SELECT (@organization_member_id := @organization_member_id + 1),`group_id`,`target_type`,`target_id`,`member_group`,`contact_name`,`contact_type`,`contact_token`,`contact_description`,`status`,`employee_no`,`avatar`,0,`group_path`,`gender`,`update_time`,`create_time`,`integral_tag1`,`integral_tag2`,`integral_tag3`,`integral_tag4`,`integral_tag5`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`namespace_id` FROM `eh_organization_members` WHERE `group_id` IS NOT NULL AND `group_id` != 0;

-- 保证数据处理完后 再修改 之前的数据
UPDATE `eh_organization_members` SET `group_id` = 0 WHERE `group_id` IS NOT NULL AND `group_id` != 0;




-- 物业报修2.0 (由于web还没开发完且没经过测试，3.9.0先不上线 by lqs)
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);

-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
--   VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
--   VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');  

-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
--   VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', 'PM_TASK', '任务2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 51, '', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
--   VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', 'PM_TASK', '任务2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 51, '', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');  

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'default');   


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('150', 'pmtask.category.ancestor', '任务', '任务分类名称', '0', NULL);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('180', 'pmtask.notification', '1', 'zh_CN', '任务操作模版', '任务已生成，${operatorName} ${operatorPhone}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('181', 'pmtask.notification', '2', 'zh_CN', '任务操作模版', '已派单，${operatorName} ${operatorPhone} 已将任务分配给了 ${targetName} ${targetPhone}，将会很快联系您。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('182', 'pmtask.notification', '3', 'zh_CN', '任务操作模版', '已完成，${operatorName} ${operatorPhone} 已完成该单，稍后我们将进行回访。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('183', 'pmtask.notification', '4', 'zh_CN', '任务操作模版', '您的任务已被 ${operatorName} ${operatorPhone} 关闭', '0');

INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES ('6', '0', '0', '任务', '任务', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '999992');


INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (250, 'pmtask', '10001', 'zh_CN', '任务分类已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (251, 'pmtask', '10002', 'zh_CN', '服务类型不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (252, 'pmtask', '10003', 'zh_CN', '目标用户不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (253, 'pmtask', '10004', 'zh_CN', '内容不能为空');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (280, 'pmtask', '10005', 'zh_CN', '服务类型已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (281, 'pmtask', '10006', 'zh_CN', '服务类型不存在');

-- 物业保修2.0的菜单权限 by sunwen 20160830
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (24000,'任务列表',20000,NULL,'task_management_list',0,2,'/20000/24000','park',456);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (25000,'服务类型设置',20000,NULL,'service_type_setting',0,2,'/20000/25000','park',457);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (26000,'分类设置',20000,NULL,'classify_setting',0,2,'/20000/26000','park',458);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (27000,'统计',20000,NULL,'task_statistics',0,2,'/20000/27000','park',459);

  
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (904,0,'任务管理 任务列表','任务管理 任务列表 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (905,0,'任务管理 服务类型设置','任务管理 服务类型设置 全部权限',NULL);
  
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('331', '0', '分派任务', '分派任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('332', '0', '完成任务', '完成任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('333', '0', '关闭任务', '关闭任务', NULL);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (906,0,'任务管理 分类设置','任务管理 分类设置 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (907,0,'任务管理 统计','任务管理 统计 全部权限',NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),904,24000,'任务列表',1,1,'任务列表 全部权限',603);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),331,24000,'分派任务',0,1,'分派任务 全部权限',604);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),332,24000,'完成任务',0,1,'完成任务 全部权限',605);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),333,24000,'关闭任务',0,1,'关闭任务 全部权限',606);
  
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),905,25000,'服务类型设置',1,1,'服务类型设置 全部权限',604);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),906,26000,'分类设置',1,1,'分类设置 全部权限',605);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),907,27000,'统计',1,1,'统计 全部权限',606);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%20000/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1002,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%20000/%');

-- merge from  serviceAlliance3.0-delta-data-release.sql 20160826
-- 迁移yellowpage中的数据
INSERT INTO `eh_service_alliances` (`id`,`parent_id`,`owner_type`,`owner_id`,`name`,`display_name`,`type`,`address`,`contact`,`description`,`poster_uri`,`status`,`default_order`,`longitude`,`latitude`,`geohash`,`discount`,`category_id`,`contact_name`,`contact_mobile`,`service_type`,`service_url`,`discount_desc`,`creator_uid`,`create_time`)
SELECT `id`,`parent_id`,`owner_type`,`owner_id`,`name`,`nick_name`,`type`,`address`,`contact`,`description`,`poster_uri`,`status`,`default_order`,`longitude`,`latitude`,`geohash`,`integral_tag1`,`integral_tag2`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`creator_uid`,`create_time` FROM `eh_yellow_pages` WHERE `type` = 2;

-- 迁移yellowpage attachment中的数据
INSERT INTO `eh_service_alliance_attachments` (`id`,`owner_id`,`content_type`,`content_uri`,`creator_uid`,`create_time`)
SELECT `id`,`owner_id`,`content_type`,`content_uri`,`creator_uid`,`create_time` FROM `eh_yellow_page_attachments`;


INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('11', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '1000000');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('12', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '999990');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('13', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '999999');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('14', '0', '政府资源类型', '政府资源类型', '2', '1', UTC_TIMESTAMP(), '999999');
    

update eh_service_alliances set type = 11 where owner_id = 240111044331048623;
update eh_service_alliances set type = 12 where owner_id = 240111044331051500;
update eh_service_alliances set type = 13 where owner_id = 240111044331049963;

INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
SELECT `id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id` FROM `eh_categories` WHERE `parent_id` = 100001;

update eh_service_alliance_categories SET owner_type = 'community';
update eh_service_alliance_categories SET owner_id = 240111044331048623 WHERE namespace_id = 1000000;
update eh_service_alliance_categories SET parent_id = 11 WHERE namespace_id = 1000000 and parent_id = 100001;
update eh_service_alliance_categories SET owner_id = 240111044331051500 WHERE namespace_id = 999990;
update eh_service_alliance_categories SET parent_id = 12 WHERE namespace_id = 999990 and parent_id = 100001;
update eh_service_alliance_categories SET owner_id = 240111044331049963 WHERE namespace_id = 999999;
update eh_service_alliance_categories SET parent_id = 13 WHERE namespace_id = 999999 and parent_id = 100001;

update eh_launch_pad_items set action_data = '{"type":11,"parentId":11}' where action_type = 33 and namespace_id = 1000000;
update eh_launch_pad_items set action_data = '{"type":12,"parentId":12}' where action_type = 33 and namespace_id = 999990;
update eh_launch_pad_items set action_data = '{"type":13,"parentId":13}' where action_type = 33 and namespace_id = 999999;


-- merge from videoconf3.0-delta-data-release.sql 20160829
update `eh_locale_strings` set `text` = "抱歉您当前不可更换此账号（最短更换频率为7天）" where `scope` = "videoConf" and `code` = "10005";

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('videoConf', '10010', 'zh_CN', '公司不存在或已删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('videoConf', '10011', 'zh_CN', '未过期状态不可删除');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '500001', 'zh_CN', '该域下该公司已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '500002', 'zh_CN', '公司类型错误，只能为普通公司或物业公司');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '500003', 'zh_CN', '手机号只能为11位的数字');


-- 整理公司内部管理菜单 by sfyan 20160827
DELETE FROM `eh_web_menus` WHERE `id` IN (51000, 51100, 52000, 52100, 52200, 52300, 54000, 54100);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51000,'组织架构管理',50000,null,null,1,2,'/50000/51000','park',510);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51100,'组织架构管理',51000,null,'architecture_management',0,2,'/50000/51000/51100','park',511);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52000,'角色管理',50000,null,null,1,2,'/50000/52000','park',520);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52100,'管理员管理',52000,null,'admin_management',0,2,'/50000/52000/52100','park',521);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52200,'角色与权限管理',52000,null,'roles_management',0,2,'/50000/52000/52200','park',522);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52300,'成员管理',52000,null,'personal_management',0,2,'/50000/52000/52300','park',523);

DELETE FROM `eh_acl_privileges` WHERE `id` >= 600 AND `id` <= 615;
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (600,0,'查看子公司列表信息','查看子公司列表信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (601,0,'增删改分公司','增删改分公司',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (604,0,'查看管理员信息','查看管理员信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (605,0,'管理员的增删改','管理员的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (606,0,'查看角色与权限','查看角色与权限',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (607,0,'角色与权限的增删改','角色与权限的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (608,0,'成员列表','成员列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (609,0,'成员的增删改','成员的增删改',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (610,0,'设置角色','设置角色',null);


DELETE FROM `eh_web_menu_privileges` WHERE `id` >= 46 AND `id` <= 61;

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (46,600,51100,'组织架构列表',1,1,'查看组织架构列表',410);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (47,601,51100,'增删改组织架构',0,1,'增删改组织架构',420);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (48,604,52100,'查看管理员信息',1,1,'查看管理员信息',430);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (49,605,52100,'管理员的增删改',0,1,'管理员的增删改',440);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (50,606,52200,'查看角色与权限',1,1,'角色与权限查看',450);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (51,607,52200,'角色与权限的增删改',0,1,'角色与权限的增删改',460);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (52,608,52300,'成员列表',1,1,'成员列表查看',470);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (53,609,52300,'成员的增删改',0,1,'成员的增删改',480);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (54,610,52300,'设置角色',0,1,'设置角色',490);

-- 增加结算服务配置 by sfyan 20160829
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (1, 1000000, 'EhOrganizations', 1000001, 'parking_recharge','停车充值', 1, now());
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (2, 1000000, 'EhOrganizations', 1000001, 'pmsy','物业缴费', 1, now());
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (3, 1000000, 'EhOrganizations', 1000001, 'payment_card','一卡通', 1, now());


-- 屏蔽结算菜单 by sfyan 20160829 
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49000,'', 'EhNamespaces', 999989 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49000,'', 'EhNamespaces', 999990 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49000,'', 'EhNamespaces', 999993 , 0);

-- 海岸屏蔽场所预定
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 42000,'', 'EhNamespaces', 999993 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%42000/%';

-- 海岸取消资源预定的屏蔽
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 43400 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999993;
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` IN (SELECT `id`  FROM `eh_web_menus` WHERE `path` LIKE '%43400/%') AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999993;



-- fix bug 1184
update eh_activities a set signup_attendee_count = (select count(id) from eh_activity_roster where activity_id = a.id)


-- 储能交流大厅改官方活动
update eh_launch_pad_items set item_name = '园区活动' where id in(10617, 10635);
update eh_launch_pad_items set item_label = '园区活动' where id in(10617, 10635);
update eh_launch_pad_items set action_type = '50' where id in(10617, 10635);
update eh_launch_pad_items set action_data = '' where id in(10617, 10635);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMk9ETTNZMk14TVRFeVl6bGlZek5pTVRnNFlUQXpOV0ZrWWpabE4yVXpOUQ' where id in(10617, 10635);
-- delete from eh_launch_pad_items where id in(10613, 10631, 10614, 10632);


-- 海岸取消服务热线的屏蔽
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 46000 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999993;

-- 海岸会议室预约和广告租赁

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES(10,'会议室预约','0',NULL,'0', 999993);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES(11,'广告租赁','0',NULL,'0', 999993);
    
update eh_launch_pad_items set action_type = 49 where id in(1768, 1769, 1773, 1774);
update eh_launch_pad_items set action_data = '{"resourceTypeId":10,"pageType":0}' where id in(1768, 1769);
update eh_launch_pad_items set action_data = '{"resourceTypeId":11,"pageType":0}' where id in(1773, 1774);


-- 增加考勤统计菜单 by sfyan 20160822
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56106,'考勤统计',56100,null,null,1,2,'/50000/56000/56100/56106','park',571);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56111,'考勤统计',56106,null,'punch_statistics',0,2,'/50000/56000/56100/56106/56111','park',572);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56112,'打卡详情',56106,null,'punch_detail',0,2,'/50000/56000/56100/56106/56112','park',573);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (820,0,'考勤统计','考勤统计',null);


set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),820,56111,'考勤统计',1,1,'考勤统计  全部权限',577);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),797,56112,'打卡详情',1,1,'打卡详情 全部权限',578);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56106/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1002,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56106/%');


INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56106/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56106/%');

-- 更新系统小助手、电商小助手默认头像（图片已经上传到alpha/beta/release） by lqs 20160831
UPDATE `eh_users` SET `avatar`='cs://1/image/aW1hZ2UvTVRwbE1UY3lOVFk0TVRZNU5HTXlPR014TVRSbU1UTTJNems1TmpVNE5UZzNZZw' WHERE `id`=2;
UPDATE `eh_users` SET `avatar`='cs://1/image/aW1hZ2UvTVRvNE0yWXdOVE15TlRJeE5UZzVPVFl3TjJFek5EZGpZemN4TURJMllUa3lZZw' WHERE `id`=3;



-- 更新讯美服务市场 by lqs 20160831
-- 创客空间layout
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (141, 999999, 'MakerLayout', '{"versionCode":"2016083102","versionName":"3.9.0","layoutName":"MakerLayout","displayName":"创客空间","groups":[{"groupName":"创客banner","widget":"Navigator","instanceConfig":{"itemGroup":"MakerBanners"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"创客空间","widget":"Navigator","instanceConfig":{"itemGroup":"MakerSpaces"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":3}]}', '2016083102', '0', '2', '2016-08-31 13:40:30', 'pm_admin');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (142, 999999, 'MakerLayout', '{"versionCode":"2016083102","versionName":"3.9.0","layoutName":"MakerLayout","displayName":"创客空间","groups":[{"groupName":"创客banner","widget":"Navigator","instanceConfig":{"itemGroup":"MakerBanners"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"创客空间","widget":"Navigator","instanceConfig":{"itemGroup":"MakerSpaces"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":3}]}', '2016083102', '0', '2', '2016-08-31 13:40:30', 'park_tourist');

-- 删除掉Bizs中的免费wifi/
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (904, 911) AND `namespace_id`=999999;
-- 视频会议移到更多里面
UPDATE `eh_launch_pad_items` SET `display_flag`=0 WHERE `id` IN (902, 909);
	
-- 把便捷生活里面的两个店铺复制一份到广场上（齐彩网->办公采购、楼下Leisure ade->饮品）
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (16001, 999999, '0', '0', '0', '/home', 'Bizs', 'RoutineBus', '通勤巴士', 'cs://1/image/aW1hZ2UvTVRvd056azNPVFkxWkRJMFpUZzFNR1JtWVRSak9EVTVaRFU0WkdSak1XSXdNQQ', '1', '1', '14', '{\"url\":\"http://wx.dudubashi.com\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (16002, 999999, '0', '0', '0', '/home', 'Bizs', 'RoutineBus', '通勤巴士', 'cs://1/image/aW1hZ2UvTVRvd056azNPVFkxWkRJMFpUZzFNR1JtWVRSak9EVTVaRFU0WkdSak1XSXdNQQ', '1', '1', '14', '{\"url\":\"http://wx.dudubashi.com\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, scene_type) 
	VALUES (16003, 999999, 0, '0', 0, '/home', 'Bizs', 'OfficePurchase', '办公采购', 'shop/3010/1471502227046-2040368847.jpg', 1, 1, 14, '', 0, 0, 1, 1, '', 0, NULL, 'biz', 105, 'park_tourist');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, scene_type) 
	VALUES (16004, 999999, 0, '0', 0, '/home', 'Bizs', 'OfficePurchase', '办公采购', 'shop/3010/1471502227046-2040368847.jpg', 1, 1, 14, '', 0, 0, 1, 1, '', 0, NULL, 'biz', 105, 'pm_admin');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, scene_type) 
	VALUES (16005, 999999, 0, '0', 0, '/home', 'Bizs', 'BizDrink', '饮品', 'shop/3007/14715010230651035403229.jpg', 1, 1, 14, '', 0, 0, 1, 1, '', 0, NULL, 'biz', 106, 'park_tourist');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, scene_type) 
	VALUES (16006, 999999, 0, '0', 0, '/home', 'Bizs', 'BizDrink', '饮品', 'shop/3007/14715010230651035403229.jpg', 1, 1, 14, '', 0, 0, 1, 1, '', 0, NULL, 'biz', 106, 'pm_admin');	
	
-- 更新讯美的创客空间为有layout形式的：一个大banner图和三个item
UPDATE `eh_launch_pad_items` SET `action_type`=2, `action_data`='{"itemLocation":"/home/Makers","layoutName":"MakerLayout"}' WHERE `id` in (10088, 10327) AND `namespace_id`=999999;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16021, 999999, '0', '0', '0', '/home/Makers', 'MakerBanners', 'MakerBanner', '', 'cs://1/image/aW1hZ2UvTVRwbVlqUmhNakF6TVRRMk1tUTVOelZsWkRZeFpqQmxOamxtTXpRMVl6RmlNUQ', '1', '1', '0', NULL, '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16022, 999999, '0', '0', '0', '/home/Makers', 'MakerBanners', 'MakerBanner', '', 'cs://1/image/aW1hZ2UvTVRwbVlqUmhNakF6TVRRMk1tUTVOelZsWkRZeFpqQmxOamxtTXpRMVl6RmlNUQ', '1', '1', '0', NULL, '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16023, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', '工位预订', '工位预订', 'cs://1/image/aW1hZ2UvTVRvME1tWmpNREV4WkRObVkySmxOVFJrTjJOaE1EQmhOMk14T1RoaE5EQmpNZw', '1', '1', '14', '{"url":"http://core.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16024, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', '工位预订', '工位预订', 'cs://1/image/aW1hZ2UvTVRvME1tWmpNREV4WkRObVkySmxOVFJrTjJOaE1EQmhOMk14T1RoaE5EQmpNZw', '1', '1', '14', '{"url":"http://core.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES (16025, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRvME5EY3pZemM0WlRnek1qUTRNbUV5WldVMk5UQXdPVGxqWlRjeE56UTBOZw', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES (16026, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRvME5EY3pZemM0WlRnek1qUTRNbUV5WldVMk5UQXdPVGxqWlRjeE56UTBOZw', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16027, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', 'MEETINGROOM', '会议室预订', 'cs://1/image/aW1hZ2UvTVRvME5XSXlZVGs1T0dVeVpqazROelUyWW1Ga05URXpOemhrTmpsaU16YzBOUQ', '1', '1', '49', '{\"resourceTypeId\":4,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16028, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', 'MEETINGROOM', '会议室预订', 'cs://1/image/aW1hZ2UvTVRvME5XSXlZVGs1T0dVeVpqazROelUyWW1Ga05URXpOemhrTmpsaU16YzBOUQ', '1', '1', '49', '{\"resourceTypeId\":10002,\"pageType\":0,\"communityFilterFlag\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');

-- 产业服务体系使用服务联盟V2.0（上面已进行数据迁移）
-- 政府资源使用服务联盟V2.0
UPDATE `eh_launch_pad_items` SET `action_data` = '{"type":14,"parentId":14}', action_type = 33 WHERE ID IN (10086, 10325);
-- 公共服务平台使用资源预订3.0
UPDATE `eh_launch_pad_items` SET `action_data` = '{\"resourceTypeId\":10001,\"pageType\":0,\"communityFilterFlag\":0}', action_type = 49 WHERE ID IN (10087, 10326);


-- 在讯美中展示 资源预订及各子菜单 by lqs 20160831
delete from eh_web_menu_scopes where menu_id in (select id from eh_web_menus where path like '/40000/43400%') and owner_id=999999;
delete from eh_web_menu_scopes where menu_id in (select id from eh_web_menus where path like '/40000/43300%') and owner_id=999999;

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES(10001,'公共资源预订','0',NULL,2,999999);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES(10002,'会议室预订','0',NULL,2,999999);
	
	
	
-- 把打卡考勤的老功能重新开放  by sfyan 20160831  
DELETE FROM `eh_web_menu_privileges` WHERE `menu_id` IN (56172, 56174, 56176, 56111, 56112);
DELETE FROM `eh_web_menus` WHERE `id` IN (56172, 56174, 56176, 56106, 56111, 56112);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56106,'打卡详情',56100,null,null,1,2,'/50000/56000/56100/56108','park',571);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56181,'打卡详情',56106,null,'punch_detail',0,2,'/50000/56000/56100/56106/56181','park',572);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56107,'异常申请处理',56100,null,null,1,2,'/50000/56000/56100/56107','park',573);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56186,'异常申请处理',56107,null,'attendance_manage',0,2,'/50000/56000/56100/56107/56186','park',574);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56108,'考勤统计',56100,null,null,1,2,'/50000/56000/56100/56108','park',575);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56191,'考勤统计',56108,null,'punch_statistics',0,2,'/50000/56000/56100/56108/56191','park',576);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),797,56181,'打卡详情',1,1,'打卡详情  全部权限',577);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),799,56186,'异常处理',1,1,'异常处理 全部权限',578);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),820,56191,'考勤统计',1,1,'考勤统计  全部权限',579);



-- 威新link 屏蔽
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 56000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%56000/%';



-- 屏蔽物业报修2.0 by lqs 20160831 
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
-- 任务列表
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 999989 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 999990 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 999993 , 0);
-- 服务类型设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 999989 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 999990 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 999993 , 0);
-- 分类设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 999989 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 999990 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 999993 , 0);
-- 统计
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 999989 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 999990 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 999993 , 0);

-- 海岸换icon by xiongying20160901
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRveU1HRTVNVFZpT0dJMVpEYzJaalZoTWpJMU1qYzFOelZqTkRkaFpUQXdOZw' where id in(10376,10377);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRveU5UaG1NV1E0TXpsa05UZGlZVGN3TXpGak1EY3haR1EyTmpObFlqRTJOQQ' where id in(1773,1774);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwak5qSmlORFJpWXpBd01tWXpOak0wTVdFeFlXRmpORGhpTlRWalkyUXdZdw' where id in(1768,1769);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwa05EWmlOVE0zTTJFNU16WTBPR05sTXpGbU9XTXlZekUzTVdZMVlUbGtNUQ' where id in(1760,1761);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwak5qQm1ZemxrWXpaaU9XVXlaVEZqWVdFd1lUa3hPR0kxWldOaFpqazNNUQ' where id in(1764,1765);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvME5XWTRPV1JrT1RobE5XUXhaVFptWWpZeVkyWmtOelUyTldVM05qVXdPUQ' where id in(10370,10371);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvM05tRmlNR0V3TjJZMU1qZzNPVEl5WXpGa05UYzNNMlV4TVdKaFkyVmxOQQ' where id in(10372,10373);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvd01HSmhaRGM1TTJFek9UZzNPVFkzWVRFNU1UZGhPRFpqT1RjeVlUUTNNdw' where id in(1762,1763);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvek5qWTFOekV6WlRJMFpEYzBaR1psTnpjNE9XSXlZV0UxTm1SbVlXUmhaZw' where id in(1766,1767);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMk56QTJZbU5qTWpGa1pXWmhOalUyT1RCbFlUa3dNR1UyWWpSaE1UTmhPUQ' where id in(10374,10375);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwalpHVTROR0poWlRkaE1XUTJZemxsWXpnMU5EUTBZMlUyTXpoa01XRmpaQQ' where id in(10380,10381);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvd1pUQTVZek5rTUdFNE9XUm1OelExT0RZeE5EZGpabVU0T0ROa1lUTTJNQQ' where id in(10382,10383);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvME56ZzNNemt5TkdKa1pEZ3dZVEZsWldZeU9XVXlaREUxTjJJM05EYzRaUQ' where id in(10394,10395);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwaFkyUXlaalkyTldWaVptRXhZekZpT1RVME9EYzJZVFUwTkRVeU1UVXpZUQ' where id in(1785, 1905);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvME5XWTRPV1JrT1RobE5XUXhaVFptWWpZeVkyWmtOelUyTldVM05qVXdPUQ' where id in(10700, 10702);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvM05tRmlNR0V3TjJZMU1qZzNPVEl5WXpGa05UYzNNMlV4TVdKaFkyVmxOQQ' where id in(10704, 10706);


update eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRvek0yTXhaamhtWldVNVpXTTFZbVF3TWpjMU1XTTVOR1ZoTWpRMlpqQmtaQQ' where id in(52, 85);
update eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRwbFlURTBNbU5tTkRJNU5qa3haamszWmpJd01tTTJaREEyTVdFeU1XVTFPQQ' where id in(57, 86);
update eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRvMU9UWmhZVGs1TXpOaFpUbGpNVEZtTnpGbE5URmhOakEwTXpWaU5HWmlZZw' where id in(83, 87);
update eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRvd1pUVmtOekJrTjJNMlptWTVaV1kyWmpnek5UVmhOelU1T1RZNE1qUmhPUQ' where id in(84, 1012);


update eh_launch_pad_items set action_data = '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fpromotion%2Fall%3F_k%3Dzlbiz#sign_suffix"}' where id in(10380,10381);


-- 储能园区活动换icon by xiongying20160901
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRveFpXVmtOMkprTnpKak1XTXhPVFJpTXprd01qa3pZamMyTkdSa05EWmhaZw' where id in( 10635, 10617);



-- 不能添加和删除服务联盟根类型 by xiongying20160901
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10004', 'zh_CN', '未找到上级类型');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10005', 'zh_CN', '不能删除根类型');

-- 储能app升级规则
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(75,34,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (46, 34, '3.9.0', 'http://apk.zuolin.com/apk/UFinePark-3.9.0.2016090119-release.apk', '${homeurl}/web/download/apk/andriod-UFinePark-3-9-0.html', '0');


-- 讯美屏蔽菜单 by sfyan 20160902
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 41000,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 42000,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%42000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 43500,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%43500/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 48000,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 45000,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 46100,'', 'EhNamespaces', 999999 , 0);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 59000,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%59000/%';



-- 任务列表
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 999999 , 0);
-- 服务类型设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 999999 , 0);
-- 分类设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 999999 , 0);
-- 统计
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 999999 , 0);


-- 任务列表
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 0 , 0);
-- 服务类型设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 0 , 0);
-- 分类设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 0 , 0);
-- 统计
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 0 , 0);



-- 退款host和API的配置 add by wuhan  date:2016-9-2
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) VALUES('pay.zuolin.refound','POST /EDS_PAY/rest/pay_common/refund/save_refundInfo_record','退款的api','0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) VALUES('pay.zuolin.host','https://pay.zuolin.com','退款的host','0');

-- 屏蔽任务菜单 by sfyan 20160902
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
-- 任务列表
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 0 , 0);
-- 服务类型设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 0 , 0);
-- 分类设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 0 , 0);
-- 统计
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 0 , 0);

-- 现网删掉门牌地址  by sfyan 20160905
DELETE FROM `eh_addresses` WHERE `community_id` = 240111044331051500 AND `building_name` = '中国储能大厦' AND `apartment_name` in ('B1','B2','B3','B4');



-- 深业更换广场icon图 by xujuan 20160902
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvME9UWTJNemszT0RRd1l6WmtNell6TXprMVpEVTNPV1UzWkdObE1UbG1OUQ' where item_label = "任务管理" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaFpXRmtZek5qTWpobE1UWTRaVE5qWlRjek4yWTFaRFU1WlRJeVlqUXlNQQ' where item_label = "停车充值" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaE9HVmlNVFJpWlRGaU1tSmpZMkZsTXpWa01qSTRNemhsTW1NM016RXdaQQ' where item_label = "公告管理" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvd1pUSmlPREUwT1dWa1pqQmtOR1l6TUdVMVlXUTJNVEZrT0RSbFkyUmxZdw' where item_label = "品质核查" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwbE9HSXpZelUzWlRaak9XTTVOekkyTVdRME1XWmhZalJrWXpFd01tRm1aUQ' where item_label = "场地预约" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvMllXWXhZVFEwWXpkak9UazNPV0UzWkdNd1pHRTNOR1ptTkRoaVpqa3pNUQ' where item_label = "快递查询" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRveVltSmhNMlkzTjJJMU5URmtOMkl5WXpZNE9UaG1ZV0l6WlRBd01EVXhOUQ' where item_label = "打卡" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRveE5qaGpNV05pWm1FMFpXSmlNbVUzT0RReVpqWmlNVFV5WkdOaU4ySXhOQQ' where item_label = "更多" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvek5USmpZV1JtTm1FMll6UXdZVFpoWlRNeFlqVXdObU0xTXpRME16WmlPUQ' where item_label = "服务预约" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvek56VTNPRE01WkdJeU1tRTFOVFl6WmpNMk5XUmxZMll4TWpSalpqZGxZdw' where item_label = "流程审批" and namespace_id=999992;	
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaVpqazBOVEE1T1dRNE5XSTRNekF6WW1Fek5qZ3lPREExT1dWak1qWmtPUQ' where item_label = "物业报修" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvM05XUmlZbUZqTURGbU1HVXhaR1JsTVdJMU5EZGpOVE5tWW1VeU9UQmpNQQ' where item_label = "物业服务" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaU1tSTRNMkU1T1RNeU56aGlZV0ZsT1dWbE1XTmpNakZsTURVeU1XUmhNQQ' where item_label = "物业缴费" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaFpERTNOekUyWkRReE5UTTVabVJtT0dJMU1tVmlOR0UzWVRVNU5HVXlOZw' where item_label = "视频会议" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaE9UZzVOV001Wm1aaU5qSTJZamRoTXpRMVlXSXdOVGd4WmpFeE9XTXhOZw' where item_label = "企业通讯录" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvME56Y3dPRGN3TW1FeU9ERXdOR016WlRrd05UTXpNVGMyTURreFpUZzNNZw' where item_label = "设备巡检" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvMVlXVTFNelJpWVdVeU1EY3pOREptTlRZeFlqaGlNV1k1TldZM1pEVTFOUQ' where item_label = "门禁" and namespace_id=999992;
	
-- 左邻更换广场icon图 by xujuan 20160902
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvME5qYzVPVGxtTXpVMk56ZGpNV0U1WVRZeFpqUmpPRFUyTm1SalkyWXlZUQ' where item_label = "VIP车位" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvME9UWTJNemszT0RRd1l6WmtNell6TXprMVpEVTNPV1UzWkdObE1UbG1OUQ' where item_label = "任务管理" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvM1pUYzBaV1ZrT1RrMVlqRTROVFZoTVRFMk5EZGtOakl5TjJRNU1EUXhPUQ' where item_label = "会议室预订" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaFpXRmtZek5qTWpobE1UWTRaVE5qWlRjek4yWTFaRFU1WlRJeVlqUXlNQQ' where item_label = "停车充值" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwbE9EZGtNakJsT1dKbVl6STRZemczWW1ZMVpqUmhaVEJqTUdZM01EQXlPQQ' where item_label = "公共会议室" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaE9HVmlNVFJpWlRGaU1tSmpZMkZsTXpWa01qSTRNemhsTW1NM016RXdaQQ' where item_label = "公告管理" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaE1tUmxOVE5oT1dJNVpUYzBNRE13WWpRMFlqQTBNRGt5WmpZek9HUTJaQQ' where item_label = "创客空间" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvd1pUSmlPREUwT1dWa1pqQmtOR1l6TUdVMVlXUTJNVEZrT0RSbFkyUmxZdw' where item_label = "品质核查" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwbFpHRTRaRFkyWlRnM1pHVXhOekJpWW1Vell6ZzVZVEpsTXpnellUVXpNQQ' where item_label = "园区入驻" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaFlXTm1ORFZsWWpSbFkyUmtPREkzTldWbE1qQTRZamhtTURVNE9ERTRaZw' where item_label = "家政服务" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvMllXWXhZVFEwWXpkak9UazNPV0UzWkdNd1pHRTNOR1ptTkRoaVpqa3pNUQ' where item_label = "快递查询" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRveE5qaGpNV05pWm1FMFpXSmlNbVUzT0RReVpqWmlNVFV5WkdOaU4ySXhOQQ' where item_label = "更多" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwa01qRTJOekl5TUdaaU9XSTBNRGc0WWpJd01HTXlNemhtTkdSa1l6TmxNQQ' where item_label = "服务热线" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvek5USmpZV1JtTm1FMll6UXdZVFpoWlRNeFlqVXdObU0xTXpRME16WmlPUQ' where item_label = "服务预约" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvM05XUmlZbUZqTURGbU1HVXhaR1JsTVdJMU5EZGpOVE5tWW1VeU9UQmpNQQ' where item_label = "物业服务" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaU1tSTRNMkU1T1RNeU56aGlZV0ZsT1dWbE1XTmpNakZsTURVeU1XUmhNQQ' where item_label = "物业缴费" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRveVpqVTFZbUUwWm1SbE9HSTRNREZqTWpkbU1HRmpNV1ZpTVRFek5UTXdOZw' where item_label = "电子屏预订" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvNFlUVTJaVEUwTVRGaU16RmtNMll4T0RObVptWXpNVFV4WW1KbE5UWTFaQQ' where item_label = "管道疏通" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaFpERTNOekUyWkRReE5UTTVabVJtT0dJMU1tVmlOR0UzWVRVNU5HVXlOZw' where item_label = "视频会议" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaE9UZzVOV001Wm1aaU5qSTJZamRoTXpRMVlXSXdOVGd4WmpFeE9XTXhOZw' where item_label = "通讯录" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvMVlXVTFNelJpWVdVeU1EY3pOREptTlRZeFlqaGlNV1k1TldZM1pEVTFOUQ' where item_label = "公共门禁" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwbE9EYzJaREF6Tm1FMk1EUXhNekptTkRWbE9XSmhZekppTTJZMFlqVTRNQQ' where item_label = "预约参观" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaFpEZ3lNV0ZtWXpGbVpqVmpOVFpoTkRVNVpHRTRNREpqWVRrek5UbGlOZw' where item_label = "咨询求助" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvMFpUVTJaV1kxWXpSaFkyWTRPVFV4T0RNeU9HUm1ZelkyWldFd05XUTBPQQ' where item_label = "投诉建议" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaVpqazBOVEE1T1dRNE5XSTRNekF6WW1Fek5qZ3lPREExT1dWak1qWmtPUQ' where item_label = "报修" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRveE0yTmlOVEZtTjJSaU56YzVZV1U1WkdabVlURmxNMk5pTldVME5XSXlOQQ' where item_label = "紧急求助" and namespace_id=0;		
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaFpEZ3lNV0ZtWXpGbVpqVmpOVFpoTkRVNVpHRTRNREpqWVRrek5UbGlOZw' where item_label = "咨询求助" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRvMFpUVTJaV1kxWXpSaFkyWTRPVFV4T0RNeU9HUm1ZelkyWldFd05XUTBPQQ' where item_label = "投诉建议" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaVpqazBOVEE1T1dRNE5XSTRNekF6WW1Fek5qZ3lPREExT1dWak1qWmtPUQ' where item_label = "报修" and namespace_id=999992;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRveE0yTmlOVEZtTjJSaU56YzVZV1U1WkdabVlURmxNMk5pTldVME5XSXlOQQ' where item_label = "紧急求助" and namespace_id=999992;	
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRwaE9UZzVOV001Wm1aaU5qSTJZamRoTXpRMVlXSXdOVGd4WmpFeE9XTXhOZw' where item_label = "企业通讯录" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRveU5qTmtaakptTm1aaU5tUTBNVEUzT0dZd016QXpOamN6Tm1JeFkyVTJaQQ' where item_label = "预订工位" and namespace_id=0;
update eh_launch_pad_items set icon_uri ='cs://1/image/aW1hZ2UvTVRveVltSmhNMlkzTjJJMU5URmtOMkl5WXpZNE9UaG1ZV0l6WlRBd01EVXhOUQ' where item_label = "打卡考勤" and namespace_id=0;		
			

-- 新建公司深圳正中置业有限公司 modified by xiongying 20160906
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1003275, UUID(), '深圳正中置业有限公司', '深圳正中置业有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180870, 1, 0);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(180870, UUID(), 0, 2, 'EhGroups', 1003275,'深圳正中置业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (318600, 229376, 'enterprise', 1002875, 0, 0, 7, 3, UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1002875, 0, 'ENTERPRISE', '深圳正中置业有限公司', 0, NULL, '/1002875', 1, 2, 'ENTERPRISE', 1003275, 0);
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2107081, 1002875, 'USER', 229376, 'manager', '陈勇', 0, '13682339935', NULL, 3);
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES (11170, 'EhOrganizations', 1002875, 'EhUsers', 229376, 1005, 0, UTC_TIMESTAMP());
INSERT INTO `eh_organization_community_requests`(`id`, `community_id`, `member_type`, `member_id`, `member_status`, `create_time`, `update_time`)
    VALUES(1111290,240111044331051380, 'organization', 1002875, 3, UTC_TIMESTAMP(), UTC_TIMESTAMP());
    
    
-- 微信link的短信通知 by sfyan 20160908   
SET @organization_task_target_id = (SELECT MAX(id) FROM `eh_organization_task_targets`);
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',225500,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',225500,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',228432,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',228432,'REPAIRS','push');



-- 政府资源首页
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ('200105', '0', 'community', '240111044331049963', '政府资源', '政府资源首页', '14', '', '0755-86620293', '公共服务联盟旨在集聚社会优质服务资源，为园区广大企业提供专业、高效、有保障的服务，以帮助广大企业畅通信息渠道，提升企业管理，降低经营成本，增强市场竞争能力。服务类型包括项目融资对接、人才招聘和培训、信息化建设、管理咨询、知识产权管理、质量管理、外贸事务、法律事务、 物流服务、场地装修等。', 'cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);    









-- merge from 3.9.0 20160908
-- 屏蔽任务菜单 by sfyan 20160902
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
-- 任务列表
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 0 , 0);
-- 服务类型设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 0 , 0);
-- 分类设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 0 , 0);
-- 统计
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 0 , 0);

-- 威新link 保修任务 短信通知  by sfyan 20160905
SET @organization_task_target_id = (SELECT MAX(id) FROM `eh_organization_task_targets`);
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',229293,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',228429,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',229293,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',228429,'REPAIRS','push');

-- 增加模板配置 by sfyan 20160905 
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 7, 'zh_CN', '新发布一条任务短信消息', '28177', 999999);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 7, 'zh_CN', '新发布一条任务短信消息', '28181', 999991);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 7, 'zh_CN', '新发布一条任务短信消息', '28182', 999990);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 7, 'zh_CN', '新发布一条任务短信消息', '28183', 999989);


-- 调整深业资源预订的菜单 by sfyan 20160907
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 42000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%42000/%';
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 48000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';

DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 43400 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999992;
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` IN (SELECT `id`  FROM `eh_web_menus` WHERE `path` LIKE '%43400/%') AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999992;

-- 升级规则：海岸、深业、ibase、左邻、讯美 3.9.0 Android版；储能 3.9.0 IOS版 add by xiongying20160907
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(81,27,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) 
    VALUES (51, 27, '3.9.0', 'http://apk.zuolin.com/apk/HaianPark-3.9.0.2016090606-release.apk', '${homeurl}/web/download/apk/andriod-haian-3-9-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(82,30,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) 
    VALUES (52, 30, '3.9.0', 'http://apk.zuolin.com/apk/ShenyeProperty-3.9.0.2016090606-release.apk', '${homeurl}/web/download/apk/andriod-sywy-3-9-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(83,38,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) 
    VALUES (53, 38, '3.9.0', 'http://apk.zuolin.com/apk/IBase-3.9.0.2016090606-release.apk', '${homeurl}/web/download/apk/andriod-ibase-3-9-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(84,1,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) 
    VALUES (54, 1, '3.9.0', 'http://apk.zuolin.com/apk/Zuolin-3.9.0.2016090606-release.apk	', '${homeurl}/web/download/apk/andriod-everhomes-3-9-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(85,5,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) 
    VALUES (55, 5, '3.9.0', 'http://apk.zuolin.com/apk/XmTecPark-3.9.0.2016090606-release.apk', '${homeurl}/web/download/apk/andriod-xunmei-3-9-0.html', '0');

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(86,35,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) 
    VALUES (56, 35, '3.9.0', '', '${homeurl}/web/download/apk/iOS-UFinePark-3-9-0.html', '0');    

-- 迅美更新banner 和添加左邻小店
UPDATE eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRwa01tRmtaakV6TTJaaU5qRTNaR1pqWmpGa04yRmpNamt5TVRRM01XVXlPQQ',action_type='0',action_data='' where id in (19,1007);

UPDATE eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRvd1lUWmtObU01T1dFd056RmpaR1kxWWpNMU5ERTNNRE00T1dFeVptTTNNQQ',action_type='0',action_data='' where id in (20,1008);

UPDATE eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRveU5qTXlNRE0wTWpNNE1UZzRNR1UxTmpkbE5UTTFNRGhrTldJeU1UbGtaUQ',action_type='0',action_data='' where id in (21,1009);

UPDATE eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRwbE1EZG1ZelV3TW1Sa1lXTmlOV0k1Wm1FelkySTNaak0yTldFNE5UUTJOQQ',action_type='0',action_data='' where id in (22,1010);

UPDATE eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRwak4yRmhNVGsyT1RNNE9HSTFNamN6WVdRd1ptUmhORFkxTm1Ka09UZ3lZZw',action_type='0',action_data='' where id in (23,1011);

-- SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
--    VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), '999999', '0', '0', '0', '/home/life', 'Lifes', '左邻小店', '左邻小店', 'shop/3005/1462962042600874121332.jpg', '1', '1', '14', '', '1', '0', '1', '1', '', '0', NULL, 'biz', '93', '1', 'park_tourist', '1');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
--    VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), '999999', '0', '0', '0', '/home/life', 'Lifes', '左邻小店', '左邻小店', 'shop/3005/1462962042600874121332.jpg', '1', '1', '14', '', '1', '0', '1', '1', '', '0', NULL, 'biz', '93', '1', 'pm_admin', '1');

-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
--    VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), '999999', '0', '0', '0', '/home', 'Bizs', '左邻小店', '左邻小店', 'shop/3005/1462962042600874121332.jpg', '1', '1', '14', '', '1', '0', '1', '1', '', '0', NULL, 'biz', '93', '1', 'park_tourist', '1');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
--    VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), '999999', '0', '0', '0', '/home', 'Bizs', '左邻小店', '左邻小店', 'shop/3005/1462962042600874121332.jpg', '1', '1', '14', '', '1', '0', '1', '1', '', '0', NULL, 'biz', '93', '1', 'pm_admin', '1');

-- 深业

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
  VALUES(12,'服务预约','0',NULL,'0', 999992);

delete from eh_launch_pad_items where id = 10026;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
  VALUES ('10026', '999992', '0', '0', '0', '/home', 'Bizs', '服务预约', '服务预约', 'cs://1/image/aW1hZ2UvTVRvek5USmpZV1JtTm1FMll6UXdZVFpoWlRNeFlqVXdObU0xTXpRME16WmlPUQ', '1', '1', '49', '{\"resourceTypeId\":12,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
  VALUES ('10026', '999992', '0', '0', '0', '/home', 'Bizs', '服务预约', '服务预约', 'cs://1/image/aW1hZ2UvTVRvek5USmpZV1JtTm1FMll6UXdZVFpoWlRNeFlqVXdObU0xTXpRME16WmlPUQ', '1', '1', '49', '{\"resourceTypeId\":12,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1');


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
  VALUES( 240111044331051304, UUID(), 14956, '深圳市',  14958, '罗湖区', '深业中心大厦', '深业中心大厦', '深圳市罗湖区深南东路5045号', NULL, '深业中心大厦地处深南大道的金融中心区，是由深业中心发展（深圳）有限公司投资开发的5A智能化高级写字楼。大厦总建筑面积73542M2，总高33层 155米，深圳证券交易所入驻其中，决定其具有极其特殊的地位和影响。大厦建立了准军事化的保安队伍，人防、技防相结合的防范网络，以及与派出所和其它小区保安队形成的快速反应体系。根据《全国城市物业管理大厦考核标准》，结合金融机构的运作特点和星级酒店服务要求，形成了独具特色的金融商厦管理模式。1998年被建设部授予"全国物业管理示范大厦"称号，1999年"全国物业管理工作会议"指定为高层写字楼参观点，建设部部长俞振声高度评价为"点睛之作"。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 682, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'0', 179900, 179901, UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
  VALUES(240111044331047904, 240111044331051304, '', 114.11492, 22.54703, 'ws10k8xcyr58');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
  VALUES(1000750, 240111044331051304);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
  VALUES(177109, 240111044331051304, '深业中心大厦', '深业中心大厦', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999992);

INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
  VALUES(1256, 999992, 'COMMUNITY', 240111044331051304, UTC_TIMESTAMP()); 

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101751,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0101','深业中心大厦','0101','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101752,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0102','深业中心大厦','0102','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101753,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0103','深业中心大厦','0103','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101754,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0104','深业中心大厦','0104','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101755,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0105','深业中心大厦','0105','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101756,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0106','深业中心大厦','0106','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101757,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0107','深业中心大厦','0107','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101758,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0108','深业中心大厦','0108','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101759,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0109','深业中心大厦','0109','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101760,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0110','深业中心大厦','0110','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101761,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0111','深业中心大厦','0111','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101762,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0112','深业中心大厦','0112','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101763,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0113','深业中心大厦','0113','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101764,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0201-1901','深业中心大厦','0201-1901','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101765,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-20F','深业中心大厦','20F','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101766,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2201','深业中心大厦','2201','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101767,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2202','深业中心大厦','2202','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101768,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2203','深业中心大厦','2203','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101769,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2204','深业中心大厦','2204','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101770,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2205','深业中心大厦','2205','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101771,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2206','深业中心大厦','2206','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101772,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2207','深业中心大厦','2207','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101773,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2208-2211','深业中心大厦','2208-2211','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101774,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2212','深业中心大厦','2212','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101775,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2213','深业中心大厦','2213','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101776,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2215','深业中心大厦','2215','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101777,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2216','深业中心大厦','2216','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101778,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2218','深业中心大厦','2218','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101779,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2301/2314/2315/2316','深业中心大厦','2301/2314/2315/2316','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101780,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2302-2313','深业中心大厦','2302-2313','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101781,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2404-2411','深业中心大厦','2404-2411','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101782,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2401-03/12-16','深业中心大厦','2401-03/12-16','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101783,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2501/2516','深业中心大厦','2501/2516','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101784,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2502-2503','深业中心大厦','2502-2503','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101785,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2505-2511','深业中心大厦','2505-2511','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101786,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2512-2513','深业中心大厦','2512-2513','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101787,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2514-2515','深业中心大厦','2514-2515','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101788,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-26F','深业中心大厦','26F','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101789,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-27F','深业中心大厦','27F','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101790,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2901A-2901B','深业中心大厦','2901A-2901B','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101791,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2902-2903','深业中心大厦','2902-2903','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101792,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2904','深业中心大厦','2904','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101793,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2906','深业中心大厦','2906','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101794,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3002','深业中心大厦','3002','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101795,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3003','深业中心大厦','3003','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101796,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3005','深业中心大厦','3005','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101797,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3006','深业中心大厦','3006','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101798,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3007-3008','深业中心大厦','3007-3008','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101799,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3101','深业中心大厦','3101','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101800,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3105A','深业中心大厦','3105A','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101801,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3106','深业中心大厦','3106','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101802,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3107','深业中心大厦','3107','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
  VALUES(239825274387101803,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-32F','深业中心大厦','32F','2','0',UTC_TIMESTAMP(), 999992);


INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20483, 1000750, 240111044331051304, 239825274387101751, '深业中心大厦-0101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20484, 1000750, 240111044331051304, 239825274387101752, '深业中心大厦-0102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20485, 1000750, 240111044331051304, 239825274387101753, '深业中心大厦-0103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20486, 1000750, 240111044331051304, 239825274387101754, '深业中心大厦-0104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20487, 1000750, 240111044331051304, 239825274387101755, '深业中心大厦-0105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20488, 1000750, 240111044331051304, 239825274387101756, '深业中心大厦-0106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20489, 1000750, 240111044331051304, 239825274387101757, '深业中心大厦-0107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20490, 1000750, 240111044331051304, 239825274387101758, '深业中心大厦-0108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20491, 1000750, 240111044331051304, 239825274387101759, '深业中心大厦-0109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20492, 1000750, 240111044331051304, 239825274387101760, '深业中心大厦-0110', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20493, 1000750, 240111044331051304, 239825274387101761, '深业中心大厦-0111', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20494, 1000750, 240111044331051304, 239825274387101762, '深业中心大厦-0112', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20495, 1000750, 240111044331051304, 239825274387101763, '深业中心大厦-0113', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20496, 1000750, 240111044331051304, 239825274387101764, '深业中心大厦-0201-1901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20497, 1000750, 240111044331051304, 239825274387101765, '深业中心大厦-20F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20498, 1000750, 240111044331051304, 239825274387101766, '深业中心大厦-2201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20499, 1000750, 240111044331051304, 239825274387101767, '深业中心大厦-2202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20500, 1000750, 240111044331051304, 239825274387101768, '深业中心大厦-2203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20501, 1000750, 240111044331051304, 239825274387101769, '深业中心大厦-2204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20502, 1000750, 240111044331051304, 239825274387101770, '深业中心大厦-2205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20503, 1000750, 240111044331051304, 239825274387101771, '深业中心大厦-2206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20504, 1000750, 240111044331051304, 239825274387101772, '深业中心大厦-2207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20505, 1000750, 240111044331051304, 239825274387101773, '深业中心大厦-2208-2211', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20506, 1000750, 240111044331051304, 239825274387101774, '深业中心大厦-2212', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20507, 1000750, 240111044331051304, 239825274387101775, '深业中心大厦-2213', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20508, 1000750, 240111044331051304, 239825274387101776, '深业中心大厦-2215', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20509, 1000750, 240111044331051304, 239825274387101777, '深业中心大厦-2216', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20510, 1000750, 240111044331051304, 239825274387101778, '深业中心大厦-2218', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20511, 1000750, 240111044331051304, 239825274387101779, '深业中心大厦-2301/2314/2315/2316', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20512, 1000750, 240111044331051304, 239825274387101780, '深业中心大厦-2302-2313', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20513, 1000750, 240111044331051304, 239825274387101781, '深业中心大厦-2404-2411', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20514, 1000750, 240111044331051304, 239825274387101782, '深业中心大厦-2401-03/12-16', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20515, 1000750, 240111044331051304, 239825274387101783, '深业中心大厦-2501/2516', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20516, 1000750, 240111044331051304, 239825274387101784, '深业中心大厦-2502-2503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20517, 1000750, 240111044331051304, 239825274387101785, '深业中心大厦-2505-2511', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20518, 1000750, 240111044331051304, 239825274387101786, '深业中心大厦-2512-2513', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20519, 1000750, 240111044331051304, 239825274387101787, '深业中心大厦-2514-2515', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20520, 1000750, 240111044331051304, 239825274387101788, '深业中心大厦-26F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20521, 1000750, 240111044331051304, 239825274387101789, '深业中心大厦-27F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20522, 1000750, 240111044331051304, 239825274387101790, '深业中心大厦-2901A-2901B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20523, 1000750, 240111044331051304, 239825274387101791, '深业中心大厦-2902-2903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20524, 1000750, 240111044331051304, 239825274387101792, '深业中心大厦-2904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20525, 1000750, 240111044331051304, 239825274387101793, '深业中心大厦-2906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20526, 1000750, 240111044331051304, 239825274387101794, '深业中心大厦-3002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20527, 1000750, 240111044331051304, 239825274387101795, '深业中心大厦-3003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20528, 1000750, 240111044331051304, 239825274387101796, '深业中心大厦-3005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20529, 1000750, 240111044331051304, 239825274387101797, '深业中心大厦-3006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20530, 1000750, 240111044331051304, 239825274387101798, '深业中心大厦-3007-3008', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20531, 1000750, 240111044331051304, 239825274387101799, '深业中心大厦-3101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20532, 1000750, 240111044331051304, 239825274387101800, '深业中心大厦-3105A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20533, 1000750, 240111044331051304, 239825274387101801, '深业中心大厦-3106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20534, 1000750, 240111044331051304, 239825274387101802, '深业中心大厦-3107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
  VALUES (20535, 1000750, 240111044331051304, 239825274387101803, '深业中心大厦-32F', '0');
