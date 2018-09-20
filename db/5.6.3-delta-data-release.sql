
-- 更新企业客户管理模块
UPDATE eh_service_modules set client_handler_type = 2 where id = 21100;

-- 人事档案2.6 数据同步(基线已经执行过,可重复执行)start by ryan
UPDATE eh_organization_member_details AS t SET check_in_time_index = (CONCAT(SUBSTRING(t.check_in_time,6,2),SUBSTRING(t.check_in_time,9,2)));
UPDATE eh_organization_member_details AS t SET birthday_index = (CONCAT(SUBSTRING(t.birthday_index,6,2),SUBSTRING(t.birthday_index,9,2)));
-- end

-- 人事2.7 数据同步 (基线已经执行过,不不不不可重复执行)start by ryan.

-- 执行 /archives/syncArchivesConfigAndLogs 接口

-- 人事2.7 数据同步 end by ryan.

-- #issue-31731【标准版V2.0】【园区快讯】【Android&Ios】客户端内容详情显示失败：404 not found
update eh_configurations set  value = '/html/news_text_review.html'  where name = 'news.content.url';

-- 园区入驻从“敬请期待”改为“上线”
UPDATE eh_service_modules set client_handler_type = 0, instance_config = '{}' WHERE id = 40100;
UPDATE eh_service_module_apps set instance_config = '{}' WHERE module_id = 40100;

-- janson
delete from eh_rentalv2_orders where  rental_resource_id not in (select id from eh_rentalv2_resources);
update eh_organizations set organization_type = 'ENTERPRISE' where organization_type is null;
-- end janson