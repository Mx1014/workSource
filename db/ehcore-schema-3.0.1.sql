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

UPDATE eh_communities SET apt_count=0 WHERE apt_count IS NULL;
ALTER TABLE eh_communities MODIFY `apt_count` INT NOT NULL DEFAULT 0;

DROP TABLE IF EXISTS `eh_push_messages`;
CREATE TABLE `eh_push_messages` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `message_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'NORMAL_MESSAGE, UPGRADE_MESSAGE, NOTIFY_MESSAGE',
    `title` VARCHAR(128) COMMENT 'title of message',
    `content` VARCHAR(4096) COMMENT 'content for message',
    `target_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'CITY, COMMUNITY, FAMILY, USER',
    `target_id` BIGINT NOT NULL DEFAULT 0,
    `status` INT NOT NULL DEFAULT 0 COMMENT 'WAITING, RUNNING, FINISHED',
    `create_time` DATETIME DEFAULT NULL,
    `start_time` DATETIME DEFAULT NULL,
    `finish_time` DATETIME DEFAULT NULL,
    `device_type` VARCHAR(64),
    `device_tag` VARCHAR(64),
    `app_version` VARCHAR(64),
    `push_count` BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_push_message_results`;
CREATE TABLE `eh_push_message_results` (
    `id` BIGINT NOT NULL COMMENT 'id of the push message result, not auto increment',
    `message_id` BIGINT NOT NULL DEFAULT 0,
    `user_id` BIGINT NOT NULL DEFAULT 0,
    `identifier_token` VARCHAR(128) COMMENT 'The mobile phone of user',
    `send_time` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('user.avatar.male.url', 'cs://1/image/aW1hZ2UvTVRvME1qVTBZalpqT1dGa05USm1aVEE1WVRnMU9EWmhOVE0zTm1Nd1pXSTVZUQ','默认男头像url'); 
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('user.avatar.female.url', 'cs://1/image/aW1hZ2UvTVRvMU1EQTVZVEZrTkdVek9EQXhZbVE0WlRZd1l6UXdOVE0zWVdJNFkyTmlNUQ','默认女头像url'); 
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('user.def.pwd', '123456','用户默认密码');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('hotusers', '', 'phone numbers split by ,')

UPDATE `eh_groups` SET member_count=1105 WHERE ID=173130; -- 旅行
UPDATE `eh_groups` SET member_count=1089 WHERE ID=173132; -- 透过镜头看世界
UPDATE `eh_groups` SET member_count=956 WHERE ID=173131; -- 吃货
UPDATE `eh_groups` SET member_count=923 WHERE ID=173127; -- 音乐
UPDATE `eh_groups` SET member_count=907 WHERE ID=173129; -- 化妆
UPDATE `eh_groups` SET member_count=884 WHERE ID=172789; -- 每天一句话

SET foreign_key_checks = 1;
