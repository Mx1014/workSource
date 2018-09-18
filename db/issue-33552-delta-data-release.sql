-- AUTHOR: 张智伟
-- REMARK: 企业公告分享提示文案
SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'enterprise.notice', '10000', 'zh_CN', '未设置公告分享链接');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'enterprise.notice', '10001', 'zh_CN', '该公告设置了保密');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'enterprise.notice', '10002', 'zh_CN', '该公告已被删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'enterprise.notice', '10003', 'zh_CN', '分享链接已失效');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('enterprise.notice.share.url', '/announcement/?ns=%s&noticeToken=%s', '企业公告分享uri', '0', NULL, '1');