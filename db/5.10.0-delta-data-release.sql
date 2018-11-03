-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR:xq.tian 20181016
-- REMARK:网关及注册中心部署.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539677844

-- AUTHOR:梁燕龙 20181016
-- REMARK:统一用户上线操作.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539678631

-- AUTHOR:杨崇鑫 20181018
-- REMARK:解决缺陷 #39352: 【全量标准版】【物业缴费】新增收费标准前端提示成功，但实际未新增成功，无相关数据，后台提示“应用开小差”
-- REMARK：备份eh_payment_variables表
-- select * from eh_payment_variables;

-- AUTHOR:杨崇鑫 20181027
-- REMARK:解决缺陷 #39571:
-- 第一步请执行在es上执行db/search/energy_task.sh
-- 第二步执行同步接口/energy/syncEnergyTaskIndex

-- AUTHOR:梁家声 20181030
-- REMARK: 对照营销的core_key，在eh_apps中插入营销core_key对


-- --------------------- SECTION END OPERATION------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR:唐岑2018年10月23日14:29:41
-- REMARK:添加资产报表定时配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('schedule.property.task.time', '0 30 2 * * ?', '资产报表定时任务', '0', NULL, '1');

-- AUTHOR: 杨崇鑫 20181023
-- REMARK: 新增“资产报表中心”模块和菜单
set @module_id=230000; -- 模块Id（运维分配的）
set @data_type='property-statistic';-- 前端发给你的页面跳转链接
-- 新增模块 eh_service_modules
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES (230000, '资产报表中心', 130000, '/200/130000/230000', 1, 3, 2, 10, UTC_TIMESTAMP(), NULL, NULL, UTC_TIMESTAMP(), 0, 0, '0', 0, 'unlimit_control', 1, 0, NULL, NULL, 1, 1, 'module');
-- 新增模块菜单 eh_web_menus
-- 左邻后台:zuolin、园区：park
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`)
	VALUES (79850000, '资产报表中心', 17000000, NULL, @data_type, 1, 2, '/27000000/17000000/79850000', 'zuolin', 10, 230000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`)
	VALUES (79860000, '资产报表中心', 49000000, NULL, @data_type, 1, 2, '/40000040/49000000/79860000', 'park', 10, 230000, 3, 'system', 'module', 2, 1);













-- AUTHOR:黄鹏宇 2018年10月22日
-- REMARK:将计划任务中的拜访时间改为计划时间
update eh_var_fields set display_name = '计划时间' where display_name='拜访时间' and group_id = 20;
update eh_var_field_scopes set field_display_name = '计划时间' where field_display_name='拜访时间' and group_id = 20;


-- AUTHOR:黄鹏宇 2018年10月22日
-- REMARK:更改module表中的client_handler_type类型为外部链接
update eh_service_modules set client_handler_type = 2 where id = 25000;
update eh_service_modules set client_handler_type = 2 where id = 150020;







UPDATE eh_service_modules SET client_handler_type = 1 WHERE id in (90100,  180000);

-- AUTHOR:严军 201801030
-- REMARK: issue-null 设置路由相关参数
UPDATE eh_service_modules SET client_handler_type = 2 WHERE id = 40500;
UPDATE eh_service_modules SET `host` = 'workflow' WHERE id = 13000;
UPDATE eh_service_modules SET `host` = 'community-map' WHERE id = 40070;



-- AUTHOR: 吴寒
-- REMARK: 打卡考勤V8.2 - 支持人脸识别关联考勤；支持自动打卡
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.create','1','zh_CN','打卡发送消息','${createType}: ${punchTime}','0');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','1','zh_CN','自动打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','2','zh_CN','人脸识别打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','3','zh_CN','门禁打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','4','zh_CN','其他第三方打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('oa.unit','1','zh_CN','天');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('oa.unit','2','zh_CN','次');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('remind.msg','1','zh_CN','日程提醒');

INSERT INTO eh_locale_strings(scope,CODE,locale,TEXT) VALUE( 'PunchStatusStatisticsItemName', 11, 'zh_CN', '外出');

-- AUTHOR: 吴寒
-- REMARK: 日程提醒给共享人发消息
SET @tem_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','6','zh_CN','创建给被共享人发消息','${trackContractName}共享了日程“${planDescription}” ','0');

-- AUTHOR: 马世亨
-- REMARK: 邀请者查看邀请详情路由修改
UPDATE `eh_configurations` SET `value`='%s/visitor-appointment/build/index.html?detailId=%s&ns=%s#/appointment-detail#sign_suffix' WHERE `name`='visitorsys.inviter.route';

