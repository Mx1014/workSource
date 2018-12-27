-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END OPERATION------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR: 李清岩
-- REMARK：访客来访提示
INSERT INTO eh_locale_templates ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('aclink.notification', '2', 'zh_CN', '访客使用了临时授权二维码进入门禁A。', '访客${visitorName}使用了临时授权二维码进入${doorName}。', '0');

-- AUTHOR: 梁燕龙
-- REMARK: 修改模块名称
UPDATE eh_service_modules SET name = '资源预订' WHERE id = 40400;

-- AUTHOR:2018年12月21日 黄鹏宇
-- REMARK:为创业场添加客户类型
SET @id = (SELECT MAX(id) FROM eh_var_field_ranges);
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',4,'investment_promotion','enterprise_customer');

-- AUTHOR:st.zheng
-- REMARK:物品放行1.2
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`)
VALUES ('49210', '申请记录', '49200', '/200/40000/49200/49210', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', 'subModule', '1', '0', NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`)
VALUES ('49220', '统计信息', '49200', '/200/40000/49200/49220', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', 'subModule', '1', '0', NULL, NULL, NULL, NULL);

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4920049210, '0', '物品放行 申请记录权限', '物品放行 申请记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '49210', '0', 4920049210, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4920049220, '0', '物品放行 统计信息权限', '物品放行 统计信息权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '49220', '0', 4920049220, '全部权限', '0', now());

-- AUTHOR: 谢旭双
-- REMARK: 同事圈默认选择器文本
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '1', 'zh_CN', '全部');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '2', 'zh_CN', '我发布的');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '3', 'zh_CN', '我点赞的');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '4', 'zh_CN', '我评论的');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '506', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '40002', 'zh_CN', '你不能删除其他人的评论');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', 'title', 'zh_CN', '同事圈');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('enterprise_default_moment', '1', 'zh_CN', '系统小助手');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('enterprise_default_moment', '2', 'zh_CN', '同事圈是公司员工交流、记录公司成长点滴的地方。在这里可以发表心情、分享经验、寻求帮助，培养融洽、和谐的办公氛围，提升团队凝聚力。');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('enterprise_default_moment', '3', 'zh_CN', '图片');

-- AUTHOR: 谢旭双
-- REMARK: 同事圈头部背景图
SET @config_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `is_readonly`) VALUES (@config_id:=@config_id+1, 'enterprise.moment.banner', 'cs://1/image/aW1hZ2UvTVRwak5HSm1ZV05pWkdZeVpUa3dZamxsT1RVeFlqUXhPRGxqWVRJMk1ETmlZUQ', '同事圈头部背景图', 1);

-- AUTHOR: 吴寒
-- REMARK: 同事圈菜单
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES('274000','同事圈','50000','/100/50000/274000','1','3','2','10','2018-09-26 16:51:46',NULL,NULL,'2018-09-26 16:51:46','0','0','0',NULL,'org_control','1','1','module','0','0',NULL,NULL,'moments',NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('79887000','同事圈','70000010',NULL,'enterprise-payment-auth','1','2','/70000010/79887000','park','8','274000','3','system','module','2','1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('79888000','同事圈','70000010',NULL,'enterprise-payment-auth','1','2','/70000010/79888000','zuolin','8','274000','3','system','module','2','1');

-- AUTHOR: 谢旭双
-- REMARK: 同事圈初始tag
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20001', 'zh_CN', '新人报道');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20002', 'zh_CN', '快讯');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20003', 'zh_CN', '求推荐');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20004', 'zh_CN', '活动');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20005', 'zh_CN', '讨论');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20006', 'zh_CN', '建议');

-- AUTHOR:刘一麟
-- REMARK:人脸识别v1.8
INSERT INTO `ehcore`.`eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('aclink.notification', '3', 'zh_CN', '您于【日期】上传的人脸照片已通过审核，现可正常使用。', '您于${dateTime}上传的人脸照片已通过审核，现可正常使用。', '0');
INSERT INTO `ehcore`.`eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('aclink.notification', '4', 'zh_CN', '您于【日期】上传的人脸照片因质量过低，未能通过审核，请重新拍摄上传。', '您于${dateTime}上传的人脸照片因质量过低，未能通过审核，请重新拍摄上传。', '0');

-- AUTHOR:st.zheng
-- REMARK:资源预约3.8.3
update eh_configurations set `value` = '/resource-rental/build/index.html#/resource-detail?namespaceId=%s&rentalSiteId=%s&detailPageType=%s&canRental=1&isApp=1#sign_suffix' where name = 'rental.resource.detail.url';

-- AUTHOR:梁燕龙
-- REMARK: 域空间管理v3.0 主页签
DELETE from eh_launch_pad_indexs;
DELETE from eh_portal_navigation_bars;
-- --------------------- SECTION END ALL -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:2018年12月13日 黄鹏宇
-- REMARK:关联缺陷#40159 修复中天的数据并且根据任务43743的代码更改数据库
update eh_enterprise_customers set source_item_id = 14788 where source_item_id = 14559 and community_id = 240111044332061061;
update eh_enterprise_customers set source_item_id = 14787 where source_item_id = 14558 and community_id = 240111044332061061;
update eh_enterprise_customers set source_item_id = 14786 where source_item_id = 14557 and community_id = 240111044332061061;
update eh_enterprise_customers set source_item_id = 14789 where source_item_id = 14560 and community_id = 240111044332061061;

