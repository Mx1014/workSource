
-- 发短信模板 

SET @id = (SELECT MAX(id) FROM eh_locale_templates) +1;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@id := @id + 1,'sms.default.yzx','12','zh_CN','付费预约成功 独占资源','31934','1000000');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@id := @id + 1,'sms.default.yzx','13','zh_CN','付费预约成功 非独占资源，不需要选择资源编号','32008','1000000');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@id := @id + 1,'sms.default.yzx','14','zh_CN','付费预约成功 非独占资源，需要选择资源编号','32009','1000000');


-- 修改之前审批发送消息模板，add by wh, 20161018
UPDATE `eh_locale_templates` SET TEXT = '${creatorName}提交了请假申请，请假时间：${time}，请及时进行处理。' WHERE scope = 'approval.notification' AND CODE = 11;
UPDATE `eh_locale_templates` SET TEXT = '${creatorName}提交了请假申请，请假时间：${time}，${approver}已同意，请及时进行处理。' WHERE scope = 'approval.notification' AND CODE = 12;
UPDATE `eh_locale_templates` SET TEXT = '${creatorName}提交了异常申请，请及时进行处理。' WHERE scope = 'approval.notification' AND CODE = 21;
UPDATE `eh_locale_templates` SET TEXT = '${creatorName}提交了异常申请，${approver}已同意，请及时进行处理。' WHERE scope = 'approval.notification' AND CODE = 22; 



-- 审批发送消息模板，add by wh, 20161018
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 31, 'zh_CN', '用户提交加班申请', '${creatorName}提交了加班申请，申请${date}加班${hour}小时，请及时进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 32, 'zh_CN', '加班申请到下一级别', '${creatorName}提交了加班申请，申请${date}加班${hour}小时，${approver}已同意，请及时进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 33, 'zh_CN', '加班申请通过', '您的加班申请：${date}加班${hour}小时，已通过审批。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 34, 'zh_CN', '加班申请驳回', '您的加班申请：${date}加班${hour}小时，被${approver}驳回，理由是：${reason}。', 0);
 
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.title', 1, 'zh_CN', '提交申请', '${nickName}提交了${approvalType}申请', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.title', 2, 'zh_CN', '提交申请', '等待${nickNames}审批', 0);
 
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.title', 3, 'zh_CN', '加班申请列表', '${nickName}申请${date}加班${hour}小时', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.title', 4, 'zh_CN', '请假申请列表', '${nickName}申请${category}(共${day}${hour}${min})\n${beginDate}到${endDate}', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.title', 5, 'zh_CN', '异常申请列表', '${nickName}对${date}提交异常申请', 0); 
 
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.title', 6, 'zh_CN', '加班申请详情title', '${date}加班${hour}小时\n打卡记录:${punchLog}', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.title', 7, 'zh_CN', '请假申请详情title', '${category}(共${day}${hour}${min})\n${beginDate}到${endDate}', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.title', 8, 'zh_CN', '异常申请详情title', '${date}\n打卡记录:${punchLog}\n打卡状态:${punchStatus}', 0); 
  
  
  


-- 审批错误提示，added by wh ,2016-10-20 
SET @id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'time.unit', 'day', 'zh_CN', '天');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'time.unit', 'hour', 'zh_CN', '小时');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'time.unit', 'min', 'zh_CN', '分钟');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10022', 'zh_CN', '所选日期已提交过加班申请，请重新选择');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10023', 'zh_CN', '申请单已经被审批');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'ApprovalStatus', '0', 'zh_CN', '待审批');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'ApprovalStatus', '1', 'zh_CN', '已同意');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'ApprovalStatus', '2', 'zh_CN', '已驳回');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval.type', '3', 'zh_CN', '加班');



-- 广场加入我的审批

SET @id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (@id:=@id+1, 999992, '0', '0', '0', '/home', 'Bizs', 'MY_APPROVAL', '我的审批', 'cs://1/image/aW1hZ2UvTVRwaU1HUmtaR00yTlRjMFpESTJZbVpqTW1Vd05UTTFPVEprTkdVMU4yTTJNZw', '1', '1', '54', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (@id:=@id+1, 999992, '0', '0', '0', '/home', 'Bizs', 'MY_APPROVAL', '我的审批', 'cs://1/image/aW1hZ2UvTVRwaU1HUmtaR00yTlRjMFpESTJZbVpqTW1Vd05UTTFPVEprTkdVMU4yTTJNZw', '1', '1', 54,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
    
    
     
-- 邮箱认证发邮件内容模板，add by wh,2016-10-27
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES (@id:=@id+1, 'verify.mail', 1, 'zh_CN', '用户提交加班申请', '尊敬的${nickName}：\n您好，感谢您使用${appName}，点击下面的链接进行邮箱验证：\n${verifyUrl}\n如果链接没有跳转，请直接复制链接地址到您的浏览器地址栏中访问。（30分钟内有效）\n \n此邮件为系统邮件，请勿直接回复。\n \n如非本人操作，请忽略此邮件。\n \n谢谢，${appName}', 0);

-- 邮箱认证发邮件标题，added by wh ,2016-10-27
SET @id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'verify.mail', 'subject', 'zh_CN', '加入企业验证邮件');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '600001', 'zh_CN', '没有此邮箱域名');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '600002', 'zh_CN', '已经过了验证时间(有效期30分钟)');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '600003', 'zh_CN', '该邮箱已被占用');

