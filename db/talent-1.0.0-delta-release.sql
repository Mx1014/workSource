-- 错误提示，add by tt, 20170527
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'talent', '1', 'zh_CN', '本功能仅对企业管理员开放，如有需要请联系企业管理员');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'talent', '2', 'zh_CN', '分类名称不能重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'parameters.error', '10002', 'zh_CN', '参数长度不足');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'parameters.error', '10003', 'zh_CN', '参数不能为空');

-- 默认头像，add by tt, 20170527
select max(id) into @id from `eh_configurations`;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'talent.male.uri', 'cs://1/image/aW1hZ2UvTVRvM1pUUTJObUV6T0RNM1pEWmhOekl3TldReE5XSXdNbUV5WVRFeU5qZ3hOdw', 'talen default male uri', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'talent.female.uri', 'cs://1/image/aW1hZ2UvTVRwaVpETXdOelE1WWpJMU5tRTFPVEF4T1dJME16UXpNakUxT0dObU5USmhNZw', 'talen default female uri', 0, NULL);
