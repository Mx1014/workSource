-- merge from mergeAsset by xiongying20170417
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) VALUES ('1', 'community', '240111044331055940', '科兴物业缴费', 'EBEI', '2', '999983');
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) VALUES ('2', 'community', '240111044331055035', '华润物业缴费', 'ZUOLIN', '2', '999985');
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) VALUES ('3', 'community', '240111044331055036', '华润物业缴费', 'ZUOLIN', '2', '999985');


-- merge from activity-bug-1.0 更改华润的查询方式为当前小区或者是管理公司，具体实现是scope使用3  add by yanjun 20170114
UPDATE eh_launch_pad_items SET action_data = REPLACE(action_data, '"scope":2', '"scope":3') WHERE namespace_id = 999985 AND action_type = 61 AND scene_type = 'park_tourist' ;
