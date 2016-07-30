ALTER TABLE `eh_activities` ADD COLUMN `official_flag` TINYINT NULL DEFAULT '0' COMMENT 'whether it is an official activity, 0 not, 1 yes' AFTER `media_url`;

ALTER TABLE `eh_forum_posts` ADD COLUMN `official_flag` TINYINT NULL DEFAULT '0' COMMENT 'whether it is an official activity, 0 not, 1 yes' AFTER `end_time`;


-- 增加是否显示图片一列
ALTER TABLE `eh_forum_posts` ADD COLUMN `media_display_flag` TINYINT(4) NOT NULL DEFAULT '1' COMMENT 'whether display image' AFTER `official_flag`;


-- merge from new-rental-delta-schema.sql 20160728
-- 
-- 保存一个公司的一个场所图标的默认设置
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_default_rules;
CREATE TABLE `eh_rentalv2_default_rules` (
  `id` BIGINT COMMENT 'id',
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',
  `owner_id` BIGINT COMMENT 'community id or organization id',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `rental_start_time` BIGINT COMMENT '最多提前多少时间预定',
  `rental_end_time` BIGINT COMMENT '最少提前多少时间预定',
  `pay_start_time` BIGINT COMMENT '',
  `pay_end_time` BIGINT COMMENT '',
  `payment_ratio` INTEGER COMMENT 'payment ratio',
  `refund_flag` TINYINT COMMENT '是否支持退款: 1-是, 0-否',
  `refund_ratio` INTEGER COMMENT '退款比例',
  `contact_num` VARCHAR(20) COMMENT 'phone number',
  `creator_uid` BIGINT COMMENT '',
  `create_time` DATETIME COMMENT '',
  `operator_uid` BIGINT COMMENT '',
  `operate_time` DATETIME COMMENT '',
  `rental_type` TINYINT COMMENT '0-as hour:min, 1-as half day, 2-as day, 3-支持晚上的半天',
  `cancel_time` BIGINT COMMENT '至少提前取消时间',
  `overtime_time` BIGINT COMMENT '超期时间',
  `exclusive_flag` TINYINT COMMENT '是否为独占资源: 0-否, 1-是',
  `unit` DOUBLE  COMMENT '1-整租, 0.5-可半个租',
  `auto_assign` TINYINT COMMENT '是否动态分配: 1-是, 0-否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所: 1-是, 0-否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段: 1-是, 0-否',
  `cancel_flag` TINYINT COMMENT '是否允许取消: 1-是, 0-否',
  `rental_step` INTEGER COMMENT 'how many time_step must be rental every time',
  `need_pay` TINYINT COMMENT '是否需要支付: 1-是, 0-否', 
  `workday_price` DECIMAL(10,2) COMMENT '工作日价格',
  `weekend_price` DECIMAL(10,2) COMMENT '周末价格',
  `resource_counts` DOUBLE  COMMENT '可预约个数',
  `begin_date` DATE  COMMENT '开始日期',
  `end_date` DATE  COMMENT '结束日期',
  `open_weekday` VARCHAR(7) COMMENT '7位二进制，0000000每一位表示星期7123456',
  `time_step` DOUBLE  COMMENT '步长，每个单元格是多少小时（半小时是0.5）',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 对于按小时预定的场所默认设置，保存时间段
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_resource_numbers;
CREATE TABLE `eh_rentalv2_resource_numbers` (
  `id` BIGINT COMMENT '',
  `owner_id` BIGINT COMMENT '',
  `owner_type` VARCHAR(255)  COMMENT 'EhRentalv2DefaultRules-默认规则,EhRentalv2Resources-具体场所',
  `resource_number` VARCHAR(255) COMMENT '场所编号', 
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 对于按小时预定的场所默认设置，保存时间段
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_time_interval;
CREATE TABLE `eh_rentalv2_time_interval` (
  `id` BIGINT COMMENT '',
  `owner_id` BIGINT COMMENT '',
  `owner_type` VARCHAR(255)  COMMENT '"default_rule","resource_rule"',
  `begin_time` DOUBLE  COMMENT '开始时间-24小时制',
  `end_time` DOUBLE  COMMENT '结束时间-24小时制',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 保存默认设置的关闭时间
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_close_dates;
CREATE TABLE `eh_rentalv2_close_dates` (
  `id` BIGINT COMMENT '',
  `owner_id` BIGINT COMMENT '',
  `owner_type` VARCHAR(255)  COMMENT '"default_rule","resource_rule"',
  `close_date` DATE  COMMENT '',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 场所表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_resources;
CREATE TABLE `eh_rentalv2_resources` (
  `id` BIGINT COMMENT 'id',
  `parent_id` BIGINT  COMMENT '',
  `resource_name` VARCHAR(127) COMMENT '名称：',
  `resource_type2` TINYINT COMMENT '',
  `building_name` VARCHAR(128) COMMENT '',
  `building_id` BIGINT COMMENT '',
  `address` VARCHAR(192) COMMENT '地址',
  `address_id` BIGINT COMMENT '',
  `spec` VARCHAR(255) COMMENT '规格',
  `own_company_name` VARCHAR(60) COMMENT '',
  `contact_name` VARCHAR(40) COMMENT '',
  `contact_phonenum` VARCHAR(20) COMMENT '咨询电话',
  `contact_phonenum2` VARCHAR(20) COMMENT '',
  `contact_phonenum3` VARCHAR(20) COMMENT '',
  `status` TINYINT COMMENT '',
  `creator_uid` BIGINT COMMENT '',
  `create_time` DATETIME COMMENT '',
  `operator_uid` BIGINT COMMENT '',
  `operate_time` DATETIME COMMENT '',
  `introduction` TEXT COMMENT '详情',
  `notice` TEXT COMMENT '',
  `charge_uid` BIGINT  COMMENT '负责人id',
  `cover_uri` VARCHAR(1024)  COMMENT '封面图uri',
  `discount_type` TINYINT COMMENT '折扣信息：0不打折 1满减优惠2比例折扣',
  `full_price` DECIMAL(10,2)  COMMENT '满XX元',
  `cut_price` DECIMAL(10,2)  COMMENT '减XX元',
  `discount_ratio` DOUBLE  COMMENT '折扣比例',
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
  `time_step` DOUBLE COMMENT '按小时预约：最小单元格是多少小时，浮点型',
  `exclusive_flag` TINYINT COMMENT '是否为独占资源0否 1 是',
  `auto_assign` TINYINT COMMENT '是否动态分配 1是 0否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所 1是 0否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段 1是 0否',
  `cancel_flag` TINYINT  COMMENT '是否允许取消 1是 0否',
  `need_pay` TINYINT COMMENT '是否需要支付 1是 0否',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `cancel_time` BIGINT COMMENT '至少提前取消时间',
  `rental_start_time` BIGINT COMMENT '最多提前多少时间预定',
  `rental_end_time` BIGINT COMMENT '最少提前多少时间预定',
  `refund_flag` TINYINT COMMENT '是否支持退款 1是 0否',
  `refund_ratio` INTEGER COMMENT '退款比例',
  `longitude` DOUBLE  COMMENT '地址经度',
  `latitude` DOUBLE  COMMENT '地址纬度',
  `organization_id` BIGINT COMMENT '所属公司的ID',
  `day_begin_time` TIME  COMMENT '对于按小时预定的每天开始时间',
  `day_end_time` TIME  COMMENT '对于按小时预定的每天结束时间',
  `community_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `resource_counts` DOUBLE  COMMENT '可预约个数',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 场所和归属园区的关联表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_resource_ranges;
CREATE TABLE `eh_rentalv2_resource_ranges` (
  `id` BIGINT COMMENT 'id',
  `owner_type` VARCHAR(255)  COMMENT 'owner type : community ; organization',
  `owner_id` BIGINT COMMENT 'community id or organization id',
  `rental_resource_id` BIGINT  COMMENT 'rental_resource id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 保存场所详情图片
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_resource_pics;
CREATE TABLE `eh_rentalv2_resource_pics` (
  `id` BIGINT COMMENT '',
  `owner_id` BIGINT COMMENT '',
  `owner_type` VARCHAR(255)  COMMENT 'EhRentalv2Resources',
  `uri` VARCHAR(1024)  COMMENT '',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 场所设定好的单元格表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_cells;
CREATE TABLE `eh_rentalv2_cells` (
  `id` BIGINT COMMENT 'id',
  `rental_resource_id` BIGINT COMMENT 'rental_resource id',
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
  `amorpm` TINYINT COMMENT '0:am 1:pm 2:night',
  `rental_step` INTEGER COMMENT 'how many time_step must be rental every time',
  `begin_time` DATETIME COMMENT '开始时间 对于按时间定',
  `end_time` DATETIME COMMENT '结束时间 对于按时间定',
  `counts` DOUBLE COMMENT '共多少个',
  `unit` DOUBLE COMMENT '是否支持0.5个',
  `price` DECIMAL(10,2) COMMENT '折后价',
  `resource_rental_date` DATE COMMENT 'which day',
  `status` TINYINT COMMENT 'unuse 0:open 1:closed',
  `creator_uid` BIGINT COMMENT '',
  `create_time` DATETIME COMMENT '',
  `operator_uid` BIGINT COMMENT '',
  `operate_time` DATETIME COMMENT '',
  `time_step` DOUBLE COMMENT '',
  `original_price` DECIMAL(10,2) COMMENT '原价（如果不为null则price为打折价）',
  `exclusive_flag` TINYINT COMMENT '是否为独占资源0否 1 是',
  `auto_assign` TINYINT COMMENT '是否动态分配 1是 0否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所 1是 0否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段 1是 0否',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `resource_number` VARCHAR(100)  COMMENT '场所号',
  `halfresource_price` DECIMAL(10,2) COMMENT '半场折后价',
  `halfresource_original_price` DECIMAL(10,2) COMMENT '半场原价（如果不为null则price为打折价）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 保存场所附件设置
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_config_attachments;
CREATE TABLE `eh_rentalv2_config_attachments` (
  `id` BIGINT COMMENT '',
  `owner_id` BIGINT COMMENT '',
  `owner_type` VARCHAR(255)  COMMENT '"default_rule","resource_rule"',
  `attachment_type` TINYINT COMMENT '',
  `content` TEXT  COMMENT '根据type，这里可能是文本或者附件url',
  `must_options` TINYINT COMMENT '0 非必须 1 必选',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 物品表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_items;
CREATE TABLE `eh_rentalv2_items` (
  `id` BIGINT COMMENT 'id',
  `rental_resource_id` BIGINT COMMENT 'rental_resource id',
  `name` VARCHAR(128) COMMENT '',
  `price` DECIMAL(10,2) COMMENT '',
  `counts` INTEGER COMMENT 'item count',
  `status` TINYINT COMMENT '',
  `creator_uid` BIGINT COMMENT '',
  `create_time` DATETIME COMMENT '',
  `operator_uid` BIGINT COMMENT '',
  `operate_time` DATETIME COMMENT '',
  `default_order` INTEGER COMMENT '排序字段',
  `img_uri` VARCHAR(1024) COMMENT '图片uri',
  `item_type` TINYINT COMMENT '1购买型 2租用型',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 订单主表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_orders;
CREATE TABLE `eh_rentalv2_orders` (
  `id` BIGINT NOT NULL  COMMENT 'id',
  `order_no` VARCHAR(20) NOT NULL COMMENT '订单编号',
  `rental_resource_id` BIGINT NOT NULL  COMMENT 'id',
  `rental_uid` BIGINT NULL  COMMENT 'rental user id',
  `rental_date` DATE NULL  COMMENT '使用日期',
  `start_time` DATETIME NULL  COMMENT '使用开始时间',
  `end_time` DATETIME NULL  COMMENT '使用结束时间',
  `rental_count` DOUBLE NULL  COMMENT '预约数',
  `pay_total_money` DECIMAL(10,2) NULL  COMMENT '总价',
  `resource_total_money` DECIMAL(10,2) NULL  COMMENT '',
  `reserve_money` DECIMAL(10,2) NULL  COMMENT '',
  `reserve_time` DATETIME NULL  COMMENT 'reserve time',
  `pay_start_time` DATETIME NULL  COMMENT '',
  `pay_end_time` DATETIME NULL  COMMENT '',
  `pay_time` DATETIME NULL  COMMENT '',
  `cancel_time` DATETIME NULL  COMMENT '',
  `paid_money` DECIMAL(10,2) NULL  COMMENT '',
  `status` TINYINT NULL  COMMENT '0:wait for reserve 1:paid reserve 2:paid all money reserve success 3:wait for final payment 4:unlock reserve fail',
  `visible_flag` TINYINT NULL  COMMENT '0:visible 1:unvisible',
  `invoice_flag` TINYINT NULL  COMMENT '0:want invocie 1 no need',
  `creator_uid` BIGINT NULL  COMMENT '预约人',
  `create_time` DATETIME NULL  COMMENT '下单时间',
  `operator_uid` BIGINT NULL  COMMENT '',
  `operate_time` DATETIME NULL  COMMENT '',
  `resource_name` VARCHAR(255) NULL  COMMENT '名称',
  `use_detail` VARCHAR(255) NULL  COMMENT '使用时间',
  `vendor_type` VARCHAR(255) NULL  COMMENT '支付方式,10001-支付宝，10002-微信 --多次支付怎木办，估计产品都没想清楚',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `organization_id` BIGINT COMMENT '所属公司的ID', 
  `spec` VARCHAR(255) COMMENT '规格',
  `address` VARCHAR(192) COMMENT '地址',
  `longitude` DOUBLE  COMMENT '地址经度',
  `latitude` DOUBLE  COMMENT '地址纬度',
  `contact_phonenum` VARCHAR(20) COMMENT '咨询电话',
  `introduction` TEXT COMMENT '详情',
  `notice` TEXT COMMENT '',
  `community_id` BIGINT COMMENT '资源所属园区的ID',
  `namespace_id` INTEGER  COMMENT '域空间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 订单分表-场所订单表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_resource_orders;
CREATE TABLE `eh_rentalv2_resource_orders` (
  `id` BIGINT NOT NULL  COMMENT 'id',
  `rental_order_id` BIGINT NULL  COMMENT '',
  `rental_resource_rule_id` BIGINT NULL  COMMENT '',
  `rental_count` DOUBLE NULL  COMMENT '',
  `total_money` DECIMAL(10,2) NULL  COMMENT '',
  `creator_uid` BIGINT NULL  COMMENT '',
  `create_time` DATETIME NULL  COMMENT '',
  `operator_uid` BIGINT NULL  COMMENT '',
  `operate_time` DATETIME NULL  COMMENT '',
  `resource_type_id` BIGINT COMMENT 'resource type id',  
  `price` DECIMAL(10,2) COMMENT '折后价',
  `resource_rental_date` DATE COMMENT 'which day', 
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
  `amorpm` TINYINT COMMENT '0:am 1:pm 2:night',
  `rental_step` INTEGER COMMENT 'how many time_step must be rental every time',
  `begin_time` DATETIME COMMENT '开始时间 对于按时间定',
  `end_time` DATETIME COMMENT '结束时间 对于按时间定',  
  `exclusive_flag` TINYINT COMMENT '是否为独占资源0否 1 是',
  `auto_assign` TINYINT COMMENT '是否动态分配 1是 0否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所 1是 0否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段 1是 0否',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 

-- 
-- 订单分表-物品订单表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_items_orders;
CREATE TABLE `eh_rentalv2_items_orders` (
  `id` BIGINT NOT NULL  COMMENT 'id',
  `rental_order_id` BIGINT NULL  COMMENT '',
  `rental_resource_item_id` BIGINT NULL  COMMENT '',
  `rental_count` INTEGER NULL  COMMENT '',
  `total_money` DECIMAL(10,2) NULL  COMMENT '',
  `creator_uid` BIGINT NULL  COMMENT '',
  `create_time` DATETIME NULL  COMMENT '',
  `operator_uid` BIGINT NULL  COMMENT '',
  `operate_time` DATETIME NULL  COMMENT '',
  `resource_type_id` BIGINT COMMENT 'resource type id',

  `item_name` VARCHAR(128) COMMENT '',
  `img_uri` VARCHAR(1024) COMMENT '',
  `item_type` TINYINT COMMENT '',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 订单附件表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_order_attachments;
CREATE TABLE `eh_rentalv2_order_attachments` (
  `id` BIGINT NOT NULL  COMMENT 'id',
  `rental_order_id` BIGINT NULL  COMMENT '',
  `attachment_type` TINYINT NULL  COMMENT '0:文本 1:车牌 2:显示内容 3：附件链接',
  `content` VARCHAR(500) NULL  COMMENT '',
  `file_path` VARCHAR(500) NULL  COMMENT '',
  `creator_uid` BIGINT NULL  COMMENT '',
  `create_time` DATETIME NULL  COMMENT '',
  `operator_uid` BIGINT NULL  COMMENT '',
  `operate_time` DATETIME NULL  COMMENT '',
  `resource_type_id` BIGINT COMMENT 'resource type id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 订单-支付关联表 
-- 订单可能多次支付
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_order_payorder_map;
CREATE TABLE `eh_rentalv2_order_payorder_map` (
  `id` BIGINT NOT NULL  COMMENT 'id',
  `order_id` BIGINT NULL  COMMENT '',
  `order_no` BIGINT NULL  COMMENT '',
  `creator_uid` BIGINT NULL  COMMENT '',
  `create_time` DATETIME NULL  COMMENT '',
  `operator_uid` BIGINT NULL  COMMENT '',
  `operate_time` DATETIME NULL  COMMENT '',
  `vendor_type` VARCHAR(255) NULL  COMMENT '支付方式,10001-支付宝，10002-微信',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 订单-退款表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_refund_orders;
CREATE TABLE `eh_rentalv2_refund_orders` (
  `id` BIGINT  COMMENT 'id',
  `order_id` BIGINT COMMENT '订单id',
  `refund_order_no` BIGINT COMMENT '退款的refoundOrderNo-服务端退款时候生成',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `order_no` BIGINT COMMENT '支付的orderno-下单时候生成',
  `online_pay_style_no` VARCHAR(20) COMMENT '支付方式,alipay-支付宝,wechat-微信',
  `amount` DECIMAL(10,2)  COMMENT '退款金额',
  `url` VARCHAR(1024) COMMENT '支付宝的退款链接',
  `status` TINYINT COMMENT '退款的状态，和订单状态保持一致',
  `creator_uid` BIGINT COMMENT '',
  `create_time` DATETIME COMMENT '',
  `operator_uid` BIGINT COMMENT '',
  `operate_time` DATETIME COMMENT '',


  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 资源类型表
-- 
-- DROP TABLE IF EXISTS eh_rentalv2_resource_types;
CREATE TABLE `eh_rentalv2_resource_types` (
  `id` BIGINT COMMENT 'id',
  `name` VARCHAR(50) COMMENT '名称',
  `page_type` TINYINT COMMENT '预定展示0代表默认页面DefaultType, 1代表定制页面CustomType',
  `icon_uri` VARCHAR(1024) COMMENT '工作日价格',
  `status` TINYINT COMMENT '状态：0关闭 2开启',
  `namespace_id` INTEGER  COMMENT '域空间',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- merge from ibase-office-delta-schema.sql 20160728
-- 
-- 工位预定空间表
-- 
-- DROP TABLE IF EXISTS `eh_office_cubicle_spaces`;
CREATE TABLE `eh_office_cubicle_spaces` (
  `id` BIGINT  COMMENT 'id',
  `namespace_id` INTEGER  COMMENT '',
  `name` VARCHAR(128)  COMMENT '工位空间名称',
  `province_id` BIGINT   COMMENT '省份id',
  `province_name` VARCHAR(128)  COMMENT '省份名称',
  `city_id` BIGINT   COMMENT '城市id',
  `city_name` VARCHAR(128)  COMMENT '城市名称',
  `cover_uri` VARCHAR(1024)  COMMENT '封面图片',
  `address` VARCHAR(1024)  COMMENT '地址',
  `longitude` DOUBLE  COMMENT '经度',
  `latitude` DOUBLE  COMMENT '纬度',
  `geohash` VARCHAR(32)  COMMENT '',
  `contact_phone` VARCHAR(32)  COMMENT '咨询电话',
  `manager_uid` BIGINT   COMMENT '负责人uid',
  `description` TEXT  COMMENT '详情-html片',
  `status` TINYINT  COMMENT '状态：2-正常 ,0-不可用',
  `creator_uid` BIGINT   COMMENT '',
  `create_time` DATETIME   COMMENT '',
  `operator_uid` BIGINT   COMMENT '',
  `operate_time` DATETIME   COMMENT '',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
  
-- 
-- 工位预定  附件-banner图表
-- 
-- DROP TABLE IF EXISTS `eh_office_cubicle_attachments`;
CREATE TABLE `eh_office_cubicle_attachments` (  
  `id` BIGINT  COMMENT 'id',
  `owner_id` BIGINT  COMMENT '工位空间id',
  `content_type` VARCHAR(32)  COMMENT '内容类型',
  `content_uri` VARCHAR(1024)  COMMENT 'uri',
  `creator_uid` BIGINT   COMMENT '',
  `create_time` DATETIME   COMMENT '',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;  

-- 
-- 工位预定 出租空间表
-- 
-- DROP TABLE IF EXISTS `eh_office_cubicle_categories`;
CREATE TABLE `eh_office_cubicle_categories` ( 
  `id` BIGINT  COMMENT 'id',
  `namespace_id` INTEGER  COMMENT '',
  `space_id` BIGINT  COMMENT '工位空间id',
  `rent_type` TINYINT  COMMENT '租赁类别:1-开放式（默认space_type 1）,2-办公室',
  `space_type` TINYINT  COMMENT '空间类别:1-工位,2-面积',
  `space_size` INTEGER  COMMENT '工位数或面积数',
  `creator_uid` BIGINT   COMMENT '',
  `create_time` DATETIME   COMMENT '',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;  

-- 
-- 工位预定 订单表
-- 
-- DROP TABLE IF EXISTS `eh_office_cubicle_orders`;
CREATE TABLE `eh_office_cubicle_orders` ( 
  `id` BIGINT  COMMENT 'id',
  `namespace_id` INTEGER  COMMENT '',
  `space_id` BIGINT  COMMENT '工位空间id',
  `space_name` VARCHAR(128)  COMMENT '工位空间名称',
  `province_id` BIGINT   COMMENT '省份id',
  `province_name` VARCHAR(100)  COMMENT '省份名称',
  `city_id` BIGINT   COMMENT '城市id',
  `city_name` VARCHAR(128)  COMMENT '城市名称',
  `cover_uri` VARCHAR(1024)  COMMENT '封面图片',
  `address` VARCHAR(1024)  COMMENT '地址',
  `longitude` DOUBLE  COMMENT '经度',
  `latitude` DOUBLE  COMMENT '纬度',
  `geohash` VARCHAR(32)  COMMENT '',
  `contact_phone` VARCHAR(32)  COMMENT '咨询电话',
  `manager_uid` BIGINT   COMMENT '负责人uid',
  `description` TEXT  COMMENT '详情-html片',
  `rent_type` TINYINT  COMMENT '租赁类别:1-开放式（默认space_type 1）,2-办公室',
  `space_type` TINYINT  COMMENT '空间类别:1-工位,2-面积',
  `space_size` VARCHAR(32)  COMMENT '工位数或面积数',
  `status` TINYINT  COMMENT '状态:2-用户可见,0-用户不可见',
  `order_type` TINYINT  COMMENT '预定类别：1-参观 2-预定',
  `reserver_uid` BIGINT   COMMENT '预订人uid',
  `reserve_time` DATETIME  COMMENT '预定时间',
  `reserver_name` VARCHAR(64)  COMMENT '预订人姓名',
  `reserve_contact_token` VARCHAR(32)  COMMENT '预定联系方式',
  `reserve_enterprise` VARCHAR(512)  COMMENT '预订人公司',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 添加字段


 ALTER TABLE  eh_rentalv2_refund_orders ADD COLUMN  `refund_flag` TINYINT COMMENT '是否支持退款 1是 0否';
 ALTER TABLE  eh_rentalv2_refund_orders ADD COLUMN  `refund_ratio` INTEGER COMMENT '退款比例';
 
 ALTER TABLE  eh_rentalv2_refund_orders ADD COLUMN  `cancel_flag` TINYINT  COMMENT '是否允许取消 1是 0否';