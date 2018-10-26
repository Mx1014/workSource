-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 对接国贸报错信息 20181022
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10026', 'zh_CN', '用户不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10027', 'zh_CN', '初始化失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10028', 'zh_CN', '获取数据失败');

-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 支持多应用服务类型 20181025
INSERT INTO `eh_pm_task_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('6', '0', '0', '物业报修', '物业报修', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '0', NULL, '0');
INSERT INTO `eh_pm_task_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('9', '0', '0', '投诉建议', '投诉建议', '0', '2', '2017-12-04 13:09:45', NULL, NULL, NULL, '0', NULL, '0');
