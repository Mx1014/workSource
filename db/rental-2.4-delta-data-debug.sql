
-- 资源预订工作流模板，add by wh, 20161219
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES (@id:=@id+1, 'rental.flow', 1, 'zh_CN', '工作流列表内容', '资源名称：${resourceName}使用时间：${useDetail}', 0);

-- 资源预订工作流中文，added by wh ,2016-12-19
SET @id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'user', 'zh_CN', '发起人');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'contact', 'zh_CN', '联系电话');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'organization', 'zh_CN', '企业');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'resourceName', 'zh_CN', '资源名称');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'useDetail', 'zh_CN', '使用时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'count', 'zh_CN', '预约数量');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'price', 'zh_CN', '订单金额');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'item', 'zh_CN', '购买商品');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'content', 'zh_CN', '显示内容');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'license', 'zh_CN', '车牌');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'remark', 'zh_CN', '备注');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'attachment', 'zh_CN', '附件'); 