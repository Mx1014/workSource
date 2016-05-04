
#
#20160415
#
UPDATE `eh_organization_members` SET `status` = 0 WHERE `organization_id` = 178395;


#
#20160504
#
update `eh_forum_posts` set `forum_id` = 178622 where `creator_uid` in (select id from eh_users where namespace_id=999999) and `forum_id` = 1; 
update `eh_forum_posts` set `forum_id` = 176520 where `creator_uid` in (select id from eh_users where namespace_id=1000000) and `forum_id` = 1; 

