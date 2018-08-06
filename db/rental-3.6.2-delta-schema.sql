ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `remark_flag`  tinyint(4) NULL COMMENT '备注是否必填 0否 1是' AFTER `overtime_strategy`,
ADD COLUMN `remark`  varchar(255) NULL COMMENT '备注显示文案' AFTER `remark_flag`;

CREATE TABLE `eh_rentalv2_refund_tips` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int NOT NULL ,
`source_type`  varchar(20) NULL ,
`source_id`  bigint(20) NULL ,
`refund_strategy`  tinyint(4) NULL ,
`tips`  varchar(255) NULL ,
`resource_type`  varchar(20) NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
