
CREATE TABLE `eh_parking_spaces` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `space_no` varchar(64) NOT NULL DEFAULT '',
  `space_address` varchar(64) NOT NULL DEFAULT '',
  `lock_id` varchar(128) DEFAULT NULL,
  `lock_status` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_space_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `space_no` varchar(64) NOT NULL,
  `lock_id` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(64) DEFAULT NULL,
  `contact_name` varchar(64) DEFAULT NULL,
  `contact_enterprise_name` varchar(128) DEFAULT NULL,
  `operate_type` tinyint(4) NOT NULL COMMENT '1: up, 2: down',
  `user_type` tinyint(4) NOT NULL COMMENT '1: booking person, 2: plate owner',
  `operate_uid` bigint(20) NOT NULL DEFAULT '0',
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `menu_type` tinyint(4) DEFAULT 1 COMMENT '1: 通用 2:公司会议室',
ADD COLUMN `identify` varchar(64) DEFAULT NULL COMMENT '类型标识';





CREATE TABLE `eh_rentalv2_order_rules` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'default_rule, resource_rule',
  `owner_id` bigint(20) DEFAULT NULL,
  `handle_type` tinyint(4) DEFAULT NULL COMMENT '1: 退款, 2: 加收',
  `duration_type` tinyint(4) DEFAULT NULL COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` varchar(20) DEFAULT NULL COMMENT '时长单位，比如 天，小时',
  `duration` double DEFAULT NULL COMMENT '时长',
  `factor` double DEFAULT NULL COMMENT '价格系数',

  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `source_type` varchar(255) DEFAULT NULL COMMENT 'default_rule, resource_rule',
ADD COLUMN `source_id` bigint(20) DEFAULT NULL,
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型',
ADD COLUMN `holiday_open_flag` tinyint(4) DEFAULT NULL COMMENT '节假日是否开放预约: 1-是, 0-否',
ADD COLUMN `holiday_type` tinyint(4) DEFAULT NULL COMMENT '1-普通双休, 2-同步中国节假日',
ADD COLUMN `refund_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full',
ADD COLUMN `overtime_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full';

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
drop column exclusive_flag;

UPDATE eh_rentalv2_default_rules set source_type = 'default_rule' where source_type IS NULL;
-- 资源表中规则信息迁移到规则表中
SET @id = (SELECT MAX(id) FROM `eh_rentalv2_default_rules`);
INSERT INTO `eh_rentalv2_default_rules` (`id`, `owner_type`, `owner_id`, `resource_type_id`, `rental_start_time`, `rental_end_time`, `refund_flag`, `refund_ratio`, `creator_uid`, `create_time`, `auto_assign`, `multi_unit`, `multi_time_interval`, `need_pay`, `resource_counts`, `begin_date`, `end_date`, `day_open_time`, `day_close_time`, `open_weekday`, `rental_start_time_flag`, `rental_end_time_flag`, `source_type`, `source_id`)
SELECT (@id := @id + 1), 'organization', organization_id, resource_type_id, rental_start_time, rental_end_time, refund_flag, refund_ratio, creator_uid, create_time, auto_assign, multi_unit, multi_time_interval, need_pay, resource_counts, begin_date, end_date, day_open_time, day_close_time, open_weekday, rental_start_time_flag, rental_end_time_flag, 'resource_rule', id  from eh_rentalv2_resources;


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
drop column resource_counts,
drop column open_weekday,
drop column begin_date,
drop column end_date,
drop column rental_start_time,
drop column rental_end_time,
drop column refund_flag,
drop column refund_ratio,
drop column auto_assign,
drop column multi_unit,
drop column multi_time_interval,
drop column day_open_time,
drop column day_close_time,
drop column day_begin_time,
drop column day_end_time,
drop column need_pay;

ALTER TABLE `eh_rentalv2_resources`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';


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


ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';
































