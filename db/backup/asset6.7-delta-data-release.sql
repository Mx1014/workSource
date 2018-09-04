-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR: 
-- REMARK: 

-- --------------------- SECTION END ---------------------------------------------------------






-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: liangqishi 20180810
-- REMARK: 增加统一订单错误码
SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'pay', '10013', 'zh_CN', '支付回调链接配置为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'asset', '10013', 'zh_CN', '支付回调链接配置为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10009', 'zh_CN', '帐单组不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10010', 'zh_CN', '没有配置收款方帐号');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10011', 'zh_CN', '缴费订单创建失败');

-- AUTHOR: liangqishi 20180811
-- REMARK: 增加连接统一订单服务器的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.connect_url', 'https://gorder.zuolin.com', '连接统一订单服务器的链接', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_key', '69ee0cb3-5afb-4d83-ae12-ef493de48de2', '连接统一订单服务器的appkey', 0, NULL, 0);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.server.app_secret', 'T6PEjA9GBAVMBmlBYDs9RkoQMurrH5XQjFoP1v+oGomKeIdsqVhwpTVv8AHPLWo/I09IudgxR4/zjvM9YYwxzg==', '连接统一订单服务器的appsecret', 0, NULL, 0);


INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.default.personal_bind_phone', '12000001802', '支付个人帐号默认的绑定手机号', 0, NULL, 0);
	
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('gorder.system_id', '10', '由支付系统分配的业务系统ID', 0, NULL, 0);
	
-- --------------------- SECTION END ---------------------------------------------------------






-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: liangqishi 20180810
-- REMARK: 海岸的支付回调URL是定制的，但不需要用另外一个key，只需要按域空间分即可；
-- REMARK: 删除海岸域空间特殊的回调URL定制key，而补充该域空间的脚本；
DELETE FROM `eh_configurations` WHERE `name`='pay.v2.callback.url.pmsy';
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('pay.v2.callback.url.asset', '/pmsy/payNotify', '物业缴费新支付回调接口', 999993, NULL, 0);
	
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('pay.v2.asset.haian_environment', 'release', 'release：正式环境，beta：测试环境，以此来判断是否需要为海岸造测试数据', 999993, NULL, 0);

-- --------------------- SECTION END ---------------------------------------------------------





-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR:
-- REMARK:


-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------





-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR: 
-- REMARK: 

-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------





-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:


-- --------------------- SECTION END ---------------------------------------------------------


