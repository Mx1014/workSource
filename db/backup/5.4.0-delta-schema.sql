-- Designer: zhiwei zhang
-- Description: ISSUE#24392 固定资产管理V1.0（支持对内部各类固定资产进行日常维护）

CREATE TABLE `eh_fixed_asset_default_categories` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `parent_id` INTEGER NOT NULL DEFAULT 0 COMMENT '父级分类id',
  `path` VARCHAR(128) NOT NULL COMMENT '分类层级路径，如 /123/1234',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序字段',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '固定资产默认分类表';


CREATE TABLE `eh_fixed_asset_categories` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations ',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `parent_id` INTEGER NOT NULL DEFAULT 0 COMMENT '父级分类id',
  `path` VARCHAR(128) NOT NULL COMMENT '分类层级路径，如 /123/1234',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0 : IN_ACTIVE 1: ACTIVE',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序字段',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`),
  KEY `i_eh_namespace_owner_id`(`namespace_id`,`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '固定资产分类表';


CREATE TABLE `eh_fixed_assets` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `item_no` VARCHAR(20) NOT NULL COMMENT '资产编号',
  `name` VARCHAR(64) NOT NULL COMMENT '资产名称',
  `fixed_asset_category_id` INTEGER NOT NULL COMMENT '资产分类  id of the table eh_fixed_asset_categories',
  `specification` VARCHAR(128) COMMENT '规格',
  `price` DECIMAL(14,2) COMMENT '单价',
  `buy_date` DATE COMMENT '购买日期 格式:yyyy-MM-dd',
  `vendor` VARCHAR(128) COMMENT '所属供应商',
  `add_from` TINYINT NOT NULL DEFAULT 0 COMMENT '来源 :0-其它,1-购入,2-自建,3-租赁,4-捐赠',
  `image_uri` VARCHAR(1024) COMMENT '图片uri',
  `barcode_uri` VARCHAR(1024) COMMENT '条形码uri',
  `other_info` VARCHAR(512) COMMENT '其它',
  `remark` VARCHAR(512) COMMENT '备注',
  `status` TINYINT NOT NULL COMMENT '状态:1-闲置,2-使用中,3-维修中,4-已出售,5-已报废,6-遗失',
  `location` VARCHAR(256) COMMENT '存放地点',
  `occupied_date` DATE COMMENT '领用时间',
  `occupied_department_id` BIGINT COMMENT '领用部门ID',
  `occupied_member_detail_id` BIGINT COMMENT '领用人 id of the table eh_organization_member_details',
  `occupied_member_name` VARCHAR(64) COMMENT '领用人姓名',
  `operator_name` VARCHAR(64) NOT NULL COMMENT '操作人姓名',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT '删除操作人userId',
  `delete_time` DATETIME COMMENT '记录删除时间',
  PRIMARY KEY(`id`),
  KEY `i_eh_namespace_owner_id`(`namespace_id`,`owner_type`,`owner_id`),
  KEY `i_eh_fixed_asset_category_id`(`fixed_asset_category_id`),
  KEY `i_eh_create_time`(`create_time` DESC)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '固定资产表';


CREATE TABLE `eh_fixed_asset_operation_logs` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `fixed_asset_id` BIGINT NOT NULL COMMENT '资产ID',
  `operation_info` TEXT NOT NULL COMMENT '变更记录JSON格式记录',
  `operation_type` VARCHAR(16) NOT NULL COMMENT '新增、编辑或者删除',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_name` VARCHAR(45) NOT NULL COMMENT '操作人姓名 ',
  PRIMARY KEY(`id`),
  KEY `i_eh_namespace_asset_id`(`namespace_id`,`fixed_asset_id`),
  KEY `i_eh_create_time`(`create_time` DESC)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '固定资产信息变更记录表';


-- End by: zhiwei zhang


-- 工位预定2.3 dengs

ALTER TABLE eh_office_cubicle_categories ADD COLUMN  `unit_price`  DECIMAL(10,2);
ALTER TABLE eh_office_cubicle_orders ADD COLUMN  `employee_number`  INTEGER;
ALTER TABLE eh_office_cubicle_orders ADD COLUMN  `financing_flag`  TINYINT;

-- 工位城市表 , add by dengs, 20180403
-- DROP TABLE IF EXISTS `eh_office_cubicle_cities`;
CREATE TABLE `eh_office_cubicle_cities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `province_id` bigint(20) DEFAULT NULL COMMENT '省份id',
  `province_name` varchar(100) DEFAULT NULL COMMENT '省份名称',
  `city_id` bigint(20) DEFAULT NULL COMMENT '城市id',
  `city_name` varchar(128) DEFAULT NULL COMMENT '城市名称',
	`icon_uri` VARCHAR(1024) COMMENT '城市图片uri',
  `default_order` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 用户选中城市表 , add by dengs, 20180403
