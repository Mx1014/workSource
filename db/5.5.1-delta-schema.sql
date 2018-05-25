-- Designer: wuhan
-- Description: ISSUE#25515: 薪酬V2.2（工资条发放管理；app支持工资条查看/确认）

CREATE TABLE `eh_salary_payslips` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INT ,
  `owner_type` VARCHAR(32)  COMMENT 'organization',
  `owner_id` BIGINT  COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT  COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(12) COMMENT 'example:201705',
  `name` VARCHAR(1024) NOT NULL COMMENT '工资表名称',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`),
  KEY `i_eh_owner_period` (`owner_id`,`salary_period`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '工资条表';


CREATE TABLE `eh_salary_payslip_details` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `payslip_id` BIGINT NOT NULL COMMENT '父键;工资条id',
  `namespace_id` INT,
  `salary_period` VARCHAR(12) COMMENT 'example:201705',
  `owner_type` VARCHAR(32)  COMMENT 'organization',
  `owner_id` BIGINT  COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT  COMMENT '属于哪一个总公司的',
  `user_id` BIGINT,
  `user_detail_id` BIGINT,
  `name` VARCHAR(512) NOT NULL COMMENT '姓名',
  `user_contact` VARCHAR(20) NOT NULL COMMENT '手机号',
  `payslip_content` TEXT  COMMENT '导入的工资条数据(key-value对的json字符串)',
  `viewed_flag` TINYINT COMMENT '已查看0-否 1-是',
  `status` TINYINT COMMENT '状态0-已发送 1-已撤回  2-已确认',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`),
  KEY `i_eh_payslip_id` (`payslip_id`),
  KEY `i_eh_organization_user` (`user_id`,`organization_id`),
  KEY `i_eh_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '工资条详情表';
-- 薪酬2.2 end

-- app配置 1.2.1 start  add by yanjun 201805241019
ALTER TABLE `eh_portal_versions` ADD COLUMN `preview_count`  int(11) NULL DEFAULT 0 COMMENT '预览版本发布次数';

-- app配置 1.2.1 end  add by yanjun


-- 任务中心添加执行开始时间和上传开始时间  add by yanjun 201805241345
ALTER TABLE `eh_tasks` ADD COLUMN `execute_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_finish_time`  datetime NULL;



-- 资源预约 订单资源表增加字段
ALTER TABLE `eh_rentalv2_resource_orders` ADD COLUMN `resource_number`  VARCHAR(64) NULL;


-- ------------------------------
-- 工作流动态函数     add by xq.tian  2018/04/24
-- ------------------------------
DROP TABLE IF EXISTS `eh_flow_scripts`; -- 原来存在这张表，没有数据，删掉重新建
CREATE TABLE `eh_flow_scripts` (
	`id` BIGINT NOT NULL,
	`namespace_id` INTEGER NOT NULL DEFAULT '0',

	`module_type` VARCHAR(64) NOT NULL,
	`module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',

	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`script_category` VARCHAR(64) NOT NULL COMMENT 'system_script, user_script',
	`script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',

	`script_main_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'ref eh_flow_scripts',
	`script_version` INTEGER NOT NULL DEFAULT '0' COMMENT 'script version',

	`name` VARCHAR(128) DEFAULT NULL COMMENT 'script name',
	`description` TEXT DEFAULT NULL COMMENT 'script description',
	`script` LONGTEXT DEFAULT NULL COMMENT 'script content',

	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,

	`string_tag1` VARCHAR(128) DEFAULT NULL,
	`string_tag2` VARCHAR(128) DEFAULT NULL,
	`string_tag3` VARCHAR(128) DEFAULT NULL,
	`string_tag4` VARCHAR(128) DEFAULT NULL,
	`string_tag5` VARCHAR(128) DEFAULT NULL,
	`integral_tag1` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag2` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag3` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag4` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag5` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'flow scripts in dev mode';

-- ------------------------------
-- 工作流动态函数配置表     add by xq.tian  2018/04/24
-- ------------------------------
-- DROP TABLE IF EXISTS `eh_flow_script_configs`;
CREATE TABLE `eh_flow_script_configs` (
	`id` BIGINT NOT NULL,
	`namespace_id` INTEGER NOT NULL DEFAULT '0',

	`module_type` VARCHAR(64) NOT NULL,
	`module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',

	`flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',
	`flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'flow version',

	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',

	`script_name` VARCHAR(128) NULL DEFAULT NULL COMMENT 'export script name, only for script type of java',
	`script_main_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'ref eh_flow_scripts',
	`script_version` INTEGER NOT NULL DEFAULT '0' COMMENT 'script version',

	`field_name` VARCHAR(1024) DEFAULT NULL COMMENT 'field name',
	`field_desc` TEXT DEFAULT NULL COMMENT 'field description',
	`field_value` VARCHAR(1024) DEFAULT NULL COMMENT 'field value',

	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
	`create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,

	`string_tag1` VARCHAR(128) DEFAULT NULL,
	`string_tag2` VARCHAR(128) DEFAULT NULL,
	`string_tag3` VARCHAR(128) DEFAULT NULL,
	`string_tag4` VARCHAR(128) DEFAULT NULL,
	`string_tag5` VARCHAR(128) DEFAULT NULL,
	`integral_tag1` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag2` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag3` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag4` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag5` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'flow scripts config in dev mode';

ALTER TABLE eh_flow_evaluate_items ADD COLUMN flow_case_id BIGINT;

ALTER TABLE eh_flow_actions ADD COLUMN script_type VARCHAR(64);
ALTER TABLE eh_flow_actions ADD COLUMN script_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_flow_actions ADD COLUMN script_version INTEGER NOT NULL DEFAULT 0;

ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag6` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag7` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag8` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag9` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag10` VARCHAR(128) DEFAULT NULL;

ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag6` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag7` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag8` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag9` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag10` BIGINT(20) NOT NULL DEFAULT '0';

ALTER TABLE eh_flow_cases ADD COLUMN path VARCHAR(1024) COMMENT 'flow case path';

ALTER TABLE eh_flow_actions CHANGE COLUMN script_id script_main_id BIGINT NOT NULL DEFAULT 0;
