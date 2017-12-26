-- fix 21422 add by xiongying20171222
delete from eh_var_fields where group_id in(19,20);

SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);  
SET @item_id = (SELECT MAX(id) FROM `eh_var_field_items`);  
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'intentionGrade', '����ȼ�', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '20', '1', '2', '1', NOW(), NULL, NULL, '20');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '40', '2', '2', '1', NOW(), NULL, NULL, '40');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '60', '3', '2', '1', NOW(), NULL, NULL, '60');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '80', '4', '2', '1', NOW(), NULL, NULL, '80');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '100', '5', '2', '1', NOW(), NULL, NULL, '100');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingTime', '����ʱ��', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'content', '��������', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"multiText\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingType', '������ʽ', 'Long', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '�绰', '1', '2', '1', NOW(), NULL, NULL, '1');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '�ʼ�', '2', '2', '1', NOW(), NULL, NULL, '3');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '����', '3', '2', '1', NOW(), NULL, NULL, '2');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '����', '4', '2', '1', NOW(), NULL, NULL, '4');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'customerName', '�ͻ�����', 'String', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingTime', '����ʱ��', 'Long', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'notifyTime', '��ǰ����ʱ��', 'Long', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'title', '����', 'Long', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'content', '����', 'String', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"multiText\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingType', '������ʽ', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '�绰', '1', '2', '1', NOW(), NULL, NULL, '1');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '�ʼ�', '2', '2', '1', NOW(), NULL, NULL, '3');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '����', '3', '2', '1', NOW(), NULL, NULL, '2');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '����', '4', '2', '1', NOW(), NULL, NULL, '4');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingUid', '������', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- fix 20310
INSERT INTO eh_service_module_functions(id, module_id, privilege_id) SELECT privilege_id, module_id, privilege_id FROM eh_service_module_privileges WHERE privilege_type = 0 and module_id = 21210;
SET @exclude_id = (SELECT MAX(id) FROM `eh_service_module_exclude_functions`);   
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '21210', '21201');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '21210', '21208');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '21210', '21209');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '21210', '21214');

-- 服务热线1.3 by st.zheng
set @eh_service_configurations_id = (select max(id) from eh_service_configurations);
INSERT INTO `eh_service_configurations` (`id`, `owner_type`, `owner_id`, `name`, `value`, `namespace_id`, `display_name`) VALUES (@eh_service_configurations_id+1, 'community', '240111044331050370', 'hotline-notshow', '0', '999968', '专属客服');

UPDATE `eh_web_menus` SET `data_type`='react:/service-online/management' WHERE `id`='40300';

-- 初始化为按时长定价 by st.zheng
update `eh_rentalv2_price_rules` set `price_type` = 0;
update `eh_rentalv2_price_packages` set `price_type` = 0;

set @eh_locale_templates_id = (select max(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`) VALUES (@eh_locale_templates_id+1, 'rental.notification', '12', 'zh_CN', '修改金额的推送', '您申请预订的${resourceName}，使用时间：${startTime}，订单金额调整为${amount}');

-- if_use_feelist字段初始化
update `eh_pm_tasks` set `if_use_feelist` = 1;


-- merge from forum-2.2 by yanjun 20171225 start

-- 更新活动分享页面链接 add by yanjun 20171219
UPDATE eh_configurations set `value` = '/activity/build/index.html#detail' where name = 'activity.share.url';

-- merge from forum-2.2 by yanjun 20171225 end


-- merge from incubator-1.2 by yanjun 201712252013 start
-- 文件有效时间 add by yanjun 20171219
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (( @id :=  @id + 1), 'filedownload.valid.interval', '7', 'filedownload valid interval', '0', NULL);


SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10001', 'zh_CN', '任务不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10002', 'zh_CN', '权限不足');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10003', 'zh_CN', '任务已执行完成，不可取消');


-- 文件中心菜单  add by yanjun 20171220
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`,operator_uid,creator_uid)
VALUES ('61000', '文件中心', '60000', '/60000/61000', '0', '2', '2', '0', UTC_TIMESTAMP(),1,1);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES ('61000', '文件中心', '60000', NULL, 'react:/file-center/file-list', '0', '2', '/60000/61000', 'park', '630', '61000', '2', NULL, 'module');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
SELECT (@menu_scope_id := @menu_scope_id + 1), 61000, '', 'EhNamespaces', id, 2 from eh_namespaces;


-- “入孵申请”菜单改成“入驻申请”  add by yanjun 20171223
UPDATE eh_web_menus set name = '入驻申请' where id = 36000 and name = '入孵申请';

UPDATE eh_launch_pad_layouts set version_code = '2017121901', layout_json  = '{"versionCode":"2017121901","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":1,"separatorHeight":16,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"title":"园区快讯","iconUrl": "https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":2,"newsSize":3},"defaultOrder":30,"separatorFlag":0,"separatorHeight":0}]}' where namespace_id = 999964 and `name` = 'ServiceMarketLayout' and `status` = 2;
UPDATE eh_launch_pad_items set display_flag = 1 where namespace_id = 999964;

UPDATE eh_launch_pad_items set item_label = '入驻申请', item_name = '入驻申请' where namespace_id = 999964 and item_name = '入孵申请';
UPDATE eh_launch_pad_items set item_label = '产品信息查询', item_name = '产品信息查询' where namespace_id = 999964 and item_name = '产品信息发布';
UPDATE eh_launch_pad_items set item_label = '机构信息查询', item_name = '机构信息查询' where namespace_id = 999964 and item_name = '机构信息发布';
DELETE from eh_launch_pad_items where namespace_id = 999964 and item_name in ('新闻快讯', '更多');

UPDATE eh_incubator_applies set root_id = id  WHERE root_id is NULL;
-- merge from incubator-1.2 by yanjun 201712252013 end


-- auth分支开始
-- 仓库管理 权限细化脚の本 by wentian

-- 需要把service—module中改为园区control


-- imitate service since we don't have an entry at app side
set @reflect_id = (select MAX(`id`) from `eh_reflection_service_module_apps`);
set @app_id = (select MAX(`active_app_id`) from `eh_reflection_service_module_apps`);
INSERT INTO `eh_reflection_service_module_apps`
(`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`)
VALUES
(@reflect_id:=@reflect_id+1, @app_id:=@app_id+1, '999992', '仓库管理', '21000', NULL, '2', 13, NULL, NOW(), 'community-control', '0', '', '', '21000');

-- privilege
set @module_id = 21010;
set @p_id = 210001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '仓库维护 查找', '仓库维护 查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '查看', '0', NOW());

set @module_id = 21010;
set @p_id = 210001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '仓库维护 新增，编辑，删除', '仓库维护 新增，编辑，删除', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '新增、编辑、删除', '0', NOW());

