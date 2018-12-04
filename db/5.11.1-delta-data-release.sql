
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V7.5
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10022', 'zh_CN', '此账单不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10023', 'zh_CN', '该账单不是已出账单');

-- AUTHOR: 缪洲
-- REMARK: issue-43583 【启迪香山】对接停车缴费功能上线正式环境
SET @id = (SELECT MAX(id) from eh_locale_templates);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.url', 'http://openapi.daodingtech.com:51000', '启迪香山URL', 0, NULL, 0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.code', 'qdxs001', '启迪香山code', 0, NULL, 0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.key', 'jtq231j2sfisbgsy', '启迪香山key', 0, NULL, 0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.secret', 'ed62d4335a294932849415a4cc171e8c', '启迪香山secret', 0, NULL, 0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.parkingId', '0755000212018070500000000000', '启迪香山parkingId', 0, NULL, 0);
