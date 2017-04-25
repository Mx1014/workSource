--by dengs 国际化 邮件发送
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('serviceAlliance.request.notification', '10001', 'zh_CN', '的申请单');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('serviceAlliance.request.notification', '10002', 'zh_CN', '\n预订人:');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('serviceAlliance.request.notification', '10003', 'zh_CN', ' \n手机号:');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('serviceAlliance.request.notification', '10004', 'zh_CN', '\n公司名称:');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('serviceAlliance.request.notification', '10005', 'zh_CN', '\n服务名称:');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`)
    VALUES( 'serviceAlliance.request.notification', 4, 'zh_CN', '提交申请通知给机构和管理员', '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>${title}</title></head><body><p>预订人：${creatorName}</p><p>手机号：${creatorMobile}</p><p>公司名称：${creatorOrganization}</p><p>服务名称：${categoryName}</p>${note}</body></html>');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`)
    VALUES( 'serviceAlliance.request.notification', 5, 'zh_CN', '邮件内容生成PDF文件', '预订人：${creatorName}\n手机号：${creatorMobile}\n公司名称：${creatorOrganization}\n服务名称：${categoryName}\n');
