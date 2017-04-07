-- 增加一个“活动时间：”中文字符串 2017-03-31 19:17 add by yanjun
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('activity','11','zh_CN','活动时间：');

-- 更新报修与工作流状态不同步 add by sw 20170206
UPDATE eh_pm_tasks join (SELECT refer_id from eh_flow_cases where id in (SELECT flow_case_id from eh_pm_tasks where flow_case_id != 0) and `status` = 4) t on t.refer_id = id set `status` = 4;

-- 处理设备-标准关联关系脏数据 add by xiongying20170406
update eh_equipment_inspection_equipment_standard_map set review_status = 4 where target_id in(select id from eh_equipment_inspection_equipments where status = 0);

-- 科技园报修 add by sw 20170406
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 34, 'zh_CN', '物业任务分配人员', '40775');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:20100', 34, 'zh_CN', '物业任务分配人员', '40775');

-- 更新华润 园区入驻 add by sw 20170406
DELETE from eh_lease_configs where id =3;
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`) 
	VALUES ('3', '999985', '1', '1', '1', '0', '0');

-- 更新深业优惠券 add by sw 20170407
update eh_launch_pad_layouts set layout_json = '{"versionCode":"2017040701","versionName":"4.4.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Coupons"},"style":"Gallery","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":2},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', version_code = '2017040701' where namespace_id = 999992 and id in (34, 36);
UPDATE eh_launch_pad_items set item_width = 1,icon_uri='cs://1/image/aW1hZ2UvTVRvMllUSXpaakkxTkdNMlpUVmpOekkxTlRBMllXVXhabVV3WVRBeFpXTmhNQQ' where item_label = '优惠券' and namespace_id = 999992;
UPDATE eh_launch_pad_items set item_width = 1,icon_uri='cs://1/image/aW1hZ2UvTVRvM056WXlOVGM0WkRFNE9UQTNOVGczWW1Jd1pERXdORFkwT1RjME1ETmhaQQ' where item_label = '优选商城' and namespace_id = 999992;

	
-- 更新储能icon顺序  add by sw 20170407
SET @order_ = 0;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '上网' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '门禁' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '钱包' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '停车' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '送水' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '班车' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '旅游' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '会议室' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '嗒嗒会议室' and namespace_id = 999990;

UPDATE eh_launch_pad_items set delete_flag = 0,icon_uri = 'cs://1/image/aW1hZ2UvTVRwaU1qWTNZVEl3TVRBeE5tTXhNVEl3WmpKaE1EZ3dZVE13WlRRNU9UUXhNQQ' where item_label = '会议室' and namespace_id = 999990;
