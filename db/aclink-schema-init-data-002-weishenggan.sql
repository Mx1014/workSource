
INSERT INTO `eh_configurations` (name,value,description) VALUES ('aclink.lingling.server', 'http://120.24.172.108:8889', '令令开门后台服务地址');
INSERT INTO `eh_configurations` (name,value,description) VALUES ('aclink.lingling.appKey', '1FB1325FCAA4B17753858CD0E7933284', '令令开门appKey');
INSERT INTO `eh_configurations` (name,value,description) VALUES ('aclink.lingling.signature', 'f2877f02-5638-45ab-8425-8bd198f36a9b', '令令开门signature');
INSERT INTO `eh_configurations` (name,value,description) VALUES ('aclink.lingling.token', '1461381932233', '令令开门token');

#2016-06-08
delete from eh_locale_templates where `text`='24901' and `code`=8 and `namespace_id`='999990';
delete from eh_locale_templates where `text`='24901' and `code`=8 and `namespace_id`='1000000';
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999990, 'sms.default', 8, 'zh_CN', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}/aclink/v?id=${id}（24小时有效）', '24901');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999990, 'sms.default.yzx', 8, 'zh_CN', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}/aclink/v?id=${id}（24小时有效）', '24901');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(1000000, 'sms.default', 8, 'zh_CN', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}/aclink/v?id=${id}（24小时有效）', '24901');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(1000000, 'sms.default.yzx', 8, 'zh_CN', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}/aclink/v?id=${id}（24小时有效）', '24901');
