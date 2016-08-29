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



-- merge from  serviceAlliance3.0-delta-data-release.sql 20160826
-- 迁移yellowpage中的数据
INSERT INTO `eh_service_alliances` (`id`,`parent_id`,`owner_type`,`owner_id`,`name`,`display_name`,`type`,`address`,`contact`,`description`,`poster_uri`,`status`,`default_order`,`longitude`,`latitude`,`geohash`,`discount`,`category_id`,`contact_name`,`contact_mobile`,`service_type`,`service_url`,`discount_desc`,`creator_uid`,`create_time`)
SELECT `id`,`parent_id`,`owner_type`,`owner_id`,`name`,`nick_name`,`type`,`address`,`contact`,`description`,`poster_uri`,`status`,`default_order`,`longitude`,`latitude`,`geohash`,`integral_tag1`,`integral_tag2`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`creator_uid`,`create_time` FROM `eh_yellow_pages` WHERE `type` = 2;

-- 迁移yellowpage attachment中的数据
INSERT INTO `eh_service_alliance_attachments` (`id`,`owner_id`,`content_type`,`content_uri`,`creator_uid`,`create_time`)
SELECT `id`,`owner_id`,`content_type`,`content_uri`,`creator_uid`,`create_time` FROM `eh_yellow_page_attachments`;


INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('1', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '1000000');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('2', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '999990');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('3', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '999999');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('4', '0', '政府资源类型', '政府资源类型', '2', '1', UTC_TIMESTAMP(), '999999');
    

update eh_service_alliances set type = 1 where owner_id = 240111044331048623;
update eh_service_alliances set type = 2 where owner_id = 240111044331051500;
update eh_service_alliances set type = 3 where owner_id = 240111044331049963;

INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`)
SELECT `id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id` FROM `eh_categories` WHERE `parent_id` = 100001;

update eh_service_alliance_categories SET owner_type = 'community';
update eh_service_alliance_categories SET owner_id = 240111044331048623 WHERE namespace_id = 1000000;
update eh_service_alliance_categories SET parent_id = 1 WHERE namespace_id = 1000000;
update eh_service_alliance_categories SET owner_id = 240111044331051500 WHERE namespace_id = 999990;
update eh_service_alliance_categories SET parent_id = 2 WHERE namespace_id = 999990;
update eh_service_alliance_categories SET owner_id = 240111044331049963 WHERE namespace_id = 999999;
update eh_service_alliance_categories SET parent_id = 3 WHERE namespace_id = 999999;

update eh_service_alliances set type = 1 where namespace_id = 1000000;
update eh_service_alliances set type = 2 where namespace_id = 999990;
update eh_service_alliances set type = 3 where namespace_id = 999999;



-- 物业报修2.0
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999991, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999991, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');  

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('331', '0', '分派任务', '分派任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('332', '0', '完成任务', '完成任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('333', '0', '关闭任务', '关闭任务', NULL);

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) VALUES ('760', 'EhOrganizations', NULL, '1', '331', '1001', '0', '1', '2016-08-05 13:29:01');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) VALUES ('761', 'EhOrganizations', NULL, '1', '332', '1001', '0', '1', '2016-08-05 13:29:01');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) VALUES ('762', 'EhOrganizations', NULL, '1', '333', '1001', '0', '1', '2016-08-05 13:29:01');

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('122', 'pmtask.category.ancestor', '任务', '任务分类名称', '0', NULL);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('152', 'pmtask.notification', '1', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('153', 'pmtask.notification', '2', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone} 已将任务分配给了 ${targetName} ${targetPhone}，将会很快联系您。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('154', 'pmtask.notification', '3', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone} 已完成该单，稍后我们将进行回访。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('155', 'pmtask.notification', '4', 'zh_CN', '任务操作模版', '您的任务已被 ${operatorName} ${operatorPhone} 关闭', '0');

update eh_service_alliances set type = 3 where namespace_id = 999999




-- merge from videoconf3.0-delta-data-release.sql 20160829
update `eh_locale_strings` set `text` = "抱歉您当前不可更换此账号（最短更换频率为7天）" where `scope` = "videoConf" and `code` = "10005";

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('videoConf', '10010', 'zh_CN', '公司不存在或已删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('videoConf', '10011', 'zh_CN', '未过期状态不可删除');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '500001', 'zh_CN', '该域下该公司已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '500002', 'zh_CN', '公司类型错误，只能为普通公司或物业公司');



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

