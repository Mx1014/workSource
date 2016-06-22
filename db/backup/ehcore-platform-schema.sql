SET foreign_key_checks = 0;

use ehcore;

DROP TABLE IF EXISTS `eh_servers`;
CREATE TABLE `eh_servers`(
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `master_id` INTEGER COMMENT 'master server id',
    `address_uri` VARCHAR(256),
    `address_port` INTEGER,
    `server_type` INTEGER NOT NULL DEFAULT 0 COMMENT '0: DB, 1: redis storage server, 2: redis cache server',
    `status` INTEGER NOT NULL DEFAULT 0 COMMENT '0 : disabled, 1: enabled',
    `config_tag` VARCHAR(32),
    `description` VARCHAR(256),
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_servers_config_tag`(`config_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_shards`;
CREATE TABLE `eh_shards`(
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `sharding_domain` VARCHAR(64) NOT NULL,
    `anchor` BIGINT,
    `create_time` DATETIME COMMENT 'time that shard has been created',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_shards_domain_anchor` (`sharding_domain`, `anchor`),
    INDEX `i_eh_shards_anchor`(`anchor`),
    INDEX `i_eh_shards_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_server_shard_map`;
CREATE TABLE `eh_server_shard_map` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `server_id` INTEGER NOT NULL,
    `shard_id` INTEGER NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE `u_eh_ssm_server_shard` (`server_id`, `shard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_content_shard_map`;
CREATE TABLE `eh_content_shard_map` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `sharding_domain` VARCHAR(32) NOT NULL,
    `sharding_page` BIGINT,
    `shard_id` INTEGER,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_csm_domain_page` (`sharding_domain`, `sharding_page`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_configurations`;
CREATE TABLE `eh_configurations` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `name` VARCHAR(64) NOT NULL,
    `display_name` VARCHAR(128),
    `value` VARCHAR(256) NOT NULL,
    `description` VARCHAR(256),
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_conf_namespace_name` (`namespace_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_messages`;
CREATE TABLE `eh_messages` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `app_id` BIGINT NOT NULL DEFAULT 1 COMMENT 'default to messaging app itself',
    `message_seq` BIGINT NOT NULL COMMENT 'message sequence id generated at server side',
    `sender_uid` BIGINT NOT NULL,
    `context_type` VARCHAR(32),
    `context_token` VARCHAR(32),
    `channel_type` VARCHAR(32) NOT NULL,
    `channel_token` VARCHAR(32) NOT NULL,
    `message_text` TEXT COMMENT 'message content',
    `meta_app_id` BIGINT COMMENT 'app that is in charge of message content and meta intepretation',
    `message_meta` TEXT COMMENT 'JSON encoded message meta info, in format of string to string map',
    `encode_version` INT NOT NULL DEFAULT 1 COMMENT 'message meta encode version',
    `sender_tag` VARCHAR(32) COMMENT 'sender generated tag',
    `create_time` DATETIME NOT NULL COMMENT 'message creation time',
    PRIMARY KEY (`id`),
    INDEX `i_eh_msgs_namespace`(`namespace_id`),
    INDEX `i_eh_msgs_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_message_boxs`;
CREATE TABLE `eh_message_boxs` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `box_key` VARCHAR(64) NOT NULL COMMENT 'message box unique identifier',
    `message_id` BIGINT NOT NULL COMMENT 'foreign key to message record',
    `message_seq` BIGINT NOT NULL COMMENT 'message sequence id that identifies the message',
    `box_seq` BIGINT NOT NULL COMMENT 'sequence of the message inside the box',
    `create_time` DATETIME NOT NULL COMMENT 'time that message goes into the box, taken from create time of the message',
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_mbx_msg_id` (`message_id`) REFERENCES `eh_messages` (`id`) ON DELETE CASCADE,
    UNIQUE `u_eh_mbx_msg_box_seqs`(`message_seq`, `box_seq`),
    INDEX `i_eh_mbx_namespace`(`namespace_id`),
    INDEX `i_eh_mbx_msg_seq`(`message_seq`),
    INDEX `i_eh_mbx_box_seq`(`box_seq`),
    INDEX `i_eh_mbx_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_acls`;
CREATE TABLE `eh_acls` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_type` VARCHAR(32) NOT NULL,
    `owner_id` BIGINT,
    `grant_type` TINYINT NOT NULL DEFAULT 1 COMMENT '0 - decline, 1 - grant',
    `privilege_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    `order_seq` INTEGER NOT NULL DEFAULT 0,
    `creator_uid` BIGINT NOT NULL COMMENT 'assignment creator uid',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_acl_owner_privilege`(`owner_type`, `owner_id`),
    INDEX `i_eh_acl_owner_order_seq`(`order_seq`),
    INDEX `i_eh_acl_creator`(`creator_uid`),
    INDEX `i_eh_acl_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_acl_privileges`;
CREATE TABLE `eh_acl_privileges` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `app_id` BIGINT,
    `name` VARCHAR(32) NOT NULL COMMENT 'name of the operation privilege',
    `description` VARCHAR(512) COMMENT 'privilege description',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_acl_priv_app_id_name`(`app_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_acl_roles`;
CREATE TABLE `eh_acl_roles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `app_id` BIGINT,
    `name` VARCHAR(32) NOT NULL COMMENT 'name of hte operating role',
    `description` VARCHAR(512),
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_acl_role_app_id_name`(`app_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_acl_role_assignments`;
CREATE TABLE `eh_acl_role_assignments` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_type` VARCHAR(32) NOT NULL COMMENT 'owner resource(i.e., forum) type',
    `owner_id` BIGINT COMMENT 'owner resource(i.e., forum) id',
    `target_type` VARCHAR(32) NOT NULL COMMENT 'target object(user/group) type',
    `target_id` BIGINT COMMENT 'target object(user/group) id',
    `role_id`   BIGINT NOT NULL COMMENT 'role id that is assigned', 
    `creator_uid` BIGINT NOT NULL COMMENT 'assignment creator uid',
    `create_time` DATETIME COMMENT 'record create time',
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_acl_role_asgn_owner`(`owner_type`, `owner_id`),
    UNIQUE `u_eh_acl_role_asgn_unique`(`owner_type`, `owner_id`, `target_type`, `target_id`, `role_id`),
    INDEX `i_eh_acl_role_asgn_creator`(`creator_uid`),
    INDEX `i_eh_acl_role_asgn_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_apps`;
CREATE TABLE `eh_apps` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `creator_uid` BIGINT,
    `app_key` VARCHAR(64),
    `secret_key` VARCHAR(1024),
    `name` VARCHAR(128) NOT NULL,
    `description` VARCHAR(2048),
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '0 - inactive, 1 - active',
    `create_time` DATETIME,
	`update_uid` bigint(20) DEFAULT NULL,
	`update_time` datetime DEFAULT NULL,

    PRIMARY KEY (`id`),
    UNIQUE `u_eh_app_reg_app_key`(`app_key`),
    UNIQUE `u_eh_app_reg_name`(`name`),
    INDEX `i_eh_app_reg_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_app_profiles`;
CREATE TABLE `eh_app_profiles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `app_id` BIGINT NOT NULL COMMENT 'owner app id',
    `item_group` VARCHAR(32) COMMENT 'for profile grouping purpose',
    `item_name` VARCHAR(32),
    `item_value` TEXT,
    `item_tag` VARCHAR(32) COMMENT 'for profile value tagging purpose',

    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_appprof_app_id`(`app_id`) REFERENCES `eh_apps`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_appprof_app_id_group`(`app_id`, `item_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_sequences`;
CREATE TABLE `eh_sequences` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `domain` VARCHAR(32) NOT NULL,
    `start_seq` BIGINT NOT NULL DEFAULT 1,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_namespaces`;
CREATE TABLE `eh_namespaces` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `name` VARCHAR(64),
    
    PRIMARY KEY (`id`),
    UNIQUE `u_ns_name`(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET foreign_key_checks = 1;
