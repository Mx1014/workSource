-- rental-3.6 by st.zheng
ALTER TABLE eh_rentalv2_resource_pics rename eh_rentalv2_site_resources;
ALTER TABLE `eh_rentalv2_resource_pics`
ADD COLUMN `type`  varchar(64) NULL DEFAULT 'pic' AFTER `owner_type`;