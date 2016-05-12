-- ALTER TABLE `eh_organizations` ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of community where the organization member is working';
ALTER TABLE `eh_scene_types` ADD COLUMN `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the parent id of scene, it is used to inherit something from the parent scene';

ALTER TABLE `eh_organization_community_requests` MODIFY COLUMN `update_time` DATETIME DEFAULT NULL;

CREATE TABLE `eh_namespace_details` (
  `id` BIGINT NOT NULL,
  `namespace_id` Integer NOT NULL DEFAULT 0,
  `resource_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the type of resource in the namespace, community_residential, community_commercial, community_mix',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_namespace_id` (`namespace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
