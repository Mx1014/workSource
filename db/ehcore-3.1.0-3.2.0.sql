# belong eh_users partition

DROP TABLE IF EXISTS `eh_user_group_histories`;
CREATE TABLE `eh_user_group_histories` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `group_discriminator` VARCHAR(32) COMMENT 'redendant info for quickly distinguishing associated group', 
    `group_id` BIGINT,
    `community_id` BIGINT,
    `address_id` BIGINT,
    `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



##alter tables 

ALTER TABLE  `eh_rental_rules`  ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE  `eh_rental_rules`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE  `eh_rental_rules`   ADD COLUMN `rental_type` TINYINT(4) DEFAULT NULL COMMENT '0: as hour:min  1-as half day 2-as day';
ALTER TABLE  `eh_rental_rules`   ADD COLUMN `cancel_time` BIGINT(20) DEFAULT NULL;
ALTER TABLE  `eh_rental_rules`   ADD COLUMN `overtime_time` BIGINT(20) DEFAULT NULL;


ALTER TABLE `eh_rental_sites` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_sites`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_sites` ADD COLUMN `introduction` TEXT;
ALTER TABLE `eh_rental_sites` ADD COLUMN `notice` TEXT;

ALTER TABLE `eh_rental_sites_bills` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_sites_bills`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_site_rules` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_si`ehcore`te_rules`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_bills` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_bills`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_bill_paybill_map` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_bill_paybill_map`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_bill_attachments` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_bill_attachments`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 28, 'zh_CN', '加入公众圈', '您已订阅兴趣圈 “${groupName}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 29, 'zh_CN', '加入公众圈,管理员', '兴趣圈“${groupName}”人数有变化');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 30, 'zh_CN', '管理员收到删除消息', '${userName}已删除圈“${groupName}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 31, 'zh_CN', '创建者删除', '您已删除圈“${groupName}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 32, 'zh_CN', '其它成员收到删除消息', '您已取消订阅兴趣圈“${groupName}”');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'forum.notification', 1, 'zh_CN', '有人回复话题/活动/投票，通知话题/活动/投票发起者', '${userName}回复了‘${postName}’');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'activity.notification', 1, 'zh_CN', '有人报名了活动，通知活动发起者', '${userName}报名参加了活动“${postName}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'activity.notification', 2, 'zh_CN', '有人取消了活动报名，通知活动发起者', '${userName}取消了活动“${postName}”报名');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'activity.notification', 3, 'zh_CN', '活动被管理员同意，通知活动报名者', '您报名的活动“${postName}”已经通过管理员同意');
