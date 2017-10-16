-- 增加资源预订到期文案 by st.zheng
set @eh_locale_templates_id = (select max(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id:=eh_locale_templates_id+1, 'rental.notification', '10', 'zh_CN', '资源到期给资源负责人推送提醒(半天/小时)', '温馨提醒：${resourceName}资源的使用将在15分钟后结束，使用客户${requestorName}(${requestorPhone}),请进行确认', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id:=eh_locale_templates_id+1, 'rental.notification', '11', 'zh_CN', '资源到期给资源负责人推送提醒(天/月)', '温馨提醒：${resourceName}资源的使用将在今日结束，使用客户${requestorName}(${requestorPhone}),请进行确认', '0');
