-- 电商链接配置alpha环境
INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.realm', 'biz', 'business realm');
INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.url', 'http://biz-alpha.lab.everhomes.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=http://biz-alpha.lab.everhomes.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14646085111657370488%3F_k%3Dzlbiz#sign_suffix', 'business url');

-- 电商链接配置beta环境
-- INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.realm', 'biz', 'business realm');
-- INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.url', 'https://biz-beta.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz-beta.zuolin.com%2Fnar%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix', 'business url');


-- 预约2.0配置支付host
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES('pay.zuolin.refound','POST /EDS_PAY/rest/pay_common/refund/save_refundInfo_record','退款的api','999989',NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES('pay.zuolin.host','https://pay-beta.zuolin.com','退款的host','999989',NULL);


-- 预约2.0广场配置
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
 VALUES('15081','999989','0','0','0','/home','Bizs','资源预订','资源预订','cs://1/image/aW1hZ2UvTVRvNE9UWTVNamc0WmpReU5EVXhNakJsTjJNek9EY3dNamczWTJObE1HWm1Ndw','1','1','49','{\"resourceTypeId\":3,\"pageType\":0}','0','0','1','0','1','0',NULL,NULL,NULL,'1','pm_admin','0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
 VALUES('15082','999989','0','0','0','/home','Bizs','资源预订','资源预订','cs://1/image/aW1hZ2UvTVRvNE9UWTVNamc0WmpReU5EVXhNakJsTjJNek9EY3dNamczWTJObE1HWm1Ndw','1','1','49','{\"resourceTypeId\":3,\"pageType\":0}','0','0','1','0','1','0',NULL,NULL,NULL,'1','park_tourist','0');

-- 预约2.0的
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES('3','测试资源类型','0',NULL,'0','999989');

-- 工位预定广场配置 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
 VALUES('109789','999989','0','0','0','/home','Bizs','OFFICECUBICLE','工位预订','cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw','1','1','14','{\"url\":\"http://beta.zuolin.com/station-booking/index.html?hidenavigationbar=1#/station_booking#sign_suffix\"}','0','0','1','0','','0',NULL,NULL,NULL,'1','park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
 VALUES('109790','999989','0','0','0','/home','Bizs','OFFICECUBICLE','工位预订','cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw','1','1','14','{\"url\":\"http://beta.zuolin.com/station-booking/index.html?hidenavigationbar=1#/station_booking#sign_suffix\"}','0','0','1','0','','0',NULL,NULL,NULL,'1','pm_admin');
