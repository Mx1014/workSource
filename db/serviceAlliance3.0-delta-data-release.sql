-- 迁移yellowpage中的数据
INSERT INTO `eh_service_alliances` (`id`,`parent_id`,`owner_type`,`owner_id`,`name`,`display_name`,`type`,`address`,`contact`,`description`,`poster_uri`,`status`,`default_order`,`longitude`,`latitude`,`geohash`,`discount`,`category_id`,`contact_name`,`contact_mobile`,`service_type`,`service_url`,`discount_desc`,`creator_uid`,`create_time`)
SELECT `id`,`parent_id`,`owner_type`,`owner_id`,`name`,`nick_name`,`type`,`address`,`contact`,`description`,`poster_uri`,`status`,`default_order`,`longitude`,`latitude`,`geohash`,`integral_tag1`,`integral_tag2`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`creator_uid`,`create_time` FROM `eh_yellow_pages` WHERE `type` = 2;

-- 迁移yellowpage attachment中的数据
INSERT INTO `eh_service_alliance_attachments` (`id`,`owner_id`,`content_type`,`content_uri`,`creator_uid`,`create_time`)
SELECT `id`,`owner_id`,`content_type`,`content_uri`,`creator_uid`,`create_time` FROM `eh_yellow_page_attachments`;


INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('1', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '1000000');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('2', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '999990');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('3', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '999999');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('4', '0', '政府资源类型', '政府资源类型', '2', '1', UTC_TIMESTAMP(), '999999');
    
update eh_service_alliances set type = 1 where owner_id = 240111044331048623;
update eh_service_alliances set type = 2 where owner_id = 240111044331051500;
update eh_service_alliances set type = 3 where owner_id = 240111044331049963;