-- 访客管理1.0事由列表 by dengs,20180507
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('1', '0', 'community', '0', '临时来访', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('2', '0', 'community', '0', '入职', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('3', '0', 'community', '0', '面试', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('4', '0', 'community', '0', '送货', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('5', '0', 'community', '0', '出差	', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('6', '0', 'community', '0', '开会', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('7', '0', 'community', '0', '施工', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('8', '0', 'community', '0', '其它原因', '2', '0', now(), '0', now());

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('sms.default', 67, 'zh_CN', '验证码-访客管理', '${modlueName}你的验证码是${verificationCode}。验证码15分钟内有效。', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('sms.default', 68, 'zh_CN', '访客邀请-访客管理', '${appName}你收到了一条来自${visitEnterpriseName}的访客邀请，请点击查看：${invitationLink}', '0');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('visitorsys.invitation.link', '%s/r?token=%s', '访客管理邀请函连接地址', '0', NULL);

