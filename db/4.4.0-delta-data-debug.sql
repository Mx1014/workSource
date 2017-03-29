-- 用户管理 add by sw 20170329
delete from eh_web_menus where id = 34000;
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('34000', '用户管理', '30000', NULL, 'user--user_management/1', '0', '2', '/30000/34000', 'park', '340', '34000');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('34100', '用户管理', '30000', NULL, 'user--user_management/0', '0', '2', '/30000/34100', 'park', '340', '34000');
	
	
