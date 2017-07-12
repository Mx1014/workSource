-- by dengs 国际化 邮件发送
select max(id) into @id from eh_locale_strings;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10001', 'zh_CN', '的申请单');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10002', 'zh_CN', '见邮件附件');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10003', 'zh_CN', '审批');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10004', 'zh_CN', '序号,用户姓名,手机号码,企业,服务机构,提交时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10005', 'zh_CN', '申请类型,申请来源,服务机构');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10006', 'zh_CN', '姓名,联系电话,企业');

select max(id) into @id from eh_locale_templates;
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`,`locale`, `description`, `text`)
    VALUES(@id:=@id+1, 'serviceAlliance.request.notification', 4, 'zh_CN', '提交申请通知给机构和管理员', '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>${title}</title></head><body><p>预订人：${creatorName}</p><p>手机号：${creatorMobile}</p><p>公司名称：${creatorOrganization}</p><p>服务名称：${serviceOrgName}</p>${note}</body></html>');
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`,`locale`, `description`, `text`)
    VALUES(@id:=@id+1, 'serviceAlliance.request.notification', 5, 'zh_CN', '邮件内容生成PDF文件', '预订人：${creatorName}\n手机号：${creatorMobile}\n公司名称：${creatorOrganization}\n服务名称：${serviceOrgName}\n');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', 6, 'zh_CN', '邮件主题', '${serviceOrgName}的申请单', 0);

-- by dengs 20170508 服务联盟后台菜单（审批列表、申请记录）移动到上一层
UPDATE `eh_web_menus` SET  `parent_id` = 40500,`path` = '/40000/40500/40541' WHERE ID = 40541;
UPDATE `eh_web_menus` SET  `parent_id` = 40500,`path` = '/40000/40500/40542' WHERE ID = 40542;

DELETE FROM `eh_web_menu_scopes` WHERE `menu_id`=40540;
DELETE FROM `eh_web_menu_privileges` WHERE `menu_id`=40540;
DELETE FROM `eh_web_menus` WHERE `id`=40540;
