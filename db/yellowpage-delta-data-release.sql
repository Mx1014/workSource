INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10001', 'zh_CN', '有机构正在使用此分类');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10002', 'zh_CN', '该类型已删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10003', 'zh_CN', '该服务联盟企业记录不存在');

-- 富文本default数据
SET @rich_texts_id = (SELECT MAX(id) FROM `eh_rich_texts`);
INSERT INTO `eh_rich_texts` (`id`, `namespace_id`, `owner_id`, `owner_type`, `resource_type`, `content_type`, `content`, `create_time`)
    VALUES ((@rich_texts_id := @rich_texts_id + 1), '0', '0', 'DEFAULT', 'about', 'richtext', '', UTC_TIMESTAMP());
INSERT INTO `eh_rich_texts` (`id`, `namespace_id`, `owner_id`, `owner_type`, `resource_type`, `content_type`, `content`, `create_time`)
    VALUES ((@rich_texts_id := @rich_texts_id + 1), '0', '0', 'DEFAULT', 'introduction', 'richtext', '', UTC_TIMESTAMP());
INSERT INTO `eh_rich_texts` (`id`, `namespace_id`, `owner_id`, `owner_type`, `resource_type`, `content_type`, `content`, `create_time`)
    VALUES ((@rich_texts_id := @rich_texts_id + 1), '0', '0', 'DEFAULT', 'agreement', 'link', '', UTC_TIMESTAMP());