-- DROP TABLE IF EXISTS `eh_office_cubicle_selected_cities`;
CREATE TABLE `eh_office_cubicle_selected_cities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `province_name` varchar(100) DEFAULT NULL COMMENT '省份名称',
  `city_name` varchar(128) DEFAULT NULL COMMENT '城市名称',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- end

-- by zheng 招租增加园区id 和 客服id
ALTER TABLE `eh_lease_promotions`
ADD COLUMN `community_id` BIGINT(20) NOT NULL DEFAULT '0' AFTER `namespace_id`;
ALTER TABLE `eh_lease_promotions`
ADD COLUMN `contact_uid` BIGINT(20) NULL DEFAULT NULL AFTER `contact_phone`;
ALTER TABLE `eh_lease_projects`
ADD COLUMN `contact_uid` BIGINT(20) NULL DEFAULT NULL AFTER `contact_phone`;
ALTER TABLE `eh_lease_buildings`
ADD COLUMN `manager_uid` BIGINT(20) NULL DEFAULT NULL AFTER `manager_contact`;

-- 人事2.6新需求 by ryan 03/21/2018
ALTER TABLE `eh_organization_member_details` DROP COLUMN `profile_integrity`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `department`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `department_ids`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position_ids`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level_ids`;

ALTER TABLE `eh_organization_member_details` ADD COLUMN `check_in_time_index` VARCHAR(64) NOT NULL DEFAULT '0000' COMMENT'only month&day like 0304' AFTER `check_in_time`;
ALTER TABLE `eh_organization_member_details` ADD COLUMN `birthday_index` VARCHAR(64) COMMENT'only month like 0304' AFTER `birthday`;
UPDATE eh_organization_member_details AS t SET check_in_time_index = (CONCAT(SUBSTRING(t.check_in_time,6,2),SUBSTRING(t.check_in_time,9,2)));
UPDATE eh_organization_member_details AS t SET birthday_index = (CONCAT(SUBSTRING(t.birthday_index,6,2),SUBSTRING(t.birthday_index,9,2)));

ALTER TABLE `eh_archives_notifications` DROP COLUMN `notify_emails`;
ALTER TABLE `eh_archives_notifications` CHANGE COLUMN `notify_hour` `notify_time` INTEGER COMMENT 'the hour of sending notifications';
ALTER TABLE `eh_archives_notifications` ADD COLUMN `mail_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'email sending, 0-no 1-yes' AFTER `notify_time`;
ALTER TABLE `eh_archives_notifications` ADD COLUMN `message_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'message sending, 0-no 1-yes' AFTER `mail_flag`;
ALTER TABLE `eh_archives_notifications` ADD COLUMN `notify_target` TEXT COMMENT 'the target email address' AFTER `message_flag`;
-- end by ryan


-- Designer: zhiwei zhang
-- Description: ISSUE#26208 日程1.0

CREATE TABLE `eh_remind_settings` (
  `id` INT NOT NULL COMMENT '主键' ,
  `name` VARCHAR(64) NOT NULL COMMENT '名称，如 提前一天（09:00）' ,
  `offset_day` TINYINT NOT NULL DEFAULT 0 COMMENT '提前几天' ,
  `fix_time` TIME NULL COMMENT '提醒的固定时间，格式:09:00:00' ,
  `default_order` INT NOT NULL DEFAULT 0 COMMENT '排序字段' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  `operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
  `operate_time` DATETIME NULL COMMENT '记录更新时间' ,
  PRIMARY KEY (`id`) ,
  UNIQUE KEY `u_eh_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程提醒时间设置表';


CREATE TABLE `eh_remind_categories` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间ID' ,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations' ,
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID' ,
  `user_id` BIGINT NOT NULL COMMENT '分类拥有人的用户ID' ,
  `name` VARCHAR(64) NOT NULL COMMENT '日程分类的名称' ,
  `colour` VARCHAR(16) NULL COMMENT '日程分类的颜色RGB的argb-hex值，如 #FFF58F3E' ,
  `share_short_display` VARCHAR(64) NULL COMMENT '默认共享人概要信息：如 xx等3人' ,
  `default_order` INT NOT NULL DEFAULT 0 COMMENT '排序字段' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  `operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
  `operate_time` DATETIME NULL COMMENT '记录更新时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_owner_user_id` (`namespace_id`, `owner_type`, `owner_id`, `user_id`) ,
  UNIQUE KEY `u_eh_name` (`namespace_id`, `owner_type`, `owner_id`, `user_id`, `name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程分类表';

CREATE TABLE `eh_remind_category_default_shares` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `remind_category_id` BIGINT NOT NULL COMMENT '日程分类的ID,id of eh_remind_categories' ,
  `shared_source_type` VARCHAR(32) NOT NULL COMMENT '默认MEMBER_DETAIL' ,
  `shared_source_id` BIGINT NOT NULL COMMENT 'source_type对应的ID，员工档案ID' ,
  `shared_contract_name` VARCHAR(45) NOT NULL COMMENT '默认共享人的姓名' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_remind_category_id` (`remind_category_id`)
  ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程分类默认共享人设置表';


CREATE TABLE `eh_reminds` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间' ,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations' ,
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID' ,
  `user_id` BIGINT NOT NULL COMMENT '日程所有人的用户ID' ,
  `contact_name` VARCHAR(64) NOT NULL COMMENT '日程所有人的姓名' ,
  `plan_description` VARCHAR(512) NOT NULL COMMENT '日程描述' ,
  `plan_date` DATETIME NULL COMMENT '日程的计划日期' ,
  `expect_day_of_month` TINYINT NULL COMMENT '最初的重复日程计划的日期，DAY值' ,
  `remind_summary` VARCHAR(64) NULL COMMENT '提醒的文本概要',
  `repeat_type` TINYINT NOT NULL DEFAULT 0 COMMENT '重复类型：0-无，1-每日，2-每周，3-每月，4-每年' ,
  `remind_type_id` INT COMMENT '提醒类型ID',
  `remind_type` VARCHAR(32) NULL COMMENT '提醒类型的名称，如  提前一天（09:00）' ,
  `remind_time` DATETIME NULL COMMENT '提醒时间，根据选择的提醒类型计算得到' ,
  `act_remind_time` DATETIME NULL COMMENT '实际提醒时间，即实际出发提醒的时间' ,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT ' 1: UNDO 未完成 , 2 : DONE 已完成  ' ,
  `remind_category_id` BIGINT NOT NULL COMMENT '日程分类ID' ,
  `track_remind_id` BIGINT NULL COMMENT '关注的日程ID' ,
  `track_remind_user_id` BIGINT NULL COMMENT '关注的日程的所有人的uid' ,
  `track_contract_name` VARCHAR(45) NULL COMMENT '关注的日程所有人的姓名' ,
  `share_short_display` VARCHAR(64) NULL COMMENT '共享人概要信息：如 xx等3人' ,
  `share_count` INT NOT NULL DEFAULT 0 COMMENT '共享人数量',
  `default_order` INT NOT NULL DEFAULT 0 COMMENT '排序字段' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  `operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
  `operate_time` DATETIME NULL COMMENT '记录更新时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_owner_user_id` (`namespace_id`, `owner_type`, `owner_id`, `user_id`) ,
  KEY `i_eh_remind_time` (`remind_time`),
  KEY `i_eh_track_remind_id` (`track_remind_id`),
  KEY `i_eh_remind_category_id` (`remind_category_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程表';



CREATE TABLE `eh_remind_shares` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间' ,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations' ,
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID' ,
  `remind_id` BIGINT NOT NULL COMMENT '日程ID' ,
  `owner_user_id` BIGINT NOT NULL COMMENT '分享人的userId' ,
  `owner_contract_name` VARCHAR(64) NOT NULL COMMENT '分享人的姓名' ,
  `shared_source_type` VARCHAR(32) NOT NULL COMMENT '默认MEMBER_DETAIL，被分享人ID类型' ,
  `shared_source_id` BIGINT NOT NULL COMMENT 'source_type对应的ID，被分享人员工档案ID' ,
  `shared_source_name` VARCHAR(128) NOT NULL COMMENT '被分享人员工姓名' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_shared_source_id` (`namespace_id`, `owner_type`, `owner_id`, `shared_source_type`,`shared_source_id`) ,
  KEY `i_eh_remind_id` (`remind_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程共享人记录表';


 CREATE TABLE `eh_remind_demo_create_logs` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间' ,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations' ,
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID' ,
  `user_id` BIGINT NOT NULL COMMENT '日程所有人的用户ID' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_owner_user_id` (`namespace_id`, `owner_type`, `owner_id`, `user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程案例创建记录，避免案例重复创建';


-- End by: zhiwei zhang

--
-- 启动广告   add by xq.tian  2018/04/18
--
ALTER TABLE eh_launch_advertisements ADD COLUMN resource_name VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'Resource name';

ALTER TABLE eh_apps DROP INDEX u_eh_app_reg_name;

-- end xq.tian



-- Designer: wuhan
-- Description: 考勤4.0
-- Created: 2018-4-12


CREATE TABLE `eh_punch_vacation_balances` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT COMMENT 'organization_id',
  `owner_type` VARCHAR(32) DEFAULT '' COMMENT 'organization',
  `user_id` BIGINT COMMENT 'user_id',
  `detail_id` BIGINT COMMENT 'user_id',
  `annual_leave_balance` DOUBLE COMMENT '年假余额',
  `overtime_compensation_balance` DOUBLE COMMENT '调休余额',
  `creator_uid` BIGINT DEFAULT '0',
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `update_time` DATETIME ,
  `namespace_id` INT(11) ,
  PRIMARY KEY (`id`),
  KEY `ix_detail_id`(`detail_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '假期余额表';


CREATE TABLE `eh_punch_vacation_balance_logs` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT COMMENT 'organization_id',
  `owner_type` VARCHAR(32) DEFAULT '' COMMENT 'organization',
  `user_id` BIGINT COMMENT 'user_id',
  `detail_id` BIGINT COMMENT 'user_id',
  `annual_leave_balance_correction` DOUBLE COMMENT'年假余额修改',
  `overtime_compensation_balance_correction` DOUBLE COMMENT '调休余额修改',
  `annual_leave_balance` DOUBLE COMMENT '修改后年假余额',
  `overtime_compensation_balance` DOUBLE COMMENT '修改后调休余额',
  `description` TEXT COMMENT '备注',
  `creator_uid` BIGINT DEFAULT '0',
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `update_time` DATETIME ,
  `namespace_id` INT(11) ,
  PRIMARY KEY (`id`),
  KEY `ix_detail_id`(`detail_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '假期余额操作日志表';

ALTER TABLE eh_punch_statistics ADD COLUMN `annual_leave_balance` DOUBLE COMMENT '年假余额';
ALTER TABLE eh_punch_statistics ADD COLUMN `overtime_compensation_balance` DOUBLE COMMENT '调休余额';
ALTER TABLE eh_punch_statistics ADD COLUMN `device_change_counts` INT COMMENT '设备异常次数';
ALTER TABLE eh_punch_statistics ADD COLUMN `exception_request_counts` INT COMMENT '异常申报次数';
ALTER TABLE eh_punch_statistics ADD COLUMN `belate_time` BIGINT COMMENT '迟到时长(毫秒数)';
ALTER TABLE eh_punch_statistics ADD COLUMN `leave_early_time` BIGINT COMMENT '早退时长(毫秒数)';
ALTER TABLE eh_punch_statistics ADD COLUMN `forgot_count` INT COMMENT '下班缺卡次数';
ALTER TABLE eh_punch_statistics ADD COLUMN `status_list` VARCHAR(1024) COMMENT '校正后状态列表(月初到月末)';
ALTER TABLE eh_punch_logs ADD COLUMN `wifi_info` VARCHAR(1024) COMMENT '打卡用到的WiFi信息';
ALTER TABLE eh_punch_logs ADD COLUMN `location_info` VARCHAR(1024) COMMENT '打卡用到的地址定位';

-- end 考勤4.0

-- Designer: zhiwei zhang
-- Description: ISSUE#25301 【打卡考勤】按日导出打卡记录导出文件时间过长
-- Created: 2018-4-4


ALTER TABLE eh_punch_exception_requests ADD INDEX i_eh_enterprise_user_punch_date(`enterprise_id`,`user_id`,`punch_date`);
ALTER TABLE eh_punch_day_logs ADD INDEX i_eh_enterprise_user_punch_date(`enterprise_id`,`user_id`,`punch_date`);

-- End by: zhiwei zhang

-- 优化客户和合同的流程 by wentian
ALTER TABLE eh_sync_data_tasks ADD COLUMN `view_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否被查看';
ALTER TABLE eh_customer_talents ADD COLUMN `member_id` BIGINT NOT NULL DEFAULT 0 COMMENT '通讯录表中的id';

-- end by: wentian


-- 免租日期
ALTER TABLE `eh_contract_charging_changes` ADD COLUMN `change_duration_days` INTEGER DEFAULT NULL COMMENT '变化的天数，例如免租了xx天';