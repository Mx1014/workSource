-- domain 增加name, icon
ALTER TABLE `eh_domains` ADD COLUMN `favicon_uri`  varchar(255) NULL;
ALTER TABLE `eh_domains` ADD COLUMN `name`  varchar(255) NULL AFTER `namespace_id`;
ALTER TABLE `eh_domains` ADD COLUMN `login_bg_uri`  varchar(255) NULL;
ALTER TABLE `eh_domains` ADD COLUMN `login_logo_uri`  varchar(255) NULL;
ALTER TABLE `eh_domains` ADD COLUMN `menu_logo_uri`  varchar(255) NULL;
ALTER TABLE `eh_domains` ADD COLUMN `menu_logo_collapsed_uri`  varchar(255) NULL;

ALTER TABLE `eh_web_menu_scopes` ADD COLUMN `app_id`  bigint(20) NULL COMMENT ''eh_service_module_app id'';
ALTER TABLE `eh_web_menu_scopes` DROP INDEX `u_menu_scope_owner` , ADD UNIQUE INDEX `u_menu_scope_owner` (`menu_id`, `owner_type`, `owner_id`, `app_id`) USING BTREE ;

-- 账单item关联滞纳金 by wentian
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `late_fine_standard_id` BIGINT DEFAULT NULL COMMENT ''滞纳金标准id'';
-- 滞纳金表 by wentian
DROP TABLE IF EXISTS `eh_payment_late_fine`;
CREATE TABLE `eh_payment_late_fine`(
  `id` BIGINT NOT NULL COMMENT ''primary key'',
  `name` VARCHAR(20) COMMENT ''滞纳金名称'',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT ''0.00'' COMMENT ''amount of overdue payment'',
  `bill_id` BIGINT NOT NULL COMMENT ''the id of the corresponding bill, one to one'',
  `bill_item_id` BIGINT NOT NULL COMMENT ''the id of the corresponding bill item id, one to one'',
  `create_time` DATETIME DEFAULT NOW(),
  `upate_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `namespace_id` INTEGER DEFAULT NULL COMMENT ''location info, for possible statistics later'',
  `community_id` BIGINT DEFAULT NULL,
  `customer_id` BIGINT NOT NULL COMMENT ''allows searching taking advantage of it'',
  `customer_type` VARCHAR(20) NOT NULL COMMENT ''break of user info benefits'',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

-- 增加项目区分 by st.zheng
ALTER TABLE `eh_categories`
ADD COLUMN `owner_type` VARCHAR(32) NULL DEFAULT NULL AFTER `namespace_id`;
ALTER TABLE `eh_categories`
ADD COLUMN `owner_id` BIGINT(20) NULL DEFAULT ''0'' AFTER `owner_type`;

-- by st.zheng
ALTER TABLE `eh_pm_tasks`
  ADD COLUMN `refer_type` VARCHAR(32) NULL COMMENT ''引用类型'' AFTER `if_use_feelist`;
ALTER TABLE `eh_pm_tasks`
  ADD COLUMN `refer_id` BIGINT(20) NULL COMMENT ''引用id'' AFTER `refer_type`;

-- 物业巡检V3.1
-- 设备巡检计划表
CREATE TABLE `eh_equipment_inspection_plans` (
  `id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL DEFAULT ''0'' COMMENT ''organization_id'',
  `owner_type` varchar(32) NOT NULL DEFAULT '''' COMMENT ''organization'',
  `target_type` varchar(32) NOT NULL DEFAULT '''' COMMENT ''zone resource_type '',
  `target_id` bigint(20) NOT NULL DEFAULT ''0'' COMMENT ''zone  resource_id'',
  `plan_number` varchar(128) NOT NULL DEFAULT ''0'' COMMENT ''the plans number '',
  `plan_type` tinyint(4) NOT NULL DEFAULT ''0'' COMMENT ''the type of plan 0: 巡检  1: 保养'',
  `name` varchar(1024) DEFAULT NULL COMMENT ''the name of plan_number'',
  `status` tinyint(4) NOT NULL DEFAULT ''0'' COMMENT ''status of plans  0:waitting for starting 1: waitting for approving  2: QUALIFIED 3:UN_QUALIFIED'',
  `reviewer_uid` bigint(20) NOT NULL DEFAULT ''0'',
  `review_time` datetime DEFAULT NULL,
  `repeat_setting_id` bigint(20) NOT NULL DEFAULT ''0'' COMMENT ''refers to eh_repeatsetting '',
  `remarks` text,
  `creator_uid` bigint(20) NOT NULL DEFAULT ''0'',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleter_uid` bigint(20) DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `last_create_taskTime` datetime DEFAULT NULL COMMENT ''the last time when gen task'',
  `inspection_category_id` bigint(20) DEFAULT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 设备巡检计划--设备 关联表
CREATE TABLE `eh_equipment_inspection_equipment_plan_map` (
  `id` bigint(20) NOT NULL,
  `equiment_id` bigint(20) NOT NULL DEFAULT ''0'',
  `owner_id` bigint(20) NOT NULL DEFAULT ''0'',
  `owner_type` varchar(32) NOT NULL DEFAULT '''',
  `target_id` bigint(20) NOT NULL DEFAULT ''0'',
  `target_type` varchar(32) NOT NULL DEFAULT '''',
  `plan_id` bigint(20) NOT NULL DEFAULT ''0'',
  `standard_id` bigint(20) NOT NULL DEFAULT ''0'',
  `namespace_id` int(11) NOT NULL DEFAULT ''0'',
  `default_order` bigint(20) NOT NULL DEFAULT ''0'' COMMENT ''show order of equipment_maps'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 巡检计划 执行组审批组 关联表 start   by jiarui
CREATE TABLE `eh_equipment_inspection_plan_group_map` (
  `id` bigint(20) NOT NULL COMMENT ''id'',
  `group_type` tinyint(4) NOT NULL DEFAULT ''0'' COMMENT ''0: none, 1: executive group, 2: review group'',
  `plan_id` bigint(20) NOT NULL DEFAULT ''0'' COMMENT ''refernece to the id of eh_equipment_inspection_plans'',
  `group_id` bigint(20) NOT NULL DEFAULT ''0'' COMMENT ''refernece to the id of eh_organizations'',
  `position_id` bigint(20) NOT NULL DEFAULT ''0'' COMMENT ''refernece to the id of eh_organization_job_positions'',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 巡检计划 执行组审批组 关联表 end  by jiarui

CREATE TABLE `eh_equipment_inspection_review_date` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL COMMENT ''refer to object type EhEquipmentInspectionTasksReviewExpireDays...'',
  `scope_type` tinyint(4) NOT NULL DEFAULT ''0'' COMMENT ''0: all; 1: namespace; 2: community'',
  `scope_id` bigint(20) NOT NULL,
  `review_expired_days` int(11) NOT NULL DEFAULT ''0'' COMMENT ''review_expired_days'',
  `status` tinyint(4) NOT NULL COMMENT ''0: invalid, 1: valid'',
  `create_time` datetime NOT NULL COMMENT ''record create time'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_equipment_inspection_equipment_logs` (
  `id` bigint(20) NOT NULL COMMENT ''id'',
  `owner_type` varchar(32) NOT NULL DEFAULT '''' COMMENT ''the type of who own the log, enterprise, etc'',
  `owner_id` bigint(20) NOT NULL DEFAULT ''0'',
  `target_type` varchar(32) NOT NULL DEFAULT '''' COMMENT ''standard, etc'',
  `target_id` bigint(20) NOT NULL DEFAULT ''0'',
  `process_type` tinyint(4) NOT NULL DEFAULT ''0'' COMMENT ''0: none, 1: insert, 2: update, 3: delete'',
  `operator_uid` bigint(20) NOT NULL DEFAULT ''0'' COMMENT ''record operator user id'',
  `create_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT ''0'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- eh_equipment_inspection_tasks 增加plan_id字段 用于关联task和equipments
ALTER TABLE `eh_equipment_inspection_tasks`
  ADD COLUMN `plan_id`  bigint(20) NOT NULL ;


-- 标准增加周期类型
ALTER TABLE `eh_equipment_inspection_standards`
  ADD COLUMN `repeat_type` tinyint(4) NOT NULL COMMENT '' 0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year'';
-- 操作记录表增加设备id表
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `equipment_id`  bigint(20) NULL DEFAULT 0 ;
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `flow_case_id`  bigint(20) NULL AFTER `equipment_id`;

-- 离线支持  jiarui
ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `update_uid` BIGINT (20) NULL AFTER `status`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP AFTER `update_uid`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `delete_uid` BIGINT (20) NULL AFTER `update_uid`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `delete_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP AFTER `delete_uid`;

-- 离线支持  jiarui


-- 文档管理1.0 add by nan.rong 01/12/2018
-- DROP TABLE eh_file_management_catalogs;
CREATE TABLE `eh_file_management_catalogs` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) ,
  `name` VARCHAR(64) COMMENT ''the name of the catalog'',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT ''0-invalid 1-valid'',
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
  `catalog_id` BIGINT NOT NULL COMMENT ''the id of the file catalog'',
  `source_id` BIGINT NOT NULL COMMENT ''the id of the source'',
  `source_description` VARCHAR(128) COMMENT ''the description of the scope class'',
  `download_permission` TINYINT NOT NULL DEFAULT 0 COMMENT ''0-refuse, 1-allow'',
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
  `catalog_id` BIGINT COMMENT ''the id of the catalog'',
  `content_name` VARCHAR(256) NOT NULL COMMENT ''the name of the content'',
  `size` INT NOT NULL DEFAULT 0 COMMENT ''the size of the content'',
  `parent_id` BIGINT COMMENT ''the parent id of the folder'',
  `content_type` VARCHAR(32) COMMENT ''file, folder'',
  `content_suffix` VARCHAR(64) COMMENT ''the suffix of the file'',
  `content_uri` VARCHAR(2048) COMMENT ''the uri of the content'',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT ''0-invalid, 1-valid'',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- DROP TABLE eh_file_icons;
CREATE TABLE `eh_file_icons` (
  `id` BIGINT,
  `file_type` VARCHAR(64) NOT NULL COMMENT ''the type of the file'',
  `icon_name` VARCHAR(128) COMMENT ''the name of the icon'',
  `icon_uri` VARCHAR(2048) NOT NULL COMMENT ''the uri of the type'',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- end by nan.rong

-- 能耗离线 add by xiongying 01/18/2018
CREATE TABLE `eh_sync_offline_tasks` (
  `id` BIGINT NOT NULL COMMENT ''id of the record'',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '''',
  `owner_id` BIGINT NOT NULL DEFAULT ''0'',
  `type` VARCHAR(64) NOT NULL DEFAULT '''',
  `status` TINYINT NOT NULL,
  `result` LONGTEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- end by xiongying


-- add by sw 20180122 vip车位
CREATE TABLE `eh_parking_spaces` (
  `id` bigint(20) NOT NULL COMMENT ''id of the record'',
  `namespace_id` int(11) NOT NULL DEFAULT ''0'',
  `owner_type` varchar(32) NOT NULL DEFAULT '''' COMMENT ''the type of who own the standard, community, etc'',
  `owner_id` bigint(20) NOT NULL DEFAULT ''0'',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT ''0'' COMMENT ''reference to id of eh_parking_lots'',
  `space_no` varchar(64) NOT NULL DEFAULT '''',
  `space_address` varchar(64) NOT NULL DEFAULT '''',
  `lock_id` varchar(128) DEFAULT NULL,
  `lock_status` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT ''0'' COMMENT ''0: inactive, 1: waitingForApproval, 2: active'',
  `creator_uid` bigint(20) NOT NULL DEFAULT ''0'',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_space_logs` (
  `id` bigint(20) NOT NULL COMMENT ''id of the record'',
  `space_no` varchar(64) NOT NULL,
  `lock_id` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(64) DEFAULT NULL,
  `contact_name` varchar(64) DEFAULT NULL,
  `contact_enterprise_name` varchar(128) DEFAULT NULL,
  `operate_type` tinyint(4) NOT NULL COMMENT ''1: up, 2: down'',
  `user_type` tinyint(4) NOT NULL COMMENT ''1: booking person, 2: plate owner'',
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `menu_type` tinyint(4) DEFAULT 1 COMMENT ''1: 通用 2:公司会议室'',
ADD COLUMN `identify` varchar(64) DEFAULT NULL COMMENT ''类型标识'';

CREATE TABLE `eh_rentalv2_order_rules` (
  `id` bigint(20) NOT NULL DEFAULT ''0'',
  `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'',
  `owner_type` varchar(255) DEFAULT NULL COMMENT ''default_rule, resource_rule'',
  `owner_id` bigint(20) DEFAULT NULL,
  `handle_type` tinyint(4) DEFAULT NULL COMMENT ''1: 退款, 2: 加收'',
  `duration_type` tinyint(4) DEFAULT NULL COMMENT ''1: 时长内, 2: 时长外'',
  `duration_unit` tinyint(4) DEFAULT NULL COMMENT ''时长单位，比如 天，小时'',
  `duration` double DEFAULT NULL COMMENT ''时长'',
  `factor` double DEFAULT NULL COMMENT ''价格系数'',

  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT ''0'',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `source_type` varchar(255) DEFAULT NULL COMMENT ''default_rule, resource_rule'',
ADD COLUMN `source_id` bigint(20) DEFAULT NULL,
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'',
ADD COLUMN `holiday_open_flag` tinyint(4) DEFAULT NULL COMMENT ''节假日是否开放预约: 1-是, 0-否'',
ADD COLUMN `holiday_type` tinyint(4) DEFAULT NULL COMMENT ''1-普通双休, 2-同步中国节假日'',
ADD COLUMN `refund_strategy` tinyint(4) DEFAULT NULL COMMENT ''1-custom, 2-full'',
ADD COLUMN `overtime_strategy` tinyint(4) DEFAULT NULL COMMENT ''1-custom, 2-full'';

ALTER TABLE `eh_rentalv2_default_rules`
drop column pay_start_time,
drop column pay_end_time,
drop column payment_ratio,
drop column contact_num,
drop column overtime_time,
drop column unit,
drop column rental_step,
drop column time_step,
drop column cancel_time,
drop column cancel_flag,
drop column workday_price,
drop column weekend_price,
drop column org_member_workday_price,
drop column org_member_weekend_price,
drop column approving_user_workday_price,
drop column approving_user_weekend_price,
drop column rental_type,
drop column exclusive_flag,
drop column auto_assign,
drop column multi_unit,
drop column resource_counts;

UPDATE eh_rentalv2_default_rules set source_type = ''default_rule'' where source_type IS NULL;
-- 资源表中规则信息迁移到规则表中
SET @id = (SELECT MAX(id) FROM `eh_rentalv2_default_rules`);
INSERT INTO `eh_rentalv2_default_rules` (`id`, `owner_type`, `owner_id`, `resource_type_id`, `rental_start_time`, `rental_end_time`, `refund_flag`, `refund_ratio`, `creator_uid`, `create_time`, `multi_time_interval`, `need_pay`, `begin_date`, `end_date`, `day_open_time`, `day_close_time`, `open_weekday`, `rental_start_time_flag`, `rental_end_time_flag`, `source_type`, `source_id`)
SELECT (@id := @id + 1), ''organization'', organization_id, resource_type_id, rental_start_time, rental_end_time, refund_flag, refund_ratio, creator_uid, create_time, multi_time_interval, need_pay, begin_date, end_date, day_open_time, day_close_time, open_weekday, rental_start_time_flag, rental_end_time_flag, ''resource_rule'', id  from eh_rentalv2_resources;


ALTER TABLE `eh_rentalv2_resources`
drop column unit,
drop column time_step,
drop column cancel_time,
drop column cancel_flag,
drop column exclusive_flag;

-- 资源表中规则信息迁移到规则表中
ALTER TABLE `eh_rentalv2_resources`
drop column rental_start_time_flag,
drop column rental_end_time_flag,
drop column open_weekday,
drop column begin_date,
drop column end_date,
drop column rental_start_time,
drop column rental_end_time,
drop column refund_flag,
drop column refund_ratio,
drop column multi_time_interval,
drop column day_open_time,
drop column day_close_time,
drop column day_begin_time,
drop column day_end_time,
drop column need_pay,
drop column resource_type2;

ALTER TABLE `eh_rentalv2_resources`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';


-- 资源单元格表
ALTER TABLE `eh_rentalv2_cells`
drop column unit,
drop column exclusive_flag,
drop column rental_step,
drop column halfresource_price,
drop column halfresource_original_price,
drop column half_org_member_original_price,
drop column half_org_member_price,
drop column half_approving_user_original_price,
drop column half_approving_user_price;

ALTER TABLE `eh_rentalv2_cells`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';

ALTER TABLE `eh_rentalv2_price_rules`
drop column weekend_price,
drop column org_member_weekend_price,
drop column approving_user_weekend_price;

ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `user_price_type` tinyint(4) DEFAULT NULL COMMENT ''用户价格类型, 1:统一价格 2：用户类型价格'';
ALTER TABLE `eh_rentalv2_price_packages`
ADD COLUMN `user_price_type` tinyint(4) DEFAULT NULL COMMENT ''用户价格类型, 1:统一价格 2：用户类型价格'';


ALTER TABLE eh_rentalv2_orders CHANGE requestor_organization_id user_enterprise_id bigint(20) DEFAULT NULL COMMENT ''申请人公司ID'';

ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'',
ADD COLUMN `custom_object` text,
ADD COLUMN `user_enterprise_name` varchar(64) DEFAULT NULL COMMENT ''申请人公司名称'',
ADD COLUMN `user_phone` varchar(20) DEFAULT NULL COMMENT ''申请人手机'',
ADD COLUMN `user_name` varchar(20) DEFAULT NULL COMMENT ''申请人姓名'',
ADD COLUMN `address_id` bigint(20) DEFAULT NULL COMMENT ''楼栋门牌ID'',
ADD COLUMN `refund_amount` decimal(10,2) DEFAULT NULL,
ADD COLUMN `actual_start_time` datetime DEFAULT NULL COMMENT ''实际使用开始时间'',
ADD COLUMN `actual_end_time` datetime DEFAULT NULL COMMENT ''实际使用结束时间'',
ADD COLUMN `string_tag1` varchar(128) DEFAULT NULL,
ADD COLUMN `string_tag2` varchar(128) DEFAULT NULL,
ADD COLUMN `scene` varchar(64) DEFAULT NULL COMMENT ''下单时场景信息，用来计算价格'',
ADD COLUMN `refund_strategy` tinyint(4) DEFAULT NULL COMMENT ''1-custom, 2-full'',
ADD COLUMN `overtime_strategy` tinyint(4) DEFAULT NULL COMMENT ''1-custom, 2-full'',
ADD COLUMN `old_end_time` datetime DEFAULT NULL COMMENT ''延长订单时，存老的使用结束时间'',
ADD COLUMN `old_custom_object` text;


ALTER TABLE `eh_rentalv2_orders`
drop column reserve_money,
drop column pay_start_time,
drop column pay_end_time;

ALTER TABLE `eh_rentalv2_resource_orders`
drop column rental_step,
drop column exclusive_flag;

UPDATE eh_rentalv2_default_rules set resource_type = ''default'';
UPDATE eh_rentalv2_resources set resource_type = ''default'';
UPDATE eh_rentalv2_cells set resource_type = ''default'';
UPDATE eh_rentalv2_orders set resource_type = ''default'';

ALTER TABLE `eh_rentalv2_close_dates`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';
ALTER TABLE `eh_rentalv2_config_attachments`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';

ALTER TABLE `eh_rentalv2_items`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';
ALTER TABLE `eh_rentalv2_items_orders`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';
ALTER TABLE `eh_rentalv2_refund_orders`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';

ALTER TABLE `eh_rentalv2_order_attachments`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';

ALTER TABLE `eh_rentalv2_price_packages`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';
ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';
ALTER TABLE `eh_rentalv2_time_interval`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';
ALTER TABLE `eh_rentalv2_resource_orders`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';

ALTER TABLE `eh_rentalv2_resource_numbers`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';
ALTER TABLE `eh_rentalv2_resource_pics`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';
ALTER TABLE `eh_rentalv2_resource_ranges`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT ''资源类型'';

UPDATE eh_rentalv2_close_dates set resource_type = ''default'';
UPDATE eh_rentalv2_config_attachments set resource_type = ''default'';
UPDATE eh_rentalv2_items set resource_type = ''default'';
UPDATE eh_rentalv2_items_orders set resource_type = ''default'';
UPDATE eh_rentalv2_resource_orders set resource_type = ''default'';
UPDATE eh_rentalv2_order_attachments set resource_type = ''default'';
UPDATE eh_rentalv2_price_packages set resource_type = ''default'';
UPDATE eh_rentalv2_price_rules set resource_type = ''default'';
UPDATE eh_rentalv2_time_interval set resource_type = ''default'';
UPDATE eh_rentalv2_resource_orders set resource_type = ''default'';

UPDATE eh_rentalv2_resource_numbers set resource_type = ''default'';
UPDATE eh_rentalv2_resource_ranges set resource_type = ''default'';
UPDATE eh_rentalv2_resource_pics set resource_type = ''default'';











