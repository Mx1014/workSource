-- 工作流字符串模板 add by xq.tian  2017/06/05
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ('flow', 10010, 'zh_CN', '工作流多条评价时显示的摘要', '用户已评价，查看详情', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ('flow', 10011, 'zh_CN', '工作流带评价内容时显示的摘要', '用户评价：${score}分，${content}', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ('flow', 10012, 'zh_CN', '非发起人附言上传图片', '${processorName}：上传了${imageCount}张图片', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ('flow', 10013, 'zh_CN', '发起人附言上传图片', '发起人：上传了${imageCount}张图片', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ('flow', 10014, 'zh_CN', '非发起人附言', '${processorName}：${content}', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ('flow', 10015, 'zh_CN', '发起人附言', '发起人：${content}', 0);

UPDATE `eh_locale_templates` SET `text` = '任务已被${processorName}驳回' WHERE `scope` = 'flow' AND `code` = 10002;

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('flow', '10011', 'zh_CN', '评价项数量需在1-5个之间');

-- 把任务管理菜单放到内部管理下   add by xq.tian  2017/06/05
DELETE FROM `eh_web_menus` WHERE `id` IN (70000, 70100, 70200);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
VALUES (70000, '任务管理', 50000, 'fa fa-group', NULL, 1, 2, '/50000/70000', 'park', 600, 70000);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
VALUES (70100, '任务汇总', 70000, NULL, 'react:/task-management/task-list/70100', 0, 2, '/50000/70000/70100', 'park', 610, 70000);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
VALUES (70200, '业务授权视图', 70000, NULL, 'flow_view', 0, 2, '/50000/70000/70200', 'park', 620, 70000);

-- 给普通公司加上任务管理菜单  add by xq.tian  2017/06/05
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10078, 1005, 'EhAclRoles', 0, 1, NOW());

-- 给普通公司场景加任务管理icon   add by xq.tian  2017/06/05
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 1000000, 0, 0, 0, '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRveE4yVmxOak0wWkdReU9UY3dPVGMzTlRrM05UWmxOV1U1TVRneFltTTVaZw', 1, 1, 56, '', 13, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0, NULL);


-- 仓库管理menu更换datatype add by xiongying 20170607
update eh_web_menus set data_type = "react:/working-flow/flow-list/store-management/21000" where name = '工作流设置' and parent_id = 21050;

-- 修正邮箱服务器的地址和value值
UPDATE eh_configurations set `value` = 'webmail.zuolin.com' WHERE `name` = 'mail.smtp.address' and `namespace_id` = 1000000;

UPDATE eh_configurations set `value` = '465' WHERE `name` = 'mail.smtp.port' and `namespace_id` = 1000000;

UPDATE eh_configurations set `value` = 'webmail.zuolin.com' WHERE `name` = 'mail.smtp.address' and `namespace_id` = 0;

UPDATE eh_configurations set `value` = '465' WHERE `name` = 'mail.smtp.port' and `namespace_id` = 0;


-- 资源预约 add by sw 20170608
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('rental.notification', '9', 'zh_CN', '支付成功发给处理人', '客户${userName}（${userPhone}）完成支付，成功预约${useTime}的${resourceName}，请提前做好相关准备工作。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default.yzx', '50', 'zh_CN', '正中会-预成功', '47318', '999983');

UPDATE `eh_locale_templates` SET `text`='您申请预约的${useTime}的${resourceName}已通过审批，为确保您成功预约，请尽快到APP完成在线支付，感谢您的使用。'
	WHERE `scope`='rental.notification' and `code`='6' and `locale`='zh_CN' and `namespace_id`='0';

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (999983, 'flow:40400', 28, 'zh_CN', '线下支付模式,审批通过短信', '38570');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (999983, 'flow:40400', 29, 'zh_CN', '审批驳回短信', '38572');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (999983, 'flow:40400', 30, 'zh_CN', '支付成功短信', '38573');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (999983, 'flow:40400', 31, 'zh_CN', '审批线上支付模式,审批通过短信', '38574');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (999983, 'flow:40400', 32, 'zh_CN', '取消短信', '38575');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (999983, 'flow:40400', 33, 'zh_CN', '催办短信', '38832');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('flow:40400', '50', 'zh_CN', '正中会-预成功', '47318', '999983');

UPDATE eh_web_menus set data_type = 'resource--defaultParameter/status/3' WHERE id = 50912;
UPDATE eh_web_menus set data_type = 'resource_publish/status/3' WHERE id = 50914;
UPDATE eh_web_menus set data_type = 'rental_info/status/3' WHERE id = 50916;
UPDATE eh_web_menus set data_type = 'react:/working-flow/flow-list/resource-reservation/40400?status=3' WHERE id = 50920;

DELETE from eh_web_menus where id = 50918;
DELETE from eh_web_menu_scopes where menu_id = 50918;	

DELETE from eh_rentalv2_time_interval where owner_type = 'resource_half_day';
SET @id = (SELECT MAX(id) FROM `eh_rentalv2_time_interval`);

INSERT INTO `eh_rentalv2_time_interval` (`id`, `owner_id`, `owner_type`, `begin_time`, `end_time`, `time_step`) 
	select (@id := @id + 1), id, 'resource_half_day', '8', '12', '0.25' from eh_rentalv2_resources where rental_type = 1;
INSERT INTO `eh_rentalv2_time_interval` (`id`, `owner_id`, `owner_type`, `begin_time`, `end_time`, `time_step`) 
	select (@id := @id + 1), id, 'resource_half_day', '14', '18', '0.25' from eh_rentalv2_resources where rental_type = 1;

UPDATE eh_rentalv2_resources set org_member_weekend_price = weekend_price, org_member_workday_price = workday_price;
UPDATE eh_rentalv2_cells set org_member_original_price = original_price, org_member_price = price;

	
-- 删除重复的菜单配置 add by sfyan 20170609
delete from eh_web_menu_scopes where id in (select a.id from (select id from eh_web_menu_scopes group by owner_type, owner_id, menu_id having count(*) > 1) a);

-- by dengs,20170609 服务联盟消息提示格式调整 issue10974
update eh_locale_templates SET text='您收到一条${categoryName}:${serviceAllianceName}的申请
 提交者信息：
 预订人：${creatorName} 
 手机号：${creatorMobile} 
 公司名称：${creatorOrganization} 
 
 提交的信息：
${notemessage}
 您可以登录管理后台查看详情' WHERE scope='serviceAlliance.request.notification' AND `code`=1;

-- 黑名单消息 by sfyan 20170612
update `eh_locale_templates` set text = '由于您的发言涉及部分违反相关版规行为，您已被禁言，将不能正常使用部分板块的发言功能。如有疑问，请联系客服。' where scope = 'blacklist.notification' and code = 1;
update `eh_locale_templates` set text = '您的禁言已被解除，可继续使用各大板块的发言功能。如有疑问，请联系客服。' where scope = 'blacklist.notification' and code = 2;

	
