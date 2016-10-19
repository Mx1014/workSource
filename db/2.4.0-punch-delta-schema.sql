
-- added by wh  2016-10-19  申请表添加时长和生效日期  

ALTER TABLE `eh_approval_requests` ADD COLUMN `effective_date` DATE COMMENT 'when the request approval effective';
ALTER TABLE `eh_approval_requests` ADD COLUMN `hour_length` DOUBLE COMMENT 'how long (hours) does the request effective'; 