-- 初始化为按时长定价 by st.zheng
update `eh_rentalv2_price_rules` set `price_type` = 0;
update `eh_rentalv2_price_packages` set `price_type` = 0;

set @eh_locale_templates_id = (select max(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`) VALUES (@eh_locale_templates_id+1, 'rental.notification', '12', 'zh_CN', '修改金额的推送', '您申请预订的${resourceName}，使用时间：${startTime}，订单金额调整为${amount}');
