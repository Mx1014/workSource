-- oauth2client 1.0   add by xq.tian 2017/03/09
--
-- 创源对接第三方门禁icon
--
SET @max_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)
VALUES ((@max_id := @max_id + 1), 999986, 0, 0, 0, '/home', 'Bizs', 'CHUANG_YUAN_DOOR', '智能家居', 'cs://1/image/aW1hZ2UvTVRwaVl6ZGtPVFE0TURZd1pUZzRZekppTTJNMVl6QmlPVFprTWpWbFpHRXlNUQ', 1, 1, 13, '{"url":"https://beta.zuolin.com/evh/oauth2cli/redirect/huanteng?serviceUrl=%2fzlapp%2fdist%2f%3fhideNavigationBar%3d1%23%2faccess-control%2flock-list-cy&hideNavigationBar=1#sign_suffix"}', 0, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'default', 0, NULL, NULL);
SET @max_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)
VALUES ((@max_id := @max_id + 1), 999986, 0, 0, 0, '/home', 'Bizs', 'CHUANG_YUAN_DOOR', '智能家居', 'cs://1/image/aW1hZ2UvTVRwaVl6ZGtPVFE0TURZd1pUZzRZekppTTJNMVl6QmlPVFprTWpWbFpHRXlNUQ', 1, 1, 13, '{"url":"https://beta.zuolin.com/evh/oauth2cli/redirect/huanteng?serviceUrl=%2fzlapp%2fdist%2f%3fhideNavigationBar%3d1%23%2faccess-control%2flock-list-cy&hideNavigationBar=1#sign_suffix"}', 0, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL);

-- 帖子评论时给创建者或父评论者发送消息模板，add by tt, 20170316
select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'forum.notification', 2, 'zh_CN', '帖子评论给创建者发消息', '有人评论了你的帖子\t${userName} 评论了你的帖子 ${postName}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'forum.notification', 3, 'zh_CN', '帖子评论给父评论者发消息', '有人回复了你的评论\t${userName} 回复了你在帖子 ${postName} 的评论。', 0);

-- 更改帖子删除提示，add by tt, 20170316
UPDATE `eh_locale_strings` SET `text`='该帖子已被主人删除' WHERE  `id`=44;

