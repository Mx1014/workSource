-- 埋点类型 服务联盟v3.4 huangmingbo 2018.08.03
SET @event_display_name = '服务联盟埋点统计';
SET @event_name = 'service_alliance_click';
SET @eh_stat_events_id = (SELECT MAX(id) FROM eh_stat_events);
INSERT INTO eh_stat_events (id, namespace_id, event_scope, event_type, event_name, event_version, event_display_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_events_id := @eh_stat_events_id + 1), 0, 1, 1, @event_name, '1.0', @event_display_name, 2, null, null, null, null);

-- 埋点参数
SET @eh_stat_event_params_id = (SELECT MAX(id) FROM eh_stat_event_params);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'service_id', 'service_id', 2, null, null, null, null);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'category_id', 'category_id', 2, null, null, null, null);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'user_id', 'user_id', 2, null, null, null, null);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'click_type', 'click_type', 2, null, null, null, null);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'community_id', 'community_id', 2, null, null, null, null);

-- 错误信息
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11200', 'zh_CN', '获取统计信息时type为null');

-- 权限
SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id+1), 40550, 0, 4050040550, '用户行为统计', 0, '2018-03-31 22:18:44');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040550, 0, '服务联盟 用户行为统计', '服务联盟 用户行为统计', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES (40550, '用户行为统计权限', 40500, '/200/120000/40500/40550', 1, 4, 2, 3, '2018-03-31 22:18:44', NULL, NULL, '2018-03-31 22:18:44', 0, 1, '1', NULL, '', 1, 1, 'subModule');
