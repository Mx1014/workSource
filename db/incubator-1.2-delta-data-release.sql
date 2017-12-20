
-- 文件有效时间 add by yanjun 20171219
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (( @id :=  @id + 1), 'filedownload.valid.interval', '10', 'filedownload valid interval', '0', NULL);


SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10001', 'zh_CN', '任务不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10002', 'zh_CN', '权限不足');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10003', 'zh_CN', '任务已执行完成，不可取消');


-- 文件中心菜单  add by yanjun 20171220
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('61000', '文件中心', '60000', '/60000/61000', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES ('61000', '文件中心', '60000', NULL, 'react:/file-center/file-list', '0', '2', '/60000/61000', 'park', '630', '61000', '2', NULL, 'module');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
SELECT (@menu_scope_id := @menu_scope_id + 1), 61000, '', 'EhNamespaces', id, 2 from eh_namespaces;