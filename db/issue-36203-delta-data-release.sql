-- add by yanlong.liang 20180831
-- 初始化个人中心配置数据
set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'会员等级','会员等级',0,1,1,1,0,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'积分','积分',0,2,1,1,1,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'发票','发票',1,4,1,1,5,2,'',1,1);
INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,1,6,2,'',1,1);
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
VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,1,12,2,'',1,1);