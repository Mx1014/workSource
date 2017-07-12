
-- 发短信模板 

SET @id = (SELECT MAX(id) FROM eh_locale_templates) +1;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@id := @id + 1,'sms.default.yzx','12','zh_CN','付费预约成功 独占资源','31934','1000000');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@id := @id + 1,'sms.default.yzx','13','zh_CN','付费预约成功 非独占资源，不需要选择资源编号','32008','1000000');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@id := @id + 1,'sms.default.yzx','14','zh_CN','付费预约成功 非独占资源，需要选择资源编号','32009','1000000');
