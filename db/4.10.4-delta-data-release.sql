-- version 1 的手动复制出来
SET @conf_id := (SELECT MAX(id) FROM eh_uniongroup_configures);
SET @member_id := (SELECT MAX(id) FROM eh_uniongroup_configures);
;
INSERT INTO eh_uniongroup_configures 

SELECT
  @conf_id + id id,
  `namespace_id`,
  `enterprise_id`,
  `group_type`,
  `group_id`,
  `current_id`,
  `current_type`,
  `current_name`,
  `operator_uid`,
  `update_time`,
  1 version_code
FROM `eh_uniongroup_configures`
 ;
 INSERT INTO eh_uniongroup_member_details 
 SELECT
  @member_id  + `id` id,
  `namespace_id`,
  `group_type`,
  `group_id`,
  `detail_id`,
  `target_type`,
  `target_id`,
  `enterprise_id`,
  `contact_name`,
  `contact_token`,
  `update_time`,
  `operator_uid`,
  1 version_code
FROM `aa`.`eh_uniongroup_member_details`;