-- merge from energy-delta-data-debug.sql
--
-- 基础的三种参数
--
INSERT INTO `eh_energy_meter_formula_variables` (`id`, `namespace_id`, `name`, `display_name`, `description`, `status`, `creator_uid`, `create_time`)
VALUES ('1', '0', 'p', '单价', '当期单价', '2', '1', '2016-11-01 19:06:09');
INSERT INTO `eh_energy_meter_formula_variables` (`id`, `namespace_id`, `name`, `display_name`, `description`, `status`, `creator_uid`, `create_time`)
VALUES ('2', '0', 't', '倍率', '当期倍率', '2', '1', '2016-11-01 19:06:12');
INSERT INTO `eh_energy_meter_formula_variables` (`id`, `namespace_id`, `name`, `display_name`, `description`, `status`, `creator_uid`, `create_time`)
VALUES ('3', '0', 'a', '读表用量差', '每日或每月读表差', '2', '1', '2016-11-01 19:06:13');

--
-- 表记默认分类(深业域空间下)
--
INSERT INTO `eh_energy_meter_categories` (`id`, `namespace_id`, `name`, `status`, `category_type`, `delete_flag`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('1', '999992', '应收部分', '2', '1', '0', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_categories` (`id`, `namespace_id`, `name`, `status`, `category_type`, `delete_flag`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('2', '999992', '应付部分', '2', '1', '0', NULL, NULL, NULL, NULL);

--
-- default setting
--
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('1', '999992', '1', '1', '单价', '2', NULL, '2', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('2', '999992', '1', '2', '倍率', '1', NULL, '2', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('3', '999992', '1', '3', '用量计算公式', NULL, '1', '2', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('4', '999992', '1', '4', '费用计算公式', NULL, '2', '2', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('5', '999992', '2', '1', '单价', '2', NULL, '2', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('6', '999992', '2', '2', '倍率', '1', NULL, '2', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('7', '999992', '2', '3', '用量计算公式', NULL, '1', '2', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('8', '999992', '2', '4', '费用计算公式', NULL, '2', '2', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('9', '999992', '3', '5', '每日抄表提示', '20', NULL, '3', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_default_settings` (`id`, `namespace_id`, `meter_type`, `setting_type`, `name`, `setting_value`, `formula_id`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('10', '999992', '3', '6', '每月抄表提示', '30', NULL, '3', NULL, NULL, NULL, NULL);

--
-- 能耗管理在服务广场的入口
--
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
VALUES(@launch_pad_item_id := @launch_pad_item_id + 1, 999992, 0, 0, 0, '/home', 'Bizs', 'Energy', '能耗管理', 'cs://1/image/aW1hZ2UvTVRvM1pHVTNPVE16Wm1NMVlURTFabUpsTXpoaE9ERmpOamhqTmpneVpHVXlZZw', 1, 1, 13, '{"url":"http://beta.zuolin.com/energy-management/index.html?hideNavigationBar=1#/energy_management#sign_suffix"}', 0, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin');

--
-- 字符串
--
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10001', 'zh_CN', '表记不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10002', 'zh_CN', '表记分类不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10003', 'zh_CN', '公式不能计算');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10004', 'zh_CN', '公式格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10005', 'zh_CN', '读表记录不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10006', 'zh_CN', '只允许删除今天的读表记录');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10008', 'zh_CN', '删除失败,该公式已被引用');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10009', 'zh_CN', '起始读数大于最大量程');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10010', 'zh_CN', '默认分类无法删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10011', 'zh_CN', '导入失败,请检查数据准确性');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10012', 'zh_CN', '删除失败,该选项已被引用');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10013', 'zh_CN', '开始不能时间小于现在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10014', 'zh_CN', '结束时间不能小于开始时间');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy.meter.type', '1', 'zh_CN', '水表');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy.meter.type', '2', 'zh_CN', '电表');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy.meter.status', '2', 'zh_CN', '正常');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy.meter.status', '3', 'zh_CN', '已报废');

