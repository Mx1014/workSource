-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫  20180811
-- REMARK: 物业缴费V6.7 统一订单
CREATE TABLE `eh_payment_bill_orders`(
  `id` BIGINT NOT NULL,
  `namespace_id` INT(10),
  `bill_id` VARCHAR(255),
  `order_number` varchar(255) COMMENT '业务订单编号，如：WUF00000000000004926',
  `payment_order_id` BIGINT COMMENT '支付系统订单ID',
  `general_order_id` BIGINT COMMENT '统一订单ID',
  `amount` DECIMAL(10,2),
  `payment_status` INT DEFAULT 0 COMMENT '支付状态，0-待支付、1-支付成功、2-支付中、5-支付失败',
  `payment_order_type` INTEGER NOT NULL DEFAULT 0 COMMENT '支付系统中的订单类型：1-RECHARGE(充值)、2-WITHDRAW(提现)、3-PURCHACE(支付), 4-REFUND(退款)',
  `payment_type` INT NOT NULL DEFAULT 0 COMMENT '支付类型，由支付系统定义（参考通联），如0-未选择支付方式、1-WECHAT_APPPAY(微信APP支付)、8-ALI_SCAN_PAY(阿里扫码支付)、9-WECHAT_JS_PAY(微信公众号支付)、21-WECHAT_JS_ORG_PAY(微信公众号集团支付)',
  `payment_time` DATETIME COMMENT '支付时间（缴费时间）',
  `payment_channel` INTEGER DEFAULT 0 COMMENT '支付渠道: 0-未知、1-微信、2-支付宝、3-现金',
  `uid` BIGINT COMMENT '缴费人',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- AUTHOR: 梁燕龙 20180815
-- REMARK: 活动报名 统一订单
ALTER TABLE `eh_activity_roster` MODIFY COLUMN `order_no` VARCHAR(64);
ALTER TABLE `eh_activity_roster` MODIFY COLUMN `refund_order_no` VARCHAR(64);

-- AUTHOR: 郑思挺 20180816
-- REMARK: 停车 保存biz_order_no
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `biz_order_no`  varchar(64) NULL AFTER `order_no`;

-- AUTHOR: 马世亨 20180817
-- REMARK: 物业报修 保存统一订单系统id
ALTER TABLE `eh_pm_task_orders` ADD COLUMN `general_order_id` varchar(64) NULL COMMENT '统一订单系统订单id';

-- AUTHOR: huangmingbo 0820
-- REMARK: 云打印 保存统一订单系统id
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `general_order_id` VARCHAR(64) NULL COMMENT '统一订单系统订单编号' ;

-- --------------------- SECTION END ---------------------------------------------------------

