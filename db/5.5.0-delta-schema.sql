-- 文档管理1.2 start by ryan.
ALTER TABLE `eh_file_management_catalog_scopes` ADD COLUMN `source_type` VARCHAR(64) COMMENT'the type of the source' AFTER `source_id`;
UPDATE `eh_file_management_catalog_scopes` SET source_type = 'MEMBERDETAIL';
-- 文档管理1.2 end by ryan.
