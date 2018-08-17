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

-- 通用脚本
-- AUTHOR: dengs
-- REMARK: 访客管理 管理者消息接受表 , add by dengs, 20180425
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

-- AUTHOR: 黄良铭
-- REMARK: #31347 #33785  保存用户当前所在场景
CREATE TABLE `eh_user_current_scene` (
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `uid` BIGINT(32) NOT NULL COMMENT '用户ID',
  `namespace_id` INT(11) DEFAULT NULL COMMENT '域空间ID',
  `community_id` BIGINT(32) DEFAULT NULL COMMENT '园区ID',
  `community_type` TINYINT(4) DEFAULT NULL COMMENT '园区类型',
  `create_time` DATETIME DEFAULT NULL ,
  `update_time` DATETIME DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- END

-- --------------------- SECTION END ---------------------------------------------------------