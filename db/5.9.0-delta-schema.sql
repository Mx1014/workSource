-- AUTHOR: 梁燕龙
-- REMARK: 用户表增加企业ID
ALTER TABLE `eh_users` ADD COLUMN `company_id` BIGINT COMMENT '公司ID';
-- END

 
-- AUTHOR: 吴寒
-- REMARK: 公告1.8 修改表结构
ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是';
ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_time` DATETIME;
-- REMARK: 公告1.8 修改表结构
-- END

-- AUTHOR: 张智伟 20180822
-- REMARK: issue-36367 考勤规则新增打卡提醒设置
ALTER TABLE eh_punch_rules ADD COLUMN punch_remind_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启上下班打卡提醒：1 开启 0 关闭' AFTER china_holiday_flag;
ALTER TABLE eh_punch_rules ADD COLUMN remind_minutes_on_duty INT NOT NULL DEFAULT 0 COMMENT '上班提前分钟数打卡提醒' AFTER punch_remind_flag;

-- AUTHOR: 张智伟 20180822
-- REMARK: issue-36367 考勤规则新增打卡提醒设置,该表保存生成的提醒记录
CREATE TABLE `eh_punch_notifications` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `enterprise_id` BIGINT NOT NULL COMMENT '总公司id',
  `user_id` BIGINT NOT NULL COMMENT '被提醒人的uid',
  `detail_id` BIGINT NOT NULL COMMENT '被提醒人的detailId',
  `punch_rule_id` BIGINT NOT NULL COMMENT '所属考勤规则',
  `punch_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0- 上班打卡 ; 1- 下班打卡',
  `punch_interval_no` INT(11) DEFAULT '1' COMMENT '第几次排班的打卡',
  `punch_date` DATE NOT NULL COMMENT '打卡日期',
  `rule_time` DATETIME NOT NULL COMMENT '规则设置的该次打卡时间',
  `except_remind_time` DATETIME NOT NULL COMMENT '规则设置的打卡提醒时间',
  `act_remind_time` DATETIME NULL COMMENT '实际提醒时间',
  `invalid_reason` VARCHAR(512) COMMENT '提醒记录失效的原因',
  `invalid_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0- 有效 ; 1- 无效',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY i_eh_enterprise_detail_id(`namespace_id`,`enterprise_id`,`detail_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='打卡提醒队列，该数据只保留一天';

-- AUTHOR: 张智伟 20180822
-- REMARK: issue-36367 打卡记录报表排序
ALTER TABLE eh_punch_logs ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;
ALTER TABLE eh_punch_log_files ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;


