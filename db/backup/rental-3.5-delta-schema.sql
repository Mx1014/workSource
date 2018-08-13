-- rental-3.5 by st.zheng
CREATE TABLE `eh_rentalv2_holiday` (
`id`  int NOT NULL ,
`holiday_type`  tinyint(8) NULL COMMENT '1:普通双休 2:法定节假日',
`close_date`  text NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `original_price`  decimal(10,2) NULL AFTER `workday_price`,
ADD COLUMN `org_member_original_price`  decimal(10,2) NULL AFTER `org_member_workday_price`,
ADD COLUMN `approving_user_original_price`  decimal(10,2) NULL AFTER `approving_user_workday_price`;
