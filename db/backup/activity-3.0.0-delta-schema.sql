-- 活动增加支付相关字段 add by yanjun 20170502
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_flag` TINYINT(4) DEFAULT '0' NULL COMMENT '0: no pay, 1:have pay, 2:refund';
ALTER TABLE `eh_activity_roster` ADD COLUMN `order_no` BIGINT(20) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `order_start_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `order_expire_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `vendor_type` VARCHAR(32) NULL COMMENT '10001: alipay, 10002: wechatpay';
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_amount` DECIMAL(10, 2) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_order_no` BIGINT(20) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_amount` DECIMAL(10, 2) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `status` TINYINT(4) DEFAULT '2' NULL COMMENT '0: cancel, 1: reject, 2:normal';
ALTER TABLE `eh_activity_roster` ADD COLUMN `organization_id` BIGINT(20) NULL;

ALTER TABLE `eh_activities` ADD COLUMN `charge_flag` TINYINT(4) DEFAULT '0' NULL COMMENT '0: no charge, 1: charge'; 
ALTER TABLE `eh_activities` ADD COLUMN `charge_price` DECIMAL(10, 2) NULL COMMENT 'charge_price';


-- 订单过期时间的设置表  add by yanjun 20170502
CREATE TABLE `eh_roster_order_settings` (
   `id` BIGINT(20) NOT NULL,
   `namespace_id` INT(11) NOT NULL COMMENT 'namespace id',
   `time` BIGINT(20) DEFAULT NULL COMMENT 'millisecond',
   `create_time` DATETIME DEFAULT NULL,
   `creator_uid` BIGINT(20) DEFAULT NULL,
   `update_time` DATETIME DEFAULT NULL,
   `operator_uid` BIGINT(20) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 增加取消时间，用于统计  add by yanjun 20170519
ALTER TABLE `eh_activity_roster` ADD COLUMN `cancel_time` DATETIME NULL;

-- 在eh_activity_categories增加一个实际id，现在要将入口数据添加到此表。不同的namespace会用重复   add by yanjun 20170523
ALTER TABLE `eh_activity_categories` ADD COLUMN `entry_id` BIGINT(20) NOT NULL COMMENT 'entry id, Differ from each other\n in the same namespace' AFTER `owner_id`;