-- AUTHER：李清岩 20181019
-- REMARK: 新增公共门禁权限子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41011, '门禁授权', '40000', '/200/40000/41010/41011', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41012, '门禁日志', '40000', '/200/40000/41010/41012', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41013, '数据统计', '40000', '/200/40000/41010/41013', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41014, '移动端管理', '40000', '/200/40000/41010/41014', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- REMARK: 新增企业门禁权限子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41021, '门禁授权', '310000', '/100/310000/41020/4102', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41022, '门禁日志', '310000', '/100/310000/41020/41022', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41023, '数据统计', '310000', '/100/310000/41020/41023', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41024, '移动端管理', '310000', '/100/310000/41020/41024', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- 新增门禁权限项
INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041010, 0, '公共门禁 全部权限', '公共门禁 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041011, 0, '公共门禁 门禁授权', '公共门禁 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041012, 0, '公共门禁 门禁日志', '公共门禁 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041013, 0, '公共门禁 数据统计', '公共门禁 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041014, 0, '公共门禁 移动端管理', '公共门禁 移动端管理权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041020, 0, '企业门禁 全部权限', '企业门禁 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041021, 0, '企业门禁 门禁授权', '企业门禁 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041022, 0, '企业门禁 门禁日志', '企业门禁 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041023, 0, '企业门禁 数据统计', '企业门禁 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041024, 0, '企业门禁 移动端管理', '企业门禁 移动端管理权限', NULL );
-- AUTHER：李清岩 20181019
-- 模块权限关联
SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41010', '0', 4101041010, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41011', '0', 4101041011, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41012', '0', 4101041012, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41013', '0', 4101041013, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41014', '0', 4101041014, '移动端管理权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41020', '0', 4102041020, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41021', '0', 4102041021, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41022', '0', 4102041022, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41023', '0', 4102041023, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41024', '0', 4102041024, '移动端管理权限', '0', NOW());
-- AUTHER：李清岩 20181019
-- 新增两个门禁模块：公共门禁beta，企业门禁beta
INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 16030610, '公共门禁beta', 16030000, NULL, 'public-access', 1, 2, '/16000000/16030000/16030610', 'zuolin', 40, 41110, 3, 'system', 'module', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 16030620, '公共门禁beta', 45000000, NULL, 'public-access', 1, 2, '/40000040/45000000/16030620', 'park', 30, 41110, 3, 'system', 'module', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 48140010, '企业门禁beta', 16040000, NULL, 'company-access', 1, 2, '/23000000/16040000/48140010', 'zuolin', 60, 41120, 3, 'system', 'module', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 48140020, '企业门禁beta', 48000000, NULL, 'company-access', 1, 2, '/40000010/48000000/48140020', 'park', 80, 41120, 3, 'system', 'module', NULL );
-- AUTHER：李清岩 20181019
-- 新增公共门禁beta子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41110, '公共门禁beta', '40000', '/200/40000/41110', '1', '3', '2', 10, NOW(), '{\"isSupportQR\":1,\"isSupportSmart\":0}', '78', NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'module' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41111', '门禁授权', '40000', '/200/40000/41110/41111', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41112', '门禁日志', '40000', '/200/40000/41110/41112', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41113', '数据统计', '40000', '/200/40000/41110/41113', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41114', '移动端管理', '40000', '/200/40000/41110/41114', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- 新增企业门禁beta子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41120', '企业门禁beta', '310000', '/100/310000/41120', '1', '3', '2', '100', NOW(), '{\"isSupportQR\":1,\"isSupportSmart\":0}', '79', NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'module' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41121', '门禁授权', '310000', '/100/310000/41120/41121', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41122', '门禁日志', '310000', '/100/310000/41120/41122', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41123', '数据统计', '310000', '/100/310000/41120/41123', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41124', '移动端管理', '310000', '/100/310000/41120/41124', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- 新增公共门禁beta权限
INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041110, 0, '公共门禁beta 全部权限', '公共门禁beta 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041111, 0, '公共门禁beta 门禁授权', '公共门禁beta 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041112, 0, '公共门禁beta 门禁日志', '公共门禁beta 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041113, 0, '公共门禁beta 数据统计', '公共门禁beta 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041114, 0, '公共门禁beta 移动端管理', '公共门禁beta 移动端管理权限', NULL );
-- AUTHER：李清岩 20181019
-- 新增企业门禁beta权限
INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041120, 0, '企业门禁beta 全部权限', '企业门禁beta 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041121, 0, '企业门禁beta 门禁授权', '企业门禁beta 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041122, 0, '企业门禁beta 门禁日志', '企业门禁beta 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041123, 0, '企业门禁beta 数据统计', '企业门禁beta 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041124, 0, '企业门禁beta 移动端管理', '企业门禁beta 移动端管理权限', NULL );
-- AUTHER：李清岩 20181019
-- 模块权限关联
SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41110', '0', 4111041110, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41111', '0', 4111041111, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41112', '0', 4111041112, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41113', '0', 4111041113, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41114', '0', 4111041114, '移动端管理权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41120', '0', 4112041120, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41121', '0', 4112041121, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41122', '0', 4112041122, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41123', '0', 4112041123, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41124', '0', 4112041124, '移动端管理权限', '0', NOW());
-- AUTHER：李清岩 20181019
-- 新增左邻后台门禁管理模块
INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 79300000, '智能硬件', 15000000, NULL, NULL, 1, 2, '/15000000/79300000', 'zuolin', 60, NULL, 2, 'system', 'classify', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 79410000, '门禁管理', 79300000, NULL, 'access-management', 1, 2, '/15000000/79300000/79410000', 'zuolin', 1, 70400, 3, 'system', 'module', NULL );
-- AUTHER：李清岩 20181019
-- 更新门禁设备所属城市id
UPDATE eh_door_access dc LEFT JOIN eh_communities c ON c.id = dc.owner_id SET dc.city_id = c.city_id WHERE dc.owner_type = 0;

