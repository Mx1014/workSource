-- 文档管理1.2 start by ryan.
ALTER TABLE `eh_file_management_catalog_scopes` ADD COLUMN `source_type` VARCHAR(64) COMMENT'the type of the source' AFTER `source_id`;
UPDATE `eh_file_management_catalog_scopes` SET source_type = 'MEMBERDETAIL';
-- 文档管理1.2 end by ryan.


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by tianxiaoqiang 2018.5.8
CREATE TABLE `eh_message_records` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) DEFAULT '0',
  `dst_channel_token` VARCHAR(32) DEFAULT NULL,
  `dst_channel_type` VARCHAR(32) DEFAULT NULL,
  `status` VARCHAR(32) COMMENT 'message status',
  `app_id` BIGINT(20) DEFAULT '1' COMMENT 'default to messaging app itself',
  `message_seq` BIGINT(20) DEFAULT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` BIGINT(20) DEFAULT NULL,
  `sender_tag` VARCHAR(32) DEFAULT NULL COMMENT 'sender generated tag',
  `channels_info` VARCHAR(2048) DEFAULT NULL,
  `body_type`VARCHAR(32),
  `body` VARCHAR(2048),
  `delivery_option` INT(2) DEFAULT '0',
  `create_time` DATETIME NOT NULL COMMENT 'message creation time',
  `session_token` VARCHAR(128),
  `device_id` VARCHAR(2048),
  `index_id` BIGINT(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by huangmingbo 2018.5.8
ALTER TABLE `eh_service_hotlines` ADD COLUMN `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0-deleted 1-active';

-- by zheng
ALTER TABLE `eh_configurations` MODIFY `value` VARCHAR(1024);

-- add by yanjun 增加字段长度 201805091806
 
ALTER TABLE `eh_service_module_apps` MODIFY COLUMN `custom_tag`  VARCHAR(256)  NULL DEFAULT '';
-- issue-23470 by liuyilin
ALTER TABLE `eh_door_access`
ADD `enable_amount` TINYINT COMMENT '是否支持按次数开门的授权';

ALTER TABLE `eh_door_auth`
ADD `auth_rule_type` TINYINT COMMENT '授权规则的种类,0 按时间,1 按次数';
ALTER TABLE `eh_door_auth`
ADD `total_auth_amount` INT COMMENT '授权的总开门次数';
ALTER TABLE `eh_door_auth`
ADD `valid_auth_amount` INT COMMENT '剩余的开门次数';
-- End by liuyilin 


-- added by wh 
-- 增加应打卡时间给punch_log
ALTER TABLE `eh_punch_logs` ADD should_punch_time BIGINT COMMENT '应该打卡时间(用以计算早退迟到时长)';


-- asset_5.1_wentian by wentian
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_day_after` INTEGER DEFAULT NULL COMMENT '欠费日期后多少天';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_day_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1:欠费前；2：欠费后';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_objs` VARCHAR(3064) DEFAULT NULL COMMENT '催缴对象,格式为{type+id,type+id,...}';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_app_id` BIGINT DEFAULT NULL COMMENT '催缴app信息模板的id';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_msg_id` BIGINT DEFAULT NULL COMMENT '催缴sms信息模板的id';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `create_time` DATETIME DEFAULT NULL COMMENT '创建时间';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `create_uid` BIGINT DEFAULT NULL COMMENT '创建账号id';

