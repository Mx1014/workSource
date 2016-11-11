
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

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'ApprovalStatus', '0', 'zh_CN', '待审批');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'ApprovalStatus', '1', 'zh_CN', '已同意');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'ApprovalStatus', '2', 'zh_CN', '已驳回');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval.type', '3', 'zh_CN', '加班');



-- 广场加入我的审批

SET @id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (@id:=@id+1, 1000000, '0', '0', '0', '/home', 'Bizs', 'MY_APPROVAL', '我的审批', 'cs://1/image/aW1hZ2UvTVRwak16azJOelEwWW1RNU5HRTFZalF4T1dGaE1qWTBOelE1TVRjNU4yTmhNQQ', '1', '1', '54', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (@id:=@id+1, 1000000, '0', '0', '0', '/home', 'Bizs', 'MY_APPROVAL', '我的审批', 'cs://1/image/aW1hZ2UvTVRveE9HRTBObVUzTkRZMk16UTJNVEUxTWpnNU0yTTRZemt6TnpObU5XRm1NQQ', '1', '1', 54,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');