UPDATE eh_door_access dc LEFT JOIN ( SELECT DISTINCT r.member_id organization_id, c.city_id city_id FROM eh_organization_community_requests r LEFT JOIN eh_communities c ON c.id = r.community_id ) t ON t.organization_id = dc.owner_id SET dc.city_id = t.city_id WHERE dc.owner_type = 1;
-- AUTHER：李清岩 20181019
-- 更新门禁设备所属域空间id
UPDATE eh_door_access dc LEFT JOIN eh_communities c ON c.id = dc.owner_id SET dc.namespace_id = c.namespace_id WHERE dc.owner_type = 0;

UPDATE eh_door_access dc LEFT JOIN eh_organizations t ON dc.owner_id = t.id SET dc.namespace_id = t.namespace_id WHERE dc.owner_type = 1;

-- AUTHOR: 张智伟 20180912
-- REMARK: issue-37602 审批单支持编辑
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '终止并重新提交', '终止并重新提交', '{"nodeType":"APPROVAL_CANCEL_AND_RESUMBIT"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '复制审批单', '复制审批单', '{"nodeType":"APPROVAL_COPY_FORM_VALUES"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '显示撤销规则', '显示撤销规则', '{"nodeType":"APPROVAL_SHOW_CANCEL_INFO"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '编辑当前节点表单', '编辑当前节点表单', '{"nodeType":"APPROVAL_EDIT_CURRENT_FORM"}', 2);

-- AUTHOR: 张智伟 20180912
-- REMARK: issue-37602 审批单支持编辑
SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '30002', 'zh_CN', '关联表单需要填写才能进入下一步');

-- AUTHOR: 张智伟 20181031
-- REMARK: issue-40880 离职申请执行下一步，提示“No rights to remove the admin.”无法执行成功。
SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id +1, 'archives', '200002', 'zh_CN', '无法删除管理员，请先解除其管理权限再重试');


-- AUTHOR: 黄良铭
-- REMARK: 修改积分系统状态
UPDATE  eh_point_systems SET STATUS='2' ,point_exchange_flag='1' WHERE id = 1;

-- AUTHOR: 马世亨 20181031
-- REMARK: 访客1.3合并访客与访客管理后清除app
-- REMARK: 访客1.3园区访客地址修改
delete from eh_service_module_apps where module_id in (42100,52200);
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-appointment/build/index.html?ns=%s&appId=%s&ownerType=community&sceneType=1#/home#sign_suffix"}' where id = 41800;

-- AUTHOR: 黄明波
-- REMARK: 修改默认新闻为 NewsFlash
update eh_service_modules set  instance_config = replace(instance_config, '}', ',"widget":"NewsFlash"}') , action_type = 55, client_handler_type =  0  where id = 10800 and instance_config not like '%"widget"%';

