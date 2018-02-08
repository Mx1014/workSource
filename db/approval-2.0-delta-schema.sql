-- 审批2.0 add by nan.rong
ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_remark` VARCHAR(256) COMMENT 'the remark of the approval';

ALTER TABLE `eh_general_approvals` ADD COLUMN `default_order` INTEGER NOT NULL DEFAULT 0;

-- DROP TABLE IF EXISTS `eh_general_approval_scope_map`;
CREATE TABLE `eh_general_approval_scope_map` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `approval_id` BIGINT NOT NULL COMMENT 'id of the approval',
  `source_type` VARCHAR(64) NOT NULL COMMENT 'ORGANIZATION, MEMBERDETAIL',
  `source_id` BIGINT NOT NULL COMMENT 'id of the source',
  `source_description` VARCHAR(128) COMMENT 'the description of the source',
  `create_time` DATETIME COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;