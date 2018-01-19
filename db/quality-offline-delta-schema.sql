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