-- 通用脚本
-- ADD BY xq.tian
-- ISSUE-32697 运营统计重构
ALTER TABLE eh_terminal_hour_statistics ADD COLUMN cumulative_active_user_number BIGINT NOT NULL DEFAULT 0;

ALTER TABLE eh_terminal_day_statistics ADD COLUMN average_active_user_number BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_terminal_day_statistics ADD COLUMN average_active_user_change_rate DECIMAL(10, 2) NOT NULL DEFAULT 0;

-- ISSUE-32697 END

-- 通用脚本  
-- AUTHOR: 黄良铭
-- REMARK: #Issue-33216 服务协议信息表
CREATE TABLE `eh_service_agreement` (

  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11) NOT NULL  COMMENT '域空间ID',
  `agreement_content` MEDIUMTEXT  COMMENT '协议内容',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '服务协议信息表';

-- #Issue-33216  end


-- 通用脚本
-- AUTHOR jiarui  20180625
-- REMARK issue- 	26688  企业信息V1.0
CREATE TABLE `eh_customer_attachments` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(1024) DEFAULT  NULL ,
  `namespace_id` INT(11) NOT NULL COMMENT 'namespaceId',
  `customer_id` BIGINT(20) NOT NULL DEFAULT '0',
  `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `status` TINYINT(4) NOT NULL COMMENT '0:inactive 2:active',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业管理1.0 附件表';
-- end


-- 通用脚本 所有环境
-- AUTHOR wuhan 2018-7-12
-- REMARK issues-32611 社保bug:月份为空导致错误 先删除数据再修改表
DELETE FROM eh_social_security_payments WHERE pay_month IS NULL;
ALTER TABLE eh_social_security_payments CHANGE `pay_month` `pay_month` VARCHAR(8) NOT NULL COMMENT 'yyyymm'; 
-- end


-- 通用脚本
-- AUTHOR jiarui  20180717
-- REMARK issue-27396	 服务联盟 活动企业数据同步
CREATE TABLE `eh_customer_configutations` (
  `id` bigint(20) NOT NULL,
  `scope_type` VARCHAR(64)  DEFAULT NULL COMMENT 'service_alliance or activity',
  `scope_id` bigint(20) NOT NULL COMMENT 'code',
  `value`  tinyint(4) NOT NULL DEFAULT '0' ,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: invalid, 2: valid',
  `namespace_id` int(11) not NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'record create time',
  `creator_uid` BIGINT(20) DEFAULT NULL COMMENT 'creatorUid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_customer_potential_datas` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL  DEFAULT 0,
  `name` text COMMENT 'potential customer name',
  `source_id` bigint(20) DEFAULT NULL COMMENT 'refer to service allance activity categoryId',
  `source_type` varchar(1024) DEFAULT NULL COMMENT 'service_alliance or activity',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: invalid, 2: valid',
  `operate_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ,
  `create_time` datetime NOT NULL ,
  `delete_time` datetime  NULL ,
  `delete_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER  TABLE `eh_enterprise_customers` ADD COLUMN `source_id` BIGINT(20)  NULL ;
ALTER  TABLE `eh_enterprise_customers` ADD COLUMN `source_type` VARCHAR(64)  NULL;
ALTER TABLE `eh_customer_talents` ADD COLUMN `talent_source_item_id`  BIGINT(20) NULL COMMENT 'categoryId' AFTER `age`;
ALTER TABLE `eh_customer_talents` ADD COLUMN `origin_source_id` bigint(20) NULL COMMENT 'origin potential data primary key' AFTER `age`;
ALTER TABLE `eh_customer_talents` ADD COLUMN `origin_source_type`  VARCHAR(64) NULL COMMENT 'service_alliance or activity' AFTER `age`;
ALTER TABLE `eh_customer_talents` ADD COLUMN `register_status`  TINYINT(4) NOT NULL  DEFAULT  0 AFTER `age`;

-- end

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK:物业缴费6.2 增加减免费项
CREATE TABLE `eh_payment_subtraction_items` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `bill_group_id` BIGINT,
  `subtraction_type` VARCHAR(255) COMMENT '减免费项类型，eh_payment_bill_items：费项（如：物业费），eh_payment_late_fine：减免滞纳金（如：物业费滞纳金）',
  `charging_item_id` BIGINT COMMENT '减免费项的id，存的都是charging_item_id，因为滞纳金是跟着费项走，所以可以通过subtraction_type类型，判断是否减免费项滞纳金',
  `charging_item_name` VARCHAR(255) COMMENT '减免费项名称',
  `creator_uid` BIGINT COMMENT '创建者ID',
  `create_time` DATETIME,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='减免费项配置表';

-- AUTHOR: 杨崇鑫
-- REMARK: 取消滞纳金表字段非空限制
ALTER TABLE eh_payment_late_fine MODIFY COLUMN customer_id BIGINT COMMENT 'allows searching taking advantage of it';