-- set @module_id = 21020;
-- set @p_id = 210001003;
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '物品维护 物品分类新增、编辑、删除、导入', '物品维护 物品分类新增、编辑、删除、导入', NULL);
-- set @mp_id = (select MAX(id) from eh_service_module_privileges);
-- INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
-- VALUES
-- (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '物品分类新增、编辑、删除、导入', '0', NOW());

set @module_id = 21020;
set @p_id = 210001004;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '物品维护 物品信息新增、编辑、查看、删除、导入', '物品维护 物品信息新增、编辑、查看、删除、导入', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '物品信息新增、编辑、查看、删除、导入', '0', NOW());

set @module_id = 21030;
set @p_id = 210001005;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 库存、日志查找', '库存维护 库存、日志查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '库存、日志查询', '0', NOW());

set @module_id = 21030;
set @p_id = 210001006;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 入库', '库存维护 入库', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '入库', '0', NOW());

-- set @module_id = 21030;
-- set @p_id = 210001007;
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 日志查找', '库存维护 日志查找', NULL);
-- set @mp_id = (select MAX(id) from eh_service_module_privileges);
-- INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
-- VALUES
-- (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '日志查找', '0', NOW());

set @module_id = 21030;
set @p_id = 210001008;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 日志导出', '库存维护 日志导出', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '日志导出', '0', NOW());

set @module_id = 21040;
set @p_id = 210001009;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '领用管理 领用查找', '领用管理 领用查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '领用查找', '0', NOW());

set @module_id = 21040;
set @p_id = 210001010;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '领用管理 领用申请', '领用管理 领用申请', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '领用申请', '0', NOW());

-- set @module_id = 21050;
-- set @p_id = 210001011;
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '参数配置 工作流配置', '参数配置 工作流配置', NULL);
-- set @mp_id = (select MAX(id) from eh_service_module_privileges);
-- INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
-- VALUES
-- (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '工作流配置', '0', NOW());
--
-- set @module_id = 21050;
-- set @p_id = 210001012;
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '参数配置 参数配置', '参数配置 参数配置', NULL);
-- set @mp_id = (select MAX(id) from eh_service_module_privileges);
-- INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
-- VALUES
-- (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '参数配置', '0', NOW());


set @module_id = 21040;
set @p_id = 210001013;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '领用管理 出库', '领用管理 出库', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '出库', '0', NOW());

-- end of wentian's script, farewell


-- custoemrAuth xiongying
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21110,'客户列表',21100,'/20000/21100/21110','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21120,'统计分析',21100,'/20000/21100/21120','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21210,'合同列表',21200,'/20000/21200/21210','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21220,'合同基础参数配置',21200,'/20000/21200/21220','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21230,'工作流配置',21200,'/20000/21200/21230','1','3','2','0',NOW(),1,1);

