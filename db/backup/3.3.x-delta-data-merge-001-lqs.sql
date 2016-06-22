-- merge from 3.3.x-delta-data-001-liangqishi.sql
INSERT INTO `eh_scene_types`(`id`, `namespace_id`, `name`, `display_name`, `create_time`) VALUES(1, 0, 'default', '默认场景', '2016-03-06 10:50:32');
INSERT INTO `eh_scene_types`(`id`, `namespace_id`, `name`, `display_name`, `create_time`) VALUES(2, 0, 'pm_admin', '物业管理员场景', '2016-03-06 10:50:32');

INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '101', 'zh_CN', '小区圈');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '102', 'zh_CN', '周边小区');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '103', 'zh_CN', '本小区');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '104', 'zh_CN', '俱乐部');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '105', 'zh_CN', '公司圈');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '106', 'zh_CN', '全部');

-- INSERT INTO `eh_launch_pad_layouts`(`id`, `namespace_id`, `scene_type`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`) 
-- 	VALUES (28, 0, 'default', 'DiscoveryLayout', '{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"DiscoveryLayout","displayName":"发现","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NeighborLifes"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21,"columnCount":2},{"groupName":"","widget":"ScenePosts","instanceConfig":{"filterType":"discovery"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21}]}', '2016031201', '0', 2, '2016-03-12 19:16:25');
-- INSERT INTO `eh_launch_pad_layouts`(`id`, `namespace_id`, `scene_type`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`) 
--	VALUES (29, 0, 'pm_admin', 'DiscoveryLayout', '{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"DiscoveryLayout","displayName":"发现","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NeighborLifes"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21,"columnCount":2},{"groupName":"","widget":"ScenePosts","instanceConfig":{"filterType":"discovery"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21}]}', '2016031201', '0', 2, '2016-03-12 19:16:25');
INSERT INTO `eh_launch_pad_layouts`(`id`, `namespace_id`, `scene_type`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`) 
	VALUES (30, 0, 'pm_admin', 'ServiceMarketLayout', '{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2016031201', '0', 2, '2016-03-12 19:16:25');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, `scene_type`, name, layout_json, version_code, min_version_code, status, create_time) 
	VALUES (31, 0, 'pm_admin', 'SceneNoticeLayout', '{"versionCode":"2015072815","versionName":"3.0.0","displayName":"公告管理","layoutName":"SceneNoticeLayout","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GaActions"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"ScenePosts","instanceConfig":{"filterType":"ga_notice"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0}]}', '2015082914', '2015061701', '2', '2015-06-27 14:04:57');
	
-- INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
--	VALUES (10001, 'default', 0, 0, 0, 0, '/discovery', 'NeighborLifes', 'NeighborActivities', '周边活动', 'cs://1/image/aW1hZ2UvTVRvNFlXSXdaRGRqTnpKaU56UmtZMll3WlRNeU1XUmxOekU0WXpNNU9HWmpZUQ', '1', '1', '41', '', '0', '0', '1', '1', '', '0', null, null, null);
-- INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
--	VALUES (10002, 'default', 0, 0, 0, 0, '/discovery', 'NeighborLifes', 'NeighborClubs', '周边俱乐部', 'cs://1/image/aW1hZ2UvTVRvNFlXSXdaRGRqTnpKaU56UmtZMll3WlRNeU1XUmxOekU0WXpNNU9HWmpZUQ', '1', '1', '42', '', '0', '0', '1', '1', '', '0', null, null, null);
-- INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
--	VALUES (10003, 'pm_admin', 0, 0, 0, 0, '/discovery', 'NeighborLifes', 'NeighborActivities', '周边活动', 'cs://1/image/aW1hZ2UvTVRvNFlXSXdaRGRqTnpKaU56UmtZMll3WlRNeU1XUmxOekU0WXpNNU9HWmpZUQ', '1', '1', '41', '', '0', '0', '1', '1', '', '0', null, null, null);
-- INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
--	VALUES (10004, 'pm_admin', 0, 0, 0, 0, '/discovery', 'NeighborLifes', 'NeighborClubs', '周边俱乐部', 'cs://1/image/aW1hZ2UvTVRvNFlXSXdaRGRqTnpKaU56UmtZMll3WlRNeU1XUmxOekU0WXpNNU9HWmpZUQ', '1', '1', '42', '', '0', '0', '1', '1', '', '0', null, null, null);
INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10005, 'pm_admin', 0, 0, 0, 0, '/home', 'GovAgencies', 'OrgTaskManagement', '任务管理', 'cs://1/image/aW1hZ2UvTVRwbE16QXlNekJrTVRZMU16Rm1OakpoTnpVNE9URmtaalU1WXpJMFpUaG1Ndw', 1, 1, '39', '{"module":"ORG_TASK_MANAGEMENT"}', 0, 0, 1, 1, '','0', NULL, NULL, NULL);
INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10006, 'pm_admin', 0, 0, 0, 0, '/home','GovAgencies','NoticeManager','公告管理','cs://1/image/aW1hZ2UvTVRvMU1tUmhZbVE0T0RZek5URm1NVEl5WVRWa05HSTJaVEJpWWpVeU1UUm1OZw',1,1,2, '{"itemLocation":"/home/NoticeManager","layoutName":"SceneNoticeLayout","title":"公告管理","entityTag":"PM"}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL);
INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10007, 'pm_admin', 0, 0, 0, 0, '/home/NoticeManager', 'GaActions', 'PUBLIC_NOTICE', '发布公告', 'cs://1/image/aW1hZ2UvTVRvMk1USXdPREJpTkRSa05XVXhNV1EzTW1ZeVpEWTNaRE5tTnpCaU0ySmhNQQ', 1, 1, 19, '{"contentCategory":1003,"actionCategory":0,"targetEntityTag":"PM","visibleRegionType":0}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL);
INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10008, 'pm_admin', 0, 0, 0, 0, '/home', 'Coupons', '优惠券', '优惠券', 'cs://1/image/aW1hZ2UvTVRwbFlXUTBOemd5WkdZd1l6RTVaV1l6WkRRNU1UZ3dOVFl3T1Rjd1pEazFNdw', 3, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fallpromotions#sign_suffix"}', 0, 0, 1, 1, '','0', NULL, NULL, NULL);
INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10009, 'pm_admin', 0, 0, 0, 0, '/home', 'Coupons', '市场动态', '市场动态', 'cs://1/image/aW1hZ2UvTVRvME5qRmhZVEF4TW1JeVpHWmpZVEU0TW1Kak9HRTNNR0l3TXpCak5UQXpOZw', 5, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14477417463124576784#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL);
INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10010, 'pm_admin', 0, 0, 0, 0, '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwa01ESXpZalEwTkdZeE1ETTNPRGMyWkdVMU5UQXlOMlZrT1dKaE9UZzBNZw', '1', '1', '23', '', 0, 0, 1, 1, '', '0', NULL, NULL, NULL);
INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10011, 'pm_admin', 0, 0, 0, 0, '/home', 'Bizs', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', 1, 1, 40, '', 0, 0, 1, 1, '', 0, NULL, NULL, NULL);
INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10012, 'pm_admin', 0, 0, 0, 0, '/home', 'Bizs', 'QualityInspection', '品质核查', 'cs://1/image/aW1hZ2UvTVRvNVpEWTROR0prTnpGak0yVm1NRGhsWVRNMU5EY3lPR0kzT1RSalkySmxZdw', '1', '1', '44', ' {"realm":"quality","entryUrl":"https://core.zuolin.com/nar/quality/index.html?hideNavigationBar=1#/task_list#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`,  `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`) 
	VALUES (10013, 'pm_admin', 0, 0, 0, 0, '/home', 'Bizs', '视频会议', '视频会议', 'cs://1/image/aW1hZ2UvTVRveE9HRmlOelV6TUdJNE9ERTRaalF6Wm1Nd05EWmxabVkyTkRjMlltVTNaZw', '1', '1', '27', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1');

INSERT INTO  `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10014, '0', '0', '0', '0', '/home/pm', 'GaActions', '服务预约', '服务预约', 'cs://1/image/aW1hZ2UvTVRvNFpEVTVOalpqTVdNMk1UQTVPREpqWW1RME5qTTFOalV5WVRKa09UY3laUQ', '1', '1', '37', ' {\"ownerType\":\"organization\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL);	
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (10015, 0, 0, '0', 0, '/home/Gacw', 'GaPosts', 'ADVISE', '投诉建议', NULL, 1, 1, 15, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"embedAppId":27}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (10016, 0, 0, '0', 0, '/home/Gacw', 'GaPosts', 'HELP', '咨询求助', NULL, 1, 1, 15, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"embedAppId":27}', 0, 0, 1, 1, '', 0,NULL);



-- merge from 3.3.x-delta-data-001-xiongying.sql
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10008', 'zh_CN', '左邻会议账号--发票');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10009', 'zh_CN', '亲爱的用户：

您好！

感谢您使用左邻会议服务，请按照附件格式填写开票信息，并以邮件形式反馈给我们，我们将尽快为您开票并邮寄给您。

请确认您填写了正确的发票信息，如因您提供了错误的发票信息而导致开错发票，左邻不予重新开票。

再次感谢您对左邻的信任，如有任何疑问，请拨打左邻客服热线：4008384688。');

-- 3.3.x.qa-delta-data-001-xiongying.sql
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10001', 'zh_CN', '该业务组没有执行核查的成员');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10002', 'zh_CN', '任务不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10003', 'zh_CN', '标准不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10004', 'zh_CN', '类型不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10005', 'zh_CN', '权重不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10006 ', 'zh_CN', '说明：');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10007', 'zh_CN', '该类型下存在有效的标准，不能删除');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10008', 'zh_CN', '该任务已关闭');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10009', 'zh_CN', '生成excel信息有问题');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10010', 'zh_CN', '下载excel信息有问题');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10011', 'zh_CN', '不能分配自己');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'quality.notification', 1, 'zh_CN', '生成核查任务通知核查人', '你被安排执行“${taskName}”任务，截至日期为“${deadline}”，请及时执行');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'quality.notification', 2, 'zh_CN', '通知整改人', '你被“${userName}”安排执行“${taskName}”任务，截至日期为“${deadline}”，请及时执行');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'quality.notification', 3, 'zh_CN', '分配和转发任务', '“${operator}”安排${target}”执行“${taskName}”任务，截至日期为“${deadline}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'quality.notification', 4, 'zh_CN', '审阅不合格通知执行人', '您的任务“${taskNumber}”被“${userName}”审阅为不合格，请知悉');

INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
	VALUES(200501, 0, 0, '品质核查类别', '品质核查类别', 0, 2, UTC_TIMESTAMP(), 0);

INSERT INTO `eh_version_realm` (`id`, `realm`, `description`, `create_time`, `namespace_id`) VALUES ('29', 'quality', NULL, UTC_TIMESTAMP(), '0');	
	
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ('7', '29', '1.0.0', 'https://core.zuolin.com/nar/quality/dist/quality-1-0-0.zip', 'https://core.zuolin.com/nar/quality/dist/quality-1-0-0.zip', '0');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(29,29,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

-- merge from 3.3.x-delta-data-002-xiongying.sql
DELETE FROM `eh_conf_account_categories`;
INSERT INTO `eh_conf_account_categories`(`id`,`multiple_account_threshold`,`conf_type`,`min_period`,`single_account_price`,`namespace_id`,`multiple_account_price`,`display_flag`)
	VALUES ('1', '5', '0', '12', '358.00', '0', '298.00', '1');
INSERT INTO `eh_conf_account_categories`(`id`,`multiple_account_threshold`,`conf_type`,`min_period`,`single_account_price`,`namespace_id`,`multiple_account_price`,`display_flag`) 
	VALUES ('2', '5', '2', '12', '358.00', '0', '298.00', '0');
INSERT INTO `eh_conf_account_categories`(`id`,`multiple_account_threshold`,`conf_type`,`min_period`,`single_account_price`,`namespace_id`,`multiple_account_price`,`display_flag`)
	VALUES ('3', '5', '1', '12', '358.00', '0', '298.00', '1');
INSERT INTO `eh_conf_account_categories`(`id`,`multiple_account_threshold`,`conf_type`,`min_period`,`single_account_price`,`namespace_id`,`multiple_account_price`,`display_flag`) 
	VALUES ('4', '5', '3', '12', '358.00', '0', '298.00', '0');
INSERT INTO `eh_conf_account_categories`(`id`,`multiple_account_threshold`,`conf_type`,`min_period`,`single_account_price`,`namespace_id`,`multiple_account_price`,`display_flag`)
	VALUES ('9', '5', '4', '12', '358.00', '0', '298.00', '0');
INSERT INTO `eh_conf_account_categories`(`id`,`multiple_account_threshold`,`conf_type`,`min_period`,`single_account_price`,`namespace_id`,`multiple_account_price`,`display_flag`)
	VALUES ('10', '5', '5', '12', '358.00', '0', '298.00', '0');
INSERT INTO `eh_conf_account_categories`(`id`,`multiple_account_threshold`,`conf_type`,`min_period`,`single_account_price`,`namespace_id`,`multiple_account_price`,`display_flag`)
	VALUES ('11', '5', '6', '12', '358.00', '0', '298.00', '0');

UPDATE `eh_conf_accounts` SET `account_category_id` = 1 WHERE `account_category_id` = 5;
UPDATE `eh_conf_accounts` SET `account_category_id` = 3 WHERE `account_category_id` = 7;
UPDATE `eh_conf_accounts` SET `account_category_id` = 2 WHERE `account_category_id` = 6;
UPDATE `eh_conf_accounts` SET `account_category_id` = 4 WHERE `account_category_id` = 8;
UPDATE `eh_conf_accounts` SET `account_category_id` = 9 WHERE `account_category_id` = 12;
UPDATE `eh_conf_accounts` SET `account_category_id` = 10 WHERE `account_category_id` = 13;
UPDATE `eh_conf_accounts` SET `account_category_id` = 11 WHERE `account_category_id` = 14; 
 
UPDATE `eh_conf_account_histories` SET `account_category_id` = 1 WHERE `account_category_id` = 5;
UPDATE `eh_conf_account_histories` SET `account_category_id` = 3 WHERE `account_category_id` = 7;
UPDATE `eh_conf_account_histories` SET `account_category_id` = 2 WHERE `account_category_id` = 6;
UPDATE `eh_conf_account_histories` SET `account_category_id` = 4 WHERE `account_category_id` = 8;
UPDATE `eh_conf_account_histories` SET `account_category_id` = 9 WHERE `account_category_id` = 12;
UPDATE `eh_conf_account_histories` SET `account_category_id` = 10 WHERE `account_category_id` = 13;
UPDATE `eh_conf_account_histories` SET `account_category_id` = 11 WHERE `account_category_id` = 14;
 
UPDATE `eh_conf_orders` SET `account_category_id` = 1 WHERE `account_category_id` = 5;
UPDATE `eh_conf_orders` SET `account_category_id` = 3 WHERE `account_category_id` = 7;
UPDATE `eh_conf_orders` SET `account_category_id` = 2 WHERE `account_category_id` = 6;
UPDATE `eh_conf_orders` SET `account_category_id` = 4 WHERE `account_category_id` = 8;
UPDATE `eh_conf_orders` SET `account_category_id` = 9 WHERE `account_category_id` = 12;
UPDATE `eh_conf_orders` SET `account_category_id` = 10 WHERE `account_category_id` = 13;
UPDATE `eh_conf_orders` SET `account_category_id` = 11 WHERE `account_category_id` = 14;
 
UPDATE `eh_conf_source_accounts` SET `account_category_id` = 1 WHERE `account_category_id` = 5;
UPDATE `eh_conf_source_accounts` SET `account_category_id` = 3 WHERE `account_category_id` = 7;
UPDATE `eh_conf_source_accounts` SET `account_category_id` = 2 WHERE `account_category_id` = 6;
UPDATE `eh_conf_source_accounts` SET `account_category_id` = 4 WHERE `account_category_id` = 8;
UPDATE `eh_conf_source_accounts` SET `account_category_id` = 9 WHERE `account_category_id` = 12;
UPDATE `eh_conf_source_accounts` SET `account_category_id` = 10 WHERE `account_category_id` = 13;
UPDATE `eh_conf_source_accounts` SET `account_category_id` = 11 WHERE `account_category_id` = 14;

 
-- merge from 3.3.x-delta-data-003-xiongying.sql
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES ( 10485, 'EhOrganizations', 1000705, 'EhUsers', 211208, 1005, 0, UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES ( 10486, 'EhOrganizations', 1000700, 'EhUsers', 70691, 1005, 0, UTC_TIMESTAMP());
	
-- merge from 3.3.x-delta-data-002-liangqishi.sql
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mail.smtp.address', 'smtp.mxhichina.com', '访问邮件SMTP服务器的地址');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mail.smtp.port', '25', '访问邮件SMTP服务器的端口号');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mail.smtp.account', '', '系统邮件发送者帐号');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mail.smtp.passwod', '', '系统邮件发送者帐号密码');



-- merge from 3.3.x-delta-data-007-yanshaofan.sql
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 10, 'zh_CN','接受任务时回复的帖子消息', '该任务已由${targetUName}（${targetUToken}）确认，将会很快联系您。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 11, 'zh_CN', '新发布一条任务短信消息','您有一个新的任务，请尽快处理。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 12, 'zh_CN', '处理任务时发送的短信消息','${operatorUName}给你分配了一个任务，请直接联系用户${targetUName}（电话${targetUToken}），帮他处理该问题。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 13, 'zh_CN', '处理任务时回复的帖子消息','${operatorUName}（${operatorUToken}）已将该任务指派给${targetUName}处理，请等待。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 14, 'zh_CN', '任务被拒绝收到的短信消息','该任务已被${targetUName}（${targetUToken}）拒绝，请重新分配');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 15, 'zh_CN', '任务已完成后的短信消息','您的服务已完成');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 16, 'zh_CN', '任务已完成后回复的帖子消息','该服务已由${operatorUName}完成，稍后我们会将进行回访');


INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1012, 1, 0, '紧急求助', '帖子/紧急求助', 0, 2, UTC_TIMESTAMP());
	
UPDATE `eh_acl_roles` SET `namespace_id` = 0, `owner_type` = 'EhOrganizations', `name` = '物业超级管理员',description='所有权限（All rights）'  where `id` = 1001;
UPDATE `eh_acl_roles` SET `namespace_id` = 0, `owner_type` = 'EhOrganizations', `name` = '物业普通管理员',description='不能添加修改删除管理员，其他权限都有' where `id` = 1002;
UPDATE `eh_acl_roles` SET `namespace_id` = 0, `owner_type` = 'EhOrganizations', `name` = '企业超级管理员',description='内部管理的所有权限' where `id` = 1005;
UPDATE `eh_acl_roles` SET `namespace_id` = 0, `owner_type` = 'EhOrganizations', `name` = '企业普通管理员',description='不能添加修改删除管理员，其他内部管理的所有权限都有' where `id` = 1006;

UPDATE `eh_organization_tasks` eot SET `visible_region_type` = (SELECT `visible_region_type` FROM `eh_forum_posts` WHERE `embedded_id` = eot.id and embedded_app_id =27 limit 0,1), `visible_region_id` = (SELECT `visible_region_id` FROM `eh_forum_posts` WHERE `embedded_id` = eot.id and embedded_app_id =27 limit 0,1);

UPDATE `eh_organizations` SET directly_enterprise_id = 1000001 WHERE directly_enterprise_id = 178395;
UPDATE `eh_organizations` SET directly_enterprise_id = 1000100 WHERE directly_enterprise_id = 178689;
UPDATE `eh_organizations` SET directly_enterprise_id = 1000631 WHERE directly_enterprise_id = 180000; 


-- merge from 3.3.x-delta-data-008-yanshaofan.sql
-- 查询用户拥有2个同样的角色数据
-- select `target_id`,`role_id`, count(*) from `eh_acl_role_assignments` group by `target_id`, `role_id` having count(*) >1;
-- select * from `eh_acl_role_assignments` where role_id=1005 and target_id=205341;
-- 删除用户拥有2个同样角色数据的其中一条，最好是id小的
DELETE FROM `eh_acl_role_assignments`  WHERE `id` = 56;
DELETE FROM `eh_acl_role_assignments`  WHERE `id` = 10030;
DELETE FROM `eh_acl_role_assignments`  WHERE `id` = 10011;
DELETE FROM `eh_acl_role_assignments`  WHERE `id` = 10037;
DELETE FROM `eh_acl_role_assignments`  WHERE `id` = 10029;
DELETE FROM `eh_acl_role_assignments`  WHERE `id` = 46;
DELETE FROM `eh_acl_role_assignments`  WHERE `id` = 51;
DELETE FROM `eh_acl_role_assignments`  WHERE `id` = 50;
DELETE FROM `eh_acl_role_assignments`  WHERE `id` = 52;

-- 查询用户在多个机构的数据（需要过滤掉用户在多个机构的数据，不做处理）
-- select `target_id`, count(*) from `eh_organization_members` where `status`= 3 and `target_id` != 0 group by `target_id` having count(*) >1;
UPDATE `eh_acl_role_assignments` eara SET `owner_type` = 'EhOrganizations', `owner_id` = (select organization_id from `eh_organization_members` where `target_id`=eara.target_id and `status`= 3) 
WHERE `target_type` = 'EhUsers' AND `role_id` > 1000 AND `role_id` < 2000 AND `target_id` NOT IN (10713,192642,196846,198773,198943,199030, 205261);
-- 先把当前数据处理成用户第一个机构的角色
UPDATE `eh_acl_role_assignments` eara SET `owner_type` = 'EhOrganizations', `owner_id` = (select organization_id from `eh_organization_members` where `target_id`=eara.target_id and `status`= 3 limit 0,1) 
WHERE `target_type` = 'EhUsers' AND `role_id` > 1000 AND `role_id` < 2000 AND  `target_id` IN (10713,192642,196846,198773,198943,199030, 205261);

-- 复制添加成用户第二个机构的角色
-- 查询最大id  select max(id) from  `eh_acl_role_assignments` ;
-- 根据最大id设置值
set @assi_id = (select max(id) from  `eh_acl_role_assignments`);
INSERT INTO `eh_acl_role_assignments`(`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time`) 
SELECT (@assi_id := @assi_id + 1),`owner_type`,(select organization_id from `eh_organization_members` where `target_id`=eara.target_id and `status`= 3 limit 1,1),`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time` FROM `eh_acl_role_assignments` eara  
WHERE `target_type` = 'EhUsers' AND `role_id` > 1000 AND `role_id` < 2000 AND  `target_id` IN (10713,192642,196846,198773,198943,199030, 205261);

-- 处理对象是机构的权限数据
UPDATE `eh_acl_role_assignments` eara SET `owner_type` = 'EhOrganizations', `owner_id` = (select directly_enterprise_id from `eh_organizations` where `id`=eara.target_id and `status`= 2)
	WHERE `target_type` = 'EhOrganizations' AND `role_id` > 1000 AND `role_id` < 2000; 





-- merge from 3.3.x-delta-data-009-yanshaofan.sql
DELETE FROM `eh_acl_privileges` WHERE `id` > 199;
DELETE FROM `eh_web_menus`;
DELETE FROM `eh_web_menu_privileges`;
DELETE FROM `eh_acls` WHERE `role_id` IN (1001,1002,1005,1006);

-- 权限数据
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (200,0,'发帖','发公告和任务贴',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (201,0,'推送消息','推送消息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (301,0,'分配人员','分配任务',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (302,0,'修改任务可见性','可将任务修改为可见或不可见',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (303,0,'查询报修任务帖子','查看报修任务贴','ORG_TASK_MANAGEMENT');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (304,0,'查询求助任务帖子','查询求助任务帖子','ORG_TASK_MANAGEMENT');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (305,0,'查询所有任务帖子','查看所有任务帖子','ORG_TASK_MANAGEMENT');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (306,0,'接受／拒绝任务','接受／拒绝任务',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (307,0,'处理任务状态','处理任务状态',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (308,0,'设置任务的分类','设置任务的分类',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (309,0,'信息统计','任务统计',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (380,0,'查看区信息','查看区信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (381,0,'修改区信息','修改区信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (390,0,'楼栋列表','楼栋列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (391,0,'楼栋的增删改','楼栋的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (401,0,'查询园区企业','查询园区企业',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (402,0,'增加企业','增加企业',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (403,0,'删除企业','删除企业',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (404,0,'修改企业','修改企业信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (405,0,'开通企业管理员账号','开通企业管理员账号',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (406,0,'查看用户列表','查看用户列表信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (407,0,'用户的增删改','用户的增删改',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (408,0,'门牌管理','门牌的管理',null);


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (503,0,'查看物业缴费统计','查看物业缴费统计',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (504,0,'查询物业缴费单','查询物业缴费单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (505,0,'查询物业缴费记录','查询物业缴费记录',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (506,0,'查询欠费家庭','查询欠费家庭',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (507,0,'给所有欠费家庭推送缴费通知','给所有欠费家庭推送缴费通知',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (508,0,'给指定家庭发缴费通知','给指定家庭发缴费通知',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (509,0,'上传缴费账单','上传缴费账单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (510,0,'删除物业缴费单','删除物业缴费单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (511,0,'添加物业缴费单','添加物业缴费单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (512,0,'增加停车充值项','增加停车充值项',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (513,0,'删除停车充值项','删除停车充值项',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (514,0,'设置月卡有效领取天数','设置月卡有效领取天数',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (515,0,'查询充值记录','查询充值记录',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (516,0,'查询月卡申请记录','查询月卡申请记录',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (517,0,'发放月卡','发放月卡',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (518,0,'领取月卡','领取月卡',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (519,0,'设置通用设置','设置通用设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (520,0,'查询通用设置','查询通用设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (521,0,'查询服务类型','查询服务类型',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (522,0,'服务类型的增删改','增删改服务预约类型权限，包括服务类型的启用停用状态修改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (523,0,'查询预约详情','查询预约详情',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (524,0,'设置订单完成状态','设置订单完成状态',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (525,0,'查询数据统计','查询数据统计',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (526,0,'设置场所通用设置','设置场所预订通用设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (527,0,'查询场所','查询场所',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (528,0,'添加/修改具体场所','添加/修改具体场所',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (529,0,'查询预定详情','查询预定详情',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (530,0,'添加具体场所预订规则','添加具体场所预订规则',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (531,0,'删除场所规则','删除场所规则',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (532,0,'查询具体场所商品信息','查询具体场所商品信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (533,0,'添加具体场所商品信息','添加具体场所商品信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (534,0,'删除具体场所商品信息','删除具体场所商品信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (535,0,'删除具体场所','删除具体场所',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (536,0,'查看招租信息','查看招租信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (537,0,'查看入驻申请','查看入驻申请',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (538,0,'删除入驻申请','删除入驻申请',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (539,0,'增删改招租信息','增删改招租信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (540,0,'查看服务联盟信息','查看服务联盟信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (541,0,'增删改服务联盟信息','增删改服务联盟信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (542,0,'查看创客空间信息','查看创客空间信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (543,0,'删除修改创客空间','删除修改创客空间',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (544,0,'考勤设置','考勤设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (545,0,'查看打卡详情','查看打卡详情',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (546,0,'查看异常统计','查看异常统计',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (547,0,'进行异常处理','进行异常处理',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (548,0,'查看账号','查看账号',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (549,0,'分配账号','分配账号',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (550,0,'更换用户','更换用户',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (551,0,'查看我的订单','查看我的订单',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (552,0,'查看账号信息','查看账号信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (553,0,'查看开票信息','查看开票信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (554,0,'查看服务热线','查看服务热线',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (555,0,'增加删除服务热线','增加删除服务热线',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (556,0,'停车设置查询','停车设置查询',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (557,0,'设置活动规则','停车设置查询',null);


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (600,0,'查看子公司列表信息','查看子公司列表信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (601,0,'增删改分公司','增删改分公司',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (602,0,'查看角色权限列表','查看角色权限列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (603,0,'增删改角色，设置权限','增删改角色，设置权限',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (604,0,'查看管理员信息','查看管理员信息',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (605,0,'管理员的增删改','管理员的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (606,0,'查看部门','查看部门',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (607,0,'部门的增删改','部门的增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (608,0,'查看通讯录','查看通讯录',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (609,0,'通讯录人员的增删改','通讯录人员的增删改',null);



INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (610,0,'设置角色','设置角色',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (611,0,'查看业务组','查看业务组',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (612,0,'业务组增删改','业务组增删改',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (613,0,'业务组成员设置','业务组成员设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (614,0,'组成员的修改和删除','组成员的修改和删除',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (615,0,'组成员权限设置','组成员权限设置',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (616,0,'管辖范围','管辖范围，包括查询管辖小区楼栋，添加删除小区楼栋',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (620,0,'审批列表','审批的通讯录列表',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (621,0,'审批认证','同意，拒绝审批',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (700,0,'参考标准','参考标准 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (701,0,'类型管理','类型管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (702,0,'任务列表','任务列表 全部功能',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (710,0,'任务审阅','任务审阅 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (711,0,'权重管理','权重管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (712,0,'绩效统计','绩效统计 全部功能',null);

-- 菜单模块数据
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (10000,'消息管理',0,'fa fa-volume-up',null,1,2,'/10000','park',100);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (11000,'论坛/公告',10000,null,'forum_notice',0,2,'/10000/11000','park',110);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (12000,'一键推送',10000,null,'messagepush',0,2,'/10000/12000','park',120);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (20000,'任务管理',0,'fa fa-coffee',null,1,2,'/20000','park',200);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (21000,'全部任务',20000,null,'all_task',0,2,'/20000/21000','park',210);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (22000,'我的任务',20000,null,'my_task',0,2,'/20000/22000','park',220);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (23000,'统计',20000,null,'statistics',0,2,'/20000/23000','park',230);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (30000,'项目管理',0,'fa fa-building',null,1,2,'/30000','park',300);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (30500,'区管理',30000,null,'community_list',0,2,'/30000/30500','park',305);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (31000,'楼栋管理',30000,null,'building_management',0,2,'/30000/31000','park',310);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (32000,'门牌管理',30000,null,'apartment_statistics',0,2,'/30000/32000','park',320);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (33000,'企业管理',30000,null,'enterprise_management',0,2,'/30000/33000','park',330);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (34000,'用户管理',30000,null,'user_management',0,2,'/30000/34000','park',340);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (40000,'运营服务',0,'fa fa-comment',null,1,2,'/40000','park',400);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41000,'停车充值',40000,null,null,1,2,'/40000/41000','park',410);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41100,'停车设置',41000,null,'park_setting',0,2,'/40000/41000/41100','park',411);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41200,'活动规则',41000,null,'park_rules',0,2,'/40000/41000/41200','park',412);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41300,'月卡申请',41000,null,'park_card',0,2,'/40000/41000/41300','park',413);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41400,'充值记录',41000,null,'park_recharge',0,2,'/40000/41000/41400','park',414);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (42000,'场所预定',40000,null,null,1,2,'/40000/42000','park',420);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (42100,'通用设置',42000,null,'rental_setting',0,2,'/40000/42000/42100','park',421);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (42200,'场所发布',42000,null,'rental_publish',0,2,'/40000/42000/42200','park',422);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (42300,'预定详情',42000,null,'rental_detail',0,2,'/40000/42000/42300','park',423);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43000,'招租管理',40000,null,null,1,2,'/40000/43000','park',430);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43100,'招租管理',43000,null,'rent_manage',0,2,'/40000/43000/43100','park',431);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43200,'入住申请',43000,null,'enter_apply',0,2,'/40000/43000/43200','park',432);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44000,'服务联盟',40000,null,'service_alliance',0,2,'/40000/44000','park',440);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (45000,'创客空间',40000,null,'market_zone',0,2,'/40000/45000','park',450);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (46000,'服务热线',40000,null,'service_hotline',0,2,'/40000/46000','park',460);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (50000,'内部管理',0,'fa fa-group',null,1,2,'/50000','park',500);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51000,'层级管理',50000,null,null,1,2,'/50000/51000','park',510);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (51100,'层级管理',51000,null,'#',0,2,'/50000/51000/51100','park',511);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52000,'人员管理',50000,null,null,1,2,'/50000/52000','park',520);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52100,'管理员',52000,null,'#',0,2,'/50000/52000/52100','park',521);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52200,'部门通讯录',52000,null,'#',0,2,'/50000/52000/52200','park',522);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52300,'群组通讯录',52000,null,'#',0,2,'/50000/52000/52300','park',523);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (52400,'认证管理',52000,null,'#',0,2,'/50000/52000/52400','park',524);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (53000,'管辖范围',50000,null,null,1,2,'/50000/53000','park',530);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (53100,'管辖范围',53000,null,'#',0,2,'/50000/53000/53100','park',531);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (54000,'权限管理',50000,null,null,1,2,'/50000/54000','park',540);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (54100,'权限管理',54000,null,'#',0,2,'/50000/54000/54100','park',541);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (55000,'物业管理',50000,null,null,1,2,'/50000/55000','park',550);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (55100,'设备巡检',55000,null,'#',0,2,'/50000/55000/55100','park',551);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (55200,'品质核查',55000,null,'#',0,2,'/50000/55000/55200','park',552);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56000,'OA管理',50000,null,null,1,2,'/50000/56000','park',560);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56100,'考勤管理',56000,null,null,1,2,'/50000/56000/56100','park',561);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56110,'考勤设置',56100,null,'#',0,2,'/50000/56000/56100/56110','park',563);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56120,'打卡详情',56100,null,'#',0,2,'/50000/56000/56100/56120','park',564);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56130,'异常统计',56100,null,'#',0,2,'/50000/56000/56100/56130','park',565);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56140,'异常处理',56100,null,'#',0,2,'/50000/56000/56100/56140','park',566);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56200,'视频会议',56000,null,null,1,2,'/50000/56000/56200','park',567);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56210,'账号管理',56200,null,'#',0,2,'/50000/56000/56200/56210','park',568);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56220,'我的订单',56200,null,'#',0,2,'/50000/56000/56200/56220','park',569);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58000,'物业服务',50000,null,null,1,2,'/50000/58000','park',600);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58100,'品质核查',58000,null,null,1,2,'/50000/58000/58100','park',610);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58110,'作业标准',58100,null,null,1,2,'/50000/58000/58100/58110','park',611);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58111,'参考标准',58110,null,'reference_standard',0,2,'/50000/58000/58100/58110/58111','park',612);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58112,'类型管理',58110,null,'type_management',0,2,'/50000/58000/58100/58110/58112','park',613);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58113,'任务列表',58110,null,'task_list',0,2,'/50000/58000/58100/58110/58113','park',614);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58120,'品质分析',58100,null,null,1,2,'/50000/58000/58100/58120','park',614);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58121,'任务审阅',58120,null,'task_review',0,2,'/50000/58000/58100/58120/58121','park',615);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58122,'权重管理',58120,null,'weighting_management',0,2,'/50000/58000/58100/58120/58122','park',616);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58123,'绩效统计',58120,null,'performance_statistics',0,2,'/50000/58000/58100/58120/58123','park',617);

-- 模块权限数据
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (1,200,11000,'发帖',1,1,'发公告和任务贴',10);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (2,201,12000,'推送消息',1,1,'推送消息',20);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (3,306,21000,'接受，拒绝任务',0,1,'接受，拒绝任务',30);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (4,307,21000,'任务处理',0,1,'修改任务状态',40);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (5,303,21000,'查询报修任务帖子',1,1,'查询报修任务帖子',50);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (6,304,21000,'查看求助任务帖子',1,1,'查看求助任务帖子',60);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (7,305,21000,'查看所有任务帖子',1,1,'查看所有任务帖子',70);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (8,306,22000,'接受，拒绝任务',0,1,'接受，拒绝任务',79);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (77,307,22000,'任务处理',0,1,'修改任务状态',80);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (9,303,22000,'查询报修任务帖子',1,1,'查询报修任务帖子',81);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (75,304,22000,'查看求助任务帖子',1,1,'查看求助任务帖子',82);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (74,305,22000,'查看所有任务帖子',1,1,'查看所有任务帖子',83);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (73,309,23000,'任务统计',1,1,'任务统计',88);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (76,390,31000,'楼栋列表',1,1,'楼栋列表',90);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (72,391,31000,'楼栋的增删改',0,1,'楼栋的增删改',91);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (10,408,32000,'门牌管理',1,1,'门牌管理操作',100);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (11,401,33000,'企业列表',1,1,'查询企业列表',110);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (12,402,33000,'增加企业',0,1,'增加企业',120);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (13,403,33000,'删除企业',0,1,'删除企业',130);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (14,404,33000,'修改企业',0,1,'修改企业',140);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (15,405,33000,'开通企业管理员',0,1,'开通企业管理员',150);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (16,406,34000,'用户列表',1,1,'查询用户列表',160);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (17,556,41100,'停车设置列表',1,1,'查询停车设置列表',170);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (18,512,41100,'增加停车充值项',0,1,'增加停车充值项',180);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (19,513,41100,'删除停车充值项',0,1,'删除停车充值项',190);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (20,557,41200,'设置活动规则',1,1,'设置活动规则',200);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (21,515,41400,'充值记录',1,1,'查询充值记录',210);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (22,516,41300,'月卡申请记录',1,1,'查询月卡申请记录',220);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (23,514,41300,'设置月卡有效领取天数',0,1,'设置月卡有效领取天数',230);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (24,517,41300,'发放月卡',0,1,'发放月卡',240);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (25,518,41300,'领取月卡',0,1,'领取月卡',250);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (26,526,42100,'设置通用设置',1,1,'设置通用设置',260);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (27,527,42200,'查询场所',1,1,'查询场所',270);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (28,528,42200,'添加/修改具体场所',0,1,'添加/修改具体场所',280);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (29,530,42200,'添加具体场所预订规则',0,1,'添加具体场所预订规则',290);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (30,531,42200,'删除场所规则',0,1,'删除场所规则',300);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (31,532,42200,'查询具体场所商品信息',0,1,'查询具体场所商品信息',310);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (32,533,42200,'添加具体场所商品信息',0,1,'添加具体场所商品信息',320);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (33,534,42200,'删除具体场所商品信息',0,1,'删除具体场所商品信息',330);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (34,535,42200,'删除具体场所',0,1,'删除具体场所',340);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (35,529,42300,'查询预定详情',1,1,'查询预定详情',260);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (36,536,43100,'招租信息列表',1,1,'查看招租信息列表',260);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (37,539,43100,'增删改招租信息',0,1,'增删改招租信息',340);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (38,537,43200,'查看入驻申请',1,1,'查看入驻申请',260);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (39,538,43200,'删除入驻申请',0,1,'删除入驻申请',340);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (40,554,46000,'查看服务热线',1,1,'查看服务热线信息',350);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (41,555,46000,'增加删除服务热线',0,1,'增加删除服务热线',360);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (42,540,44000,'查看服务联盟',1,1,'查看服务联盟信息',370);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (43,541,44000,'增删改服务联盟信息',0,1,'增删改服务联盟信息',380);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (44,542,45000,'查看创客空间',1,1,'查看创客空间信息',390);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (45,543,45000,'删除修改创客空间',0,1,'删除修改创客空间',400);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (46,600,51100,'子公司列表',1,1,'查看子公司列表信息',410);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (47,601,51100,'增删改分公司',0,1,'增删改分公司',420);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (48,604,52100,'查看管理员信息',1,1,'查看管理员信息',430);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (49,605,52100,'管理员的增删改',0,1,'管理员的增删改',440);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (50,606,52200,'查看部门',1,1,'查看部门',450);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (51,607,52200,'部门的增删改',0,1,'部门的增删改',460);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (52,608,52200,'查看通讯录',1,1,'查看通讯录',470);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (53,609,52200,'通讯录人员的增删改',0,1,'通讯录人员的增删改',480);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (54,609,52200,'设置角色',0,1,'设置角色',490);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (55,611,52300,'查看业务组',1,1,'查看业务组',500);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (56,612,52300,'业务组增删改',0,1,'业务组增删改',510);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (57,613,52300,'业务组成员设置',1,1,'业务组成员设置',520);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (58,614,52300,'组成员的修改和删除',0,1,'组成员的修改和删除',530);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (59,615,52300,'组成员权限设置',0,1,'组成员权限设置',540);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (60,602,54100,'查看角色权限列表',1,1,'查看角色权限列表',550);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (61,603,54100,'增删改角色，设置权限',0,1,'增删改角色，设置权限',560);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (62,544,56110,'考勤设置',1,1,'考勤设置',570);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (63,545,56120,'打卡详情',1,1,'打卡详情',580);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (64,546,56130,'异常统计',1,1,'异常统计',590);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (65,547,56140,'打卡详情',1,1,'打卡详情',600);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (66,548,56210,'查看账号',1,1,'查看账号',610);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (67,549,56210,'分配账号',0,1,'分配账号',620);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (68,550,56210,'更换用户',0,1,'更换用户',630);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (69,551,56220,'我的订单',1,1,'查看我的订单',640);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (70,552,56220,'查看账号信息',0,1,'查看账号信息',650);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (71,553,56220,'查看开票信息',0,1,'查看开票信息',660);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (87,616,53100,'管辖范围管理',1,1,'管辖范围管理，包括管辖的小区楼栋查询，添加，删除',545);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (81,700,58111,'参考标准',1,1,'参考标准 全部权限',700);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (82,701,58112,'类型管理',1,1,'类型管理 全部权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (83,702,58113,'任务列表',1,1,'任务列表 全部权限',720);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (84,710,58121,'任务审阅',1,1,'任务审阅 全部权限',730);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (85,711,58122,'权重管理',1,1,'权重管理 全部权限',740);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (86,712,58123,'绩效统计',1,1,'绩效统计 全部权限',750);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (89,620,52400,'审批列表',1,1,'绩效统计 全部权限',760);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (88,621,52400,'审批认证',0,1,'审批同意，拒绝',770);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (90,380,30500,'查看区信息',1,1,'查看区信息',88);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (91,381,30500,'修改区信息',0,1,'修改区信息',89);

-- 检查目前最大的id，然后设置@acl_id   SELECT MAX(id) FROM `eh_acls`;
set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, id, 1001,0,1,now() FROM `eh_acl_privileges`;

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, id, 1002,0,1,now() FROM `eh_acl_privileges`;
DELETE FROM `eh_acls` WHERE `privilege_id` = 605 AND `role_id` = 1002;

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 51100;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 52100;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 52200;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 52300;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 54100;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56110;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56120;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56130;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56140;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56210;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56220;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58111;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58112;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58113;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58121;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58122;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58123;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 53100;

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 51100;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 52100;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 52200;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 52300;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 54100;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56110;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56120;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56130;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56140;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56210;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 56220;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58111;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58112;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58113;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58121;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58122;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58123;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 53100;

DELETE FROM `eh_acls` WHERE `privilege_id` = 605 AND `role_id` = 1006;

-- merge from 3.3.x-delta-data-010-yanshaofan.sql
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default', 6, 'zh_CN', '处理任务时发送的短信','${operatorUName}给你分配了一个任务，请直接联系用户${targetUName}（电话${targetUToken}），帮他处理该问题。', 0);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-左邻',22716, 0);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-科技园',22717, 1000000);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-讯美',22718, 999999);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-金隅嘉业',22719, 999995);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-海岸城',22720, 999993);


INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '100001', 'zh_CN', '没有权限！');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '100201', 'zh_CN', '任务已经被处理啦，请刷新！');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '100202', 'zh_CN', '请指定分配的人员！');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '100203', 'zh_CN', '请做正确的处理方式！');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'group.notification', 34, 'zh_CN', '删除俱乐部管理员收到的消息','${userName}已删除俱乐部“${groupName}”', 0);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'group.notification', 35, 'zh_CN', '删除俱乐部创建人收到的消息','已删除俱乐部“${groupName}”', 0);


UPDATE `eh_acl_roles` SET `name` = '超级管理员' where `id` = 1001;
UPDATE `eh_acl_roles` SET `name` = '普通管理员' where `id` = 1002;
UPDATE `eh_acl_roles` SET `name` = '超级管理员' where `id` = 1005;
UPDATE `eh_acl_roles` SET `name` = '普通管理员' where `id` = 1006;


-- merge from 3.3.x-delta-data-011-liangqishi.sql 与 3.3.x-delta-data-002-liangqishi.sql重复
-- INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mail.smtp.address', 'smtp.mxhichina.com', '访问邮件SMTP服务器的地址');
-- INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mail.smtp.port', '25', '访问邮件SMTP服务器的端口号');
-- INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mail.smtp.account', '', '系统邮件发送者帐号');
-- INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mail.smtp.passwod', '', '系统邮件发送者帐号密码');


-- merge from 3.3.x-delta-data-012-liangqishi.sql
-- change the interest group to club, change private group to group-chat
UPDATE eh_locale_templates SET `text`='您已加入群聊“${groupName}”。' WHERE id=1;
UPDATE eh_locale_templates SET `text`='${userName}已加入群聊“${groupName}”。' WHERE id=2;
UPDATE eh_locale_templates SET `text`='您已成功提交加入群聊“${groupName}”的申请，请耐心等待审核通过。' WHERE id=3;
UPDATE eh_locale_templates SET `text`='${userName}正在申请加入群聊“${groupName}”，您同意此申请吗？' WHERE id=4;
UPDATE eh_locale_templates SET `text`='管理员${operatorName}已通过您加入群聊“${groupName}”的申请，您可以在群聊内聊天、分享了！！' WHERE id=5;
UPDATE eh_locale_templates SET `text`='您已通过${userName}加入群聊“${groupName}”的申请。' WHERE id=6;
UPDATE eh_locale_templates SET `text`='管理员${operatorName}已通过${userName}加入群聊“${groupName}”的申请。' WHERE id=7;
UPDATE eh_locale_templates SET `text`='管理员${operatorName}已拒绝您加入群聊“${groupName}”的申请。' WHERE id=8;
UPDATE eh_locale_templates SET `text`='您已拒绝${userName}加入群聊“${groupName}”的申请。' WHERE id=9;
UPDATE eh_locale_templates SET `text`='管理员${operatorName}已拒绝${userName}加入群聊“${groupName}”的申请。' WHERE id=10;
UPDATE eh_locale_templates SET `text`='${operatorName}邀请您加入了群聊“${groupName}”，您可以在群聊内留便条或者发帖了！' WHERE id=11;
UPDATE eh_locale_templates SET `text`='您邀请${userName}加入了群聊“${groupName}”，请耐心等待对方回复。' WHERE id=12;
UPDATE eh_locale_templates SET `text`='${operatorName}邀请${userName}加入了群聊“${groupName}”。' WHERE id=13;
UPDATE eh_locale_templates SET `text`='您邀请${userName}加入群聊“${groupName}”，请耐心等待对方回复。' WHERE id=14;
UPDATE eh_locale_templates SET `text`='${operatorName}邀请您加入群聊“${groupName}”，您同意加入吗？' WHERE id=15;
UPDATE eh_locale_templates SET `text`='${operatorName}正在邀请${userName}加入群聊“${groupName}”。' WHERE id=16;
UPDATE eh_locale_templates SET `text`='${userName}同意您的邀请，已加入群聊“${groupName}”。' WHERE id=17;
UPDATE eh_locale_templates SET `text`='您已加入群聊“${groupName}”，可以在群聊内聊天、分享了！' WHERE id=18;
UPDATE eh_locale_templates SET `text`='${userName}已接受${operatorName}的邀请，加入了群聊“${groupName}”。' WHERE id=19;
UPDATE eh_locale_templates SET `text`='${userName}已拒绝加入群聊“${groupName}”的邀请。' WHERE id=20;
UPDATE eh_locale_templates SET `text`='您已拒绝加入群聊“${groupName}”的邀请。' WHERE id=21;
UPDATE eh_locale_templates SET `text`='${userName}已拒绝${operatorName}加入群聊“${GROUP_NAME}”的邀请。' WHERE id=22;
UPDATE eh_locale_templates SET `text`='您已退出群聊“${groupName}”。' WHERE id=23;
UPDATE eh_locale_templates SET `text`='${userName}已退出群聊“${groupName}”。' WHERE id=24;
UPDATE eh_locale_templates SET `text`='您已被${operatorName}请出了群聊“${groupName}”。' WHERE id=25;
UPDATE eh_locale_templates SET `text`='您已将${userName}请出群聊“${groupName}”。' WHERE id=26;
UPDATE eh_locale_templates SET `text`='${operatorName}已将${userName}请出了群聊“${groupName}”。' WHERE id=27;
UPDATE eh_locale_templates SET `text`='您正在申请成为群聊“${groupName}”的管理员，请耐心等待审核通过。' WHERE id=28;
UPDATE eh_locale_templates SET `text`='${userName}正在申请成为群聊“${groupName}”的管理员，您同意此申请吗？' WHERE id=29;
UPDATE eh_locale_templates SET `text`='您已成为群聊“${groupName}”的管理员。' WHERE id=30;
UPDATE eh_locale_templates SET `text`='您已通过${userName}成为群聊“${groupName}”管理员的申请。' WHERE id=31;
UPDATE eh_locale_templates SET `text`='管理员${operatorName}已通过${userName}成为群聊“${groupName}”管理员的申请。' WHERE id=32;
UPDATE eh_locale_templates SET `text`='管理员${operatorName}已拒绝您成为群聊“${groupName}”管理员的申请。' WHERE id=33;
UPDATE eh_locale_templates SET `text`='您已拒绝${userName}成为群聊“${groupName}”管理员的申请。' WHERE id=34;
UPDATE eh_locale_templates SET `text`='管理员${operatorName}已拒绝${userName}成为群聊“${groupName}”的管理员的申请。' WHERE id=35;
UPDATE eh_locale_templates SET `text`='您正邀请${userName}成为群聊“${groupName}”管理员，请耐心等待对方同意。' WHERE id=36;
UPDATE eh_locale_templates SET `text`='${operatorName}邀请您成为群聊“${groupName}”的管理员，您同意此邀请吗？' WHERE id=37;
UPDATE eh_locale_templates SET `text`='您已接受${operatorName}的邀请，现成为群聊“${groupName}”的管理员。' WHERE id=38;
UPDATE eh_locale_templates SET `text`='${userName}已接受您的邀请，现成为群聊“${groupName}”的管理员。' WHERE id=39;
UPDATE eh_locale_templates SET `text`='${userName}已接受${operatorName}的邀请，现成为群聊“${groupName}”的管理员。' WHERE id=40;
UPDATE eh_locale_templates SET `text`='您已拒绝担任群聊“${groupName}”的管理员。' WHERE id=41;
UPDATE eh_locale_templates SET `text`='${userName}已拒绝担任群聊“${groupName}”的管理员。' WHERE id=42;
UPDATE eh_locale_templates SET `text`='${userName}已拒绝担任群聊“${groupName}”的管理员。' WHERE id=43;
UPDATE eh_locale_templates SET `text`='您已辞去群聊“${groupName}”的管理员身份。' WHERE id=44;
UPDATE eh_locale_templates SET `text`='${userName}已辞去群聊“${groupName}”的管理员身份。' WHERE id=45;
UPDATE eh_locale_templates SET `text`='您已解除${userName}在群聊“${groupName}”的管理员身份。' WHERE id=46;
UPDATE eh_locale_templates SET `text`='${operatorName}已解除您在群聊“${groupName}”的管理员身份。' WHERE id=47;
UPDATE eh_locale_templates SET `text`='${operatorName}已解除${userName}在群聊“${groupName}”的管理员身份。' WHERE id=48;
UPDATE eh_locale_templates SET `text`='${operatorName}已经邀请您成为群聊“${groupName}”的管理员了。' WHERE id=49;
UPDATE eh_locale_templates SET `text`='您已邀请${userName}成为群聊“${groupName}”的管理员了。' WHERE id=50;
UPDATE eh_locale_templates SET `text`='${operatorName}已经邀请${userName}成为群聊“${groupName}”的管理员了。' WHERE id=51;
UPDATE eh_locale_templates SET `text`='您已订阅俱乐部 “${groupName}”' WHERE id=92;
UPDATE eh_locale_templates SET `text`='俱乐部“${groupName}”人数有变化' WHERE id=93;
UPDATE eh_locale_templates SET `text`='${userName}已删除群聊“${groupName}”' WHERE id=94;
UPDATE eh_locale_templates SET `text`='您已删除群聊“${groupName}”' WHERE id=95;
UPDATE eh_locale_templates SET `text`='您已取消订阅俱乐部“${groupName}”' WHERE id=96;
UPDATE eh_locale_templates SET `text`='俱乐部“${groupName}”推荐' WHERE id=107;




-- 修改items图片

delete from eh_launch_pad_items where  item_group ='GaActions' and namespace_id  = 0 and item_label  like "%缴费%";
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (8, 0, 0, '0', 0, '/home/Pm', 'GaActions', 'HELP', '咨询求助', 'cs://1/image/aW1hZ2UvTVRvMFlqazJOalppWkRjME4yTXpOVEE1TlRnNFlqRTFNemMwWXpjeVlqTTNZUQ', 1, 1, 19, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27}', 0, 0, 1, 1, '', 0,NULL);	

delete from eh_launch_pad_items where id= 6;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('6', '0', '0', '0', '0', '/home/Pm', 'GaActions', 'HELP', '紧急求助', 'cs://1/image/aW1hZ2UvTVRvNE9XSTFaakU1WWprNE4yVTFPV0pqT1dNMk9XWXlPRGN5WVdFMU1qUXpaZw', '1', '1', '19', '{\"contentCategory\":1012,\"actionCategory\":0,\"forumId\":1,\"targetEntityTag\":\"PM\",\"embedAppId\":27}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default');

delete from eh_launch_pad_items where id=17;
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (25, 0, 0, '0', 0, '/home/Pm', 'GaPosts', 'HELP', '咨询求助', NULL, 1, 1, 15, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"embedAppId":27} ', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (17, 0, 0, '0', 0, '/home/Pm', 'GaPosts', 'HELP', '紧急求助', NULL, 1, 1, 15, '{"contentCategory":1012,"actionCategory":0,"forumId":1,"embedAppId":27} ', 0, 0, 1, 1, '', 0,NULL);
		
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMVl6aGlOekkxT0dGbVlXSTBZbVZtTURjMU5EQmtOMkU0WmpCbU1tSXpOdw' where item_group = 'GaActions' and item_label like "%投诉建议%";
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwaFpqUmxNRGd6WkRJME1URmtNMlJtWm1Sak1USmtNamN6WTJJelltSTJZUQ' where item_group = 'GaActions' and item_label like "%报修%";
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMFlqazJOalppWkRjME4yTXpOVEE1TlRnNFlqRTFNemMwWXpjeVlqTTNZUQ' where item_group = 'GaActions' and item_label like "%咨询求助%" ;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvM1lqZGpObUUyTlRSaE5tUmxOamhsT1RJNVpEQTBaR0ZpTlRrMU16VTNaUQ' where item_group = 'GaActions' and item_label like "%缴费%" ;

-- add by lqs 20160428
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.all', 'cs://1/image/aW1hZ2UvTVRveVlUTTNPR1ZqWW1ZelltWXpOR1F3TlRBMVkySTRNMlpqWlRVM09EUmlNUQ', '帖子菜单默认头像：全部');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.community_only', 'cs://1/image/aW1hZ2UvTVRwaE1HSmhaR0V5TWpWbFkyVm1ZakkxT0RKak9ETTVZemN6TnpFeU56bGhOZw', '帖子菜单默认头像：本小区');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.community_nearby', 'cs://1/image/aW1hZ2UvTVRvMllURTVZekJsTmpRelpXUXdabUZtTXpBM1lUWTNZVEZqT0dSbU9EbGlOZw', '帖子菜单默认头像：周边小区');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.organization', 'cs://1/image/aW1hZ2UvTVRvM1pUWXlaVEExTmpOaE5qWXhNVFEzWmpabE5ERmpPRFV4TkRWaE56RTJOUQ', '帖子菜单默认头像：公司');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.group', 'cs://1/image/aW1hZ2UvTVRwaE5HWTJaV0ptWlRJek4yTmtPR0UyTlRaaU5UQTNOekpqT0RsaU1UZzFaQQ', '帖子菜单默认头像：兴趣圈');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'user.avatar.system_assistant', 'cs://1/image/aW1hZ2UvTVRwbU1ERXlaV05rWWpNME1UWTBaREJtWldFMVpUQmlPR0U1TVdJeVlXSmtaZw', '系统小助手默认头像');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000795, UUID(), '深业物业','', 1, 0, 240111044331051300, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179902, 1, 999992); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179902, UUID(), 999992, 2, 'EhGroups', 1000795,'深业物业论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000795 where id=1000750;


INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000796, UUID(), '深圳科技工业园','', 1, 0, 240111044331048623, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179903, 1, 1000000); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179903, UUID(), 1000000, 2, 'EhGroups', 1000796,'深圳科技工业园论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000796 where id=1000001;	

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000797, UUID(), '讯美科技','', 1, 0, 240111044331049963, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179904, 1, 999999); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179904, UUID(), 999999, 2, 'EhGroups', 1000797,'讯美科技论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000797 where id=1000100;	

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000798, UUID(), '龙岗智慧社区','', 1, 0, 240111044331050395, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179905, 1, 999994); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179905, UUID(), 999994, 2, 'EhGroups', 1000798,'龙岗智慧论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000798 where id=1000531;

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000799, UUID(), '海岸物业','', 1, 0, 240111044331050812, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179906, 1, 999993); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179906, UUID(), 999993, 2, 'EhGroups', 1000799,'海岸物业论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000799 where id=1000631;
 