delete from eh_launch_pad_items where target_type='biz' and scope_code = 0;

INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.realm', 'biz', 'business realm');
INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix', 'business url');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('group', '10030', 'zh_CN', '您不是群主，无权操作');

-- 电商离线包配置
INSERT INTO `eh_version_realm` VALUES ('49', 'biz', null, UTC_TIMESTAMP(), '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(52,49,'-0.1','2100224','0','2.3.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ('24', '49', '2.3.0', 'http://biz.zuolin.com/nar/biz/web/app/dist/biz-2-3-0-tag.zip', 'http://biz.zuolin.com/nar/biz/web/app/dist/biz-2-3-0-tag.zip', '0');

-- 发帖要收到消息的配置  20160725 by sfyan
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(1,'EhCommunities',240111044331052505,'EhUsers',222568,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(2,'EhCommunities',240111044331052505,'EhUsers',222569,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(3,'EhCommunities',240111044331052506,'EhUsers',222568,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(4,'EhCommunities',240111044331052506,'EhUsers',222569,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(5,'EhCommunities',240111044331052507,'EhUsers',222568,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(6,'EhCommunities',240111044331052507,'EhUsers',222569,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(7,'EhCommunities',240111044331052508,'EhUsers',222568,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(8,'EhCommunities',240111044331052508,'EhUsers',222569,'REPAIRS','push');

-- 补充菜单屏蔽的完整数据  20160725 by sfyan
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%43500/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%47000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%43000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%53000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%58000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%47000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%42000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%58100/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%43000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%43500/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%51000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%53000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%56000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%58000/%';

