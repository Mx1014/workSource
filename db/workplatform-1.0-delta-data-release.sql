-- 级别全部加1
UPDATE eh_service_modules set `level` = `level` + 1;

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('100', '企业办公', '0', '/100', '1', '1', '2', '10', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('200', '园区运营', '0', '/200', '1', '1', '2', '20', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('300', '企业服务', '0', '/300', '1', '1', '2', '30', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');

-- 更新小类path
UPDATE eh_service_modules SET path = CONCAT('/100',path) WHERE parent_id <> 0 AND path like '/50000/%';
UPDATE eh_service_modules SET path = CONCAT('/200',path) WHERE parent_id <> 0 AND path NOT like '/50000/%';

-- 更新大类
UPDATE eh_service_modules SET parent_id = 100, path = CONCAT('/100',path) WHERE  parent_id = 0 AND id = 50000;
UPDATE eh_service_modules SET parent_id = 200, path = CONCAT('/200',path) WHERE parent_id = 0 AND id NOT IN(50000);



-- oa分类
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('160000', '人力资源', '0', '/100/160000', '1', '1', '2', '30', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');

-- 企业公告 分到人力资源
UPDATE eh_service_modules set parent_id = 160000, path = '/100/160000/57000' where id = 57000;


