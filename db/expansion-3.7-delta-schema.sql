ALTER TABLE `eh_lease_promotions`
ADD COLUMN `house_resource_type` VARCHAR(256) NULL COMMENT '房源类型  rentHouse 出租房源   sellHouse 出售房源' AFTER `category_id`;