-- 缴费app端支付按钮和合同查看隐藏
CREATE TABLE `eh_payment_app_views`(
  `id` BIGINT COMMENT 'primary key',
  `namespace_id` INTEGER DEFAULT NULL ,
  `community_id` BIGINT DEFAULT NULL ,
  `has_view` TINYINT NOT NULL ,
  `view_item` VARCHAR(16) NOT NULL ,
  `remark1_type` VARCHAR(16) DEFAULT NULL ,
  `remark1_identifier` VARCHAR(128) DEFAULT NULL ,
  `remark2_type` VARCHAR(16) DEFAULT NULL ,
  `remark2_identifier` VARCHAR(128) DEFAULT NULL,
  `remark3_type` VARCHAR(16) DEFAULT NULL ,
  `remark3_identifier` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '缴费app端支付按钮和合同查看隐藏';

-- 客户管理增加企业管理字段 hotline  post_uri refer to  organization_detail    unified_social_credit_code  refer to organization
ALTER  TABLE  eh_enterprise_customers  ADD  COLUMN  `hotline` varchar(256) DEFAULT NULL;
ALTER  TABLE  eh_enterprise_customers  ADD  COLUMN  `post_uri` varchar(128) DEFAULT NULL;
ALTER  TABLE  eh_enterprise_customers  ADD  COLUMN  `unified_social_credit_code` varchar(256) DEFAULT NULL;
ALTER  TABLE  eh_enterprise_customers  ADD  COLUMN  `admin_flag` TINYINT(4) NOT NULL DEFAULT 0;



-- 客户增加附件表  by jiarui
CREATE TABLE `eh_enterprise_customer_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `customer_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 客户增加企业管理员记录表   by jiarui
CREATE TABLE `eh_enterprise_customer_admins` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `customer_id` bigint(20) NOT NULL DEFAULT '0',
  `contact_name` varchar(256) DEFAULT NULL,
  `contact_token` varchar(256) DEFAULT NULL,
  `contact_type` varchar(256) DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 1: 同步客户
-- /customer/syncEnterpriseCustomerIndex


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


-- 政务服务 1.0
-- by shiheng.ma
-- 政策表
CREATE TABLE `eh_policies` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20),
	`title` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '',
	`outline` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '',
	`content` text COMMENT 'content data',
	`priority` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the rank of policy',
	`creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
	`updater_uid` bigint(20) NOT NULL DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 政策类型中间表
CREATE TABLE `eh_policy_categories` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `policy_id` bigint(20) NOT NULL COMMENT 'id of the policy',
  `category_id` bigint(20) NOT NULL COMMENT 'category of policy',
	`active_flag` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 政策查询记录表
CREATE TABLE `eh_policy_records` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20),
	`creator_id` bigint(20) NOT NULL COMMENT '',
	`creator_name` varchar(128) NOT NULL COMMENT '',
	`creator_phone` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '',
	`creator_org_id` bigint(20) NOT NULL COMMENT '',
	`creator_org_name` varchar(128) NOT NULL COMMENT '',
	`turnover` varchar(60) NOT NULL DEFAULT '' COMMENT '营业额',
	`tax` varchar(60) NOT NULL DEFAULT '' COMMENT '纳税总额',
	`qualification` varchar(60) NOT NULL DEFAULT '' COMMENT '单位资质',
	`financing` varchar(60) NOT NULL DEFAULT '' COMMENT 'A轮融资',
	`create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 政策代办配置表
CREATE TABLE `eh_policy_agent_rules` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id',
	`namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'community id or organization id',
  `agent_flag` TINYINT(4) DEFAULT NULL COMMENT '是否代办:0为不可代办，1为可代办',
  `agent_phone` varchar(64) DEFAULT NULL COMMENT '联系方式',
	`agent_info` text DEFAULT NULL COMMENT '代办介绍',
	`creator_id` bigint(20) NOT NULL COMMENT '创建人',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`updater_uid` bigint(20) COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 政务服务 1.0 end

-- 企业管理员列表 by jiarui
ALTER TABLE `eh_enterprise_customer_admins` ADD COLUMN `namespace_id`  int NOT NULL DEFAULT 0 AFTER `create_time`;



-- ------------------------------
-- 服务联盟V3.3（新增需求提单功能）
-- 产品功能 #26469 add by huangmingbo  2018/05/29
-- ------------------------------
-- DROP TABLE IF EXISTS `eh_flow_script_configs`;
CREATE TABLE `eh_service_alliance_providers` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '\'\'',
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0',
	`app_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'module_app id, new type of alliance, represent one kine fo alliance',
	`type` BIGINT(20) NOT NULL COMMENT 'old type of Alliance，represent one kind of alliance',
	`name` VARCHAR(50) NOT NULL COMMENT 'provider name',
	`category_id` BIGINT(20) NOT NULL COMMENT '见 categories表',
	`mail` VARCHAR(50) NOT NULL COMMENT 'enterprise mail',
	`contact_number` VARCHAR(50) NOT NULL COMMENT 'mobile or contact phone',
	`contact_name` VARCHAR(50) NOT NULL COMMENT 'contact name',
	`total_score` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'total score',
	`score_times` INT(11) NOT NULL DEFAULT '0' COMMENT 'the num of times make the score',
	`score_flow_case_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'the final flow case id that make score',
	`status` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '0-deleted 1-active',
	`create_time` DATETIME NOT NULL,
	`create_uid` BIGINT(20) NOT NULL COMMENT 'create user id',
	PRIMARY KEY (`id`)
)
COMMENT='服务商信息'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `eh_alliance_extra_events` (
	`id` BIGINT(20) NOT NULL,
	`flow_case_id` BIGINT(20) NOT NULL,
	`topic` VARCHAR(200) NOT NULL COMMENT 'topic of current event',
	`time` DATETIME NOT NULL COMMENT 'the time that event happen',
	`address` VARCHAR(200) NULL DEFAULT NULL,
	`provider_id` BIGINT(20) NULL DEFAULT NULL COMMENT 'id of alliance_providers',
	`provider_name` VARCHAR(50) NULL DEFAULT NULL COMMENT 'name of alliance_provider',
	`members` VARCHAR(500) NOT NULL COMMENT 'those who participate in',
	`content` MEDIUMTEXT NOT NULL COMMENT 'main body',
	`enable_read` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '0-hide for applier  1-show for applier',
	`enable_notify_by_email` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '0-not send email  1-send email to provider',
	`create_time` DATETIME NOT NULL,
	`create_uid` BIGINT(20) NOT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='工作流中，新建事件表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `eh_alliance_extra_event_attachment` (
	`id` BIGINT(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL COMMENT 'the id of eh_alliance_extra_events',
	`file_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'like image,jpg. in lower case',
	`file_uri` VARCHAR(1024) NOT NULL COMMENT 'like cs://1/...',
	`file_name` VARCHAR(200) NULL DEFAULT NULL,
	`file_size` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'file size (Byte)',
	`create_uid` BIGINT(20) NOT NULL,
	`create_time` DATETIME NOT NULL COMMENT 'create time',
	PRIMARY KEY (`id`)
)
COMMENT='用于服务联盟工作流中新建事件时保存附件使用'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


