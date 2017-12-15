-- 增加入孵申请类型  add by yanjun 20171215
ALTER TABLE `eh_incubator_applies` ADD COLUMN `apply_type`  tinyint(4) NOT NULL DEFAULT 0 ;

