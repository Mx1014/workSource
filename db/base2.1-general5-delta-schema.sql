-- 工作流节点添加表单字段  ADD BY xq.tian  2018/07/11
ALTER TABLE eh_flow_nodes ADD COLUMN form_status TINYINT NOT NULL DEFAULT 0 COMMENT '0: disable, 1: enable';
ALTER TABLE eh_flow_nodes ADD COLUMN form_origin_id BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_origin_id';
ALTER TABLE eh_flow_nodes ADD COLUMN form_version BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_version';
ALTER TABLE eh_flow_nodes ADD COLUMN form_update_time DATETIME;

ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_type1 VARCHAR(32);
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_id1 BIGINT DEFAULT 0;
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_type2 VARCHAR(32);
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_id2 BIGINT DEFAULT 0;

-- END

--
-- 工作流和业务映射表  add by xq.tian  20180724
--
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

-- ---

--
-- 工作流 key-value 表  add by xq.tian  20180814
--
DROP TABLE IF EXISTS `eh_flow_kv_configs`;
CREATE TABLE `eh_flow_kv_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `key` VARCHAR(64) NOT NULL,
  `value` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 表单 key-value 表  add by xq.tian  20180814
--
DROP TABLE IF EXISTS `eh_general_form_kv_configs`;
CREATE TABLE `eh_general_form_kv_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `key` VARCHAR(64) NOT NULL,
  `value` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_general_forms ADD COLUMN project_type VARCHAR(64) NOT NULL DEFAULT 'EhCommunities';
ALTER TABLE eh_general_forms ADD COLUMN project_id BIGINT NOT NULL DEFAULT 0;

ALTER TABLE `eh_service_alliances` CHANGE COLUMN `integral_tag1` `integral_tag1` BIGINT(20) NULL DEFAULT NULL COMMENT '跳转类型 0-不跳转 2-表单/表单+工作流 3-跳转应用' ;
ALTER TABLE `eh_service_alliances` 	ADD COLUMN `form_id` BIGINT NULL DEFAULT NULL COMMENT '表单id' ;
ALTER TABLE `eh_service_alliances` 	ADD COLUMN `flow_id` BIGINT NULL DEFAULT NULL COMMENT '工作流id' ;
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `skip_type` TINYINT NOT NULL DEFAULT '0' COMMENT '1-当该服务类型下只有一个服务时，点击服务类型直接进入服务。0-反之';

ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `type` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '服务联盟类型' ;

ALTER TABLE `eh_alliance_tag` ADD COLUMN `owner_type` VARCHAR(15) NOT NULL DEFAULT 'organization';

ALTER TABLE `eh_alliance_tag` ADD COLUMN `owner_id` INT(11) NOT NULL DEFAULT '0' ;


-- by st.zheng 允许表单为空
ALTER TABLE `eh_lease_form_requests`
MODIFY COLUMN `source_id`  bigint(20) NULL AFTER `owner_type`;


-- 工位预订 城市管理 通用修改 shiheng.ma 20180824
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `org_id` BIGINT(20) DEFAULT NULL COMMENT '所属管理公司Id';
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `owner_type` VARCHAR(128) DEFAULT NULL COMMENT '项目类型';
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `owner_id` BIGINT(20) DEFAULT NULL COMMENT '项目Id';
