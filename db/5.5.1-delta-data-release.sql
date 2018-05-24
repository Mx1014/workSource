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