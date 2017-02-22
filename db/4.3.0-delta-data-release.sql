-- 更改威新LINK+的两条活动为官方活动，add by tt, 20170222
update eh_activities set official_flag = 1, category_id = 1 where id in (1508,1509);
update eh_forum_posts set official_flag = 1, activity_category_id = 1 where id in (188703, 188704);