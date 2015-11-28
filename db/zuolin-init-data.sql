USE `ehcore`;

#alter tables

ALTER TABLE `eh_punch_logs` ADD COLUMN  `identification` VARCHAR(255) COMMENT 'unique identification for a phone';
ALTER TABLE `eh_punch_logs` CHANGE company_id `enterprise_id` BIGINT COMMENT 'compay id';
ALTER TABLE eh_punch_day_logs CHANGE company_id `enterprise_id` BIGINT COMMENT 'compay id';
ALTER TABLE eh_punch_day_logs ADD COLUMN `morning_status` TINYINT  DEFAULT NULL COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)';
ALTER TABLE eh_punch_day_logs ADD COLUMN `afternoon_status` TINYINT  DEFAULT NULL COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)';
ALTER TABLE eh_punch_day_logs ADD COLUMN  `punch_times_per_day` TINYINT  NOT NULL DEFAULT 2 COMMENT '2 or  4 times';
ALTER TABLE eh_punch_day_logs ADD COLUMN  `noon_leave_time` TIME ;
ALTER TABLE eh_punch_day_logs ADD COLUMN `afternoon_arrive_time` TIME ; 

ALTER TABLE `eh_punch_rules` CHANGE company_id `enterprise_id` BIGINT COMMENT 'compay id';
ALTER TABLE eh_punch_rules ADD COLUMN  `noon_leave_time` TIME ;
ALTER TABLE eh_punch_rules ADD COLUMN `afternoon_arrive_time` TIME ;
ALTER TABLE eh_punch_rules ADD COLUMN  `punch_times_per_day` TINYINT  NOT NULL DEFAULT 2 COMMENT '2 or  4 times';

ALTER TABLE `eh_punch_geopoints` CHANGE company_id `enterprise_id` BIGINT COMMENT 'compay id';

ALTER TABLE `eh_punch_exception_requests` CHANGE company_id `enterprise_id` BIGINT COMMENT 'compay id';
ALTER TABLE eh_punch_exception_requests  CHANGE process_code  `approval_status` TINYINT COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)';
ALTER TABLE eh_punch_exception_requests ADD COLUMN  `morning_approval_status` TINYINT  DEFAULT 1 COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)';
ALTER TABLE eh_punch_exception_requests ADD COLUMN   `afternoon_approval_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)';


ALTER TABLE `eh_punch_exception_approvals` CHANGE company_id `enterprise_id` BIGINT COMMENT 'compay id';
ALTER TABLE eh_punch_exception_approvals ADD COLUMN  `morning_approval_status` TINYINT  DEFAULT 1 COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)';
ALTER TABLE eh_punch_exception_approvals ADD COLUMN   `afternoon_approval_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)';
ALTER TABLE eh_punch_exception_approvals ADD COLUMN  `punch_times_per_day` TINYINT  NOT NULL DEFAULT 2 COMMENT '2 or  4 times';

ALTER TABLE `eh_punch_workday`  ADD COLUMN  `enterprise_id` BIGINT COMMENT 'compay id';

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`) 
VALUES('233',UUID(),'5636851','深圳市','4151','南山区','武汉大学产学研大楼','武汉大学','深圳市南山区科技园',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1000087',NULL,'2','2015-11-05 14:43:25',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1','3','4',NULL);

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`) VALUES('44','0','GANC','科技园物业','0',NULL,'/科技园物业','0','2');


