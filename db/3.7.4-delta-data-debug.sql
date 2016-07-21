#删除电商相关图标
delete from eh_launch_pad_items where target_type='biz' and scope_code = 0;

#配置电商链接
INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.realm', 'biz', 'business realm');
INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.url', 'https://biz-beta.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz-beta.zuolin.com%2Fnar%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix', 'business url');

#group下增加错误提示
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('group', '10030', 'zh_CN', '您不是群主，无权操作');

#删除左邻小站和优惠券的入口
delete from `eh_launch_pad_items` where id in (809, 10306, 810, 10307);

#添加社区活动的入口，替代原左邻小站和优惠券的位置
INSERT INTO `eh_launch_pad_items` ( `id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
VALUES ( 809, '1000000', '0', '0', '0', '/home', 'GovAgencies', '社区活动', '社区活动', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', '4', '1', '50', '', '1', '0', '1', '1', '', '1', NULL, NULL, NULL, '1', 'default');
		
INSERT INTO `eh_launch_pad_items` ( `id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
VALUES ( 810, '1000000', '0', '0', '0', '/home', 'GovAgencies', '社区活动', '社区活动', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', '4', '1', '50', '', '1', '0', '1', '1', '', '1', NULL, NULL, NULL, '1', 'pm_admin');
