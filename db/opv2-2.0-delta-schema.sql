-- 下载资源名称字段长度过短,加长
ALTER TABLE  eh_content_server_resources CHANGE resource_name resource_name VARCHAR(1024) NOT NULL ;

-- domain 增加name, icon
ALTER TABLE `eh_domains` ADD COLUMN `favicon_uri`  VARCHAR(255) NULL;
ALTER TABLE `eh_domains` ADD COLUMN `name`  VARCHAR(255) NULL AFTER `namespace_id`;
ALTER TABLE `eh_domains` ADD COLUMN `login_bg_uri`  VARCHAR(255) NULL;
ALTER TABLE `eh_domains` ADD COLUMN `login_logo_uri`  VARCHAR(255) NULL;
ALTER TABLE `eh_domains` ADD COLUMN `menu_logo_uri`  VARCHAR(255) NULL;
ALTER TABLE `eh_domains` ADD COLUMN `menu_logo_collapsed_uri`  VARCHAR(255) NULL;

ALTER TABLE `eh_web_menu_scopes` ADD COLUMN `app_id`  BIGINT(20) NULL COMMENT 'eh_service_module_app id';
ALTER TABLE `eh_web_menu_scopes` DROP INDEX `u_menu_scope_owner` , ADD UNIQUE INDEX `u_menu_scope_owner` (`menu_id`, `owner_type`, `owner_id`, `app_id`) USING BTREE ;


-- 增加项目区分 by st.zheng
ALTER TABLE `eh_categories`
ADD COLUMN `owner_type` VARCHAR(32) NULL DEFAULT NULL AFTER `namespace_id`;
ALTER TABLE `eh_categories`
ADD COLUMN `owner_id` BIGINT(20) NULL DEFAULT '0' AFTER `owner_type`;

-- by st.zheng
ALTER TABLE `eh_pm_tasks`
  ADD COLUMN `refer_type` VARCHAR(32) NULL COMMENT '引用类型' AFTER `if_use_feelist`;
ALTER TABLE `eh_pm_tasks`
  ADD COLUMN `refer_id` BIGINT(20) NULL COMMENT '引用id' AFTER `refer_type`;

