
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V7.5
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10022', 'zh_CN', '此账单不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10023', 'zh_CN', '该账单不是已出账单');

-- AUTHOR: 缪洲
-- REMARK: issue-43583 【启迪香山】对接停车缴费功能上线正式环境
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.url', 'http://openapi.daodingtech.com:51000', '启迪香山URL', 0, NULL, 0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.code', 'qdxs001', '启迪香山code', 0, NULL, 0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.key', 'jtq231j2sfisbgsy', '启迪香山key', 0, NULL, 0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.secret', 'ed62d4335a294932849415a4cc171e8c', '启迪香山secret', 0, NULL, 0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@id+1, 'parking.qididaoding.parkingId', '0755000212018070500000000000', '启迪香山parkingId', 0, NULL, 0);
SET @id = (SELECT MAX(id) from eh_parking_lots);
INSERT INTO `eh_parking_lots`(`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`, `notice_contact`, `summary`, `default_data`, `default_plate`) VALUES (@id+1, 'community', 240111044332060091, '启迪香山停车场', 'QIDI_DAODING', '', 2, 1, '2018-05-21 14:36:29', 999959, NULL, '{\"tempfeeFlag\":1,\"rateFlag\":1,\"lockCarFlag\":1,\"searchCarFlag\":1,\"currentInfoType\":2,\"invoiceFlag\":0,\"businessLicenseFlag\":0,\"vipParkingFlag\":0,\"monthRechargeFlag\":1,\"identityCardFlag\":0,\"monthCardFlag\":1,\"flowMode\":3,\"noticeFlag\":0,\"invoiceTypeFlag\":0}', '042', 1, 'nsY8Rra1ahF45J0W-llr', '[\"vipParking\",\"tempfee\",\"monthRecharge\",\"freePlace\",\"invoiceApply\",\"userNotice\",\"lockCar\",\"searchCar\",\"monthCardApply\"]', '', '', 'identity,driver,driving', '粤,B');