ALTER TABLE `eh_service_alliance_jump_module`
	ADD COLUMN `module_id` BIGINT NOT NULL DEFAULT '0' AFTER `module_name`;


ALTER TABLE `eh_service_alliance_jump_module`
	ADD COLUMN `instance_config` TEXT NULL DEFAULT NULL AFTER `module_url`;

-- 超级管理员 Added by janson
ALTER TABLE `eh_organizations` ADD COLUMN `admin_target_id`  bigint(20) NULL ;
-- 能耗抄表精度  by jiarui
ALTER TABLE `eh_energy_meter_reading_logs`
	MODIFY COLUMN `reading`  decimal(10,2) NULL DEFAULT NULL AFTER `meter_id`;
ALTER TABLE `eh_energy_meter_tasks`
	MODIFY COLUMN `last_task_reading`  decimal(10,2) NULL DEFAULT NULL AFTER `executive_expire_time`;
ALTER TABLE `eh_energy_meter_tasks`
	MODIFY COLUMN `reading`  decimal(10,2) NULL DEFAULT NULL AFTER `last_task_reading`;

-- added a new column for eh_payment_bill_groups by wentian
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `bills_day_type` TINYINT NOT NULL DEFAULT 4 COMMENT '1. 本周期前几日；2.本周期第几日；3.本周期结束日；4.下周期首月第几日';

