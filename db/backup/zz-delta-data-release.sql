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
