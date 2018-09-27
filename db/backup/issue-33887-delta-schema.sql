-- AUTHOR: 吴寒
-- REMARK: issue-33887: 增加操作人姓名到目录/文件表
ALTER TABLE `eh_file_management_contents` ADD COLUMN `operator_name`  VARCHAR(256) ;
ALTER TABLE `eh_file_management_catalogs` ADD COLUMN `operator_name`  VARCHAR(256) ;
-- REMARK: issue-33887: 给文件表增加索引
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_name` (`content_name`);
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_catalog_id` (`catalog_id`);
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_parent_id` (`parent_id`);