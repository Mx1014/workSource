
CREATE TABLE `eh_relocation_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `request_no` varchar(128) NOT NULL,
  `requestor_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization where the requestor is in',
  `requestor_entperise_name` varchar(64) DEFAULT NULL COMMENT 'the enterprise name of requestor',
  `requestor_entperise_address` varchar(256) DEFAULT NULL COMMENT 'the enterprise address of requestor',
  `requestor_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'requestor id',
  `requestor_name` varchar(64) DEFAULT NULL COMMENT 'the name of requestor',
  `contact_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of requestor',
  `relocation_date` datetime NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: processing, 2: completed',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `flow_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'flow case id',
  `cancel_time` datetime DEFAULT NULL,
  `cancel_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'cancel user id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_relocation_request_items` (++
+-4
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',

  `item_name` varchar(64) DEFAULT NULL COMMENT 'the name of item',
  `item_quantity` int(11) DEFAULT 0 COMMENT 'the quantity of item',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_relocation_request_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id, e.g comment_id',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
ADD/*  COLUMN `package_name` VARCHAR(45) NULL DEFAULT NULL AFTER `door_auth_id`;
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