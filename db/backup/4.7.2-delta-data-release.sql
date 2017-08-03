-- 用户申诉手机号成功消息内容  add by xq.tian  2017/07/12
SET @locale_max_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '3', 'zh_CN', '您修改手机号的申请已被拒绝，若有疑问请联系客服，感谢您的使用。');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300005', 'zh_CN', '请回到第一步重试');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300006', 'zh_CN', '验证码错误或已过期');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300007', 'zh_CN', '审核申诉失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300008', 'zh_CN', '该手机号码已被注册');


-- 申诉短信模板 add by xq.tian  2017/07/12
SET @max_template_id = (SELECT max(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-科技园', '90012', 1000000);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-海岸', '90015', 999993);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-深业', '90018', 999992);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-威新', '90019', 999991);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-储能', '90020', 999990);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-ibase', '90021', 999989);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-爱特家', '90022', 999988);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-清华', '90091', 999984);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-创源', '90026', 999986);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-华润', '90027', 999985);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-T空间', '90028', 999982);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-正中会', '90029', 999983);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-星商汇', '90030', 999981);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-全至100', '90031', 999980);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-光大', '90034', 999979);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-Volgo', '90035', 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-名网邦', '90037', 999976);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-荣超', '90038', 999975);


--
-- 修改用户认证的菜单data_type
--
UPDATE `eh_web_menus` SET `data_type` = 'react:/identification-management/user-identification' WHERE `id` = 35000;
UPDATE `eh_web_menus` SET `name` = '员工认证', `data_type` = 'react:/identification-management/employee-identification' WHERE `id` = 50500;
UPDATE `eh_web_menus` SET `name` = '员工认证', `data_type` = 'react:/identification-management/employee-identification' WHERE `id` = 505000;

-- 企业人才申请的发消息，add by tt, 20170710
select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id+1, 'talent.notification', 1, 'zh_CN', '企业人才申请时发消息', '企业人才|收到一条企业人才的申请单', 0);


-- merge from activity-3.4.0 start 20170719  by yanjun

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`)
VALUES (10640, '旅游与团建', 10000, null, 'forum_activity/3', 0, 2, '/10000/10640', 'park', 600, 10600, 2, 'project', 'module');

SET @namespace_id = 999983;
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 10640, '', 'EhNamespaces', @namespace_id, 2);

SET @item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) select @item_id:=@item_id+1, @namespace_id, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, '旅游与团建', '旅游与团建', 'cs://1/image/aW1hZ2UvTVRvMVlXSTNOalEyTWpaa01XUTRPRGRrWXpJell6YzBNalk0TkdFNVlXWTBaQQ', `item_width`, `item_height`, 50, '{"categoryId":3}', `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri` from `eh_launch_pad_items` where `item_label` = '通知' and namespace_id = @namespace_id;

insert into `eh_activity_categories`
(`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
values('1000049','','0','3','-1','旅游与团建','/3','0','2','1','2017-05-24 11:01:42','0',NULL,@namespace_id,'0','1','cs://1/image/aW1hZ2UvTVRvMVlXSTNOalEyTWpaa01XUTRPRGRrWXpJell6YzBNalk0TkdFNVlXWTBaQQ',NULL,NULL,'0');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) select @item_id:=@item_id+1, @namespace_id, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, '招聘与求职', '招聘与求职', 'cs://1/image/aW1hZ2UvTVRvek4yWmpNMlU1WkRJek5EQXpNMk16WVRrd1ltTXlPR1E1WlRRNVpqVmxOUQ', `item_width`, `item_height`, 62, '{"tag":"招聘与求职"}', `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri` from `eh_launch_pad_items` where `item_label` = '通知' and namespace_id = @namespace_id;

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) select @item_id:=@item_id+1, @namespace_id, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, '园区生活', '园区生活', 'cs://1/image/aW1hZ2UvTVRveVl6RTFOREF4WTJKbU56ZG1ZMlkyTkdVMk1EUTRaREUwWlRFeU1URXdaUQ', `item_width`, `item_height`, 62, '{"tag":"二手交易"}', `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri` from `eh_launch_pad_items` where `item_label` = '通知' and namespace_id = @namespace_id;

-- merge from activity-3.4.0 end 20170719  by yanjun

