INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES(7,0,'0','设备类型','设备类型','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');

set @category_id = (SELECT MAX(id) FROM `eh_categories`);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),7,'0','消防','设备类型/消防','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),7,'0','强电','设备类型/强电','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),7,'0','弱电','设备类型/弱电','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),7,'0','其他','设备类型/其他','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10001', 'zh_CN', '设备不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10002', 'zh_CN', '设备没有设置经纬度');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10003', 'zh_CN', '不在设备附近');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10004', 'zh_CN', '设备标准不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10005', 'zh_CN', '设备标准已失效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10006', 'zh_CN', '设备经纬度不能修改');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10007', 'zh_CN', '设备状态后台不能设为维修中');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10008', 'zh_CN', '设备已失效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10009', 'zh_CN', '备品备件不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10010', 'zh_CN', '备品备件已失效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10011', 'zh_CN', '只有已失效的设备-标准关联关系可以删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10012', 'zh_CN', '只有待审核的设备-标准关联关系可以审核');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10013', 'zh_CN', '任务不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10014', 'zh_CN', '只有待执行和维修中的任务可以上报');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10015', 'zh_CN', '设备参数记录');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10016', 'zh_CN', '生成excel信息有问题');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10017', 'zh_CN', '下载excel信息有问题');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 1, 'zh_CN', '生成核查任务', '您有新的巡检任务，请及时处理，截止日期为：“${deadline}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 2, 'zh_CN', '指派任务给维修人', '您有新的维修任务，请及时处理，截止日期为：“${deadline}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 3, 'zh_CN', '指派任务记录信息', '“${reviewerName}”分配维修任务给“${operatorName}”，截止日期为：“${deadline}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 4, 'zh_CN', '设备-标准关联审阅不合格通知','设备“${equipmentName}”被审批为不合格，请及时选择新的标准');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 5, 'zh_CN', '设备-标准关联审阅合格通知','设备“${equipmentName}”已审批合格，可生成巡检/保养任务');