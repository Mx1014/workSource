-- 地址表添加两列存储电商使用的楼栋和门牌， add by tt, 20170213（这样写速度快点）
ALTER TABLE `eh_addresses` ADD COLUMN `business_building_name` VARCHAR(128),
	ADD COLUMN `business_apartment_name` VARCHAR(128);