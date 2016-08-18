DROP TABLE IF EXISTS `eh_community_services`;
CREATE TABLE `eh_community_services` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
  `scope_id` BIGINT,
  `item_name` VARCHAR(32),
  `item_label` VARCHAR(64),
  `icon_uri` VARCHAR(1024),
  `action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_users ADD COLUMN `namespace_user_type` varchar(128) DEFAULT NULL COMMENT 'the type of user';

ALTER TABLE eh_organizations ADD COLUMN `namespace_organization_token` varchar(256) DEFAULT NULL COMMENT 'the token from third party';

ALTER TABLE eh_organizations ADD COLUMN `namespace_organization_type` varchar(128) DEFAULT NULL COMMENT 'the type of organization';