-- ISSUE#26184 门禁人脸识别 by liuyilin 201180524
-- 内网服务器表创建
CREATE TABLE `eh_aclink_servers` (
    `id` BIGINT(20) NOT NULL,
    `name` VARCHAR(32) DEFAULT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`uuid` VARCHAR(6) NOT NULL COMMENT '配对码',
    `ip_address` VARCHAR(128) DEFAULT NULL COMMENT 'IP地址',
	`link_status` TINYINT NOT NULL DEFAULT '0' COMMENT '联网状态',
	`active_time` DATETIME DEFAULT NULL COMMENT '激活时间',
	`sync_time`  DATETIME DEFAULT NULL COMMENT '上次同步时间',
	`version` VARCHAR(8) DEFAULT NULL COMMENT '版本号',
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '组织id',
	`owner_type` TINYINT NOT NULL DEFAULT '0' COMMENT '组织类型',
	`aes_server_key` VARCHAR(64) COMMENT 'AES公钥',
	`status` TINYINT NOT NULL DEFAULT '0' COMMENT '激活状态0未激活1已激活2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	PRIMARY KEY (`id`),
	UNIQUE `u_eh_aclink_servers_uuid`(`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '内网服务器表';

-- 内网ipad表创建
CREATE TABLE `eh_aclink_ipads` (
	`id` BIGINT(20) NOT NULL,
	`name` VARCHAR(32) DEFAULT NULL,
	`door_access_id` BIGINT(20) NOT NULL COMMENT '关联门禁id',
	`enter_status` TINYINT DEFAULT '0' COMMENT '进出标识 1进0出',
	`uuid` VARCHAR(6) NOT NULL COMMENT '配对码',
	`link_status` TINYINT NOT NULL DEFAULT '0' COMMENT '联网状态',
	`server_id` BIGINT(20) COMMENT '服务器id',
	`active_time` DATETIME DEFAULT NULL COMMENT '激活时间',
	`status` TINYINT NOT NULL DEFAULT '0' COMMENT '激活状态0未激活1已激活2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	PRIMARY KEY (`id`),
	UNIQUE `u_eh_aclink_ipads_uuid`(`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '内网ipad表';

-- 内网摄像头表创建
CREATE TABLE `eh_aclink_cameras` (
	`id` BIGINT(20) NOT NULL,
	`name` VARCHAR(32) DEFAULT NULL,
	`door_access_id` BIGINT NOT NULL COMMENT '关联门禁id',
	`enter_status` TINYINT DEFAULT '0' COMMENT '进出标识 1进0出',
	`link_status` TINYINT NOT NULL DEFAULT '0' COMMENT '联网状态',
	`ip_address` VARCHAR(128) DEFAULT NULL COMMENT 'IP地址',
	`server_id` BIGINT(20) COMMENT '服务器id',
	`status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态1正常2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	`key_code` VARCHAR(128) NOT NULL COMMENT '摄像头密钥' ,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT = '内网摄像头表';

-- 门禁与服务器多对一关联
ALTER TABLE `eh_door_access`
ADD `local_server_id` BIGINT(20) COMMENT '服务器id';

-- 人脸识别照片表创建
CREATE TABLE `eh_face_recognition_photos` (
	`id` BIGINT(20) NOT NULL,
    `user_id` BIGINT(20) COMMENT '用户id(正式用户)',
	`auth_id` BIGINT(20) COMMENT '授权id(访客)',
	`user_type` TINYINT NOT NULL DEFAULT '0' COMMENT '照片关联的用户类型,0正式用户,1访客',
	`img_uri` VARCHAR(2048) COMMENT '照片uri',
	`img_url` VARCHAR(2048) NOT NULL COMMENT '照片url',
	`sync_time` DATETIME COMMENT '上次同步时间时间' ,
	`status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态1正常2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT = '人脸识别照片表';

-- 门禁版本包url长度限制修改
ALTER TABLE eh_aclink_firmware MODIFY COLUMN `download_url` VARCHAR(1024);
ALTER TABLE eh_aclink_firmware MODIFY COLUMN `info_url` VARCHAR(1024);

-- issue28839允许修改门禁开门方式,增加字段表示是否支持二维码
ALTER TABLE `eh_door_access` ADD COLUMN `has_qr` TINYINT NOT NULL DEFAULT '1' COMMENT '门禁二维码能力0无1有';

-- End by: yilin Liu


-- 大沙河梯控
-- by shiheng.ma
-- 新增字段 第三方KeyU字段
ALTER TABLE `eh_door_auth`
ADD COLUMN `key_u` VARCHAR(16) NULL DEFAULT NULL COMMENT '第三方用户秘钥' AFTER `right_remote`;

-- 新增字段 授权楼层
ALTER TABLE `eh_door_access`
ADD COLUMN `floor_id` VARCHAR(2000) NULL DEFAULT NULL COMMENT '授权楼层' AFTER `groupId`;
-- 大沙河梯控 end

-- 唐岑
ALTER TABLE `eh_contract_param_group_map` ADD COLUMN `user_id` BIGINT DEFAULT 0 COMMENT '用户id';

-- 给eh_payment_bills表添加支付方式、留言、账单是否有缴费凭证的标志字段  by Steve Tang
ALTER TABLE `eh_payment_bills` ADD COLUMN `payment_type` int DEFAULT null COMMENT '账单的支付方式（0-线下缴费，1-微信支付，2-对公转账，8-支付宝支付）';

ALTER TABLE `eh_payment_bills` ADD COLUMN `certificate_note` varchar(255) DEFAULT NULL COMMENT '上传凭证图片时附加的留言';

ALTER TABLE `eh_payment_bills` ADD COLUMN `is_upload_certificate` tinyint(4) DEFAULT null COMMENT '该账单是否上传了缴费凭证（0:否，1：是）';

-- 创建缴费凭证表
DROP TABLE IF EXISTS `eh_payment_bill_certificate`;

CREATE TABLE `eh_payment_bill_certificate` (
  `id` bigint(20) NOT NULL,
  `bill_id` bigint(20) NOT NULL COMMENT '该凭证记录对应的账单id',
  `certificate_uri` varchar(255) DEFAULT NULL COMMENT '上传凭证图片的uri',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
