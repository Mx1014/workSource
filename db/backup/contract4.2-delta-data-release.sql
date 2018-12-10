SET @eh_configurations = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ((@eh_configurations := @eh_configurations + 1), 'schedule.contractstatics.cronexpression', '0 30 2 * * ?', '合同报表定时任务', '0', NULL, '1');


-- AUTHOR:丁建民 20180920
-- REMARK: issue-37007 资产设置一房一价，租赁价格大于设定的价格，则合同不需要审批
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '30001', 'zh_CN', '请输入正确的查询时间');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '30002', 'zh_CN', '请输入查询项目');
