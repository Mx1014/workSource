-- merge from organization-delta-data-release.sql by lqs 20161128
-- by sfyan 获取电商平台的店铺信息接口
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'get.businesses.info.api', 'zl-ec/rest/openapi/shop/listByCondition', '获取店铺信息');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
VALUES ('300', 'organization', '600001', 'zh_CN', '通用岗位已存在');

-- merge from activityentry-delta-data-release.sql by xiongying20161128
update eh_activities a set forum_id = (select forum_id from eh_forum_posts where id = a.post_id);
update eh_activities a set creator_tag = (select creator_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set target_tag = (select target_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_type = (select visible_region_type from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_id = (select visible_region_id from eh_forum_posts where id = a.post_id);