-- add by xiongying
UPDATE eh_user_favorites SET target_type = 'activity' WHERE target_id IN (
  SELECT id FROM eh_forum_posts WHERE id IN (SELECT a.target_id FROM (SELECT target_id FROM eh_user_favorites WHERE target_type = 'topic') a) 
  AND category_id = 1010);
  
UPDATE eh_user_posts SET target_type = 'topic';
  
UPDATE eh_user_posts SET target_type = 'activity' WHERE target_id IN (
  SELECT id FROM eh_forum_posts WHERE id IN (SELECT a.target_id FROM (SELECT target_id FROM eh_user_posts WHERE target_type = 'topic') a) 
  AND category_id = 1010);

-- add by yanshaofan

UPDATE `eh_launch_pad_items` SET `default_order` = 1000 WHERE `action_type` = 1;

-- 20160628
UPDATE `eh_launch_pad_layouts` SET `layout_json` = '{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}' where namespace_id = 0 and name = 'ServiceMarketLayout';

UPDATE `eh_launch_pad_layouts` SET `layout_json` = '{"versionCode":"2015111401","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Metro","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21,columnCount:6}]}' where namespace_id = 999999 and name = 'ServiceMarketLayout';

-- 20160629
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @order_id = 50;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
SELECT (@item_id = @item_id + 1),0,'0','0','0','/home','Bizs',`display_name`,`name`,`logo_uri`,'1','1','14','',(@order_id = @order_id + 1),'0','1','0','','0',NULL,'biz',`id`,'1','pm_admin' FROM `eh_businesses` WHERE target_type = 1;

SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @order_id = 50;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
SELECT (@item_id = @item_id + 1),0,'0','0','0','/home','Bizs',`display_name`,`name`,`logo_uri`,'1','1','14','',(@order_id = @order_id + 1),'0','1','0','','0',NULL,'biz',`id`,'1','park_tourist' FROM `eh_businesses` WHERE target_type = 1;

SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @order_id = 50;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
SELECT (@item_id = @item_id + 1),0,'0','0','0','/home','Bizs',`display_name`,`name`,`logo_uri`,'1','1','14','',(@order_id = @order_id + 1),'0','1','0','','0',NULL,'biz',`id`,'1','default' FROM `eh_businesses` WHERE target_type = 1;

SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @order_id = 50;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
SELECT (@item_id = @item_id + 1),0,'1000000','0','0','/home','Bizs',`display_name`,`name`,`logo_uri`,'1','1','14','',(@order_id = @order_id + 1),'0','1','0','','0',NULL,'biz',`id`,'1','pm_admin' FROM `eh_businesses` WHERE target_type = 1;

SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @order_id = 50;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
SELECT (@item_id = @item_id + 1),0,'1000000','0','0','/home','Bizs',`display_name`,`name`,`logo_uri`,'1','1','14','',(@order_id = @order_id + 1),'0','1','0','','0',NULL,'biz',`id`,'1','park_tourist' FROM `eh_businesses` WHERE target_type = 1;

SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @order_id = 50;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
SELECT (@item_id = @item_id + 1),0,'1000000','0','0','/home','Bizs',`display_name`,`name`,`logo_uri`,'1','1','14','',(@order_id = @order_id + 1),'0','1','0','','0',NULL,'biz',`id`,'1','default' FROM `eh_businesses` WHERE target_type = 1;

SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @order_id = 50;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
SELECT (@item_id = @item_id + 1),0,'999999','0','0','/home','Bizs',`display_name`,`name`,`logo_uri`,'1','1','14','',(@order_id = @order_id + 1),'0','1','0','','0',NULL,'biz',`id`,'1','pm_admin' FROM `eh_businesses` WHERE target_type = 1;

SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @order_id = 50;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
SELECT (@item_id = @item_id + 1),0,'999999','0','0','/home','Bizs',`display_name`,`name`,`logo_uri`,'1','1','14','',(@order_id = @order_id + 1),'0','1','0','','0',NULL,'biz',`id`,'1','park_tourist' FROM `eh_businesses` WHERE target_type = 1;

SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @order_id = 50;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
SELECT (@item_id = @item_id + 1),0,'999999','0','0','/home','Bizs',`display_name`,`name`,`logo_uri`,'1','1','14','',(@order_id = @order_id + 1),'0','1','0','','0',NULL,'biz',`id`,'1','default' FROM `eh_businesses` WHERE target_type = 1;


