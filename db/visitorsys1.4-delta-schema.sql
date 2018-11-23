-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 到访类型修改
ALTER TABLE `eh_visitor_sys_visit_reason` ADD COLUMN `reason_type`  tinyint NULL DEFAULT null COMMENT '类型 0为住宅小区,1为商业小区';
ALTER TABLE `eh_visitor_sys_visit_reason` ADD COLUMN `visit_reason_code`  tinyint NULL DEFAULT null COMMENT '到访原因类型码';