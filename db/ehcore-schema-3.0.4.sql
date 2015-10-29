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
	
SET foreign_key_checks = 1;
