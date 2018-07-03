-- 通用脚本
-- ADD BY 郑思挺
-- 数据迁移
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_order_records`),10000);
INSERT INTO `eh_rentalv2_order_records` (`id`,`order_no`,`biz_order_num`,`pay_order_id`,`payment_order_type`,`status`,`create_time`,`update_time`)
    SELECT (@id := @id + 1), order_id,order_num,payment_order_id,payment_order_type,0,create_time,create_time  FROM eh_payment_order_records where order_type = 'rentalOrder';

update `eh_rentalv2_order_records` t1 right join `eh_rentalv2_orders` t2 on t1.`order_no` = t2.`order_no` set t1.order_id = t2.id,t1.amount = t2.pay_total_money,t1.status = IF(t2.status in (2,7,9,10,14,20),1,0) ;
-- END BY 郑思挺

-- 通用脚本 
-- ADD BY 杨崇鑫
-- 新支付的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.appKey', '6caa8584-c723-4b7b-9aec-071b4e31418f', '新支付appKey', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.secretKey', 'zChUBcTTn0CPR31fwRr96qdEmkn53SCZCMzNGwnBa7yREcC2a/Phlxsml4dmFBZnuuLRjPiSoJxJRA2GtsIkpg==', '新支付secretKey', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.payHomeUrl', 'http://payv2-beta.zuolin.com/pay', '新支付payHomeUrl', '0');
	
-- 支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.callback.url.asset', '/asset/payNotify', '物业缴费新支付回调接口', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.callback.url.pmsy', '/pmsy/payNotify', '物业缴费新支付回调接口', '999993');

-- 由于海岸馨服务是定制的，web没有账单组管理，所以需要初始化收款方账户配置
SET @id = ifnull((SELECT MAX(id) FROM `eh_payment_bill_groups`),0);
INSERT INTO `eh_payment_bill_groups` (`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`, `brother_group_id`, `bills_day_type`, `biz_payee_type`, `biz_payee_id`, `category_id`) 
VALUES (@id := @id + 1, '999993', '999993', 'community', '物业缴费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '1', NULL, NULL, NULL, '4',
	'EhOrganizations', '4443', '1028');

-- 新支付数据迁移
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='2327' where namespace_id=1; -- Volgo
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4592' where namespace_id=999944; -- 天企汇
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4333' where namespace_id=999946; -- 聚变
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4241' where namespace_id=999947; -- 智慧空港
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4334' where namespace_id=999948; -- 国贸服务
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4303' where namespace_id=999950; -- 智汇银星
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4208' where namespace_id=999951; -- 鼎峰汇
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4335' where namespace_id=999952; -- 创集合
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4242' where namespace_id=999955; -- ELive
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4197' where namespace_id=999957; -- 杭州越空间
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4249' where namespace_id=999958; -- 创意园
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='3799' where namespace_id=999959; -- 启迪香山
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='3777' where namespace_id=999961; -- 智富汇
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4590' where namespace_id=999963; -- 路福联合广场
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4526' where namespace_id=999967; -- 大沙河建投
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1000' where namespace_id=999971; -- 张江高科
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1004' where namespace_id=999973; -- E-BOILL
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='3800' where namespace_id=999981; -- 上海万科星商汇
-- update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1006' where namespace_id=999983; 
-- update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1001' where namespace_id=999985;
-- update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1003' where namespace_id=999990;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4443' where namespace_id=999993; -- 海岸馨服务

-- END BY 杨崇鑫

-- 深圳湾适用脚本[999966]  
-- ADD BY 杨崇鑫 
-- 新支付数据迁移
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1005' where namespace_id=999966; -- 深圳湾
-- END BY 杨崇鑫


-- 光大we谷适用脚本[999979]  
-- ADD BY 杨崇鑫 
-- 新支付数据迁移
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4422' where namespace_id=999979; -- 光大we谷
-- END BY 杨崇鑫


-- -----------------------------SECTION BEGIN--------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 支付回调
-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动模块支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('pay.v2.callback.url.activity', '/activity/payNotify', '活动报名新支付回调接口', '0');
-- -----------------------------SECTION END----------------------------------------------

-- -----------------------------SECTION BEGIN--------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 活动支付订单迁移,在执行该语句前，将eh_activity_roster，eh_payment_order_records这两张表进行全表备份
-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动支付订单迁移
update eh_activity_roster r,eh_payment_order_records t set r.pay_order_id = t.payment_order_id where t.order_type = 'activitySignupOrder' and r.order_no = t.order_id ;
update eh_activity_roster r,eh_payment_order_records t set r.refund_pay_order_id = t.payment_order_id where t.order_type = 'activitySignupOrder' and r.refund_order_no = t.order_id ;
-- -----------------------------SECTION END----------------------------------------------

-- -----------------------------SECTION BEGIN--------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 收款方账号迁移
-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动收款方账号迁移,在执行语句前，请与 陈毅峰 对照一下域空间是否有遗漏；
SET @id = ifnull((SELECT MAX(id) FROM `eh_activity_biz_payee`),10000);
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

set @namespace_id = 999993;
set @account_id = 4443;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;
-- -----------------------------SECTION END----------------------------------------------

-- -----------------------------SECTION BEGIN--------------------------------------------
-- ENV: 光大we谷
-- DESCRIPTION: 收款方账号迁移
-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动收款方账号迁移；

set @namespace_id = 999979;
set @account_id = 4422;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;
-- -----------------------------SECTION END----------------------------------------------

-- -----------------------------SECTION BEGIN--------------------------------------------
-- ENV: 深圳湾
-- DESCRIPTION: 收款方账号迁移
-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动收款方账号迁移；

set @namespace_id = 999966;
set @account_id = 1005;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;
-- -----------------------------SECTION END----------------------------------------------
-- 通用脚本
-- ADD BY 郑思挺
-- 回调接口
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('pay.v2.callback.url.rental', '/rental/payNotify', '资源预订新支付回调接口', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ('refund.v2.callback.url.rental', '/rental/refundNotify', '资源预订新退款回调接口', '0');
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
-- END 郑思挺

-- bydengs
-- 通用脚本
-- 32033	左邻	任务	停车支持发票系统接口 (未处理)

set @id= IFNULL((select max(id) from eh_apps),0);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES (@id:=@id+1, '1', '95908e84-fe3c-4bb3-a2a4-db6a078abfe3', 'fz8QGHnJ0796c1LIyyMQI2z1rAVY0DRcynEh23CdpPatapDmHkv0sqGWDBVLWHLBVmOu3StHw4JrD4TB8iX1EQ==', '发票系统', NULL, '1', NOW(), NULL, NULL);

-- 设置发票系统的appkey

-- 通用脚本  
-- ADD BY 杨崇鑫
-- 解决导入的时候费项名称多了*的bug by cx.yang
update eh_payment_bill_items set charging_item_name=REPLACE(charging_item_name,"*","");
update eh_payment_bill_items set charging_item_name=REPLACE(charging_item_name,"(元)","");
-- END BY 杨崇鑫


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- 此SECTION放升级相关的操作要求，如调接口、查询数据确认等
-- AUTHOR: liangqishi  20180702
-- REMARK: 某某模块涉及到数据迁移，升级后需要调用/xxxx/xxxx接口更新ES
-- --------------------- SECTION END -------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: 基线执行
-- AUTHOR: dengs  20180703
-- REMARK: 基线停车支付收款方迁移

-- Volgo
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 1;
set @communityid = '240111044331058733';
set @parkinglotId = 10030;
SET @parkingLotName = '左邻停车场';
set @payeeId = 2327;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- 天企汇
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999944;
set @communityid = '240111044332061061';
set @parkinglotId = 10034;
SET @parkingLotName = '联科停车场';
set @payeeId = 4592;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999944;
set @communityid = '240111044332061063';
set @parkinglotId = 10035;
SET @parkingLotName = '创投大厦停车场';
set @payeeId = 4592;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999944;
set @communityid = '240111044332061063';
set @parkinglotId = 10036;
SET @parkingLotName = '孵化中心停车场';
set @payeeId = 4592;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999944;
set @communityid = '240111044332061063';
set @parkinglotId = 10037;
SET @parkingLotName = 'IT研发中心停车场';
set @payeeId = 4592;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- 智汇银星
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999950;
set @communityid = '240111044332060200';
set @parkinglotId = 10028;
SET @parkingLotName = '银星工业区停车场';
set @payeeId = 4303;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());


-- 创意园
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999958;
set @communityid = '240111044332060016';
set @parkinglotId = 10024;
SET @parkingLotName = '中百畅停车场';
set @payeeId = 4249;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- 路福联合广场
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999963;
set @communityid = '240111044332060139';
set @parkinglotId = 10032;
SET @parkingLotName = '路福联合大厦停车场';
set @payeeId = 4590;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- 上海万科星商汇
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999981;
set @communityid = '240111044331056041';
set @parkinglotId = 10021;
SET @parkingLotName = '御河企业公馆停车场';
set @payeeId = 3800;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- --------------------- SECTION END -------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: 深圳湾独立环境执行
-- AUTHOR: dengs  20180703
-- REMARK: 基线停车支付收款方迁移
-- 深圳湾
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999966;
set @communityid = '240111044331050370';
set @parkinglotId = 10011;
SET @parkingLotName = '软件产业基地停车场';
set @payeeId = 1129;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999966;
set @communityid = '240111044331050371';
set @parkinglotId = 10012;
SET @parkingLotName = '创投大厦停车场';
set @payeeId = 1129;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999966;
set @communityid = '240111044331050369';
set @parkinglotId = 10013;
SET @parkingLotName = '生态园停车场';
set @payeeId = 1129;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- --------------------- SECTION END -------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: 光大we谷独立环境执行
-- AUTHOR: dengs  20180703
-- REMARK: 基线停车支付收款方迁移
-- 光大we谷
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999979;
set @communityid = '240111044331056800';
set @parkinglotId = 10026;
SET @parkingLotName = '光大we谷停车场';
set @payeeId = 4422;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- --------------------- SECTION END -------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: 基线执行
-- AUTHOR: dengs  20180703
-- REMARK: 基线执行云打印收款方
-- volgo
SET @id = IFNULL((SELECT MAX(id) from eh_siyin_print_business_payee_accounts),9999);
INSERT INTO `eh_siyin_print_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, '1', 'community', '240111044331058733', '2327', 'EhOrganizations', '2', '1', now(), '1', now());
-- --------------------- SECTION END -------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- AUTHOR: tangcen 20180703
-- REMARK: 合同日志的模板
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '1', 'zh_CN', '合同事件', '创建合同', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '2', 'zh_CN', '合同事件', '删除合同', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '3', 'zh_CN', '合同事件', '修改${display}:由${oldData}更改为${newData}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '4', 'zh_CN', '合同资产事件', '新增合同资产:${apartmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '5', 'zh_CN', '合同资产事件', '删除合同资产:${apartmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '6', 'zh_CN', '合同计价条款事件', '新增计价条款:${chargingItemName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '7', 'zh_CN', '合同计价条款事件', '删除计价条款:${chargingItemName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '8', 'zh_CN', '合同计价条款事件', '修改计价条款:${chargingItemName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '9', 'zh_CN', '合同附件事件', '新增附件:${attachmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '10', 'zh_CN', '合同附件事件', '删除附件:${attachmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '11', 'zh_CN', '调租计划事件', '新增调租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '12', 'zh_CN', '调租计划事件', '删除调租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '13', 'zh_CN', '调租计划事件', '修改调租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '14', 'zh_CN', '免租计划事件', '新增免租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '15', 'zh_CN', '免租计划事件', '删除免租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '16', 'zh_CN', '免租计划事件', '修改免租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '17', 'zh_CN', '合同续约事件', '产生续约合同，子合同名称为:${contractName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '18', 'zh_CN', '合同变更事件', '产生变更合同，子合同名称为:${contractName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '19', 'zh_CN', '资产更改事件', '修改关联资产，由${oldApartmnets}变为${newApartmnets}', '0');
-- --------------------- SECTION END -------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄明波  20180703
-- REMARK: 服务联盟v3.4 issue-29989
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11100', 'zh_CN', '需要更新的筛选为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11101', 'zh_CN', '筛选的类型不合法');

update eh_locale_templates set text = '<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><style>img{height: 200px;width: 200px;}</style><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>${title}</title></head><body><p>预订人：${creatorName}</p><p>手机号：${creatorMobile}</p><p>公司名称：${creatorOrganization}</p><p>服务名称：${serviceOrgName}</p>${note}</body></html>' where scope = 'serviceAlliance.request.notification' and code = 4;
update eh_locale_strings set text = 'USER_NAME,USER_PHONE,USER_COMPANY' where scope = 'serviceAlliance.request.notification' and code = '10006';

update eh_service_modules set action_type = 14 where id = 40500;
update eh_service_module_apps set action_type = 14 where module_id = 40500;
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: 黄明波  20180703
-- REMARK: 服务联盟v3.4 issue-29989
-- REMARK: 更新服务的封面图，迁移服务广场的item

-- /yellowPage/transferPosterUriToAttachment
-- 参数:1802


--/yellowPage/transferLaunchPadItems
-- 参数:1802

-- --------------------- SECTION END ---------------------------------------------------------