-- 电商首页运营API  add by xq.tian  2017/07/20
UPDATE `eh_configurations` SET VALUE = 'zl-ec/rest/openapi/commodity/listRecommend' WHERE `name` = 'biz.business.promotion.api';

-- 清华信息港初始数据 停车充值  add by sw 20170720
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.zijing.url', 'http://tgd.poapi.parkingjet.cn:8082/CommonOpenApi/default.ashx', NULL, '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.zijing.appid', '201706221000', NULL, '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.zijing.appkey', 'qyruirxn20145601739', NULL, '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.zijing.parkingid', '0755000120170301000000000003', NULL, '0', NULL);

-- 资源预约 add by sw 20170720
UPDATE eh_rentalv2_orders set organization_id = NULL;



-- 开放内部管理和管理员管理的模块权限分配 add by sfyan 20170721
update `eh_service_modules` set type = 1 where id in (42100, 41400, 50000, 60000, 60100, 60200) or path like '/50000/%';
update `eh_service_modules` set path = '/50000/52000' where id = 52000;
update `eh_service_modules` set name = '责任部门配置' where id = 60200;

update `eh_web_menus` set `module_id` = 50100 where path like '/50000/50100%';
update `eh_web_menus` set `module_id` = 50200 where path like '/50000/50200%';
update `eh_web_menus` set `module_id` = 50300 where path like '/50000/50300%';
update `eh_web_menus` set `module_id` = 50400 where path like '/50000/50400%';
update `eh_web_menus` set `module_id` = 50500 where path like '/50000/50500%';
update `eh_web_menus` set `module_id` = 50600 where path like '/50000/50600%';
update `eh_web_menus` set `module_id` = 50700 where path like '/50000/50700%';
update `eh_web_menus` set `module_id` = 50800 where path like '/50000/50800%';
update `eh_web_menus` set `module_id` = 50900 where path like '/50000/50900%';
update `eh_web_menus` set `module_id` = 51000 where path like '/50000/51000%';
update `eh_web_menus` set `module_id` = 52000 where path like '/50000/52000%';
update `eh_web_menus` set `module_id` = 60100 where path like '/60000/60100%';
update `eh_web_menus` set `module_id` = 60200 where path like '/60000/60200%';


-- fix bug 12536 by xiongying20170721
update `eh_locale_strings` set text = '抱歉你没有代发权限' where scope = 'pmtask' and code = '10012';

-- 给所有域空间配上内部管理模块以及管理模块  add by sfyan 20170721
delete from eh_service_module_scopes where module_id in (select module_id from eh_web_menus where path like '%/50000/%' or id in (42100, 41400, 50000, 60000, 60100) group by module_id having module_id is not null) or module_id in (50000, 60000);
SET @service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) select (@service_module_scopes_id := @service_module_scopes_id + 1), ewms.owner_id, ifnull(ewm.module_id, ewm.id),2 from eh_web_menu_scopes ewms left join eh_web_menus ewm on ewms.menu_id = ewm.id where owner_type = 'EhNamespaces' and (ewm.path like '%/50000/%' or ewm.id in (42100, 41400, 50000, 60000, 60100)) group by ifnull(ewm.module_id, ewm.id),ewms.owner_id;

-- 配置模块下的权限分类  add by sfyan 20170725
SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(60110,'超级管理员',60100,'/60000/60100/60110','1','3','2','0',NOW()); 
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40007, '0', 'super.admin.list', '超级管理员列表查询', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60110','0',40007,'超级管理员列表查询','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40008, '0', 'super.admin.create', '超级管理员列表查询', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60110','0',40008,'创建修改超级管理员','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40010, '0', 'super.admin.delete', '超级管理员列表查询', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60110','0',40010,'删除超级管理员','0',NOW());


INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(60120,'模块管理员',60100,'/60000/60100/60120','1','3','2','0',NOW()); 
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40019, '0', 'module.admin.list', '模块管理员列表查询', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60120','0',40019,'模块管理员列表查询','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40020, '0', 'module.admin.create', '创建模块超级管理员', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60120','0',40020,'创建模块超级管理员','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40021, '0', 'module.admin.update', '修改模块管理员', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60120','0',40021,'修改模块管理员','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40022, '0', 'module.admin.delete', '删除模块管理员', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60120','0',40022,'删除模块管理员','0',NOW());

-- 暂时不开放业务责任部门分配的模块权限  add by sfyan 20170725
delete from eh_service_module_scopes where module_id = 60200;	