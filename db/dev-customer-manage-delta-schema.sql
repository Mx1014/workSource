-- 
-- 考勤时间管理
-- 
-- DROP TABLE IF EXISTS `eh_punch_time_rules`;
CREATE TABLE `eh_punch_time_rules` (
  `id` BIGINT COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'time rule name ',
  `start_early_time` TIME COMMENT 'how early can i arrive',
  `start_late_time` TIME COMMENT 'how late can i arrive ',
  `work_time` TIME COMMENT 'how long do i must be work',
  `noon_leave_time` TIME ,
  `afternoon_arrive_time` TIME ,
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT 'how many times should punch everyday :2/4',
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;


-- 
-- 具体打卡地点
-- 

ALTER TABLE `eh_punch_geopoints` ADD COLUMN `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type';
ALTER TABLE `eh_punch_geopoints` ADD COLUMN `owner_id` BIGINT COMMENT 'owner resource(user/organization) id';
ALTER TABLE `eh_punch_geopoints` ADD COLUMN `location_rule_id` BIGINT COMMENT 'fk:eh_punch_geopoints id'; 


-- 
-- 考勤地点表
-- 
-- DROP TABLE IF EXISTS `eh_punch_location_rules`;
CREATE TABLE `eh_punch_location_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'location rule name ',
  `description` VARCHAR(256) ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 具体wifi列表
-- 
-- DROP TABLE IF EXISTS `eh_punch_wifis`;
CREATE TABLE `eh_punch_wifis` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `wifi_rule_id` BIGINT COMMENT 'fk:eh_punch_wifi_rules id', 
  `ssid` VARCHAR(64) COMMENT 'wifi ssid', 
  `mac_address` VARCHAR(32) COMMENT 'wifi mac address', 
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 考勤wifi表
-- 
-- DROP TABLE IF EXISTS `eh_punch_wifi_rules`;
CREATE TABLE `eh_punch_wifi_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `description` VARCHAR(256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;


-- 
-- 假期表
-- 
-- DROP TABLE IF EXISTS `eh_punch_holidays`;
CREATE TABLE `eh_punch_holidays` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `workday_rule_id` BIGINT COMMENT 'fk:eh_punch_workday_rules id', 
  `status` TINYINT COMMENT 'its holiday or workday:0-workday ; 1-holiday',
  `rule_date` DATE COMMENT 'date',
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 打卡排班表
-- 
-- DROP TABLE IF EXISTS `eh_punch_workday_rules`;
CREATE TABLE `eh_punch_workday_rules` (
  `id` BIGINT NOT NULL COMMENT 'id', 
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `description` VARCHAR(256) ,
  `work_week_dates` VARCHAR(8) DEFAULT '0000000' COMMENT '7位，从左至右每一位表示星期7123456,值:0-关闭 1-开放 example:周12345上班[0111110]',
  `creator_uid` BIGINT ,
  `create_time` DATETIME , 
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 打卡总规则表
-- 
DROP TABLE IF EXISTS `eh_punch_rules`;
CREATE TABLE `eh_punch_rules` (
  `id` BIGINT NOT NULL COMMENT 'id', 
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `time_rule_id` BIGINT COMMENT 'fk:eh_punch_time_rules id',
  `location_rule_id` BIGINT COMMENT 'fk:eh_punch_geopoints id',
  `wifi_rule_id` BIGINT COMMENT 'fk:eh_punch_wifi_rules id',
  `workday_rule_id` BIGINT COMMENT 'fk:eh_punch_workday_rules id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;
 


-- 
-- 打卡规则和owner的映射表
-- 
-- DROP TABLE IF EXISTS `eh_punch_rule_owner_map`;
CREATE TABLE `eh_punch_rule_owner_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `target_type` VARCHAR(128) COMMENT 'target resource(user/organization) type',
  `target_id` BIGINT COMMENT 'target resource(user/organization) id',
  `punch_rule_id` BIGINT COMMENT 'fk:eh_punch_rules id',
  `review_rule_id` BIGINT COMMENT 'fk:review rule id',
  `description` VARCHAR(256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;




-- 
-- 打卡统计表-个人报表-每日生成
-- 
-- DROP TABLE IF EXISTS `eh_punch_statistics`;
CREATE TABLE `eh_punch_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `punch_month` VARCHAR(8) COMMENT 'yyyymm',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `user_id` BIGINT COMMENT 'user id',
  `user_name` VARCHAR(128) COMMENT 'user name',
  `dept_id` BIGINT COMMENT 'user department id',
  `dept_name` VARCHAR(128) COMMENT 'user department name',
  `work_day_count` INTEGER COMMENT '应上班天数',
  `work_count` INTEGER COMMENT '实际上班天数',
  `belate_count` INTEGER COMMENT '迟到次数',
  `leave_early_count` INTEGER COMMENT '早退次数',
  `blandle_count` INTEGER COMMENT '迟到且早退次数',
  `unpunch_count` DOUBLE COMMENT '缺勤天数',
  `absence_count` DOUBLE COMMENT '事假天数',
  `sick_count` DOUBLE COMMENT '病假天数',
  `exchange_count` DOUBLE COMMENT '调休天数',
  `outwork_count` DOUBLE COMMENT '公出天数',
  `over_time_sum` BIGINT COMMENT '加班累计(非工作日上班)',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT 'how many times should punch everyday :2/4',
  `exception_status` TINYINT COMMENT '异常状态: 0-正常;1-异常',
  `description` VARCHAR(256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;



-- 打卡每日统计表应该加入异常状态的字段 

ALTER TABLE `eh_punch_day_logs` ADD COLUMN `exception_status` TINYINT COMMENT '异常状态: 0-正常;1-异常';

-- DROP TABLE IF EXISTS `eh_community_services`;
CREATE TABLE `eh_community_services` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
  `scope_id` BIGINT,
  `item_name` VARCHAR(32),
  `item_label` VARCHAR(64),
  `icon_uri` VARCHAR(1024),
  `action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_users ADD COLUMN `namespace_user_type` varchar(128) DEFAULT NULL COMMENT 'the type of user';

ALTER TABLE eh_organizations ADD COLUMN `namespace_organization_token` varchar(256) DEFAULT NULL COMMENT 'the token from third party';

ALTER TABLE eh_organizations ADD COLUMN `namespace_organization_type` varchar(128) DEFAULT NULL COMMENT 'the type of organization';


-- 结算表  by sfyan 2016010
-- 订单交易流水表
CREATE TABLE `eh_stat_orders` (
  `id` bigint(20) NOT NULL,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` int(11) DEFAULT 0,
  `order_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `payer_uid` bigint(20) COMMENT '支付用户编号',
  `item_code` varchar(64) DEFAULT null COMMENT '商品编号',
  `vendor_code` varchar(64) DEFAULT null COMMENT '供应商编号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `order_type` varchar(64) DEFAULT NULL COMMENT '订单类型  transaction refund',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单金额',
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `shop_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1-platform shop,2-self shop',
  `order_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算交易流水表
CREATE TABLE `eh_stat_transactions` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `item_code` varchar(64) DEFAULT null COMMENT '商品编号',
  `vendor_code` varchar(64) DEFAULT null COMMENT '供应商编号',
  `payer_uid` bigint(20) COMMENT '支付用户编号',
  `transaction_no` varchar(100) DEFAULT NULL COMMENT '平台流水号',
  `vendor_transaction_no` varchar(100) DEFAULT NULL COMMENT '第三方支付流水号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单金额',
  `paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '支付金额',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `paid_account` varchar(100) DEFAULT NULL COMMENT '第三方支付账号 ',
  `paid_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '支付类型 二维码支付 等。。 ',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '结算金额',
  `paid_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付状态',
  `paid_time` DATETIME DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算退款流水表
CREATE TABLE `eh_stat_refunds` (
  `id` bigint(20) NOT NULL,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` int(11) DEFAULT 0,
  `refund_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `payer_uid` bigint(20) COMMENT '支付用户编号',
  `refund_no` varchar(100) DEFAULT NULL COMMENT '平台退款流水号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `vendor_refund_no` varchar(100) DEFAULT NULL COMMENT'第三方退款流水号',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单金额',
  `refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款金额',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `refund_time` DATETIME DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算支付退款详情表
CREATE TABLE `eh_stat_settlements` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单总金额',
  `paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总金额',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `paid_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '交易总笔数',
  `refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款总金额',
  `refund_fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款费率',
  `refund_fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款总手续费',
  `refund_settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款总结算金额，退款总金额-退款总手续费',
  `refund_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '退款总笔数',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算統計結果表
CREATE TABLE `eh_stat_service_settlement_results` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `alipay_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '支付寶支付金額',
  `alipay_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '支付寶退款金額',
  `wechat_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '微信支付金額',
  `wechat_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '微信退款金額',
  `payment_card_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '一卡通交易金額',
  `payment_card_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '一卡通退款金額',
  `total_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总金额',
  `total_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总金额',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_stat_task_logs` (
  `id` bigint(20) NOT NULL,
  `task_no` varchar(20) NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '10 同步物业缴费订单到结算订单表 20 同步电商订单订单到结算订单表 30 同步停车充值订单到结算订单表 40 同步一卡通订单到结算订单表 50 同步支付平台交易流水到结算交易流水表 60 同步一卡通交易流水到结算交易流水表 70 同步支付平台退款流水到结算退款流水表 80 同步一卡通退款流水到结算退款流水表 90 生成结算数据 100 生成结算数据结果 110 完成',
  `islock` tinyint(4) DEFAULT '0' COMMENT '0 未锁 1 有锁',
  `update_Time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_no` (`task_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 园区电子报表
-- DROP TABLE IF EXISTS `eh_journals`;
CREATE TABLE `eh_journals` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0, 

  `title` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'title',
  `content_type` TINYINT NOT NULL DEFAULT 1 COMMENT ' 1:link ',
  `content` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'link',

  `cover_uri` VARCHAR(1024) COMMENT 'cover file uri',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,

  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive,1:active ',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_journal_configs`;
CREATE TABLE `eh_journal_configs` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0, 

  `description` TEXT  COMMENT 'description',
  `poster_path` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT 'poster_path',

  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 停车充值费率添加卡类型
ALTER TABLE `eh_parking_recharge_rates` ADD COLUMN `card_type` VARCHAR(128);

-- 设备巡检表结构 add by xiongying 20160812
-- 1、参考标准表：
-- DROP TABLE IF EXISTS `eh_equipment_inspection_standards`;
CREATE TABLE `eh_equipment_inspection_standards` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `standard_number` VARCHAR(128),
  `name` VARCHAR(1024),
  `standard_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: routing inspection, 2:maintain',
  `repeat_setting_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_repeat_settings',
  `description` TEXT COMMENT 'content data',
  `remarks` TEXT,
  `standard_source` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: not completed, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2、设备表：
-- DROP TABLE IF EXISTS `eh_equipment_inspection_equipments`;
CREATE TABLE `eh_equipment_inspection_equipments` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the equipment, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the equipment, etc',
  `target_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(1024),
  `manufacturer` VARCHAR(1024),
  `equipment_model` VARCHAR(1024),
  `category_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_categories',
  `category_path` VARCHAR(128) DEFAULT NULL COMMENT 'refernece to the path of eh_categories',
  `location` VARCHAR(1024),
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(64),
  `qr_code_token` TEXT,
  `qr_code_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: incomplete(不完整), 2: in use(使用中), 3: in maintenance(维修中), 4: discarded(报废), 5: disabled(停用), 6: standby(备用)',
  `installation_time` DATETIME,
  `repair_time` DATETIME,
  `initial_asset_value` VARCHAR(128),
  `remarks` TEXT,
  `manager` VARCHAR(128),
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `standard_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `review_status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waiting for approval, 2: reviewed，3: delete',
  `review_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified',
  `reviewer_uid` BIGINT NOT NULL DEFAULT '0',
  `review_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3、备品备件表：
-- DROP TABLE IF EXISTS `eh_equipment_inspection_accessories`;
CREATE TABLE `eh_equipment_inspection_accessories` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the spare parts, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the accessory, etc',
  `target_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(1024),
  `manufacturer` VARCHAR(1024),
  `model_number` VARCHAR(1024),
  `specification` VARCHAR(1024),
  `location` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4、设备-备件 关联表：equipment_id和accessory_id共同确立一条记录
-- DROP TABLE IF EXISTS `eh_equipment_inspection_accessory_map`;
CREATE TABLE `eh_equipment_inspection_accessory_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `equipment_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
  `accessory_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_accessories',
  `quantity` INTEGER NOT NULL DEFAULT '0', 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5、设备参数表
-- DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_parameters`;
CREATE TABLE `eh_equipment_inspection_equipment_parameters` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `equipment_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
  `parameter_name` VARCHAR(128),
  `parameter_unit` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6、设备操作图示表 attachments 及说明书  type区分
-- DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_attachments`;
CREATE TABLE `eh_equipment_inspection_equipment_attachments` (
     `id` BIGINT NOT NULL COMMENT 'id of the record',
     `equipment_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
     `attachment_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: tu shi, 2: shuo ming shu',
     `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
     `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
     `creator_uid` BIGINT NOT NULL DEFAULT 0,
     `create_time` DATETIME,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7、任务表
-- DROP TABLE IF EXISTS `eh_equipment_inspection_tasks`;
CREATE TABLE `eh_equipment_inspection_tasks` (
     `id` BIGINT NOT NULL COMMENT 'id',
     `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
     `owner_id` BIGINT NOT NULL DEFAULT 0,
     `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_standards',
     `equipment_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
     `task_number` VARCHAR(128),
     `task_name` VARCHAR(1024),
     `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '0: parent task, others children-task',
     `child_count` BIGINT NOT NULL DEFAULT 0,
     `executive_group_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, etc',
     `executive_group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
     `executive_start_time` DATETIME,
     `executive_expire_time` DATETIME,
     `executive_time` DATETIME,
     `executor_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
     `executor_id` BIGINT NOT NULL DEFAULT 0,
     `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
     `operator_id` BIGINT NOT NULL DEFAULT 0,
     `process_expire_time` DATETIME,
     `process_time` DATETIME,
     `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: waiting for executing, 2: waiting for maintenance, 3: in maintenance, 4: closed',
     `result` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: complete ok, 2: complete delay, 3: need maintenance ok, 4: need maintenance delay, 5：need maintenance ok complete ok, 6: need maintenance ok complete delay, 7: need maintenance delay complete ok, 8: need maintenance delay complete delay',
     `reviewer_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who review the task, organization, etc',
     `reviewer_id`  BIGINT NOT NULL DEFAULT 0,
     `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
     `review_time` DATETIME,
     `create_time` DATETIME,
     PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
    
-- 8、任务attachments表
-- DROP TABLE IF EXISTS `eh_equipment_inspection_task_attachments`;
CREATE TABLE `eh_equipment_inspection_task_attachments` (
     `id` BIGINT NOT NULL COMMENT 'id',
     `log_id` BIGINT NOT NULL COMMENT 'id of the eh_equipment_inspection_task_logs',
     `task_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_tasks',
     `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
     `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
     `creator_uid` BIGINT NOT NULL DEFAULT 0,
     `create_time` DATETIME,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9、记录表
-- DROP TABLE IF EXISTS `eh_equipment_inspection_task_logs`;
CREATE TABLE `eh_equipment_inspection_task_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `task_id` BIGINT NOT NULL DEFAULT '0',
  `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who operates the task, USER, etc',
  `operator_id` BIGINT NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who is the target of processing the task, USER, etc',
  `target_id` BIGINT NOT NULL DEFAULT '0',
  `process_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: complete, 2: complete maintenance, 3: review, 4: need maintenance ',
  `process_end_time` DATETIME DEFAULT NULL,
  `process_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: complete ok, 2: complete delay, 3: need maintenance ok, 4: need maintenance delay, 5：need maintenance ok complete ok, 6: need maintenance ok complete delay, 7: need maintenance delay complete ok, 8: need maintenance delay complete delay, 9: review qualified, 10: review unqualified',
  `process_message` TEXT,
  `parameter_value` VARCHAR(1024),
  `process_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 富文本
-- DROP TABLE IF EXISTS `eh_rich_texts`;
CREATE TABLE `eh_rich_texts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT '0',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(128) COMMENT '',
  `resource_type` VARCHAR(128) COMMENT 'about, introduction, agreement', 
  `content_type` VARCHAR(128) COMMENT 'richtext, link', 
  `content` TEXT,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 给eh_organization_owners增加一些客户资料管理增加的字段       by xq.tian
--
ALTER TABLE `eh_organization_owners` ADD COLUMN `registered_residence` VARCHAR(128) COMMENT 'registered residence';
ALTER TABLE `eh_organization_owners` ADD COLUMN `org_owner_type_id` BIGINT COMMENT '业主类型id';
ALTER TABLE `eh_organization_owners` ADD COLUMN `gender` TINYINT COMMENT '男,女';
ALTER TABLE `eh_organization_owners` ADD COLUMN `birthday` DATE COMMENT 'birthday';
ALTER TABLE `eh_organization_owners` ADD COLUMN `marital_status` VARCHAR(10);
ALTER TABLE `eh_organization_owners` ADD COLUMN `job` VARCHAR(10) COMMENT 'job';
ALTER TABLE `eh_organization_owners` ADD COLUMN `company` VARCHAR(100) COMMENT 'company';
ALTER TABLE `eh_organization_owners` ADD COLUMN `id_card_number` VARCHAR(18) COMMENT 'id card number';
ALTER TABLE `eh_organization_owners` ADD COLUMN `avatar` VARCHAR(128) COMMENT 'avatar';
ALTER TABLE `eh_organization_owners` ADD COLUMN `status` TINYINT COMMENT 'delete: 0, normal: 1';
ALTER TABLE `eh_organization_owners`  MODIFY COLUMN `address_id`  bigint(20) NULL COMMENT 'address id';

--
-- 创建eh_organization_owner_cars表,汽车管理的汽车表    by xq.tian
--
CREATE TABLE `eh_organization_owner_cars` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `brand` VARCHAR(20) COMMENT '汽车品牌',
  `parking_space` VARCHAR(20) COMMENT '停车位',
  `parking_type` TINYINT COMMENT '停车类型',
  `plate_number` VARCHAR(20) COMMENT '车牌号',
  `contacts` VARCHAR(20) COMMENT '联系人姓名',
  `contact_number` VARCHAR(20) COMMENT '联系电话',
  `content_uri` VARCHAR(20) COMMENT '照片uri',
  `color` VARCHAR(20) COMMENT '汽车颜色',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 创建eh_organization_owner与eh_address的多对多表    by xq.tian
--
CREATE TABLE `eh_organization_owner_address` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `address_id` BIGINT,
  `living_status` TINYINT,
  `auth_type` TINYINT COMMENT 'Auth type',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 创建eh_organization_owner_owner_car与eh_organization_owner_cars的多对多表    by xq.tian
--
CREATE TABLE `eh_organization_owner_owner_car` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `car_id` BIGINT,
  `primary_flag` TINYINT COMMENT 'primary flag, yes: 1, no: 0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 客户资料管理中的附件上传记录表    by xq.tian
--
CREATE TABLE `eh_organization_owner_attachments` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(256) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 车辆管理中的附件上传记录表    by xq.tian
--
CREATE TABLE `eh_organization_owner_car_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'car id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(256) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 客户的活动记录表   by xq.tian
--
CREATE TABLE `eh_organization_owner_behaviors` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `address_id` BIGINT COMMENT 'address id',
  `behavior_type` VARCHAR(20) COMMENT '迁入, 迁出..',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `behavior_time` DATETIME,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 客户类型表    by xq.tian
--
CREATE TABLE `eh_organization_owner_type` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(20) COMMENT 'owner, tenant, relative, friend',
  `display_name` VARCHAR(20) COMMENT '业主, 租户, 亲戚, 朋友',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;