
#
#20160415
#
UPDATE `eh_organization_members` SET `status` = 0 WHERE `organization_id` = 178395;


#
#20160504
#
update `eh_forum_posts` set `forum_id` = 178622 where `creator_uid` in (select id from eh_users where namespace_id=999999) and `forum_id` = 1; 
update `eh_forum_posts` set `forum_id` = 176520 where `creator_uid` in (select id from eh_users where namespace_id=1000000) and `forum_id` = 1; 


#
#20160505
#
UPDATE `eh_organizations` SET `show_flag` = 1 WHERE `group_type` = 'ENTERPRISE';
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES( 1, 33000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES( 2, 35000,'', 'EhNamespaces', 1000000 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES( 3, 35000,'', 'EhNamespaces', 999999 , 0);