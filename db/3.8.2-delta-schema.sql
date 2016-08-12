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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_users ADD COLUMN `namespace_user_type` VARCHAR(128) DEFAULT NULL COMMENT 'the type of user';

ALTER TABLE eh_organizations ADD COLUMN `namespace_organization_token` VARCHAR(256) DEFAULT NULL COMMENT 'the token from third party';

ALTER TABLE eh_organizations ADD COLUMN `namespace_organization_type` VARCHAR(128) DEFAULT NULL COMMENT 'the type of organization';


-- 结算表  by sfyan 2016010
-- 订单交易流水表
CREATE TABLE `eh_stat_orders` (
  `id` BIGINT(20) NOT NULL,
  `community_id` BIGINT(20) DEFAULT 0,
  `namespace_id` INT(11) DEFAULT 0,
  `order_date` VARCHAR(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) DEFAULT NULL COMMENT '来源实体店ID',
  `payer_uid` BIGINT(20) COMMENT '支付用户编号',
  `item_code` VARCHAR(64) DEFAULT NULL COMMENT '商品编号',
  `vendor_code` VARCHAR(64) DEFAULT NULL COMMENT '供应商编号',
  `order_no` VARCHAR(100) DEFAULT NULL COMMENT '订单号',
  `order_type` VARCHAR(64) DEFAULT NULL COMMENT '订单类型  transaction refund',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单金额',
  `status` TINYINT(4) NOT NULL DEFAULT '0',
  `shop_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '1-platform shop,2-self shop',
  `order_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 结算交易流水表
CREATE TABLE `eh_stat_transactions` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) DEFAULT 0,
  `community_id` BIGINT(20) DEFAULT 0,
  `paid_date` VARCHAR(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` VARCHAR(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) DEFAULT NULL COMMENT '来源实体店ID',
  `item_code` VARCHAR(64) DEFAULT NULL COMMENT '商品编号',
  `vendor_code` VARCHAR(64) DEFAULT NULL COMMENT '供应商编号',
  `payer_uid` BIGINT(20) COMMENT '支付用户编号',
  `transaction_no` VARCHAR(100) DEFAULT NULL COMMENT '平台流水号',
  `vendor_transaction_no` VARCHAR(100) DEFAULT NULL COMMENT '第三方支付流水号',
  `order_no` VARCHAR(100) DEFAULT NULL COMMENT '订单号',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单金额',
  `paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '支付金额',
  `paid_channel` TINYINT(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `paid_account` VARCHAR(100) DEFAULT NULL COMMENT '第三方支付账号 ',
  `paid_type` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '支付类型 二维码支付 等。。 ',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '结算金额',
  `paid_status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '支付状态',
  `paid_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 结算退款流水表
CREATE TABLE `eh_stat_refunds` (
  `id` BIGINT(20) NOT NULL,
  `community_id` BIGINT(20) DEFAULT 0,
  `namespace_id` INT(11) DEFAULT 0,
  `refund_date` VARCHAR(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` VARCHAR(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) DEFAULT NULL COMMENT '来源实体店ID',
  `paid_channel` TINYINT(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `payer_uid` BIGINT(20) COMMENT '支付用户编号',
  `refund_no` VARCHAR(100) DEFAULT NULL COMMENT '平台退款流水号',
  `order_no` VARCHAR(100) DEFAULT NULL COMMENT '订单号',
  `vendor_refund_no` VARCHAR(100) DEFAULT NULL COMMENT'第三方退款流水号',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单金额',
  `refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款金额',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `refund_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 结算支付退款详情表
CREATE TABLE `eh_stat_settlements` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) DEFAULT 0,
  `community_id` BIGINT(20) DEFAULT 0,
  `paid_date` VARCHAR(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` VARCHAR(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) DEFAULT NULL COMMENT '来源实体店ID',
  `paid_channel` TINYINT(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单总金额',
  `paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总金额',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `paid_count` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '交易总笔数',
  `refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款总金额',
  `refund_fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款费率',
  `refund_fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款总手续费',
  `refund_settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款总结算金额，退款总金额-退款总手续费',
  `refund_count` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '退款总笔数',
  `update_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 结算統計結果表
CREATE TABLE `eh_stat_service_settlement_results` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) DEFAULT 0,
  `community_id` BIGINT(20) DEFAULT 0,
  `paid_date` VARCHAR(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` VARCHAR(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` VARCHAR(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) DEFAULT NULL COMMENT '来源实体店ID',
  `alipay_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '支付寶支付金額',
  `alipay_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '支付寶退款金額',
  `wechat_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '微信支付金額',
  `wechat_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '微信退款金額',
  `payment_card_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '一卡通交易金額',
  `payment_card_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '一卡通退款金額',
  `total_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总金额',
  `total_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总金额',
  `update_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_stat_task_logs` (
  `id` BIGINT(20) NOT NULL,
  `task_no` VARCHAR(20) NOT NULL,
  `status` TINYINT(4) DEFAULT NULL COMMENT '10 同步物业缴费订单到结算订单表 20 同步电商订单订单到结算订单表 30 同步停车充值订单到结算订单表 40 同步一卡通订单到结算订单表 50 同步支付平台交易流水到结算交易流水表 60 同步一卡通交易流水到结算交易流水表 70 同步支付平台退款流水到结算退款流水表 80 同步一卡通退款流水到结算退款流水表 90 生成结算数据 100 生成结算数据结果 110 完成',
  `islock` TINYINT(4) DEFAULT '0' COMMENT '0 未锁 1 有锁',
  `update_Time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_no` (`task_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- 园区电子报表
DROP TABLE IF EXISTS `eh_journals`;
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_journal_configs`;
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- 停车充值费率添加卡类型
ALTER TABLE `eh_parking_recharge_rates` ADD COLUMN `card_type` VARCHAR(128);

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