-- 增加设备字段
ALTER TABLE `eh_equipment_inspection_equipments`
ADD COLUMN `brand_name` varchar(1024) COMMENT 'brand_name',
ADD COLUMN `construction_party` varchar(1024) COMMENT 'construction party',
ADD COLUMN `discard_time` datetime COMMENT 'discard time ',
ADD COLUMN `manager_contact` varchar(1024) ,
ADD COLUMN `detail` varchar(1024) ,
ADD COLUMN `factory_time` datetime ,
ADD COLUMN `provenance` varchar(1024) ,
ADD COLUMN `price` decimal  ,
ADD COLUMN `buy_time` datetime ,
ADD COLUMN `depreciation_years` bigint(10) COMMENT '折旧年限' ;

--增加字段
ALTER TABLE `eh_var_field_items` ADD COLUMN `business_value` TINYINT COMMENT 'the value defined in special business like status';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `business_value` TINYINT COMMENT 'the value defined in special business like status';