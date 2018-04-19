-- for 住总ELive物业查费对接 by 杨崇鑫  start
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) VALUES ( 'asset.zhuzong.url', 'http://139.129.204.232:8007/LsInterfaceServer', '物业查费', '999955');

SET @asset_vendor_id = IFNULL((SELECT MAX(id) FROM `eh_asset_vendor`),1);
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`)
select @asset_vendor_id := @asset_vendor_id + 1, 'community', id, name, 'ZZ', '2', '999955' from eh_communities where namespace_id = 999955;




-- for 住总ELive物业查费对接 by 杨崇鑫  end
