-- 所有环境
-- added by wh 2018-6-12  :薪酬工资条发放消息
SET @template_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'welfare.msg', 1, 'zh_CN', '发福利消息', '$你收到了${subject},快去查看吧!', 0);

-- end