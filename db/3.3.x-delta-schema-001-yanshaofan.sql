ALTER TABLE `eh_organizations` ADD COLUMN `group_type` VARCHAR(64)  COMMENT 'enterprise, department, service_group'; 
ALTER TABLE `eh_organization_members` ADD COLUMN `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to the organization id';
ALTER TABLE `eh_organization_members` ADD COLUMN `group_path` VARCHAR(128) COMMENT 'refer to the organization path';

/**
 * 创建机构组织跟角色的关系表
 */
DROP TABLE IF EXISTS `eh_organization_role_maps`;
CREATE TABLE `eh_organization_role_maps`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `owner_type` varchar(64),
    `owner_id` BIGINT,
    `role_id` BIGINT NOT NULL,
    `private_flag` varchar(64) COMMENT 'public , private',
    `status` TINYINT DEFAULT 0 COMMENT '0: invalid 1: valid',
    `create_time` DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;