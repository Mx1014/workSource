-- 园区生活、招聘与求职去掉tag
UPDATE eh_launch_pad_items set action_data = '{"forumEntryId":"1"}' where namespace_id = 999983 and item_label = '园区生活' and action_type = 62;
UPDATE eh_launch_pad_items set action_data = '{"forumEntryId":"2"}' where namespace_id = 999983 and item_label = '招聘与求职' and action_type = 62;

SET @id = (SELECT MAX(id) from eh_hot_tags);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '闲置', '1', '1', NOW(), '1', NOW(), NULL, '1', '1');
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '合租', '1', '2', NOW(), '1', NOW(), NULL, '1', '1');
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '拼车', '1', '3', NOW(), '1', NOW(), NULL, '1', '1');

INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '招聘', '1', '1', NOW(), '1', NOW(), NULL, '2', '1');
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '求职', '1', '2', NOW(), '1', NOW(), NULL, '2', '1');

INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'activity', '旅游', '1', '1', NOW(), '1', NOW(), NULL, '2', '2');
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'activity', '团建', '1', '2', NOW(), '1', NOW(), NULL, '2', '2');

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) VALUES ('1', '0', NULL, NULL, 'topic', '话题', '0', NOW());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) VALUES ('2', '0', NULL, NULL, 'activity', '活动', '0', NOW());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) VALUES ('3', '0', NULL, NULL, 'poll', '投票', '0', NOW());
