SET @eh_configurations = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ((@eh_configurations := @eh_configurations + 1), 'schedule.contractstatics.cronexpression', '0 30 2 * * ?', '合同报表定时任务', '0', NULL, '1');
