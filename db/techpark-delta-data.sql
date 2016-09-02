-- 深业停车充值
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ('151', 'parking.shenye.projectId', '207', '深业停车充值项目ID', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ('152', 'parking.default.nickname', '默认昵称', '停车充值默认昵称', '0', NULL);

INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`) 
VALUES ('10003', 'community', '240111044331053517', '深业停车场', 'BOSIGAO2', NULL, '1', '2', '1025', '2016-08-29 17:28:10');
