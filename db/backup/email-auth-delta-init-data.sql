 
-- 邮箱认证发邮件内容模板，add by wh,2016-10-27
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES (@id:=@id+1, 'verify.mail', 1, 'zh_CN', '用户提交加班申请', '尊敬的${nickName}：\n您好，感谢您使用${appName}，点击下面的链接进行邮箱验证：\n${verifyUrl}\n如果链接没有跳转，请直接复制链接地址到您的浏览器地址栏中访问。（30分钟内有效）\n \n此邮件为系统邮件，请勿直接回复。\n \n如非本人操作，请忽略此邮件。\n \n谢谢，${appName}', 0);

-- 邮箱认证发邮件标题，added by wh ,2016-10-27
SET @id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'verify.mail', 'subject', 'zh_CN', '加入企业验证邮件');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '600001', 'zh_CN', '没有此邮箱域名');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '600002', 'zh_CN', '已经过了验证时间(有效期30分钟)');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '600003', 'zh_CN', '该邮箱已被占用');
-- 审批发送消息模板，add by wh, 20161018
SET @id := (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES(@id:=@id+1,'auth.success','http://core.zuolin.com/mobile/static/email_page/success.html','email auth verify success','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES(@id:=@id+1,'auth.fail','http://core.zuolin.com/mobile/static/email_page/fail.html','email auth verify success','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES(@id:=@id+1,'auth.overtime','http://core.zuolin.com/mobile/static/email_page/overtime.html','email auth verify success','0',NULL);

