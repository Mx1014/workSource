-- bydengs,20171114 服务联盟加客服id service_alliance2.9.3
ALTER TABLE `eh_service_alliances` ADD COLUMN `online_service_uid` BIGINT COMMENT 'online service user id';
ALTER TABLE `eh_service_alliances` ADD COLUMN `online_service_uname` varchar(64) COMMENT 'online service user name';

-- bydengs,20171114 物业报修2.9.3
ALTER TABLE `eh_pm_tasks` ADD COLUMN `organization_name` VARCHAR(128) COMMENT '报修的任务的公司名称';


-- 增加结束时间提醒门禁时间 by st.zheng
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `reminder_end_time` DATETIME NULL DEFAULT NULL AFTER `reminder_time`,
ADD COLUMN `auth_start_time` DATETIME NULL DEFAULT NULL AFTER `reminder_end_time`,
ADD COLUMN `auth_end_time` DATETIME NULL DEFAULT NULL AFTER `auth_start_time`,
ADD COLUMN `door_auth_id` BIGINT(20) NULL DEFAULT NULL AFTER `auth_end_time`,
ADD COLUMN `package_name` VARCHAR(45) NULL DEFAULT NULL AFTER `door_auth_id`;
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
  `original_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `org_member_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `org_member_original_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `approving_user_price` DECIMAL(10,2) NULL DEFAULT NULL,
  `approving_user_original_price` DECIMAL(10,2) NULL DEFAULT NULL,
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

ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `day_open_time` DOUBLE NULL DEFAULT NULL AFTER `end_date`,
ADD COLUMN `day_close_time` DOUBLE NULL DEFAULT NULL AFTER `day_open_time`;

ALTER TABLE `eh_rentalv2_resources`
ADD COLUMN `day_open_time` DOUBLE NULL DEFAULT NULL AFTER `end_date`,
ADD COLUMN `day_close_time` DOUBLE NULL DEFAULT NULL AFTER `day_open_time`;

-- merge from customer20171108 add by xiongying
ALTER TABLE `eh_customer_economic_indicators` ADD COLUMN `month` DATETIME;

CREATE TABLE `eh_customer_economic_indicator_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `turnover` DECIMAL(10,2) COMMENT '营业额',
  `tax_payment` DECIMAL(10,2) COMMENT '纳税额',
  `start_time` DATETIME,
  `end_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 2,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_news_tag ADD COLUMN `category_id` BIGINT default 0;


-- merge from payv2-withdraw-delta-schema.sql by lqs 20171121
ALTER TABLE `eh_payment_users` ADD COLUMN `bank_name` VARCHAR(512) COMMENT 'the name of bank where enterprise has a account';
ALTER TABLE `eh_payment_users` ADD COLUMN `bank_number` VARCHAR(128) COMMENT 'the number of bank where enterprise has a account';
ALTER TABLE `eh_payment_users` ADD COLUMN `bank_card_number` VARCHAR(128) COMMENT 'the card number of enterprise bank account';

ALTER TABLE `eh_payment_users` ADD COLUMN `enterprise_name` VARCHAR(512) COMMENT 'the name of enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `enterprise_business_licence` VARCHAR(128) COMMENT 'the business licence number of enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `enterprise_business_licence_uri` VARCHAR(128) COMMENT 'the image of business licence of enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `enterprise_account_licence_uri` VARCHAR(128) COMMENT 'the image of account licence of enterprise';

ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_name` VARCHAR(512) COMMENT 'the real name of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_phone` VARCHAR(512) COMMENT 'the phone number of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_identity_type` VARCHAR(512) COMMENT 'the identity type of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_identity_number` VARCHAR(512) COMMENT 'the identity number of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_identity_front_uri` VARCHAR(1024) COMMENT 'the front side of identity image of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_identity_back_uri` VARCHAR(1024) COMMENT 'the back side identity image of legal person in an enterprise';


ALTER TABLE `eh_payment_users` ADD COLUMN `status` TINYINT COMMENT '0-inactive, 1-waiting for approval, 2-active';
ALTER TABLE `eh_payment_users` ADD COLUMN `creator_uid` BIGINT;
ALTER TABLE `eh_payment_users` ADD COLUMN `operator_uid` BIGINT;
ALTER TABLE `eh_payment_users` ADD COLUMN `update_time` DATETIME;

-- record the withdraw orders
-- DROP TABLE IF EXISTS `eh_payment_withdraw_orders`;
CREATE TABLE `eh_payment_withdraw_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `order_no` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `payment_user_type` INTEGER NOT NULL COMMENT 'the account type to withdraw the monty: 1-普通会员,2-企业会员',
  `payment_user_id` BIGINT NOT NULL COMMENT 'the account in pay-system to withdraw the monty',
  `amount` DECIMAL(10,2) COMMENT 'the amount to withdraw',
  `status` TINYINT NOT NULL COMMENT '0-inactive, 1-waiting for confirm, 2-success, 3-failed',
  `callback_time` DATETIME,
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user who withdraw the money',
  `operate_time` DATETIME COMMENT 'the time to withdraw the money',
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;