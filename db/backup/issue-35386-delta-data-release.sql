-- 圳智慧beta
-- addy yanlong.liang 20180814
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('zhenzhihui.url', 'http://120.132.117.22:8016/ZHYQ/restservices/LEAPAuthorize/attributes/query?TICKET=', '圳智慧认证url', '999931', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('zhenzhihui.appkey', 'c00f02aac30d0822', '圳智慧APPKEY', '999931', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('zhenzhihui.redirect.url', 'http://120.132.117.22:8016/ZHYQ/restservices/common/zhyq_zlgetUserinfo/query?code=', '圳智慧对接跳转url', '999931', NULL, '1');