-- #issue-26479 园区快讯V1.8（增加项目导航，支持游客模式）  add by 黄明波  2018/04/28

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

-- 品质核查离线包版本更新  by jiarui
update eh_version_urls set download_url = replace(download_url,'1-0-0','1-0-1'),
  info_url = replace(info_url,'1-0-0','1-0-1'),
  target_version = '1.0.1' where app_name = '品质核查';

--
-- 一键推送的数据范围改成不限园区  add by xq.tian  2018/04/26
--
UPDATE eh_service_modules SET module_control_type = 'unlimit_control' WHERE name = '一键推送';

-- 张江高科现在可以展示批量导入导出的按钮了 by wentian 2018/04/28
delete from eh_service_module_exclude_functions where module_id = 20400 and function_id = 95 and namespace_id = 999971;