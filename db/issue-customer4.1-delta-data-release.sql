-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: liangqishi  20180702
-- REMARK: 某某模块涉及到数据迁移，升级后需要调用/xxxx/xxxx接口更新ES
-- REMARK: content图片程序升级，从本版中的content二进制更新到正式环境中

-- AUTHOR: 唐岑 2018年10月8日19:56:37
-- REMARK: 在瑞安CM部署时，需执行该任务（issue-38706）同步资产数据。详细步骤咨询 唐岑
       
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruiancm
-- DESCRIPTION: 此SECTION只在瑞安新天地-999929执行的脚本

-- AUTHOR: 杨崇鑫
-- REMARK: 配置客户V4.1瑞安CM对接的访问地址
SET @id = ifnull((SELECT MAX(id) FROM `eh_configurations`),0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES (@id := @id + 1, 'RuiAnCM.sync.url', 'http://10.50.12.39/cm/WebService/OfficeApp-CM/OfficeApp_CMService.asmx', '瑞安新天地对接的第三方地址', 0, NULL, 1);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES (@id:=@id+1, 'contractService', '999929', NULL, 999929, NULL, 1);
	
-- AUTHOR: 杨崇鑫
-- REMARK: 初始化瑞安CM对接的默认账单组，由于该账单组是默认账单组，所以不允许删除
set @id = 1000000;
INSERT INTO `eh_payment_bill_groups`(`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`, `brother_group_id`, `bills_day_type`, `category_id`, `biz_payee_type`, `biz_payee_id`, `is_default`) 
	select @id:=@id+1, 999929, id, 'community', '缴费', 2, 5, 67663, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 1, 5, 1, NULL, 4, 3, NULL, NULL, 1
		from eh_communities;
-- REMARK:瑞安CM对接 账单区分数据来源
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10005', 'zh_CN', '瑞安CM产生');
-- REMARK: 瑞安CM对接 APP只支持查费，隐藏掉缴费
SET @id = ifnull((SELECT MAX(id) FROM `eh_payment_app_views`),0); 
INSERT INTO `eh_payment_app_views`(`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) 
VALUES (@id := @id + 1, 999929, NULL, 0, 'PAY', NULL, NULL, NULL, NULL, NULL, NULL);		
	
-- AUTHOR: 唐岑
-- REMARK: 修改楼宇资产管理web menu的module id
UPDATE eh_web_menus SET module_id=38000 WHERE id=16010100;

-- AUTHOR: 唐岑
-- REMARK: 创建新的园区
-- 添加园区
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063579', 'ca94e3a2-f36e-4033-9cbc-869881b643a4', '21977', '南京市', '21978', '玄武', 'SOP Office', NULL, '南京市玄武区珠江路未来城', NULL, 'TPQ项目', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 14:20:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '299', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063580', '8c5043ee-846f-4927-bb17-3ef65b999f0d', '21977', '南京市', '21978', '玄武', 'Inno Office', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:03:02', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '441', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063581', '43877d13-8995-46ed-9bcb-4f8a307c41f2', '21977', '南京市', '21978', '玄武', 'Inno Work', NULL, '南京市玄武区珠江路未来城', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:18:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '442', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063582', '6b61ab1c-efeb-4624-b160-543f2bb6363f', '21977', '南京市', '21978', '玄武', 'INNO创智A栋 Office', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:29:51', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '443', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063583', '239e0eac-c818-41d7-887d-651a037acc09', '21977', '南京市', '21978', '玄武', 'INNO创智B栋 Office', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:36:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '444', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063584', '88d29ccf-8cf4-4ff9-8dfc-896e8159af99', '21977', '南京市', '21978', '玄武', 'INNO创智A栋 Retail', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:38:27', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '445', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063585', '59e51ce5-5f1d-4150-9bd6-78741c6afd83', '21977', '南京市', '21978', '玄武', 'INNO创智B栋 Retail', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:43:52', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '446', NULL, NULL);

-- 园区和域空间关联
set @eh_namespace_resources_id = IFNULL((select MAX(id) from eh_namespace_resources),1);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063579', '2018-08-27 14:20:21', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063580', '2018-08-27 16:03:02', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063581', '2018-08-27 16:18:54', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063582', '2018-08-27 16:29:52', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063583', '2018-08-27 16:36:00', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063584', '2018-08-27 16:38:27', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063585', '2018-08-27 16:43:52', NULL);

-- 创建经纬度数据
set @eh_community_geopoints_id = IFNULL((select MAX(id) from eh_community_geopoints),1);
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063579', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063580', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063581', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063582', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063583', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063584', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063585', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');

-- 添加企业可见园区,管理公司可以看到添加的园区
set @eh_organization_communities_id = IFNULL((select MAX(id) from eh_organization_communities),1);
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063579');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063580');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063581');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063582');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063583');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063584');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063585');		

-- --------------------- SECTION END ---------------------------------------------------------