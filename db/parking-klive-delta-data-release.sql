-- 广兴源停车场基本配置 ，修改联系人
INSERT INTO `ehcore`.`eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_code`, `order_tag`) VALUES ('10026', 'community', '24011104433104862', '光大we谷停车场', 'GUANG_DA_WE_GU', '', '2', '1', '2018-04-04 16:34:58', '1000000', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0,\"identityCardFlag\":0}', '0', '026');
-- 接口配置，trade_code,秘钥配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.guangxinyuan.url', 'http://187k01282j.iask.in', '停车广兴源url', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.guangxinyuan.trade_code', 'YYZZ_GXYS4672_GXYT7374', '停车广兴源trade_code', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.guangxinyuan.secretKey', 'YYZZ_GXYS4672_GXYT7374', '停车广兴源秘钥', 0, NULL);
