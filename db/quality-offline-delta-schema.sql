-- 离线支持  jiarui
ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `update_uid` BIGINT (20) NULL AFTER `status`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP AFTER `update_uid`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `delete_uid` BIGINT (20) NULL AFTER `update_uid`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `delete_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP AFTER `delete_uid`;

-- 离线支持  jiarui
-- 日志增加项目 jiarui

ALTER TABLE `eh_quality_inspection_logs`
  ADD COLUMN `scope_code`  tinyint(4) NULL DEFAULT 1 ;
ALTER TABLE `eh_quality_inspection_logs`
  ADD COLUMN `scope_id`  bigint(20) NULL DEFAULT 0 ;

UPDATE  `eh_quality_inspection_logs`
SET scope_id =(SELECT eh_quality_inspection_standards.target_id FROM  eh_quality_inspection_standards where eh_quality_inspection_standards.id = eh_quality_inspection_logs.target_id);

-- 日志增加项目 jiarui
