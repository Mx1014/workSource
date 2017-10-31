-- 论坛入口表默认入口
set @m_id = (select ifnull(max(id), 0) from eh_forum_categories);
INSERT INTO `eh_forum_categories` (id, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `interact_flag`, `create_time`, `update_time`)
SELECT (@m_id:=@m_id+1),UUID(), namespace_id, default_forum_id, 0, '发现', '0', '1', NOW(), NOW() from eh_communities where `status` = 2 GROUP BY namespace_id, default_forum_id ORDER BY namespace_id ;

set @id := (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'forum', '10020', 'zh_CN', '此版本目前不支持评论功能。由此带来的不便请谅解。');
