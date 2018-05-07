-- #issue-26479增加园区快讯错误码
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10010', 'zh_CN', '未获取到公司');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10011', 'zh_CN', '未获取到项目');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10012', 'zh_CN', '项目权限不足');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10013', 'zh_CN', '未找到服务广场版本');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10014', 'zh_CN', '未找到资讯模块');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10015', 'zh_CN', '系统转码出错');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10016', 'zh_CN', '未找到页面模块组');

-- #issue-26479更新园区快讯权限为项目隔离
UPDATE `eh_service_modules` SET `module_control_type`='community_control' WHERE  `id`=10800;
update eh_service_module_apps set  module_control_type='community_control' where module_id = 10800 and  module_control_type = 'unlimit_control';

-- #issue-26479更新园区快讯WEB跳转地址
update eh_launch_pad_items set action_data = replace(action_data,'#/newsList#sign_suffix',concat('&ns=',namespace_id,'#/newsList#sign_suffix')) where action_type = 13 and action_data like '%park-news-web%';

-- #issue-26479 迁移原新闻从organization属性至community属性
update  eh_news nu ,
	(select news.id as news_id,  com.community_id as community_id from eh_news news 
	left join 	(select n.namespace_id as namespace_id, n.resource_id as community_id   
					from eh_namespace_resources n 
					where n.resource_type  = 'COMMUNITY'
					group by n.namespace_id
					order by n.namespace_id,n.default_order,n.resource_id) 
					as com 
	on com.namespace_id = news.namespace_id
	where news.owner_type = 'organization' and news.owner_id > 0) 
	as n_c
set nu.owner_type = 'EhCommunities', nu.owner_id = n_c.community_id 
where nu.id = n_c.news_id;


-- 停车订单标签 by dengs,2018.04.27
update eh_parking_lots SET order_tag=SUBSTR(CONCAT(id,'') FROM 3 FOR 5);


-- for 住总ELive物业查费对接 by 杨崇鑫  start

-- 根据手机号返回房产
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ( 'asset.zhuzong.QueryHouseByPhoneNumber.url', 'http://139.129.204.232:8007/LsInterfaceServer/phoneServer/QueryHouseByPhoneNumber', '根据手机号返回房产', '999955');
-- 根据房屋查询费用
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ( 'asset.zhuzong.QueryCostByHouseList.url', 'http://139.129.204.232:8007/LsInterfaceServer/phoneServer/QueryCostByHouseList', '根据房屋查询费用', '999955');
-- 查询费用明细
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ( 'asset.zhuzong.QueryCostDetailByID.url', 'http://139.129.204.232:8007/LsInterfaceServer/phoneServer/QueryCostDetailByID', '查询费用明细', '999955');
-- 账套编码
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
VALUES ( 'asset.zhuzong.AccountCode', 'sdgj', '账套编码', '999955');

