
-- 增加结束时间提醒门禁时间 by st.zheng
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `reminder_end_time` DATETIME NULL DEFAULT NULL AFTER `reminder_time`,
ADD COLUMN `auth_start_time` DATETIME NULL DEFAULT NULL AFTER `reminder_end_time`,
ADD COLUMN `auth_end_time` DATETIME NULL DEFAULT NULL AFTER `auth_start_time`,
ADD COLUMN `door_auth_id` BIGINT(20) NULL DEFAULT NULL AFTER `auth_end_time`;
-- 资源预定增加门禁 by st.zheng
ALTER TABLE `eh_rentalv2_resources`
ADD COLUMN `aclink_id` BIGINT(20) NULL DEFAULT NULL AFTER `default_order`;

-- 增加套餐 by st.zheng
CREATE TABLE `eh_rentalv2_price_packages` (
  `id` BIGINT(20) NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `rental_type` TINYINT(4) NULL DEFAULT NULL,
  `price` DECIMAL(10,2) NULL DEFAULT NULL,
  `org_member_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `approving_user_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `discount_type` TINYINT(4) NULL DEFAULT NULL,
  `full_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `cut_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `discount_ratio` DOUBLE NULL DEFAULT NULL,
  `org_member_discount_type` TINYINT(4) NULL DEFAULT NULL,
  `org_member_full_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `org_member_cut_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `org_member_discount_ratio` DOUBLE NULL DEFAULT NULL,
  `approving_user_discount_type` TINYINT(4) NULL DEFAULT NULL,
  `approving_user_full_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `approving_user_cut_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `approving_user_discount_ratio` DOUBLE NULL DEFAULT NULL,
  `cell_begin_id` BIGINT(20) NOT NULL DEFAULT '0',
  `cell_end_id` BIGINT(20) NOT NULL DEFAULT '0',
  `creator_uid` BIGINT(20) NULL DEFAULT NULL,
  `create_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `eh_rentalv2_cells`
ADD COLUMN `price_package_id` BIGINT(20) NULL DEFAULT NULL AFTER `half_approving_user_price`;

ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `org_member_discount_type` TINYINT(4) NULL DEFAULT NULL AFTER `discount_ratio`,
ADD COLUMN `org_member_full_price` DECIMAL(10,2) NULL DEFAULT NULL AFTER `org_member_discount_type`,
ADD COLUMN `org_member_cut_price` DECIMAL(10,2) NULL DEFAULT NULL AFTER `org_member_full_price`,
ADD COLUMN `org_member_discount_ratio` DOUBLE NULL DEFAULT NULL AFTER `org_member_cut_price`,
ADD COLUMN `approving_user_discount_type` TINYINT(4) NULL DEFAULT NULL AFTER `org_member_discount_ratio`,
ADD COLUMN `approving_user_full_price` DECIMAL(10,2) NULL DEFAULT NULL AFTER `approving_user_discount_type`,
ADD COLUMN `approving_user_cut_price` DECIMAL(10,2) NULL DEFAULT NULL AFTER `approving_user_full_price`,
ADD COLUMN `approving_user_discount_ratio` DOUBLE NULL DEFAULT NULL AFTER `approving_user_cut_price`;
