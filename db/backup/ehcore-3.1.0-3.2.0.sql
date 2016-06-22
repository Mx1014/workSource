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
ALTER TABLE `eh_rental_site_rules`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_site_rules` ADD COLUMN  `time_step` double ;
ALTER TABLE `eh_rental_site_rules`  CHANGE  `rentalStep` `rental_step` int(11) DEFAULT 1 COMMENT 'how many time_step must be rental every time ';


ALTER TABLE `eh_rental_bills` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_bills`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_bill_paybill_map` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_bill_paybill_map`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_bill_attachments` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_bill_attachments`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';


ALTER TABLE `eh_rental_bills` CHANGE `paid_money` `paid_money` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` CHANGE `pay_total_money` `pay_total_money` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` CHANGE `site_total_money` `site_total_money` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` CHANGE `reserve_money` `reserve_money` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_items_bills` CHANGE `total_money` `total_money` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_site_items` CHANGE `price` `price` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_site_rules` CHANGE `price` `price` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_sites_bills` CHANGE `total_money` `total_money` DECIMAL(10,2) DEFAULT NULL; 


INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 20, 'zh_CN', '地址加速审核，通知操作者', '您已提交加速审核${address}，我们会尽快为您处理的。');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 28, 'zh_CN', '加入公众圈', '您已订阅兴趣圈 “${groupName}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 29, 'zh_CN', '加入公众圈,管理员', '兴趣圈“${groupName}”人数有变化');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 30, 'zh_CN', '管理员收到删除消息', '${userName}已删除圈“${groupName}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 31, 'zh_CN', '创建者删除', '您已删除圈“${groupName}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 32, 'zh_CN', '其它成员收到删除消息', '您已取消订阅兴趣圈“${groupName}”');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'forum.notification', 1, 'zh_CN', '有人回复话题/活动/投票，通知话题/活动/投票发起者', '${userName}回复了‘${postName}’');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'activity.notification', 1, 'zh_CN', '有人报名了活动，通知活动发起者', '${userName}报名参加了活动“${postName}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'activity.notification', 2, 'zh_CN', '有人取消了活动报名，通知活动发起者', '${userName}取消了活动“${postName}”报名');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'activity.notification', 3, 'zh_CN', '活动被管理员同意，通知活动报名者', '您报名的活动“${postName}”已经通过管理员同意');


INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'rental', '10006', 'zh_CN', '已经过了预定取消时间');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'rental', '10007', 'zh_CN', '您还未付款！');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'rental', '10008', 'zh_CN', '不是已预订订单');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'rental', '10009', 'zh_CN', '不是已完成订单'); 
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'rental', '10010', 'zh_CN', '您在这个时间段已经有别的预约了哟');



INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 33, 'zh_CN', '兴趣圈分享', '兴趣圈“${groupName}”推荐');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'sms.vcodetest.flag','true','是否可以发送测试验证码短信');

ALTER TABLE `eh_regions` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_nearby_community_map` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_communities` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_buildings` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_addresses` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';