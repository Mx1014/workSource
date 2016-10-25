ALTER TABLE eh_parking_lots ADD COLUMN `max_request_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the request card';

ALTER TABLE eh_parking_lots ADD COLUMN `tempfee_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'is support temp fee';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: monthly, 2: temporary';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `order_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be from 3rd system';

ALTER TABLE eh_parking_lots ADD COLUMN `rate_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'is support add or delete rate';

-- 通讯录增加 隐藏 显示字段 by sfyan 20161010
ALTER TABLE `eh_organization_members` ADD COLUMN `visible_flag` TINYINT(4) DEFAULT 0 COMMENT '0 show 1 hide';

-- DROP TABLE IF EXISTS `eh_region_codes`;
CREATE TABLE `eh_region_codes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL COMMENT 'region name',
  `code` INTEGER NOT NULL COMMENT 'region code',
  `pinyin` VARCHAR(256) NOT NULL COMMENT 'region name pinyin',
  `first_letter` CHAR(2) NOT NULL COMMENT 'region name pinyin first letter',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active', 
  `hot_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: no, 1: yes', 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 增加区号 by sfyan 20161012
ALTER TABLE `eh_user_identifiers` ADD COLUMN `region_code` INTEGER DEFAULT 86 COMMENT 'region code 86 852';

-- 结算增加统计笔数 by sfyan 20161017
ALTER TABLE `eh_stat_service_settlement_results` ADD COLUMN `alipay_paid_count` BIGINT DEFAULT 0 COMMENT '支付宝消费笔数';
ALTER TABLE `eh_stat_service_settlement_results` ADD COLUMN `alipay_refund_count` BIGINT DEFAULT 0 COMMENT '支付宝退款笔数';
ALTER TABLE `eh_stat_service_settlement_results` ADD COLUMN `wechat_paid_count` BIGINT DEFAULT 0 COMMENT '微信消费笔数';
ALTER TABLE `eh_stat_service_settlement_results` ADD COLUMN `wechat_refund_count` BIGINT DEFAULT 0 COMMENT '微信退款笔数';
ALTER TABLE `eh_stat_service_settlement_results` ADD COLUMN `payment_card_paid_count` BIGINT DEFAULT 0 COMMENT '一卡通消费笔数';
ALTER TABLE `eh_stat_service_settlement_results` ADD COLUMN `payment_card_refund_count` BIGINT DEFAULT 0 COMMENT '一卡通退款笔数';
ALTER TABLE `eh_stat_service_settlement_results` ADD COLUMN `total_paid_count` BIGINT DEFAULT 0 COMMENT '总消费笔数';
ALTER TABLE `eh_stat_service_settlement_results` ADD COLUMN `total_refund_count` BIGINT DEFAULT 0 COMMENT '总退款消费笔数';
