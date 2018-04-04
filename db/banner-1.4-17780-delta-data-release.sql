-- 广告管理 v1.4    add by xq.tian  2018/03/07
SET @eh_locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
    VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'banner', '10004', 'zh_CN', '跳转数据处理失败');


INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
    VALUES (20400, '启动广告', '20000', '/20000/20400', '1', '2', '2', '0', NOW(), null, '13', NOW(), '0', '0', '0', '0','unlimit_control');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
	VALUES (2040020400, NULL, '启动广告 全部权限', '启动广告 全部权限', NULL);

SET @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
	VALUES (@mp_id:=@mp_id+1, 20400, '0', 2040020400, '启动广告 全部权限', '0', NOW());