SET @asset_vendor_id = IFNULL((SELECT MAX(id) FROM `eh_asset_vendor`),1);
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`)
select @asset_vendor_id := @asset_vendor_id + 1, 'community', id, name, 'ZZ', '2', '999955' from eh_communities where namespace_id = 999955;

-- 为住总ELive增加物业查费入口（物业缴费模块还没有对接严军的应用发布） by 杨崇鑫
delete from eh_launch_pad_items where item_label = '账单查询' and namespace_id = 999955;
set @id = (select MAX(`id`) from `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`)
	VALUES (@id:=@id+1,'999955',0,0,0,'/secondhome','NavigatorGroup1','账单查询','账单查询','cs://1/image/aW1hZ2UvTVRvNFl6VTVaV1E0WVdVME1ERTBNemd5WVRJd01ETXpaVFE1TWpneVlUYzBaZw',1,1,14,'{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\"}',10,0,1,1,0,0,'pm_admin',0,0,'');

-- for 住总ELive物业查费对接 by 杨崇鑫  end

-- 品质核查 能耗  巡检 离线包版本更新  by jiarui
update eh_version_urls set download_url = replace(download_url,'1-0-0','1-0-1'),
  info_url = replace(info_url,'1-0-0','1-0-1'),
  target_version = '1.0.1' where app_name = '品质核查';

update eh_version_urls set download_url = replace(download_url,'1-0-0','1-0-1'),
	info_url = replace(info_url,'1-0-0','1-0-1'),
	target_version = '1.0.1' where app_name = '物业巡检';

update eh_version_urls set download_url = replace(download_url,'1-0-0','1-0-1'),
	info_url = replace(info_url,'1-0-0','1-0-1'),
	target_version = '1.0.1' where realm_id = (select id from eh_version_realm where realm = 'energyManagement');

--
-- 一键推送的数据范围改成不限园区  add by xq.tian  2018/04/26
--
UPDATE eh_service_modules SET module_control_type = 'unlimit_control' WHERE name = '一键推送';

-- 张江高科现在可以展示批量导入导出的按钮了 by wentian 2018/04/28
delete from eh_service_module_exclude_functions where module_id = 20400 and function_id = 95 and namespace_id = 999971;

UPDATE `eh_locale_templates` SET `text`='尊敬的${plateOwnerName}，用户（${userName}：${userPhone}）已为您成功预约${useDetail}，请在该时间内前往指定车位（地址：${spaceAddress}），并点击以下链接使用：${orderDetailUrl} 谢谢。' WHERE `scope`='sms.default' and `code`=59;



-- 对接 北科建远程抄表  by jiarui 20180416
SET  @id = (SELECT MAX(id) FROM  eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id:=@id+1), 'energy.meter.thirdparty.server', 'http://139.196.103.205:8787/beikejian', 'energy.meter.thirdparty.server', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id:=@id+1), 'energy.meter.thirdparty.publicKey', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJQeFrVhmHoWYNwPkXFVScpdwsZ/BnVhsUuGGvozfgcyde6Q7nFaTmvNBGuxbSqsSmatQLKEZWkPDDzP/Yv7zPcCAwEAAQ==', 'energy.meter.thirdparty.publicKey', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id:=@id+1), 'energy.meter.thirdparty.client.id', 'joy000001', 'energy.meter.thirdparty.client.id', '0', NULL);

-- 增加公摊水电费，水电费改为自用水电费 by wentian
set @id = IFNULL((select MAX(`id`) from eh_payment_charging_items),0);
INSERT INTO `eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
  (@id:=@id+1, '公摊水费', '0', NOW(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
  (@id:=@id+1, '公摊电费', '0', NOW(), NULL, NULL, '1');

UPDATE `eh_payment_charging_items` SET `name` = '自用水费' where `name` = '水费';
UPDATE `eh_payment_charging_items` SET `name` = '自用电费' where `name` = '电费';

-- 增加错误码 by jiarui
set @id = IFNULL((SELECT MAX(id) from eh_locale_strings),0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy', '10032', 'zh_CN', '分摊比例大于1');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy', '10033', 'zh_CN', '比例系数不是数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy', '10034', 'zh_CN', '比例系数未添加');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy', '10035', 'zh_CN', '比例系数在一个楼栋门牌的时候不等于1');

-- 增加表计类型及文案修改 by jiarui
set @id=(select MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy.meter.type', '4', 'zh_CN', '公摊水表');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy.meter.type', '5', 'zh_CN', '公摊电表');


UPDATE `eh_locale_strings`
SET `text`='自用水表' WHERE scope = 'energy.meter.type' and `code` = 1;

UPDATE `eh_locale_strings`
SET `text`='自用电表' WHERE scope='energy.meter.type' and `code` = 2 ;

-- 增加比例系数 by jiarui
set @id = ifnull((SELECT MAX(`id`) FROM eh_payment_variables), 0);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@id:=@id+1, NULL, NULL , '比例系数', '0', now(), NULL, now(), 'blxs');

-- 没有生成账单以及账单任务记录时的提示语更改 by wentian 2018/4/28
UPDATE `eh_locale_strings` SET `text` = '无数据' WHERE `scope` = 'assetv2' AND `code` = '10003';

UPDATE eh_enterprise_customers SET tracking_uid = NULL WHERE tracking_uid = '-1';

-- 客户增加的权限  by jiarui
UPDATE `eh_service_module_privileges` SET  default_order = 1, `remark`='新增客户' WHERE `module_id`='21110' and  `privilege_id`='21101';
UPDATE `eh_service_module_privileges` SET  default_order = 2,`remark`='修改客户' WHERE `module_id`='21110' and  `privilege_id`='21102';
UPDATE `eh_service_module_privileges` SET  default_order = 4,`remark`='导入客户' WHERE `module_id`='21110' and  `privilege_id`='21103';
UPDATE `eh_service_module_privileges` SET  default_order = 6,`remark`='同步客户' WHERE `module_id`='21110' and  `privilege_id`='21104';
UPDATE `eh_service_module_privileges` SET  default_order = 3,`remark`='删除客户' WHERE `module_id`='21110' and  `privilege_id`='21105';
UPDATE `eh_service_module_privileges` SET  default_order = 0 , `remark`='查看客户' WHERE `module_id`='21110' and  `privilege_id`='21106';
UPDATE `eh_service_module_privileges` SET  default_order = 7,`remark`='查看管理信息'WHERE `module_id`='21110' and  `privilege_id`='21107';
UPDATE `eh_service_module_privileges` SET  default_order = 8,`remark`='新增管理信息'WHERE `module_id`='21110' and  `privilege_id`='21108';
UPDATE `eh_service_module_privileges` SET  default_order = 9,`remark`='修改管理信息'WHERE `module_id`='21110' and  `privilege_id`='21109';
UPDATE `eh_service_module_privileges` SET  default_order = 10,`remark`='删除管理信息'WHERE `module_id`='21110' and  `privilege_id`='21110';
UPDATE `eh_service_module_privileges` SET  default_order = 11,`remark`='导入管理信息'WHERE `module_id`='21110' and  `privilege_id`='21111';
UPDATE `eh_service_module_privileges` SET  default_order = 12,`remark`='导出管理信息'WHERE `module_id`='21110' and  `privilege_id`='21112';
UPDATE `eh_service_module_privileges` SET  default_order = 0,`remark`='查看' WHERE `module_id`='21120' and  `privilege_id`='21113';

-- 客户增加的权限  by jiarui
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('21114', '0', '客户管理导出权限', '客户管理 业务模块权限', NULL);

set @id =(select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@id:=@id+1) ,'21110', '0', '21114', '导出客户', '5', NOW());