update eh_enterprise_customers set source_item_id = 14788 where source_item_id = 14784 and community_id = 240111044332061061;
update eh_enterprise_customers set source_item_id = 14698 where source_item_id = 14744 and community_id = 240111044332063624;

update eh_var_field_item_scopes set status = 0 where module_name = 'investment_promotion'and field_id = 6 and community_id in (240111044332061061 ,240111044332061062 ,240111044332061063 ,240111044332061064 ,240111044332063624 ,240111044332063625 ,240111044332063626 );
update eh_var_field_item_scopes set module_name = 'enterprise_customer' where module_name = 'investment_promotion' and field_id = 6 and community_id is null and namespace_id = 999944;

update eh_var_field_item_scopes set module_name = 'enterprise_customer' where module_name = 'investment_promotion' and field_id = 24 and namespace_id = 999944;

update eh_var_field_item_scopes set status = 0 where namespace_id in (0,999954) and status = 2 and module_name = 'investment_promotion' and field_id = 24;
update eh_var_field_item_scopes set module_name = 'enterprise_customer' where namespace_id= 0 and status = 2 and module_name = 'investment_promotion' and field_id = 6;

update eh_var_field_item_scopes set status = 0 where status = 2 and module_name = 'enterprise_customer' and field_id = 5;

update eh_var_field_item_scopes set module_name = 'enterprise_customer' where status = 2 and module_name = 'investment_promotion' and field_id = 5;

update eh_var_field_item_scopes set module_name = 'enterprise_customer' where module_name = 'investment_promotion' and field_id = 12113 and status = 2 ;


-- AUTHOR:2018年12月24日 黄明波
-- REMARK:issue-44627 增加国贸服务快递的微信支持
set @max_id = (select ifnull(max(id), 0) from eh_express_companies);
set @namespace_id = 999923;
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `logistics_url`, `order_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@max_id :=  @max_id + 1, @namespace_id, 'community', 240111044832061111, 2, 'EMS', '', '国贸服务，EMS快递公司', NULL, NULL, NULL, NULL, NULL, 2, 0, now(), null, 0);
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `logistics_url`, `order_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@max_id :=  @max_id + 1, @namespace_id, 'community', 240111044832061111, 3, '中国邮政', '', '国贸服务，中国邮政快递公司', NULL, NULL, NULL, NULL, NULL, 2, 0, now(), null, 0);
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `logistics_url`, `order_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@max_id :=  @max_id + 1, @namespace_id, 'community', 240111044832061111, 4, '国贸物业酒店管理有限公司', '', '国贸项目，国贸快递业务对应的公司', NULL, NULL, NULL, NULL, NULL, 1, 0, now(), null, 0);


set @max_id = (select ifnull(max(id), 0) from eh_express_company_businesses);
INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@max_id :=  @max_id + 1, @namespace_id, 'EhNamespaces', @namespace_id, 3, 9, '邮政快递包裹', '[{"packageType": 3},{"packageType": 2},{"packageType": 1}]', '请确认寄件物品价值不超过2万元，贵重物品务必保价，保价费=保价金额*1%，最低2元', '[{"status": 1},{"status": 2},{"status": 5},{"status": 4}]', 1, 2, 0, now(), null, 0);
INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@max_id :=  @max_id + 1, @namespace_id, 'EhNamespaces', @namespace_id, 3, 2, '同城信筒快件', '[{"packageType": 4},{"packageType": 5}]', NULL, '[{"status": 1},{"status": 2},{"status": 5},{"status": 4}]', 1, 2, 0, now(), null, 0);
INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@max_id :=  @max_id + 1, @namespace_id, 'EhNamespaces', @namespace_id, 2, 3, 'EMS标准快递', '[]', '请确认寄件物品价值不超过5万元，贵重物品务必保价，保价费=保价金额*0.5%，最低1元', '[{"status": 1},{"status": 2},{"status": 5},{"status": 4}]', 2, 2, 0, now(), null, 0);
INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@max_id :=  @max_id + 1, @namespace_id, 'EhNamespaces', @namespace_id, 4, 10, '国贸快递', '[]', NULL, '[{"status": 6},{"status": 5},{"status": 4}]', 3, 2, 0, now(), null, 0);


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
-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本
-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本

-- AUTHOR: 杨崇鑫 2018-12-17
-- REMARK: 瑞安项目专用、同瑞安CM系统服务账单同步失败的账单展示
INSERT INTO `eh_service_module_functions`(`id`, `module_id`, `privilege_id`, `explain`) VALUES (102, 20400, 0, 'CM同步失败账单');
-- AUTHOR: 杨崇鑫 2018-12-17
-- REMARK: 瑞安项目专用、同瑞安CM系统服务账单同步失败的账单展示
set @id=(select ifnull((SELECT max(id) from eh_service_module_include_functions),1));
INSERT INTO `eh_service_module_include_functions`(`id`, `namespace_id`, `module_id`, `community_id`, `function_id`) VALUES (@id:= @id +1, 999929, 20400, NULL, 102);

-- AUTHOR: 黄鹏宇 2018-12-17
-- REMARK: 瑞安屏蔽同步客户的按钮
set @id= (select max(id)+1 from eh_service_module_exclude_functions);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id, 999929, NULL, 21100, 99);


-- --------------------- SECTION END ruianxintiandi ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: jinmao
-- DESCRIPTION: 此SECTION只在上海金茂-智臻生活 -999925执行的脚本
-- --------------------- SECTION END jinmao ------------------------------------------