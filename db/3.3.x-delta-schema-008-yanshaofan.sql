# 
# 任务处理人员组 
# 
DROP TABLE IF EXISTS `eh_organization_task_targets`; 
CREATE TABLE `eh_organization_task_targets`( 
`id` BIGINT NOT NULL, 
`owner_type` VARCHAR(64) NOT NULL,
`owner_id` BIGINT DEFAULT NULL,
`target_type` VARCHAR(64) NOT NULL COMMENT 'target object(user/group) type',
`target_id` BIGINT DEFAULT NULL COMMENT 'target object(user/group) id',
`task_type` VARCHAR(64) NOT NULL,
PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 


# 
# 20160413
#
ALTER TABLE `eh_organizations` ADD COLUMN `show_flag` TINYINT DEFAULT 1 COMMENT '';
ALTER TABLE `eh_organization_owners` ADD COLUMN `namespace_id` INT NOT NULL DEFAULT '0';

# 
# 20160414
#
ALTER TABLE `eh_organization_owners` ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT '0';