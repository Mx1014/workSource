-- 停车订单编号修改 by dengs,2018.04.27
ALTER TABLE `eh_parking_lots` ADD COLUMN `order_tag` VARCHAR(3) NOT NULL COMMENT '停车场订单生成标识，固定3位';
ALTER TABLE `eh_parking_lots` ADD COLUMN `order_code` BIGINT NOT NULL DEFAULT 0 COMMENT '停车场订单生成码,从0开始，最多8位';

-- 增加发票 by wentian
ALTER TABLE `eh_payment_bills` ADD COLUMN `invoice_number` VARCHAR(128) COMMENT '发票编号';

-- 能耗增加分摊比例  by jiarui 20180416
ALTER TABLE `eh_energy_meter_addresses`
  ADD COLUMN `burden_rate`  decimal(10,2) NULL DEFAULT NULL AFTER `status`;
-- 能耗增表计是否远程自动抄表 by jiarui 20180416
ALTER TABLE `eh_energy_meters`
  ADD COLUMN `auto_flag`  tinyint(4) NOT NULL DEFAULT 0 AFTER `status`;

-- 客户事件设备类型  by jiarui
ALTER TABLE `eh_customer_events`
  ADD COLUMN `device_type`  tinyint(4) NOT NULL DEFAULT 0 AFTER `content`;

-- 客户的跟进人  by jiarui
ALTER TABLE `eh_enterprise_customers`
  MODIFY COLUMN `tracking_uid`  bigint(20) NULL DEFAULT NULL COMMENT 'tracking uid' AFTER `update_time`;

-- 人事2.6 的表字段修改 by ryan
ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time` DATE COMMENT '入职日期';
ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time_index` VARCHAR(64) COMMENT '入职日期索引字段';

-- 资源预约订单统计 By st.zheng
CREATE TABLE `eh_rentalv2_order_statistics` (
  `id` BIGINT(20) NOT NULL,
  `order_id` BIGINT(20) NOT NULL,
  `rental_resource_id` BIGINT(20) NOT NULL,
  `rental_uid` BIGINT(20) NULL DEFAULT NULL,
  `rental_date` DATE NULL DEFAULT NULL,
  `start_time` DATETIME NULL DEFAULT NULL,
  `end_time` DATETIME NULL DEFAULT NULL,
  `reserve_time` DATETIME NULL DEFAULT NULL,
  `valid_time_long` BIGINT(20) NULL DEFAULT NULL,
  `community_id` BIGINT(20) NULL DEFAULT NULL,
  `namespace_id` INT(11) NULL DEFAULT NULL,
  `user_enterprise_id` BIGINT(20) NULL DEFAULT NULL,
  `rental_type` TINYINT(4) NULL DEFAULT NULL,
  `resource_type` VARCHAR(64) NULL DEFAULT NULL,
  `resource_type_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));

  CREATE TABLE `eh_rentalv2_dayopen_time` (
  `id` BIGINT(20) NOT NULL,
  `owner_id` BIGINT(20) NULL DEFAULT NULL,
  `owner_type` VARCHAR(255) NULL DEFAULT NULL,
  `open_time` DOUBLE NULL DEFAULT NULL,
  `close_time` DOUBLE NULL DEFAULT NULL,
  `rental_type` TINYINT(4) NULL DEFAULT NULL,
  `resource_type` VARCHAR(64) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));