--
-- 菜单添加
--
-- 新增能耗管理菜单
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49100, '能耗管理', 40000, NULL, 'energy_management', 1, 2, '/40000/49100', 'park', 390);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49110, '表计管理', 49100, NULL, 'energy_table_management', 0, 2, '/40000/49100/49110', 'park', 391);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49120, '抄表记录', 49100, NULL, 'energy_table_record', 0, 2, '/40000/49100/49120', 'park', 392);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49130, '统计信息', 49100, NULL, 'energy_statistics_info', 0, 2, '/40000/49100/49130', 'park', 393);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49140, '参数设置', 49100, NULL, 'energy_param_setting', 0, 2, '/40000/49100/49140', 'park', 394);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (422, 0, '能耗管理', '能耗管理 全部权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (423, 0, '表计管理', '能耗管理 表计管理 全部权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (424, 0, '抄表记录', '能耗管理 抄表记录 全部权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (425, 0, '统计信息', '能耗管理 统计信息 全部权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (426, 0, '参数设置', '能耗管理 参数设置 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 422, 49100, '能耗管理', 1, 1, '能耗管理  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 423, 49110, '表计管理', 1, 1, '能耗管理 表计管理 全部权限', 203);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 424, 49120, '抄表记录', 1, 1, '能耗管理 抄表记录 全部权限', 204);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 425, 49130, '统计信息', 1, 1, '能耗管理 统计信息 全部权限', 205);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 426, 49140, '参数设置', 1, 1, '能耗管理 参数设置 全部权限', 206);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
  VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 422, 1001, 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
  VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 423, 1001, 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
  VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 424, 1001, 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
  VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 425, 1001, 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
  VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 426, 1001, 0, 1, NOW());


SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES ((@menu_scope_id := @menu_scope_id + 1), 49100, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES ((@menu_scope_id := @menu_scope_id + 1), 49110, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES ((@menu_scope_id := @menu_scope_id + 1), 49120, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES ((@menu_scope_id := @menu_scope_id + 1), 49130, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES ((@menu_scope_id := @menu_scope_id + 1), 49140, '', 'EhNamespaces', 999992, 2);
  
  
  
  
-- 俱乐部相关配置， add by tt, 20161102
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10031', 'zh_CN', '名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10032', 'zh_CN', '今天广播发送次数已用完');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10033', 'zh_CN', '标题不能超过10个字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10034', 'zh_CN', '内容不能超过200个字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10035', 'zh_CN', '不可小于10个字');

select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 36, 'zh_CN', '俱乐部发消息', '${newCreator}已成为“${groupName}”的创建者', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 37, 'zh_CN', '俱乐部发消息', '你已成为“${groupName}”的创建者', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 38, 'zh_CN', '俱乐部发消息', '你创建“俱乐部A”的申请已通过', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 39, 'zh_CN', '俱乐部发消息', '你已成功创建“${groupName}”', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 40, 'zh_CN', '俱乐部发消息', '你提交了创建“${groupName}”的申请，需要人工审核，请耐心等候', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 41, 'zh_CN', '俱乐部发消息', '你创建“${groupName}”的申请被拒绝', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 42, 'zh_CN', '俱乐部发消息', '你加入的“${groupName}”已解散', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 43, 'zh_CN', '俱乐部发消息', '不允许创建${clubPlaceholderName}', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 44, 'zh_CN', '俱乐部发消息', '${userName}申请加入“${groupName}”，理由：${reason}', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 45, 'zh_CN', '俱乐部发消息', '你加入“${groupName}”的申请已通过', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 46, 'zh_CN', '俱乐部发消息', '你加入“${groupName}”的申请被拒绝', 0);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (402, 'club.placeholder.name', '俱乐部', 'club placeholder name', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (403, 'club.share.url', '/mobile/static/group_share/index.html', 'club share url', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (404, 'club.placeholder.name', '白领社团', 'club placeholder name', 999985, NULL);


-- 俱乐部菜单 by tt, 20161117
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (571,0,'俱乐部管理','俱乐部管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (572,0,'审核俱乐部','审核俱乐部 全部功能',null);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59500,'俱乐部',40000,null,null,1,2,'/40000/59500','park',455);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59510,'俱乐部管理',59500,null,'groups_management',0,2,'/40000/59500/59510','park',456);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59520,'审核俱乐部',59500,null,'audit_groups',0,2,'/40000/59500/59520','park',457);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),571,59510,'俱乐部管理',1,1,'俱乐部管理  全部权限',350);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),572,59520,'审核俱乐部',1,1,'审核俱乐部 全部权限',351);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%59500/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1002,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%59500/%');


set @scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
-- 科技园
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES (@scope_id := @scope_id+1, 59500,'俱乐部', 'EhNamespaces', 1000000 , 1);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES (@scope_id := @scope_id+1, 59510,'俱乐部管理', 'EhNamespaces', 1000000 , 1);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES (@scope_id := @scope_id+1, 59520,'审核俱乐部', 'EhNamespaces', 1000000 , 1);

-- 华润
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES (@scope_id := @scope_id+1, 59500,'社团', 'EhNamespaces', 999985 , 1);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES (@scope_id := @scope_id+1, 59510,'社团管理', 'EhNamespaces', 999985 , 1);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES (@scope_id := @scope_id+1, 59520,'审核社团', 'EhNamespaces', 999985 , 1);

-- 清华信息港
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES (@scope_id := @scope_id+1, 59500,'俱乐部', 'EhNamespaces', 999984 , 1);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES (@scope_id := @scope_id+1, 59510,'俱乐部管理', 'EhNamespaces', 999984 , 1);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES (@scope_id := @scope_id+1, 59520,'审核俱乐部', 'EhNamespaces', 999984 , 1);
