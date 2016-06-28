-- merge from 3.4.x.paymentcard-delta-data-release.sql 20160628
-- add by xiongying
UPDATE eh_user_favorites SET target_type = 'activity' WHERE target_id IN (
  SELECT id FROM eh_forum_posts WHERE id IN (SELECT a.target_id FROM (SELECT target_id FROM eh_user_favorites WHERE target_type = 'topic') a) 
  AND category_id = 1010);
  
UPDATE eh_user_posts SET target_type = 'topic';
  
UPDATE eh_user_posts SET target_type = 'activity' WHERE target_id IN (
  SELECT id FROM eh_forum_posts WHERE id IN (SELECT a.target_id FROM (SELECT target_id FROM eh_user_posts WHERE target_type = 'topic') a) 
  AND category_id = 1010);

-- add by yanshaofan
UPDATE `eh_launch_pad_layouts` SET `layout_json` = '{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}' where id = 3;

UPDATE `eh_launch_pad_items` SET `default_order` = 1000 WHERE `action_type` = 1;