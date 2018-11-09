CREATE TABLE `eh_rentalv2_price_classification` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int NULL ,
`source_id`  bigint(20) NULL ,
`source_type`  varchar(255) NULL ,
`owner_id`  bigint(20) NULL ,
`owner_type`  varchar(255) NULL ,
`user_price_type`  tinyint(4) NULL ,
`classification`  varchar(255) NULL ,
`workday_price`  decimal(10,2) NULL ,
`original_price`  decimal(10,2) NULL ,
`initiate_price`  decimal(10,2) NULL ,
`discount_type`  tinyint(4) NULL ,
`full_price`  decimal(10,2) NULL ,
`cut_price`  decimal(10,2) NULL ,
`discount_ratio`  double NULL ,
`resource_type`  varchar(255) NULL
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

