update eh_locale_templates SET text = '您收到一条${categoryName}的申请；

服务名称：${serviceAllianceName}
提交者信息：
预订人：${creatorName}
手机号：${creatorMobile}
公司名称：${creatorOrganization}

提交的信息：
${notemessage}
您可以登录管理后台查看详情
' WHERE scope = 'serviceAlliance.request.notification' AND `code` in (1,2);