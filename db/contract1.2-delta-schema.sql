ALTER TABLE `eh_var_field_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE eh_var_fields ADD COLUMN `field_param` VARCHAR(128);

ALTER TABLE `eh_buildings` ADD INDEX building_name ( `name`, `alias_name`);
-- 4.9.2
-- 增加域空间左上角显示场景名称的配置项
ALTER TABLE eh_namespace_details ADD COLUMN name_type tinyint(4) DEFAULT 0;

-- fix 15631 & 15636 add by xiongying20170919
ALTER TABLE eh_organizations ADD COLUMN website VARCHAR(256);
ALTER TABLE eh_organizations ADD COLUMN unified_social_credit_code VARCHAR(256);

