-- 深圳湾适用脚本[999966]
-- ADD by 黄良铭 20180615
-- #issue-31670  深圳湾携程用户对接

SET @c_id = (SELECT MAX(id) FROM eh_configurations);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.AppKey', 'obk_Shenzhenwan','携程方提供的接入账号','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.AppSecurity', 'obk_Shenzhenwan','携程方提供的接入密码','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.Appid', 'Shenzhenwan','携程方提供的公司ID','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.CorpPayType', 'public','public（因公）/private（因私）','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.InitPage', 'Home','登录携程成功后的第一个页面','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.Callback', '','返回到客户页面的URL','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.getTicketURL', 'https://ct.ctrip.com/corpservice/authorize/getticket','获取Ticket的URL','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.signInfoURL', 'https://ct.ctrip.com/m/SingleSignOn/H5SignInfo','单点登录H5SignInfo 的URL','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.OnError', 'ErrorCode','错误处理方式','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.batch.Logon_Appid', 'obk_Shenzhenwan','公司登陆ID(obk账户名)，由携程方提供','999966','',null);


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.batch.production.getTicketURL', 'https://ct.ctrip.com/SwitchAPI/Order/Ticket','获取Ticket生产环境服务地址','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.batch.test.getTicketURL', 'https://cta.fat.ctripqa.com/SwitchAPI/Order/Ticket','获取Ticket测试环境服务地址','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.restful.production.batchURL', 'https://ct.ctrip.com/corpservice/CorpCustService/SaveCorpCustInfoList','人事信息批量更新生产环境服务Restful地址','999966','',null);


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.soap.production.batchURL', 'https://ct.ctrip.com/corpservice/CorpCustService.asmx','人事信息批量更新生产环境服务SOAP地址','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.restful.test.batchURL', 'https://cta.fat.ctripqa.com/corpservice/CorpCustService/SaveCorpCustInfoList','人事信息批量更新测试环境服务Restful地址','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.soap.test.batchURL', 'https://cta.fat.ctripqa.com/corpservice/CorpCustService.asmx','人事信息批量更新测试环境服务SOAP地址','999966','',null);


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.isOpenedCardURL', 'https://ct.ctrip.com/corpservice/OpenCard/IsOpenedCard?type=json','判断员工是否开卡','999966','',null);
-- END