INSERT INTO `eh_forums`(`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(3, UUID(), 0, 2, '', 0, '科技园论坛', '', 0, 0, UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums`(`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(4, UUID(), 0, 2, '', 0, '科技园意见反馈论坛', '', 0, 0, UTC_TIMESTAMP(), UTC_TIMESTAMP());	

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`)
VALUES('2','233','武汉大学产学研大楼B座','武汉大学B','419','12345678910',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2','1',NULL,'419','2015-11-05 14:56:31',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `zipcode`, `address`, `longitude`, `latitude`, `geohash`, `address_alias`, `building_name`, `building_alias_name`, `apartment_name`, `apartment_floor`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) 
VALUES('24206890946790451','ea8a4260-358f-11e5-a5df-000c291c2799','233','5636851',NULL,'4151',NULL,NULL,'科技工业园大厦二层东',NULL,NULL,NULL,'','科技工业园大厦','','二层东','','2','0',NULL,'0','2014-08-01 14:30:15',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);



INSERT INTO `eh_groups` (`id`, `uuid`, `namespace_id`, `name`, `display_name`, `avatar`, `description`, `creator_uid`, `private_flag`, `join_policy`, `discriminator`, `visibility_scope`, `visibility_scope_id`, `category_id`, `category_path`, `status`, `member_count`, `share_count`, `post_flag`, `tag`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `update_time`, `create_time`, `delete_time`, `visible_region_type`, `visible_region_id`)
 VALUES('419',UUID(),'0','左邻','左邻','','','0','1','1','enterprise',NULL,NULL,NULL,NULL,'1','1','0','0',NULL,'24206890946793209','24206890946790476','5636106','419','24206890946793209',NULL,NULL,NULL,NULL,NULL,'2015-08-16 13:14:03','2014-08-01 14:30:15','2015-08-16 13:14:03','0','233');

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
VALUES('419',UUID(),'0','2','EhGroups','419','左邻','','0','0','2015-08-16 13:30:10','2014-11-13 18:05:10');

 

INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `requestor_comment`, `operation_type`, `inviter_uid`, `invite_time`, `update_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) 
VALUES('419','233','enterprise','419','2','1','2015-10-27 16:28:41',NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL,NULL,'2015-10-27 16:28:41',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`)
 VALUES('1','419','24206890946790451','2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-08-16 13:14:03');
 
 
;
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('8','419','陈伟杰',NULL,NULL,'100035','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('9','419','梁其师',NULL,NULL,'100001','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('10','419','彭海星',NULL,NULL,'100002','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('11','419','周荟荣',NULL,NULL,'100003','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('12','419','冯业猛',NULL,NULL,'100004','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('13','419','杨光红',NULL,NULL,'100005','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('14','419','杨娟',NULL,NULL,'100006','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('15','419','洪尉',NULL,NULL,'100007','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('16','419','王重纲',NULL,NULL,'100008','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('17','419','郭晓晶',NULL,NULL,'100009','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('18','419','林玉生',NULL,NULL,'100010','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('19','419','刘晓春',NULL,NULL,'100011','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('21','419','阳兰芬',NULL,NULL,'100012','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('22','419','吴满',NULL,NULL,'100013','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('23','419','张宇',NULL,NULL,'100014','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('24','419','韦宁',NULL,NULL,'100015','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('25','419','吴寒',NULL,NULL,'100016','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('26','419','杨承立',NULL,NULL,'100017','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('27','419','黄钰',NULL,NULL,'0','7','2','100020',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('28','419','韦晟敢',NULL,NULL,'100018','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('30','419','冯译萱',NULL,NULL,'100020','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('31','419','姚业',NULL,NULL,'100021','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('32','419','熊庆',NULL,NULL,'100022','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('33','419','庄家华',NULL,NULL,'100023','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('34','419','邱露权',NULL,NULL,'100024','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('35','419','何建升',NULL,NULL,'100025','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('36','419','李祥涛',NULL,NULL,'100026','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('37','419','王雯',NULL,NULL,'100027','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('38','419','戴云',NULL,NULL,'100028','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('39','419','林龙',NULL,NULL,'100029','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('40','419','陈慕葶',NULL,NULL,'100030','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('41','419','朱伟娟',NULL,NULL,'100031','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('43','419','丁浩',NULL,NULL,'100032','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('44','419','谢玲俐',NULL,NULL,'100033','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('45','419','耿莉',NULL,NULL,'100034','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('46','419','刘金文',NULL,NULL,'100036','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('47','419','龙莎莎',NULL,NULL,'100037','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('48','419','林园',NULL,NULL,'100038','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('49','419','刘铫',NULL,NULL,'100039','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('50','419','廖晓霞',NULL,NULL,'100040','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('51','419','何艺',NULL,NULL,'100041','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('52','419','宋少良',NULL,NULL,'100042','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('53','419','吕欣欣',NULL,NULL,'100043','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('54','419','熊颖',NULL,NULL,'100044','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('56','419','孙存贤',NULL,NULL,'100045','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('58','419','高磊',NULL,NULL,'100046','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('59','419','石雨佳',NULL,NULL,'100047','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('60','419','李未波',NULL,NULL,'100048','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('61','419','陆勇',NULL,NULL,'100049','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('62','419','邓荣君',NULL,NULL,'100050','7','2','2149',UTC_TIMESTAMP());
INSERT INTO `eh_enterprise_contacts` (`id`, `enterprise_id`, `name`, `nick_name`, `avatar`, `user_id`, `role`, `status`, `creator_uid`, `create_time`) VALUES('63','419','方丽娇',NULL,NULL,'100051','7','2','2149',UTC_TIMESTAMP());


INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('8',  '419',  '8',  '0',  '13530178494',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('9',  '419',  '9',  '0',  '13927485221',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('10',  '419',  '10',  '0',  '13603071871',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('11',  '419',  '11',  '0',  '13510152733',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('12',  '419',  '12',  '0',  '13911950186',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('13',  '419',  '13',  '0',  '13716300380',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('14',  '419',  '14',  '0',  '18026990762',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('15',  '419',  '15',  '0',  '13823282798',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('16',  '419',  '16',  '0',  '15827099968',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('17',  '419',  '17',  '0',  '18600158807',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('18',  '419',  '18',  '0',  '13621276823',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('19',  '419',  '19',  '0',  '13138184726',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('21',  '419',  '21',  '0',  '13510359905',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('22',  '419',  '22',  '0',  '13632734656',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('23',  '419',  '23',  '0',  '13043479040',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('24',  '419',  '24',  '0',  '18565607382',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('25',  '419',  '25',  '0',  '13544221542',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('26',  '419',  '26',  '0',  '15002095483',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('27',  '419',  '27',  '0',  '13501199649',  '100020',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('28',  '419',  '28',  '0',  '15889660710',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('30',  '419',  '30',  '0',  '13570893886',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('31',  '419',  '31',  '0',  '15099931812',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('32',  '419',  '32',  '0',  '15818167069',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('33',  '419',  '33',  '0',  '13824464512',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('34',  '419',  '34',  '0',  '13049860074',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('35',  '419',  '35',  '0',  '18589029400',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('36',  '419',  '36',  '0',  '13590318460',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('37',  '419',  '37',  '0',  '13701096239',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('38',  '419',  '38',  '0',  '18675535761',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('39',  '419',  '39',  '0',  '13163366563',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('40',  '419',  '40',  '0',  '13043452532',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('41',  '419',  '41',  '0',  '13266840559',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('43',  '419',  '43',  '0',  '13809028667',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('44',  '419',  '44',  '0',  '13760277314',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('45',  '419',  '45',  '0',  '13246648327',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('46',  '419',  '46',  '0',  '13760276565',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('47',  '419',  '47',  '0',  '13510703276',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('48',  '419',  '48',  '0',  '13380343639',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('49',  '419',  '49',  '0',  '18202853865',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('50',  '419',  '50',  '0',  '13510900991',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('51',  '419',  '51',  '0',  '18719041689',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('52',  '419',  '52',  '0',  '15889754085',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('53',  '419',  '53',  '0',  '15972019821',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('54',  '419',  '54',  '0',  '15910334737',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('56',  '419',  '56',  '0',  '13128939429',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('58',  '419',  '58',  '0',  '15112529349',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('59',  '419',  '59',  '0',  '18520825077',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('60',  '419',  '60',  '0',  '15016728035',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('61',  '419',  '61',  '0',  '18589092373',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('62',  '419',  '62',  '0',  '18211556127',  '2149',  UTC_TIMESTAMP() );
INSERT INTO `ehcore`.`eh_enterprise_contact_entries`  (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`) VALUES ('63',  '419',  '63',  '0',  '13751135711',  '2149',  UTC_TIMESTAMP() );


