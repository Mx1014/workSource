 -- 更改索引
ALTER TABLE `eh_uniongroup_member_details`  DROP INDEX `uniongroup_member_uniqueIndex`;
ALTER TABLE eh_uniongroup_member_details ADD UNIQUE INDEX `uniongroup_member_uniqueIndex` (`group_type`, `group_id`, `detail_id`, `contact_token`,`version_code`) ;