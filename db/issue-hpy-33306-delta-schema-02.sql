-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.1（展示能耗数据）
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `energy_consume` VARCHAR(1024) COMMENT '能耗用量';

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.1 新增账单上传附件
CREATE TABLE `eh_payment_bill_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `bill_group_id` BIGINT,
  `filename` VARCHAR(1024) COMMENT '附件名称',
  `content_type` VARCHAR(64) COMMENT '附件类型：word/pdf/png...',
  `content_uri` VARCHAR(1024) COMMENT '附件uri',
  `content_url` VARCHAR(1024) COMMENT '附件url',
  `creator_uid` BIGINT COMMENT '创建者ID',
  `create_time` DATETIME,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- --------------------- SECTION END ---------------------------------------------------------
-- 通用脚本
-- add by yanlong.liang 20180713
-- 帖子和活动表增加最低限制人数
ALTER TABLE `eh_forum_posts` ADD COLUMN `min_quantity` INT(11) COMMENT '最低限制人数';
ALTER TABLE `eh_activities` ADD COLUMN `min_quantity` INT(11) COMMENT '最低限制人数';
-- END BY yanlong.liang

-- 通用脚本
-- add by yanlong.liang 20180719
-- 导出中心增加阅读状态和下载次数
ALTER TABLE `eh_tasks` ADD COLUMN `read_status` TINYINT(4) COMMENT '阅读状态';
ALTER TABLE `eh_tasks` ADD COLUMN `download_times` INT(11) COMMENT '下载次数';
-- end

-- 访客管理 管理者消息接受表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_message_receivers`;
CREATE TABLE `eh_visitor_sys_message_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `creator_uid` BIGINT COMMENT '创建者/访客管理者id',
  `create_time` DATETIME COMMENT '事件发生时间',
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理 管理者消息接受表';

ALTER TABLE eh_visitor_sys_visitors ADD COLUMN source TINYINT DEFAULT '0' COMMENT '访客来源，0:内部录入 1:外部对接';
ALTER TABLE eh_visitor_sys_visitors ADD COLUMN notify_third_success_flag TINYINT DEFAULT '0' COMMENT '访客来源为外部对接，0：未回调到第三方 1：回调失败 2:回调成功';











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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
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
-- --------------------- SECTION END ---------------------------------------------------------

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
-- AUTHOR: dengs
-- REMARK: issue-34945 添加字段存储支付信息 add by dengs, 20180808
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `pay_dto` text COMMENT '支付系统返回预付单信息';

-- AUTHOR: xq.tian
-- REMARK: 工作流节点添加表单字段  ADD BY xq.tian  2018/07/11
ALTER TABLE eh_flow_nodes ADD COLUMN form_status TINYINT NOT NULL DEFAULT 0 COMMENT '0: disable, 1: enable';
ALTER TABLE eh_flow_nodes ADD COLUMN form_origin_id BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_origin_id';
ALTER TABLE eh_flow_nodes ADD COLUMN form_version BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_version';
ALTER TABLE eh_flow_nodes ADD COLUMN form_update_time DATETIME;

ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_type1 VARCHAR(32);
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_id1 BIGINT DEFAULT 0;
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_type2 VARCHAR(32);
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_id2 BIGINT DEFAULT 0;

-- AUTHOR: xq.tian
-- REMARK: 工作流和业务映射表  add by xq.tian  20180724
-- DROP TABLE `eh_flow_service_mappings`;
CREATE TABLE `eh_flow_service_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `display_name` VARCHAR(64) NOT NULL,
  `flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the real flow id for all copy, the first flow_main_id=0',
  `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_goto_node_id BIGINT COMMENT 'only sub flow node, when sub flow absort go to node id';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_step_type VARCHAR(32) COMMENT 'only sub flow node, when sub flow absort step type';

ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_project_type VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings project_type';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_project_id BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings project_id';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_module_type VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings module_type';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_module_id BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings module_id';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_owner_type VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings owner_type';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_owner_id BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings owenr_id';

ALTER TABLE eh_flow_cases ADD COLUMN sub_flow_parent_id BIGINT NOT NULL DEFAULT 0 ;
ALTER TABLE eh_flow_cases ADD COLUMN sub_flow_path VARCHAR(128) NOT NULL DEFAULT '';

ALTER TABLE eh_flows ADD COLUMN config_status TINYINT NOT NULL DEFAULT 0 COMMENT 'config status, 2: config, 3: snapshot';
-- END



-- --------------------- SECTION END ---------------------------------------------------------