-- by st.zheng 数据迁移
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_order_record`),0);
INSERT INTO `eh_rentalv2_order_record` (`id`,`order_no`,`pay_order_id`,`payment_order_type`,status`,`create_time`,`update_time`)
SELECT (@id := @id + 1), order_id,payment_order_id,payment_order_type,0,create_time,create_time  FROM eh_payment_order_records where order_type = 'rentalOrder';

update `eh_rentalv2_order_record` t1 right join `eh_rentalv2_orders` t2 on t1.`order_no` = t2.`order_no` set t1.order_id = t2.id,t1.amount = t2.pay_total_money,t1.status = IF(t2.status in (2,7,9,10,14,20),1,0) ;

-- by cx.yang 新支付 start
-- by cx.yang 支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('asset.pay.v2.callback.url', '/asset/payNotify', '物业缴费新支付回调接口', '0');
-- 由于海岸馨服务是定制的，web没有账单组管理，所以需要初始化收款方账户配置
SET @id = ifnull((SELECT MAX(id) FROM `eh_payment_bill_groups`),0);
INSERT INTO `eh_payment_bill_groups` VALUES (@id := @id + 1, '999993', '999993', 'community', '物业缴费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '1', NULL, NULL, NULL, '4', 
	'EhOrganizations', '4443');

-- 新新支付数据迁移
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='2327' where namespace_id=1;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4592' where namespace_id=999944;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4333' where namespace_id=999946;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4241' where namespace_id=999947;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4334' where namespace_id=999948;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4303' where namespace_id=999950;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4208' where namespace_id=999951;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4335' where namespace_id=999952;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4242' where namespace_id=999955;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4197' where namespace_id=999957;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4249' where namespace_id=999958;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='3799' where namespace_id=999959;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='3777' where namespace_id=999961;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4590' where namespace_id=999963;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1005' where namespace_id=999966;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4526' where namespace_id=999967;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1000' where namespace_id=999971;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1004' where namespace_id=999973;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='3800' where namespace_id=999981;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1006' where namespace_id=999983;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1001' where namespace_id=999985;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1003' where namespace_id=999990;

-- by cx.yang 新支付end

-- by yanlong.liang 支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('activity.pay.v2.callback.url', '/activity/payNotify', '活动报名新支付回调接口', '0');

-- by st.zheng
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('rental.pay.v2.callback.url', '/rental/payNotify', '资源预订新支付回调接口', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('rental.refund.v2.callback.url', '/rental/refundNotify', '资源预订新退款回调接口', '0');