update eh_service_modules set parent_id = 20000, path = '/20000/21100' where name = '客户管理' and id = 21100;
update eh_service_modules set parent_id = 20000, path = '/20000/21200' where name = '合同管理' and id = 21200;

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21100, '0', '客户管理 管理员', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21200, '0', '合同管理 管理员', '合同管理 业务模块权限', NULL);

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21100', '1', '21100', '客户管理管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21100', '2', '21100', '客户管理全部权限', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21200', '1', '21200', '合同管理管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21200', '2', '21200', '合同管理全部权限', '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21101, '0', '客户管理 新增权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21102, '0', '客户管理 修改权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21103, '0', '客户管理 导入权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21104, '0', '客户管理 同步权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21105, '0', '客户管理 删除权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21106, '0', '客户管理 查看权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21107, '0', '客户管理 管理查看权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21108, '0', '客户管理 管理新增权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21109, '0', '客户管理 管理修改权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21110, '0', '客户管理 管理删除权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21111, '0', '客户管理 管理导入权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21112, '0', '客户管理 管理导出权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21113, '0', '客户管理 统计分析查看权限', '客户管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21201, '0', '合同管理 新增权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21202, '0', '合同管理 签约、发起审批权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21203, '0', '合同管理 修改权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21204, '0', '合同管理 删除权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21205, '0', '合同管理 作废权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21206, '0', '合同管理 入场权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21207, '0', '合同管理 查看权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21208, '0', '合同管理 续约权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21209, '0', '合同管理 变更权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21210, '0', '合同管理 合同参数查看权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21211, '0', '合同管理 合同参数修改权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21212, '0', '合同管理 合同工作流设置权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21213, '0', '合同管理 合同同步', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21214, '0', '合同管理 退约权限', '合同管理 业务模块权限', NULL);

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21101,'客户管理 新增权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21102,'客户管理 修改权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21103,'客户管理 导入权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21104,'客户管理 同步权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21105,'客户管理 删除权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21106,'客户管理 查看权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21107,'客户管理 管理查看权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21108,'客户管理 管理新增权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21109,'客户管理 管理修改权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21110,'客户管理 管理删除权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21111,'客户管理 管理导入权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21112,'客户管理 管理导出权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21120','0',21113,'客户管理 统计分析查看权限','0',NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21201,'合同管理 新增权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21202,'合同管理 签约、发起审批权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21203,'合同管理 修改权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21204,'合同管理 删除权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21205,'合同管理 作废权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21206,'合同管理 入场权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21207,'合同管理 查看权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21208,'合同管理 续约权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21209,'合同管理 变更权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21220','0',21210,'合同管理 合同参数查看权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21220','0',21211,'合同管理 合同参数修改权限','0',NOW());  
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21230','0',21212,'合同管理 合同工作流设置权限','0',NOW());   
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21213,'合同管理 合同同步','0',NOW());    
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21214,'合同管理 退约权限','0',NOW());   
    
    

delete from eh_reflection_service_module_apps where module_id = 49100;
DROP PROCEDURE IF EXISTS create_app;
DELIMITER //
CREATE PROCEDURE `create_app` ()
BEGIN
  DECLARE ns INTEGER;
  DECLARE moduleId LONG;
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR SELECT module_id, namespace_id FROM eh_service_module_scopes where module_id in (21100, 21200, 49100) and apply_policy = 2;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO moduleId, ns;
                IF done THEN
                    LEAVE read_loop;
                END IF;

        SET @app_id = (SELECT MAX(id) FROM `eh_reflection_service_module_apps`);   
        INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES ((@app_id := @app_id + 1), @app_id, ns, '', moduleId, NULL, '2', '13', '', NOW(), 'community_control', '0', '', NULL, moduleId);

  END LOOP;
  CLOSE cur;
END
//
DELIMITER ;
CALL create_app;
DROP PROCEDURE IF EXISTS create_app;    
    
update eh_reflection_service_module_apps set name = '客户管理', action_data = 'customer' where module_id = 21100;      
update eh_reflection_service_module_apps set name = '合同管理', action_data = 'contract' where module_id = 21200;      
update eh_reflection_service_module_apps set name = '能耗管理', action_data = '{"url":"http://core.zuolin.com/energy-management/build/index/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' where module_id = 49100;      
-- custoemrAuth xiongying end    
    

-- 物业巡检菜单显示不全 start  by jiarui 20171220

UPDATE eh_service_module_privileges
SET module_id = 20810
WHERE module_id = 20811;

UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20840;
UPDATE eh_service_modules
SET LEVEL =4
WHERE id = 20841;

UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20841;

UPDATE eh_service_modules
SET `status` = 2
WHERE id = 20655;

UPDATE eh_service_modules
SET `status` = 2
WHERE id = 20670;

-- 物业巡检菜单显示不全 end  by jiarui 20171220

-- 删除旧数据 管理员权限start by jiarui 20171220

DELETE FROM  eh_acls
WHERE privilege_id = 10011;

DELETE  FROM eh_acls
WHERE privilege_id = 10010;

-- 删除旧数据 管理员权限 end by jiarui 20171220


-- 删除EhAll的权限细化记录
DELETE from eh_acls where owner_type = 'EhAll' and comment_tag1 like '%EhAuthorizationRelations.%';
-- auth分支结束

-- 将仓库管理改为园区控制 by wentian
update eh_service_modules SET module_control_type = 'community_control' where `name` = '仓库管理';
