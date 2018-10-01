

-- AUTHOR: 严军
-- REMARK: 组件表增加标题栏信息  20181001
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_flag`  tinyint(4) NULL COMMENT '0-none,1-left,2-center，reference  TitleFlag.java';
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title`  varchar(255) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_uri`  varchar(1024) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_style`  int(11) NULL COMMENT 'title style, reference TitleStyle.java' ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `sub_title`  varchar(255) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_size`  tinyint(4) NULL COMMENT '0-small, 1-medium, 2-large    TitleSize.java' ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_more_flag`  tinyint(4) NULL COMMENT '0-no, 1-yes. reference trueOrFalseFlag.java' ;

