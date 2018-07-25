-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: huangpengyu  20180709
-- REMARK: 此功能将所有不需要走审批的表单字段关联起来
create table `eh_general_form_val_requests`
(
   `id`                   bigint not null,
   `organization_id`      bigint comment 'organization_id',
   `owner_id`             bigint comment 'owner_id',
   `owner_type`           varchar(64) comment 'owner_type',
   `namespace_id`         int comment 'namespace_id',
   `module_id`            bigint comment 'module_id',
   `module_type`          varchar(64) comment 'module_type',
   `source_id`            bigint comment 'source_id',
   `source_type`          varchar(64) comment 'source_type',
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_general_form_val_requests in dev mode';


create table `eh_general_form_filter_user_map`
(
   `id`                   bigint not null,
   `owner_id`             bigint comment 'owner_id' not null,
   `owner_type`           varchar(64) comment 'owner_type',
   `namespace_id`         int comment 'namespace_id' not null,
   `module_id`            bigint comment 'module_id' not null,
   `module_type`          varchar(64) comment 'module_type' ,
	 `form_origin_id`				bigint comment '关联的表id' not null,
	 `form_version`					bigint comment '关联的表version' not null,
   `field_name`           varchar(64) comment '被选中的字段名' not null,
	 `user_uuid`						varchar(128) comment '当前登录的用户用于获取字段' not null,
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_general_form_filter_user_map in dev mode';

-- --------------------- SECTION END ---------------------------------------------------------