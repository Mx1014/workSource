-- by st.zheng 数据迁移
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_order_record`),0);
INSERT INTO `eh_rentalv2_order_records` (`id`,`order_no`,`biz_order_num`,pay_order_id`,`payment_order_type`,status`,`create_time`,`update_time`)
SELECT (@id := @id + 1), order_id,order_num,payment_order_id,payment_order_type,0,create_time,create_time  FROM eh_payment_order_records where order_type = 'rentalOrder';

update `eh_rentalv2_order_records` t1 right join `eh_rentalv2_orders` t2 on t1.`order_no` = t2.`order_no` set t1.order_id = t2.id,t1.amount = t2.pay_total_money,t1.status = IF(t2.status in (2,7,9,10,14,20),1,0) ;


-- by cx.yang 支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('asset.pay.v2.callback.url', '/asset/payNotify', '物业缴费新支付回调接口', '0');

-- by yanlong.liang 支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('activity.pay.v2.callback.url', '/activity/payNotify', '活动报名新支付回调接口', '0');

-- by st.zheng
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('rental.pay.v2.callback.url', '/rental/payNotify', '资源预订新支付回调接口', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('rental.refund.v2.callback.url', '/rental/refundNotify', '资源预订新退款回调接口', '0');