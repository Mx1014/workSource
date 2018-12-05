-- 资源预约3.8
ALTER TABLE `eh_rentalv2_resources`
ADD COLUMN `people_spec`  integer(10) NULL COMMENT '容纳人数' AFTER `address_id`;

CREATE TABLE `eh_rentalv2_structure_template` (
`id`  bigint(20) NOT NULL ,
`name`  varchar(255) NULL ,
`display_name`  varchar(255) NULL ,
`icon_uri`  varchar(255) NULL ,
`default_order`  bigint(20) NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_rentalv2_structures` (
`id`  bigint(20) NOT NULL ,
`template_id`  bigint(20) NOT NULL ,
`source_type`  varchar(45) NULL ,
`source_id`  bigint(20) NULL ,
`name`  varchar(255) NULL ,
`display_name`  varchar(255) NULL ,
`icon_uri`  varchar(255) NULL ,
`is_surport`  tinyint(4) NULL ,
`default_order`  bigint(20) NULL ,
`resource_type`  varchar(45) NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;