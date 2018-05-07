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

	

-- 新增一个企业管理员测试账号
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (825069,'EhOrganizations',1040129,1,10,867321,0,1,UTC_TIMESTAMP(),999955,'EhUsers','admin');

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
VALUES (867321,UUID(),'18220994181','四叶草',NULL,1,45,'1','2','zh_CN','06f37ba88f5a260c110f726c1319e67b','b6addf3ee9faff2b889507edfc4d1ad0b9f4c7c4c3135c29bd4621832a394eea',
UTC_TIMESTAMP(),999955);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (829733,1040129,'USER',867321,'四叶草',0,'13810281614',UTC_TIMESTAMP(),999955);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (8438642,867321,0,'13810281614',NULL,3,UTC_TIMESTAMP(),999955);	

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (82180316,1040129,'/1040129','USER',867321,'manager','四叶草',0,'13810281614',3,999955,'ENTERPRISE',0,829733);
	
	
	
	