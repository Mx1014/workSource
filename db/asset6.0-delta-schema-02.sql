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
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_name` (`content_name`);
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_catalog_id` (`catalog_id`);
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_parent_id` (`parent_id`);
-- REMARK: issue-33887
-- END













