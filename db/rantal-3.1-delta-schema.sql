-- add by st.zheng 增加起步价
ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `price_type` TINYINT(4) NULL AFTER `rental_type`,
ADD COLUMN `initiate_price` DECIMAL(10,2) NULL AFTER `weekend_price`,
ADD COLUMN `org_member_initiate_price` DECIMAL(10,2) NULL AFTER `org_member_weekend_price`,
ADD COLUMN `approving_user_initiate_price` DECIMAL(10,2) NULL AFTER `approving_user_weekend_price`;

ALTER TABLE `eh_rentalv2_price_packages`
ADD COLUMN `price_type` TINYINT(4) NULL AFTER `rental_type`,
ADD COLUMN `initiate_price` DECIMAL(10,2) NULL AFTER `price`,
ADD COLUMN `org_member_initiate_price` DECIMAL(10,2) NULL AFTER `org_member_price`,
ADD COLUMN `approving_user_initiate_price` DECIMAL(10,2) NULL AFTER `approving_user_price`;
