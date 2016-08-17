INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10001', 'zh_CN', '有机构正在使用此分类');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10002', 'zh_CN', '该类型已删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10003', 'zh_CN', '该服务联盟企业记录不存在');

-- 富文本default数据
INSERT INTO `eh_rich_texts` (`id`, `namespace_id`, `owner_id`, `owner_type`, `resource_type`, `content_type`, `content`, `create_time`)
    VALUES (1, '0', '0', 'DEFAULT', 'about', 'richtext', '', UTC_TIMESTAMP());
INSERT INTO `eh_rich_texts` (`id`, `namespace_id`, `owner_id`, `owner_type`, `resource_type`, `content_type`, `content`, `create_time`)
    VALUES (2, '0', '0', 'DEFAULT', 'introduction', 'richtext', '', UTC_TIMESTAMP());
INSERT INTO `eh_rich_texts` (`id`, `namespace_id`, `owner_id`, `owner_type`, `resource_type`, `content_type`, `content`, `create_time`)
    VALUES (3, '0', '0', 'DEFAULT', 'agreement', 'link', '', UTC_TIMESTAMP());
    
update eh_yellow_pages a set integral_tag2 = (select id from eh_categories where name = a.string_tag3 and namespace_id=(select namespace_id from eh_communities where id = a.owner_id) and parent_id = 100001);
