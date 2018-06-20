-- rental-3.5 by st.zheng
CREATE TABLE `eh_rentalv2_holiday` (
`id`  int NOT NULL ,
`holiday_type`  tinyint(8) NULL COMMENT '1:普通双休 2:法定节假日',
`close_date`  text NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
