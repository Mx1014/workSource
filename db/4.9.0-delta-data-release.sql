-- 给一碑增加app_key by st.zheng
SET @id = (SELECT MAX(id) FROM eh_apps);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`) VALUES (@id:=@id+1, '1', 'c9620212-8877-11e7-b08e-0050569605f3', 'OmnSTXMJPqvCxW8n5AmkT1xSGnJ2sWZSyWcDUi32HAD7htoLLxuzGaZUPgRN9bew6mOBW55WliSbcXRV3laC3g==', 'yibei sign', 'yibei.app', '1', now());

-- 添加服务录入菜单 by st.zheng
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `category`) VALUES (20225, '服务录入', '20200', 'task_management_service_entry/property', '0', '2', '/20000/20200/20225', 'park', '420', '20100', '3', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `category`) VALUES (20226, '工作流设置', '20200', 'react:/working-flow/flow-list/property-service/20100?moduleType=repair', '0', '2', '/20000/20200/20226', 'park', '420', '20100', '3', 'module');

set @privilege_id = (select distinct privilege_id from eh_web_menu_privileges where name='物业报修' );
set @eh_web_menu_privilege_id = (select max(id) from eh_web_menu_privileges);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@eh_web_menu_privilege_id+1, @privilege_id, '20225', '物业报修', '1', '1', '物业报修 管理员权限', '720');
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@eh_web_menu_privilege_id+2, @privilege_id, '20226', '物业报修', '1', '1', '物业报修 管理员权限', '720');

set @menu_scope_id = (select max(id) from eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@menu_scope_id  + 1, '20225', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@menu_scope_id  + 2, '20226', '', 'EhNamespaces', '999983', '2');

update eh_launch_pad_items set action_data='{"url":"zl://propertyrepair/create?type=user&taskCategoryId=1&displayName=报修"}',action_type=60 where namespace_id=999983 and item_label = '报修';

-- 修改owner_type by st.zheng
update eh_service_alliances set owner_id=(select organization_id from eh_organization_communities where eh_service_alliances.owner_id = community_id limit 0,1),owner_type = 'organaization' where owner_type = 'community';

-- 园区入驻3.5 add by sw 20170904
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('40105', '项目介绍', '40100', NULL, 'projects_introduce', '0', '2', '/40000/40100/40105', 'park', '412', '40100', '3', NULL, 'module');

 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	SELECT (@menu_scope_id := @menu_scope_id + 1), '40105', '', 'EhNamespaces', owner_id, '2' from eh_web_menu_scopes where menu_id = 40100 and owner_type = 'EhNamespaces';

UPDATE eh_enterprise_op_request_buildings lp JOIN eh_lease_buildings lb ON lb.building_id = lp.building_id set lp.building_id = lb.id;

UPDATE eh_lease_promotions lp JOIN eh_lease_buildings lb ON lb.building_id = lp.building_id set lp.building_id = lb.id;

UPDATE eh_web_menus set `name` = '房源招租' where id = 40110;
UPDATE eh_web_menus set `name` = '申请记录' where id = 40120;

update eh_lease_promotion_attachments set owner_type = 'EhLeasePromotions';
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text` ) VALUES ('expansion', '7', 'zh_CN', '你要添加的楼栋已存在！');

DELETE from eh_lease_form_requests where source_type in ('EhLeasePromotions', 'EhBuildings');

UPDATE eh_locale_strings set scope = 'expansion' where scope = 'expansion.applyType';
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('expansion', '8', 'zh_CN', '无');
DELETE from eh_locale_strings where scope = 'expansion' and code in ('1', '2', '3', '4');

--园区地图 add by sw
INSERT INTO `eh_community_map_infos` (`id`, `namespace_id`, `community_id`, `map_uri`, `map_name`, `version`, `center_longitude`, `center_latitude`, `north_east_longitude`, `north_east_latitude`, `south_west_longitude`, `south_west_latitude`, `longitude_delta`, `latitude_delta`, `status`, `creator_uid`, `create_time`)
  VALUES ('1', '999981', '240111044331056041', 'cs://1/image/aW1hZ2UvTVRvd01XSmpabVkyWkRSa05qWXpOMkV4WmpkalpETTFPRGt6TVRKaE9EVmhaUQ', NULL, '1.0', '121.57425299921417', '31.176432503695498', '121.577029', '31.179732', '121.573704', '31.173716', '0.005241036325429604', '0.007979148321588525', '2', '1', '2017-08-21 14:55:01');
INSERT INTO `eh_community_map_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`, `order`) VALUES ('1', '999981', '', '0', '楼栋', 'building', '1', NULL, NULL, '1');
INSERT INTO `eh_community_map_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`, `order`) VALUES ('2', '999981', '', '0', '企业', 'organization', '1', NULL, NULL, '2');
INSERT INTO `eh_community_map_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`, `order`) VALUES ('3', '999981', '', '0', '商户', 'shop', '1', NULL, NULL, '3');

