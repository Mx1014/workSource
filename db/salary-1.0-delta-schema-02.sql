DROP TABLE IF EXISTS `eh_acl_roles`;
CREATE TABLE `eh_acl_roles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `app_id` BIGINT,
  `name` VARCHAR(32) NOT NULL COMMENT 'name of hte operating role',
  `description` VARCHAR(512),
  `tag` VARCHAR(32),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `creator_uid` BIGINT DEFAULT 0 COMMENT 'creator uid',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'record create time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_acl_role_name` (`namespace_id`,`app_id`,`name`,`owner_type`,`owner_id`),
  KEY `u_eh_acl_role_tag` (`tag`),
  KEY `i_eh_ach_role_owner` (`namespace_id`,`app_id`,`owner_type`,`owner_id`),
  KEY `i_eh_acl_role_creator_uid` (`creator_uid`),
  KEY `i_eh_acl_role_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;