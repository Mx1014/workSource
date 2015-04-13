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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_server_shard_map`;
CREATE TABLE `eh_server_shard_map` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `server_id` INTEGER NOT NULL,
    `shard_id` INTEGER NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE `u_eh_ssm_server_shard` (`server_id`, `shard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_content_shard_map`;
CREATE TABLE `eh_content_shard_map` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `sharding_domain` VARCHAR(32) NOT NULL,
    `sharding_page` BIGINT,
    `shard_id` INTEGER,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_csm_domain_page` (`sharding_domain`, `sharding_page`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_configurations`;
CREATE TABLE `eh_configurations` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `name` VARCHAR(64) NOT NULL,
    `value` VARCHAR(256) NOT NULL,
    `description` VARCHAR(256),
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_conf_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_messages`;
CREATE TABLE `eh_messages` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `app_id` BIGINT NOT NULL DEFAULT 1 COMMENT 'default to messaging app itself',
    `message_seq` BIGINT NOT NULL COMMENT 'message sequence id generated at server side',
    `sender_uid` BIGINT NOT NULL,
    `channel_type` VARCHAR(32) NOT NULL,
    `channel_token` VARCHAR(32) NOT NULL,
    `message_text` TEXT COMMENT 'message content',
    `message_meta` TEXT COMMENT 'encoded message meta info',
    `encode_version` INT NOT NULL DEFAULT 1 COMMENT 'message meta encode version',
    `sender_tag` VARCHAR(32) COMMENT 'sender generated tag',
    `create_time` DATETIME NOT NULL COMMENT 'message creation time',
    PRIMARY KEY (`id`),
    INDEX `i_eh_msgs_namespace`(`namespace_id`),
    INDEX `i_eh_msgs_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_forums`;
CREATE TABLE `eh_forums` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `app_id` BIGINT NOT NULL DEFAULT 2 COMMENT 'default to forum application itself',
    `owner_type` VARCHAR(32) NOT NULL,
    `owner_id` BIGINT,
    `name` VARCHAR(64) NOT NULL,
    `description` TEXT,
    `post_count` BIGINT NOT NULL DEFAULT 0,
    `modify_seq` BIGINT NOT NULL,
    `update_time` DATETIME NOT NULL,
    `create_time` DATETIME NOT NULL,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_frm_owner_name`(`owner_type`, `owner_id`, `name`),
    INDEX `i_eh_frm_namespace`(`namespace_id`),
    INDEX `i_eh_frm_owner`(`owner_type`, `owner_id`),
    INDEX `i_eh_frm_post_count` (`post_count`),
    INDEX `i_eh_frm_modify_seq` (`modify_seq`),
    INDEX `i_eh_frm_update_time` (`update_time`),
    INDEX `i_eh_frm_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_forum_posts`;
CREATE TABLE `eh_forum_posts` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `app_id` BIGINT NOT NULL DEFAULT 2 COMMENT 'default to forum application itself',
    `forum_id` BIGINT NOT NULL COMMENT 'forum that it belongs',
    `parent_post_id` BIGINT COMMENT 'replied post id',
    `creator_uid` BIGINT NOT NULL COMMENT 'post creator uid',
    
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `geohash` VARCHAR(64),
    `source_geo_id` BIGINT,
    
    `category_id` BIGINT,
    `category_path` VARCHAR(128),
    
    `modify_seq` BIGINT NOT NULL,
    `child_count` BIGINT NOT NULL DEFAULT 0,
    `forward_count` BIGINT NOT NULL DEFAULT 0,
    `like_count` BIGINT NOT NULL DEFAULT 0,

    `subject` VARCHAR(512),
    `content_type` INTEGER,
    `content` TEXT COMMENT 'post content text',
    
    `embedded_obj_id` BIGINT,
    `embedded_obj_json` TEXT COMMENT 'json encoded embedded object',
    `embedded_obj_version` INT NOT NULL DEFAULT 1 COMMENT 'encode version',
    
    `integral_tag1` BIGINT,
    `integral_tag2` BIGINT,
    `integral_tag3` BIGINT,
    `integral_tag4` BIGINT,
    `integral_tag5` BIGINT,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
    
    `update_time` DATETIME NOT NULL,
    `create_time` DATETIME NOT NULL,
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_post_seqs`(`modify_seq`),
    INDEX `i_eh_post_geohash`(`geohash`),
    INDEX `i_eh_post_creator`(`creator_uid`),
    INDEX `i_eh_post_itag1`(`integral_tag1`),
    INDEX `i_eh_post_itag2`(`integral_tag2`),
    INDEX `i_eh_post_stag1`(`string_tag1`),
    INDEX `i_eh_post_stag2`(`string_tag2`),
    INDEX `i_eh_post_update_time`(`update_time`),
    INDEX `i_eh_post_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_forum_attachments`;
CREATE TABLE `eh_forum_attachments` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `post_id` BIGINT NOT NULL,
    `store_type` VARCHAR(32) COMMENT 'content store type',
    `store_uri` VARCHAR(32) COMMENT 'identify the store instance',
    `content_type` VARCHAR(32) COMMENT 'attachment object content type',
    `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
    `creator_uid` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `i_eh_frmatt_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_acl_privileges`;
CREATE TABLE `eh_acl_privileges` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `app_id` BIGINT,
    `name` VARCHAR(32) NOT NULL COMMENT 'name of the operation privilege',
    `description` VARCHAR(512) COMMENT 'privilege description',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_acl_priv_app_id_name`(`app_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_acl_roles`;
CREATE TABLE `eh_acl_roles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `app_id` BIGINT,
    `name` VARCHAR(32) NOT NULL COMMENT 'name of hte operating role',
    `description` VARCHAR(512),
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_acl_role_app_id_name`(`app_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_apps`;
CREATE TABLE `eh_apps` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `app_key` VARCHAR(64),
    `secret_key` VARCHAR(1024),
    `name` VARCHAR(128) NOT NULL,
    `description` VARCHAR(2048),
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '0 - inactive, 1 - active',
    `create_time` DATETIME,

    PRIMARY KEY (`id`),
    UNIQUE `u_eh_app_reg_app_key`(`app_key`),
    UNIQUE `u_eh_app_reg_name`(`name`),
    INDEX `i_eh_app_reg_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_sequences`;
CREATE TABLE `eh_sequences` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `domain` VARCHAR(32) NOT NULL,
    `start_seq` BIGINT NOT NULL DEFAULT 1,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_namespaces`;
CREATE TABLE `eh_namespaces` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `name` VARCHAR(64),
    
    PRIMARY KEY (`id`),
    UNIQUE `u_ns_name`(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `eh_namespaces` AUTO_INCREMENT = 4096;

SET foreign_key_checks = 1;
