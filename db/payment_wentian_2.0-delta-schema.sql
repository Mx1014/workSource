ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `instruction` VARCHAR(1024) DEFAULT NULL COMMENT '说明';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `due_day` INTEGER DEFAULT NULL COMMENT '最晚还款日，距离账单日的距离，单位可以为月 ';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `due_day_type` TINYINT DEFAULT 1 COMMENT '1:日，2：月 ';

DROP TABLE IF EXISTS `eh_payment_formula`;
CREATE TABLE `eh_payment_formula` (
  `id` bigint(20) NOT NULL,
  `charging_standard_id` bigint(20) DEFAULT NULL,
  `name` varchar(10) DEFAULT NULL,
  `constraint_variable_identifer` varchar(255) DEFAULT NULL,
  `start_constraint` tinyint(4) DEFAULT NULL COMMENT '1:大于；2：大于等于；3：小于；4：小于等于',
  `start_num` decimal(10,2) DEFAULT '0.00',
  `end_constraint` tinyint(4) DEFAULT NULL COMMENT '1:大于；2：大于等于；3：小于；4：小于等于',
  `end_num` decimal(10,2) DEFAULT '0.00',
  `variables_json_string` varchar(2048) DEFAULT NULL COMMENT 'json strings of variables injected for a particular formula',
  `formula` varchar(1024) DEFAULT NULL,
  `formula_json` varchar(2048) DEFAULT NULL,
  `formula_type` tinyint(4) DEFAULT NULL COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `price_unit_type` tinyint(4) DEFAULT NULL COMMENT '1:日单价; 2:月单价; 3:季单价; 4:年单价',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准公式表';

ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `suggest_unit_price` DECIMAL(10,2) DEFAULT NULL COMMENT '建议单价';
ALTER TABLE `eh_payment_bill_groups_rules` ADD COLUMN `bill_item_month_offset` INTEGER DEFAULT NULL COMMENT '收费项产生时间偏离当前月的月数';
ALTER TABLE `eh_payment_bill_groups_rules` ADD COLUMN `bill_item_day_offset` INTEGER DEFAULT NULL COMMENT '收费项产生时间偏离当前月的日数';
ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `area_size_type` INTEGER DEFAULT 1 COMMENT '计费面积类型,1：合同面积；2.建筑面积；3：使用面积；4：出租面积';
ALTER TABLE `eh_payment_bill_groups` MODIFY COLUMN `name` VARCHAR(50) DEFAULT NULL COMMENT '账单组名称';

ALTER TABLE `eh_payment_bill_items` ADD COLUMN `due_day_deadline` varchar(30) DEFAULT NULL COMMENT '最后付款日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `date_str_begin` varchar(30) DEFAULT NULL COMMENT '账期开始日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `date_str_end` varchar(30) DEFAULT NULL COMMENT '账期结束日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `date_str_due` varchar(30) DEFAULT NULL COMMENT '出账单日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `due_day_deadline` varchar(30) DEFAULT NULL COMMENT '最后付款日期';

ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `project_level_name` VARCHAR(30) DEFAULT NULL COMMENT '园区自定义的收费项目名字';

ALTER TABLE `eh_payment_bill_items` ADD COLUMN `date_str_generation` VARCHAR(40) DEFAULT NULL COMMENT '费用产生日期';

ALTER TABLE `eh_payment_bills` ADD COLUMN `charge_status` TINYINT DEFAULT 0 COMMENT '缴费状态，0：正常；1：欠费';

-- 4.10.3

-- 园区入驻3.6 add by sw 20171023
DROP TABLE IF EXISTS `eh_lease_configs`;
DROP TABLE IF EXISTS `eh_lease_configs2`;

CREATE TABLE `eh_lease_configs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'owner type, e.g EhCommunities',
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'owner id, e.g eh_communities id',
  `config_name` varchar(128) DEFAULT NULL,
  `config_value` varchar(128) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_lease_project_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `lease_project_id` bigint(20) NOT NULL COMMENT 'lease project id',
  `community_id` bigint(20) NOT NULL COMMENT 'community id',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_lease_projects` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `project_id` bigint(20) NOT NULL COMMENT 'id of the record',
  `city_id` bigint(20) NOT NULL COMMENT 'city id in region table',
  `city_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `area_id` bigint(20) NOT NULL COMMENT 'area id in region table',
  `area_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `address` varchar(512) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `contact_name` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(128) DEFAULT NULL COMMENT 'the phone number',
  `description` text,
  `traffic_description` text,
  `poster_uri` varchar(256) DEFAULT NULL,
  `extra_info_json` text,

  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'user who suggested the creation',
  `create_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 帖子增加置顶功能  add by yanjun 201710231623
ALTER TABLE `eh_forum_posts` ADD COLUMN `stick_flag`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是';
ALTER TABLE `eh_activities` ADD COLUMN `stick_flag`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是';

-- merge from feature-customer add by xiongying20171024
CREATE TABLE `eh_customer_events` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER  NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT NOT NULL COMMENT '所属客户类型',
  `customer_id`  BIGINT    COMMENT '所属客户id',
  `customer_name` VARCHAR(128)   COMMENT '客户名称',
  `contact_name` VARCHAR(64) ,
  `content`  TEXT,
  `creator_uid`  BIGINT   COMMENT '创建人uid',
  `create_time` DATETIME  ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_customer_tracking_plans` (
  `id` BIGINT  NOT NULL,
  `namespace_id` INTEGER  NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT  NOT NULL COMMENT '所属客户类型',
  `customer_id`  BIGINT   COMMENT '所属客户id',
  `customer_name` VARCHAR(128)   COMMENT '客户名称',
  `contact_name` VARCHAR(64)   COMMENT '联系人',
  `tracking_type` BIGINT   COMMENT '计划跟进类型',
  `tracking_time` DATETIME   COMMENT '跟进时间',
  `notify_time` DATETIME   COMMENT '提醒时间',
  `title` VARCHAR(128) DEFAULT NULL COMMENT '标题',
  `content` TEXT  COMMENT '内容',
  `status`  TINYINT  NOT NULL COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `creator_uid` BIGINT     COMMENT '创建人uid',
  `create_time` DATETIME   COMMENT '创建时间',
  `update_uid`  BIGINT     COMMENT '修改人uid',
  `update_time` DATETIME   COMMENT '修改时间',
  `delete_uid`  BIGINT     COMMENT '删除人uid',
  `delete_time` DATETIME   COMMENT '删除时间',
  `notify_status` TINYINT  DEFAULT NULL COMMENT '提醒状态  0:无需提醒   1:待提醒   2:已提醒',
  `read_status` TINYINT DEFAULT '0' COMMENT 'is read?  0:no  1:yes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_customer_trackings` (
  `id` BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT NOT NULL COMMENT '所属客户类型',
  `customer_id` BIGINT    COMMENT '所属客户id',
  `customer_name` VARCHAR(128)   COMMENT '客户名称',
  `contact_name` VARCHAR(64)    COMMENT '联系人',
  `tracking_type` BIGINT    COMMENT '跟进类型',
  `tracking_uid` BIGINT     COMMENT '跟进人uid ',
  `intention_grade` INTEGER    COMMENT '意向等级',
  `tracking_time` DATETIME   COMMENT '跟进时间',
  `content` TEXT COMMENT '跟进内容',
  `content_img_uri` VARCHAR(2048) COMMENT '跟进内容图片uri',
  `status` TINYINT NOT NULL COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `creator_uid` BIGINT     COMMENT '创建人uid',
  `create_time` DATETIME   COMMENT '创建时间',
  `update_uid` BIGINT     COMMENT '修改人uid',
  `update_time` DATETIME   COMMENT '修改时间',
  `delete_uid` BIGINT     COMMENT '删除人uid',
  `delete_time` DATETIME    COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_tracking_notify_logs` (
  `id` BIGINT NOT NULL,
  `customer_type` TINYINT  NOT NULL,
  `customer_id`  BIGINT  NOT NULL,
  `notify_text` TEXT,
  `receiver_id` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL ,
  `status` TINYINT NOT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_enterprise_customers`
ADD COLUMN `tracking_uid`  BIGINT  NULL DEFAULT -1 COMMENT '跟进人uid' AFTER `update_time`,
ADD COLUMN `tracking_name`   VARCHAR(32) NULL COMMENT '跟进人姓名' AFTER `tracking_uid`,
ADD COLUMN `property_area`  DOUBLE NULL COMMENT '资产面积' AFTER `tracking_name`,
ADD COLUMN `property_unit_price`  DOUBLE NULL COMMENT '资产单价' AFTER `property_area`,
ADD COLUMN `property_type`  BIGINT NULL COMMENT '资产类型' AFTER `property_unit_price`,
ADD COLUMN `longitude`  DOUBLE NULL COMMENT '经度' AFTER `property_type`,
ADD COLUMN `latitude`  DOUBLE NULL COMMENT '纬度' AFTER `longitude`,
ADD COLUMN `geohash`  VARCHAR(32) NULL DEFAULT NULL AFTER `latitude` ,
ADD COLUMN `last_tracking_time`  DATETIME   COMMENT '最后一次跟进时间' AFTER `geohash` ,
ADD COLUMN `contact_duty`  VARCHAR(64) NULL AFTER `contact_mobile`;

ALTER TABLE `eh_buildings` ADD INDEX building_name ( `name`, `alias_name`);

ALTER TABLE `eh_contracts` ADD COLUMN `settled` VARCHAR(128);
ALTER TABLE `eh_contracts` ADD COLUMN `layout` VARCHAR(128);

ALTER TABLE `eh_addresses` ADD COLUMN `apartment_number` VARCHAR(32);
ALTER TABLE `eh_addresses` ADD COLUMN `address_unit` VARCHAR(32);
ALTER TABLE `eh_addresses` ADD COLUMN `address_ownership_id` BIGINT COMMENT '产权归属: 自有、出售、非产权..., refer to the id of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN `address_ownership_name` VARCHAR(128) COMMENT '产权归属: 自有、出售、非产权..., refer to the display_name of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN `remark` VARCHAR(128);

CREATE TABLE `eh_address_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to the id of eh_addresses',
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_contract_charging_changes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `charging_item_id` BIGINT COMMENT '收费项',
  `change_type` TINYINT COMMENT '1: 调租; 2: 免租',
  `change_method` TINYINT COMMENT '1: 按金额递增; 2: 按金额递减; 3: 按比例递增; 4: 按比例递减',
  `change_period` INTEGER,
  `period_unit` TINYINT COMMENT '1: 天; 2: 月; 3: 年',
  `change_range` DECIMAL(10,2),
  `change_start_time` DATETIME,
  `change_expired_time` DATETIME,
  `remark` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_contract_charging_change_addresses` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `charging_change_id` bigint(20) NOT NULL COMMENT 'id of eh_contract_charging_changes',
  `address_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `decoupling_flag` TINYINT DEFAULT 0 COMMENT '解耦标志，0:耦合中，收到域名下全部设置的影响;1:副本解耦';
ALTER TABLE `eh_payment_bills` ADD COLUMN `real_paid_time` DATETIME DEFAULT NULL COMMENT '实际付款时间';
