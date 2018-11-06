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

ALTER TABLE `eh_alliance_tag` ADD COLUMN `owner_id` BIGINT(20) NOT NULL DEFAULT '0' ;


-- by st.zheng 允许表单为空
ALTER TABLE `eh_lease_form_requests`
MODIFY COLUMN `source_id`  bigint(20) NULL AFTER `owner_type`;


-- 工位预订 城市管理 通用修改 shiheng.ma 20180824
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `org_id` BIGINT(20) DEFAULT NULL COMMENT '所属管理公司Id';
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `owner_type` VARCHAR(128) DEFAULT NULL COMMENT '项目类型';
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `owner_id` BIGINT(20) DEFAULT NULL COMMENT '项目Id';

CREATE TABLE `eh_office_cubicle_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `org_id` BIGINT NOT NULL DEFAULT 0 COMMENT '管理公司Id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `customize_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: general configure, 1:customize configure',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- END 工位预订