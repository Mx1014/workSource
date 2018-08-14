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
  `general_order_id` BIGINT COMMENT '统一订单ID',
  `amount` DECIMAL(10,2),
  `payment_status` INT DEFAULT 0 COMMENT '支付状态，0-待支付、1-支付成功、2-支付中、5-支付失败',
  `payment_order_type` INTEGER NOT NULL DEFAULT 0 COMMENT '支付系统中的订单类型：1-RECHARGE(充值)、2-WITHDRAW(提现)、3-PURCHACE(支付), 4-REFUND(退款)',
  `payment_type` INT NOT NULL DEFAULT 0 COMMENT '支付类型，由支付系统定义（参考通联），如1-WECHAT_APPPAY(微信APP支付)、8-ALI_SCAN_PAY(阿里扫码支付)、9-WECHAT_JS_PAY(微信公众号支付)、21-WECHAT_JS_ORG_PAY(微信公众号集团支付)',
  `payment_time` DATETIME COMMENT '支付时间（缴费时间）',
  `payment_channel` INTEGER DEFAULT 0 COMMENT '支付渠道: 0-未知、1-微信、2-支付宝、3-现金',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- --------------------- SECTION END ---------------------------------------------------------

