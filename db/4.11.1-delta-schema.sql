-- bydengs,20171114 服务联盟加客服id service_alliance2.9.3
ALTER TABLE `eh_service_alliances` ADD COLUMN `online_service_uid` BIGINT COMMENT 'online service user id';
ALTER TABLE `eh_service_alliances` ADD COLUMN `online_service_uname` varchar(64) COMMENT 'online service user name';

-- bydengs,20171114 物业报修2.9.3
ALTER TABLE `eh_pm_tasks` ADD COLUMN `organization_name` VARCHAR(128) COMMENT '报修的任务的公司名称';