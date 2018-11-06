-- add by yanlong.liang 20180831
-- 初始化个人中心配置数据
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('personal.wallet.home.url', 'https://payv2.zuolin.com', '个人中心我的钱包跳转URL域名', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('personal.order.home.url', 'https://biz.zuolin.com', '个人中心我的订单跳转URL域名', '0', NULL, '1');


set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'会员等级','会员等级',0,1,0,1,0,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'积分','积分',0,2,0,1,1,2,'',1,1,'/integral-management/build/index.html?systemId=%s&ehnavigatorstyle=2#/home#sign_suffix');
-- 左邻
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'',1,1,'/app/wallet');
-- 深圳湾
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),999966,'钱包','钱包',1,1,1,1,2,2,'',1,1,'/app/wallet?systemId=11#/');
-- 光大
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),999979,'钱包','钱包',1,1,1,1,2,2,'',1,1,'/app/wallet?systemId=12#/');
-- 安邦
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),999949,'钱包','钱包',1,1,1,1,2,2,'',1,1,'/app/wallet?systemId=13#/');
-- 清华信息港
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),999984,'钱包','钱包',1,1,1,1,2,2,'',1,1,'/app/wallet?systemId=14#/');
-- 左邻标准版
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),2,'钱包','钱包',1,1,1,1,2,2,'',1,1,'/app/wallet?systemId=15#/');
-- INNO+
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),999929,'钱包','钱包',1,1,1,1,2,2,'',1,1,'/app/wallet?systemId=16#/');

INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'',1,1);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('personal.show.private.flag', 'true', '个人中心是否展示隐私设置', '999929', NULL, '1');