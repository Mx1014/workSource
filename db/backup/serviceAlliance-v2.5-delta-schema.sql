-- 服务联盟	增加排序和是否显示在app端的字段 by dengs, 20170523
ALTER TABLE `eh_service_alliances` ADD COLUMN `display_flag` TINYINT NOT NULL DEFAULT '1' COMMENT '0:hide,1:display';
ALTER TABLE `eh_service_alliances` CHANGE COLUMN `default_order` `default_order` BIGINT COMMENT 'default value is id';