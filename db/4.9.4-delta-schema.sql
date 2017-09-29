-- 同步数据时为企业添加门牌时太慢，添加索引 add by xiongying20170922
ALTER TABLE `eh_addresses` ADD INDEX namespace_address ( `namespace_address_type`, `namespace_address_token`);

-- by wentian
ALTER TABLE `eh_payment_order_records` ADD COLUMN `order_num` varchar(255) DEFAULT NULL COMMENT '订单编号';
ALTER TABLE `eh_payment_service_configs` modify COLUMN `payment_split_rule_id` bigint default null;

-- by sun wen 20170929
ALTER TABLE eh_parking_recharge_orders ADD COLUMN paid_version tinyint(4) DEFAULT NULL;
ALTER TABLE eh_rentalv2_orders ADD COLUMN paid_version tinyint(4) DEFAULT NULL;
