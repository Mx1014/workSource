-- by dengs,2017.08.28 快递2.0
update eh_configurations SET `value`='http://pay-beta.zuolin.com/EDS_PAY/rest/pay_common/payInfo_record/save_payInfo_record' WHERE `name` = 'guomao.payserver.url' AND namespace_id = 999901;
set @eh_configurations_id = (select Max(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id:=@eh_configurations_id+1), 
'debug.flag', 'true', '国贸 debug flag', 0, NULL);
-- by dengs,2017.08.28 快递2.0 end

DELETE from eh_parking_lots where id = 10002;
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 1, \"searchCarFlag\": 0, \"currentInfoType\": 1, \"contact\": \"13632650699\"}' WHERE `id`='10001';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 0, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"13510551322\"}' WHERE `id`='10003';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"18927485550\"}' WHERE `id`='10004';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 1, \"currentInfoType\": 2, \"contact\": \"18718523489\"}', expired_recharge_json='{"expiredRechargeFlag":1, "maxExpiredDay":365, "expiredRechargeMonthCount":1, "expiredRechargeType":1}' WHERE `id`='10006';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"13918348877\"}' WHERE `id`='10021';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"18051307125\"}' WHERE `id`='10023';
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `expired_recharge_json`, `config_json`)
  VALUES ('10011', 'community', '240111044331050370', '软件产业基地停车场', 'XIAOMAO', NULL, '2', '1025', '2016-12-16 17:07:20', '0', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 1, \"searchCarFlag\": 0, \"currentInfoType\": 1, \"contact\": \"18665331243\"}');
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `expired_recharge_json`, `config_json`)
  VALUES ('10012', 'community', '240111044331050371', '创投大厦停车场', 'XIAOMAO', NULL, '2', '1025', '2016-12-16 17:07:20', '0', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 1, \"searchCarFlag\": 0, \"currentInfoType\": 1, \"contact\": \"18665331243\"}');
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `expired_recharge_json`, `config_json`)
  VALUES ('10013', 'community', '240111044331050369', '生态园停车场', 'Mybay', NULL, '2', '1025', '2016-12-16 17:07:20', '0', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 2, \"contact\": \"18665331243\"}');
