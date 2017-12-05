-- 在post表中更新模块和入口信息
-- 活动帖子
UPDATE eh_forum_posts set module_type = 2, module_category_id = activity_category_id where activity_category_id IS NO NULL and activity_category_id != 0;
-- 论坛帖子
UPDATE eh_forum_posts set module_type = 1, module_category_id = forum_entry_id where forum_entry_id != null and category_id != 1003;
-- 公告帖子
UPDATE eh_forum_posts set module_type = 3 where category_id == 1003;

-- 我-我的发布，按钮是否需要展示
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id := @id + 1), 'my.publish.flag', 0, 'my.publish.flag 0-hide, 1-display', '999973', NULL);
