-- 批量导入账单的显示控制 by xiongying20180413
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('95', '20400', '0', '批量导入账单');

SET @exclude_id = (SELECT MAX(id) FROM `eh_service_module_exclude_functions`);
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999971', NULL, '20400', '95');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '20400', '95');

