ALTER TABLE `eh_rentalv2_items`
  CHANGE COLUMN `rental_resource_id` `source_id` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_rentalv2_items`
  ADD COLUMN `source_type` VARCHAR(45) NULL COMMENT 'DEFAULT: default_rule  RESOURCE: resource_rule' AFTER `id`;

UPDATE eh_rentalv2_items set source_type = 'default_rule' where source_type IS NULL;

ALTER TABLE `eh_rentalv2_config_attachments`
ADD COLUMN `default_order` INT NULL DEFAULT '0' AFTER `string_tag5`;

UPDATE eh_rentalv2_default_rules set rental_start_time = 7776000000 where rental_start_time = 0;
UPDATE eh_rentalv2_default_rules set rental_start_time_flag = 1;

update eh_rentalv2_resource_types set identify = 'conference' where name like '%会议室%';
update eh_rentalv2_resource_types set identify = 'screen' where name like '%电子屏%';
update eh_rentalv2_resource_types set identify = 'area' where name like '%场地%';
-- beta 和 现网执行
update eh_rentalv2_resource_types set identify = 'conference' where id in (10819,12030);
update eh_rentalv2_resource_types set identify = 'screen' where id in (11,12168);
update eh_rentalv2_resource_types set identify = 'area' where id in (10012,10062,10715,10716,10717,10814,12044,12078,12081,12082,12131,12175);