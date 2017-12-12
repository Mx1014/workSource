ALTER TABLE `eh_payment_bill_groups_rules` ADD `brother_rule_id` BIGINT DEFAULT NULL COMMENT '兄弟账单组收费项id';

-- from asset-org by xiongying
CREATE TABLE `eh_community_organization_detail_display` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `detail_flag` TINYINT NOT NULL DEFAULT 0,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_communities` ADD COLUMN `community_number` VARCHAR(64) COMMENT '项目编号';
ALTER TABLE `eh_buildings` ADD COLUMN `building_number` VARCHAR(64) COMMENT '楼栋编号';

ALTER TABLE `eh_enterprise_customers` ADD COLUMN `version` VARCHAR(32) COMMENT '版本号';
ALTER TABLE `eh_contracts` ADD COLUMN `version` VARCHAR(32) COMMENT '版本号';
ALTER TABLE `eh_contracts` ADD COLUMN `building_rename` VARCHAR(64) COMMENT '房间别名';

ALTER TABLE `eh_contracts` ADD COLUMN `namespace_contract_type` VARCHAR(128);
ALTER TABLE `eh_contracts` ADD COLUMN `namespace_contract_token` VARCHAR(128);