-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.6（对接统一账单） 业务应用与缴费的关联关系表
-- REMARK: 1、contract_category_id字段改名为source_id
ALTER TABLE eh_asset_module_app_mappings CHANGE `contract_category_id` `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）';
-- REMARK: 2、增加相关字段
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）' after `source_id`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `config` VARCHAR(1024) COMMENT '各个业务系统自定义的JSON配置' after `source_type`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `owner_id` BIGINT COMMENT '园区ID' after `config`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `owner_type` VARCHAR(64) COMMENT '园区类型' after `owner_id`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `bill_group_id` BIGINT COMMENT '账单组ID';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `charging_item_id` BIGINT COMMENT '费项ID';
-- REMARK: 3、删除无效字段
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `energy_category_id`;
-- REMARK: 4、去掉原来的限制索引
ALTER TABLE eh_asset_module_app_mappings DROP INDEX u_asset_category_id;
ALTER TABLE eh_asset_module_app_mappings DROP INDEX u_contract_category_id;

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.6（对接统一账单） 账单要增加来源字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `source_name` VARCHAR(1024) COMMENT '账单来源（如：停车缴费，缴费的新增/导入等）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `consume_user_id` BIGINT COMMENT '企业下面的某个人的ID';
-- REMARK: 物业缴费V6.6（对接统一账单） 账单费项要增加来源字段
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `source_name` VARCHAR(1024) COMMENT '账单来源（如：停车缴费，缴费的新增/导入等）';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `consume_user_id` BIGINT COMMENT '企业下面的某个人的ID';



-- AUTHOR: 吴寒
-- REMARK: issue-33887: 增加操作人姓名到目录/文件表
ALTER TABLE `eh_file_management_contents` ADD COLUMN `operator_name`  VARCHAR(256) ;
ALTER TABLE `eh_file_management_catalogs` ADD COLUMN `operator_name`  VARCHAR(256) ;
-- REMARK: issue-33887: 给文件表增加索引
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_catalog_id` (`catalog_id`);
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_parent_id` (`parent_id`);
-- REMARK: issue-33887
-- END



 
-- AUTHOR: 吴寒
-- REMARK: issue-33943 日程提醒1.2
ALTER TABLE eh_remind_settings ADD COLUMN app_version VARCHAR(32) DEFAULT '5.8.0' COMMENT '对应app版本(历史数据5.8.0),根据APP版本选择性展示';
ALTER TABLE eh_remind_settings ADD COLUMN before_time BIGINT COMMENT '提前多少时间(毫秒数)不超过1天的部分在这里减';
-- END issue-33943


 
-- AUTHOR: 吴寒
-- REMARK: 会议管理V1.2
ALTER TABLE `eh_meeting_reservations`  CHANGE `content` `content` TEXT COMMENT '会议详细内容';
ALTER TABLE `eh_meeting_reservations`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';
ALTER TABLE `eh_meeting_records`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';

-- 增加附件表 会议预定和会议纪要共用
CREATE TABLE `eh_meeting_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'owner type EhMeetingRecords/EhMeetingReservations',
  `owner_id` BIGINT NOT NULL COMMENT 'key of the owner',
  `content_name` VARCHAR(1024) COMMENT 'attachment object content name like: abc.jpg',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `content_size` INT(11)  COMMENT 'attachment object size',
  `content_icon_uri` VARCHAR(1024) COMMENT 'attachment object link of content icon',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- END 会议管理V1.2

-- AUTHOR: 荣楠
-- REMARK: issue-34029 工作汇报1.2
ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `organization_id` BIGINT DEFAULT 0 NOT NULL COMMENT 'the orgId for the user' AFTER `namespace_id`;
ALTER TABLE `eh_work_report_val_receiver_map` ADD INDEX `i_work_report_receiver_id` (`receiver_user_id`) ;

ALTER TABLE `eh_work_reports` ADD COLUMN `validity_setting` VARCHAR(512) COMMENT 'the expiry date of the work report' AFTER `form_version`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the receiver message settings' AFTER `validity_setting`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_seeting` VARCHAR(512) COMMENT 'the time range of the receiver message' AFTER `receiver_msg_type`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the author message settings' AFTER `receiver_msg_seeting`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_seeting` VARCHAR(512) COMMENT 'the time range of the author message' AFTER `author_msg_type`;

ALTER TABLE `eh_work_report_vals` ADD COLUMN `receiver_avatar` VARCHAR(1024) COMMENT 'the avatar of the fisrt receiver' AFTER `report_type`;
ALTER TABLE `eh_work_report_vals` ADD COLUMN `applier_avatar` VARCHAR(1024) COMMENT 'the avatar of the author' AFTER `receiver_avatar`;

ALTER TABLE `eh_work_report_vals` MODIFY COLUMN `report_time` DATE COMMENT 'the target time of the report';


CREATE TABLE `eh_work_report_val_receiver_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_val_id` BIGINT NOT NULL COMMENT 'id of the report val',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` DATE NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `receiver_user_id` BIGINT NOT NULL COMMENT 'the id of the receiver',
  `create_time` DATETIME COMMENT 'record create time',

  KEY `i_eh_work_report_val_receiver_msg_report_id`(`report_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_val_id`(`report_val_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_time`(`report_time`),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_work_report_scope_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` DATE NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `end_time` DATETIME COMMENT 'the deadline of the report',
  `scope_ids` TEXT COMMENT 'the id list of the receiver',
  `create_time` DATETIME COMMENT 'record create time',

  KEY `i_eh_work_report_scope_msg_report_id`(`report_id`),
  KEY `i_eh_work_report_scope_msg_report_time`(`report_time`),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- END issue-34029



