INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) 
    VALUES( 'serviceAlliance.request.notification', 1, 'zh_CN', '提交申请通知给管理员', '您收到一条“${categoryName}”的申请 \n 预订人：“${creatorName}” \n 手机号：“${creatorMobile}” \n 公司名称：“${creatorOrganization}” \n 备注：“${note}” \n 您可以登录管理后台查看详情');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) 
    VALUES( 'serviceAlliance.request.notification', 2, 'zh_CN', '提交申请通知给机构', '您收到一条“${categoryName}”的申请 \n 预订人：“${creatorName}” \n 手机号：“${creatorMobile}” \n 公司名称：“${creatorOrganization} \n 备注：“${note}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) 
    VALUES( 'serviceAlliance.request.notification', 3, 'zh_CN', '邮件主题', '您收到一条“${categoryName}”的申请');