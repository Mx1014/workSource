update eh_activities a set forum_id = (select forum_id from eh_forum_posts where id = a.post_id);
update eh_activities a set creator_tag = (select creator_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set target_tag = (select target_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_type = (select visible_region_type from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_id = (select visible_region_id from eh_forum_posts where id = a.post_id);