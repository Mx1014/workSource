-- 资源预订工作流模板，add by wh, 20161219
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES (@id:=@id+1, 'rental.flow', 2, 'zh_CN', '自定义字段', '请等待${offlinePayeeName}（${offlinePayeeContact}）上门收费，或者到${offlineCashierAddress}去支付', 0);