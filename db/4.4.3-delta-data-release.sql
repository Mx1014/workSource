-- merge from mergeAsset by xiongying20170417
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) VALUES ('1', 'community', '240111044331055940', '科兴物业缴费', 'EBEI', '2', '999983');
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) VALUES ('2', 'community', '240111044331055035', '华润物业缴费', 'ZUOLIN', '2', '999985');
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) VALUES ('3', 'community', '240111044331055036', '华润物业缴费', 'ZUOLIN', '2', '999985');

update eh_launch_pad_items set action_data = '{"url":"http://core.zuolin.com/property-bill/index.html?hideNavigationBar=1&name=%e4%b8%9a%e4%b8%bb%e6%9f%a5%e8%af%a2#/verify_account#sign_suffix"}' where namespace_id = 999985 and item_label = "业主查询";
update eh_launch_pad_items set action_data = '{"url":"http://core.zuolin.com/property-bill/index.html?hideNavigationBar=1&name=%e7%a7%9f%e6%88%b7%e6%9f%a5%e8%af%a2#/verify_account#sign_suffix"}' where namespace_id = 999985 and item_label = "租户查询";

-- merge from activity-bug-1.0 更改华润的查询方式为当前小区或者是管理公司，具体实现是scope使用3  add by yanjun 20170114
UPDATE eh_launch_pad_items SET action_data = REPLACE(action_data, '"scope":2', '"scope":3') WHERE namespace_id = 999985 AND action_type = 61 AND scene_type = 'park_tourist' ;

-- 更新华润楼栋名称 add by sw 20170418
UPDATE eh_buildings set `name` = '华润置地大厦E座' where `name` = '华润置地大厦' and community_id = 240111044331055035;

-- added by Janson huarun aclink
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (999985, 'aclink.qr_driver_type', 'huarun_anguan', 'anguan for huarun');

-- 停车菜单 add by sw 20170418
UPDATE `eh_web_menus` SET `data_type`='parking--park_setting' WHERE `id`='40810';
UPDATE `eh_web_menus` SET `data_type`='parking--park_rules' WHERE `id`='40820';
UPDATE `eh_web_menus` SET `data_type`='parking--park_card' WHERE `id`='40830';
UPDATE `eh_web_menus` SET `data_type`='parking--park_recharge' WHERE `id`='40840';
UPDATE `eh_web_menus` SET `data_type`='car--car_management' WHERE `id`='40900';

update eh_launch_pad_items set action_data = '{"url":"http://core.zuolin.com/property-bill/index.html?hideNavigationBar=1&name=%e7%89%a9%e4%b8%9a%e6%9f%a5%e8%b4%b9#/verify_account#sign_suffix"}' where namespace_id = 999983 and item_label = "物业查费";