-- 薪酬2.2 begin
-- added by wh :薪酬工资条发放消息
SET @template_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'salary.notification', 1, 'zh_CN', '后台发工资条', '${salaryDate} 工资已发放。', 0);

-- added by wh 薪酬的模块对应action_type 改为74(工资条)
UPDATE eh_service_modules SET action_type = 74 WHERE id = 51400;
-- 薪酬2.2 end

-- issue-30235 新增模块 "我的钥匙" by liuyilin 20180524
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `instance_config`, `action_type`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('42000', '我的钥匙', '40000', '/10000/42000', '1', '2', '2', '10', '{\"isSupportQR\":1,\"isSupportSmart\":0}', 76, '0', '0', '0', '0', 'community_control');

-- issue-30573 路福门禁切换成zuolin_v2 by liuyilin 20180521
SET @var_id = (SELECT MAX(`id`) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@var_id:=@var_id+1, 'aclink.qr_driver_zuolin_inner', 'zuolin_v2', 'use version2 of zuolin driver', '999963', NULL);