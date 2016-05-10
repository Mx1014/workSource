ALTER TABLE `eh_organizations` ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of community where the organization member is working';
ALTER TABLE `eh_scene_types` ADD COLUMN `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the parent id of scene, it is used to inherit something from the parent scene';
