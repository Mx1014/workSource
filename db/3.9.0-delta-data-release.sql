
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

