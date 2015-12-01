# belong eh_users partition

DROP TABLE IF EXISTS `eh_user_group_histories`;
CREATE TABLE `eh_user_group_histories` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `group_discriminator` VARCHAR(32) COMMENT 'redendant info for quickly distinguishing associated group', 
    `group_id` BIGINT,
    `region_scope` TINYINT COMMENT 'redundant group info to help region-based group user search',
    `region_scope_id` BIGINT COMMENT 'redundant group info to help region-based group user search',
    
    `member_role` BIGINT NOT NULL DEFAULT 7 COMMENT 'default to ResourceUser role', 
    `member_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance, 3: active',
    `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
