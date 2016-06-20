#
#保存一个公司的一个场所图标的默认设置
#
#
DROP TABLE IF EXISTS eh_rental_default_rules;

CREATE TABLE `eh_rental_default_rules` (
`id` BIGINT(20)    COMMENT 'id',
`owner_type` VARCHAR(255)  COMMENT 'owner type : community ; organization',
`owner_id` BIGINT(20)    COMMENT 'community id or organization id',
`site_type` BIGINT(20)    COMMENT 'rule for what function',
`rental_start_time` BIGINT(20)    COMMENT '最多提前多少时间预定',
`rental_end_time` BIGINT(20)    COMMENT '最少提前多少时间预定',
`pay_start_time` BIGINT(20)    COMMENT '',
`pay_end_time` BIGINT(20)    COMMENT '',
`payment_ratio` INT(11)       COMMENT 'payment ratio',
`refund_flag` TINYINT(4)    COMMENT '是否支持退款 1是 0否',
`refund_ratio` INT(11)       COMMENT '退款比例',
`contact_num` VARCHAR(20)   COMMENT 'phone number',
`creator_uid` BIGINT(20)    COMMENT '',
`create_time` DATETIME      COMMENT '',
`operator_uid` BIGINT(20)    COMMENT '',
`operate_time` DATETIME      COMMENT '',
`rental_type` TINYINT(4)    COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
`cancel_time` BIGINT(20)    COMMENT '至少提前取消时间',
`overtime_time` BIGINT(20)    COMMENT '超期时间',
`exclusive_flag` TINYINT(4)    COMMENT '是否为独占资源0否 1 是',
`unit` DOUBLE  COMMENT '1 整租 0.5 可半个租',
`auto_assign` TINYINT(4)    COMMENT '是否动态分配 1是 0否',
`multi_unit` TINYINT(4)    COMMENT '是否允许预约多个场所 1是 0否',
`multi_time_interval` TINYINT(4)    COMMENT '是否允许预约多个时段 1是 0否',
`cancel_flag` TINYINT(4)    COMMENT '是否允许取消 1是 0否',
`rental_step` INT(11)       COMMENT 'how many time_step must be rental every time',
`need_pay` TINYINT(4)    COMMENT '是否需要支付 1是 0否',
`launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id',
`workday_price` DECIMAL(10,2)  COMMENT '工作日价格',
`weekend_price` DECIMAL(10,2)  COMMENT '周末价格',
`site_counts` DOUBLE  COMMENT '可预约个数',
`begin_date` DATE  COMMENT '开始日期',
`end_date` DATE  COMMENT '结束日期',
`open_weekday` VARCHAR(7)   COMMENT '7位二进制，0000000每一位表示星期7123456',


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
#场所表
#
DROP TABLE IF EXISTS eh_rental_sites;

CREATE TABLE `eh_rental_sites` (
`id` BIGINT(20)   COMMENT 'id',
`parent_id` BIGINT(20)  COMMENT '',
`site_name` VARCHAR(127)   COMMENT '名称：',
`site_type2` TINYINT(4)   COMMENT '',
`building_name` VARCHAR(128)   COMMENT '',
`building_id` BIGINT(20)   COMMENT '',
`address` VARCHAR(192)   COMMENT '地址',
`address_id` BIGINT(20)   COMMENT '',
`spec` VARCHAR(255)   COMMENT '规格',
`own_company_name` VARCHAR(60)   COMMENT '',
`contact_name` VARCHAR(40)   COMMENT '',
`contact_phonenum` VARCHAR(20)   COMMENT '咨询电话',
`contact_phonenum2` VARCHAR(20)   COMMENT '',
`contact_phonenum3` VARCHAR(20)   COMMENT '',
`status` TINYINT(4)   COMMENT '',
`creator_uid` BIGINT(20)   COMMENT '',
`create_time` DATETIME   COMMENT '',
`operator_uid` BIGINT(20)   COMMENT '',
`operate_time` DATETIME   COMMENT '',
`introduction` TEXT   COMMENT '详情',
`notice` TEXT   COMMENT '',
`charge_uid` BIGINT(20)  COMMENT '负责人id',
`cover_uri` VARCHAR(1024)  COMMENT '封面图uri',
`discount_type` TINYINT(4) COMMENT '折扣信息：0不打折 1满减优惠2比例折扣',
`full_price` DECIMAL(10,2)  COMMENT '满XX元',
`cut_price` DECIMAL(10,2)  COMMENT '减XX元',
`discount_ratio` DOUBLE  COMMENT '折扣比例',
`rental_type` TINYINT(4)   COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
`rental_step` DOUBLE   COMMENT '按小时预约：最小单元格是多少小时，浮点型',
`exclusive_flag` TINYINT(4)    COMMENT '是否为独占资源0否 1 是',
`auto_assign` TINYINT(4)    COMMENT '是否动态分配 1是 0否',
`multi_unit` TINYINT(4)    COMMENT '是否允许预约多个场所 1是 0否',
`multi_time_interval` TINYINT(4)    COMMENT '是否允许预约多个时段 1是 0否',
`cancel_flag` TINYINT(4)  COMMENT '是否允许取消 1是 0否',
`need_pay` TINYINT(4)    COMMENT '是否需要支付 1是 0否',
`launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id',
`cancel_time` BIGINT(20)    COMMENT '至少提前取消时间',
`rental_start_time` BIGINT(20)    COMMENT '最多提前多少时间预定',
`rental_end_time` BIGINT(20)    COMMENT '最少提前多少时间预定',
`refund_flag` TINYINT(4)    COMMENT '是否支持退款 1是 0否',
`refund_ratio` INT(11)       COMMENT '退款比例',
`longitude` DOUBLE  COMMENT '地址经度',
`latitude` DOUBLE  COMMENT '地址纬度',
`organization_id` BIGINT(20)    COMMENT '所属公司的ID',
`day_begin_time` TIME  COMMENT '对于按小时预定的每天开始时间',
`day_end_time` TIME  COMMENT '对于按小时预定的每天结束时间',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;

#
#场所和归属园区的关联表
#
DROP TABLE IF EXISTS eh_rental_site_owners;

CREATE TABLE `eh_rental_site_owners` (
`id` BIGINT(20)    COMMENT 'id',
`owner_type` VARCHAR(255)  COMMENT 'owner type : community ; organization',
`owner_id` BIGINT(20)    COMMENT 'community id or organization id',
`rental_site_id` BIGINT(20)  COMMENT 'rental_site id',

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
#场所设定好的单元格表
#
DROP TABLE IF EXISTS eh_rental_site_rules;

CREATE TABLE `eh_rental_site_rules` (
`id` BIGINT(20)    COMMENT 'id',
`rental_site_id` BIGINT(20)    COMMENT 'rental_site id',
`rental_type` TINYINT(4)   COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
`amorpm` TINYINT(4)   COMMENT '0:am 1:pm 2:night',
`rental_step` INT(11)   COMMENT 'how many time_step must be rental every time',
`begin_time` DATETIME   COMMENT '开始时间 对于按时间定',
`end_time` DATETIME   COMMENT '结束时间 对于按时间定',
`counts` DOUBLE   COMMENT '共多少个',
`unit` DOUBLE   COMMENT '是否支持0.5个',
`price` DECIMAL(10,2)   COMMENT '折后价',
`site_rental_date` DATE   COMMENT 'which day',
`status` TINYINT(4)   COMMENT 'unuse 0:open 1:closed',
`creator_uid` BIGINT(20)   COMMENT '',
`create_time` DATETIME   COMMENT '',
`operator_uid` BIGINT(20)   COMMENT '',
`operate_time` DATETIME   COMMENT '',
`time_step` DOUBLE   COMMENT '',
`original_price` DECIMAL(10,2)   COMMENT '原价（如果不为null则price为打折价）',
`exclusive_flag` TINYINT(4)    COMMENT '是否为独占资源0否 1 是',
`auto_assign` TINYINT(4)    COMMENT '是否动态分配 1是 0否',
`multi_unit` TINYINT(4)    COMMENT '是否允许预约多个场所 1是 0否',
`multi_time_interval` TINYINT(4)    COMMENT '是否允许预约多个时段 1是 0否',
`launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id',
`site_number` INT  COMMENT '场所号',

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
#物品表
#
DROP TABLE IF EXISTS eh_rental_site_items;

CREATE TABLE `eh_rental_site_items` (
`id` BIGINT(20)    COMMENT 'id',
`rental_site_id` BIGINT(20)    COMMENT 'rental_site id',
`name` VARCHAR(128)   COMMENT '',
`price` DECIMAL(10,2)   COMMENT '',
`counts` INT(11)   COMMENT 'item count',
`status` TINYINT(4)   COMMENT '',
`creator_uid` BIGINT(20)   COMMENT '',
`create_time` DATETIME   COMMENT '',
`operator_uid` BIGINT(20)   COMMENT '',
`operate_time` DATETIME   COMMENT '',
`default_order` INT(11)   COMMENT '排序字段',
`img_uri` VARCHAR(1024)   COMMENT '图片uri',
`item_type` TINYINT(4)   COMMENT '1购买型 2租用型',

 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;

#
#订单主表
#
DROP TABLE IF EXISTS eh_rental_bills;

CREATE TABLE `eh_rental_bills` (
`id` BIGINT(20) NOT NULL  COMMENT 'id',
`rental_site_id` BIGINT(20) NOT NULL  COMMENT 'id',
`rental_uid` BIGINT(20) NULL  COMMENT 'rental user id',
`rental_date` DATE NULL  COMMENT '使用日期',
`start_time` DATETIME NULL  COMMENT '使用开始时间',
`end_time` DATETIME NULL  COMMENT '使用结束时间',
`rental_count` DOUBLE NULL  COMMENT '预约数',
`pay_total_money` DECIMAL(10,2) NULL  COMMENT '总价',
`site_total_money` DECIMAL(10,2) NULL  COMMENT '',
`reserve_money` DECIMAL(10,2) NULL  COMMENT '',
`reserve_time` DATETIME NULL  COMMENT 'reserve time',
`pay_start_time` DATETIME NULL  COMMENT '',
`pay_end_time` DATETIME NULL  COMMENT '',
`pay_time` DATETIME NULL  COMMENT '',
`cancel_time` DATETIME NULL  COMMENT '',
`paid_money` DECIMAL(10,2) NULL  COMMENT '',
`status` TINYINT(4) NULL  COMMENT '0:wait for reserve 1:paid reserve 2:paid all money reserve success 3:wait for final payment 4:unlock reserve fail',
`visible_flag` TINYINT(4) NULL  COMMENT '0:visible 1:unvisible',
`invoice_flag` TINYINT(4) NULL  COMMENT '0:want invocie 1 no need',
`creator_uid` BIGINT(20) NULL  COMMENT '预约人',
`create_time` DATETIME NULL  COMMENT '下单时间',
`operator_uid` BIGINT(20) NULL  COMMENT '',
`operate_time` DATETIME NULL  COMMENT '',
`site_name` VARCHAR(255) NULL  COMMENT '名称',
`use_time` VARCHAR(255) NULL  COMMENT '使用时间',
`vendor_type` VARCHAR(255) NULL  COMMENT '支付方式,10001-支付宝，10002-微信',
`launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id',
`organization_id` BIGINT(20)    COMMENT '所属公司的ID', 
 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;

#
#订单分表-场所订单表
#
DROP TABLE IF EXISTS eh_rental_sites_bills;

CREATE TABLE `eh_rental_sites_bills` (
`id` BIGINT(20) NOT NULL  COMMENT 'id',
`rental_bill_id` BIGINT(20) NULL  COMMENT '',
`rental_site_rule_id` BIGINT(20) NULL  COMMENT '',
`rental_count` DOUBLE NULL  COMMENT '',
`total_money` DECIMAL(10,2) NULL  COMMENT '',
`creator_uid` BIGINT(20) NULL  COMMENT '',
`create_time` DATETIME NULL  COMMENT '',
`operator_uid` BIGINT(20) NULL  COMMENT '',
`operate_time` DATETIME NULL  COMMENT '',
`launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id',

 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;

#
#订单分表-场所订单-对应资源编号表
#
DROP TABLE IF EXISTS eh_rental_sites_bill_numbers;

CREATE TABLE `eh_rental_sites_bill_numbers` (
`id` BIGINT(20) NOT NULL  COMMENT 'id',
`rental_site_bill_id` BIGINT(20) NULL  COMMENT 'fk',
`rental_site_rule_id` BIGINT(20) NULL  COMMENT '场所单元格id',
`site_number` INT  COMMENT '场所编号',
`rental_count` DOUBLE NULL  COMMENT '0.5为半个1为全个',

 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;


#
#订单分表-物品订单表
#
DROP TABLE IF EXISTS eh_rental_items_bills;

CREATE TABLE `eh_rental_items_bills` (
`id` BIGINT(20) NOT NULL  COMMENT 'id',
`rental_bill_id` BIGINT(20) NULL  COMMENT '',
`rental_site_item_id` BIGINT(20) NULL  COMMENT '',
`rental_count` INT(11) NULL  COMMENT '',
`total_money` DECIMAL(10,2) NULL  COMMENT '',
`creator_uid` BIGINT(20) NULL  COMMENT '',
`create_time` DATETIME NULL  COMMENT '',
`operator_uid` BIGINT(20) NULL  COMMENT '',
`operate_time` DATETIME NULL  COMMENT '',
`launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id',


 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;


#
#订单附件表
#
DROP TABLE IF EXISTS eh_rental_bill_attachments;

CREATE TABLE `eh_rental_bill_attachments` (
`id` BIGINT(20) NOT NULL  COMMENT 'id',
`rental_bill_id` BIGINT(20) NULL  COMMENT '',
`attachment_type` TINYINT(4) NULL  COMMENT '0:文本 1:车牌 2:显示内容 3：附件链接',
`content` VARCHAR(500) NULL  COMMENT '',
`file_path` VARCHAR(500) NULL  COMMENT '',
`creator_uid` BIGINT(20) NULL  COMMENT '',
`create_time` DATETIME NULL  COMMENT '',
`operator_uid` BIGINT(20) NULL  COMMENT '',
`operate_time` DATETIME NULL  COMMENT '',
`launch_pad_item_id` BIGINT(20)    COMMENT '广场图标id',

 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;


#
#订单-支付关联表
#
DROP TABLE IF EXISTS eh_rental_bill_paybill_maps;

CREATE TABLE `eh_rental_bill_paybill_maps` (
`id` BIGINT(20) NOT NULL  COMMENT 'id',
`rental_bill_id` BIGINT(20) NULL  COMMENT '',
`online_pay_bill_id` BIGINT(20) NULL  COMMENT '',
`creator_uid` BIGINT(20) NULL  COMMENT '',
`create_time` DATETIME NULL  COMMENT '',
`operator_uid` BIGINT(20) NULL  COMMENT '',
`operate_time` DATETIME NULL  COMMENT '',
`vendor_type` VARCHAR(255) NULL  COMMENT '支付方式,10001-支付宝，10002-微信',

 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;


