-- 通用脚本
-- add by yanlong.liang 20180719
-- 导出中心增加阅读状态和下载次数
ALTER TABLE `eh_tasks` ADD COLUMN `read_status` TINYINT(4) COMMENT '阅读状态';
ALTER TABLE `eh_tasks` ADD COLUMN `download_times` INT(11) COMMENT '下载次数';
-- end