-- 物业巡检V3.1    by jiarui
-- 设备巡检计划表
CREATE TABLE `eh_equipment_inspection_plans` (
  `id` BIGINT(20) NOT NULL,
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'organization_id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'organization',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'zone resource_type ',
  `target_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'zone  resource_id',
  `plan_number` VARCHAR(128) NOT NULL DEFAULT '0' COMMENT 'the plans number ',
  `plan_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'the type of plan 0: 巡检  1: 保养',
  `name` VARCHAR(1024) DEFAULT NULL COMMENT 'the name of plan_number',
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'status of plans  0:waitting for starting 1: waitting for approving  2: QUALIFIED 3:UN_QUALIFIED',
  `reviewer_uid` BIGINT(20) NOT NULL DEFAULT '0',
  `review_time` DATETIME DEFAULT NULL,
  `repeat_setting_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'refers to eh_repeatsetting ',
  `remarks` TEXT,
  `creator_uid` BIGINT(20) NOT NULL DEFAULT '0',
  `create_time` DATETIME DEFAULT NULL,
  `operator_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `deleter_uid` BIGINT(20) DEFAULT NULL,
  `delete_time` DATETIME DEFAULT NULL,
  `last_create_taskTime` DATETIME DEFAULT NULL COMMENT 'the last time when gen task',
  `inspection_category_id` BIGINT(20) DEFAULT NULL,
  `namespace_id` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 设备巡检计划--设备 关联表   by jiarui
CREATE TABLE `eh_equipment_inspection_equipment_plan_map` (
  `id` BIGINT(20) NOT NULL,
  `equipment_id` BIGINT(20) NOT NULL DEFAULT '0',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `target_id` BIGINT(20) NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `plan_id` BIGINT(20) NOT NULL DEFAULT '0',
  `standard_id` BIGINT(20) NOT NULL DEFAULT '0',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `default_order` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'show order of equipment_maps',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 巡检计划 执行组审批组 关联表 start   by jiarui
CREATE TABLE `eh_equipment_inspection_plan_group_map` (
  `id` BIGINT(20) NOT NULL COMMENT 'id',
  `group_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: executive group, 2: review group',
  `plan_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_plans',
  `group_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 巡检计划 审批时间表 end  by jiarui

CREATE TABLE `eh_equipment_inspection_review_date` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL COMMENT 'refer to object type EhEquipmentInspectionTasksReviewExpireDays...',
  `scope_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all; 1: namespace; 2: community',
  `scope_id` bigint(20) NOT NULL,
  `review_expired_days` int(11) NOT NULL DEFAULT '0' COMMENT 'review_expired_days',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: invalid, 1: valid',
  `refer_id` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime  COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 设备操作记录表   by jiarui
CREATE TABLE `eh_equipment_inspection_equipment_logs` (
  `id` BIGINT(20) NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'standard, etc',
  `target_id` BIGINT(20) NOT NULL DEFAULT '0',
  `process_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: insert, 2: update, 3: delete',
  `operator_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'record operator user id',
  `create_time` DATETIME DEFAULT NULL,
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--   设备增加经纬度字符串  by jiarui
ALTER TABLE `eh_equipment_inspection_equipments`
  ADD COLUMN `coordinate`  VARCHAR(1024) NULL AFTER `geohash`;


-- eh_equipment_inspection_tasks 增加plan_id字段 用于关联task和equipments  by jiarui
ALTER TABLE `eh_equipment_inspection_tasks`
  ADD COLUMN `plan_id`  BIGINT(20) NOT NULL ;

ALTER TABLE eh_equipment_inspection_tasks ADD INDEX eq_task_plan_id (plan_id) ;


-- 标准增加周期类型   by jiarui
ALTER TABLE `eh_equipment_inspection_standards`
  ADD COLUMN `repeat_type` TINYINT(4) NOT NULL COMMENT ' 0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year';

-- 任务操作记录表增加设备id表   by jiarui
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `equipment_id`  BIGINT(20) NULL DEFAULT 0 ;
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `maintance_type`  VARCHAR(255) NULL DEFAULT '';
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `flow_case_id`  BIGINT(20) NULL AFTER `equipment_id`;
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `maintance_status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed';
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `pm_task_id` BIGINT(20) NULL DEFAULT 0 ;

ALTER TABLE eh_equipment_inspection_task_logs ADD INDEX eq_log_pm_task_id (pm_task_id) ;
ALTER TABLE eh_equipment_inspection_task_logs ADD INDEX eq_log_task_id (task_id) ;

-- 物业巡检V3.1  end   by jiarui

-- 离线支持   by jiarui
ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `update_uid` BIGINT (20) NULL AFTER `status`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `update_time` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP AFTER `update_uid`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `delete_uid` BIGINT (20) NULL AFTER `update_uid`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `delete_time` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP AFTER `delete_uid`;

-- 日志增加项目 jiarui

ALTER TABLE `eh_quality_inspection_logs`
  ADD COLUMN `scope_id`  BIGINT(20) NULL DEFAULT 0 ;


-- 文档管理1.0 add by nan.rong 01/12/2018
-- DROP TABLE eh_file_management_catalogs;
CREATE TABLE `eh_file_management_catalogs` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) ,
  `name` VARCHAR(64) COMMENT 'the name of the catalog',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid 1-valid',
  `creator_uid` BIGINT  DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- DROP TABLE eh_file_management_catalog_scopes;
CREATE TABLE `eh_file_management_catalog_scopes` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `catalog_id` BIGINT NOT NULL COMMENT 'the id of the file catalog',
  `source_id` BIGINT NOT NULL COMMENT 'the id of the source',
  `source_description` VARCHAR(128) COMMENT 'the description of the scope class',
  `download_permission` TINYINT NOT NULL DEFAULT 0 COMMENT '0-refuse, 1-allow',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- DROP TABLE eh_file_management_contents;
CREATE TABLE `eh_file_management_contents` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR (64),
  `catalog_id` BIGINT COMMENT 'the id of the catalog',
  `content_name` VARCHAR(256) NOT NULL COMMENT 'the name of the content',
  `size` INT NOT NULL DEFAULT 0 COMMENT 'the size of the content',
  `parent_id` BIGINT COMMENT 'the parent id of the folder',
  `content_type` VARCHAR(32) COMMENT 'file, folder',
  `content_suffix` VARCHAR(64) COMMENT 'the suffix of the file',
  `content_uri` VARCHAR(2048) COMMENT 'the uri of the content',
  `path` VARCHAR(128) NOT NULL COMMENT 'the path of the content',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `file_management_contents_path` (`path`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- DROP TABLE eh_file_icons;
CREATE TABLE `eh_file_icons` (
  `id` BIGINT,
  `file_type` VARCHAR(64) NOT NULL COMMENT 'the type of the file',
  `icon_name` VARCHAR(128) COMMENT 'the name of the icon',
  `icon_uri` VARCHAR(2048) NOT NULL COMMENT 'the uri of the type',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- end by nan.rong

-- 能耗离线 add by xiongying 01/18/2018
CREATE TABLE `eh_sync_offline_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `type` VARCHAR(64) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL,
  `result` LONGTEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- end by xiongying


-- add by sw 20180122 vip车位
CREATE TABLE `eh_parking_spaces` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `parking_lot_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `space_no` VARCHAR(64) NOT NULL DEFAULT '',
  `space_address` VARCHAR(64) NOT NULL DEFAULT '',
  `lock_id` VARCHAR(128) DEFAULT NULL,
  `lock_status` VARCHAR(128) DEFAULT NULL,
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT '0',
  `create_time` DATETIME DEFAULT NULL,
  `update_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_space_logs` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `space_no` VARCHAR(64) NOT NULL,
  `lock_id` VARCHAR(128) DEFAULT NULL,
  `contact_phone` VARCHAR(64) DEFAULT NULL,
  `contact_name` VARCHAR(64) DEFAULT NULL,
  `contact_enterprise_name` VARCHAR(128) DEFAULT NULL,
  `operate_type` TINYINT(4) NOT NULL COMMENT '1: up, 2: down',
  `user_type` TINYINT(4) NOT NULL COMMENT '1: booking person, 2: plate owner',
  `operate_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `menu_type` TINYINT(4) DEFAULT 1 COMMENT '1: 通用 2:公司会议室';
ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `identify` VARCHAR(64) DEFAULT NULL COMMENT '类型标识';

CREATE TABLE `eh_rentalv2_order_rules` (
  `id` BIGINT(20) NOT NULL DEFAULT '0',
  `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型',
  `owner_type` VARCHAR(255) DEFAULT NULL COMMENT 'default_rule, resource_rule',
  `owner_id` BIGINT(20) DEFAULT NULL,
  `handle_type` TINYINT(4) DEFAULT NULL COMMENT '1: 退款, 2: 加收',
  `duration_type` TINYINT(4) DEFAULT NULL COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` TINYINT(4) DEFAULT NULL COMMENT '时长单位，比如 天，小时',
  `duration` DOUBLE DEFAULT NULL COMMENT '时长',
  `factor` DOUBLE DEFAULT NULL COMMENT '价格系数',

  `status` TINYINT(4) DEFAULT NULL,
  `creator_uid` BIGINT(20) NOT NULL DEFAULT '0',
  `create_time` DATETIME DEFAULT NULL,
  `update_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `source_type` VARCHAR(255) DEFAULT NULL COMMENT 'default_rule, resource_rule';
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `source_id` BIGINT(20) DEFAULT NULL;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `holiday_open_flag` TINYINT(4) DEFAULT NULL COMMENT '节假日是否开放预约: 1-是, 0-否';
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `holiday_type` TINYINT(4) DEFAULT NULL COMMENT '1-普通双休, 2-同步中国节假日';
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `refund_strategy` TINYINT(4) DEFAULT NULL COMMENT '1-custom, 2-full';
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `overtime_strategy` TINYINT(4) DEFAULT NULL COMMENT '1-custom, 2-full';

ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN pay_start_time;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN pay_end_time;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN payment_ratio;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN contact_num;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN overtime_time;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN unit;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN rental_step;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN time_step;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN cancel_time;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN cancel_flag;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN workday_price;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN weekend_price;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN org_member_workday_price;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN org_member_weekend_price;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN approving_user_workday_price;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN approving_user_weekend_price;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN rental_type;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN exclusive_flag;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN auto_assign;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN multi_unit;
ALTER TABLE `eh_rentalv2_default_rules`
DROP COLUMN resource_counts;

UPDATE eh_rentalv2_default_rules SET source_type = 'default_rule' WHERE source_type IS NULL;
-- 资源表中规则信息迁移到规则表中
SET @id = (SELECT MAX(id) FROM `eh_rentalv2_default_rules`);
INSERT INTO `eh_rentalv2_default_rules` (`id`, `owner_type`, `owner_id`, `resource_type_id`, `rental_start_time`, `rental_end_time`, `refund_flag`, `refund_ratio`, `creator_uid`, `create_time`, `multi_time_interval`, `need_pay`, `begin_date`, `end_date`, `day_open_time`, `day_close_time`, `open_weekday`, `rental_start_time_flag`, `rental_end_time_flag`, `source_type`, `source_id`)
SELECT (@id := @id + 1), 'organization', organization_id, resource_type_id, rental_start_time, rental_end_time, refund_flag, refund_ratio, creator_uid, create_time, multi_time_interval, need_pay, begin_date, end_date, day_open_time, day_close_time, open_weekday, rental_start_time_flag, rental_end_time_flag, 'resource_rule', id  FROM eh_rentalv2_resources;


ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN unit;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN time_step;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN cancel_time;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN cancel_flag;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN exclusive_flag;

-- 资源表中规则信息迁移到规则表中
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN rental_start_time_flag;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN rental_end_time_flag;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN open_weekday;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN begin_date;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN end_date;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN rental_start_time;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN rental_end_time;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN refund_flag;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN refund_ratio;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN multi_time_interval;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN day_open_time;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN day_close_time;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN day_begin_time;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN day_end_time;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN need_pay;
ALTER TABLE `eh_rentalv2_resources`
DROP COLUMN resource_type2;

ALTER TABLE `eh_rentalv2_resources`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';


-- 资源单元格表
ALTER TABLE `eh_rentalv2_cells`
DROP COLUMN unit;
ALTER TABLE `eh_rentalv2_cells`
DROP COLUMN exclusive_flag;
ALTER TABLE `eh_rentalv2_cells`
DROP COLUMN rental_step;
ALTER TABLE `eh_rentalv2_cells`
DROP COLUMN halfresource_price;
ALTER TABLE `eh_rentalv2_cells`
DROP COLUMN halfresource_original_price;
ALTER TABLE `eh_rentalv2_cells`
DROP COLUMN half_org_member_original_price;
ALTER TABLE `eh_rentalv2_cells`
DROP COLUMN half_org_member_price;
ALTER TABLE `eh_rentalv2_cells`
DROP COLUMN half_approving_user_original_price;
ALTER TABLE `eh_rentalv2_cells`
DROP COLUMN half_approving_user_price;

ALTER TABLE `eh_rentalv2_cells`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_price_rules`
DROP COLUMN weekend_price;
ALTER TABLE `eh_rentalv2_price_rules`
DROP COLUMN org_member_weekend_price;
ALTER TABLE `eh_rentalv2_price_rules`
DROP COLUMN approving_user_weekend_price;

ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `user_price_type` TINYINT(4) DEFAULT NULL COMMENT '用户价格类型, 1:统一价格 2：用户类型价格';
ALTER TABLE `eh_rentalv2_price_packages`
ADD COLUMN `user_price_type` TINYINT(4) DEFAULT NULL COMMENT '用户价格类型, 1:统一价格 2：用户类型价格';
ALTER TABLE `eh_rentalv2_cells`
ADD COLUMN `user_price_type` TINYINT(4) DEFAULT NULL COMMENT '用户价格类型, 1:统一价格 2：用户类型价格';


ALTER TABLE eh_rentalv2_orders CHANGE requestor_organization_id user_enterprise_id BIGINT(20) DEFAULT NULL COMMENT '申请人公司ID';

ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `custom_object` TEXT;
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `user_enterprise_name` VARCHAR(64) DEFAULT NULL COMMENT '申请人公司名称';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `user_phone` VARCHAR(20) DEFAULT NULL COMMENT '申请人手机';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `user_name` VARCHAR(20) DEFAULT NULL COMMENT '申请人姓名';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `address_id` BIGINT(20) DEFAULT NULL COMMENT '楼栋门牌ID';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `refund_amount` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `actual_start_time` DATETIME DEFAULT NULL COMMENT '实际使用开始时间';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `actual_end_time` DATETIME DEFAULT NULL COMMENT '实际使用结束时间';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `string_tag1` VARCHAR(128) DEFAULT NULL;
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `string_tag2` VARCHAR(128) DEFAULT NULL;
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `scene` VARCHAR(64) DEFAULT NULL COMMENT '下单时场景信息，用来计算价格';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `refund_strategy` TINYINT(4) DEFAULT NULL COMMENT '1-custom, 2-full';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `overtime_strategy` TINYINT(4) DEFAULT NULL COMMENT '1-custom, 2-full';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `old_end_time` DATETIME DEFAULT NULL COMMENT '延长订单时，存老的使用结束时间';
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `old_custom_object` TEXT;


ALTER TABLE `eh_rentalv2_orders`
DROP COLUMN reserve_money;
ALTER TABLE `eh_rentalv2_orders`
DROP COLUMN pay_start_time;
ALTER TABLE `eh_rentalv2_orders`
DROP COLUMN pay_end_time;

ALTER TABLE `eh_rentalv2_resource_orders`
DROP COLUMN rental_step;
ALTER TABLE `eh_rentalv2_resource_orders`
DROP COLUMN exclusive_flag;

UPDATE eh_rentalv2_default_rules SET resource_type = 'default';
UPDATE eh_rentalv2_resources SET resource_type = 'default';
UPDATE eh_rentalv2_cells SET resource_type = 'default';
UPDATE eh_rentalv2_orders SET resource_type = 'default';

ALTER TABLE `eh_rentalv2_close_dates`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_config_attachments`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_items`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_items_orders`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_refund_orders`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_order_attachments`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_price_packages`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_time_interval`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_resource_orders`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_resource_numbers`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_resource_pics`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_resource_ranges`
ADD COLUMN `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '资源类型';

UPDATE eh_rentalv2_close_dates SET resource_type = 'default';
UPDATE eh_rentalv2_config_attachments SET resource_type = 'default';
UPDATE eh_rentalv2_items SET resource_type = 'default';
UPDATE eh_rentalv2_items_orders SET resource_type = 'default';
UPDATE eh_rentalv2_resource_orders SET resource_type = 'default';
UPDATE eh_rentalv2_order_attachments SET resource_type = 'default';
UPDATE eh_rentalv2_price_packages SET resource_type = 'default';
UPDATE eh_rentalv2_price_rules SET resource_type = 'default';
UPDATE eh_rentalv2_time_interval SET resource_type = 'default';
UPDATE eh_rentalv2_resource_orders SET resource_type = 'default';

UPDATE eh_rentalv2_resource_numbers SET resource_type = 'default';
UPDATE eh_rentalv2_resource_ranges SET resource_type = 'default';
UPDATE eh_rentalv2_resource_pics SET resource_type = 'default';

-- add by yanjun  start
-- 运营后台数据版本
CREATE TABLE `eh_portal_versions` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) NOT NULL,
  `parent_id` BIGINT(20) DEFAULT NULL,
  `big_version` INT(11) DEFAULT NULL,
  `minor_version` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `sync_time` DATETIME DEFAULT NULL,
  `publish_time` DATETIME DEFAULT NULL,
  `status` TINYINT(4) DEFAULT NULL COMMENT '0-init,1-edit,2-publis success, 3-publish fail',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 预览版本可见用户表
CREATE TABLE `eh_portal_version_users` (
  `id` BIGINT(20) NOT NULL,
  `user_id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) NOT NULL,
  `version_id` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `u_user_id` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_portal_layouts` ADD COLUMN `version_id`  BIGINT(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `version_id`  BIGINT(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_item_categories` ADD COLUMN `version_id`  BIGINT(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_items` ADD COLUMN `version_id`  BIGINT(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `version_id`  BIGINT(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `origin_id`  BIGINT(20) NULL AFTER `version_id`;
ALTER TABLE `eh_portal_publish_logs` ADD COLUMN `version_id`  BIGINT(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_publish_logs` ADD COLUMN `process`  INT(11) NULL AFTER `version_id`;

ALTER TABLE `eh_web_menu_scopes` ADD COLUMN `config_id`  BIGINT(20) NULL COMMENT 'get config, eg multiple application.';

ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `preview_portal_version_id`  BIGINT(20) NULL COMMENT '预览版本的id，正式版本数据不要配置该数据';
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `preview_portal_version_id`  BIGINT(20) NULL COMMENT '预览版本的id，正式版本数据不要配置该数据';
ALTER TABLE `eh_item_service_categries` ADD COLUMN `preview_portal_version_id`  BIGINT(20) NULL COMMENT '预览版本的id，正式版本数据不要配置该数据';

ALTER TABLE `eh_web_menus` ADD COLUMN `config_type`  TINYINT(4) NULL COMMENT 'null, 1-config by application, 2-all namespace have';

-- add by yanjun  end


-- merge from payment-contract add by xiongying
ALTER TABLE `eh_contracts` ADD COLUMN `remaining_amount` DECIMAL(10,2) COMMENT '剩余金额';
ALTER TABLE `eh_contracts` ADD COLUMN `bid_item_id` BIGINT COMMENT '是否通过招投标';
ALTER TABLE `eh_contracts` ADD COLUMN `create_org_id` BIGINT COMMENT '经办部门';
ALTER TABLE `eh_contracts` ADD COLUMN `create_position_id` BIGINT COMMENT '岗位';
ALTER TABLE `eh_contracts` ADD COLUMN `our_legal_representative` VARCHAR(256) COMMENT '我方法人代表';
ALTER TABLE `eh_contracts` ADD COLUMN `taxpayer_identification_code` VARCHAR(256) COMMENT '纳税人识别码';
ALTER TABLE `eh_contracts` ADD COLUMN `registered_address` VARCHAR(512) COMMENT '注册地址';
ALTER TABLE `eh_contracts` ADD COLUMN `registered_phone` VARCHAR(256) COMMENT '注册电话';
ALTER TABLE `eh_contracts` ADD COLUMN `payee` VARCHAR(256) COMMENT '收款单位';
ALTER TABLE `eh_contracts` ADD COLUMN `payer` VARCHAR(256) COMMENT '付款单位';
ALTER TABLE `eh_contracts` ADD COLUMN `due_bank` VARCHAR(256) COMMENT '收款银行';
ALTER TABLE `eh_contracts` ADD COLUMN `bank_account` VARCHAR(256) COMMENT '银行账号';
ALTER TABLE `eh_contracts` ADD COLUMN `exchange_rate` DECIMAL(10,2) COMMENT '兑换汇率';
ALTER TABLE `eh_contracts` ADD COLUMN `age_limit` INTEGER COMMENT '年限';
ALTER TABLE `eh_contracts` ADD COLUMN `application_id` BIGINT COMMENT '关联请示单';
ALTER TABLE `eh_contracts` ADD COLUMN `payment_mode_item_id` BIGINT COMMENT '预计付款方式';
ALTER TABLE `eh_contracts` ADD COLUMN `paid_time` DATETIME COMMENT '预计付款时间';
ALTER TABLE `eh_contracts` ADD COLUMN `lump_sum_payment` DECIMAL(10,2) COMMENT '一次性付款金额';
ALTER TABLE `eh_contracts` ADD COLUMN `treaty_particulars` TEXT COMMENT '合同摘要';

ALTER TABLE `eh_contracts` ADD COLUMN `payment_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0:普通合同；1：付款合同';

ALTER TABLE eh_contract_params ADD COLUMN `paid_period` INTEGER NOT NULL DEFAULT '0' COMMENT '付款日期';

CREATE TABLE `eh_contract_param_group_map` (		
  `id` BIGINT NOT NULL COMMENT 'id',	
  `param_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_contract_params',		
  `group_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: notify group, 2: pay group',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',		
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  `name` VARCHAR(256) COMMENT '部门名',  
  `create_time` DATETIME,
  		
  PRIMARY KEY (`id`)		
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_contract_payment_plans` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `paid_amount` DECIMAL(10,2) COMMENT '应付金额',
  `paid_time` DATETIME COMMENT '应付日期',
  `remark` VARCHAR(256) COMMENT '备注',
  `notify_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no; 1: notified',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_payment_applications` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` BIGINT COMMENT '园区id',
  `owner_id` BIGINT COMMENT '公司id',
  `title` VARCHAR(256) COMMENT '标题',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `request_id` BIGINT,
  `applicant_uid` BIGINT NOT NULL COMMENT '申请人id',
  `applicant_org_id` BIGINT NOT NULL COMMENT '申请人所属部门id',
  `payee` VARCHAR(256) COMMENT '收款单位',
  `payer` VARCHAR(256) COMMENT '付款单位',
  `due_bank` VARCHAR(256) COMMENT '收款银行',
  `bank_account` VARCHAR(256) COMMENT '银行账号',
  `payment_amount` DECIMAL(10,2) COMMENT '付款金额',
  `payment_rate` DOUBLE COMMENT '付款百分比',
  `remark` VARCHAR(256) COMMENT '备注',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: QUALIFIED; 3: UNQUALIFIED',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- add by xiongying end 

-- 在版本号中加入日期 add by yanjun 201801231805
ALTER TABLE `eh_portal_versions` ADD COLUMN `date_version`  INT(11) NULL AFTER `parent_id`;

-- 仓库管理2.0 added by wentian 2018/01/24

-- 供应商
DROP TABLE IF EXISTS `eh_warehouse_suppliers`;
CREATE TABLE `eh_warehouse_suppliers`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `identity` VARCHAR(128) DEFAULT NULL COMMENT '供应商编号',
  `name` VARCHAR(128) NOT NULL COMMENT '供应商名称',
  `enterprise_business_licence` VARCHAR(32) DEFAULT NULL COMMENT '企业营业执照注册号',
  `legal_person_name` VARCHAR(32) DEFAULT NULL COMMENT '法人的名字',
  `contact_name` VARCHAR(32) DEFAULT NULL COMMENT '联系人',
  `contact_tel` VARCHAR(64) DEFAULT NULL COMMENT '联系电话',
  `enterprise_register_address` VARCHAR(256) DEFAULT NULL COMMENT '注册地址',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `corp_address` VARCHAR(256) DEFAULT NULL COMMENT '公司地址',
  `corp_intro_info` TEXT DEFAULT NULL COMMENT '公司简介',
  `industry` VARCHAR(128) DEFAULT NULL COMMENT '所属行业',
  `bank_name` VARCHAR(128) DEFAULT NULL COMMENT '开户行',
  `bank_card_number` VARCHAR(64) DEFAULT NULL COMMENT '银行账号',
  `evaluation_category` TINYINT DEFAULT NULL COMMENT '评定类别， 1：合格；2：临时',
  `evaluation_levle` TINYINT DEFAULT NULL COMMENT '供应商等级 1：A类；2：B类；3：C类',
  `main_biz_scope` VARCHAR(1024) DEFAULT NULL COMMENT '主要经营范围',
  `attachment_url` VARCHAR(2048) DEFAULT NULL COMMENT '附件地址',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 采购管理schemas
-- 采购单
DROP TABLE IF EXISTS `eh_warehouse_purchase_orders`;
CREATE TABLE `eh_warehouse_purchase_orders`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `applicant_id` BIGINT DEFAULT NULL COMMENT '申请人id',
  `applicant_time` DATETIME DEFAULT NOW() COMMENT '申请时间',
  `supplier_id` BIGINT DEFAULT NULL COMMENT '供应商id',
  `submission_status` TINYINT DEFAULT NULL COMMENT '审核状态',
  `total_amount` DECIMAL(20,2) DEFAULT 0.00 COMMENT '总金额',
  `warehouse_status` TINYINT DEFAULT NULL COMMENT '库存状态',
  `delivery_date` DATETIME DEFAULT NULL COMMENT '交付日期',
  `community_id` BIGINT DEFAULT NULL,
  `applicant_name` VARCHAR(128) DEFAULT NULL,
  `contact_tel` VARCHAR(128) DEFAULT NULL,
  `contact_name` VARCHAR(128) DEFAULT NULL,
  `remark` VARCHAR(2048) DEFAULT NULL COMMENT '备注',
  `approval_order_id` BIGINT DEFAULT NULL COMMENT '关联的审批单的id',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 采购单物品表
DROP TABLE IF EXISTS `eh_warehouse_purchase_items`;
CREATE TABLE `eh_warehouse_purchase_items`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `purchase_request_id` BIGINT NOT NULL COMMENT '所属的采购单id',
  `material_id` BIGINT DEFAULT NULL COMMENT '采购物品id',
  `purchase_quantity` BIGINT DEFAULT 0 COMMENT '采购数量',
  `unit_price` DECIMAL(20,2) DEFAULT 0.00 COMMENT '单价',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 为物品增加供应商字段
ALTER TABLE `eh_warehouse_materials` ADD COLUMN `supplier_id` BIGINT DEFAULT NULL COMMENT '物品的供应商的主键id';
ALTER TABLE `eh_warehouse_materials` ADD COLUMN `supplier_name` VARCHAR(128) DEFAULT NULL COMMENT '物品的供应商的名字';

DROP TABLE IF EXISTS `eh_warehouse_orders`;
-- 出入库单 模型
CREATE TABLE `eh_warehouse_orders`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `identity` VARCHAR(128) NOT NULL COMMENT '出入库单号',
  `executor_id` BIGINT DEFAULT NULL COMMENT '执行人id',
  `executor_name` VARCHAR(128) DEFAULT NULL COMMENT '执行人姓名',
  `executor_time` DATETIME DEFAULT NOW() COMMENT '执行时间',
  `service_type` TINYINT DEFAULT NULL COMMENT '服务类型，1. 普通入库,2.领用出库，3.采购入库',
  `community_id` BIGINT DEFAULT NULL COMMENT '园区id',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `i_service_type` (`service_type`) COMMENT '出入库状态得索引，用于搜索'
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- 仓库出入库单增加community_id字段
-- ALTER TABLE `eh_warehouse_orders` ADD COLUMN `community_id` BIGINT DEFAULT NULL COMMENT '园区id';
-- 增加出入库记录关联出入库单的字段
ALTER TABLE `eh_warehouse_stock_logs` ADD COLUMN `warehouse_order_id` BIGINT DEFAULT NULL COMMENT '关联的出入库单的id';
ALTER TABLE `eh_warehouse_requests` ADD COLUMN `requisition_id` BIGINT DEFAULT NULL COMMENT '关联的请示单的id';

-- 请示单
DROP TABLE IF EXISTS `eh_requisitions`;
CREATE TABLE `eh_requisitions`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `identity` VARCHAR(32) NOT NULL COMMENT '请示单号',
  `theme` VARCHAR(32) NOT NULL COMMENT '请示主题',
  `requisition_type_id` BIGINT DEFAULT NULL COMMENT '请示类型,参考eh_requisition_type表',
  `applicant_name` VARCHAR(128) NOT NULL COMMENT '请示人id',
  `applicant_department` VARCHAR(256) DEFAULT NULL COMMENT '请示人部门',
  `amount` DECIMAL (20,2) DEFAULT 0.00 COMMENT '申请金额',
  `description` TEXT DEFAULT NULL COMMENT '申请说明',
  `attachment_url` VARCHAR(2048) DEFAULT NULL COMMENT '附件地址',
  `status` TINYINT DEFAULT 1 NOT NULL COMMENT '审批状态，1:处理中；2:已完成; 3:已取消;',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 请示单类型
DROP TABLE IF EXISTS `eh_requisition_types`;
CREATE TABLE `eh_requisition_types`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `name` VARCHAR(32) NOT NULL COMMENT '类型名字',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 仓库管理2.0 end of the script

-- 增加版本号 201801252125
ALTER TABLE `eh_portal_content_scopes` ADD COLUMN `version_id`  BIGINT(20) NULL;
ALTER TABLE `eh_portal_launch_pad_mappings` ADD COLUMN `version_id`  BIGINT(20) NULL;


-- 薪酬2.0 

 
-- added by wh 
-- drop语句是必须的,因为之前薪酬1.0的表要删掉,数据全不要
-- 薪酬设置可以用的基础字段项(可以被公司继承,不可删除)
DROP TABLE IF EXISTS eh_salary_default_entities;
CREATE TABLE `eh_salary_default_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `editable_flag` TINYINT COMMENT '是否可编辑:-1 数值也不能编辑 0-否   1-是',
  `delete_flag` TINYINT COMMENT '是否可删除:0-否   1-是',
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `name` VARCHAR(64), 
  `description` TEXT COMMENT '说明文字',
  `template_name` VARCHAR(32) COMMENT '',
  `default_order` INT,
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '默认是否开启0不开启 2-开启',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;



-- 薪酬批次可用的选项的标签类型 基础数据
DROP TABLE IF EXISTS eh_salary_entity_categories;
CREATE TABLE `eh_salary_entity_categories` (
  `id` BIGINT,
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用', 
  `category_name` VARCHAR(64)  COMMENT 'name of category',
  `description` TEXT COMMENT '说明文字',
  `custom_flag` TINYINT COMMENT '是否可以自定义: 0-否 1-是',
  `custom_type` TINYINT COMMENT '自定义字段的类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `status` TINYINT ,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次包含的选项
DROP TABLE IF EXISTS eh_salary_group_entities;
CREATE TABLE `eh_salary_group_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `default_id` BIGINT COMMENT 'id of the eh_salary_default_entities', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的', 
  `origin_entity_id` BIGINT,
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用',
  `default_flag` TINYINT COMMENT '是否是缺省启用参数:0-否 1-是',
  `editable_flag` TINYINT COMMENT '是否可编辑:0-否   1-是',
  `delete_flag` TINYINT COMMENT '是否可删除:0-否   1-是',
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `name` VARCHAR(64), 
  `description` TEXT COMMENT '说明文字',
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `operator_uid` BIGINT,
  `update_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 某个人的薪酬设定
DROP TABLE IF EXISTS eh_salary_employee_origin_vals;
CREATE TABLE `eh_salary_employee_origin_vals` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `namespace_id` INT ,
  `user_id` BIGINT ,
  `user_detail_id` BIGINT , 
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `group_entity_name` VARCHAR(64),
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `salary_value` TEXT COMMENT '值:如果次月清空则在新建报表时候置为null', 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次每期的数据 
DROP TABLE IF EXISTS eh_salary_groups;
CREATE TABLE `eh_salary_groups` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(8) COMMENT 'example:201705', 
  `creator_uid` BIGINT COMMENT'创建者',
  `create_time` DATETIME,  
  `creator_Name` VARCHAR(128) COMMENT'创建者姓名',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;

-- 薪酬批次归档数据
-- DROP TABLE IF EXISTS eh_salary_groups_files;
CREATE TABLE `eh_salary_groups_files` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(8) COMMENT 'example:201705', 
  `creator_Name` VARCHAR(128) COMMENT'创建者姓名',
  `creator_uid` BIGINT COMMENT'创建者',
  `create_time` DATETIME,  
  `filer_Name` VARCHAR(128) COMMENT'归档者姓名',
  `filer_uid` BIGINT COMMENT'归档者',
  `file_time` DATETIME,  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;

-- 薪酬批次每个人的每期数据
DROP TABLE IF EXISTS eh_salary_employees;
CREATE TABLE `eh_salary_employees` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `namespace_id` INT ,
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `user_id` BIGINT ,
  `user_detail_id` BIGINT , 
  `regular_salary` DECIMAL (10, 2) COMMENT '固定工资合计',
  `should_pay_salary` DECIMAL (10, 2) COMMENT '应发工资合计',
  `real_pay_salary` DECIMAL (10, 2) COMMENT '实发工资合计',
  `cost_salary` DECIMAL (10, 2) COMMENT '人工成本合计',
  `creator_uid` BIGINT COMMENT'人员id',
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '状态0-正常 1-实发合计为负  2-未定薪',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;




-- 薪酬批次每个人的归档数据
DROP TABLE IF EXISTS eh_salary_employees_files;
CREATE TABLE `eh_salary_employees_files` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `namespace_id` INT ,
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `user_id` BIGINT ,
  `user_detail_id` BIGINT , 
  `regular_salary` DECIMAL (10, 2) COMMENT '固定工资合计',
  `should_pay_salary` DECIMAL (10, 2) COMMENT '应发工资合计',
  `real_pay_salary` DECIMAL (10, 2) COMMENT '实发工资合计',
  `cost_salary` DECIMAL (10, 2) COMMENT '人工成本合计',
  `creator_uid` BIGINT COMMENT'人员id',
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '状态0-正常 1-实发合计为负  2-未定薪',
  `filer_uid` BIGINT COMMENT'创建者',
  `file_time` DATETIME,  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次每个人的每期数据
DROP TABLE IF EXISTS eh_salary_depart_statistics;
CREATE TABLE `eh_salary_depart_statistics` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `dept_id` BIGINT (20) DEFAULT NULL COMMENT 'user department id',
  `dept_name` VARCHAR (128) DEFAULT NULL COMMENT 'user department name',
  `namespace_id` INT ,
  `salary_period` VARCHAR(8) COMMENT 'example:201705', 
  `regular_salary` DECIMAL (10, 2) COMMENT '固定工资合计',
  `should_pay_salary` DECIMAL (10, 2) COMMENT '应发工资合计',
  `real_pay_salary` DECIMAL (10, 2) COMMENT '实发工资合计',
  `cost_salary` DECIMAL (10, 2) COMMENT '人工成本合计',
  `cost_mom_salary` DECIMAL (10, 2) COMMENT '人工成本环比',
  `cost_yoy_salary` DECIMAL (10, 2) COMMENT '人工成本同比',
  `creator_uid` BIGINT COMMENT'人员id',
  `create_time` DATETIME, 
  `is_file` TINYINT COMMENT '0-普通 1-归档',
  `filer_uid` BIGINT COMMENT'创建者',
  `file_time` DATETIME,  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;

-- 薪酬薪酬人员每期的实际值
DROP TABLE IF EXISTS eh_salary_employee_period_vals;
CREATE TABLE `eh_salary_employee_period_vals` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `user_id` BIGINT ,
  `user_detail_id` BIGINT , 
  `salary_employee_id` BIGINT COMMENT '标签(统计分类) salary_employee表pk', 
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `group_entity_name` VARCHAR(64),
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `salary_value` TEXT COMMENT '值:如果次月清空则在新建报表时候置为null', 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;

-- 薪酬批次归档文件
-- DROP TABLE IF EXISTS eh_salary_groups_report_resources;
CREATE TABLE `eh_salary_groups_report_resources` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(8) COMMENT 'example:201705', 
  `report_type` TINYINT COMMENT'文件类型:0-工资明细 1-部门汇总',
  `uri` VARCHAR(1024),
  `url` VARCHAR(1024),
  `creator_Name` VARCHAR(128) COMMENT'创建者姓名',
  `creator_uid` BIGINT COMMENT'创建者',
  `create_time` DATETIME,  
  `filer_Name` VARCHAR(128) COMMENT'归档者姓名',
  `filer_uid` BIGINT COMMENT'归档者',
  `file_time` DATETIME,  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;
-- 薪酬2.0结束   
   
-- 请示单增加编号字段
ALTER TABLE `eh_payment_applications` ADD COLUMN `application_number`  VARCHAR(32);


ALTER TABLE `eh_lease_promotions`
ADD COLUMN `house_resource_type` VARCHAR(256) NULL COMMENT '房源类型  rentHouse 出租房源   sellHouse 出售房源' AFTER `category_id`;



-- 公告管理 add by zhiwei.zhang 
-- 企业公告1.0
-- 企业公告表
CREATE TABLE `eh_enterprise_notices` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL,
  `title` VARCHAR(256) NOT NULL COMMENT '企业公告标题',
  `summary` VARCHAR(512) COMMENT '摘要',
  `content_type` VARCHAR(32),
  `content` TEXT COMMENT '公告正文',
  `publisher` VARCHAR(256) COMMENT '公告发布者',
  `secret_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '状态 : 0-(PUBLIC)公开, 1-(PRIVATE)保密',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 : 0-(DELETED)已删除, 1-(DRAFT)草稿, 2-(ACTIVE)已发送, 3-(INACTIVE)已撤销',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `operator_name` VARCHAR(128) COMMENT 'the name of the operator',
  `delete_uid` BIGINT,
  `delete_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `i_notices_namespace_id`(`namespace_id`),
  KEY `i_notices_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

-- 企业公告附件表
CREATE TABLE `eh_enterprise_notice_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `notice_id` BIGINT NOT NULL COMMENT 'key of the table eh_enterprise_notices',
  `content_name` VARCHAR(256) NOT NULL COMMENT 'the name of the content',
  `content_suffix` VARCHAR(64) COMMENT 'the suffix of the file',
  `size` INT NOT NULL DEFAULT 0 COMMENT 'the size of the content',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_notice_attachment_notice_id`(`notice_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


-- 企业公告发送信息表
CREATE TABLE `eh_enterprise_notice_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `notice_id` BIGINT NOT NULL COMMENT 'key of table the eh_enterprise_notices',
  `receiver_type` VARCHAR(64) NOT NULL COMMENT 'DEPARTMENT OR MEMBER',
  `receiver_id` BIGINT NOT NULL,
  `name` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_notice_receivers_notice_id`(`notice_id`),
  KEY `i_notice_receivers_receiver_id`(`receiver_type`,`receiver_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

--  end by zhiwei.zhang


-- 社保开始
-- added by wh 
-- 社保建表social_security
-- 社保基准表
-- DROP TABLE eh_social_security_bases;
CREATE TABLE `eh_social_security_bases` (
  `id` BIGINT,
  `city_id` BIGINT DEFAULT '0',
  `household_type` VARCHAR (32) COMMENT '户籍类型',
  `pay_item` VARCHAR (32) COMMENT '缴费项:医疗/养老/失业/工伤/生育/大病/残障金/补充医疗',
  `company_radix_min` DECIMAL (10, 2) COMMENT '企业基数最小值',
  `company_radix_max` DECIMAL (10, 2) COMMENT '企业基数最大值',
  `company_ratio_min` INT COMMENT '企业比例最小值 万分之 eq:100=1%;1=0.01%',
  `company_ratio_max` INT COMMENT '企业比例最大值 万分之 eq:100=1%;1=0.01%',
  `employee_radix_min` DECIMAL (10, 2) COMMENT '个人基数最小值',
  `employee_radix_max` DECIMAL (10, 2) COMMENT ' 个人基数最大值',
  `employee_ratio_min` INT COMMENT '个人比例最小值 万分之 eq:100=1%;1=0.01%',
  `employee_ratio_max` INT COMMENT '个人比例最大值 万分之 eq:100=1%;1=0.01%',
  `editable_flag` TINYINT COMMENT '是否可编辑',
  `is_default` TINYINT COMMENT '是否是默认选项(1-是:普通社保;0-否:补充保险)',
  `effect_time_begin` DATETIME COMMENT '生效起始日期',
  `effect_time_end` DATETIME COMMENT '生效结束日期',
  `ratio_options` TEXT COMMENT '比例可选项,如果为null就是手动填写:eq:[120,230,380,480,520]',
  `accum_or_socail` TINYINT COMMENT '社保/公积金标识 : 1-社保 2-公积金',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_city_id` (`city_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- 社保设置表
-- DROP TABLE eh_social_security_settings;
CREATE TABLE `eh_social_security_settings` (
  `id` BIGINT,
  `city_id` BIGINT DEFAULT '0',
  `namespace_id` INT (11) DEFAULT '0',
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `household_type` VARCHAR (32) COMMENT '户籍类型',
  `pay_item` VARCHAR (32) COMMENT '缴费项:医疗/养老/失业/工伤/生育/大病/残障金/补充医疗',
  `radix` DECIMAL (10, 2) COMMENT '基数',
  `company_radix` DECIMAL (10, 2) COMMENT '企业基数',
  `company_ratio` INT COMMENT '企业比例万分之 eq:100=1%;1=0.01%',
  `employee_radix` DECIMAL (10, 2) COMMENT '个人基数',
  `employee_ratio` INT COMMENT '个人比例 万分之 eq:100=1%;1=0.01%',
  `editable_flag` TINYINT COMMENT '是否可编辑',
  `is_default` TINYINT COMMENT '是否是默认选项(1-是:普通社保;0-否:补充保险)',
  `accum_or_socail` TINYINT COMMENT '社保/公积金标识 : 1-社保 2-公积金',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- 社保组表 存新建报表的人和归档的人
-- DROP TABLE eh_social_security_groups;
CREATE TABLE `eh_social_security_groups` (
  `id` BIGINT, 
  `organization_id` BIGINT, 
  `pay_month` VARCHAR (8) DEFAULT NULL COMMENT 'yyyymm',
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `file_uid` BIGINT (20) COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- 社保缴费表
-- DROP TABLE eh_social_security_payments;
CREATE TABLE `eh_social_security_payments` (
  `id` BIGINT,
  `city_id` BIGINT DEFAULT '0',
  `namespace_id` INT (11) DEFAULT '0',
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `pay_month` VARCHAR (8) DEFAULT NULL COMMENT 'yyyymm',
  `after_pay_flag` TINYINT DEFAULT 0 COMMENT '补缴标记',
  `household_type` VARCHAR (32) COMMENT '户籍类型',
  `pay_item` VARCHAR (32) COMMENT '缴费项:医疗/养老/失业/工伤/生育/大病/残障金/补充医疗',
  `company_radix` DECIMAL (10, 2) COMMENT '企业基数',
  `company_ratio` INT COMMENT '企业比例万分之 eq:100=1%;1=0.01%',
  `employee_radix` DECIMAL (10, 2) COMMENT '个人基数',
  `employee_ratio` INT COMMENT '个人比例 万分之 eq:100=1%;1=0.01%',
  `editable_flag` TINYINT COMMENT '是否可编辑',
  `is_default` TINYINT COMMENT '是否是默认选项(1-是:普通社保;0-否:补充保险)',
  `accum_or_socail` TINYINT COMMENT '社保/公积金标识 : 1-社保 2-公积金',
  `is_new` TINYINT COMMENT '增减员:0正常,1增员,-1减员',
  `is_work` TINYINT COMMENT '入职离职:0正常,1入职,-1离职',
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `file_uid` BIGINT (20) COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- 社保缴费历史表
-- DROP TABLE eh_social_security_payment_logs;
CREATE TABLE `eh_social_security_payment_logs` (
  `id` BIGINT,
  `city_id` BIGINT DEFAULT '0',
  `namespace_id` INT (11) DEFAULT '0',
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `pay_month` VARCHAR (8) DEFAULT NULL COMMENT 'yyyymm',
  `after_pay_flag` TINYINT DEFAULT 0 COMMENT '补缴标记',
  `household_type` VARCHAR (32) COMMENT '户籍类型',
  `pay_item` VARCHAR (32) COMMENT '缴费项:医疗/养老/失业/工伤/生育/大病/残障金/补充医疗',
  `company_radix` DECIMAL (10, 2) COMMENT '企业基数',
  `company_ratio` INT COMMENT '企业比例万分之 eq:100=1%;1=0.01%',
  `employee_radix` DECIMAL (10, 2) COMMENT '个人基数',
  `employee_ratio` INT COMMENT '个人比例 万分之 eq:100=1%;1=0.01%',
  `editable_flag` TINYINT COMMENT '是否可编辑',
  `is_default` TINYINT COMMENT '是否是默认选项(1-是:普通社保;0-否:补充保险)',
  `accum_or_socail` TINYINT COMMENT '社保/公积金标识 : 1-社保 2-公积金',
  `is_new` TINYINT COMMENT '增减员:0正常,1增员,-1减员',
  `is_work` TINYINT COMMENT '入职离职:0正常,1入职,-1离职', 
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `file_uid` BIGINT (20) COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- 汇总表
-- DROP TABLE eh_social_security_summary;
CREATE TABLE `eh_social_security_summary` (
  `id` BIGINT,
  `namespace_id` INT (11) DEFAULT '0',
  `organization_id` BIGINT,
  `pay_month` VARCHAR (8) DEFAULT NULL COMMENT 'yyyymm',
  `company_payment` DECIMAL (10, 2) COMMENT '企业缴纳',
  `employee_payment` DECIMAL (10, 2) COMMENT '个人缴纳',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `file_uid` BIGINT (20) COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`), 
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- 部门汇总表
-- DROP TABLE eh_social_security_department_summary;
CREATE TABLE `eh_social_security_department_summary` (
  `id` BIGINT,
  `namespace_id` INT (11) DEFAULT '0',
  `owner_id` BIGINT COMMENT '所属企业',
  `dept_id` BIGINT (20) DEFAULT NULL COMMENT 'user department id',
  `dept_name` VARCHAR (128) DEFAULT NULL COMMENT 'user department name',
  `pay_month` VARCHAR (8) DEFAULT NULL COMMENT 'yyyymm',
  `employee_count` INT COMMENT '人数', 
  `social_security_sum` DECIMAL (10, 2) COMMENT '社保合计',
  `social_security_company_sum` DECIMAL (10, 2) COMMENT '社保企业合计',
  `social_security_employee_sum` DECIMAL (10, 2) COMMENT '社保个人合计',
  `pension_company_sum` DECIMAL (10, 2) COMMENT '养老企业合计',
  `pension_employee_sum` DECIMAL (10, 2) COMMENT '养老个人合计',
  `medical_company_sum` DECIMAL (10, 2) COMMENT '医疗企业合计',
  `medical_employee_sum` DECIMAL (10, 2) COMMENT '医疗个人合计',
  `injury_company_sum` DECIMAL (10, 2) COMMENT '工伤企业合计',
  `injury_employee_sum` DECIMAL (10, 2) COMMENT '工伤个人合计',
  `unemployment_company_sum` DECIMAL (10, 2) COMMENT '失业企业合计',
  `unemployment_employee_sum` DECIMAL (10, 2) COMMENT '失业个人合计',
  `birth_company_sum` DECIMAL (10, 2) COMMENT '生育企业合计',
  `birth_employee_sum` DECIMAL (10, 2) COMMENT '生育个人合计',
  `critical_illness_company_sum` DECIMAL (10, 2) COMMENT '大病企业合计',
  `critical_illness_employee_sum` DECIMAL (10, 2) COMMENT '大病个人合计',
  `after_social_security_company_sum` DECIMAL (10, 2) COMMENT '补缴社保企业合计',
  `after_social_security_employee_sum` DECIMAL (10, 2) COMMENT '补缴社保个人合计',
  `after_pension_company_sum` DECIMAL (10, 2) COMMENT '补缴养老企业合计',
  `after_pension_employee_sum` DECIMAL (10, 2) COMMENT '补缴养老个人合计',
  `after_medical_company_sum` DECIMAL (10, 2) COMMENT '补缴医疗企业合计',
  `after_medical_employee_sum` DECIMAL (10, 2) COMMENT '补缴医疗个人合计',
  `after_injury_company_sum` DECIMAL (10, 2) COMMENT '补缴工伤企业合计',
  `after_injury_employee_sum` DECIMAL (10, 2) COMMENT '补缴工伤个人合计',
  `after_unemployment_company_sum` DECIMAL (10, 2) COMMENT '补缴失业企业合计',
  `after_unemployment_employee_sum` DECIMAL (10, 2) COMMENT '补缴失业个人合计',
  `after_birth_company_sum` DECIMAL (10, 2) COMMENT '补缴生育企业合计',
  `after_birth_employee_sum` DECIMAL (10, 2) COMMENT '补缴生育个人合计',
  `after_critical_illness_company_sum` DECIMAL (10, 2) COMMENT '补缴大病企业合计',
  `after_critical_illness_employee_sum` DECIMAL (10, 2) COMMENT '补缴大病个人合计',
  `disability_sum` DECIMAL (10, 2) COMMENT '残障金',
  `commercial_insurance` DECIMAL (10, 2) COMMENT '商业保险', 
  `accumulation_fund_sum` DECIMAL (10, 2) COMMENT '公积金合计',
  `accumulation_fund_company_sum` DECIMAL (10, 2) COMMENT '公积金企业合计',
  `accumulation_fund_employee_sum` DECIMAL (10, 2) COMMENT '公积金个人合计',
  `after_accumulation_fund_company_sum` DECIMAL (10, 2) COMMENT '补缴公积金企业合计',
  `after_accumulation_fund_employee_sum` DECIMAL (10, 2) COMMENT '补缴公积金个人合计',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `file_uid` BIGINT (20) COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_owner_id` (`owner_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- 社保报表
-- DROP TABLE eh_social_security_report;
CREATE TABLE `eh_social_security_report` (
  `id` BIGINT,
  `namespace_id` INT (11) DEFAULT '0',
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `user_name` VARCHAR (128) COMMENT '姓名',
  `entry_date` DATE COMMENT '入职日期',
  `contact_token` VARCHAR (128) DEFAULT NULL COMMENT '手机号',
  `id_number` VARCHAR (128) DEFAULT NULL COMMENT '身份证号',
  `degree` VARCHAR (64) DEFAULT NULL COMMENT '学历',
  `salary_card_bank` VARCHAR (64) DEFAULT NULL COMMENT '开户行',
  `salary_card_number` VARCHAR (128) DEFAULT NULL COMMENT '工资卡号',
  `dept_name` VARCHAR (128) DEFAULT NULL COMMENT '部门',
  `social_security_number` VARCHAR (128) DEFAULT NULL COMMENT '社保号',
  `provident_fund_number` VARCHAR (128) DEFAULT NULL COMMENT '公积金号',
  `out_work_date` DATE COMMENT '离职日期',
  `household_type` VARCHAR (32) COMMENT '户籍类型',
  `social_security_city_id` BIGINT COMMENT '参保城市id',
  `social_security_city_name` VARCHAR (32) COMMENT '参保城市',
  `pay_month` VARCHAR (8) DEFAULT NULL COMMENT '社保月份',
  `social_security_radix` DECIMAL (10, 2) COMMENT '社保基数',
  `social_security_sum` DECIMAL (10, 2) COMMENT '社保合计',
  `social_security_company_sum` DECIMAL (10, 2) COMMENT '社保企业合计',
  `social_security_employee_sum` DECIMAL (10, 2) COMMENT '社保个人合计',
  `accumulation_fund_city_id` BIGINT COMMENT '公积金城市id',
  `accumulation_fund_city_name` VARCHAR (32) COMMENT '公积金城市',
  `accumulation_fund_radix` DECIMAL (10, 2) COMMENT '公积金基数',
  `accumulation_fund_company_radix` DECIMAL (10, 2) COMMENT '公积金企业基数',
  `accumulation_fund_company_ratio` INT COMMENT '公积金企业比例万分之 eq:100=1%;1=0.01%',
  `accumulation_fund_employee_radix` DECIMAL (10, 2) COMMENT '公积金个人基数',
  `accumulation_fund_employee_ratio` INT COMMENT '公积金个人比例 万分之 eq:100=1%;1=0.01%',
  `accumulation_fund_sum` DECIMAL (10, 2) COMMENT '公积金合计',
  `accumulation_fund_company_sum` DECIMAL (10, 2) COMMENT '公积金企业合计',
  `accumulation_fund_employee_sum` DECIMAL (10, 2) COMMENT '公积金个人合计',
  `accumulation_fund_tax` DECIMAL (10, 2) COMMENT '公积金需纳税额',
  `pension_company_radix` DECIMAL (10, 2) COMMENT '养老保险企业基数',
  `pension_company_ratio` INT COMMENT '养老保险企业比例万分之 eq:100=1%;1=0.01%',
  `pension_employee_radix` DECIMAL (10, 2) COMMENT '养老保险个人基数',
  `pension_employee_ratio` INT COMMENT '养老保险个人比例 万分之 eq:100=1%;1=0.01%',
  `pension_company_sum` DECIMAL (10, 2) COMMENT '养老保险企业合计',
  `pension_employee_sum` DECIMAL (10, 2) COMMENT '养老保险个人合计',
  `medical_company_radix` DECIMAL (10, 2) COMMENT '医疗保险企业基数',
  `medical_company_ratio` INT COMMENT '医疗保险企业比例万分之 eq:100=1%;1=0.01%',
  `medical_employee_radix` DECIMAL (10, 2) COMMENT '医疗保险个人基数',
  `medical_employee_ratio` INT COMMENT '医疗保险个人比例 万分之 eq:100=1%;1=0.01%',
  `medical_company_sum` DECIMAL (10, 2) COMMENT '医疗保险企业合计',
  `medical_employee_sum` DECIMAL (10, 2) COMMENT '医疗保险个人合计',
  `injury_company_radix` DECIMAL (10, 2) COMMENT '工伤保险企业基数',
  `injury_company_ratio` INT COMMENT '工伤保险企业比例万分之 eq:100=1%;1=0.01%',
  `injury_employee_radix` DECIMAL (10, 2) COMMENT '工伤保险个人基数',
  `injury_employee_ratio` INT COMMENT '工伤保险个人比例 万分之 eq:100=1%;1=0.01%',
  `injury_company_sum` DECIMAL (10, 2) COMMENT '工伤保险企业合计',
  `injury_employee_sum` DECIMAL (10, 2) COMMENT '工伤保险个人合计',
  `unemployment_company_radix` DECIMAL (10, 2) COMMENT '失业保险企业基数',
  `unemployment_company_ratio` INT COMMENT '失业保险企业比例万分之 eq:100=1%;1=0.01%',
  `unemployment_employee_radix` DECIMAL (10, 2) COMMENT '失业保险个人基数',
  `unemployment_employee_ratio` INT COMMENT '失业保险个人比例 万分之 eq:100=1%;1=0.01%',
  `unemployment_company_sum` DECIMAL (10, 2) COMMENT '失业保险企业合计',
  `unemployment_employee_sum` DECIMAL (10, 2) COMMENT '失业保险个人合计',
  `birth_company_radix` DECIMAL (10, 2) COMMENT '生育保险企业基数',
  `birth_company_ratio` INT COMMENT '生育保险企业比例万分之 eq:100=1%;1=0.01%',
  `birth_employee_radix` DECIMAL (10, 2) COMMENT '生育保险个人基数',
  `birth_employee_ratio` INT COMMENT '生育保险个人比例 万分之 eq:100=1%;1=0.01%',
  `birth_company_sum` DECIMAL (10, 2) COMMENT '生育保险企业合计',
  `birth_employee_sum` DECIMAL (10, 2) COMMENT '生育保险个人合计',
  `critical_illness_company_radix` DECIMAL (10, 2) COMMENT '大病保险企业基数',
  `critical_illness_company_ratio` INT COMMENT '大病保险企业比例万分之 eq:100=1%;1=0.01%',
  `critical_illness_employee_radix` DECIMAL (10, 2) COMMENT '大病保险个人基数',
  `critical_illness_employee_ratio` INT COMMENT '大病保险个人比例 万分之 eq:100=1%;1=0.01%',
  `critical_illness_company_sum` DECIMAL (10, 2) COMMENT '大病保险企业合计',
  `critical_illness_employee_sum` DECIMAL (10, 2) COMMENT '大病保险个人合计',
  `after_social_security_company_sum` DECIMAL (10, 2) COMMENT '补缴社保企业合计',
  `after_social_security_employee_sum` DECIMAL (10, 2) COMMENT '补缴社保个人合计',
  `after_accumulation_fund_company_sum` DECIMAL (10, 2) COMMENT '补缴公积金企业合计',
  `after_accumulation_fund_employee_sum` DECIMAL (10, 2) COMMENT '补缴公积金个人合计',
  `after_pension_company_sum` DECIMAL (10, 2) COMMENT '补缴养老企业合计',
  `after_pension_employee_sum` DECIMAL (10, 2) COMMENT '补缴养老个人合计',
  `after_medical_company_sum` DECIMAL (10, 2) COMMENT '补缴医疗企业合计',
  `after_medical_employee_sum` DECIMAL (10, 2) COMMENT '补缴医疗个人合计',
  `after_injury_company_sum` DECIMAL (10, 2) COMMENT '补缴工伤企业合计',
  `after_injury_employee_sum` DECIMAL (10, 2) COMMENT '补缴工伤个人合计',
  `after_unemployment_company_sum` DECIMAL (10, 2) COMMENT '补缴失业企业合计',
  `after_unemployment_employee_sum` DECIMAL (10, 2) COMMENT '补缴失业个人合计',
  `after_birth_company_sum` DECIMAL (10, 2) COMMENT '补缴生育企业合计',
  `after_birth_employee_sum` DECIMAL (10, 2) COMMENT '补缴生育个人合计',
  `after_critical_illness_company_sum` DECIMAL (10, 2) COMMENT '补缴大病企业合计',
  `after_critical_illness_employee_sum` DECIMAL (10, 2) COMMENT '补缴大病个人合计',
  `disability_sum` DECIMAL (10, 2) COMMENT '残障金',
  `commercial_insurance` DECIMAL (10, 2) COMMENT '商业保险',
  `is_work` TINYINT COMMENT '入职离职:0正常,1入职,-1离职',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `file_uid` BIGINT (20) COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- 社保增减员报表
-- DROP TABLE eh_social_security_inout_report;
CREATE TABLE `eh_social_security_inout_report` (
  `id` BIGINT,
  `namespace_id` INT (11) DEFAULT '0',
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `user_name` VARCHAR (128) COMMENT '姓名',
  `entry_date` DATE COMMENT '入职日期',
  `out_work_date` DATE COMMENT '离职日期',
  `contact_token` VARCHAR (128) DEFAULT NULL COMMENT '手机号',
  `id_number` VARCHAR (128) DEFAULT NULL COMMENT '身份证号',
  `degree` VARCHAR (64) DEFAULT NULL COMMENT '学历',
  `salary_card_bank` VARCHAR (64) DEFAULT NULL COMMENT '开户行',
  `salary_card_number` VARCHAR (128) DEFAULT NULL COMMENT '工资卡号',
  `dept_name` VARCHAR (128) DEFAULT NULL COMMENT '部门',
  `social_security_number` VARCHAR (128) DEFAULT NULL COMMENT '社保号',
  `provident_fund_number` VARCHAR (128) DEFAULT NULL COMMENT '公积金号',
  `household_type` VARCHAR (32) COMMENT '户籍类型',
  `social_security_city_id` BIGINT COMMENT '参保城市id',
  `social_security_city_name` VARCHAR (32) COMMENT '参保城市',
  `pay_month` VARCHAR (8) DEFAULT NULL COMMENT '社保月份',
  `social_security_radix` DECIMAL (10, 2) COMMENT '社保基数',
  `social_security_increase` VARCHAR (8) COMMENT '社保增',
  `social_security_decrease` VARCHAR (8) COMMENT '社保减',
  `social_security_after` DECIMAL (10, 2) COMMENT '社保补缴',
  `accumulation_fund_city_id` BIGINT COMMENT '公积金城市id',
  `accumulation_fund_city_name` VARCHAR (32) COMMENT '公积金城市',
  `accumulation_fund_radix` DECIMAL (10, 2) COMMENT '公积金基数',
  `accumulation_fund_increase` VARCHAR (8) COMMENT '公积金增',
  `accumulation_fund_decrease` VARCHAR (8) COMMENT '公积金减',
  `accumulation_fund_after` DECIMAL (10, 2) COMMENT '公积金补缴',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `file_uid` BIGINT (20) COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- 社保增减时间表
-- drop table eh_social_security_inout_time;
CREATE TABLE `eh_social_security_inout_time` (
  `id` BIGINT,
  `namespace_id` INT (11) DEFAULT '0',
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `type` TINYINT NOT NULL COMMENT '0-SOCIAL SECURITY, 1-ACCUMULATION FUND',
  `start_month` VARCHAR(8) COMMENT 'the start month, yyyyMM',
  `end_month` VARCHAR(8) COMMENT 'the end month, yyyyMM',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `eh_social_security_inout_log` (
  `id` BIGINT,
  `namespace_id` INT (11) DEFAULT '0',
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `type` TINYINT NOT NULL COMMENT '0社保增员1社保减员2公积金增员3公积金减员4社保补缴5公积金补缴',
  `log_month` VARCHAR(8) COMMENT 'the start month, yyyyMM',
  `log_date`DATE COMMENT 'log具体日期',
  `creator_uid` BIGINT  DEFAULT '0',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

ALTER TABLE `eh_organization_member_details` ADD COLUMN `social_security_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-pending, 1-paying';
ALTER TABLE `eh_organization_member_details` ADD COLUMN `accumulation_fund_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-pending, 1-paying';

-- 社保结束

-- 工位预定开始 dengs

-- by dengs. 工位预约添加范围 20180120
ALTER TABLE `eh_office_cubicle_spaces` ADD COLUMN `owner_type` VARCHAR(128);
ALTER TABLE `eh_office_cubicle_spaces` ADD COLUMN `owner_id` BIGINT;
ALTER TABLE `eh_office_cubicle_categories` ADD COLUMN `status` TINYINT;

ALTER TABLE `eh_office_cubicle_categories` ADD COLUMN `position_nums` INTEGER;
ALTER TABLE `eh_office_cubicle_categories` ADD COLUMN `name` VARCHAR(256);

ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `flow_case_Id` BIGINT;
ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `work_flow_status` TINYINT;
ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `owner_type` VARCHAR(128);
ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `owner_id` BIGINT;
ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `position_nums` INTEGER;
ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `category_name` VARCHAR(256);
ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `category_id` BIGINT;

CREATE TABLE `eh_office_cubicle_ranges` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the ranges, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `space_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- 工位预定结束

-- 资源预订3.2
ALTER TABLE `eh_rentalv2_items`
  CHANGE COLUMN `rental_resource_id` `source_id` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_rentalv2_items`
  ADD COLUMN `source_type` VARCHAR(45) NULL COMMENT 'DEFAULT: default_rule  RESOURCE: resource_rule' AFTER `id`;

UPDATE eh_rentalv2_items set source_type = 'default_rule' where source_type IS NULL;

ALTER TABLE `eh_rentalv2_config_attachments`
ADD COLUMN `default_order` INT NULL DEFAULT '0' AFTER `string_tag5`;

UPDATE eh_rentalv2_default_rules set rental_start_time = 7776000000 where rental_start_time = 0;
UPDATE eh_rentalv2_default_rules set rental_start_time_flag = 1;
-- 资源预订3.2结束

-- 审批2.0 & 人事2.0.1 start by nan.rong
ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_remark` VARCHAR(256) COMMENT 'the remark of the approval';

ALTER TABLE `eh_general_approvals` ADD COLUMN `default_order` INTEGER NOT NULL DEFAULT 0;

ALTER TABLE `eh_general_approvals` ADD COLUMN `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the userId of the operator';

ALTER TABLE `eh_general_approvals` ADD COLUMN `operator_name` VARCHAR(128) COMMENT 'the real name of the operator';

-- DROP TABLE IF EXISTS `eh_general_approval_scope_map`;
CREATE TABLE `eh_general_approval_scope_map` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `approval_id` BIGINT NOT NULL COMMENT 'id of the approval',
  `source_type` VARCHAR(64) NOT NULL COMMENT 'ORGANIZATION, MEMBERDETAIL',
  `source_id` BIGINT NOT NULL COMMENT 'id of the source',
  `source_description` VARCHAR(128) COMMENT 'the description of the source',
  `create_time` DATETIME COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

UPDATE eh_general_approvals SET approval_attribute = 'CUSTOMIZE' WHERE approval_attribute IS NULL;
ALTER TABLE eh_general_approvals MODIFY approval_attribute VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'CUSTOMIZE, DEFAULT';

UPDATE eh_general_approvals SET modify_flag = 1 WHERE modify_flag IS NULL;
ALTER TABLE eh_general_approvals MODIFY modify_flag TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes';

UPDATE eh_general_approvals SET delete_flag = 1 WHERE delete_flag IS NULL;
ALTER TABLE eh_general_approvals MODIFY delete_flag TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes';

UPDATE eh_general_forms SET form_attribute = 'CUSTOMIZE' WHERE form_attribute IS NULL;
ALTER TABLE eh_general_forms MODIFY form_attribute VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'CUSTOMIZE, DEFAULT';

UPDATE eh_general_forms SET modify_flag = 1 WHERE modify_flag IS NULL;
ALTER TABLE eh_general_forms MODIFY modify_flag TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes';

UPDATE eh_general_forms SET delete_flag = 1 WHERE delete_flag IS NULL;
ALTER TABLE eh_general_forms MODIFY delete_flag TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes';

-- add by ryan at 03/01/02018.
ALTER TABLE eh_organization_member_details MODIFY employee_status TINYINT NOT NULL DEFAULT 0 COMMENT '0:probation, 1:on the job, 2:internship, 3:dismissal';

ALTER TABLE eh_archives_dismiss_employees MODIFY department VARCHAR(128) COMMENT '离职前部门';

ALTER TABLE eh_archives_dismiss_employees MODIFY employee_status TINYINT NOT NULL DEFAULT 0 COMMENT '0:probation, 1:on the job, 2:internship, 3:dismissal';

ALTER TABLE eh_archives_dismiss_employees ADD COLUMN department_id BIGINT COMMENT '离职前部门id';

ALTER TABLE eh_archives_dismiss_employees ADD COLUMN job_position VARCHAR(128) COMMENT '离职前职位';

ALTER TABLE eh_archives_dismiss_employees ADD COLUMN job_level VARCHAR(128) COMMENT '离职前职级';

ALTER TABLE eh_archives_logs MODIFY operation_reason VARCHAR(1024) COMMENT 'the reason of the operation';

-- end by nan.rong



