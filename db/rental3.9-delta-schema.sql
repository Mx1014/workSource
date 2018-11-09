CREATE TABLE `eh_rentalv2_price_classification` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int NULL ,
`source_id`  bigint(20) NULL ,
`source_type`  varchar(255) NULL ,
`owner_id`  bigint(20) NULL ,
`owner_type`  varchar(255) NULL ,
`classification`  varchar(255) NULL ,
`workday_price`  decimal(10,2) NULL ,
`original_price`  decimal(10,2) NULL ,
`initiate_price`  decimal(10,2) NULL ,
`discount_type`  tinyint(4) NULL ,
`full_price`  decimal(10,2) NULL ,
`cut_price`  decimal(10,2) NULL ,
`discount_ratio`  double NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_vip_priority` (
  `id`  bigint(20) NOT NULL ,
  `namespace_id`  int NULL ,
  `vip_level` INT COMMENT '会员等级',
  `vip_level_text` VARCHAR(64)  COMMENT '会员等级文本',
  `priority` INT COMMENT '优先级,数字越大，优先级越高',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '会员等级优先级表';