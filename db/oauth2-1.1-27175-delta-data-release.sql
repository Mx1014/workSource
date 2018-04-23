-- 广告管理 v1.4    add by xq.tian  2018/03/07
SET @eh_locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
    VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'banner', '10004', 'zh_CN', '跳转数据处理失败');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
    VALUES (10900, '启动广告', '10000', '/10000/10900', '1', '2', '2', '0', NOW(), null, '13', NOW(), '0', '0', '0', '0','unlimit_control');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
	VALUES (1090010000, NULL, '启动广告 全部权限', '启动广告 全部权限', NULL);

SET @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
	VALUES (@mp_id:=@mp_id+1, 10900, '0', 1090010900, '启动广告 全部权限', '0', NOW());

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`)
    VALUES (16021500, '启动广告', 16020000, NULL, 'startup-advert', 1, 2, '/16000000/16020000/16021500', 'zuolin', 15, 10900, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`)
    VALUES (41070000, '启动广告', 41000000, NULL, 'startup-advert', 1, 2, '/41000000/41070000', 'park', 15, 10900, 2, 'system', 'module', NULL);


INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`)
    VALUES (11040000, '第三方应用管理', 11000000, NULL, 'other-apps', 1, 2, '/11000000/11040000', 'zuolin', 15, NULL, 2, 'system', 'module', NULL);


SET @eh_locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
    VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'oauth2', '10000', 'zh_CN', '用户名或密码错误');