INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2106,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999948','the relative path for 国贸服务 app agreements',999948,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2107,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for 国贸服务',999948,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2108,'pmtask.handler-999948','flow','0',999948,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2109,'pay.platform','1','支付类型',999948,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (637,'Android_GuoMaoFuWu',NULL,UTC_TIMESTAMP(),999948);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (638,'iOS_GuoMaoFuWu',NULL,UTC_TIMESTAMP(),999948);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (998,637,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999948);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (999,638,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999948);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999948,'国贸服务');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1628,999948,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (1060,'sms.default.sign',0,'zh_CN','国贸服务','【国贸服务】',999948);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18593,0,'北京','BEIJING','BJ','/北京',1,1,NULL,NULL,2,0,999948);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18594,18593,'北京市','BEIJINGSHI','BJS','/北京/北京市',2,2,NULL,'010',2,1,999948);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18595,18594,'昌平区','CHANGPINGQU','CPQ','/北京/北京市/昌平区',3,3,NULL,'010',2,0,999948);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060225,UUID(),18594,'北京市',18595,'昌平区','云集园','','北京市昌平区北七家镇七北路42号院','云集园集办公、居住、休闲、娱乐、教育为一体，以智慧园区，生态园区、文化园区和24小时，生活园区的崭新面貌，引领中国商务园区步入一个全新的时代。整个园区总建筑面积24万平米，由4栋甲级写字楼、9栋企业花园独栋、5万平方米住宅，2万平方米超大中央景观广场及2000平方米的星级会所组团而成。业态丰富，使“产”和“城”的概念得到有机的结合，是中国首个第四代商务园区的先行者。整个园区在设计和建造上秉承“生态、智能”的主题，充分张扬了第四代商务园区的设计理念。云集园终将在京城北部勾画出一座具有“资源聚集、开发包容、绿色低碳、功能完备的智能化综合社区的标志性建筑。',0,1,2,UTC_TIMESTAMP(),1,192913,192914,UTC_TIMESTAMP(),999948);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092998,240111044332060225,'',116.374208,40.103873,'wx4u0htjgxfb');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20335,999948,'COMMUNITY',240111044332060225,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1042227,0,'PM','国贸物业酒店管理有限公司','中国国际贸易中心是中外合资兴建的综合性高档商务服务设施，是目前全球最大的世界贸易中心。它集写字楼、公寓、酒店、展厅、商城等五大业态为一体，被誉为“中国人民与世界人民相会之地”的“城中之城”。
国贸物业酒店管理有限公司是隶属中国国际贸易中心的全资子公司，为国家物业管理一级资质企业，公司成立于1998年，前身为国贸中心物业部。秉承优秀的企业文化，努力实现“为客户提供卓越服务的国际化一流物业酒店管理公司”的企业愿景。我们通过多种形式为客户带来“方便快捷、物超所值、追求完美、喜出望外”的服务，并在完成物业合同规定范围内的基本物业服务的前提下，针对物业业态的不同特点，也分别推行了个性化的服务内容，从而进一步体现国贸物业酒店管理有限公司的服务特点。
','/1042227',1,2,'ENTERPRISE',999948,1059659);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1157110,240111044332060225,'organization',1042227,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37780,'EhOrganizations',1042227,1,10,487325,0,1,UTC_TIMESTAMP(),999948,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37781,'EhOrganizations',1042227,1,10,487326,0,1,UTC_TIMESTAMP(),999948,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37782,'EhOrganizations',1042227,1,10,487327,0,1,UTC_TIMESTAMP(),999948,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1059659,UUID(),'国贸物业酒店管理有限公司','国贸物业酒店管理有限公司',1,1,1042227,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192912,1,999948);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192912,UUID(),999948,2,'EhGroups',1059659,'国贸物业酒店管理有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192913,UUID(),999948,2,'',0,'国贸服务社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192914,UUID(),999948,2,'',0,'国贸服务意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256672,UUID(),999948,192913,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (487325,UUID(),'19276014111','孙振华',NULL,1,45,'1','1','zh_CN','c5aeada57e42055ebdfbcc7e070319bb','46e5e53274718cb1b4cca3a3d6d4940c032f5028c8a30cbc406aa0235d2352d4',UTC_TIMESTAMP(),999948);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (487326,UUID(),'19276014112','刘培宇',NULL,1,45,'1','1','zh_CN','d373f58c9195c1a7c91769a93bc403ff','a47b26b97750dbe805529d8f5e47f262721cc040956a830e809e7468f4494d09',UTC_TIMESTAMP(),999948);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (487327,UUID(),'19276014113','李强',NULL,1,45,'1','1','zh_CN','596c7363a0ab8c5f41c849706af54d5b','5f828ea8784cac9a8c966353debcd7ae52c7ae6ed5faf0e955d9cb03b4dbe41b',UTC_TIMESTAMP(),999948);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32971,1042227,'USER',487325,'孙振华',0,'15010499864',UTC_TIMESTAMP(),999948);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32972,1042227,'USER',487326,'刘培宇',0,'13911561756',UTC_TIMESTAMP(),999948);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32973,1042227,'USER',487327,'李强',0,'18042684464',UTC_TIMESTAMP(),999948);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (458278,487325,0,'15010499864',NULL,3,UTC_TIMESTAMP(),999948);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (458279,487326,0,'13911561756',NULL,3,UTC_TIMESTAMP(),999948);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (458280,487327,0,'18042684464',NULL,3,UTC_TIMESTAMP(),999948);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2186437,1042227,'/1042227','USER',487325,'manager','孙振华',0,'15010499864',3,999948,'ENTERPRISE',0,32971);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2186438,1042227,'/1042227','USER',487326,'manager','刘培宇',0,'13911561756',3,999948,'ENTERPRISE',0,32972);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2186439,1042227,'/1042227','USER',487327,'manager','李强',0,'18042684464',3,999948,'ENTERPRISE',0,32973);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (32330,487325,1042227,'/1042227','ENTERPRISE',3,999948,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (32331,487326,1042227,'/1042227','ENTERPRISE',3,999948,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (32332,487327,1042227,'/1042227','ENTERPRISE',3,999948,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115650,'EhOrganizations',1042227,'EhUsers',487325,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115651,'EhOrganizations',1042227,'EhUsers',487326,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115652,'EhOrganizations',1042227,'EhUsers',487327,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971579,240111044332060225,'1号楼','',0,13146337402,'云集园1号楼',NULL,'116.374208','40.103873','wx4u0htjgxfb','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971580,240111044332060225,'2号楼','',0,13811919188,'云集园2号楼',NULL,'116.374208','40.103874','wx4u0htjgxfy','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971581,240111044332060225,'3号楼','',0,15101648623,'云集园3号楼',NULL,'116.374208','40.103875','wx4u0htn584u','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971582,240111044332060225,'4号楼','',0,15101648623,'云集园4号楼',NULL,'116.374208','40.103876','wx4u0htn586f','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971583,240111044332060225,'5号楼','',0,13146337402,'云集园5号楼',NULL,'116.374208','40.103877','wx4u0htn58db','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971584,240111044332060225,'6号楼','',0,13146337402,'云集园6号楼',NULL,'116.374208','40.103878','wx4u0htn58dy','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971585,240111044332060225,'7号楼','',0,13146337402,'云集园7号楼',NULL,'116.374208','40.103879','wx4u0htn58fu','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971586,240111044332060225,'8号楼','',0,13911394241,'云集园8号楼',NULL,'116.374208','40.10388','wx4u0htn594f','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971587,240111044332060225,'13号楼','',0,13146337402,'云集园13号楼',NULL,'116.374208','40.103881','wx4u0htn596b','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971588,240111044332060225,'14号楼','',0,13146337402,'云集园14号楼',NULL,'116.374208','40.103882','wx4u0htn596y','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971589,240111044332060225,'15号楼','',0,13146337402,'云集园15号楼',NULL,'116.374208','40.103883','wx4u0htn59du','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971590,240111044332060225,'16号楼','',0,13146337402,'云集园16号楼',NULL,'116.374208','40.103884','wx4u0htn59ff','',NULL,2,0,NULL,1,NOW(),999948,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971591,240111044332060225,'下沉广场','',0,13522172068,'云集园下沉广场',NULL,'116.374208','40.103885','wx4u0htn5d4b','',NULL,2,0,NULL,1,NOW(),999948,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468135,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-303','2号楼','2C-303','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468167,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-608','2号楼','2C-608','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468199,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1004','2号楼','2C-1004','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468231,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-302','3号楼','3C-302','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468263,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-607','3号楼','3C-607','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468295,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1003','3号楼','3C-1003','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468327,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4B-701','4号楼','4B-701','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468359,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-604','4号楼','4C-604','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468391,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-909','4号楼','4C-909','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468423,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','13号楼-1-4层','13号楼','1-4层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468136,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-304','2号楼','2C-304','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468168,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-609','2号楼','2C-609','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468200,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1005','2号楼','2C-1005','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468232,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-303','3号楼','3C-303','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468264,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-608','3号楼','3C-608','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468296,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1004','3号楼','3C-1004','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468328,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4B-801','4号楼','4B-801','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468360,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-605','4号楼','4C-605','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468392,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1001','4号楼','4C-1001','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468424,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','14号楼-1-6层','14号楼','1-6层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468137,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-305','2号楼','2C-305','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468169,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-701','2号楼','2C-701','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468201,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1006','2号楼','2C-1006','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468233,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-304','3号楼','3C-304','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468265,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-609','3号楼','3C-609','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468297,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1005','3号楼','3C-1005','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468329,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-301','4号楼','4C-301','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468361,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-606','4号楼','4C-606','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468393,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1002','4号楼','4C-1002','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468425,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','15号楼-1-8层','15号楼','1-8层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468138,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-306','2号楼','2C-306','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468170,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-702','2号楼','2C-702','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468202,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1007','2号楼','2C-1007','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468234,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-305','3号楼','3C-305','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468266,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-701','3号楼','3C-701','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468298,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1006','3号楼','3C-1006','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468330,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-302','4号楼','4C-302','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468362,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-607','4号楼','4C-607','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468394,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1003','4号楼','4C-1003','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468426,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','16号楼-1-7层','16号楼','1-7层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468139,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-307','2号楼','2C-307','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468171,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-703','2号楼','2C-703','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468203,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1101','2号楼','2C-1101','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468235,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-306','3号楼','3C-306','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468267,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-702','3号楼','3C-702','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468299,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1007','3号楼','3C-1007','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468331,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-303','4号楼','4C-303','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468363,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-608','4号楼','4C-608','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468395,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1004','4号楼','4C-1004','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468427,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-101','下沉广场','101','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468140,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-308','2号楼','2C-308','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468172,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-704','2号楼','2C-704','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468204,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1102','2号楼','2C-1102','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468236,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-307','3号楼','3C-307','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468268,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-703','3号楼','3C-703','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468300,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1101','3号楼','3C-1101','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468332,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-304','4号楼','4C-304','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468364,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-609','4号楼','4C-609','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468396,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1005','4号楼','4C-1005','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468428,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-102','下沉广场','102','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468141,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-309','2号楼','2C-309','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468173,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-705','2号楼','2C-705','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468205,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1103','2号楼','2C-1103','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468237,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-308','3号楼','3C-308','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468269,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-704','3号楼','3C-704','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468301,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1102','3号楼','3C-1102','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468333,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-305','4号楼','4C-305','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468365,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-701','4号楼','4C-701','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468397,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1006','4号楼','4C-1006','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468429,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-103','下沉广场','103','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468142,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-401','2号楼','2C-401','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468174,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-706','2号楼','2C-706','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468206,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1104','2号楼','2C-1104','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468238,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-309','3号楼','3C-309','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468270,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-705','3号楼','3C-705','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468302,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1103','3号楼','3C-1103','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468334,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-306','4号楼','4C-306','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468366,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-702','4号楼','4C-702','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468398,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1007','4号楼','4C-1007','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468430,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-104','下沉广场','104','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468143,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-402','2号楼','2C-402','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468175,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-707','2号楼','2C-707','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468207,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1105','2号楼','2C-1105','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468239,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-401','3号楼','3C-401','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468271,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-706','3号楼','3C-706','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468303,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1104','3号楼','3C-1104','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468335,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-307','4号楼','4C-307','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468367,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-703','4号楼','4C-703','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468399,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1101','4号楼','4C-1101','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468431,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-105','下沉广场','105','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468144,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-403','2号楼','2C-403','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468176,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-708','2号楼','2C-708','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468208,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1106','2号楼','2C-1106','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468240,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-402','3号楼','3C-402','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468272,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-707','3号楼','3C-707','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468304,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1105','3号楼','3C-1105','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468336,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-308','4号楼','4C-308','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468368,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-704','4号楼','4C-704','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468400,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1102','4号楼','4C-1102','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468432,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-106','下沉广场','106','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468145,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-404','2号楼','2C-404','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468177,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-709','2号楼','2C-709','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468209,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1107','2号楼','2C-1107','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468241,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-403','3号楼','3C-403','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468273,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-708','3号楼','3C-708','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468305,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1106','3号楼','3C-1106','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468337,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-309','4号楼','4C-309','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468369,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-705','4号楼','4C-705','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468401,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1103','4号楼','4C-1103','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468433,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-107','下沉广场','107','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468146,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-405','2号楼','2C-405','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468178,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-801','2号楼','2C-801','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468210,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1201','2号楼','2C-1201','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468242,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-404','3号楼','3C-404','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468274,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-709','3号楼','3C-709','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468306,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1107','3号楼','3C-1107','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468338,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-401','4号楼','4C-401','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468370,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-706','4号楼','4C-706','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468402,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1104','4号楼','4C-1104','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468434,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-108','下沉广场','108','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468147,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-406','2号楼','2C-406','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468179,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-802','2号楼','2C-802','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468211,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1202','2号楼','2C-1202','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468243,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-405','3号楼','3C-405','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468275,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-801','3号楼','3C-801','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468307,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1201','3号楼','3C-1201','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468339,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-402','4号楼','4C-402','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468371,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-707','4号楼','4C-707','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468403,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1105','4号楼','4C-1105','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468435,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-109','下沉广场','109','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468148,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-407','2号楼','2C-407','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468180,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-803','2号楼','2C-803','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468212,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1203','2号楼','2C-1203','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468244,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-406','3号楼','3C-406','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468276,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-802','3号楼','3C-802','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468308,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1202','3号楼','3C-1202','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468340,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-403','4号楼','4C-403','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468372,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-708','4号楼','4C-708','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468404,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1106','4号楼','4C-1106','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468436,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-110','下沉广场','110','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468149,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-408','2号楼','2C-408','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468181,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-804','2号楼','2C-804','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468213,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1204','2号楼','2C-1204','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468245,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-407','3号楼','3C-407','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468277,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-803','3号楼','3C-803','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468309,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1203','3号楼','3C-1203','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468341,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-404','4号楼','4C-404','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468373,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-709','4号楼','4C-709','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468405,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1107','4号楼','4C-1107','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468437,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','下沉广场-111','下沉广场','111','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468150,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-409','2号楼','2C-409','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468182,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-805','2号楼','2C-805','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468214,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1205','2号楼','2C-1205','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468246,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-408','3号楼','3C-408','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468278,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-804','3号楼','3C-804','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468310,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1204','3号楼','3C-1204','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468342,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-405','4号楼','4C-405','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468374,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-801','4号楼','4C-801','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468406,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1201','4号楼','4C-1201','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468119,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','1号楼-101','1号楼','101','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468151,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-501','2号楼','2C-501','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468183,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-806','2号楼','2C-806','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468215,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1206','2号楼','2C-1206','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468247,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-409','3号楼','3C-409','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468279,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-805','3号楼','3C-805','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468311,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1205','3号楼','3C-1205','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468343,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-406','4号楼','4C-406','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468375,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-802','4号楼','4C-802','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468407,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1202','4号楼','4C-1202','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468120,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','1号楼-102','1号楼','102','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468152,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-502','2号楼','2C-502','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468184,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-807','2号楼','2C-807','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468216,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1207','2号楼','2C-1207','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468248,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-501','3号楼','3C-501','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468280,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-806','3号楼','3C-806','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468312,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1206','3号楼','3C-1206','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468344,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-407','4号楼','4C-407','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468376,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-803','4号楼','4C-803','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468408,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1203','4号楼','4C-1203','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468121,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','1号楼-103','1号楼','103','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468153,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-503','2号楼','2C-503','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468185,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-808','2号楼','2C-808','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468217,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1301','2号楼','2C-1301','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468249,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-502','3号楼','3C-502','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468281,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-807','3号楼','3C-807','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468313,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1207','3号楼','3C-1207','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468345,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-408','4号楼','4C-408','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468377,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-804','4号楼','4C-804','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468409,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1204','4号楼','4C-1204','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468122,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','1号楼-104','1号楼','104','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468154,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-504','2号楼','2C-504','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468186,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-809','2号楼','2C-809','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468218,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1302','2号楼','2C-1302','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468250,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-503','3号楼','3C-503','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468282,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-808','3号楼','3C-808','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468314,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1301','3号楼','3C-1301','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468346,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-409','4号楼','4C-409','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468378,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-805','4号楼','4C-805','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468410,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1205','4号楼','4C-1205','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468123,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','1号楼-105','1号楼','105','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468155,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-505','2号楼','2C-505','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468187,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-901','2号楼','2C-901','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468219,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1303','2号楼','2C-1303','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468251,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-504','3号楼','3C-504','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468283,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-809','3号楼','3C-809','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468315,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1302','3号楼','3C-1302','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468347,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-501','4号楼','4C-501','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468379,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-806','4号楼','4C-806','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468411,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1206','4号楼','4C-1206','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468124,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','1号楼-106','1号楼','106','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468156,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-506','2号楼','2C-506','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468188,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-902','2号楼','2C-902','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468220,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1304','2号楼','2C-1304','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468252,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-505','3号楼','3C-505','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468284,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-901','3号楼','3C-901','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468316,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1303','3号楼','3C-1303','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468348,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-502','4号楼','4C-502','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468380,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-807','4号楼','4C-807','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468412,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1207','4号楼','4C-1207','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468125,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','1号楼-2--9层','1号楼','2--9层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468157,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-507','2号楼','2C-507','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468189,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-903','2号楼','2C-903','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468221,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1305','2号楼','2C-1305','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468253,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-506','3号楼','3C-506','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468285,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-902','3号楼','3C-902','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468317,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1304','3号楼','3C-1304','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468349,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-503','4号楼','4C-503','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468381,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-808','4号楼','4C-808','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468413,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1301','4号楼','4C-1301','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468126,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2A2--5层','2号楼','2A2--5层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468158,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-508','2号楼','2C-508','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468190,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-904','2号楼','2C-904','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468222,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1306','2号楼','2C-1306','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468254,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-507','3号楼','3C-507','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468286,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-903','3号楼','3C-903','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468318,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1305','3号楼','3C-1305','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468350,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-504','4号楼','4C-504','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468382,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-809','4号楼','4C-809','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468414,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1302','4号楼','4C-1302','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468127,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2B-301','2号楼','2B-301','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468159,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-509','2号楼','2C-509','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468191,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-905','2号楼','2C-905','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468223,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1307','2号楼','2C-1307','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468255,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-508','3号楼','3C-508','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468287,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-904','3号楼','3C-904','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468319,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1306','3号楼','3C-1306','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468351,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-505','4号楼','4C-505','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468383,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-901','4号楼','4C-901','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468415,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1303','4号楼','4C-1303','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468128,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2B-401','2号楼','2B-401','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468160,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-601','2号楼','2C-601','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468192,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-906','2号楼','2C-906','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468224,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3B-301','3号楼','3B-301','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468256,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-509','3号楼','3C-509','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468288,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-905','3号楼','3C-905','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468320,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1307','3号楼','3C-1307','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468352,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-506','4号楼','4C-506','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468384,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-902','4号楼','4C-902','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468416,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1304','4号楼','4C-1304','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468129,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2B-501','2号楼','2B-501','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468161,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-602','2号楼','2C-602','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468193,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-907','2号楼','2C-907','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468225,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3B-401','3号楼','3B-401','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468257,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-601','3号楼','3C-601','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468289,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-906','3号楼','3C-906','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468321,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3-A-2-5层','3号楼','3-A-2-5层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468353,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-507','4号楼','4C-507','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468385,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-903','4号楼','4C-903','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468417,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1305','4号楼','4C-1305','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468130,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2B-601','2号楼','2B-601','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468162,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-603','2号楼','2C-603','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468194,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-908','2号楼','2C-908','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468226,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3B-501','3号楼','3B-501','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468258,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-602','3号楼','3C-602','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468290,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-907','3号楼','3C-907','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468322,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4-A-2-5层','4号楼','4-A-2-5层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468354,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-508','4号楼','4C-508','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468386,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-904','4号楼','4C-904','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468418,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1306','4号楼','4C-1306','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468131,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2B-701','2号楼','2B-701','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468163,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-604','2号楼','2C-604','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468195,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-909','2号楼','2C-909','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468227,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3B-601','3号楼','3B-601','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468259,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-603','3号楼','3C-603','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468291,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-908','3号楼','3C-908','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468323,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4B-301','4号楼','4B-301','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468355,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-509','4号楼','4C-509','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468387,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-905','4号楼','4C-905','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468419,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-1307','4号楼','4C-1307','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468132,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2B-801','2号楼','2B-801','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468164,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-605','2号楼','2C-605','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468196,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1001','2号楼','2C-1001','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468228,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3B-701','3号楼','3B-701','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468260,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-604','3号楼','3C-604','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468292,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-909','3号楼','3C-909','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468324,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4B-401','4号楼','4B-401','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468356,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-601','4号楼','4C-601','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468388,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-906','4号楼','4C-906','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468420,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','5号楼-1-5层','5号楼','1-5层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468133,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-301','2号楼','2C-301','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468165,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-606','2号楼','2C-606','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468197,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1002','2号楼','2C-1002','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468229,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3B-801','3号楼','3B-801','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468261,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-605','3号楼','3C-605','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468293,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1001','3号楼','3C-1001','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468325,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4B-501','4号楼','4B-501','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468357,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-602','4号楼','4C-602','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468389,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-907','4号楼','4C-907','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468421,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','6号楼-1-4层','6号楼','1-4层','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468134,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-302','2号楼','2C-302','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468166,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-607','2号楼','2C-607','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468198,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','2号楼-2C-1003','2号楼','2C-1003','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468230,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-301','3号楼','3C-301','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468262,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-606','3号楼','3C-606','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468294,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','3号楼-3C-1002','3号楼','3C-1002','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468326,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4B-601','4号楼','4B-601','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468358,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-603','4号楼','4C-603','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468390,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','4号楼-4C-908','4号楼','4C-908','2','0',UTC_TIMESTAMP(),999948,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387468422,UUID(),240111044332060225,18594,'北京市',18595,'昌平区','7号楼-1层','7号楼','1层','2','0',UTC_TIMESTAMP(),999948,NULL);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91516,1042227,240111044332060225,239825274387468134,'2号楼-2C-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91548,1042227,240111044332060225,239825274387468166,'2号楼-2C-607',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91580,1042227,240111044332060225,239825274387468198,'2号楼-2C-1003',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91612,1042227,240111044332060225,239825274387468230,'3号楼-3C-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91644,1042227,240111044332060225,239825274387468262,'3号楼-3C-606',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91676,1042227,240111044332060225,239825274387468294,'3号楼-3C-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91708,1042227,240111044332060225,239825274387468326,'4号楼-4B-601',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91740,1042227,240111044332060225,239825274387468358,'4号楼-4C-603',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91772,1042227,240111044332060225,239825274387468390,'4号楼-4C-908',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91804,1042227,240111044332060225,239825274387468422,'7号楼-1层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91517,1042227,240111044332060225,239825274387468135,'2号楼-2C-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91549,1042227,240111044332060225,239825274387468167,'2号楼-2C-608',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91581,1042227,240111044332060225,239825274387468199,'2号楼-2C-1004',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91613,1042227,240111044332060225,239825274387468231,'3号楼-3C-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91645,1042227,240111044332060225,239825274387468263,'3号楼-3C-607',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91677,1042227,240111044332060225,239825274387468295,'3号楼-3C-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91709,1042227,240111044332060225,239825274387468327,'4号楼-4B-701',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91741,1042227,240111044332060225,239825274387468359,'4号楼-4C-604',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91773,1042227,240111044332060225,239825274387468391,'4号楼-4C-909',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91805,1042227,240111044332060225,239825274387468423,'13号楼-1-4层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91518,1042227,240111044332060225,239825274387468136,'2号楼-2C-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91550,1042227,240111044332060225,239825274387468168,'2号楼-2C-609',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91582,1042227,240111044332060225,239825274387468200,'2号楼-2C-1005',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91614,1042227,240111044332060225,239825274387468232,'3号楼-3C-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91646,1042227,240111044332060225,239825274387468264,'3号楼-3C-608',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91678,1042227,240111044332060225,239825274387468296,'3号楼-3C-1004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91710,1042227,240111044332060225,239825274387468328,'4号楼-4B-801',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91742,1042227,240111044332060225,239825274387468360,'4号楼-4C-605',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91774,1042227,240111044332060225,239825274387468392,'4号楼-4C-1001',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91806,1042227,240111044332060225,239825274387468424,'14号楼-1-6层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91519,1042227,240111044332060225,239825274387468137,'2号楼-2C-305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91551,1042227,240111044332060225,239825274387468169,'2号楼-2C-701',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91583,1042227,240111044332060225,239825274387468201,'2号楼-2C-1006',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91615,1042227,240111044332060225,239825274387468233,'3号楼-3C-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91647,1042227,240111044332060225,239825274387468265,'3号楼-3C-609',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91679,1042227,240111044332060225,239825274387468297,'3号楼-3C-1005',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91711,1042227,240111044332060225,239825274387468329,'4号楼-4C-301',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91743,1042227,240111044332060225,239825274387468361,'4号楼-4C-606',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91775,1042227,240111044332060225,239825274387468393,'4号楼-4C-1002',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91807,1042227,240111044332060225,239825274387468425,'15号楼-1-8层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91520,1042227,240111044332060225,239825274387468138,'2号楼-2C-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91552,1042227,240111044332060225,239825274387468170,'2号楼-2C-702',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91584,1042227,240111044332060225,239825274387468202,'2号楼-2C-1007',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91616,1042227,240111044332060225,239825274387468234,'3号楼-3C-305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91648,1042227,240111044332060225,239825274387468266,'3号楼-3C-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91680,1042227,240111044332060225,239825274387468298,'3号楼-3C-1006',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91712,1042227,240111044332060225,239825274387468330,'4号楼-4C-302',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91744,1042227,240111044332060225,239825274387468362,'4号楼-4C-607',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91776,1042227,240111044332060225,239825274387468394,'4号楼-4C-1003',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91808,1042227,240111044332060225,239825274387468426,'16号楼-1-7层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91521,1042227,240111044332060225,239825274387468139,'2号楼-2C-307',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91553,1042227,240111044332060225,239825274387468171,'2号楼-2C-703',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91585,1042227,240111044332060225,239825274387468203,'2号楼-2C-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91617,1042227,240111044332060225,239825274387468235,'3号楼-3C-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91649,1042227,240111044332060225,239825274387468267,'3号楼-3C-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91681,1042227,240111044332060225,239825274387468299,'3号楼-3C-1007',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91713,1042227,240111044332060225,239825274387468331,'4号楼-4C-303',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91745,1042227,240111044332060225,239825274387468363,'4号楼-4C-608',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91777,1042227,240111044332060225,239825274387468395,'4号楼-4C-1004',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91809,1042227,240111044332060225,239825274387468427,'下沉广场-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91522,1042227,240111044332060225,239825274387468140,'2号楼-2C-308',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91554,1042227,240111044332060225,239825274387468172,'2号楼-2C-704',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91586,1042227,240111044332060225,239825274387468204,'2号楼-2C-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91618,1042227,240111044332060225,239825274387468236,'3号楼-3C-307',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91650,1042227,240111044332060225,239825274387468268,'3号楼-3C-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91682,1042227,240111044332060225,239825274387468300,'3号楼-3C-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91714,1042227,240111044332060225,239825274387468332,'4号楼-4C-304',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91746,1042227,240111044332060225,239825274387468364,'4号楼-4C-609',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91778,1042227,240111044332060225,239825274387468396,'4号楼-4C-1005',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91810,1042227,240111044332060225,239825274387468428,'下沉广场-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91523,1042227,240111044332060225,239825274387468141,'2号楼-2C-309',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91555,1042227,240111044332060225,239825274387468173,'2号楼-2C-705',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91587,1042227,240111044332060225,239825274387468205,'2号楼-2C-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91619,1042227,240111044332060225,239825274387468237,'3号楼-3C-308',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91651,1042227,240111044332060225,239825274387468269,'3号楼-3C-704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91683,1042227,240111044332060225,239825274387468301,'3号楼-3C-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91715,1042227,240111044332060225,239825274387468333,'4号楼-4C-305',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91747,1042227,240111044332060225,239825274387468365,'4号楼-4C-701',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91779,1042227,240111044332060225,239825274387468397,'4号楼-4C-1006',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91811,1042227,240111044332060225,239825274387468429,'下沉广场-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91524,1042227,240111044332060225,239825274387468142,'2号楼-2C-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91556,1042227,240111044332060225,239825274387468174,'2号楼-2C-706',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91588,1042227,240111044332060225,239825274387468206,'2号楼-2C-1104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91620,1042227,240111044332060225,239825274387468238,'3号楼-3C-309',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91652,1042227,240111044332060225,239825274387468270,'3号楼-3C-705',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91684,1042227,240111044332060225,239825274387468302,'3号楼-3C-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91716,1042227,240111044332060225,239825274387468334,'4号楼-4C-306',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91748,1042227,240111044332060225,239825274387468366,'4号楼-4C-702',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91780,1042227,240111044332060225,239825274387468398,'4号楼-4C-1007',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91812,1042227,240111044332060225,239825274387468430,'下沉广场-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91525,1042227,240111044332060225,239825274387468143,'2号楼-2C-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91557,1042227,240111044332060225,239825274387468175,'2号楼-2C-707',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91589,1042227,240111044332060225,239825274387468207,'2号楼-2C-1105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91621,1042227,240111044332060225,239825274387468239,'3号楼-3C-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91653,1042227,240111044332060225,239825274387468271,'3号楼-3C-706',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91685,1042227,240111044332060225,239825274387468303,'3号楼-3C-1104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91717,1042227,240111044332060225,239825274387468335,'4号楼-4C-307',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91749,1042227,240111044332060225,239825274387468367,'4号楼-4C-703',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91781,1042227,240111044332060225,239825274387468399,'4号楼-4C-1101',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91813,1042227,240111044332060225,239825274387468431,'下沉广场-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91526,1042227,240111044332060225,239825274387468144,'2号楼-2C-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91558,1042227,240111044332060225,239825274387468176,'2号楼-2C-708',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91590,1042227,240111044332060225,239825274387468208,'2号楼-2C-1106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91622,1042227,240111044332060225,239825274387468240,'3号楼-3C-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91654,1042227,240111044332060225,239825274387468272,'3号楼-3C-707',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91686,1042227,240111044332060225,239825274387468304,'3号楼-3C-1105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91718,1042227,240111044332060225,239825274387468336,'4号楼-4C-308',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91750,1042227,240111044332060225,239825274387468368,'4号楼-4C-704',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91782,1042227,240111044332060225,239825274387468400,'4号楼-4C-1102',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91814,1042227,240111044332060225,239825274387468432,'下沉广场-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91527,1042227,240111044332060225,239825274387468145,'2号楼-2C-404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91559,1042227,240111044332060225,239825274387468177,'2号楼-2C-709',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91591,1042227,240111044332060225,239825274387468209,'2号楼-2C-1107',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91623,1042227,240111044332060225,239825274387468241,'3号楼-3C-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91655,1042227,240111044332060225,239825274387468273,'3号楼-3C-708',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91687,1042227,240111044332060225,239825274387468305,'3号楼-3C-1106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91719,1042227,240111044332060225,239825274387468337,'4号楼-4C-309',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91751,1042227,240111044332060225,239825274387468369,'4号楼-4C-705',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91783,1042227,240111044332060225,239825274387468401,'4号楼-4C-1103',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91815,1042227,240111044332060225,239825274387468433,'下沉广场-107',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91528,1042227,240111044332060225,239825274387468146,'2号楼-2C-405',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91560,1042227,240111044332060225,239825274387468178,'2号楼-2C-801',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91592,1042227,240111044332060225,239825274387468210,'2号楼-2C-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91624,1042227,240111044332060225,239825274387468242,'3号楼-3C-404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91656,1042227,240111044332060225,239825274387468274,'3号楼-3C-709',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91688,1042227,240111044332060225,239825274387468306,'3号楼-3C-1107',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91720,1042227,240111044332060225,239825274387468338,'4号楼-4C-401',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91752,1042227,240111044332060225,239825274387468370,'4号楼-4C-706',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91784,1042227,240111044332060225,239825274387468402,'4号楼-4C-1104',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91816,1042227,240111044332060225,239825274387468434,'下沉广场-108',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91529,1042227,240111044332060225,239825274387468147,'2号楼-2C-406',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91561,1042227,240111044332060225,239825274387468179,'2号楼-2C-802',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91593,1042227,240111044332060225,239825274387468211,'2号楼-2C-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91625,1042227,240111044332060225,239825274387468243,'3号楼-3C-405',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91657,1042227,240111044332060225,239825274387468275,'3号楼-3C-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91689,1042227,240111044332060225,239825274387468307,'3号楼-3C-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91721,1042227,240111044332060225,239825274387468339,'4号楼-4C-402',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91753,1042227,240111044332060225,239825274387468371,'4号楼-4C-707',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91785,1042227,240111044332060225,239825274387468403,'4号楼-4C-1105',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91817,1042227,240111044332060225,239825274387468435,'下沉广场-109',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91530,1042227,240111044332060225,239825274387468148,'2号楼-2C-407',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91562,1042227,240111044332060225,239825274387468180,'2号楼-2C-803',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91594,1042227,240111044332060225,239825274387468212,'2号楼-2C-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91626,1042227,240111044332060225,239825274387468244,'3号楼-3C-406',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91658,1042227,240111044332060225,239825274387468276,'3号楼-3C-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91690,1042227,240111044332060225,239825274387468308,'3号楼-3C-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91722,1042227,240111044332060225,239825274387468340,'4号楼-4C-403',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91754,1042227,240111044332060225,239825274387468372,'4号楼-4C-708',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91786,1042227,240111044332060225,239825274387468404,'4号楼-4C-1106',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91818,1042227,240111044332060225,239825274387468436,'下沉广场-110',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91531,1042227,240111044332060225,239825274387468149,'2号楼-2C-408',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91563,1042227,240111044332060225,239825274387468181,'2号楼-2C-804',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91595,1042227,240111044332060225,239825274387468213,'2号楼-2C-1204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91627,1042227,240111044332060225,239825274387468245,'3号楼-3C-407',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91659,1042227,240111044332060225,239825274387468277,'3号楼-3C-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91691,1042227,240111044332060225,239825274387468309,'3号楼-3C-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91723,1042227,240111044332060225,239825274387468341,'4号楼-4C-404',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91755,1042227,240111044332060225,239825274387468373,'4号楼-4C-709',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91787,1042227,240111044332060225,239825274387468405,'4号楼-4C-1107',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91819,1042227,240111044332060225,239825274387468437,'下沉广场-111',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91532,1042227,240111044332060225,239825274387468150,'2号楼-2C-409',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91564,1042227,240111044332060225,239825274387468182,'2号楼-2C-805',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91596,1042227,240111044332060225,239825274387468214,'2号楼-2C-1205',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91628,1042227,240111044332060225,239825274387468246,'3号楼-3C-408',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91660,1042227,240111044332060225,239825274387468278,'3号楼-3C-804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91692,1042227,240111044332060225,239825274387468310,'3号楼-3C-1204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91724,1042227,240111044332060225,239825274387468342,'4号楼-4C-405',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91756,1042227,240111044332060225,239825274387468374,'4号楼-4C-801',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91788,1042227,240111044332060225,239825274387468406,'4号楼-4C-1201',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91501,1042227,240111044332060225,239825274387468119,'1号楼-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91533,1042227,240111044332060225,239825274387468151,'2号楼-2C-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91565,1042227,240111044332060225,239825274387468183,'2号楼-2C-806',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91597,1042227,240111044332060225,239825274387468215,'2号楼-2C-1206',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91629,1042227,240111044332060225,239825274387468247,'3号楼-3C-409',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91661,1042227,240111044332060225,239825274387468279,'3号楼-3C-805',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91693,1042227,240111044332060225,239825274387468311,'3号楼-3C-1205',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91725,1042227,240111044332060225,239825274387468343,'4号楼-4C-406',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91757,1042227,240111044332060225,239825274387468375,'4号楼-4C-802',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91789,1042227,240111044332060225,239825274387468407,'4号楼-4C-1202',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91502,1042227,240111044332060225,239825274387468120,'1号楼-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91534,1042227,240111044332060225,239825274387468152,'2号楼-2C-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91566,1042227,240111044332060225,239825274387468184,'2号楼-2C-807',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91598,1042227,240111044332060225,239825274387468216,'2号楼-2C-1207',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91630,1042227,240111044332060225,239825274387468248,'3号楼-3C-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91662,1042227,240111044332060225,239825274387468280,'3号楼-3C-806',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91694,1042227,240111044332060225,239825274387468312,'3号楼-3C-1206',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91726,1042227,240111044332060225,239825274387468344,'4号楼-4C-407',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91758,1042227,240111044332060225,239825274387468376,'4号楼-4C-803',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91790,1042227,240111044332060225,239825274387468408,'4号楼-4C-1203',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91503,1042227,240111044332060225,239825274387468121,'1号楼-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91535,1042227,240111044332060225,239825274387468153,'2号楼-2C-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91567,1042227,240111044332060225,239825274387468185,'2号楼-2C-808',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91599,1042227,240111044332060225,239825274387468217,'2号楼-2C-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91631,1042227,240111044332060225,239825274387468249,'3号楼-3C-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91663,1042227,240111044332060225,239825274387468281,'3号楼-3C-807',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91695,1042227,240111044332060225,239825274387468313,'3号楼-3C-1207',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91727,1042227,240111044332060225,239825274387468345,'4号楼-4C-408',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91759,1042227,240111044332060225,239825274387468377,'4号楼-4C-804',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91791,1042227,240111044332060225,239825274387468409,'4号楼-4C-1204',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91504,1042227,240111044332060225,239825274387468122,'1号楼-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91536,1042227,240111044332060225,239825274387468154,'2号楼-2C-504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91568,1042227,240111044332060225,239825274387468186,'2号楼-2C-809',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91600,1042227,240111044332060225,239825274387468218,'2号楼-2C-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91632,1042227,240111044332060225,239825274387468250,'3号楼-3C-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91664,1042227,240111044332060225,239825274387468282,'3号楼-3C-808',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91696,1042227,240111044332060225,239825274387468314,'3号楼-3C-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91728,1042227,240111044332060225,239825274387468346,'4号楼-4C-409',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91760,1042227,240111044332060225,239825274387468378,'4号楼-4C-805',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91792,1042227,240111044332060225,239825274387468410,'4号楼-4C-1205',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91505,1042227,240111044332060225,239825274387468123,'1号楼-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91537,1042227,240111044332060225,239825274387468155,'2号楼-2C-505',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91569,1042227,240111044332060225,239825274387468187,'2号楼-2C-901',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91601,1042227,240111044332060225,239825274387468219,'2号楼-2C-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91633,1042227,240111044332060225,239825274387468251,'3号楼-3C-504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91665,1042227,240111044332060225,239825274387468283,'3号楼-3C-809',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91697,1042227,240111044332060225,239825274387468315,'3号楼-3C-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91729,1042227,240111044332060225,239825274387468347,'4号楼-4C-501',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91761,1042227,240111044332060225,239825274387468379,'4号楼-4C-806',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91793,1042227,240111044332060225,239825274387468411,'4号楼-4C-1206',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91506,1042227,240111044332060225,239825274387468124,'1号楼-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91538,1042227,240111044332060225,239825274387468156,'2号楼-2C-506',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91570,1042227,240111044332060225,239825274387468188,'2号楼-2C-902',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91602,1042227,240111044332060225,239825274387468220,'2号楼-2C-1304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91634,1042227,240111044332060225,239825274387468252,'3号楼-3C-505',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91666,1042227,240111044332060225,239825274387468284,'3号楼-3C-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91698,1042227,240111044332060225,239825274387468316,'3号楼-3C-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91730,1042227,240111044332060225,239825274387468348,'4号楼-4C-502',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91762,1042227,240111044332060225,239825274387468380,'4号楼-4C-807',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91794,1042227,240111044332060225,239825274387468412,'4号楼-4C-1207',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91507,1042227,240111044332060225,239825274387468125,'1号楼-2--9层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91539,1042227,240111044332060225,239825274387468157,'2号楼-2C-507',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91571,1042227,240111044332060225,239825274387468189,'2号楼-2C-903',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91603,1042227,240111044332060225,239825274387468221,'2号楼-2C-1305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91635,1042227,240111044332060225,239825274387468253,'3号楼-3C-506',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91667,1042227,240111044332060225,239825274387468285,'3号楼-3C-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91699,1042227,240111044332060225,239825274387468317,'3号楼-3C-1304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91731,1042227,240111044332060225,239825274387468349,'4号楼-4C-503',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91763,1042227,240111044332060225,239825274387468381,'4号楼-4C-808',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91795,1042227,240111044332060225,239825274387468413,'4号楼-4C-1301',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91508,1042227,240111044332060225,239825274387468126,'2号楼-2A2--5层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91540,1042227,240111044332060225,239825274387468158,'2号楼-2C-508',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91572,1042227,240111044332060225,239825274387468190,'2号楼-2C-904',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91604,1042227,240111044332060225,239825274387468222,'2号楼-2C-1306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91636,1042227,240111044332060225,239825274387468254,'3号楼-3C-507',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91668,1042227,240111044332060225,239825274387468286,'3号楼-3C-903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91700,1042227,240111044332060225,239825274387468318,'3号楼-3C-1305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91732,1042227,240111044332060225,239825274387468350,'4号楼-4C-504',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91764,1042227,240111044332060225,239825274387468382,'4号楼-4C-809',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91796,1042227,240111044332060225,239825274387468414,'4号楼-4C-1302',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91509,1042227,240111044332060225,239825274387468127,'2号楼-2B-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91541,1042227,240111044332060225,239825274387468159,'2号楼-2C-509',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91573,1042227,240111044332060225,239825274387468191,'2号楼-2C-905',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91605,1042227,240111044332060225,239825274387468223,'2号楼-2C-1307',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91637,1042227,240111044332060225,239825274387468255,'3号楼-3C-508',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91669,1042227,240111044332060225,239825274387468287,'3号楼-3C-904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91701,1042227,240111044332060225,239825274387468319,'3号楼-3C-1306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91733,1042227,240111044332060225,239825274387468351,'4号楼-4C-505',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91765,1042227,240111044332060225,239825274387468383,'4号楼-4C-901',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91797,1042227,240111044332060225,239825274387468415,'4号楼-4C-1303',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91510,1042227,240111044332060225,239825274387468128,'2号楼-2B-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91542,1042227,240111044332060225,239825274387468160,'2号楼-2C-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91574,1042227,240111044332060225,239825274387468192,'2号楼-2C-906',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91606,1042227,240111044332060225,239825274387468224,'3号楼-3B-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91638,1042227,240111044332060225,239825274387468256,'3号楼-3C-509',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91670,1042227,240111044332060225,239825274387468288,'3号楼-3C-905',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91702,1042227,240111044332060225,239825274387468320,'3号楼-3C-1307',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91734,1042227,240111044332060225,239825274387468352,'4号楼-4C-506',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91766,1042227,240111044332060225,239825274387468384,'4号楼-4C-902',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91798,1042227,240111044332060225,239825274387468416,'4号楼-4C-1304',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91511,1042227,240111044332060225,239825274387468129,'2号楼-2B-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91543,1042227,240111044332060225,239825274387468161,'2号楼-2C-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91575,1042227,240111044332060225,239825274387468193,'2号楼-2C-907',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91607,1042227,240111044332060225,239825274387468225,'3号楼-3B-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91639,1042227,240111044332060225,239825274387468257,'3号楼-3C-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91671,1042227,240111044332060225,239825274387468289,'3号楼-3C-906',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91703,1042227,240111044332060225,239825274387468321,'3号楼-3-A-2-5层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91735,1042227,240111044332060225,239825274387468353,'4号楼-4C-507',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91767,1042227,240111044332060225,239825274387468385,'4号楼-4C-903',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91799,1042227,240111044332060225,239825274387468417,'4号楼-4C-1305',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91512,1042227,240111044332060225,239825274387468130,'2号楼-2B-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91544,1042227,240111044332060225,239825274387468162,'2号楼-2C-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91576,1042227,240111044332060225,239825274387468194,'2号楼-2C-908',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91608,1042227,240111044332060225,239825274387468226,'3号楼-3B-501',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91640,1042227,240111044332060225,239825274387468258,'3号楼-3C-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91672,1042227,240111044332060225,239825274387468290,'3号楼-3C-907',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91704,1042227,240111044332060225,239825274387468322,'4号楼-4-A-2-5层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91736,1042227,240111044332060225,239825274387468354,'4号楼-4C-508',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91768,1042227,240111044332060225,239825274387468386,'4号楼-4C-904',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91800,1042227,240111044332060225,239825274387468418,'4号楼-4C-1306',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91513,1042227,240111044332060225,239825274387468131,'2号楼-2B-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91545,1042227,240111044332060225,239825274387468163,'2号楼-2C-604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91577,1042227,240111044332060225,239825274387468195,'2号楼-2C-909',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91609,1042227,240111044332060225,239825274387468227,'3号楼-3B-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91641,1042227,240111044332060225,239825274387468259,'3号楼-3C-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91673,1042227,240111044332060225,239825274387468291,'3号楼-3C-908',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91705,1042227,240111044332060225,239825274387468323,'4号楼-4B-301',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91737,1042227,240111044332060225,239825274387468355,'4号楼-4C-509',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91769,1042227,240111044332060225,239825274387468387,'4号楼-4C-905',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91801,1042227,240111044332060225,239825274387468419,'4号楼-4C-1307',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91514,1042227,240111044332060225,239825274387468132,'2号楼-2B-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91546,1042227,240111044332060225,239825274387468164,'2号楼-2C-605',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91578,1042227,240111044332060225,239825274387468196,'2号楼-2C-1001',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91610,1042227,240111044332060225,239825274387468228,'3号楼-3B-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91642,1042227,240111044332060225,239825274387468260,'3号楼-3C-604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91674,1042227,240111044332060225,239825274387468292,'3号楼-3C-909',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91706,1042227,240111044332060225,239825274387468324,'4号楼-4B-401',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91738,1042227,240111044332060225,239825274387468356,'4号楼-4C-601',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91770,1042227,240111044332060225,239825274387468388,'4号楼-4C-906',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91802,1042227,240111044332060225,239825274387468420,'5号楼-1-5层',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91515,1042227,240111044332060225,239825274387468133,'2号楼-2C-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91547,1042227,240111044332060225,239825274387468165,'2号楼-2C-606',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91579,1042227,240111044332060225,239825274387468197,'2号楼-2C-1002',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91611,1042227,240111044332060225,239825274387468229,'3号楼-3B-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91643,1042227,240111044332060225,239825274387468261,'3号楼-3C-605',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91675,1042227,240111044332060225,239825274387468293,'3号楼-3C-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91707,1042227,240111044332060225,239825274387468325,'4号楼-4B-501',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91739,1042227,240111044332060225,239825274387468357,'4号楼-4C-602',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91771,1042227,240111044332060225,239825274387468389,'4号楼-4C-907',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91803,1042227,240111044332060225,239825274387468421,'6号楼-1-4层',4);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142573,10000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142574,10100,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142575,10400,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142576,10200,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142577,10600,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142578,10750,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142579,10751,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142580,10752,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142581,11000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142582,12200,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142583,20000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142584,20100,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142585,20140,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142586,20150,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142587,20155,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142588,20158,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142589,20170,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142590,20180,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142591,20190,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142592,20191,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142593,40000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142594,40150,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142595,40400,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142596,40410,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142597,40420,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142598,40430,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142599,40440,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142600,40450,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142601,40500,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142602,41600,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142603,41610,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142604,41620,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142605,41630,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142606,30000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142607,30500,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142608,30550,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142609,31000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142610,32000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142611,33000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142612,34000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142613,35000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142614,50000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142615,50100,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142616,50300,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142617,50400,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142618,50500,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142619,50700,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142620,50710,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142621,50720,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142622,50730,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142623,50900,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142624,52000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142625,52010,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142626,52020,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142627,52030,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142628,70000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142629,70300,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142630,70100,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142631,70200,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142632,60000,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142633,60100,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142634,60200,NULL,'EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142635,808000,NULL,'EhNamespaces',999948,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001461,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999948,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001462,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999948,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001463,'',0,1001463,1,'活动管理-默认子分类','/1/1001463',0,2,1,NOW(),0,NULL,999948,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001464,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999948,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001465,'',0,1001465,2,'活动管理二-默认子分类','/2/1001465',0,2,1,NOW(),0,NULL,999948,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001466,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999948,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001467,'',0,1001467,3,'活动管理三-默认子分类','/3/1001467',0,2,1,NOW(),0,NULL,999948,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (616,999948,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (617,999948,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (618,999948,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (619,999948,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (620,999948,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (621,999948,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (622,999948,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (623,999948,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (624,999948,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1042227,240111044332060225);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1172,999948,'HomeKuaidiLayout','{"versionCode":"2018030501","layoutName":"HomeKuaidiLayout","displayName":"快递","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1173,999948,'HomeKuaidiLayout','{"versionCode":"2018030501","layoutName":"HomeKuaidiLayout","displayName":"快递","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1174,999948,'ServiceMarketLayout','{"versionCode":"2018030501","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"title":"空间活动","iconUrl": "https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize":3,"entityCount":3,"subjectHeight":1,"descriptionHeight":3},"style": "ListView","defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2018030501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1175,999948,'ServiceMarketLayout','{"versionCode":"2018030501","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"title":"空间活动","iconUrl": "https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize":3,"entityCount":3,"subjectHeight":1,"descriptionHeight":3},"style": "ListView","defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2018030501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
-- INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
-- 	VALUES (1176,999948,'HomeYunjikuaixunLayout','{"versionCode":"2018030501","layoutName":"HomeYunjikuaixunLayout","displayName":"云集快讯","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
-- INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
-- 	VALUES (1177,999948,'HomeYunjikuaixunLayout','{"versionCode":"2018030501","layoutName":"HomeYunjikuaixunLayout","displayName":"云集快讯","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1178,999948,'HomeYunjishanghuLayout','{"versionCode":"2018030501","layoutName":"HomeYunjishanghuLayout","displayName":"云集商户","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1179,999948,'HomeYunjishanghuLayout','{"versionCode":"2018030501","layoutName":"HomeYunjishanghuLayout","displayName":"云集商户","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1180,999948,'HomeYunjifuwuLayout','{"versionCode":"2018030501","layoutName":"HomeYunjifuwuLayout","displayName":"云集服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1181,999948,'HomeYunjifuwuLayout','{"versionCode":"2018030501","layoutName":"HomeYunjifuwuLayout","displayName":"云集服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1182,999948,'HomeYunjibaoxiuLayout','{"versionCode":"2018030501","layoutName":"HomeYunjibaoxiuLayout","displayName":"云集报修","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1183,999948,'HomeYunjibaoxiuLayout','{"versionCode":"2018030501","layoutName":"HomeYunjibaoxiuLayout","displayName":"云集报修","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018030501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (217155,999948,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRvd1pUZGhOekZsTmpOaVpERmxOV1poT0RabE5qazNOVEJqTnpFeE5qYzRNQQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (217156,999948,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRvd1pUZGhOekZsTmpOaVpERmxOV1poT0RabE5qazNOVEJqTnpFeE5qYzRNQQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161325,999948,0,0,0,'/home','NavigatorGroup3','云集报修','云集报修','cs://1/image/aW1hZ2UvTVRvMk16aGhNREEwTW1ReU1EY3pZamd5T0RObE5tRTROR0ZtWmpsaFlUSTNaZw',1,1,2,'{"itemLocation":"/home/yunjibaoxiu","layoutName":"HomeYunjibaoxiuLayout","title":"云集报修","entityTag":"PM"}',10,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161326,999948,0,0,0,'/home','NavigatorGroup3','云集报修','云集报修','cs://1/image/aW1hZ2UvTVRvMk16aGhNREEwTW1ReU1EY3pZamd5T0RObE5tRTROR0ZtWmpsaFlUSTNaZw',1,1,2,'{"itemLocation":"/home/yunjibaoxiu","layoutName":"HomeYunjibaoxiuLayout","title":"云集报修","entityTag":"PM"}',10,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161327,999948,0,0,0,'/home','NavigatorGroup3','云集快讯','云集快讯','cs://1/image/aW1hZ2UvTVRvMU1qQmtOR0prWVRVNU5ESmxPV1V3TkRVd056WTJOR0V4T0Rsak56Y3pZZw',1,1,48,'{"categoryId":1,"timeWidgetStyle":"date"}',20,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161328,999948,0,0,0,'/home','NavigatorGroup3','云集快讯','云集快讯','cs://1/image/aW1hZ2UvTVRvMU1qQmtOR0prWVRVNU5ESmxPV1V3TkRVd056WTJOR0V4T0Rsak56Y3pZZw',1,1,48,'{"categoryId":1,"timeWidgetStyle":"date"}',20,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161329,999948,0,0,0,'/home','NavigatorGroup3','云集商户','云集商户','cs://1/image/aW1hZ2UvTVRvMFptRXdZekJoTlRNM1pqQXpaVFV3WkdJNVlUZG1NemczTlRNNVpHWTJPUQ',1,1,2,'{"itemLocation":"/home/yunjishanghu","layoutName":"HomeYunjishanghuLayout","title":"云集商户","entityTag":"PM"}',30,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161330,999948,0,0,0,'/home','NavigatorGroup3','云集商户','云集商户','cs://1/image/aW1hZ2UvTVRvMFptRXdZekJoTlRNM1pqQXpaVFV3WkdJNVlUZG1NemczTlRNNVpHWTJPUQ',1,1,2,'{"itemLocation":"/home/yunjishanghu","layoutName":"HomeYunjishanghuLayout","title":"云集商户","entityTag":"PM"}',30,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161331,999948,0,0,0,'/home','NavigatorGroup3','云集服务','云集服务','cs://1/image/aW1hZ2UvTVRvMFptRXdZekJoTlRNM1pqQXpaVFV3WkdJNVlUZG1NemczTlRNNVpHWTJPUQ',1,1,2,'{"itemLocation":"/home/yunjifuwu","layoutName":"HomeYunjifuwuLayout","title":"云集服务","entityTag":"PM"}',40,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161332,999948,0,0,0,'/home','NavigatorGroup3','云集服务','云集服务','cs://1/image/aW1hZ2UvTVRvMFptRXdZekJoTlRNM1pqQXpaVFV3WkdJNVlUZG1NemczTlRNNVpHWTJPUQ',1,1,2,'{"itemLocation":"/home/yunjifuwu","layoutName":"HomeYunjifuwuLayout","title":"云集服务","entityTag":"PM"}',40,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161333,999948,0,0,0,'/home','NavigatorGroup3','快递','快递','cs://1/image/aW1hZ2UvTVRvellUSXdOVFE1WWpRMk5EWTFZV1l4WldFeU1UazVZakE0TkdZNFpHSTJNZw',1,1,2,'{"itemLocation":"/home/kuaidi","layoutName":"HomeKuaidiLayout","title":"快递","entityTag":"PM"}',50,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161334,999948,0,0,0,'/home','NavigatorGroup3','快递','快递','cs://1/image/aW1hZ2UvTVRvellUSXdOVFE1WWpRMk5EWTFZV1l4WldFeU1UazVZakE0TkdZNFpHSTJNZw',1,1,2,'{"itemLocation":"/home/kuaidi","layoutName":"HomeKuaidiLayout","title":"快递","entityTag":"PM"}',50,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161335,999948,0,0,0,'/home','NavigatorGroup3','送餐','送餐','cs://1/image/aW1hZ2UvTVRvMU0yWmtZV1k0TnpFNE1UQmpZMll5T0RoaVpUa3pObUl4TkRVeE9HWmxPUQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',60,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161336,999948,0,0,0,'/home','NavigatorGroup3','送餐','送餐','cs://1/image/aW1hZ2UvTVRvMU0yWmtZV1k0TnpFNE1UQmpZMll5T0RoaVpUa3pObUl4TkRVeE9HWmxPUQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',60,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161337,999948,0,0,0,'/home','NavigatorGroup3','送水','送水','cs://1/image/aW1hZ2UvTVRvNE9EYzJOell3Wm1VMU1EQXhaak01TmpNMFltVXhOak5qWkRCbVpUbGlOQQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',70,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161338,999948,0,0,0,'/home','NavigatorGroup3','送水','送水','cs://1/image/aW1hZ2UvTVRvNE9EYzJOell3Wm1VMU1EQXhaak01TmpNMFltVXhOak5qWkRCbVpUbGlOQQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',70,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161339,999948,0,0,0,'/home','NavigatorGroup3','满意度调查','满意度调查','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,13,'{"url":"${home.url}/questionnaire-survey/build/index.html#/home#sign_suffix"}',80,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161340,999948,0,0,0,'/home','NavigatorGroup3','满意度调查','满意度调查','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,13,'{"url":"${home.url}/questionnaire-survey/build/index.html#/home#sign_suffix"}',80,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161341,999948,0,0,0,'/home','NavigatorGroup3','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,56,'',90,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161342,999948,0,0,0,'/home','NavigatorGroup3','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,56,'',90,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161343,999948,0,0,0,'/home','NavigatorGroup3','店铺管理','店铺管理','cs://1/image/aW1hZ2UvTVRvMFptRXdZekJoTlRNM1pqQXpaVFV3WkdJNVlUZG1NemczTlRNNVpHWTJPUQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',100,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161344,999948,0,0,0,'/home','NavigatorGroup3','店铺管理','店铺管理','cs://1/image/aW1hZ2UvTVRvMFptRXdZekJoTlRNM1pqQXpaVFV3WkdJNVlUZG1NemczTlRNNVpHWTJPUQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',100,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161345,999948,0,0,0,'/home','NavigatorGroup3','更多','更多','cs://1/image/aW1hZ2UvTVRwak1EZzBZVFU0TWpZeVl6YzVZMkZrTW1GbE56aGpNV1U1WlRZNE9EVmhPQQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup3"}',1000,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161346,999948,0,0,0,'/home','NavigatorGroup3','更多','更多','cs://1/image/aW1hZ2UvTVRwak1EZzBZVFU0TWpZeVl6YzVZMkZrTW1GbE56aGpNV1U1WlRZNE9EVmhPQQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup3"}',1000,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161347,999948,0,0,0,'/home/yunjibaoxiu','NavigatorGroup1','报修','报修','cs://1/image/aW1hZ2UvTVRvMk16aGhNREEwTW1ReU1EY3pZamd5T0RObE5tRTROR0ZtWmpsaFlUSTNaZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=报修"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161348,999948,0,0,0,'/home/yunjibaoxiu','NavigatorGroup1','报修','报修','cs://1/image/aW1hZ2UvTVRvMk16aGhNREEwTW1ReU1EY3pZamd5T0RObE5tRTROR0ZtWmpsaFlUSTNaZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=报修"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161349,999948,0,0,0,'/home/yunjibaoxiu','NavigatorGroup1','投诉','投诉','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161350,999948,0,0,0,'/home/yunjibaoxiu','NavigatorGroup1','投诉','投诉','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161351,999948,0,0,0,'/home/yunjibaoxiu','NavigatorGroup1','物业热线','物业热线','cs://1/image/aW1hZ2UvTVRvMFptRXdZekJoTlRNM1pqQXpaVFV3WkdJNVlUZG1NemczTlRNNVpHWTJPUQ',1,1,45,'',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161352,999948,0,0,0,'/home/yunjibaoxiu','NavigatorGroup1','物业热线','物业热线','cs://1/image/aW1hZ2UvTVRvMFptRXdZekJoTlRNM1pqQXpaVFV3WkdJNVlUZG1NemczTlRNNVpHWTJPUQ',1,1,45,'',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161353,999948,0,0,0,'/home/yunjishanghu','NavigatorGroup1','云集园展示','云集园展示','cs://1/image/aW1hZ2UvTVRwaE9UQmhPV013TldJMVptVmxNVGRsT0dZd016Y3daV00zWkdRME1XSmxaZw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161354,999948,0,0,0,'/home/yunjishanghu','NavigatorGroup1','云集园展示','云集园展示','cs://1/image/aW1hZ2UvTVRwaE9UQmhPV013TldJMVptVmxNVGRsT0dZd016Y3daV00zWkdRME1XSmxaZw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161355,999948,0,0,0,'/home/yunjishanghu','NavigatorGroup1','商家展示','商家展示','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,33,'{"type":213159,"parentId":213159,"displayType": "list"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161356,999948,0,0,0,'/home/yunjishanghu','NavigatorGroup1','商家展示','商家展示','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,33,'{"type":213159,"parentId":213159,"displayType": "list"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161357,999948,0,0,0,'/home/yunjishanghu','NavigatorGroup1','企业展示','企业展示','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,34,'',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161358,999948,0,0,0,'/home/yunjishanghu','NavigatorGroup1','企业展示','企业展示','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,34,'',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161359,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','保洁服务','保洁服务','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,33,'{"type":213160,"parentId":213160,"displayType": "list"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161360,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','保洁服务','保洁服务','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,33,'{"type":213160,"parentId":213160,"displayType": "list"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161361,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','装修服务','装修服务','cs://1/image/aW1hZ2UvTVRvMk16aGhNREEwTW1ReU1EY3pZamd5T0RObE5tRTROR0ZtWmpsaFlUSTNaZw',1,1,33,'{"type":213161,"parentId":213161,"displayType": "list"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161362,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','装修服务','装修服务','cs://1/image/aW1hZ2UvTVRvMk16aGhNREEwTW1ReU1EY3pZamd5T0RObE5tRTROR0ZtWmpsaFlUSTNaZw',1,1,33,'{"type":213161,"parentId":213161,"displayType": "list"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161363,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','文印室','文印室','cs://1/image/aW1hZ2UvTVRwa05HVmtOR05sWlRJNU56TTFOamN3TW1JNU16SXpaR00zT1Rsa00yUmtNQQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161364,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','文印室','文印室','cs://1/image/aW1hZ2UvTVRwa05HVmtOR05sWlRJNU56TTFOamN3TW1JNU16SXpaR00zT1Rsa00yUmtNQQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161365,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRvMU1qQmtOR0prWVRVNU5ESmxPV1V3TkRVd056WTJOR0V4T0Rsak56Y3pZZw',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161366,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRvMU1qQmtOR0prWVRVNU5ESmxPV1V3TkRVd056WTJOR0V4T0Rsak56Y3pZZw',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161367,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','出行','出行','cs://1/image/aW1hZ2UvTVRwbE56YzVOREV3TXpZek4yTmpNMlptWW1JeE9UQXpNVFUzWkRRNU9EZGtaUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',50,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161368,999948,0,0,0,'/home/yunjifuwu','NavigatorGroup1','出行','出行','cs://1/image/aW1hZ2UvTVRwbE56YzVOREV3TXpZek4yTmpNMlptWW1JeE9UQXpNVFUzWkRRNU9EZGtaUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',50,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161369,999948,0,0,0,'/home/kuaidi','NavigatorGroup1','发快递','发快递','cs://1/image/aW1hZ2UvTVRvellUSXdOVFE1WWpRMk5EWTFZV1l4WldFeU1UazVZakE0TkdZNFpHSTJNZw',1,1,65,'',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161370,999948,0,0,0,'/home/kuaidi','NavigatorGroup1','发快递','发快递','cs://1/image/aW1hZ2UvTVRvellUSXdOVFE1WWpRMk5EWTFZV1l4WldFeU1UazVZakE0TkdZNFpHSTJNZw',1,1,65,'',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161371,999948,0,0,0,'/home/kuaidi','NavigatorGroup1','快递柜','快递柜','cs://1/image/aW1hZ2UvTVRvellUSXdOVFE1WWpRMk5EWTFZV1l4WldFeU1UazVZakE0TkdZNFpHSTJNZw',1,1,13,'{"url":"${home.url}/deliver/dist/index.html#/home_page#sign_suffix"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161372,999948,0,0,0,'/home/kuaidi','NavigatorGroup1','快递柜','快递柜','cs://1/image/aW1hZ2UvTVRvellUSXdOVFE1WWpRMk5EWTFZV1l4WldFeU1UazVZakE0TkdZNFpHSTJNZw',1,1,13,'{"url":"${home.url}/deliver/dist/index.html#/home_page#sign_suffix"}',20,0,1,1,0,0,'park_tourist',0,0,'');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
-- 	VALUES (161373,999948,0,0,0,'/home','NavigatorGroup4','空间活动','空间活动','cs://1/image/aW1hZ2UvTVRwbFkyWTBZMk5oT1RSbE9XWmhaVEpsTVRBeU9EUmlPRGc1TkRCbU9HWm1ZUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'pm_admin',0,0,'');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
-- 	VALUES (161374,999948,0,0,0,'/home','NavigatorGroup4','空间活动','空间活动','cs://1/image/aW1hZ2UvTVRwbFkyWTBZMk5oT1RSbE9XWmhaVEpsTVRBeU9EUmlPRGc1TkRCbU9HWm1ZUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161375,999948,0,0,0,'/home','OPPushActivity','活动','活动','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',110,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161376,999948,0,0,0,'/home','OPPushActivity','活动','活动','cs://1/image/aW1hZ2UvTVRvMlpEY3pabVExWlRoak5XWmpNR015TnpreU4yWTBObVkyWkRSbFpXTTVNQQ',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',110,0,1,1,0,1,'park_tourist',0,0,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213159,'community','240111044331055940',0,'商家展示','商家展示',0,2,1,UTC_TIMESTAMP(),999948,1);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213160,'community','240111044331055940',0,'保洁服务','保洁服务',0,2,1,UTC_TIMESTAMP(),999948,2);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213161,'community','240111044331055940',0,'装修服务','装修服务',0,2,1,UTC_TIMESTAMP(),999948,3);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211892,0,'organaization',1042227,'all','商家展示','商家展示',213159,2,211892,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211893,0,'organaization',1042227,'all','保洁服务','保洁服务',213160,2,211893,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211894,0,'organaization',1042227,'all','装修服务','装修服务',213161,2,211894,UTC_TIMESTAMP(),2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142638,41700,'商家展示','EhNamespaces',999948,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142639,41710,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142640,41720,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142641,41730,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142642,41740,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142643,41750,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142644,41760,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142645,41800,'保洁服务','EhNamespaces',999948,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142646,41810,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142647,41820,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142648,41830,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142649,41840,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142650,41850,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142651,41860,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142652,41900,'装修服务','EhNamespaces',999948,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142653,41910,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142654,41920,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142655,41930,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142656,41940,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142657,41950,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (142658,41960,'','EhNamespaces',999948,2);

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (204185,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999948);
INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (204186,9,0,'投诉建议','任务/投诉建议',1,2,UTC_TIMESTAMP(),999948);
    
-- 添加其它菜单
set @id = (select Max(id) from  eh_web_menu_scopes) +1;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,20230,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,20220,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,20240,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,20250,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,20280,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,20255,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,20258,'','EhNamespaces',999948,2);    
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,20290,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,20291,'','EhNamespaces',999948,2);  

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,40300,'','EhNamespaces',999948,2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,10800,'','EhNamespaces',999948,2);


INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,40700,'','EhNamespaces',999948,2);  
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,40710,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,40720,'','EhNamespaces',999948,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (@id:=@id+1,40730,'','EhNamespaces',999948,2);   

set @namespaceId = 999948;
set @id = (select Max(id) from  eh_service_module_scopes) +1;
set @appId = (select max(id) from eh_reflection_service_module_apps);
set @activeAppId = (select max(active_app_id) from eh_reflection_service_module_apps);

INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10100', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10400', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10600', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '11000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '20000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '20100', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40100', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40300', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40500', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40600', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40800', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '30000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '31000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '32000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '33000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '34000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10750', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '32500', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '12200', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '41300', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '41000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '35000', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50000', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50100', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50300', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50500', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50600', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50700', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50800', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50900', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40400', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '51000', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '60000', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '60100', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '60200', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '41500', '用户行为统计', 'EhNamespaces', @namespaceId, NULL, '2');



INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '招聘与求职', '10100', '{\"forumEntryId\":\"2\"}', '2', '62', '{\"forumEntryId\":\"2\"}', '2018-02-25 09:30:11', 'community_control', '0', '', NULL, '10100');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '广告管理', '10400', '', '2', NULL, '', '2018-01-22 14:47:01', 'community_control', '0', '', NULL, '10400');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '活动管理', '10600', '{\"id\":1001370,\"entryId\":1001174,\"publishPrivilege\":1,\"livePrivilege\":0,\"listStyle\":2,\"scope\":3,\"style\":4,\"categoryFlag\":1,\"categoryDTOList\":[{\"id\":1001371,\"ownerType\":\"\",\"ownerId\":0,\"entryId\":1001175,\"parentId\":1001174,\"name\":\"all\",\"path\":\"/1001174/1001175\",\"defaultOrder\":0,\"status\":2,\"creatorUid\":1,\"createTime\":1516777998000,\"deleteUid\":0,\"namespaceId\":999958,\"enabled\":1,\"allFlag\":1}]}', '2', '61', '{\"categoryId\":1001174,\"publishPrivilege\":1,\"livePrivilege\":0,\"listStyle\":2,\"scope\":3,\"style\":4}', '2018-02-25 09:28:49', 'community_control', '1', '1001174', NULL, 10600);
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '旅游与团建', '10600', '{\"id\":1001057,\"name\":\"旅游与团建\",\"entryId\":2,\"publishPrivilege\":1,\"livePrivilege\":0,\"listStyle\":2,\"scope\":3,\"style\":4,\"categoryFlag\":1,\"categoryDTOList\":[{\"id\":1001058,\"ownerType\":\"\",\"ownerId\":0,\"entryId\":1001058,\"parentId\":2,\"name\":\"活动管理二-默认子分类\",\"path\":\"/2/1001058\",\"defaultOrder\":0,\"status\":2,\"creatorUid\":1,\"createTime\":1507610899000,\"deleteUid\":0,\"namespaceId\":999983,\"enabled\":1,\"iconUri\":\"\",\"selectedIconUri\":\"\",\"allFlag\":1}]}', '2', '61', '{\"categoryId\":2,\"publishPrivilege\":1,\"livePrivilege\":0,\"listStyle\":2,\"scope\":3,\"style\":4,\"title\":\"旅游与团建\"}', '2018-02-25 09:30:11', 'community_control', '1', '2', NULL, '10602');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '一键推送', '11000', '', '2', NULL, '', '2018-01-19 15:14:24', 'community_control', '0', '', NULL, '11000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '任务管理', '13000', '', '2', '56', '', '2018-02-25 09:30:09', 'unlimit_control', '0', '', NULL, '13000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '投诉与需求', '20100', '{\"url\":\"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉与需求\"}', '2', '60', '{\"url\":\"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉与需求\"}', '2018-02-25 09:30:10', 'community_control', '1', '9', NULL, '20230');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '报修', '20100', '{\"url\":\"zl://propertyrepair/create?type=user&taskCategoryId=1&displayName=报修\"}', '2', '60', '{\"url\":\"zl://propertyrepair/create?type=user&taskCategoryId=1&displayName=报修\"}', '2018-02-25 09:30:10', 'community_control', '1', '1', NULL, NULL);
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '物业查费', '20400', '{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\"}', '2', '13', '{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\"}', '2018-02-25 09:30:10', 'community_control', '0', '', NULL, '20400');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '物业巡检', '20800', '{\"url\":\"https://core.zuolin.com/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}', '2', '13', '{\"url\":\"https://core.zuolin.com/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}', '2018-01-06 10:22:31', 'community_control', '0', '', NULL, '20800');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '企业客户管理', '21100', NULL, '2', '13', 'customer', '2018-01-10 18:20:20', 'community_control', '0', '', NULL, '21100');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '合同管理', '21200', NULL, '2', '13', 'contract', '2017-12-28 21:51:32', 'community_control', '0', '', NULL, '21200');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '项目信息', '30500', '', '2', NULL, '', '2018-01-19 15:14:53', 'community_control', '0', '', NULL, '30500');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '黑名单管理', '30600', '', '2', '34', '', '2018-01-19 15:15:13', 'community_control', '0', '', NULL, '30600');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '楼栋管理', '31000', '', '2', NULL, '', '2018-01-19 15:14:53', 'community_control', '0', '', NULL, '31000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '门牌管理', '32000', '', '2', NULL, '', '2018-01-19 15:14:53', 'community_control', '0', '', NULL, '32000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '企业管理', '33000', '', '2', '34', '', '2018-02-25 09:29:18', 'community_control', '0', '', NULL, '33000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '用户管理', '34000', '', '2', '34', '', '2018-01-19 15:15:13', 'community_control', '0', '', NULL, '34000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '用户认证', '35000', '', '2', '34', '', '2018-01-19 15:15:13', 'community_control', '0', '', NULL, '35000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '园区入驻', '40100', '', '2', '28', '', '2018-02-25 09:30:10', 'community_control', '0', '', NULL, '40100');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '服务热线', '40300', '', '2', '45', '', '2018-02-25 09:30:09', 'community_control', '0', '', NULL, '40300');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '资源预定', '40400', '{\"resourceTypeId\":12193,\"pageType\":0}', '2', '49', '{\"resourceTypeId\":12193,\"pageType\":0}', '2018-02-25 09:29:23', 'community_control', '1', '12193', NULL, '40400');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '电子屏预订', '40400', '{\"resourceTypeId\":10506, \"pageType\":0, \"payMode\":2}', '2', '49', '{\"resourceTypeId\":10506, \"pageType\":0, \"payMode\":2}', '2018-02-25 09:30:09', 'community_control', '1', '10506', NULL, '40400');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '会议室预订', '40400', '{\"resourceTypeId\":10505, \"pageType\":0, \"payMode\":1}', '2', '49', '{\"resourceTypeId\":10505, \"pageType\":0, \"payMode\":1}', '2018-02-25 09:30:09', 'community_control', '1', '10505', NULL, '40400');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '场地预约', '40400', '{\"resourceTypeId\":10507, \"pageType\":0, \"payMode\":1}', '2', '49', '{\"resourceTypeId\":10507, \"pageType\":0, \"payMode\":1}', '2018-02-25 09:30:10', 'community_control', '1', '10507', NULL, '40400');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '物品搬迁', '40500', '{\"type\":213090,\"displayType\":\"list\",\"detailFlag\":0}', '2', '33', '{\"type\":213090,\"parentId\":213090,\"displayType\":\"list\"}', '2018-02-25 09:28:23', 'community_control', '1', '213090', NULL, '41800');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '园区办事', '40500', '{\"type\":155,\"displayType\":\"list\",\"detailFlag\":0}', '2', '33', '{\"type\":155,\"parentId\":155,\"displayType\":\"list\"}', '2018-02-25 09:30:10', 'community_control', '1', '155', NULL, '42100');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '企业服务', '40500', '{\"type\":151,\"displayType\":\"grid\",\"detailFlag\":0}', '2', '33', '{\"type\":151,\"parentId\":151,\"displayType\":\"grid\"}', '2018-02-25 09:30:10', 'community_control', '1', '151', NULL, '41800');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '停车', '40800', '', '2', '30', '', '2018-02-25 09:30:09', 'community_control', '0', '', NULL, '40800');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '统计分析', '41300', '', '2', '34', '', '2018-01-19 15:15:13', 'community_control', '0', '', NULL, '41300');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '能耗管理', '49100', '{\"url\":\"https://core.zuolin.com/energy-management/build/index.html?hideNavigationBar\\u003d1#/address_choose#sign_suffix\"}', '2', '13', '{\"url\":\"https://core.zuolin.com/energy-management/build/index.html?hideNavigationBar\\u003d1#/address_choose#sign_suffix\"}', '2018-01-08 14:27:58', 'community_control', '0', '', NULL, '49100');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '组织架构', '50100', '', '2', '46', '', '2017-12-28 20:46:07', 'org_control', '0', '', NULL, '50100');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '审批管理', '50500', '', '2', '34', '', '2018-01-19 15:15:13', 'community_control', '0', '', NULL, '50500');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '表单管理', '50900', '', '2', '34', '', '2018-01-19 15:15:13', 'community_control', '0', '', NULL, '50900');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '审批管理', '52000', '', '2', '34', '', '2018-01-19 15:15:13', 'community_control', '0', '', NULL, '52000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '品质核查', '20600', '{\"realm\":\"quality\",\"entryUrl\":\"https://core.zuolin.com/nar/quality/index.html?hideNavigationBar=1#/select_community#sign_suffix\"}', '2', '44', '{\"realm\":\"quality\",\"entryUrl\":\"https://core.zuolin.com/nar/quality/index.html?hideNavigationBar=1#/select_community#sign_suffix\"}', '2018-01-08 14:28:49', 'community_control', '0', '', NULL, '20600');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '仓库管理', '21000', NULL, '2', '13', NULL, '2017-12-28 19:49:16', 'community-control', '0', '', '', '21000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '物业报修', '20100', '{\"url\":\"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修\"}', '2', '60', '{\"url\":\"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修\"}', '2018-01-08 14:29:02', 'community_control', '1', '6', NULL, '20100');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '文件下载中心', '61000', NULL, '2', NULL, NULL, '2018-03-01 17:12:44', 'unlimit_control', '0', '', '', '61000');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES (@appId := @appId + 1, @activeAppId := @activeAppId + 1, @namespaceId, '打卡考勤', '50600', '', '2', '23', '', '2018-01-08 14:27:59', 'org_control', '0', '', NULL, '50600');
    