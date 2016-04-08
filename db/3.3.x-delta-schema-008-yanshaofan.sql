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