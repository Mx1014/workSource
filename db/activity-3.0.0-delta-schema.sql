-- 活动增加支付相关字段 add by yanjun 20170502
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_flag` TINYINT(4) DEFAULT '0' NULL COMMENT '0: no pay, 1:have pay, 2:tobepay, 3:refund';
ALTER TABLE `eh_activity_roster` ADD COLUMN `order_no` BIGINT(20) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `order_start_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `vendor_type` VARCHAR(32) NULL COMMENT '10001: alipay, 10002: wechatpay';
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_amount` INT(11) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_order_no` BIGINT(20) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_amount` INT(11) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `status` TINYINT(4) DEFAULT '2' NULL COMMENT '0: cancel, 1: reject, 2:normal';

ALTER TABLE `eh_activities` ADD COLUMN `charge_flag` TINYINT(4) DEFAULT '0' NULL COMMENT '0: no charge, 1: charge'; 
ALTER TABLE `eh_activities` ADD COLUMN `charge_price` INT(11) NULL COMMENT 'charge_price';


