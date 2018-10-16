-- AUTHOR: 黄明波
-- REMARK: 服务联盟样式列表添加排序
ALTER TABLE `eh_service_alliance_categories`	CHANGE COLUMN `default_order` `default_order` BIGINT NOT NULL DEFAULT '0' ;

ALTER TABLE `eh_service_alliances` CHANGE COLUMN `address` `address` VARCHAR(255) NULL DEFAULT NULL ;
-- END

 
