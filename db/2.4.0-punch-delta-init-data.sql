
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
