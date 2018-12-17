-- AUTHOR:吴寒
-- REMARK:给卡券对接字段扩容
ALTER TABLE eh_welfare_coupons CHANGE `service_supply_name` `service_supply_name` VARCHAR(4096) COMMENT '适用地点';
ALTER TABLE eh_welfare_coupons CHANGE `service_range` `service_range` VARCHAR(4096) COMMENT '适用范围';