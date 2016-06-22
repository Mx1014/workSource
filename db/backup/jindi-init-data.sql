INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (213211, UUID(), '9201900', '吴珊', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '491fd705ac2efb3c51af92c2561001ab', '6c7879896710223282eb8f7c1a8cd1bffdd8fb93eae4493dec9d29d65ad66ed6', UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (219129, 213211,  '0',  '13798204538',  '221616',  3, UTC_TIMESTAMP(), 999991);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179902, UUID(), 999991, 2, 'EhGroups', 0,'金地置业物业论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179903, UUID(), 999991, 2, 'EhGroups', 0,'金地置业物业意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 


INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('14960', '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', '999991');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('14961', '14960', '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', '999991');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('14962', '14961', '南山区', 'NANSHANQU', 'NSQ', '/广东/深圳市/南山区', '3', '3', NULL, '0755', '2', '0', '999991');

	
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES( 240111044331051224, UUID(), 14961, '深圳市',  14962, '南山区', '威新软件园', '威新软件园', '深圳市南山区高新区高新南九道9号', NULL, '金地威新软件科技园位于深圳高新科技产业园的核心,紧邻香港，快速链接深港经济圈,3分钟到达地铁站，20分钟可达皇岗口岸,15分钟到达蛇口港码头， 30分钟抵达宝安国际机场，1小时进入香港国际机场，依托亚太地区乃至全球最具活力和国际竞争力的智慧型城市群，辐射世界的科技产业中心。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 423, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 179902, 179903, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331047834, 240111044331051224, '', 113.9480796697, 22.5292371168, 'ws100vq6qdnq');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1000751, 240111044331051224);

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`) 
	VALUES(1000751, 0, 'PM', '金地商置集团有限公司', 0, '金地商置集团有限公司(简称「金地商置」)是大中华区具领先地位的地产发展商和运营商，是香港联合交易所的上市公司(535.HK)，是全国化上市房地产企业金地（集团）股份有限公司(简称「金地集团」)（600383.SH）旗下独立运作的商业地产投资、开发及运营管理的唯一业务平台。', '/1000751', 1, 2, 'ENTERPRISE');
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2102361, 1000751, 'USER', 213211, 'manager', '吴珊', 0, '13798204538', 3);	

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(10500, 'EhOrganizations', 1000751, 'EhUsers', 213211, 1001, 1, UTC_TIMESTAMP());

INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1207, 999991, 'COMMUNITY', 240111044331051224, UTC_TIMESTAMP());	

INSERT INTO `eh_version_realm` VALUES ('32', 'Android_Jindi', null, UTC_TIMESTAMP(), '999991');
INSERT INTO `eh_version_realm` VALUES ('33', 'iOS_Jindi', null, UTC_TIMESTAMP(), '999991');

insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(32,32,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(33,33,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());


INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999991, 'sms.default.yzx', 1, 'zh_CN', '验证码-威新', '23050');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999991, 'sms.default.yzx', 4, 'zh_CN', '派单-威新', '23051');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999991, 'sms.default.yzx', 6, 'zh_CN', '任务2-威新', '23053');


INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`) 
	VALUES(67, 999991, 0, '/home', 'Default', 0, 0, 'jindi', 'jindi','cs://1/image/aW1hZ2UvTVRveFl6YzJOVEkwTVRsaFptVXdNbUUxTkdZMlpEWXpZbUV6WVRrMllqUTBOUQ','13','{"url":"http://www.imagicbrand.com/wx2/index.html"}',NULL,NULL,'2','10','0','2015-06-30 16:01:45',NULL);	
	
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time) 
	VALUES (37, 999991, 'ServiceMarketLayout', '{"versionCode":"2015120406","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2015120406', '0', '2', '2015-06-24 16:09:30');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`) 
	VALUES ('2139', '999991', '0', '0', '0', '/home', 'GovAgencies', '园区入驻', '园区入驻', 'cs://1/image/aW1hZ2UvTVRvNFlXSXdaRGRqTnpKaU56UmtZMll3WlRNeU1XUmxOekU0WXpNNU9HWmpZUQ', '1', '1', '28', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`) 
	VALUES ('2140', 999991, '0', '0', '0', '/home', 'GovAgencies', '交流大厅', '交流大厅', 'cs://1/image/aW1hZ2UvTVRwaE5HRTBNR1ZtTmpSa1lqVm1NbVJrWVRNNFlURXdZMlppTnpVNE1ESTRaQQ', '1', '1', '29', '{\"categoryId\":1003}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`) 
	VALUES ('2141', 999991, '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟', 'cs://1/image/aW1hZ2UvTVRvM016RmpNelJtTURGbU9ETTJZVEJrTWprNE1qbGpZbU0xTTJZMFlqWTFNZw', '1', '1', '33', '{\"type\":2,\"parentId\":100001}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`) 
	VALUES ('2142', 999991, '0', '0', '0', '/home', 'GovAgencies', '创客空间', '创客空间', 'cs://1/image/aW1hZ2UvTVRvd1kyUmxZakZsWW1Sa1lUVXpZakUwT1RJMU9XUm1PRFU0T1dRNVptSmxPQQ', '1', '1', '32', '{\"type\":1,\"forumId\":177000,\"categoryId\":1003,\"parentId\":110001,\"tag\":\"创客\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0');

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (2143, 999991, 0, 0, 0, '/home', 'Coupons', '优惠券', '优惠券', 'cs://1/image/aW1hZ2UvTVRwaFl6RTRZMlppT1dOak1HRTVZVFpqWlRKaFltWmtNMk0xTmpoak1XUmpaQQ', 3, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fallpromotions#sign_suffix"}', 1, 0, 1, 1, '', 0,NULL);	
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (2144, 999991, 0, 0, 0, '/home', 'Coupons', '市场动态', '市场动态', 'cs://1/image/aW1hZ2UvTVRwaU5HUTJNMkptTldVMk5qUTVZakk0TVdRNFl6WXlaak0zT1dNNU16TTRNdw', 5, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14477417463124576784#sign_suffix"}', 2, 0, 1, 1, '', 0,NULL);

	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`) 
	VALUES ('2145', '999991', '0', '0', '0', '/home', 'Bizs', 'PM', '物业服务', 'cs://1/image/aW1hZ2UvTVRwaE5qWmlZMkV3TW1Gall6aGxOVFJrWkRjNU1qQXlPV1kwTnpVek5XSXpZZw', '1', '1', '2', '{\"itemLocation\":\"/home/Pm\",\"layoutName\":\"PmLayout\",\"title\":\"物业报修\",\"entityTag\":\"PM\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1');
	

INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200455, 2, 0, '亲子与教育', '兴趣/亲子与教育', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200456, 2, 0, '运动与音乐', '兴趣/运动与音乐', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200457, 2, 0, '美食与厨艺', '兴趣/美食与厨艺', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200458, 2, 0, '美容化妆', '兴趣/美容化妆', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200459, 2, 0, '家庭装饰', '兴趣/家庭装饰', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200460, 2, 0, '名牌汇', '兴趣/名牌汇', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200461, 2, 0, '宠物会', '兴趣/宠物会', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200462, 2, 0, '旅游摄影', '兴趣/旅游摄影', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200463, 2, 0, '拼车', '兴趣/拼车', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200464, 2, 0, '老乡群', '兴趣/老乡群', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200465, 2, 0, '同事群', '兴趣/同事群', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200466, 2, 0, '同学群', '兴趣/同学群', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200467, 2, 0, '其他', '兴趣/其他', 1, 2, UTC_TIMESTAMP(), 999991);
	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200468, 4, 0, '亲子与教育', '活动/亲子与教育', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200469, 4, 0, '运动与音乐', '活动/运动与音乐', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200470, 4, 0, '宠物会', '活动/宠物会', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200471, 4, 0, '美食与厨艺', '活动/美食与厨艺', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200472, 4, 0, '美容化妆', '活动/美容化妆', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200473, 4, 0, '家庭装饰', '活动/家庭装饰', 0, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200474, 4, 0, '其他', '活动/其他', 1, 2, UTC_TIMESTAMP(), 999991);
	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200475, 1, 0, '普通', '帖子/普通', 1, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200476, 1, 0, '二手和租售', '帖子/二手和租售', 1, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200477, 1, 0, '免费物品', '帖子/免费物品', 1, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200478, 1, 0, '失物招领', '帖子/失物招领', 1, 2, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200479, 1, 0, '紧急通知', '帖子/紧急通知', 1, 2, UTC_TIMESTAMP(), 999991);
	
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(177020, 240111044331051224, '1号楼', '1号楼', 0, NULL, '深圳市南山区高新区高新南九道9号', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(177021, 240111044331051224, '2号楼', '2号楼', 0, NULL, '深圳市南山区高新区高新南九道9号', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(177022, 240111044331051224, '3号楼', '3号楼', 0, NULL, '深圳市南山区高新区高新南九道9号', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(177023, 240111044331051224, '5号楼', '5号楼', 0, NULL, '深圳市南山区高新区高新南九道9号', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(177024, 240111044331051224, '6号楼', '6号楼', 0, NULL, '深圳市南山区高新区高新南九道9号', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(177025, 240111044331051224, '7号楼', '7号楼', 0, NULL, '深圳市南山区高新区高新南九道9号', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(177026, 240111044331051224, '8号楼', '8号楼', 0, NULL, '深圳市南山区高新区高新南九道9号', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(177027, 240111044331051224, '9号楼', '9号楼', 0, NULL, '深圳市南山区高新区高新南九道9号', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(177028, 240111044331051224, '3A号楼', '3A号楼', 0, NULL, '深圳市南山区高新区高新南九道9号', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
