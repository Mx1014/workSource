-- 俱乐部详情页  add by yanjun 20171107
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `ehcore`.`eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id := @id + 1), 'group.content.url', '/web/lib/html/group_text_review.html', 'group content url', '0', NULL);
