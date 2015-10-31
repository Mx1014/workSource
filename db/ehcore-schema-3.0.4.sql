#
# Special notes about the schema design below (KY)
#
# Custom fileds
# 	To balance performance and flexibility, some tables carry general purpose integer fields and string fields,
# 	interpretation of these fields will be determined by the applications on top of, at database level, we only
# 	provide general indexing support for these fields, it is the responsibility of the application to map queries that
# 	are against to these fields.
#
# 	Initially, only two of integral-type and string-type fields are indexed, more indices can be added during operating
# 	time, tuning changes about the indexing will be sync-ed back into schema design afterwards
#
# namespaces and application modules
#	Reusable modules are abstracted under the concept of application. The platform provides built-in application modules
#	such as messaging application module, forum application module, etc. These built-in application modules are running 
#   in the context of core server. When a application module has external counterpart at third-party servers or remote client endpoints, 
#	the API it provides requires to go through the authentication system via appkey/secret key pair mechanism
#
#   Namespace is used to put related resources into distinct domains
#
# namespace and application design rules
#	Shared resources (usually system defined) that are common to all namespaces do not need namespace_id field
#	First level resources usually have namespace_id field
#	Secondary level resources do not need namespace_id field
#	objects that can carry information generated from multiple application modules usualy have app_id field
#	all profile items have app_id field, so that it allows other application modules to attach application specific
#	profile information into it
#
# name convention
#	index prefix: i_eh_
#	unique index prefix: u_eh_
#	foreign key constraint prefix: fk_eh_
# 	table prefix: eh_
#
# record deletion
# 	There are two deletion policies in regards to deletion
#		mark-deletion: mark it as deleted, wait for lazy cleanup or archive
#		remove-deletion: completely remove it from database
#
#   for the mark-deletion policy, the convention is to have a delete_time field which not only marks up the deletion
#	but also the deletion time
#
#

SET foreign_key_checks = 0;

ALTER TABLE eh_messages MODIFY `context_type` varchar(32);
ALTER TABLE eh_messages MODIFY `context_token` varchar(32);
ALTER TABLE eh_businesses MODIFY `visible_distance` double DEFAULT '5000';
ALTER TABLE eh_categories ADD COLUMN `description` TEXT;

ALTER TABLE eh_messages CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_servers CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_shards CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_server_shard_map CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_content_shard_map CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_configurations CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_message_boxs CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_acls CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_acl_privileges CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_acl_roles CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_acl_role_assignments CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_apps CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_app_profiles CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_sequences CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_namespaces CONVERT TO CHARACTER SET utf8mb4;


