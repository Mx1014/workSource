
ALTER TABLE `eh_portal_layout_templates` ADD COLUMN `type`  tinyint(4) NULL COMMENT '1-渐变导航栏（服务广场），2-自定义门户（二级门户），3-分页签门户（社群Tab）';


ALTER TABLE `eh_portal_layouts` ADD COLUMN `type`  tinyint(4) NULL COMMENT '1-渐变导航栏（服务广场），2-自定义门户（二级门户），3-分页签门户（社群Tab）';


ALTER TABLE `eh_portal_layouts` ADD COLUMN `index_flag`  tinyint(4) NULL DEFAULT NULL COMMENT 'index flag, 0-no, 1-yes';