
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


-- 调整深业资源预订的菜单 by sfyan 20160905
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 42000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%42000/%';
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 48000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';

DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 43400 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999992;
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` IN (SELECT `id`  FROM `eh_web_menus` WHERE `path` LIKE '%43400/%') AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999992;