-- by st.zheng
set @id =(select MAX(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id:=@id+1), 'rental.notification', '23', 'zh_CN', '即将超时', '尊敬的用户，您预约的${useDetail}剩余使用时长：15分钟，如需延时，请前往APP进行操作，否则超时系统将继续计时计费，感谢您的使用。', '0');
UPDATE `eh_locale_templates` SET `text`='尊敬的${plateOwnerName}，用户（${userName}：${userPhone}）已为您成功预约${useDetail}，请在该时间内前往指定车位（地址：${spaceAddress}），并点击以下链接使用：${orderDetailUrl} 谢谢。' WHERE `scope`='sms.default' and `code`=59;
UPDATE `eh_locale_templates` SET `text`='尊敬的${plateOwnerName}，用户（${userName}：${userPhone}）已为您将预约的${useDetail}延时到${newEndTime}，请点击以下链接使用：${orderDetailUrl} ，感谢您的使用。'WHERE (`scope`='sms.default' and `code`=62);
UPDATE `eh_locale_templates` SET  `text`='尊敬的${plateOwnerName}，用户（${userName}：${userPhone}）为您预约的${useDetail}由于前序订单使用超时，系统自动为您更换至${spaceNo}车位，请点击以下链接使用：${orderDetailUrl} ，给您带来的不便我们深感抱歉，感谢您的使用。' WHERE (`scope`='sms.default' and `code`=66);

-- 资源预定增加统计权限 by st.zheng
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40430', '统计信息', '40400', '/40000/40400/40430', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4040040430, '0', '资源预约 统计管理权限', '资源预约 统计管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40430', '0', 4040040430, '统计信息权限', '0', now());

update eh_rentalv2_orders set status = 7 where status = 8;
/*
  物业报修 pmtask-3.5 应用配置数据迁移
*/
update eh_service_module_apps set instance_config='{"taskCategoryId":6,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId_u003d6%';
update eh_service_module_apps set instance_config='{"taskCategoryId":6,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId=6%';
update eh_service_module_apps set instance_config='{"taskCategoryId":9,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId_u003d9%';
update eh_service_module_apps set instance_config='{"taskCategoryId":9,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId=9%';
/*
  物业报修 pmtask-3.5 应用配置数据迁移 正中会
*/
update eh_service_module_apps set instance_config='{"taskCategoryId":1,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId_u003d1%';
update eh_service_module_apps set instance_config='{"taskCategoryId":1,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId=1%';
/*
  物业报修 pmtask-3.5 权限配置页面信息迁移
*/
update eh_service_modules set name='统计信息' where id = 20190 and parent_id = 20100;
update eh_service_module_privileges set remark = '全部权限' where module_id = 20140 and privilege_id = 2010020140;
update eh_service_module_privileges set remark = '全部权限' where module_id = 20190 and privilege_id = 2010020190;
update eh_service_module_privileges set remark = '全部权限' where module_id = 20150 and privilege_id = 2010020150;
update eh_service_modules set status = 0 where id = 20150 and parent_id = 20100;

--  issue-28556 add by huangmingbo 
SET @kexin_xiaomao_park_lot_id = 10031;
SET @kexin_zzh_community_id = 240111044332060208;
SET @kexin_namespace_id = 999983;

-- issue-28556 add by huangmingbo 2018-05-03
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking', '10032', 'zh_CN', '未查询到月卡类型信息');

-- issue-28556 add by huangmingbo 2018-05-03
SET @car_type_id = (SELECT IFNULL(MAX(id),0) FROM eh_parking_card_types);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, @kexin_namespace_id, 'community', @kexin_zzh_community_id, @kexin_xiaomao_park_lot_id, '4', '普通客户', 2, 1, '2018-05-03 10:49:48', NULL, NULL);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, @kexin_namespace_id, 'community', @kexin_zzh_community_id, @kexin_xiaomao_park_lot_id, '10', '平安月卡', 2, 1, '2018-05-03 10:49:48', NULL, NULL);



