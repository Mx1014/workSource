
DROP TABLE IF EXISTS `eh_op_promotion_assigned_scopes`;
CREATE TABLE `eh_op_promotion_assigned_scopes`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `promotion_id` BIGINT NOT NULL COMMENT 'promotion id',
    `scope_code` TINYINT NOT NULL DEFAULT '0' COMMENT '0: all, 1: community, 2: city',
    `scope_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `u_eh_scope_promotion_code_id` (`promotion_id`,`scope_code`,`scope_id`),
    FOREIGN KEY `fk_eh_promotion_assigned_scope_promotion_id`(`promotion_id`) REFERENCES `eh_op_promotion_activities`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_op_promotion_activities` DROP COLUMN `scope_id`, DROP COLUMN `scope_code`;

ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `integral_tag1` BIGINT DEFAULT 0;
ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `integral_tag2` BIGINT DEFAULT 0;
ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `integral_tag3` BIGINT DEFAULT 0;
ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `integral_tag4` BIGINT DEFAULT 0;
ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `integral_tag5` BIGINT DEFAULT 0;

ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_op_promotion_activities` ADD COLUMN `string_tag5` VARCHAR(128);

ALTER TABLE `eh_schedule_tasks` DROP COLUMN `owner_type`, DROP COLUMN `owner_id`;
