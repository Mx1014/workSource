
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 优化 测试环境设置访问微信代理
-- AUTHOR: 严军
-- REMARK: IP代理
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wechat.proxy.host', '121.199.69.107', '访问微信的代理服务器IP', '0', NULL, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wechat.proxy.port', '6657', '访问微信的代理服务器端口', '0', NULL, NULL);

-- ENV: ALL
-- DESCRIPTION: 小猫对应的临时停车场用于测试
-- AUTHOR:dengs
-- REMARK:银星停车场对接配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.yinxingzhijietechpark.url', 'http://120.77.237.179:8090', '测试银星科技园停车场url', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.yinxingzhijietechpark.parkId', '00000222221531882001', '测试银星科技园停车场id', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.yinxingzhijietechpark.accessKeyId', 'yinxingtest', '测试银星科技园停车场访问者标识', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.yinxingzhijietechpark.accessKeyValue', '6d8c88c487ac7eb54ee4f05969f8d083', '测试银星科技园停车场加密后的-accessKeyValue', '0', NULL, '1');

-- --------------------- SECTION END ---------------------------------------------------------