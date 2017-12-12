CREATE TABLE `eh_quality_inspection_modle_community_map` (
  `id`          BIGINT(20) NOT NULL,
  `standard_id` BIGINT(20) DEFAULT 0 NOT NULL,
  `specification_id` BIGINT(20) DEFAULT 0 NOT NULL,
  `category_id` BIGINT(20) DEFAULT 0 NOT NULL,
  `model_type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '0:standard',
  `target_type` VARCHAR(255) DEFAULT NULL,
  `target_id`   BIGINT(20)   DEFAULT NULL COMMENT 'community id ',
  `create_time` DATETIME     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE `eh_quality_inspection_standards`
  ADD COLUMN `refer_id` BIGINT(20) NULL;