-- 快递初始化数据，add by tt, 20170504
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (1, 0, '', 0, 0, 'ems', 'cs://1/image/aW1hZ2UvTVRwak9XSTJOVFJqWXpjMVkyTmtNVGt4WW1NNU1qaGlNR0k1WlRNelpXRTJNdw', 2, 0, now(), now(), 0);
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'express', '10000', 'zh_CN', '订单状态错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'express', '10001', 'zh_CN', '您没有相关权限');
