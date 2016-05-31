#
#保存一个公司的一个场所图标的默认设置
#
#
DROP TABLE IF EXISTS eh_rental_default_rules;

CREATE TABLE `eh_rental_default_rules` (
`id` BIGINT(20)    COMMENT 'id',
`owner_type` VARCHAR(255)  COMMENT 'owner type : community ; organization',
`owner_id` BIGINT(20)    COMMENT 'community id or organization id',
`site_type` VARCHAR(255)    COMMENT 'rule for what function',
`rental_start_time` BIGINT(20)    COMMENT '最多提前多少时间预定',
`rental_end_time` BIGINT(20)    COMMENT '最少提前多少时间预定',
`refund_flag` TINYINT(4)    COMMENT  '是否支持退款 1是 0否',
`rental_type` TINYINT(4)    COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
`cancel_time` BIGINT(20)    COMMENT '至少提前取消时间',
`overtime_time` BIGINT(20)    COMMENT '超期时间',
`exclusive_flag` TINYINT(4)    COMMENT '是否为独占资源0否 1 是',
`unit` DOUBLE  COMMENT '1 整租 0.5 可半个租',
`auto_assign` TINYINT(4)    COMMENT '是否动态分配 1是 0否',
`multi_unit` TINYINT(4)    COMMENT '是否允许预约多个场所 1是 0否',
`multi_time_interval` TINYINT(4)    COMMENT '是否允许预约多个时段 1是 0否',
`cancel_flag`  TINYINT(4)  COMMENT '是否允许取消 1是 0否',
`rental_step` INT(11)    COMMENT 'how many time_step must be rental every time',
`need_pay` TINYINT(4)    COMMENT '是否需要支付 1是 0否',
`launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id',
`refund_ratio` INT(11)       COMMENT '退款比例',
`creator_uid` BIGINT(20)    COMMENT '',
`create_time` DATETIME      COMMENT '',
`operator_uid` BIGINT(20)    COMMENT '',
`operate_time` DATETIME      COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;

#
#对于按小时预定的场所默认设置，保存时间段
#
DROP TABLE IF EXISTS eh_rental_time_interval;

CREATE TABLE `eh_rental_time_interval` (
`id` BIGINT(20)    COMMENT '',
`owner_id` BIGINT(20)    COMMENT '',
`owner_type` VARCHAR(255)  COMMENT '"default_rule","site_rule"',
`begin_time` DOUBLE  COMMENT '开始时间-24小时制',
`end_time` DOUBLE  COMMENT '结束时间-24小时制',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;

#
#保存默认设置的关闭时间
#
DROP TABLE IF EXISTS eh_rental_close_dates;

CREATE TABLE `eh_rental_close_dates` (
`id` BIGINT(20)    COMMENT '',
`owner_id` BIGINT(20)    COMMENT '',
`owner_type` VARCHAR(255)  COMMENT '"default_rule","site_rule"',
`close_date` DATE  COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;

#
#保存场所详情图片
#
DROP TABLE IF EXISTS eh_rental_site_pics;

CREATE TABLE `eh_rental_site_pics` (
`id` BIGINT(20)    COMMENT '',
`owner_id` BIGINT(20)    COMMENT '',
`owner_type` VARCHAR(255)  COMMENT '"eh_rental_sites"',
`uri` VARCHAR(1024)  COMMENT '',
 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;

#
#保存场所附件设置
#
DROP TABLE IF EXISTS eh_rental_config_attachments;

CREATE TABLE `eh_rental_config_attachments` (
`id` BIGINT(20)    COMMENT '',
`owner_id` BIGINT(20)    COMMENT '',
`owner_type` VARCHAR(255)  COMMENT '"default_rule","site_rule"',
`attachment_type` TINYINT(4)    COMMENT '',
`content` TEXT  COMMENT '根据type，这里可能是文本或者附件url',
`must_options` TINYINT(4)    COMMENT '0 非必须 1 必选',
PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;

#
#场所增加设定信息
#
ALTER TABLE eh_rental_sites ADD `charge_uid` BIGINT(20)  COMMENT '负责人id',
ADD `cover_uri` VARCHAR(1024)  COMMENT '封面图uri',
ADD `discount_type` TINYINT(4) DEFAULT 0 COMMENT '折扣信息：0不打折 1满减优惠2比例折扣',
ADD `full_price` DECIMAL(10,2)  COMMENT '满XX元',
ADD `cut_price` DECIMAL(10,2)  COMMENT '减XX元',
ADD `discount_ratio` DOUBLE  COMMENT '折扣比例',
ADD `rental_type` TINYINT(4)    COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
ADD `rental_step` INT(11)    COMMENT 'how many time_step must be rental every time',
ADD `exclusive_flag` TINYINT(4)    COMMENT '是否为独占资源0否 1 是',
ADD `auto_assign` TINYINT(4)    COMMENT '是否动态分配 1是 0否',
ADD `multi_unit` TINYINT(4)    COMMENT '是否允许预约多个场所 1是 0否',
ADD `multi_time_interval` TINYINT(4)    COMMENT '是否允许预约多个时段 1是 0否',
ADD `cancel_flag`  TINYINT(4) COMMENT '是否允许取消 1是 0否',
ADD `need_pay` TINYINT(4)    COMMENT '是否需要支付 1是 0否',
ADD `launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id',
ADD `refund_ratio` INT(11)       COMMENT '退款比例',
ADD `refund_flag` TINYINT(4)    COMMENT  '是否支持退款 1是 0否';

#
#场所资源单元格增加设定信息
#
ALTER TABLE  eh_rental_site_rules ADD `original_price` DECIMAL(10,2)    COMMENT '原价（如果不为null则price为打折价）',
ADD `exclusive_flag` TINYINT(4)    COMMENT '是否为独占资源0否 1 是',
ADD `auto_assign` TINYINT(4)    COMMENT '是否动态分配 1是 0否',
ADD `multi_unit` TINYINT(4)    COMMENT '是否允许预约多个场所 1是 0否',
ADD `multi_time_interval` TINYINT(4)    COMMENT '是否允许预约多个时段 1是 0否',
ADD `cancel_flag` TINYINT(4)   COMMENT '是否允许取消 1是 0否',
ADD `launch_pad_item_id` BIGINT(20)   COMMENT '广场图标id';

#
#商品增加排序字段和图片
#

ALTER TABLE  eh_rental_site_items ADD `default_order` INT(11)    COMMENT '排序字段',
ADD `img_uri` VARCHAR(1024)    COMMENT '图片uri';

#
#资源订单增加必要信息(脱离对场所的依赖)
#
ALTER TABLE  eh_rental_bills ADD `site_name` VARCHAR(255)    COMMENT '名称',
ADD `use_time` VARCHAR(255)    COMMENT '使用时间',
ADD `vendor_type` VARCHAR(255)    COMMENT '支付方式,10001-支付宝，10002-微信',
ADD `launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id';

#
#资源订单子单元格订单增加广场图标id
#
ALTER TABLE  eh_rental_sites_bills ADD `launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id';

#
#资源订单子商品订单增加广场图标id
#
ALTER TABLE  eh_rental_items_bills ADD `launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id';

#
#订单附件增加广场图标id
#
#ALTER TABLE  eh_rental_bill_attachments ADD `launch_pad_item_id` bigint(20)    COMMENT '广场图标id';

#
#订单支付关联表增加支付方式
#
ALTER TABLE eh_rental_bill_paybill_map ADD `vendor_type` VARCHAR(255)    COMMENT '支付方式,10001-支付宝，10002-微信';