-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.6 V3.7 修改评分字段
ALTER TABLE `eh_pm_tasks` MODIFY COLUMN `star`  varchar(4) NULL DEFAULT NULL COMMENT 'evaluate score' AFTER `status`;
-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.6 V3.7 新增费用字段
ALTER TABLE `eh_pm_tasks` ADD COLUMN `amount`  decimal(16,0) NOT NULL DEFAULT 0 COMMENT '订单费用';

-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.6 V3.7 新增通用配置
CREATE TABLE `eh_pm_task_configs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `payment_flag` tinyint(4) DEFAULT '0' COMMENT '0: inactive, 1: active',
  `payment_account` bigint(20) DEFAULT NULL COMMENT '收款方账号',
  `payment_account_type` tinyint(4) DEFAULT NULL COMMENT '收款方类型',
  `content_hint` varchar(64) DEFAULT '请描述服务具体内容' COMMENT '服务内容提示文本',
  `creator_id` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `updater_id` bigint(20) NOT NULL DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  `task_category_id` bigint(20) DEFAULT '6' COMMENT '应用类型：6为物业报修（1为正中会报修），9为投诉建议',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物业报修通用配置表';

-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.6 V3.7 新增费用清单
CREATE TABLE `eh_pm_task_orders` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `task_id` bigint(20) NOT NULL COMMENT '报修单Id',
  `biz_order_num` varchar(64) DEFAULT NULL COMMENT '处理过的资源预订订单号',
  `pay_order_id` bigint(20) DEFAULT NULL COMMENT '支付系统订单号',
  `payment_order_type` tinyint(8) DEFAULT NULL COMMENT '订单类型 1续费订单 2欠费订单 3支付订单 4退款订单',
  `status` tinyint(8) DEFAULT NULL COMMENT '订单状态0未支付 1已支付',
  `amount` decimal(16,0) DEFAULT NULL COMMENT '订单金额',
  `service_fee` decimal(16,0) DEFAULT NULL COMMENT '服务费',
  `product_fee` decimal(16,0) DEFAULT NULL COMMENT '产品费',
  `account_id` bigint(20) DEFAULT NULL COMMENT '收款方账号',
  `order_commit_url` varchar(1024) DEFAULT NULL,
  `order_commit_token` varchar(1024) DEFAULT NULL,
  `order_commit_nonce` varchar(128) DEFAULT NULL,
  `order_commit_timestamp` bigint(20) DEFAULT NULL,
  `pay_info` text,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物业报修订单表';

CREATE TABLE `eh_pm_task_order_details` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `owner_type` varchar(32) DEFAULT NULL,
  `task_id` bigint(20) NOT NULL COMMENT '报修单Id',
  `order_id` bigint(20) NOT NULL COMMENT '资源预订订单id',
  `product_name` varchar(60) DEFAULT NULL COMMENT '产品名称',
  `product_amount` int(11) DEFAULT NULL COMMENT '产品数量',
  `product_price` decimal(16,0) DEFAULT NULL COMMENT '产品单价',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物业报修订单明细表';

-- 通用脚本
-- ADD BY jun.yan
-- ISSUE-32697 模块对应的菜单是否需要授权, add by yanjun 20180517
ALTER TABLE `eh_service_modules` ADD COLUMN `menu_auth_flag`  tinyint(4) NOT NULL DEFAULT 1 COMMENT 'if its menu need auth' ;

ALTER TABLE `eh_service_modules` ADD COLUMN `category`  varchar(255) NULL COMMENT 'classify, module, subModule';
-- end

-- 通用脚本
-- add by liangyanlong 20180710
-- 第三方应用链接白名单.
CREATE TABLE `eh_app_white_list` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `link` VARCHAR(128) NOT NULL COMMENT '第三方应用链接',
  `name` VARCHAR(128) NOT NULL COMMENT '第三方应用名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方应用白名单';
-- end

-- 通用脚本  
-- AUTHOR: 黄良铭
-- REMARK: 一键推送消息(短信)记录表
CREATE TABLE `eh_push_message_log` (

  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11) NOT NULL  COMMENT '域空间ID',
  `content` TEXT  COMMENT '推送内容',
  `push_type` INT(2)  COMMENT '推送方式（1表示应用消息推送，2表示短信推送）',
  `receiver_type` INT(2)  COMMENT '推送对象的类型（0表示所有人，1表示按项目，2表示按手机号）',
  `operator_id` INT(11)  COMMENT '操作者',
  `create_time` DATETIME  COMMENT '推送创建时间',
  `push_status` INT(2)  COMMENT '推送状态(1表示等待推送，2表示推送中，3表示推送完成)',
  `receivers` VARCHAR(600)  COMMENT '推送对象(与推送对象类型对应，所有人为空，按项目为项目ID，按手机号为手机号)',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '一键推送消息(短信)记录表';

-- end

-- 通用脚本
-- AUTHOR 郑思挺  20180718
-- REMARK 资源预约3.5
CREATE TABLE `eh_rentalv2_holiday` (
`id`  int NOT NULL ,
`holiday_type`  tinyint(8) NULL COMMENT '1:普通双休 2:法定节假日',
`close_date`  text NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `original_price`  decimal(10,2) NULL AFTER `workday_price`;
ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `org_member_original_price`  decimal(10,2) NULL AFTER `org_member_workday_price`;
ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `approving_user_original_price`  decimal(10,2) NULL AFTER `approving_user_workday_price`;

ALTER TABLE `eh_rentalv2_cells`
ADD UNIQUE INDEX `u_eh_cell_id` (`id`) ;
-- end

-- 通用脚本
-- AUTHOR ryan荣 20180718
-- REMARK 人事档案2.8 动态表单字段重构
DROP TABLE eh_general_form_groups;
DROP TABLE eh_archives_configurations;
DROP TABLE eh_archives_logs;
-- 此表未使用 直接重建
DROP TABLE eh_archives_forms;
CREATE TABLE `eh_archives_forms` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`form_name` VARCHAR(64) NOT NULL COMMENT 'name of the form',
	`static_fields` TEXT NOT NULL  COMMENT 'static form fields in json format',
	`dynamic_fields` TEXT COMMENT 'dynamic form fields in json format',
	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0. inactive, 1.active',
	`operator_uid` BIGINT,
	`operator_time` DATETIME,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_archives_form_vals`(
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`form_id` BIGINT COMMENT 'the id of the archives form',
	`source_id` BIGINT COMMENT 'the source id ',
	`field_name` VARCHAR(128) COMMENT 'the name of the field',
	`field_type` VARCHAR(128) COMMENT 'the type of the field',
	`field_value` TEXT COMMENT 'the value of the field',
	`create_time` DATETIME,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_archives_form_groups` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`form_id` BIGINT COMMENT 'the id of the archives form',
	`form_groups` TEXT NOT NULL  COMMENT 'the group of the form in json format',
	`operator_uid` BIGINT,
	`operator_time` DATETIME,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- end

-- AUTHOR: 丁建民
-- REMARK: #Issue-21713 合同管理 合同模板及打印
CREATE TABLE `eh_contract_templates` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `category_id` bigint(20) NOT NULL COMMENT 'contract category id 用于多入口',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `name` varchar(64) NOT NULL COMMENT '合同模板名称',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active',
  `content_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'gogs:存储gogs的commitId txt:存储文本内容',
  `contents` longtext COMMENT '模板内容',
  `last_commit` varchar(40) DEFAULT NULL COMMENT 'repository last commit id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '复制于哪个合同模板',
  `version` int(11) DEFAULT '0' COMMENT '版本记录',
  `contract_template_type` tinyint(2) DEFAULT '0' COMMENT '0 收款合同模板 1付款合同模板',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `delete_uid` bigint(20) DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `update_uid` bigint(20) DEFAULT '0' COMMENT 'record update user id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同模板打印表';

ALTER TABLE `eh_contracts` ADD COLUMN `template_id` bigint(20) NULL COMMENT 'contract template_id';
-- #Issue-21713  end

-- 通用脚本
-- AUTHOR liuyilin  20180712
-- REMARK issue-32260 人脸识别V1.6 - 管理控制台 增加字段长度 删除唯一约束
ALTER TABLE `eh_aclink_servers` MODIFY COLUMN `uuid` VARCHAR(64) NOT NULL;
ALTER TABLE `eh_aclink_servers` DROP INDEX `u_eh_aclink_servers_uuid`;
-- end

-- 通用脚本
-- AUTHOR huangmingbo  20180719
-- REMARK issue-33610 服务联盟 服务表字段过短导致前端解析出错
ALTER TABLE `eh_service_alliances` CHANGE COLUMN `service_url` `service_url` VARCHAR(512) NULL DEFAULT NULL;
-- end

-- 通用脚本
-- AUTHOR： jiarui  20180725
-- REMARK: issue-33088 增加索引
ALTER TABLE eh_var_field_scopes ADD INDEX `i_eh_filed_scope`(`module_name`,`status`,`namespace_id`);
ALTER TABLE eh_var_field_item_scopes ADD INDEX `i_eh_field_item_scope`(`module_name`,`status`,`namespace_id`);
ALTER TABLE eh_var_field_group_scopes ADD INDEX `i_eh_field_group_scope`(`module_name`,`status`,`namespace_id`);
ALTER TABLE eh_var_field_item_scopes ADD INDEX `i_eh_item_scope_item_id`(`module_name`,`status`,`namespace_id`);


-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇
-- REMARK:客户字段无法满足并导入，新增字段见详情
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `buy_or_lease_item_id` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `biz_address` varchar(1024) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `biz_life` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `customer_intention_level` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `enter_dev_goal` text null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `controller_name` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `controller_sun_birth` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `controller_lunar_birth` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `financing_demand_item_id` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag1` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag2` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag3` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag4` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag5` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag6` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag7` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag8` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag9` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag10` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag11` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag12` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag13` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag14` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag15` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag16` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box1` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box2` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box3` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box4` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box5` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box6` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box7` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box8` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box9` TINYINT null default null;

-- end
