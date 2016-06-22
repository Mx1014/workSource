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


# 
# 20160420
#
ALTER TABLE `eh_organization_task_targets` ADD COLUMN `message_type` VARCHAR(64) COMMENT 'PUSH COMMENT SMS ';




# 
# 20160505
# 范围内的菜单调整表 
# 
DROP TABLE IF EXISTS `eh_web_menu_scopes`; 
CREATE TABLE `eh_web_menu_scopes`( 
`id` BIGINT NOT NULL, 
`menu_id` BIGINT NULL,
`menu_name` VARCHAR(64) NULL,
`owner_type` VARCHAR(64) NOT NULL,
`owner_id` BIGINT DEFAULT NULL,
`apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: delete , 1: override, 2: revert',
PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 

