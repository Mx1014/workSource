-- merge doorAuth by sfyan 20170112
-- 运营统计的数据修改 add sfyan 20170112
UPDATE `eh_terminal_day_statistics` SET `start_change_rate` = `start_change_rate` * -1, `new_change_rate` = `new_change_rate` * -1, `active_change_rate` = `active_change_rate` * -1, `cumulative_change_rate` = `cumulative_change_rate` * -1;

-- 重复审批提示 add sfyan 20170114
SELECT max(id) FROM eh_locale_strings INTO @max_id;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@max_id := @max_id + 1), 'organization', '500005', 'zh_CN', '已被其他管理员审批！');