-- 湛江市
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(13926, '赤坎区', 'CHIKANQU', 'CKQ', '/广东/湛江市/赤坎区', 3, 3, '0759', 2);
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(13926, '霞山区', 'XIASHANQU', 'XSQ', '/广东/湛江市/霞山区', 3, 3, '0759', 2);
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(13926, '坡头区', 'POTOUQU', 'PTQ', '/广东/湛江市/坡头区', 3, 3, '0759', 2);
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(13926, '麻章区', 'MAZHANGQU', 'MZQ', '/广东/湛江市/麻章区', 3, 3, '0759', 2);
-- 温州市
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(12136, '鹿城区', 'LUCHENGQU', 'LCQ', '/浙江/温州市/鹿城区', 3, 3, '0577', 2);	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(12136, '龙湾区', 'LONGWANQU', 'LWQ', '/浙江/温州市/龙湾区', 3, 3, '0577', 2);	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(12136, '瓯海区', 'OUHAIQU', 'OHQ', '/浙江/温州市/瓯海区', 3, 3, '0577', 2);
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(12136, '洞头区', 'DONGTOUQU', 'DTQ', '/浙江/温州市/洞头区', 3, 3, '0577', 2);
-- 淄博市
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(11578, '张店区', 'ZHANGDIANQU', 'ZDQ', '/山东/淄博市/张店区', 3, 3, '0533', 2);	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(11578, '淄川区', 'ZHICHUANQU', 'ZCQ', '/山东/淄博市/淄川区', 3, 3, '0533', 2);	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(11578, '博山区', 'BOSHANQU', 'BSQ', '/山东/淄博市/博山区', 3, 3, '0533', 2);	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(11578, '周村区', 'ZHOUCUNQU', 'ZCQ', '/山东/淄博市/周村区', 3, 3, '0533', 2);	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(11578, '临淄区', 'LINZIQU', 'LZQ', '/山东/淄博市/临淄区', 3, 3, '0533', 2);	
-- 珠海市
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(13916, '香洲区', 'XIANGZHOUQU', 'XZQ', '/广东/珠海市/香洲区', 3, 3, '0756', 2);	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(13916, '斗门区', 'DOUMENQU', 'DMQ', '/广东/珠海市/斗门区', 3, 3, '0756', 2);
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(13916, '金湾区', 'JINWANQU', 'JWQ', '/广东/珠海市/金湾区', 3, 3, '0756', 2);
-- 扬州市	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(11984, '邗江区', 'HANJIANGQU', 'HJQ', '/江苏/扬州市/邗江区', 3, 3, '0514', 2);	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(11984, '广陵区', 'GUANGLINGQU', 'GLQ', '/江苏/扬州市/广陵区', 3, 3, '0514', 2);	
INSERT INTO `eh_regions`(`parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `tel_code`, `status`)
	VALUES(11984, '江都区', 'JIANGDUQU', 'JDQ', '/江苏/扬州市/江都区', 3, 3, '0514', 2);	

INSERT INTO `eh_configurations` (name,value,description) VALUES ('admin.import.address.allow.max.count', '50', '后台导入公寓时允许已存在的用户自己添加公寓的最大数目');
INSERT INTO `eh_configurations` (name,value,description) VALUES ('admin.import.data.seperator', '#@', '后台导入小区文件的分隔符');	


-- 左邻优选活动圈：1150
-- 每天学点生活小知识：1090
-- 透过镜头看世界：900
-- 化妆：206
UPDATE `eh_groups` SET `member_count`=1150  WHERE `id`=173022;
UPDATE `eh_groups` SET `member_count`=1090  WHERE `id`=173102;
UPDATE `eh_groups` SET `member_count`=900  WHERE `id`=173132;
UPDATE `eh_groups` SET `member_count`=206  WHERE `id`=173129;

-- 帖子已经删除、但活动状态仍然为active，需要把这些活动状态刷为inactive
update eh_activities set status=0 where id=1;;
update eh_activities set status=0 where id=10;
update eh_activities set status=0 where id=18;
update eh_activities set status=0 where id=20;
update eh_activities set status=0 where id=21;
update eh_activities set status=0 where id=22;
update eh_activities set status=0 where id=37;
update eh_activities set status=0 where id=51;
update eh_activities set status=0 where id=52;
update eh_activities set status=0 where id=53;
update eh_activities set status=0 where id=54;
update eh_activities set status=0 where id=55;
update eh_activities set status=0 where id=76;
update eh_activities set status=0 where id=78;
update eh_activities set status=0 where id=80;
update eh_activities set status=0 where id=81;
update eh_activities set status=0 where id=82;
update eh_activities set status=0 where id=84;
update eh_activities set status=0 where id=86;
update eh_activities set status=0 where id=87;
update eh_activities set status=0 where id=88;
update eh_activities set status=0 where id=89;
update eh_activities set status=0 where id=90;
update eh_activities set status=0 where id=92;
update eh_activities set status=0 where id=93;
update eh_activities set status=0 where id=94;
update eh_activities set status=0 where id=95;
update eh_activities set status=0 where id=98;
update eh_activities set status=0 where id=99;
update eh_activities set status=0 where id=101;
update eh_activities set status=0 where id=102;
update eh_activities set status=0 where id=103;
update eh_activities set status=0 where id=105;
update eh_activities set status=0 where id=106;
update eh_activities set status=0 where id=109;
update eh_activities set status=0 where id=110;
update eh_activities set status=0 where id=112;
update eh_activities set status=0 where id=113;
update eh_activities set status=0 where id=114;
update eh_activities set status=0 where id=115;
update eh_activities set status=0 where id=116;
update eh_activities set status=0 where id=118;
update eh_activities set status=0 where id=119;
update eh_activities set status=0 where id=121;
update eh_activities set status=0 where id=125;
update eh_activities set status=0 where id=126;
update eh_activities set status=0 where id=127;
update eh_activities set status=0 where id=132;
update eh_activities set status=0 where id=133;
update eh_activities set status=0 where id=136;
update eh_activities set status=0 where id=137;
update eh_activities set status=0 where id=144;
update eh_activities set status=0 where id=146;
update eh_activities set status=0 where id=149;
update eh_activities set status=0 where id=158;
update eh_activities set status=0 where id=159;
update eh_activities set status=0 where id=162;
update eh_activities set status=0 where id=163;
update eh_activities set status=0 where id=175;
update eh_activities set status=0 where id=191;
update eh_activities set status=0 where id=193;
update eh_activities set status=0 where id=200;
update eh_activities set status=0 where id=201;
update eh_activities set status=0 where id=202;
update eh_activities set status=0 where id=204;
update eh_activities set status=0 where id=206;
update eh_activities set status=0 where id=207;
update eh_activities set status=0 where id=211;
update eh_activities set status=0 where id=212;
update eh_activities set status=0 where id=216;
update eh_activities set status=0 where id=230;
update eh_activities set status=0 where id=238;
update eh_activities set status=0 where id=242;
update eh_activities set status=0 where id=243;
update eh_activities set status=0 where id=256;
update eh_activities set status=0 where id=263;
update eh_activities set status=0 where id=265;
update eh_activities set status=0 where id=267;
update eh_activities set status=0 where id=271;
update eh_activities set status=0 where id=272;
update eh_activities set status=0 where id=273;
update eh_activities set status=0 where id=276;
update eh_activities set status=0 where id=277;
update eh_activities set status=0 where id=281;
update eh_activities set status=0 where id=285;
update eh_activities set status=0 where id=286;
update eh_activities set status=0 where id=290;
update eh_activities set status=0 where id=291;
update eh_activities set status=0 where id=295;
update eh_activities set status=0 where id=296;
update eh_activities set status=0 where id=300;
update eh_activities set status=0 where id=302;
update eh_activities set status=0 where id=303;
update eh_activities set status=0 where id=308;
update eh_activities set status=0 where id=310;
update eh_activities set status=0 where id=318;
update eh_activities set status=0 where id=323;
update eh_activities set status=0 where id=324;
update eh_activities set status=0 where id=325;
update eh_activities set status=0 where id=326;
update eh_activities set status=0 where id=337;
update eh_activities set status=0 where id=342;
update eh_activities set status=0 where id=344;
update eh_activities set status=0 where id=350;
update eh_activities set status=0 where id=355;
update eh_activities set status=0 where id=356;
update eh_activities set status=0 where id=358;
update eh_activities set status=0 where id=364;
update eh_activities set status=0 where id=368;
update eh_activities set status=0 where id=373;
update eh_activities set status=0 where id=374;
update eh_activities set status=0 where id=377;
update eh_activities set status=0 where id=381;
update eh_activities set status=0 where id=386;
update eh_activities set status=0 where id=388;
update eh_activities set status=0 where id=405;
update eh_activities set status=0 where id=416;
update eh_activities set status=0 where id=417;
update eh_activities set status=0 where id=418;
update eh_activities set status=0 where id=419;
update eh_activities set status=0 where id=420;
update eh_activities set status=0 where id=423;
update eh_activities set status=0 where id=428;
update eh_activities set status=0 where id=431;
update eh_activities set status=0 where id=432;
update eh_activities set status=0 where id=439;
update eh_activities set status=0 where id=442;
update eh_activities set status=0 where id=448;
update eh_activities set status=0 where id=449;
update eh_activities set status=0 where id=453;
update eh_activities set status=0 where id=456;
update eh_activities set status=0 where id=457;
update eh_activities set status=0 where id=458;
update eh_activities set status=0 where id=465;
update eh_activities set status=0 where id=469;
update eh_activities set status=0 where id=471;
update eh_activities set status=0 where id=472;
update eh_activities set status=0 where id=476;
update eh_activities set status=0 where id=484;
update eh_activities set status=0 where id=485;
update eh_activities set status=0 where id=486;
update eh_activities set status=0 where id=487;
update eh_activities set status=0 where id=488;
update eh_activities set status=0 where id=489;
update eh_activities set status=0 where id=491;
update eh_activities set status=0 where id=492;
update eh_activities set status=0 where id=493;
update eh_activities set status=0 where id=494;
update eh_activities set status=0 where id=495;
update eh_activities set status=0 where id=496;
update eh_activities set status=0 where id=497;
update eh_activities set status=0 where id=498;
update eh_activities set status=0 where id=499;
update eh_activities set status=0 where id=500;
update eh_activities set status=0 where id=505;
update eh_activities set status=0 where id=508;
update eh_activities set status=0 where id=511;
update eh_activities set status=0 where id=512;
update eh_activities set status=0 where id=515;
update eh_activities set status=0 where id=516;
update eh_activities set status=0 where id=517;
update eh_activities set status=0 where id=519;
update eh_activities set status=0 where id=523;
update eh_activities set status=0 where id=525;
update eh_activities set status=0 where id=528;
update eh_activities set status=0 where id=529;
update eh_activities set status=0 where id=533;
update eh_activities set status=0 where id=534;
update eh_activities set status=0 where id=535;
update eh_activities set status=0 where id=538;
update eh_activities set status=0 where id=539;
update eh_activities set status=0 where id=540;
update eh_activities set status=0 where id=541;
update eh_activities set status=0 where id=546;
update eh_activities set status=0 where id=548;
update eh_activities set status=0 where id=554;
update eh_activities set status=0 where id=557;
update eh_activities set status=0 where id=558;
update eh_activities set status=0 where id=560;
update eh_activities set status=0 where id=561;
update eh_activities set status=0 where id=562;
update eh_activities set status=0 where id=563;
update eh_activities set status=0 where id=564;
update eh_activities set status=0 where id=565;
update eh_activities set status=0 where id=566;
update eh_activities set status=0 where id=568;
update eh_activities set status=0 where id=569;
update eh_activities set status=0 where id=571;
update eh_activities set status=0 where id=572;
update eh_activities set status=0 where id=573;
update eh_activities set status=0 where id=574;
update eh_activities set status=0 where id=575;
update eh_activities set status=0 where id=576;
update eh_activities set status=0 where id=579;
update eh_activities set status=0 where id=580;
update eh_activities set status=0 where id=585;
update eh_activities set status=0 where id=586;
update eh_activities set status=0 where id=587;
update eh_activities set status=0 where id=588;
update eh_activities set status=0 where id=589;
update eh_activities set status=0 where id=590;
update eh_activities set status=0 where id=592;
update eh_activities set status=0 where id=593;
update eh_activities set status=0 where id=594;
update eh_activities set status=0 where id=604;
update eh_activities set status=0 where id=605;
update eh_activities set status=0 where id=606;
update eh_activities set status=0 where id=607;
update eh_activities set status=0 where id=609;
update eh_activities set status=0 where id=611;
update eh_activities set status=0 where id=615;
update eh_activities set status=0 where id=620;
update eh_activities set status=0 where id=622;
update eh_activities set status=0 where id=623;
update eh_activities set status=0 where id=624;
update eh_activities set status=0 where id=625;
update eh_activities set status=0 where id=626;
update eh_activities set status=0 where id=627;
update eh_activities set status=0 where id=630;
update eh_activities set status=0 where id=631;
update eh_activities set status=0 where id=634;
update eh_activities set status=0 where id=635;
update eh_activities set status=0 where id=637;
update eh_activities set status=0 where id=639;
update eh_activities set status=0 where id=640;
update eh_activities set status=0 where id=644;
update eh_activities set status=0 where id=656;
update eh_activities set status=0 where id=657;
update eh_activities set status=0 where id=660;
update eh_activities set status=0 where id=661;
update eh_activities set status=0 where id=663;
update eh_activities set status=0 where id=665;
update eh_activities set status=0 where id=666;
update eh_activities set status=0 where id=670;
update eh_activities set status=0 where id=672;
update eh_activities set status=0 where id=676;
update eh_activities set status=0 where id=681;
update eh_activities set status=0 where id=686;
update eh_activities set status=0 where id=690;
update eh_activities set status=0 where id=691;
update eh_activities set status=0 where id=692;
update eh_activities set status=0 where id=693;
update eh_activities set status=0 where id=694;
update eh_activities set status=0 where id=695;
update eh_activities set status=0 where id=702;
update eh_activities set status=0 where id=705;
update eh_activities set status=0 where id=707;
update eh_activities set status=0 where id=708;
update eh_activities set status=0 where id=709;
update eh_activities set status=0 where id=710;
update eh_activities set status=0 where id=711;
update eh_activities set status=0 where id=714;
update eh_activities set status=0 where id=716;
update eh_activities set status=0 where id=722;
update eh_activities set status=0 where id=723;
update eh_activities set status=0 where id=724;
update eh_activities set status=0 where id=727;
update eh_activities set status=0 where id=728;
update eh_activities set status=0 where id=730;
update eh_activities set status=0 where id=731;
update eh_activities set status=0 where id=732;
update eh_activities set status=0 where id=733;
update eh_activities set status=0 where id=737;
update eh_activities set status=0 where id=738;
update eh_activities set status=0 where id=739;
update eh_activities set status=0 where id=740;
update eh_activities set status=0 where id=743;
update eh_activities set status=0 where id=753;
update eh_activities set status=0 where id=754;
update eh_activities set status=0 where id=756;
update eh_activities set status=0 where id=761;
update eh_activities set status=0 where id=768;
update eh_activities set status=0 where id=769;
update eh_activities set status=0 where id=770;
update eh_activities set status=0 where id=771;
update eh_activities set status=0 where id=772;
update eh_activities set status=0 where id=775;
update eh_activities set status=0 where id=776;
update eh_activities set status=0 where id=777;
update eh_activities set status=0 where id=778;
update eh_activities set status=0 where id=779;
update eh_activities set status=0 where id=780;
update eh_activities set status=0 where id=781;
update eh_activities set status=0 where id=796;
update eh_activities set status=0 where id=803;
update eh_activities set status=0 where id=804;
update eh_activities set status=0 where id=817;
update eh_activities set status=0 where id=818;
update eh_activities set status=0 where id=822;
update eh_activities set status=0 where id=824;
update eh_activities set status=0 where id=825;
update eh_activities set status=0 where id=831;
update eh_activities set status=0 where id=832;
update eh_activities set status=0 where id=833;
update eh_activities set status=0 where id=835;
update eh_activities set status=0 where id=837;
update eh_activities set status=0 where id=838;
update eh_activities set status=0 where id=840;
update eh_activities set status=0 where id=841;
update eh_activities set status=0 where id=842;
update eh_activities set status=0 where id=843;
update eh_activities set status=0 where id=847;
update eh_activities set status=0 where id=848;
update eh_activities set status=0 where id=867;
update eh_activities set status=0 where id=868;
update eh_activities set status=0 where id=869;
update eh_activities set status=0 where id=870;
update eh_activities set status=0 where id=871;
update eh_activities set status=0 where id=872;
update eh_activities set status=0 where id=873;
update eh_activities set status=0 where id=875;
update eh_activities set status=0 where id=876;
update eh_activities set status=0 where id=877;
update eh_activities set status=0 where id=878;
update eh_activities set status=0 where id=880;
update eh_activities set status=0 where id=885;
update eh_activities set status=0 where id=886;
update eh_activities set status=0 where id=889;
update eh_activities set status=0 where id=899;
update eh_activities set status=0 where id=904;
update eh_activities set status=0 where id=924;
update eh_activities set status=0 where id=957;
update eh_activities set status=0 where id=958;
update eh_activities set status=0 where id=961;
update eh_activities set status=0 where id=964;
update eh_activities set status=0 where id=979;
update eh_activities set status=0 where id=986;
update eh_activities set status=0 where id=988;
update eh_activities set status=0 where id=989;
update eh_activities set status=0 where id=990;
update eh_activities set status=0 where id=991;
update eh_activities set status=0 where id=992;
update eh_activities set status=0 where id=994;
update eh_activities set status=0 where id=998;
update eh_activities set status=0 where id=1000;
update eh_activities set status=0 where id=1004;
update eh_activities set status=0 where id=1005;
update eh_activities set status=0 where id=1006;
update eh_activities set status=0 where id=1007;
update eh_activities set status=0 where id=1008;
update eh_activities set status=0 where id=1009;
update eh_activities set status=0 where id=1010;
update eh_activities set status=0 where id=1011;
update eh_activities set status=0 where id=1013;
	
SET foreign_key_checks = 1;
