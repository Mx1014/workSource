-- 服务联盟	增加排序和是否显示在app端的字段 by dengs, 20170523
ALTER TABLE `eh_service_alliances` ADD COLUMN `show_or_hide` TINYINT NOT NULL DEFAULT '1' COMMENT '0:hide,1show';
ALTER TABLE `eh_service_alliances` ADD COLUMN `sort_order` BIGINT COMMENT 'order of the service alliance';