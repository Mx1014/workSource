CREATE TABLE `eh_rentalv2_price_classification` (
`id` bigint(20) NOT NULL ,
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
`resource_type`  varchar(255) NULL,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `vip_level`  varchar(255) NULL COMMENT '会员等级 白金卡 金卡 银卡' AFTER `pay_channel`;
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `source`  tinyint(4) NULL COMMENT '0 用户发起 1后台录入' AFTER `vip_level`;
ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `cross_commu_flag`  tinyint(4) NULL COMMENT '是否支持跨项目' AFTER `identify`;


ALTER TABLE `eh_rentalv2_resources`
MODIFY COLUMN `charge_uid`  varchar(256) NULL DEFAULT NULL COMMENT '负责人id' AFTER `notice`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `file_flag`  tinyint(4) NULL COMMENT '附件是否必传 0否 1是' AFTER `remark`;

CREATE TABLE `eh_vip_priority` (
  `id`  bigint(20) NOT NULL ,
  `namespace_id`  int NULL ,
  `vip_level` INT COMMENT '会员等级',
  `vip_level_text` VARCHAR(64)  COMMENT '会员等级文本',
  `priority` INT COMMENT '优先级,数字越大，优先级越高',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '会员等级优先级表';