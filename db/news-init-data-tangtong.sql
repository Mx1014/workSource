INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`) VALUES(1110, 1, 0, '新闻', '帖子/新闻', 0, 2, UTC_TIMESTAMP());
    
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10001', 'zh_CN', '类型错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10002', 'zh_CN', '公司不存在');