-- 资源预订工作流模板，add by wh, 20161219
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES (@id:=@id+1, 'rental.flow', 2, 'zh_CN', '自定义字段', '请等待${offlinePayeeName}（${offlinePayeeContact}）上门收费，或者到${offlineCashierAddress}去支付', 0);

-- 资源预订短信模板，add by wh, 20161219
SET @id =(SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','28','zh_CN','资申成-正中会','38570','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','29','zh_CN','资申败-正中会','38572','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','30','zh_CN','资付成-正中会','38573','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','31','zh_CN','资预败-正中会','38574','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','32','zh_CN','资取消-正中会','38575','999983');

-- 文案早上改成上午
UPDATE eh_locale_strings SET TEXT = '上午' WHERE scope ='rental.notification' AND CODE = 0 AND locale = 'zh_CN';