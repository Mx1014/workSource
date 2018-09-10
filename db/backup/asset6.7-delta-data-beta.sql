-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: 杨崇鑫  20180815
-- REMARK: 海岸是定制的，所以需要release：正式环境，beta：测试环境，以此来判断是否需要为海岸造测试数据
update `eh_configurations` set value = 'beta' WHERE `name`='pay.v2.asset.haian_environment';

UPDATE `dev`.`eh_configurations` SET `name` = 'gorder.server.app_key', `value` = '7bbb5727-9d37-443a-a080-55bbf37dc8e1', `description` = '连接统一订单服务器的appkey', `namespace_id` = 0, `display_name` = NULL, `is_readonly` = 0 WHERE `id` = 5656;
UPDATE `dev`.`eh_configurations` SET `name` = 'gorder.server.app_secret', `value` = 'zChUBcTTn0CPR31fwRr96qdEmkn53SCZCMzNGwnBa7yREcC2a/Phlxsml4dmFBZnuuLRjPiSoJxJRA2GtsIkpg==', `description` = '连接统一订单服务器的appsecret', `namespace_id` = 0, `display_name` = NULL, `is_readonly` = 0 WHERE `id` = 5657;
UPDATE `dev`.`eh_configurations` SET `name` = 'gorder.server.connect_url', `value` = 'http://test106.zuolin.com/gorder', `description` = '连接统一订单服务器的链接', `namespace_id` = 0, `display_name` = NULL, `is_readonly` = 0 WHERE `id` = 5658;

-- --------------------- SECTION END ---------------------------------------------------------