-- AUTHOR: 吴寒
-- REMARK: issue-33887: 增加操作人姓名到目录/文件表
ALTER TABLE `eh_file_management_contents` ADD COLUMN `operator_name`  VARCHAR(256) ;
ALTER TABLE `eh_file_management_catalogs` ADD COLUMN `operator_name`  VARCHAR(256) ;