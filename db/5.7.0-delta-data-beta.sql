-- 通用脚本
-- ADD BY jun.yan
-- ISSUE 优化 测试环境设置访问微信代理
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wechat.proxy.host', '121.199.69.107', '访问微信的代理服务器IP', '0', NULL, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wechat.proxy.port', '6657', '访问微信的代理服务器端口', '0', NULL, NULL);
-- END