-- 更新海岸东座论坛id
update eh_communities set default_forum_id = 179511, feedback_forum_id = 179512 where namespace_id = 999993 and id = 240111044331054835;
update eh_forum_posts set forum_id = 179511 where forum_id = 183102;

-- 更新海岸东座管理处名字
update eh_organizations set name = '海岸大厦东座物业服务中心' where id = 1004937 and namespace_id = 999993;