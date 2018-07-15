-- by st.zheng 数据迁移
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_order_records`),0);
INSERT INTO `eh_rentalv2_order_records` (`id`,`order_no`,`biz_order_num`,`pay_order_id`,`payment_order_type`,`status`,`create_time`,`update_time`)
    SELECT (@id := @id + 1), order_id,order_num,payment_order_id,payment_order_type,0,create_time,create_time  FROM eh_payment_order_records where order_type = 'rentalOrder';

update `eh_rentalv2_order_records` t1 right join `eh_rentalv2_orders` t2 on t1.`order_no` = t2.`order_no` set t1.order_id = t2.id,t1.amount = t2.pay_total_money,t1.status = IF(t2.status in (2,7,9,10,14,20),1,0) ;

-- 通用脚本  
-- ADD BY 杨崇鑫
-- 新支付的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.appKey', '6caa8584-c723-4b7b-9aec-071b4e31418f', '新新支付appKey', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.secretKey', 'zChUBcTTn0CPR31fwRr96qdEmkn53SCZCMzNGwnBa7yREcC2a/Phlxsml4dmFBZnuuLRjPiSoJxJRA2GtsIkpg==', '新新支付secretKey', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.payHomeUrl', 'http://payv2-beta.zuolin.com/pay', '新新支付payHomeUrl', '0');
	
-- 支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('asset.pay.v2.callback.url', '/asset/payNotify', '物业缴费新支付回调接口', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pmsy.pay.v2.callback.url', '/pmsy/payNotify', '物业缴费新支付回调接口', '999993');

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
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4443' where namespace_id=999993;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4422' where namespace_id=999979;
-- END BY 杨崇鑫

-- by yanlong.liang
-- 支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('activity.pay.v2.callback.url', '/activity/payNotify', '活动报名新支付回调接口', '0');
-- 活动支付订单迁移
update eh_activity_roster r,eh_payment_order_records t set r.pay_order_id = t.payment_order_id where t.order_type = 'activitySignupOrder' and r.order_no = t.order_id ;
update eh_activity_roster r,eh_payment_order_records t set r.refund_pay_order_id = t.payment_order_id where t.order_type = 'activitySignupOrder' and r.refund_order_no = t.order_id ;

-- 收款方账号迁移
SET @id = ifnull((SELECT MAX(id) FROM `eh_activity_biz_payee`),0);
set @namespace_id = 1;
set @account_id = 2327;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999944;
set @account_id = 4592;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999946;
set @account_id = 4333;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999947;
set @account_id = 4241;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999948;
set @account_id = 4334;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999950;
set @account_id = 4303;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999951;
set @account_id = 4208;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999952;
set @account_id = 4335;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999955;
set @account_id = 4242;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999957;
set @account_id = 4197;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999958;
set @account_id = 4249;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999959;
set @account_id = 3799;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999961;
set @account_id = 3777;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999963;
set @account_id = 4590;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999966;
set @account_id = 1005;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999967;
set @account_id = 4526;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999971;
set @account_id = 1000;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999973;
set @account_id = 1004;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999981;
set @account_id = 3800;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999979;
set @account_id = 4422;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999979;
set @account_id = 4422;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
-- yanlong.liang END



-- by st.zheng
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('rental.pay.v2.callback.url', '/rental/payNotify', '资源预订新支付回调接口', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('rental.refund.v2.callback.url', '/rental/refundNotify', '资源预订新退款回调接口', '0');
-- 收款账户迁移
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_pay_accounts`),0);
set @namespace_id = 999973;
set @account_id = 1004;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 1;
set @account_id = 2327;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999961;
set @account_id = 3777;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999959;
set @account_id = 3799;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999981;
set @account_id = 3800;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999957;
set @account_id = 4197;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999951;
set @account_id = 4208;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999947;
set @account_id = 4241;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999955;
set @account_id = 4242;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999958;
set @account_id = 4249;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999950;
set @account_id = 4303;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999946;
set @account_id = 4333;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999948;
set @account_id = 4334;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999952;
set @account_id = 4335;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999967;
set @account_id = 4526;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999963;
set @account_id = 4590;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999944;
set @account_id = 4592;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;


-- bydengs
-- 通用脚本
-- 32033	左邻	任务	停车支持发票系统接口 (未处理)

set @id= IFNULL((select max(id) from eh_apps),0);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES (@id:=@id+1, '1', '95908e84-fe3c-4bb3-a2a4-db6a078abfe3', 'fz8QGHnJ0796c1LIyyMQI2z1rAVY0DRcynEh23CdpPatapDmHkv0sqGWDBVLWHLBVmOu3StHw4JrD4TB8iX1EQ==', '发票系统', NULL, '1', NOW(), NULL, NULL);

-- 设置发票系统的appkey