-- 薪酬2.2 begin
-- added by wh :薪酬工资条发放消息
SET @template_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'salary.notification', 1, 'zh_CN', '后台发工资条', '${salaryDate} 工资已发放。', 0);

-- added by wh 薪酬的模块对应action_type 改为74(工资条)
UPDATE eh_service_modules SET action_type = 74 WHERE id = 51400;
-- 薪酬2.2 end

-- issue-30235 新增模块 "我的钥匙" by liuyilin 20180524
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `instance_config`, `action_type`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('42000', '我的钥匙', '40000', '/10000/42000', '1', '2', '2', '10', '{\"isSupportQR\":1,\"isSupportSmart\":0}', 76, '0', '0', '0', '0', 'community_control');

-- ------------------------------
-- 工作流2.6     add by xq.tian  2018/04/24
-- ------------------------------
SET @eh_configurations_id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name)
VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'flow.stepname.suspend_step', '暂缓', 'suspend_step', 0, NULL);

INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name)
VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'flow.stepname.abort_suspend_step', '取消暂缓', 'abort_suspend_step', 0, NULL);

SET @eh_locale_strings_id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', 100020, 'zh_CN', '脚本执行超时，请重试');

INSERT INTO eh_locale_strings (id, scope, code, locale, text)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1),'flow', 100021, 'zh_CN', '脚本参数校验失败');

INSERT INTO eh_locale_strings (id, scope, code, locale, text)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1),'flow', 100022, 'zh_CN', '脚本编译失败');

INSERT INTO eh_locale_strings (id, scope, code, locale, text)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1),'flow', 100023, 'zh_CN', '脚本执行失败');


-- 政务服务 1.0 模块Id 41900
-- 新增模块 eh_service_modules
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `multiple_flag`, `module_control_type`) VALUES ('41900', '政务服务', '40000', '/40000/41900', '1', '2', '2', '0', '2018-05-14 14:40:35', '2018-05-14 14:40:46', '0', '0', '0', 'community_control')
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41910', '政策管理', '41900', '/40000/41900/41910', '1', '3', '2', '0', '2018-05-14 14:40:35', '', NULL, '2018-05-14 14:40:46', '0', '0', '', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41920', '查询记录', '41900', '/40000/41900/41920', '1', '3', '2', '0', '2018-05-14 14:40:35', '', NULL, '2018-05-14 14:40:46', '0', '0', '', '0', 'community_control');
-- 新增模块菜单 eh_web_menus
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `data_type`, `leaf_flag`, `path`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('16032200', '政务服务', '16030000', 'policy-service', '1', '/16000000/16030000/16032200', '22', '41900', '3', 'system', 'module')
-- 权限
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) VALUES ('4190041910', '0', '政策管理', '政策管理 全部权限');
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) VALUES ('4190041920', '0', '查询记录', '查询记录 全部权限');
-- 权限中间表
SET @module_privileges_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@module_privileges_id := @module_privileges_id + 1, '41910', '0', '4190041910', '全部权限', '0', '2018-05-16 18:03:11')
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@module_privileges_id := @module_privileges_id + 1, '41920', '0', '4190041920', '全部权限', '0', '2018-05-16 18:04:18')
