-- 级别全部加1
-- 级别全部加1
UPDATE eh_service_modules set `level` = `level` + 1;

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('100', '企业办公', '0', '/100', '1', '1', '2', '10', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('200', '园区运营', '0', '/200', '1', '1', '2', '20', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('300', '企业服务', '0', '/300', '1', '1', '2', '30', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');

-- 更新小类path
UPDATE eh_service_modules SET path = CONCAT('/100',path) WHERE parent_id <> 0 AND path like '%/50000/%';
UPDATE eh_service_modules SET path = CONCAT('/200',path) WHERE parent_id <> 0 AND path NOT like '%/50000/%';

-- 更新大类
UPDATE eh_service_modules SET parent_id = 100, path = CONCAT('/100',path) WHERE  parent_id = 0 AND id = 50000;
UPDATE eh_service_modules SET parent_id = 200, path = CONCAT('/200',path) WHERE parent_id = 0 AND id NOT IN(50000, 100, 200, 300);


-- oa分类
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('160000', '人力资源', '0', '/100/160000', '1', '1', '2', '30', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');

-- 企业公告 分到人力资源
UPDATE eh_service_modules set parent_id = 160000, path = '/100/160000/57000' where id = 57000;




-- 菜单

-- 菜单

-- 级别全部加1
UPDATE eh_web_menus set `level` = `level` + 1 WHERE id > 40000000 and id <69000000;

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('40000010', '企业办公', '0', NULL, NULL, '1', '2', '/40000010', 'park', '10', NULL, '1', 'system', 'classify', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('40000020', '园区运营', '0', NULL, NULL, '1', '2', '/40000020', 'park', '20', NULL, '1', 'system', 'classify', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('40000030', '企业服务', '0', NULL, NULL, '1', '2', '/40000030', 'park', '30', NULL, '1', 'system', 'classify', '2');


-- 更新小类path
UPDATE eh_web_menus SET path = CONCAT('/40000010',path) WHERE parent_id <> 0 AND  id > 40000000 and id <69000000 and path like '%/48000000/%';
UPDATE eh_web_menus SET path = CONCAT('/40000020',path) WHERE parent_id <> 0 AND  id > 40000000 and id <69000000 AND path NOT like '%/48000000/%';

-- 更新大类
UPDATE eh_web_menus SET parent_id = 40000010, path = CONCAT('/40000010',path) WHERE  parent_id = 0  AND  id > 40000000 and id <69000000 AND id = 48000000;
UPDATE eh_web_menus SET parent_id = 40000020, path = CONCAT('/40000020',path) WHERE parent_id = 0  AND  id > 40000000 and id <69000000 AND id NOT IN(48000000, 40000010, 40000020, 40000030);



-- oa分类
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('53000000', '人力资源', '40000010', NULL, NULL, '1', '2', '/40000010/53000000', 'park', '30', NULL, '2', 'system', 'classify', '2');

-- 企业公告 分到人力资源
UPDATE eh_web_menus set parent_id = 53000000, path = '/40000010/53000000/48160000' where id = 48160000;





update eh_portal_layout_templates set status = 0 where id in (3, 4, 5) and label like '%主页签门户%';

UPDATE eh_portal_layout_templates set template_json = '{"groups":[{"label":"应用", "separatorFlag":"0", "separatorHeight":"0","widget":"Navigator","style":"Gallery","instanceConfig":{"margin":20,"padding":16,"backgroundColor":"#ffffff","titleFlag":0,"title":"标题","titleUri":""},"defaultOrder":3,"description":""}]}' WHERE id = 1 and label = '门户';
