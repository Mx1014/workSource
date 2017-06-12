-- 给flowCase增加申请人在当前场景下的公司id字段   add by xq.tian  2017/06/08
ALTER TABLE `eh_flow_cases` ADD COLUMN `applier_organization_id` BIGINT COMMENT 'applier current organization_id';