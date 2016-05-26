
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
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES( 4, 33000,'', 'EhNamespaces', 0 , 0);

#
#20160525
#
insert into `eh_yellow_pages` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `nick_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) values('200051','0','community', '240111044331049963','创客空间','科技园创业孵化器','1','深圳市南山区科苑路金融科技大厦A座5楼','0755-86620293','园区建设创新创业孵化器约4万平方米。有“创业之星”孵化基地；移动互联网孵化基地；亚马逊孵化器等。许勤市长于2014年11月率队视察讯美科技广场，对公司建设创新创业孵化器，通过产业平台综合服务和“租金换股权”模式等支持入园企业表示赞赏。 孵化器建设空间超过4万平米。 共建及自建共7个，其中包括： 讯美创新创业孵化器（自建） 讯美前海之星创新孵化器（共建） 讯美灵思创新孵化器（共建） 讯美移动互联网联盟孵化器（共建） 讯美联合企航孵化器（共建） 讯美阿基米创新金融孵化器（共建） 讯美乐创空间孵化器（共建） 创客空间，产品展区，路演中心 创客咖啡室 创客产品展示中心 多功能路演中心 远程会议中心
','cs://1/image/aW1hZ2UvTVRvNE9EazJNRGRpWm1OaFlqVTVaV1JtT1Rrd05tVm1OVE0xT1dOaE9ETXpPUQ','2',NULL,'113.95097','22.552217','',NULL,NULL,NULL,NULL,NULL,'苟丹','13686467512',NULL,NULL,NULL,NULL,NULL);

set @address_id = (select max(id) from `eh_addresses`);

update `eh_buildings` set `name` = '金融基地' where id = 176124;

INSERT INTO `eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`city_name`,`area_id`,`area_name`,`zipcode`,`address`,`longitude`,`latitude`,`geohash`,`address_alias`,`building_name`,`building_alias_name`,`apartment_name`,`apartment_floor`,`status`,`operator_uid`,`operate_time`,`creator_uid`,`create_time`,`delete_time`,`integral_tag1`,`integral_tag2`,`integral_tag3`,`integral_tag4`,`integral_tag5`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`area_size`,`namespace_id`)
SELECT (@address_id := @address_id + 1),`uuid`,`community_id`,`city_id`,`city_name`,`area_id`,`area_name`,`zipcode`,'金融基地-1-6D1',`longitude`,`latitude`,`geohash`,`address_alias`,`building_name`,`building_alias_name`,'1-6D1',`apartment_floor`,`status`,`operator_uid`,`operate_time`,`creator_uid`,`create_time`,`delete_time`,`integral_tag1`,`integral_tag2`,`integral_tag3`,`integral_tag4`,`integral_tag5`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`area_size`,`namespace_id`  from `eh_addresses` where id = 239825274387091134;

INSERT INTO `eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`city_name`,`area_id`,`area_name`,`zipcode`,`address`,`longitude`,`latitude`,`geohash`,`address_alias`,`building_name`,`building_alias_name`,`apartment_name`,`apartment_floor`,`status`,`operator_uid`,`operate_time`,`creator_uid`,`create_time`,`delete_time`,`integral_tag1`,`integral_tag2`,`integral_tag3`,`integral_tag4`,`integral_tag5`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`area_size`,`namespace_id`)
SELECT (@address_id := @address_id + 1),`uuid`,`community_id`,`city_id`,`city_name`,`area_id`,`area_name`,`zipcode`,'金融基地-1-6D2',`longitude`,`latitude`,`geohash`,`address_alias`,`building_name`,`building_alias_name`,'1-6D2',`apartment_floor`,`status`,`operator_uid`,`operate_time`,`creator_uid`,`create_time`,`delete_time`,`integral_tag1`,`integral_tag2`,`integral_tag3`,`integral_tag4`,`integral_tag5`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`area_size`,`namespace_id`  from `eh_addresses` where id = 239825274387091134;


#
#20160526
#
UPDATE `eh_punch_exception_approvals` SET `approval_status` = 0 where `approval_status` IS NULL;
UPDATE `eh_punch_exception_approvals` SET `morning_approval_status` = 0 where `morning_approval_status` IS NULL;
UPDATE `eh_punch_exception_approvals` SET `afternoon_approval_status` = 0 where `afternoon_approval_status` IS NULL;

UPDATE `eh_punch_exception_requests` SET `approval_status` = 0 where `approval_status` IS NULL and request_type = 1;
UPDATE `eh_punch_exception_requests` SET `morning_approval_status` = 0 where `morning_approval_status` IS NULL  and request_type = 1;
UPDATE `eh_punch_exception_requests` SET `afternoon_approval_status` = 0 where `afternoon_approval_status` IS NULL  and request_type = 1;