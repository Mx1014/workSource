INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES(7,0,'0','设备类型','设备类型','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');

set @category_id = (SELECT MAX(id) FROM `eh_categories`);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),0,'0','消防','设备类型/消防','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),0,'0','强电','设备类型/强电','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),0,'0','弱电','设备类型/弱电','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),0,'0','其他','设备类型/其他','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');