-- bydengs,20171114 物业报修2.9.3
SET @eh_categories_id = (SELECT MAX(id) FROM `eh_categories`);
SET @namespace_id = 999957;
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES
((@eh_categories_id:=@eh_categories_id+1), '6', '0', '物业报修', '任务/物业报修', '-1', '2', now(), NULL, NULL, NULL, @namespace_id);

SET @namespace_id = 999957;
DELETE  FROM  eh_configurations WHERE namespace_id = 0 AND `name`=CONCAT('pmtask.handler-',@namespace_id);
INSERT INTO `eh_configurations` (`name`, `value`,`description`) VALUES (CONCAT('pmtask.handler-',@namespace_id), 'yue_kong_jian','越空间物业报修的handler');


-- added by Janson 20171128
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)VALUES (1009, 0, '禁止门禁访客访问', '禁止门禁访客访问', NULL);

-- "我的"里面的菜单，一部分是默认的，一部分是杭州项目的 add by yanjun 20171129
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('1', '0', '我的收藏', '/activity/build/index.html#/myFavorite', NULL, NULL, '1', '1', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('2', '0', '我的报名', '/activity/build/index.html#/mySignup', NULL, NULL, '1', '2', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('3', '0', '查看我的团队/企业', '/enter-apply/build/index.html#/applyRecord', NULL, NULL, '2', '3', '2');

INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('11', '999957', '我的收藏', '/activity/build/index.html#/myFavorite', NULL, NULL, '1', '1', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('12', '999957', '我的报名', '/activity/build/index.html#/mySignup', NULL, NULL, '1', '2', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('13', '999957', '会员中心', '/service-hub/build/index.html#/waiting/101', NULL, NULL, '1', '3', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('14', '999957', '意见反馈', '/service-hub/build/index.html#/waiting/102', NULL, NULL, '1', '4', '2');
