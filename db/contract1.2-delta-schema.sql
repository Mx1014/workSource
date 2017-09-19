ALTER TABLE `eh_var_field_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';