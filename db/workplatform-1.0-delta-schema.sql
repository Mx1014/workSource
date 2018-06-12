
-- 模块对应的菜单是否需要授权, add by yanjun 20180517
ALTER TABLE `eh_service_modules` ADD COLUMN `menu_auth_flag`  tinyint(4) NOT NULL DEFAULT 1 COMMENT 'if its menu need auth' ;


-- 增加索引 add by yanjun 01806121918
alter table eh_portal_layouts add index version_id_index(`version_id`);

alter table eh_portal_item_groups add index layout_id_index(`layout_id`);

alter table eh_portal_content_scopes add index content_id_index(`content_id`);

alter table eh_portal_launch_pad_mappings add index portal_content_id_index(`portal_content_id`);

alter table eh_portal_item_categories add index item_group_id_index(`item_group_id`);

alter table eh_portal_items add index item_group_id_index(`item_group_id`);

alter table eh_portal_items add index item_category_id_index(`item_category_id`);

alter table eh_service_module_apps add index version_id_index(`version_id`);

alter table eh_portal_item_groups add index version_id_index(`version_id`);

alter table eh_portal_item_categories add index version_id_index(`version_id`);

alter table eh_portal_items add index version_id_index(`version_id`);

alter table eh_portal_content_scopes add index version_id_index(`version_id`);

alter table eh_portal_launch_pad_mappings add index version_id_index(`version_id`);