update eh_service_module_apps set  instance_config = replace(instance_config, '}', ',"widget":"NewsFlash"}') , action_type = 55  where module_id = 10800 and instance_config not like '%"widget"%' ;

update eh_launch_pad_items set action_type=55, action_data = replace(action_data, '"News"', '"NewsFlash"')
where namespace_id=999938 and action_type in (48, 55) and action_data like '%"widget"%';

update eh_launch_pad_items set action_type=55, action_data = replace(action_data, '}', ',"widget":"NewsFlash"}')
where namespace_id=999938 and  action_type in (48, 55) and action_data not like '%"widget"%';

update eh_service_modules set client_handler_type = 2 where id = 10500;



-- AUTHOR: 黄鹏宇 2018年11月1日
-- REMARK: 更改楼宇房源
update eh_var_fields set display_name = '楼宇' where id = 10965;
update eh_var_fields set display_name = '房源' where id = 10966;
update eh_var_field_scopes set field_display_name = '楼宇' where field_id = 10965 and field_display_name = '楼栋';
update eh_var_field_scopes set field_display_name = '房源' where field_id = 10966 and field_display_name = '门牌名称';

-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 对接国贸报错信息 20181022
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10026', 'zh_CN', '用户不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10027', 'zh_CN', '初始化失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10028', 'zh_CN', '获取数据失败');

-- AUTHOR: 缪洲 2018年11月1日
--REMARK: 把资源预约，停车缴费，云打印加入企业支付授权
UPDATE eh_service_module_apps SET enable_enterprise_pay_flag = 1 WHERE module_id in (40800,41400,40400);

-- AUTHOR: 缪洲
-- REMARK: 增加用户自定义上传资料与默认车牌的默认值
UPDATE eh_parking_lots SET default_data = 'identity,driver,driving';
UPDATE eh_parking_lots SET default_plate = '粤,B';
-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 支持多应用服务类型 20181025
INSERT INTO `eh_pm_task_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('6', '0', '0', '物业报修', '物业报修', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '0', NULL, '0');
INSERT INTO `eh_pm_task_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('9', '0', '0', '投诉建议', '投诉建议', '0', '2', '2017-12-04 13:09:45', NULL, NULL, NULL, '0', NULL, '0');

-- AUTHOR: 黄鹏宇 2018年11月1日
-- REMARK: 更改楼宇房源
update eh_general_forms set template_text = replace(template_text,'楼栋门牌','楼宇房源') where module_id = 25000;
update eh_general_form_templates set template_text = '[{
	"dynamicFlag": 0,
	"fieldDesc": "客户名称",
	"fieldDisplayName": "客户名称",
	"fieldExtra": "{}",
	"fieldName": "客户名称",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"remark": "系统自动获取客户管理中该项目下所有客户信息供用户选择；",
	"disabled": 1,
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
},
{
	"dynamicFlag": 0,
	"fieldDesc": "楼宇房源",
	"fieldDisplayName": "楼宇房源",
	"fieldExtra": "{}",
	"fieldName": "楼宇房源",
	"fieldType": "SINGLE_LINE_TEXT",
	"remark": "系统自动获取资产管理中该项目下所有待租门牌供用户选择；",
	"disabled": 1,
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
},
{
	"dynamicFlag": 0,
	"fieldDesc": "审批状态",
	"fieldDisplayName": "审批状态",
	"fieldExtra": "{}",
	"fieldName": "审批状态",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"remark": "系统自动根据不同的触发不同的操作；",
	"disabled": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
}]' where module_id = 25000;

