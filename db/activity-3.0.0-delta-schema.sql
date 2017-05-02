-- 活动增加支付相关字段 add by yanjun 20170502
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_flag` TINYINT(4) DEFAULT '0' NULL COMMENT '0: no pay, 1:have pay';
ALTER TABLE `eh_activities` ADD COLUMN `charge_flag` TINYINT(4) DEFAULT '0' NULL COMMENT '0: no charge, 1: charge'; 
ALTER TABLE `eh_activities` ADD COLUMN `charge_price` INT(11) NULL COMMENT 'charge_price';