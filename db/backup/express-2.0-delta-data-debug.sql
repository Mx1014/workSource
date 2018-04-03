update eh_configurations SET `value`='http://pay-beta.zuolin.com/EDS_PAY/rest/pay_common/payInfo_record/save_payInfo_record' WHERE `name` = 'guomao.payserver.url' AND namespace_id = 0;
set @eh_configurations_id = (select Max(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id:=@eh_configurations_id+1), 
'debug.flag', 'true', '国贸 debug flag', 0, NULL);
update eh_configurations SET `value`='http://pay-beta.zuolin.com/EDS_PAY/rest/pay_common/payInfo_record/createWechatJsPayOrder' WHERE `name` = 'guomao.official.accounts.payserver.url' AND namespace_id = 0;