-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 国贸对接项目标识 20181031
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('pmtask.handler-999948', 'archibus', 'archibus handler', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('pmtask.archibus.areaid-999948', '000000201807140AU8ME', '物业报修国贸区域ID', '0', NULL, NULL);

-- AUTHOR: 缪洲
-- REMARK: 科兴科学园发票类型字段
UPDATE eh_parking_lots SET config_json = '{"tempfeeFlag":0,"rateFlag":0,"lockCarFlag":0,"searchCarFlag":1,"currentInfoType":2,"contact":"18718523489","invoiceFlag":1,"businessLicenseFlag":0,"vipParkingFlag":0,"monthRechargeFlag":1,"identityCardFlag":1,"monthCardFlag":1,"noticeFlag":0,"flowMode":3,"invoiceTypeFlag":1}' WHERE id = 10006;


-- AUTHOR: 吴寒z
-- REMARK: 会议室预定发邮件的内容修改
UPDATE  eh_locale_templates SET TEXT = '主题：${meetingSubject}|时间：${meetingBeginTime}|地点：${meetingRoomName}|发起人：${meetingSponsorName}|参会人：${meetingUserList}||${content}' WHERE  CODE =1000005 AND scope = 'meetingMessage';


-- AUTHOR: 梁燕龙 20181026
-- REMARK: 广告管理修改为多应用
UPDATE eh_service_modules SET multiple_flag = 1 WHERE id = 10400;
-- AUTHOR: 梁燕龙 20181026
-- REMARK: 广告管理修改为多应用
-- 刷app值
UPDATE eh_service_module_apps SET instance_config = '{"categoryId":0}' WHERE module_id = 10400;
-- AUTHOR: 梁燕龙 20181026
-- REMARK: 广告管理修改为多应用
-- 刷广告数据入口
UPDATE eh_banners SET category_id = 0;

-- AUTHOR: 梁燕龙
-- REMARK: issue-36940 用户认证，邮箱认证提示文案
SET @max_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
VALUES (@max_id:=@max_id+1,'organization', 900039, 'zh_CN', '该邮箱已被认证');

-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.8 多应用 任务迁移
-- REMARK: 执行前备份eh_pm_tasks表！！！
update eh_pm_tasks t1,eh_categories t2 set t1.app_id = t2.parent_id where t2.id = t1.task_category_id;
update eh_pm_tasks t1,eh_service_module_apps t2 set t1.app_id = t2.origin_id where t2.module_id = 20100 and t1.namespace_id = t2.namespace_id and t1.app_id = t2.custom_tag;
-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.8 多应用 通用配置迁移
update eh_pm_task_configs t1,eh_service_module_apps t2 set t1.app_id = t2.origin_id where t2.module_id = 20100 and t1.namespace_id = t2.namespace_id and t1.task_category_id = t2.custom_tag;
-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.8 多应用 物业报修类型迁移
insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id = 6;

insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id in (select t2.id from eh_categories t2 where t2.parent_id = 6);

insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id in (select t2.id from eh_categories t2 where t2.parent_id in (select t3.id from eh_categories t3 where t3.parent_id = 6));

update eh_pm_task_categories t1,eh_service_module_apps t2 set t1.app_id = t2.origin_id where t2.module_id = 20100 and t2.custom_tag = 6 and t1.namespace_id = t2.namespace_id;
-- 物业报修V3.8 多应用 物业报修类型迁移 END

-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.8 多应用 投诉建议类型迁移
insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id = 9;

insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id in (select t2.id from eh_categories t2 where t2.parent_id = 9);

insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id in (select t2.id from eh_categories t2 where t2.parent_id in (select t3.id from eh_categories t3 where t3.parent_id = 9));

update eh_pm_task_categories t1,eh_service_module_apps t2 set t1.app_id = t2.origin_id where t2.module_id = 20100 and t2.custom_tag = 9 and t1.namespace_id = t2.namespace_id;
-- 物业报修V3.8 多应用 投诉建议类型迁移 END

-- --------------------- SECTION END ALL -----------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 把基线的 2 域空间删掉，标准版不执行这个 sql
DELETE FROM eh_namespaces WHERE id=2;

-- --------------------- SECTION END zuolin-base ---------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-standard
-- DESCRIPTION: 此SECTION只在左邻标准版执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 标准版数据库的标识
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
  VALUES ('server.standard.flag','true','标准版 server 标识',2, '标准版 server 标识');

-- --------------------- SECTION END zuolin-base ---------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END dev -----------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END guangda -------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END szbay ---------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END chuangyechang -------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END anbang---------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: nanshanquzhengfu
-- DESCRIPTION: 此SECTION只在南山区政府-999931执行的脚本
-- AUTHOR: 黄良铭
-- REMARK: 新增ＩＤ为1的积分系统
INSERT INTO `eh_point_systems` (`id`, `namespace_id`, `display_name`, `point_name`, `point_exchange_flag`, `exchange_point`, `exchange_cash`, `user_agreement`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES('1','0','固定数据','固定数据　','1','1','2','','2','2018-10-27 10:48:04.621','2',NULL,NULL);

-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 越空间独立部署的 root 用户的密码修改为: eh#1802

-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- AUTHOR:梁燕龙  20181022
-- REMARK: 瑞安个人中心跳转URL

-- --------------------- SECTION END ruianxintiandi ------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
