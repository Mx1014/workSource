-- Elive by ljs
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1932,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999955','the relative path for ELive app agreements',999955,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1933,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for changfazhan',999955,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1934,'pmtask.handler-999955','flow','0',999955,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1935,'pay.platform','1','支付类型',999955,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (485,'Android_ELiveCore',NULL,UTC_TIMESTAMP(),999955);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (486,'iOS_ELiveCore',NULL,UTC_TIMESTAMP(),999955);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (826,485,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999955);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (827,486,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999955);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999955,'ELive');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1487,999955,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (915,'sms.default.sign',0,'zh_CN','世东elive','【世东elive】',999955);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18432,0,'北京','BEIJING','BJ','/北京',1,1,NULL,NULL,2,0,999955);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18433,18432,'北京市','BEIJINGSHI','BJS','/北京/北京市',2,2,NULL,'010',2,1,999955);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18434,18433,'朝阳区','ZHAOYANGQU','ZYQ','/北京/北京市/朝阳区',3,3,NULL,'010',2,0,999955);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060075,UUID(),18433,'北京市',18434,'朝阳区','ELive','','广渠南路18号','世东国际中心地处北京CBD商圈，东四环大郊亭桥东800米，地铁7号线百子湾站B口100米，项目总体量78450.89平米，由南塔、北塔两栋写字楼组成，楼内客户定位高端，项目品质优越，为大郊亭附近地标型建筑，项目扼守主城区连接行政副中心的中轴要道，百米建筑高度，国际*LEED，甲级5A写字楼智能标准。',0,1,2,UTC_TIMESTAMP(),1,192705,192706,UTC_TIMESTAMP(),999955);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092848,240111044332060075,'',116.507083,39.898648,'wx4fgpmf7z19');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20185,999955,'COMMUNITY',240111044332060075,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1040129,0,'PM','北京市北宇物业服务公司','北京市北宇物业服务公司是北京住总集团有限责任公司核心层企业，是北京市成立最早、实力雄厚的物业管理企业之一，也是全国第一批40家物业管理一级资质企业之一。
    北宇物业服务公司拥有国有资产13513万元，注册资金1680万元。目前管理房屋面积达160万平方米，管理电梯150部，锅炉84台，供暖总面积198.6万平方米。
    公司正式员工三百余人，有一百多名具有中高级技术职称的管理人员以及高级技术工人，36名部门主管取得了全国物业管理经理上岗证。公司所属的物业管理处和分公司分别座落在崇文区、朝阳区、海淀区、东城区、昌平区、通州区、密云等地等，有能力接受各项委托管理、房屋修缮、电梯安装和园林绿化设计、施工等任务。
    在企业发展的几年中，通过公司领导班子以及全体员工的共同努力，在为产权人保值增值、为使用人服务上不懈努力，社会效益、环境效益、经济效益同步提高，取得了丰硕的成果：恩济里小区、法华南里小区和卧龙花园小区先后被建设部授予“全国城市物业管理优秀住宅小区”的称号；恩济里小区、法华南里小区、松榆东里小区、卧龙花园小区、吉祥里小区和光熙门北里小区被授予“北京市优秀管理居住小区”称号。北宇公司通过了ISO9001：2000质量管理体系认证，是“中国物业管理公司100强”“首都文明单位”“北京市模范职工之家”“北京市守信企业”“北京市物业管理规范化服务活动先进集体”“京城物业生态好管家”等诸多荣誉称号的拥有者。
    公司以现代化的理念管理物业，建立了符合GB/T19001—2000标准的质量管理体系和计算机网络管理系统，优质、高效地向顾客提供满意的服务。为了适应顾客不断增长的物业智能化的需求，目前各个小区宽带网络、智能安防系统已陆续建成并投入使用。','/1040129',1,2,'ENTERPRISE',999955,1058236);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1155249,240111044332060075,'organization',1040129,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (25068,'EhOrganizations',1040129,1,10,467320,0,1,UTC_TIMESTAMP(),999955,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (25069,'EhOrganizations',1040129,1,10,467321,0,1,UTC_TIMESTAMP(),999955,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (25070,'EhOrganizations',1040129,1,10,467322,0,1,UTC_TIMESTAMP(),999955,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1058236,UUID(),'北京市北宇物业服务公司','北京市北宇物业服务公司',1,1,1040129,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192704,1,999955);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192704,UUID(),999955,2,'EhGroups',1058236,'北京市北宇物业服务公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192705,UUID(),999955,2,'',0,'ELive社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192706,UUID(),999955,2,'',0,'ELive意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256531,UUID(),999955,192705,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (467320,UUID(),'19220994180','BUCC-KC',NULL,1,45,'1','1','zh_CN','818862e2d08df30dc1375b7468debe25','a15a387770b44b732ef9f6b3ce83aa00793df135ca0ce78d1aeec6bd50c45360',UTC_TIMESTAMP(),999955);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (467321,UUID(),'19220994181','唐红',NULL,1,45,'1','2','zh_CN','06f37ba88f5a260c110f726c1319e67b','b6addf3ee9faff2b889507edfc4d1ad0b9f4c7c4c3135c29bd4621832a394eea',UTC_TIMESTAMP(),999955);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (467322,UUID(),'19220994182','左邻官方账号',NULL,1,45,'1','1','zh_CN','d33c8b7b3511d299e53730c9c104c3d8','72301b1b4c075814561d4e0f1469863901b90100257337e46c124d75ec23bd60',UTC_TIMESTAMP(),999955);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (29732,1040129,'USER',467320,'BUCC-KC',0,'13911310356',UTC_TIMESTAMP(),999955);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (29733,1040129,'USER',467321,'唐红',0,'15650723221',UTC_TIMESTAMP(),999955);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (29734,1040129,'USER',467322,'左邻官方账号',0,'12000001802',UTC_TIMESTAMP(),999955);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (438641,467320,0,'13911310356',NULL,3,UTC_TIMESTAMP(),999955);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (438642,467321,0,'15650723221',NULL,3,UTC_TIMESTAMP(),999955);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (438643,467322,0,'12000001802',NULL,3,UTC_TIMESTAMP(),999955);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2180315,1040129,'/1040129','USER',467320,'manager','BUCC-KC',0,'13911310356',3,999955,'ENTERPRISE',0,29732);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2180316,1040129,'/1040129','USER',467321,'manager','唐红',0,'15650723221',3,999955,'ENTERPRISE',0,29733);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2180317,1040129,'/1040129','USER',467322,'manager','左邻官方账号',0,'12000001802',3,999955,'ENTERPRISE',0,29734);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (29443,467320,1040129,'/1040129','ENTERPRISE',3,999955,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (29444,467321,1040129,'/1040129','ENTERPRISE',3,999955,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (29445,467322,1040129,'/1040129','ENTERPRISE',3,999955,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115173,'EhOrganizations',1040129,'EhUsers',467320,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115174,'EhOrganizations',1040129,'EhUsers',467321,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115175,'EhOrganizations',1040129,'EhUsers',467322,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970993,240111044332060075,'世东国际南塔','',0,01067766699,'广渠南路18号',NULL,'116.507083','39.898648','wx4fgpmf7z19','',NULL,2,0,NULL,1,NOW(),999955,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970994,240111044332060075,'世东国际北塔','',0,01067766699,'广渠南路18号',NULL,'116.509279','39.898273','wx4fgpr2qcpj','',NULL,2,0,NULL,1,NOW(),999955,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462465,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-409','世东国际北塔','409','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462497,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-613','世东国际北塔','613','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462529,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-903','世东国际北塔','903','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462561,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1107','世东国际北塔','1107','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462593,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1311','世东国际北塔','1311','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462625,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1601','世东国际北塔','1601','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462657,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1805','世东国际北塔','1805','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462689,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2009','世东国际北塔','2009','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462721,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-413','世东国际南塔','413','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462753,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-703','世东国际南塔','703','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462785,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-907','世东国际南塔','907','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462817,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1113','世东国际南塔','1113','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462849,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1403','世东国际南塔','1403','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462466,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-410','世东国际北塔','410','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462498,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-614','世东国际北塔','614','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462530,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-904','世东国际北塔','904','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462562,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1108','世东国际北塔','1108','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462594,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1312','世东国际北塔','1312','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462626,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1602','世东国际北塔','1602','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462658,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1806','世东国际北塔','1806','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462690,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2010','世东国际北塔','2010','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462722,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-414','世东国际南塔','414','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462754,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-704','世东国际南塔','704','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462786,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-908','世东国际南塔','908','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462818,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1114','世东国际南塔','1114','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462850,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1404','世东国际南塔','1404','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462467,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-411','世东国际北塔','411','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462499,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-701','世东国际北塔','701','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462531,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-905','世东国际北塔','905','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462563,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1109','世东国际北塔','1109','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462595,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1313','世东国际北塔','1313','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462627,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1603','世东国际北塔','1603','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462659,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1807','世东国际北塔','1807','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462691,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2011','世东国际北塔','2011','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462723,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-501','世东国际南塔','501','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462755,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-705','世东国际南塔','705','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462787,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-909','世东国际南塔','909','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462819,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1201','世东国际南塔','1201','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462851,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1405','世东国际南塔','1405','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462468,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-412','世东国际北塔','412','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462500,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-702','世东国际北塔','702','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462532,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-906','世东国际北塔','906','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462564,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1110','世东国际北塔','1110','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462596,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1314','世东国际北塔','1314','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462628,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1604','世东国际北塔','1604','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462660,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1808','世东国际北塔','1808','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462692,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2012','世东国际北塔','2012','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462724,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-502','世东国际南塔','502','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462756,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-706','世东国际南塔','706','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462788,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-910','世东国际南塔','910','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462820,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1202','世东国际南塔','1202','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462852,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1406','世东国际南塔','1406','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462469,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-413','世东国际北塔','413','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462501,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-703','世东国际北塔','703','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462533,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-907','世东国际北塔','907','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462565,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1111','世东国际北塔','1111','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462597,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1401','世东国际北塔','1401','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462629,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1605','世东国际北塔','1605','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462661,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1809','世东国际北塔','1809','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462693,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2013','世东国际北塔','2013','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462725,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-503','世东国际南塔','503','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462757,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-707','世东国际南塔','707','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462789,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-911','世东国际南塔','911','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462821,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1203','世东国际南塔','1203','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462853,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1407','世东国际南塔','1407','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462470,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-414','世东国际北塔','414','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462502,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-704','世东国际北塔','704','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462534,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-908','世东国际北塔','908','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462566,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1112','世东国际北塔','1112','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462598,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1402','世东国际北塔','1402','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462630,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1606','世东国际北塔','1606','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462662,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1810','世东国际北塔','1810','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462694,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2014','世东国际北塔','2014','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462726,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-504','世东国际南塔','504','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462758,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-708','世东国际南塔','708','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462790,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-912','世东国际南塔','912','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462822,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1204','世东国际南塔','1204','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462854,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1408','世东国际南塔','1408','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462471,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-501','世东国际北塔','501','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462503,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-705','世东国际北塔','705','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462535,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-909','世东国际北塔','909','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462567,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1113','世东国际北塔','1113','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462599,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1403','世东国际北塔','1403','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462631,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1607','世东国际北塔','1607','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462663,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1811','世东国际北塔','1811','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462695,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-301','世东国际南塔','301','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462727,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-505','世东国际南塔','505','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462759,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-709','世东国际南塔','709','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462791,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1001','世东国际南塔','1001','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462823,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1205','世东国际南塔','1205','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462855,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1409','世东国际南塔','1409','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462472,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-502','世东国际北塔','502','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462504,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-706','世东国际北塔','706','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462536,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-910','世东国际北塔','910','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462568,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1114','世东国际北塔','1114','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462600,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1404','世东国际北塔','1404','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462632,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1608','世东国际北塔','1608','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462664,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1812','世东国际北塔','1812','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462696,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-302','世东国际南塔','302','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462728,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-506','世东国际南塔','506','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462760,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-710','世东国际南塔','710','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462792,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1002','世东国际南塔','1002','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462824,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1206','世东国际南塔','1206','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462856,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1410','世东国际南塔','1410','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462473,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-503','世东国际北塔','503','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462505,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-707','世东国际北塔','707','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462537,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-911','世东国际北塔','911','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462569,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1201','世东国际北塔','1201','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462601,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1405','世东国际北塔','1405','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462633,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1609','世东国际北塔','1609','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462665,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1813','世东国际北塔','1813','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462697,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-303','世东国际南塔','303','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462729,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-507','世东国际南塔','507','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462761,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-711','世东国际南塔','711','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462793,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1003','世东国际南塔','1003','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462825,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1207','世东国际南塔','1207','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462857,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1411','世东国际南塔','1411','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462474,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-504','世东国际北塔','504','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462506,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-708','世东国际北塔','708','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462538,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-912','世东国际北塔','912','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462570,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1202','世东国际北塔','1202','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462602,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1406','世东国际北塔','1406','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462634,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1610','世东国际北塔','1610','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462666,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1814','世东国际北塔','1814','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462698,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-304','世东国际南塔','304','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462730,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-508','世东国际南塔','508','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462762,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-712','世东国际南塔','712','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462794,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1004','世东国际南塔','1004','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462826,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1208','世东国际南塔','1208','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462858,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1412','世东国际南塔','1412','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462443,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-301','世东国际北塔','301','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462475,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-505','世东国际北塔','505','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462507,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-709','世东国际北塔','709','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462539,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-913','世东国际北塔','913','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462571,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1203','世东国际北塔','1203','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462603,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1407','世东国际北塔','1407','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462635,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1611','世东国际北塔','1611','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462667,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1901','世东国际北塔','1901','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462699,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-305','世东国际南塔','305','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462731,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-509','世东国际南塔','509','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462763,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-713','世东国际南塔','713','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462795,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1005','世东国际南塔','1005','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462827,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1209','世东国际南塔','1209','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462859,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1413','世东国际南塔','1413','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462444,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-302','世东国际北塔','302','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462476,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-506','世东国际北塔','506','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462508,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-710','世东国际北塔','710','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462540,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-914','世东国际北塔','914','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462572,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1204','世东国际北塔','1204','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462604,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1408','世东国际北塔','1408','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462636,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1612','世东国际北塔','1612','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462668,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1902','世东国际北塔','1902','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462700,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-306','世东国际南塔','306','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462732,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-510','世东国际南塔','510','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462764,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-714','世东国际南塔','714','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462796,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1006','世东国际南塔','1006','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462828,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1210','世东国际南塔','1210','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462860,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1414','世东国际南塔','1414','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462445,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-303','世东国际北塔','303','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462477,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-507','世东国际北塔','507','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462509,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-711','世东国际北塔','711','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462541,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1001','世东国际北塔','1001','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462573,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1205','世东国际北塔','1205','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462605,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1409','世东国际北塔','1409','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462637,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1613','世东国际北塔','1613','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462669,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1903','世东国际北塔','1903','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462701,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-307','世东国际南塔','307','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462733,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-511','世东国际南塔','511','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462765,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-801','世东国际南塔','801','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462797,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1007','世东国际南塔','1007','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462829,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1211','世东国际南塔','1211','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462861,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1501','世东国际南塔','1501','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462446,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-304','世东国际北塔','304','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462478,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-508','世东国际北塔','508','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462510,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-712','世东国际北塔','712','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462542,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1002','世东国际北塔','1002','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462574,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1206','世东国际北塔','1206','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462606,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1410','世东国际北塔','1410','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462638,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1614','世东国际北塔','1614','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462670,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1904','世东国际北塔','1904','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462702,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-308','世东国际南塔','308','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462734,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-512','世东国际南塔','512','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462766,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-802','世东国际南塔','802','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462798,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1008','世东国际南塔','1008','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462830,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1212','世东国际南塔','1212','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462862,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1502','世东国际南塔','1502','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462447,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-305','世东国际北塔','305','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462479,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-509','世东国际北塔','509','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462511,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-713','世东国际北塔','713','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462543,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1003','世东国际北塔','1003','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462575,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1207','世东国际北塔','1207','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462607,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1411','世东国际北塔','1411','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462639,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1701','世东国际北塔','1701','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462671,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1905','世东国际北塔','1905','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462703,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-309','世东国际南塔','309','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462735,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-513','世东国际南塔','513','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462767,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-803','世东国际南塔','803','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462799,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1009','世东国际南塔','1009','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462831,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1213','世东国际南塔','1213','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462863,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1503','世东国际南塔','1503','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462448,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-306','世东国际北塔','306','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462480,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-510','世东国际北塔','510','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462512,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-714','世东国际北塔','714','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462544,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1004','世东国际北塔','1004','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462576,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1208','世东国际北塔','1208','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462608,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1412','世东国际北塔','1412','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462640,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1702','世东国际北塔','1702','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462672,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1906','世东国际北塔','1906','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462704,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-310','世东国际南塔','310','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462736,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-514','世东国际南塔','514','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462768,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-804','世东国际南塔','804','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462800,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1010','世东国际南塔','1010','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462832,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1214','世东国际南塔','1214','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462864,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1504','世东国际南塔','1504','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462449,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-307','世东国际北塔','307','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462481,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-511','世东国际北塔','511','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462513,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-801','世东国际北塔','801','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462545,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1005','世东国际北塔','1005','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462577,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1209','世东国际北塔','1209','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462609,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1413','世东国际北塔','1413','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462641,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1703','世东国际北塔','1703','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462673,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1907','世东国际北塔','1907','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462705,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-311','世东国际南塔','311','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462737,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-601','世东国际南塔','601','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462769,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-805','世东国际南塔','805','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462801,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1011','世东国际南塔','1011','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462833,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1301','世东国际南塔','1301','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462865,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1505','世东国际南塔','1505','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462450,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-308','世东国际北塔','308','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462482,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-512','世东国际北塔','512','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462514,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-802','世东国际北塔','802','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462546,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1006','世东国际北塔','1006','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462578,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1210','世东国际北塔','1210','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462610,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1414','世东国际北塔','1414','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462642,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1704','世东国际北塔','1704','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462674,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1908','世东国际北塔','1908','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462706,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-312','世东国际南塔','312','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462738,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-602','世东国际南塔','602','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462770,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-806','世东国际南塔','806','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462802,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1012','世东国际南塔','1012','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462834,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1302','世东国际南塔','1302','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462866,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1506','世东国际南塔','1506','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462451,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-309','世东国际北塔','309','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462483,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-513','世东国际北塔','513','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462515,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-803','世东国际北塔','803','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462547,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1007','世东国际北塔','1007','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462579,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1211','世东国际北塔','1211','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462611,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1501','世东国际北塔','1501','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462643,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1705','世东国际北塔','1705','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462675,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1909','世东国际北塔','1909','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462707,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-313','世东国际南塔','313','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462739,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-603','世东国际南塔','603','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462771,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-807','世东国际南塔','807','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462803,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1013','世东国际南塔','1013','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462835,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1303','世东国际南塔','1303','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462867,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1507','世东国际南塔','1507','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462452,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-310','世东国际北塔','310','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462484,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-514','世东国际北塔','514','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462516,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-804','世东国际北塔','804','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462548,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1008','世东国际北塔','1008','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462580,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1212','世东国际北塔','1212','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462612,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1502','世东国际北塔','1502','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462644,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1706','世东国际北塔','1706','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462676,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1910','世东国际北塔','1910','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462708,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-314','世东国际南塔','314','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462740,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-604','世东国际南塔','604','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462772,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-808','世东国际南塔','808','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462804,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1014','世东国际南塔','1014','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462836,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1304','世东国际南塔','1304','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462868,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1508','世东国际南塔','1508','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462453,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-311','世东国际北塔','311','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462485,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-601','世东国际北塔','601','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462517,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-805','世东国际北塔','805','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462549,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1009','世东国际北塔','1009','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462581,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1213','世东国际北塔','1213','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462613,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1503','世东国际北塔','1503','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462645,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1707','世东国际北塔','1707','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462677,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1911','世东国际北塔','1911','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462709,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-401','世东国际南塔','401','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462741,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-605','世东国际南塔','605','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462773,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-809','世东国际南塔','809','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462805,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1101','世东国际南塔','1101','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462837,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1305','世东国际南塔','1305','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462869,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1509','世东国际南塔','1509','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462454,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-312','世东国际北塔','312','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462486,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-602','世东国际北塔','602','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462518,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-806','世东国际北塔','806','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462550,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1010','世东国际北塔','1010','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462582,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1214','世东国际北塔','1214','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462614,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1504','世东国际北塔','1504','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462646,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1708','世东国际北塔','1708','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462678,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1912','世东国际北塔','1912','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462710,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-402','世东国际南塔','402','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462742,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-606','世东国际南塔','606','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462774,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-810','世东国际南塔','810','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462806,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1102','世东国际南塔','1102','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462838,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1306','世东国际南塔','1306','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462870,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1510','世东国际南塔','1510','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462455,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-313','世东国际北塔','313','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462487,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-603','世东国际北塔','603','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462519,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-807','世东国际北塔','807','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462551,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1011','世东国际北塔','1011','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462583,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1301','世东国际北塔','1301','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462615,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1505','世东国际北塔','1505','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462647,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1709','世东国际北塔','1709','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462679,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1913','世东国际北塔','1913','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462711,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-403','世东国际南塔','403','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462743,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-607','世东国际南塔','607','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462775,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-811','世东国际南塔','811','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462807,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1103','世东国际南塔','1103','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462839,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1307','世东国际南塔','1307','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462871,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1511','世东国际南塔','1511','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462456,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-314','世东国际北塔','314','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462488,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-604','世东国际北塔','604','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462520,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-808','世东国际北塔','808','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462552,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1012','世东国际北塔','1012','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462584,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1302','世东国际北塔','1302','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462616,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1506','世东国际北塔','1506','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462648,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1710','世东国际北塔','1710','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462680,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1914','世东国际北塔','1914','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462712,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-404','世东国际南塔','404','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462744,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-608','世东国际南塔','608','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462776,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-812','世东国际南塔','812','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462808,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1104','世东国际南塔','1104','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462840,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1308','世东国际南塔','1308','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462872,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1512','世东国际南塔','1512','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462457,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-401','世东国际北塔','401','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462489,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-605','世东国际北塔','605','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462521,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-809','世东国际北塔','809','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462553,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1013','世东国际北塔','1013','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462585,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1303','世东国际北塔','1303','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462617,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1507','世东国际北塔','1507','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462649,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1711','世东国际北塔','1711','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462681,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2001','世东国际北塔','2001','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462713,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-405','世东国际南塔','405','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462745,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-609','世东国际南塔','609','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462777,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-813','世东国际南塔','813','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462809,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1105','世东国际南塔','1105','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462841,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1309','世东国际南塔','1309','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462873,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1513','世东国际南塔','1513','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462458,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-402','世东国际北塔','402','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462490,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-606','世东国际北塔','606','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462522,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-810','世东国际北塔','810','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462554,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1014','世东国际北塔','1014','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462586,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1304','世东国际北塔','1304','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462618,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1508','世东国际北塔','1508','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462650,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1712','世东国际北塔','1712','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462682,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2002','世东国际北塔','2002','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462714,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-406','世东国际南塔','406','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462746,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-610','世东国际南塔','610','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462778,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-814','世东国际南塔','814','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462810,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1106','世东国际南塔','1106','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462842,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1310','世东国际南塔','1310','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462874,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1514','世东国际南塔','1514','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462459,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-403','世东国际北塔','403','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462491,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-607','世东国际北塔','607','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462523,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-811','世东国际北塔','811','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462555,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1101','世东国际北塔','1101','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462587,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1305','世东国际北塔','1305','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462619,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1509','世东国际北塔','1509','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462651,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1713','世东国际北塔','1713','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462683,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2003','世东国际北塔','2003','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462715,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-407','世东国际南塔','407','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462747,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-611','世东国际南塔','611','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462779,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-901','世东国际南塔','901','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462811,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1107','世东国际南塔','1107','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462843,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1311','世东国际南塔','1311','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462460,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-404','世东国际北塔','404','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462492,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-608','世东国际北塔','608','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462524,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-812','世东国际北塔','812','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462556,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1102','世东国际北塔','1102','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462588,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1306','世东国际北塔','1306','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462620,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1510','世东国际北塔','1510','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462652,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1714','世东国际北塔','1714','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462684,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2004','世东国际北塔','2004','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462716,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-408','世东国际南塔','408','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462748,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-612','世东国际南塔','612','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462780,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-902','世东国际南塔','902','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462812,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1108','世东国际南塔','1108','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462844,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1312','世东国际南塔','1312','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462461,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-405','世东国际北塔','405','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462493,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-609','世东国际北塔','609','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462525,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-813','世东国际北塔','813','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462557,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1103','世东国际北塔','1103','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462589,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1307','世东国际北塔','1307','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462621,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1511','世东国际北塔','1511','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462653,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1801','世东国际北塔','1801','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462685,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2005','世东国际北塔','2005','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462717,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-409','世东国际南塔','409','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462749,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-613','世东国际南塔','613','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462781,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-903','世东国际南塔','903','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462813,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1109','世东国际南塔','1109','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462845,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1313','世东国际南塔','1313','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462462,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-406','世东国际北塔','406','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462494,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-610','世东国际北塔','610','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462526,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-814','世东国际北塔','814','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462558,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1104','世东国际北塔','1104','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462590,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1308','世东国际北塔','1308','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462622,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1512','世东国际北塔','1512','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462654,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1802','世东国际北塔','1802','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462686,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2006','世东国际北塔','2006','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462718,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-410','世东国际南塔','410','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462750,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-614','世东国际南塔','614','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462782,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-904','世东国际南塔','904','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462814,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1110','世东国际南塔','1110','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462846,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1314','世东国际南塔','1314','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462463,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-407','世东国际北塔','407','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462495,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-611','世东国际北塔','611','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462527,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-901','世东国际北塔','901','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462559,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1105','世东国际北塔','1105','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462591,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1309','世东国际北塔','1309','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462623,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1513','世东国际北塔','1513','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462655,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1803','世东国际北塔','1803','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462687,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2007','世东国际北塔','2007','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462719,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-411','世东国际南塔','411','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462751,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-701','世东国际南塔','701','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462783,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-905','世东国际南塔','905','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462815,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1111','世东国际南塔','1111','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462847,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1401','世东国际南塔','1401','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462464,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-408','世东国际北塔','408','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462496,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-612','世东国际北塔','612','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462528,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-902','世东国际北塔','902','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462560,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1106','世东国际北塔','1106','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462592,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1310','世东国际北塔','1310','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462624,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1514','世东国际北塔','1514','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462656,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-1804','世东国际北塔','1804','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462688,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际北塔-2008','世东国际北塔','2008','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462720,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-412','世东国际南塔','412','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462752,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-702','世东国际南塔','702','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462784,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-906','世东国际南塔','906','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462816,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1112','世东国际南塔','1112','2','0',UTC_TIMESTAMP(),999955,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462848,UUID(),240111044332060075,18433,'北京市',18434,'朝阳区','世东国际南塔-1402','世东国际南塔','1402','2','0',UTC_TIMESTAMP(),999955,NULL);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85544,1040129,240111044332060075,239825274387462465,'世东国际北塔-409',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85576,1040129,240111044332060075,239825274387462497,'世东国际北塔-613',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85608,1040129,240111044332060075,239825274387462529,'世东国际北塔-903',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85640,1040129,240111044332060075,239825274387462561,'世东国际北塔-1107',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85672,1040129,240111044332060075,239825274387462593,'世东国际北塔-1311',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85704,1040129,240111044332060075,239825274387462625,'世东国际北塔-1601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85736,1040129,240111044332060075,239825274387462657,'世东国际北塔-1805',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85768,1040129,240111044332060075,239825274387462689,'世东国际北塔-2009',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85800,1040129,240111044332060075,239825274387462721,'世东国际南塔-413',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85832,1040129,240111044332060075,239825274387462753,'世东国际南塔-703',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85864,1040129,240111044332060075,239825274387462785,'世东国际南塔-907',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85896,1040129,240111044332060075,239825274387462817,'世东国际南塔-1113',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85928,1040129,240111044332060075,239825274387462849,'世东国际南塔-1403',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85545,1040129,240111044332060075,239825274387462466,'世东国际北塔-410',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85577,1040129,240111044332060075,239825274387462498,'世东国际北塔-614',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85609,1040129,240111044332060075,239825274387462530,'世东国际北塔-904',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85641,1040129,240111044332060075,239825274387462562,'世东国际北塔-1108',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85673,1040129,240111044332060075,239825274387462594,'世东国际北塔-1312',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85705,1040129,240111044332060075,239825274387462626,'世东国际北塔-1602',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85737,1040129,240111044332060075,239825274387462658,'世东国际北塔-1806',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85769,1040129,240111044332060075,239825274387462690,'世东国际北塔-2010',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85801,1040129,240111044332060075,239825274387462722,'世东国际南塔-414',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85833,1040129,240111044332060075,239825274387462754,'世东国际南塔-704',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85865,1040129,240111044332060075,239825274387462786,'世东国际南塔-908',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85897,1040129,240111044332060075,239825274387462818,'世东国际南塔-1114',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85929,1040129,240111044332060075,239825274387462850,'世东国际南塔-1404',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85546,1040129,240111044332060075,239825274387462467,'世东国际北塔-411',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85578,1040129,240111044332060075,239825274387462499,'世东国际北塔-701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85610,1040129,240111044332060075,239825274387462531,'世东国际北塔-905',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85642,1040129,240111044332060075,239825274387462563,'世东国际北塔-1109',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85674,1040129,240111044332060075,239825274387462595,'世东国际北塔-1313',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85706,1040129,240111044332060075,239825274387462627,'世东国际北塔-1603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85738,1040129,240111044332060075,239825274387462659,'世东国际北塔-1807',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85770,1040129,240111044332060075,239825274387462691,'世东国际北塔-2011',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85802,1040129,240111044332060075,239825274387462723,'世东国际南塔-501',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85834,1040129,240111044332060075,239825274387462755,'世东国际南塔-705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85866,1040129,240111044332060075,239825274387462787,'世东国际南塔-909',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85898,1040129,240111044332060075,239825274387462819,'世东国际南塔-1201',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85930,1040129,240111044332060075,239825274387462851,'世东国际南塔-1405',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85547,1040129,240111044332060075,239825274387462468,'世东国际北塔-412',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85579,1040129,240111044332060075,239825274387462500,'世东国际北塔-702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85611,1040129,240111044332060075,239825274387462532,'世东国际北塔-906',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85643,1040129,240111044332060075,239825274387462564,'世东国际北塔-1110',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85675,1040129,240111044332060075,239825274387462596,'世东国际北塔-1314',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85707,1040129,240111044332060075,239825274387462628,'世东国际北塔-1604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85739,1040129,240111044332060075,239825274387462660,'世东国际北塔-1808',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85771,1040129,240111044332060075,239825274387462692,'世东国际北塔-2012',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85803,1040129,240111044332060075,239825274387462724,'世东国际南塔-502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85835,1040129,240111044332060075,239825274387462756,'世东国际南塔-706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85867,1040129,240111044332060075,239825274387462788,'世东国际南塔-910',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85899,1040129,240111044332060075,239825274387462820,'世东国际南塔-1202',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85931,1040129,240111044332060075,239825274387462852,'世东国际南塔-1406',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85548,1040129,240111044332060075,239825274387462469,'世东国际北塔-413',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85580,1040129,240111044332060075,239825274387462501,'世东国际北塔-703',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85612,1040129,240111044332060075,239825274387462533,'世东国际北塔-907',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85644,1040129,240111044332060075,239825274387462565,'世东国际北塔-1111',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85676,1040129,240111044332060075,239825274387462597,'世东国际北塔-1401',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85708,1040129,240111044332060075,239825274387462629,'世东国际北塔-1605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85740,1040129,240111044332060075,239825274387462661,'世东国际北塔-1809',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85772,1040129,240111044332060075,239825274387462693,'世东国际北塔-2013',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85804,1040129,240111044332060075,239825274387462725,'世东国际南塔-503',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85836,1040129,240111044332060075,239825274387462757,'世东国际南塔-707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85868,1040129,240111044332060075,239825274387462789,'世东国际南塔-911',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85900,1040129,240111044332060075,239825274387462821,'世东国际南塔-1203',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85932,1040129,240111044332060075,239825274387462853,'世东国际南塔-1407',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85549,1040129,240111044332060075,239825274387462470,'世东国际北塔-414',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85581,1040129,240111044332060075,239825274387462502,'世东国际北塔-704',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85613,1040129,240111044332060075,239825274387462534,'世东国际北塔-908',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85645,1040129,240111044332060075,239825274387462566,'世东国际北塔-1112',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85677,1040129,240111044332060075,239825274387462598,'世东国际北塔-1402',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85709,1040129,240111044332060075,239825274387462630,'世东国际北塔-1606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85741,1040129,240111044332060075,239825274387462662,'世东国际北塔-1810',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85773,1040129,240111044332060075,239825274387462694,'世东国际北塔-2014',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85805,1040129,240111044332060075,239825274387462726,'世东国际南塔-504',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85837,1040129,240111044332060075,239825274387462758,'世东国际南塔-708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85869,1040129,240111044332060075,239825274387462790,'世东国际南塔-912',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85901,1040129,240111044332060075,239825274387462822,'世东国际南塔-1204',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85933,1040129,240111044332060075,239825274387462854,'世东国际南塔-1408',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85550,1040129,240111044332060075,239825274387462471,'世东国际北塔-501',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85582,1040129,240111044332060075,239825274387462503,'世东国际北塔-705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85614,1040129,240111044332060075,239825274387462535,'世东国际北塔-909',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85646,1040129,240111044332060075,239825274387462567,'世东国际北塔-1113',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85678,1040129,240111044332060075,239825274387462599,'世东国际北塔-1403',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85710,1040129,240111044332060075,239825274387462631,'世东国际北塔-1607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85742,1040129,240111044332060075,239825274387462663,'世东国际北塔-1811',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85774,1040129,240111044332060075,239825274387462695,'世东国际南塔-301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85806,1040129,240111044332060075,239825274387462727,'世东国际南塔-505',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85838,1040129,240111044332060075,239825274387462759,'世东国际南塔-709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85870,1040129,240111044332060075,239825274387462791,'世东国际南塔-1001',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85902,1040129,240111044332060075,239825274387462823,'世东国际南塔-1205',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85934,1040129,240111044332060075,239825274387462855,'世东国际南塔-1409',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85551,1040129,240111044332060075,239825274387462472,'世东国际北塔-502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85583,1040129,240111044332060075,239825274387462504,'世东国际北塔-706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85615,1040129,240111044332060075,239825274387462536,'世东国际北塔-910',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85647,1040129,240111044332060075,239825274387462568,'世东国际北塔-1114',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85679,1040129,240111044332060075,239825274387462600,'世东国际北塔-1404',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85711,1040129,240111044332060075,239825274387462632,'世东国际北塔-1608',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85743,1040129,240111044332060075,239825274387462664,'世东国际北塔-1812',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85775,1040129,240111044332060075,239825274387462696,'世东国际南塔-302',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85807,1040129,240111044332060075,239825274387462728,'世东国际南塔-506',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85839,1040129,240111044332060075,239825274387462760,'世东国际南塔-710',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85871,1040129,240111044332060075,239825274387462792,'世东国际南塔-1002',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85903,1040129,240111044332060075,239825274387462824,'世东国际南塔-1206',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85935,1040129,240111044332060075,239825274387462856,'世东国际南塔-1410',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85552,1040129,240111044332060075,239825274387462473,'世东国际北塔-503',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85584,1040129,240111044332060075,239825274387462505,'世东国际北塔-707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85616,1040129,240111044332060075,239825274387462537,'世东国际北塔-911',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85648,1040129,240111044332060075,239825274387462569,'世东国际北塔-1201',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85680,1040129,240111044332060075,239825274387462601,'世东国际北塔-1405',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85712,1040129,240111044332060075,239825274387462633,'世东国际北塔-1609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85744,1040129,240111044332060075,239825274387462665,'世东国际北塔-1813',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85776,1040129,240111044332060075,239825274387462697,'世东国际南塔-303',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85808,1040129,240111044332060075,239825274387462729,'世东国际南塔-507',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85840,1040129,240111044332060075,239825274387462761,'世东国际南塔-711',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85872,1040129,240111044332060075,239825274387462793,'世东国际南塔-1003',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85904,1040129,240111044332060075,239825274387462825,'世东国际南塔-1207',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85936,1040129,240111044332060075,239825274387462857,'世东国际南塔-1411',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85553,1040129,240111044332060075,239825274387462474,'世东国际北塔-504',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85585,1040129,240111044332060075,239825274387462506,'世东国际北塔-708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85617,1040129,240111044332060075,239825274387462538,'世东国际北塔-912',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85649,1040129,240111044332060075,239825274387462570,'世东国际北塔-1202',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85681,1040129,240111044332060075,239825274387462602,'世东国际北塔-1406',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85713,1040129,240111044332060075,239825274387462634,'世东国际北塔-1610',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85745,1040129,240111044332060075,239825274387462666,'世东国际北塔-1814',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85777,1040129,240111044332060075,239825274387462698,'世东国际南塔-304',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85809,1040129,240111044332060075,239825274387462730,'世东国际南塔-508',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85841,1040129,240111044332060075,239825274387462762,'世东国际南塔-712',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85873,1040129,240111044332060075,239825274387462794,'世东国际南塔-1004',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85905,1040129,240111044332060075,239825274387462826,'世东国际南塔-1208',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85937,1040129,240111044332060075,239825274387462858,'世东国际南塔-1412',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85522,1040129,240111044332060075,239825274387462443,'世东国际北塔-301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85554,1040129,240111044332060075,239825274387462475,'世东国际北塔-505',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85586,1040129,240111044332060075,239825274387462507,'世东国际北塔-709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85618,1040129,240111044332060075,239825274387462539,'世东国际北塔-913',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85650,1040129,240111044332060075,239825274387462571,'世东国际北塔-1203',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85682,1040129,240111044332060075,239825274387462603,'世东国际北塔-1407',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85714,1040129,240111044332060075,239825274387462635,'世东国际北塔-1611',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85746,1040129,240111044332060075,239825274387462667,'世东国际北塔-1901',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85778,1040129,240111044332060075,239825274387462699,'世东国际南塔-305',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85810,1040129,240111044332060075,239825274387462731,'世东国际南塔-509',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85842,1040129,240111044332060075,239825274387462763,'世东国际南塔-713',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85874,1040129,240111044332060075,239825274387462795,'世东国际南塔-1005',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85906,1040129,240111044332060075,239825274387462827,'世东国际南塔-1209',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85938,1040129,240111044332060075,239825274387462859,'世东国际南塔-1413',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85523,1040129,240111044332060075,239825274387462444,'世东国际北塔-302',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85555,1040129,240111044332060075,239825274387462476,'世东国际北塔-506',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85587,1040129,240111044332060075,239825274387462508,'世东国际北塔-710',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85619,1040129,240111044332060075,239825274387462540,'世东国际北塔-914',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85651,1040129,240111044332060075,239825274387462572,'世东国际北塔-1204',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85683,1040129,240111044332060075,239825274387462604,'世东国际北塔-1408',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85715,1040129,240111044332060075,239825274387462636,'世东国际北塔-1612',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85747,1040129,240111044332060075,239825274387462668,'世东国际北塔-1902',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85779,1040129,240111044332060075,239825274387462700,'世东国际南塔-306',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85811,1040129,240111044332060075,239825274387462732,'世东国际南塔-510',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85843,1040129,240111044332060075,239825274387462764,'世东国际南塔-714',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85875,1040129,240111044332060075,239825274387462796,'世东国际南塔-1006',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85907,1040129,240111044332060075,239825274387462828,'世东国际南塔-1210',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85939,1040129,240111044332060075,239825274387462860,'世东国际南塔-1414',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85524,1040129,240111044332060075,239825274387462445,'世东国际北塔-303',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85556,1040129,240111044332060075,239825274387462477,'世东国际北塔-507',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85588,1040129,240111044332060075,239825274387462509,'世东国际北塔-711',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85620,1040129,240111044332060075,239825274387462541,'世东国际北塔-1001',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85652,1040129,240111044332060075,239825274387462573,'世东国际北塔-1205',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85684,1040129,240111044332060075,239825274387462605,'世东国际北塔-1409',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85716,1040129,240111044332060075,239825274387462637,'世东国际北塔-1613',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85748,1040129,240111044332060075,239825274387462669,'世东国际北塔-1903',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85780,1040129,240111044332060075,239825274387462701,'世东国际南塔-307',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85812,1040129,240111044332060075,239825274387462733,'世东国际南塔-511',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85844,1040129,240111044332060075,239825274387462765,'世东国际南塔-801',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85876,1040129,240111044332060075,239825274387462797,'世东国际南塔-1007',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85908,1040129,240111044332060075,239825274387462829,'世东国际南塔-1211',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85940,1040129,240111044332060075,239825274387462861,'世东国际南塔-1501',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85525,1040129,240111044332060075,239825274387462446,'世东国际北塔-304',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85557,1040129,240111044332060075,239825274387462478,'世东国际北塔-508',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85589,1040129,240111044332060075,239825274387462510,'世东国际北塔-712',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85621,1040129,240111044332060075,239825274387462542,'世东国际北塔-1002',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85653,1040129,240111044332060075,239825274387462574,'世东国际北塔-1206',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85685,1040129,240111044332060075,239825274387462606,'世东国际北塔-1410',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85717,1040129,240111044332060075,239825274387462638,'世东国际北塔-1614',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85749,1040129,240111044332060075,239825274387462670,'世东国际北塔-1904',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85781,1040129,240111044332060075,239825274387462702,'世东国际南塔-308',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85813,1040129,240111044332060075,239825274387462734,'世东国际南塔-512',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85845,1040129,240111044332060075,239825274387462766,'世东国际南塔-802',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85877,1040129,240111044332060075,239825274387462798,'世东国际南塔-1008',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85909,1040129,240111044332060075,239825274387462830,'世东国际南塔-1212',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85941,1040129,240111044332060075,239825274387462862,'世东国际南塔-1502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85526,1040129,240111044332060075,239825274387462447,'世东国际北塔-305',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85558,1040129,240111044332060075,239825274387462479,'世东国际北塔-509',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85590,1040129,240111044332060075,239825274387462511,'世东国际北塔-713',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85622,1040129,240111044332060075,239825274387462543,'世东国际北塔-1003',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85654,1040129,240111044332060075,239825274387462575,'世东国际北塔-1207',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85686,1040129,240111044332060075,239825274387462607,'世东国际北塔-1411',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85718,1040129,240111044332060075,239825274387462639,'世东国际北塔-1701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85750,1040129,240111044332060075,239825274387462671,'世东国际北塔-1905',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85782,1040129,240111044332060075,239825274387462703,'世东国际南塔-309',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85814,1040129,240111044332060075,239825274387462735,'世东国际南塔-513',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85846,1040129,240111044332060075,239825274387462767,'世东国际南塔-803',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85878,1040129,240111044332060075,239825274387462799,'世东国际南塔-1009',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85910,1040129,240111044332060075,239825274387462831,'世东国际南塔-1213',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85942,1040129,240111044332060075,239825274387462863,'世东国际南塔-1503',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85527,1040129,240111044332060075,239825274387462448,'世东国际北塔-306',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85559,1040129,240111044332060075,239825274387462480,'世东国际北塔-510',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85591,1040129,240111044332060075,239825274387462512,'世东国际北塔-714',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85623,1040129,240111044332060075,239825274387462544,'世东国际北塔-1004',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85655,1040129,240111044332060075,239825274387462576,'世东国际北塔-1208',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85687,1040129,240111044332060075,239825274387462608,'世东国际北塔-1412',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85719,1040129,240111044332060075,239825274387462640,'世东国际北塔-1702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85751,1040129,240111044332060075,239825274387462672,'世东国际北塔-1906',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85783,1040129,240111044332060075,239825274387462704,'世东国际南塔-310',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85815,1040129,240111044332060075,239825274387462736,'世东国际南塔-514',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85847,1040129,240111044332060075,239825274387462768,'世东国际南塔-804',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85879,1040129,240111044332060075,239825274387462800,'世东国际南塔-1010',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85911,1040129,240111044332060075,239825274387462832,'世东国际南塔-1214',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85943,1040129,240111044332060075,239825274387462864,'世东国际南塔-1504',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85528,1040129,240111044332060075,239825274387462449,'世东国际北塔-307',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85560,1040129,240111044332060075,239825274387462481,'世东国际北塔-511',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85592,1040129,240111044332060075,239825274387462513,'世东国际北塔-801',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85624,1040129,240111044332060075,239825274387462545,'世东国际北塔-1005',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85656,1040129,240111044332060075,239825274387462577,'世东国际北塔-1209',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85688,1040129,240111044332060075,239825274387462609,'世东国际北塔-1413',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85720,1040129,240111044332060075,239825274387462641,'世东国际北塔-1703',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85752,1040129,240111044332060075,239825274387462673,'世东国际北塔-1907',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85784,1040129,240111044332060075,239825274387462705,'世东国际南塔-311',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85816,1040129,240111044332060075,239825274387462737,'世东国际南塔-601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85848,1040129,240111044332060075,239825274387462769,'世东国际南塔-805',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85880,1040129,240111044332060075,239825274387462801,'世东国际南塔-1011',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85912,1040129,240111044332060075,239825274387462833,'世东国际南塔-1301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85944,1040129,240111044332060075,239825274387462865,'世东国际南塔-1505',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85529,1040129,240111044332060075,239825274387462450,'世东国际北塔-308',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85561,1040129,240111044332060075,239825274387462482,'世东国际北塔-512',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85593,1040129,240111044332060075,239825274387462514,'世东国际北塔-802',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85625,1040129,240111044332060075,239825274387462546,'世东国际北塔-1006',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85657,1040129,240111044332060075,239825274387462578,'世东国际北塔-1210',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85689,1040129,240111044332060075,239825274387462610,'世东国际北塔-1414',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85721,1040129,240111044332060075,239825274387462642,'世东国际北塔-1704',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85753,1040129,240111044332060075,239825274387462674,'世东国际北塔-1908',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85785,1040129,240111044332060075,239825274387462706,'世东国际南塔-312',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85817,1040129,240111044332060075,239825274387462738,'世东国际南塔-602',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85849,1040129,240111044332060075,239825274387462770,'世东国际南塔-806',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85881,1040129,240111044332060075,239825274387462802,'世东国际南塔-1012',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85913,1040129,240111044332060075,239825274387462834,'世东国际南塔-1302',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85945,1040129,240111044332060075,239825274387462866,'世东国际南塔-1506',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85530,1040129,240111044332060075,239825274387462451,'世东国际北塔-309',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85562,1040129,240111044332060075,239825274387462483,'世东国际北塔-513',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85594,1040129,240111044332060075,239825274387462515,'世东国际北塔-803',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85626,1040129,240111044332060075,239825274387462547,'世东国际北塔-1007',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85658,1040129,240111044332060075,239825274387462579,'世东国际北塔-1211',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85690,1040129,240111044332060075,239825274387462611,'世东国际北塔-1501',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85722,1040129,240111044332060075,239825274387462643,'世东国际北塔-1705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85754,1040129,240111044332060075,239825274387462675,'世东国际北塔-1909',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85786,1040129,240111044332060075,239825274387462707,'世东国际南塔-313',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85818,1040129,240111044332060075,239825274387462739,'世东国际南塔-603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85850,1040129,240111044332060075,239825274387462771,'世东国际南塔-807',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85882,1040129,240111044332060075,239825274387462803,'世东国际南塔-1013',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85914,1040129,240111044332060075,239825274387462835,'世东国际南塔-1303',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85946,1040129,240111044332060075,239825274387462867,'世东国际南塔-1507',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85531,1040129,240111044332060075,239825274387462452,'世东国际北塔-310',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85563,1040129,240111044332060075,239825274387462484,'世东国际北塔-514',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85595,1040129,240111044332060075,239825274387462516,'世东国际北塔-804',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85627,1040129,240111044332060075,239825274387462548,'世东国际北塔-1008',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85659,1040129,240111044332060075,239825274387462580,'世东国际北塔-1212',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85691,1040129,240111044332060075,239825274387462612,'世东国际北塔-1502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85723,1040129,240111044332060075,239825274387462644,'世东国际北塔-1706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85755,1040129,240111044332060075,239825274387462676,'世东国际北塔-1910',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85787,1040129,240111044332060075,239825274387462708,'世东国际南塔-314',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85819,1040129,240111044332060075,239825274387462740,'世东国际南塔-604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85851,1040129,240111044332060075,239825274387462772,'世东国际南塔-808',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85883,1040129,240111044332060075,239825274387462804,'世东国际南塔-1014',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85915,1040129,240111044332060075,239825274387462836,'世东国际南塔-1304',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85947,1040129,240111044332060075,239825274387462868,'世东国际南塔-1508',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85532,1040129,240111044332060075,239825274387462453,'世东国际北塔-311',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85564,1040129,240111044332060075,239825274387462485,'世东国际北塔-601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85596,1040129,240111044332060075,239825274387462517,'世东国际北塔-805',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85628,1040129,240111044332060075,239825274387462549,'世东国际北塔-1009',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85660,1040129,240111044332060075,239825274387462581,'世东国际北塔-1213',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85692,1040129,240111044332060075,239825274387462613,'世东国际北塔-1503',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85724,1040129,240111044332060075,239825274387462645,'世东国际北塔-1707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85756,1040129,240111044332060075,239825274387462677,'世东国际北塔-1911',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85788,1040129,240111044332060075,239825274387462709,'世东国际南塔-401',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85820,1040129,240111044332060075,239825274387462741,'世东国际南塔-605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85852,1040129,240111044332060075,239825274387462773,'世东国际南塔-809',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85884,1040129,240111044332060075,239825274387462805,'世东国际南塔-1101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85916,1040129,240111044332060075,239825274387462837,'世东国际南塔-1305',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85948,1040129,240111044332060075,239825274387462869,'世东国际南塔-1509',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85533,1040129,240111044332060075,239825274387462454,'世东国际北塔-312',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85565,1040129,240111044332060075,239825274387462486,'世东国际北塔-602',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85597,1040129,240111044332060075,239825274387462518,'世东国际北塔-806',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85629,1040129,240111044332060075,239825274387462550,'世东国际北塔-1010',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85661,1040129,240111044332060075,239825274387462582,'世东国际北塔-1214',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85693,1040129,240111044332060075,239825274387462614,'世东国际北塔-1504',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85725,1040129,240111044332060075,239825274387462646,'世东国际北塔-1708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85757,1040129,240111044332060075,239825274387462678,'世东国际北塔-1912',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85789,1040129,240111044332060075,239825274387462710,'世东国际南塔-402',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85821,1040129,240111044332060075,239825274387462742,'世东国际南塔-606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85853,1040129,240111044332060075,239825274387462774,'世东国际南塔-810',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85885,1040129,240111044332060075,239825274387462806,'世东国际南塔-1102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85917,1040129,240111044332060075,239825274387462838,'世东国际南塔-1306',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85949,1040129,240111044332060075,239825274387462870,'世东国际南塔-1510',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85534,1040129,240111044332060075,239825274387462455,'世东国际北塔-313',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85566,1040129,240111044332060075,239825274387462487,'世东国际北塔-603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85598,1040129,240111044332060075,239825274387462519,'世东国际北塔-807',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85630,1040129,240111044332060075,239825274387462551,'世东国际北塔-1011',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85662,1040129,240111044332060075,239825274387462583,'世东国际北塔-1301',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85694,1040129,240111044332060075,239825274387462615,'世东国际北塔-1505',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85726,1040129,240111044332060075,239825274387462647,'世东国际北塔-1709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85758,1040129,240111044332060075,239825274387462679,'世东国际北塔-1913',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85790,1040129,240111044332060075,239825274387462711,'世东国际南塔-403',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85822,1040129,240111044332060075,239825274387462743,'世东国际南塔-607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85854,1040129,240111044332060075,239825274387462775,'世东国际南塔-811',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85886,1040129,240111044332060075,239825274387462807,'世东国际南塔-1103',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85918,1040129,240111044332060075,239825274387462839,'世东国际南塔-1307',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85950,1040129,240111044332060075,239825274387462871,'世东国际南塔-1511',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85535,1040129,240111044332060075,239825274387462456,'世东国际北塔-314',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85567,1040129,240111044332060075,239825274387462488,'世东国际北塔-604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85599,1040129,240111044332060075,239825274387462520,'世东国际北塔-808',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85631,1040129,240111044332060075,239825274387462552,'世东国际北塔-1012',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85663,1040129,240111044332060075,239825274387462584,'世东国际北塔-1302',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85695,1040129,240111044332060075,239825274387462616,'世东国际北塔-1506',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85727,1040129,240111044332060075,239825274387462648,'世东国际北塔-1710',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85759,1040129,240111044332060075,239825274387462680,'世东国际北塔-1914',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85791,1040129,240111044332060075,239825274387462712,'世东国际南塔-404',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85823,1040129,240111044332060075,239825274387462744,'世东国际南塔-608',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85855,1040129,240111044332060075,239825274387462776,'世东国际南塔-812',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85887,1040129,240111044332060075,239825274387462808,'世东国际南塔-1104',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85919,1040129,240111044332060075,239825274387462840,'世东国际南塔-1308',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85951,1040129,240111044332060075,239825274387462872,'世东国际南塔-1512',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85536,1040129,240111044332060075,239825274387462457,'世东国际北塔-401',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85568,1040129,240111044332060075,239825274387462489,'世东国际北塔-605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85600,1040129,240111044332060075,239825274387462521,'世东国际北塔-809',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85632,1040129,240111044332060075,239825274387462553,'世东国际北塔-1013',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85664,1040129,240111044332060075,239825274387462585,'世东国际北塔-1303',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85696,1040129,240111044332060075,239825274387462617,'世东国际北塔-1507',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85728,1040129,240111044332060075,239825274387462649,'世东国际北塔-1711',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85760,1040129,240111044332060075,239825274387462681,'世东国际北塔-2001',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85792,1040129,240111044332060075,239825274387462713,'世东国际南塔-405',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85824,1040129,240111044332060075,239825274387462745,'世东国际南塔-609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85856,1040129,240111044332060075,239825274387462777,'世东国际南塔-813',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85888,1040129,240111044332060075,239825274387462809,'世东国际南塔-1105',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85920,1040129,240111044332060075,239825274387462841,'世东国际南塔-1309',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85952,1040129,240111044332060075,239825274387462873,'世东国际南塔-1513',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85537,1040129,240111044332060075,239825274387462458,'世东国际北塔-402',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85569,1040129,240111044332060075,239825274387462490,'世东国际北塔-606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85601,1040129,240111044332060075,239825274387462522,'世东国际北塔-810',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85633,1040129,240111044332060075,239825274387462554,'世东国际北塔-1014',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85665,1040129,240111044332060075,239825274387462586,'世东国际北塔-1304',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85697,1040129,240111044332060075,239825274387462618,'世东国际北塔-1508',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85729,1040129,240111044332060075,239825274387462650,'世东国际北塔-1712',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85761,1040129,240111044332060075,239825274387462682,'世东国际北塔-2002',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85793,1040129,240111044332060075,239825274387462714,'世东国际南塔-406',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85825,1040129,240111044332060075,239825274387462746,'世东国际南塔-610',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85857,1040129,240111044332060075,239825274387462778,'世东国际南塔-814',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85889,1040129,240111044332060075,239825274387462810,'世东国际南塔-1106',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85921,1040129,240111044332060075,239825274387462842,'世东国际南塔-1310',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85953,1040129,240111044332060075,239825274387462874,'世东国际南塔-1514',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85538,1040129,240111044332060075,239825274387462459,'世东国际北塔-403',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85570,1040129,240111044332060075,239825274387462491,'世东国际北塔-607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85602,1040129,240111044332060075,239825274387462523,'世东国际北塔-811',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85634,1040129,240111044332060075,239825274387462555,'世东国际北塔-1101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85666,1040129,240111044332060075,239825274387462587,'世东国际北塔-1305',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85698,1040129,240111044332060075,239825274387462619,'世东国际北塔-1509',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85730,1040129,240111044332060075,239825274387462651,'世东国际北塔-1713',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85762,1040129,240111044332060075,239825274387462683,'世东国际北塔-2003',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85794,1040129,240111044332060075,239825274387462715,'世东国际南塔-407',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85826,1040129,240111044332060075,239825274387462747,'世东国际南塔-611',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85858,1040129,240111044332060075,239825274387462779,'世东国际南塔-901',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85890,1040129,240111044332060075,239825274387462811,'世东国际南塔-1107',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85922,1040129,240111044332060075,239825274387462843,'世东国际南塔-1311',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85539,1040129,240111044332060075,239825274387462460,'世东国际北塔-404',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85571,1040129,240111044332060075,239825274387462492,'世东国际北塔-608',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85603,1040129,240111044332060075,239825274387462524,'世东国际北塔-812',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85635,1040129,240111044332060075,239825274387462556,'世东国际北塔-1102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85667,1040129,240111044332060075,239825274387462588,'世东国际北塔-1306',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85699,1040129,240111044332060075,239825274387462620,'世东国际北塔-1510',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85731,1040129,240111044332060075,239825274387462652,'世东国际北塔-1714',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85763,1040129,240111044332060075,239825274387462684,'世东国际北塔-2004',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85795,1040129,240111044332060075,239825274387462716,'世东国际南塔-408',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85827,1040129,240111044332060075,239825274387462748,'世东国际南塔-612',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85859,1040129,240111044332060075,239825274387462780,'世东国际南塔-902',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85891,1040129,240111044332060075,239825274387462812,'世东国际南塔-1108',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85923,1040129,240111044332060075,239825274387462844,'世东国际南塔-1312',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85540,1040129,240111044332060075,239825274387462461,'世东国际北塔-405',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85572,1040129,240111044332060075,239825274387462493,'世东国际北塔-609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85604,1040129,240111044332060075,239825274387462525,'世东国际北塔-813',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85636,1040129,240111044332060075,239825274387462557,'世东国际北塔-1103',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85668,1040129,240111044332060075,239825274387462589,'世东国际北塔-1307',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85700,1040129,240111044332060075,239825274387462621,'世东国际北塔-1511',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85732,1040129,240111044332060075,239825274387462653,'世东国际北塔-1801',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85764,1040129,240111044332060075,239825274387462685,'世东国际北塔-2005',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85796,1040129,240111044332060075,239825274387462717,'世东国际南塔-409',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85828,1040129,240111044332060075,239825274387462749,'世东国际南塔-613',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85860,1040129,240111044332060075,239825274387462781,'世东国际南塔-903',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85892,1040129,240111044332060075,239825274387462813,'世东国际南塔-1109',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85924,1040129,240111044332060075,239825274387462845,'世东国际南塔-1313',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85541,1040129,240111044332060075,239825274387462462,'世东国际北塔-406',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85573,1040129,240111044332060075,239825274387462494,'世东国际北塔-610',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85605,1040129,240111044332060075,239825274387462526,'世东国际北塔-814',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85637,1040129,240111044332060075,239825274387462558,'世东国际北塔-1104',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85669,1040129,240111044332060075,239825274387462590,'世东国际北塔-1308',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85701,1040129,240111044332060075,239825274387462622,'世东国际北塔-1512',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85733,1040129,240111044332060075,239825274387462654,'世东国际北塔-1802',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85765,1040129,240111044332060075,239825274387462686,'世东国际北塔-2006',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85797,1040129,240111044332060075,239825274387462718,'世东国际南塔-410',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85829,1040129,240111044332060075,239825274387462750,'世东国际南塔-614',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85861,1040129,240111044332060075,239825274387462782,'世东国际南塔-904',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85893,1040129,240111044332060075,239825274387462814,'世东国际南塔-1110',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85925,1040129,240111044332060075,239825274387462846,'世东国际南塔-1314',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85542,1040129,240111044332060075,239825274387462463,'世东国际北塔-407',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85574,1040129,240111044332060075,239825274387462495,'世东国际北塔-611',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85606,1040129,240111044332060075,239825274387462527,'世东国际北塔-901',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85638,1040129,240111044332060075,239825274387462559,'世东国际北塔-1105',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85670,1040129,240111044332060075,239825274387462591,'世东国际北塔-1309',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85702,1040129,240111044332060075,239825274387462623,'世东国际北塔-1513',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85734,1040129,240111044332060075,239825274387462655,'世东国际北塔-1803',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85766,1040129,240111044332060075,239825274387462687,'世东国际北塔-2007',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85798,1040129,240111044332060075,239825274387462719,'世东国际南塔-411',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85830,1040129,240111044332060075,239825274387462751,'世东国际南塔-701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85862,1040129,240111044332060075,239825274387462783,'世东国际南塔-905',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85894,1040129,240111044332060075,239825274387462815,'世东国际南塔-1111',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85926,1040129,240111044332060075,239825274387462847,'世东国际南塔-1401',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85543,1040129,240111044332060075,239825274387462464,'世东国际北塔-408',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85575,1040129,240111044332060075,239825274387462496,'世东国际北塔-612',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85607,1040129,240111044332060075,239825274387462528,'世东国际北塔-902',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85639,1040129,240111044332060075,239825274387462560,'世东国际北塔-1106',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85671,1040129,240111044332060075,239825274387462592,'世东国际北塔-1310',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85703,1040129,240111044332060075,239825274387462624,'世东国际北塔-1514',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85735,1040129,240111044332060075,239825274387462656,'世东国际北塔-1804',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85767,1040129,240111044332060075,239825274387462688,'世东国际北塔-2008',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85799,1040129,240111044332060075,239825274387462720,'世东国际南塔-412',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85831,1040129,240111044332060075,239825274387462752,'世东国际南塔-702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85863,1040129,240111044332060075,239825274387462784,'世东国际南塔-906',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85895,1040129,240111044332060075,239825274387462816,'世东国际南塔-1112',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85927,1040129,240111044332060075,239825274387462848,'世东国际南塔-1402',2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120792,40400,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120793,40410,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120794,40420,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120795,40430,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120796,40440,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120797,40450,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120798,40500,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120799,40800,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120800,40830,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120801,40835,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120802,40810,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120803,40840,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120804,40850,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120805,40900,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120806,41000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120807,41010,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120808,41020,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120809,41030,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120810,41040,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120811,41050,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120812,41060,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120813,41100,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120814,41600,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120815,41610,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120816,41620,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120817,41630,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120818,41300,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120819,41310,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120820,41330,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120821,41320,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120822,30000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120823,30500,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120824,30550,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120825,31000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120826,32000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120827,33000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120828,34000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120829,35000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120830,30600,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120831,50000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120832,50100,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120833,50300,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120834,50500,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120835,50700,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120836,50710,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120837,50720,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120838,50730,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120839,50800,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120840,50810,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120841,50820,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120842,50830,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120843,50840,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120844,50850,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120845,50860,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120846,50900,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120847,50910,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120848,50912,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120849,50914,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120850,50916,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120851,50920,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120852,51000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120853,52000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120854,52010,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120855,52020,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120856,52030,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120857,70000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120858,70300,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120859,70100,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120860,70200,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120861,60000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120862,60100,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120863,60200,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120747,10000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120748,10100,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120749,10400,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120750,10600,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120751,10750,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120752,10751,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120753,10752,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120754,10800,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120755,11000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120756,12200,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120757,20000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120758,20100,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120759,20140,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120760,20150,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120761,20155,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120762,20158,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120763,20170,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120764,20180,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120765,20190,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120766,20191,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120767,20400,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120768,204011,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120769,204021,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120770,20430,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120771,49100,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120772,49110,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120773,49150,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120774,49120,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120775,49130,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120776,49140,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120777,21100,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120778,21110,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120779,21120,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120780,20900,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120781,20910,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120782,20920,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120783,20930,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120784,40000,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120785,40100,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120786,40103,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120787,40105,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120788,40110,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120789,40120,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120790,40130,NULL,'EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120791,40300,NULL,'EhNamespaces',999955,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001245,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999955,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001246,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999955,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001247,'',0,1001247,1,'活动管理-默认子分类','/1/1001247',0,2,1,NOW(),0,NULL,999955,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001248,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999955,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001249,'',0,1001249,2,'活动管理二-默认子分类','/2/1001249',0,2,1,NOW(),0,NULL,999955,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001250,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999955,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001251,'',0,1001251,3,'活动管理三-默认子分类','/3/1001251',0,2,1,NOW(),0,NULL,999955,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (363,999955,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (364,999955,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (365,999955,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (366,999955,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (367,999955,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (368,999955,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (369,999955,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (370,999955,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (371,999955,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1040129,240111044332060075);
    
    
    
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (900,999955,'AssociationLayout','{"versionCode":"2017121501","layoutName":"AssociationLayout","displayName":"交流大厅","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2017121501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (901,999955,'AssociationLayout','{"versionCode":"2017121501","layoutName":"AssociationLayout","displayName":"交流大厅","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2017121501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (902,999955,'SecondServiceMarketLayout','{"versionCode":"2017121501","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":1,"separatorHeight":16,"columnCount":2},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "Gallery","newsSize":3,"entityCount":2},"style": "LargeImageListView","defaultOrder":20,"separatorFlag":0,"separatorHeight":0}]}',2017121501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (903,999955,'SecondServiceMarketLayout','{"versionCode":"2017121501","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":1,"separatorHeight":16,"columnCount":2},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "Gallery","newsSize":3,"entityCount":2},"style": "LargeImageListView","defaultOrder":20,"separatorFlag":0,"separatorHeight":0}]}',2017121501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (904,999955,'HomeResourceLayout','{"versionCode":"2017121501","layoutName":"HomeResourceLayout","displayName":"资源预订","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2017121501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (905,999955,'HomeResourceLayout','{"versionCode":"2017121501","layoutName":"HomeResourceLayout","displayName":"资源预订","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2017121501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (906,999955,'ServiceMarketLayout','{"versionCode":"2017120724","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize":3,"entityCount":3,"subjectHeight":1,"descriptionHeight":1},"style": "ListView","defaultOrder":40,"separatorFlag":1,"separatorHeight":16},{"groupName":"快讯标题","widget":"Navigator","instanceConfig":{"cssStyleFlag":1,"itemGroup":"EhPortalItemGroups4643","paddingTop":0,"paddingLeft":0,"paddingBottom":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0,"backgroundColor":"#ffffff"},"style":"Gallery","defaultOrder":45,"separatorFlag":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2017120724,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (907,999955,'ServiceMarketLayout','{"versionCode":"2017120724","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize":3,"entityCount":3,"subjectHeight":1,"descriptionHeight":1},"style": "ListView","defaultOrder":40,"separatorFlag":1,"separatorHeight":16},{"groupName":"快讯标题","widget":"Navigator","instanceConfig":{"cssStyleFlag":1,"itemGroup":"EhPortalItemGroups4643","paddingTop":0,"paddingLeft":0,"paddingBottom":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0,"backgroundColor":"#ffffff"},"style":"Gallery","defaultOrder":45,"separatorFlag":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2017120724,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216724,999955,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRvelpHRmpOamM1TWpRM04yTTJaamMyTm1aaE5tWTRNVFJoWldaak9HTmtPQQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216725,999955,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRvelpHRmpOamM1TWpRM04yTTJaamMyTm1aaE5tWTRNVFJoWldaak9HTmtPQQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139898,999955,0,0,0,'/home','NavigatorGroup3','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRvME1EY3pOVEE0TWpVMVpqY3daalZrT1RnM05EazFPV1ZpWW1VeE1UTmhaQQ',1,1,45,'',10,0,1,1,0,1,'pm_admin',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139899,999955,0,0,0,'/home','NavigatorGroup3','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRvME1EY3pOVEE0TWpVMVpqY3daalZrT1RnM05EazFPV1ZpWW1VeE1UTmhaQQ',1,1,45,'',10,0,1,1,0,1,'park_tourist',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139900,999955,0,0,0,'/home','NavigatorGroup3','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRvNFpXVmlORE5pTURsaE1EWTBNelV5TmpRNFpqUTBabUUyWWpneFltVTNNZw',1,1,27,'',20,0,1,1,0,1,'pm_admin',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139901,999955,0,0,0,'/home','NavigatorGroup3','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRvNFpXVmlORE5pTURsaE1EWTBNelV5TmpRNFpqUTBabUUyWWpneFltVTNNZw',1,1,27,'',20,0,1,1,0,1,'park_tourist',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139902,999955,0,0,0,'/home','NavigatorGroup3','园区企业','园区企业','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,34,'',30,0,1,1,0,1,'pm_admin',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139903,999955,0,0,0,'/home','NavigatorGroup3','园区企业','园区企业','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,34,'',30,0,1,1,0,1,'park_tourist',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139904,999955,0,0,0,'/home','NavigatorGroup3','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwaE9HSXpOREk0TW1Jd05HTTNOekJsTW1Rd01ESXlaR05tTkRObE5qTTFaUQ',1,1,46,'',40,0,1,1,0,1,'pm_admin',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139905,999955,0,0,0,'/home','NavigatorGroup3','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwaE9HSXpOREk0TW1Jd05HTTNOekJsTW1Rd01ESXlaR05tTkRObE5qTTFaUQ',1,1,46,'',40,0,1,1,0,1,'park_tourist',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139906,999955,0,0,0,'/home','NavigatorGroup3','企业服务','企业服务','cs://1/image/aW1hZ2UvTVRvME4yVmpOekprWWpobU1XSmxOV0UzWWpneE5qaGlPV1kzWWpkaE9UVTJNZw',1,1,33,'{"type":212828,"parentId":212828,"displayType": "grid"}',50,0,1,1,0,1,'pm_admin',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139907,999955,0,0,0,'/home','NavigatorGroup3','企业服务','企业服务','cs://1/image/aW1hZ2UvTVRvME4yVmpOekprWWpobU1XSmxOV0UzWWpneE5qaGlPV1kzWWpkaE9UVTJNZw',1,1,33,'{"type":212828,"parentId":212828,"displayType": "grid"}',50,0,1,1,0,1,'park_tourist',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139908,999955,0,0,0,'/home','NavigatorGroup3','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwaVlUTXpOVEZpTVdZNFpHWTFaakZsT0dJd1lUTmxabVE0WldJNVkyTTFaQQ',1,1,33,'{"type":212829,"parentId":212829,"displayType": "grid"}',60,0,1,1,0,1,'pm_admin',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139909,999955,0,0,0,'/home','NavigatorGroup3','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwaVlUTXpOVEZpTVdZNFpHWTFaakZsT0dJd1lUTmxabVE0WldJNVkyTTFaQQ',1,1,33,'{"type":212829,"parentId":212829,"displayType": "grid"}',60,0,1,1,0,1,'park_tourist',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139910,999955,0,0,0,'/home','NavigatorGroup3','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRwaE1HRXdPVEF6WVRnNFltRmhaalExWlRJeU56UmtOemxoTXpNeE5qUTBOQQ',1,1,56,'',70,0,1,1,0,1,'pm_admin',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139911,999955,0,0,0,'/home','NavigatorGroup3','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRwaE1HRXdPVEF6WVRnNFltRmhaalExWlRJeU56UmtOemxoTXpNeE5qUTBOQQ',1,1,56,'',70,0,1,1,0,1,'park_tourist',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139912,999955,0,0,0,'/home','NavigatorGroup3','更多','更多','cs://1/image/aW1hZ2UvTVRvM1pXRTVObVpsTnpZek1tTXhZMkUxTW1FMllqTmpNMlkwT1RBMlkySXdOQQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup3"}',1000,0,1,1,0,1,'pm_admin',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139913,999955,0,0,0,'/home','NavigatorGroup3','更多','更多','cs://1/image/aW1hZ2UvTVRvM1pXRTVObVpsTnpZek1tTXhZMkUxTW1FMllqTmpNMlkwT1RBMlkySXdOQQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup3"}',1000,0,1,1,0,1,'park_tourist',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139914,999955,0,0,0,'/home','NavigatorGroup3','一键上网','一键上网','cs://1/image/aW1hZ2UvTVRvMU16TTNaVGRpT0RRek5EUXdaakppWWpGall6Vm1ObVV5TVRaak1UWTFOZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',90,0,1,0,0,1,'pm_admin',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139915,999955,0,0,0,'/home','NavigatorGroup3','一键上网','一键上网','cs://1/image/aW1hZ2UvTVRvMU16TTNaVGRpT0RRek5EUXdaakppWWpGall6Vm1ObVV5TVRaak1UWTFOZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',90,0,1,0,0,1,'park_tourist',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139916,999955,0,0,0,'/home','NavigatorGroup3','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRwaE1HUmhZek5pTldVd01UY3lNVGMxWTJZM05UQmpaVGcwWVdKaFpEWTNPUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',100,0,1,0,0,1,'pm_admin',0,100,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139917,999955,0,0,0,'/home','NavigatorGroup3','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRwaE1HUmhZek5pTldVd01UY3lNVGMxWTJZM05UQmpaVGcwWVdKaFpEWTNPUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',100,0,1,0,0,1,'park_tourist',0,100,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139918,999955,0,0,0,'/home','NavigatorGroup3','食堂通','食堂通','cs://1/image/aW1hZ2UvTVRwalpURm1PV1V3WW1ZNU5UWXlNR1EyWW1FNU1XWmlOV0ZtTjJSaE1XVXdZZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',110,0,1,0,0,1,'pm_admin',0,110,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139919,999955,0,0,0,'/home','NavigatorGroup3','食堂通','食堂通','cs://1/image/aW1hZ2UvTVRwalpURm1PV1V3WW1ZNU5UWXlNR1EyWW1FNU1XWmlOV0ZtTjJSaE1XVXdZZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',110,0,1,0,0,1,'park_tourist',0,110,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139920,999955,0,0,0,'/home','NavigatorGroup3','智能门禁','智能门禁','cs://1/image/aW1hZ2UvTVRvNE5tRTBaalV5TTJRMlltWXpNR1l3TnpnM01EZGhaREF3WldZek5UTmpNQQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',120,0,1,0,0,1,'pm_admin',0,120,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139921,999955,0,0,0,'/home','NavigatorGroup3','智能门禁','智能门禁','cs://1/image/aW1hZ2UvTVRvNE5tRTBaalV5TTJRMlltWXpNR1l3TnpnM01EZGhaREF3WldZek5UTmpNQQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',120,0,1,0,0,1,'park_tourist',0,120,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139922,999955,0,0,0,'/home','NavigatorGroup3','智慧停车','智慧停车','cs://1/image/aW1hZ2UvTVRwaFlXWTNZelpqTWprMU9XRmlOVEppTTJWbU1XTXlabU00WWpJMVptUTNNUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',130,0,1,0,0,1,'pm_admin',0,130,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139923,999955,0,0,0,'/home','NavigatorGroup3','智慧停车','智慧停车','cs://1/image/aW1hZ2UvTVRwaFlXWTNZelpqTWprMU9XRmlOVEppTTJWbU1XTXlabU00WWpJMVptUTNNUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',130,0,1,0,0,1,'park_tourist',0,130,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139924,999955,0,0,0,'/home','Default','园区快讯','园区快讯','cs://1/image/aW1hZ2UvTVRwalpHTmtZMk5tWkdRNVl6Z3dZalk1TURObE1qQmtabUpsTkdNd1lXSTRPUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139925,999955,0,0,0,'/home','Default','园区快讯','园区快讯','cs://1/image/aW1hZ2UvTVRwalpHTmtZMk5tWkdRNVl6Z3dZalk1TURObE1qQmtabUpsTkdNd1lXSTRPUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139926,999955,0,0,0,'/secondhome','NavigatorGroup1','账单查询','账单查询','cs://1/image/aW1hZ2UvTVRvNFl6VTVaV1E0WVdVME1ERTBNemd5WVRJd01ETXpaVFE1TWpneVlUYzBaZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139927,999955,0,0,0,'/secondhome','NavigatorGroup1','账单查询','账单查询','cs://1/image/aW1hZ2UvTVRvNFl6VTVaV1E0WVdVME1ERTBNemd5WVRJd01ETXpaVFE1TWpneVlUYzBaZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139928,999955,0,0,0,'/secondhome','NavigatorGroup1','预约看房','预约看房','cs://1/image/aW1hZ2UvTVRvMVlUSTVNbVZpWlRJM1kyUTRaV1U0WlRObFlqYzRNamxoWWpNd1l6aGlPQQ',1,1,28,'',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139929,999955,0,0,0,'/secondhome','NavigatorGroup1','预约看房','预约看房','cs://1/image/aW1hZ2UvTVRvMVlUSTVNbVZpWlRJM1kyUTRaV1U0WlRObFlqYzRNamxoWWpNd1l6aGlPQQ',1,1,28,'',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139930,999955,0,0,0,'/secondhome','Gallery','项目介绍','项目介绍','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,33,'{"type":212830,"parentId":212830,"displayType": "list"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139931,999955,0,0,0,'/secondhome','Gallery','项目介绍','项目介绍','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,33,'{"type":212830,"parentId":212830,"displayType": "list"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139932,999955,0,0,0,'/association','TabGroup','活动','活动','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139933,999955,0,0,0,'/association','TabGroup','活动','活动','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139934,999955,0,0,0,'/association','TabGroup','社群','社群','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,36,'{"privateFlag": 0}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139935,999955,0,0,0,'/association','TabGroup','社群','社群','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,36,'{"privateFlag": 0}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139936,999955,0,0,0,'/association','TabGroup','发现','发现','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,62,'{"tag":"发现"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139937,999955,0,0,0,'/association','TabGroup','发现','发现','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,62,'{"tag":"发现"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139938,999955,0,0,0,'/home','NavigatorGroup3','资源预订','资源预订','cs://1/image/aW1hZ2UvTVRwaU16Z3dPRGd4TmpCaU9XTTFPVEEzTjJaaFl6WTVaR0prTnpJNFl6UTVNUQ',1,1,60,'{"url":"zl://association/main?layoutName=HomeResourceLayout&itemLocation=/home/resource&versionCode=2017121501&displayName=资源预订"}',80,0,1,0,0,1,'pm_admin',0,200,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139939,999955,0,0,0,'/home','NavigatorGroup3','资源预订','资源预订','cs://1/image/aW1hZ2UvTVRwaU16Z3dPRGd4TmpCaU9XTTFPVEEzTjJaaFl6WTVaR0prTnpJNFl6UTVNUQ',1,1,60,'{"url":"zl://association/main?layoutName=HomeResourceLayout&itemLocation=/home/resource&versionCode=2017121501&displayName=资源预订"}',80,0,1,0,0,1,'park_tourist',0,200,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139940,999955,0,0,0,'/home/resource','TabGroup','会议室预订','会议室预订','',1,1,49,'{"resourceTypeId":12198,"pageType":0}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139941,999955,0,0,0,'/home/resource','TabGroup','会议室预订','会议室预订','',1,1,49,'{"resourceTypeId":12198,"pageType":0}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139942,999955,0,0,0,'/home/resource','TabGroup','电子屏','电子屏','',1,1,49,'{"resourceTypeId":12199,"pageType":0}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139943,999955,0,0,0,'/home/resource','TabGroup','电子屏','电子屏','',1,1,49,'{"resourceTypeId":12199,"pageType":0}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139944,999955,0,0,0,'/home/resource','TabGroup','VIP车位','VIP车位','',1,1,49,'{"resourceTypeId":12200,"pageType":0}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (139945,999955,0,0,0,'/home/resource','TabGroup','VIP车位','VIP车位','',1,1,49,'{"resourceTypeId":12200,"pageType":0}',30,0,1,1,0,0,'park_tourist',0,0,'');
    
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) 
    VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), '999955', '0', '0', '0', '/home', 'EhPortalItemGroups4643', 'EhPortalItemGroups4643', '快讯标题', 'cs://1/image/aW1hZ2UvTVRvell6QXlOalppWWpaa09HRTVORGhtWkRReU5qQmlPRGd4WVRjME5HSmtaZw', '1', '1', '0', NULL, '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '1', NULL, NULL, '0', NULL, '');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) 
    VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), '999955', '0', '0', '0', '/home', 'EhPortalItemGroups4643', 'EhPortalItemGroups4643', '快讯标题', 'cs://1/image/aW1hZ2UvTVRvell6QXlOalppWWpaa09HRTVORGhtWkRReU5qQmlPRGd4WVRjME5HSmtaZw', '1', '1', '0', NULL, '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL, NULL, '0', NULL, '');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999955,0,0,0,'/home','OPPushActivity','最新活动','最新活动','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999955,0,0,0,'/home','OPPushActivity','最新活动','最新活动','cs://1/image/aW1hZ2UvTVRvME5URXlNbVExTVdNMU1HRmtNemMzTnpObVpUSmxNMlU1WW1SaE9HRXhZZw',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',10,0,1,1,0,0,'park_tourist',0,0,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212828,'community','240111044331055940',0,'企业服务','企业服务',0,2,1,UTC_TIMESTAMP(),999955,1);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212829,'community','240111044331055940',0,'服务联盟','服务联盟',0,2,1,UTC_TIMESTAMP(),999955,2);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212830,'community','240111044331055940',0,'项目介绍','项目介绍',0,2,1,UTC_TIMESTAMP(),999955,3);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211435,0,'organaization',1040129,'all','企业服务','企业服务',212828,2,211435,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211436,0,'organaization',1040129,'all','服务联盟','服务联盟',212829,2,211436,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211437,0,'organaization',1040129,'all','项目介绍','项目介绍',212830,2,211437,UTC_TIMESTAMP(),2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120866,41700,'企业服务','EhNamespaces',999955,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120867,41710,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120868,41720,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120869,41730,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120870,41740,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120871,41750,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120872,41760,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120873,41800,'服务联盟','EhNamespaces',999955,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120874,41810,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120875,41820,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120876,41830,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120877,41840,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120878,41850,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120879,41860,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120880,41900,'项目介绍','EhNamespaces',999955,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120881,41910,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120882,41920,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120883,41930,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120884,41940,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120885,41950,'','EhNamespaces',999955,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (120886,41960,'','EhNamespaces',999955,2);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12198,'会议室预订',0,2,999955,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12199,'电子屏',0,2,999955,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12200,'VIP车位',0,2,999955,0,0);

