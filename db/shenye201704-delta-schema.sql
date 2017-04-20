ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `create_time` DATETIME; 
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN target_type` VARCHAR(32) NOT NULL DEFAULT '' ; 
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT '0' ; 
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `standard_id` BIGINT NOT NULL DEFAULT '0' ; 
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `equipment_id` BIGINT NOT NULL DEFAULT '0' ; 
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `inspection_category_id` BIGINT NOT NULL DEFAULT '0' ; 

ALTER TABLE eh_equipment_inspection_task_logs ADD COLUMN `inspection_category_id` BIGINT NOT NULL DEFAULT '0' ; 
ALTER TABLE eh_equipment_inspection_task_logs ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT '0' ; 
ALTER TABLE eh_equipment_inspection_task_logs ADD COLUMN `namespace_id` BIGINT NOT NULL DEFAULT '0' ; 