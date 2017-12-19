-- merge from energy3.2
CREATE TABLE `eh_energy_meter_category_map`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'category id',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_energy_meter_fomular_map`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `fomular_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'fomular id',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_energy_meter_setting_logs ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id' AFTER `namespace_id`;
-- merge from energy3.2 end

-- 物业巡检权限细化 start  by jiarui
CREATE TABLE `eh_equipment_modle_community_map` (
  `id`          BIGINT(20) NOT NULL,
  `standard_id` BIGINT(20) DEFAULT 0 NOT NULL,
  `template_id` BIGINT(20) DEFAULT 0 NOT NULL,
  `model_type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '0:standard 1:template',
  `target_type` VARCHAR(255) DEFAULT NULL,
  `target_id`   BIGINT(20)   DEFAULT NULL COMMENT 'community id ',
  `create_time` DATETIME     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE `eh_equipment_inspection_standards`
  ADD COLUMN `refer_id` BIGINT(20) NULL;

ALTER TABLE `eh_equipment_inspection_templates`
  ADD COLUMN `refer_id`  bigint(20) NULL;


ALTER TABLE `eh_equipment_inspection_templates`
  ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  ADD COLUMN `target_id` BIGINT(20) NOT NULL DEFAULT 0 ;
-- 物业巡检权限细化 end  by  jiarui
-- merge from forum2.6 by yanjun 201712121010 start


