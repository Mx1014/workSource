-- 因为后面会出现clone帖的功能，现在现将原有帖子设置为正常帖  add by yanjun 20170809
UPDATE eh_forum_posts set clone_flag = 2 where clone_flag is null;
UPDATE eh_activities set clone_flag = 2 where clone_flag is null;