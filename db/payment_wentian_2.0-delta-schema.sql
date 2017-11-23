ALTER TABLE `eh_payment_bill_groups_rules` ADD `brother_rule_id` BIGINT DEFAULT NULL COMMENT '兄弟账单组收费项id';


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

-- from asset-org by xiongying
CREATE TABLE `eh_community_organization_detail_display` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `detail_flag` TINYINT NOT NULL DEFAULT 0,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_communities` ADD COLUMN `community_number` VARCHAR(64) COMMENT '项目编号';
ALTER TABLE `eh_buildings` ADD COLUMN `building_number` VARCHAR(64) COMMENT '楼栋编号';

ALTER TABLE `eh_enterprise_customers` ADD COLUMN `version` VARCHAR(32) COMMENT '版本号';
ALTER TABLE `eh_contracts` ADD COLUMN `version` VARCHAR(32) COMMENT '版本号';

ALTER TABLE `eh_contracts` ADD COLUMN `namespace_contract_type` VARCHAR(128);
ALTER TABLE `eh_contracts` ADD COLUMN `namespace_contract_token` VARCHAR(128);

-- club
-- 行业协会类型
CREATE TABLE `eh_industry_types` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_guild_applies` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `group_id` bigint(22) NOT NULL,
  `applicant_uid` bigint(22) NOT NULL,
  `group_member_id` bigint(22) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(18) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `organization_name` varchar(255) DEFAULT NULL,
  `registered_capital` varchar(255) DEFAULT NULL,
  `industry_type` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_uid` bigint(22) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_group_settings` ADD COLUMN `club_type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;
-- 未加入俱乐部成员在俱乐部论坛的权限 0-不可见，1-可见，2-可交互
ALTER TABLE `eh_groups` ADD COLUMN `tourist_post_policy`  tinyint(4) NULL DEFAULT 2 COMMENT '0-hide, 1-see only, 2-interact';
-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_groups` ADD COLUMN `club_type`  tinyint(4) NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;

ALTER TABLE `eh_groups` ADD COLUMN `phone_number`  varchar(18) NULL ;

ALTER TABLE `eh_groups`  ADD COLUMN `description_type`  tinyint(4) NULL DEFAULT 0;

-- 拒绝理由
ALTER TABLE `eh_group_member_logs` ADD COLUMN `reject_text`  varchar(255) NULL;